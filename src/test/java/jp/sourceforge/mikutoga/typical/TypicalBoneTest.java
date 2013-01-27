/*
 */

package jp.sourceforge.mikutoga.typical;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class TypicalBoneTest {

    public TypicalBoneTest() {
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
     * Test of findWithPrimary method, of class TypicalBone.
     */
    @Test
    public void testFindWithPrimary() {
        System.out.println("findWithPrimary");

        TypicalBone result;
        result = TypicalBone.findWithPrimary("頭");

        assertEquals("頭", result.getTopPrimaryName());
        assertEquals("head", result.getTopGlobalName());
        assertEquals(1, result.getPrimaryList().size());
        assertEquals("頭", result.getPrimaryList().get(0));
        assertEquals(1, result.getGlobalList().size());
        assertEquals("head", result.getGlobalList().get(0));

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
     * Test of getBoneList method, of class TypicalBone.
     */
    @Test
    public void testGetBoneList() {
        System.out.println("getBoneList");

        List<TypicalBone> boneList;

        boneList = TypicalBone.getBoneList();

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

}
