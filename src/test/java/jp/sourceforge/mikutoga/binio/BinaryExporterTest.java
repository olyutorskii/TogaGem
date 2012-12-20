/*
 */

package jp.sourceforge.mikutoga.binio;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
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
public class BinaryExporterTest {

    public BinaryExporterTest() {
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
     * Test of close method, of class BinaryExporter.
     */
    @Test
    public void testClose() throws Exception {
        System.out.println("close");

        TestOutputStream os = new TestOutputStream();
        BinaryExporter bex;
        bex = new BinaryExporter(os);

        assertFalse(os.closed);
        bex.close();
        assertTrue(os.closed);

        return;
    }

    /**
     * Test of flush method, of class BinaryExporter.
     */
    @Test
    public void testFlush() throws Exception {
        System.out.println("flush");

        TestOutputStream os = new TestOutputStream();
        BinaryExporter bex;
        bex = new BinaryExporter(os);

        assertFalse(os.flushed);
        bex.flush();
        assertTrue(os.flushed);

        return;
    }

    /**
     * Test of dumpByte method, of class BinaryExporter.
     */
    @Test
    public void testDumpByte_byte() throws Exception {
        System.out.println("dumpByte");

        ByteArrayOutputStream bos;
        BinaryExporter bex;
        byte[] barr;

        bos = new ByteArrayOutputStream();
        bex = new BinaryExporter(bos);

        bex.dumpByte((byte)10);
        bex.flush();

        barr = bos.toByteArray();
        assertEquals(1, barr.length);
        assertEquals((byte)10, barr[0]);

        return;
    }

    /**
     * Test of dumpByte method, of class BinaryExporter.
     */
    @Test
    public void testDumpByte_int() throws Exception {
        System.out.println("dumpByte");

        ByteArrayOutputStream bos;
        BinaryExporter bex;
        byte[] barr;

        bos = new ByteArrayOutputStream();
        bex = new BinaryExporter(bos);

        bex.dumpByte(10);
        bex.flush();
        barr = bos.toByteArray();
        assertEquals(1, barr.length);
        assertEquals((byte)10, barr[0]);

        bos.reset();
        bex.dumpByte(257);
        bex.flush();
        barr = bos.toByteArray();
        assertEquals(1, barr.length);
        assertEquals((byte)1, barr[0]);


        return;
    }

    /**
     * Test of dumpByteArray method, of class BinaryExporter.
     */
    @Test
    public void testDumpByteArray_byteArr() throws Exception {
        System.out.println("dumpByteArray");

        ByteArrayOutputStream bos;
        BinaryExporter bex;
        byte[] barr;

        bos = new ByteArrayOutputStream();
        bex = new BinaryExporter(bos);

        bex.dumpByteArray(new byte[]{0x01, 0x02, 0x03});
        bex.flush();

        barr = bos.toByteArray();
        assertEquals(3, barr.length);
        assertEquals((byte)0x01, barr[0]);
        assertEquals((byte)0x02, barr[1]);
        assertEquals((byte)0x03, barr[2]);

        return;
    }

    /**
     * Test of dumpByteArray method, of class BinaryExporter.
     */
    @Test
    public void testDumpByteArray_3args() throws Exception {
        System.out.println("dumpByteArray");

        ByteArrayOutputStream bos;
        BinaryExporter bex;
        byte[] barr;

        bos = new ByteArrayOutputStream();
        bex = new BinaryExporter(bos);

        bex.dumpByteArray(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05},
                          1, 3);
        bex.flush();

        barr = bos.toByteArray();
        assertEquals(3, barr.length);
        assertEquals((byte)0x02, barr[0]);
        assertEquals((byte)0x03, barr[1]);
        assertEquals((byte)0x04, barr[2]);

        return;
    }

    /**
     * Test of dumpLeShort method, of class BinaryExporter.
     */
    @Test
    public void testDumpLeShort_short() throws Exception {
        System.out.println("dumpLeShort");

        ByteArrayOutputStream bos;
        BinaryExporter bex;
        byte[] barr;

        bos = new ByteArrayOutputStream();
        bex = new BinaryExporter(bos);

        bos.reset();
        bex.dumpLeShort((short)( 256 * 2 + 1));
        bex.flush();
        barr = bos.toByteArray();
        assertEquals(2, barr.length);
        assertEquals((byte)1, barr[0]);
        assertEquals((byte)2, barr[1]);

        bos.reset();
        bex.dumpLeShort((short)-2);
        bex.flush();
        barr = bos.toByteArray();
        assertEquals(2, barr.length);
        assertEquals((byte)0xfe, barr[0]);
        assertEquals((byte)0xff, barr[1]);

        return;
    }

    /**
     * Test of dumpLeShort method, of class BinaryExporter.
     */
    @Test
    public void testDumpLeShort_int() throws Exception {
        System.out.println("dumpLeShort");

        ByteArrayOutputStream bos;
        BinaryExporter bex;
        byte[] barr;

        bos = new ByteArrayOutputStream();
        bex = new BinaryExporter(bos);

        bos.reset();
        bex.dumpLeShort(256 * 2 + 1);
        bex.flush();
        barr = bos.toByteArray();
        assertEquals(2, barr.length);
        assertEquals((byte)1, barr[0]);
        assertEquals((byte)2, barr[1]);

        bos.reset();
        bex.dumpLeShort(0xff1234);
        bex.flush();
        barr = bos.toByteArray();
        assertEquals(2, barr.length);
        assertEquals((byte)0x34, barr[0]);
        assertEquals((byte)0x12, barr[1]);

        bos.reset();
        bex.dumpLeShort(-2);
        bex.flush();
        barr = bos.toByteArray();
        assertEquals(2, barr.length);
        assertEquals((byte)0xfe, barr[0]);
        assertEquals((byte)0xff, barr[1]);

        return;
    }

    /**
     * Test of dumpLeInt method, of class BinaryExporter.
     */
    @Test
    public void testDumpLeInt() throws Exception {
        System.out.println("dumpLeInt");

        ByteArrayOutputStream bos;
        BinaryExporter bex;
        byte[] barr;

        bos = new ByteArrayOutputStream();
        bex = new BinaryExporter(bos);

        bos.reset();
        bex.dumpLeInt(0x12345678);
        bex.flush();
        barr = bos.toByteArray();
        assertEquals(4, barr.length);
        assertEquals((byte)0x78, barr[0]);
        assertEquals((byte)0x56, barr[1]);
        assertEquals((byte)0x34, barr[2]);
        assertEquals((byte)0x12, barr[3]);

        bos.reset();
        bex.dumpLeInt(-2);
        bex.flush();
        barr = bos.toByteArray();
        assertEquals(4, barr.length);
        assertEquals((byte)0xfe, barr[0]);
        assertEquals((byte)0xff, barr[1]);
        assertEquals((byte)0xff, barr[2]);
        assertEquals((byte)0xff, barr[3]);

        return;
    }

    /**
     * Test of dumpLeLong method, of class BinaryExporter.
     */
    @Test
    public void testDumpLeLong() throws Exception {
        System.out.println("dumpLeLong");

        ByteArrayOutputStream bos;
        BinaryExporter bex;
        byte[] barr;

        bos = new ByteArrayOutputStream();
        bex = new BinaryExporter(bos);

        bos.reset();
        bex.dumpLeLong(0x12345678abcdef00L);
        bex.flush();
        barr = bos.toByteArray();
        assertEquals(8, barr.length);
        assertEquals((byte)0x00, barr[0]);
        assertEquals((byte)0xef, barr[1]);
        assertEquals((byte)0xcd, barr[2]);
        assertEquals((byte)0xab, barr[3]);
        assertEquals((byte)0x78, barr[4]);
        assertEquals((byte)0x56, barr[5]);
        assertEquals((byte)0x34, barr[6]);
        assertEquals((byte)0x12, barr[7]);

        bos.reset();
        bex.dumpLeLong(-2L);
        bex.flush();
        barr = bos.toByteArray();
        assertEquals(8, barr.length);
        assertEquals((byte)0xfe, barr[0]);
        assertEquals((byte)0xff, barr[1]);
        assertEquals((byte)0xff, barr[2]);
        assertEquals((byte)0xff, barr[3]);
        assertEquals((byte)0xff, barr[4]);
        assertEquals((byte)0xff, barr[5]);
        assertEquals((byte)0xff, barr[6]);
        assertEquals((byte)0xff, barr[7]);

        return;
    }

    /**
     * Test of dumpLeFloat method, of class BinaryExporter.
     */
    @Test
    public void testDumpLeFloat() throws Exception {
        System.out.println("dumpLeFloat");

        ByteArrayOutputStream bos;
        BinaryExporter bex;
        byte[] barr;

        bos = new ByteArrayOutputStream();
        bex = new BinaryExporter(bos);

        bos.reset();
        bex.dumpLeFloat(Float.intBitsToFloat(0x12345678));
        bex.flush();
        barr = bos.toByteArray();
        assertEquals(4, barr.length);
        assertEquals((byte)0x78, barr[0]);
        assertEquals((byte)0x56, barr[1]);
        assertEquals((byte)0x34, barr[2]);
        assertEquals((byte)0x12, barr[3]);

        bos.reset();
        bex.dumpLeFloat(-1.5f);
        bex.flush();
        barr = bos.toByteArray();
        assertEquals(4, barr.length);
        assertEquals((byte)0x00, barr[0]);
        assertEquals((byte)0x00, barr[1]);
        assertEquals((byte)0xc0, barr[2]);
        assertEquals((byte)0xbf, barr[3]);

        return;
    }

    /**
     * Test of dumpLeDouble method, of class BinaryExporter.
     */
    @Test
    public void testDumpLeDouble() throws Exception {
        System.out.println("dumpLeDouble");

        ByteArrayOutputStream bos;
        BinaryExporter bex;
        byte[] barr;

        bos = new ByteArrayOutputStream();
        bex = new BinaryExporter(bos);

        bos.reset();
        bex.dumpLeDouble(Double.longBitsToDouble(0x12345678abcdef00L));
        bex.flush();
        barr = bos.toByteArray();
        assertEquals(8, barr.length);
        assertEquals((byte)0x00, barr[0]);
        assertEquals((byte)0xef, barr[1]);
        assertEquals((byte)0xcd, barr[2]);
        assertEquals((byte)0xab, barr[3]);
        assertEquals((byte)0x78, barr[4]);
        assertEquals((byte)0x56, barr[5]);
        assertEquals((byte)0x34, barr[6]);
        assertEquals((byte)0x12, barr[7]);

        bos.reset();
        bex.dumpLeDouble(-1.5);
        bex.flush();
        barr = bos.toByteArray();
        assertEquals(8, barr.length);
        assertEquals((byte)0x00, barr[0]);
        assertEquals((byte)0x00, barr[1]);
        assertEquals((byte)0x00, barr[2]);
        assertEquals((byte)0x00, barr[3]);
        assertEquals((byte)0x00, barr[4]);
        assertEquals((byte)0x00, barr[5]);
        assertEquals((byte)0xf8, barr[6]);
        assertEquals((byte)0xbf, barr[7]);

        return;
    }

    /**
     * Test of dumpFiller method, of class BinaryExporter.
     */
    @Test
    public void testDumpFiller() throws Exception {
        System.out.println("dumpFiller");

        ByteArrayOutputStream bos;
        BinaryExporter bex;
        byte[] barr;

        bos = new ByteArrayOutputStream();
        bex = new BinaryExporter(bos);

        bex.dumpFiller(new byte[]{}, 1);
        bex.flush();
        barr = bos.toByteArray();
        assertEquals(0, barr.length);

        bos.reset();
        bex.dumpFiller(new byte[]{0x01}, 3);
        bex.flush();
        barr = bos.toByteArray();
        assertEquals(3, barr.length);
        assertEquals((byte)0x01, barr[0]);
        assertEquals((byte)0x01, barr[1]);
        assertEquals((byte)0x01, barr[2]);

        bos.reset();
        bex.dumpFiller(new byte[]{0x01, 0x02}, 3);
        bex.flush();
        barr = bos.toByteArray();
        assertEquals(3, barr.length);
        assertEquals((byte)0x01, barr[0]);
        assertEquals((byte)0x02, barr[1]);
        assertEquals((byte)0x02, barr[2]);

        bos.reset();
        bex.dumpFiller(new byte[]{0x01, 0x02, 0x03}, 2);
        bex.flush();
        barr = bos.toByteArray();
        assertEquals(2, barr.length);
        assertEquals((byte)0x01, barr[0]);
        assertEquals((byte)0x02, barr[1]);

        return;
    }

    /**
     * Test of dumpFixedW31j method, of class BinaryExporter.
     */
    @Test
    public void testDumpFixedW31j() throws Exception {
        System.out.println("dumpFixedW31j");

        ByteArrayOutputStream bos;
        BinaryExporter bex;
        byte[] barr;

        bos = new ByteArrayOutputStream();
        bex = new BinaryExporter(bos);

        bos.reset();
        bex.dumpFixedW31j("あい", 7, new byte[]{0x01, 0x02});
        bex.flush();
        barr = bos.toByteArray();
        assertEquals(7, barr.length);
        assertEquals((byte)0x82, barr[0]);
        assertEquals((byte)0xA0, barr[1]);
        assertEquals((byte)0x82, barr[2]);
        assertEquals((byte)0xA2, barr[3]);
        assertEquals((byte)0x01, barr[4]);
        assertEquals((byte)0x02, barr[5]);
        assertEquals((byte)0x02, barr[6]);

        bos.reset();
        try{
            bex.dumpFixedW31j("あい", 3, new byte[]{0x00});
            fail();
        }catch(IllegalTextExportException e){
            // GOOD
        }

        bos.reset();
        try{
            bex.dumpFixedW31j("¤", 10, new byte[]{0x00});
            fail();
        }catch(IllegalTextExportException e){
            // GOOD
        }

        return;
    }

    /**
     * Test of dumpHollerithUtf16LE method, of class BinaryExporter.
     */
    @Test
    public void testDumpHollerithUtf16LE() throws Exception {
        System.out.println("dumpHollerithUtf16LE");

        ByteArrayOutputStream bos;
        BinaryExporter bex;
        byte[] barr;

        bos = new ByteArrayOutputStream();
        bex = new BinaryExporter(bos);

        bex.dumpHollerithUtf16LE("あい");
        bex.flush();
        barr = bos.toByteArray();
        assertEquals(8, barr.length);
        assertEquals((byte)0x04, barr[0]);
        assertEquals((byte)0x00, barr[1]);
        assertEquals((byte)0x00, barr[2]);
        assertEquals((byte)0x00, barr[3]);
        assertEquals((byte)0x42, barr[4]);
        assertEquals((byte)0x30, barr[5]);
        assertEquals((byte)0x44, barr[6]);
        assertEquals((byte)0x30, barr[7]);

        bos.reset();
        bex.dumpHollerithUtf16LE("");
        bex.flush();
        barr = bos.toByteArray();
        assertEquals(4, barr.length);
        assertEquals((byte)0x00, barr[0]);
        assertEquals((byte)0x00, barr[1]);
        assertEquals((byte)0x00, barr[2]);
        assertEquals((byte)0x00, barr[3]);

        return;
    }

    private static class TestOutputStream extends FilterOutputStream{
        public boolean closed = false;
        public boolean flushed = false;

        TestOutputStream(){
            super(new ByteArrayOutputStream());
            return;
        }

        @Override
        public void flush() throws IOException {
            super.flush();
            this.flushed = true;
            return;
        }

        @Override
        public void close() throws IOException {
            super.close();
            this.closed = true;
            return;
        }

    }

}
