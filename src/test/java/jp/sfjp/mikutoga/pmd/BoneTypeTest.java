/*
 */

package jp.sfjp.mikutoga.pmd;

import java.util.Locale;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class BoneTypeTest {

    public BoneTypeTest() {
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
     * Test of values method, of class BoneType.
     */
    @Test
    public void testValues() {
        System.out.println("values");

        BoneType[] array = BoneType.values();

        assertEquals(10, array.length);

        assertEquals(BoneType.ROTATE,      array[0]);
        assertEquals(BoneType.ROTMOV,      array[1]);
        assertEquals(BoneType.IK,          array[2]);
        assertEquals(BoneType.UNKNOWN,     array[3]);
        assertEquals(BoneType.UNDERIK,     array[4]);
        assertEquals(BoneType.UNDERROT,    array[5]);
        assertEquals(BoneType.IKCONNECTED, array[6]);
        assertEquals(BoneType.HIDDEN,      array[7]);
        assertEquals(BoneType.TWIST,       array[8]);
        assertEquals(BoneType.LINKEDROT,   array[9]);

        return;
    }

    /**
     * Test of decode method, of class BoneType.
     */
    @Test
    public void testDecode() {
        System.out.println("decode");

        assertEquals(BoneType.ROTATE,      BoneType.decode((byte)0x00));
        assertEquals(BoneType.ROTMOV,      BoneType.decode((byte)0x01));
        assertEquals(BoneType.IK,          BoneType.decode((byte)0x02));
        assertEquals(BoneType.UNKNOWN,     BoneType.decode((byte)0x03));
        assertEquals(BoneType.UNDERIK,     BoneType.decode((byte)0x04));
        assertEquals(BoneType.UNDERROT,    BoneType.decode((byte)0x05));
        assertEquals(BoneType.IKCONNECTED, BoneType.decode((byte)0x06));
        assertEquals(BoneType.HIDDEN,      BoneType.decode((byte)0x07));
        assertEquals(BoneType.TWIST,       BoneType.decode((byte)0x08));
        assertEquals(BoneType.LINKEDROT,   BoneType.decode((byte)0x09));

        assertNull(BoneType.decode((byte)0x0a));

        return;
    }

    /**
     * Test of encode method, of class BoneType.
     */
    @Test
    public void testEncode() {
        System.out.println("encode");

        assertEquals(0x00, BoneType.ROTATE.encode());
        assertEquals(0x01, BoneType.ROTMOV.encode());
        assertEquals(0x02, BoneType.IK.encode());
        assertEquals(0x03, BoneType.UNKNOWN.encode());
        assertEquals(0x04, BoneType.UNDERIK.encode());
        assertEquals(0x05, BoneType.UNDERROT.encode());
        assertEquals(0x06, BoneType.IKCONNECTED.encode());
        assertEquals(0x07, BoneType.HIDDEN.encode());
        assertEquals(0x08, BoneType.TWIST.encode());
        assertEquals(0x09, BoneType.LINKEDROT.encode());

        return;
    }

    /**
     * Test of getGuiName method, of class BoneType.
     */
    @Test
    public void testGetGuiName_0args() {
        System.out.println("getGuiName");

        Locale locale = Locale.getDefault();

        for(BoneType type : BoneType.values()){
            assertEquals(type.getGuiName(locale), type.getGuiName());
        }

        return;
    }

    /**
     * Test of getGuiName method, of class BoneType.
     */
    @Test
    public void testGetGuiName_Locale() {
        System.out.println("getGuiName");

        Locale locale;

        locale = Locale.JAPANESE;
        assertEquals("回転",           BoneType.ROTATE.getGuiName(locale));
        assertEquals("回転/移動",      BoneType.ROTMOV.getGuiName(locale));
        assertEquals("IK",             BoneType.IK.getGuiName(locale));
        assertEquals("不明",           BoneType.UNKNOWN.getGuiName(locale));
        assertEquals("IK影響下(回転)", BoneType.UNDERIK.getGuiName(locale));
        assertEquals("回転影響下",     BoneType.UNDERROT.getGuiName(locale));
        assertEquals("IK接続先",       BoneType.IKCONNECTED.getGuiName(locale));
        assertEquals("非表示",         BoneType.HIDDEN.getGuiName(locale));
        assertEquals("捩り",           BoneType.TWIST.getGuiName(locale));
        assertEquals("回転連動",       BoneType.LINKEDROT.getGuiName(locale));

        locale = Locale.JAPAN;
        assertEquals("回転",           BoneType.ROTATE.getGuiName(locale));

        locale = Locale.ITALY;
        assertEquals("Rotate",         BoneType.ROTATE.getGuiName(locale));
        assertEquals("Rotate/Move",    BoneType.ROTMOV.getGuiName(locale));
        assertEquals("IK",             BoneType.IK.getGuiName(locale));
        assertEquals("Unknown",        BoneType.UNKNOWN.getGuiName(locale));
        assertEquals("Under IK",       BoneType.UNDERIK.getGuiName(locale));
        assertEquals("Under rotate",   BoneType.UNDERROT.getGuiName(locale));
        assertEquals("IK connected",   BoneType.IKCONNECTED.getGuiName(locale));
        assertEquals("Hidden",         BoneType.HIDDEN.getGuiName(locale));
        assertEquals("Twist",          BoneType.TWIST.getGuiName(locale));
        assertEquals("Linked Rotate",  BoneType.LINKEDROT.getGuiName(locale));

        locale = Locale.ENGLISH;
        assertEquals("Rotate",         BoneType.ROTATE.getGuiName(locale));

        locale = Locale.US;
        assertEquals("Rotate",         BoneType.ROTATE.getGuiName(locale));

        return;
    }

}
