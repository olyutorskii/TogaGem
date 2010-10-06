/*
 *
 */

package jp.sourceforge.mikutoga.parser;

import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 */
public class ParseStageTest extends TestCase {

    public ParseStageTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of toString method, of class ParseStage.
     */
    @Test
    public void testToString(){
        System.out.println("toString");

        ParseStage instance;

        instance = new ParseStage();
        assertEquals("", instance.toString());

        instance = new ParseStage("ABC");
        assertEquals("ABC", instance.toString());

        return;
    }

}
