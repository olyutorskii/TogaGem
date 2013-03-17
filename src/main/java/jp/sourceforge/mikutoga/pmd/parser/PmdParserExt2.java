/*
 * pmd parser extensin 2
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sourceforge.mikutoga.pmd.parser;

import java.io.IOException;
import java.io.InputStream;
import jp.sfjp.mikutoga.bin.parser.MmdFormatException;

/**
 * PMDモデルファイルのパーサ拡張その2。
 * ※独自トゥーンテクスチャファイル名対応
 */
public class PmdParserExt2 extends PmdParserExt1 {

    private PmdToonHandler toonHandler = null;

    /**
     * コンストラクタ。
     * @param source 入力ソース
     */
    public PmdParserExt2(InputStream source){
        super(source);
        return;
    }

    /**
     * トゥーンテクスチャファイルハンドラを登録する。
     * @param handler ハンドラ
     */
    public void setToonHandler(PmdToonHandler handler){
        this.toonHandler = handler;
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
            parseToonName();
        }

        return;
    }

    /**
     * 独自トゥーンテクスチャファイル名のパースと通知。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseToonName() throws IOException, MmdFormatException{
        if(this.toonHandler == null){
            skip(PmdLimits.MAXBYTES_TOONFILENAME * PmdLimits.TOON_FIXEDNUM);
            return;
        }

        this.toonHandler.loopStart(PmdToonHandler.TOON_LIST,
                                   PmdLimits.TOON_FIXEDNUM );

        for(int ct = 0; ct < PmdLimits.TOON_FIXEDNUM; ct++){
            String toonName =
                    parsePmdText(PmdLimits.MAXBYTES_TOONFILENAME);
            this.toonHandler.pmdToonFileInfo(toonName);

            this.toonHandler.loopNext(PmdToonHandler.TOON_LIST);
        }

        this.toonHandler.loopEnd(PmdToonHandler.TOON_LIST);

        return;
    }

}
