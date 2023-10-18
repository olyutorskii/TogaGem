/*
 */

package jp.sfjp.mikutoga.pmd;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 *
 */
public class RigidBehaviorTypeTest {

    public RigidBehaviorTypeTest() {
    }

    /**
     * Test of values method, of class RigidBehaviorType.
     */
    @Test
    public void testValues() {
        System.out.println("values");

        RigidBehaviorType[] array = RigidBehaviorType.values();

        assertEquals(3, array.length);

        assertEquals(RigidBehaviorType.FOLLOWBONE,    array[0]);
        assertEquals(RigidBehaviorType.ONLYDYNAMICS,  array[1]);
        assertEquals(RigidBehaviorType.BONEDDYNAMICS, array[2]);

        return;
    }

    /**
     * Test of decode method, of class RigidBehaviorType.
     */
    @Test
    public void testDecode() {
        System.out.println("decode");

        assertEquals(RigidBehaviorType.FOLLOWBONE,    RigidBehaviorType.decode((byte)0x00));
        assertEquals(RigidBehaviorType.ONLYDYNAMICS,  RigidBehaviorType.decode((byte)0x01));
        assertEquals(RigidBehaviorType.BONEDDYNAMICS, RigidBehaviorType.decode((byte)0x02));

        assertNull(RigidBehaviorType.decode((byte)0x03));

        return;
    }

    /**
     * Test of encode method, of class RigidBehaviorType.
     */
    @Test
    public void testEncode() {
        System.out.println("encode");

        assertEquals(0x00, RigidBehaviorType.FOLLOWBONE.encode());
        assertEquals(0x01, RigidBehaviorType.ONLYDYNAMICS.encode());
        assertEquals(0x02, RigidBehaviorType.BONEDDYNAMICS.encode());

        return;
    }

    /**
     * Test of getGuiName method, of class RigidBehaviorType.
     */
    @Test
    public void testGetGuiName_0args() {
        System.out.println("getGuiName");

        Locale locale = Locale.getDefault();

        for(RigidBehaviorType type : RigidBehaviorType.values()){
            assertEquals(type.getGuiName(locale), type.getGuiName());
        }

        return;
    }

    /**
     * Test of getGuiName method, of class RigidBehaviorType.
     */
    @Test
    public void testGetGuiName_Locale() {
        System.out.println("getGuiName");

        Locale locale;

        locale = Locale.JAPANESE;
        assertEquals("ボーン追従",   RigidBehaviorType.FOLLOWBONE.getGuiName(locale));
        assertEquals("物理演算",   RigidBehaviorType.ONLYDYNAMICS.getGuiName(locale));
        assertEquals("ボーン位置合わせ",   RigidBehaviorType.BONEDDYNAMICS.getGuiName(locale));

        locale = Locale.JAPAN;
        assertEquals("ボーン追従",   RigidBehaviorType.FOLLOWBONE.getGuiName(locale));

        locale = Locale.ITALY;
        assertEquals("static(to bone)",   RigidBehaviorType.FOLLOWBONE.getGuiName(locale));
        assertEquals("dynamic",   RigidBehaviorType.ONLYDYNAMICS.getGuiName(locale));
        assertEquals("bone matching",   RigidBehaviorType.BONEDDYNAMICS.getGuiName(locale));

        locale = Locale.ENGLISH;
        assertEquals("static(to bone)",   RigidBehaviorType.FOLLOWBONE.getGuiName(locale));

        locale = Locale.US;
        assertEquals("static(to bone)",   RigidBehaviorType.FOLLOWBONE.getGuiName(locale));

        return;
    }

}
