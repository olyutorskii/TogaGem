/*
 * binary parser interface
 *
 * License : The MIT License
 * Copyright(c) 2013 MikuToga Partners
 */

package jp.sfjp.mikutoga.bin.parser;

import java.io.IOException;

/**
 * バイナリパーサの共通インタフェース。
 *
 * <p>バイト列、各種プリミティブ型値およびエンコードされた文字列を読み込む。
 *
 * <p>long,double、およびビッグエンディアン形式のデータは未サポート。
 */
public interface BinParser {

    /**
     * 入力ソースの読み込み位置を返す。
     *
     * @return 入力ソースの読み込み位置。単位はbyte。
     */
    long getPosition();

    /**
     * 入力ソースにまだデータが残っているか判定する。
     *
     * @return まだ読み込んでいないデータが残っていればtrue
     * @throws IOException IOエラー
     */
    boolean hasMore() throws IOException;

    /**
     * 入力ソースを読み飛ばす。
     *
     * @param skipLength 読み飛ばすバイト数。
     * @throws IOException IOエラー
     * @throws MmdEofException 読み飛ばす途中でストリーム終端に達した。
     * @see java.io.InputStream#skip(long)
     */
    void skip(long skipLength) throws IOException, MmdEofException;

    /**
     * byte配列を読み込む。
     *
     * @param dst 格納先配列
     * @param off 読み込み開始オフセット
     * @param length 読み込みバイト数
     * @throws NullPointerException 配列がnull
     * @throws IndexOutOfBoundsException 引数が配列属性と矛盾
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     * @see java.io.InputStream#read(byte[], int, int)
     */
    void parseByteArray(byte[] dst, int off, int length)
            throws NullPointerException,
                   IndexOutOfBoundsException,
                   IOException,
                   MmdEofException;

    /**
     * byte配列を読み込む。
     *
     * <p>配列要素全ての読み込みが試みられる。
     *
     * @param dst 格納先配列
     * @throws NullPointerException 配列がnull
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     * @see java.io.InputStream#read(byte[])
     */
    void parseByteArray(byte[] dst)
            throws NullPointerException, IOException, MmdEofException;

    /**
     * byte値を読み込む。
     *
     * @return 読み込んだbyte値
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     */
    byte parseByte() throws IOException, MmdEofException;

    /**
     * 符号無し値としてbyte値を読み込み、int型に変換して返す。
     *
     * <p>符号は拡張されない。(0xffは0x000000ffとなる)
     *
     * @return 読み込まれた値のint値
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     */
    int parseUByteAsInt() throws IOException, MmdEofException;

    /**
     * byte値を読み込み、boolean型に変換して返す。
     *
     * <p>0x00は偽、それ以外は真と解釈される。
     *
     * @return 読み込まれた値のboolean値
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     */
    boolean parseBoolean() throws IOException, MmdEofException;

    /**
     * short値を読み込む。
     *
     * <p>short値はリトルエンディアンで格納されていると仮定される。
     *
     * @return 読み込んだshort値
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     */
    short parseLeShort() throws IOException, MmdEofException;

    /**
     * 符号無し値としてshort値を読み込み、int型に変換して返す。
     *
     * <p>符号は拡張されない。(0xffffは0x0000ffffとなる)
     *
     * <p>short値はリトルエンディアンで格納されていると仮定される。
     *
     * @return 読み込まれた値のint値
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     */
    int parseLeUShortAsInt() throws IOException, MmdEofException;

    /**
     * int値を読み込む。
     *
     * <p>int値はリトルエンディアンで格納されていると仮定される。
     *
     * @return 読み込んだint値
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     */
    int parseLeInt() throws IOException, MmdEofException;

    /**
     * float値を読み込む。
     *
     * <p>float値はリトルエンディアンで格納されていると仮定される。
     *
     * @return 読み込んだfloat値
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     */
    float parseLeFloat() throws IOException, MmdEofException;

    /**
     * 固定バイト長の文字列を読み込む。
     * @param decoder 文字デコーダ
     * @param byteLen 読み込む固定バイト長
     * @return 文字列
     * @throws IOException 入力エラー
     * @throws MmdEofException 固定長バイト列を読む前に末端に達した。
     * @throws MmdFormatException 文字エンコーディングに関するエラー
     */
    String parseString(TextDecoder decoder, int byteLen)
            throws IOException, MmdEofException, MmdFormatException;

}
