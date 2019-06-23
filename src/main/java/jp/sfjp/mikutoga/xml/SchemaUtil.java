/*
 * xml schema utility
 *
 * License : The MIT License
 * Copyright(c) 2013 MikuToga Partners
 */

package jp.sfjp.mikutoga.xml;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * XML schema (XSD) utilities.
 */
public final class SchemaUtil {


    /** XML Schema. */
    public static final String SCHEMA_XML =
            "http://www.w3.org/2001/xml.xsd";

    /** XSD namespace. */
    public static final String NS_XSD =
            "http://www.w3.org/2001/XMLSchema-instance";

    private static final String LOCAL_SCHEMA_XML =
            "resources/xmlspace.xsd";

    private static final URI URI_XSD_ORIG;
    private static final URI URI_XSD_LOCAL;

    private static final String ALLOWED_USCHEMA = "http";

    private static final Class<?> THISCLASS = SchemaUtil.class;


    static{
        URL redirectRes = THISCLASS.getResource(LOCAL_SCHEMA_XML);
        String redirectResName = redirectRes.toString();

        URI_XSD_ORIG  = URI.create(SCHEMA_XML);
        URI_XSD_LOCAL = URI.create(redirectResName);

        assert ALLOWED_USCHEMA.equalsIgnoreCase(URI_XSD_ORIG.getScheme());
    }


    /**
     * Hidden constructor.
     */
    private SchemaUtil(){
        assert false;
        throw new AssertionError();
    }


    /**
     * build xml.xsd redirection info.
     *
     * @return resolver
     */
    public static XmlResourceResolver buildXmlXsdResolver(){
        XmlResourceResolver result = new XmlResourceResolver();
        result.putRedirected(URI_XSD_ORIG, URI_XSD_LOCAL);
        return result;
    }

    /**
     * Build SchemaFactory for XML Schema but safety.
     *
     * <p>Includes some considerations for XXE vulnerabilities.
     *
     * <p>Restrict access to
     * External Entity Reference &amp; external DTDs
     * in xml schema file.
     *
     * <p>Restrict access to External schema file access in xml schema file,
     * but HTTP access is allowed.
     * This special limit considers access to
     * importing http://www.w3.org/2001/xml.xsd
     * in top of common xml schema file.
     * If HTTP access controll is needed, customize resolver yourself.
     *
     * @param resolver Custom resolver for reading xml schema.
     *     Resolve reference to nothing if null.
     * @return schema factory
     */
    public static SchemaFactory newSchemaFactory(
            LSResourceResolver resolver ){
        SchemaFactory schemaFactory;
        schemaFactory = SchemaFactory.newInstance(
                XMLConstants.W3C_XML_SCHEMA_NS_URI);

        try{
            // Prevent denial of service attack.
            schemaFactory.setFeature(
                    XMLConstants.FEATURE_SECURE_PROCESSING, true);
        }catch(SAXNotRecognizedException | SAXNotSupportedException e){
            // FEATURE MUST BE SUPPORTED
            assert false;
        }

        try{
            // Disallow external entity reference &amp; external DTD access.
            schemaFactory.setProperty(
                    XMLConstants.ACCESS_EXTERNAL_DTD, "");
            // Allow only HTTP external schema file.
            schemaFactory.setProperty(
                    XMLConstants.ACCESS_EXTERNAL_SCHEMA, ALLOWED_USCHEMA);
        }catch(SAXNotRecognizedException | SAXNotSupportedException e){
            // PROPERTY MUST BE SUPPORTED JAXP1.5 or later
            assert false;
        }

        schemaFactory.setResourceResolver(resolver);

        schemaFactory.setErrorHandler(BotherHandler.HANDLER);

        return schemaFactory;
    }

    /**
     * ローカルリソースをSourceに変換する。
     * @param resource ローカルリソース
     * @return XML Source
     * @throws MalformedURLException 不正なURI
     * @throws IOException オープンエラー
     */
    private static Source toLocalSource(LocalXmlResource resource)
            throws MalformedURLException, IOException{
        URI localUri = resource.getLocalResource();
        URL localUrl = localUri.toURL();

        InputStream is = localUrl.openStream();
        is = new BufferedInputStream(is);

        Source result = new StreamSource(is);
        return result;
    }

    /**
     * ローカルリソース群をSource群に変換する。
     * @param resArray ローカルリソースURI並び
     * @return XML Source並び
     * @throws MalformedURLException 不正なURI
     * @throws IOException オープンエラー
     */
    private static Source[] toLocalSourceArray(LocalXmlResource... resArray)
            throws MalformedURLException, IOException{
        List<Source> sourceList = new ArrayList<>(resArray.length);

        for(LocalXmlResource resource : resArray){
            Source localSource = toLocalSource(resource);
            sourceList.add(localSource);
        }

        Source[] result = new Source[sourceList.size()];
        result = sourceList.toArray(result);
        return result;
    }

    /**
     * ローカルスキーマをロードする。
     *
     * <p>任意のリゾルバを指定可能
     *
     * @param resolver リゾルバ
     * @param resArray ローカルスキーマ情報並び
     * @return スキーマ
     */
    public static Schema newSchema(
            XmlResourceResolver resolver,
            LocalXmlResource... resArray){
        XmlResourceResolver totalResolver = buildXmlXsdResolver();
        totalResolver.putRedirected(resolver);

        for(LocalXmlResource resource : resArray){
            totalResolver.putRedirected(resource);
        }

        Source[] sources;
        try{
            sources = toLocalSourceArray(resArray);
        }catch(IOException e){                   // ビルド障害
            assert false;
            throw new AssertionError(e);
        }

        SchemaFactory schemaFactory = newSchemaFactory(totalResolver);

        Schema result;
        try{
            if(sources.length <= 0){
                // ドキュメント埋め込みスキーマURLにリゾルバ経由でアクセス
                result = schemaFactory.newSchema();
            }else{
                result = schemaFactory.newSchema(sources);
            }
        }catch(SAXException e){   // Build error
            assert false;
            throw new AssertionError(e);
        }

        // TODO: Sourceを閉めるのは誰の責務？

        return result;
    }

}
