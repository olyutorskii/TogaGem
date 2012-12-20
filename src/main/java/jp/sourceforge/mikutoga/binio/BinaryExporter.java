/*
 * binary data exporter
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.binio;

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
 * <p>デフォルトではリトルエンディアン形式で出力される。
 */
public class BinaryExporter implements Closeable, Flushable{

    private static final Charset CS_UTF16LE = Charset.forName("UTF-16LE");
    private static final Charset CS_WIN31J  = Charset.forName("windows-31j");

    private static final String ERRMSG_ILLENC    = "illegal encoding";
    private static final String ERRMSG_TOOLONGTX =
              "too long text: "
            + "text \"{0}\" needs {1}bytes encoded but limit={2}bytes";

    private static final int MASK_16 = 0xffff;
    private static final int MASK_8  =   0xff;

    private static final int BYTES_SHORT  = Short   .SIZE / Byte.SIZE;
    private static final int BYTES_INT    = Integer .SIZE / Byte.SIZE;
    private static final int BYTES_LONG   = Long    .SIZE / Byte.SIZE;
    private static final int BYTES_FLOAT  = Float   .SIZE / Byte.SIZE;
    private static final int BYTES_DOUBLE = Double  .SIZE / Byte.SIZE;

    private static final int BUFSZ_PRIM = BYTES_DOUBLE;


    private final OutputStream ostream;

    private final byte[] barray;

    private final TextExporter texporter_w31j;
    private final TextExporter texporter_u16le;
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

        this.texporter_w31j  = new TextExporter(CS_WIN31J);
        this.texporter_u16le = new TextExporter(CS_UTF16LE);
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
     * short値をリトルエンディアンで出力する。
     * @param sVal short値
     * @return this自身
     * @throws IOException 出力エラー
     */
    @SuppressWarnings("PMD.AvoidUsingShortType")
    public BinaryExporter dumpLeShort(short sVal) throws IOException{
        this.barray[0] = (byte)( (sVal >>  0) & MASK_8 );
        this.barray[1] = (byte)( (sVal >>  8) & MASK_8 );

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
        short sVal = (short)(iVal & MASK_16);
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
        this.barray[0] = (byte)( (iVal >>  0) & MASK_8 );
        this.barray[1] = (byte)( (iVal >>  8) & MASK_8 );
        this.barray[2] = (byte)( (iVal >> 16) & MASK_8 );
        this.barray[3] = (byte)( (iVal >> 24) & MASK_8 );

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
        this.barray[0] = (byte)( (lVal >>  0) & 0xffL );
        this.barray[1] = (byte)( (lVal >>  8) & 0xffL );
        this.barray[2] = (byte)( (lVal >> 16) & 0xffL );
        this.barray[3] = (byte)( (lVal >> 24) & 0xffL );
        this.barray[4] = (byte)( (lVal >> 32) & 0xffL );
        this.barray[5] = (byte)( (lVal >> 40) & 0xffL );
        this.barray[6] = (byte)( (lVal >> 48) & 0xffL );
        this.barray[7] = (byte)( (lVal >> 56) & 0xffL );

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

        this.barray[0] = (byte)( (rawiVal >>  0) & MASK_8 );
        this.barray[1] = (byte)( (rawiVal >>  8) & MASK_8 );
        this.barray[2] = (byte)( (rawiVal >> 16) & MASK_8 );
        this.barray[3] = (byte)( (rawiVal >> 24) & MASK_8 );

        this.ostream.write(this.barray, 0, BYTES_FLOAT);

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

        this.barray[0] = (byte)( (rawlVal >>  0) & MASK_8 );
        this.barray[1] = (byte)( (rawlVal >>  8) & MASK_8 );
        this.barray[2] = (byte)( (rawlVal >> 16) & MASK_8 );
        this.barray[3] = (byte)( (rawlVal >> 24) & MASK_8 );
        this.barray[4] = (byte)( (rawlVal >> 32) & MASK_8 );
        this.barray[5] = (byte)( (rawlVal >> 40) & MASK_8 );
        this.barray[6] = (byte)( (rawlVal >> 48) & MASK_8 );
        this.barray[7] = (byte)( (rawlVal >> 56) & MASK_8 );

        this.ostream.write(this.barray, 0, BYTES_DOUBLE);

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

        dumpLeInt(encodedSize);

        this.xos.writeTo(this.ostream);
        int xferred = this.xos.size();
        assert xferred == encodedSize;

        return xferred;
    }

}
