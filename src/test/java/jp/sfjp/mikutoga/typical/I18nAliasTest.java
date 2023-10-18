/*
 */

package jp.sfjp.mikutoga.typical;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 *
 */
public class I18nAliasTest {

    public I18nAliasTest() {
    }

    /**
     * Test of orderNo method, of class I18nAlias.
     */
    @Test
    public void testOrderNo() {
        System.out.println("OrderNo");

        I18nAlias alias = new I18nAlias();

        assertEquals(0, alias.getOrderNo());
        alias.setOrderNo(99);
        assertEquals(99, alias.getOrderNo());

        I18nAlias alias2 = new I18nAlias();

        alias2.setOrderNo(999);
        assertTrue(0 > I18nAlias.ORDER_COMPARATOR.compare(alias, alias2));

        alias2.setOrderNo(99);
        assertTrue(0 == I18nAlias.ORDER_COMPARATOR.compare(alias, alias2));

        alias2.setOrderNo(9);
        assertTrue(0 < I18nAlias.ORDER_COMPARATOR.compare(alias, alias2));

        return;
    }

    /**
     * Test of method, of class I18nAlias.
     */
    @Test
    public void testName() {
        System.out.println("Name");

        I18nAlias alias;

        alias = new I18nAlias();

        alias.addPrimaryName("p1");

        assertEquals("p1", alias.getTopPrimaryName());
        assertNull(alias.getTopGlobalName());

        List<String> primaryList;
        List<String> globalList;

        primaryList = alias.getPrimaryNameList();
        globalList = alias.getGlobalNameList();

        assertEquals(1, primaryList.size());
        assertEquals(0, globalList.size());
        assertEquals("p1", primaryList.get(0));

        alias.addGlobalName("g1");

        assertEquals("g1", alias.getTopGlobalName());
        globalList = alias.getGlobalNameList();
        assertEquals(1, globalList.size());
        assertEquals("g1", globalList.get(0));

        return;
    }

}
