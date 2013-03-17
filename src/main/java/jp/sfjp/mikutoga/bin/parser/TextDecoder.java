/*
 * character decoder
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sfjp.mikutoga.bin.parser;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

/**
 * 文字デコーダー。
 * <p>あらかじめバイト長が既知であるバイトバッファを読み取り、
 * 文字列へのデコード結果を返す。
 * <p>デコード対象のバイト列が全てメモリ上に展開される必要があるので、
 * 巨大なテキストのデコードには不適当。
 * <p>入力バイト値0x00以降をデコード処理の対象から外す
 * 「ゼロチョップモード」を備える。
 * デフォルトではゼロチョップモードはオフ。
 * ゼロチョップモードはUTF16などのデコーディング時に使ってはならない。
 */
public class TextDecoder {

    private final CharsetDecoder decoder;

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

        this.decoder = decoder;
        this.decoder.reset();
        this.decoder.onMalformedInput     (CodingErrorAction.REPORT);
        this.decoder.onUnmappableCharacter(CodingErrorAction.REPORT);

        return;
    }

    /**
     * 指定されたバイト長のデコードに必要な出力バッファを用意する。
     * <p>既存バッファで足りなければ新たに確保し直す。
     * @param byteLength 入力バイト長
     * @return 出力バッファ長。(キャラクタ単位)
     */
    protected int prepareCharBuffer(int byteLength){
        float maxCharsPerByte = this.decoder.maxCharsPerByte();
        int maxChars = (int)( byteLength * maxCharsPerByte ) + 1;

        if(this.charBuffer != null){
            int capacity = this.charBuffer.capacity();
            if(capacity >= maxChars){
                return capacity;
            }
        }

        this.charBuffer = CharBuffer.allocate(maxChars);

        return maxChars;
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
     * 入力バイトバッファのバイト値'0'出現以降をチョップする。
     * ゼロチョップモードでなければ何もしない。
     * @param bBuf 入力バイトバッファ
     */
    protected void chopZeroTermed(ByteBuffer bBuf){
        if( ! this.chopZero ) return;

        int start = bBuf.position();
        int limit = bBuf.limit();
        for(int idx = start; idx < limit; idx++){
            byte bVal = bBuf.get(idx);
            if(bVal == 0x00){
                bBuf.limit(idx);
                break;
            }
        }

        return;
    }

    /**
     * バイトバッファの文字列デコードを行う。
     * @param bBuf バイトバッファ
     * @return デコードされた文字列
     * @throws CharacterCodingException デコード異常
     */
    public String decode(ByteBuffer bBuf) throws CharacterCodingException{
        chopZeroTermed(bBuf);

        int blen = bBuf.remaining();
        prepareCharBuffer(blen);
        this.charBuffer.clear();

        this.decoder.reset();
        CoderResult decResult;
        decResult = this.decoder.decode(bBuf, this.charBuffer, true);

        if(decResult.isError()){
            decResult.throwException();
            assert false;
        }

        assert ! decResult.isOverflow();

        String result = this.charBuffer.flip().toString();
        return result;
    }

}
