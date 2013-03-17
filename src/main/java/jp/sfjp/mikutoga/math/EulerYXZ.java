/*
 * YXZ Euler rotation
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sfjp.mikutoga.math;

/**
 * YXZオイラー角。
 * 三次元空間での方向及び姿勢を定義する。
 * <p>回転量はラジアンで表される。
 * <p>※XYZオイラー角ではない。
 */
public strictfp class EulerYXZ {

    private double xRot;
    private double yRot;
    private double zRot;


    /**
     * コンストラクタ。
     * <p>三軸とも回転量0の状態になる。
     */
    public EulerYXZ(){
        this(0.0, 0.0, 0.0);
        return;
    }

    /**
     * コンストラクタ。
     * @param xRot X軸回転量。(ラジアン)
     * @param yRot Y軸回転量。(ラジアン)
     * @param zRot Z軸回転量。(ラジアン)
     */
    public EulerYXZ(double xRot, double yRot, double zRot){
        super();
        this.xRot = xRot;
        this.yRot = yRot;
        this.zRot = zRot;
        return;
    }

    /**
     * X軸回転量を返す。
     * @return X軸回転量を返す。(ラジアン)
     */
    public double getXRot(){
        return this.xRot;
    }

    /**
     * Y軸回転量を返す。
     * @return Y軸回転量を返す。(ラジアン)
     */
    public double getYRot(){
        return this.yRot;
    }

    /**
     * Z軸回転量を返す。
     * @return Z軸回転量を返す。(ラジアン)
     */
    public double getZRot(){
        return this.zRot;
    }

    /**
     * X軸回転量を設定する。
     * @param xRotArg X軸回転量。(ラジアン)
     */
    public void setXRot(double xRotArg){
        this.xRot = xRotArg;
        return;
    }

    /**
     * Y軸回転量を設定する。
     * @param yRotArg Y軸回転量。(ラジアン)
     */
    public void setYRot(double yRotArg){
        this.yRot = yRotArg;
        return;
    }

    /**
     * Z軸回転量を設定する。
     * @param zRotArg Z軸回転量。(ラジアン)
     */
    public void setZRot(double zRotArg){
        this.zRot = zRotArg;
        return;
    }

    /**
     * 三軸の回転量を設定する。
     * @param xRotArg X軸回転量。(ラジアン)
     * @param yRotArg Y軸回転量。(ラジアン)
     * @param zRotArg Z軸回転量。(ラジアン)
     */
    public void setRot(double xRotArg, double yRotArg, double zRotArg){
        this.xRot = xRotArg;
        this.yRot = yRotArg;
        this.zRot = zRotArg;
        return;
    }

    /**
     * パラメータ情報の文字列化。
     * @param x x値
     * @param y y値
     * @param z z値
     * @return 文字列
     */
    private static String toString(double x, double y, double z){
        StringBuilder result = new StringBuilder();

        result.append("x=") .append(x);
        result.append(" y=").append(y);
        result.append(" z=").append(z);

        return result.toString();
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String toString(){
        String result;
        result = toString(this.xRot, this.yRot, this.zRot);
        return result;
    }

    /**
     * 度数法による文字列表現を返す。
     * @return 文字列表現
     */
    public String toDegString(){
        double xDeg = StrictMath.toDegrees(this.xRot);
        double yDeg = StrictMath.toDegrees(this.yRot);
        double zDeg = StrictMath.toDegrees(this.zRot);

        String result;
        result = toString(xDeg, yDeg, zDeg);
        return result;
    }

}
