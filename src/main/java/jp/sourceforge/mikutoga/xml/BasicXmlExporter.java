/*
 * basic xml exporter
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sourceforge.mikutoga.xml;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 * Appendable用XMLエクスポータ実装。
 */
public class BasicXmlExporter extends AbstractXmlExporter{

    /** デフォルトエンコーディング。 */
    private static final Charset CS_UTF8 = Charset.forName("UTF-8");


    private final Appendable appendable;


    /**
     * コンストラクタ。
     * 文字エンコーディングはUTF-8が用いられる。
     * @param stream 出力ストリーム
     */
    public BasicXmlExporter(OutputStream stream){
        this(stream, CS_UTF8);
        return;
    }

    /**
     * コンストラクタ。
     * @param stream 出力ストリーム
     * @param charSet 文字エンコーディング指定
     */
    public BasicXmlExporter(OutputStream stream, Charset charSet){
        this(
            new BufferedWriter(
                new OutputStreamWriter(stream, charSet)
            )
        );
        return;
    }

    /**
     * コンストラクタ。
     * @param appendable 文字列出力
     */
    public BasicXmlExporter(Appendable appendable){
        super();
        this.appendable = appendable;
        return;
    }


    /**
     * {@inheritDoc}
     * @param ch {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public BasicXmlExporter putRawCh(char ch) throws IOException{
        this.appendable.append(ch);
        return this;
    }

    /**
     * {@inheritDoc}
     * @param seq {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public BasicXmlExporter putRawText(CharSequence seq) throws IOException{
        this.appendable.append(seq);
        return this;
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
