/*
 */

package jp.sfjp.mikutoga.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 *
 */
public strictfp class MkPos2DTest {

    public MkPos2DTest() {
    }

    /**
     * Test of constructor, of class MkPos2D.
     */
    @Test
    public void testCons() {
        System.out.println("constructor");

        MkPos2D pos;

        pos = new MkPos2D();
        assertTrue(pos.isOriginPoint());
        assertEquals(0.0, pos.getXpos(), 0.0);
        assertEquals(0.0, pos.getYpos(), 0.0);

        pos.setXpos(1.0);
        assertFalse(pos.isOriginPoint());
        assertEquals(1.0, pos.getXpos(), 0.0);
        assertEquals(0.0, pos.getYpos(), 0.0);

        pos.setYpos(2.0);
        assertEquals(1.0, pos.getXpos(), 0.0);
        assertEquals(2.0, pos.getYpos(), 0.0);

        pos.setPosition(3.0, 4.0);
        assertEquals(3.0, pos.getXpos(), 0.0);
        assertEquals(4.0, pos.getYpos(), 0.0);

        pos.setPosition(1.0, 0.0);
        assertFalse(pos.isOriginPoint());

        pos.setPosition(0.0, 1.0);
        assertFalse(pos.isOriginPoint());

        pos.setPosition(0.0, 0.0);
        assertTrue(pos.isOriginPoint());

        pos.setPosition(-0.0, -0.0);
        assertTrue(pos.isOriginPoint());

        pos = new MkPos2D(5.0, 6.0);
        assertEquals(5.0, pos.getXpos(), 0.0);
        assertEquals(6.0, pos.getYpos(), 0.0);

        return;
    }

    /**
     * Test of toString method, of class MkPos2D.
     */
    @Test
    public void testToString() {
        System.out.println("toString");

        MkPos2D pos;

        pos = new MkPos2D();
        assertEquals("pos=[0.0, 0.0]", pos.toString());

        pos = new MkPos2D(-0.0, StrictMath.PI);
        assertEquals("pos=[-0.0, 3.141592653589793]", pos.toString());

        return;
    }

}
