/*
 * sample parser
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package sample.pmd;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import jp.sourceforge.mikutoga.parser.MmdFormatException;
import jp.sourceforge.mikutoga.parser.MmdSource;
import jp.sourceforge.mikutoga.parser.pmd.PmdParser;

/**
 * パーサ利用のサンプルプログラム。
 */
public class DummyMain {

    private static final String PMDFILE;
    private static final int BUF_SZ = 4086;
    private static final DummyHandler handler = new DummyHandler();

    static{
        PMDFILE = "D:\\Test\\test.pmd";
    }

    /**
     * 入力ソースを準備する。
     * @param fname ファイル名
     * @return 入力ソース
     */
    private static MmdSource buildSource(String fname){
        File file = new File(fname);

        InputStream is;
        try{
            is = new FileInputStream(file);
        }catch(FileNotFoundException e){
            System.err.println(e);
            System.exit(1);
            return null;
        }
        is = new BufferedInputStream(is, BUF_SZ);

        MmdSource source = new MmdSource(is);

        return source;
    }

    /**
     * 各種ハンドラをパーサにセットアップする。
     * @param parser パーサ
     */
    private static void setupHandler(PmdParser parser){
        parser.setBasicHandler(handler);
        parser.setShapeHandler(handler);
        parser.setMaterialHandler(handler);
        parser.setBoneHandler(handler);
        parser.setMorphHandler(handler);
        parser.setEngHandler(handler);
        parser.setToonHandler(handler);
        parser.setRigidHandler(handler);
        parser.setJointHandler(handler);

        return;
    }

    /**
     * Java実行エントリ
     * @param args 起動引数
     */
    public static void main(String[] args){
        String fname;
        if(args.length == 1) fname = args[0];
        else                 fname = PMDFILE;

        MmdSource source = buildSource(fname);

        PmdParser parser = new PmdParser(source);

        setupHandler(parser);

        try{
            parser.parsePmd();
        }catch(IOException e){
            System.err.println(e);
            System.exit(1);
        }catch(MmdFormatException e){
            System.err.println(e);
            System.exit(1);
        }

        System.exit(0);

        return;
    }

}
