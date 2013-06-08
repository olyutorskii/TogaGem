/*
 * VMD boolean info handler
 *
 * License : The MIT License
 * Copyright(c) 2013 MikuToga Partners
 */

package jp.sfjp.mikutoga.vmd.parser;

import jp.sfjp.mikutoga.bin.parser.LoopHandler;
import jp.sfjp.mikutoga.bin.parser.MmdFormatException;
import jp.sfjp.mikutoga.bin.parser.ParseStage;

/**
 * VMDモーションファイルの各種ON/OFF情報(モデル表示・IK有効無効)
 * の通知用ハンドラ。
 * <p>MikuMikuDance Ver7.40よりVMDファイルに導入された新仕様。
 */
public interface VmdBoolHandler extends LoopHandler {

    /** モデル表示スイッチ抽出ループ識別子。 */
    ParseStage MODELSIGHT_LIST = new ParseStage();

    /** IK有効スイッチ抽出ループ識別子。 */
    ParseStage IKSW_LIST = new ParseStage();


    /**
     * モデルの表示フラグを通知する。
     * <p>{@link #MODELSIGHT_LIST}ループの構成要素。
     * @param show モデルの表示が行われる場合true
     * @param keyFrameNo キーフレーム番号
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void vmdModelSight(boolean show, int keyFrameNo)
            throws MmdFormatException;

    /**
     * IKボーン別のIK処理のON/OFFを通知する。
     * <p>{@link #MODELSIGHT_LIST}ループの下位
     * {@link #IKSW_LIST}ループの構成要素。
     * @param boneName IKボーン名
     * @param validIk IK処理が無効になる場合false
     * @param keyFrameNo キーフレーム番号
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void vmdIkSwitch(String boneName, boolean validIk, int keyFrameNo)
            throws MmdFormatException;

}
