/*
 * luminous direction vector
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.model;

/**
 * 光源からの照射方向を表す方向ベクトル。
 * <p>照明方向は、
 * ワールド座標原点から伸びる方向ベクトルとして記述される。
 * この方向ベクトルに向けて、無限遠の光源から照明が当たる。
 * <p>MMDのスライダUI上では各軸成分の定義域は-1.0以上+1.0以下だが、
 * さらに絶対値の大きな値を指定することも可能。
 * <p>方向ベクトルの長さは演出上の意味を持たないが、
 * キーフレーム間の照明方向の補間に影響を及ぼすかもしれない。
 * <p>方向ベクトルが零ベクトル(0,0,0)の場合、MMDでは全ポリゴンに影が落ちる。
 */
public class LuminousVector {

    /** デフォルトのX成分。 */
    public static final float DEF_VECX = -0.5f;
    /** デフォルトのY成分。 */
    public static final float DEF_VECY = -1.0f;
    /** デフォルトのZ成分。 */
    public static final float DEF_VECZ = +0.5f;


    private float vecX = DEF_VECX;
    private float vecY = DEF_VECY;
    private float vecZ = DEF_VECZ;


    /**
     * コンストラクタ。
     */
    public LuminousVector(){
        super();
        return;
    }


    /**
     * 照射方向ベクトルのX成分を設定する。
     * @param vecX ベクトルのX成分
     */
    public void setVecX(float vecX) {
        this.vecX = vecX;
        return;
    }

    /**
     * 照射方向ベクトルのY成分を設定する。
     * @param vecY ベクトルのY成分
     */
    public void setVecY(float vecY) {
        this.vecY = vecY;
        return;
    }

    /**
     * 照射方向ベクトルのZ成分を設定する。
     * @param vecZ ベクトルのZ成分
     */
    public void setVecZ(float vecZ) {
        this.vecZ = vecZ;
        return;
    }

    /**
     * 照射方向ベクトルのX成分を返す。
     * @return 方向ベクトルX成分
     */
    public float getVecX() {
        return this.vecX;
    }

    /**
     * 照射方向ベクトルのY成分を返す。
     * @return 方向ベクトルY成分
     */
    public float getVecY() {
        return this.vecY;
    }

    /**
     * 照射方向ベクトルのZ成分を返す。
     * @return 方向ベクトルZ成分
     */
    public float getVecZ() {
        return this.vecZ;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();

        result.append("x=").append(this.vecX);
        result.append(" y=").append(this.vecY);
        result.append(" z=").append(this.vecZ);

        return result.toString();
    }

}
