/*
 */

package jp.sfjp.mikutoga.pmd;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class Rad3dTest {

    public Rad3dTest() {
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
