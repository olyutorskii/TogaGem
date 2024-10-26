/*
 * VMD boolean data parser
 *
 * License : The MIT License
 * Copyright(c) 2013 MikuToga Partners
 */

package jp.sfjp.mikutoga.vmd.parser;

import java.io.IOException;
import jp.sfjp.mikutoga.bin.parser.BinParser;
import jp.sfjp.mikutoga.bin.parser.MmdFormatException;
import jp.sfjp.mikutoga.bin.parser.ProxyParser;
import jp.sfjp.mikutoga.bin.parser.TextDecoder;
import jp.sfjp.mikutoga.vmd.VmdConst;

/**
 * VMDモーションファイルの各種ON/OFF情報(モデル表示・IK有効無効)
 * パーサ。
 *
 * <p>MikuMikuDance Ver7.40以降でサポート
 */
class VmdBoolParser extends ProxyParser {

    private final TextDecoder decoderWin31j =
            new TextDecoder(VmdBasicParser.CS_WIN31J);

    private VmdBoolHandler handler = VmdUnifiedHandler.EMPTY;


    /**
     * コンストラクタ。
     *
     * @param parser 委譲先パーサ
     */
    VmdBoolParser(BinParser parser){
        super(parser);
        this.decoderWin31j.setZeroChopMode(true);
        return;
    }


    /**
     * ON/OFF情報通知用ハンドラを登録する。
     *
     * @param boolHandler ハンドラ
     */
    void setBoolHandler(VmdBoolHandler boolHandler){
        if(boolHandler == null){
            this.handler = VmdUnifiedHandler.EMPTY;
        }else{
            this.handler = boolHandler;
        }

        return;
    }

    /**
     * データのパースと通知。
     *
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    void parse() throws IOException, MmdFormatException {
        if( ! hasMore() ) return;

        parseVmdModelSight();

        return;
    }

    /**
     * モデル表示フラグデータのパースと通知。
     *
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseVmdModelSight()
            throws IOException, MmdFormatException{
        int modelSightNo = parseLeInt();

        this.handler.loopStart(VmdBoolHandler.MODELSIGHT_LIST,
                               modelSightNo );

        for(int ct = 0; ct < modelSightNo; ct++){
            int keyFrameNo = parseLeInt();
            boolean show = parseBoolean();
            this.handler.vmdModelSight(show, keyFrameNo);

            parseVmdIkSwitch(keyFrameNo);

            this.handler.loopNext(VmdBoolHandler.MODELSIGHT_LIST);
        }

        this.handler.loopEnd(VmdBoolHandler.MODELSIGHT_LIST);

        return;
    }

    /**
     * IK有効スイッチデータのパースと通知。
     *
     * @param keyFrameNo キーフレーム番号
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseVmdIkSwitch(int keyFrameNo)
            throws IOException, MmdFormatException{
        int ikSwitchNo = parseLeInt();

        this.handler.loopStart(VmdBoolHandler.IKSW_LIST, ikSwitchNo);

        for(int ct = 0; ct < ikSwitchNo; ct++){
            String boneName = parseString(this.decoderWin31j,
                                          VmdConst.IKSWBONENAME_MAX );
            boolean valid = parseBoolean();
            this.handler.vmdIkSwitch(boneName, valid, keyFrameNo);

            this.handler.loopNext(VmdBoolHandler.IKSW_LIST);
        }

        this.handler.loopEnd(VmdBoolHandler.IKSW_LIST);

        return;
    }

}
