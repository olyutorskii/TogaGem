/*
 * xml resources for PMD-XML
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sourceforge.mikutoga.pmd.xml;

import java.net.URI;
import java.net.URISyntaxException;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import jp.sourceforge.mikutoga.xml.XmlResourceResolver;
import org.xml.sax.SAXException;

/**
 *
 */
public final class PmdXmlResources {

    public static final String NS_PMDXML =
            "http://mikutoga.sourceforge.jp/xml/ns/pmdxml/100923";
    public static final String SCHEMA_PMDXML =
            "http://mikutoga.sourceforge.jp/xml/xsd/pmdxml-100923.xsd";
    public static final String DTD_PMDXML =
            "http://mikutoga.sourceforge.jp/xml/dtd/pmdxml-100923.dtd";
    public static final String VER_PMDXML =
            "100923";
    public static final String LOCAL_SCHEMA_PMDXML =
            "resources/pmdxml-100923.xsd";
    public static final String LOCAL_DTD_PMDXML =
            "resources/pmdxml-100923.dtd";

    public static final URI URI_SCHEMA_PMDXML = URI.create(SCHEMA_PMDXML);
    public static final URI URI_DTD_PMDXML = URI.create(DTD_PMDXML);
    public static final URI RES_SCHEMA_PMDXML;
    public static final URI RES_DTD_PMDXML;

    private static final Class THISCLASS = PmdXmlResources.class;

    static{
        Object dummy = new PmdXmlResources();

        try{
            RES_SCHEMA_PMDXML =
                    THISCLASS.getResource(LOCAL_SCHEMA_PMDXML).toURI();
            RES_DTD_PMDXML =
                    THISCLASS.getResource(LOCAL_DTD_PMDXML).toURI();
        }catch(URISyntaxException e){
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * 隠しコンストラクタ。
     */
    private PmdXmlResources(){
        super();
        assert this.getClass().equals(THISCLASS);
        return;
    }

    public static DocumentBuilder newBuilder()
            throws SAXException, ParserConfigurationException {
        XmlResourceResolver resolver = new XmlResourceResolver();
        resolver.putURIMap(URI_SCHEMA_PMDXML, RES_SCHEMA_PMDXML);
        resolver.putURIMap(URI_DTD_PMDXML, RES_DTD_PMDXML);

        SchemaFactory schemaFactory =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schemaFactory.setResourceResolver(resolver);
        Schema schema = schemaFactory.newSchema();

        DocumentBuilderFactory builderFactory =
                DocumentBuilderFactory.newInstance();
        builderFactory.setCoalescing(true);
        builderFactory.setExpandEntityReferences(true);
        builderFactory.setIgnoringComments(true);
        builderFactory.setIgnoringElementContentWhitespace(false);
        builderFactory.setNamespaceAware(true);
        builderFactory.setValidating(false);
        builderFactory.setSchema(schema);

        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        builder.setEntityResolver(resolver);

        return builder;
    }

}
