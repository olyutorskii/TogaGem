/*
 */

package jp.sfjp.mikutoga.xml;

import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class BasicXmlExporterTest {

    public BasicXmlExporterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of putCharRef2Hex method, of class BasicXmlExporter.
     * @throws IOException
     */
    @Test
    public void testPutCharRef2Hex() throws IOException{
        System.out.println("putCharRef2Hex");

        BasicXmlExporter instance;
        StringBuffer buf;

        instance = new BasicXmlExporter();

        buf = new StringBuffer();
        instance.setAppendable(buf);
        instance.putCharRef2Hex('\u0000');
        assertEquals("&#x00;", buf.toString());

        buf = new StringBuffer();
        instance.setAppendable(buf);
        instance.putCharRef2Hex('A');
        assertEquals("&#x41;", buf.toString());

        buf = new StringBuffer();
        instance.setAppendable(buf);
        instance.putCharRef2Hex('\u00ff');
        assertEquals("&#xFF;", buf.toString());

        buf = new StringBuffer();
        instance.setAppendable(buf);
        instance.putCharRef2Hex('\u0100');
        assertEquals("&#x0100;", buf.toString());

        return;
    }

    /**
     * Test of putCh method, of class BasicXmlExporter.
     * @throws IOException
     */
    @Test
    public void testPutCh() throws IOException{
        System.out.println("putCh");

        BasicXmlExporter instance;
        StringBuffer buf;

        instance = new BasicXmlExporter();

        buf = new StringBuffer();
        instance.setAppendable(buf);
        instance.putCh('A');
        assertEquals("A", buf.toString());

        buf = new StringBuffer();
        instance.setAppendable(buf);
        instance.putCh('B').putCh('7').putCh('あ');
        assertEquals("B7あ", buf.toString());

        buf = new StringBuffer();
        instance.setAppendable(buf);
        instance.putCh('&').putCh('<').putCh('>').putCh('"').putCh('\'');
        assertEquals("&amp;&lt;&gt;&quot;&apos;", buf.toString());

        buf = new StringBuffer();
        instance.setAppendable(buf);
        instance.putCh('\b');
        assertEquals("&#x08;", buf.toString());

        return;
    }

    /**
     * Test of append method, of class BasicXmlExporter.
     */
    @Test
    public void testAppend() {
        System.out.println("setAppendable");

        BasicXmlExporter instance;
        Appendable app;
        StringBuffer buf;
        
        instance = new BasicXmlExporter();

        try{
            instance.append(null);
            fail();
        }catch(NullPointerException e){
            assert true;
        }catch(IOException e){
            fail();
        }

        buf = new StringBuffer();
        app = buf;

        instance.setAppendable(app);

        try{
            instance.append("abc");
            instance.append('d');
            instance.append("abcdef", 4, 5);
        }catch(IOException e){
            fail();
        }

        assertEquals("abcde", buf.toString());

        try{
            instance.flush();
            instance.close();
        }catch(IOException e){
            fail();
        }

        return;
    }

    @Test
    public void testPutXsdInt() throws IOException{
        System.out.println("putXsdInt");

        BasicXmlExporter instance;
        StringBuffer buf;

        instance = new BasicXmlExporter();

        buf = new StringBuffer();
        instance.setAppendable(buf);
        instance.putXsdInt(-1).putCh(',').putXsdInt(0).putCh(',').putXsdInt(1);
        assertEquals("-1,0,1", buf.toString());

        buf = new StringBuffer();
        instance.setAppendable(buf);
        instance.putXsdInt(-999).putCh(',').putXsdInt(9999);
        assertEquals("-999,9999", buf.toString());

        buf = new StringBuffer();
        instance.setAppendable(buf);
        instance.putXsdInt(Integer.MIN_VALUE).putCh(',').putXsdInt(Integer.MAX_VALUE);
        assertEquals("-2147483648,2147483647", buf.toString());

        return;
    }

    @Test
    public void testPutXsdFloat() throws IOException{
        System.out.println("putXsdFloat");

        BasicXmlExporter instance;
        StringBuffer buf;

        instance = new BasicXmlExporter();

        buf = new StringBuffer();
        instance.setAppendable(buf);
        instance.putXsdFloat(-1.0f).putCh(',')
                .putXsdFloat(-0.0f).putCh(',')
                .putXsdFloat(0.0f).putCh(',')
                .putXsdFloat(1.0f);
        assertEquals("-1.0,-0.0,0.0,1.0", buf.toString());

        buf = new StringBuffer();
        instance.setAppendable(buf);
        instance.putXsdFloat(Float.NEGATIVE_INFINITY).putCh(',')
                .putXsdFloat(Float.POSITIVE_INFINITY).putCh(',')
                .putXsdFloat(Float.NaN);
        assertEquals("-INF,INF,NaN", buf.toString());

        buf = new StringBuffer();
        instance.setAppendable(buf);
        instance.putXsdFloat(Float.MIN_VALUE).putCh(',')
                .putXsdFloat(Float.MIN_NORMAL).putCh(',')
                .putXsdFloat(Float.MAX_VALUE);
        assertEquals("1.4E-45,1.17549435E-38,3.4028235E38", buf.toString());

        buf = new StringBuffer();
        instance.setAppendable(buf);
        instance.putXsdFloat(0.001f);
        assertEquals("0.001", buf.toString()); // fuzzy "0.0010" on JDK1.6

        buf = new StringBuffer();
        instance.setAppendable(buf);
        instance.putXsdFloat(StrictMath.nextDown(0.001f));
        assertEquals("9.999999E-4", buf.toString());

        buf = new StringBuffer();
        instance.setAppendable(buf);
        instance.putXsdFloat(10000000.0f);
        assertEquals("1.0E7", buf.toString());

        buf = new StringBuffer();
        instance.setAppendable(buf);
        instance.putXsdFloat(StrictMath.nextDown(10000000.0f));
        assertEquals("9999999.0", buf.toString());

        buf = new StringBuffer();
        instance.setAppendable(buf);
        instance.putXsdFloat((float)StrictMath.E).putCh(',')
                .putXsdFloat((float)StrictMath.PI);
        assertEquals("2.7182817,3.1415927", buf.toString());

        return;
    }

}
