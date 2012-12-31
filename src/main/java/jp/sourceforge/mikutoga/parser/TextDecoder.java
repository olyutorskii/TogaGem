/*
 * character decoder
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.parser;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

/**
 * 文字デコーダー。
 * <p>あらかじめ長さが既知であるバイト列を読み取り、
 * 文字列へのデコード結果を返す。
 * <p>デコード対象のバイト列が全てメモリ上に展開されるので、
 * 巨大なテキストのデコードには不適当。
 * <p>入力バイト値0x00以降をデコード処理の対象から外す
 * 「ゼロチョップモード」を備える。
 * デフォルトではゼロチョップモードはオフ。
 * ゼロチョップモードはUTF16などのデコーディング時に使ってはならない。
 */
public class TextDecoder {

    /** デコード作業用入力バッファ長のデフォルト。バイト単位。 */
    public static final int DEF_BUFSZ = 512;


    private final CharsetDecoder decoder;

    private byte[] byteArray;
    private ByteBuffer byteBuffer;  // byteArrayの別ビュー
    private CharBuffer charBuffer;

    private boolean chopZero = false;


    /**
     * コンストラクタ。
     * @param cs キャラクタセット
     */
    public TextDecoder(Charset cs){
        this(cs.newDecoder());
        return;
    }

    /**
     * コンストラクタ。
     * @param decoder デコーダ
     */
    public TextDecoder(CharsetDecoder decoder){
        super();

        if(decoder == null) throw new NullPointerException();

        this.decoder = decoder;
        this.decoder.onMalformedInput     (CodingErrorAction.REPORT);
        this.decoder.onUnmappableCharacter(CodingErrorAction.REPORT);

        return;
    }

    /**
     * 指定されたバイト長を満たす、デコード用入力バッファを用意する。
     * 既存バッファで足りなければ新たに確保し直す。
     * <p>内部用出力用バッファも同時に適切な長さで確保される。
     * @param newSize 新たなバッファ長。単位はバイト数。
     * @return 入力バッファ。指定バイト長より長いかもしれない。他用厳禁
     */
    public byte[] prepareBuffer(int newSize){
        if(this.byteArray != null && this.byteArray.length >= newSize){
            return this.byteArray;
        }

        int rounded = newSize;
        if(rounded < DEF_BUFSZ) rounded = DEF_BUFSZ;

        this.byteArray = new byte[rounded];
        this.byteBuffer = ByteBuffer.wrap(this.byteArray);

        float maxCharsPerByte = this.decoder.maxCharsPerByte();
        int maxChars =
                (int)( this.byteArray.length * maxCharsPerByte ) + 1;
        this.charBuffer = CharBuffer.allocate(maxChars);

        return this.byteArray;
    }

    /**
     * ゼロチョップモードを設定する。
     * ゼロチョップモードをオンにすると、
     * 入力バイト値0x00以降はデコード対象外となる。
     * @param chop trueならゼロチョップモードオン
     */
    public void setZeroChopMode(boolean chop){
        this.chopZero = chop;
        return;
    }

    /**
     * ゼロチョップモードか否か判定する。
     * @return ゼロチョップモードならtrue
     */
    public boolean isZeroChopMode(){
        return this.chopZero;
    }

    /**
     * 入力バイト列のバイト値'0'出現以降をチョップする。
     * ゼロチョップモードでなければ何もしない。
     */
    protected void chopZeroTermed(){
        if( ! this.chopZero ) return;

        int limit = this.byteBuffer.limit();

        for(int idx = 0; idx < limit; idx++){
            byte bVal = this.byteArray[idx];
            if(bVal == 0x00){
                this.byteBuffer.limit(idx);
                break;
            }
        }

        return;
    }

    /**
     * 指定配列を内部にコピーした後、デコード処理を行う。
     * @param basePos エラー情報に含まれるストリーム位置
     * @param buf 入力バッファ
     * @return デコードされた文字列
     * @throws MmdFormatException デコード異常
     */
    public String decode(long basePos, byte[] buf)
            throws MmdFormatException {
        String result = decode(basePos, buf, 0, buf.length);
        return result;
    }

    /**
     * 指定配列の一部を内部にコピーした後、デコード処理を行う。
     * @param basePos エラー情報に含まれるストリーム位置
     * @param buf 入力バッファ
     * @param off 位置オフセット
     * @param byteLen バイト長
     * @return デコードされた文字列
     * @throws MmdFormatException デコード異常
     * @throws IndexOutOfBoundsException 不正な位置指定。
     */
    public String decode(long basePos, byte[] buf, int off, int byteLen)
            throws MmdFormatException, IndexOutOfBoundsException {
        prepareBuffer(byteLen);
        System.arraycopy(buf, off, this.byteArray, 0, byteLen);
        String result = decode(basePos, byteLen);
        return result;
    }

    /**
     * 内部バッファのデコード処理を行う。
     * @param basePos エラー情報に含まれるストリーム位置
     * @param byteLen バイト長
     * @return デコードされた文字列
     * @throws MmdFormatException デコード異常
     * @throws IndexOutOfBoundsException 不正なバイト長。
     */
    public String decode(long basePos, int byteLen)
            throws MmdFormatException, IndexOutOfBoundsException {
        if(this.byteArray.length < byteLen){
            throw new IndexOutOfBoundsException();
        }

        this.byteBuffer.rewind().limit(byteLen);
        chopZeroTermed();

        this.charBuffer.clear();

        this.decoder.reset();
        CoderResult decResult =
                this.decoder.decode(this.byteBuffer, this.charBuffer, true);

        if(decResult.isError()){
            String errMsg;
            if(decResult.isUnmappable()){
                errMsg = "unmapped character";
            }else{
                errMsg = "illegal character encoding";
            }
            long errPos = basePos + decResult.length();
            throw new MmdFormatException(errMsg, errPos);
        }

        assert ! decResult.isOverflow();

        String result = this.charBuffer.flip().toString();
        return result;
    }

}
