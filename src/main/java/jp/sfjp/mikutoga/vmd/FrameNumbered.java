/*
 * frame-numbered interface
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sfjp.mikutoga.vmd;

import java.util.Comparator;

/**
 * キーフレーム番号を持つオブジェクトの抽象化インタフェース。
 */
public interface FrameNumbered {

    /** 昇順での比較子。 */
    public static final Comparator<FrameNumbered> COMPARATOR =
            new FrameComparator();

    /**
     * フレーム番号を設定する。
     *
     * @param num フレーム番号
     */
    public abstract void setFrameNumber(int num);

    /**
     * フレーム番号を返す。
     *
     * @return フレーム番号
     */
    public abstract int getFrameNumber();


    /**
     * フレーム番号による比較子Comparator。
     * フレーム番号の昇順を定義づける。
     */
    @SuppressWarnings("serial")
    public static class FrameComparator
            implements Comparator<FrameNumbered> {

        /**
         * コンストラクタ。
         */
        public FrameComparator(){
            super();
            return;
        }

        /**
         * {@inheritDoc}
         *
         * @param o1 {@inheritDoc}
         * @param o2 {@inheritDoc}
         * @return {@inheritDoc}
         */
        @Override
        @SuppressWarnings("PMD.CompareObjectsWithEquals")
        public int compare(FrameNumbered o1, FrameNumbered o2){
            if(o1 == o2) return 0;
            if(o1 == null) return -1;
            if(o2 == null) return +1;

            int num1 = o1.getFrameNumber();
            int num2 = o2.getFrameNumber();

            return num1 - num2;
        }

    }

}
