/*
 * VMD loader
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.model.binio;

import java.io.IOException;
import jp.sourceforge.mikutoga.parser.MmdFormatException;
import jp.sourceforge.mikutoga.parser.MmdSource;
import jp.sourceforge.mikutoga.vmd.model.VmdMotion;
import jp.sourceforge.mikutoga.vmd.parser.VmdParser;

/**
 * VMDモーションファイルを読み込むためのバイナリローダ。
 */
public final class VmdLoader {

    /**
     * コンストラクタ。
     */
    private VmdLoader(){
        super();
        assert false;
        throw new AssertionError();
    }


    /**
     * VMDファイルの読み込みを行いモーション情報を返す。
     * @param source 入力ソース
     * @return モーション情報
     * @throws IOException 入力エラー
     * @throws MmdFormatException VMDファイルフォーマットの異常を検出
     */
    public static VmdMotion load(MmdSource source)
            throws IOException,
                   MmdFormatException {
        VmdParser parser = new VmdParser(source);

        VmdMotion result = new VmdMotion();

        BasicLoader basicBuilder       = new BasicLoader(result);
        CameraLoader cameraBuilder     = new CameraLoader(result);
        LightingLoader lightingBuilder = new LightingLoader(result);

        parser.setBasicHandler(basicBuilder);
        parser.setCameraHandler(cameraBuilder);
        parser.setLightingHandler(lightingBuilder);

        parser.parseVmd();

        return result;
    }

}
