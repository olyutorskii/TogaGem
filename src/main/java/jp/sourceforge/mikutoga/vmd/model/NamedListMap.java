/*
 * string named list map
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 名前付けされたリストのマップ。
 * 登録名の追加順が保持される。
 * @param <E> リスト要素の型
 */
public class NamedListMap<E> {

    private final List<String> nameList;
    private final Map<String, List<E>> listMap;


    /**
     * コンストラクタ。
     */
    public NamedListMap(){
        super();
        this.nameList = new LinkedList<String>();
        this.listMap = new HashMap<String, List<E>>();
        return;
    }


    /**
     * マップをクリアする。
     */
    public void clear(){
        this.nameList.clear();
        this.listMap.clear();
        return;
    }

    /**
     * マップが空か否か判定する。
     * @return 空ならtrue
     */
    public boolean isEmpty(){
        if(this.listMap.isEmpty()) return true;
        return false;
    }

    /**
     * 名前一覧を返す。
     * <p>名前は登録順に並ぶ。
     * <p>ここで返されるListへの修正操作は不可能。
     * @return 名前一覧のリスト
     */
    public List<String> getNames(){
        List<String> result = Collections.unmodifiableList(this.nameList);
        return result;
    }

    /**
     * 名前付けされたリストを返す。
     * @param name 名前
     * @return 名前付けされたリスト。リストが存在しなければnull。
     */
    public List<E> getNamedList(String name){
        List<E> result = this.listMap.get(name);
        return result;
    }

    /**
     * 名前付けされたリストを削除する。
     * 存在しない名前が渡された場合、何もしない。
     * @param name 名前
     */
    public void removeNamedList(String name){
        if(this.listMap.remove(name) == null) return;
        this.nameList.remove(name);
        return;
    }

    /**
     * 名前付けされたリストに新要素を追加する。
     * 未登録の名前であれば新たにリストが作成される。
     * @param name 名前
     * @param elem 新要素
     * @throws NullPointerException 引数がnull
     */
    public void addNamedElement(String name, E elem)
            throws NullPointerException{
        if(name == null || elem == null) throw new NullPointerException();

        List<E> list = this.listMap.get(name);
        if(list == null){
            list = new LinkedList<E>();
            this.listMap.put(name, list);
            this.nameList.add(name);
        }

        list.add(elem);

        return;
    }

}
