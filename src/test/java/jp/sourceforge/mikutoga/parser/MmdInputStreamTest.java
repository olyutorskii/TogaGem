/*
 */

package jp.sourceforge.mikutoga.parser;

import java.io.ByteArrayInputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class MmdInputStreamTest {

    public MmdInputStreamTest() {
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
     * Test of parseByte method, of class MmdInputStream.
     */
    @Test
    public void testParseByte() throws Exception {
        System.out.println("parseByte");

        MmdInputStream mis;
        ByteArrayInputStream bis;

        bis = new ByteArrayInputStream(new byte[]{0x01, 0x02});
        mis = new MmdInputStream(bis);

        byte result;

        result = mis.parseByte();
        assertEquals(result, (byte)0x01);

        result = mis.parseByte();
        assertEquals(result, (byte)0x02);

        try{
            mis.parseByte();
            fail();
        }catch(MmdEofException e){
            // GOOD
        }

        assertEquals(-1, mis.read());

        return;
    }

    /**
     * Test of parseBoolean method, of class MmdInputStream.
     */
    @Test
    public void testParseBoolean() throws Exception {
        System.out.println("parseBoolean");

        MmdInputStream mis;
        ByteArrayInputStream bis;

        bis = new ByteArrayInputStream(new byte[]{0x00, 0x01, 0x02});
        mis = new MmdInputStream(bis);

        boolean result;

        result = mis.parseBoolean();
        assertFalse(result);

        result = mis.parseBoolean();
        assertTrue(result);

        result = mis.parseBoolean();
        assertTrue(result);

        try{
            mis.parseBoolean();
            fail();
        }catch(MmdEofException e){
            // GOOD
        }

        assertEquals(-1, mis.read());

        return;
    }

    /**
     * Test of parseBeShort method, of class MmdInputStream.
     */
    @Test
    public void testParseBeShort() throws Exception {
        System.out.println("parseBeShort");

        MmdInputStream mis;
        ByteArrayInputStream bis;

        bis = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        mis = new MmdInputStream(bis);

        short result;

        result = mis.parseBeShort();
        assertEquals((short)0x0102, result);

        try{
            mis.parseBeShort();
            fail();
        }catch(MmdEofException e){
            // GOOD
        }

        assertEquals(-1, mis.read());

        return;
    }

    /**
     * Test of parseLeShort method, of class MmdInputStream.
     */
    @Test
    public void testParseLeShort() throws Exception {
        System.out.println("parseLeShort");

        MmdInputStream mis;
        ByteArrayInputStream bis;

        bis = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        mis = new MmdInputStream(bis);

        short result;

        result = mis.parseLeShort();
        assertEquals((short)0x0201, result);

        try{
            mis.parseLeShort();
            fail();
        }catch(MmdEofException e){
            // GOOD
        }

        assertEquals(-1, mis.read());

        return;
    }

    /**
     * Test of parseBeInt method, of class MmdInputStream.
     */
    @Test
    public void testParseBeInt() throws Exception {
        System.out.println("parseBeInt");

        MmdInputStream mis;
        ByteArrayInputStream bis;

        bis = new ByteArrayInputStream(
                new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        mis = new MmdInputStream(bis);

        int result;

        result = mis.parseBeInt();
        assertEquals(0x01020304, result);

        try{
            mis.parseBeInt();
            fail();
        }catch(MmdEofException e){
            // GOOD
        }

        assertEquals(-1, mis.read());

        return;
    }

    /**
     * Test of parseLeInt method, of class MmdInputStream.
     */
    @Test
    public void testParseLeInt() throws Exception {
        System.out.println("parseLeInt");

        MmdInputStream mis;
        ByteArrayInputStream bis;

        bis = new ByteArrayInputStream(
                new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        mis = new MmdInputStream(bis);

        int result;

        result = mis.parseLeInt();
        assertEquals(0x04030201, result);

        try{
            mis.parseLeInt();
            fail();
        }catch(MmdEofException e){
            // GOOD
        }

        assertEquals(-1, mis.read());

        return;
    }

    /**
     * Test of parseBeFloat method, of class MmdInputStream.
     */
    @Test
    public void testParseBeFloat() throws Exception {
        System.out.println("parseBeFloat");

        MmdInputStream mis;
        ByteArrayInputStream bis;

        bis = new ByteArrayInputStream(
                new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        mis = new MmdInputStream(bis);

        float result;

        result = mis.parseBeFloat();
        assertEquals(Float.intBitsToFloat(0x01020304), result, 0.0f);

        try{
            mis.parseBeFloat();
            fail();
        }catch(MmdEofException e){
            // GOOD
        }

        assertEquals(-1, mis.read());

        return;
    }

    /**
     * Test of parseLeFloat method, of class MmdInputStream.
     */
    @Test
    public void testParseLeFloat() throws Exception {
        System.out.println("parseLeFloat");

        MmdInputStream mis;
        ByteArrayInputStream bis;

        bis = new ByteArrayInputStream(
                new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        mis = new MmdInputStream(bis);

        float result;

        result = mis.parseLeFloat();
        assertEquals(Float.intBitsToFloat(0x04030201), result, 0.0f);

        try{
            mis.parseLeFloat();
            fail();
        }catch(MmdEofException e){
            // GOOD
        }

        assertEquals(-1, mis.read());

        return;
    }

    /**
     * Test of skipRepeat method, of class MmdInputStream.
     */
    @Test
    public void testSkipRepeat() throws Exception {
        System.out.println("skipRepeat");

        MmdInputStream mis;
        ByteArrayInputStream bis;

        bis = new ByteArrayInputStream(new byte[]{0x11, 0x12, 0x13});
        mis = new MmdInputStream(bis);

        int result;
        long skipped;

        result = mis.read();
        assertEquals(0x11, result);

        skipped = mis.skip(1L);
        assertEquals(1L, skipped);

        result = mis.read();
        assertEquals(0x13, result);

        skipped = mis.skip(1L);
        assertEquals(0L, skipped);

        // TODO: BufferedInputStreamと組み合わせた時の不思議なskip動作

        return;
    }

}
