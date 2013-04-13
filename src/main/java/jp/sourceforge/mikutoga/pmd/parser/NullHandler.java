/*
 * PMD nothing handler
 *
 * License : The MIT License
 * Copyright(c) 2013 MikuToga Partners
 */

package jp.sourceforge.mikutoga.pmd.parser;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 何もしない統合ハンドラを提供する。
 */
public final class NullHandler{

    /** 何もしない統合ハンドラ。 */
    public static final PmdUnifiedHandler HANDLER;

    static{
        Class types[] = { PmdUnifiedHandler.class };
        ClassLoader loader = types[0].getClassLoader();
        InvocationHandler nothing = new Nothing();

        Object proxy = Proxy.newProxyInstance(loader, types, nothing);
        assert proxy instanceof PmdUnifiedHandler;

        HANDLER = (PmdUnifiedHandler) proxy;
    }


    /**
     * ダミーコンストラクタ。
     */
    private NullHandler(){
        assert false;
        throw new AssertionError();
    }


    /**
     * 何もしないInvoker実装。
     */
    private static class Nothing implements InvocationHandler{

        /**
         * コンストラクタ。
         */
        Nothing(){
            super();
            return;
        }

        /**
         * {@inheritDoc}
         * NOTHING...
         * @param proxy {@inheritDoc}
         * @param method {@inheritDoc}
         * @param args {@inheritDoc}
         * @return {@inheritDoc}
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args){
            return null;
        }

    }

}
