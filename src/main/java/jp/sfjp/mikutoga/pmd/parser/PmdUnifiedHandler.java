/*
 * PMD unified handler
 *
 * License : The MIT License
 * Copyright(c) 2013 MikuToga Partners
 */

package jp.sfjp.mikutoga.pmd.parser;

import jp.sfjp.mikutoga.corelib.EmptyProxyFactory;

/**
 * PMDパーサ用の統合ハンドラ。
 */
public interface PmdUnifiedHandler
    extends PmdBasicHandler,
            PmdShapeHandler,
            PmdMaterialHandler,
            PmdBoneHandler,
            PmdMorphHandler,
            PmdEngHandler,
            PmdToonHandler,
            PmdRigidHandler,
            PmdJointHandler {

    /** 何もしない統合ハンドラ。 */
    PmdUnifiedHandler EMPTY =
            (PmdUnifiedHandler)
            ( EmptyProxyFactory.buildEmptyProxy(PmdUnifiedHandler.class) );

}
