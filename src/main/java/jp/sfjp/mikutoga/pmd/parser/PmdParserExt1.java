/*
 * pmd parser extension 1
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sfjp.mikutoga.pmd.parser;

import java.io.IOException;
import java.io.InputStream;
import jp.sfjp.mikutoga.bin.parser.MmdFormatException;
import jp.sfjp.mikutoga.pmd.PmdConst;

/**
 * PMDモデルファイルのパーサ拡張その1。
 * <p>※ 英名対応
 */
public class PmdParserExt1 extends PmdParserBase {

    private PmdEngHandler engHandler = PmdUnifiedHandler.EMPTY;
    private boolean hasEnglishInfo = true;

    /**
     * コンストラクタ。
     * @param source 入力ソース
     */
    public PmdParserExt1(InputStream source){
        super(source);
        return;
    }

    /**
     * 英語ハンドラを登録する。
     * @param handler ハンドラ
     */
    public void setEngHandler(PmdEngHandler handler){
        if(handler == null){
            this.engHandler = PmdUnifiedHandler.EMPTY;
        }else{
            this.engHandler = handler;
        }
        return;
    }

    /**
     * {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    protected void parseBody()
            throws IOException, MmdFormatException{
        super.parseBody();

        if(hasMore()){
            parseEngHeader();
            if(this.hasEnglishInfo){
                parseEngBoneList();
                parseEngMorphName();
                parseEngBoneGroupName();
            }
        }

        return;
    }

    /**
     * PMDモデル英語基本情報のパースと通知。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseEngHeader()
            throws IOException, MmdFormatException{
        this.hasEnglishInfo = parseBoolean();

        this.engHandler.pmdEngEnabled(this.hasEnglishInfo);
        if( ! this.hasEnglishInfo ) return;

        String modelName =
                parsePmdText(PmdConst.MAXBYTES_MODELNAME);
        String description =
                parsePmdText(PmdConst.MAXBYTES_MODELDESC);
        description = description.replace(CRLF, LF);

        this.engHandler.pmdEngModelInfo(modelName, description);

        return;
    }

    /**
     * PMDモデル英語ボーン名のパースと通知。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseEngBoneList()
            throws IOException, MmdFormatException{
        int boneNum = getBoneCount();

        this.engHandler.loopStart(PmdEngHandler.ENGBONE_LIST, boneNum);

        for(int ct = 0; ct < boneNum; ct++){
            String boneName =
                    parsePmdText(PmdConst.MAXBYTES_BONENAME);
            this.engHandler.pmdEngBoneInfo(boneName);

            this.engHandler.loopNext(PmdEngHandler.ENGBONE_LIST);
        }

        this.engHandler.loopEnd(PmdEngHandler.ENGBONE_LIST);

        return;
    }

    /**
     * PMDモデル英語モーフ名のパースと通知。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseEngMorphName()
            throws IOException, MmdFormatException{
        int morphNum = getMorphCount() - 1;  // base は英名なし

        this.engHandler.loopStart(PmdEngHandler.ENGMORPH_LIST, morphNum);

        for(int ct = 0; ct < morphNum; ct++){
            String morphName =
                    parsePmdText(PmdConst.MAXBYTES_MORPHNAME);
            this.engHandler.pmdEngMorphInfo(morphName);

            this.engHandler.loopNext(PmdEngHandler.ENGMORPH_LIST);
        }

        this.engHandler.loopEnd(PmdEngHandler.ENGMORPH_LIST);

        return;
    }

    /**
     * PMDモデル英語ボーングループ名のパースと通知。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseEngBoneGroupName()
            throws IOException, MmdFormatException{
        int groupNum = getBoneGroupCount();

        this.engHandler.loopStart(PmdEngHandler.ENGBONEGROUP_LIST, groupNum);

        for(int ct = 0; ct < groupNum; ct++){
            String boneGroupName =
                    parsePmdText(PmdConst.MAXBYTES_BONEGROUPNAME);
            this.engHandler.pmdEngBoneGroupInfo(boneGroupName);

            this.engHandler.loopNext(PmdEngHandler.ENGBONEGROUP_LIST);
        }

        this.engHandler.loopEnd(PmdEngHandler.ENGBONEGROUP_LIST);

        return;
    }

}
