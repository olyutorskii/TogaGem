/*
 * PMD toon texture file information handler
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sourceforge.mikutoga.pmd.parser;

import jp.sourceforge.mikutoga.parser.LoopHandler;
import jp.sourceforge.mikutoga.parser.MmdFormatException;
import jp.sourceforge.mikutoga.parser.ParseStage;

/**
 * PMDモデルの独自トゥーンテクスチャファイル名の通知用ハンドラ。
 */
public interface PmdToonHandler extends LoopHandler {

    /** トゥーンテクスチャファイル名抽出ループ。 */
    ParseStage TOON_LIST = new ParseStage();

    /**
     * 独自トゥーンテクスチャファイル名の通知を受け取る。
     * {@link #TOON_LIST}ループの構成要素
     * @param toonName 独自トゥーンテクスチャファイル名
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void pmdToonFileInfo(String toonName) throws MmdFormatException;

}
