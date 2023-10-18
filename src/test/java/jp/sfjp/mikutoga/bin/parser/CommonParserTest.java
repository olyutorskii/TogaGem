/*
 */

package jp.sfjp.mikutoga.bin.parser;

import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 *
 */
public class CommonParserTest {

    public CommonParserTest() {
    }

    /**
     * Test of getPosition method, of class CommonParser.
     * @throws Exception
     */
    @Test
    public void testGetPosition() throws Exception{
        System.out.println("getPosition");

        CommonParser parser;
        DummyInputStream is;

        is = new DummyInputStream(new byte[100]);
        parser = new CommonParser(is);

        assertEquals(0, parser.getPosition());

        parser.parseByte();
        assertEquals(1, parser.getPosition());

        parser.parseLeInt();
        assertEquals(5, parser.getPosition());

        parser.skip(10);
        assertEquals(15, parser.getPosition());

        return;
    }

    /**
     * Test of hasMore method, of class CommonParser.
     * @throws Exception
     */
    @Test
    public void testHasMore() throws Exception {
        System.out.println("hasMore");

        CommonParser parser;
        DummyInputStream is;

        is = new DummyInputStream(0x00, 0x01, 0x02);
        parser = new CommonParser(is);

        assertTrue(parser.hasMore());

        assertEquals((byte)0x00, parser.parseByte());
        assertTrue(parser.hasMore());

        assertEquals((byte)0x01, parser.parseByte());
        assertTrue(parser.hasMore());

        assertEquals((byte)0x02, parser.parseByte());
        assertFalse(parser.hasMore());

        return;
    }

    /**
     * Test of skip method, of class CommonParser.
     * @throws Exception
     */
    @Test
    public void testSkip() throws Exception {
        System.out.println("skip");

        CommonParser parser;
        DummyInputStream is;

        is = new DummyInputStream(0x00, 0x01, 0x02);
        parser = new CommonParser(is);

        assertEquals((byte)0x00, parser.parseByte());

        parser.skip(1L);
        assertEquals((byte)0x02, parser.parseByte());

        try{
            parser.skip(1L);
            fail();
        }catch(MmdEofException e){
            // GOOD
        }

        return;
    }

    /**
     * Test of parseByteArray method, of class CommonParser.
     * @throws Exception
     */
    @Test
    public void testParseByteArray_3args() throws Exception {
        System.out.println("parseByteArray");

        CommonParser parser;
        DummyInputStream is;

        is = new DummyInputStream(0x01, 0x02, 0x03);
        parser = new CommonParser(is);

        byte[] dst = {
            (byte)0xf1, (byte)0xf2, (byte)0xf3, (byte)0xf4, (byte)0xf5
        };

        parser.parseByteArray(dst, 1, 2);

        assertEquals((byte)0xf1, dst[0]);
        assertEquals((byte)0x01, dst[1]);
        assertEquals((byte)0x02, dst[2]);
        assertEquals((byte)0xf4, dst[3]);
        assertEquals((byte)0xf5, dst[4]);

        return;
    }

    /**
     * Test of parseByteArray method, of class CommonParser.
     * @throws Exception
     */
    @Test
    public void testParseByteArray_byteArr() throws Exception {
        System.out.println("parseByteArray");

        CommonParser parser;
        DummyInputStream is;

        is = new DummyInputStream(0x01, 0x02, 0x03);
        parser = new CommonParser(is);

        byte[] dst = {
            (byte)0xf1, (byte)0xf2
        };

        parser.parseByteArray(dst);

        assertEquals((byte)0x01, dst[0]);
        assertEquals((byte)0x02, dst[1]);

        return;
    }

    /**
     * Test of parseByte method, of class CommonParser.
     * @throws Exception
     */
    @Test
    public void testParseByte() throws Exception {
        System.out.println("parseByte");

        CommonParser parser;
        DummyInputStream is;

        is = new DummyInputStream(0x01, 0x02, 0x03);
        parser = new CommonParser(is);

        assertEquals((byte)0x01, parser.parseByte());
        assertEquals((byte)0x02, parser.parseByte());
        assertEquals((byte)0x03, parser.parseByte());

        try{
            parser.parseByte();
            fail();
        }catch(MmdEofException e){
            // GOOD
        }

        return;
    }

    /**
     * Test of parseUByteAsInt method, of class CommonParser.
     * @throws Exception
     */
    @Test
    public void testParseUByteAsInt() throws Exception {
        System.out.println("parseUByteAsInt");

        CommonParser parser;
        DummyInputStream is;

        is = new DummyInputStream(0x00, 0x01, 0xff);
        parser = new CommonParser(is);

        assertEquals(0x00, parser.parseUByteAsInt());
        assertEquals(0x01, parser.parseUByteAsInt());
        assertEquals(0xff, parser.parseUByteAsInt());

        try{
            parser.parseUByteAsInt();
            fail();
        }catch(MmdEofException e){
            // GOOD
        }

        return;
    }

    /**
     * Test of parseBoolean method, of class CommonParser.
     * @throws Exception
     */
    @Test
    public void testParseBoolean() throws Exception {
        System.out.println("parseBoolean");

        CommonParser parser;
        DummyInputStream is;

        is = new DummyInputStream(0x00, 0x01, 0x02, 0xff);
        parser = new CommonParser(is);

        assertFalse(parser.parseBoolean());
        assertTrue(parser.parseBoolean());
        assertTrue(parser.parseBoolean());
        assertTrue(parser.parseBoolean());

        try{
            parser.parseBoolean();
            fail();
        }catch(MmdEofException e){
            // GOOD
        }

        return;
    }

    /**
     * Test of parseLeShort method, of class CommonParser.
     * @throws Exception
     */
    @Test
    public void testParseLeShort() throws Exception {
        System.out.println("parseLeShort");

        CommonParser parser;
        DummyInputStream is;

        is = new DummyInputStream(0xfe, 0xff, 0x01, 0x00, 0x80);
        parser = new CommonParser(is);

        assertEquals((short)-2, parser.parseLeShort());
        assertEquals((short)1, parser.parseLeShort());

        try{
            parser.parseLeShort();
            fail();
        }catch(MmdEofException e){
            // GOOD
        }

        return;
    }

    /**
     * Test of parseLeUShortAsInt method, of class CommonParser.
     * @throws Exception
     */
    @Test
    public void testParseLeUShortAsInt() throws Exception {
        System.out.println("parseLeUShortAsInt");

        CommonParser parser;
        DummyInputStream is;

        is = new DummyInputStream(0xfe, 0xff, 0x01, 0x00, 0x80);
        parser = new CommonParser(is);

        assertEquals(0xfffe, parser.parseLeUShortAsInt());
        assertEquals(1, parser.parseLeUShortAsInt());

        try{
            parser.parseLeUShortAsInt();
            fail();
        }catch(MmdEofException e){
            // GOOD
        }

        return;
    }

    /**
     * Test of parseLeInt method, of class CommonParser.
     * @throws Exception
     */
    @Test
    public void testParseLeInt() throws Exception {
        System.out.println("parseLeInt");

        CommonParser parser;
        DummyInputStream is;

        is = new DummyInputStream(
                0xfe, 0xff, 0xff, 0xff,
                0x78, 0x56, 0x34, 0x12,
                0x7f );
        parser = new CommonParser(is);

        assertEquals(-2, parser.parseLeInt());
        assertEquals(0x12345678, parser.parseLeInt());

        try{
            parser.parseLeInt();
            fail();
        }catch(MmdEofException e){
            // GOOD
        }

        return;
    }

    /**
     * Test of parseLeFloat method, of class CommonParser.
     * @throws Exception
     */
    @Test
    public void testParseLeFloat() throws Exception {
        System.out.println("parseLeFloat");

        CommonParser parser;
        DummyInputStream is;

        is = new DummyInputStream(
                0x00, 0x00, 0xc0, 0xbf,
                0x78, 0x56, 0x34, 0x12,
                0x7f );
        parser = new CommonParser(is);

        assertEquals(-1.5f, parser.parseLeFloat(), 0.0);
        assertEquals(
                Float.intBitsToFloat(0x12345678), parser.parseLeFloat(),
                0.0 );

        try{
            parser.parseLeFloat();
            fail();
        }catch(MmdEofException e){
            // GOOD
        }

        return;
    }

    /**
     * Test of parseString method, of class CommonParser.
     * @throws Exception
     */
    @Test
    public void testParseString() throws Exception {
        System.out.println("parseString");

        CommonParser parser;
        DummyInputStream is;
        TextDecoder decoder;

        decoder = new TextDecoder(Charset.forName("Shift_JIS"));

        is = new DummyInputStream(0x82, 0xa0, 0x82, 0xa2, 0x46);
        parser = new CommonParser(is);

        assertEquals("あ", parser.parseString(decoder, 2));
        assertEquals("い", parser.parseString(decoder, 2));

        try{
            parser.parseString(decoder, 2);
            fail();
        }catch(MmdEofException e){
            // GOOD
        }

        is = new DummyInputStream(0x82, 0xa0, 0x82, 0xa2);
        parser = new CommonParser(is);

        assertEquals("あい", parser.parseString(decoder, 4));

        is = new DummyInputStream(0x82, 0xa0, 0x82, 0xff);
        parser = new CommonParser(is);

        try{
            parser.parseString(decoder, 4);
            fail();
        }catch(MmdFormatException e){
//          assertEquals("unmapped character(position:2)", e.getMessage());
            // GOOD
        }

        is = new DummyInputStream(0x82, 0xa0, 0x82);
        parser = new CommonParser(is);

        try{
            parser.parseString(decoder, 3);
            fail();
        }catch(MmdFormatException e){
//          assertEquals("illegal character encoding(position:1)", e.getMessage());
            // GOOD
        }

        is = new DummyInputStream(0x41, 0x42, 0x43);
        parser = new CommonParser(is);

        assertEquals("A", parser.parseString(decoder, 1));
        assertEquals("BC", parser.parseString(decoder, 2));

        return;
    }

}
