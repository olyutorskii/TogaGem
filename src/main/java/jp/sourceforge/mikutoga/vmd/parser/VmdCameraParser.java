/*
 * VMD camera motion parser
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.parser;

import java.io.IOException;
import java.io.InputStream;
import jp.sourceforge.mikutoga.parser.CommonParser;
import jp.sourceforge.mikutoga.parser.MmdFormatException;
import jp.sourceforge.mikutoga.vmd.VmdConst;

/**
 * VMDモーションファイルのカメラモーションパーサ。
 */
class VmdCameraParser extends CommonParser{

    private static final int BZ_SIZE = 4;           // 4byte Bezier parameter
    private static final int BZXYZ_SIZE = BZ_SIZE * 3; // XYZ Bezier
    private static final int BZETC_SIZE = BZ_SIZE * 3; // etc. Bezier


    private final byte[] xyzIntplt = new byte[BZXYZ_SIZE];
    private final byte[] etcIntplt = new byte[BZETC_SIZE];

    private VmdCameraHandler handler = null;


    /**
     * コンストラクタ。
     * @param source 入力ソース
     */
    VmdCameraParser(InputStream source){
        super(source);
        return;
    }


    /**
     * カメラワーク情報通知用ハンドラを登録する。
     * @param cameraHandler ハンドラ
     */
    void setCameraHandler(VmdCameraHandler cameraHandler){
        this.handler = cameraHandler;
        return;
    }

    /**
     * カメラモーションデータのパースと通知。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    void parse() throws IOException, MmdFormatException {
        int cameraMotionNo = parseLeInt();

        if(this.handler == null){
            skip(VmdConst.CAMERA_DATA_SZ * cameraMotionNo);
            return;
        }

        this.handler.loopStart(VmdCameraHandler.CAMERA_LIST, cameraMotionNo);

        for(int ct = 0; ct < cameraMotionNo; ct++){
            int keyFrameNo = parseLeInt();
            this.handler.vmdCameraMotion(keyFrameNo);

            float range = parseLeFloat();
            this.handler.vmdCameraRange(range);

            float xPos = parseLeFloat();
            float yPos = parseLeFloat();
            float zPos = parseLeFloat();
            this.handler.vmdCameraPosition(xPos, yPos, zPos);

            float latitude  = parseLeFloat();
            float longitude = parseLeFloat();
            float roll      = parseLeFloat();
            this.handler.vmdCameraRotation(latitude, longitude, roll);

            parseCameraXyzInterpolation();
            parseCameraEtcInterpolation();

            int angle = parseLeInt();
            boolean hasPerspective = ! parseBoolean();
            this.handler.vmdCameraProjection(angle, hasPerspective);

            this.handler.loopNext(VmdCameraHandler.CAMERA_LIST);
        }

        this.handler.loopEnd(VmdCameraHandler.CAMERA_LIST);

        return;
    }

    /**
     * カメラターゲット補間データのパースと通知。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseCameraXyzInterpolation()
            throws IOException, MmdFormatException{
        if(this.handler == null){
            skip(this.xyzIntplt.length);
            return;
        }

        parseByteArray(this.xyzIntplt);

        int idx = 0;

        byte xP1x = this.xyzIntplt[idx++];
        byte xP2x = this.xyzIntplt[idx++];
        byte xP1y = this.xyzIntplt[idx++];
        byte xP2y = this.xyzIntplt[idx++];

        byte yP1x = this.xyzIntplt[idx++];
        byte yP2x = this.xyzIntplt[idx++];
        byte yP1y = this.xyzIntplt[idx++];
        byte yP2y = this.xyzIntplt[idx++];

        byte zP1x = this.xyzIntplt[idx++];
        byte zP2x = this.xyzIntplt[idx++];
        byte zP1y = this.xyzIntplt[idx++];
        byte zP2y = this.xyzIntplt[idx++];

        assert idx == this.xyzIntplt.length;

        this.handler.vmdCameraIntpltXpos(xP1x, xP1y, xP2x, xP2y);
        this.handler.vmdCameraIntpltYpos(yP1x, yP1y, yP2x, yP2y);
        this.handler.vmdCameraIntpltZpos(zP1x, zP1y, zP2x, zP2y);

        return;
    }

    /**
     * ターゲット位置以外の補間データのパースと通知。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseCameraEtcInterpolation()
            throws IOException, MmdFormatException{
        if(this.handler == null){
            skip(this.etcIntplt.length);
            return;
        }

        parseByteArray(this.etcIntplt);

        int idx = 0;

        byte rP1x = this.etcIntplt[idx++];
        byte rP2x = this.etcIntplt[idx++];
        byte rP1y = this.etcIntplt[idx++];
        byte rP2y = this.etcIntplt[idx++];

        byte dP1x = this.etcIntplt[idx++];
        byte dP2x = this.etcIntplt[idx++];
        byte dP1y = this.etcIntplt[idx++];
        byte dP2y = this.etcIntplt[idx++];

        byte pP1x = this.etcIntplt[idx++];
        byte pP2x = this.etcIntplt[idx++];
        byte pP1y = this.etcIntplt[idx++];
        byte pP2y = this.etcIntplt[idx++];

        assert idx == this.etcIntplt.length;

        this.handler.vmdCameraIntpltRotation  (rP1x, rP1y, rP2x, rP2y);
        this.handler.vmdCameraIntpltRange     (dP1x, dP1y, dP2x, dP2y);
        this.handler.vmdCameraIntpltProjection(pP1x, pP1y, pP2x, pP2y);

        return;
    }

}
