/*
 * 3D vector
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sourceforge.mikutoga.math;

/**
 * XYZ三次元ベクトル。
 */
public class MkVec3D {

    private double xVal;
    private double yVal;
    private double zVal;

    /**
     * コンストラクタ。
     */
    public MkVec3D(){
        super();
        return;
    }

    /**
     * X値を設定する。
     * @param xVal X値
     */
    public void setXVal(double xVal){
        this.xVal = xVal;
        return;
    }

    /**
     * X値を返す。
     * @return X値
     */
    public double getXVal(){
        return this.xVal;
    }

    /**
     * Y値を設定する。
     * @param yVal Y値
     */
    public void setYVal(double yVal){
        this.yVal = yVal;
        return;
    }

    /**
     * Y値を返す。
     * @return Y値
     */
    public double getYVal(){
        return this.yVal;
    }

    /**
     * Z値を設定する。
     * @param zVal Z値
     */
    public void setZVal(double zVal){
        this.zVal = zVal;
        return;
    }

    /**
     * Z値を返す。
     * @return Z値
     */
    public double getZVal(){
        return this.zVal;
    }

    /**
     * ベクトル成分を設定する。
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
     * @return {@inheritDoc}
     */
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();

        result.append("vec=[")
              .append(this.xVal).append(", ")
              .append(this.yVal).append(", ")
              .append(this.zVal).append(']');

        return result.toString();
    }

}
