/*
 * implemented FrameNumbered
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd;

/**
 * FrameNumberedの抽象実装クラス。
 */
public abstract class AbstractNumbered implements FrameNumbered {

    private int frameNo = 0;


    /**
     * コンストラクタ。
     */
    protected AbstractNumbered(){
        super();
        return;
    }


    /**
     * {@inheritDoc}
     * @param num {@inheritDoc}
     */
    @Override
    public void setFrameNumber(int num){
        this.frameNo = num;
        return;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public int getFrameNumber(){
        return this.frameNo;
    }

}
