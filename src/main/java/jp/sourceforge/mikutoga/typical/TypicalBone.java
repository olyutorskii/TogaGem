/*
 * typical bone information
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.typical;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 一般的な標準ボーン構成に関する情報。
 * <p>各ボーン情報はひとつ以上のプライマリ名(≒日本語名)と
 * ゼロ個以上のグローバル名(≒英語名)を持つ。
 * <p>選択基準は独断。
 * <p>和英対訳はMMD Ver7.39の同梱モデルにほぼ準拠。
 */
public final class TypicalBone extends I18nAlias {

    private static final Class<?> THISCLASS = TypicalBone.class;
    private static final String BONE_XML = "resources/typicalBone.xml";

    private static final List<TypicalBone> TYP_BONE_LIST =
            new LinkedList<TypicalBone>();
    private static final Map<String, TypicalBone> PRIMARY_MAP =
            new HashMap<String, TypicalBone>();
    private static final Map<String, TypicalBone> GLOBAL_MAP =
            new HashMap<String, TypicalBone>();

    private static final List<TypicalBone> TYP_BONE_UNMODLIST =
            Collections.unmodifiableList(TYP_BONE_LIST);

    static{
        InputStream is = THISCLASS.getResourceAsStream(BONE_XML);
        Element top;
        try{
            top = I18nAlias.loadXml(is);
        }catch(ParserConfigurationException e){
            throw new ExceptionInInitializerError(e);
        }catch(SAXException e){
            throw new ExceptionInInitializerError(e);
        }catch(IOException e){
            throw new ExceptionInInitializerError(e);
        }

        parse(top);

        numbering();
    }


    /**
     * コンストラクタ。
     * <p>各初期数が0以下の場合は、
     * 状況に応じて伸長する連結リストが用意される。
     * @param primaryNo プライマリ名初期数。
     * @param globalNo グローバル名初期数。
     */
    private TypicalBone(int primaryNo, int globalNo){
        super(primaryNo, globalNo);
        return;
    }


    /**
     * XML文書の最上位構造を解読する。
     * @param top 最上位要素
     */
    private static void parse(Element top) {
        NodeList boneList = top.getElementsByTagName("bone");
        int boneNo = boneList.getLength();
        for(int idx = 0; idx < boneNo; idx++){
            Element bone = (Element) boneList.item(idx);
            TypicalBone typBone = parseBone(bone);
            TYP_BONE_LIST.add(typBone);
        }

        return;
    }

    /**
     * bone要素を解読する。
     * @param bone bone要素
     * @return ボーン情報
     */
    private static TypicalBone parseBone(Element bone){
        NodeList primaryNodes = bone.getElementsByTagName("primary");
        NodeList globalNodes  = bone.getElementsByTagName("global");
        int primaryNo = primaryNodes.getLength();
        int globalNo  = globalNodes.getLength();

        TypicalBone typBone = new TypicalBone(primaryNo, globalNo);

        for(int idx = 0; idx < primaryNo; idx++){
            Element primary = (Element) primaryNodes.item(idx);
            String name = primary.getAttribute("name");
            typBone.addPrimaryName(name);
        }

        for(int idx = 0; idx < globalNo; idx++){
            Element global = (Element) globalNodes.item(idx);
            String name = global.getAttribute("name");
            typBone.addGlobalName(name);
        }

        for(String primaryName : typBone.getPrimaryList()){
            String key = normalize(primaryName).intern();
            PRIMARY_MAP.put(key, typBone);
        }

        for(String globalName : typBone.getGlobalList()){
            String key = normalize(globalName).intern();
            GLOBAL_MAP.put(key, typBone);
        }

        return typBone;
    }

    /**
     * 全ボーン情報を一意に順序付ける設定を行う。
     * <p>XMLでの定義順が反映される。
     */
    private static void numbering(){
        int order = 0;
        for(TypicalBone bone : TYP_BONE_LIST){
            bone.setOrderNo(order++);
        }

        return;
    }

    /**
     * 全ボーンの不変リストを返す。
     * @return 全ボーンのリスト
     */
    public static List<TypicalBone> getBoneList(){
        return TYP_BONE_UNMODLIST;
    }

    /**
     * プライマリ名の合致するボーン情報を返す。
     * NFKCで正規化されたプライマリ名で検索される。
     * @param primaryName プライマリ名
     * @return モーフ情報。見つからなければnull
     */
    public static TypicalBone findWithPrimary(String primaryName){
        String key = normalize(primaryName);
        TypicalBone result = PRIMARY_MAP.get(key);
        return result;
    }

    /**
     * グローバル名の合致するボーン情報を返す。
     * NFKCで正規化されたグローバル名で検索される。
     * @param globalName グローバル名
     * @return モーフ情報。見つからなければnull
     */
    public static TypicalBone findWithGlobal(String globalName){
        String key = normalize(globalName);
        TypicalBone result = GLOBAL_MAP.get(key);
        return result;
    }

    /**
     * プライマリ名をグローバル名に変換する。
     * @param primaryName プライマリ名
     * @return グローバル名。見つからなければnull
     */
    public static String primary2global(String primaryName){
        TypicalBone bone = findWithPrimary(primaryName);
        if(bone == null) return null;
        String global = bone.getTopGlobalName();
        return global;
    }

    /**
     * グローバル名をプライマリ名へ変換する。
     * @param globalName グローバル名
     * @return プライマリ名。見つからなければnull
     */
    public static String global2primary(String globalName){
        TypicalBone bone = findWithGlobal(globalName);
        if(bone == null) return null;
        String primary = bone.getTopPrimaryName();
        return primary;
    }

}
