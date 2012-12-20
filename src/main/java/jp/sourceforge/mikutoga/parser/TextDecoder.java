/*
 * character decoder
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

/**
 * 文字デコーダー。
 * <p>あらかじめ長さが既知であるバイト列をMMD入力ソースから読み取り、
 * デコーディング結果を返す。
 * <p>デコード対象のバイト列が全てメモリ上に展開されるので、
 * 巨大なテキストのデコードには不適当。
 * <p>入力バイト値0x00以降をデコーディングの対象から外す
 * 「ゼロチョップモード」を備える。
 * デフォルトではゼロチョップモードはオフ。
 * ゼロチョップモードはUTF16などのデコーディング時に使っても意味が無い。
 */
public class TextDecoder {

    /** デコード作業用入力バッファ長のデフォルト。バイト単位。 */
    public static final int BYTEBUF_SZ = 512;

    /** バッファ成長率。 */
    private static final double WIDEN_RATE = 1.5;


    private final CharsetDecoder decoder;

    private boolean chopZero = false;

    private byte[] byteArray;
    private ByteBuffer byteBuffer;  // byteArrayの別ビュー
    private CharBuffer charBuffer;
    private CharBuffer roBuffer;    // charBufferの閲覧用ビュー


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
        this.decoder = decoder;
        this.decoder.onMalformedInput(CodingErrorAction.REPORT);
        this.decoder.onUnmappableCharacter(CodingErrorAction.REPORT);
        return;
    }

    /**
     * 指定されたサイズで文字デコード用バッファを用意する。
     * 既存バッファで足りなければ新たに確保し直す。
     * @param newSize バッファ長さ。単位はバイト数。
     */
    protected void prepareBuffer(int newSize){
        if(this.byteArray != null && this.byteArray.length >= newSize){
            return;
        }

        int rounded = (int)( newSize * WIDEN_RATE );
        if(rounded < BYTEBUF_SZ) rounded = BYTEBUF_SZ;

        this.byteArray = new byte[rounded];
        this.byteBuffer = ByteBuffer.wrap(this.byteArray);

        float maxCharsPerByte = this.decoder.maxCharsPerByte();
        int maxChars =
                (int)( this.byteBuffer.capacity() * maxCharsPerByte ) + 1;
        this.charBuffer = CharBuffer.allocate(maxChars);

        this.roBuffer = this.charBuffer.asReadOnlyBuffer();

        return;
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
     * バイト列を読み込み文字列へデコーディングする。
     * @param is 入力ストリーム
     * @param byteSize 読み込みバイトサイズ
     * @return 内部に保持されるデコード結果。
     * 次回呼び出しまでに結果の適切なコピーがなされなければならない。
     * @throws MmdEofException 意図しないファイル末端
     * @throws MmdFormatException 矛盾したバイトシーケンス
     * もしくは未定義文字
     * @throws IOException 入力エラー
     */
    public CharBuffer parseString(MmdInputStream is, int byteSize)
            throws MmdEofException, MmdFormatException, IOException{
        prepareBuffer(byteSize);

        int readSize = is.read(this.byteArray, 0, byteSize);
        if(readSize != byteSize){
            throw new MmdEofException(is.getPosition());
        }

        this.byteBuffer.rewind().limit(byteSize);
        chopZeroTermed();

        this.charBuffer.clear();

        this.decoder.reset();
        CoderResult decResult =
                this.decoder.decode(this.byteBuffer, this.charBuffer, true);
        if(decResult.isError()){
            if(decResult.isUnmappable()){
                throw new MmdFormatException("unmapped character",
                                             is.getPosition() );
            }else{
                throw new MmdFormatException("illegal character encoding",
                                             is.getPosition() );
            }
        }else if(decResult.isOverflow()){
            assert false;
        }

        this.roBuffer.rewind().limit(this.charBuffer.position());

        return this.roBuffer;
    }

}
