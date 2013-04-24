/*
 */

package jp.sfjp.mikutoga.typical;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class UniqBoneTest {

    public UniqBoneTest() {
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
     * Test of isPrimaryKneeName method, of class UniqBone.
     */
    @Test
    public void testIsPrimaryKneeName() {
        System.out.println("isPrimaryKneeName");

        assertTrue(UniqBone.isPrimaryKneeName("左ひざ"));
        assertTrue(UniqBone.isPrimaryKneeName("右ひざ"));
        assertTrue(UniqBone.isPrimaryKneeName("左ひざ蹴り"));

        assertFalse(UniqBone.isPrimaryKneeName(""));
        assertFalse(UniqBone.isPrimaryKneeName("左ひ"));
        assertFalse(UniqBone.isPrimaryKneeName("ひざ"));
        assertFalse(UniqBone.isPrimaryKneeName("前ひざ"));
        assertFalse(UniqBone.isPrimaryKneeName("左ひさ゛"));
        assertFalse(UniqBone.isPrimaryKneeName("左ヒザ"));
        assertFalse(UniqBone.isPrimaryKneeName("左ﾋｻﾞ"));
        assertFalse(UniqBone.isPrimaryKneeName("左膝"));
        assertFalse(UniqBone.isPrimaryKneeName("Knee_L"));

        return;
    }

}
