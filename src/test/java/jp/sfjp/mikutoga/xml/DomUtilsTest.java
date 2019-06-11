/*
@see https://www.w3.org/TR/xmlschema-2/
 */

package jp.sfjp.mikutoga.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 */
public class DomUtilsTest {

    private static final DocumentBuilderFactory FACTORY =
            DocumentBuilderFactory.newInstance();
    private static final DocumentBuilder BUILDER;

    private static final String TESTELEM = "testelem";
    private static final String TESTATTR = "testattr";

    private static Element getTestAttredElem(String attrVal){
        Document doc = BUILDER.newDocument();
        Element elem = doc.createElement(TESTELEM);
        elem.setAttribute(TESTATTR, attrVal);
        return elem;
    }

    static{
        try{
            BUILDER = FACTORY.newDocumentBuilder();
        }catch(ParserConfigurationException e){
            throw new ExceptionInInitializerError(e);
        }
    }

    public DomUtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws ParserConfigurationException{
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
     * Test of getBooleanAttr method, of class DomUtils.
     */
    @Test
    public void testGetBooleanAttr() throws Exception {
        System.out.println("getBooleanAttr");

        boolean result;
        Element elem;

        elem = getTestAttredElem("true");
        result = DomUtils.getBooleanAttr(elem, TESTATTR);
        assertTrue(result);

        elem = getTestAttredElem("false");
        result = DomUtils.getBooleanAttr(elem, TESTATTR);
        assertFalse(result);

        elem = getTestAttredElem("0");
        result = DomUtils.getBooleanAttr(elem, TESTATTR);
        assertFalse(result);

        elem = getTestAttredElem("1");
        result = DomUtils.getBooleanAttr(elem, TESTATTR);
        assertTrue(result);

        elem = getTestAttredElem("\n\rtrue\u0020\t");
        result = DomUtils.getBooleanAttr(elem, TESTATTR);
        assertTrue(result);

        elem = getTestAttredElem("?");
        try{
            DomUtils.getBooleanAttr(elem, TESTATTR);
            fail();
        }catch(TogaXmlException e){
            assert true;
        }

        return;
    }

    /**
     * Test of getIntegerAttr method, of class DomUtils.
     */
    @Test
    public void testGetIntegerAttr() throws TogaXmlException {
        System.out.println("getIntegerAttr");

        int result;
        Element elem;

        elem = getTestAttredElem("0");
        result = DomUtils.getIntegerAttr(elem, TESTATTR);
        assertEquals(0, result);

        elem = getTestAttredElem("1");
        result = DomUtils.getIntegerAttr(elem, TESTATTR);
        assertEquals(1, result);

        elem = getTestAttredElem("-1");
        result = DomUtils.getIntegerAttr(elem, TESTATTR);
        assertEquals(-1, result);

        elem = getTestAttredElem("999");
        result = DomUtils.getIntegerAttr(elem, TESTATTR);
        assertEquals(999, result);

        elem = getTestAttredElem("-9999");
        result = DomUtils.getIntegerAttr(elem, TESTATTR);
        assertEquals(-9999, result);

        elem = getTestAttredElem("\n\r999\u0020\t");
        result = DomUtils.getIntegerAttr(elem, TESTATTR);
        assertEquals(999, result);

        elem = getTestAttredElem("?");
        try{
            result = DomUtils.getIntegerAttr(elem, TESTATTR);
            fail();
        }catch(TogaXmlException e){
            assert true;
        }

        return;
    }

    /**
     * Test of getFloatAttr method, of class DomUtils.
     */
    @Test
    public void testGetFloatAttr() throws TogaXmlException {
        System.out.println("getFloatAttr");

        float result;
        Element elem;

        elem = getTestAttredElem("0.0");
        result = DomUtils.getFloatAttr(elem, TESTATTR);
        assertEquals(0.0f, result, 0.0f);

        elem = getTestAttredElem("-0.0");
        result = DomUtils.getFloatAttr(elem, TESTATTR);
        assertEquals(0.0f, result, 0.0f);
        assertEquals("-0.0", Float.toString(result));

        elem = getTestAttredElem("-123.456");
        result = DomUtils.getFloatAttr(elem, TESTATTR);
        assertEquals(-123.456f, result, 0.0f);

        elem = getTestAttredElem("654.321");
        result = DomUtils.getFloatAttr(elem, TESTATTR);
        assertEquals(654.321f, result, 0.0f);

        elem = getTestAttredElem("2.3E4");
        result = DomUtils.getFloatAttr(elem, TESTATTR);
        assertEquals(23000.0f, result, 0.0f);

        elem = getTestAttredElem("INF");
        result = DomUtils.getFloatAttr(elem, TESTATTR);
        assertEquals(Float.POSITIVE_INFINITY, result, 0.0f);

        elem = getTestAttredElem("+INF");
        try{
            DomUtils.getFloatAttr(elem, TESTATTR);
            fail();
        }catch(TogaXmlException e){
            assert true;
        }

        elem = getTestAttredElem("NaN");
        result = DomUtils.getFloatAttr(elem, TESTATTR);
        assertTrue(Float.isNaN(result));

        elem = getTestAttredElem("\n\r123.456\u0020\t");
        result = DomUtils.getFloatAttr(elem, TESTATTR);
        assertEquals(123.456f, result, 0.0f);

        elem = getTestAttredElem("?");
        try{
            DomUtils.getFloatAttr(elem, TESTATTR);
            fail();
        }catch(TogaXmlException e){
            assert true;
        }

        return;
    }

}