/*
 */

package jp.sfjp.mikutoga.bin.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedOutputStream;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class TextExporterTest {

    private static Charset CS_ASCII = Charset.forName("US-ASCII");
    private static Charset CS_UTF8  = Charset.forName("UTF-8");

    public TextExporterTest() {
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
     * Test of constructor, of class TextExporter.
     * @throws Exception
     */
    @Test
    public void testCons() throws Exception {
        System.out.println("constructor");

        TextExporter exp;

        try{
            exp = new TextExporter((Charset)null);
            fail();
        }catch(NullPointerException e){
            // GOOD
        }

        try{
            exp = new TextExporter((CharsetEncoder)null);
            fail();
        }catch(NullPointerException e){
            // GOOD
        }

        return;
    }

    /**
     * Test of getEncoder method, of class TextExporter.
     */
    @Test
    public void testGetEncoder() {
        System.out.println("getEncoder");

        Charset cs;
        cs = Charset.forName("US-ASCII");
        CharsetEncoder usenc = cs.newEncoder();

        TextExporter exporter;
        CharsetEncoder enc;

        exporter = new TextExporter(cs);
        enc = exporter.getEncoder();
        assertEquals(usenc.charset(), enc.charset());

        exporter = new TextExporter(usenc);
        enc = exporter.getEncoder();
        assertEquals(usenc.charset(), enc.charset());

        return;
    }

    /**
     * Test of setCharBufSize method, of class TextExporter.
     */
    @Test
    public void testSetBufSize() {
        System.out.println("setCharBufSize");

        TextExporter exporter;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        exporter = new TextExporter(CS_UTF8);
        // JRE 1.6 と 1.7 でびみょーに違う
        float maxb = CS_UTF8.newEncoder().maxBytesPerChar();

        exporter.setCharBufSize(1);
        int minb = (int)( StrictMath.floor(maxb) );
        exporter.setByteBufSize(minb);
        bout.reset();
        try{
            exporter.encodeToByteStream("あいう", bout);
        }catch(CharacterCodingException e){
            fail();
        }
        assertEquals(9, bout.size());

        try{
            int failSize = minb - 1;
            exporter.setByteBufSize(failSize);
            fail();
        }catch(IllegalArgumentException e){
            // GOOD
        }

        try{
            exporter.setCharBufSize(0);
            fail();
        }catch(IllegalArgumentException e){
            // GOOD
        }

        return;
    }

    /**
     * Test of dumpText method, of class TextExporter.
     * @throws Exception
     */
    @Test
    public void testDumpText() throws Exception {
        System.out.println("dumpText");

        TextExporter exporter;
        ByteArrayOutputStream bout;
        byte[] barr;

        exporter = new TextExporter(CS_ASCII);

        bout = new ByteArrayOutputStream();
        exporter.dumpText("ABC", bout);
        assertEquals(3, bout.size());
        barr = bout.toByteArray();
        assertEquals(3, barr.length);
        assertEquals((byte)0x41, barr[0]);
        assertEquals((byte)0x42, barr[1]);
        assertEquals((byte)0x43, barr[2]);

        PipedOutputStream pout;
        pout = new PipedOutputStream();
        try{
            exporter.dumpText("ABC", pout);
            fail();
        }catch(IOException e){
            // GOOD
        }

        return;
    }

    /**
     * Test of encodeToByteStream method, of class TextExporter.
     * @throws Exception
     */
    @Test
    public void testEncodeToByteStream() throws Exception {
        System.out.println("encodeToByteStream");

        TextExporter exporter;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] barr;

        exporter = new TextExporter(CS_ASCII);

        bout.reset();
        exporter.encodeToByteStream("ABC", bout);
        assertEquals(3, bout.size());
        barr = bout.toByteArray();
        assertEquals(3, barr.length);
        assertEquals((byte)0x41, barr[0]);
        assertEquals((byte)0x42, barr[1]);
        assertEquals((byte)0x43, barr[2]);

        bout.reset();
        exporter.encodeToByteStream("", bout);
        assertEquals(0, bout.size());

        bout.reset();
        try{
            exporter.encodeToByteStream("あ", bout);
            fail();
        }catch(CharacterCodingException e){
            // GOOD
        }

        exporter = new TextExporter(CS_UTF8);
        bout.reset();
        exporter.encodeToByteStream("あ", bout);
        assertEquals(3, bout.size());
        barr = bout.toByteArray();
        assertEquals(3, barr.length);
        assertEquals((byte)0xe3, barr[0]);
        assertEquals((byte)0x81, barr[1]);
        assertEquals((byte)0x82, barr[2]);

        return;
    }

}
