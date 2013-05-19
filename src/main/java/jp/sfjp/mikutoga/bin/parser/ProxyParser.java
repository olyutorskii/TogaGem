/*
 * parser proxy
 *
 * License : The MIT License
 * Copyright(c) 2013 MikuToga Partners
 */

package jp.sfjp.mikutoga.bin.parser;

import java.io.IOException;

/**
 * 委譲パーサ。
 * <p>別のパーサにパース処理を委譲する。
 */
public class ProxyParser implements BinParser{

    private final BinParser delegate;


    /**
     * コンストラクタ。
     * @param delegate 委譲先パーサ
     * @throws NullPointerException 引数がnull
     */
    public ProxyParser(BinParser delegate) throws NullPointerException{
        super();

        if(delegate == null) throw new NullPointerException();
        this.delegate = delegate;

        return;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public long getPosition() {
        return this.delegate.getPosition();
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public boolean hasMore() throws IOException {
        return this.delegate.hasMore();
    }

    /**
     * {@inheritDoc}
     * @param skipLength {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @throws MmdEofException {@inheritDoc}
     */
    @Override
    public void skip(long skipLength) throws IOException, MmdEofException {
        this.delegate.skip(skipLength);
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
        this.delegate.parseByteArray(dst, off, length);
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
            throws NullPointerException,
                   IOException,
                   MmdEofException {
        this.delegate.parseByteArray(dst);
        return;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @throws MmdEofException {@inheritDoc}
     */
    @Override
    public byte parseByte() throws IOException, MmdEofException {
        return this.delegate.parseByte();
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @throws MmdEofException {@inheritDoc}
     */
    @Override
    public int parseUByteAsInt() throws IOException, MmdEofException {
        return this.delegate.parseUByteAsInt();
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @throws MmdEofException {@inheritDoc}
     */
    @Override
    public boolean parseBoolean() throws IOException, MmdEofException {
        return this.delegate.parseBoolean();
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @throws MmdEofException {@inheritDoc}
     */
    @Override
    public short parseLeShort() throws IOException, MmdEofException {
        return this.delegate.parseLeShort();
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @throws MmdEofException {@inheritDoc}
     */
    @Override
    public int parseLeUShortAsInt() throws IOException, MmdEofException {
        return this.delegate.parseLeUShortAsInt();
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @throws MmdEofException {@inheritDoc}
     */
    @Override
    public int parseLeInt() throws IOException, MmdEofException {
        return this.delegate.parseLeInt();
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @throws MmdEofException {@inheritDoc}
     */
    @Override
    public float parseLeFloat() throws IOException, MmdEofException {
        return this.delegate.parseLeFloat();
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
        return this.delegate.parseString(decoder, byteLen);
    }

}
