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
public class IllegalPmdDataExceptionTest {

    public IllegalPmdDataExceptionTest() {
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
        IllegalPmdDataException ex;

        ex = new IllegalPmdDataException();
        assertNull(ex.getCause());
        assertNull(ex.getMessage());

        ex = new IllegalPmdDataException("msg");
        assertNull(ex.getCause());
        assertEquals("msg", ex.getMessage());

        Throwable cex = new IllegalArgumentException();
        ex = new IllegalPmdDataException(cex);
        assertEquals(cex, ex.getCause());
        assertEquals(cex.toString(), ex.getMessage());

        return;
    }

}
