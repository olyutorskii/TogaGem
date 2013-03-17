/*
 */

package jp.sfjp.mikutoga.bin.parser;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class MmdFormatExceptionTest {

    public MmdFormatExceptionTest() {
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
     * Test of getMessage method, of class MmdFormatException.
     */
    @Test
    public void testGetMessage() {
        System.out.println("getMessage");

        MmdFormatException ex;

        ex = new MmdFormatException();
        assertNull(ex.getMessage());

        ex = new MmdFormatException("ERROR");
        assertEquals("ERROR", ex.getMessage());

        ex = new MmdFormatException(99);
        assertEquals("(position:99)", ex.getMessage());

        ex = new MmdFormatException("ERROR", 99);
        assertEquals("ERROR(position:99)", ex.getMessage());

        return;
    }

    /**
     * Test of toString method, of class MmdFormatException.
     */
    @Test
    public void testToString() {
        System.out.println("toString");

        MmdFormatException ex;

        ex = new MmdFormatException();
        assertNull(ex.getMessage());
        assertEquals(MmdFormatException.class.getName(), ex.toString());

        ex = new MmdFormatException("ERROR", 99);
        assertEquals(MmdFormatException.class.getName()
                + ": ERROR(position:99)",
                ex.toString() );

        return;
    }

    /**
     * Test of getPosition method, of class MmdFormatException.
     */
    @Test
    public void testGetPosition() {
        System.out.println("getPosition");

        MmdFormatException ex;

        ex = new MmdFormatException();
        assertTrue(ex.getPosition() < 0);

        ex = new MmdFormatException("ERROR");
        assertTrue(ex.getPosition() < 0);

        ex = new MmdFormatException(99);
        assertEquals(99, ex.getPosition());

        ex = new MmdFormatException("ERROR", 99);
        assertEquals(99, ex.getPosition());

        return;
    }

    /**
     * Test of hasPosition method, of class MmdFormatException.
     */
    @Test
    public void testHasPosition() {
        System.out.println("hasPosition");

        MmdFormatException ex;

        ex = new MmdFormatException();
        assertFalse(ex.hasPosition());

        ex = new MmdFormatException("ERROR");
        assertFalse(ex.hasPosition());

        ex = new MmdFormatException(99);
        assertTrue(ex.hasPosition());

        ex = new MmdFormatException("ERROR", 99);
        assertTrue(ex.hasPosition());

        return;
    }

}
