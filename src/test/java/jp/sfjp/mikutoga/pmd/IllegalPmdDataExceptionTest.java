/*
 */

package jp.sfjp.mikutoga.pmd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 *
 */
public class IllegalPmdDataExceptionTest {

    public IllegalPmdDataExceptionTest() {
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
