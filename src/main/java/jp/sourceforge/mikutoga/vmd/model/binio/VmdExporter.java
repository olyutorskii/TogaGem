/*
 * vmd exporter
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.model.binio;

import java.io.IOException;
import java.io.OutputStream;
import jp.sourceforge.mikutoga.binio.BinaryExporter;
import jp.sourceforge.mikutoga.binio.IllegalTextExportException;
import jp.sourceforge.mikutoga.vmd.IllegalVmdDataException;
import jp.sourceforge.mikutoga.vmd.VmdConst;
import jp.sourceforge.mikutoga.vmd.model.VmdMotion;

/**
 * VMDファイルのエクスポーター。
 */
public class VmdExporter extends BinaryExporter {

    private static final byte[] HEADFILLER = {
        (byte)'J',
        (byte)'K',
        (byte)'L',
        (byte)'M',
    };

    private static final byte[] FDFILLER =
        { (byte)0x00, (byte)0xfd };


    private final BasicExporter    basicExporter;
    private final CameraExporter   cameraExporter;
    private final LightingExporter lightingExporter;


    /**
     * コンストラクタ。
     * @param stream 出力ストリーム
     */
    public VmdExporter(OutputStream stream){
        super(stream);

        this.basicExporter    = new BasicExporter(stream);
        this.cameraExporter   = new CameraExporter(stream);
        this.lightingExporter = new LightingExporter(stream);

        return;
    }

    /**
     * モーションデータをVMDファイル形式で出力する。
     * <p>異常時には出力データのフラッシュが試みられる。
     * @param motion モーションデータ
     * @throws IOException 出力エラー
     * @throws IllegalVmdDataException モーションデータに不備が発見された
     */
    public void dumpVmdMotion(VmdMotion motion)
            throws IOException, IllegalVmdDataException{
        try{
            dumpVmdMotionImpl(motion);
        }finally{
            flush();
        }

        return;
    }

    /**
     * モーションデータをVMDファイル形式で出力する。
     * @param motion モーションデータ
     * @throws IOException 出力エラー
     * @throws IllegalVmdDataException モーションデータに不備が発見された
     */
    private void dumpVmdMotionImpl(VmdMotion motion)
            throws IOException, IllegalVmdDataException{
        dumpHeader();

        try{
            dumpModelName(motion);
            this.basicExporter.dumpBoneMotion(motion);
            this.basicExporter.dumpMorphMotion(motion);
        }catch(IllegalTextExportException e){
            throw new IllegalVmdDataException(e);
        }

        this.cameraExporter.dumpCameraMotion(motion);
        this.lightingExporter.dumpLuminousMotion(motion);
        this.lightingExporter.dumpShadowMotion(motion);

        return;
    }

    /**
     * ヘッダ情報を出力する。
     * @throws IOException 出力エラー
     */
    private void dumpHeader() throws IOException{
        byte[] header = VmdConst.createMagicHeader();
        dumpByteArray(header);
        dumpByteArray(HEADFILLER);

        assert header.length + HEADFILLER.length == VmdConst.HEADER_LENGTH;

        return;
    }

    /**
     * モデル名を出力する。
     * <p>演出データのモデル名には
     * 便宜的に{@link VmdConst.MODELNAME_STAGEACT}が使われる。
     * @param motion モーションデータ
     * @throws IOException 出力エラー
     * @throws IllegalTextExportException 不正なモデル名の出現
     */
    private void dumpModelName(VmdMotion motion)
            throws IOException, IllegalTextExportException{
        String modelName = motion.getModelName();
        if(modelName == null) modelName = VmdConst.MODELNAME_STAGEACT;

        dumpFixedW31j(modelName, VmdConst.MODELNAME_MAX, FDFILLER);

        return;
    }

}
