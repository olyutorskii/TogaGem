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

        instance = new Test1("A.B.C");
        assertEquals("A.B.C", instance.toString());

        instance = new Test1("");
        assertEquals("", instance.toString());

        try{
            instance = new Test1(null);
            fail();
        }catch(NullPointerException e){
            // OK
        }

        instance = new Test2();
        assertEquals("ParseStageTest$Test2", instance.toString());

        return;
    }

    class Test1 extends ParseStage{
        public Test1(String txt){
            super(txt);
        }
    }

    class Test2 extends ParseStage{
        public Test2(){
            super();
        }
    }

}
