/*
 * binary data exporter
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sfjp.mikutoga.bin.export;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.text.MessageFormat;

/**
 * バイナリデータの出力を行う汎用エクスポーター。
 *
 * <p>基本的にリトルエンディアン形式で出力される。
 */
public class BinaryExporter implements Closeable, Flushable{

    private static final Charset CS_UTF16LE = Charset.forName("UTF-16LE");
    private static final Charset CS_WIN31J  = Charset.forName("windows-31j");

    private static final String ERRMSG_ILLENC    = "illegal encoding";
    private static final String ERRMSG_TOOLONGTX =
              "too long text: "
            + "text \"{0}\" needs {1}bytes encoded but limit={2}bytes";

    private static final int BYTES_SHORT  = Short   .SIZE / Byte.SIZE;
    private static final int BYTES_INT    = Integer .SIZE / Byte.SIZE;
    private static final int BYTES_LONG   = Long    .SIZE / Byte.SIZE;
    private static final int BYTES_FLOAT  = Float   .SIZE / Byte.SIZE;
    private static final int BYTES_DOUBLE = Double  .SIZE / Byte.SIZE;

    private static final int BUFSZ_PRIM = BYTES_DOUBLE;

    private static final int IDX0 = 0;
    private static final int IDX1 = 1;
    private static final int IDX2 = 2;
    private static final int IDX3 = 3;
    private static final int IDX4 = 4;
    private static final int IDX5 = 5;
    private static final int IDX6 = 6;
    private static final int IDX7 = 7;

    private static final int SH00 =  0;
    private static final int SH08 =  8;
    private static final int SH16 = 16;
    private static final int SH24 = 24;
    private static final int SH32 = 32;
    private static final int SH40 = 40;
    private static final int SH48 = 48;
    private static final int SH56 = 56;

    static{
        assert BYTES_DOUBLE <= BUFSZ_PRIM;
        assert BYTES_FLOAT  <= BUFSZ_PRIM;
        assert BYTES_LONG   <= BUFSZ_PRIM;
        assert BYTES_INT    <= BUFSZ_PRIM;
        assert BYTES_SHORT  <= BUFSZ_PRIM;
    }


    private final OutputStream ostream;

    private final byte[] barray;

    private final TextExporter texporterW31j;
    private final TextExporter texporterU16le;
    private final ByteArrayOutputStream xos;


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

        this.texporterW31j  = new TextExporter(CS_WIN31J);
        this.texporterU16le = new TextExporter(CS_UTF16LE);
        this.xos = new ByteArrayOutputStream();

        return;
    }


    /**
     * 出力ストリームを閉じる。
     * @throws IOException 出力エラー
     */
    @Override
    public void close() throws IOException{
        this.ostream.close();
        return;
    }

    /**
     * 出力をフラッシュする。
     * I/O効率とデバッグ効率のバランスを考え、ご利用は計画的に。
     * @throws IOException 出力エラー
     */
    @Override
    public void flush() throws IOException{
        this.ostream.flush();
        return;
    }

    /**
     * byte値を出力する。
     * @param bVal byte値
     * @return this自身
     * @throws IOException 出力エラー
     */
    public BinaryExporter dumpByte(byte bVal) throws IOException{
        this.ostream.write(bVal);
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
        this.ostream.write(array);
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
     * short値をリトルエンディアンで出力する。
     * @param sVal short値
     * @return this自身
     * @throws IOException 出力エラー
     */
    @SuppressWarnings("PMD.AvoidUsingShortType")
    public BinaryExporter dumpLeShort(short sVal) throws IOException{
        this.barray[IDX0] = (byte)(sVal >>> SH00);
        this.barray[IDX1] = (byte)(sVal >>> SH08);

        this.ostream.write(this.barray, 0, BYTES_SHORT);

        return this;
    }

    /**
     * short値をリトルエンディアンで出力する。
     * @param iVal int値。上位16bitは捨てられる。
     * @return this自身
     * @throws IOException 出力エラー
     */
    @SuppressWarnings("PMD.AvoidUsingShortType")
    public BinaryExporter dumpLeShort(int iVal) throws IOException{
        short sVal = (short)iVal;
        dumpLeShort(sVal);
        return this;
    }

    /**
     * int値をリトルエンディアンで出力する。
     * @param iVal int値
     * @return this自身
     * @throws IOException 出力エラー
     */
    public BinaryExporter dumpLeInt(int iVal) throws IOException{
        this.barray[IDX0] = (byte)(iVal >>> SH00);
        this.barray[IDX1] = (byte)(iVal >>> SH08);
        this.barray[IDX2] = (byte)(iVal >>> SH16);
        this.barray[IDX3] = (byte)(iVal >>> SH24);

        this.ostream.write(this.barray, 0, BYTES_INT);

        return this;
    }

    /**
     * long値をリトルエンディアンで出力する。
     * @param lVal long値
     * @return this自身
     * @throws IOException 出力エラー
     */
    public BinaryExporter dumpLeLong(long lVal) throws IOException{
        this.barray[IDX0] = (byte)(lVal >>> SH00);
        this.barray[IDX1] = (byte)(lVal >>> SH08);
        this.barray[IDX2] = (byte)(lVal >>> SH16);
        this.barray[IDX3] = (byte)(lVal >>> SH24);
        this.barray[IDX4] = (byte)(lVal >>> SH32);
        this.barray[IDX5] = (byte)(lVal >>> SH40);
        this.barray[IDX6] = (byte)(lVal >>> SH48);
        this.barray[IDX7] = (byte)(lVal >>> SH56);

        this.ostream.write(this.barray, 0, BYTES_LONG);

        return this;
    }

    /**
     * float値をリトルエンディアンで出力する。
     * @param fVal float値
     * @return this自身
     * @throws IOException 出力エラー
     */
    public BinaryExporter dumpLeFloat(float fVal) throws IOException{
        int rawiVal = Float.floatToRawIntBits(fVal);
        dumpLeInt(rawiVal);
        return this;
    }

    /**
     * double値をリトルエンディアンで出力する。
     * @param dVal double値
     * @return this自身
     * @throws IOException 出力エラー
     */
    public BinaryExporter dumpLeDouble(double dVal) throws IOException{
        long rawlVal = Double.doubleToRawLongBits(dVal);
        dumpLeLong(rawlVal);
        return this;
    }

    /**
     * 詰め物パディングを出力する。
     *
     * @param filler byte型配列によるパディングデータの並び。
     *
     *     <p>指定パディング長より長い部分は出力されない。
     *     指定パディング長に満たない場合は
     *     最後の要素が繰り返し出力される。</p>
     *
     *     <p>配列長が0の場合は何も出力されない。</p>
     *
     * @param fillerLength パディング長。
     *
     *     <p>パディング長が0以下の場合は何も出力されない。</p>
     *
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
     *     出力が固定長を超えようとした、
     *     もしくは不正なエンコードが行われたかのいずれか。
     */
    public BinaryExporter dumpFixedW31j(CharSequence text,
                                          int fixedLength,
                                          byte[] filler )
            throws IOException, IllegalTextExportException{
        this.xos.reset();

        int encodedSize;
        try{
            encodedSize =
                    this.texporterW31j.encodeToByteStream(text, this.xos);
        }catch(CharacterCodingException e){
            throw new IllegalTextExportException(ERRMSG_ILLENC, e);
        }

        if( 0 < fixedLength && fixedLength < encodedSize ){
            String message =
                    MessageFormat.format(ERRMSG_TOOLONGTX,
                                         text, encodedSize, fixedLength);
            throw new IllegalTextExportException(message);
        }

        this.xos.writeTo(this.ostream);
        int xferred = this.xos.size();

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
     *     出力が固定長を超えようとした、
     *     もしくは不正なエンコードが行われたかのいずれか。
     */
    public int dumpHollerithUtf16LE(CharSequence text)
            throws IOException, IllegalTextExportException{
        this.xos.reset();

        int encodedSize;
        try{
            encodedSize =
                    this.texporterU16le.encodeToByteStream(text, this.xos);
        }catch(CharacterCodingException e){
            assert false;  // これはない
            throw new IllegalTextExportException(ERRMSG_ILLENC, e);
        }

        dumpLeInt(encodedSize);

        this.xos.writeTo(this.ostream);
        int xferred = this.xos.size();

        assert xferred == encodedSize;

        return xferred;
    }

}
