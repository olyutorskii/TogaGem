/*
 * nothing proxy factory
 *
 * License : The MIT License
 * Copyright(c) 2013 MikuToga Partners
 */

package jp.sfjp.mikutoga.corelib;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 何もしないインタフェース実装を生成する。
 */
public final class EmptyProxyFactory {

    /** 何もせず何も返さないInvoker。 */
    public static final InvocationHandler NOTHING_INVOKER = new Nothing();


    /**
     * ダミーコンストラクタ。
     */
    private EmptyProxyFactory(){
        assert false;
        throw new AssertionError();
    }


    /**
     * 何もしないインタフェース実装のインスタンスを生成する。
     *
     * <p>インタフェースの各メソッド戻り値はvoidでなければならない。
     *
     * @param types インタフェース群
     * @return インタフェースを実装したインスタンス。
     */
    public static Object buildEmptyProxy(Class<?>... types){
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Object proxy = buildEmptyProxy(loader, types);
        return proxy;
    }

    /**
     * 何もしないインタフェース実装のインスタンスを生成する。
     *
     * <p>インタフェースの各メソッド戻り値はvoidでなければならない。
     *
     * @param loader class-loader
     * @param types インタフェース群
     * @return インタフェースを実装したインスタンス。
     */
    public static Object buildEmptyProxy(
            ClassLoader loader, Class<?>... types){
        Object proxy =
                Proxy.newProxyInstance(loader, types, NOTHING_INVOKER);
        return proxy;
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
