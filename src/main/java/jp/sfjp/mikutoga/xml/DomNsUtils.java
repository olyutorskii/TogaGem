/*
 * XML DOM utilities with namespace
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sfjp.mikutoga.xml;

import java.text.MessageFormat;
import java.util.Iterator;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * DOMユーティリティ(名前空間対応)。
 * <p>各種名前空間引数にnullが渡された場合、全ての名前空間にマッチする。
 * <p>各種ローカル名引数にnullが渡された場合、全てのローカル名にマッチする。
 * <p>ノードの持つ名前空間がnullの場合、全ての名前空間引数にマッチする。
 */
public final class DomNsUtils {

    private static final String ERRMSG_NOELEM =
            "Elem:[{0}] was not found in Elem:[{1}]";
    private static final String ERRMSG_NOATTR =
            "Attr:[{0}] was not found in Elem:[{1}]";
    private static final String ERRMSG_INVATTR =
            "Invalid attribute form Attr[{0}] Value[{1}]";


    /**
     * 隠しコンストラクタ。
     */
    private DomNsUtils(){
        assert false;
        throw new AssertionError();
    }


    /**
     * 名前空間とローカル名が一致するノードか判定する。
     * @param node ノード
     * @param nsuri 名前空間URI
     * @param localName ローカル名。
     * @return ノードの名前空間およびローカル名が一致したらtrue
     */
    public static boolean hasNsLocalNameNode(Node node,
                                                String nsuri,
                                                String localName ){
        String nodeLocalName = node.getLocalName();
        String nodeNsUri     = node.getNamespaceURI();

        if(localName != null){
            if( ! localName.equals(nodeLocalName) ) return false;
        }

        if(nsuri != null && nodeNsUri != null){
            if( ! nsuri.equals(nodeNsUri) ) return false;
        }

        return true;
    }

    /**
     * 名前空間とローカル名が一致する要素か判定する。
     * @param node ノード
     * @param nsuri 名前空間URI
     * @param localName ローカル名。
     * @return 名前空間およびローカル名が一致する要素であればtrue
     */
    public static boolean hasNsLocalNameElem(Node node,
                                             String nsuri,
                                             String localName ){
        if(node.getNodeType() != Node.ELEMENT_NODE) return false;
        if( ! hasNsLocalNameNode(node, nsuri, localName) ) return false;
        return true;
    }

    /**
     * 親要素が指定された名前の子要素を持つか判定する。
     * @param parent 親要素
     * @param nsuri 名前空間URI
     * @param localName ローカル名
     * @return 指定名の子要素が存在すればtrue
     */
    public static boolean hasChild(Element parent,
                                    String nsuri,
                                    String localName ){
        for(Node node = parent.getFirstChild();
            node != null;
            node = node.getNextSibling() ){

            if(hasNsLocalNameElem(node, nsuri, localName)){
                return true;
            }
        }

        return false;
    }

    /**
     * 指定された名前空間とローカル名に合致する最初の直下子要素を返す。
     * @param parent 親要素
     * @param nsuri 名前空間URI
     * @param localName ローカル名
     * @return 最初の直下子要素。見つからなければnull。
     */
    public static Element pickFirstChild(Node parent,
                                         String nsuri,
                                         String localName ){
        Node node = parent.getFirstChild();
        while(node != null){
            if(hasNsLocalNameElem(node, nsuri, localName)){
                break;
            }
            node = node.getNextSibling();
        }
        return (Element) node;
    }

    /**
     * 指定された名前空間とローカル名に合致する最初の直下子要素を返す。
     * <p>見つからなければ例外を投げる。
     * @param parent 親要素
     * @param nsuri 名前空間URI
     * @param localName ローカル名
     * @return 最初の直下子要素。
     * @throws TogaXmlException 1つも見つからなかった
     */
    public static Element getFirstChild(Element parent,
                                        String nsuri,
                                        String localName )
            throws TogaXmlException{
        Element elem = pickFirstChild(parent, nsuri, localName);

        if(elem == null){
            String message = MessageFormat.format(ERRMSG_NOELEM,
                                                  localName,
                                                  parent.getLocalName() );
            throw new TogaXmlException(message);
        }

        return elem;
    }

    /**
     * 指定された名前の子要素のforeachを返す。
     * @param parent 親要素
     * @param nsuri 名前空間URI
     * @param localName 子要素名
     * @return 子要素のforeach
     */
    public static Iterable<Element> getEachChild(final Element parent,
                                                 final String nsuri,
                                                 final String localName ){
        Iterable<Element> result = new Iterable<Element>(){
            @Override
            public Iterator<Element> iterator(){
                return new SiblingElemIterator(parent, nsuri, localName);
            }
        };
        return result;
    }

    /**
     * 要素に属性が存在するか判定する。
     * @param elem 要素
     * @param nsuri 名前空間URI
     * @param localName ローカル名
     * @return 存在するならtrue
     */
    public static boolean hasAttrNS(Element elem,
                                    String nsuri,
                                    String localName ){
        return elem.hasAttributeNS(nsuri, localName);
    }

    /**
     * 要素からxsd:string型属性値を読み取る。
     * @param elem 要素
     * @param nsuri 名前空間URI
     * @param localName 属性名
     * @return 文字列
     * @throws TogaXmlException 属性値が見つからなかった。
     */
    public static String getStringAttrNS(Element elem,
                                         String nsuri,
                                         String localName )
            throws TogaXmlException{
        if( ! hasAttrNS(elem, nsuri, localName) ){
            String message = MessageFormat.format(ERRMSG_NOATTR,
                                                  localName,
                                                  elem.getLocalName() );
            throw new TogaXmlException(message);
        }

        String result;
        try{
            result = elem.getAttributeNS(nsuri, localName);
        }catch(DOMException e){
            assert false;
            throw new AssertionError(e);
        }

        return result;
    }

    /**
     * 要素からxsd:boolean型属性値を読み取る。
     * @param elem 要素
     * @param nsuri 名前空間URI
     * @param localName 属性名
     * @return 真ならtrue
     * @throws TogaXmlException 属性値が見つからなかった。
     */
    public static boolean getBooleanAttrNS(Element elem,
                                           String nsuri,
                                           String localName )
            throws TogaXmlException{
        String value = getStringAttrNS(elem, nsuri, localName);

        boolean result;
        try{
            result = DatatypeIo.parseBoolean(value);
        }catch(IllegalArgumentException e){
            String message = MessageFormat.format(ERRMSG_INVATTR,
                                                  localName,
                                                  value );
            throw new TogaXmlException(message, e);
        }

        return result;
    }

    /**
     * 要素からxsd:integer型属性値を読み取る。
     * @param elem 要素
     * @param nsuri 名前空間URI
     * @param localName 属性名
     * @return int値
     * @throws TogaXmlException 属性値が見つからなかった。
     */
    public static int getIntegerAttrNS(Element elem,
                                       String nsuri,
                                       String localName )
            throws TogaXmlException{
        String value = getStringAttrNS(elem, nsuri, localName);

        int result;
        try{
            result = DatatypeIo.parseInt(value);
        }catch(NumberFormatException e){
            String message = MessageFormat.format(ERRMSG_INVATTR,
                                                  localName,
                                                  value );
            throw new TogaXmlException(message, e);
        }

        return result;
    }

    /**
     * 要素からxsd:float型属性値を読み取る。
     * @param elem 要素
     * @param nsuri 名前空間URI
     * @param localName 属性名
     * @return float値
     * @throws TogaXmlException 属性値が見つからなかった。
     */
    public static float getFloatAttrNS(Element elem,
                                       String nsuri,
                                       String localName )
            throws TogaXmlException{
        String value = getStringAttrNS(elem, nsuri, localName);

        float result;
        try{
            result = DatatypeIo.parseFloat(value);
        }catch(NumberFormatException e){
            String message = MessageFormat.format(ERRMSG_INVATTR,
                                                  localName,
                                                  value );
            throw new TogaXmlException(message, e);
        }

        return result;
    }

}
