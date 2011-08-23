/*
 * YXZ Euler rotation
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.math;

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
     * @param xRot X軸回転量。(ラジアン)
     */
    public void setXRot(double xRot){
        this.xRot = xRot;
        return;
    }

    /**
     * Y軸回転量を設定する。
     * @param yRot Y軸回転量。(ラジアン)
     */
    public void setYRot(double yRot){
        this.yRot = yRot;
        return;
    }

    /**
     * Z軸回転量を設定する。
     * @param zRot Z軸回転量。(ラジアン)
     */
    public void setZRot(double zRot){
        this.zRot = zRot;
        return;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();

        result.append("x=") .append(this.xRot);
        result.append(" y=").append(this.yRot);
        result.append(" z=").append(this.zRot);

        return result.toString();
    }

    /**
     * 度数法による文字列表現を返す。
     * @return 文字列表現
     */
    public String toDegString(){
        StringBuilder result = new StringBuilder();

        result.append("x=") .append(StrictMath.toDegrees(this.xRot));
        result.append(" y=").append(StrictMath.toDegrees(this.yRot));
        result.append(" z=").append(StrictMath.toDegrees(this.zRot));

        return result.toString();
    }

}
