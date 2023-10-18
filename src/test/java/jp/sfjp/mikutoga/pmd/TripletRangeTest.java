/*
 */

package jp.sfjp.mikutoga.pmd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 *
 */
public class TripletRangeTest {

    public TripletRangeTest() {
    }

    /**
     * Test of method, of class TripletRange.
     */
    @Test
    public void test() {
        System.out.println("test");

        TripletRange range;

        range = new TripletRange();
        assertEquals(0.0f, range.getXFrom(), 0);
        assertEquals(0.0f, range.getXTo(), 0);
        assertEquals(0.0f, range.getYFrom(), 0);
        assertEquals(0.0f, range.getYTo(), 0);
        assertEquals(0.0f, range.getZFrom(), 0);
        assertEquals(0.0f, range.getZTo(), 0);

        range.setXRange(1.0f, 2.0f);
        assertEquals(1.0f, range.getXFrom(), 0);
        assertEquals(2.0f, range.getXTo(), 0);
        range.setXRange(2.0f, 1.0f);
        assertEquals(1.0f, range.getXFrom(), 0);
        assertEquals(2.0f, range.getXTo(), 0);
        assertTrue(range.isValidX(1.0f));
        assertTrue(range.isValidX(1.5f));
        assertTrue(range.isValidX(2.0f));
        assertFalse(range.isValidX(0.5f));
        assertFalse(range.isValidX(2.5f));

        range.setYRange(3.0f, 4.0f);
        assertEquals(3.0f, range.getYFrom(), 0);
        assertEquals(4.0f, range.getYTo(), 0);
        range.setYRange(4.0f, 3.0f);
        assertEquals(3.0f, range.getYFrom(), 0);
        assertEquals(4.0f, range.getYTo(), 0);
        assertTrue(range.isValidY(3.0f));
        assertTrue(range.isValidY(3.5f));
        assertTrue(range.isValidY(4.0f));
        assertFalse(range.isValidY(2.5f));
        assertFalse(range.isValidY(4.5f));

        range.setZRange(5.0f, 6.0f);
        assertEquals(5.0f, range.getZFrom(), 0);
        assertEquals(6.0f, range.getZTo(), 0);
        range.setZRange(6.0f, 5.0f);
        assertEquals(5.0f, range.getZFrom(), 0);
        assertEquals(6.0f, range.getZTo(), 0);
        assertTrue(range.isValidZ(5.0f));
        assertTrue(range.isValidZ(5.5f));
        assertTrue(range.isValidZ(6.0f));
        assertFalse(range.isValidZ(4.5f));
        assertFalse(range.isValidZ(6.5f));

        assertEquals("x=[1.0 - 2.0] y=[3.0 - 4.0] z=[5.0 - 6.0]", range.toString());

        return;
    }

}
