/*
 * xml common
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.model.xml;

import java.util.Iterator;
import jp.sourceforge.mikutoga.vmd.model.BezierParam;
import jp.sourceforge.mikutoga.vmd.model.PosCurve;
import jp.sourceforge.mikutoga.xml.DomNsUtils;
import jp.sourceforge.mikutoga.xml.SiblingElemIterator;
import jp.sourceforge.mikutoga.xml.TogaXmlException;
import org.w3c.dom.Element;

/**
 * XMLユーティリティ集。
 * <p>VMDに特化したDomNsUtilsのWrapper群や共通要素の共通処理を含む。
 * <p>要素に関する名前空間は{@link VmdXmlResources.NS_VMDXML}が暗黙で用いられる。
 * <p>非グローバル属性に関する名前空間は{@NS_NULL}が暗黙で用いられる。
 * @see jp.sourceforge.mikutoga.xml.DomNsUtils
 */
final class Xml {

    /** 非グローバル属性用名前空間。 */
    static final String NS_NULL = null;


    /**
     * 隠しコンストラクタ。
     */
    private Xml(){
        assert false;
        throw new AssertionError();
    }


    /**
     * ローカル名が一致する要素か判定する。
     * @param elem 要素
     * @param localName ローカル名。
     * @return ローカル名が一致する要素であればtrue
     */
    static boolean hasNsLocalNameElem(Element elem,
                                      String localName ){
        return DomNsUtils.hasNsLocalNameElem(elem,
                                             VmdXmlResources.NS_VMDXML,
                                             localName );
    }

    /**
     * ローカル名に合致する最初の直下子要素を返す。
     * @param parent 親要素
     * @param localName 子要素名
     * @return 最初の直下子要素。見つからなければnull。
     */
    static Element pickChild(Element parent, String localName){
        return DomNsUtils.pickFirstChild(parent,
                                         VmdXmlResources.NS_VMDXML,
                                         localName );
    }

    /**
     * ローカル名に合致する最初の直下子要素を返す。
     * <p>見つからなければ例外を投げる。
     * @param parent 親要素
     * @param localName 子要素名
     * @return 最初の直下子要素
     * @throws TogaXmlException 1つも見つからなかった
     */
    static Element getChild(Element parent, String localName)
            throws TogaXmlException{
        return DomNsUtils.getFirstChild(parent,
                                        VmdXmlResources.NS_VMDXML,
                                        localName );
    }

    /**
     * 指定された名前の子要素のforeachを返す。
     * @param parent 親要素
     * @param localName 子要素名
     * @return 子要素のforeach
     */
    static Iterable<Element> eachChild(Element parent, String localName){
        return DomNsUtils.getEachChild(parent,
                                       VmdXmlResources.NS_VMDXML,
                                       localName );
    }

    /**
     * 要素からxsd:string型属性値を読み取る。
     * @param elem 要素
     * @param attrName 属性名
     * @return 文字列
     * @throws TogaXmlException 属性値が見つからなかった。
     */
    static String getStringAttr(Element elem, String attrName)
            throws TogaXmlException{
        return DomNsUtils.getStringAttrNS(elem, NS_NULL, attrName);
    }

    /**
     * 要素からxsd:boolean型属性値を読み取る。
     * @param elem 要素
     * @param attrName 属性名
     * @return 真ならtrue
     * @throws TogaXmlException 属性値が見つからなかった。
     */
    static boolean getBooleanAttr(Element elem, String attrName)
            throws TogaXmlException{
        return DomNsUtils.getBooleanAttrNS(elem, NS_NULL, attrName);
    }

    /**
     * 要素からxsd:integer型属性値を読み取る。
     * @param elem 要素
     * @param attrName 属性名
     * @return int値
     * @throws TogaXmlException 属性値が見つからなかった。
     */
    static int getIntegerAttr(Element elem, String attrName)
            throws TogaXmlException{
        return DomNsUtils.getIntegerAttrNS(elem, NS_NULL, attrName);
    }

    /**
     * 要素から符号付きbyte型整数属性値を読み取る。
     * @param elem 要素
     * @param attrName 属性名
     * @return byte値
     * @throws TogaXmlException 属性値が見つからなかった。
     */
    static byte getByteAttr(Element elem, String attrName)
            throws TogaXmlException{
        int iVal = getIntegerAttr(elem, attrName);
        byte result = (byte) iVal;
        return result;
    }

    /**
     * 要素からxsd:float型属性値を読み取る。
     * @param elem 要素
     * @param attrName 属性名
     * @return float値
     * @throws TogaXmlException 属性値が見つからなかった。
     */
    static float getFloatAttr(Element elem, String attrName)
            throws TogaXmlException{
        return DomNsUtils.getFloatAttrNS(elem, NS_NULL, attrName);
    }

    /**
     * ベジェ曲線による補間カーブ記述を読み込む。
     * @param elem defLinear,defEaseInOut,bezier要素のいずれか
     * @param bezier ベジェ曲線
     * @throws TogaXmlException 構文エラー
     */
    static void setBezier(Element elem, BezierParam bezier)
            throws TogaXmlException{
        byte p1x;
        byte p1y;
        byte p2x;
        byte p2y;

        if(hasNsLocalNameElem(elem, "defLinear")){
            p1x = BezierParam.DEF_P1X;
            p1y = BezierParam.DEF_P1Y;
            p2x = BezierParam.DEF_P2X;
            p2y = BezierParam.DEF_P2Y;
        }else if(hasNsLocalNameElem(elem, "defEaseInOut")){
            p1x = BezierParam.EIO_P1X;
            p1y = BezierParam.EIO_P1Y;
            p2x = BezierParam.EIO_P2X;
            p2y = BezierParam.EIO_P2Y;
        }else if(hasNsLocalNameElem(elem, "bezier")){
            p1x = getByteAttr(elem, "p1x");
            p1y = getByteAttr(elem, "p1y");
            p2x = getByteAttr(elem, "p2x");
            p2y = getByteAttr(elem, "p2y");
        }else{
            assert false;
            throw new AssertionError();
        }

        bezier.setP1(p1x, p1y);
        bezier.setP2(p2x, p2y);

        return;
    }

    /**
     * 補間カーブ情報を読み込む。
     * @param parentElem 親要素
     * @param bezier 補間カーブ
     * @throws TogaXmlException 構文エラー
     */
    static void buildCurve(Element parentElem, BezierParam bezier)
            throws TogaXmlException{
        Element bezierElem = pickChild(parentElem, null);
        if(bezierElem == null) return;

        setBezier(bezierElem, bezier);

        return;
    }

    /**
     * 位置補間情報を読み込む。
     * @param positionElem 親要素
     * @param curve 位置補間カーブ
     * @throws TogaXmlException 構文エラー
     */
    static void buildPosCurve(Element positionElem, PosCurve curve)
            throws TogaXmlException{
        Iterator<Element> it =
                new SiblingElemIterator(positionElem,
                                        VmdXmlResources.NS_VMDXML, null);

        int ct = 0;
        while(it.hasNext()){
            Element curveElem = it.next();
            BezierParam bz = curve.item(ct);
            setBezier(curveElem, bz);
            ct++;
        }

        assert ct == 0 || ct == 3;

        return;
    }

}
