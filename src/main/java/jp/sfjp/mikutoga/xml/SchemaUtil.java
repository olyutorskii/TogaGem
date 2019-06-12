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

/**
 * XMLスキーマの各種ビルダ。
 */
public final class SchemaUtil {

    /**
     * 隠しコンストラクタ。
     */
    private SchemaUtil(){
        assert false;
        throw new AssertionError();
    }


    /**
     * XML Schema 用のスキーマファクトリを返す。
     * @return スキーマファクトリ
     */
    public static SchemaFactory newSchemaFactory(){
        SchemaFactory result = newSchemaFactory(null);
        return result;
    }

    /**
     * XML Schema 用のスキーマファクトリを返す。
     * @param resolver カスタムリゾルバ。nullも可。
     * @return スキーマファクトリ
     */
    public static SchemaFactory newSchemaFactory(
            LSResourceResolver resolver ){
        SchemaFactory schemaFactory =
                SchemaFactory.newInstance(
                    XMLConstants.W3C_XML_SCHEMA_NS_URI
                );

        // schemaFactory.setFeature(name, value);
        // schemaFactory.setProperty(name, object);

        schemaFactory.setErrorHandler(BotherHandler.HANDLER);
        schemaFactory.setResourceResolver(resolver);

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
    private static Source[] toLocalSourceArray(LocalXmlResource[] resArray)
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
    public static Schema newSchema(XmlResourceResolver resolver,
                                    LocalXmlResource... resArray ){
        for(LocalXmlResource resource : resArray){
            resolver.putRedirected(resource);
        }

        Source[] sources;
        try{
            sources = toLocalSourceArray(resArray);
        }catch(IOException e){                   // ビルド障害
            assert false;
            throw new AssertionError(e);
        }

        SchemaFactory schemaFactory = newSchemaFactory(resolver);

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
