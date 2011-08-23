/*
 * camera information exporter
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.model.binio;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import jp.sourceforge.mikutoga.binio.BinaryExporter;
import jp.sourceforge.mikutoga.math.MkPos3D;
import jp.sourceforge.mikutoga.vmd.model.BezierParam;
import jp.sourceforge.mikutoga.vmd.model.CameraMotion;
import jp.sourceforge.mikutoga.vmd.model.CameraRotation;
import jp.sourceforge.mikutoga.vmd.model.PosCurve;
import jp.sourceforge.mikutoga.vmd.model.VmdMotion;

/**
 * カメラ情報のエクスポーター。
 */
class CameraExporter extends BinaryExporter {

    /**
     * コンストラクタ。
     * @param stream 出力ストリーム
     */
    CameraExporter(OutputStream stream){
        super(stream);
        return;
    }


    /**
     * カメラモーション情報を出力する。
     * @param motion モーションデータ
     * @throws IOException 出力エラー
     */
    void dumpCameraMotion(VmdMotion motion) throws IOException{
        List<CameraMotion> list = motion.getCameraMotionList();

        int count = list.size();
        dumpInt(count);

        for(CameraMotion cameraMotion : list){
            int frame = cameraMotion.getFrameNumber();
            dumpInt(frame);

            float range = cameraMotion.getRange();
            dumpFloat(range);

            MkPos3D targetPos = cameraMotion.getCameraTarget();
            dumpFloat((float) targetPos.getXpos());
            dumpFloat((float) targetPos.getYpos());
            dumpFloat((float) targetPos.getZpos());

            CameraRotation rotation = cameraMotion.getCameraRotation();
            dumpFloat(rotation.getLatitude());
            dumpFloat(rotation.getLongitude());
            dumpFloat(rotation.getRoll());

            dumpCameraCurve(cameraMotion);

            dumpInt(cameraMotion.getProjectionAngle());

            byte perspectiveSwitch;
            if(cameraMotion.hasPerspective()) perspectiveSwitch = 0x00;
            else                              perspectiveSwitch = 0x01;
            dumpByte(perspectiveSwitch);
        }

        return;
    }

    /**
     * カメラ補間情報を出力する。
     * @param cameraMotion モーションデータ
     * @throws IOException 出力エラー
     */
    private void dumpCameraCurve(CameraMotion cameraMotion)
            throws IOException{
        PosCurve posCurve = cameraMotion.getTargetPosCurve();
        BezierParam xCurve = posCurve.getIntpltXpos();
        BezierParam yCurve = posCurve.getIntpltYpos();
        BezierParam zCurve = posCurve.getIntpltZpos();
        dumpCameraBezier(xCurve);
        dumpCameraBezier(yCurve);
        dumpCameraBezier(zCurve);

        BezierParam rotCurve   = cameraMotion.getIntpltRotation();
        BezierParam rangeCurve = cameraMotion.getIntpltRange();
        BezierParam projCurve  = cameraMotion.getIntpltProjection();
        dumpCameraBezier(rotCurve);
        dumpCameraBezier(rangeCurve);
        dumpCameraBezier(projCurve);

        return;
    }

    /**
     * ベジェ曲線情報を出力する。
     * @param bezier ベジェ曲線
     * @throws IOException 出力エラー
     */
    private void dumpCameraBezier(BezierParam bezier) throws IOException{
        dumpByte(bezier.getP1x());
        dumpByte(bezier.getP2x());
        dumpByte(bezier.getP1y());
        dumpByte(bezier.getP2y());

        return;
    }

}
