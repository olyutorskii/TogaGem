/*
 * internationalization name alias
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.typical;

import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
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
 * 国際化&別名管理オブジェクトの実装基板。
 * <p>国産モデルではプライマリ名に日本語名が収められることが多い。
 * プライマリ名は必ず一つ以上なければならない。
 * <p>国産モデルではグローバル名に英語名が収められることが多いが、
 * プライマリ名と同一の日本語名が収められている場合も多い。
 */
class I18nAlias {

    /** オーダ番号によるコンパレータ。 */
    public static final Comparator<I18nAlias> ORDER_COMPARATOR =
            new OrderComparator();


    private int orderNo;

    private final List<String> primaryList;
    private final List<String> globalList;

    private final List<String> umodPrimaryList;
    private final List<String> umodGlobalList;


    /**
     * コンストラクタ。
     * <p>各初期数が0以下の場合は、
     * 状況に応じて伸長する連結リストが用意される。
     * @param primaryNo プライマリ名初期数。
     * @param globalNo グローバル名初期数。
     */
    protected I18nAlias(int primaryNo, int globalNo){
        super();

        if(primaryNo <= 0){
            this.primaryList = new LinkedList<String>();
        }else{
            this.primaryList = new ArrayList<String>(primaryNo);
        }

        if(globalNo <= 0){
            this.globalList  = new LinkedList<String>();
        }else{
            this.globalList  = new ArrayList<String>(globalNo);
        }

        this.umodPrimaryList =
                Collections.unmodifiableList(this.primaryList);
        this.umodGlobalList =
                Collections.unmodifiableList(this.globalList);

        return;
    }

    /**
     * コンストラクタ。
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
        factory.setNamespaceAware(true);

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(is);

        Element top = doc.getDocumentElement();

        return top;
    }

    /**
     * NFKC正規化された文字列を返す。
     * <ul>
     * <li>「ﾎﾞｰﾝ」は「ボーン」になる
     * <li>「ホ゛ーン９」は「ボーン9」になる
     * </ul>
     * @param name
     * @return
     */
    protected static String normalize(CharSequence name){
        String result;
        result = Normalizer.normalize(name, Normalizer.Form.NFKC);
        return result;
    }


    /**
     * オーダー番号を返す。
     * @return
     */
    protected int getOrderNo(){
        return this.orderNo;
    }

    /**
     * オーダー番号を設定する。
     * @param orderNo
     */
    protected void setOrderNo(int orderNo){
        this.orderNo = orderNo;
        return;
    }

    /**
     * プライマリ名の代表をひとつ返す。
     * @return 最初のプライマリ名
     */
    public String getTopPrimaryName(){
        String result = this.primaryList.get(0);
        return result;
    }

    /**
     * グローバル名の代表をひとつ返す。
     * @return 最初のグローバル名。ひとつもなければnull
     */
    public String getTopGlobalName(){
        String result;
        if(this.globalList.isEmpty()) result = null;
        else                          result = this.globalList.get(0);
        return result;
    }

    /**
     * プライマリ名の全エイリアス文字列リストを返す。
     * @return 全プライマリ名リスト。(不可変)
     */
    public List<String> getPrimaryList(){
        return this.umodPrimaryList;
    }

    /**
     * プライマリ名を追加。
     * @param name プライマリ名
     */
    protected void addPrimaryName(String name){
        this.primaryList.add(name);
        return;
    }

    /**
     * グローバル名の全エイリアス文字列リストを返す。
     * @return 全グローバル名リスト。(不可変)
     */
    public List<String> getGlobalList(){
        return this.umodGlobalList;
    }

    /**
     * グローバル名を追加。
     * @param name グローバル名
     */
    protected void addGlobalName(String name){
        this.globalList.add(name);
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
