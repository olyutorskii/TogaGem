/*
 * VMD lighting handler
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sfjp.mikutoga.vmd.parser;

import jp.sfjp.mikutoga.bin.parser.LoopHandler;
import jp.sfjp.mikutoga.bin.parser.MmdFormatException;
import jp.sfjp.mikutoga.bin.parser.ParseStage;

/**
 * VMDモーションファイルのライティング情報(照明光源・セルフシャドウ)
 * の通知用ハンドラ。
 *
 * <p>フレーム番号は相対的なものとなる。
 * カメラ・照明・シャドウのうち
 * 一番若いモーションのフレーム番号が0となる模様。
 */
public interface VmdLightingHandler extends LoopHandler {

    /** 照明光源データ抽出ループ識別子。 */
    public static final ParseStage LUMINOUS_LIST = new ParseStage();

    /** セルフシャドウデータ抽出ループ識別子。 */
    public static final ParseStage SHADOW_LIST = new ParseStage();


    /**
     * 照明情報のキーフレーム番号に関する情報を通知する。
     *
     * <p>{@link #LUMINOUS_LIST}ループの構成要素。
     *
     * @param keyFrameNo キーフレーム番号
     * @throws MmdFormatException
     *     不正フォーマットによるパース処理の中断をパーサに指示
     */
    public abstract void vmdLuminousMotion(int keyFrameNo)
            throws MmdFormatException;

    /**
     * 光源の色情報を通知する。
     *
     * <p>色情報はRGB色空間で記述される。
     *
     * <p>MMDのUI上の各色成分指定0～255定義域に便宜上256を追加したものが、
     * 0.0以上1.0以下にマップされる。
     * <ul>
     * <li>0は正しく0.0にマップされる。
     * <li>128は正しく0.5にマップされる。
     * <li>255は1.0より少しだけ小さい数にマップされる。
     * </ul>
     *
     * <p>{@link #LUMINOUS_LIST}ループの構成要素。
     *
     * @param rVal 赤成分(0.0以上1.0以下)
     * @param gVal 緑成分(0.0以上1.0以下)
     * @param bVal 青成分(0.0以上1.0以下)
     * @throws MmdFormatException
     *     不正フォーマットによるパース処理の中断をパーサに指示
     */
    public abstract void vmdLuminousColor(float rVal, float gVal, float bVal)
            throws MmdFormatException;

    /**
     * 光源の方向情報を通知する。
     *
     * <p>照明方向は、
     * ワールド座標原点から伸びる方向ベクトルとして記述される。
     * この方向ベクトルに向けて、無限遠の光源から照明が当たる。
     *
     * <p>MMDのスライダUI上では各軸成分の定義域は-1.0以上+1.0以下だが、
     * さらに絶対値の大きな値を指定することも可能。
     *
     * <p>方向ベクトルの長さは演出上の意味を持たないが、
     * キーフレーム間の照明方向の補間に影響を及ぼすかもしれない。
     *
     * <p>方向ベクトルが零ベクトル(0,0,0)の場合、全ポリゴンに影が落ちる。
     *
     * <p>{@link #LUMINOUS_LIST}ループの構成要素。
     *
     * @param xVec 方向ベクトルX軸成分
     * @param yVec 方向ベクトルY軸成分
     * @param zVec 方向ベクトルZ軸成分
     * @throws MmdFormatException
     *     不正フォーマットによるパース処理の中断をパーサに指示
     */
    public abstract void vmdLuminousDirection(
            float xVec, float yVec, float zVec)
            throws MmdFormatException;

    /**
     * シャドウ演出情報のキーフレーム番号に関する情報を通知する。
     *
     * <p>{@link #SHADOW_LIST}ループの構成要素。
     *
     * @param keyFrameNo キーフレーム番号
     * @throws MmdFormatException
     *     不正フォーマットによるパース処理の中断をパーサに指示
     */
    public abstract void vmdShadowMotion(int keyFrameNo)
            throws MmdFormatException;

    /**
     * セルフシャドウモードを通知する。
     *
     * <p>{@link #SHADOW_LIST}ループの構成要素。
     *
     * @param shadowMode シャドウモード指定。
     * <ul>
     * <li>0 : シャドウOFF
     * <li>1 : mode1 影描画の質がカメラからの距離の影響をあまり受けない。
     * <li>2 : mode2 影描画の質をカメラからの距離に応じて劣化させる
     * ことにより、カメラに近いオブジェクトの影描画の質を向上させる。
     * </ul>
     * @throws MmdFormatException
     *     不正フォーマットによるパース処理の中断をパーサに指示
     */
    public abstract void vmdShadowMode(byte shadowMode)
            throws MmdFormatException;

    /**
     * セルフシャドウの描画対象となるオブジェクトの範囲(カメラからの距離)
     * を通知する。
     *
     * <p>通知されるのは幾何的な距離ではない。
     * MMDのUI値(カメラからの距離の100倍？)を
     * 10万で割った商を0.1から引いた値が通知される。
     *
     * <p>{@link #SHADOW_LIST}ループの構成要素。
     *
     * @param shadowScope 距離情報。
     * @throws MmdFormatException
     *     不正フォーマットによるパース処理の中断をパーサに指示
     */
    public abstract void vmdShadowScopeRaw(float shadowScope)
            throws MmdFormatException;

}
