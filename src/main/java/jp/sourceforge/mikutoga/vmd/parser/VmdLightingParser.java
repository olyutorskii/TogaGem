/*
 * VMD lighting data parser
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.parser;

import java.io.IOException;
import jp.sourceforge.mikutoga.parser.CommonParser;
import jp.sourceforge.mikutoga.parser.MmdFormatException;
import jp.sourceforge.mikutoga.parser.MmdSource;
import jp.sourceforge.mikutoga.vmd.VmdConst;

/**
 * VMDモーションファイルのライティング情報パーサ。
 * 照明光源演出データと影演出データを含む。
 * <p>古い版のVMDファイルには影演出データが記述されていない場合がある。
 */
class VmdLightingParser extends CommonParser {

    private VmdLightingHandler handler = null;


    /**
     * コンストラクタ。
     * @param source 入力ソース
     */
    VmdLightingParser(MmdSource source){
        super(source);
        return;
    }


    /**
     * ライティング情報通知用ハンドラを登録する。
     * @param lightingHandler ハンドラ
     */
    void setLightingHandler(VmdLightingHandler lightingHandler){
        this.handler = lightingHandler;
        return;
    }

    /**
     * ライティングデータのパースと通知。
     * <p>影演出データが無ければ読みに行かない。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    void parse() throws IOException, MmdFormatException {
        parseVmdLighting();
        if(hasMore()){
            parseVmdShadow();
        }
        return;
    }

    /**
     * 光源モーションデータのパースと通知。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseVmdLighting() throws IOException, MmdFormatException{
        int lightMotionNo = parseInteger();

        if(this.handler == null){
            skip(VmdConst.LUMINOUS_DATA_SZ * lightMotionNo);
            return;
        }

        this.handler.loopStart(VmdLightingHandler.LUMINOUS_LIST,
                lightMotionNo);

        for(int ct = 0; ct < lightMotionNo; ct++){
            int keyFrameNo = parseInteger();
            this.handler.vmdLuminousMotion(keyFrameNo);

            float rVal = parseFloat();
            float gVal = parseFloat();
            float bVal = parseFloat();
            this.handler.vmdLuminousColor(rVal, gVal, bVal);

            float xVec = parseFloat();
            float yVec = parseFloat();
            float zVec = parseFloat();
            this.handler.vmdLuminousDirection(xVec, yVec, zVec);

            this.handler.loopNext(VmdLightingHandler.LUMINOUS_LIST);
        }

        this.handler.loopEnd(VmdLightingHandler.LUMINOUS_LIST);

        return;
    }

    /**
     * 影演出データのパースと通知。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseVmdShadow() throws IOException, MmdFormatException{
        int shadowMotionNo = parseInteger();

        if(this.handler == null){
            skip(VmdConst.SHADOW_DATA_SZ * shadowMotionNo);
            return;
        }

        this.handler.loopStart(VmdLightingHandler.SHADOW_LIST,
                shadowMotionNo);

        for(int ct = 0; ct < shadowMotionNo; ct++){
            int keyFrameNo = parseInteger();
            this.handler.vmdShadowMotion(keyFrameNo);

            byte shadowMode = parseByte();
            this.handler.vmdShadowMode(shadowMode);

            float shadowScope = parseFloat();
            this.handler.vmdShadowScopeRaw(shadowScope);

            this.handler.loopNext(VmdLightingHandler.SHADOW_LIST);
        }

        this.handler.loopEnd(VmdLightingHandler.SHADOW_LIST);

        return;
    }

}