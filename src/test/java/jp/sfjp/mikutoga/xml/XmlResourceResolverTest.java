/*
 */

package jp.sfjp.mikutoga.xml;

import java.net.URI;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 */
public class XmlResourceResolverTest {

    public XmlResourceResolverTest() {
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
