/*
 * luminous motion
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.model;

import jp.sourceforge.mikutoga.vmd.AbstractNumbered;

/**
 * 光源演出情報。
 */
public class LuminousMotion extends AbstractNumbered {

    private final LuminousColor color = new LuminousColor();
    private final LuminousVector direction = new LuminousVector();


    /**
     * コンストラクタ。
     */
    public LuminousMotion(){
        super();
        return;
    }


    /**
     * 光源の色情報を返す。
     * @return 光源の色情報
     */
    public LuminousColor getColor(){
        return this.color;
    }

    /**
     * 光源からの照射方向情報を返す。
     * @return 光源からの照射方向情報
     */
    public LuminousVector getDirection(){
        return this.direction;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();

        result.append("#").append(getFrameNumber());
        result.append(" luminous color : ").append(this.color);
        result.append(" direction : ").append(this.direction);

        return result.toString();
    }

}
