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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
 *
 * <p>RELAX NG is not supported.
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
        URL localXsdUrl = THISCLASS.getResource(LOCAL_SCHEMA_XML);
        URI localXsdUri;
        try{
            localXsdUri = localXsdUrl.toURI();
        }catch(URISyntaxException e){
            throw new ExceptionInInitializerError(e);
        }

        URI_XSD_ORIG  = URI.create(SCHEMA_XML);
        URI_XSD_LOCAL = localXsdUri;

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
     *
     * @return schema factory
     */
    public static SchemaFactory newSchemaFactory(){
        SchemaFactory schemaFactory;
        schemaFactory = SchemaFactory.newInstance(
                XMLConstants.W3C_XML_SCHEMA_NS_URI);

        try{
            // Prevent denial-of-service attack.
            schemaFactory.setFeature(
                    XMLConstants.FEATURE_SECURE_PROCESSING, true);
        }catch(SAXNotRecognizedException | SAXNotSupportedException e){
            // THIS FEATURE MUST BE SUPPORTED
            assert false;
        }

        try{
            // Disallow external entity reference & external DTD access.
            schemaFactory.setProperty(
                    XMLConstants.ACCESS_EXTERNAL_DTD, "");
            // Allow only HTTP external schema file.
            schemaFactory.setProperty(
                    XMLConstants.ACCESS_EXTERNAL_SCHEMA, ALLOWED_USCHEMA);
        }catch(SAXNotRecognizedException | SAXNotSupportedException e){
            // THIS PROPERTY MUST BE SUPPORTED JAXP1.5 or later
            assert false;
        }

        LSResourceResolver resolver = buildXmlXsdResolver();
        schemaFactory.setResourceResolver(resolver);

        schemaFactory.setErrorHandler(BotherHandler.HANDLER);

        return schemaFactory;
    }

    /**
     * pre-load &amp; pre-compile local schema files.
     *
     * @param localSchemaUris local schema resources.
     * @return schema
     * @throws SAXException invalid schema definition
     * @throws IOException local resource i/o error
     */
    public static Schema newSchema(URI... localSchemaUris)
            throws SAXException, IOException{
        SchemaFactory schemaFactory = newSchemaFactory();

        int uris = localSchemaUris.length;
        if(uris <= 0){
            // on-demand xml-schema with schemaLocation attribute.
            return schemaFactory.newSchema();
        }

        InputStream[] iss = new InputStream[uris];
        for(int idx = 0; idx < uris; idx++){
            URI localUri = localSchemaUris[idx];
            URL localUrl = localUri.toURL();
            InputStream is;
            is = localUrl.openStream();
            is = new BufferedInputStream(is);
            iss[idx] = is;
        }

        Source[] sources = new Source[uris];
        for(int idx = 0; idx < uris; idx++){
            InputStream is = iss[idx];
            sources[idx] = new StreamSource(is);
        }

        Schema result;
        try{
            result = schemaFactory.newSchema(sources);
        }finally{
            for(InputStream is : iss){
                is.close();
            }
        }

        return result;
    }

}
