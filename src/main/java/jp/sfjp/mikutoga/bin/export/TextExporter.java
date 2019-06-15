/*
 * text data exporter
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sfjp.mikutoga.bin.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

/**
 * 任意のエンコーダによるテキストデータのバイナリ出力を行う。
 */
public class TextExporter {

    /** デフォルトの入力バッファサイズ(単位:char)。 */
    public static final int DEFBUFSZ_CHAR = 128;
    /** デフォルトの出力バッファサイズ(単位:byte)。 */
    public static final int DEFBUFSZ_BYTE = 128;

    private static final String DUMMYTXT = "";


    private final CharsetEncoder encoder;
    private CharBuffer cbuf = CharBuffer.allocate(DEFBUFSZ_CHAR);
    private byte[] barray = new byte[DEFBUFSZ_BYTE];
    private ByteBuffer bbuf = ByteBuffer.wrap(this.barray);

    private CharSequence textData = DUMMYTXT;
    private int textLength;
    private int inPos;


    /**
     * コンストラクタ。
     * @param encoder エンコーダ
     * @throws NullPointerException 引数がnull
     */
    public TextExporter(CharsetEncoder encoder) throws NullPointerException{
        super();

        if(encoder == null) throw new NullPointerException();
        this.encoder = encoder;
        this.encoder.reset();
        this.encoder.onMalformedInput(CodingErrorAction.REPORT);
        this.encoder.onUnmappableCharacter(CodingErrorAction.REPORT);

        this.cbuf.clear();
        this.bbuf.clear();

        return;
    }

    /**
     * コンストラクタ。
     * @param cs 文字セット
     */
    public TextExporter(Charset cs){
        this(cs.newEncoder());
        return;
    }

    /**
     * エンコーダを返す。
     * @return エンコーダ
     */
    public CharsetEncoder getEncoder(){
        return this.encoder;
    }

    /**
     * 入力内部バッファサイズを設定する。
     * @param newSize バッファサイズ。(単位:char)
     * @throws IllegalArgumentException サイズ指定が正で無かった。
     */
    public void setCharBufSize(int newSize)
            throws IllegalArgumentException {
        if(newSize <= 0) throw new IllegalArgumentException();

        this.cbuf = CharBuffer.allocate(newSize);
        this.cbuf.clear();

        return;
    }

    /**
     * 出力内部バッファサイズを設定する。
     * 最低限必要な出力バッファサイズはエンコード設定により異なる。
     * @param newSize バッファサイズ。(単位:byte)
     * @throws IllegalArgumentException サイズ指定が小さすぎる。
     */
    public void setByteBufSize(int newSize)
            throws IllegalArgumentException {
        float ratio = this.encoder.maxBytesPerChar();
        int minSz = (int)( StrictMath.floor(ratio) );
        if(newSize < minSz) throw new IllegalArgumentException();

        this.barray = new byte[newSize];
        this.bbuf = ByteBuffer.wrap(this.barray);
        this.bbuf.clear();

        return;
    }

    /**
     * 与えられた文字列をエンコードしてストリームに出力する。
     * @param text 文字列
     * @param os 出力ストリーム
     * @return 出力バイト長
     * @throws IOException 出力エラー
     * @throws CharacterCodingException エンコードエラー
     */
    public int dumpText(CharSequence text, OutputStream os)
            throws IOException, CharacterCodingException {
        this.textData = text;

        int total = 0;
        try{
            total = dumpTextImpl(os);
        }finally{
            this.textData = DUMMYTXT;
        }

        return total;
    }

    /**
     * 文字列をエンコードしてストリームに出力する。
     * @param os 出力ストリーム
     * @return 出力バイト長
     * @throws IOException 出力エラー
     * @throws CharacterCodingException エンコードエラー
     */
    private int dumpTextImpl(OutputStream os)
            throws IOException, CharacterCodingException {
        reset();

        int total = 0;

        for(;;){
            loadCharBuffer();
            CoderResult result = encode();
            if(result.isUnderflow()){
                this.cbuf.clear();
                if( ! hasMoreInput() ){
                    total += sweepByteBuffer(os);
                    break;
                }
            }else if(result.isOverflow()){
                total += sweepByteBuffer(os);
                this.cbuf.compact();
            }else if(result.isError()){
                result.throwException();
            }
        }

        total += flush(os);

        return total;
    }

    /**
     * 各種内部状態をリセットする。
     */
    private void reset(){
        this.cbuf.clear();
        this.bbuf.clear();

        this.encoder.reset();

        this.textLength = this.textData.length();
        this.inPos = 0;

        return;
    }

    /**
     * 入力バッファにまだ入力していない文字があるか判定する。
     * @return 入力バッファにまだ入力していない文字があればtrue
     */
    private boolean hasMoreInput(){
        if(this.inPos < this.textLength) return true;
        return false;
    }

    /**
     * 入力バッファに文字を埋める。
     *
     * <p>入力バッファが一杯になるか
     * 入力文字列がなくなるまでバッファが埋められる。
     */
    private void loadCharBuffer(){
        while(this.cbuf.hasRemaining() && hasMoreInput()){
            char ch = this.textData.charAt(this.inPos++);
            this.cbuf.put(ch);
        }
        this.cbuf.flip();
        return;
    }

    /**
     * エンコードを行う。
     * @return エンコード結果
     */
    private CoderResult encode(){
        boolean endOfInput;
        if(hasMoreInput()) endOfInput = false;
        else               endOfInput = true;

        CoderResult result;
        result = this.encoder.encode(this.cbuf, this.bbuf, endOfInput);
        return result;
    }

    /**
     * 出力バッファを吐き出す。
     * @param os 出力ストリーム
     * @return 出力バイト長
     * @throws IOException 出力エラー
     */
    private int sweepByteBuffer(OutputStream os) throws IOException{
        this.bbuf.flip();

        int total = this.bbuf.remaining();
        os.write(this.barray, 0, total);

        this.bbuf.clear();

        return total;
    }

    /**
     * エンコーダをフラッシュする。
     * @param os 出力ストリーム
     * @return 出力バイト長
     * @throws IOException 出力エラー
     * @throws CharacterCodingException エンコーディングエラー
     */
    private int flush(OutputStream os)
            throws IOException, CharacterCodingException {
        int total = 0;

        CoderResult result;
        do{
            result = this.encoder.flush(this.bbuf);
            if(result.isError()) result.throwException();

            total += sweepByteBuffer(os);

        }while( ! result.isUnderflow() );

        return total;
    }

    /**
     * 与えられた文字列のエンコード結果を格納先バイトストリームへ格納する。
     *
     * <p>エンコード結果は格納先ストリームに追記される。
     *
     * @param text 文字列
     * @param bos 格納先ストリーム
     * @return エンコードしたバイト数。
     * @throws CharacterCodingException エンコードエラー
     */
    @SuppressWarnings("PMD.AvoidRethrowingException")
    public int encodeToByteStream(CharSequence text,
                                     ByteArrayOutputStream bos )
            throws CharacterCodingException {
        int result = 0;
        try{
            result = dumpText(text, bos);
        }catch(CharacterCodingException e){
            throw e;
        }catch(IOException e){
            // ありえない
            assert false;
            throw new AssertionError(e);
        }

        return result;
    }

}
