/*
 */

package jp.sfjp.mikutoga.math;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public strictfp class MkPos3DTest {

    public MkPos3DTest() {
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
     * Test of constructor, of class MkPos3D.
     */
    @Test
    public void testCons() {
        System.out.println("constructor");

        MkPos3D pos;

        pos = new MkPos3D();
        assertTrue(pos.isOriginPoint());
        assertEquals(0.0, pos.getXpos(), 0.0);
        assertEquals(0.0, pos.getYpos(), 0.0);
        assertEquals(0.0, pos.getZpos(), 0.0);

        pos.setXpos(1.0);
        assertFalse(pos.isOriginPoint());
        assertEquals(1.0, pos.getXpos(), 0.0);
        assertEquals(0.0, pos.getYpos(), 0.0);
        assertEquals(0.0, pos.getZpos(), 0.0);

        pos.setYpos(2.0);
        assertEquals(1.0, pos.getXpos(), 0.0);
        assertEquals(2.0, pos.getYpos(), 0.0);
        assertEquals(0.0, pos.getZpos(), 0.0);

        pos.setZpos(3.0);
        assertEquals(1.0, pos.getXpos(), 0.0);
        assertEquals(2.0, pos.getYpos(), 0.0);
        assertEquals(3.0, pos.getZpos(), 0.0);

        pos.setPosition(4.0, 5.0, 6.0);
        assertEquals(4.0, pos.getXpos(), 0.0);
        assertEquals(5.0, pos.getYpos(), 0.0);
        assertEquals(6.0, pos.getZpos(), 0.0);

        pos.setPosition(0.0, 0.0, 0.0);
        assertTrue(pos.isOriginPoint());

        pos.setPosition(1.0, 0.0, 0.0);
        assertFalse(pos.isOriginPoint());

        pos.setPosition(0.0, 1.0, 0.0);
        assertFalse(pos.isOriginPoint());

        pos.setPosition(0.0, 0.0, 1.0);
        assertFalse(pos.isOriginPoint());

        pos.setPosition(-0.0, -0.0, -0.0);
        assertTrue(pos.isOriginPoint());

        pos = new MkPos3D(7.0, 8.0, 9.0);
        assertEquals(7.0, pos.getXpos(), 0.0);
        assertEquals(8.0, pos.getYpos(), 0.0);
        assertEquals(9.0, pos.getZpos(), 0.0);

        return;
    }

    /**
     * Test of toString method, of class MkPos3D.
     */
    @Test
    public void testToString() {
        System.out.println("toString");

        MkPos3D pos;

        pos = new MkPos3D();
        assertEquals("x=0.0 y=0.0 z=0.0", pos.toString());

        pos = new MkPos3D(-0.0, StrictMath.PI, Double.MAX_VALUE);
        assertEquals("x=-0.0 y=3.141592653589793 z=1.7976931348623157E308", pos.toString());

        return;
    }

}
