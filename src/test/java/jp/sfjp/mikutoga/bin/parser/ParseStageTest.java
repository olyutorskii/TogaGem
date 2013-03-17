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
public class ParseStageTest {

    public ParseStageTest() {
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
     * Test of getNo method, of class ParseStage.
     */
    @Test
    public void testGetNo() {
        System.out.println("getNo");

        ParseStage p1 = new ParseStage();
        ParseStage p2 = new ParseStage();
        ParseStage p3 = new ParseStage();

        assertEquals(1, p2.getNo() - p1.getNo());
        assertEquals(1, p3.getNo() - p2.getNo());

        return;
    }

}
