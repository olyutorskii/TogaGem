/*
 * shading file utility
 *
 * License : The MIT License
 * Copyright(c) 2013 MikuToga Partners
 */

package jp.sfjp.mikutoga.pmd;

import java.util.regex.Pattern;

/**
 * シェーディング情報の各種ユーティリティ。
 *
 * <p>※ スフィアマップファイルの中身はBMP(DIB)形式。
 *
 * <p><a href="http://en.wikipedia.org/wiki/BMP_file_format">
 * BMP file format </a>
 */
public final class ShadingUtil {

    /** スフィアマップファイル名(乗算)拡張子。 */
    public static final String EXT_SPH = ".sph";
    /** スフィアマップファイル名(加算)拡張子。 */
    public static final String EXT_SPA = ".spa";

    private static final String SEPARATOR = Pattern.quote("*");
    private static final Pattern SPLITTER = Pattern.compile(SEPARATOR);


    /**
     * 隠しコンストラクタ。
     */
    private ShadingUtil(){
        assert false;
        throw new AssertionError();
    }


    /**
     * スフィアマップファイル名か否か判定する。
     *
     * <p>拡張子が「.sph」(乗算)もしくは「.spa」(加算)なら
     * スフィアマップファイル名と判定する。
     *
     * @param fname ファイル名
     * @return スフィアマップファイルならtrue
     */
    public static boolean isSpheremapFile(String fname) {
        if(fname.endsWith(EXT_SPH)) return true;
        if(fname.endsWith(EXT_SPA)) return true;
        return false;
    }

    /**
     * シェーディング用ファイル情報文字列から
     * テクスチャファイル名とスフィアマップファイル名を分離する。
     *
     * <p>2つのファイル名は単一の「*」で区切られ、
     * 前部がテクスチャファイル名、後部がスフィアマップファイル名となる。
     * 「*」がなく末尾が「.sph」か「.spa」なら
     * スフィアマップファイル名のみ、
     * 末尾がどちらでもなければテクスチャファイル名のみとなる。
     *
     * @param shadingFile シェーディング用ファイル情報
     * @return [0]:テクスチャファイル名 [1]:スフィアマップファイル名。
     *     該当ファイル名が無い場合は空文字列。
     */
    public static String[] splitShadingFileInfo(String shadingFile) {
        String[] result;

        result = SPLITTER.split(shadingFile, 2);
        assert result.length == 1 || result.length == 2;

        if(result.length == 1){
            String onlyFile = result[0];
            if (isSpheremapFile(onlyFile)){
                result = new String[]{ "", onlyFile };
            }else{
                result = new String[]{ onlyFile, "" };
            }
        }

        assert result.length == 2;

        return result;
    }

}
