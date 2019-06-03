/*
 */
package jp.sfjp.mikutoga.xml;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class DatatypeIoTest {

    public DatatypeIoTest() {
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
     * Test of printInt method, of class DatatypeIo.
     */
    @Test
    public void testPrintInt() {
        System.out.println("printInt");

        CharSequence result;

        result = DatatypeIo.printInt(0);
        assertEquals("0", result.toString());

        result = DatatypeIo.printInt(1);
        assertEquals("1", result.toString());

        result = DatatypeIo.printInt(-1);
        assertEquals("-1", result.toString());

        result = DatatypeIo.printInt(999);
        assertEquals("999", result.toString());

        result = DatatypeIo.printInt(-9999);
        assertEquals("-9999", result.toString());

        result = DatatypeIo.printInt(Integer.MIN_VALUE);
        assertEquals("-2147483648", result.toString());

        result = DatatypeIo.printInt(Integer.MAX_VALUE);
        assertEquals("2147483647", result.toString());

        return;
    }

    /**
     * Test of printFloat method, of class DatatypeIo.
     */
    @Test
    public void testPrintFloat() {
        System.out.println("printFloat");

        CharSequence result;

        result = DatatypeIo.printFloat(0.0f);
        assertEquals("0.0", result.toString());

        result = DatatypeIo.printFloat(-0.0f);
        assertEquals("-0.0", result.toString());

        result = DatatypeIo.printFloat(1.0f);
        assertEquals("1.0", result.toString());

        result = DatatypeIo.printFloat(-1.0f);
        assertEquals("-1.0", result.toString());

        result = DatatypeIo.printFloat((float)StrictMath.E);
        assertEquals("2.7182817", result.toString());

        result = DatatypeIo.printFloat((float)StrictMath.PI);
        assertEquals("3.1415927", result.toString());

        result = DatatypeIo.printFloat(0.001f);
        assertEquals("0.001", result.toString());

        result = DatatypeIo.printFloat(StrictMath.nextDown(0.001f));
        assertEquals("9.999999E-4", result.toString());

        result = DatatypeIo.printFloat(10_000_000.0f);
        assertEquals("1.0E7", result.toString());

        result = DatatypeIo.printFloat(StrictMath.nextDown(10_000_000.0f));
        assertEquals("9999999.0", result.toString());

        result = DatatypeIo.printFloat(Float.POSITIVE_INFINITY);
        assertEquals("INF", result.toString());

        result = DatatypeIo.printFloat(Float.NEGATIVE_INFINITY);
        assertEquals("-INF", result.toString());

        result = DatatypeIo.printFloat(Float.NaN);
        assertEquals("NaN", result.toString());

        result = DatatypeIo.printFloat(Float.MIN_VALUE);
        assertEquals("1.4E-45", result.toString());

        result = DatatypeIo.printFloat(Float.MIN_NORMAL);
        assertEquals("1.17549435E-38", result.toString());

        result = DatatypeIo.printFloat(Float.MAX_VALUE);
        assertEquals("3.4028235E38", result.toString());

        return;
    }

    /**
     * Test of parseBoolean method, of class DatatypeIo.
     */
    @Test
    public void testParseBoolean() {
        System.out.println("parseBoolean");

        assertFalse(DatatypeIo.parseBoolean("0"));
        assertFalse(DatatypeIo.parseBoolean("false"));
        assertTrue(DatatypeIo.parseBoolean("1"));
        assertTrue(DatatypeIo.parseBoolean("true"));

        assertTrue(DatatypeIo.parseBoolean("\n\rtrue\u0020\t"));

        try{
            DatatypeIo.parseBoolean("yes");
            fail();
        }catch(IllegalArgumentException e){
            assert true;
        }

        try{
            DatatypeIo.parseBoolean("");
            fail();
        }catch(IllegalArgumentException e){
            assert true;
        }

        return;
    }

    /**
     * Test of parseInt method, of class DatatypeIo.
     */
    @Test
    public void testParseInt() {
        System.out.println("parseInt");

        int result;

        result = DatatypeIo.parseInt("0");
        assertEquals(0, result);

        result = DatatypeIo.parseInt("+0");
        assertEquals(0, result);

        result = DatatypeIo.parseInt("-0");
        assertEquals(0, result);

        result = DatatypeIo.parseInt("1");
        assertEquals(1, result);

        result = DatatypeIo.parseInt("+1");
        assertEquals(1, result);

        result = DatatypeIo.parseInt("-1");
        assertEquals(-1, result);

        result = DatatypeIo.parseInt("999");
        assertEquals(999, result);

        result = DatatypeIo.parseInt("-9999");
        assertEquals(-9999, result);

        result = DatatypeIo.parseInt("-2147483648");
        assertEquals(Integer.MIN_VALUE, result);

        result = DatatypeIo.parseInt("2147483647");
        assertEquals(Integer.MAX_VALUE, result);

        result = DatatypeIo.parseInt("\n\r999\u0020\t");
        assertEquals(999, result);

        try{
            DatatypeIo.parseInt("?");
            fail();
        }catch(NumberFormatException e){
            assert true;
        }

        try{
            DatatypeIo.parseInt("-2147483649");
            fail();
        }catch(NumberFormatException e){
            assert true;
        }

        try{
            DatatypeIo.parseInt("2147483648");
            fail();
        }catch(NumberFormatException e){
            assert true;
        }

        try{
            DatatypeIo.parseInt("3.14");
            fail();
        }catch(NumberFormatException e){
            assert true;
        }

        return;
    }

    /**
     * Test of parseFloat method, of class DatatypeIo.
     */
    @Test
    public void testParseFloat() {
        System.out.println("parseFloat");

        float result;

        result = DatatypeIo.parseFloat("0.0");
        assertEquals(0.0f, result, 0.0f);

        result = DatatypeIo.parseFloat("+0.0");
        assertEquals(0.0f, result, 0.0f);

        result = DatatypeIo.parseFloat("-0.0");
        assertEquals(-0.0f, result, 0.0f);

        result = DatatypeIo.parseFloat("-123.45");
        assertEquals(-123.45f, result, 0.0f);

        result = DatatypeIo.parseFloat("654.32");
        assertEquals(654.32f, result, 0.0f);

        result = DatatypeIo.parseFloat("2.718281828459045");
        assertEquals((float)StrictMath.E, result, 0.0f);

        result = DatatypeIo.parseFloat("3.141592653589793");
        assertEquals((float)StrictMath.PI, result, 0.0f);

        result = DatatypeIo.parseFloat("1.401298464324817E-45");
        assertEquals(Float.MIN_VALUE, result, 0.0f);

        result = DatatypeIo.parseFloat("1.1754943508222875E-38");
        assertEquals(Float.MIN_NORMAL, result, 0.0f);

        result = DatatypeIo.parseFloat("3.4028234663852886E38");
        assertEquals(Float.MAX_VALUE, result, 0.0f);

        result = DatatypeIo.parseFloat("2E3");
        assertEquals(2000.0f, result, 0.0f);

        result = DatatypeIo.parseFloat("2.3E4");
        assertEquals(23000.0f, result, 0.0f);

        result = DatatypeIo.parseFloat("2.3e4");
        assertEquals(23000.0f, result, 0.0f);

        result = DatatypeIo.parseFloat("2.3E+4");
        assertEquals(23000.0f, result, 0.0f);

        result = DatatypeIo.parseFloat("2.3E-4");
        assertEquals(0.00023f, result, 0.0f);

        result = DatatypeIo.parseFloat("INF");
        assertEquals(Float.POSITIVE_INFINITY, result, 0.0f);

        result = DatatypeIo.parseFloat("-INF");
        assertEquals(Float.NEGATIVE_INFINITY, result, 0.0f);

        try{
            DatatypeIo.parseFloat("+INF");
            fail();
        }catch(NumberFormatException e){
            assert true;
        }

        try{
            DatatypeIo.parseFloat("Infinity");
            fail();
        }catch(NumberFormatException e){
            assert true;
        }

        result = DatatypeIo.parseFloat("NaN");
        assertTrue(Float.isNaN(result));

        result = DatatypeIo.parseFloat("\n\r1.2\u0020\t");
        assertEquals(1.2f, result, 0.0f);

        try{
            DatatypeIo.parseFloat("?");
            fail();
        }catch(NumberFormatException e){
            assert true;
        }

        try{
            DatatypeIo.parseFloat("0x1.0p0");
            fail();
        }catch(NumberFormatException e){
            assert true;
        }

        return;
    }

    /**
     * Test of trim method, of class DatatypeIo.
     */
    @Test
    public void testTrim() {
        System.out.println("trim");

        CharSequence txt;
        CharSequence result;

        txt = "abc";
        result = DatatypeIo.xsdTrim(txt);
        assertEquals("abc", result.toString());

        txt = "";
        result = DatatypeIo.xsdTrim(txt);
        assertEquals("", result.toString());

        txt = "  abc   ";
        result = DatatypeIo.xsdTrim(txt);
        assertEquals("abc", result.toString());

        txt = "  abc";
        result = DatatypeIo.xsdTrim(txt);
        assertEquals("abc", result.toString());

        txt = "abc  ";
        result = DatatypeIo.xsdTrim(txt);
        assertEquals("abc", result.toString());

        txt = " a b c ";
        result = DatatypeIo.xsdTrim(txt);
        assertEquals("a b c", result.toString());

        txt = "\n\rabc\u0020\t";
        result = DatatypeIo.xsdTrim(txt);
        assertEquals("abc", result.toString());

        return;
    }

    /**
     * Test of isWhitespace method, of class DatatypeIo.
     */
    @Test
    public void testIsWhitespace() {
        System.out.println("isWhitespace");

        assertTrue(DatatypeIo.isXsdWhitespace('\u0020'));
        assertTrue(DatatypeIo.isXsdWhitespace('\n'));
        assertTrue(DatatypeIo.isXsdWhitespace('\r'));
        assertTrue(DatatypeIo.isXsdWhitespace('\t'));
        assertFalse(DatatypeIo.isXsdWhitespace('X'));
        assertFalse(DatatypeIo.isXsdWhitespace('\u0001'));

        return;
    }

}
