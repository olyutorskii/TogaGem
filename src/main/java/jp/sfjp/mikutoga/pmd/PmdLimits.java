/*
 * pmd limit numbers
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sfjp.mikutoga.pmd;

/**
 * PMDファイルフォーマットの各種リミット値その他定数。
 */
public final class PmdLimits {

    /** モデル名最大長。バイト単位。 */
    public static final int MAXBYTES_MODELNAME = 20;

    /** モデル説明文最大長。バイト単位。 */
    public static final int MAXBYTES_MODELDESC = 256;

    /** ボーン名最大長。バイト単位。 */
    public static final int MAXBYTES_BONENAME = 20;

    /** モーフ名最大長。バイト単位。 */
    public static final int MAXBYTES_MORPHNAME = 20;

    /** ボーングループ名最大長。バイト単位。 */
    public static final int MAXBYTES_BONEGROUPNAME = 50;

    /** テクスチャファイル名最大長。バイト単位。 */
    public static final int MAXBYTES_TEXTUREFILENAME = 20;

    /** 独自トゥーンテクスチャファイル名の最大長。バイト単位。 */
    public static final int MAXBYTES_TOONFILENAME = 100;

    /** 剛体名最大長。バイト単位。 */
    public static final int MAXBYTES_RIGIDNAME = 20;

    /** ジョイント名最大長。バイト単位。 */
    public static final int MAXBYTES_JOINTNAME = 20;


    /**
     * ボーン最大数。
     * (Id : 0 - 0xfffe)
     * <p>MMDがいくつまで受け入れるかはまた別の話だよ。
     */
    public static final int MAX_BONE = 65535;

    /** 剛体衝突グループ総数。 */
    public static final int RIGIDGROUP_FIXEDNUM = 16;

    /** 独自トゥーンテクスチャファイル名テーブルの固定数。 */
    public static final int TOON_FIXEDNUM = 10;


    /**
     * 隠しコンストラクタ。
     */
    private PmdLimits(){
        super();
        assert false;
        throw new AssertionError();
    }

}
