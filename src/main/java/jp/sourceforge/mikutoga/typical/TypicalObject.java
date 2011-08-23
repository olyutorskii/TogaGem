/*
 * typical object information
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.typical;

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
 * 各種標準オブジェクトの実装基板。
 * <p>国産モデルではプライマリ名に日本語名が収められることが多い。
 * プライマリ名は必ず一つ以上なければならない。
 * <p>国産モデルではグローバル名に英語名が収められることが多いが、
 * プライマリ名と同一の日本語名が収められている場合も多い。
 */
class TypicalObject {

    /** オーダ番号によるコンパレータ。 */
    public static final Comparator<TypicalObject> ORDER_COMPARATOR =
            new OrderComparator();

    protected final List<String> primaryList;
    protected final List<String> globalList;

    protected final List<String> umodPrimaryList;
    protected final List<String> umodGlobalList;

    protected int orderNo;


    /**
     * コンストラクタ。
     * <p>各初期数が0以下の場合は、状況に応じて伸長する連結リストが用意される。
     * @param primaryNo プライマリ名初期数。
     * @param globalNo グローバル名初期数。
     */
    protected TypicalObject(int primaryNo, int globalNo){
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
     * <p>プライマリ名、グローバル名共、状況に応じて伸長する連結リストが用意される。
     */
    protected TypicalObject(){
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
     * プライマリ名をひとつ返す。
     * @return 最初のプライマリ名
     */
    public String getTopPrimaryName(){
        String result = this.primaryList.get(0);
        return result;
    }

    /**
     * グローバル名をひとつ返す。
     * @return 最初のグローバル名。ひとつもなければnull
     */
    public String getTopGlobalName(){
        String result;
        if(this.globalList.isEmpty()) result = null;
        else                          result = this.globalList.get(0);
        return result;
    }

    /**
     * 全プライマリ名を返す。
     * @return 全プライマリ名リスト。(不可変)
     */
    public List<String> getPrimaryList(){
        return this.umodPrimaryList;
    }

    /**
     * 全グローバル名を返す。
     * @return 全グローバル名リスト。(不可変)
     */
    public List<String> getGlobalList(){
        return this.umodGlobalList;
    }

    /**
     * オーダ番号によるコンパレータ。
     */
    private static class OrderComparator
            implements Comparator<TypicalObject> {

        /**
         * コンストラクタ。
         */
        private OrderComparator(){
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
        public int compare(TypicalObject o1, TypicalObject o2){
            int result = o1.orderNo - o2.orderNo;
            return result;
        }

    }

}
