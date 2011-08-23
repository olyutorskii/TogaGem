/*
 * xml camera loader
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.model.xml;

import java.util.List;
import jp.sourceforge.mikutoga.math.MkPos3D;
import jp.sourceforge.mikutoga.vmd.model.BezierParam;
import jp.sourceforge.mikutoga.vmd.model.CameraMotion;
import jp.sourceforge.mikutoga.vmd.model.CameraRotation;
import jp.sourceforge.mikutoga.vmd.model.PosCurve;
import jp.sourceforge.mikutoga.vmd.model.VmdMotion;
import jp.sourceforge.mikutoga.xml.TogaXmlException;
import org.w3c.dom.Element;

/**
 * XMLによるカメラ制御データを読み取る。
 */
final class XmlCameraLoader {

    /**
     * 隠しコンストラクタ。
     */
    private XmlCameraLoader(){
        assert false;
        throw new AssertionError();
    }


    /**
     * カメラシーケンスを読み込む。
     * @param vmdMotionElem vmdMotion要素
     * @param vmdMotion モーション
     * @throws TogaXmlException 構文エラー
     */
    static void buildCameraSeq(Element vmdMotionElem, VmdMotion vmdMotion)
            throws TogaXmlException{
        List<CameraMotion> cameraList = vmdMotion.getCameraMotionList();

        Element cameraSeqElem =
                Xml.getChild(vmdMotionElem, "cameraSequence");

        Iterable<Element> childs =
                Xml.eachChild(cameraSeqElem, "cameraMotion");
        for(Element cameraMotionElem : childs){
            buildCameraMotion(cameraMotionElem, cameraList);
        }

        return;
    }

    /**
     * カメラモーションを読み込む。
     * @param cameraMotionElem cameraMotion要素
     * @param cameraList カメラモーションリスト
     * @throws TogaXmlException 構文エラー
     */
    private static void buildCameraMotion(Element cameraMotionElem,
                                          List<CameraMotion> cameraList)
            throws TogaXmlException{
        CameraMotion cameraMotion = new CameraMotion();

        int frameNo = Xml.getIntegerAttr(cameraMotionElem, "frame");
        cameraMotion.setFrameNumber(frameNo);

        if(cameraMotionElem.hasAttributeNS(Xml.NS_NULL, "hasPerspective")){
            boolean hasPerspective =
                    Xml.getBooleanAttr(cameraMotionElem, "hasPerspective");
            cameraMotion.setPerspectiveMode(hasPerspective);
        }

        buildCameraTarget(cameraMotionElem, cameraMotion);
        buildCameraRotation(cameraMotionElem, cameraMotion);
        buildCameraRange(cameraMotionElem, cameraMotion);
        buildProjection(cameraMotionElem, cameraMotion);

        cameraList.add(cameraMotion);

        return;
    }

    /**
     * カメラターゲット情報を読み込む。
     * @param cameraMotionElem cameraMotion要素
     * @param cameraMotion カメラモーション
     * @throws TogaXmlException 構文エラー
     */
    private static void buildCameraTarget(Element cameraMotionElem,
                                          CameraMotion cameraMotion)
            throws TogaXmlException{
        Element cameraTargetElem =
                Xml.getChild(cameraMotionElem, "cameraTarget");
        MkPos3D targetPos = cameraMotion.getCameraTarget();

        float xPos = Xml.getFloatAttr(cameraTargetElem, "xPos");
        float yPos = Xml.getFloatAttr(cameraTargetElem, "yPos");
        float zPos = Xml.getFloatAttr(cameraTargetElem, "zPos");
        targetPos.setPosition(xPos, yPos, zPos);

        PosCurve curve = cameraMotion.getTargetPosCurve();
        Xml.buildPosCurve(cameraTargetElem, curve);

        return;
    }

    /**
     * カメラ回転情報を読み込む。
     * @param cameraMotionElem cameraMotion要素
     * @param cameraMotion カメラモーション
     * @throws TogaXmlException 構文エラー
     */
    private static void buildCameraRotation(Element cameraMotionElem,
                                            CameraMotion cameraMotion)
            throws TogaXmlException{
        Element cameraRotationElem =
                Xml.getChild(cameraMotionElem, "cameraRotation");
        CameraRotation cameraRotation = cameraMotion.getCameraRotation();

        float latitude  = Xml.getFloatAttr(cameraRotationElem, "xRad");
        float longitude = Xml.getFloatAttr(cameraRotationElem, "yRad");
        float roll      = Xml.getFloatAttr(cameraRotationElem, "zRad");
        cameraRotation.setLatitude(latitude);
        cameraRotation.setLongitude(longitude);
        cameraRotation.setRoll(roll);

        BezierParam rotationCurve = cameraMotion.getIntpltRotation();
        Xml.buildCurve(cameraRotationElem, rotationCurve);

        return;
    }

    /**
     * カメラ距離情報を読み込む。
     * @param cameraMotionElem cameraMotion要素
     * @param cameraMotion カメラモーション
     * @throws TogaXmlException 構文エラー
     */
    private static void buildCameraRange(Element cameraMotionElem,
                                         CameraMotion cameraMotion)
            throws TogaXmlException{
        Element cameraRangeElem =
                Xml.getChild(cameraMotionElem, "cameraRange");

        float range = Xml.getFloatAttr(cameraRangeElem, "range");
        cameraMotion.setRange(range);

        BezierParam rangeCurve = cameraMotion.getIntpltRange();
        Xml.buildCurve(cameraRangeElem, rangeCurve);

        return;
    }

    /**
     * カメラ投影情報を読み込む。
     * @param cameraMotionElem cameraMotion要素
     * @param cameraMotion カメラモーション
     * @throws TogaXmlException 構文エラー
     */
    private static void buildProjection(Element cameraMotionElem,
                                        CameraMotion cameraMotion)
            throws TogaXmlException{
        Element projectionElem =
                Xml.getChild(cameraMotionElem, "projection");

        int vertDeg = Xml.getIntegerAttr(projectionElem, "vertDeg");
        cameraMotion.setProjectionAngle(vertDeg);

        BezierParam projCurve = cameraMotion.getIntpltProjection();
        Xml.buildCurve(projectionElem, projCurve);

        return;
    }

}
