/*
 * xml resource resolver
 *
 * License : The MIT License
 * Copyright(c) 2009 olyutorskii
 */

package jp.sfjp.mikutoga.xml;

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
 * URL変換マップに従い、XML文書からの外部参照をリダイレクトする。
 * 相対URIはこのクラスをベースに解決される。
 * 主な用途は外部スキーマのリソース化など。
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
     * コンストラクタ。
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
     * 絶対URIと相対URIを合成したURIを返す。
     * 正規化も行われる。
     *
     * @param base 絶対URIでなければならない。nullでもよい。
     * @param relative 絶対URIでもよいがその場合baseは無視される。null可。
     * @return 合成結果のURLオブジェクト。必ず絶対URIになる。
     * @throws java.net.URISyntaxException URIとして変。
     * @throws java.lang.IllegalArgumentException 絶対URIが生成できない。
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
     * 絶対URIと相対URIを合成したURIを返す。
     * 正規化も行われる。
     *
     * @param baseURI 絶対URIでなければならない。nullでもよい。
     * @param relativeURI 絶対URIでもよいがその場合baseは無視される。
     * @return 合成結果のURLオブジェクト。必ず絶対URIになる。
     * @throws java.lang.IllegalArgumentException 絶対URIが生成できない。
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
     * オリジナルURIとリダイレクト先のURIを登録する。
     * オリジナルURIへのアクセスはリダイレクトされる。
     *
     * @param original オリジナルURI
     * @param redirect リダイレクトURI
     */
    private void putRedirectedImpl(URI original, URI redirect){
        URI oridinalNorm = original.normalize();
        URI redirectNorm = redirect.normalize();

        this.uriMap.put(oridinalNorm, redirectNorm);

        return;
    }

    /**
     * オリジナルURIとリダイレクト先のURIを登録する。
     * オリジナルURIへのアクセスはリダイレクトされる。
     *
     * @param original オリジナルURI
     * @param redirect リダイレクトURI
     */
    public void putRedirected(URI original, URI redirect){
        putRedirectedImpl(original, redirect);
        return;
    }

    /**
     * ローカル版リソース参照解決を登録する。
     *
     * @param lsc ローカル版リソース参照解決
     */
    public void putRedirected(LocalXmlResource lsc){
        URI original = lsc.getOriginalResource();
        if(original == null) return;

        URI local = lsc.getLocalResource();

        putRedirected(original, local);

        return;
    }

    /**
     * 別リゾルバの登録内容を追加登録する。
     *
     * @param other 別リゾルバ
     */
    public void putRedirected(XmlResourceResolver other){
        this.uriMap.putAll(other.uriMap);
        return;
    }

    /**
     * 登録済みリダイレクト先URIを返す。
     *
     * @param original オリジナルURI
     * @return リダイレクト先URI。未登録の場合はnull
     */
    public URI getRedirected(URI original){
        URI keyURI = original.normalize();
        URI resourceURI = this.uriMap.get(keyURI);
        return resourceURI;
    }

    /**
     * 登録済みリダイレクト先URIを返す。
     *
     * @param original オリジナルURI
     * @return リダイレクト先URI。未登録の場合はオリジナルを返す
     */
    public URI resolveRedirected(URI original){
        URI result = getRedirected(original);
        if(result == null) result = original;
        return result;
    }

    /**
     * 登録済みリダイレクト先リソースの入力ストリームを得る。
     *
     * @param originalURI オリジナルURI
     * @return 入力ストリーム。リダイレクト先が未登録の場合はnull
     * @throws java.io.IOException 入出力エラー。
     *     もしくはリソースが見つからない。
     */
    private InputStream getXMLResourceAsStream(URI originalURI)
            throws IOException{
        URI resourceURI = getRedirected(originalURI);
        if(resourceURI == null) return null;

        URL resourceURL = resourceURI.toURL();
        InputStream is = resourceURL.openStream();

        return is;
    }

    /**
     * {@inheritDoc}
     *
     * <p>URL変換したあとの入力ソースを返す。
     *
     * @param type {@inheritDoc}
     * @param namespaceURI {@inheritDoc}
     * @param publicId {@inheritDoc}
     * @param systemId {@inheritDoc}
     * @param baseURI {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public LSInput resolveResource(String type,
                                     String namespaceURI,
                                     String publicId,
                                     String systemId,
                                     String baseURI ){
        if(systemId == null) return null;

        URI originalURI;
        try{
            originalURI = buildBaseRelativeURI(baseURI, systemId);
        }catch(URISyntaxException e){
            return null;
        }

        InputStream is;
        try{
            is = getXMLResourceAsStream(originalURI);
        }catch(IOException e){
            return null;
        }
        if(is == null) return null;

        LSInput input = createLSInput();
        input.setBaseURI(baseURI);
        input.setPublicId(publicId);
        input.setSystemId(systemId);
        input.setByteStream(is);

        return input;
    }

}
