/*
 * proxy xml exporter
 *
 * License : The MIT License
 * Copyright(c) 2013 MikuToga Partners
 */

package jp.sfjp.mikutoga.xml;

import java.io.IOException;

/**
 * 委譲型XMLエクスポータ。
 */
public class ProxyXmlExporter extends AbstractXmlExporter{

    private final XmlExporter delegate;


    /**
     * コンストラクタ。
     *
     * @param delegate 委譲先
     */
    public ProxyXmlExporter(XmlExporter delegate){
        super();
        this.delegate = delegate;
        return;
    }


    /**
     * {@inheritDoc}
     *
     * @param ch {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public Appendable append(char ch) throws IOException{
        return this.delegate.append(ch);
    }

    /**
     * {@inheritDoc}
     *
     * @param seq {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public Appendable append(CharSequence seq) throws IOException{
        return this.delegate.append(seq);
    }

    /**
     * {@inheritDoc}
     *
     * @param seq {@inheritDoc}
     * @param start {@inheritDoc}
     * @param end {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public Appendable append(CharSequence seq, int start, int end)
            throws IOException{
        return this.delegate.append(seq, start, end);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException {@inheritDoc}
     */
    @Override
    public void flush() throws IOException{
        this.delegate.flush();
        return;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException {@inheritDoc}
     */
    @Override
    public void close() throws IOException{
        this.delegate.close();
        return;
    }

    /**
     * {@inheritDoc}
     *
     * @param ch {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter putRawCh(char ch) throws IOException{
        return this.delegate.putRawCh(ch);
    }

    /**
     * {@inheritDoc}
     *
     * @param seq {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public XmlExporter putRawText(CharSequence seq) throws IOException{
        return this.delegate.putRawText(seq);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public boolean isBasicLatinOnlyOut(){
        return this.delegate.isBasicLatinOnlyOut();
    }

    /**
     * {@inheritDoc}
     *
     * @param bool {@inheritDoc}
     */
    @Override
    public void setBasicLatinOnlyOut(boolean bool){
        this.delegate.setBasicLatinOnlyOut(bool);
        return;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public String getNewLine(){
        return this.delegate.getNewLine();
    }

    /**
     * {@inheritDoc}
     *
     * @param newLine {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void setNewLine(String newLine) throws NullPointerException{
        this.delegate.setNewLine(newLine);
        return;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public String getIndentUnit(){
        return this.delegate.getIndentUnit();
    }

    /**
     * {@inheritDoc}
     *
     * @param indUnit {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void setIndentUnit(String indUnit) throws NullPointerException{
        this.delegate.setIndentUnit(indUnit);
        return;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pushNest(){
        this.delegate.pushNest();
        return;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void popNest(){
        this.delegate.popNest();
        return;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public int getIndentLevel(){
        return this.delegate.getIndentLevel();
    }

}
