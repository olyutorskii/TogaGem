/*
 * parse-processing stage
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sourceforge.mikutoga.parser;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * パース処理の進行ステージ種別を表す。
 * ループ構造の識別に用いられる。
 */
public class ParseStage {

    private static final AtomicInteger lastNo = new AtomicInteger(0);

    private final int no;

    /**
     * コンストラクタ。
     */
    public ParseStage(){
        super();
        this.no = lastNo.getAndIncrement();
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

}
