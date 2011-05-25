/*
 * common MMD parser
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sourceforge.mikutoga.parser;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * 各種パーサの共通実装。
 */
public class CommonParser {

    /**
     * PMDで用いられる文字エンコーディング(windows-31j)。
     * ほぼShift_JISのスーパーセットと思ってよい。
     * デコード結果はUCS-2集合に収まるはず。
     */
    public static final Charset CS_WIN31J = Charset.forName("windows-31j");

    /** PMXで用いられる文字エンコーディング(UTF-8)。 */
    public static final Charset CS_UTF8 = Charset.forName("UTF-8");

    /** PMXで用いられる文字エンコーディング(UTF-16のリトルエンディアン)。 */
    public static final Charset CS_UTF16LE = Charset.forName("UTF-16LE");

    private final MmdSource source;

    private final TextDecoder decoderWin31j  = new TextDecoder(CS_WIN31J);
    private final TextDecoder decoderUTF8    = new TextDecoder(CS_UTF8);
    private final TextDecoder decoderUTF16LE = new TextDecoder(CS_UTF16LE);

    /**
     * コンストラクタ。
     * @param source 入力ソース
     */
    public CommonParser(MmdSource source){
        super();

        this.source = source;

        this.decoderWin31j .setZeroChopMode(true);
        this.decoderUTF8   .setZeroChopMode(false);
        this.decoderUTF16LE.setZeroChopMode(false);

        return;
    }

    /**
     * 入力ソースにまだデータが残っているか判定する。
     * @return まだ読み込んでいないデータが残っていればtrue
     * @throws IOException IOエラー
     * @see MmdSource#hasMore()
     */
    protected boolean hasMore() throws IOException{
        boolean result = this.source.hasMore();
        return result;
    }

    /**
     * 入力ソースを読み飛ばす。
     * @param skipLength 読み飛ばすバイト数。
     * @throws IOException IOエラー
     * @throws MmdEofException 読み飛ばす途中でストリーム終端に達した。
     * @see MmdSource#skip(long)
     */
    protected void skip(long skipLength)
            throws IOException, MmdEofException {
        long result = this.source.skip(skipLength);
        if(result != skipLength){
            throw new MmdEofException(this.source.getPosition());
        }

        return;
    }

    /**
     * 入力ソースを読み飛ばす。
     * @param skipLength 読み飛ばすバイト数。
     * @throws IOException IOエラー
     * @throws MmdEofException 読み飛ばす途中でストリーム終端に達した。
     * @see MmdSource#skip(long)
     */
    protected void skip(int skipLength)
            throws IOException, MmdEofException {
        skip((long) skipLength);
    }

    /**
     * byte値を読み込む。
     * @return 読み込んだbyte値
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     * @see MmdSource#parseByte()
     */
    protected byte parseByte()
            throws IOException, MmdEofException{
        return this.source.parseByte();
    }

    /**
     * 符号無し値としてbyte値を読み込み、int型に変換して返す。
     * 符号は拡張されない。(0xffは0x000000ffとなる)
     * @return 読み込まれた値のint値
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     * @see MmdSource#parseUByteAsInteger()
     */
    protected int parseUByteAsInteger()
            throws IOException, MmdEofException{
        return this.source.parseUByteAsInteger();
    }

    /**
     * byte値を読み込み、boolean型に変換して返す。
     * 0x00は偽、それ以外は真と解釈される。
     * @return 読み込まれた値のboolean値
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     * @see MmdSource#parseBoolean()
     */
    protected boolean parseBoolean()
            throws IOException, MmdEofException{
        return this.source.parseBoolean();
    }

    /**
     * short値を読み込む。
     * short値はリトルエンディアンで格納されていると仮定される。
     * @return 読み込んだshort値
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     * @see MmdSource#parseShort()
     */
    protected short parseShort()
            throws IOException, MmdEofException{
        return this.source.parseShort();
    }

    /**
     * 符号無し値としてshort値を読み込み、int型に変換して返す。
     * 符号は拡張されない。(0xffffは0x0000ffffとなる)
     * short値はリトルエンディアンで格納されていると仮定される。
     * @return 読み込まれた値のint値
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     * @see MmdSource#parseUShortAsInteger()
     */
    protected int parseUShortAsInteger()
            throws IOException, MmdEofException{
        return this.source.parseUShortAsInteger();
    }

    /**
     * int値を読み込む。
     * int値はリトルエンディアンで格納されていると仮定される。
     * @return 読み込んだint値
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     * @see MmdSource#parseInteger()
     */
    protected int parseInteger()
            throws IOException, MmdEofException{
        return this.source.parseInteger();
    }

    /**
     * float値を読み込む。
     * float値はリトルエンディアンで格納されていると仮定される。
     * @return 読み込んだfloat値
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     * @see MmdSource#parseFloat()
     */
    protected float parseFloat()
            throws IOException, MmdEofException{
        return this.source.parseFloat();
    }

    /**
     * byte配列を読み込む。
     * @param dst 格納先配列
     * @param offset 読み込み開始オフセット
     * @param length 読み込みバイト数
     * @throws IOException IOエラー
     * @throws NullPointerException 配列がnull
     * @throws IndexOutOfBoundsException 引数が配列属性と矛盾
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     * @see MmdSource#parseByteArray(byte[], int, int)
     */
    protected void parseByteArray(byte[] dst, int offset, int length)
            throws IOException,
                   NullPointerException,
                   IndexOutOfBoundsException,
                   MmdEofException {
        this.source.parseByteArray(dst, offset, length);
        return;
    }

    /**
     * byte配列を読み込む。
     * 配列要素全ての読み込みが試みられる。
     * @param dst 格納先配列
     * @throws IOException IOエラー
     * @throws NullPointerException 配列がnull
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     * @see MmdSource#parseByteArray(byte[])
     */
    protected void parseByteArray(byte[] dst)
            throws IOException, NullPointerException, MmdEofException{
        this.source.parseByteArray(dst);
        return;
    }

    /**
     * float配列を読み込む。
     * @param dst 格納先配列
     * @param offset 読み込み開始オフセット
     * @param length 読み込みfloat要素数
     * @throws IOException IOエラー
     * @throws NullPointerException 配列がnull
     * @throws IndexOutOfBoundsException 引数が配列属性と矛盾
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     * @see MmdSource#parseFloatArray(float[], int, int)
     */
    protected void parseFloatArray(float[] dst, int offset, int length)
            throws IOException,
                   NullPointerException,
                   IndexOutOfBoundsException,
                   MmdEofException {
        this.source.parseFloatArray(dst, offset, length);
        return;
    }

    /**
     * float配列を読み込む。
     * 配列要素全ての読み込みが試みられる。
     * @param dst 格納先配列
     * @throws IOException IOエラー
     * @throws NullPointerException 配列がnull
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     * @see MmdSource#parseFloatArray(float[])
     */
    protected void parseFloatArray(float[] dst)
            throws IOException, NullPointerException, MmdEofException{
        this.source.parseFloatArray(dst);
        return;
    }

    /**
     * 指定された最大バイト長に収まるゼロ終端(0x00)文字列を読み込む。
     * 入力バイト列はwindows-31jエンコーディングとして解釈される。
     * ゼロ終端以降のデータは無視されるが、
     * IO入力は指定バイト数だけ読み進められる。
     * ゼロ終端が見つからないまま指定バイト数が読み込み終わった場合、
     * そこまでのデータから文字列を構成する。
     * @param maxlen 読み込みバイト数
     * @return デコードされた文字列
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     * @throws MmdFormatException 不正な文字エンコーディングが検出された。
     */
    protected String parseZeroTermWin31J(int maxlen)
            throws IOException,
                   MmdEofException,
                   MmdFormatException {
        CharBuffer encoded =
                this.decoderWin31j.parseString(this.source, maxlen);

        String result = encoded.toString();

        return result;
    }

    /**
     * 4byte整数によるバイト列長とそれに続くUTF8バイト列を
     * 文字にデコードする。
     * @return デコードされた文字列。
     * @throws IOException IOエラー
     * @throws MmdEofException 予期せぬ入力終端
     * @throws MmdFormatException 不正な文字エンコーディングが検出された。
     */
    protected String parseHollerithUtf8()
            throws IOException,
                   MmdEofException,
                   MmdFormatException {
        int byteLen = this.source.parseInteger();

        CharBuffer encoded =
                this.decoderUTF8.parseString(this.source, byteLen);

        String result = encoded.toString();

        return result;
    }

    /**
     * 4byte整数によるバイト列長とそれに続くUTF16-LEバイト列を
     * 文字にデコードする。
     * @return デコードされた文字列。
     * @throws IOException IOエラー
     * @throws MmdEofException 予期せぬ入力終端
     * @throws MmdFormatException 不正な文字エンコーディングが検出された。
     */
    protected String parseHollerithUtf16LE()
            throws IOException,
                   MmdEofException,
                   MmdFormatException {
        int byteLen = this.source.parseInteger();

        CharBuffer encoded =
                this.decoderUTF16LE.parseString(this.source, byteLen);

        String result = encoded.toString();

        return result;
    }

}
