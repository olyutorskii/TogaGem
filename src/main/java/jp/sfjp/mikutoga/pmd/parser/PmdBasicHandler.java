/*
 * PMD basic information handler
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sfjp.mikutoga.pmd.parser;

import jp.sfjp.mikutoga.bin.parser.MmdFormatException;

/**
 * PMDモデル情報ファイルの基本情報の通知用ハンドラ。
 */
public interface PmdBasicHandler {

    /**
     * PMDファイルのパース処理開始の通知を受け取る。
     *
     * @throws MmdFormatException
     *     不正フォーマットによるパース処理の中断をパーサに指示
     */
    public abstract void pmdParseStart()
            throws MmdFormatException;

    /**
     * PMDファイルのパース処理終了の通知を受け取る。
     *
     * @param hasMoreData
     *     入力ソースにまだ読み込まれていないデータがあればtrue
     * @throws MmdFormatException
     *     不正フォーマットによるパース処理の中断をパーサに指示
     */
    public abstract void pmdParseEnd(boolean hasMoreData)
            throws MmdFormatException;

    /**
     * PMDファイルのヘッダ情報の通知を受け取る。
     *
     * @param header ヘッダ情報バイト列。
     * @throws MmdFormatException
     *     不正フォーマットによるパース処理の中断をパーサに指示
     */
    public abstract void pmdHeaderInfo(byte[] header)
            throws MmdFormatException;

    /**
     * PMDファイルのモデル基本情報の通知を受け取る。
     *
     * @param modelName モデル名
     * @param description モデルの説明文。改行CRLFは"\n"に変換される。
     * @throws MmdFormatException
     *     不正フォーマットによるパース処理の中断をパーサに指示
     */
    public abstract void pmdModelInfo(String modelName, String description)
            throws MmdFormatException;

}
