/*
 * MMD model file types.
 *
 * License : The MIT License
 * Copyright(c) 2012 MikuToga Partners
 */

package jp.sourceforge.mikutoga.pmd;

/**
 * モデルファイル種別。
 */
public enum ModelFileType {

    /** 不明。 */
    NONE,

    /** MikuMikuDance ver7 前後で読み書きが可能なPMDファイル。 */
    PMD,

    /**
     * スキーマ
     * http://mikutoga.sourceforge.jp/xml/xsd/pmdxml-101009.xsd
     * で定義されたXMLファイル。
     */
    XML_101009,

    ;

}
