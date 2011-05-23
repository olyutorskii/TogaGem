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
 */
public class TextDecoder {

    /** デコード作業用入力バッファ長のデフォルト。バイト単位。 */
    public static final int BYTEBUF_SZ = 512;

    /** バッファ成長率。 */
    private static final double WIDEN_RATE = 1.5;


    private final CharsetDecoder decoder;

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
    protected TextDecoder(CharsetDecoder decoder){
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
     * バイト列を読み込み文字列へデコーディングする。
     * @param source 入力ソース
     * @param byteSize 読み込みバイトサイズ
     * @return 文字へのデコード結果。
     * @throws MmdEofException 意図しないファイル末端
     * @throws MmdFormatException 矛盾したバイトシーケンス
     * もしくは未定義文字
     * @throws IOException 入力エラー
     */
    public CharBuffer parseString(MmdSource source, int byteSize)
            throws MmdEofException, MmdFormatException, IOException{
        prepareBuffer(byteSize);

        source.parseByteArray(this.byteArray, 0, byteSize);
        this.byteBuffer.rewind().limit(byteSize);

        this.charBuffer.clear();

        this.decoder.reset();
        CoderResult decResult =
                this.decoder.decode(this.byteBuffer, this.charBuffer, true);
        if(decResult.isError()){
            throw new MmdFormatException("illegal character encoding",
                                         source.getPosition() );
        }else if(decResult.isOverflow()){
            assert false;
        }

        this.roBuffer.rewind().limit(this.charBuffer.position());

        return this.roBuffer;
    }

}
