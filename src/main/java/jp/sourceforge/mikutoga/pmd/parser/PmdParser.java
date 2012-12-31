/*
 * pmd file parser (up to date)
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sourceforge.mikutoga.pmd.parser;

import java.io.InputStream;

/**
 * PMDモデルファイルのパーサ最新版。
 * 将来のリリースにおいて、
 * 常に最新のPMDモデルファイルフォーマットに対応したパーサの
 * 別名であることが保証される。つもり。
 */
public class PmdParser extends PmdParserExt3{

    /**
     * コンストラクタ。
     * @param source 入力ソース
     */
    public PmdParser(InputStream source){
        super(source);
        return;
    }

}
