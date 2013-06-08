/*
 * basic xml exporter
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sfjp.mikutoga.xml;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

/**
 * Appendable用XMLエクスポータ実装。
 */
public class BasicXmlExporter extends AbstractXmlExporter{

    private Appendable appendable = null;


    /**
     * コンストラクタ。
     */
    public BasicXmlExporter(){
        super();
        return;
    }


    /**
     * 出力先アペンダを指定する。
     * @param app 出力先
     * @throws NullPointerException 引数がnull
     */
    public void setAppendable(Appendable app) throws NullPointerException{
        if(app == null) throw new NullPointerException();

        this.appendable = app;

        return;
    }

    /**
     * {@inheritDoc}
     * @param ch {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public Appendable append(char ch) throws IOException{
        return this.appendable.append(ch);
    }

    /**
     * {@inheritDoc}
     * @param seq {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public Appendable append(CharSequence seq) throws IOException{
        return this.appendable.append(seq);
    }

    /**
     * {@inheritDoc}
     * @param seq {@inheritDoc}
     * @param start {@inheritDoc}
     * @param end {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public Appendable append(CharSequence seq, int start, int end)
            throws IOException{
        return this.appendable.append(seq, start, end);
    }

    /**
     * {@inheritDoc}
     * 可能であれば出力をフラッシュする。
     * @throws IOException {@inheritDoc}
     */
    @Override
    public void flush() throws IOException{
        if(this.appendable instanceof Flushable){
            ((Flushable)this.appendable).flush();
        }
        return;
    }

    /**
     * {@inheritDoc}
     * 可能であれば出力をクローズする。
     * @throws IOException {@inheritDoc}
     */
    @Override
    public void close() throws IOException{
        if(this.appendable instanceof Closeable){
            ((Closeable)this.appendable).close();
        }
        return;
    }

}
