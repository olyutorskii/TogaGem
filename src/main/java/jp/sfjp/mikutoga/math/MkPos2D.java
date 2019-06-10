/*
 * 2D position
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sfjp.mikutoga.math;

/**
 * 二次元空間座標及び変量を表す。
 *
 * <p>直交座標を二つの倍精度値で表す。
 *
 * <p>主な用途はUVマッピングなど。
 */
public strictfp class MkPos2D {

    private double xPos;
    private double yPos;

    /**
     * コンストラクタ。
     * [0,0]が設定される
     */
    public MkPos2D(){
        this(0.0, 0.0);
        return;
    }

    /**
     * コンストラクタ。
     *
     * @param xPosArg X座標
     * @param yPosArg Y座標
     */
    public MkPos2D(double xPosArg, double yPosArg){
        super();
        this.xPos = xPosArg;
        this.yPos = yPosArg;
        return;
    }

    /**
     * X座標を設定する。
     *
     * @param xPosArg X座標
     */
    public void setXpos(double xPosArg){
        this.xPos = xPosArg;
        return;
    }

    /**
     * X座標を返す。
     *
     * @return X座標
     */
    public double getXpos(){
        return this.xPos;
    }

    /**
     * Y座標を設定する。
     *
     * @param yPosArg Y座標
     */
    public void setYpos(double yPosArg){
        this.yPos = yPosArg;
        return;
    }

    /**
     * Y座標を返す。
     *
     * @return Y座標
     */
    public double getYpos(){
        return this.yPos;
    }

    /**
     * 座標を設定する。
     *
     * @param xPosArg X軸座標
     * @param yPosArg Y軸座標
     */
    public void setPosition(double xPosArg, double yPosArg){
        this.xPos = xPosArg;
        this.yPos = yPosArg;
        return;
    }

    /**
     * この点が原点(0,0)か否か判定する。
     *
     * @return 原点ならtrue
     */
    public boolean isOriginPoint(){
        if(this.xPos != 0.0) return false;
        if(this.yPos != 0.0) return false;
        return true;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();

        result.append("pos=[")
              .append(this.xPos).append(", ")
              .append(this.yPos).append(']');

        return result.toString();
    }

}
