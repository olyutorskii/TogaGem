/*
 * typical bone information
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sfjp.mikutoga.typical;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 一般的な標準ボーン構成に関する情報。
 *
 * <p>各ボーン情報はひとつ以上のプライマリ名(≒日本語名)と
 * ゼロ個以上のグローバル名(≒英語名)を持つ。
 *
 * <p>選択基準は独断。
 *
 * <p>和英対訳はMMD Ver7.39の同梱モデルにほぼ準拠。
 */
public final class TypicalBone extends I18nAlias {

    private static final Class<?> THISCLASS = TypicalBone.class;
    private static final String BONE_XML = "resources/typicalBone.xml";

    private static final String ELEM_BONE    = "bone";
    private static final String ELEM_ROOT    = "root";
    private static final String ELEM_PRIMARY = "primary";
    private static final String ELEM_GLOBAL  = "global";
    private static final String ATTR_NAME    = "name";

    private static final List<TypicalBone> BONE_LIST =
            new LinkedList<>();
    private static final AliasMap<TypicalBone> BONE_ALIAS_MAP =
            new AliasMap<>();

    private static final List<TypicalBone> BONE_UNMODLIST =
            Collections.unmodifiableList(BONE_LIST);

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


    private boolean isRoot;


    /**
     * コンストラクタ。
     *
     * <p>各初期数が0以下の場合は、
     * 状況に応じて伸長する連結リストが用意される。
     *
     * @param primaryNum プライマリ名初期数。
     * @param globalNum グローバル名初期数。
     */
    private TypicalBone(int primaryNum, int globalNum){
        super(primaryNum, globalNum);
        assert this.getClass() == THISCLASS;
        return;
    }


    /**
     * XML文書の最上位構造を解読する。
     * @param top 最上位要素
     */
    private static void parse(Element top) {
        NodeList boneList = top.getElementsByTagName(ELEM_BONE);
        int boneNo = boneList.getLength();
        for(int idx = 0; idx < boneNo; idx++){
            Element boneElem = (Element) boneList.item(idx);
            TypicalBone typBone = parseBone(boneElem);
            BONE_LIST.add(typBone);
            BONE_ALIAS_MAP.addAlias(typBone);
        }

        return;
    }

    /**
     * bone要素を解読する。
     * @param boneElem bone要素
     * @return ボーン情報
     */
    private static TypicalBone parseBone(Element boneElem){
        NodeList primaryNodes = boneElem.getElementsByTagName(ELEM_PRIMARY);
        NodeList globalNodes  = boneElem.getElementsByTagName(ELEM_GLOBAL);
        int primaryNo = primaryNodes.getLength();
        int globalNo  = globalNodes.getLength();

        assert primaryNo > 0;

        TypicalBone bone = new TypicalBone(primaryNo, globalNo);

        for(int idx = 0; idx < primaryNo; idx++){
            Element primaryElem = (Element) primaryNodes.item(idx);
            String name = primaryElem.getAttribute(ATTR_NAME);
            bone.addPrimaryName(name);
        }

        for(int idx = 0; idx < globalNo; idx++){
            Element globalElem = (Element) globalNodes.item(idx);
            String name = globalElem.getAttribute(ATTR_NAME);
            bone.addGlobalName(name);
        }

        NodeList rootNodes = boneElem.getElementsByTagName(ELEM_ROOT);
        if(rootNodes.getLength() > 0){
            bone.isRoot = true;
        }else{
            bone.isRoot = false;
        }

        return bone;
    }

    /**
     * 全ボーン情報に通し番号を付ける。
     *
     * <p>XMLでの定義順が反映される。
     */
    private static void numbering(){
        int order = 0;
        for(TypicalBone bone : BONE_LIST){
            bone.setOrderNo(order++);
        }

        return;
    }

    /**
     * 全ボーンの不変リストを返す。
     * @return 全ボーンのリスト
     */
    public static List<TypicalBone> getTypicalBoneList(){
        return BONE_UNMODLIST;
    }

    /**
     * プライマリ名の合致するボーン情報を返す。
     * NFKCで正規化されたプライマリ名で検索される。
     * @param primaryName プライマリ名
     * @return モーフ情報。見つからなければnull
     */
    public static TypicalBone findWithPrimary(String primaryName){
        TypicalBone result = BONE_ALIAS_MAP.getAliasByPrimary(primaryName);
        return result;
    }

    /**
     * グローバル名の合致するボーン情報を返す。
     * NFKCで正規化されたグローバル名で検索される。
     * @param globalName グローバル名
     * @return モーフ情報。見つからなければnull
     */
    public static TypicalBone findWithGlobal(String globalName){
        TypicalBone result = BONE_ALIAS_MAP.getAliasByGlobal(globalName);
        return result;
    }

    /**
     * プライマリ名をグローバル名に変換する。
     * @param primaryName プライマリ名
     * @return グローバル名。見つからなければnull
     */
    public static String primary2global(String primaryName){
        String globalName = BONE_ALIAS_MAP.primary2global(primaryName);
        return globalName;
    }

    /**
     * グローバル名をプライマリ名へ変換する。
     * @param globalName グローバル名
     * @return プライマリ名。見つからなければnull
     */
    public static String global2primary(String globalName){
        String primaryName = BONE_ALIAS_MAP.global2primary(globalName);
        return primaryName;
    }


    /**
     * このボーンが親を持たないルートボーンとして扱われる慣習なのか
     * 判定する。
     *
     * <p>※「全親」ボーンに関する慣習は無視される。
     *
     * @return 親を持たなければtrue
     */
    public boolean isRoot(){
        return this.isRoot;
    }

}
