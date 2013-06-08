/*
 * sample parser
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package sample.vmd;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import jp.sfjp.mikutoga.bin.parser.MmdFormatException;
import jp.sfjp.mikutoga.vmd.parser.VmdParser;

/**
 * パーサ利用のサンプルプログラム。
 * これはユニットテストではない。
 */
public class DummyMain {

    private static final String VMDFILE;
    private static final int BUF_SZ = 4086;
    private static final DummyHandler handler = new DummyHandler();

    static{
        VMDFILE = "D:\\Test\\test.vmd";
//        VMDFILE = "D:\\Test\\camera.vmd";
    }

    private static InputStream buildSource(String fname){
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

        return is;
    }

    private static void setupHandler(VmdParser parser){
        parser.setBasicHandler(handler);
        parser.setLightingHandler(handler);
        parser.setCameraHandler(handler);
        parser.setBoolHandler(handler);

        return;
    }

    public static void main(String[] args){
        String fname;
        if(args.length == 1) fname = args[0];
        else                 fname = VMDFILE;

        InputStream source = buildSource(fname);

        VmdParser parser = new VmdParser(source);

        setupHandler(parser);
        parser.setRedundantCheck(false);

        try{
            parser.parseVmd();
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
