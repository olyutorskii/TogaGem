/*
 * PMD unified handler
 *
 * License : The MIT License
 * Copyright(c) 2013 MikuToga Partners
 */

package jp.sfjp.mikutoga.pmd.parser;

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
}
