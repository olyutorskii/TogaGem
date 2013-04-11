/*
 * illegal model exception
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sfjp.mikutoga.pmd;

/**
 * モデルデータの不備を発見した場合の例外。
 */
@SuppressWarnings("serial")
public class IllegalPmdDataException extends Exception{

    /**
     * コンストラクタ。
     */
    public IllegalPmdDataException(){
        super();
        return;
    }

    /**
     * コンストラクタ。
     * @param message メッセージ
     */
    public IllegalPmdDataException(String message){
        super(message);
        return;
    }

    /**
     * コンストラクタ。
     * @param cause 原因
     */
    public IllegalPmdDataException(Throwable cause){
        super(cause);
        return;
    }

}
