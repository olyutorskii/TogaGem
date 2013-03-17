/*
 * 3D position
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sfjp.mikutoga.math;

/**
 * 三次元位置情報。
 * <p>直交座標を三つの倍精度値で表す。
 * <p>具体的にはボーン位置やカメラターゲット位置など。
 */
public strictfp class MkPos3D {

    private double xPos;
    private double yPos;
    private double zPos;


    /**
     * コンストラクタ。
     */
    public MkPos3D(){
        this(0.0, 0.0, 0.0);
        return;
    }

    /**
     * コンストラクタ。
     * @param xPosArg X軸座標
     * @param yPosArg Y軸座標
     * @param zPosArg Z軸座標
     */
    public MkPos3D(double xPosArg, double yPosArg, double zPosArg){
        this.xPos = xPosArg;
        this.yPos = yPosArg;
        this.zPos = zPosArg;
        return;
    }

    /**
     * X軸座標を返す。
     * @return X軸座標
     */
    public double getXpos() {
        return this.xPos;
    }

    /**
     * Y軸座標を返す。
     * @return Y軸座標
     */
    public double getYpos() {
        return this.yPos;
    }

    /**
     * Z軸座標を返す。
     * @return Z軸座標
     */
    public double getZpos() {
        return this.zPos;
    }

    /**
     * X軸座標を設定する。
     * @param xPosArg X軸座標
     */
    public void setXpos(double xPosArg){
        this.xPos = xPosArg;
        return;
    }

    /**
     * Y軸座標を設定する。
     * @param yPosArg Y軸座標
     */
    public void setYpos(double yPosArg){
        this.yPos = yPosArg;
        return;
    }

    /**
     * Z軸座標を設定する。
     * @param zPosArg Z軸座標
     */
    public void setZpos(double zPosArg){
        this.zPos = zPosArg;
        return;
    }

    /**
     * 座標を設定する。
     * @param xPosArg X軸座標
     * @param yPosArg Y軸座標
     * @param zPosArg Z軸座標
     */
    public void setPosition(double xPosArg, double yPosArg, double zPosArg){
        this.xPos = xPosArg;
        this.yPos = yPosArg;
        this.zPos = zPosArg;
        return;
    }

    /**
     * この点が原点(0,0,0)か否か判定する。
     * @return 原点ならtrue
     */
    public boolean isOriginPoint(){
        if(this.xPos != 0.0) return false;
        if(this.yPos != 0.0) return false;
        if(this.zPos != 0.0) return false;
        return true;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();

        result.append("x=").append(this.xPos);
        result.append(" y=").append(this.yPos);
        result.append(" z=").append(this.zPos);

        return result.toString();
    }

}
