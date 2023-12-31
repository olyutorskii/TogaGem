/*
 * PMD material information handler
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sfjp.mikutoga.pmd.parser;

import jp.sfjp.mikutoga.bin.parser.LoopHandler;
import jp.sfjp.mikutoga.bin.parser.MmdFormatException;
import jp.sfjp.mikutoga.bin.parser.ParseStage;

/**
 * PMDモデルの各種材質情報の通知用ハンドラ。
 * 色空間はsRGB?
 */
public interface PmdMaterialHandler extends LoopHandler {

    /** 材質抽出ループ。 */
    public static final ParseStage MATERIAL_LIST = new ParseStage();

    /**
     * 材質の拡散光成分の通知を受け取る。
     *
     * <p>{@link #MATERIAL_LIST}ループの構成要素。
     *
     * @param red 0.0～1.0の範囲の赤成分
     * @param green 0.0～1.0の範囲の緑成分
     * @param blue 0.0～1.0の範囲の青成分
     * @param alpha 0.0(透明)～1.0(不透明)のアルファ値。
     * @throws MmdFormatException 不正フォーマットによる
     *     パース処理の中断をパーサに指示
     */
    public abstract void pmdMaterialDiffuse(
            float red, float green, float blue, float alpha)
            throws MmdFormatException;

    /**
     * 材質の反射光成分の通知を受け取る。
     *
     * <p>{@link #MATERIAL_LIST}ループの構成要素。
     *
     * @param red 0.0～1.0の範囲の赤成分
     * @param green 0.0～1.0の範囲の緑成分
     * @param blue 0.0～1.0の範囲の青成分
     * @param shininess 光沢強度(1～15ぐらい)
     * @throws MmdFormatException 不正フォーマットによる
     *     パース処理の中断をパーサに指示
     */
    public abstract void pmdMaterialSpecular(
            float red, float green, float blue, float shininess)
            throws MmdFormatException;

    /**
     * 材質の環境色成分の通知を受け取る。
     *
     * <p>{@link #MATERIAL_LIST}ループの構成要素。
     *
     * @param red 0.0～1.0の範囲の赤成分
     * @param green 0.0～1.0の範囲の緑成分
     * @param blue 0.0～1.0の範囲の青成分
     * @throws MmdFormatException 不正フォーマットによる
     *     パース処理の中断をパーサに指示
     */
    public abstract void pmdMaterialAmbient(
            float red, float green, float blue)
            throws MmdFormatException;

    /**
     * シェーディング情報の通知を受け取る。
     *
     * <p>{@link #MATERIAL_LIST}ループの構成要素。
     *
     * @param toonIdx トゥーンファイル番号。
     *     0ならtoon01.bmp。9ならtoon10.bmp。0xffならtoon0.bmp。
     * @param textureFile テクスチャファイル名。
     *     無ければ空文字。
     * @param sphereFile スフィアマップファイル名。
     *     無ければ空文字。
     * @throws MmdFormatException 不正フォーマットによる
     *     パース処理の中断をパーサに指示
     */
    public abstract void pmdMaterialShading(int toonIdx,
                               String textureFile, String sphereFile )
            throws MmdFormatException;

    /**
     * 材質情報の通知を受け取る。
     *
     * <p>{@link #MATERIAL_LIST}ループの構成要素。
     *
     * @param hasEdge エッジを表示するならtrue
     * @param vertexNum 面頂点数。
     *     3の倍数のはず。
     *     3で割ると、材質に属する面の数を表す。
     *
     *     <p>通算した面数を面情報通知順と突き合わせることにより、
     *     材質に属する面の集合を得ることが可能。</p>
     *
     * @throws MmdFormatException 不正フォーマットによる
     *     パース処理の中断をパーサに指示
     * @see PmdShapeHandler#pmdSurfaceTriangle(int, int, int)
     */
    public abstract void pmdMaterialInfo(boolean hasEdge, int vertexNum)
            throws MmdFormatException;

}
