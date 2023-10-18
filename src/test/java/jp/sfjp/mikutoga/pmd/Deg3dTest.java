/*
 */

package jp.sfjp.mikutoga.pmd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 *
 */
public class Deg3dTest {

    public Deg3dTest() {
    }

    /**
     * Test of method, of class Deg3d.
     */
    @Test
    public void test() {
        System.out.println("test Deg3d");

        Deg3d deg = new Deg3d();

        assertEquals(0.0f, deg.getXDeg(), 0);
        assertEquals(0.0f, deg.getYDeg(), 0);
        assertEquals(0.0f, deg.getZDeg(), 0);

        deg.setXDeg(1.0f);
        deg.setYDeg(2.0f);
        deg.setZDeg(3.0f);

        assertEquals(1.0f, deg.getXDeg(), 0);
        assertEquals(2.0f, deg.getYDeg(), 0);
        assertEquals(3.0f, deg.getZDeg(), 0);

        assertEquals("deg=[1.0, 2.0, 3.0]", deg.toString());

        return;
    }

}
