/*
 */
package jp.sourceforge.mikutoga.parser;

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
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
public class SpottedInputStreamTest {

    public SpottedInputStreamTest() {
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
     * Test of read method, of class SpottedInputStream.
     */
    @Test
    public void testRead_0args() throws Exception {
        System.out.println("read");

        SpottedInputStream sis;
        ByteArrayInputStream bis;

        bis = new ByteArrayInputStream(new byte[]{0x01, 0x02});
        sis = new SpottedInputStream(bis);

        int result;

        result = sis.read();
        assertEquals(0x01, result);

        result = sis.read();
        assertEquals(0x02, result);

        result = sis.read();
        assertEquals(-1, result);

        return;
    }

    /**
     * Test of read method, of class SpottedInputStream.
     */
    @Test
    public void testRead_byteArr() throws Exception {
        System.out.println("read");

        SpottedInputStream sis;
        ByteArrayInputStream bis;

        bis = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        sis = new SpottedInputStream(bis);

        byte[] buf = new byte[2];
        int result;

        result = sis.read(buf);
        assertEquals(2, result);
        assertEquals((byte)0x01, buf[0]);
        assertEquals((byte)0x02, buf[1]);

        result = sis.read(buf);
        assertEquals(1, result);
        assertEquals((byte)0x03, buf[0]);

        result = sis.read(buf);
        assertEquals(-1, result);

        return;
    }

    /**
     * Test of read method, of class SpottedInputStream.
     */
    @Test
    public void testRead_3args() throws Exception {
        System.out.println("read");


        SpottedInputStream sis;
        ByteArrayInputStream bis;

        bis = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        sis = new SpottedInputStream(bis);

        byte[] buf = new byte[3];
        buf[0] = (byte)0xf1;
        buf[1] = (byte)0xf2;
        buf[2] = (byte)0xf3;
        int result;

        result = sis.read(buf, 1, 2);
        assertEquals(2, result);
        assertEquals((byte)0xf1, buf[0]);
        assertEquals((byte)0x01, buf[1]);
        assertEquals((byte)0x02, buf[2]);

        result = sis.read(buf, 0, 1);
        assertEquals(1, result);
        assertEquals((byte)0x03, buf[0]);
        assertEquals((byte)0x01, buf[1]);
        assertEquals((byte)0x02, buf[2]);

        result = sis.read(buf, 0, 1);
        assertEquals(-1, result);

        return;
    }

    /**
     * Test of skip method, of class SpottedInputStream.
     */
    @Test
    public void testSkip() throws Exception {
        System.out.println("skip");

        SpottedInputStream sis;
        ByteArrayInputStream bis;

        bis = new ByteArrayInputStream(new byte[]{0x11, 0x12, 0x13});
        sis = new SpottedInputStream(bis);

        int result;
        long skipped;

        result = sis.read();
        assertEquals(0x11, result);

        skipped = sis.skip(1L);
        assertEquals(1L, skipped);

        result = sis.read();
        assertEquals(0x13, result);

        skipped = sis.skip(1L);
        assertEquals(0L, skipped);

        return;
    }

    /**
     * Test of close method, of class SpottedInputStream.
     */
    @Test
    public void testClose() throws Exception {
        System.out.println("close");

        SpottedInputStream sis;
        TestInputStream tis;

        tis = new TestInputStream();
        sis = new SpottedInputStream(tis);

        assertFalse(tis.closed);
        sis.close();
        assertTrue(tis.closed);

        return;
    }

    /**
     * Test of getPosition method, of class SpottedInputStream.
     */
    @Test
    public void testGetPosition() throws Exception{
        System.out.println("getPosition");

        SpottedInputStream sis;
        ByteArrayInputStream bis;

        bis = new ByteArrayInputStream(new byte[]{0x01,0x02});
        sis = new SpottedInputStream(bis);

        assertEquals(0L, sis.getPosition());

        sis.read();
        assertEquals(1L, sis.getPosition());

        sis.read();
        assertEquals(2L, sis.getPosition());

        sis.read();
        assertEquals(2L, sis.getPosition());

        return;
    }

    /**
     * Test of hasMore method, of class SpottedInputStream.
     */
    @Test
    public void testHasMore() throws Exception {
        System.out.println("hasMore");

        SpottedInputStream sis;
        ByteArrayInputStream bis;

        bis = new ByteArrayInputStream(new byte[]{0x01,0x02});
        sis = new SpottedInputStream(bis);

        assertTrue(sis.hasMore());
        sis.read();
        assertTrue(sis.hasMore());
        sis.read();
        assertFalse(sis.hasMore());

        return;
    }

    private static class TestInputStream extends FilterInputStream{
        public boolean closed = false;
        public boolean flushed = false;

        TestInputStream(){
            super(new ByteArrayInputStream(new byte[]{}));
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
