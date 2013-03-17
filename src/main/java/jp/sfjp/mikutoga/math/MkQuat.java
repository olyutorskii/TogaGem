/*
 * quaternion rotation
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sfjp.mikutoga.math;

/**
 * クォータニオンによる回転表現。
 * <p>虚部q1,q2,q3と実部qwから構成される。
 */
public strictfp class MkQuat {

    private static final double HALF_PI = StrictMath.PI / 2.0;
    private static final double EPSILON = StrictMath.ulp(1.0);
    private static final double TDELTA = EPSILON * 4;

    static{
        assert StrictMath.ulp(StrictMath.PI) <= TDELTA;
    }


    private double q1;
    private double q2;
    private double q3;
    private double qw;


    /**
     * コンストラクタ。
     * <p>虚部が全て0.0、実部が1.0となる。
     */
    public MkQuat(){
        this(0.0, 0.0, 0.0, 1.0);
        return;
    }

    /**
     * コンストラクタ。
     * @param q コピー元クォータニオン
     */
    public MkQuat(MkQuat q){
        this(q.q1, q.q2, q.q3, q.qw);
        return;
    }

    /**
     * コンストラクタ。
     * @param q1 虚部1
     * @param q2 虚部2
     * @param q3 虚部3
     * @param qw 実部
     */
    public MkQuat(double q1, double q2, double q3, double qw){
        super();
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
        this.qw = qw;
        return;
    }


    /**
     * クォータニオン積を求め格納する。
     * <p>クォータニオン積では交換則が成り立たない。
     * <p>引数は同一インスタンスを含んでもよい。
     * @param qA 積前項
     * @param qB 積後項
     * @param result 積の格納先
     */
    public static void mul(MkQuat qA,
                           MkQuat qB,
                           MkQuat result ){
        double aq1 = qA.getQ1();
        double aq2 = qA.getQ2();
        double aq3 = qA.getQ3();
        double aqw = qA.getQW();

        double bq1 = qB.getQ1();
        double bq2 = qB.getQ2();
        double bq3 = qB.getQ3();
        double bqw = qB.getQW();

        double rq1;
        double rq2;
        double rq3;
        double rqw;

        rq1 = aq2 * bq3 - aq3 * bq2 + aqw * bq1 + aq1 * bqw;
        rq2 = aq3 * bq1 - aq1 * bq3 + aqw * bq2 + aq2 * bqw;
        rq3 = aq1 * bq2 - aq2 * bq1 + aqw * bq3 + aq3 * bqw;
        rqw = aqw * bqw - aq1 * bq1 - aq2 * bq2 - aq3 * bq3;

        result.q1 = rq1;
        result.q2 = rq2;
        result.q3 = rq3;
        result.qw = rqw;

        return;
    }

    /**
     * 共役(共軛)クォータニオンを求め格納する。
     * <p>引数は同一インスタンスでもよい。
     * @param q クォータニオン
     * @param result 格納先
     */
    public static void conjugate(MkQuat q, MkQuat result){
        result.q1 = -q.q1;
        result.q2 = -q.q2;
        result.q3 = -q.q3;
        result.qw =  q.qw;
        return;
    }

    /**
     * 単位クォータニオンを求め格納する。
     * <p>引数は同一インスタンスでもよい。
     * @param q クォータニオン
     * @param result 格納先
     */
    public static void normalize(MkQuat q, MkQuat result){
        double abs = q.abs();

        double nq1 = q.q1 / abs;
        double nq2 = q.q2 / abs;
        double nq3 = q.q3 / abs;
        double nqw = q.qw / abs;

        result.q1 = nq1;
        result.q2 = nq2;
        result.q3 = nq3;
        result.qw = nqw;

        return;
    }

    /**
     * 逆元クォータニオンを求め格納する。
     * <p>対象クォータニオンの絶対値が小さい場合、
     * 無限大が虚部実部に入る可能性がある。
     * <p>引数は同一インスタンスでもよい。
     * @param q クォータニオン
     * @param result 格納先
     */
    public static void inverse(MkQuat q, MkQuat result){
        double sum = 0.0;
        sum += q.q1 * q.q1;
        sum += q.q2 * q.q2;
        sum += q.q3 * q.q3;
        sum += q.qw * q.qw;

        double nq1 = -q.q1 / sum;
        double nq2 = -q.q2 / sum;
        double nq3 = -q.q3 / sum;
        double nqw =  q.qw / sum;

        result.q1 = nq1;
        result.q2 = nq2;
        result.q3 = nq3;
        result.qw = nqw;

        return;
    }


    /**
     * 虚部1を返す。
     * @return 虚部1
     */
    public double getQ1() {
        return this.q1;
    }

    /**
     * 虚部2を返す。
     * @return 虚部2
     */
    public double getQ2() {
        return this.q2;
    }

    /**
     * 虚部3を返す。
     * @return 虚部3
     */
    public double getQ3() {
        return this.q3;
    }

    /**
     * 実部を返す。
     * @return 実部
     */
    public double getQW() {
        return this.qw;
    }

    /**
     * 虚部1を設定する。
     * @param q1Arg 虚部1
     */
    public void setQ1(double q1Arg) {
        this.q1 = q1Arg;
        return;
    }

    /**
     * 虚部2を設定する。
     * @param q2Arg 虚部2
     */
    public void setQ2(double q2Arg) {
        this.q2 = q2Arg;
        return;
    }

    /**
     * 虚部3を設定する。
     * @param q3Arg 虚部3
     */
    public void setQ3(double q3Arg) {
        this.q3 = q3Arg;
        return;
    }

    /**
     * 実部を設定する。
     * @param wArg 実部
     */
    public void setQW(double wArg) {
        this.qw = wArg;
        return;
    }

    /**
     * 虚部実部を設定する。
     * @param q1Arg 虚部1
     * @param q2Arg 虚部2
     * @param q3Arg 虚部3
     * @param wArg 実部
     */
    public void setQ123W(double q1Arg, double q2Arg, double q3Arg,
                          double wArg ){
        this.q1 = q1Arg;
        this.q2 = q2Arg;
        this.q3 = q3Arg;
        this.qw = wArg;
        return;
    }

    /**
     * クォータニオンの絶対値を返す。
     * @return クォータニオンの絶対値
     */
    public double abs(){
        double sum = 0.0;
        sum += this.q1 * this.q1;
        sum += this.q2 * this.q2;
        sum += this.q3 * this.q3;
        sum += this.qw * this.qw;
        double result = StrictMath.sqrt(sum);
        return result;
    }

    /**
     * 位置情報を読み込む。
     * <p>虚部q1,q2,q3にX,Y,Z軸の変量が入る。
     * <p>実部には0が入る。
     * @param xPos X位置
     * @param yPos Y位置
     * @param zPos Z位置
     */
    public void setPos3D(double xPos, double yPos, double zPos){
        this.q1 = xPos;
        this.q2 = yPos;
        this.q3 = zPos;
        this.qw = 0.0;
        return;
    }

    /**
     * 位置情報を読み込む。
     * <p>虚部q1,q2,q3にX,Y,Z軸の変量が入る。
     * <p>実部には0が入る。
     * @param pos 位置情報
     */
    public void setPos3D(MkPos3D pos){
        setPos3D(pos.getXpos(), pos.getYpos(), pos.getZpos());
        return;
    }

    /**
     * YXZオイラー角を読み込む。
     * <p>Y軸回転、X軸回転、Z軸回転の順に
     * 個別回転クォータニオンの積をとったものと等しい。
     * @param xRot X軸回転量(ラジアン)。第2軸
     * @param yRot Y軸回転量(ラジアン)。第1軸
     * @param zRot Z軸回転量(ラジアン)。第3軸
     */
    public void setEulerYXZ(double xRot, double yRot, double zRot){
        double hx = xRot / 2.0;
        double hy = yRot / 2.0;
        double hz = zRot / 2.0;

        double chx = StrictMath.cos(hx);
        double chy = StrictMath.cos(hy);
        double chz = StrictMath.cos(hz);

        double shx = StrictMath.sin(hx);
        double shy = StrictMath.sin(hy);
        double shz = StrictMath.sin(hz);

        this.q1 = chy * shx * chz + shy * chx * shz;
        this.q2 = shy * chx * chz - chy * shx * shz;
        this.q3 = chy * chx * shz - shy * shx * chz;
        this.qw = chy * chx * chz + shy * shx * shz;

        return;
    }

    /**
     * YXZオイラー角を読み込む。
     * <p>Y軸回転、X軸回転、Z軸回転の順に
     * 個別回転クォータニオンの積をとったものと等しい。
     * @param rot YXZオイラー角
     */
    public void setEulerYXZ(EulerYXZ rot){
        setEulerYXZ(rot.getXRot(), rot.getYRot(), rot.getZRot());
        return;
    }

    /**
     * クォータニオンをYXZオイラー角へと変換する。
     * <p>ジンバルロック時のYZ配分が指定可能。
     * @param result YXZオイラー角
     * @param oldY ジンバルロック時(オイラー角Xが直角etc.)
     * に使われるY軸回転量
     */
    public void toEulerYXZ(EulerYXZ result, double oldY){
        double qx = this.q1;
        double qy = this.q2;
        double qz = this.q3;
        double qqw = this.qw;

        double qx2 = qx * qx;
        double qy2 = qy * qy;
        double qz2 = qz * qz;

        double qwx = qqw * qx;
        double qwy = qqw * qy;
        double qwz = qqw * qz;

        double qxy = qx * qy;
        double qxz = qx * qz;
        double qyz = qy * qz;

        double m00 = 1.0 - 2.0 * (qy2 + qz2);
        double m01 = 2.0 * (qxy - qwz);
        double m02 = 2.0 * (qwy + qxz);

        double m10 = 2.0 * (qxy + qwz);
        double m11 = 1.0 - 2.0 * (qx2 + qz2);
        double m12 = 2.0 * (qyz - qwx);

//      double m20 = 2.0 * (qxz - qwy);
//      double m21 = 2.0 * (qwx + qyz);
        double m22 = 1.0 - 2.0 * (qx2 + qy2);

        double resultX;
        double resultY;
        double resultZ;

        if     (m12 < -1.0) resultX = +HALF_PI;
        else if(m12 > +1.0) resultX = -HALF_PI;
        else                resultX = StrictMath.asin(-m12);

        if(   StrictMath.abs(m11) <= TDELTA    // Y,Zが一意に定まらない場合
           || StrictMath.abs(m22) <= TDELTA ){
            resultY = oldY;
            resultZ = StrictMath.atan2(-m01, m00) + oldY;
        }else{
            resultY = StrictMath.atan2(m02, m22);
            resultZ = StrictMath.atan2(m10, m11);
        }

        result.setXRot(resultX);
        result.setYRot(resultY);
        result.setZRot(resultZ);

        return;
    }

    /**
     * クォータニオンをYXZオイラー角へと変換する。
     * @param result YXZオイラー角
     */
    public void toEulerYXZ(EulerYXZ result){
        toEulerYXZ(result, 0.0);
        return;
    }

    /**
     * 回転クォータニオンを用いて点座標を回転させる。
     * 座標インスタンスは同一でもよい。
     * @param pos 点座標
     * @param result 格納先
     */
    public void rotatePos(MkPos3D pos, MkPos3D result){
        // 回転クォータニオンr (Q)
        double rQ1 = this.q1;
        double rQ2 = this.q2;
        double rQ3 = this.q3;
        double rQW = this.qw;

        // 点座標p (P)
        double pQ1 = pos.getXpos();
        double pQ2 = pos.getYpos();
        double pQ3 = pos.getZpos();
        double pQW = 0.0;

        // 共役クォータニオンrr (Q')
        double rrQ1 = -rQ1;
        double rrQ2 = -rQ2;
        double rrQ3 = -rQ3;
        double rrQW =  rQW;

        // QP
        double rpQ1;
        double rpQ2;
        double rpQ3;
        double rpQW;

        rpQ1 = rQ2 * pQ3 - rQ3 * pQ2 + rQW * pQ1 + rQ1 * pQW;
        rpQ2 = rQ3 * pQ1 - rQ1 * pQ3 + rQW * pQ2 + rQ2 * pQW;
        rpQ3 = rQ1 * pQ2 - rQ2 * pQ1 + rQW * pQ3 + rQ3 * pQW;
        rpQW = rQW * pQW - rQ1 * pQ1 - rQ2 * pQ2 - rQ3 * pQ3;

        // QPQ'
        double rprrQ1;
        double rprrQ2;
        double rprrQ3;
//      double rprrQW;

        rprrQ1 = rpQ2 * rrQ3 - rpQ3 * rrQ2 + rpQW * rrQ1 + rpQ1 * rrQW;
        rprrQ2 = rpQ3 * rrQ1 - rpQ1 * rrQ3 + rpQW * rrQ2 + rpQ2 * rrQW;
        rprrQ3 = rpQ1 * rrQ2 - rpQ2 * rrQ1 + rpQW * rrQ3 + rpQ3 * rrQW;
//      rprrQW = rpQW * rrQW - rpQ1 * rrQ1 - rpQ2 * rrQ2 - rpQ3 * rrQ3;

//      assert rprrQW == 0.0;

        result.setXpos(rprrQ1);
        result.setYpos(rprrQ2);
        result.setZpos(rprrQ3);

        return;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        result.append("q1=") .append(this.q1);
        result.append(" q2=").append(this.q2);
        result.append(" q3=").append(this.q3);
        result.append(" w=") .append(this.qw);
        return  result.toString();
    }

}
