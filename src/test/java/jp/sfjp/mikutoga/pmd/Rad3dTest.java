/*
 */

package jp.sfjp.mikutoga.pmd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 *
 */
public class Rad3dTest {

    public Rad3dTest() {
    }

    /**
     * Test of method, of class Deg3d.
     */
    @Test
    public void test() {
        System.out.println("test Rad3d");

        Rad3d rad = new Rad3d();

        assertEquals(0.0f, rad.getXRad(), 0);
        assertEquals(0.0f, rad.getYRad(), 0);
        assertEquals(0.0f, rad.getZRad(), 0);

        rad.setXRad(1.0f);
        rad.setYRad(2.0f);
        rad.setZRad(3.0f);

        assertEquals(1.0f, rad.getXRad(), 0);
        assertEquals(2.0f, rad.getYRad(), 0);
        assertEquals(3.0f, rad.getZRad(), 0);

        assertEquals("rad=[1.0, 2.0, 3.0]", rad.toString());

        return;
    }

}
