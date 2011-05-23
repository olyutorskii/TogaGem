/*
 */

package jp.sourceforge.mikutoga.parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class TextDecoderTest {

    private static final Charset CS_WIN31J = Charset.forName("windows-31j");
    private static final Charset CS_UTF8 = Charset.forName("UTF-8");
    private static final Charset CS_UTF16LE = Charset.forName("UTF-16LE");

    public TextDecoderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
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
     * Test of prepareBuffer method, of class TextDecoder.
     */
    @Test
    public void testPrepareBuffer() {
        System.out.println("prepareBuffer");
        return;
    }

    /**
     * Test of parseString method, of class TextDecoder.
     */
    @Test
    public void testParseString() throws Exception {
        System.out.println("parseString");

        TextDecoder decoder;
        byte[] bdata;
        InputStream istream;
        MmdSource source;
        CharBuffer cb;

        decoder = new TextDecoder(CS_WIN31J);

        bdata = byteArray("41:42");
        istream = new ByteArrayInputStream(bdata);
        source = new MmdSource(istream);
        cb =decoder.parseString(source, 2);
        assertEquals("AB", cb.toString());

        istream = new ByteArrayInputStream(bdata);
        source = new MmdSource(istream);
        cb =decoder.parseString(source, 1);
        assertEquals("A", cb.toString());

        bdata = byteArray("88:9F");
        istream = new ByteArrayInputStream(bdata);
        source = new MmdSource(istream);
        cb =decoder.parseString(source, 2);
        assertEquals("亜", cb.toString());

        bdata = byteArray("88:9F:88:A0");
        istream = new ByteArrayInputStream(bdata);
        source = new MmdSource(istream);
        cb =decoder.parseString(source, 4);
        assertEquals("亜唖", cb.toString());

        bdata = byteArray("88:9F:41:88:A0");
        istream = new ByteArrayInputStream(bdata);
        source = new MmdSource(istream);
        cb =decoder.parseString(source, 5);
        assertEquals("亜A唖", cb.toString());

        bdata = byteArray("88:9F:88:A0");
        istream = new ByteArrayInputStream(bdata);
        source = new MmdSource(istream);
        try{
            cb =decoder.parseString(source, 5);
            fail();
        }catch(MmdEofException e){
            // OK
        }

        bdata = byteArray("88:9F:88:A0");
        istream = new ByteArrayInputStream(bdata);
        source = new MmdSource(istream);
        try{
            cb =decoder.parseString(source, 3);
            fail();
        }catch(MmdFormatException e){
            // OK
        }

        return;
    }

}
