/*
 * common MMD parser
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sfjp.mikutoga.bin.parser;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.UnmappableCharacterException;

/**
 * 各種バイナリファイルパーサの共通実装。
 * <p>バイト列、各種プリミティブ型値およびエンコードされた文字列を読み込む。
 * <p>long,double、およびビッグエンディアン形式のデータは未サポート。
 */
public class CommonParser {

    private static final String ERRMSG_ILLENC =
            "illegal character encoding";
    private static final String ERRMSG_UNMAP =
            "unmapped character";

    private static final int BYTES_SHORT = Short  .SIZE / Byte.SIZE;
    private static final int BYTES_INT   = Integer.SIZE / Byte.SIZE;
    private static final int BYTES_FLOAT = Float  .SIZE / Byte.SIZE;
    private static final int BYTES_PRIM = 4;

    private static final int MASK_8BIT  =   0xff;
    private static final int MASK_16BIT = 0xffff;

    static{
        assert BYTES_PRIM >= BYTES_FLOAT;
        assert BYTES_PRIM >= BYTES_INT;
        assert BYTES_PRIM >= BYTES_SHORT;
    }


    private final PushbackInputStream is;

    private final byte[] readBuffer;
//  private final ByteBuffer beBuf;
    private final ByteBuffer leBuf;

    private long position = 0L;

    private ByteBuffer btextBuf;


    /**
     * コンストラクタ。
     * @param source 入力ソース
     */
    public CommonParser(InputStream source){
        super();

        this.is = new PushbackInputStream(source, 1);

        this.readBuffer = new byte[BYTES_PRIM];

//      this.beBuf = ByteBuffer.wrap(this.readBuffer);
        this.leBuf = ByteBuffer.wrap(this.readBuffer);

//      this.beBuf.order(ByteOrder.BIG_ENDIAN);
        this.leBuf.order(ByteOrder.LITTLE_ENDIAN);

        return;
    }


    /**
     * 入力ソースの読み込み位置を返す。
     * @return 入力ソースの読み込み位置。単位はbyte。
     */
    protected long getPosition(){
        long result = this.position;
        return result;
    }

    /**
     * 入力ソースにまだデータが残っているか判定する。
     * @return まだ読み込んでいないデータが残っていればtrue
     * @throws IOException IOエラー
     */
    public boolean hasMore() throws IOException{
        int bVal;

        try{
            bVal = this.is.read();
        }catch(EOFException e){ // ありえない？
            return false;
        }

        if(bVal < 0){
            return false;
        }

        this.is.unread(bVal);

        return true;
    }

    /**
     * 入力ソースを読み飛ばす。
     * @param skipLength 読み飛ばすバイト数。
     * @throws IOException IOエラー
     * @throws MmdEofException 読み飛ばす途中でストリーム終端に達した。
     * @see InputStream#skip(long)
     */
    protected void skip(long skipLength)
            throws IOException, MmdEofException {
        long remain = skipLength;

        while(remain > 0L){
            long txSize = this.is.skip(remain);
            if(txSize <= 0L){
                throw new MmdEofException(this.position);
            }
            remain -= txSize;
            this.position += txSize;
        }

        return;
    }

    /**
     * byte配列を読み込む。
     * @param dst 格納先配列
     * @param off 読み込み開始オフセット
     * @param length 読み込みバイト数
     * @throws IOException IOエラー
     * @throws NullPointerException 配列がnull
     * @throws IndexOutOfBoundsException 引数が配列属性と矛盾
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     * @see InputStream#read(byte[], int, int)
     */
    protected void parseByteArray(byte[] dst, int off, int length)
            throws IOException,
                   NullPointerException,
                   IndexOutOfBoundsException,
                   MmdEofException {
        int remain = length;
        int offset = off;

        while(remain > 0){
            int txSize = this.is.read(dst, offset, remain);
            if(txSize <= 0){
                throw new MmdEofException(this.position);
            }
            remain -= txSize;
            offset += txSize;
            this.position += txSize;
        }

        return;
    }

    /**
     * byte配列を読み込む。
     * <p>配列要素全ての読み込みが試みられる。
     * @param dst 格納先配列
     * @throws IOException IOエラー
     * @throws NullPointerException 配列がnull
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     * @see InputStream#read(byte[])
     */
    protected void parseByteArray(byte[] dst)
            throws IOException, NullPointerException, MmdEofException{
        parseByteArray(dst, 0, dst.length);
        return;
    }

    /**
     * 内部バッファへ指定バイト数だけ読み込む。
     * @param fillSize バイト長
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     */
    private void fillBuffer(int fillSize)
            throws IOException, MmdEofException {
        parseByteArray(this.readBuffer, 0, fillSize);
        return;
    }

    /**
     * byte値を読み込む。
     * @return 読み込んだbyte値
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     */
    protected byte parseByte()
            throws IOException, MmdEofException{
        int bData = this.is.read();
        if(bData < 0){
            throw new MmdEofException(this.position);
        }

        byte result = (byte) bData;
        this.position++;

        return result;
    }

    /**
     * 符号無し値としてbyte値を読み込み、int型に変換して返す。
     * <p>符号は拡張されない。(0xffは0x000000ffとなる)
     * @return 読み込まれた値のint値
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     */
    protected int parseUByteAsInt()
            throws IOException, MmdEofException{
        return parseByte() & MASK_8BIT;
    }

    /**
     * byte値を読み込み、boolean型に変換して返す。
     * <p>0x00は偽、それ以外は真と解釈される。
     * @return 読み込まれた値のboolean値
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     */
    protected boolean parseBoolean()
            throws IOException, MmdEofException{
        byte result = parseByte();
        if(result == 0x00) return false;
        return true;
    }

    /**
     * short値を読み込む。
     * <p>short値はリトルエンディアンで格納されていると仮定される。
     * @return 読み込んだshort値
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     */
    protected short parseLeShort()
            throws IOException, MmdEofException{
        fillBuffer(BYTES_SHORT);
        short result = this.leBuf.getShort(0);
        return result;
    }

    /**
     * 符号無し値としてshort値を読み込み、int型に変換して返す。
     * <p>符号は拡張されない。(0xffffは0x0000ffffとなる)
     * <p>short値はリトルエンディアンで格納されていると仮定される。
     * @return 読み込まれた値のint値
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     */
    protected int parseLeUShortAsInt()
            throws IOException, MmdEofException{
        return parseLeShort() & MASK_16BIT;
    }

    /**
     * int値を読み込む。
     * <p>int値はリトルエンディアンで格納されていると仮定される。
     * @return 読み込んだint値
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     */
    protected int parseLeInt()
            throws IOException, MmdEofException{
        fillBuffer(BYTES_INT);
        int result = this.leBuf.getInt(0);
        return result;
    }

    /**
     * float値を読み込む。
     * <p>float値はリトルエンディアンで格納されていると仮定される。
     * @return 読み込んだfloat値
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     */
    protected float parseLeFloat()
            throws IOException, MmdEofException{
        fillBuffer(BYTES_FLOAT);
        float result = this.leBuf.getFloat(0);
        return result;
    }

    /**
     * 固定バイト長の文字列を読み込む。
     * @param decoder 文字デコーダ
     * @param byteLen 読み込む固定バイト長
     * @return 文字列
     * @throws IOException 入力エラー
     * @throws MmdEofException 固定長バイト列を読む前に末端に達した。
     * @throws MmdFormatException 文字エンコーディングに関するエラー
     */
    protected String parseString(TextDecoder decoder, int byteLen)
            throws IOException, MmdEofException, MmdFormatException {
        if(this.btextBuf == null || this.btextBuf.capacity() < byteLen){
            this.btextBuf = ByteBuffer.allocate(byteLen);
        }

        byte[] buf = this.btextBuf.array();
        this.btextBuf.clear();
        parseByteArray(buf, 0, byteLen);
        this.btextBuf.limit(byteLen);

        String result;

        try{
            result = decoder.decode(this.btextBuf);
        }catch(UnmappableCharacterException e){
            String errmsg = ERRMSG_UNMAP;
            long errpos = getPosition() - byteLen + e.getInputLength();
            MmdFormatException ex = new MmdFormatException(errmsg, errpos);
            ex.initCause(e);
            throw ex;
        }catch(MalformedInputException e){
            String errmsg = ERRMSG_ILLENC;
            long errpos = getPosition() - byteLen + e.getInputLength();
            MmdFormatException ex = new MmdFormatException(errmsg, errpos);
            ex.initCause(e);
            throw ex;
        }catch(CharacterCodingException e){  // 状況不明
            String errmsg = ERRMSG_ILLENC;
            long errpos = getPosition();
            MmdFormatException ex = new MmdFormatException(errmsg, errpos);
            ex.initCause(e);
            throw ex;
        }

        return result;
    }

}
