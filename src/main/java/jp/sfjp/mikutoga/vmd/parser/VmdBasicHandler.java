/*
 * VMD basic handler
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sfjp.mikutoga.vmd.parser;

import jp.sfjp.mikutoga.bin.parser.LoopHandler;
import jp.sfjp.mikutoga.bin.parser.MmdFormatException;
import jp.sfjp.mikutoga.bin.parser.ParseStage;

/**
 * VMDモーションファイルの基本情報(ボーンモーション／モーフモーション)
 * の通知用ハンドラ。
 *
 * <p>フレーム番号は相対的なものとなる。
 * 一番若いモーションのフレーム番号が0となる模様。
 *
 * <p>位置情報の座標基準は左手系ワールド座標で表される。
 *
 * <p>モーション補間情報は三次ベジェ曲線により記述される。
 * 三次ベジェ曲線は4つの制御点P0,P1,P2,P3により定義される。
 * P0は(0,0)、P3は(127,127)で固定。
 * P1,P2はP0,P3を対角線とする正方形の内部になければならない。
 * 直線補間の場合、P1には(20,20)、P2には(107,107)が使われることが多い。
 *
 * <p>補間情報は直後のボーンモーション情報との差分に関するもの。
 */
public interface VmdBasicHandler extends LoopHandler {

    /** ボーンモーション抽出ループ識別子。 */
    ParseStage BONEMOTION_LIST = new ParseStage();

    /** モーフ抽出ループ識別子。 */
    ParseStage MORPH_LIST = new ParseStage();


    /**
     * VMDファイルのパース処理開始の通知を受け取る。
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void vmdParseStart()
            throws MmdFormatException;

    /**
     * VMDファイルのパース処理終了の通知を受け取る。
     * @param hasMoreData 入力ソースに
     * まだ読み込まれていないデータがあればtrue
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void vmdParseEnd(boolean hasMoreData)
            throws MmdFormatException;

    /**
     * VMDファイルの固定長ヘッダを通知する。
     * @param header ヘッダ情報
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void vmdHeaderInfo(byte[] header)
            throws MmdFormatException;


    /**
     * モーションの適用先モデル名に関する情報を通知する。
     *
     * <p>カメラやライティングなどの演出データには
     * 特殊なモデル名が使われる。
     *
     * @param modelName モデル名
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void vmdModelName(String modelName)
            throws MmdFormatException;

    /**
     * ボーンモーションのボーン名及びキーフレーム番号に関する情報を通知する。
     *
     * <p>{@link #BONEMOTION_LIST}ループの構成要素。
     *
     * @param boneName ボーン名
     * @param keyFrameNo キーフレーム番号
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void vmdBoneMotion(String boneName, int keyFrameNo)
            throws MmdFormatException;

    /**
     * ボーン移動モーション情報の通知を受け取る。
     *
     * <p>座標基準は親ボーンもしくはワールド座標。
     *
     * <p>{@link #BONEMOTION_LIST}ループの構成要素。
     *
     * @param xPos X座標
     * @param yPos Y座標
     * @param zPos Z座標
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void vmdBonePosition(float xPos, float yPos, float zPos)
            throws MmdFormatException;

    /**
     * ボーン回転モーション情報の通知を受け取る。
     *
     * <p>回転及び姿勢はクォータニオンによって記述される。
     * MMDUI上のボーン数値入力YXZオイラー角と等価な回転。
     *
     * <p>座標基準は親ボーンもしくはワールド座標。
     *
     * <p>※ボーン種別によっては、無意味な情報。
     *
     * <p>{@link #BONEMOTION_LIST}ループの構成要素。
     *
     * @param qx クォータニオン虚部 X
     * @param qy クォータニオン虚部 Y
     * @param qz クォータニオン虚部 Z
     * @param qw クォータニオン実部 W
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void vmdBoneRotationQt(float qx, float qy, float qz, float qw)
            throws MmdFormatException;

    /**
     * ボーンモーションX軸移動補間情報の通知を受け取る。
     *
     * <p>三次ベジェ曲線のP1,P2点に関する情報を受け取る。
     *
     * <p>{@link #BONEMOTION_LIST}ループの構成要素。
     *
     * @param xP1x P1点のX座標
     * @param xP1y P1点のY座標
     * @param xP2x P2点のX座標
     * @param xP2y P2点のY座標
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void vmdBoneIntpltXpos(byte xP1x, byte xP1y, byte xP2x, byte xP2y)
            throws MmdFormatException;

    /**
     * ボーンモーションY軸移動補間情報の通知を受け取る。
     *
     * <p>三次ベジェ曲線のP1,P2点に関する情報を受け取る。
     *
     * <p>{@link #BONEMOTION_LIST}ループの構成要素。
     *
     * @param yP1x P1点のX座標
     * @param yP1y P1点のY座標
     * @param yP2x P2点のX座標
     * @param yP2y P2点のY座標
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void vmdBoneIntpltYpos(byte yP1x, byte yP1y, byte yP2x, byte yP2y)
            throws MmdFormatException;

    /**
     * ボーンモーションZ軸移動補間情報の通知を受け取る。
     *
     * <p>三次ベジェ曲線のP1,P2点に関する情報を受け取る。
     *
     * <p>{@link #BONEMOTION_LIST}ループの構成要素。
     *
     * @param zP1x P1点のX座標
     * @param zP1y P1点のY座標
     * @param zP2x P2点のX座標
     * @param zP2y P2点のY座標
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void vmdBoneIntpltZpos(byte zP1x, byte zP1y, byte zP2x, byte zP2y)
            throws MmdFormatException;

    /**
     * ボーンモーション回転量補間情報の通知を受け取る。
     *
     * <p>三次ベジェ曲線のP1,P2点に関する情報を受け取る。
     *
     * <p>{@link #BONEMOTION_LIST}ループの構成要素。
     *
     * <p>クォータニオン間のslerp処理に利用される。
     *
     * @param rP1x P1点のX座標
     * @param rP1y P1点のY座標
     * @param rP2x P2点のX座標
     * @param rP2y P2点のY座標
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void vmdBoneIntpltRot(byte rP1x, byte rP1y, byte rP2x, byte rP2y)
            throws MmdFormatException;


    /**
     * モーフモーション情報の通知を受け取る。
     *
     * <p>{@link #MORPH_LIST}ループの構成要素。
     *
     * @param morphName モーフ名。特殊モーフ名「base」は無視してもよい？
     * @param keyFrameNo フレーム番号
     * @param flex モーフ変量。通常は0.0以上1.0以下。
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void vmdMorphMotion(String morphName, int keyFrameNo, float flex)
            throws MmdFormatException;

}
