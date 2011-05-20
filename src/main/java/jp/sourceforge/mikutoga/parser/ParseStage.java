/*
 * parse-processing stage
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sourceforge.mikutoga.parser;

/**
 * パース処理の進行ステージ種別を表す。
 * ループ構造の識別に用いられる。
 */
public abstract class ParseStage {

    private final String name;

    /**
     * コンストラクタ。
     * 進行ステージ名は実行時クラス名(パッケージ名抜き)が指定される。
     */
    protected ParseStage(){
        super();

        Class<?> klass = getClass();
        String fullName = klass.getName();
        int idx = fullName.lastIndexOf('.');

        String stripName;
        if(idx >= 0){
            stripName = fullName.substring(idx + 1);
        }else{
            stripName = fullName;
        }

        this.name = stripName;

        return;
    }

    /**
     * コンストラクタ。
     * @param name 進行ステージ名
     * @throws NullPointerException 引数がnull
     */
    protected ParseStage(String name) throws NullPointerException{
        super();
        if(name == null) throw new NullPointerException();
        this.name = name;
        return;
    }

    /**
     * {@inheritDoc}
     * 進行ステージ名を返す。
     * @return {@inheritDoc} 進行ステージ名
     */
    @Override
    public String toString(){
        return this.name;
    }

}
