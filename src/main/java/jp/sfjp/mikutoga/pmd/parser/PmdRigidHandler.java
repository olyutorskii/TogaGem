/*
 * PMD rigid information handler
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sfjp.mikutoga.pmd.parser;

import jp.sfjp.mikutoga.bin.parser.LoopHandler;
import jp.sfjp.mikutoga.bin.parser.MmdFormatException;
import jp.sfjp.mikutoga.bin.parser.ParseStage;

/**
 * PMDモデルの各種剛体情報の通知用ハンドラ。
 *
 * <p>MMDでの剛体力学では「Bullet Physics Library」が用いられる。
 *
 * @see <a href="http://www.bulletphysics.org/">Bullet Physics Library</a>
 */
public interface PmdRigidHandler extends LoopHandler {

    /** 剛体情報抽出ループ。 */
    public static final ParseStage RIGID_LIST = new ParseStage();

    /**
     * 剛体名の通知を受け取る。
     *
     * <p>{@link #RIGID_LIST}ループの構成要素。
     *
     * @param rigidName 剛体名
     * @throws MmdFormatException 不正フォーマットによる
     *     パース処理の中断をパーサに指示
     */
    public abstract void pmdRigidName(String rigidName)
        throws MmdFormatException;

    /**
     * 剛体基本情報の通知を受け取る。
     *
     * <p>{@link #RIGID_LIST}ループの構成要素。
     *
     * @param rigidGroupId 剛体グループ番号から1引いた数。(0-15)
     * @param linkedBoneId 接続先ボーンID。
     *     [ 0x0000 - 0xfffe ] に収まらない場合は接続先ボーン無し。
     * @throws MmdFormatException 不正フォーマットによる
     *     パース処理の中断をパーサに指示
     */
    public abstract void pmdRigidInfo(int rigidGroupId,
                        int linkedBoneId)
        throws MmdFormatException;

    /**
     * 剛体形状の通知を受け取る。
     *
     * <p>{@link #RIGID_LIST}ループの構成要素。
     *
     * @param shapeType 形状種別。
     *     <ul>
     *     <li>0x00:球
     *     <li>0x01:箱
     *     <li>0x02:カプセル
     *     </ul>
     * @param width 球orカプセル半径。箱の幅。
     * @param height 箱orカプセルの高さ
     * @param depth 箱の奥行き
     * @throws MmdFormatException 不正フォーマットによる
     *     パース処理の中断をパーサに指示
     */
    public abstract void pmdRigidShape(byte shapeType,
                         float width, float height, float depth)
        throws MmdFormatException;

    /**
     * 剛体位置の通知を受け取る。
     *
     * <p>{@link #RIGID_LIST}ループの構成要素。
     *
     * @param posX X座標
     * @param posY Y座標
     * @param posZ Z座標
     * @throws MmdFormatException 不正フォーマットによる
     *     パース処理の中断をパーサに指示
     */
    public abstract void pmdRigidPosition(float posX, float posY, float posZ)
        throws MmdFormatException;

    /**
     * 剛体姿勢の通知を受け取る。
     *
     * <p>{@link #RIGID_LIST}ループの構成要素。
     *
     * @param radX X軸回転量(radian)
     * @param radY Y軸回転量(radian)
     * @param radZ Z軸回転量(radian)
     * @throws MmdFormatException 不正フォーマットによる
     *     パース処理の中断をパーサに指示
     */
    public abstract void pmdRigidRotation(float radX, float radY, float radZ)
        throws MmdFormatException;

    /**
     * 剛体物理系数の通知を受け取る。
     *
     * <p>{@link #RIGID_LIST}ループの構成要素。
     *
     * @param mass 質量
     * @param dampingPos 移動減衰率
     * @param dampingRot 回転減衰率
     * @param restitution 反発力
     * @param friction 摩擦力
     * @throws MmdFormatException 不正フォーマットによる
     *     パース処理の中断をパーサに指示
     */
    public abstract void pmdRigidPhysics(float mass,
                           float dampingPos, float dampingRot,
                           float restitution, float friction )
        throws MmdFormatException;

    /**
     * 剛体の振る舞い情報の通知を受け取る。
     *
     * <p>{@link #RIGID_LIST}ループの構成要素。
     *
     * @param behaveType 剛体タイプ。
     *     <ul>
     *     <li>0:ボーン追従
     *     <li>1:物理演算
     *     <li>2:物理演算+ボーン位置合わせ
     *     </ul>
     * @param collisionMap 非衝突剛体グループビットマップ。
     *     <p>(衝突グループ番号-1)位置のビット位置は1に、
     *     (非衝突グループ番号-1)位置のビット位置は0になる。
     *     例)グループ1と8のみが非衝突指定の場合、0xff7eになる。</p>
     * @throws MmdFormatException 不正フォーマットによる
     *     パース処理の中断をパーサに指示
     */
    public abstract void pmdRigidBehavior(byte behaveType, short collisionMap)
        throws MmdFormatException;

}
