/*
 * VMD basic parser
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.parser;

import java.io.IOException;
import jp.sourceforge.mikutoga.parser.CommonParser;
import jp.sourceforge.mikutoga.parser.MmdFormatException;
import jp.sourceforge.mikutoga.parser.MmdInputStream;
import jp.sourceforge.mikutoga.vmd.VmdConst;

/**
 * VMDモーションファイルの基本部パーサ。
 * <p>ボーンのモーション情報およびモーフモーション情報のパース処理を含む。
 */
class VmdBasicParser extends CommonParser{

    private static final int BZ_SIZE = 4;           // 4byte Bezier parameter
    private static final int BZXYZR_SIZE = BZ_SIZE * 4; // XYZR Bezier
    private static final int BZ_REDUNDANT = 4;          // redundant spare
    private static final int BZTOTAL_SIZE = BZXYZR_SIZE * BZ_REDUNDANT;

    private static final String ERRMSG_INVINTPLT =
            "there is potential inconsistency in motion interpolation data. "
            +"(Strict-mode)";


    private final byte[] motionIntplt = new byte[BZTOTAL_SIZE];

    private VmdBasicHandler handler = null;

    private boolean hasStageActName = false;
    private boolean strictMode = true;


    /**
     * コンストラクタ。
     * @param source 入力ソース
     */
    VmdBasicParser(MmdInputStream source){
        super(source);
        return;
    }


    /**
     * パースしたモデル名がカメラ及びライティング用モデル名だったか判定する。
     * @return カメラ及びライティング用モデル名だったらtrue
     */
    boolean hasStageActName(){
        return this.hasStageActName;
    }

    /**
     * 基本情報通知用ハンドラを登録する。
     * @param basicHandler ハンドラ
     */
    void setBasicHandler(VmdBasicHandler basicHandler){
        this.handler = basicHandler;
        return;
    }

    /**
     * 厳密なパース(Strict-mode)を行うか否か設定する。
     * デフォルトではStrict-modeはオン。
     * <p>Strict-mode下では、
     * ボーンモーションの冗長な補間情報の一貫性チェックが行わる。
     * @param mode Strict-modeに設定したければtrue
     */
    void setStrictMode(boolean mode){
        this.strictMode = mode;
        return;
    }

    /**
     * VMDファイル基本部のパースを開始する。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    void parse() throws IOException, MmdFormatException {
        this.hasStageActName = false;

        parseVmdHeader();
        parseVmdModelName();
        parseVmdBoneMotion();
        parseVmdMorph();

        return;
    }

    /**
     * VMDファイルヘッダ部のパースと通知。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseVmdHeader() throws IOException, MmdFormatException{
        byte[] header = new byte[VmdConst.HEADER_LENGTH];
        parseByteArray(header);

        if( ! VmdConst.startsWithMagic(header) ){
            throw new MmdFormatException("unknown VMD-header type");
        }

        if(this.handler != null){
            this.handler.vmdHeaderInfo(header);
        }

        return;
    }

    /**
     * モデル名のパースと通知。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseVmdModelName() throws IOException, MmdFormatException{
        String modelName = parseZeroTermWin31J(VmdConst.MODELNAME_MAX);

        if(VmdConst.isStageActName(modelName)){
            this.hasStageActName = true;
        }

        if(this.handler != null){
            this.handler.vmdModelName(modelName);
        }

        return;
    }

    /**
     * ボーンモーションデータのパースと通知。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseVmdBoneMotion()
            throws IOException, MmdFormatException{
        int boneMotionNo = parseLeInt();

        if(this.handler == null){
            skip(VmdConst.BONEMOTION_DATA_SZ * boneMotionNo);
            return;
        }

        this.handler.loopStart(
                VmdBasicHandler.BONEMOTION_LIST, boneMotionNo);

        for(int ct = 0; ct < boneMotionNo; ct++){
            String boneName = parseZeroTermWin31J(VmdConst.BONENAME_MAX);
            int keyFrameNo = parseLeInt();
            this.handler.vmdBoneMotion(boneName, keyFrameNo);

            float xPos = parseLeFloat();
            float yPos = parseLeFloat();
            float zPos = parseLeFloat();
            this.handler.vmdBonePosition(xPos, yPos, zPos);

            float qx = parseLeFloat();
            float qy = parseLeFloat();
            float qz = parseLeFloat();
            float qw = parseLeFloat();
            this.handler.vmdBoneRotationQt(qx, qy, qz, qw);

            parseVmdMotionInterpolation();

            this.handler.loopNext(VmdBasicHandler.BONEMOTION_LIST);
        }

        this.handler.loopEnd(VmdBasicHandler.BONEMOTION_LIST);

        return;
    }

    /**
     * ボーンモーション補間データのパースと通知。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseVmdMotionInterpolation()
            throws IOException, MmdFormatException{
        if(this.handler == null){
            skip(this.motionIntplt.length);
            return;
        }

        parseByteArray(this.motionIntplt);

        if(this.strictMode){
            checkIntpltStrict();
        }

        int idx = 0;

        byte xP1x = this.motionIntplt[idx++];
        byte yP1x = this.motionIntplt[idx++];
        byte zP1x = this.motionIntplt[idx++];
        byte rP1x = this.motionIntplt[idx++];

        byte xP1y = this.motionIntplt[idx++];
        byte yP1y = this.motionIntplt[idx++];
        byte zP1y = this.motionIntplt[idx++];
        byte rP1y = this.motionIntplt[idx++];

        byte xP2x = this.motionIntplt[idx++];
        byte yP2x = this.motionIntplt[idx++];
        byte zP2x = this.motionIntplt[idx++];
        byte rP2x = this.motionIntplt[idx++];

        byte xP2y = this.motionIntplt[idx++];
        byte yP2y = this.motionIntplt[idx++];
        byte zP2y = this.motionIntplt[idx++];
        byte rP2y = this.motionIntplt[idx++];

        assert idx == BZXYZR_SIZE;

        this.handler.vmdBoneIntpltXpos(xP1x, xP1y, xP2x, xP2y);
        this.handler.vmdBoneIntpltYpos(yP1x, yP1y, yP2x, yP2y);
        this.handler.vmdBoneIntpltZpos(zP1x, zP1y, zP2x, zP2y);
        this.handler.vmdBoneIntpltRot (rP1x, rP1y, rP2x, rP2y);

        return;
    }

    /**
     * 補間情報の冗長箇所の整合性チェックを行う。
     * @throws MmdFormatException 冗長箇所の不整合を検出した。
     */
    private void checkIntpltStrict() throws MmdFormatException{
        int lack = 1;
        for(int ct = 1; ct < BZ_REDUNDANT; ct++){
            int sourceIdx = 0 + lack;
            int targetIdx = BZXYZR_SIZE * ct;
            int span = BZXYZR_SIZE - lack;

            for(int idx = 0; idx < span; idx++){
                byte sourceVal = this.motionIntplt[sourceIdx + idx];
                byte targetVal = this.motionIntplt[targetIdx + idx];
                if(sourceVal != targetVal){
                    throw new MmdFormatException(ERRMSG_INVINTPLT,
                                                 getPosition());
                }
            }

            int onePos = targetIdx + span;
            if(this.motionIntplt[onePos] != (byte) 0x01){
                throw new MmdFormatException(ERRMSG_INVINTPLT, getPosition());
            }

            int zeroPosStart = onePos + 1;
            int zeroPosEnd = targetIdx + BZXYZR_SIZE;
            for(int idx = zeroPosStart; idx < zeroPosEnd; idx++){
                if(this.motionIntplt[idx] != (byte) 0x00){
                    throw new MmdFormatException(ERRMSG_INVINTPLT,
                                                 getPosition());
                }
            }

            lack++;
        }

        return;
    }

    /**
     * モーフモーションデータのパースと通知。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseVmdMorph() throws IOException, MmdFormatException{
        int morphMotionNo = parseLeInt();

        if(this.handler == null){
            skip(VmdConst.MORPH_DATA_SZ * morphMotionNo);
            return;
        }

        this.handler.loopStart(
                VmdBasicHandler.MORPH_LIST, morphMotionNo);

        for(int ct = 0; ct < morphMotionNo; ct++){
            String morphName = parseZeroTermWin31J(VmdConst.MORPHNAME_MAX);
            int keyFrameNo = parseLeInt();
            float flex = parseLeFloat();
            this.handler.vmdMorphMotion(morphName, keyFrameNo, flex);

            this.handler.loopNext(VmdBasicHandler.MORPH_LIST);
        }

        this.handler.loopEnd(VmdBasicHandler.MORPH_LIST);

        return;
    }

}
