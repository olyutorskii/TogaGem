/*
 * binary data exporter
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.binio;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.text.MessageFormat;

/**
 * バイナリデータの出力を行う汎用エクスポーター。
 * <p>デフォルトではリトルエンディアン形式で出力される。
 */
public class BinaryExporter {

    private static final Charset CS_UTF16LE = Charset.forName("UTF-16LE");
    private static final Charset CS_WIN31J  = Charset.forName("windows-31j");

    private static final String ERRMSG_ILLENC    = "illegal encoding";
    private static final String ERRMSG_TOOLONGTX =
              "too long text: "
            + "text \"{0}\" needs {1}bytes encoded but limit={2}bytes";

    private static final int MASK_16 = 0xffff;

    private static final int BYTES_SHORT  = Short   .SIZE / Byte.SIZE;
    private static final int BYTES_INT    = Integer .SIZE / Byte.SIZE;
    private static final int BYTES_LONG   = Long    .SIZE / Byte.SIZE;
    private static final int BYTES_FLOAT  = Float   .SIZE / Byte.SIZE;
    private static final int BYTES_DOUBLE = Double  .SIZE / Byte.SIZE;

    private static final int BUFSZ_PRIM = BYTES_DOUBLE;


    private final OutputStream ostream;

    private final byte[] barray;
    private final ByteBuffer primbuf;

    private final TextExporter texporter_w31j;
    private final TextExporter texporter_u16le;
    private final FeedableOutputStream xos;


    /**
     * コンストラクタ。
     * @param ostream 出力ストリーム
     * @throws NullPointerException 引数がnull
     */
    public BinaryExporter(OutputStream ostream) throws NullPointerException{
        super();

        if(ostream == null) throw new NullPointerException();
        this.ostream = ostream;

        this.barray = new byte[BUFSZ_PRIM];
        this.primbuf = ByteBuffer.wrap(this.barray);
        this.primbuf.order(ByteOrder.LITTLE_ENDIAN);

        this.primbuf.clear();

        this.texporter_w31j  = new TextExporter(CS_WIN31J);
        this.texporter_u16le = new TextExporter(CS_UTF16LE);
        this.xos = new FeedableOutputStream();

        return;
    }


    /**
     * バイトオーダーを設定する。
     * @param order バイトオーダー
     */
    public void setOrder(ByteOrder order){
        this.primbuf.order(order);
        return;
    }

    /**
     * 設定されたバイトオーダーを返す。
     * @return 設定されたバイトオーダー
     */
    public ByteOrder getOrder(){
        return this.primbuf.order();
    }

    /**
     * 出力ストリームを閉じる。
     * @throws IOException 出力エラー
     */
    public void close() throws IOException{
        this.ostream.close();
        return;
    }

    /**
     * 出力をフラッシュする。
     * I/O効率とデバッグ効率のバランスを考え、ご利用は計画的に。
     * @return this
     * @throws IOException 出力エラー
     */
    public BinaryExporter flush() throws IOException{
        this.ostream.flush();
        return this;
    }

    /**
     * byte値を出力する。
     * @param bVal byte値
     * @return this自身
     * @throws IOException 出力エラー
     */
    public BinaryExporter dumpByte(byte bVal) throws IOException{
        this.ostream.write((int)bVal);
        return this;
    }

    /**
     * byte値を出力する。
     * @param iVal int値。上位24bitは捨てられる。
     * @return this自身
     * @throws IOException 出力エラー
     */
    public BinaryExporter dumpByte(int iVal) throws IOException{
        this.ostream.write(iVal);
        return this;
    }

    /**
     * byte型配列を出力する。
     * @param array 配列
     * @return this自身
     * @throws IOException 出力エラー
     */
    public BinaryExporter dumpByteArray(byte[] array)
            throws IOException{
        dumpByteArray(array, 0, array.length);
        return this;
    }

    /**
     * byte型配列の部分列を出力する。
     * @param array 配列
     * @param offset 出力開始位置
     * @param length 出力バイト数
     * @return this自身
     * @throws IOException 出力エラー
     */
    public BinaryExporter dumpByteArray(byte[] array, int offset, int length)
            throws IOException{
        this.ostream.write(array, offset, length);
        return this;
    }

    /**
     * 内部バッファの先頭を出力する。
     * @param length 出力バイト数
     * @throws IOException 出力エラー
     */
    private void dumpBuffer(int length) throws IOException{
        this.ostream.write(this.barray, 0, length);
        return;
    }

    /**
     * short値を出力する。
     * @param sVal short値
     * @return this自身
     * @throws IOException 出力エラー
     */
    @SuppressWarnings("PMD.AvoidUsingShortType")
    public BinaryExporter dumpShort(short sVal) throws IOException{
        this.primbuf.putShort(0, sVal);
        dumpBuffer(BYTES_SHORT);
        return this;
    }

    /**
     * short値を出力する。
     * @param iVal int値。上位16bitは捨てられる。
     * @return this自身
     * @throws IOException 出力エラー
     */
    @SuppressWarnings("PMD.AvoidUsingShortType")
    public BinaryExporter dumpShort(int iVal) throws IOException{
        short sVal = (short)(iVal & MASK_16);
        dumpShort(sVal);
        return this;
    }

    /**
     * int値を出力する。
     * @param iVal int値
     * @return this自身
     * @throws IOException 出力エラー
     */
    public BinaryExporter dumpInt(int iVal) throws IOException{
        this.primbuf.putInt(0, iVal);
        dumpBuffer(BYTES_INT);
        return this;
    }

    /**
     * long値を出力する。
     * @param lVal long値
     * @return this自身
     * @throws IOException 出力エラー
     */
    public BinaryExporter dumpLong(long lVal) throws IOException{
        this.primbuf.putLong(0, lVal);
        dumpBuffer(BYTES_LONG);
        return this;
    }

    /**
     * float値を出力する。
     * @param fVal float値
     * @return this自身
     * @throws IOException 出力エラー
     */
    public BinaryExporter dumpFloat(float fVal) throws IOException{
        this.primbuf.putFloat(0, fVal);
        dumpBuffer(BYTES_FLOAT);
        return this;
    }

    /**
     * double値を出力する。
     * @param dVal double値
     * @return this自身
     * @throws IOException 出力エラー
     */
    public BinaryExporter dumpDouble(double dVal) throws IOException{
        this.primbuf.putDouble(0, dVal);
        dumpBuffer(BYTES_DOUBLE);
        return this;
    }

    /**
     * 詰め物パディングを出力する。
     * @param filler byte型配列によるパディングデータの並び。
     * <p>指定パディング長より長い部分は出力されない。
     * 指定パディング長に満たない場合は最後の要素が繰り返し出力される。
     * <p>配列長が0の場合は何も出力されない。
     * @param fillerLength パディング長。
     * <p>パディング長が0以下の場合は何も出力されない。
     * @return this
     * @throws IOException 出力エラー
     */
    public BinaryExporter dumpFiller(byte[] filler, int fillerLength)
            throws IOException {
        if(filler.length <= 0 || fillerLength <= 0){
            return this;
        }

        byte lastData = filler[filler.length - 1];

        int fillerIdx = 0;
        for(int remain = fillerLength; remain > 0; remain--){
            byte bVal;
            if(fillerIdx < filler.length) bVal = filler[fillerIdx++];
            else                          bVal = lastData;
            dumpByte(bVal);
        }

        return this;
    }

    /**
     * Windows31J文字列をを固定バイト長で出力する。
     * 固定バイト長に満たない箇所はパディングデータが詰められる。
     * @param text テキスト
     * @param fixedLength 固定バイト長。0以下の場合は無制限。
     * @param filler 詰め物パディングデータ
     * @return this
     * @throws IOException 出力エラー
     * @throws IllegalTextExportException テキスト出力エラー。
     * 出力が固定長を超えようとした、
     * もしくは不正なエンコードが行われたかのいずれか。
     */
    public BinaryExporter dumpFixedW31j(CharSequence text,
                                          int fixedLength,
                                          byte[] filler )
            throws IOException, IllegalTextExportException{
        this.xos.reset();

        int encodedSize;
        try{
            encodedSize =
                    this.texporter_w31j.encodeToByteStream(text, this.xos);
        }catch(CharacterCodingException e){
            throw new IllegalTextExportException(ERRMSG_ILLENC, e);
        }

        if( 0 < fixedLength && fixedLength < encodedSize ){
            String message =
                    MessageFormat.format(ERRMSG_TOOLONGTX,
                                         text, encodedSize, fixedLength);
            throw new IllegalTextExportException(message);
        }

        int xferred = this.xos.feedStored(this.ostream);

        int remain = fixedLength - xferred;
        if(remain > 0){
            dumpFiller(filler, remain);
        }

        return this;
    }

    /**
     * UTF16-LE文字列をホレリス形式で出力する。
     * UTF16-LEエンコード結果のバイト長を
     * 4byte整数としてリトルエンディアンで出力した後に、
     * エンコード結果のバイト列が出力される。
     * @param text 文字列
     * @return エンコードバイト列長
     * @throws IOException 出力エラー
     * @throws IllegalTextExportException テキスト出力エラー。
     * 出力が固定長を超えようとした、
     * もしくは不正なエンコードが行われたかのいずれか。
     */
    public int dumpHollerithUtf16LE(CharSequence text)
            throws IOException, IllegalTextExportException{
        this.xos.reset();

        int encodedSize;
        try{
            encodedSize =
                    this.texporter_u16le.encodeToByteStream(text, this.xos);
        }catch(CharacterCodingException e){
            throw new IllegalTextExportException(ERRMSG_ILLENC, e);
        }

        dumpInt(encodedSize);

        int xferred = this.xos.feedStored(this.ostream);
        assert xferred == encodedSize;

        return xferred;
    }

}
