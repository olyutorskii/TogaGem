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
public strictfp class MkVec3DTest {

    public MkVec3DTest() {
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
     * Test of constructor, of class MkVec3D.
     */
    @Test
    public void testCons() {
        System.out.println("constructor");

        MkVec3D vec;

        vec = new MkVec3D();
        assertEquals(0.0, vec.getXVal(), 0.0);
        assertEquals(0.0, vec.getYVal(), 0.0);
        assertEquals(0.0, vec.getZVal(), 0.0);

        vec.setXVal(1.0);
        assertEquals(1.0, vec.getXVal(), 0.0);
        assertEquals(0.0, vec.getYVal(), 0.0);
        assertEquals(0.0, vec.getZVal(), 0.0);

        vec.setYVal(2.0);
        assertEquals(1.0, vec.getXVal(), 0.0);
        assertEquals(2.0, vec.getYVal(), 0.0);
        assertEquals(0.0, vec.getZVal(), 0.0);

        vec.setZVal(3.0);
        assertEquals(1.0, vec.getXVal(), 0.0);
        assertEquals(2.0, vec.getYVal(), 0.0);
        assertEquals(3.0, vec.getZVal(), 0.0);

        vec.setVector(4.0, 5.0, 6.0);
        assertEquals(4.0, vec.getXVal(), 0.0);
        assertEquals(5.0, vec.getYVal(), 0.0);
        assertEquals(6.0, vec.getZVal(), 0.0);

        vec = new MkVec3D(7.0, 8.0, 9.0);
        assertEquals(7.0, vec.getXVal(), 0.0);
        assertEquals(8.0, vec.getYVal(), 0.0);
        assertEquals(9.0, vec.getZVal(), 0.0);

        return;
    }

    /**
     * Test of toString method, of class MkVec3D.
     */
    @Test
    public void testToString() {
        System.out.println("toString");

        MkVec3D vec;

        vec = new MkVec3D();
        assertEquals("vec=[0.0, 0.0, 0.0]", vec.toString());

        vec = new MkVec3D(-0.0, StrictMath.PI, Double.MAX_VALUE);
        assertEquals("vec=[-0.0, 3.141592653589793, 1.7976931348623157E308]", vec.toString());

        return;
    }

}
