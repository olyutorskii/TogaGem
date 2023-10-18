/*
 */

package jp.sfjp.mikutoga.pmd.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 *
 */
public class PmdParserBaseTest {

    public PmdParserBaseTest() {
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
