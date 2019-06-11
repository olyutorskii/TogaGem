/*
 * PMD toon texture file information handler
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sfjp.mikutoga.pmd.parser;

import jp.sfjp.mikutoga.bin.parser.LoopHandler;
import jp.sfjp.mikutoga.bin.parser.MmdFormatException;
import jp.sfjp.mikutoga.bin.parser.ParseStage;

/**
 * PMDモデルの独自トゥーンテクスチャファイル名の通知用ハンドラ。
 */
public interface PmdToonHandler extends LoopHandler {

    /** トゥーンテクスチャファイル名抽出ループ。 */
    public static final ParseStage TOON_LIST = new ParseStage();

    /**
     * 独自トゥーンテクスチャファイル名の通知を受け取る。
     *
     * <p>{@link #TOON_LIST}ループの構成要素
     *
     * @param toonName 独自トゥーンテクスチャファイル名
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    public abstract void pmdToonFileInfo(String toonName)
            throws MmdFormatException;

}
