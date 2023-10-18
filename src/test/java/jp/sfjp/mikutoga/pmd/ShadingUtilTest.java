/*
 */

package jp.sfjp.mikutoga.pmd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 *
 */
public class ShadingUtilTest {

    public ShadingUtilTest() {
    }

    /**
     * Test of isSpheremapFile method, of class ShadingUtil.
     */
    @Test
    public void testIsSpheremapFile() {
        System.out.println("isSpheremapFile");

        assertFalse(ShadingUtil.isSpheremapFile(""));
        assertFalse(ShadingUtil.isSpheremapFile("a"));
        assertFalse(ShadingUtil.isSpheremapFile("a.png"));
        assertTrue(ShadingUtil.isSpheremapFile("a.sph"));
        assertTrue(ShadingUtil.isSpheremapFile("a.spa"));
        assertTrue(ShadingUtil.isSpheremapFile(".sph"));
        assertTrue(ShadingUtil.isSpheremapFile(".spa"));
        assertFalse(ShadingUtil.isSpheremapFile("sph"));
        assertFalse(ShadingUtil.isSpheremapFile("spa"));
        assertFalse(ShadingUtil.isSpheremapFile("a.SPH"));
        assertFalse(ShadingUtil.isSpheremapFile("a.SPA"));

        return;
    }

    /**
     * Test of splitShadingFileInfo method, of class PmdParserBase.
     */
    @Test
    public void testSplitShadingFileInfo() {
        System.out.println("splitShadingFileInfo");

        String[] result;

        result = ShadingUtil.splitShadingFileInfo("");
        assertEquals(2, result.length);
        assertEquals("", result[0]);
        assertEquals("", result[1]);

        result = ShadingUtil.splitShadingFileInfo("a");
        assertEquals(2, result.length);
        assertEquals("a", result[0]);
        assertEquals("", result[1]);

        result = ShadingUtil.splitShadingFileInfo("a.sph");
        assertEquals(2, result.length);
        assertEquals("", result[0]);
        assertEquals("a.sph", result[1]);

        result = ShadingUtil.splitShadingFileInfo("a.spa");
        assertEquals(2, result.length);
        assertEquals("", result[0]);
        assertEquals("a.spa", result[1]);

        result = ShadingUtil.splitShadingFileInfo("a.spz");
        assertEquals(2, result.length);
        assertEquals("a.spz", result[0]);
        assertEquals("", result[1]);

        result = ShadingUtil.splitShadingFileInfo("a*b");
        assertEquals(2, result.length);
        assertEquals("a", result[0]);
        assertEquals("b", result[1]);

        result = ShadingUtil.splitShadingFileInfo("a*b*c");
        assertEquals(2, result.length);
        assertEquals("a", result[0]);
        assertEquals("b*c", result[1]);

        result = ShadingUtil.splitShadingFileInfo("*");
        assertEquals(2, result.length);
        assertEquals("", result[0]);
        assertEquals("", result[1]);

        result = ShadingUtil.splitShadingFileInfo("a*");
        assertEquals(2, result.length);
        assertEquals("a", result[0]);
        assertEquals("", result[1]);

        result = ShadingUtil.splitShadingFileInfo("*b");
        assertEquals(2, result.length);
        assertEquals("", result[0]);
        assertEquals("b", result[1]);

        result = ShadingUtil.splitShadingFileInfo("a.sph*");
        assertEquals(2, result.length);
        assertEquals("a.sph", result[0]);
        assertEquals("", result[1]);

        result = ShadingUtil.splitShadingFileInfo("a.spa*");
        assertEquals(2, result.length);
        assertEquals("a.spa", result[0]);
        assertEquals("", result[1]);

        return;
    }

}
