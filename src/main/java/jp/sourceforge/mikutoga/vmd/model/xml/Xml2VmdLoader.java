/*
 * xml loader
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.model.xml;

import java.io.IOException;
import java.text.MessageFormat;
import javax.xml.parsers.DocumentBuilder;
import jp.sourceforge.mikutoga.math.MkPos3D;
import jp.sourceforge.mikutoga.math.MkQuat;
import jp.sourceforge.mikutoga.vmd.model.BezierParam;
import jp.sourceforge.mikutoga.vmd.model.BoneMotion;
import jp.sourceforge.mikutoga.vmd.model.MorphMotion;
import jp.sourceforge.mikutoga.vmd.model.NamedListMap;
import jp.sourceforge.mikutoga.vmd.model.PosCurve;
import jp.sourceforge.mikutoga.vmd.model.VmdMotion;
import jp.sourceforge.mikutoga.xml.TogaXmlException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * XML形式でのモーションファイルを読み込む。
 */
public class Xml2VmdLoader {

    private static final String ERRMSG_INVROOT =
            "RootElem:[{0}] must be [vmdMotion]";


    private final DocumentBuilder builder;


    /**
     * コンストラクタ。
     * @param builder ビルダ
     */
    public Xml2VmdLoader(DocumentBuilder builder){
        super();
        assert builder.isNamespaceAware();
        this.builder = builder;
        return;
    }


    /**
     * ルート要素の取得とチェックを行う。
     * @param document XMLドキュメント
     * @return ルート要素
     * @throws TogaXmlException 不正なルート要素の検出
     */
    private static Element getRootElem(Document document)
            throws TogaXmlException {
        Element vmdMotionElem = document.getDocumentElement();
        if( ! Xml.hasNsLocalNameElem(vmdMotionElem, "vmdMotion") ){
            String message =
                    MessageFormat.format(ERRMSG_INVROOT,
                                         vmdMotionElem.getLocalName() );
            throw new TogaXmlException(message);
        }
        return vmdMotionElem;
    }

    /**
     * モーションのモデル名を読み込む。
     * @param vmdMotionElem vmdMotion要素
     * @param vmdMotion モーション
     * @throws TogaXmlException 構文エラー
     */
    private static void buildModelName(Element vmdMotionElem,
                                       VmdMotion vmdMotion)
            throws TogaXmlException{
        Element modelNameElem = Xml.getChild(vmdMotionElem, "modelName");
        String modelName = Xml.getStringAttr(modelNameElem, "name");
        vmdMotion.setModelName(modelName);
        return;
    }

    /**
     * ボーンシーケンスを読み込む。
     * @param vmdMotionElem vmdMotion要素
     * @param vmdMotion モーション
     * @throws TogaXmlException 構文エラー
     */
    private static void buildBoneSeq(Element vmdMotionElem,
                                     VmdMotion vmdMotion )
            throws TogaXmlException{
        NamedListMap<BoneMotion> boneMap = vmdMotion.getBonePartMap();

        Element boneSeqElem =
                Xml.getChild(vmdMotionElem, "boneMotionSequence");

        for(Element bonePartElem : Xml.eachChild(boneSeqElem, "bonePart")){
            buildBonePart(bonePartElem, boneMap);
        }

        return;
    }

    /**
     * ボーンパートを読み込む。
     * @param bonePartElem bonePart要素
     * @param boneMap 名前マップ
     * @throws TogaXmlException 構文エラー
     */
    private static void buildBonePart(Element bonePartElem,
                                      NamedListMap<BoneMotion> boneMap )
            throws TogaXmlException{
        String boneName = Xml.getStringAttr(bonePartElem, "name");

        for(Element boneMotionElem :
                Xml.eachChild(bonePartElem, "boneMotion")){
            BoneMotion boneMotion = buildBoneMotion(boneMotionElem);
            boneMotion.setBoneName(boneName);
            boneMap.addNamedElement(boneName, boneMotion);
        }

        return;
    }

    /**
     * ボーンモーションを読み込む。
     * @param boneMotionElem boneMotion要素
     * @return ボーンモーション
     * @throws TogaXmlException 構文エラー
     */
    private static BoneMotion buildBoneMotion(Element boneMotionElem)
            throws TogaXmlException {
        BoneMotion boneMotion = new BoneMotion();
        int frameNo = Xml.getIntegerAttr(boneMotionElem, "frame");
        boneMotion.setFrameNumber(frameNo);

        buildBonePosition(boneMotionElem, boneMotion);
        if(Xml.pickChild(boneMotionElem, "boneRotQuat") != null){
            buildBoneRotQuat(boneMotionElem, boneMotion);
        }else{
            buildBoneRotEyxz(boneMotionElem, boneMotion);
        }

        return boneMotion;
    }

    /**
     * ボーン位置を読み込む。
     * @param boneMotionElem boneMotion要素
     * @param boneMotion ボーンモーション
     * @throws TogaXmlException 構文エラー
     */
    private static void buildBonePosition(Element boneMotionElem,
                                          BoneMotion boneMotion )
            throws TogaXmlException {
        Element bonePositionElem =
                Xml.pickChild(boneMotionElem, "bonePosition");
        if(bonePositionElem == null) return;

        MkPos3D position = boneMotion.getPosition();
        float xPos = Xml.getFloatAttr(bonePositionElem, "xPos");
        float yPos = Xml.getFloatAttr(bonePositionElem, "yPos");
        float zPos = Xml.getFloatAttr(bonePositionElem, "zPos");
        position.setXpos(xPos);
        position.setYpos(yPos);
        position.setZpos(zPos);

        PosCurve curve = boneMotion.getPosCurve();
        Xml.buildPosCurve(bonePositionElem, curve);

        return;
    }

    /**
     * ボーン回転をクォータニオン形式で読み込む。
     * @param boneMotionElem boneMotion要素
     * @param boneMotion ボーンモーション
     * @throws TogaXmlException 構文エラー
     */
    private static void buildBoneRotQuat(Element boneMotionElem,
                                          BoneMotion boneMotion )
            throws TogaXmlException{
        Element boneRotationElem =
                Xml.getChild(boneMotionElem, "boneRotQuat");

        MkQuat rotation = boneMotion.getRotation();
        float qx = Xml.getFloatAttr(boneRotationElem, "qx");
        float qy = Xml.getFloatAttr(boneRotationElem, "qy");
        float qz = Xml.getFloatAttr(boneRotationElem, "qz");
        float qw = Xml.getFloatAttr(boneRotationElem, "qw");

        rotation.setQ1(qx);
        rotation.setQ2(qy);
        rotation.setQ3(qz);
        rotation.setQW(qw);

        BezierParam rotationCurve = boneMotion.getIntpltRotation();
        Xml.buildCurve(boneRotationElem, rotationCurve);

        return;
    }

    /**
     * ボーン回転をオイラー角で読み込む。
     * @param boneMotionElem boneMotion要素
     * @param boneMotion ボーンモーション
     * @throws TogaXmlException 構文エラー
     */
    private static void buildBoneRotEyxz(Element boneMotionElem,
                                         BoneMotion boneMotion )
            throws TogaXmlException{
        Element boneRotationElem =
                Xml.getChild(boneMotionElem, "boneRotEyxz");

        MkQuat rotation = boneMotion.getRotation();

        float xDeg = Xml.getFloatAttr(boneRotationElem, "xDeg");
        float yDeg = Xml.getFloatAttr(boneRotationElem, "yDeg");
        float zDeg = Xml.getFloatAttr(boneRotationElem, "zDeg");
        float xRad = (float)StrictMath.toRadians(xDeg);
        float yRad = (float)StrictMath.toRadians(yDeg);
        float zRad = (float)StrictMath.toRadians(zDeg);
        rotation.readEulerYXZ(xRad, yRad, zRad);

        BezierParam rotationCurve = boneMotion.getIntpltRotation();
        Xml.buildCurve(boneRotationElem, rotationCurve);

        return;
    }

    /**
     * モーフシーケンスを読み込む。
     * @param vmdMotionElem vmdMotion要素
     * @param vmdMotion モーション
     * @throws TogaXmlException 構文エラー
     */
    private static void buildMorphSeq(Element vmdMotionElem,
                                      VmdMotion vmdMotion )
            throws TogaXmlException{
        NamedListMap<MorphMotion> morphMap = vmdMotion.getMorphPartMap();

        Element morphSeqElem = Xml.getChild(vmdMotionElem, "morphSequence");

        for(Element morphPartElem :
                Xml.eachChild(morphSeqElem, "morphPart")){
            buildMorphPart(morphPartElem, morphMap);
        }

        return;
    }

    /**
     * モーフパートを読み込む。
     * @param morphPartElem morphPart要素
     * @param morphMap 名前マップ
     * @throws TogaXmlException 構文エラー
     */
    private static void buildMorphPart(Element morphPartElem,
                                       NamedListMap<MorphMotion> morphMap )
            throws TogaXmlException{
        String morphName = Xml.getStringAttr(morphPartElem, "name");

        Iterable<Element> childs =
                Xml.eachChild(morphPartElem, "morphMotion");
        for(Element morphMotionElem : childs){
            MorphMotion morphMotion = buildMorphMotion(morphMotionElem);
            morphMotion.setMorphName(morphName);
            morphMap.addNamedElement(morphName, morphMotion);
        }

        return;
    }

    /**
     * モーフモーションを読み込む。
     * @param morphMotionElem morphMotion要素
     * @return モーフモーション
     * @throws TogaXmlException 構文エラー
     */
    private static MorphMotion buildMorphMotion(Element morphMotionElem)
            throws TogaXmlException {
        MorphMotion morphMotion = new MorphMotion();

        int frameNo = Xml.getIntegerAttr(morphMotionElem, "frame");
        float flex = Xml.getFloatAttr(morphMotionElem, "flex");

        morphMotion.setFrameNumber(frameNo);
        morphMotion.setFlex(flex);

        return morphMotion;
    }

    /**
     * XMLのパースを開始する。
     * @param source XML入力
     * @return モーションデータ
     * @throws SAXException 構文エラー
     * @throws IOException 入力エラー
     * @throws TogaXmlException 構文エラー
     */
    public VmdMotion parse(InputSource source)
            throws SAXException, IOException, TogaXmlException{
        Document document = this.builder.parse(source);
        Element vmdMotionElem = getRootElem(document);
        VmdMotion vmdMotion = new VmdMotion();

        // ignore <meta>

        if(Xml.pickChild(vmdMotionElem, "modelName") != null){
            buildModelName(vmdMotionElem, vmdMotion);
            buildBoneSeq(vmdMotionElem, vmdMotion);
            buildMorphSeq(vmdMotionElem, vmdMotion);
        }else{
            XmlCameraLoader.buildCameraSeq(vmdMotionElem, vmdMotion);
            XmlLightingLoader.buildLuminousSeq(vmdMotionElem, vmdMotion);
            XmlLightingLoader.buildShadowSeq(vmdMotionElem, vmdMotion);
        }

        return vmdMotion;
    }

}
