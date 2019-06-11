/*
 * xml local resource map
 *
 * License : The MIT License
 * Copyright(c) 2013 olyutorskii
 */

package jp.sfjp.mikutoga.xml;

import java.net.URI;

/**
 * 代用ローカルリソースの管理を行う。
 *
 * <p>ネットワークを介したグローバルなリソースと、
 * アプリ上のローカルな代用リソースとを対応付ける。
 */
public interface LocalXmlResource {

    /**
     * オリジナル版XMLリソースのURIを返す。
     * @return オリジナル版リソースのURL。
     */
    public abstract URI getOriginalResource();

    /**
     * ローカル版XMLリソースのURIを返す。
     * @return ローカル版リソースのURL。
     */
    public abstract URI getLocalResource();

}
