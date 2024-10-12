/*
 * Sax 2 Xsd-types converter
 *
 * License : The MIT License
 * Copyright(c) 2013 MikuToga Partners
 */

package jp.sfjp.mikutoga.xml;

import org.xml.sax.Attributes;

/**
 * XSD各種型のSAX属性値をJavaプリミティブ型へ変換する。
 */
public final class SaxAttr {

    /**
     * 隠しコンストラクタ。
     */
    private SaxAttr(){
        assert false;
        throw new AssertionError();
    }


    /**
     * 属性名に対応する属性値があるか否か判定する。
     *
     * @param attr 属性群
     * @param name 属性名
     * @return 属性名に対応する属性値がある場合はtrue
     */
    public static boolean hasAttr(Attributes attr, String name){
        if(attr.getValue(name) == null) return false;
        return true;
    }

    /**
     * xsd:string型属性値の読み込み。
     *
     * @param attr 属性群
     * @param name 属性名
     * @return 属性値。該当する属性が無ければnull。
     */
    public static String getStringAttr(Attributes attr, String name){
        String attrVal = attr.getValue(name);
        return attrVal;
    }

    /**
     * xsd:boolean型属性値の読み込み。
     *
     * @param attr 属性群
     * @param name 属性名
     * @return 属性値。
     * @throws IllegalArgumentException boolean型表記ではない
     */
    public static boolean getBooleanAttr(Attributes attr, String name)
            throws IllegalArgumentException{
        String attrVal = attr.getValue(name);
        boolean bVal;
        bVal = DatatypeIo.parseBoolean(attrVal);
        return bVal;
    }

    /**
     * xsd:boolean型属性値の読み込み。
     *
     * @param attr 属性群
     * @param name 属性名
     * @param def 属性が無い場合のデフォルト値
     * @return 属性値。
     * @throws IllegalArgumentException boolean型表記ではない
     */
    public static boolean getBooleanAttr(Attributes attr,
                                           String name,
                                           boolean def )
            throws IllegalArgumentException{
        String attrVal = attr.getValue(name);
        if(attrVal == null) return def;

        boolean bVal;
        bVal = DatatypeIo.parseBoolean(attrVal);

        return bVal;
    }

    /**
     * xsd:byte型属性の読み込み。
     *
     * @param attr 属性群
     * @param name 属性名
     * @return 属性値。
     * @throws NumberFormatException byte型表記ではない
     */
    public static byte getByteAttr(Attributes attr, String name)
            throws NumberFormatException{
        String attrVal = attr.getValue(name);
        byte bVal;
        bVal = DatatypeIo.parseByte(attrVal);
        return bVal;
    }

    /**
     * xsd:float型属性値の読み込み。
     *
     * @param attr 属性群
     * @param name 属性名
     * @return 属性値。
     * @throws NumberFormatException float型表記ではない
     */
    public static float getFloatAttr(Attributes attr, String name)
            throws NumberFormatException {
        String attrVal = attr.getValue(name);
        float fVal;
        fVal = DatatypeIo.parseFloat(attrVal);
        return fVal;
    }

    /**
     * xsd:int型属性値の読み込み。
     *
     * @param attr 属性群
     * @param name 属性名
     * @return 属性値。
     * @throws NumberFormatException int型表記ではない
     */
    public static int getIntAttr(Attributes attr, String name)
            throws NumberFormatException {
        String attrVal = attr.getValue(name);
        int iVal;
        iVal = DatatypeIo.parseInt(attrVal);
        return iVal;
    }

}
