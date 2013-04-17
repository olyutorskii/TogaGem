/*
 */

package jp.sfjp.mikutoga.pmd.parser;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class PmdParserBaseTest {

    public PmdParserBaseTest() {
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
     * Test of chopLastLF method, of class PmdParserBase.
     */
    @Test
    public void testChopLastLF() {
        System.out.println("chopLastLF");

        assertEquals("", PmdParserBase.chopLastLF(""));
        assertEquals("abc", PmdParserBase.chopLastLF("abc"));
        assertEquals("abc\r", PmdParserBase.chopLastLF("abc\r"));
        assertEquals("abc", PmdParserBase.chopLastLF("abc\n"));
        assertEquals("abc\r", PmdParserBase.chopLastLF("abc\r\n"));
        assertEquals("abc\nxyz", PmdParserBase.chopLastLF("abc\nxyz"));
        assertEquals("abc\r\nxyz", PmdParserBase.chopLastLF("abc\r\nxyz"));
        assertEquals("\r", PmdParserBase.chopLastLF("\r"));
        assertEquals("", PmdParserBase.chopLastLF("\n"));
        assertEquals("\r", PmdParserBase.chopLastLF("\r\n"));

        return;
    }

}
