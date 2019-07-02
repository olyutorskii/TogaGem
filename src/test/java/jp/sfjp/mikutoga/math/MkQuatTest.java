/*
 */

package jp.sfjp.mikutoga.math;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public strictfp class MkQuatTest {

    private static final double RAD_90DEG = StrictMath.PI / 2.0;
    private static final double RAD_60DEG = StrictMath.PI / 3.0;
    private static final double RAD_45DEG = StrictMath.PI / 4.0;
    private static final double RAD_30DEG = StrictMath.PI / 6.0;
    private static final double RAD_15DEG = StrictMath.PI / 12.0;
    private static final double EPSILON = StrictMath.ulp(1.0);

    public MkQuatTest() {
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
     * StrictMath.toRadians(6) results are not same between JDK8 & JDK9.
     */
    private static double toRadians(double deg){
        double result = deg * 0.01745329251994329576924;
//        assert StrictMath.toRadians(6) == 0.10471975511965977; //JDK8
//        assert StrictMath.toRadians(6) == 0.10471975511965978; //JDK9
        return result;
    }

    private static void assert0UlpEquals(double expected, double result){
        assertUlpEquals(expected, result, 0);
        return;
    }

    private static void assertUlpEquals(double expected, double result, int ulpNum){
        double ulpExpected = StrictMath.ulp(expected);
        double ulpResult   = StrictMath.ulp(result);
        double ulp = StrictMath.max(ulpExpected, ulpResult);
        double delta = ulp * ulpNum;

        try{
            assertEquals(expected, result, delta);
        }catch(AssertionError e){
            double dist = StrictMath.abs(expected - result);
            String msg = e.getMessage();
            msg += " ULP * " + (dist / ulp);
            AssertionError err = new AssertionError(msg);
            err.initCause(e);
            throw err;
        }

        return;
    }

    /**
     * Test of constructor, of class MkQuat.
     */
    @Test
    public void testCons() {
        System.out.println("constructor");

        MkQuat qq;

        qq = new MkQuat();
        assert0UlpEquals(0.0, qq.getQ1());
        assert0UlpEquals(0.0, qq.getQ2());
        assert0UlpEquals(0.0, qq.getQ3());
        assert0UlpEquals(1.0, qq.getQW());

        qq.setQ1(2.0);
        assert0UlpEquals(2.0, qq.getQ1());
        assert0UlpEquals(0.0, qq.getQ2());
        assert0UlpEquals(0.0, qq.getQ3());
        assert0UlpEquals(1.0, qq.getQW());

        qq.setQ2(3.0);
        assert0UlpEquals(2.0, qq.getQ1());
        assert0UlpEquals(3.0, qq.getQ2());
        assert0UlpEquals(0.0, qq.getQ3());
        assert0UlpEquals(1.0, qq.getQW());

        qq.setQ3(4.0);
        assert0UlpEquals(2.0, qq.getQ1());
        assert0UlpEquals(3.0, qq.getQ2());
        assert0UlpEquals(4.0, qq.getQ3());
        assert0UlpEquals(1.0, qq.getQW());

        qq.setQW(5.0);
        assert0UlpEquals(2.0, qq.getQ1());
        assert0UlpEquals(3.0, qq.getQ2());
        assert0UlpEquals(4.0, qq.getQ3());
        assert0UlpEquals(5.0, qq.getQW());

        qq.setQ123W(6.0, 7.0, 8.0, 9.0);
        assert0UlpEquals(6.0, qq.getQ1());
        assert0UlpEquals(7.0, qq.getQ2());
        assert0UlpEquals(8.0, qq.getQ3());
        assert0UlpEquals(9.0, qq.getQW());

        qq = new MkQuat(0.1, 0.2, 0.3, 0.4);
        assert0UlpEquals(0.1, qq.getQ1());
        assert0UlpEquals(0.2, qq.getQ2());
        assert0UlpEquals(0.3, qq.getQ3());
        assert0UlpEquals(0.4, qq.getQW());

        MkQuat qqx = new MkQuat(1.0, 2.0, 3.0, 4.0);
        qq = new MkQuat(qqx);
        assert0UlpEquals(1.0, qq.getQ1());
        assert0UlpEquals(2.0, qq.getQ2());
        assert0UlpEquals(3.0, qq.getQ3());
        assert0UlpEquals(4.0, qq.getQW());

        return;
    }

    /**
     * Test of mul method, of class MkQuat.
     */
    @Test
    public void testMul() {
        System.out.println("mul");

        MkQuat qqa;
        MkQuat qqb;
        MkQuat result;

        result = new MkQuat();

        qqa = new MkQuat(1.0, 2.0, 3.0, 4.0);
        qqb = new MkQuat(5.0, 6.0, 7.0, 8.0);
        MkQuat.mul(qqa, qqb, result);
        assert0UlpEquals(24.0, result.getQ1());
        assert0UlpEquals(48.0, result.getQ2());
        assert0UlpEquals(48.0, result.getQ3());
        assert0UlpEquals(-6.0, result.getQW());

        qqa = new MkQuat(1.0, 2.0, 3.0, 4.0);
        qqb = new MkQuat(6.0, 7.0, 8.0, 9.0);
        MkQuat.mul(qqa, qqb, result);
        assert0UlpEquals(28.0, result.getQ1());
        assert0UlpEquals(56.0, result.getQ2());
        assert0UlpEquals(54.0, result.getQ3());
        assert0UlpEquals(-8.0, result.getQW());

        EulerYXZ eu = new EulerYXZ();
        qqa.setEulerYXZ(0.0, RAD_60DEG, 0.0);
        qqb.setEulerYXZ(RAD_30DEG, 0.0, 0.0);
        MkQuat.mul(qqa, qqb, result);
        result.toEulerYXZ(eu, 0.0);
        assertUlpEquals(RAD_30DEG, eu.getXRot(), 1);
        assertUlpEquals(RAD_60DEG, eu.getYRot(), 0);
        assertEquals(0.0, eu.getZRot(), EPSILON);

        qqa.setEulerYXZ(0.0, RAD_15DEG, 0.0);
        qqb.setEulerYXZ(0.0, RAD_30DEG, 0.0);
        MkQuat.mul(qqa, qqb, result);
        result.toEulerYXZ(eu, 0.0);
        assert0UlpEquals(0.0, eu.getXRot());
        assert0UlpEquals(RAD_45DEG, eu.getYRot());
        assert0UlpEquals(0.0, eu.getZRot());

        return;
    }

    /**
     * Test of conjugate method, of class MkQuat.
     */
    @Test
    public void testConjugate() {
        System.out.println("conjugate");

        MkQuat qq;
        MkQuat result;

        qq = new MkQuat(1.0, 2.0, 3.0, 4.0);
        result = new MkQuat();
        MkQuat.conjugate(qq, result);
        assert0UlpEquals(-1.0, result.getQ1());
        assert0UlpEquals(-2.0, result.getQ2());
        assert0UlpEquals(-3.0, result.getQ3());
        assert0UlpEquals(4.0, result.getQW());

        return;
    }

    /**
     * Test of normalize method, of class MkQuat.
     */
    @Test
    public void testNormalize() {
        System.out.println("normalize");

        MkQuat qq;
        MkQuat result;

        qq = new MkQuat();
        result = new MkQuat();
        MkQuat.normalize(qq, result);
        assert0UlpEquals(0.0, result.getQ1());
        assert0UlpEquals(0.0, result.getQ2());
        assert0UlpEquals(0.0, result.getQ3());
        assert0UlpEquals(1.0, result.getQW());

        qq = new MkQuat(2.0, 2.0, 2.0, 2.0);
        MkQuat.normalize(qq, result);
        assert0UlpEquals(0.5, result.getQ1());
        assert0UlpEquals(0.5, result.getQ2());
        assert0UlpEquals(0.5, result.getQ3());
        assert0UlpEquals(0.5, result.getQW());

        return;
    }

    /**
     * Test of inverse method, of class MkQuat.
     */
    @Test
    public void testInverse() {
        System.out.println("inverse");

        MkQuat qq;
        MkQuat result;

        qq = new MkQuat();
        result = new MkQuat();
        MkQuat.inverse(qq, result);
        assert0UlpEquals(0.0, result.getQ1());
        assert0UlpEquals(0.0, result.getQ2());
        assert0UlpEquals(0.0, result.getQ3());
        assert0UlpEquals(1.0, result.getQW());

        qq = new MkQuat(2.0, 2.0, 2.0, 2.0);
        MkQuat.inverse(qq, result);
        assert0UlpEquals(-0.125, result.getQ1());
        assert0UlpEquals(-0.125, result.getQ2());
        assert0UlpEquals(-0.125, result.getQ3());
        assert0UlpEquals(0.125, result.getQW());

        return;
    }

    /**
     * Test of abs method, of class MkQuat.
     */
    @Test
    public void testAbs() {
        System.out.println("abs");

        MkQuat qq;

        qq = new MkQuat();
        assert0UlpEquals(1.0, qq.abs());

        qq = new MkQuat(0.0, 0.0, 0.0, 0.0);
        assert0UlpEquals(0.0, qq.abs());

        qq = new MkQuat(1.0, 2.0, 0.0, 2.0);
        assert0UlpEquals(3.0, qq.abs());

        qq = new MkQuat(2.0, 2.0, 2.0, 2.0);
        assert0UlpEquals(4.0, qq.abs());

        return;
    }

    /**
     * Test of setPos3D method, of class MkQuat.
     */
    @Test
    public void testReadPos3D_3args() {
        System.out.println("readPos3D");

        MkQuat qq;

        qq = new MkQuat(9, 9, 9, 9);
        qq.setPos3D(1.0, 2.0, 3.0);
        assert0UlpEquals(1.0, qq.getQ1());
        assert0UlpEquals(2.0, qq.getQ2());
        assert0UlpEquals(3.0, qq.getQ3());
        assert0UlpEquals(0.0, qq.getQW());

        return;
    }

    /**
     * Test of setPos3D method, of class MkQuat.
     */
    @Test
    public void testReadPos3D_MkPos3D() {
        System.out.println("readPos3D");

        MkQuat qq;
        MkPos3D pos;

        qq = new MkQuat(9, 9, 9, 9);
        pos = new MkPos3D(1.0, 2.0, 3.0);
        qq.setPos3D(pos);
        assert0UlpEquals(1.0, qq.getQ1());
        assert0UlpEquals(2.0, qq.getQ2());
        assert0UlpEquals(3.0, qq.getQ3());
        assert0UlpEquals(0.0, qq.getQW());

        return;
    }

    /**
     * Test of setEulerYXZ method, of class MkQuat.
     */
    @Test
    public void testReadEulerYXZ_3args() {
        System.out.println("readEulerYXZ");

        MkQuat qq;

        qq = new MkQuat(9, 9, 9, 9);
        qq.setEulerYXZ(0.0, 0.0, 0.0);
        assert0UlpEquals(0.0, qq.getQ1());
        assert0UlpEquals(0.0, qq.getQ2());
        assert0UlpEquals(0.0, qq.getQ3());
        assert0UlpEquals(1.0, qq.getQW());

        return;
    }

    /**
     * Test of setEulerYXZ method, of class MkQuat.
     */
    @Test
    public void testReadEulerYXZ_EulerYXZ() {
        System.out.println("readEulerYXZ");

        MkQuat qq;
        EulerYXZ yxz;

        qq = new MkQuat(9, 9, 9, 9);
        yxz = new EulerYXZ();
        qq.setEulerYXZ(yxz);
        assert0UlpEquals(0.0, qq.getQ1());
        assert0UlpEquals(0.0, qq.getQ2());
        assert0UlpEquals(0.0, qq.getQ3());
        assert0UlpEquals(1.0, qq.getQW());

        return;
    }

    /**
     * Test of toEulerYXZ method, of class MkQuat.
     */
    @Test
    public void testToEulerYXZ_EulerYXZ_double() {
        System.out.println("toEulerYXZ");

        EulerYXZ eu;
        MkQuat qq;

        qq = new MkQuat();

        qq.setEulerYXZ(1.0, 2.0, 3.0);
        eu = new EulerYXZ();
        qq.toEulerYXZ(eu, 0.0);
        assertUlpEquals(1.0, eu.getXRot(), 1);
        assertUlpEquals(2.0, eu.getYRot(), 2);
        assertUlpEquals(3.0, eu.getZRot(), 0);

        // ジンバルロック

        qq.setEulerYXZ(RAD_90DEG, 0.0, 0.0);
        qq.toEulerYXZ(eu, 0.0);
        assert0UlpEquals(RAD_90DEG, eu.getXRot());
        assert0UlpEquals(0.0, eu.getYRot());
        assert0UlpEquals(0.0, eu.getZRot());

        qq.setEulerYXZ(RAD_90DEG, RAD_30DEG, 0.0);
        qq.toEulerYXZ(eu, 0.0);
        assert0UlpEquals(RAD_90DEG, eu.getXRot());
        assert0UlpEquals(0.0, eu.getYRot());
        assertUlpEquals(-RAD_30DEG, eu.getZRot(), 1);

        qq.toEulerYXZ(eu, RAD_15DEG);
        assert0UlpEquals(RAD_90DEG, eu.getXRot());
        assert0UlpEquals(RAD_15DEG, eu.getYRot());
        assertUlpEquals(-RAD_15DEG, eu.getZRot(), 2);

        qq.setEulerYXZ(-RAD_90DEG, RAD_30DEG, 0.0);
        qq.toEulerYXZ(eu, 0.0);
        assert0UlpEquals(-RAD_90DEG, eu.getXRot());
        assert0UlpEquals(0.0, eu.getYRot());
        assertUlpEquals(RAD_30DEG, eu.getZRot(), 1);

        qq.toEulerYXZ(eu, RAD_15DEG);
        assert0UlpEquals(-RAD_90DEG, eu.getXRot());
        assert0UlpEquals(RAD_15DEG, eu.getYRot());
        assertUlpEquals(RAD_15DEG, eu.getZRot(), 2);

        qq.setEulerYXZ(RAD_90DEG, RAD_45DEG, 0.0);
        qq.toEulerYXZ(eu, 0.0);
        assert0UlpEquals(RAD_90DEG, eu.getXRot());
        assert0UlpEquals(0.0, eu.getYRot());
        assert0UlpEquals(-RAD_45DEG, eu.getZRot());

        qq.setEulerYXZ(RAD_90DEG, RAD_45DEG, RAD_45DEG);
        qq.toEulerYXZ(eu, 0.0);
        assert0UlpEquals(RAD_90DEG, eu.getXRot());
        assert0UlpEquals(0.0, eu.getYRot());
        assertEquals(0.0, eu.getZRot(), EPSILON);

        qq.setEulerYXZ(RAD_90DEG, RAD_30DEG, RAD_60DEG);
        qq.toEulerYXZ(eu, 0.0);
        assert0UlpEquals(RAD_90DEG, eu.getXRot());
        assert0UlpEquals(0.0, eu.getYRot());
        assert0UlpEquals(RAD_30DEG, eu.getZRot());

        qq.setEulerYXZ(RAD_30DEG, RAD_45DEG, RAD_45DEG);
        qq.toEulerYXZ(eu, 0.0);
        assertUlpEquals(RAD_30DEG, eu.getXRot(), 1);
        assertUlpEquals(RAD_45DEG, eu.getYRot(), 1);
        assertUlpEquals(RAD_45DEG, eu.getZRot(), 1);

        double xRad;
        double yRad;
        double zRad;

        xRad = toRadians(95);
        yRad = toRadians(0);
        zRad = toRadians(0);
        qq.setEulerYXZ(xRad, yRad, zRad);
        qq.toEulerYXZ(eu, yRad);
        assertUlpEquals(toRadians(85), eu.getXRot(), 1);
        assertUlpEquals(toRadians(180), eu.getYRot(), 0);
        assertUlpEquals(toRadians(180), eu.getZRot(), 0);

        return;
    }

    /**
     * Test of critical case, of class MkQuat.
     */
    @Test
    public void testCritical() {
        System.out.println("critical");

        EulerYXZ eu;
        MkQuat qq;
        double xRad;
        double yRad;
        double zRad;

        qq = new MkQuat();
        eu = new EulerYXZ();

        xRad = toRadians(89);
        yRad = toRadians(80);
        zRad = toRadians(41);
        qq.setEulerYXZ(xRad, yRad, zRad);
        qq.toEulerYXZ(eu, 0.0);
        assertUlpEquals(xRad, eu.getXRot(), 164);
        assertUlpEquals(yRad, eu.getYRot(), 143);
        assertUlpEquals(zRad, eu.getZRot(), 211);

        // ジンバルロック判定境界ケース
        xRad = toRadians(90);
        yRad = toRadians(6);
        zRad = toRadians(7);
        qq.setEulerYXZ(xRad, yRad, zRad);
        qq.toEulerYXZ(eu, yRad);
        assert0UlpEquals(xRad, eu.getXRot());
        assert0UlpEquals(yRad, eu.getYRot());
        assertUlpEquals(zRad, eu.getZRot(), 1);

        xRad = toRadians(90);
        yRad = toRadians(-120);
        zRad = toRadians(120);
        qq.setEulerYXZ(xRad, yRad, zRad);
        qq.toEulerYXZ(eu, yRad);
        assert0UlpEquals(xRad, eu.getXRot());
        assert0UlpEquals(yRad, eu.getYRot());
        assertUlpEquals(zRad, eu.getZRot(), 1);

        xRad = toRadians(89.999);
        yRad = toRadians(89.999);
        zRad = toRadians(89.999);
        qq.setEulerYXZ(xRad, yRad, zRad);
        qq.toEulerYXZ(eu, yRad);
        assertUlpEquals(xRad, eu.getXRot(), 83029);
        assertUlpEquals(yRad, eu.getYRot(), 91782);
        assertUlpEquals(zRad, eu.getZRot(), 91782);

        return;
    }

    /**
     * Test of toEulerYXZ method, of class MkQuat.
     */
    @Test
    public void testToEulerYXZ_EulerYXZ() {
        System.out.println("toEulerYXZ");

        EulerYXZ eu;
        MkQuat qq;

        qq = new MkQuat();

        qq.setEulerYXZ(1.0, 2.0, 3.0);
        eu = new EulerYXZ();
        qq.toEulerYXZ(eu);
        assertUlpEquals(1.0, eu.getXRot(), 1);
        assertUlpEquals(2.0, eu.getYRot(), 2);
        assertUlpEquals(3.0, eu.getZRot(), 0);

        qq.setEulerYXZ(StrictMath.PI, 0.0, 0.0);
        qq.toEulerYXZ(eu);
        assertEquals(0.0, eu.getXRot(), EPSILON);
        assert0UlpEquals(StrictMath.PI, eu.getYRot());
        assert0UlpEquals(StrictMath.PI, eu.getZRot());

        return;
    }

    /**
     * Test of rotatePos method, of class MkQuat.
     */
    @Test
    public void testRotatePos() {
        System.out.println("rotatePos");

        MkPos3D pos;
        MkPos3D result;
        MkQuat qq;

        qq = new MkQuat();

        pos = new MkPos3D(1.0, 1.0, 1.0);
        result = new MkPos3D();

        // No Rotation
        qq.setEulerYXZ(0.0, 0.0, 0.0);
        qq.rotatePos(pos, result);
        assert0UlpEquals(1.0, result.getXpos());
        assert0UlpEquals(1.0, result.getYpos());
        assert0UlpEquals(1.0, result.getZpos());

        // test Left Hand Rotation
        qq.setEulerYXZ(0.0, RAD_90DEG, 0.0);
        qq.rotatePos(pos, result);
        assertUlpEquals(1.0, result.getXpos(), 0);
        assertUlpEquals(1.0, result.getYpos(), 0);
        assertUlpEquals(-1.0, result.getZpos(), 2);

        qq.setEulerYXZ(RAD_90DEG, 0.0, 0.0);
        qq.rotatePos(pos, result);
        assertUlpEquals(1.0, result.getXpos(), 0);
        assertUlpEquals(-1.0, result.getYpos(), 2);
        assertUlpEquals(1.0, result.getZpos(), 0);

        qq.setEulerYXZ(0.0, 0.0, RAD_90DEG);
        qq.rotatePos(pos, result);
        assertUlpEquals(-1.0, result.getXpos(), 2);
        assertUlpEquals(1.0, result.getYpos(), 0);
        assertUlpEquals(1.0, result.getZpos(), 0);

        // test Euler axis order

        qq.setEulerYXZ(RAD_90DEG, RAD_90DEG, 0.0);
        qq.rotatePos(pos, result);
        assertUlpEquals(1.0, result.getXpos(), 2);
        assertUlpEquals(-1.0, result.getYpos(), 1);
        assertUlpEquals(-1.0, result.getZpos(), 1);

        qq.setEulerYXZ(0.0, RAD_90DEG, RAD_90DEG);
        qq.rotatePos(pos, result);
        assertUlpEquals(1.0, result.getXpos(), 1);
        assertUlpEquals(1.0, result.getYpos(), 1);
        assertUlpEquals(1.0, result.getZpos(), 0);

        qq.setEulerYXZ(RAD_90DEG, 0.0, RAD_90DEG);
        qq.rotatePos(pos, result);
        assertUlpEquals(-1.0, result.getXpos(), 1);
        assertUlpEquals(-1.0, result.getYpos(), 1);
        assertUlpEquals(1.0, result.getZpos(), 2);

        pos = new MkPos3D(1.0, 2.0, 3.0);

        qq.setEulerYXZ(0.0, RAD_90DEG, 0.0);
        qq.rotatePos(pos, result);
        assertUlpEquals(3.0, result.getXpos(), 0);
        assertUlpEquals(2.0, result.getYpos(), 0);
        assertUlpEquals(-1.0, result.getZpos(), 3);

        qq.setEulerYXZ(RAD_90DEG, 0.0, 0.0);
        qq.rotatePos(pos, result);
        assertUlpEquals(1.0, result.getXpos(), 0);
        assertUlpEquals(-3.0, result.getYpos(), 0);
        assertUlpEquals(2.0, result.getZpos(), 2);

        qq.setEulerYXZ(0.0, 0.0, RAD_90DEG);
        qq.rotatePos(pos, result);
        assertUlpEquals(-2.0, result.getXpos(), 0);
        assertUlpEquals(1.0, result.getYpos(), 2);
        assertUlpEquals(3.0, result.getZpos(), 0);

        // rotate origin point

        pos = new MkPos3D(0.0, 0.0, 0.0);
        qq.setEulerYXZ(RAD_15DEG, RAD_30DEG, RAD_45DEG);
        qq.rotatePos(pos, result);
        assert0UlpEquals(0.0, result.getXpos());
        assert0UlpEquals(0.0, result.getYpos());
        assert0UlpEquals(0.0, result.getZpos());

        return;
    }

    /**
     * Test of full matching, of class MkQuat.
     */
//    @Test
    public void testFullMatch(){
        System.out.println("full match");

        for(int ix = -5; ix <= 90; ix++){
            for(int iy = -5; iy <= 179; iy++){
                for(int iz = -5; iz <= 179; iz++){
                    try{
                        rotTest(ix, iy, iz);
                    }catch(AssertionError e){
                        System.out.println(""+ix+":"+iy+":"+iz);
                        throw e;
                    }
                }
            }
        }

        return;
    }

    /**
     * Test of full matching, of class MkQuat.
     */
//    @Test
    public void testFullMatchRev(){
        System.out.println("full match rev");

        for(int ix = -90; ix <= 5; ix++){
            for(int iy = -179; iy <= 5; iy++){
                for(int iz = -179; iz <= 5; iz++){
                    try{
                        rotTest(ix, iy, iz);
                    }catch(AssertionError e){
                        System.out.println(""+ix+":"+iy+":"+iz);
                        throw e;
                    }
                }
            }
        }

        return;
    }

    private void rotTest(double ix, double iy, double iz){
        EulerYXZ result;
        MkQuat qq;

        qq = new MkQuat();
        result = new EulerYXZ();

        double xRad = toRadians(ix);
        double yRad = toRadians(iy);
        double zRad = toRadians(iz);

        qq.setEulerYXZ(xRad, yRad, zRad);
        qq.toEulerYXZ(result, yRad);

        final double DELTA = EPSILON * 164;

        assertEquals(xRad, result.getXRot(), DELTA);
        assertEquals(yRad, result.getYRot(), DELTA);
        assertEquals(zRad, result.getZRot(), DELTA);

        return;
    }

    /**
     * Test of toString method, of class MkQuat.
     */
    @Test
    public void testToString() {
        System.out.println("toString");

        MkQuat qq;

        qq = new MkQuat(-0.0, Double.MIN_NORMAL, Double.MAX_EXPONENT, StrictMath.PI);
        assertEquals("q1=-0.0 q2=2.2250738585072014E-308 q3=1023.0 w=3.141592653589793", qq.toString());

        return;
    }

}
