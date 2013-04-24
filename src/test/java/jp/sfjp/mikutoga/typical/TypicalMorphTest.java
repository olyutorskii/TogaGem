/*
 */

package jp.sfjp.mikutoga.typical;

import java.util.List;
import jp.sfjp.mikutoga.pmd.MorphType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class TypicalMorphTest {

    public TypicalMorphTest() {
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
     * Test of getTypicalMorphList method, of class TypicalMorph.
     */
    @Test
    public void testGetTypedMorphList() {
        System.out.println("getTypedMorphList");

        List<TypicalMorph> morphList;

        morphList = TypicalMorph.getTypicalMorphList(MorphType.EYEBROW);
        assertEquals(6, morphList.size());

        morphList = TypicalMorph.getTypicalMorphList(MorphType.EYE);
        assertEquals(7, morphList.size());

        morphList = TypicalMorph.getTypicalMorphList(MorphType.LIP);
        assertEquals(12, morphList.size());

        morphList = TypicalMorph.getTypicalMorphList(MorphType.EXTRA);
        assertEquals(2, morphList.size());

        return;
    }

    /**
     * Test of findWithPrimary method, of class TypicalMorph.
     */
    @Test
    public void testFindWithPrimary() {
        System.out.println("findWithPrimary");

        TypicalMorph result;

        result = TypicalMorph.findWithPrimary("あ");
        assertNotNull(result);
        assertEquals(MorphType.LIP, result.getMorphType());
        assertEquals("あ", result.getTopPrimaryName());
        assertEquals("a", result.getTopGlobalName());
        assertEquals(1, result.getPrimaryNameList().size());
        assertEquals("あ", result.getPrimaryNameList().get(0));
        assertEquals(1, result.getGlobalNameList().size());
        assertEquals("a", result.getGlobalNameList().get(0));

        TypicalMorph result1;
        TypicalMorph result2;

        result1 = TypicalMorph.findWithPrimary("べー");
        result2 = TypicalMorph.findWithPrimary("ぺろっ");
        assertSame(result1, result2);
        assertEquals(MorphType.EXTRA, result1.getMorphType());
        assertEquals("べー", result1.getTopPrimaryName());
        assertEquals("tongue", result1.getTopGlobalName());
        assertEquals(2, result1.getPrimaryNameList().size());
        assertEquals("べー", result1.getPrimaryNameList().get(0));
        assertEquals("ぺろっ", result1.getPrimaryNameList().get(1));
        assertEquals(1, result1.getGlobalNameList().size());
        assertEquals("tongue", result1.getGlobalNameList().get(0));

        return;
    }

    /**
     * Test of findWithGlobal method, of class TypicalMorph.
     */
    @Test
    public void testFindWithGlobal() {
        System.out.println("findWithGlobal");

        TypicalMorph result;

        result = TypicalMorph.findWithGlobal("a");
        assertNotNull(result);

        TypicalMorph result2;
        result2 = TypicalMorph.findWithPrimary("あ");

        assertSame(result, result2);

        return;
    }

    /**
     * Test of primary2global method, of class TypicalMorph.
     */
    @Test
    public void testPrimary2global() {
        System.out.println("primary2global");

        String result;

        result = TypicalMorph.primary2global("あ");
        assertEquals("a", result);

        String result1;
        String result2;

        result1 = TypicalMorph.primary2global("べー");
        result2 = TypicalMorph.primary2global("ぺろっ");
        assertSame(result1, result2);

        result1 = TypicalMorph.primary2global("ウィンク");
        result2 = TypicalMorph.primary2global("ｳｨﾝｸ");
        assertSame(result1, result2);

        result = TypicalMorph.primary2global("XXX");
        assertNull(result);

        return;
    }

    /**
     * Test of global2primary method, of class TypicalMorph.
     */
    @Test
    public void testGlobal2primary() {
        System.out.println("global2primary");

        String result;

        result = TypicalMorph.global2primary("a");
        assertEquals("あ", result);

        result = TypicalMorph.global2primary("tongue");
        assertEquals("べー", result);

        result = TypicalMorph.global2primary("XXX");
        assertNull(result);

        return;
    }

    /**
     * Test of getMorphType method, of class TypicalMorph.
     */
    @Test
    public void testGetMorphType() {
        System.out.println("getMorphType");

        List<TypicalMorph> morphList;

        morphList = TypicalMorph.getTypicalMorphList(MorphType.EYEBROW);

        for(TypicalMorph morph : morphList){
            MorphType type = morph.getMorphType();
            assertEquals(MorphType.EYEBROW, type);
        }

        morphList = TypicalMorph.getTypicalMorphList(MorphType.EYE);

        for(TypicalMorph morph : morphList){
            MorphType type = morph.getMorphType();
            assertEquals(MorphType.EYE, type);
        }

        morphList = TypicalMorph.getTypicalMorphList(MorphType.LIP);

        for(TypicalMorph morph : morphList){
            MorphType type = morph.getMorphType();
            assertEquals(MorphType.LIP, type);
        }

        morphList = TypicalMorph.getTypicalMorphList(MorphType.EXTRA);

        for(TypicalMorph morph : morphList){
            MorphType type = morph.getMorphType();
            assertEquals(MorphType.EXTRA, type);
        }

        return;
    }

}
