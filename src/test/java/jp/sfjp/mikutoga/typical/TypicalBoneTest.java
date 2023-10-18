/*
 */

package jp.sfjp.mikutoga.typical;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 *
 */
public class TypicalBoneTest {

    public TypicalBoneTest() {
    }

    /**
     * Test of findWithPrimary method, of class TypicalBone.
     */
    @Test
    public void testFindWithPrimary() {
        System.out.println("findWithPrimary");

        TypicalBone result;
        result = TypicalBone.findWithPrimary("頭");

        assertEquals("頭", result.getTopPrimaryName());
        assertEquals("head", result.getTopGlobalName());
        assertEquals(1, result.getPrimaryNameList().size());
        assertEquals("頭", result.getPrimaryNameList().get(0));
        assertEquals(1, result.getGlobalNameList().size());
        assertEquals("head", result.getGlobalNameList().get(0));

        return;
    }

    /**
     * Test of findWithGlobal method, of class TypicalBone.
     */
    @Test
    public void testFindWithGlobal() {
        System.out.println("findWithGlobal");

        TypicalBone result;

        result = TypicalBone.findWithGlobal("head");
        assertNotNull(result);

        TypicalBone result2;
        result2 = TypicalBone.findWithPrimary("頭");

        assertSame(result, result2);

        return;
    }

    /**
     * Test of primary2global method, of class TypicalBone.
     */
    @Test
    public void testPrimary2global() {
        System.out.println("primary2global");

        String result;

        result = TypicalBone.primary2global("頭");
        assertEquals("head", result);

        String result1;
        String result2;

        result1 = TypicalBone.primary2global("ﾎﾞｰﾝ15");
        result2 = TypicalBone.primary2global("ボーン１５");
        assertSame(result1, result2);

        result = TypicalBone.primary2global("XXX");
        assertNull(result);

        return;
    }

    /**
     * Test of global2primary method, of class TypicalBone.
     */
    @Test
    public void testGlobal2primary() {
        System.out.println("global2primary");

        String result;

        result = TypicalBone.global2primary("head");
        assertEquals("頭", result);

        result = TypicalBone.global2primary("ｈｅａｄ");
        assertEquals("頭", result);

        result = TypicalBone.global2primary("XXX");
        assertNull(result);

        return;
    }

    /**
     * Test of getTypicalBoneList method, of class TypicalBone.
     */
    @Test
    public void testGetBoneList() {
        System.out.println("getBoneList");

        List<TypicalBone> boneList;

        boneList = TypicalBone.getTypicalBoneList();

        assertNotNull(boneList);
        assertEquals(77, boneList.size());

        TypicalBone bone1st = boneList.get(0);
        TypicalBone boneLast = boneList.get(77-1);

        assertEquals("センター", bone1st.getTopPrimaryName());
        assertEquals("ﾎﾞｰﾝ15", boneLast.getTopPrimaryName());

        assertEquals("center", bone1st.getTopGlobalName());
        assertEquals("bone15", boneLast.getTopGlobalName());

        return;
    }

    /**
     * Test of isRoot method, of class TypicalBone.
     */
    @Test
    public void testIsRoot() {
        System.out.println("isRoot");

        TypicalBone bone;

        bone = TypicalBone.findWithPrimary("センター");
        assertTrue(bone.isRoot());

        bone = TypicalBone.findWithPrimary("頭");
        assertFalse(bone.isRoot());

        bone = TypicalBone.findWithPrimary("右足IK");
        assertTrue(bone.isRoot());

        bone = TypicalBone.findWithPrimary("左足IK");
        assertTrue(bone.isRoot());

        bone = TypicalBone.findWithPrimary("ボーン01");
        assertTrue(bone.isRoot());

        return;
    }

}
