/*
 */

package jp.sfjp.mikutoga.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 *
 */
public strictfp class EulerYXZTest {

    public EulerYXZTest() {
    }

    /**
     * Test of constructor, of class EulerYXZ.
     */
    @Test
    public void testCons() {
        System.out.println("constructor");

        EulerYXZ euler;

        euler = new EulerYXZ();
        assertEquals(0.0, euler.getXRot(), 0.0);
        assertEquals(0.0, euler.getYRot(), 0.0);
        assertEquals(0.0, euler.getZRot(), 0.0);

        euler = new EulerYXZ(1.0, 2.0, 3.0);
        assertEquals(1.0, euler.getXRot(), 0.0);
        assertEquals(2.0, euler.getYRot(), 0.0);
        assertEquals(3.0, euler.getZRot(), 0.0);

        euler.setXRot(4.0);
        assertEquals(4.0, euler.getXRot(), 0.0);
        assertEquals(2.0, euler.getYRot(), 0.0);
        assertEquals(3.0, euler.getZRot(), 0.0);

        euler.setYRot(5.0);
        assertEquals(4.0, euler.getXRot(), 0.0);
        assertEquals(5.0, euler.getYRot(), 0.0);
        assertEquals(3.0, euler.getZRot(), 0.0);

        euler.setZRot(6.0);
        assertEquals(4.0, euler.getXRot(), 0.0);
        assertEquals(5.0, euler.getYRot(), 0.0);
        assertEquals(6.0, euler.getZRot(), 0.0);

        euler.setRot(10.0, 11.0, 12.0);
        assertEquals(10.0, euler.getXRot(), 0.0);
        assertEquals(11.0, euler.getYRot(), 0.0);
        assertEquals(12.0, euler.getZRot(), 0.0);

        return;
    }

    /**
     * Test of toString method, of class EulerYXZ.
     */
    @Test
    public void testToString() {
        System.out.println("toString");

        EulerYXZ euler;

        euler = new EulerYXZ(1.0, 2.0, 3.0);
        assertEquals("x=1.0 y=2.0 z=3.0", euler.toString());

        euler = new EulerYXZ(-0.0, Double.MIN_VALUE, Double.MAX_VALUE);
        assertEquals("x=-0.0 y=4.9E-324 z=1.7976931348623157E308", euler.toString());

        euler = new EulerYXZ(0.5, 0.25, StrictMath.PI);
        assertEquals("x=0.5 y=0.25 z=3.141592653589793", euler.toString());

        return;
    }

    /**
     * Test of toDegString method, of class EulerYXZ.
     */
    @Test
    public void testToDegString() {
        System.out.println("toDegString");

        EulerYXZ euler;

        euler = new EulerYXZ(1.0, 2.0, 3.0);
        assertEquals("x=57.29577951308232 y=114.59155902616465 z=171.88733853924697", euler.toDegString());

        return;
    }

}
