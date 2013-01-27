/*
 * basic xml exporter
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sourceforge.mikutoga.xml;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.DatatypeConverter;

/**
 * 各種XMLエクスポータの基本機能。
 * UCS4は未サポート。
 */
public class BasicXmlExporter {

    /** デフォルトエンコーディング。 */
    private static final Charset CS_UTF8 = Charset.forName("UTF-8");

    /** デフォルトの改行文字列。 */
    private static final String DEF_NL = "\n";       // 0x0a(LF)
    /** デフォルトのインデント単位。 */
    private static final String DEF_INDENT_UNIT = "\u0020\u0020"; // ␣␣

    private static final char CH_SP     = '\u0020';    // ␣
    private static final char CH_YEN    = '\u00a5';    // ¥
    private static final char CH_BSLASH = (char)0x005c; // \
    private static final char CH_DQ     = '\u0022';    // "
    private static final char CH_SQ     = (char)0x0027; // '

    private static final String COMM_START = "<!--";
    private static final String COMM_END   =   "-->";
    private static final String REF_HEX = "&#x";

    private static final Pattern NUM_FUZZY =
            Pattern.compile("([^.]*\\.[0-9][0-9]*?)0+");

    private static final int HEX_EXP = 4;    // 2 ** 4 == 16
    private static final int MASK_1HEX = (1 << HEX_EXP) - 1;  // 0b00001111
    private static final int MAX_OCTET = (1 << Byte.SIZE) - 1;   // 0xff
    private static final char[] HEXCHAR_TABLE = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F',
    };


    static{
        assert HEX_EXP * 2 == Byte.SIZE;
        assert HEXCHAR_TABLE.length == (1 << HEX_EXP);
    }


    private final Appendable appendable;

    private String newline = DEF_NL;
    private String indentUnit = DEF_INDENT_UNIT;

    private int indentNest = 0;
    private boolean basicLatinOnlyOut = true;


    /**
     * コンストラクタ。
     * 文字エンコーディングはUTF-8が用いられる。
     * @param stream 出力ストリーム
     */
    public BasicXmlExporter(OutputStream stream){
        this(stream, CS_UTF8);
        return;
    }

    /**
     * コンストラクタ。
     * @param stream 出力ストリーム
     * @param charSet 文字エンコーディング指定
     */
    public BasicXmlExporter(OutputStream stream, Charset charSet){
        this(
            new BufferedWriter(
                new OutputStreamWriter(stream, charSet)
            )
        );
        return;
    }

    /**
     * コンストラクタ。
     * @param appendable 文字列出力
     */
    public BasicXmlExporter(Appendable appendable){
        super();
        this.appendable = appendable;
        return;
    }


    /**
     * ASCIIコード相当(UCS:Basic-Latin)の文字か否か判定する。
     * <p>※ Basic-Latinには各種制御文字も含まれる。
     * @param ch 判定対象文字
     * @return Basic-Latin文字ならtrue
     */
    public static boolean isBasicLatin(char ch){
        if('\u0000' <= ch && ch <= '\u007f'){
            return true;
        }
        return false;
    }

    /**
     * 冗長な実数出力を抑止する。
     * <p>DatatypeConverterにおけるJDK1.6系と1.7系の仕様変更を吸収する。
     * <p>0.001fは"0.0010"ではなく"0.001"と出力される。
     * <p>指数表記での冗長桁は無視する。
     * @param numTxt 実数表記
     * @return 冗長桁が抑止された実数表記
     */
    public static String chopFuzzyZero(String numTxt){
        String result;

        Matcher matcher = NUM_FUZZY.matcher(numTxt);
        if(matcher.matches()){
            result = matcher.group(1);
        }else{
            result = numTxt;
        }

        return result;
    }


    /**
     * BasicLatin文字だけで出力するか設定する。
     * <p>BasicLatin以外の文字(≒日本語)を、そのまま出力するか、
     * 文字参照で出力するか、の設定が可能。
     * <p>コメント部中身は対象外。
     * @param bool BasicLatin文字だけで出力するならtrue
     */
    public void setBasicLatinOnlyOut(boolean bool){
        this.basicLatinOnlyOut = bool;
        return;
    }

    /**
     * BasicLatin文字だけを出力する状態か判定する。
     * <p>コメント部中身は対象外。
     * @return BasicLatin文字だけで出力するならtrue
     */
    public boolean isBasicLatinOnlyOut(){
        return this.basicLatinOnlyOut;
    }

    /**
     * 改行文字列を設定する。
     * @param newLine 改行文字列
     * @throws NullPointerException 引数がnull
     */
    public void setNewLine(String newLine) throws NullPointerException{
        if(newLine == null) throw new NullPointerException();
        this.newline = newLine;
        return;
    }

    /**
     * 改行文字列を返す。
     * @return 改行文字列
     */
    public String getNewLine(){
        return this.newline;
    }

    /**
     * インデント単位文字列を設定する。
     * <p>デフォルトでは空白2個。
     * @param indUnit インデント単位文字列。
     * @throws NullPointerException 引数がnull
     */
    public void setIndentUnit(String indUnit) throws NullPointerException{
        if(indUnit == null) throw new NullPointerException();
        this.indentUnit = indUnit;
        return;
    }

    /**
     * インデント単位文字列を返す。
     * @return インデント単位文字列
     */
    public String getIndentUnit(){
        return this.indentUnit;
    }

    /**
     * 可能であれば出力をフラッシュする。
     * @throws IOException 出力エラー
     */
    public void flush() throws IOException{
        if(this.appendable instanceof Flushable){
            ((Flushable)this.appendable).flush();
        }
        return;
    }

    /**
     * 可能であれば出力をクローズする。
     * @throws IOException 出力エラー
     */
    public void close() throws IOException{
        if(this.appendable instanceof Closeable){
            ((Closeable)this.appendable).close();
        }
        return;
    }

    /**
     * 1文字出力する。
     * @param ch 文字
     * @return this本体
     * @throws IOException 出力エラー
     */
    public BasicXmlExporter putRawCh(char ch) throws IOException{
        this.appendable.append(ch);
        return this;
    }

    /**
     * 文字列を出力する。
     * @param seq 文字列
     * @return this本体
     * @throws IOException 出力エラー
     */
    public BasicXmlExporter putRawText(CharSequence seq) throws IOException{
        this.appendable.append(seq);
        return this;
    }

    /**
     * 指定された文字を16進2桁の文字参照形式で出力する。
     * 2桁で出力できない場合(>0x00ff)は4桁で出力する。
     * @param ch 文字
     * @return this本体
     * @throws IOException 出力エラー
     */
    public BasicXmlExporter putCharRef2Hex(char ch) throws IOException{
        if(ch > MAX_OCTET) return putCharRef4Hex(ch);

        int ibits = ch;   // 常に正なので符号拡張なし

        int idx4 = ibits & MASK_1HEX;
        ibits >>= HEX_EXP;
        int idx3 = ibits & MASK_1HEX;

        char hex3 = HEXCHAR_TABLE[idx3];
        char hex4 = HEXCHAR_TABLE[idx4];

        putRawText(REF_HEX).putRawCh(hex3).putRawCh(hex4)
                           .putRawCh(';');

        return this;
    }

    /**
     * 指定された文字を16進4桁の文字参照形式で出力する。
     * UCS4に伴うサロゲートペアは未サポート
     * @param ch 文字
     * @return this本体
     * @throws IOException 出力エラー
     */
    public BasicXmlExporter putCharRef4Hex(char ch) throws IOException{
        int ibits = ch;   // 常に正なので符号拡張なし

        int idx4 = ibits & MASK_1HEX;
        ibits >>= HEX_EXP;
        int idx3 = ibits & MASK_1HEX;
        ibits >>= HEX_EXP;
        int idx2 = ibits & MASK_1HEX;
        ibits >>= HEX_EXP;
        int idx1 = ibits & MASK_1HEX;

        char hex1 = HEXCHAR_TABLE[idx1];
        char hex2 = HEXCHAR_TABLE[idx2];
        char hex3 = HEXCHAR_TABLE[idx3];
        char hex4 = HEXCHAR_TABLE[idx4];

        putRawText(REF_HEX).putRawCh(hex1).putRawCh(hex2)
                           .putRawCh(hex3).putRawCh(hex4)
                           .putRawCh(';');

        return this;
    }

    /**
     * 要素の中身および属性値中身を出力する。
     * <p>XMLの構文規則を守る上で必要な各種エスケープ処理が行われる。
     * @param ch 文字
     * @return this本体
     * @throws IOException 出力エラー
     */
    public BasicXmlExporter putCh(char ch) throws IOException{
        if(Character.isISOControl(ch)){
            putCharRef2Hex(ch);
            return this;
        }

        String escTxt;
        switch(ch){
        case '&':   escTxt = "&amp;";  break;
        case '<':   escTxt = "&lt;";   break;
        case '>':   escTxt = "&gt;";   break;
        case CH_DQ: escTxt = "&quot;"; break;
        case CH_SQ: escTxt = "&apos;"; break;
        default:    escTxt = null;     break;
        }

        if(escTxt != null){
            putRawText(escTxt);
        }else{
            putRawCh(ch);
        }

        return this;
    }

    /**
     * 要素の中身および属性値中身を出力する。
     * <p>必要に応じてXML定義済み実体文字が割り振られた文字、
     * コントロールコード、および非BasicLatin文字がエスケープされる。
     * <p>半角通貨記号U+00A5はバックスラッシュU+005Cに置換される。
     * <p>連続するスペースU+0020の2文字目以降は文字参照化される。
     * <p>全角スペースその他空白文字は無条件に文字参照化される。
     * @param content 内容
     * @return this本体
     * @throws IOException 出力エラー
     */
    public BasicXmlExporter putContent(CharSequence content)
            throws IOException{
        int length = content.length();

        char prev = '\0';
        for(int pos = 0; pos < length; pos++){
            char ch = content.charAt(pos);

            if( isBasicLatinOnlyOut() && ! isBasicLatin(ch) ){
                putCharRef4Hex(ch);
            }else if(ch == CH_YEN){
                putRawCh(CH_BSLASH);
            }else if(Character.isSpaceChar(ch)){
                if(ch == CH_SP && prev != CH_SP){
                    putRawCh(ch);
                }else{
                    putCharRef2Hex(ch);
                }
            }else{
                putCh(ch);
            }

            prev = ch;
        }

        return this;
    }

    /**
     * 改行を出力する。
     * @return this本体
     * @throws IOException 出力エラー
     */
    public BasicXmlExporter ln() throws IOException{
        this.appendable.append(this.newline);
        return this;
    }

    /**
     * 改行を指定回数出力する。
     * @param count 改行回数。0以下の場合は何も出力しない。
     * @return this本体
     * @throws IOException 出力エラー
     */
    public BasicXmlExporter ln(int count) throws IOException{
        for(int ct = 1; ct <= count; ct++){
            this.appendable.append(this.newline);
        }
        return this;
    }

    /**
     * 空白を出力する。
     * @return this本体
     * @throws IOException 出力エラー
     */
    public BasicXmlExporter sp() throws IOException{
        this.appendable.append(CH_SP);
        return this;
    }

    /**
     * 空白を指定回数出力する。
     * @param count 空白回数。0以下の場合は何も出力しない。
     * @return this本体
     * @throws IOException 出力エラー
     */
    public BasicXmlExporter sp(int count) throws IOException{
        for(int ct = 1; ct <= count; ct++){
            this.appendable.append(CH_SP);
        }
        return this;
    }

    /**
     * インデントを出力する。
     * インデント単位文字列をネストレベル回数分出力する。
     * @return this本体
     * @throws IOException 出力エラー
     */
    public BasicXmlExporter ind() throws IOException{
        for(int ct = 1; ct <= this.indentNest; ct++){
            putRawText(this.indentUnit);
        }
        return this;
    }

    /**
     * インデントレベルを一段下げる。
     */
    public void pushNest(){
        this.indentNest++;
        return;
    }

    /**
     * インデントレベルを一段上げる。
     * インデントレベル0の状態をさらに上げようとした場合、何も起こらない。
     */
    public void popNest(){
        this.indentNest--;
        if(this.indentNest < 0) this.indentNest = 0;
        return;
    }

    /**
     * int値をXMLスキーマ準拠の形式で出力する。
     * @param iVal int値
     * @return this本体
     * @throws IOException 出力エラー
     * @see java.lang.Integer#toString(int)
     */
    public BasicXmlExporter putXsdInt(int iVal) throws IOException{
        String value = DatatypeConverter.printInt(iVal);
        this.appendable.append(value);
        return this;
    }

    /**
     * float値をXMLスキーマ準拠の形式で出力する。
     * @param fVal float値
     * @return this本体
     * @throws IOException 出力エラー
     * @see java.lang.Float#toString(float)
     */
    public BasicXmlExporter putXsdFloat(float fVal) throws IOException{
        String value = DatatypeConverter.printFloat(fVal);
        this.appendable.append(value);
        return this;
    }

    /**
     * 属性値を出力する。
     * @param attrName 属性名
     * @param content 属性内容
     * @return this本体
     * @throws IOException 出力エラー
     */
    public BasicXmlExporter putAttr(CharSequence attrName,
                                     CharSequence content)
            throws IOException{
        putRawText(attrName).putRawCh('=');

        putRawCh('"');
        putContent(content);
        putRawCh('"');

        return this;
    }

    /**
     * int型属性値を出力する。
     * @param attrName 属性名
     * @param iVal int値
     * @return this本体
     * @throws IOException 出力エラー
     */
    public BasicXmlExporter putIntAttr(CharSequence attrName,
                                        int iVal)
            throws IOException{
        String attrValue = DatatypeConverter.printInt(iVal);
        putAttr(attrName, attrValue);
        return this;
    }

    /**
     * float型属性値を出力する。
     * @param attrName 属性名
     * @param fVal float値
     * @return this本体
     * @throws IOException 出力エラー
     */
    public BasicXmlExporter putFloatAttr(CharSequence attrName,
                                           float fVal)
            throws IOException{
        String attrValue = DatatypeConverter.printFloat(fVal);
        attrValue = chopFuzzyZero(attrValue);
        putAttr(attrName, attrValue);
        return this;
    }

    /**
     * コメントの内容を出力する。
     * <p>コメント中の'\n'記号出現に伴い、
     * あらかじめ指定された改行文字が出力される。
     * <p>コメント中の'\n'以外のコントロールコードは
     * Control Pictures(U+2400〜)で代替される。
     * <p>それ以外の非BasicLatin文字はそのまま出力される。
     * <p>連続するハイフン(-)記号間には強制的にスペースが挿入される。
     * @param comment コメント内容
     * @return this本体
     * @throws IOException 出力エラー
     */
    public BasicXmlExporter putCommentContent(CharSequence comment)
            throws IOException{
        int length = comment.length();

        char prev = '\0';
        for(int pos = 0; pos < length; pos++){
            char ch = comment.charAt(pos);

            if(ch == '\n'){
                ln();
            }else if('\u0000' <= ch && ch <= '\u001f'){
                putRawCh((char)('\u2400' + ch));
            }else if(ch == '\u007f'){
                putRawCh('\u2421');
            }else if(prev == '-' && ch == '-'){
                sp().putRawCh(ch);
            }else{
                putRawCh(ch);
            }

            prev = ch;
        }

        return this;
    }

    /**
     * 1行コメントを出力する。
     * コメント内部の頭及び末尾に空白が1つ挿入される。
     * @param comment コメント内容
     * @return this本体
     * @throws IOException 出力エラー
     */
    public BasicXmlExporter putLineComment(CharSequence comment)
            throws IOException{
        putRawText(COMM_START).sp();
        putCommentContent(comment);
        sp().putRawText(COMM_END);
        return this;
    }

    /**
     * ブロックコメントを出力する。
     * <p>コメント内部の頭の前に改行が出力される。
     * <p>コメント内部の末尾が改行でない場合、改行が挿入される。
     * <p>ブロックコメント末尾は改行で終わる。
     * <p>インデント設定は無視される。
     * @param comment コメント内容
     * @return this本体
     * @throws IOException 出力エラー
     */
    public BasicXmlExporter putBlockComment(CharSequence comment)
            throws IOException{
        putRawText(COMM_START).ln();

        putCommentContent(comment);

        int commentLength = comment.length();
        if(commentLength > 0){
            char lastCh = comment.charAt(commentLength - 1);
            if(lastCh != '\n'){
                ln();
            }
        }

        putRawText(COMM_END).ln();

        return this;
    }

}
