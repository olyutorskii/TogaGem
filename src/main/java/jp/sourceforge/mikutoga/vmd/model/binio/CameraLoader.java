/*
 * camera information builder
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.model.binio;

import java.util.List;
import jp.sourceforge.mikutoga.parser.MmdFormatException;
import jp.sourceforge.mikutoga.parser.ParseStage;
import jp.sourceforge.mikutoga.vmd.model.BezierParam;
import jp.sourceforge.mikutoga.vmd.model.CameraMotion;
import jp.sourceforge.mikutoga.vmd.model.CameraRotation;
import jp.sourceforge.mikutoga.vmd.model.PosCurve;
import jp.sourceforge.mikutoga.vmd.model.VmdMotion;
import jp.sourceforge.mikutoga.vmd.parser.VmdCameraHandler;

/**
 * カメラ情報のビルダ。
 */
class CameraLoader implements VmdCameraHandler {

    private final List<CameraMotion> cameraMotionList;

    private CameraMotion currentCameraMotion;


    /**
     * コンストラクタ。
     * @param vmdMotion モーションデータ。
     */
    CameraLoader(VmdMotion vmdMotion){
        super();
        this.cameraMotionList = vmdMotion.getCameraMotionList();
        this.currentCameraMotion = null;
        return;
    }


    /**
     * {@inheritDoc}
     * @param stage {@inheritDoc}
     * @param loops {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void loopStart(ParseStage stage, int loops)
            throws MmdFormatException{
        this.currentCameraMotion = new CameraMotion();
        return;
    }

    /**
     * {@inheritDoc}
     * @param stage {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void loopNext(ParseStage stage)
            throws MmdFormatException{
        this.cameraMotionList.add(this.currentCameraMotion);
        this.currentCameraMotion = new CameraMotion();
        return;
    }

    /**
     * {@inheritDoc}
     * @param stage {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void loopEnd(ParseStage stage)
            throws MmdFormatException{
        this.currentCameraMotion = null;
        return;
    }

    /**
     * {@inheritDoc}
     * @param keyFrameNo {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void vmdCameraMotion(int keyFrameNo) throws MmdFormatException{
        this.currentCameraMotion.setFrameNumber(keyFrameNo);
        return;
    }

    /**
     * {@inheritDoc}
     * @param xPos {@inheritDoc}
     * @param yPos {@inheritDoc}
     * @param zPos {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void vmdCameraPosition(float xPos, float yPos, float zPos)
            throws MmdFormatException{
        this.currentCameraMotion.getCameraTarget()
                .setPosition(xPos, yPos, zPos);
        return;
    }

    /**
     * {@inheritDoc}
     * @param latitude {@inheritDoc}
     * @param longitude {@inheritDoc}
     * @param roll {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void vmdCameraRotation(float latitude,
                                    float longitude,
                                    float roll )
            throws MmdFormatException{
        CameraRotation rotation =
                this.currentCameraMotion.getCameraRotation();
        rotation.setLatitude(latitude);
        rotation.setLongitude(longitude);
        rotation.setRoll(roll);
        return;
    }

    /**
     * {@inheritDoc}
     * @param range {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void vmdCameraRange(float range) throws MmdFormatException{
        this.currentCameraMotion.setRange(range);
        return;
    }

    /**
     * {@inheritDoc}
     * @param angle {@inheritDoc}
     * @param hasPerspective {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void vmdCameraProjection(int angle, boolean hasPerspective)
            throws MmdFormatException{
        this.currentCameraMotion.setProjectionAngle(angle);
        this.currentCameraMotion.setPerspectiveMode(hasPerspective);
        return;
    }

    /**
     * {@inheritDoc}
     * @param p1x {@inheritDoc}
     * @param p1y {@inheritDoc}
     * @param p2x {@inheritDoc}
     * @param p2y {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void vmdCameraIntpltXpos(byte p1x, byte p1y, byte p2x, byte p2y)
            throws MmdFormatException{
        PosCurve posCurve = this.currentCameraMotion.getTargetPosCurve();
        BezierParam bezier = posCurve.getIntpltXpos();
        bezier.setP1(p1x, p1y);
        bezier.setP2(p2x, p2y);
        return;
    }

    /**
     * {@inheritDoc}
     * @param p1x {@inheritDoc}
     * @param p1y {@inheritDoc}
     * @param p2x {@inheritDoc}
     * @param p2y {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void vmdCameraIntpltYpos(byte p1x, byte p1y, byte p2x, byte p2y)
            throws MmdFormatException{
        PosCurve posCurve = this.currentCameraMotion.getTargetPosCurve();
        BezierParam bezier = posCurve.getIntpltYpos();
        bezier.setP1(p1x, p1y);
        bezier.setP2(p2x, p2y);
        return;
    }

    /**
     * {@inheritDoc}
     * @param p1x {@inheritDoc}
     * @param p1y {@inheritDoc}
     * @param p2x {@inheritDoc}
     * @param p2y {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void vmdCameraIntpltZpos(byte p1x, byte p1y, byte p2x, byte p2y)
            throws MmdFormatException{
        PosCurve posCurve = this.currentCameraMotion.getTargetPosCurve();
        BezierParam bezier = posCurve.getIntpltZpos();
        bezier.setP1(p1x, p1y);
        bezier.setP2(p2x, p2y);
        return;
    }

    /**
     * {@inheritDoc}
     * @param p1x {@inheritDoc}
     * @param p1y {@inheritDoc}
     * @param p2x {@inheritDoc}
     * @param p2y {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void vmdCameraIntpltRotation(byte p1x, byte p1y,
                                           byte p2x, byte p2y )
            throws MmdFormatException{
        BezierParam bezier = this.currentCameraMotion.getIntpltRotation();
        bezier.setP1(p1x, p1y);
        bezier.setP2(p2x, p2y);
        return;
    }

    /**
     * {@inheritDoc}
     * @param p1x {@inheritDoc}
     * @param p1y {@inheritDoc}
     * @param p2x {@inheritDoc}
     * @param p2y {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void vmdCameraIntpltRange(byte p1x, byte p1y, byte p2x, byte p2y)
            throws MmdFormatException{
        BezierParam bezier = this.currentCameraMotion.getIntpltRange();
        bezier.setP1(p1x, p1y);
        bezier.setP2(p2x, p2y);
        return;
    }

    /**
     * {@inheritDoc}
     * @param p1x {@inheritDoc}
     * @param p1y {@inheritDoc}
     * @param p2x {@inheritDoc}
     * @param p2y {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void vmdCameraIntpltProjection(byte p1x, byte p1y,
                                              byte p2x, byte p2y )
            throws MmdFormatException{
        BezierParam bezier = this.currentCameraMotion.getIntpltProjection();
        bezier.setP1(p1x, p1y);
        bezier.setP2(p2x, p2y);
        return;
    }

}
