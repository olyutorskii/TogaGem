/*
 * xml local resource map
 *
 * License : The MIT License
 * Copyright(c) 2013 olyutorskii
 */

package jp.sourceforge.mikutoga.xml;

import java.net.URI;

/**
 * 代用ローカルリソースの管理を行う。
 * <p>ネットワークを介したグローバルなリソースと、
 * アプリ上のローカルな代用リソースとを対応付ける。
 */
public interface LocalXmlResource {

    /**
     * オリジナル版XMLリソースのURIを返す。
     * @return オリジナル版リソースのURL。
     */
    URI getOriginalResource();

    /**
     * ローカル版XMLリソースのURIを返す。
     * @return ローカル版リソースのURL。
     */
    URI getLocalResource();

}
