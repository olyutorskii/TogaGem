/*
 * illegal VMD motion data exception
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sfjp.mikutoga.vmd;

/**
 * VMDモーションデータの不備を発見した場合の例外。
 */
@SuppressWarnings("serial")
public class IllegalVmdDataException extends Exception{

    /**
     * コンストラクタ。
     */
    public IllegalVmdDataException(){
        super();
        return;
    }

    /**
     * コンストラクタ。
     *
     * @param message 詳細メッセージ
     */
    public IllegalVmdDataException(String message){
        super(message);
        return;
    }

    /**
     * コンストラクタ。
     *
     * @param cause 原因
     */
    public IllegalVmdDataException(Throwable cause){
        super(cause);
        return;
    }

    /**
     * コンストラクタ。
     *
     * @param message 詳細メッセージ
     * @param cause 原因
     */
    public IllegalVmdDataException(String message, Throwable cause){
        super(message, cause);
        return;
    }

}
