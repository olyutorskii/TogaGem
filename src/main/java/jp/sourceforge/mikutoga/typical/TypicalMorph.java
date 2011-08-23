/*
 * typical morph information
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.typical;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import jp.sourceforge.mikutoga.pmd.MorphType;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * 一般的な標準モーフに関する情報。
 * <p>各モーフ情報はひとつ以上のプライマリ名(≒日本語名)と
 * ゼロ個以上のグローバル名(≒英語名)を持つ。
 * <p>選択基準は独断。
 * <p>和英対訳はMMD Ver7.39の同梱モデルにほぼ準拠。
 */
public final class TypicalMorph extends TypicalObject {

    private static final Class<?> THISCLASS = TypicalMorph.class;
    private static final String MORPH_XML = "resources/typicalMorph.xml";

    private static final List<TypicalMorph> EMPTY = Collections.emptyList();

    private static final Map<MorphType, List<TypicalMorph>> TYPED_MAP =
            new EnumMap<MorphType, List<TypicalMorph>>(MorphType.class);

    private static final Map<String, TypicalMorph> PRIMARY_MAP =
            new HashMap<String, TypicalMorph>();
    private static final Map<String, TypicalMorph> GLOBAL_MAP =
            new HashMap<String, TypicalMorph>();


    static{
        InputStream is = THISCLASS.getResourceAsStream(MORPH_XML);
        Element top;
        try{
            top = TypicalObject.loadXml(is);
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


    private final MorphType type;


    /**
     * コンストラクタ。
     * <p>各初期数が0以下の場合は、状況に応じて伸長する連結リストが用意される。
     * @param type モーフ種別
     * @param primaryNo プライマリ名初期数。
     * @param globalNo グローバル名初期数。
     */
    private TypicalMorph(MorphType type, int primaryNo, int globalNo){
        super(primaryNo, globalNo);
        this.type = type;
        return;
    }


    /**
     * XML文書の最上位構造を解読する。
     * @param top 最上位要素
     */
    private static void parse(Element top) {
        NodeList groupList = top.getElementsByTagName("morphGroup");
        int groupNo = groupList.getLength();
        for(int idx = 0; idx < groupNo; idx++){
            Element group = (Element) groupList.item(idx);
            parseGroup(group);
        }

        // 空リスト登録
        for(MorphType morphType : MorphType.values()){
            if( ! TYPED_MAP.containsKey(morphType) ){
                TYPED_MAP.put(morphType, EMPTY);
            }
        }

        return;
    }

    /**
     * モーフグループ構造を解読する。
     * @param group morphGroup要素
     */
    private static void parseGroup(Element group){
        String typeAttr = group.getAttribute("type");
        MorphType morphType = MorphType.valueOf(typeAttr);

        NodeList morphList = group.getElementsByTagName("morph");
        int morphNo = morphList.getLength();
        List<TypicalMorph> groupedList = new ArrayList<TypicalMorph>(morphNo);

        for(int idx = 0; idx < morphNo; idx++){
            Element morph = (Element) morphList.item(idx);
            TypicalMorph common = parseMorph(morph, morphType);
            groupedList.add(common);
        }

        TYPED_MAP.put(morphType, Collections.unmodifiableList(groupedList));

        return;
    }

    /**
     * morph要素を解読する。
     * @param morph morph要素
     * @param mtype モーフ種別
     * @return モーフ情報
     */
    private static TypicalMorph parseMorph(Element morph, MorphType mtype){
        NodeList primaryNodes = morph.getElementsByTagName("primary");
        NodeList globalNodes  = morph.getElementsByTagName("global");
        int primaryNo = primaryNodes.getLength();
        int globalNo  = globalNodes.getLength();

        TypicalMorph typMorph = new TypicalMorph(mtype, primaryNo, globalNo);

        for(int idx = 0; idx < primaryNo; idx++){
            Element primary = (Element) primaryNodes.item(idx);
            String name = primary.getAttribute("name");
            typMorph.primaryList.add(name);
        }

        for(int idx = 0; idx < globalNo; idx++){
            Element global = (Element) globalNodes.item(idx);
            String name = global.getAttribute("name");
            typMorph.globalList.add(name);
        }

        for(String primaryName : typMorph.primaryList){
            PRIMARY_MAP.put(primaryName, typMorph);
        }

        for(String globalName : typMorph.globalList){
            GLOBAL_MAP.put(globalName, typMorph);
        }

        return typMorph;
    }

    /**
     * 全モーフ情報を一意に順序付ける設定を行う。
     * <p>同一グループ内ではXMLでの定義順が反映される。
     */
    private static void numbering(){
        int order = 0;
        for(MorphType morphType : MorphType.values()){
            for(TypicalMorph common : TYPED_MAP.get(morphType)){
                common.orderNo = order++;
            }
        }

        return;
    }


    /**
     * 種別ごとのモーフ情報リストを取得する。
     * @param morphType モーフ種別
     * @return モーフ情報リスト
     */
    public static List<TypicalMorph> getTypedMorphList(MorphType morphType){
        List<TypicalMorph> result = TYPED_MAP.get(morphType);
        return result;
    }

    /**
     * プライマリ名の合致するモーフ情報を返す。
     * @param primaryName プライマリ名
     * @return モーフ情報。見つからなければnull
     */
    public static TypicalMorph findWithPrimary(String primaryName){
        TypicalMorph result = PRIMARY_MAP.get(primaryName);
        return result;
    }

    /**
     * グローバル名の合致するモーフ情報を返す。
     * @param globalName グローバル名
     * @return モーフ情報。見つからなければnull
     */
    public static TypicalMorph findWithGlobal(String globalName){
        TypicalMorph result = GLOBAL_MAP.get(globalName);
        return result;
    }

    /**
     * プライマリ名をグローバル名に変換する。
     * @param primaryName プライマリ名
     * @return グローバル名。見つからなければnull
     */
    public static String primary2global(String primaryName){
        TypicalMorph morph = findWithPrimary(primaryName);
        if(morph == null) return null;
        String global = morph.getTopGlobalName();
        return global;
    }

    /**
     * グローバル名をプライマリ名へ変換する。
     * @param globalName グローバル名
     * @return プライマリ名。見つからなければnull
     */
    public static String global2primary(String globalName){
        TypicalMorph morph = findWithGlobal(globalName);
        if(morph == null) return null;
        String primary = morph.getTopPrimaryName();
        return primary;
    }

    /**
     * モーフ種別を返す。
     * @return モーフ種別
     */
    public MorphType getMorphType(){
        return this.type;
    }

}
