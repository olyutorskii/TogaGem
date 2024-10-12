/*
 * 3D vector
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sfjp.mikutoga.math;

/**
 * XYZ三次元ベクトル。
 */
public strictfp class MkVec3D {

    private static final String DELIM = ", ";

    private double xVal;
    private double yVal;
    private double zVal;

    /**
     * コンストラクタ。
     */
    public MkVec3D(){
        this(0.0, 0.0, 0.0);
        return;
    }

    /**
     * コンストラクタ。
     *
     * @param xValArg X値
     * @param yValArg Y値
     * @param zValArg Z値
     */
    public MkVec3D(double xValArg, double yValArg, double zValArg){
        this.xVal = xValArg;
        this.yVal = yValArg;
        this.zVal = zValArg;
        return;
    }

    /**
     * X値を設定する。
     *
     * @param xValArg X値
     */
    public void setXVal(double xValArg){
        this.xVal = xValArg;
        return;
    }

    /**
     * X値を返す。
     *
     * @return X値
     */
    public double getXVal(){
        return this.xVal;
    }

    /**
     * Y値を設定する。
     *
     * @param yValArg Y値
     */
    public void setYVal(double yValArg){
        this.yVal = yValArg;
        return;
    }

    /**
     * Y値を返す。
     *
     * @return Y値
     */
    public double getYVal(){
        return this.yVal;
    }

    /**
     * Z値を設定する。
     *
     * @param zValArg Z値
     */
    public void setZVal(double zValArg){
        this.zVal = zValArg;
        return;
    }

    /**
     * Z値を返す。
     *
     * @return Z値
     */
    public double getZVal(){
        return this.zVal;
    }

    /**
     * ベクトル成分を設定する。
     *
     * @param xValArg X値
     * @param yValArg Y値
     * @param zValArg Z値
     */
    public void setVector(double xValArg, double yValArg, double zValArg){
        this.xVal = xValArg;
        this.yVal = yValArg;
        this.zVal = zValArg;
        return;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();

        result.append("vec=[")
              .append(this.xVal).append(DELIM)
              .append(this.yVal).append(DELIM)
              .append(this.zVal).append(']');

        return result.toString();
    }

}
