/*
 * PMD english information handler
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sourceforge.mikutoga.pmd.parser;

import jp.sfjp.mikutoga.bin.parser.LoopHandler;
import jp.sfjp.mikutoga.bin.parser.MmdFormatException;
import jp.sfjp.mikutoga.bin.parser.ParseStage;

/**
 * PMDモデルの英語情報の通知用ハンドラ。
 */
public interface PmdEngHandler extends LoopHandler {

    /** ボーン英語名抽出ループ。 */
    ParseStage ENGBONE_LIST = new ParseStage();

    /** モーフ英語名抽出ループ。 */
    ParseStage ENGMORPH_LIST = new ParseStage();

    /** ボーングループ英語名抽出ループ。 */
    ParseStage ENGBONEGROUP_LIST = new ParseStage();

    /**
     * PMD英語情報の有無の通知を受け取る。
     * @param hasEnglishInfo 英語情報が含まれればtrue
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void pmdEngEnabled(boolean hasEnglishInfo) throws MmdFormatException;

    /**
     * PMD英語基本情報の通知を受け取る。
     * @param modelName モデル名
     * @param description モデルの説明文。改行CRLFは"\n"に変換される。
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void pmdEngModelInfo(String modelName, String description)
            throws MmdFormatException;

    /**
     * 英語ボーン名の通知を受け取る。
     * <p>{@link #ENGBONE_LIST}ループの構成要素
     * @param boneName 英語ボーン名
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void pmdEngBoneInfo(String boneName) throws MmdFormatException;

    /**
     * 英語モーフ名の通知を受け取る。
     * 特殊モーフ名「base」に対応する英語名は通知されない。
     * <p>{@link #ENGMORPH_LIST}ループの構成要素
     * @param morphName 英語モーフ名
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void pmdEngMorphInfo(String morphName) throws MmdFormatException;

    /**
     * 英語ボーングループ名の通知を受け取る。
     * <p>{@link #ENGBONEGROUP_LIST}ループの構成要素
     * @param groupName 英語ボーングループ名
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void pmdEngBoneGroupInfo(String groupName) throws MmdFormatException;

}
