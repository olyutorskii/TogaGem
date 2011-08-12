/*
 * XML DOM utilities with namespace
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.xml;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * DOMユーティリティ。(名前空間対応)
 *
 */
public class DomNsUtils {

    /**
     * ノードの名前空間とローカル名を判定する。
     * @param node ノード
     * @param nsuri 名前空間URI
     * @param localName ローカル名
     * @return ノードの名前空間とローカル名が一致したらtrue
     */
    public static boolean hasNsLocalName(Node node,
                                         String nsuri,
                                         String localName ){
        String nodeLocalName = node.getLocalName();
        String nodeNsUri     = node.getNamespaceURI();

        if( ! localName.equals(nodeLocalName) ) return false;
        if( ! nsuri.equals(nodeNsUri) )         return false;

        return true;
    }

    /**
     * 指定された名前の子要素を1つだけ返す。
     * @param parent 親要素
     * @param nsuri 名前空間URI
     * @param localName 子要素名
     * @return 子要素
     * @throws TogaXmlException 1つも見つからなかった
     */
    public static Element getChild(Element parent,
                                   String nsuri,
                                   String localName )
            throws TogaXmlException{
        Element result = null;

        for(Node node = parent.getFirstChild();
            node != null;
            node = node.getNextSibling() ){

            if(node.getNodeType() != Node.ELEMENT_NODE) continue;
            Element elem = (Element) node;

            if(hasNsLocalName(elem, nsuri, localName)){
                result = elem;
                break;
            }
        }

        if(result == null){
            String message =
                    "Elem:[" + localName + "] was not found in "
                    +"Elem:[" + parent.getTagName() + "]";
            throw new TogaXmlException(message);
        }

        return result;
    }

    /**
     * 親要素が指定された名前の子要素を持つか判定する。
     * @param parent 親要素
     * @param nsuri 名前空間URI
     * @param localName 子要素名
     * @return 指定名の子要素が存在すればtrue
     */
    public static boolean hasChild(Element parent,
                                   String nsuri,
                                   String localName ){
        for(Node node = parent.getFirstChild();
            node != null;
            node = node.getNextSibling() ){

            if(node.getNodeType() != Node.ELEMENT_NODE) continue;
            Element elem = (Element) node;

            if(hasNsLocalName(elem, nsuri, localName)){
                return true;
            }
        }

        return false;
    }

    /**
     * 指定された名前の子要素のリストを返す。
     * @param parent 親要素
     * @param nsuri 名前空間URI
     * @param localName 子要素名
     * @return 子要素のリスト
     */
    public static List<Element> getChildList(Element parent,
                                             String nsuri,
                                             String localName ){
        List<Element> result = new LinkedList<Element>();

        for(Node node = parent.getFirstChild();
            node != null;
            node = node.getNextSibling() ){

            if(node.getNodeType() != Node.ELEMENT_NODE) continue;
            Element elem = (Element) node;

            if(hasNsLocalName(elem, nsuri, localName)){
                result.add(elem);
            }
        }

        return result;
    }

    /**
     * 指定された名前の子要素の列挙子を返す。
     * @param parent 親要素
     * @param nsuri 名前空間URI
     * @param localName 子要素名
     * @return 子要素の列挙子
     */
    public static Iterator<Element> getChildIterator(Element parent,
                                                     String nsuri,
                                                     String localName ){
        Element firstElem;
        try{
            firstElem = getChild(parent, nsuri, localName);
        }catch(TogaXmlException e){
            firstElem = null;
        }

        Iterator<Element> result = new ElemIterator(firstElem);

        return result;
    }

    /**
     * 指定された名前の子要素のforeachを返す。
     * @param parent 親要素
     * @param nsuri 名前空間URI
     * @param localName 子要素名
     * @return 子要素のforeach
     */
    public static Iterable<Element> getEachChild(Element parent,
                                                 String nsuri,
                                                 String localName ){
        final Iterator<Element> iterator =
                getChildIterator(parent, nsuri, localName);
        Iterable<Element> result = new Iterable<Element>(){
            @Override
            public Iterator<Element> iterator(){
                return iterator;
            }
        };
        return result;
    }

    /**
     * 同じ名前空間とローカル名を持つ次の要素を返す。
     * @param elem 要素
     * @return 次の要素。なければnull
     */
    public static Element nextNamedElement(Element elem){
        String nsuri = elem.getNamespaceURI();
        String localName = elem.getLocalName();
        Element nextElem = elem;
        for(;;){
            nextElem = DomUtils.nextElement(nextElem);
            if(nextElem == null) break;
            if(hasNsLocalName(nextElem, nsuri, localName)) break;
        }

        return nextElem;
    }


    /**
     * 同じ親要素と同じ要素名を持つ兄弟要素を列挙する列挙子。
     */
    private static class ElemIterator implements Iterator<Element>{
        private Element next;

        /**
         * コンストラクタ。
         * @param elem 最初の要素。nullを指定すれば空列挙子となる。
         */
        private ElemIterator(Element elem){
            super();
            this.next = elem;
        }

        /**
         * {@inheritDoc}
         * @return {@inheritDoc}
         */
        @Override
        public boolean hasNext(){
            if(this.next == null) return false;
            return true;
        }

        /**
         * {@inheritDoc}
         * @return {@inheritDoc}
         * @throws NoSuchElementException {@inheritDoc}
         */
        @Override
        public Element next() throws NoSuchElementException{
            if(this.next == null) throw new NoSuchElementException();
            Element result = this.next;
            this.next = nextNamedElement(this.next);
            return result;
        }

        /**
         * {@inheritDoc}
         * ※ 未サポート。
         */
        @Override
        public void remove(){
            throw new UnsupportedOperationException();
        }

    }

}
