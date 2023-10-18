/*
 */

package jp.sfjp.mikutoga.bin.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 *
 */
public class MmdEofExceptionTest {

    public MmdEofExceptionTest() {
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
