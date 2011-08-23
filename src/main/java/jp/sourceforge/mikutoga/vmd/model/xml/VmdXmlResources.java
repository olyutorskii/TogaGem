/*
 * xml resources for VMD-XML
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.model.xml;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import jp.sourceforge.mikutoga.xml.XmlResourceResolver;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/**
 * モーションデータ用各種XMLリソースの定義。
 */
public final class VmdXmlResources {

    /** 定義の版数。 */
    public static final String VER_VMDXML =
            "110820";
    /** XML名前空間識別子。 */
    public static final String NS_VMDXML =
            "http://mikutoga.sourceforge.jp/xml/ns/vmdxml/110820";
    /** XMLスキーマURI名。 */
    public static final String SCHEMAURI_VMDXML =
            "http://mikutoga.sourceforge.jp/xml/xsd/vmdxml-110820.xsd";
    /** ローカルなスキーマファイル名。 */
    public static final String LOCAL_SCHEMA_VMDXML =
            "resources/vmdxml-110820.xsd";

    /** XMLスキーマURI。 */
    public static final URI URI_SCHEMA_VMDXML;
    /** XMLスキーマのローカルリソース。 */
    public static final URI RES_SCHEMA_VMDXML;


    private static final Class<?> THISCLASS = VmdXmlResources.class;


    static{
        try{
            URI_SCHEMA_VMDXML = URI.create(SCHEMAURI_VMDXML);
            RES_SCHEMA_VMDXML =
                    THISCLASS.getResource(LOCAL_SCHEMA_VMDXML).toURI();
        }catch(URISyntaxException e){
            throw new ExceptionInInitializerError(e);
        }

        new VmdXmlResources().hashCode();
    }


    /**
     * 隠しコンストラクタ。
     */
    private VmdXmlResources(){
        super();
        assert this.getClass().equals(THISCLASS);
        return;
    }


    /**
     * ビルダの生成。
     * @param handler エラーハンドラ
     * @return ビルダ
     */
    public static DocumentBuilder newBuilder(ErrorHandler handler){
        XmlResourceResolver resolver = createResolver();

        Schema schema = createSchema(resolver);

        DocumentBuilderFactory builderFactory = createBuilderFactory();
        builderFactory.setSchema(schema);

        DocumentBuilder builder;
        try{
            builder = builderFactory.newDocumentBuilder();
        }catch(ParserConfigurationException e){
            assert false;
            throw new AssertionError(e);
        }
        builder.setEntityResolver(resolver);
        builder.setErrorHandler(handler);

        return builder;
    }

    /**
     * URI参照をローカルなリソースアクセスへとリダイレクトするリゾルバを生成する。
     * @return リゾルバ
     */
    private static XmlResourceResolver createResolver(){
        XmlResourceResolver resolver = new XmlResourceResolver();
        resolver.putURIMap(URI_SCHEMA_VMDXML, RES_SCHEMA_VMDXML);
//        resolver.putURIMap(URI_DTD_VMDXML, RES_DTD_VMDXML);

        return resolver;
    }

    /**
     * 検証用スキーマ(XML schema)を生成する。
     * @param resolver リゾルバ
     * @return スキーマ
     */
    private static Schema createSchema(LSResourceResolver resolver){
        SchemaFactory schemaFactory =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schemaFactory.setResourceResolver(resolver);

        URL localXsd;
        try{
            localXsd = RES_SCHEMA_VMDXML.toURL();
        }catch(MalformedURLException e){
            assert false;
            throw new AssertionError(e);
        }

        Schema schema;
        try{
            schema = schemaFactory.newSchema(localXsd);
        }catch(SAXException e){
            assert false;
            throw new AssertionError(e);
        }

        return schema;
    }

    /**
     * DocumentBuilderFavtoryを生成する。
     * @return ファクトリ
     */
    private static DocumentBuilderFactory createBuilderFactory(){
        DocumentBuilderFactory builderFactory =
                DocumentBuilderFactory.newInstance();

        builderFactory.setNamespaceAware(true);
        builderFactory.setValidating(false);    // DTD validation off
        builderFactory.setIgnoringComments(true);
        try{
            builderFactory.setXIncludeAware(true);
        }catch(UnsupportedOperationException e){
            // NOTHING
            assert true;
        }

        return builderFactory;
    }

}
