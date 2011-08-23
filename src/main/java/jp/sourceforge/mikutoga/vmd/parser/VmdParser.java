/*
 * VMD file parser
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.parser;

import java.io.IOException;
import jp.sourceforge.mikutoga.parser.MmdFormatException;
import jp.sourceforge.mikutoga.parser.MmdSource;

/**
 * VMDモーションファイルのパーサ。
 */
public class VmdParser {

    private final MmdSource source;

    private final VmdBasicParser    basicParser;
    private final VmdCameraParser   cameraParser;
    private final VmdLightingParser lightingParser;

    private VmdBasicHandler basicHandler  = null;
    private boolean strictMode = true;


    /**
     * コンストラクタ。
     * @param source 入力ソース
     * @throws NullPointerException 引数がnull
     */
    public VmdParser(MmdSource source) throws NullPointerException{
        super();

        if(source == null) throw new NullPointerException();
        this.source = source;

        this.basicParser    = new VmdBasicParser(source);
        this.cameraParser   = new VmdCameraParser(source);
        this.lightingParser = new VmdLightingParser(source);

        return;
    }


    /**
     * 入力ソースを返す。
     * @return 入力ソース
     */
    public MmdSource getSource(){
        return this.source;
    }

    /**
     * 基本情報通知用ハンドラを登録する。
     * @param handler ハンドラ
     */
    public void setBasicHandler(VmdBasicHandler handler){
        this.basicParser.setBasicHandler(handler);
        this.basicHandler = handler;
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
     * 厳密なパース(Strict-mode)を行うか否か設定する。
     * デフォルトではStrict-modeはオン。
     * <p>Strict-mode下では、
     * ボーンモーションの冗長な補間情報の一貫性チェックが行われ、
     * モデル名がなんであろうとカメラ・ライティングデータのパースを試みる。
     * <p>※MMDVer7.30前後のVMD出力不具合を回避したい場合は、
     * Strict-modeをオフにするとパースに成功する場合がある。
     * @param mode Strict-modeに設定したければtrue
     */
    public void setStrictMode(boolean mode){
        this.strictMode = mode;
        this.basicParser.setStrictMode(this.strictMode);
        return;
    }

    /**
     * VMDファイルのパースを開始する。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    public void parseVmd() throws IOException, MmdFormatException {
        if(this.basicHandler != null){
            this.basicHandler.vmdParseStart();
        }

        parseBody();

        boolean hasMoreData = this.source.hasMore();
        if(this.basicHandler != null){
            this.basicHandler.vmdParseEnd(hasMoreData);
        }

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

        if(this.basicParser.hasStageActName() || this.strictMode){
            this.cameraParser.parse();
            this.lightingParser.parse();
        }

        return;
    }

}
