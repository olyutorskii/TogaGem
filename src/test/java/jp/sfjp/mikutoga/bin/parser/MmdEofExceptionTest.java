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
public class MmdEofExceptionTest {

    public MmdEofExceptionTest() {
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

    @Test
    public void testSomeMethod() {

        MmdFormatException ex;

        ex = new MmdEofException();
        assertNull(ex.getMessage());

        ex = new MmdEofException("ERROR");
        assertEquals("ERROR", ex.getMessage());

        ex = new MmdEofException(99);
        assertEquals("(position:99)", ex.getMessage());

        ex = new MmdEofException("ERROR", 99);
        assertEquals("ERROR(position:99)", ex.getMessage());

        return;
    }

}
