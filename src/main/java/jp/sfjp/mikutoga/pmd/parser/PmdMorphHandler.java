/*
 * PMD morph information handler
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sfjp.mikutoga.pmd.parser;

import jp.sfjp.mikutoga.bin.parser.LoopHandler;
import jp.sfjp.mikutoga.bin.parser.MmdFormatException;
import jp.sfjp.mikutoga.bin.parser.ParseStage;

/**
 * PMDモデルのモーフ情報の通知用ハンドラ。
 */
public interface PmdMorphHandler extends LoopHandler {

    /** モーフ抽出ループ。 */
    public static final ParseStage MORPH_LIST = new ParseStage();

    /** モーフ頂点抽出ループ。 */
    public static final ParseStage MORPHVERTEX_LIST = new ParseStage();

    /** モーフ出現順抽出ループ。 */
    public static final ParseStage MORPHORDER_LIST = new ParseStage();

    /**
     * モーフ情報の通知を受け取る。
     *
     * <p>{@link #MORPH_LIST}ループの構成要素
     *
     * @param morphName モーフ名
     * @param morphType モーフ種別。
     * <ul>
     * <li>0:base
     * <li>1:まゆ
     * <li>2:目
     * <li>3:リップ
     * <li>4:その他
     * </ul>
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    public abstract void pmdMorphInfo(String morphName, byte morphType)
            throws MmdFormatException;

    /**
     * モーフ形状の通知を受け取る。
     *
     * <p>{@link #MORPH_LIST}ループの下位{@link #MORPHVERTEX_LIST}の構成要素。
     *
     * <p>※ base型頂点IDの出現順がモーフ頂点IDとなる。
     *
     * @param serialId base型の場合は頂点ID、それ以外はモーフ頂点ID
     * @param xPos base型の場合はX座標、それ以外はX軸変位
     * @param yPos base型の場合はY座標、それ以外はY軸変位
     * @param zPos base型の場合はZ座標、それ以外はZ軸変位
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    public abstract void pmdMorphVertexInfo(int serialId,
                               float xPos, float yPos, float zPos)
            throws MmdFormatException;

    /**
     * 各モーフ種別内のGUI表示順の通知を受け取る。
     *
     * <p>{@link #MORPHORDER_LIST}ループの構成要素
     *
     * @param morphId モーフ通し番号。同一モーフ種別内の大小関係のみ意味がある。
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    public abstract void pmdMorphOrderInfo(int morphId)
            throws MmdFormatException;

}
