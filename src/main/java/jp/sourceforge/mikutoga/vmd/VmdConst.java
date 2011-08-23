/*
 * VMD constant data
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd;

import java.nio.charset.Charset;

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
     * カメラもしくはライティングデータに便宜上割り当てられるモデル名。
     */
    public static final String MODELNAME_STAGEACT =
            "\u30ab\u30e1\u30e9\u30fb\u7167\u660e"; // "カメラ・照明";

    /**
     * 便宜上割り振られる特殊なモーフ名。
     */
    public static final String MORPHNAME_BASE = "base";

    /**
     * ボーンモーションデータの個別サイズ。バイト単位。
     */
    public static final int BONEMOTION_DATA_SZ = 111;

    /**
     * モーフデータの個別サイズ。バイト単位。
     */
    public static final int MORPH_DATA_SZ = 23;

    /**
     * カメラデータの個別サイズ。バイト単位。
     */
    public static final int CAMERA_DATA_SZ = 61;

    /**
     * 光源データの個別サイズ。バイト単位。
     */
    public static final int LUMINOUS_DATA_SZ = 28;

    /**
     * 影演出データの個別サイズ。バイト単位。
     */
    public static final int SHADOW_DATA_SZ = 9;


    private static final Charset CS_ASCII = Charset.forName("US-ASCII");
    private static final byte[] MAGIC_BYTES;


    static{
        MAGIC_BYTES = createMagicHeader();
        assert MAGIC_BYTES.length <= HEADER_LENGTH;

        assert "カメラ・照明".equals(MODELNAME_STAGEACT);
    }


    /**
     * 隠しコンストラクタ。
     */
    private VmdConst(){
        super();
        assert false;
        throw new AssertionError();
    }

    /**
     * ヘッダ先頭のマジックバイト列を生成する。
     * @return マジックバイト列
     */
    public static byte[] createMagicHeader(){
        byte[] result = (MAGIC_TXT + '\0').getBytes(CS_ASCII);
        return result;
    }

    /**
     * バイト列先頭がマジックヘッダで始まるか検査する。
     * @param array 検査対象のバイト列。
     * @return マジックヘッダが検出されればtrue。
     */
    public static boolean startsWithMagic(byte[] array){
        if(MAGIC_BYTES.length > array.length) return false;

        for(int idx = 0; idx < MAGIC_BYTES.length; idx++){
            if(array[idx] != MAGIC_BYTES[idx]) return false;
        }

        return true;
    }

    /**
     * カメラやライティングなどのステージ演出データの可能性があるか、
     * モデル名から推測する。
     * モデル名が"カメラ・照明"である場合、
     * そのモーションファイルはほぼ
     * カメラ・ライティング用ステージ演出データであると推測される。
     * @param modelName モデル名
     * @return モデル名にカメラもしくはライティングの可能性があるならtrue
     */
    public static boolean isStageActName(String modelName){
        if(MODELNAME_STAGEACT.equals(modelName)) return true;
        return false;
    }

    /**
     * 特殊なモーフ名「base」か否か判定する。
     * @param morphName モーフ名
     * @return モーフ名が「base」ならtrue
     */
    public static boolean isBaseMorphName(String morphName){
        if(MORPHNAME_BASE.equals(morphName)) return true;
        return false;
    }

}
