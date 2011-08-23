/*
 * shadow motion
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.model;

import jp.sourceforge.mikutoga.vmd.AbstractNumbered;

/**
 * 影(セルフシャドウ)演出情報。
 * <p>カメラからの距離情報(幾何距離の100倍？)による影演出対象の範囲指定は、
 * MMDのスライダUI上では0から9999までが指定可能。
 * <p>MMDのスライダUI値SからVMDファイル上の生パラメターへの変換式は、
 * 「 0.1 - (S / 1.0E+5) 」
 * となる。
 */
public class ShadowMotion extends AbstractNumbered {

    /**
     * デフォルトの影描画モード。
     */
    public static final ShadowMode DEF_MODE = ShadowMode.MODE_1;

    /**
     * デフォルトの範囲指定生パラメータ。
     * <p>MMDのスライダUI値「8875」にほぼ相当。
     */
    public static final float DEF_SCOPE = 0.01125f;

    private static final double OFFSET = 0.1;
    private static final double SCALE = 1.0E+5;


    private ShadowMode shadowMode = DEF_MODE;
    private float rawScopeParam = DEF_SCOPE;


    /**
     * コンストラクタ。
     */
    public ShadowMotion(){
        super();
        return;
    }


    /**
     * VMDファイル上の生パラメータ数値による演出対象範囲指定を、
     * MMDのUI上の距離情報(カメラからの幾何距離×100倍？)に変換する。
     * @param param 生パラメータ
     * @return MMDのスライダUI上の距離情報
     */
    public static double rawParamToScope(float param){
        double result;
        result = OFFSET - param;
        result *= SCALE;
        return result;
    }

    /**
     * MMDのUI上の距離情報(カメラからの幾何距離×100倍？)を、
     * VMDファイル上の生パラメータ数値に変換する。
     * @param scope MMDのスライダUI上の距離情報
     * @return 生パラメータ
     */
    public static float scopeToRawParam(double scope){
        double result;
        result = scope / SCALE;
        result = OFFSET - result;
        return (float) result;
    }


    /**
     * 影演出の範囲指定の生パラメータを設定する。
     * @param rawScopeParam 生パラメータ
     */
    public void setRawScopeParam(float rawScopeParam) {
        this.rawScopeParam = rawScopeParam;
        return;
    }

    /**
     * 影演出の範囲指定の生パラメータを返す。
     * @return 生パラメータ
     */
    public float getRawScopeParam() {
        return this.rawScopeParam;
    }

    /**
     * 影演出の範囲指定のスライダUI値を設定する。
     * @param scope スライダUI値
     */
    public void setScope(double scope){
        float rawVal = scopeToRawParam(scope);
        setRawScopeParam(rawVal);
        return;
    }

    /**
     * 影演出の範囲指定のスライダUI値を返す。
     * @return スライダUI値
     */
    public double getScope(){
        float rawVal = getRawScopeParam();
        double scope = rawParamToScope(rawVal);
        return scope;
    }

    /**
     * 影描画モードを設定する。
     * @param shadowMode 影描画モード
     * @throws NullPointerException 引数がnull
     */
    public void setShadowMode(ShadowMode shadowMode)
            throws NullPointerException{
        if(shadowMode == null) throw new NullPointerException();
        this.shadowMode = shadowMode;
        return;
    }

    /**
     * 影描画モードを返す。
     * @return 影描画モード
     */
    public ShadowMode getShadowMode(){
        return this.shadowMode;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();

        result.append("#").append(getFrameNumber());
        result.append(" shadow mode : ").append(this.shadowMode);
        result.append(" rawparam=").append(this.rawScopeParam);

        return result.toString();
    }

}
