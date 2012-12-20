/*
 * MMD file input stream
 *
 * License : The MIT License
 * Copyright(c) 2012 MikuToga Partners
 */

package jp.sourceforge.mikutoga.parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * MMD各種バイナリデータ用の入力ストリーム。
 * リトルエンディアンデータおよびEOFの扱い方で
 * java.io.DataInputStreamと差別化される。
 */
public class MmdInputStream extends SpottedInputStream {

    private static final int BYTES_SHORT = Short  .SIZE / Byte.SIZE;
    private static final int BYTES_INT   = Integer.SIZE / Byte.SIZE;
    private static final int BYTES_FLOAT = Float  .SIZE / Byte.SIZE;
    private static final int BUF_SZ = 4;


    private final byte[] readArray;
    private final ByteBuffer beBuf;
    private final ByteBuffer leBuf;


    /**
     * コンストラクタ。
     * @param is 入力ストリーム
     */
    public MmdInputStream(InputStream is){
        super(is);

        this.readArray = new byte[BUF_SZ];

        this.beBuf = ByteBuffer.wrap(this.readArray);
        this.leBuf = ByteBuffer.wrap(this.readArray);

        this.beBuf.order(ByteOrder.BIG_ENDIAN);
        this.leBuf.order(ByteOrder.LITTLE_ENDIAN);

        return;
    }


    /**
     * 入力ストリームを読み飛ばす。
     * なるべく指定したバイト数全てが読み飛ばされるよう、
     * 読み飛ばし処理が繰り返される。
     * @param skipLength 読み飛ばすバイト数。
     * @return 実際に読み飛ばしたバイト数。
     * @throws IOException IOエラー
     * @see java.io.InputStream#skip(long)
     */
    public long skipRepeat(long skipLength)
            throws IOException{
        if(skipLength <= 0L) return 0L;

        long remain = skipLength;
        while(remain > 0L){      // BufferedInputStream対策
            long result = skip(remain);
            if(result <= 0L) break;
            remain -= result;
        }

        return skipLength - remain;
    }

    /**
     * 指定したバイト数だけ内部バッファに読み込む。
     * @param fillSize 読み込むバイト数
     * @throws IOException IOエラー
     * @throws IndexOutOfBoundsException 引数がバッファサイズと矛盾。
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     */
    private void fillBuffer(int fillSize)
            throws IOException, IndexOutOfBoundsException, MmdEofException{
        int result = read(this.readArray, 0, fillSize);

        if(result != fillSize){
            long pos = getPosition();
            throw new MmdEofException(pos);
        }

        return;
    }

    /**
     * byte値を読み込む。
     * @return 読み込んだbyte値
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     */
    public byte parseByte() throws IOException, MmdEofException{
        int bData = read();
        if(bData < 0){
            long pos = getPosition();
            throw new MmdEofException(pos);
        }

        byte result = (byte) bData;
        return result;
    }

    /**
     * byte値を読み込み、boolean型に変換して返す。
     * 0x00は偽、それ以外は真と解釈される。
     * @return 読み込まれた値のboolean値
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     */
    public boolean parseBoolean() throws IOException, MmdEofException{
        byte result = parseByte();
        if(result == 0x00) return false;
        return true;
    }

    /**
     * short値を読み込む。
     * @param buf オーダー指定されたバッファ
     * @return short値
     * @throws IOException 入力エラー
     * @throws MmdEofException 途中でストリーム終端に達した。
     */
    private short parseShort(ByteBuffer buf)
            throws IOException, MmdEofException{
        fillBuffer(BYTES_SHORT);
        short result = buf.getShort(0);

        return result;
    }

    /**
     * ビッグエンディアンでshort値を読み込む。
     * @return short値
     * @throws IOException 入力エラー
     * @throws MmdEofException 途中でストリーム終端に達した。
     */
    public short parseBeShort() throws IOException, MmdEofException{
        short result = parseShort(this.beBuf);
        return result;
    }

    /**
     * リトルエンディアンでshort値を読み込む。
     * @return short値
     * @throws IOException 入力エラー
     * @throws MmdEofException 途中でストリーム終端に達した。
     */
    public short parseLeShort() throws IOException, MmdEofException{
        short result = parseShort(this.leBuf);
        return result;
    }

    /**
     * int値を読み込む。
     * @param buf オーダー指定されたバッファ
     * @return int値
     * @throws IOException 入力エラー
     * @throws MmdEofException 途中でストリーム終端に達した。
     */
    private int parseInt(ByteBuffer buf)
            throws IOException, MmdEofException{
        fillBuffer(BYTES_INT);
        int result = buf.getInt(0);

        return result;
    }

    /**
     * ビッグエンディアンでint値を読み込む。
     * @return int値
     * @throws IOException 入力エラー
     * @throws MmdEofException 途中でストリーム終端に達した。
     */
    public int parseBeInt() throws IOException, MmdEofException{
        int result = parseInt(this.beBuf);
        return result;
    }

    /**
     * リトルエンディアンでint値を読み込む。
     * @return int値
     * @throws IOException 入力エラー
     * @throws MmdEofException 途中でストリーム終端に達した。
     */
    public int parseLeInt() throws IOException, MmdEofException{
        int result = parseInt(this.leBuf);
        return result;
    }

    /**
     * float値を読み込む。
     * @param buf オーダー指定されたバッファ
     * @return float値
     * @throws IOException 入力エラー
     * @throws MmdEofException 途中でストリーム終端に達した。
     */
    private float parseFloat(ByteBuffer buf)
            throws IOException, MmdEofException{
        fillBuffer(BYTES_FLOAT);
        float result = buf.getFloat(0);
        return result;
    }

    /**
     * ビッグエンディアンでfloat値を読み込む。
     * @return float値
     * @throws IOException 入力エラー
     * @throws MmdEofException 途中でストリーム終端に達した。
     */
    public float parseBeFloat() throws IOException, MmdEofException{
        float result = parseFloat(this.beBuf);
        return result;
    }

    /**
     * リトルエンディアンでfloat値を読み込む。
     * @return float値
     * @throws IOException 入力エラー
     * @throws MmdEofException 途中でストリーム終端に達した。
     */
    public float parseLeFloat() throws IOException, MmdEofException{
        float result = parseFloat(this.leBuf);
        return result;
    }

}
