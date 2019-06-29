/*
 * xml resource resolver
 *
 * License : The MIT License
 * Copyright(c) 2009 olyutorskii
 */

package jp.sfjp.mikutoga.xml;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 * register &amp; redirect original URI to local resource contents.
 */
public class XmlResourceResolver
        implements LSResourceResolver{

    private static final URI EMPTY_URI = URI.create("");

    private static final DOMImplementationLS DOM_LS;


    private final Map<URI, URI> uriMap;


    static{
        try{
            DOM_LS = buildDomImplLS();
        }catch(   ClassNotFoundException
                | IllegalAccessException
                | InstantiationException e){
            throw new ExceptionInInitializerError(e);
        }
    }


    /**
     * Constructor.
     */
    public XmlResourceResolver(){
        super();

        Map<URI, URI> map;
        map = new HashMap<>();
        map = Collections.synchronizedMap(map);
        this.uriMap = map;

        return;
    }


    /**
     * return DOMImplementationLS implement.
     *
     * @return DOMImplementationLS implement
     * @throws ClassNotFoundException no class
     * @throws InstantiationException no object
     * @throws IllegalAccessException no grant
     */
    private static DOMImplementationLS buildDomImplLS() throws
            ClassNotFoundException,
            InstantiationException,
            IllegalAccessException {
        DOMImplementationRegistry domReg;
        DOMImplementation         domImp;
        DOMImplementationLS       domImpLs;

        domReg = DOMImplementationRegistry.newInstance();
        domImp = domReg.getDOMImplementation("LS 3.0");

        Object feature = domImp.getFeature("LS", "3.0");
        assert feature instanceof DOMImplementationLS;
        domImpLs = (DOMImplementationLS) feature;

        return domImpLs;
    }

    /**
     * return LSInput implement.
     *
     * @return LSInput implement
     */
    public static LSInput createLSInput(){
        LSInput input = DOM_LS.createLSInput();
        return input;
    }

    /**
     * merge base-uri text &amp; relative URI text.
     *
     * @param base base URI text must be absolute or null.
     * @param relative relative URI text.
     *     If absolute, base is ignored.
     *     Ignored if null.
     * @return merged absolute URI.
     * @throws java.net.URISyntaxException illegal URI
     * @throws java.lang.IllegalArgumentException result is not Absolute.
     */
    protected static URI buildBaseRelativeURI(String base, String relative)
            throws URISyntaxException,
                   IllegalArgumentException {
        URI baseURI;
        if(base != null){
            baseURI = new URI(base);
            if( ! baseURI.isAbsolute() ){
                throw new IllegalArgumentException();
            }
        }else{
            baseURI = null;
        }

        URI relativeURI;
        if(relative != null){
            relativeURI = new URI(relative);
        }else{
            relativeURI = EMPTY_URI;
        }

        URI result = buildBaseRelativeURI(baseURI, relativeURI);
        return result;
    }

    /**
     * merge base-uri &amp; relative URI.
     *
     * @param baseURI base URI must be absolute or null.
     * @param relativeURI relative URI. If absolute, baseURI is ignored.
     * @return merged absolute URI.
     * @throws java.lang.IllegalArgumentException result is not Absolute.
     */
    private static URI buildBaseRelativeURI(URI baseURI, URI relativeURI)
            throws IllegalArgumentException {
        URI resultURI;

        if(baseURI == null || relativeURI.isAbsolute()){
            resultURI = relativeURI;
        }else{
            resultURI = baseURI.resolve(relativeURI);
        }

        if( ! resultURI.isAbsolute() ){
            throw new IllegalArgumentException();
        }

        resultURI = resultURI.normalize();

        return resultURI;
    }


    /**
     * map original URI &amp; local resource URI.
     *
     * @param original original URI
     * @param redirect local resource URI
     */
    public void putRedirected(URI original, URI redirect){
        URI oridinalNorm = original.normalize();
        URI redirectNorm = redirect.normalize();

        this.uriMap.put(oridinalNorm, redirectNorm);

        return;
    }

    /**
     * add other resolver mapping.
     *
     * @param other resolver
     */
    public void putRedirected(XmlResourceResolver other){
        this.uriMap.putAll(other.uriMap);
        return;
    }

    /**
     * get redirected local resource URI.
     *
     * @param original original URI
     * @return mapped local resource URI. null if unmapped.
     */
    public URI getRedirected(URI original){
        URI keyURI = original.normalize();
        URI resourceURI = this.uriMap.get(keyURI);
        return resourceURI;
    }

    /**
     * get redirected local input stream.
     *
     * @param originalURI original URI
     * @return mapped local resource input stream.
     *     If no mapping, return zero-length data stream.
     * @throws java.io.IOException local resource i/o error
     */
    private InputStream getXMLResourceAsStream(URI originalURI)
            throws IOException{
        InputStream result;

        URI resourceURI = getRedirected(originalURI);
        if(resourceURI == null){
            result = new ByteArrayInputStream(new byte[0]);
        }else{
            URL resourceURL = resourceURI.toURL();
            result = resourceURL.openStream();
        }

        result = new BufferedInputStream(result);

        return result;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Return redirected local resource data.
     *
     * @param type {@inheritDoc}
     * @param namespaceURI {@inheritDoc}
     * @param publicId {@inheritDoc}
     * @param systemId {@inheritDoc}
     * @param baseURI {@inheritDoc}
     * @return {@inheritDoc} LSInput of local resource.
     *     If no mapping, return zero-length data.
     */
    @Override
    public LSInput resolveResource(
            String type,
            String namespaceURI,
            String publicId,
            String systemId,
            String baseURI ){
        if(systemId == null) return null;

        URI originalURI;
        try{
            originalURI = buildBaseRelativeURI(baseURI, systemId);
        }catch(URISyntaxException e){
            assert false;
            throw new AssertionError(e);
        }

        InputStream is;
        try{
            is = getXMLResourceAsStream(originalURI);
        }catch(IOException e){
            assert false;
            throw new AssertionError(e);
        }

        LSInput input = createLSInput();
        input.setBaseURI(baseURI);
        input.setPublicId(publicId);
        input.setSystemId(systemId);
        input.setByteStream(is);

        return input;
    }

}
