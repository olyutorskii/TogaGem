/*
 * luminous color
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.model;

/**
 * 光源の色設定。
 * <p>色情報はRGB色空間で記述される。
 * <p>MMDのUI上の各色成分指定0～255定義域に便宜上256を追加したものが、
 * 0.0以上1.0以下にマップされる。
 * <ul>
 * <li>0は正しく0.0にマップされる。
 * <li>128は正しく0.5にマップされる。
 * <li>255は1.0より少しだけ小さい数(≒0.99609375)にマップされる。
 * </ul>
 */
public class LuminousColor {

    /** デフォルトの成分値。 */
    public static final float DEF_BRIGHT = 0.602f; // ≒ (154.0 / 256.0)


    private float colR = DEF_BRIGHT;
    private float colG = DEF_BRIGHT;
    private float colB = DEF_BRIGHT;


    /**
     * コンストラクタ。
     * <p>MMDデフォルトの光源色(154,154,154)が設定される。
     */
    public LuminousColor(){
        super();
        return;
    }


    /**
     * 光源の赤成分を設定する。
     * @param colR 赤成分
     */
    public void setColR(float colR) {
        this.colR = colR;
        return;
    }

    /**
     * 光源の緑成分を設定する。
     * @param colG 緑成分
     */
    public void setColG(float colG) {
        this.colG = colG;
        return;
    }

    /**
     * 光源の青成分を設定する。
     * @param colB 青成分
     */
    public void setColB(float colB) {
        this.colB = colB;
        return;
    }

    /**
     * 光源の赤成分を返す。
     * @return 赤成分
     */
    public float getColR(){
        return this.colR;
    }

    /**
     * 光源の緑成分を返す。
     * @return 緑成分
     */
    public float getColG(){
        return this.colG;
    }

    /**
     * 光源の青成分を返す。
     * @return 青成分
     */
    public float getColB(){
        return this.colB;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();

        result.append("r=").append(this.colR);
        result.append(" g=").append(this.colG);
        result.append(" b=").append(this.colB);

        return result.toString();
    }

}
