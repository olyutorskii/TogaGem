/*
 * xml lighting loader
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.model.xml;

import java.util.List;
import jp.sourceforge.mikutoga.vmd.model.LuminousColor;
import jp.sourceforge.mikutoga.vmd.model.LuminousMotion;
import jp.sourceforge.mikutoga.vmd.model.LuminousVector;
import jp.sourceforge.mikutoga.vmd.model.ShadowMode;
import jp.sourceforge.mikutoga.vmd.model.ShadowMotion;
import jp.sourceforge.mikutoga.vmd.model.VmdMotion;
import jp.sourceforge.mikutoga.xml.TogaXmlException;
import org.w3c.dom.Element;

/**
 * XMLによるカメラ制御データを読み取る。
 */
final class XmlLightingLoader {

    /**
     * 隠しコンストラクタ。
     */
    private XmlLightingLoader(){
        assert false;
        throw new AssertionError();
    }


    /**
     * 照明シーケンスを読み込む。
     * @param vmdMotionElem vmdMotion要素
     * @param vmdMotion モーション
     * @throws TogaXmlException 構文エラー
     */
    static void buildLuminousSeq(Element vmdMotionElem, VmdMotion vmdMotion)
            throws TogaXmlException{
        List<LuminousMotion> luminousList =
                vmdMotion.getLuminousMotionList();

        Element luminousSeqElem =
                Xml.getChild(vmdMotionElem, "luminousSequence");

        Iterable<Element> childs =
                Xml.eachChild(luminousSeqElem, "luminousAct");
        for(Element luminousActElem : childs){
            buildLuminousAct(luminousActElem, luminousList);
        }

        return;
    }

    /**
     * 照明モーションを読み込む。
     * @param luminousActElem luminousAct要素
     * @param luminousList 照明モーションリスト
     * @throws TogaXmlException 構文エラー
     */
    private static void buildLuminousAct(Element luminousActElem,
                                         List<LuminousMotion> luminousList)
            throws TogaXmlException{
        LuminousMotion luminousMotion = new LuminousMotion();

        int frameNo = Xml.getIntegerAttr(luminousActElem, "frame");
        luminousMotion.setFrameNumber(frameNo);

        Element lumiColorElem = Xml.getChild(luminousActElem, "lumiColor");
        LuminousColor color = luminousMotion.getColor();
        float rCol = Xml.getFloatAttr(lumiColorElem, "rCol");
        float gCol = Xml.getFloatAttr(lumiColorElem, "gCol");
        float bCol = Xml.getFloatAttr(lumiColorElem, "bCol");
        color.setColR(rCol);
        color.setColG(gCol);
        color.setColB(bCol);

        Element lumiDirectionElem =
                Xml.getChild(luminousActElem, "lumiDirection");
        LuminousVector vec = luminousMotion.getDirection();
        float xVec = Xml.getFloatAttr(lumiDirectionElem, "xVec");
        float yVec = Xml.getFloatAttr(lumiDirectionElem, "yVec");
        float zVec = Xml.getFloatAttr(lumiDirectionElem, "zVec");
        vec.setVecX(xVec);
        vec.setVecY(yVec);
        vec.setVecZ(zVec);

        luminousList.add(luminousMotion);

        return;
    }

    /**
     * シャドウシーケンスを読み込む。
     * @param vmdMotionElem vmdMotion要素
     * @param vmdMotion モーション
     * @throws TogaXmlException 構文エラー
     */
    static void buildShadowSeq(Element vmdMotionElem,
                               VmdMotion vmdMotion)
            throws TogaXmlException{
        List<ShadowMotion> shadowMotionList =
                vmdMotion.getShadowMotionList();

        Element shadowSeqElem =
                Xml.getChild(vmdMotionElem, "shadowSequence");

        for(Element shadowActElem :
                Xml.eachChild(shadowSeqElem, "shadowAct")){
            buildShadowAct(shadowActElem, shadowMotionList);
        }

        return;
    }

    /**
     * シャドウモーションを読み込む。
     * @param shadowActElem shadowAct要素
     * @param shadowMotionList シャドウモーションリスト
     * @throws TogaXmlException 構文エラー
     */
    private static void buildShadowAct(Element shadowActElem,
                                       List<ShadowMotion> shadowMotionList)
            throws TogaXmlException{
        ShadowMotion shadowMotion = new ShadowMotion();

        int frameNo = Xml.getIntegerAttr(shadowActElem, "frame");
        shadowMotion.setFrameNumber(frameNo);

        float rawParam = Xml.getFloatAttr(shadowActElem, "rawParam");
        shadowMotion.setRawScopeParam(rawParam);

        String modeAttr = Xml.getStringAttr(shadowActElem, "mode");
        ShadowMode mode = ShadowMode.valueOf(modeAttr);
        shadowMotion.setShadowMode(mode);

        shadowMotionList.add(shadowMotion);

        return;
    }

}
