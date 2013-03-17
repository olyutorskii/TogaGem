/*
 */

package jp.sfjp.mikutoga.bin.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

/**
 * テスト用入力ストリーム。
 * これはユニットテストではない。
 */
public class DummyInputStream extends InputStream {

    private static final int MASK_08BIT = 0xff;


    private final Queue<Byte> queue = new LinkedList<Byte>();

    private boolean closed = false;

    private long errPos = -1L;
    private IOException errEx = null;


    public DummyInputStream(byte[] bArray, int offset, int len){
        super();
        queueByteArrayImpl(bArray, offset, len);
        return;
    }

    public DummyInputStream(byte... bArray){
        this(bArray, 0, bArray.length);
        return;
    }

    public DummyInputStream(int... iArray) throws IllegalArgumentException{
        this(toBarray(iArray));
        return;
    }


    /**
     * int配列からbyte配列への変換を行う。
     * bitが欠損した場合は異常系を投げる。
     * @param iArray int配列
     * @return byte配列
     * @throws IllegalArgumentException bit欠損が発生
     */
    protected static byte[] toBarray(int[] iArray)
            throws IllegalArgumentException{
        int length = iArray.length;
        byte[] result = new byte[length];

        for(int pos = 0; pos < length; pos++){
            int iVal = iArray[pos];
            if(iVal < 0x00 || 0xff < iVal){
                throw new IllegalArgumentException();
            }
            byte bVal = (byte) iVal;
            result[pos] = bVal;
        }

        return result;
    }


    /**
     * 指定バイト数読み込んだ後に入力例外を意図的に発生させるよう設定する。
     * @param pos 正常に読める残りバイト数。負の値なら設定解除。
     * @param ex 入力例外。解除時に限ってnullも可。
     */
    public void setErrorPosition(long pos, IOException ex){
        this.errPos = pos;

        if(this.errPos >= 0L){
            if(ex == null) throw new NullPointerException();
            this.errEx = ex;
        }else{
            this.errEx = null;
        }

        return;
    }

    /**
     * 意図的な入力例外を起こすまで、
     * あと何バイト正常に読めるか設定値を返す。
     * @return 正常に読めるバイト数。
     */
    public long getErrorPosition(){
        long result = this.errPos;
        return result;
    }

    /**
     * 意図した入力エラー報告を投げる状況かチェックする。
     * @throws IOException 意図した入力エラー
     */
    protected void checkIOEx() throws IOException {
        if(this.errPos < 0L) return;
        if(this.errPos > 0L) return;

        IOException ex = this.errEx;
        this.errPos = -1L;
        this.errEx = null;

        throw ex;
    }

    /**
     * {@inheritDoc}
     * クローズの有無は外部からの観察が可能。
     * @throws IOException {@inheritDoc}
     * 設定状況によっては意図的な入力例外を投げる。
     */
    @Override
    public void close() throws IOException {
        checkIOEx();
        this.closed = true;
        return;
    }

    /**
     * クローズされた状況か判定する。
     * @return クローズ済みであればtrue
     */
    public boolean isClosed(){
        boolean result = this.closed;
        return result;
    }

    /**
     * {@inheritDoc}
     * @param bArray {@inheritDoc}
     * @param offset {@inheritDoc}
     * @param len {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public int read(byte[] bArray, int offset, int len) throws IOException {
        int result = 0;

        for(int pos = 0; pos < len; pos++){
            int iVal = read();
            if(iVal < 0){
                if(result == 0) result = -1;
                break;
            }
            bArray[offset + pos] = (byte)iVal;
            result++;
        }

        return result;
    }

    /**
     * {@inheritDoc}
     * @param bArray {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public int read(byte[] bArray) throws IOException {
        int result = read(bArray, 0, bArray.length);
        return result;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public int read() throws IOException {
        checkIOEx();

        Byte val = this.queue.poll();
        if(val == null) return  -1;

        int iVal = (int)( val.byteValue() );
        int result = iVal & MASK_08BIT;

        if(this.errPos > 0){
            this.errPos--;
        }

        return result;
    }

    /**
     * 未出力のバイト列長を返す。
     * @return バイト列長
     */
    public int getQueueLength(){
        int result = this.queue.size();
        return result;
    }

    /**
     * 未出力バイト列を空にする。
     */
    public void clearQueue(){
        this.queue.clear();
        return;
    }

    /**
     * 未出力バイト列にデータを追加する。
     * @param bArray 配列
     * @param offset 開始オフセット
     * @param len バイト長
     */
    private void queueByteArrayImpl(byte[] bArray, int offset, int len){
        for(int pos = 0; pos < len; pos++){
            byte bVal = bArray[offset + pos];
            this.queue.add(bVal);
        }
        return;
    }

    /**
     * 未出力バイト列にデータを追加する。
     * @param bArray 配列
     * @param offset 開始オフセット
     * @param len バイト長
     */
    public void queueByteArray(byte[] bArray, int offset, int len){
        queueByteArrayImpl(bArray, offset, len);
        return;
    }

    /**
     * 未出力バイト列にデータを追加する。
     * @param bArray 配列
     */
    public void queueByteArray(byte[] bArray){
        for(byte bVal : bArray){
            this.queue.add(bVal);
        }
        return;
    }

    /**
     * 未出力バイト列にデータを追加する。
     * @param bArray byte型可変引数
     */
    public void queueByte(byte... bArray){
        queueByteArray(bArray);
        return;
    }

    /**
     * 未出力バイト列にデータを追加する。
     * @param iArray int型可変引数。byte値に収まる要素値でなければならない。
     * @throws IllegalArgumentException byte値に収まらない値が指定された。
     */
    public void queueByte(int... iArray) throws IllegalArgumentException{
        for(int iVal : iArray){
            if(iVal < 0x00 || 0xff < iVal){
                throw new IllegalArgumentException();
            }
            byte bVal = (byte) iVal;
            this.queue.add(bVal);
        }
        return;
    }

}
