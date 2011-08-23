/*
 * 3D bezier intaerpolation curve params
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.model;

/**
 * 三次ベジェ曲線による補間カーブを記述する。
 * <p>制御点P0,P1,P2,P3の座標により記述される。
 * <p>P0は(0,0)で固定。P3は(127,127)で固定。
 * 残りのP1,P2は、P0,P3を対角線とする正方形の内部に位置しなければならない。
 */
public class BezierParam {

    /**
     * 制御点P1のXデフォルト値(直線補間)。
     */
    public static final byte DEF_P1X = 20;
    /**
     * 制御点P1のYデフォルト値(直線補間)。
     */
    public static final byte DEF_P1Y = 20;
    /**
     * 制御点P2のXデフォルト値(直線補間)。
     */
    public static final byte DEF_P2X = 107;
    /**
     * 制御点P2のYデフォルト値(直線補間)。
     */
    public static final byte DEF_P2Y = 107;

    /**
     * 制御点P1のXデフォルト値(EaseInOut)。
     */
    public static final byte EIO_P1X = 64;
    /**
     * 制御点P1のYデフォルト値(EaseInOut)。
     */
    public static final byte EIO_P1Y =  0;
    /**
     * 制御点P2のXデフォルト値(EaseInOut)。
     */
    public static final byte EIO_P2X = 64;
    /**
     * 制御点P2のYデフォルト値(EaseInOut)。
     */
    public static final byte EIO_P2Y = 127;

    /**
     * 制御点空間最小値。
     */
    public static final byte MIN_VAL = 0;

    /**
     * 制御点空間最大値。
     */
    public static final byte MAX_VAL = 127;


    private byte p1x = DEF_P1X;
    private byte p1y = DEF_P1Y;
    private byte p2x = DEF_P2X;
    private byte p2y = DEF_P2Y;


    /**
     * コンストラクタ。
     * <p>デフォルトの直線補間が設定される。
     */
    public BezierParam(){
        super();
        return;
    }

    /**
     * 制御点P1のX座標を返す。
     * @return 制御点P1のX座標
     */
    public byte getP1x() {
        return this.p1x;
    }

    /**
     * 制御点P1のY座標を返す。
     * @return 制御点P1のY座標
     */
    public byte getP1y() {
        return this.p1y;
    }

    /**
     * 制御点P2のX座標を返す。
     * @return 制御点P2のX座標
     */
    public byte getP2x() {
        return this.p2x;
    }

    /**
     * 制御点P2のY座標を返す。
     * @return 制御点P2のY座標
     */
    public byte getP2y() {
        return this.p2y;
    }

    /**
     * 制御点P1のX座標を設定する。
     * @param p1x 制御点P1のX座標
     */
    public void setP1x(byte p1x) {
        this.p1x = p1x;
        return;
    }

    /**
     * 制御点P1のY座標を設定する。
     * @param p1y 制御点P1のY座標
     */
    public void setP1y(byte p1y) {
        this.p1y = p1y;
        return;
    }

    /**
     * 制御点P2のX座標を設定する。
     * @param p2x 制御点P2のX座標
     */
    public void setP2x(byte p2x) {
        this.p2x = p2x;
        return;
    }

    /**
     * 制御点P2のY座標を設定する。
     * @param p2y 制御点P2のY座標
     */
    public void setP2y(byte p2y) {
        this.p2y = p2y;
        return;
    }

    /**
     * 制御点P1の座標を設定する。
     * @param p1xArg 制御点P1のX座標
     * @param p1yArg 制御点P1のY座標
     */
    public void setP1(byte p1xArg, byte p1yArg) {
        this.p1x = p1xArg;
        this.p1y = p1yArg;
        return;
    }

    /**
     * 制御点P2の座標を設定する。
     * @param p2xArg 制御点P2のX座標
     * @param p2yArg 制御点P2のY座標
     */
    public void setP2(byte p2xArg, byte p2yArg) {
        this.p2x = p2xArg;
        this.p2y = p2yArg;
        return;
    }

    /**
     * 直線補間か判定する。
     * <p>P1,P2双方がP0-P3対角線上に存在する場合を直線補間とする。
     * @return 直線補間ならtrue
     */
    public boolean isLinear(){
        if(this.p1x != this.p1y) return false;
        if(this.p2x != this.p2y) return false;
        return true;
    }

    /**
     * MMDデフォルトの直線補間か判定する。
     * @return MMDデフォルトの直線補間ならtrue
     */
    public boolean isDefaultLinear(){
        if(this.p1x != DEF_P1X) return false;
        if(this.p1y != DEF_P1Y) return false;

        if(this.p2x != DEF_P2X) return false;
        if(this.p2y != DEF_P2Y) return false;

        assert isLinear();

        return true;
    }

    /**
     * MMDデフォルトのEaseInOutカーブか判定する。
     * @return  MMDデフォルトのEaseInOutカーブならtrue
     */
    public boolean isDefaultEaseInOut(){
        if(this.p1x != EIO_P1X) return false;
        if(this.p1y != EIO_P1Y) return false;

        if(this.p2x != EIO_P2X) return false;
        if(this.p2y != EIO_P2Y) return false;

        return true;
    }

    /**
     * MMDデフォルトの直線補間に設定する。
     */
    public void setDefaultLinear(){
        this.p1x = DEF_P1X;
        this.p1y = DEF_P1Y;
        this.p2x = DEF_P2X;
        this.p2y = DEF_P2Y;
        return;
    }

    /**
     * MMDデフォルトのEaseInOutカーブに設定する。
     */
    public void setDefaultEaseInOut(){
        this.p1x = EIO_P1X;
        this.p1y = EIO_P1Y;
        this.p2x = EIO_P2X;
        this.p2y = EIO_P2Y;
        return;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();

        String delim = ", ";
        result.append("P1=(")
                .append(this.p1x).append(delim).append(this.p1y).append(") ");
        result.append("P2=(")
                .append(this.p2x).append(delim).append(this.p2y).append(")");

        return result.toString();
    }

}
