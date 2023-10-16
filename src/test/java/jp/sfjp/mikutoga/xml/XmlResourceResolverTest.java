/*
 */

package jp.sfjp.mikutoga.xml;

import java.net.URI;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 */
public class XmlResourceResolverTest {

    public XmlResourceResolverTest() {
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
     * Test of buildBaseRelativeURI method, of class XmlResourceResolver.
     */
    @Test
    public void testBuildBaseRelativeURI() throws Exception {
        System.out.println("buildBaseRelativeURI");

        URI result;

        result = XmlResourceResolver.buildBaseRelativeURI("http://example.com", "/a");
        assertEquals("http://example.com/a", result.toASCIIString());

        result = XmlResourceResolver.buildBaseRelativeURI("http://example.com/", "a");
        assertEquals("http://example.com/a", result.toASCIIString());

        result = XmlResourceResolver.buildBaseRelativeURI("http://example.com/", "/a");
        assertEquals("http://example.com/a", result.toASCIIString());

        result = XmlResourceResolver.buildBaseRelativeURI("http://example.com/a", "/b");
        assertEquals("http://example.com/b", result.toASCIIString());

        result = XmlResourceResolver.buildBaseRelativeURI("http://example.com/a/", "b");
        assertEquals("http://example.com/a/b", result.toASCIIString());

        result = XmlResourceResolver.buildBaseRelativeURI("http://example.com/a/", "/b");
        assertEquals("http://example.com/b", result.toASCIIString());

        result = XmlResourceResolver.buildBaseRelativeURI("http://example.com/a", "http://example.org/b");
        assertEquals("http://example.org/b", result.toASCIIString());

        result = XmlResourceResolver.buildBaseRelativeURI(null, "http://example.org/b");
        assertEquals("http://example.org/b", result.toASCIIString());

        result = XmlResourceResolver.buildBaseRelativeURI("http://example.com/a", null);
        assertEquals("http://example.com/", result.toASCIIString());

        result = XmlResourceResolver.buildBaseRelativeURI("http://example.com/a/b/", "../c");
        assertEquals("http://example.com/a/c", result.toASCIIString());

        try{
            XmlResourceResolver.buildBaseRelativeURI("a/b/", "c/d");
            fail();
        }catch(IllegalArgumentException e){
            assert true;
        }

        try{
            XmlResourceResolver.buildBaseRelativeURI(null, "c/d");
            fail();
        }catch(IllegalArgumentException e){
            assert true;
        }

        return;
    }

}
