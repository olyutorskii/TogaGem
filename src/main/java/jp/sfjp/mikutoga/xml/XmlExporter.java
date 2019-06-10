/*
 * xml exporter
 *
 * License : The MIT License
 * Copyright(c) 2013 MikuToga Partners
 */

package jp.sfjp.mikutoga.xml;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

/**
 * XMLエクスポータ基本機能のセット。
 */
public interface XmlExporter extends Appendable, Flushable, Closeable{

    /**
     * 1文字を生出力する。
     * @param ch 文字
     * @return this本体
     * @throws IOException 出力エラー
     */
    XmlExporter putRawCh(char ch) throws IOException;

    /**
     * 文字列を生出力する。
     * @param seq 文字列
     * @return this本体
     * @throws IOException 出力エラー
     */
    XmlExporter putRawText(CharSequence seq) throws IOException;

    /**
     * 空白を出力する。
     * @return this本体
     * @throws IOException 出力エラー
     */
    XmlExporter sp() throws IOException;

    /**
     * 空白を指定回数出力する。
     * @param count 空白回数。0以下の場合は何も出力しない。
     * @return this本体
     * @throws IOException 出力エラー
     */
    XmlExporter sp(int count) throws IOException;

    /**
     * 改行文字列を返す。
     * @return 改行文字列
     */
    String getNewLine();

    /**
     * 改行文字列を設定する。
     * @param newLine 改行文字列
     * @throws NullPointerException 引数がnull
     */
    void setNewLine(String newLine) throws NullPointerException;

    /**
     * 改行を出力する。
     * @return this本体
     * @throws IOException 出力エラー
     */
    XmlExporter ln() throws IOException;

    /**
     * 改行を指定回数出力する。
     * @param count 改行回数。0以下の場合は何も出力しない。
     * @return this本体
     * @throws IOException 出力エラー
     */
    XmlExporter ln(int count) throws IOException;

    /**
     * インデント単位文字列を返す。
     * @return インデント単位文字列
     */
    String getIndentUnit();

    /**
     * インデント単位文字列を設定する。
     *
     * <p>デフォルトでは空白2個。
     *
     * @param indUnit インデント単位文字列。
     * @throws NullPointerException 引数がnull
     */
    void setIndentUnit(String indUnit) throws NullPointerException;

    /**
     * インデントレベルを一段下げる。
     */
    void pushNest();

    /**
     * インデントレベルを一段上げる。
     * インデントレベル0の状態をさらに上げようとした場合、何も起こらない。
     */
    void popNest();

    /**
     * インデントレベルを返す。
     *
     * <p>深さ1の場合1を返す。
     *
     * @return インデントレベル
     */
    int getIndentLevel();

    /**
     * インデントを出力する。
     * インデント単位文字列をネストレベル回数分出力する。
     * @return this本体
     * @throws IOException 出力エラー
     */
    XmlExporter ind() throws IOException;

    /**
     * BasicLatin文字だけを出力する状態か判定する。
     *
     * <p>コメント部中身は対象外。
     *
     * @return BasicLatin文字だけで出力するならtrue
     */
    boolean isBasicLatinOnlyOut();

    /**
     * BasicLatin文字だけで出力するか設定する。
     *
     * <p>BasicLatin以外の文字(≒日本語)を、そのまま出力するか、
     * 文字参照で出力するか、の設定が可能。
     *
     * <p>コメント部中身は対象外。
     *
     * @param bool BasicLatin文字だけで出力するならtrue
     */
    void setBasicLatinOnlyOut(boolean bool);

    /**
     * 指定された文字を16進2桁の文字参照形式で出力する。
     *
     * <p>「A」は「&amp;#x41;」になる。
     *
     * <p>2桁で出力できない場合(&gt;0x00ff)は4桁で出力する。
     *
     * @param ch 文字
     * @return this本体
     * @throws IOException 出力エラー
     * @see <a href="http://www.w3.org/TR/xml11/#NT-CharRef">
     * W3C XML1.1 Character Reference
     * </a>
     */
    XmlExporter putCharRef2Hex(char ch) throws IOException;

    /**
     * 指定された文字を16進4桁の文字参照形式で出力する。
     *
     * <p>「亜」は「&amp;#x4E9C;」になる。
     *
     * <p>UCS4に伴うサロゲートペアは未サポート
     *
     * @param ch 文字
     * @return this本体
     * @throws IOException 出力エラー
     * @see <a href="http://www.w3.org/TR/xml11/#NT-CharRef">
     * W3C XML1.1 Character Reference
     * </a>
     */
    XmlExporter putCharRef4Hex(char ch) throws IOException;

    /**
     * 要素の中身および属性値中身を出力する。
     *
     * <p>XMLの構文規則を守る上で必要な各種エスケープ処理が行われる。
     *
     * @param ch 文字
     * @return this本体
     * @throws IOException 出力エラー
     */
    XmlExporter putCh(char ch) throws IOException;

    /**
     * 要素の中身および属性値中身を出力する。
     *
     * <p>必要に応じてXML定義済み実体文字が割り振られた文字、
     * コントロールコード、および非BasicLatin文字がエスケープされる。
     *
     * <p>半角円通貨記号U+00A5はバックスラッシュU+005Cに置換される。
     *
     * <p>連続するスペースU+0020の2文字目以降は文字参照化される。
     *
     * <p>全角スペースその他空白文字は無条件に文字参照化される。
     *
     * @param content 内容
     * @return this本体
     * @throws IOException 出力エラー
     */
    XmlExporter putContent(CharSequence content) throws IOException;

    /**
     * コメントの内容を出力する。
     *
     * <p>コメント中の'\n'記号出現に伴い、
     * あらかじめ指定された改行文字が出力される。
     *
     * <p>コメント中の'\n'以外のコントロールコードは
     * Control Pictures(U+2400〜)で代替される。
     *
     * <p>それ以外の非BasicLatin文字はそのまま出力される。
     *
     * <p>連続するハイフン(-)記号間には強制的にスペースが挿入される。
     *
     * @param comment コメント内容
     * @return this本体
     * @throws IOException 出力エラー
     * <a href="http://www.unicode.org/charts/PDF/U2400.pdf">
     * Unicode 6.2 Controll Pictures
     * </a>
     */
    XmlExporter putCommentContent(CharSequence comment) throws IOException;

    /**
     * 1行コメントを出力する。
     * コメント内部の頭及び末尾に空白が1つ挿入される。
     * @param comment コメント内容
     * @return this本体
     * @throws IOException 出力エラー
     */
    XmlExporter putLineComment(CharSequence comment) throws IOException;

    /**
     * ブロックコメントを出力する。
     *
     * <p>コメント内部の頭の前に改行が出力される。
     *
     * <p>コメント内部の末尾が改行でない場合、改行が挿入される。
     *
     * <p>ブロックコメント末尾は改行で終わる。
     *
     * <p>インデント設定は無視される。
     *
     * @param comment コメント内容
     * @return this本体
     * @throws IOException 出力エラー
     */
    XmlExporter putBlockComment(CharSequence comment) throws IOException;

    /**
     * 開始タグ開き表記を出力する。
     * @param tagName タグ名
     * @return this本体
     * @throws IOException 出力エラー
     */
    XmlExporter putOpenSTag(CharSequence tagName) throws IOException;

    /**
     * 開始タグ閉じ表記を出力する。
     * @return this本体
     * @throws IOException 出力エラー
     */
    XmlExporter putCloseSTag() throws IOException;

    /**
     * 属性の無いシンプルな開始タグ表記を出力する。
     * @param tagName タグ名
     * @return this本体
     * @throws IOException 出力エラー
     */
    XmlExporter putSimpleSTag(CharSequence tagName) throws IOException;

    /**
     * 終了タグ表記を出力する。
     * @param tagName タグ名
     * @return this本体
     * @throws IOException 出力エラー
     */
    XmlExporter putETag(CharSequence tagName) throws IOException;

    /**
     * 属性の無い単出タグ表記を出力する。
     * @param tagName タグ名
     * @return this本体
     * @throws IOException 出力エラー
     */
    XmlExporter putSimpleEmpty(CharSequence tagName) throws IOException;

    /**
     * 単出タグ閉じ表記を出力する。
     * @return this本体
     * @throws IOException 出力エラー
     */
    XmlExporter putCloseEmpty() throws IOException;

    /**
     * xsd:int値をXMLスキーマ準拠の形式で出力する。
     * @param iVal int値
     * @return this本体
     * @throws IOException 出力エラー
     * @see <a href="http://www.w3.org/TR/xmlschema11-2/#int">
     * XML Schema 1.1 Datatypes int
     * </a>
     */
    XmlExporter putXsdInt(int iVal) throws IOException;

    /**
     * xsd:float値をXMLスキーマ準拠の形式で出力する。
     * @param fVal float値
     * @return this本体
     * @throws IOException 出力エラー
     * @see <a href="http://www.w3.org/TR/xmlschema11-2/#sec-lex-float">
     * XML Schema 1.1 Datatypes float Lexical Mapping
     * </a>
     */
    XmlExporter putXsdFloat(float fVal) throws IOException;

    /**
     * int型属性値を出力する。
     * @param attrName 属性名
     * @param iVal int値
     * @return this本体
     * @throws IOException 出力エラー
     */
    XmlExporter putIntAttr(CharSequence attrName, int iVal)
            throws IOException;

    /**
     * float型属性値を出力する。
     * @param attrName 属性名
     * @param fVal float値
     * @return this本体
     * @throws IOException 出力エラー
     */
    XmlExporter putFloatAttr(CharSequence attrName, float fVal)
            throws IOException;

    /**
     * 属性値を出力する。
     * @param attrName 属性名
     * @param content 属性内容
     * @return this本体
     * @throws IOException 出力エラー
     */
    XmlExporter putAttr(CharSequence attrName, CharSequence content)
            throws IOException;

}
