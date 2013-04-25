/*
 * VMD unified handler
 *
 * License : The MIT License
 * Copyright(c) 2013 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.parser;

import jp.sfjp.mikutoga.corelib.EmptyProxyFactory;

/**
 * VMDパーサ用の統合ハンドラ。
 */
public interface VmdUnifiedHandler
    extends VmdBasicHandler,
            VmdCameraHandler,
            VmdLightingHandler {

    /** 何もしない統合ハンドラ。 */
    VmdUnifiedHandler EMPTY =
            (VmdUnifiedHandler)
            ( EmptyProxyFactory.buildEmptyProxy(VmdUnifiedHandler.class) );

}
