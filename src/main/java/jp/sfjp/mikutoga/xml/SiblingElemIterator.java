/*
 * sibling element iterator on DOM tree
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sfjp.mikutoga.xml;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 兄弟要素間用Iterator。
 *
 * <p>同じ親と名前空間とローカル名を持つ要素同士を「兄弟要素」とする。
 *
 * <p>ノードの持つ名前空間がnullの場合、全ての名前空間引数にマッチする。
 *
 * <p>削除操作は未サポート。
 */
public class SiblingElemIterator implements Iterator<Element> {

    private Element next;
    private final String nsuri;
    private final String localName;


    /**
     * コンストラクタ。
     * @param first 最初の兄弟要素。nullだと一度もiterateしない。
     */
    public SiblingElemIterator(Element first){
        super();

        this.next = first;

        if(this.next == null){
            this.nsuri     = null;
            this.localName = null;
        }else{
            this.nsuri     = this.next.getNamespaceURI();
            this.localName = this.next.getLocalName();
        }

        return;
    }

    /**
     * コンストラクタ。
     *
     * <p>名前空間引数にnullが渡された場合、全ての名前空間にマッチする。
     *
     * <p>ローカル名引数にnullが渡された場合、全てのローカル名にマッチする。
     *
     * @param parent 親要素
     * @param nsuri 子要素の名前空間URI
     * @param localName 子要素のローカル名
     */
    public SiblingElemIterator(Element parent,
                               String nsuri,
                               String localName ){
        super();

        this.next = DomNsUtils.pickFirstChild(parent, nsuri, localName);

        if(this.next == null){
            this.nsuri     = null;
            this.localName = null;
        }else{
            this.nsuri     = nsuri;
            this.localName = localName;
        }

        return;
    }


    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean hasNext(){
        if(this.next != null) return true;
        return false;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @throws NoSuchElementException {@inheritDoc}
     */
    @Override
    public Element next() throws NoSuchElementException {
        if(this.next == null) throw new NoSuchElementException();

        Element result = this.next;

        Node sibNode = result;
        do{
            sibNode = sibNode.getNextSibling();
            if(sibNode == null) break;
        }while( ! matchElemName(sibNode) );
        this.next = (Element) sibNode;

        return result;
    }

    /**
     * 兄弟要素にふさわしい名前を持つか判定する。
     * @param node 判定対象
     * @return 兄弟にふさわしい名前を持つならtrue
     */
    private boolean matchElemName(Node node){
        return DomNsUtils.hasNsLocalNameElem(node,
                                             this.nsuri, this.localName );
    }

    /**
     * {@inheritDoc}
     * ※削除不可。
     * @throws UnsupportedOperationException 削除を試みたので失敗した
     */
    @Override
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

}
