/*
 * VMD file parser
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sfjp.mikutoga.vmd.parser;

import java.io.IOException;
import java.io.InputStream;
import jp.sfjp.mikutoga.bin.parser.BinParser;
import jp.sfjp.mikutoga.bin.parser.CommonParser;
import jp.sfjp.mikutoga.bin.parser.MmdFormatException;

/**
 * VMDモーションファイルのパーサ。
 */
public class VmdParser {

    private final InputStream source;

    private final VmdBasicParser    basicParser;
    private final VmdCameraParser   cameraParser;
    private final VmdLightingParser lightingParser;

    private VmdBasicHandler basicHandler  = VmdUnifiedHandler.EMPTY;

    private boolean ignoreName = true;
    private boolean redundantCheck = false;


    /**
     * コンストラクタ。
     * @param source 入力ソース
     * @throws NullPointerException 引数がnull
     */
    public VmdParser(InputStream source) throws NullPointerException{
        super();

        if(source == null) throw new NullPointerException();
        this.source = source;

        BinParser parser = new CommonParser(this.source);

        this.basicParser    = new VmdBasicParser(parser);
        this.cameraParser   = new VmdCameraParser(parser);
        this.lightingParser = new VmdLightingParser(parser);

        return;
    }


    /**
     * 入力ソースを返す。
     * @return 入力ソース
     */
    public InputStream getSource(){
        return this.source;
    }

    /**
     * 基本情報通知用ハンドラを登録する。
     * @param handler ハンドラ
     */
    public void setBasicHandler(VmdBasicHandler handler){
        this.basicParser.setBasicHandler(handler);

        if(handler == null){
            this.basicHandler = VmdUnifiedHandler.EMPTY;
        }else{
            this.basicHandler = handler;
        }

        return;
    }

    /**
     * カメラワーク情報通知用ハンドラを登録する。
     * @param cameraHandler ハンドラ
     */
    public void setCameraHandler(VmdCameraHandler cameraHandler){
        this.cameraParser.setCameraHandler(cameraHandler);
        return;
    }

    /**
     * ライティング情報通知用ハンドラを登録する。
     * @param lightingHandler ハンドラ
     */
    public void setLightingHandler(VmdLightingHandler lightingHandler){
        this.lightingParser.setLightingHandler(lightingHandler);
        return;
    }

    /**
     * カメラ・ライティングデータのパースを試みるか否かの判断で、
     * 特殊モデル名判定を無視するか否か設定する。
     * デフォルトではモデル名を無視。
     * <p>※MMDVer7.30前後のVMD出力不具合を回避したい場合は、
     * オフにするとパースに成功する場合がある。
     * @param mode モデル名を無視するならtrue
     */
    public void setIgnoreName(boolean mode){
        this.ignoreName = mode;
        return;
    }

    /**
     * ボーンモーション補間情報冗長部のチェックを行うか否か設定する。
     * デフォルトではチェックを行わない。
     * <p>※MMDVer7.30前後のVMD出力不具合を回避したい場合は、
     * オフにするとパースに成功する場合がある。
     * @param mode チェックさせたければtrue
     */
    public void setRedundantCheck(boolean mode){
        this.redundantCheck = mode;
        this.basicParser.setRedundantCheck(mode);
        return;
    }

    /**
     * VMDファイルのパースを開始する。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    public void parseVmd() throws IOException, MmdFormatException {
        setIgnoreName(this.ignoreName);
        setRedundantCheck(this.redundantCheck);

        this.basicHandler.vmdParseStart();

        parseBody();

        boolean hasMoreData = this.lightingParser.hasMore();
        this.basicHandler.vmdParseEnd(hasMoreData);

        return;
    }

    /**
     * VMDファイル本体のパースを開始する。
     * <p>モデル名がボーンモーション用と推測され、
     * かつパーサがStrict-modeでない場合、
     * カメラ、ライティングデータのパースは行われない。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseBody() throws IOException, MmdFormatException{
        this.basicParser.parse();

        if(this.basicParser.hasStageActName() || this.ignoreName){
            this.cameraParser.parse();
            this.lightingParser.parse();
        }

        return;
    }

}
