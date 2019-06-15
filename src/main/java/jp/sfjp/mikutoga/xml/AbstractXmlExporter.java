/*
 * abstract xml exporter
 *
 * License : The MIT License
 * Copyright(c) 2013 MikuToga Partners
 */

package jp.sfjp.mikutoga.xml;

import java.io.IOException;

/**
 * Appendable実装に依存したXMLエクスポータの半実装。
 * UCS4は未サポート。
 */
abstract class AbstractXmlExporter implements XmlExporter{

    /** デフォルトの改行文字列。 */
    private static final String DEF_NL = "\n";       // 0x0a(LF)
    /** デフォルトのインデント単位。 */
    private static final String DEF_INDENT_UNIT = "\u0020\u0020"; // ␣␣

    private static final char CH_SP     = '\u0020';    // ␣
    private static final char CH_YEN    = '\u00a5';    // ¥
    private static final char CH_BSLASH = (char)0x005c; // \
    private static final char CH_DQ     = '\u0022';    // "
    private static final char CH_SQ     = (char)0x0027; // '
    private static final char CH_EQ     = '=';          // =
    private static final char CH_LT     = '<';
    private static final char CH_GT     = '>';

    private static final String COMM_START = "<!--";
    private static final String COMM_END   =   "-->";

    private static final String REF_HEX = "&#x";
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


    private boolean basicLatinOnlyOut = true;
    private String newline = DEF_NL;
    private String indentUnit = DEF_INDENT_UNIT;
    private int indentNest = 0;


    /**
     * コンストラクタ。
     */
    protected AbstractXmlExporter(){
        super();
        return;
    }


    /**
     * ASCIIコード相当(UCS:Basic-Latin)の文字か否か判定する。
     *
     * <p>※ Basic-Latinには各種制御文字も含まれる。
     *
     * @param ch 判定対象文字
     * @return Basic-Latin文字ならtrue
     * <a href="http://www.unicode.org/charts/PDF/U0000.pdf">
     * Unicode 6.2 Controls and Basic Latin
     * </a>
     */
    protected static boolean isBasicLatin(char ch){
        if('\u0000' <= ch && ch <= '\u007f'){
            return true;
        }
        return false;
    }


    /**
     * {@inheritDoc}
     * @param ch {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public abstract Appendable append(char ch) throws IOException;

    /**
     * {@inheritDoc}
     * @param seq {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public abstract Appendable append(CharSequence seq) throws IOException;

    /**
     * {@inheritDoc}
     * @param seq {@inheritDoc}
     * @param start {@inheritDoc}
     * @param end {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public abstract Appendable append(CharSequence seq, int start, int end)
            throws IOException;

    /**
     * {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public abstract void flush() throws IOException;

    /**
     * {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public abstract void close() throws IOException;


    /**
     * {@inheritDoc}
     * @param ch {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter putRawCh(char ch) throws IOException{
        append(ch);
        return this;
    }

    /**
     * {@inheritDoc}
     * @param seq {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter putRawText(CharSequence seq)
            throws IOException{
        append(seq);
        return this;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter sp() throws IOException{
        putRawCh(CH_SP);
        return this;
    }

    /**
     * {@inheritDoc}
     * @param count {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter sp(int count) throws IOException{
        for(int ct = 1; ct <= count; ct++){
            sp();
        }
        return this;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String getNewLine(){
        return this.newline;
    }

    /**
     * {@inheritDoc}
     * @param newLine {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void setNewLine(String newLine) throws NullPointerException{
        if(newLine == null) throw new NullPointerException();
        this.newline = newLine;
        return;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter ln() throws IOException{
        putRawText(getNewLine());
        return this;
    }

    /**
     * {@inheritDoc}
     * @param count {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter ln(int count) throws IOException{
        for(int ct = 1; ct <= count; ct++){
            ln();
        }
        return this;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String getIndentUnit(){
        return this.indentUnit;
    }

    /**
     * {@inheritDoc}
     * @param indUnit {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void setIndentUnit(String indUnit) throws NullPointerException{
        if(indUnit == null) throw new NullPointerException();
        this.indentUnit = indUnit;
        return;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pushNest(){
        this.indentNest++;
        return;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void popNest(){
        this.indentNest--;
        if(this.indentNest < 0) this.indentNest = 0;
        return;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public int getIndentLevel(){
        return this.indentNest;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter ind() throws IOException{
        int level = getIndentLevel();
        for(int ct = 1; ct <= level; ct++){
            putRawText(getIndentUnit());
        }
        return this;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean isBasicLatinOnlyOut(){
        return this.basicLatinOnlyOut;
    }

    /**
     * {@inheritDoc}
     * @param bool {@inheritDoc}
     */
    @Override
    public void setBasicLatinOnlyOut(boolean bool){
        this.basicLatinOnlyOut = bool;
        return;
    }

    /**
     * {@inheritDoc}
     * @param ch {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter putCharRef2Hex(char ch) throws IOException{
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
     * {@inheritDoc}
     * @param ch {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter putCharRef4Hex(char ch) throws IOException{
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
     * {@inheritDoc}
     * @param ch {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter putCh(char ch) throws IOException{
        if(Character.isISOControl(ch)){
            putCharRef2Hex(ch);
            return this;
        }

        String escTxt;
        switch(ch){
        case '&':   escTxt = "&amp;";  break;
        case CH_LT: escTxt = "&lt;";   break;
        case CH_GT: escTxt = "&gt;";   break;
        case CH_DQ: escTxt = "&quot;"; break;
        case CH_SQ: escTxt = "&apos;"; break;
        default:    return putRawCh(ch);
        }

        putRawText(escTxt);

        return this;
    }

    /**
     * {@inheritDoc}
     * @param content {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter putContent(CharSequence content)
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
     * {@inheritDoc}
     * @param comment {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter putCommentContent(CharSequence comment)
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
     * {@inheritDoc}
     * @param comment {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter putLineComment(CharSequence comment)
            throws IOException{
        putRawText(COMM_START).sp();
        putCommentContent(comment);
        sp().putRawText(COMM_END);
        return this;
    }

    /**
     * {@inheritDoc}
     * @param comment {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter putBlockComment(CharSequence comment)
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

    /**
     * {@inheritDoc}
     * @param tagName {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter putOpenSTag(CharSequence tagName)
            throws IOException{
        putRawCh(CH_LT);
        putRawText(tagName);
        return this;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter putCloseSTag()
            throws IOException{
        putRawCh(CH_GT);
        return this;
    }

    /**
     * {@inheritDoc}
     * @param tagName {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter putSimpleSTag(CharSequence tagName)
            throws IOException{
        putRawCh(CH_LT);
        putRawText(tagName);
        putRawCh(CH_GT);
        return this;
    }

    /**
     * {@inheritDoc}
     * @param tagName {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter putETag(CharSequence tagName)
            throws IOException{
        putRawText("</");
        putRawText(tagName);
        putRawCh(CH_GT);
        return this;
    }

    /**
     * {@inheritDoc}
     * @param tagName {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter putSimpleEmpty(CharSequence tagName)
            throws IOException{
        putRawCh(CH_LT);
        putRawText(tagName).sp();
        putCloseEmpty();
        return this;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter putCloseEmpty()
            throws IOException{
        putRawText("/>");
        return this;
    }

    /**
     * {@inheritDoc}
     * @param iVal {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter putXsdInt(int iVal) throws IOException{
        String value = DatatypeIo.printInt(iVal);
        putRawText(value);
        return this;
    }

    /**
     * {@inheritDoc}
     * @param fVal {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter putXsdFloat(float fVal) throws IOException{
        String value = DatatypeIo.printFloat(fVal);
        putRawText(value);
        return this;
    }

    /**
     * {@inheritDoc}
     * @param attrName {@inheritDoc}
     * @param iVal {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter putIntAttr(CharSequence attrName,
                                        int iVal)
            throws IOException{
        putRawText(attrName).putRawCh(CH_EQ);

        putRawCh(CH_DQ);
        putXsdInt(iVal);
        putRawCh(CH_DQ);

        return this;
    }

    /**
     * {@inheritDoc}
     * @param attrName {@inheritDoc}
     * @param fVal {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter putFloatAttr(CharSequence attrName,
                                           float fVal)
            throws IOException{
        putRawText(attrName).putRawCh(CH_EQ);

        putRawCh(CH_DQ);
        putXsdFloat(fVal);
        putRawCh(CH_DQ);

        return this;
    }

    /**
     * {@inheritDoc}
     * @param attrName {@inheritDoc}
     * @param content {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter putAttr(CharSequence attrName,
                                     CharSequence content)
            throws IOException{
        putRawText(attrName).putRawCh(CH_EQ);

        putRawCh(CH_DQ);
        putContent(content);
        putRawCh(CH_DQ);

        return this;
    }

}
