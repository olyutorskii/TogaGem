/*
 * 3d rotation (radian)
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sfjp.mikutoga.pmd;

/**
 * XYZ3軸によるジョイント回転量(radian)。
 * オイラー角か否か詳細は不明。
 * degereeではなくradian。(直角はΠ/2)
 */
public class Rad3d {

    private static final String DELIM = ", ";

    private float xRad;
    private float yRad;
    private float zRad;

    /**
     * コンストラクタ。
     */
    public Rad3d(){
        super();
        return;
    }

    /**
     * X軸回転量を設定する。
     *
     * @param xRadArg X軸回転量(radian)
     */
    public void setXRad(float xRadArg){
        this.xRad = xRadArg;
        return;
    }

    /**
     * X軸回転量を返す。
     *
     * @return X軸回転量(radian)
     */
    public float getXRad(){
        return this.xRad;
    }

    /**
     * Y軸回転量を設定する。
     *
     * @param yRadArg Y軸回転量(radian)
     */
    public void setYRad(float yRadArg){
        this.yRad = yRadArg;
        return;
    }

    /**
     * Y軸回転量を返す。
     *
     * @return Y軸回転量(radian)
     */
    public float getYRad(){
        return this.yRad;
    }

    /**
     * Z軸回転量を設定する。
     *
     * @param zRadArg Z軸回転量(radian)
     */
    public void setZRad(float zRadArg){
        this.zRad = zRadArg;
        return;
    }

    /**
     * Z軸回転量を返す。
     *
     * @return Z軸回転量(radian)
     */
    public float getZRad(){
        return this.zRad;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();

        result.append("rad=[")
              .append(this.xRad).append(DELIM)
              .append(this.yRad).append(DELIM)
              .append(this.zRad).append(']');

        return result.toString();
    }

}
