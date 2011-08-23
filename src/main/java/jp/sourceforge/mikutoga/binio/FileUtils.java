/*
 * file utils
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.binio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * ファイルユーティリティ。
 */
public final class FileUtils {

    /**
     * コンストラクタ。
     */
    private FileUtils(){
        super();
        assert false;
        throw new AssertionError();
    }


    /**
     * 既に存在する通常ファイルか否か判定する。
     * @param file 判定対象
     * @return 既に存在する通常ファイルならtrue
     */
    public static boolean isExistsNormalFile(File file){
        if( ! file.exists() ) return false;
        if( ! file.isFile() ) return false;
        return true;
    }

    /**
     * 既に存在する特殊ファイルか否か判定する。
     * @param file 判定対象
     * @return 既に存在する特殊ファイルならtrue
     */
    public static boolean isExistsUnnormalFile(File file){
        if( ! file.exists() ) return false;
        if( file.isFile() ) return false;
        return true;
    }

    /**
     * ファイルサイズを0に切り詰める。
     * <p>既に存在する通常ファイルでないならなにもしない。
     * @param file ファイル
     * @throws IOException 入出力エラー
     */
    public static void trunc(File file) throws IOException{
        if( ! isExistsNormalFile(file) ) return;
        if(file.length() <= 0L) return;

        FileOutputStream foStream = new FileOutputStream(file);
        try{
            FileChannel channnel = foStream.getChannel();
            try{
                channnel.truncate(0L);
            }finally{
                channnel.close();
            }
        }finally{
            foStream.close();
        }

        assert file.length() <= 0L;

        return;
    }

}
