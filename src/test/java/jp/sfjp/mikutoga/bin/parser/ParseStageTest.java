/*
 */

package jp.sfjp.mikutoga.bin.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 *
 */
public class ParseStageTest {

    public ParseStageTest() {
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

    /**
     *
     */
    @Test
    public void testToString(){
        System.out.println("toString");

        ParseStage p = new ParseStage();

        assertEquals("parse stage#:" + p.getNo(), p.toString());

        return;
    }

}
