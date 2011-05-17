/*
 *
 */

package jp.sourceforge.mikutoga.pmd;

import java.util.Locale;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 */
public class MorphTypeTest {

    public MorphTypeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception{
    }

    @AfterClass
    public static void tearDownClass() throws Exception{
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of values method, of class MorphType.
     */
    @Test
    public void testValues(){
        System.out.println("values");

        MorphType[] array = MorphType.values();

        assertEquals(5, array.length);

        assertEquals(MorphType.BASE, array[0]);
        assertEquals(MorphType.EYEBROW, array[1]);
        assertEquals(MorphType.EYE, array[2]);
        assertEquals(MorphType.LIP, array[3]);
        assertEquals(MorphType.EXTRA, array[4]);

        return;
    }

    /**
     * Test of decode method, of class MorphType.
     */
    @Test
    public void testDecode(){
        System.out.println("decode");

        assertEquals(MorphType.BASE,    MorphType.decode((byte)0x00));
        assertEquals(MorphType.EYEBROW, MorphType.decode((byte)0x01));
        assertEquals(MorphType.EYE,     MorphType.decode((byte)0x02));
        assertEquals(MorphType.LIP,     MorphType.decode((byte)0x03));
        assertEquals(MorphType.EXTRA,   MorphType.decode((byte)0x04));
        assertNull(MorphType.decode((byte)0x05));

        return;
    }

    /**
     * Test of encode method, of class MorphType.
     */
    @Test
    public void testEncode(){
        System.out.println("encode");

        assertEquals(0x00, MorphType.BASE.encode());
        assertEquals(0x01, MorphType.EYEBROW.encode());
        assertEquals(0x02, MorphType.EYE.encode());
        assertEquals(0x03, MorphType.LIP.encode());
        assertEquals(0x04, MorphType.EXTRA.encode());

        return;
    }

    /**
     * Test of getGuiName method, of class MorphType.
     */
    @Test
    public void testGetGuiName_0args(){
        System.out.println("getGuiName");

        Locale locale = Locale.getDefault();

        for(MorphType type : MorphType.values()){
            assertEquals(type.getGuiName(locale), type.getGuiName());
        }

        return;
    }

    /**
     * Test of getGuiName method, of class MorphType.
     */
    @Test
    public void testGetGuiName_Locale(){
        System.out.println("getGuiName");

        Locale locale = Locale.JAPANESE;

        assertEquals("base", MorphType.BASE.getGuiName(locale));
        assertEquals("まゆ", MorphType.EYEBROW.getGuiName(locale));
        assertEquals("目", MorphType.EYE.getGuiName(locale));
        assertEquals("リップ", MorphType.LIP.getGuiName(locale));
        assertEquals("その他", MorphType.EXTRA.getGuiName(locale));
        
        return;
    }

}
