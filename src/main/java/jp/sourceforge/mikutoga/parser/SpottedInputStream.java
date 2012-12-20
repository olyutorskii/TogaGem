/*
 * position spotted input stream
 *
 * License : The MIT License
 * Copyright(c) 2012 MikuToga Partners
 */

package jp.sourceforge.mikutoga.parser;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

/**
 * エラー報告用のバイト位置管理、
 * およびストリーム末端の判定
 * のための機能を含む入力ストリーム。
 */
class SpottedInputStream extends InputStream {

    private final PushbackInputStream pin;
    private long position = 0L;


    /**
     * コンストラクタ。
     * @param is 入力ストリーム
     */
    SpottedInputStream(InputStream is){
        super();
        this.pin = new PushbackInputStream(is, 1);
        return;
    }


    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public int read() throws IOException{
        int result = this.pin.read();
        if(result >= 0) this.position++;
        return result;
    }

    /**
     * {@inheritDoc}
     * @param b {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public int read(byte[] b) throws IOException{
        int result = this.pin.read(b);
        if(result >= 0) this.position += result;
        return result;
    }

    /**
     * {@inheritDoc}
     * @param b {@inheritDoc}
     * @param off {@inheritDoc}
     * @param len {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException{
        int result = this.pin.read(b, off, len);
        if(result >= 0) this.position += result;
        return result;
    }

    /**
     * {@inheritDoc}
     * @param skipLength {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public long skip(long skipLength) throws IOException{
        long result = this.pin.skip(skipLength);
        if(result >= 0L) this.position += result;
        return result;
    }

    /**
     * {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public void close() throws IOException{
        this.pin.close();
        return;
    }

    /**
     * 読み込み済みバイト数を返す。
     * @return 読み込み済みバイト数
     */
    public long getPosition(){
        return this.position;
    }

    /**
     * まだ入力が残っているか判定する。
     * @return 残っていればtrue
     * @throws IOException 入力エラー。java.io.EOFException ではないはず。
     */
    public boolean hasMore() throws IOException{
        int bVal;

        try{
            bVal = this.pin.read();
        }catch(EOFException e){ // ありえない？
            return false;
        }

        if(bVal < 0){
            return false;
        }

        this.pin.unread(bVal);

        return true;
    }

}
