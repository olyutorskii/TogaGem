/*
 * unique bone information
 *
 * License : The MIT License
 * Copyright(c) 2013 MikuToga Partners
 */

package jp.sfjp.mikutoga.typical;

/**
 * 特別扱いされるボーンに関する諸々。
 */
public final class UniqBone {

    private static final String HIDARI_LEFT = "\u5de6";        // 「左」
    private static final String MIGI_RIGHT  = "\u53f3";        // 「右」
    private static final String HIZA_KNEE   = "\u3072\u3056"; // 「ひざ」


    private static final String KNEE_L_PFX =
            HIDARI_LEFT + HIZA_KNEE;          // 左ひざ
    private static final String KNEE_R_PFX =
            MIGI_RIGHT + HIZA_KNEE;           // 右ひざ

    static{
        assert "左ひざ".equals(KNEE_L_PFX);
        assert "右ひざ".equals(KNEE_R_PFX);
    }


    /**
     * 隠しコンストラクタ。
     */
    private UniqBone(){
        assert false;
        throw new AssertionError();
    }

    /**
     * IK演算時の回転方向に制限を受ける「ひざボーン」か否か、
     * ボーン名で判定する。
     * <p>ボーンのプライマリ名が「左ひざ」もしくは「右ひざ」で始まれば
     * ひざボーンとする。
     * <p>ひざボーン名の例
     * <ul>
     * <li>「左ひざ」
     * <li>「左ひざげり」
     * </ul>
     * <p>ひざボーン名ではない例
     * <ul>
     * <li>「左ひ」
     * <li>「ひざ」
     * <li>「前ひざ」
     * <li>「左ひさ゛」
     * <li>「左ヒザ」
     * <li>「左ﾋｻﾞ」
     * <li>「左膝」
     * <li>「Knee_L」
     * </ul>
     * @param boneNameJp プライマリボーン名
     * @return ひざボーンならtrue
     */
    public static boolean isPrimaryKneeName(String boneNameJp){
        if(boneNameJp.startsWith(KNEE_L_PFX)) return true;
        if(boneNameJp.startsWith(KNEE_R_PFX)) return true;

        return false;
    }

}
