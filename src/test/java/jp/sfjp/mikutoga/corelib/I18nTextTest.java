/*
 */

package jp.sfjp.mikutoga.corelib;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 *
 */
public class I18nTextTest {

    public I18nTextTest() {
    }

    /**
     * Test of setPrimaryText method, of class I18nText.
     */
    @Test
    public void testSetPrimaryText() {
        System.out.println("setPrimaryText");

        I18nText text;

        text = new I18nText();
        text.setPrimaryText("pri");
        assertEquals("pri", text.getPrimaryText());

        text.setPrimaryText(null);
        assertNull(text.getPrimaryText());

        return;
    }

    /**
     * Test of setGlobalText method, of class I18nText.
     */
    @Test
    public void testSetGlobalText() {
        System.out.println("setGlobalText");

        I18nText text;

        text = new I18nText();
        text.setGlobalText("glo");

        assertEquals("glo", text.getGlobalText());

        text.setGlobalText(null);
        assertNull(text.getGlobalText());

        return;
    }

    /**
     * Test of setI18nText method, of class I18nText.
     */
    @Test
    public void testSetI18nText_Locale_CharSequence() {
        System.out.println("setI18nText");

        I18nText text;

        text = new I18nText();
        text.setI18nText(Locale.JAPANESE, "pri");
        assertEquals("pri", text.getPrimaryText());

        text.setI18nText(Locale.JAPANESE, null);
        assertNull(text.getPrimaryText());

        return;
    }

    /**
     * Test of setI18nText method, of class I18nText.
     */
    @Test
    public void testSetI18nText_String_CharSequence() {
        System.out.println("setI18nText");

        I18nText text;

        text = new I18nText();
        text.setI18nText("ja", "pri");
        assertEquals("pri", text.getPrimaryText());

        text.setI18nText("ja", null);
        assertNull(text.getPrimaryText());

        return;
    }

    /**
     * Test of getPrimaryText method, of class I18nText.
     */
    @Test
    public void testGetPrimaryText() {
        System.out.println("getPrimaryText");

        I18nText text;

        text = new I18nText();
        assertNull(text.getPrimaryText());

        text.setPrimaryText("pri");
        assertEquals("pri", text.getPrimaryText());

        return;
    }

    /**
     * Test of getGlobalText method, of class I18nText.
     */
    @Test
    public void testGetGlobalText() {
        System.out.println("getGlobalText");

        I18nText text;

        text = new I18nText();
        assertNull(text.getGlobalText());

        text.setGlobalText("glo");
        assertEquals("glo", text.getGlobalText());

        return;
    }

    /**
     * Test of getI18nText method, of class I18nText.
     */
    @Test
    public void testGetI18nText_Locale() {
        System.out.println("getI18nText");

        I18nText text;

        text = new I18nText();
        text.setPrimaryText("pri");

        assertEquals("pri", text.getI18nText(Locale.JAPANESE));
        assertNull(text.getI18nText(Locale.ENGLISH));

        return;
    }

    /**
     * Test of getI18nText method, of class I18nText.
     */
    @Test
    public void testGetI18nText_String() {
        System.out.println("getI18nText");

        I18nText text;

        text = new I18nText();
        text.setPrimaryText("pri");

        assertEquals("pri", text.getI18nText("ja"));
        assertNull(text.getI18nText("en"));

        return;
    }

    /**
     * Test of getText method, of class I18nText.
     */
    @Test
    public void testGetText() {
        System.out.println("getText");

        I18nText text;

        text = new I18nText();
        assertEquals("", text.getText());

        text.setPrimaryText("pri");
        assertEquals("pri", text.getText());

        return;
    }

    /**
     * Test of getLocalizedText method, of class I18nText.
     */
    @Test
    public void testGetLocalizedText() {
        System.out.println("getLocalizedText");

        I18nText text;

        text = new I18nText();
        assertEquals("", text.getLocalizedText());

        text = new I18nText();
        text.setPrimaryText("pri");
        assertEquals("pri", text.getLocalizedText());

        text = new I18nText();
        text.setGlobalText("glo");
        assertEquals("glo", text.getLocalizedText());

        text = new I18nText();
        text.setI18nText(Locale.CHINESE, "chi");
        assertEquals("chi", text.getLocalizedText());

        // default locale...

        return;
    }

    /**
     * Test of clearI18nText method, of class I18nText.
     */
    @Test
    public void testClearI18nText() {
        System.out.println("clearI18nText");

        I18nText text;

        text = new I18nText();

        text.setPrimaryText("pri");
        text.setGlobalText("glo");

        assertNotNull(text.getPrimaryText());
        assertNotNull(text.getGlobalText());

        text.clearI18nText();

        assertNull(text.getPrimaryText());
        assertNull(text.getGlobalText());

        return;
    }

    /**
     * Test of lang639CodeList method, of class I18nText.
     */
    @Test
    public void testLang639CodeList() {
        System.out.println("lang639CodeList");

        I18nText text;
        List<String> list;

        text = new I18nText();

        list = text.lang639CodeList();
        assertTrue(list.isEmpty());

        text.setI18nText(Locale.CHINA, "chi");
        text.setPrimaryText("pri");
        text.setGlobalText("glo");

        list = text.lang639CodeList();

        assertEquals(3, list.size());
        assertEquals("ja", list.get(0));
        assertEquals("en", list.get(1));
        assertEquals("zh", list.get(2));

        return;
    }

    /**
     * Test of hasPrimaryText method, of class I18nText.
     */
    @Test
    public void testHasPrimaryText() {
        System.out.println("hasPrimaryText");

        I18nText text;

        text = new I18nText();
        assertFalse(text.hasPrimaryText());

        text.setPrimaryText("pri");
        assertTrue(text.hasPrimaryText());

        text.setPrimaryText(null);
        assertFalse(text.hasPrimaryText());

        text.setGlobalText("glo");
        assertFalse(text.hasPrimaryText());

        return;
    }

    /**
     * Test of hasGlobalText method, of class I18nText.
     */
    @Test
    public void testHasGlobalText() {
        System.out.println("hasGlobalText");

        I18nText text;

        text = new I18nText();
        assertFalse(text.hasGlobalText());

        text.setGlobalText("glo");
        assertTrue(text.hasGlobalText());

        text.setGlobalText(null);
        assertFalse(text.hasGlobalText());

        text.setPrimaryText("pri");
        assertFalse(text.hasGlobalText());

        return;
    }

    /**
     * Test of charAt method, of class I18nText.
     */
    @Test
    public void testCharAt() {
        System.out.println("charAt");

        I18nText text;

        text = new I18nText();

        text.setPrimaryText("pri");
        text.setGlobalText("glo");
        text.setI18nText(Locale.CHINA, "chi");

        assertEquals('r', text.charAt(1));

        return;
    }

    /**
     * Test of length method, of class I18nText.
     */
    @Test
    public void testLength() {
        System.out.println("length");

        I18nText text;

        text = new I18nText();

        text.setPrimaryText("pri");
        text.setGlobalText("glob");
        text.setI18nText(Locale.CHINA, "china");

        assertEquals(3, text.length());

        return;
    }

    /**
     * Test of subSequence method, of class I18nText.
     */
    @Test
    public void testSubSequence() {
        System.out.println("subSequence");

        I18nText text;

        text = new I18nText();

        text.setPrimaryText("pri");
        text.setGlobalText("glo");
        text.setI18nText(Locale.CHINA, "chi");

        assertEquals("p", text.subSequence(0, 1));

        return;
    }

    /**
     * Test of toString method, of class I18nText.
     */
    @Test
    public void testToString() {
        System.out.println("toString");

        I18nText text;

        text = new I18nText();

        text.setPrimaryText("pri");
        text.setGlobalText("glo");
        text.setI18nText(Locale.CHINA, "chi");

        assertEquals("pri", text.toString());

        return;
    }

}
