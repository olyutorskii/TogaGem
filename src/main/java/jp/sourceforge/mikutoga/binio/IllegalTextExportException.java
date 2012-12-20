/*
 * illegal text exporting exception
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.binio;

import java.nio.charset.CharacterCodingException;

/**
 * バイナリファイルへの不正なテキスト情報の出力が検出された場合の例外。
 * {@link java.nio.charset.CharacterCodingException}に由来する異常系を
 * {@link java.io.IOException}から分離するために設計された。
 * <p>
 * 異常系の発生した理由としては
 * <ul>
 * <li>所定のフォーマットに対し文字列が長すぎる。
 * <li>文字エンコーディングできない文字が含まれている
 * <li>Unicode文字列として既に変。
 * </ul>
 * など。
 */
@SuppressWarnings("serial")
public class IllegalTextExportException extends Exception {

    /**
     * コンストラクタ。
     */
    public IllegalTextExportException(){
        super();
        return;
    }

    /**
     * コンストラクタ。
     * @param message 詳細メッセージ
     */
    public IllegalTextExportException(String message){
        super(message);
        return;
    }

    /**
     * コンストラクタ。
     * @param cause 原因
     */
    public IllegalTextExportException(CharacterCodingException cause){
        super(cause);
        return;
    }

    /**
     * コンストラクタ。
     * @param message 詳細メッセージ
     * @param cause 原因
     */
    public IllegalTextExportException(String message,
                                          CharacterCodingException cause){
        super(message, cause);
        return;
    }

}
