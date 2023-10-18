/*
 */

package jp.sfjp.mikutoga.typical;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 *
 */
public class UniqBoneTest {

    public UniqBoneTest() {
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
