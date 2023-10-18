/*
 */

package jp.sfjp.mikutoga.bin.parser;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 *
 */
public class TextDecoderTest {

    private static final Charset CS_WIN31J = Charset.forName("windows-31j");
    private static final Charset CS_UTF8 = Charset.forName("UTF-8");
    private static final Charset CS_UTF16LE = Charset.forName("UTF-16LE");

    public TextDecoderTest() {
    }

    public static byte[] byteArray(CharSequence seq){
        byte[] result;

        List<Byte> byteList = new ArrayList<Byte>();

        int length = seq.length();
        for(int pos = 0; pos < length; pos++){
            int val = 0;

            char ch = seq.charAt(pos);

            if('0' <= ch && ch <= '9'){
                val += ch - '0';
            }else if('a' <= ch && ch <= 'f'){
                val += ch - 'a' + 10;
            }else if('A' <= ch && ch <= 'F'){
                val += ch - 'A' + 10;
            }else{
                continue;
            }

            pos++;
            if(pos >= length) break;

            val *= 16;
            ch = seq.charAt(pos);

            if('0' <= ch && ch <= '9'){
                val += ch - '0';
            }else if('a' <= ch && ch <= 'f'){
                val += ch - 'a' + 10;
            }else if('A' <= ch && ch <= 'F'){
                val += ch - 'A' + 10;
            }else{
                continue;
            }

            byteList.add((byte)val);
        }

        result = new byte[byteList.size()];

        for(int pos = 0; pos < result.length; pos++){
            result[pos] = byteList.get(pos);
        }

        return result;
    }

    /**
     * Test of setChopMode, getChopMode method, of class TextDecoder.
     * @throws Exception
     */
    @Test
    public void testChopMode() throws Exception {
        System.out.println("chopMode");

        TextDecoder decoder;

        decoder = new TextDecoder(CS_WIN31J);
        assertFalse(decoder.isZeroChopMode());

        decoder.setZeroChopMode(true);
        assertTrue(decoder.isZeroChopMode());

        decoder.setZeroChopMode(false);
        assertFalse(decoder.isZeroChopMode());

        return;
    }

    /**
     * Test of parseString method, of class TextDecoder.
     * @throws Exception
     */
    @Test
    public void testParseStringChop() throws Exception {
        System.out.println("parseString(Chop)");

        TextDecoder decoder;

        decoder = new TextDecoder(CS_WIN31J);
        decoder.setZeroChopMode(true);

        assertDecoded("41:42:00", "AB", decoder);
        assertDecoded("41:00:42", "A", decoder);
        assertDecoded("00:41:42", "", decoder);
        assertDecoded("41:00:88", "A", decoder);
        assertDecoded("", "", decoder);

        decoder.setZeroChopMode(false);
        assertDecoded("41:00:42", "A\u0000B", decoder);
        assertDecoded("", "", decoder);

        return;
    }

    /**
     * Test of parseString method, of class TextDecoder.
     * @throws Exception
     */
    @Test
    public void testParseStringWin31J() throws Exception {
        System.out.println("parseString(Win31J)");

        TextDecoder decoder;

        decoder = new TextDecoder(CS_WIN31J);

        assertDecoded("41:42", "AB", decoder);
        assertDecoded("41:42", "A", decoder, 1);
        assertDecoded("88:9F", "亜", decoder);
        assertDecoded("88:9F:88:A0", "亜唖", decoder);
        assertDecoded("88:9F:41:88:A0", "亜A唖", decoder);
        assertDecoded("00", "\u0000", decoder);

        assertFormatError("88:9F:88:A0", decoder, 3);

        return;
    }

    /**
     * Test of parseString method, of class TextDecoder.
     * @throws Exception
     */
    @Test
    public void testParseStringUTF8() throws Exception {
        System.out.println("parseString(UTF8)");

        TextDecoder decoder;

        decoder = new TextDecoder(CS_UTF8);

        assertDecoded("41:42", "AB", decoder);
        assertDecoded("41:42", "A", decoder, 1);
        assertDecoded("E4:BA:9C", "亜", decoder);
        assertDecoded("E4:BA:9C:E5:94:96", "亜唖", decoder);
        assertDecoded("E4:BA:9C:41:E5:94:96", "亜A唖", decoder);
        assertDecoded("00", "\u0000", decoder);
        assertDecoded("EF:BF:BF", "\uffff", decoder);

        assertFormatError("E4:BA:9C:E5:94:96", decoder, 5);

        return;
    }

    /**
     * Test of parseString method, of class TextDecoder.
     * @throws Exception
     */
    @Test
    public void testParseStringUTF16LE() throws Exception {
        System.out.println("parseString(UTF16LE)");

        TextDecoder decoder;

        decoder = new TextDecoder(CS_UTF16LE);

        assertDecoded("41:00:42:00", "AB", decoder);
        assertDecoded("41:00:42:00", "A", decoder, 2);
        assertDecoded("9C:4E", "亜", decoder);
        assertDecoded("9C:4E:16:55", "亜唖", decoder);
        assertDecoded("9C:4E:41:00:16:55", "亜A唖", decoder);
        assertDecoded("00:00", "\u0000", decoder);
        assertDecoded("FF:FF", "\uffff", decoder);

        assertDecoded("60:08", "\u0860", decoder);

        assertDecoded("FF:FE:9C:4E", "\ufeff亜", decoder);
        // not BOM, ZERO WIDTH NO-BREAK SPACE

        assertFormatError("9C:4E:16:55", decoder, 3);

        return;
    }

    /**
     * Test of Yen(U+00A5) & Backslash(U+005C) encoding, of class TextDecoder.
     * @throws Exception
     */
    @Test
    public void testYenAndBackslash() throws Exception {
        System.out.println("Yen & Backslash");

        TextDecoder decoder;

        decoder = new TextDecoder(CS_WIN31J);
        assertDecoded("5C", "\u005c\u005c", decoder);

        decoder = new TextDecoder(CS_UTF8);
        assertDecoded("5C", "\u005c\u005c", decoder);
        assertDecoded("C2:A5", "\u00a5", decoder);

        decoder = new TextDecoder(CS_UTF16LE);
        assertDecoded("5C:00", "\u005c\u005c", decoder);
        assertDecoded("A5:00", "\u00a5", decoder);

        return;
    }

    /**
     * Test of unmapped char, of class TextDecoder.
     * @throws Exception
     */
    @Test
    public void testUnmapChar() throws Exception {
        System.out.println("unmap char");

        TextDecoder decoder;

        decoder = new TextDecoder(CS_WIN31J);
        assertFormatError("FF:FF", decoder, 2);


        // Unicode2.0の時点でU+0860は未定義文字

        decoder = new TextDecoder(CS_UTF8);
        assertFormatError("FF:FF:FF", decoder, 3);
        assertDecoded("E0:A1:A0", "\u0860", decoder);

        decoder = new TextDecoder(CS_UTF16LE);
        assertDecoded("60:08", "\u0860", decoder);

        return;
    }

    public void assertDecoded(String bin, String desired,
                                TextDecoder decoder)
            throws Exception{
        byte[] bdata = byteArray(bin);
        ByteBuffer bBuf = ByteBuffer.wrap(bdata);
        assertDecoded(bBuf, desired, decoder);
        return;
    }

    public void assertDecoded(String bin, String desired,
                                TextDecoder decoder, int len)
            throws Exception{
        byte[] bdata = byteArray(bin);
        ByteBuffer bBuf = ByteBuffer.wrap(bdata, 0, len);
        assertDecoded(bBuf, desired, decoder);
        return;
    }

    public void assertDecoded(ByteBuffer bBuf, String desired,
                                TextDecoder decoder)
            throws Exception{
        String result;
        result = decoder.decode(bBuf);

        assertEquals(desired, result);

        return;
    }

    public void assertFormatError(String bin,
                                    TextDecoder decoder, int len)
            throws Exception{
        byte[] bdata = byteArray(bin);
        ByteBuffer bBuf = ByteBuffer.wrap(bdata, 0, len);

        try{
            decoder.decode(bBuf);
            fail();
        }catch(CharacterCodingException e){
            // OK
        }

        return;
    }

}
