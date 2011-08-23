/*
 * morph motion
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.model;

import jp.sourceforge.mikutoga.vmd.AbstractNumbered;

/**
 * モーフ情報。
 * <p>通常、モーフ量は0.0以上1.0以下の値をとる。
 */
public class MorphMotion extends AbstractNumbered {

    private String morphName = "";
    private float flex = 0.0f;


    /**
     * コンストラクタ。
     */
    public MorphMotion(){
        super();
        return;
    }


    /**
     * モーフ名を設定する。
     * @param morphName モーフ名
     */
    public void setMorphName(String morphName) {
        this.morphName = morphName;
        return;
    }

    /**
     * モーフ名を返す。
     * @return モーフ名
     */
    public String getMorphName() {
        return this.morphName;
    }

    /**
     * モーフ変量を設定する。
     * @param flex 変量
     */
    public void setFlex(float flex) {
        this.flex = flex;
        return;
    }

    /**
     * モーフ変量を返す。
     * @return 変量
     */
    public float getFlex() {
        return this.flex;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();

        result.append("morph name : [").append(this.morphName);
        result.append("] #").append(getFrameNumber());
        result.append(" flex = ").append(this.flex);

        return result.toString();
    }

}
