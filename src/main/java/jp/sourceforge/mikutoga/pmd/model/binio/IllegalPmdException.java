/*
 * illegal model exception
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sourceforge.mikutoga.pmd.model.binio;

/**
 * モデルデータの不備を発見した場合の例外。
 */
@SuppressWarnings("serial")
public class IllegalPmdException extends Exception{

    /**
     * コンストラクタ。
     */
    public IllegalPmdException(){
        super();
        return;
    }

    /**
     * コンストラクタ。
     * @param message メッセージ
     */
    public IllegalPmdException(String message){
        super(message);
        return;
    }

    /**
     * コンストラクタ。
     * @param cause 原因
     */
    public IllegalPmdException(Throwable cause){
        super(cause);
        return;
    }

}
