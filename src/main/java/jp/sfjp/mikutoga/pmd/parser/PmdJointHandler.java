/*
 * PMD joint information handler
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sfjp.mikutoga.pmd.parser;

import jp.sfjp.mikutoga.bin.parser.LoopHandler;
import jp.sfjp.mikutoga.bin.parser.MmdFormatException;
import jp.sfjp.mikutoga.bin.parser.ParseStage;

/**
 * PMDモデルの各種剛体間ジョイント情報の通知用ハンドラ。
 */
public interface PmdJointHandler extends LoopHandler {

    /** ジョイント情報抽出ループ。 */
    public static final ParseStage JOINT_LIST = new ParseStage();

    /**
     * ジョイント名の通知を受け取る。
     *
     * <p>{@link #JOINT_LIST}ループの構成要素。
     *
     * @param jointName ジョイント名
     * @throws MmdFormatException
     *     不正フォーマットによるパース処理の中断をパーサに指示。
     */
    public abstract void pmdJointName(String jointName)
        throws MmdFormatException;

    /**
     * ジョイントが繋ぐ接続剛体IDの通知を受け取る。
     *
     * <p>{@link #JOINT_LIST}ループの構成要素。
     *
     * @param rigidIdA 接続剛体AのID
     * @param rigidIdB 接続剛体BのID
     * @throws MmdFormatException
     *     不正フォーマットによるパース処理の中断をパーサに指示。
     */
    public abstract void pmdJointLink(int rigidIdA, int rigidIdB)
        throws MmdFormatException;

    /**
     * ジョイント位置の通知を受け取る。
     *
     * <p>{@link #JOINT_LIST}ループの構成要素。
     *
     * @param posX X座標
     * @param posY Y座標
     * @param posZ Z座標
     * @throws MmdFormatException
     *     不正フォーマットによるパース処理の中断をパーサに指示。
     */
    public abstract void pmdJointPosition(float posX, float posY, float posZ)
        throws MmdFormatException;

    /**
     * ジョイント回転姿勢の通知を受け取る。
     *
     * <p>{@link #JOINT_LIST}ループの構成要素。
     *
     * @param radX X軸回転量(radian)
     * @param radY Y軸回転量(radian)
     * @param radZ Z軸回転量(radian)
     * @throws MmdFormatException
     *     不正フォーマットによるパース処理の中断をパーサに指示。
     */
    public abstract void pmdJointRotation(float radX, float radY, float radZ)
        throws MmdFormatException;

    /**
     * ジョイント移動制限の通知を受け取る。
     *
     * <p>{@link #JOINT_LIST}ループの構成要素。
     *
     * <p>※ [制限端 その1 &lt;= その2] 条件を満たす必要はあるか？
     *
     * @param posXlim1 X座標制限端その1
     * @param posXlim2 X座標制限端その2
     * @param posYlim1 Y座標制限端その1
     * @param posYlim2 Y座標制限端その2
     * @param posZlim1 Z座標制限端その1
     * @param posZlim2 Z座標制限端その2
     * @throws MmdFormatException
     *     不正フォーマットによるパース処理の中断をパーサに指示。
     */
    public abstract void pmdPositionLimit(float posXlim1, float posXlim2,
                            float posYlim1, float posYlim2,
                            float posZlim1, float posZlim2 )
        throws MmdFormatException;

    /**
     * ジョイント回転制限の通知を受け取る。
     *
     * <p>{@link #JOINT_LIST}ループの構成要素。
     *
     * <p>※ [制限端 その1 &lt;= その2] 条件を満たす必要はあるか？
     *
     * @param radXlim1 X軸制限端その1(radian)
     * @param radXlim2 X軸制限端その2(radian)
     * @param radYlim1 Y軸制限端その1(radian)
     * @param radYlim2 Y軸制限端その2(radian)
     * @param radZlim1 Z軸制限端その1(radian)
     * @param radZlim2 Z軸制限端その2(radian)
     * @throws MmdFormatException
     *     不正フォーマットによるパース処理の中断をパーサに指示。
     */
    public abstract void pmdRotationLimit(float radXlim1, float radXlim2,
                            float radYlim1, float radYlim2,
                            float radZlim1, float radZlim2 )
        throws MmdFormatException;

    /**
     * ジョイントのばね移動情報の通知を受け取る。
     *
     * <p>{@link #JOINT_LIST}ループの構成要素。
     *
     * @param elasticPosX X座標
     * @param elasticPosY Y座標
     * @param elasticPosZ Z座標
     * @throws MmdFormatException
     *     不正フォーマットによるパース処理の中断をパーサに指示。
     */
    public abstract void pmdElasticPosition(float elasticPosX,
                               float elasticPosY,
                               float elasticPosZ )
        throws MmdFormatException;

    /**
     * ジョイントのばね回転情報の通知を受け取る。
     *
     * <p>{@link #JOINT_LIST}ループの構成要素。
     *
     * @param elasticDegX X軸変量(degree)
     * @param elasticDegY Y軸変量(degree)
     * @param elasticDegZ Z軸変量(degree)
     * @throws MmdFormatException
     *     不正フォーマットによるパース処理の中断をパーサに指示。
     */
    public abstract void pmdElasticRotation(float elasticDegX,
                               float elasticDegY,
                               float elasticDegZ )
        throws MmdFormatException;

}
