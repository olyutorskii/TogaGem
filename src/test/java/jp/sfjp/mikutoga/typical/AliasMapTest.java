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
public class AliasMapTest {

    public AliasMapTest() {
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

    static class TestAlias extends I18nAlias{
    }

    /**
     * Test of normalize method, of class AliasMap.
     */
    @Test
    public void testNormalize() {
        System.out.println("normalize");

        assertEquals("", AliasMap.normalize(""));

        assertEquals("azAZ", AliasMap.normalize("azAZ"));
        assertEquals("azAZ", AliasMap.normalize("ａｚＡＺ"));

        assertEquals("5678", AliasMap.normalize("5678"));
        assertEquals("56VII八", AliasMap.normalize("５⑥Ⅶ八"));

        assertEquals("ガ", AliasMap.normalize("ガ"));
        assertEquals("ガ", AliasMap.normalize("ｶﾞ"));
        assertEquals("ガ", AliasMap.normalize("カﾞ"));
        assertEquals("ガ", AliasMap.normalize("カ"+"\u3099"));
        assertEquals("カ\u0020\u3099", AliasMap.normalize("カ"+"\u309b"));

        assertEquals("パ", AliasMap.normalize("パ"));
        assertEquals("パ", AliasMap.normalize("ﾊﾟ"));
        assertEquals("パ", AliasMap.normalize("ハﾟ"));
        assertEquals("パ", AliasMap.normalize("ハ"+"\u309a"));
        assertEquals("ハ\u0020\u309a", AliasMap.normalize("ハ"+"\u309c"));

        assertEquals("リットル", AliasMap.normalize("㍑"));

        return;
    }

    /**
     * Test of addAlias method, of class AliasMap.
     */
    @Test
    public void testAddAlias() {
        System.out.println("addAlias");

        AliasMap<TestAlias> map;
        TestAlias alias1;

        alias1 = new TestAlias();
        alias1.addPrimaryName("p1");
        alias1.addPrimaryName("p２");
        alias1.addGlobalName("g１");
        alias1.addGlobalName("g2");

        map = new AliasMap<TestAlias>();
        map.addAlias(alias1);

        assertSame(alias1, map.getAlias("p１"));
        assertSame(alias1, map.getAlias("p2"));
        assertSame(alias1, map.getAlias("g1"));
        assertSame(alias1, map.getAlias("g２"));

        assertNull(map.getAlias("ZZZ"));

        assertEquals("g１", map.primary2global("ｐ2"));
        assertEquals("p1", map.global2primary("ｇ2"));

        assertNull(map.primary2global("ZZZ"));
        assertNull(map.global2primary("ZZZ"));


        TestAlias aliasHand;
        TestAlias aliasFoot;

        aliasHand = new TestAlias();
        aliasHand.addPrimaryName("手1");
        aliasHand.addPrimaryName("おてて２");
        aliasHand.addPrimaryName("h");
        aliasHand.addPrimaryName("bone");
        aliasHand.addPrimaryName("cross1");
        aliasHand.addGlobalName("hand1");
        aliasHand.addGlobalName("paw2");
        aliasHand.addGlobalName("h");
        aliasHand.addGlobalName("bone");
        aliasHand.addGlobalName("cross2");

        aliasFoot = new TestAlias();
        aliasFoot.addPrimaryName("足1");
        aliasFoot.addPrimaryName("あんよ２");
        aliasFoot.addPrimaryName("f");
        aliasFoot.addPrimaryName("bone");
        aliasFoot.addPrimaryName("cross2");
        aliasFoot.addGlobalName("foot1");
        aliasFoot.addGlobalName("hoof2");
        aliasFoot.addGlobalName("f");
        aliasFoot.addGlobalName("bone");
        aliasFoot.addGlobalName("cross1");

        map = new AliasMap<TestAlias>();
        map.addAlias(aliasHand);
        map.addAlias(aliasFoot);

        assertSame(aliasHand, map.getAlias("h"));
        assertSame(aliasFoot, map.getAlias("f"));
        assertSame(aliasFoot, map.getAlias("bone"));
        assertSame(aliasHand, map.getAlias("cross1"));
        assertSame(aliasFoot, map.getAlias("cross2"));

        assertSame(aliasHand, map.getAliasByPrimary("h"));
        assertSame(aliasFoot, map.getAliasByPrimary("f"));
        assertSame(aliasFoot, map.getAliasByPrimary("bone"));
        assertSame(aliasHand, map.getAliasByPrimary("cross1"));
        assertSame(aliasFoot, map.getAliasByPrimary("cross2"));

        assertSame(aliasHand, map.getAliasByGlobal("h"));
        assertSame(aliasFoot, map.getAliasByGlobal("f"));
        assertSame(aliasFoot, map.getAliasByGlobal("bone"));
        assertSame(aliasFoot, map.getAliasByGlobal("cross1"));
        assertSame(aliasHand, map.getAliasByGlobal("cross2"));

        return;
    }

}
