/*
 */

package jp.sfjp.mikutoga.bin.export;

import java.nio.charset.CharacterCodingException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 *
 */
public class IllegalTextExportExceptionTest {

    public IllegalTextExportExceptionTest() {
    }

    @Test
    public void testSomeMethod() {
        System.out.println("constructor");

        IllegalTextExportException ex;

        ex = new IllegalTextExportException();
        assertNull(ex.getCause());
        assertNull(ex.getMessage());

        ex = new IllegalTextExportException("msg");
        assertNull(ex.getCause());
        assertEquals("msg", ex.getMessage());

        CharacterCodingException cex = new CharacterCodingException();
        ex = new IllegalTextExportException(cex);
        assertEquals(cex, ex.getCause());
        assertEquals(cex.toString(), ex.getMessage());

        ex = new IllegalTextExportException("msg", cex);
        assertEquals(cex, ex.getCause());
        assertEquals("msg", ex.getMessage());

        return;
    }

}
