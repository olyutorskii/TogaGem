/*
 * parse-processing stage
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sfjp.mikutoga.bin.parser;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * パース処理の進行ステージ種別を表す。
 * <p>ループ構造の識別に用いられる。
 * <p>enum型と異なり、実行時での要素の追加が可能。
 */
public class ParseStage {

    private static final AtomicInteger LAST_NO = new AtomicInteger(0);

    /** 全インスタンスに割り振られたユニークな番号。変更不可。 */
    public final int no;

    /**
     * コンストラクタ。
     */
    public ParseStage(){
        super();
        this.no = LAST_NO.getAndIncrement();
        assert this.no >= 0;
        return;
    }

    /**
     * 各インスタンスに割り当てられたユニークな通し番号を返す。
     * @return 通し番号
     */
    public int getNo(){
        return this.no;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String toString(){
        String result = "parse stage#:" + this.no;
        return result;
    }

}
