/*
 * camera motion
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.model;

import jp.sourceforge.mikutoga.math.MkPos3D;
import jp.sourceforge.mikutoga.vmd.AbstractNumbered;

/**
 * カメラモーション情報。
 * <p>カメラの回転は極座標で表される。
 * <p>カメラ-ターゲット間の距離は球座標(極座標)の動径に相当する。
 * 通常はターゲットより手前に位置するカメラまでの距離が負の値で表される。
 * カメラ位置がターゲットを突き抜けた場合は正の値もとりうる。
 * ※MMDのUIと符号が逆なので注意。
 * <p>パースペクティブモードがOFFの場合、
 * 画角は無視され遠近感処理が行われなくなる。(平行投影?)
 */
public class CameraMotion extends AbstractNumbered {

    private final MkPos3D cameraTarget = new MkPos3D();
    private final PosCurve posCurve = new PosCurve();

    private final CameraRotation cameraRotation = new CameraRotation();
    private final BezierParam intpltRotation = new BezierParam();

    private float range;
    private final BezierParam intpltRange = new BezierParam();

    private boolean hasPerspective;
    private int projectionAngle;
    private final BezierParam intpltProjection = new BezierParam();


    /**
     * コンストラクタ。
     */
    public CameraMotion(){
        super();
        return;
    }


    /**
     * ターゲット位置情報を返す。
     * @return ターゲット位置情報
     */
    public MkPos3D getCameraTarget(){
        return this.cameraTarget;
    }

    /**
     * ターゲット位置移動の補間情報を返す。
     * @return ターゲット位置移動の補間情報
     */
    public PosCurve getTargetPosCurve(){
        return this.posCurve;
    }

    /**
     * カメラ回転情報を返す。
     * @return カメラ回転情報
     */
    public CameraRotation getCameraRotation(){
        return this.cameraRotation;
    }

    /**
     * カメラ回転の補間曲線情報を返す。
     * @return カメラ回転の補間曲線情報
     */
    public BezierParam getIntpltRotation(){
        return this.intpltRotation;
    }

    /**
     * カメラ-ターゲット間の距離を返す。
     * @return カメラ-ターゲット間の距離
     */
    public float getRange(){
        return this.range;
    }

    /**
     * カメラ-ターゲット間の距離を設定する。
     * @param range カメラ-ターゲット間の距離
     */
    public void setRange(float range){
        this.range = range;
        return;
    }

    /**
     * カメラ-ターゲット間距離の補間曲線情報を返す。
     * @return カメラ-ターゲット間距離の補間曲線情報
     */
    public BezierParam getIntpltRange(){
        return this.intpltRange;
    }

    /**
     * パースペクティブが有効か判定する。
     * @return パースペクティブが有効ならtrue
     */
    public boolean hasPerspective(){
        return this.hasPerspective;
    }

    /**
     * パースペクティブモードを設定する。
     * @param mode trueを渡すとパースペクティブが有効になる。
     */
    public void setPerspectiveMode(boolean mode){
        this.hasPerspective = mode;
        return;
    }

    /**
     * 投影角度(スクリーン縦画角)を返す。
     * @return 投影角度(度数法)
     */
    public int getProjectionAngle(){
        return this.projectionAngle;
    }

    /**
     * 投影角度(スクリーン縦画角)を設定する。
     * @param angle 投影角度(度数法)
     */
    public void setProjectionAngle(int angle){
        this.projectionAngle = angle;
        return;
    }

    /**
     * スクリーン投射の補間曲線情報を返す。
     * @return スクリーン投射の補間曲線情報
     */
    public BezierParam getIntpltProjection(){
        return this.intpltProjection;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();

        result.append("#").append(getFrameNumber()).append(' ');
        result.append(this.cameraRotation);
        result.append(" Rot-Bezier ")
                .append(this.intpltRotation).append('\n');

        result.append("range : ").append(this.range);
        result.append(" Range-Bezier ").append(this.intpltRange).append('\n');

        result.append("target-pos : ").append(this.cameraTarget).append('\n');
        result.append(this.posCurve).append('\n');

        result.append("perspective : ");
        if(this.hasPerspective) result.append("ON");
        else                    result.append("OFF");
        result.append('\n');

        result.append("projection angle : ").append(this.projectionAngle);
        result.append("deg Bezier ").append(this.intpltProjection);

        return result.toString();
    }

}
