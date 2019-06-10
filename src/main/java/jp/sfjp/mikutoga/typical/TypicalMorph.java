/*
 * typical morph information
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sfjp.mikutoga.typical;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import jp.sfjp.mikutoga.pmd.MorphType;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * 一般的な標準モーフに関する情報。
 *
 * <p>各モーフ情報はひとつ以上のプライマリ名(≒日本語名)と
 * ゼロ個以上のグローバル名(≒英語名)を持つ。
 *
 * <p>選択基準は独断。
 *
 * <p>和英対訳はMMD Ver7.39の同梱モデルにほぼ準拠。
 */
public final class TypicalMorph extends I18nAlias {

    private static final Class<?> THISCLASS = TypicalMorph.class;
    private static final String MORPH_XML = "resources/typicalMorph.xml";

    private static final String ELEM_MORPHGROUP = "morphGroup";
    private static final String ELEM_MORPH      = "morph";
    private static final String ATTR_TYPE       = "type";
    private static final String ELEM_PRIMARY    = "primary";
    private static final String ELEM_GLOBAL     = "global";
    private static final String ATTR_NAME       = "name";

    private static final List<TypicalMorph> EMPTY = Collections.emptyList();

    private static final Map<MorphType, List<TypicalMorph>> TYPED_MAP =
            new EnumMap<MorphType, List<TypicalMorph>>(MorphType.class);

    private static final AliasMap<TypicalMorph> MORPH_ALIAS_MAP =
            new AliasMap<TypicalMorph>();


    static{
        InputStream is = THISCLASS.getResourceAsStream(MORPH_XML);

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


    private final MorphType type;


    /**
     * コンストラクタ。
     *
     * <p>各初期数が0以下の場合は、
     * 状況に応じて伸長する連結リストが用意される。
     *
     * @param type モーフ種別
     * @param primaryNum プライマリ名初期数。
     * @param globalNum グローバル名初期数。
     */
    private TypicalMorph(MorphType type, int primaryNum, int globalNum){
        super(primaryNum, globalNum);

        this.type = type;

        assert this.getClass() == THISCLASS;

        return;
    }


    /**
     * XML文書の最上位構造を解読する。
     * @param top 最上位要素
     */
    private static void parse(Element top) {
        NodeList groupList = top.getElementsByTagName(ELEM_MORPHGROUP);
        int groupNo = groupList.getLength();
        for(int idx = 0; idx < groupNo; idx++){
            Element groupElem = (Element) groupList.item(idx);
            parseGroup(groupElem);
        }

        // 必要に応じモーフ枠に不変空リスト登録
        for(MorphType morphType : MorphType.values()){
            if( ! TYPED_MAP.containsKey(morphType) ){
                TYPED_MAP.put(morphType, EMPTY);
            }
        }

        return;
    }

    /**
     * モーフグループ構造を解読する。
     * @param groupElem morphGroup要素
     */
    private static void parseGroup(Element groupElem){
        String typeAttr = groupElem.getAttribute(ATTR_TYPE);
        MorphType morphType = MorphType.valueOf(typeAttr);

        NodeList morphList = groupElem.getElementsByTagName(ELEM_MORPH);
        int morphNo = morphList.getLength();
        List<TypicalMorph> groupedList =
                new ArrayList<TypicalMorph>(morphNo);

        for(int idx = 0; idx < morphNo; idx++){
            Element morphElem = (Element) morphList.item(idx);
            TypicalMorph morph = parseMorph(morphElem, morphType);
            groupedList.add(morph);
            MORPH_ALIAS_MAP.addAlias(morph);
        }

        groupedList = Collections.unmodifiableList(groupedList);
        TYPED_MAP.put(morphType, groupedList);

        return;
    }

    /**
     * morph要素を解読する。
     * @param morphElem morph要素
     * @param mtype モーフ種別
     * @return モーフ情報
     */
    private static TypicalMorph parseMorph(Element morphElem,
                                             MorphType mtype ){
        NodeList primaryNodes = morphElem.getElementsByTagName(ELEM_PRIMARY);
        NodeList globalNodes  = morphElem.getElementsByTagName(ELEM_GLOBAL);
        int primaryNo = primaryNodes.getLength();
        int globalNo  = globalNodes.getLength();

        assert primaryNo > 0;

        TypicalMorph morph = new TypicalMorph(mtype, primaryNo, globalNo);

        for(int idx = 0; idx < primaryNo; idx++){
            Element primaryElem = (Element) primaryNodes.item(idx);
            String primaryName = primaryElem.getAttribute(ATTR_NAME);
            morph.addPrimaryName(primaryName);
        }

        for(int idx = 0; idx < globalNo; idx++){
            Element globalElem = (Element) globalNodes.item(idx);
            String globalName = globalElem.getAttribute(ATTR_NAME);
            morph.addGlobalName(globalName);
        }

        return morph;
    }

    /**
     * 全モーフ情報に通し番号を付ける。
     *
     * <p>同一グループ内ではXMLでの定義順が反映される。
     */
    private static void numbering(){
        int order = 0;
        for(MorphType morphType : MorphType.values()){
            for(TypicalMorph morph : TYPED_MAP.get(morphType)){
                morph.setOrderNo(order++);
            }
        }

        return;
    }

    /**
     * 種別ごとのモーフ情報不変リストを取得する。
     * @param morphType モーフ種別
     * @return モーフ情報不変リスト
     */
    public static List<TypicalMorph> getTypicalMorphList(
            MorphType morphType ){
        List<TypicalMorph> result = TYPED_MAP.get(morphType);
        return result;
    }

    /**
     * プライマリ名の合致するモーフ情報を返す。
     * NFKCで正規化されたプライマリ名で検索される。
     * @param primaryName プライマリ名
     * @return モーフ情報。見つからなければnull
     */
    public static TypicalMorph findWithPrimary(String primaryName){
        TypicalMorph result = MORPH_ALIAS_MAP.getAliasByPrimary(primaryName);
        return result;
    }

    /**
     * グローバル名の合致するモーフ情報を返す。
     * NFKCで正規化されたグローバル名で検索される。
     * @param globalName グローバル名
     * @return モーフ情報。見つからなければnull
     */
    public static TypicalMorph findWithGlobal(String globalName){
        TypicalMorph result = MORPH_ALIAS_MAP.getAliasByGlobal(globalName);
        return result;
    }

    /**
     * プライマリ名をグローバル名に変換する。
     * @param primaryName プライマリ名
     * @return グローバル名。見つからなければnull
     */
    public static String primary2global(String primaryName){
        String globalName = MORPH_ALIAS_MAP.primary2global(primaryName);
        return globalName;
    }

    /**
     * グローバル名をプライマリ名へ変換する。
     * @param globalName グローバル名
     * @return プライマリ名。見つからなければnull
     */
    public static String global2primary(String globalName){
        String primaryName = MORPH_ALIAS_MAP.global2primary(globalName);
        return primaryName;
    }


    /**
     * モーフ種別を返す。
     * @return モーフ種別
     */
    public MorphType getMorphType(){
        return this.type;
    }

}
