/*
 * VMD special unique names
 *
 * License : The MIT License
 * Copyright(c) 2013 MikuToga Partners
 */

package jp.sfjp.mikutoga.vmd;

/**
 * 各種特殊名に関する定義。
 */
public final class VmdUniq {

    /**
     * 便宜上割り振られる特殊なモーフ名。
     */
    public static final String MORPHNAME_BASE = "base";

    /**
     * カメラもしくはライティングデータに便宜上割り当てられるモデル名。
     */
    public static final String MODELNAME_STAGEACT =
            "\u30ab\u30e1\u30e9\u30fb\u7167\u660e"; // "カメラ・照明";

    static{
        assert "カメラ・照明".equals(VmdUniq.MODELNAME_STAGEACT);
    }


    /**
     * 隠しコンストラクタ。
     */
    private VmdUniq(){
        assert false;
        throw new AssertionError();
    }


    /**
     * 特殊なモーフ名「base」か否か判定する。
     *
     * @param morphName モーフ名
     * @return モーフ名が「base」ならtrue
     */
    public static boolean isBaseMorphName(String morphName) {
        if(MORPHNAME_BASE.equals(morphName)){
            return true;
        }
        return false;
    }

    /**
     * カメラやライティングなどのステージ演出データの可能性があるか、
     * モデル名から推測する。
     *
     * <p>モデル名が「カメラ・照明」である場合、
     * そのモーションファイルはほぼ
     * カメラ・ライティング用ステージ演出データであると推測される。
     *
     * @param modelName モデル名
     * @return モデル名にカメラもしくはライティングの可能性があるならtrue
     */
    public static boolean isStageActName(String modelName) {
        if(MODELNAME_STAGEACT.equals(modelName)){
            return true;
        }
        return false;
    }

}
