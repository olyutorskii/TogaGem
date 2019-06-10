/*
 * PMD shape information handler
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sfjp.mikutoga.pmd.parser;

import jp.sfjp.mikutoga.bin.parser.LoopHandler;
import jp.sfjp.mikutoga.bin.parser.MmdFormatException;
import jp.sfjp.mikutoga.bin.parser.ParseStage;

/**
 * PMDモデルの各種形状(頂点、面)の通知用ハンドラ。
 *
 * <p>0から始まる頂点ID順に頂点は出現する。
 *
 * <p>0から始まる面ID順に面は出現する。
 */
public interface PmdShapeHandler extends LoopHandler {

    /** 頂点抽出ループ。 */
    ParseStage VERTEX_LIST = new ParseStage();
    /** 面抽出ループ。 */
    ParseStage SURFACE_LIST = new ParseStage();

    /**
     * 頂点の座標の通知を受け取る。
     *
     * <p>{@link #VERTEX_LIST}ループの構成要素
     *
     * @param xPos X座標
     * @param yPos Y座標
     * @param zPos Z座標
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void pmdVertexPosition(float xPos, float yPos, float zPos)
            throws MmdFormatException;

    /**
     * 頂点の法線情報の通知を受け取る。
     *
     * <p>{@link #VERTEX_LIST}ループの構成要素
     *
     * <p>※単位ベクトル化必須？
     *
     * @param xVec 法線ベクトルX成分
     * @param yVec 法線ベクトルY成分
     * @param zVec 法線ベクトルZ成分
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void pmdVertexNormal(float xVec, float yVec, float zVec)
            throws MmdFormatException;

    /**
     * 頂点のUVマッピング情報の通知を受け取る。
     * (頂点UV)
     *
     * <p>{@link #VERTEX_LIST}ループの構成要素
     *
     * @param uVal テクスチャのU座標
     * @param vVal テクスチャのV座標
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void pmdVertexUV(float uVal, float vVal )
            throws MmdFormatException;

    /**
     * 頂点のボーン間ウェイトバランス情報の通知を受け取る。
     *
     * <p>{@link #VERTEX_LIST}ループの構成要素
     *
     * @param boneId1 ボーンその1識別ID
     * @param boneId2 ボーンその2識別ID
     * @param weightForB1 ボーンその1への影響度。0(min)～100(max)
     * ボーンその2への影響度は100からの引き算で求める。
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void pmdVertexWeight(int boneId1, int boneId2, int weightForB1)
            throws MmdFormatException;

    /**
     * 頂点のエッジ表現情報の通知を受け取る。
     * 材質単位でのエッジ表現指定に優先される。
     *
     * <p>{@link #VERTEX_LIST}ループの構成要素
     *
     * @param hideEdge エッジ無効ならtrue
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void pmdVertexEdge(boolean hideEdge)
            throws MmdFormatException;

    /**
     * 3つの頂点から構成される面情報の通知を受け取る。
     *
     * <p>{@link #SURFACE_LIST}ループの構成要素。
     *
     * <p>3頂点の指定順は、面カリングにおいて意味を持つ。
     *
     * @param vertexId1 頂点IDその1
     * @param vertexId2 頂点IDその1
     * @param vertexId3 頂点IDその1
     * @throws MmdFormatException 不正フォーマットによる
     * パース処理の中断をパーサに指示
     */
    void pmdSurfaceTriangle(int vertexId1, int vertexId2, int vertexId3)
            throws MmdFormatException;

}
