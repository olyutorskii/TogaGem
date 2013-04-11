/*
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
public class RigidShapeTypeTest {

    public RigidShapeTypeTest() {
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
     * Test of values method, of class RigidShapeType.
     */
    @Test
    public void testValues() {
        System.out.println("values");

        RigidShapeType[] array = RigidShapeType.values();

        assertEquals(3, array.length);

        assertEquals(RigidShapeType.SPHERE,  array[0]);
        assertEquals(RigidShapeType.BOX,     array[1]);
        assertEquals(RigidShapeType.CAPSULE, array[2]);

        return;
    }

    /**
     * Test of decode method, of class RigidShapeType.
     */
    @Test
    public void testDecode() {
        System.out.println("decode");

        assertEquals(RigidShapeType.SPHERE,  RigidShapeType.decode((byte)0x00));
        assertEquals(RigidShapeType.BOX,     RigidShapeType.decode((byte)0x01));
        assertEquals(RigidShapeType.CAPSULE, RigidShapeType.decode((byte)0x02));

        assertNull(RigidShapeType.decode((byte)0x03));

        return;
    }

    /**
     * Test of encode method, of class RigidShapeType.
     */
    @Test
    public void testEncode() {
        System.out.println("encode");

        assertEquals(0x00, RigidShapeType.SPHERE.encode());
        assertEquals(0x01, RigidShapeType.BOX.encode());
        assertEquals(0x02, RigidShapeType.CAPSULE.encode());

        return;
    }

    /**
     * Test of getGuiName method, of class RigidShapeType.
     */
    @Test
    public void testGetGuiName_0args() {
        System.out.println("getGuiName");

        Locale locale = Locale.getDefault();

        for(RigidShapeType type : RigidShapeType.values()){
            assertEquals(type.getGuiName(locale), type.getGuiName());
        }

        return;
    }

    /**
     * Test of getGuiName method, of class RigidShapeType.
     */
    @Test
    public void testGetGuiName_Locale() {
        System.out.println("getGuiName");

        Locale locale;

        locale = Locale.JAPANESE;
        assertEquals("球",   RigidShapeType.SPHERE.getGuiName(locale));
        assertEquals("箱",   RigidShapeType.BOX.getGuiName(locale));
        assertEquals("カプセル",   RigidShapeType.CAPSULE.getGuiName(locale));

        locale = Locale.JAPAN;
        assertEquals("球",   RigidShapeType.SPHERE.getGuiName(locale));

        locale = Locale.ITALY;
        assertEquals("sphere",   RigidShapeType.SPHERE.getGuiName(locale));
        assertEquals("box",      RigidShapeType.BOX.getGuiName(locale));
        assertEquals("capsule",  RigidShapeType.CAPSULE.getGuiName(locale));

        locale = Locale.ENGLISH;
        assertEquals("sphere",   RigidShapeType.SPHERE.getGuiName(locale));

        locale = Locale.US;
        assertEquals("sphere",   RigidShapeType.SPHERE.getGuiName(locale));

        return;
    }

}
