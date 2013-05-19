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
 * 入力ストリームをソースとするバイナリパーサ実装。
 */
public class CommonParser implements BinParser{

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
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public long getPosition(){
        long result = this.position;
        return result;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     * @param skipLength {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @throws MmdEofException {@inheritDoc}
     */
    @Override
    public void skip(long skipLength)
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
     * {@inheritDoc}
     * @param dst {@inheritDoc}
     * @param off {@inheritDoc}
     * @param length {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @throws MmdEofException {@inheritDoc}
     */
    @Override
    public void parseByteArray(byte[] dst, int off, int length)
            throws NullPointerException,
                   IndexOutOfBoundsException,
                   IOException,
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
     * {@inheritDoc}
     * @param dst {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @throws MmdEofException {@inheritDoc}
     */
    @Override
    public void parseByteArray(byte[] dst)
            throws NullPointerException, IOException, MmdEofException{
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
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @throws MmdEofException {@inheritDoc}
     */
    @Override
    public byte parseByte()
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
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @throws MmdEofException {@inheritDoc}
     */
    @Override
    public int parseUByteAsInt()
            throws IOException, MmdEofException{
        return parseByte() & MASK_8BIT;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @throws MmdEofException {@inheritDoc}
     */
    @Override
    public boolean parseBoolean()
            throws IOException, MmdEofException{
        byte result = parseByte();
        if(result == 0x00) return false;
        return true;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @throws MmdEofException {@inheritDoc}
     */
    @Override
    public short parseLeShort()
            throws IOException, MmdEofException{
        fillBuffer(BYTES_SHORT);
        short result = this.leBuf.getShort(0);
        return result;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @throws MmdEofException {@inheritDoc}
     */
    @Override
    public int parseLeUShortAsInt()
            throws IOException, MmdEofException{
        return parseLeShort() & MASK_16BIT;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @throws MmdEofException {@inheritDoc}
     */
    @Override
    public int parseLeInt()
            throws IOException, MmdEofException{
        fillBuffer(BYTES_INT);
        int result = this.leBuf.getInt(0);
        return result;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @throws MmdEofException {@inheritDoc}
     */
    @Override
    public float parseLeFloat()
            throws IOException, MmdEofException{
        fillBuffer(BYTES_FLOAT);
        float result = this.leBuf.getFloat(0);
        return result;
    }

    /**
     * {@inheritDoc}
     * @param decoder {@inheritDoc}
     * @param byteLen {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @throws MmdEofException {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public String parseString(TextDecoder decoder, int byteLen)
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
