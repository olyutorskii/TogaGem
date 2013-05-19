/*
 * VMD constant data
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sfjp.mikutoga.vmd;

/**
 * VMDファイルフォーマットの各種定数。
 */
public final class VmdConst {

    /**
     * VMDファイルヘッダ長。バイト単位。
     */
    public static final int HEADER_LENGTH = 30;

    /**
     * ファイルヘッダ部先頭のASCIIコード相当部。
     */
    public static final String MAGIC_TXT = "Vocaloid Motion Data 0002";

    /**
     * モデル名最大長。バイト単位。
     */
    public static final int MODELNAME_MAX = 20;

    /**
     * ボーン名最大長。バイト単位。
     */
    public static final int BONENAME_MAX = 15;

    /**
     * モーフ名最大長。バイト単位。
     */
    public static final int MORPHNAME_MAX = 15;


    /**
     * 隠しコンストラクタ。
     */
    private VmdConst(){
        super();
        assert false;
        throw new AssertionError();
    }

}
