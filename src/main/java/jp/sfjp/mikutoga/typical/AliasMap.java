/*
 * alias map with primary & global
 *
 * License : The MIT License
 * Copyright(c) 2013 MikuToga Partners
 */

package jp.sfjp.mikutoga.typical;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

/**
 * プライマリ名 - グローバル名間の対応、
 * およびUnicode正規化によるゆらぎ表記吸収処理を行う。
 *
 * @param <T> 別名管理クラス
 * @see <a href="https://ja.wikipedia.org/wiki/Unicode%E6%AD%A3%E8%A6%8F%E5%8C%96">
 * Unicode正規化 </a>
 * @see <a href="http://unicode.org/reports/tr15/">
 * UNICODE NORMALIZATION FORMS </a>
 * @see java.text.Normalizer
 */
class AliasMap<T extends I18nAlias> {

    private final Map<String, T> primaryAliasMap;
    private final Map<String, T> globalAliasMap;

    /**
     * コンストラクタ。
     */
    AliasMap(){
        super();

        this.primaryAliasMap = new HashMap<>();
        this.globalAliasMap  = new HashMap<>();

        return;
    }


    /**
     * NFKC正規化されたUnicode文字列を返す。
     *
     * <p>等価な全半角、濁点、丸付き数字などの表現の正規化を目的とする。
     * <ul>
     * <li>「ﾎﾞｰﾝ」は「ボーン」になる
     * <li>「ホ゛ーン９」は「ボーン9」になる
     * </ul>
     *
     * @param name 正規化対象文字列
     * @return 正規化済み文字列
     */
    static String normalize(CharSequence name){
        String result;
        result = Normalizer.normalize(name, Normalizer.Form.NFKC);
        return result;
    }


    /**
     * 別名管理オブジェクトを登録。
     *
     * <p>キーとなる名前は、事前にNFKC正規化で
     * 揺らぎ表記が吸収されたプライマリ名およびグローバル名。
     *
     * <p>登録キーが衝突した時は後の方が有効となる。
     *
     * @param alias 別名管理オブジェクト
     */
    void addAlias(T alias){
        addPrimary(alias);
        addGlobal(alias);
        return;
    }

    /**
     * 別名管理オブジェクトと正規化プライマリ名を対応づける。
     *
     * <p>事前にNFKC正規化されたプライマリ名が登録キーとなる。
     *
     * <p>登録キーが衝突した時は後の方が有効となる。
     *
     * @param alias 別名管理オブジェクト
     */
    private void addPrimary(T alias){
        for(String primaryName : alias.getPrimaryNameList()){
            String normalized = normalize(primaryName);
            normalized = normalized.intern();
            this.primaryAliasMap.put(normalized, alias);
        }
        return;
    }

    /**
     * 別名管理オブジェクトと正規化グローバル名を対応づける。
     *
     * <p>事前にNFKC正規化されたグローバル名が登録キーとなる。
     *
     * <p>登録キーが衝突した時は後の方が有効となる。
     *
     * @param alias 別名管理オブジェクト
     */
    private void addGlobal(T alias){
        for(String globalName : alias.getGlobalNameList()){
            String normalized = normalize(globalName);
            normalized = normalized.intern();
            this.globalAliasMap.put(normalized, alias);
        }
        return;
    }

    /**
     * 名前から別名管理オブジェクトを得る。
     *
     * <p>プライマリ名、グローバル名の順で検索される。
     *
     * <p>名前は事前にNFKC正規化された後、検索キーとなる。
     *
     * @param name 名前
     * @return 別名管理オブジェクト。見つからなければnull
     */
    T getAlias(String name){
        T result;
        result = getAliasByPrimary(name);
        if(result == null){
            result = getAliasByGlobal(name);
        }
        return result;
    }

    /**
     * プライマリ名から別名管理オブジェクトを得る。
     *
     * <p>プライマリ名は事前にNFKC正規化された後、検索キーとなる。
     *
     * @param primaryName プライマリ名
     * @return 別名管理オブジェクト。見つからなければnull
     */
    T getAliasByPrimary(String primaryName){
        String normalized = normalize(primaryName);
        T result = this.primaryAliasMap.get(normalized);
        return result;
    }

    /**
     * グローバル名から別名管理オブジェクトを得る。
     *
     * <p>グローバル名は事前にNFKC正規化された後、検索キーとなる。
     *
     * @param globalName グローバル名
     * @return 別名管理オブジェクト。見つからなければnull
     */
    T getAliasByGlobal(String globalName){
        String normalized = normalize(globalName);
        T result = this.globalAliasMap.get(normalized);
        return result;
    }

    /**
     * プライマリ名から代表グローバル名を得る。
     *
     * <p>プライマリ名は事前にNFKC正規化された後、検索キーとなる。
     *
     * @param primaryName プライマリ名
     * @return 代表グローバル名。見つからなければnull
     */
    String primary2global(String primaryName){
        T alias = getAliasByPrimary(primaryName);
        if(alias == null) return null;
        String globalName = alias.getTopGlobalName();
        return globalName;
    }

    /**
     * グローバル名から代表プライマリ名を得る。
     *
     * <p>グローバル名は事前にNFKC正規化された後、検索キーとなる。
     *
     * @param globalName グローバル名
     * @return 代表プライマリ名。見つからなければnull
     */
    String global2primary(String globalName){
        T alias = getAliasByGlobal(globalName);
        if(alias == null) return null;
        String primary = alias.getTopPrimaryName();
        return primary;
    }

}
