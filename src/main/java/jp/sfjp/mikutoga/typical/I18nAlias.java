/*
 * internationalization name alias
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sfjp.mikutoga.typical;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * 国際化&amp;別名管理オブジェクトの実装基板。
 *
 * <p>別名管理オブジェクトは、
 * 各々のリストの先頭が代表名となる、
 * プライマリ名の不変リストとグローバル名の不変リストを持つ。
 *
 * <p>国産モデルではプライマリ名に日本語名が収められることが多い。
 * プライマリ名は必ず一つ以上なければならない。
 *
 * <p>国産モデルではグローバル名に英語名が収められることが多いが、
 * プライマリ名と同一の日本語名が収められている場合も多い。
 *
 * <p>別名管理オブジェクトは、
 * インスタンス間での順序を定義するためのオーダー番号を持つ。
 */
class I18nAlias {

    /** オーダ番号によるコンパレータ。 */
    public static final Comparator<I18nAlias> ORDER_COMPARATOR =
            new OrderComparator();


    private int orderNo;

    private final List<String> primaryNameList;
    private final List<String> globalNameList;

    private final List<String> umodPrimaryNameList;
    private final List<String> umodGlobalNameList;


    /**
     * コンストラクタ。
     *
     * <p>各初期数が0以下の場合は、
     * 状況に応じて伸長する連結リストが用意される。
     *
     * @param primaryNum プライマリ名初期数。
     * @param globalNum グローバル名初期数。
     */
    protected I18nAlias(int primaryNum, int globalNum){
        super();

        if(primaryNum <= 0){
            this.primaryNameList = new LinkedList<>();
        }else{
            this.primaryNameList = new ArrayList<>(primaryNum);
        }

        if(globalNum <= 0){
            this.globalNameList  = new LinkedList<>();
        }else{
            this.globalNameList  = new ArrayList<>(globalNum);
        }

        this.umodPrimaryNameList =
                Collections.unmodifiableList(this.primaryNameList);
        this.umodGlobalNameList =
                Collections.unmodifiableList(this.globalNameList);

        return;
    }

    /**
     * コンストラクタ。
     *
     * <p>プライマリ名、グローバル名共、
     * 状況に応じて伸長する連結リストが用意される。
     */
    protected I18nAlias(){
        this(0, 0);
        return;
    }


    /**
     * XMLドキュメントをロードする。
     * @param is 入力
     * @return 最上位要素
     * @throws ParserConfigurationException XMLの構成が変
     * @throws SAXException XMLの内容が変
     * @throws IOException 入力エラー
     */
    protected static Element loadXml(InputStream is)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory;
        factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(is);

        Element top = doc.getDocumentElement();

        return top;
    }


    /**
     * オーダー番号を返す。
     * @return オーダー番号
     */
    protected int getOrderNo(){
        return this.orderNo;
    }

    /**
     * オーダー番号を設定する。
     * @param orderNo オーダー番号
     */
    protected void setOrderNo(int orderNo){
        this.orderNo = orderNo;
        return;
    }

    /**
     * プライマリ名の代表をひとつ返す。
     *
     * <p>必ず存在しなければならない。
     *
     * @return 最初のプライマリ名
     */
    public String getTopPrimaryName(){
        String result = this.primaryNameList.get(0);
        return result;
    }

    /**
     * グローバル名の代表をひとつ返す。
     * @return 最初のグローバル名。ひとつもなければnull
     */
    public String getTopGlobalName(){
        if(this.globalNameList.isEmpty()) return null;

        String result = this.globalNameList.get(0);

        return result;
    }

    /**
     * プライマリ名の全別名リストを返す。
     * @return 全別名リスト。(不可変)
     */
    public List<String> getPrimaryNameList(){
        return this.umodPrimaryNameList;
    }

    /**
     * プライマリ名を追加。
     * @param primaryName プライマリ名
     */
    protected void addPrimaryName(String primaryName){
        this.primaryNameList.add(primaryName);
        return;
    }

    /**
     * グローバル名の全別名リストを返す。
     * @return 全別名リスト。(不可変)
     */
    public List<String> getGlobalNameList(){
        return this.umodGlobalNameList;
    }

    /**
     * グローバル名を追加。
     * @param globalName グローバル名
     */
    protected void addGlobalName(String globalName){
        this.globalNameList.add(globalName);
        return;
    }

    /**
     * オーダ番号によるコンパレータ。
     */
    @SuppressWarnings("serial")
    private static class OrderComparator
            implements Comparator<I18nAlias> {

        /**
         * コンストラクタ。
         */
        OrderComparator(){
            super();
            return;
        }

        /**
         * オーダ番号を順序づける。
         * @param o1 {@inheritDoc}
         * @param o2 {@inheritDoc}
         * @return {@inheritDoc}
         */
        @Override
        public int compare(I18nAlias o1, I18nAlias o2){
            int result = o1.getOrderNo() - o2.getOrderNo();
            return result;
        }

    }

}
