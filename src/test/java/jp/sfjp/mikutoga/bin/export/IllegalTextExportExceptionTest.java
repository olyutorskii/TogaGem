/*
 */

package jp.sfjp.mikutoga.bin.export;

import java.nio.charset.CharacterCodingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class IllegalTextExportExceptionTest {

    public IllegalTextExportExceptionTest() {
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
