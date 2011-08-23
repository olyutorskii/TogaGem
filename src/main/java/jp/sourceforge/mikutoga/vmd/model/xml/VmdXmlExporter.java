/*
 * vmd-xml exporter
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.model.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import jp.sourceforge.mikutoga.math.EulerYXZ;
import jp.sourceforge.mikutoga.math.MkPos3D;
import jp.sourceforge.mikutoga.math.MkQuat;
import jp.sourceforge.mikutoga.typical.TypicalBone;
import jp.sourceforge.mikutoga.typical.TypicalMorph;
import jp.sourceforge.mikutoga.vmd.IllegalVmdDataException;
import jp.sourceforge.mikutoga.vmd.VmdConst;
import jp.sourceforge.mikutoga.vmd.model.BezierParam;
import jp.sourceforge.mikutoga.vmd.model.BoneMotion;
import jp.sourceforge.mikutoga.vmd.model.CameraMotion;
import jp.sourceforge.mikutoga.vmd.model.CameraRotation;
import jp.sourceforge.mikutoga.vmd.model.LuminousColor;
import jp.sourceforge.mikutoga.vmd.model.LuminousMotion;
import jp.sourceforge.mikutoga.vmd.model.LuminousVector;
import jp.sourceforge.mikutoga.vmd.model.MorphMotion;
import jp.sourceforge.mikutoga.vmd.model.NamedListMap;
import jp.sourceforge.mikutoga.vmd.model.PosCurve;
import jp.sourceforge.mikutoga.vmd.model.ShadowMode;
import jp.sourceforge.mikutoga.vmd.model.ShadowMotion;
import jp.sourceforge.mikutoga.vmd.model.VmdMotion;
import jp.sourceforge.mikutoga.xml.BasicXmlExporter;
import jp.sourceforge.mikutoga.xml.XmlResourceResolver;

/**
 * VMDモーションデータをXMLへエクスポートする。
 */
public class VmdXmlExporter extends BasicXmlExporter {

    private static final String XSINS = "xsi";

    private static final String TOP_COMMENT =
            "  MikuMikuDance\n    motion-data(*.vmd) on XML";

    private static final String QUATERNION_COMMENT =
          "  bone-rotation has Quaternion parameters [boneRotQuat]\n"
        + "  or YXZ-Euler angles [boneRotEyxz].\n"
        + "  Quaternion is strongly recommended"
        +   " if you are data-exchanging.";

    private static final String BEZIER_COMMENT =
          "  motion interpolation is defined by Bezier-cubic-curve.\n"
        + "  implicit bezier curve point : P0=(0,0) P3=(127,127)\n"
        + "  defLinear : MMD default linear curve."
        + " P1=(20,20) P2=(107,107) [DEFAULT]\n"
        + "  defEaseInOut : MMD default ease-in-out curve."
        + " P1=(64,0) P2=(64,127)";

    private static final String CAMERA_COMMENT =
          "  camera-rotation has polar-coordinates parameters.\n"
        + "  xRad = -radian(UI_X) [latitude]\n"
        + "  yRad =  radian(UI_Y) [longitude]\n"
        + "  zRad =  radian(UI_Z) [roll]\n"
        + "  range = -(UI_RANGE)";

    private static final String SHADOW_COMMENT =
             "  UI_VALUE = EFFECTIVE_RANGE * 100 ???\n"
            +"  rawParam = 0.1 - (UI_VALUE / 1.0E+5)\n\n"
            +"  NONE   : no self-shadow\n"
            +"  MODE_1 : reduce shadow-quality suddenly at range\n"
            +"  MODE_2 : reduce shadow-quality gradually with range";


    private boolean isQuaternionMode = true;
    private String generator = "";


    /**
     * コンストラクタ。
     * 文字エンコーディングはUTF-8が用いられる。
     * @param stream 出力ストリーム
     */
    public VmdXmlExporter(OutputStream stream){
        super(stream);
        return;
    }


    /**
     * ボーン回転量をクォータニオンで出力するか否か設定する。
     * <p>デフォルトではtrue
     * @param mode trueだとクォータニオン、falseだとオイラー角で出力される。
     */
    public void setQuaternionMode(boolean mode){
        this.isQuaternionMode = mode;
    }

    /**
     * Generatorメタ情報を設定する。
     * @param generatorArg Generatorメタ情報
     * @throws NullPointerException 引数がnull
     */
    public void setGenerator(String generatorArg)
            throws NullPointerException{
        if(generatorArg == null) throw new NullPointerException();
        this.generator = generatorArg;
        return;
    }

    /**
     * VMDモーションデータをXML形式で出力する。
     * <p>モーションデータと演出データで振り分けられる。
     * @param vmdMotion VMDモーションデータ
     * @throws IOException 出力エラー
     * @throws IllegalVmdDataException 不正なモーションデータを検出
     */
    public void putVmdXml(VmdMotion vmdMotion)
            throws IOException, IllegalVmdDataException{
        try{
            putVmdXmlImpl(vmdMotion);
        }finally{
            flush();
        }
        return;
    }

    /**
     * VMDモーションデータをXML形式で出力する。
     * <p>モーションデータと演出データで振り分けられる。
     * @param vmdMotion VMDモーションデータ
     * @throws IOException 出力エラー
     * @throws IllegalVmdDataException 不正なモーションデータを検出
     */
    private void putVmdXmlImpl(VmdMotion vmdMotion)
            throws IOException, IllegalVmdDataException{
        ind().put("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>").ln(2);

        ind().putBlockComment(TOP_COMMENT).ln(2);

        ind().put("<vmdMotion").ln();
        pushNest();
        ind().putAttr("xmlns", VmdXmlResources.NS_VMDXML).ln();
        ind().putAttr("xmlns:" + XSINS, XmlResourceResolver.NS_XSD).ln();

        ind().put(XSINS).put(":schemaLocation=").put('"');
        put(VmdXmlResources.NS_VMDXML).ln();
        ind().sp(2).put(VmdXmlResources.SCHEMAURI_VMDXML).put('"').ln();

        ind().putAttr("version", VmdXmlResources.VER_VMDXML).ln();
        popNest();
        put(">").ln(2);

        if(this.generator != null && this.generator.length() > 0){
            ind().put("<meta ");
            putAttr("name", "generator").put(' ');
            putAttr("content", this.generator);
            put(" />").ln(2);
        }

        if(vmdMotion.isModelMotion()){
            putModelName(vmdMotion);
            putBoneMotionSequence(vmdMotion);
            putMorphSequence(vmdMotion);
        }else{
            putCameraSequence(vmdMotion);
            putLuminousSequence(vmdMotion);
            putShadowSequence(vmdMotion);
        }

        ind().put("</vmdMotion>").ln(2);
        ind().put("<!-- EOF -->").ln();

        return;
    }

    /**
     * 位置移動補間カーブを出力する。
     * @param posCurve 移動補間情報
     * @throws IOException 出力エラー
     */
    private void putPositionCurve(PosCurve posCurve)
            throws IOException{
        BezierParam xCurve = posCurve.getIntpltXpos();
        BezierParam yCurve = posCurve.getIntpltYpos();
        BezierParam zCurve = posCurve.getIntpltZpos();

        ind().putLineComment("X-Y-Z interpolation *3").ln();

        ind();
        putBezierCurve(xCurve);
        ln();

        ind();
        putBezierCurve(yCurve);
        ln();

        ind();
        putBezierCurve(zCurve);
        ln();

        return;
    }

    /**
     * ベジェ曲線による補間曲線情報を出力する。
     * @param bezier ベジェ曲線
     * @throws IOException 出力エラー
     */
    private void putBezierCurve(BezierParam bezier)
            throws IOException{
        if(bezier.isDefaultLinear()){
            put("<defLinear />");
        }else if(bezier.isDefaultEaseInOut()){
            put("<defEaseInOut />");
        }else{
            put("<bezier ");
            putIntAttr("p1x", bezier.getP1x()).sp();
            putIntAttr("p1y", bezier.getP1y()).sp();
            putIntAttr("p2x", bezier.getP2x()).sp();
            putIntAttr("p2y", bezier.getP2y()).sp();
            put("/>");
        }
        return;
    }

    /**
     * モデル名を出力する。
     * @param vmdMotion モーションデータ
     * @throws IOException 出力エラー
     */
    private void putModelName(VmdMotion vmdMotion)
            throws IOException{
        String modelName = vmdMotion.getModelName();

        ind().putLineComment(modelName).ln();
        ind().put("<modelName ");
        putAttr("name", modelName).sp();
        put("/>").ln(2);

        return;
    }

    /**
     * ボーンモーションデータを出力する。
     * @param vmdMotion モーションデータ
     * @throws IOException 出力エラー
     */
    private void putBoneMotionSequence(VmdMotion vmdMotion)
            throws IOException{
        ind().putBlockComment(QUATERNION_COMMENT);
        ind().putBlockComment(BEZIER_COMMENT);

        ind().put("<boneMotionSequence>").ln();

        pushNest();
        NamedListMap<BoneMotion> listmap = vmdMotion.getBonePartMap();
        if( ! listmap.isEmpty() ) ln();
        for(String boneName : listmap.getNames()){
            List<BoneMotion> list = listmap.getNamedList(boneName);
            putBonePart(boneName, list);
        }
        popNest();

        ind().put("</boneMotionSequence>").ln(2);

        return;
    }

    /**
     * ボーン別モーションデータを出力する。
     * @param boneName ボーン名
     * @param list ボーンモーションのリスト
     * @throws IOException 出力エラー
     */
    private void putBonePart(String boneName, List<BoneMotion> list)
            throws IOException{
        ind().putLineComment(boneName);
        String globalName = TypicalBone.primary2global(boneName);
        if(globalName != null){
            sp(2).putLineComment("Perhaps : [" + globalName + "]");
        }
        ln();

        ind().put("<bonePart ");
        putAttr("name", boneName).sp();
        put(">").ln(2);

        pushNest();
        for(BoneMotion bone : list){
            putBoneMotion(bone);
        }
        popNest();

        ind().put("</bonePart>").ln(2);

        return;
    }

    /**
     * ボーンモーションを出力する。
     * @param boneMotion ボーンモーション
     * @throws IOException 出力エラー
     */
    private void putBoneMotion(BoneMotion boneMotion)
            throws IOException{
        ind().put("<boneMotion ");
        int frameNo = boneMotion.getFrameNumber();
        putIntAttr("frame", frameNo).sp();
        put(">").ln();

        pushNest();
        putBonePosition(boneMotion);
        if(this.isQuaternionMode){
            putBoneRotQuat(boneMotion);
        }else{
            putBoneRotEyxz(boneMotion);
        }
        popNest();

        ind().put("</boneMotion>").ln(2);

        return;
    }

    /**
     * ボーン位置を出力する。
     * @param boneMotion ボーンモーション
     * @throws IOException 出力エラー
     */
    private void putBonePosition(BoneMotion boneMotion)
            throws IOException{
        if(boneMotion.hasImplicitPosition()){
            return;
        }

        ind().put("<bonePosition ");
        MkPos3D position = boneMotion.getPosition();
        float xPos = (float) position.getXpos();
        float yPos = (float) position.getYpos();
        float zPos = (float) position.getZpos();
        putFloatAttr("xPos", xPos).sp();
        putFloatAttr("yPos", yPos).sp();
        putFloatAttr("zPos", zPos).sp();

        PosCurve posCurve = boneMotion.getPosCurve();
        if(posCurve.isDefaultLinear()){
            put("/>").ln();
        }else{
            put(">").ln();

            pushNest();
            putPositionCurve(posCurve);
            popNest();

            ind().put("</bonePosition>").ln();
        }

        return;
    }

    /**
     * ボーン回転を出力する。
     * @param boneMotion ボーンモーション
     * @throws IOException 出力エラー
     */
    private void putBoneRotQuat(BoneMotion boneMotion)
            throws IOException{
        MkQuat rotation = boneMotion.getRotation();
        BezierParam rotCurve = boneMotion.getIntpltRotation();

        ind().put("<boneRotQuat").ln();
        pushNest();
        ind().putFloatAttr("qx", (float) rotation.getQ1()).ln();
        ind().putFloatAttr("qy", (float) rotation.getQ2()).ln();
        ind().putFloatAttr("qz", (float) rotation.getQ3()).ln();
        ind().putFloatAttr("qw", (float) rotation.getQW()).ln();
        popNest();
        ind();

        if(rotCurve.isDefaultLinear()){
            put("/>").ln();
        }else{
            put(">").ln();
            pushNest();
            ind();
            putBezierCurve(rotCurve);
            ln();
            popNest();
            ind().put("</boneRotQuat>").ln();
        }

        return;
    }

    /**
     * ボーン回転を出力する。
     * @param boneMotion ボーンモーション
     * @throws IOException 出力エラー
     */
    private void putBoneRotEyxz(BoneMotion boneMotion)
            throws IOException{
        MkQuat rotation = boneMotion.getRotation();
        BezierParam rotCurve = boneMotion.getIntpltRotation();

        EulerYXZ euler = new EulerYXZ();
        rotation.toEulerYXZ(euler);
        float xDeg = (float)StrictMath.toDegrees(euler.getXRot());
        float yDeg = (float)StrictMath.toDegrees(euler.getYRot());
        float zDeg = (float)StrictMath.toDegrees(euler.getZRot());

        ind().put("<boneRotEyxz").ln();
        pushNest();
        ind().putFloatAttr("xDeg", xDeg).ln();
        ind().putFloatAttr("yDeg", yDeg).ln();
        ind().putFloatAttr("zDeg", zDeg).ln();
        popNest();
        ind();

        if(rotCurve.isDefaultLinear()){
            put("/>").ln();
        }else{
            put(">").ln();
            pushNest();
            ind();
            putBezierCurve(rotCurve);
            ln();
            popNest();
            ind().put("</boneRotEyxz>").ln();
        }

        return;
    }

    /**
     * モーフデータを出力する。
     * @param vmdMotion モーションデータ
     * @throws IOException 出力エラー
     */
    private void putMorphSequence(VmdMotion vmdMotion)
            throws IOException{
        ind().put("<morphSequence>").ln();

        pushNest();
        NamedListMap<MorphMotion> listmap = vmdMotion.getMorphPartMap();
        if( ! listmap.isEmpty() ) ln();
        putMorphPartList(listmap);
        popNest();

        ind().put("</morphSequence>").ln(2);

        return;
    }

    /**
     * 箇所別モーフデータを出力する。
     * @param listmap モーフデータの名前付きリストマップ
     * @throws IOException 出力エラー
     */
    private void putMorphPartList(NamedListMap<MorphMotion> listmap)
            throws IOException{
        for(String morphName : listmap.getNames()){
            if(VmdConst.isBaseMorphName(morphName)) continue;

            ind().putLineComment(morphName);
            String globalName = TypicalMorph.primary2global(morphName);
            if(globalName != null){
                sp(2).putLineComment("Perhaps : [" + globalName + "]");
            }
            ln();

            ind().put("<morphPart ");
            putAttr("name", morphName).sp();
            put(">").ln();

            pushNest();
            List<MorphMotion> list = listmap.getNamedList(morphName);
            for(MorphMotion morph : list){
                putMorphMotion(morph);
            }
            popNest();

            ind().put("</morphPart>").ln(2);
        }

        return;
    }

    /**
     * 個別のモーフモーションを出力する。
     * @param morphMotion モーフモーション
     * @throws IOException 出力エラー
     */
    private void putMorphMotion(MorphMotion morphMotion)
            throws IOException{
        ind().put("<morphMotion ");

        int frameNo = morphMotion.getFrameNumber();
        float flex = morphMotion.getFlex();

        putIntAttr("frame", frameNo).sp();
        putFloatAttr("flex", flex).sp();

        put("/>").ln();

        return;
    }

    /**
     * カメラ演出データを出力する。
     * @param vmdMotion 演出データ
     * @throws IOException 出力エラー
     */
    private void putCameraSequence(VmdMotion vmdMotion)
            throws IOException{
        ind().putBlockComment(BEZIER_COMMENT);
        ind().putBlockComment(CAMERA_COMMENT);

        ind().put("<cameraSequence>").ln();

        pushNest();
        List<CameraMotion> list = vmdMotion.getCameraMotionList();
        if( ! list.isEmpty() ) ln();
        for(CameraMotion camera : list){
            putCameraMotion(camera);
        }
        popNest();

        ind().put("</cameraSequence>").ln(2);

        return;
    }

    /**
     * カメラモーションを出力する。
     * @param cameraMotion カメラモーション
     * @throws IOException 出力エラー
     */
    private void putCameraMotion(CameraMotion cameraMotion)
            throws IOException{
        ind().put("<cameraMotion ");
        int frameNo = cameraMotion.getFrameNumber();
        putIntAttr("frame", frameNo).sp();
        if( ! cameraMotion.hasPerspective() ){
            putAttr("hasPerspective", "false").sp();
        }
        put(">").ln();

        pushNest();
        putCameraTarget(cameraMotion);
        putCameraRotation(cameraMotion);
        putCameraRange(cameraMotion);
        putProjection(cameraMotion);
        popNest();

        ind().put("</cameraMotion>").ln(2);

        return;
    }

    /**
     * カメラターゲット情報を出力する。
     * @param cameraMotion カメラモーション
     * @throws IOException 出力エラー
     */
    private void putCameraTarget(CameraMotion cameraMotion)
            throws IOException{
        ind().put("<cameraTarget ");
        MkPos3D position = cameraMotion.getCameraTarget();
        putFloatAttr("xPos", (float) position.getXpos()).sp();
        putFloatAttr("yPos", (float) position.getYpos()).sp();
        putFloatAttr("zPos", (float) position.getZpos()).sp();

        PosCurve posCurve = cameraMotion.getTargetPosCurve();
        if(posCurve.isDefaultLinear()){
            put("/>").ln();
        }else{
            put(">").ln();

            pushNest();
            putPositionCurve(posCurve);
            popNest();

            ind().put("</cameraTarget>").ln();
        }

        return;
    }

    /**
     * カメラ回転情報を出力する。
     * @param cameraMotion カメラモーション
     * @throws IOException 出力エラー
     */
    private void putCameraRotation(CameraMotion cameraMotion)
            throws IOException{
        ind().put("<cameraRotation ");
        CameraRotation rotation = cameraMotion.getCameraRotation();
        putFloatAttr("xRad", rotation.getLatitude()).sp();
        putFloatAttr("yRad", rotation.getLongitude()).sp();
        putFloatAttr("zRad", rotation.getRoll()).sp();

        BezierParam rotCurve = cameraMotion.getIntpltRotation();
        if(rotCurve.isDefaultLinear()){
            put("/>").ln();
        }else{
            put(">").ln();
            pushNest();
            ind();
            putBezierCurve(rotCurve);
            ln();
            popNest();
            ind().put("</cameraRotation>").ln();
        }

        return;
    }

    /**
     * カメラ距離情報を出力する。
     * @param cameraMotion カメラモーション
     * @throws IOException 出力エラー
     */
    private void putCameraRange(CameraMotion cameraMotion)
            throws IOException{
        ind().put("<cameraRange ");
        float range = cameraMotion.getRange();
        putFloatAttr("range", range).sp();

        BezierParam rangeCurve = cameraMotion.getIntpltRange();
        if(rangeCurve.isDefaultLinear()){
            put("/>").ln();
        }else{
            put(">").ln();
            pushNest();
            ind();
            putBezierCurve(rangeCurve);
            ln();
            popNest();
            ind().put("</cameraRange>").ln();
        }

        return;
    }

    /**
     * スクリーン投影情報を出力する。
     * @param cameraMotion カメラモーション
     * @throws IOException 出力エラー
     */
    private void putProjection(CameraMotion cameraMotion)
            throws IOException{
        ind().put("<projection ");
        putIntAttr("vertDeg", cameraMotion.getProjectionAngle()).sp();

        BezierParam projCurve = cameraMotion.getIntpltProjection();
        if(projCurve.isDefaultLinear()){
            put("/>").ln();
        }else{
            put(">").ln();
            pushNest();
            ind();
            putBezierCurve(projCurve);
            ln();
            popNest();
            ind().put("</projection>").ln();
        }

        return;
    }

    /**
     * 照明演出データを出力する。
     * @param vmdMotion 演出データ
     * @throws IOException 出力エラー
     */
    private void putLuminousSequence(VmdMotion vmdMotion)
            throws IOException{
        ind().put("<luminousSequence>").ln();

        pushNest();
        List<LuminousMotion> list = vmdMotion.getLuminousMotionList();
        if( ! list.isEmpty() ) ln();
        for(LuminousMotion luminous : list){
            putLuminousMotion(luminous);
        }
        popNest();

        ind().put("</luminousSequence>").ln(2);

        return;
    }

    /**
     * 照明モーションを出力する。
     * @param luminousMotion 照明モーション
     * @throws IOException 出力エラー
     */
    private void putLuminousMotion(LuminousMotion luminousMotion)
            throws IOException{
        ind().put("<luminousAct ");
        int frameNo = luminousMotion.getFrameNumber();
        putIntAttr("frame", frameNo);
        put(" >").ln();

        LuminousColor color = luminousMotion.getColor();
        LuminousVector vector = luminousMotion.getDirection();

        pushNest();
        putLuminousColor(color);
        putLuminousDirection(vector);
        popNest();

        ind().put("</luminousAct>").ln(2);

        return;
    }

    /**
     * 光源色情報を出力する。
     * @param color 光源色
     * @throws IOException 出力エラー
     */
    private void putLuminousColor(LuminousColor color)
            throws IOException{
        ind().put("<lumiColor ");
        putFloatAttr("rCol", color.getColR()).sp();
        putFloatAttr("gCol", color.getColG()).sp();
        putFloatAttr("bCol", color.getColB()).sp();
        put("/>").ln();

        return;
    }

    /**
     * 照明方向情報を出力する。
     * @param vector 照明方向
     * @throws IOException 出力エラー
     */
    private void putLuminousDirection(LuminousVector vector)
            throws IOException{
        ind().put("<lumiDirection ");
        putFloatAttr("xVec", vector.getVecX()).sp();
        putFloatAttr("yVec", vector.getVecY()).sp();
        putFloatAttr("zVec", vector.getVecZ()).sp();
        put("/>").ln();

        return;
    }

    /**
     * シャドウ演出データを出力する。
     * @param vmdMotion 演出データ
     * @throws IOException 出力エラー
     */
    private void putShadowSequence(VmdMotion vmdMotion)
            throws IOException{
        ind().putBlockComment(SHADOW_COMMENT);

        ind().put("<shadowSequence>").ln();

        pushNest();
        List<ShadowMotion> list = vmdMotion.getShadowMotionList();
        for(ShadowMotion shadow : list){
            putShadowMotion(shadow);
        }
        popNest();

        ind().put("</shadowSequence>").ln(2);

        return;
    }

    /**
     * シャドウモーションを出力する。
     * @param shadowMotion シャドウモーション
     * @throws IOException 出力エラー
     */
    private void putShadowMotion(ShadowMotion shadowMotion)
            throws IOException{
        ind().put("<shadowAct ");

        int frameNo = shadowMotion.getFrameNumber();
        ShadowMode mode = shadowMotion.getShadowMode();
        float rawParam = shadowMotion.getRawScopeParam();

        putIntAttr("frame", frameNo).sp();
        putAttr("mode", mode.name()).sp();
        putFloatAttr("rawParam", rawParam).sp();

        put("/>");

        double uiVal = ShadowMotion.rawParamToScope(rawParam);
        long lVal = Math.round(uiVal);
        sp().putLineComment("UI:" + lVal).ln();

        return;
    }

}
