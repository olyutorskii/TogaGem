/*
 * VMD camerawork handler
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.parser;

import jp.sourceforge.mikutoga.parser.LoopHandler;
import jp.sourceforge.mikutoga.parser.MmdFormatException;
import jp.sourceforge.mikutoga.parser.ParseStage;

/**
 * VMDモーションファイルのカメラワーク情報の通知用ハンドラ。
 * <p>フレーム番号は相対的なものとなる。
 * カメラ・照明・シャドウのうち
 * 一番若いモーションのフレーム番号が0となる模様。
 * <p>ターゲット位置、カメラ回転量とも、座標基準はワールド座標。
 * <p>カメラターゲットの座標は左手系で表される。
 * <p>ターゲットに対するカメラ位置は極座標で表される。
 * <p>補間情報は直前カメラ情報との差分に関するもの。
 */
public interface VmdCameraHandler extends LoopHandler {

    /** カメラデータ抽出ループ識別子。 */
    ParseStage CAMERA_LIST = new ParseStage();


    /**
     * カメラモーションのキーフレーム番号に関する情報を通知する。
     * <p>{@link #CAMERA_LIST}ループの構成要素。
     * @param keyFrameNo キーフレーム番号
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void vmdCameraMotion(int keyFrameNo)
            throws MmdFormatException;

    /**
     * ターゲットとカメラ間の距離情報を通知する。
     * <p>球座標(極座標)の動径に相当する。
     * 通常はターゲットより手前に位置するカメラまでの距離が負の値で渡される。
     * <p>カメラ位置がターゲットを突き抜けた場合は正の値もとりうる。
     * ※MMDのUIと符号が逆なので注意。
     * <p>{@link #CAMERA_LIST}ループの構成要素。
     * @param range 距離
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void vmdCameraRange(float range)
            throws MmdFormatException;

    /**
     * カメラのターゲット位置情報を通知する。
     * <p>{@link #CAMERA_LIST}ループの構成要素。
     * @param xPos ターゲットのX座標
     * @param yPos ターゲットのY座標
     * @param zPos ターゲットのZ座標
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void vmdCameraPosition(float xPos, float yPos, float zPos)
            throws MmdFormatException;

    /**
     * カメラの回転および回転量情報を通知する。
     * <p>極座標(球座標)が用いられる。
     * <p>180度を超える値も回転量として意味を持つ。
     * <p>{@link #CAMERA_LIST}ループの構成要素。
     * @param latitude ターゲットから見たカメラの仰俯角(≒緯度)。
     * 単位はラジアン。
     * <p>Y軸回転量が0の時のZ正軸がY正軸へ倒れる方向が正回転。
     * (MMDのUIとは符号が逆になるので注意)
     * <p>仰俯角が0の場合、
     * カメラはターゲットに対しXZ平面(水平)と平行な箇所に位置する。
     * @param longitude Y軸周りの回転量(≒経度)。単位はラジアン。
     * <p>X正軸がZ正軸へ倒れる方向が正回転。(ボーン回転と逆)
     * <p>仰俯角およびY軸回転量が0の場合、
     * カメラレンズはZ軸-∞方向からZ軸+∞方向を向く。
     * @param roll レンズをターゲットを向けたカメラのロール回転量。
     * <p>仰俯角とY軸回転量が0の時にY正軸がX正軸に倒れる方向が正回転。
     * <p>仰俯角およびロール回転量が0の場合、カメラ上部はY軸+∞の方を向く。
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void vmdCameraRotation(float latitude, float longitude, float roll)
            throws MmdFormatException;

    /**
     * カメラを通じたスクリーン座標への投影に関する情報を通知する。
     * <p>{@link #CAMERA_LIST}ループの構成要素。
     * @param angle 縦画角。単位は度数法。MMDのUIでは1から125が指定可能。
     * @param hasPerspective パースペクティブスイッチがONならtrue。
     * スイッチがOFFの場合、画角は無視され遠近感処理が行われなくなる。
     * (平行投影?)
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void vmdCameraProjection(int angle, boolean hasPerspective)
            throws MmdFormatException;

    /**
     * カメラターゲットX軸移動補間情報の通知を受け取る。
     * 三次ベジェ曲線のP1,P2点に関する情報を受け取る。
     * <p>{@link #CAMERA_LIST}ループの構成要素。
     * @param p1x P1点のX座標
     * @param p1y P1点のY座標
     * @param p2x P2点のX座標
     * @param p2y P2点のY座標
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void vmdCameraIntpltXpos(byte p1x, byte p1y, byte p2x, byte p2y)
            throws MmdFormatException;

    /**
     * カメラターゲットY軸移動補間情報の通知を受け取る。
     * 三次ベジェ曲線のP1,P2点に関する情報を受け取る。
     * <p>{@link #CAMERA_LIST}ループの構成要素。
     * @param p1x P1点のX座標
     * @param p1y P1点のY座標
     * @param p2x P2点のX座標
     * @param p2y P2点のY座標
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void vmdCameraIntpltYpos(byte p1x, byte p1y, byte p2x, byte p2y)
            throws MmdFormatException;

    /**
     * カメラターゲットZ軸移動補間情報の通知を受け取る。
     * 三次ベジェ曲線のP1,P2点に関する情報を受け取る。
     * <p>{@link #CAMERA_LIST}ループの構成要素。
     * @param p1x P1点のX座標
     * @param p1y P1点のY座標
     * @param p2x P2点のX座標
     * @param p2y P2点のY座標
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void vmdCameraIntpltZpos(byte p1x, byte p1y, byte p2x, byte p2y)
            throws MmdFormatException;

    /**
     * カメラ回転量補間情報の通知を受け取る。
     * 三次ベジェ曲線のP1,P2点に関する情報を受け取る。
     * <p>{@link #CAMERA_LIST}ループの構成要素。
     * <p>カメラ回転でクォータニオン補間は使われない。
     * @param p1x P1点のX座標
     * @param p1y P1点のY座標
     * @param p2x P2点のX座標
     * @param p2y P2点のY座標
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void vmdCameraIntpltRotation(byte p1x, byte p1y, byte p2x, byte p2y)
            throws MmdFormatException;

    /**
     * カメラ-ターゲット間距離補間情報の通知を受け取る。
     * 三次ベジェ曲線のP1,P2点に関する情報を受け取る。
     * <p>{@link #CAMERA_LIST}ループの構成要素。
     * @param p1x P1点のX座標
     * @param p1y P1点のY座標
     * @param p2x P2点のX座標
     * @param p2y P2点のY座標
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void vmdCameraIntpltRange(byte p1x, byte p1y, byte p2x, byte p2y)
            throws MmdFormatException;

    /**
     * スクリーン投影補間情報の通知を受け取る。
     * 三次ベジェ曲線のP1,P2点に関する情報を受け取る。
     * <p>{@link #CAMERA_LIST}ループの構成要素。
     * @param p1x P1点のX座標
     * @param p1y P1点のY座標
     * @param p2x P2点のX座標
     * @param p2y P2点のY座標
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void vmdCameraIntpltProjection(byte p1x, byte p1y, byte p2x, byte p2y)
            throws MmdFormatException;

}
