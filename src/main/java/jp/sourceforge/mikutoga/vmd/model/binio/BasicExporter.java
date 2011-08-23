/*
 * bone motion & morph exporter
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.model.binio;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import jp.sourceforge.mikutoga.binio.BinaryExporter;
import jp.sourceforge.mikutoga.binio.IllegalTextExportException;
import jp.sourceforge.mikutoga.math.MkPos3D;
import jp.sourceforge.mikutoga.math.MkQuat;
import jp.sourceforge.mikutoga.vmd.VmdConst;
import jp.sourceforge.mikutoga.vmd.model.BezierParam;
import jp.sourceforge.mikutoga.vmd.model.BoneMotion;
import jp.sourceforge.mikutoga.vmd.model.MorphMotion;
import jp.sourceforge.mikutoga.vmd.model.NamedListMap;
import jp.sourceforge.mikutoga.vmd.model.PosCurve;
import jp.sourceforge.mikutoga.vmd.model.VmdMotion;

/**
 * ボーンモーション及びモーフ情報のエクスポーター。
 */
class BasicExporter extends BinaryExporter {

    private static final int BZ_SIZE = 4;           // 4byte Bezier parameter
    private static final int BZXYZR_SIZE = BZ_SIZE * 4; // XYZR Bezier
    private static final int BZ_REDUNDANT = 4;          // redundant spare
    private static final int BZTOTAL_SIZE = BZXYZR_SIZE * BZ_REDUNDANT;

    private static final byte[] FDFILLER =
        { (byte)0x00, (byte)0xfd };


    private final byte[] motionIntplt = new byte[BZTOTAL_SIZE];


    /**
     * コンストラクタ。
     * @param stream 出力ストリーム
     */
    BasicExporter(OutputStream stream){
        super(stream);
        return;
    }


    /**
     * ボーンモーション情報を出力する。
     * @param motion モーションデータ
     * @throws IOException 出力エラー
     * @throws IllegalTextExportException 不正なボーン名の出現
     */
    void dumpBoneMotion(VmdMotion motion)
            throws IOException, IllegalTextExportException{
        NamedListMap<BoneMotion> map = motion.getBonePartMap();
        List<String> nameList = map.getNames();

        List<BoneMotion> bmotionList = new LinkedList<BoneMotion>();

        int count = 0;
        for(String name : nameList){
            List<BoneMotion> namedList = map.getNamedList(name);
            for(BoneMotion boneMotion : namedList){
                bmotionList.add(boneMotion);
                count++;
            }
        }
        dumpInt(count);

        for(BoneMotion boneMotion : bmotionList){
            String boneName = boneMotion.getBoneName();
            dumpFixedW31j(boneName, VmdConst.BONENAME_MAX, FDFILLER);

            int frame = boneMotion.getFrameNumber();
            dumpInt(frame);

            MkPos3D position = boneMotion.getPosition();
            dumpBonePosition(position);

            MkQuat rotation = boneMotion.getRotation();
            dumpBoneRotation(rotation);

            dumpBoneInterpolation(boneMotion);
        }

        return;
    }

    /**
     * ボーン位置情報を出力する。
     * @param position ボーン位置情報
     * @throws IOException 出力エラー
     */
    private void dumpBonePosition(MkPos3D position)
            throws IOException{
        float xPos = (float) position.getXpos();
        float yPos = (float) position.getYpos();
        float zPos = (float) position.getZpos();

        dumpFloat(xPos);
        dumpFloat(yPos);
        dumpFloat(zPos);

        return;
    }

    /**
     * ボーン回転情報を出力する。
     * @param rotation ボーン回転情報
     * @throws IOException 出力エラー
     */
    private void dumpBoneRotation(MkQuat rotation)
            throws IOException{
        float qx = (float) rotation.getQ1();
        float qy = (float) rotation.getQ2();
        float qz = (float) rotation.getQ3();
        float qw = (float) rotation.getQW();

        dumpFloat(qx);
        dumpFloat(qy);
        dumpFloat(qz);
        dumpFloat(qw);

        return;
    }

    /**
     * ボーンモーションの補間情報を出力する。
     * @param boneMotion ボーンモーション
     * @throws IOException 出力エラー
     */
    private void dumpBoneInterpolation(BoneMotion boneMotion)
            throws IOException{
        PosCurve posCurve = boneMotion.getPosCurve();
        BezierParam xCurve = posCurve.getIntpltXpos();
        BezierParam yCurve = posCurve.getIntpltYpos();
        BezierParam zCurve = posCurve.getIntpltZpos();
        BezierParam rCurve = boneMotion.getIntpltRotation();

        int idx = 0;

        this.motionIntplt[idx++] = xCurve.getP1x();
        this.motionIntplt[idx++] = yCurve.getP1x();
        this.motionIntplt[idx++] = zCurve.getP1x();
        this.motionIntplt[idx++] = rCurve.getP1x();

        this.motionIntplt[idx++] = xCurve.getP1y();
        this.motionIntplt[idx++] = yCurve.getP1y();
        this.motionIntplt[idx++] = zCurve.getP1y();
        this.motionIntplt[idx++] = rCurve.getP1y();

        this.motionIntplt[idx++] = xCurve.getP2x();
        this.motionIntplt[idx++] = yCurve.getP2x();
        this.motionIntplt[idx++] = zCurve.getP2x();
        this.motionIntplt[idx++] = rCurve.getP2x();

        this.motionIntplt[idx++] = xCurve.getP2y();
        this.motionIntplt[idx++] = yCurve.getP2y();
        this.motionIntplt[idx++] = zCurve.getP2y();
        this.motionIntplt[idx++] = rCurve.getP2y();

        assert idx == BZXYZR_SIZE;

        redundantCopy();

        dumpByteArray(this.motionIntplt);

        return;
    }

    /**
     * 補間情報冗長部の組み立て。
     */
    private void redundantCopy(){
        int lack = 1;
        for(int ct = 1; ct < BZ_REDUNDANT; ct++){
            int sourceIdx = 0 + lack;
            int targetIdx = BZXYZR_SIZE * ct;
            int span = BZXYZR_SIZE - lack;

            System.arraycopy(this.motionIntplt, sourceIdx,
                             this.motionIntplt, targetIdx,
                             span );

            int onePos = targetIdx + span;
            this.motionIntplt[onePos] = (byte) 0x01;

            int zeroPosStart = onePos + 1;
            int zeroPosEnd = targetIdx + BZXYZR_SIZE;
            for(int idx = zeroPosStart; idx < zeroPosEnd; idx++){
                this.motionIntplt[idx] = (byte) 0x00;
            }

            lack++;
        }

        return;
    }

    /**
     * モーフ情報を出力する。
     * @param motion モーションデータ
     * @throws IOException 出力エラー
     * @throws IllegalTextExportException 不正なモーフ名の出現
     */
    void dumpMorphMotion(VmdMotion motion)
            throws IOException, IllegalTextExportException{
        NamedListMap<MorphMotion> map = motion.getMorphPartMap();
        List<String> nameList = map.getNames();

        List<MorphMotion> morphList = new LinkedList<MorphMotion>();

        int count = 0;
        for(String name : nameList){
            List<MorphMotion> namedList = map.getNamedList(name);
            for(MorphMotion morphMotion : namedList){
                morphList.add(morphMotion);
                count++;
            }
        }
        dumpInt(count);

        for(MorphMotion morphMotion : morphList){
            String morphName = morphMotion.getMorphName();
            dumpFixedW31j(morphName, VmdConst.MORPHNAME_MAX, FDFILLER);

            int frame = morphMotion.getFrameNumber();
            dumpInt(frame);

            float flex = morphMotion.getFlex();
            dumpFloat(flex);
        }

        return;
    }

}
