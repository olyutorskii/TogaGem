/*
 * XML Schema datatypes input/output
 *
 * License : The MIT License
 * Copyright(c) 2019 MikuToga Partners
 */

package jp.sfjp.mikutoga.xml;

/**
 * XSD datatypes I/O utilities.
 *
 * <p>This class replaces javax.xml.bind.DatatypeConverter(JAXB) subset.
 * JAXB is not part of JDK9 or later.
 *
 * @see <a href="https://www.w3.org/TR/xmlschema-2/">
 * XML Schema Part 2: Datatypes Second Edition
 * </a>
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/javax/xml/bind/DatatypeConverter.html">
 * JavaSE8:DatatypeConverter
 * </a>
 */
public final class DatatypeIo {

    private static final String XSD_POS_INF = "INF";
    private static final String XSD_NEG_INF = "-INF";
    private static final String JAVA_INF = "Infinity";


    /**
     * Hidden constructor.
     */
    private DatatypeIo(){
        assert false;
    }

    /**
     * Converts an int value into a string as xsd:int.
     *
     * @param iVal int value
     * @return xsd:int type text
     */
    public static String printInt(int iVal){
        String result;
        result = String.valueOf(iVal);
        return result;
    }

    /**
     * Converts an float value into a string as xsd:float.
     *
     * <p>Infinite value will be "INF".
     *
     * @param fVal float value
     * @return xsd:float type text
     */
    public static String printFloat(float fVal){
        String result;

        if(fVal == Float.POSITIVE_INFINITY){
            result = XSD_POS_INF;
        }else if(fVal == Float.NEGATIVE_INFINITY){
            result = XSD_NEG_INF;
        }else{
            result = String.valueOf(fVal);
        }

        return result;
    }

    /**
     * trimming whitespace around XSD datatypes value.
     *
     * @param txt XSD value
     * @return trimmed text
     */
    public static CharSequence xsdTrim(CharSequence txt){
        int length = txt.length();
        int startPos = 0;
        int endPos = length;

        for(int pt = 0; pt < length; pt++){
            char ch = txt.charAt(pt);
            if(!isXsdWhitespace(ch)){
                startPos = pt;
                break;
            }
        }

        for(int pt = length - 1; pt >= 0; pt--){
            char ch = txt.charAt(pt);
            if(!isXsdWhitespace(ch)){
                endPos = pt + 1;
                break;
            }
        }

        CharSequence result = txt.subSequence(startPos, endPos);

        return result;
    }

    /**
     * checking whitespace character around XSD datattypes.
     *
     * <p>\n, \r, \t, and &#x5c;0020 are whitespace.
     *
     * @param ch character
     * @return true if whitespace
     */
    public static boolean isXsdWhitespace(char ch){
        boolean result;

        switch(ch){
        case '\n':
        case '\r':
        case '\t':
        case '\u0020':
            result = true;
            break;
        default:
            result = false;
            break;
        }

        return result;
    }

    /**
     * Converts the xsd:boolean string argument into a boolean value.
     *
     * <p>{"true", "1"} is true. {"false", "0"} is false.
     *
     * @param xsdVal xsd:boolean string
     * @return true if true
     * @throws IllegalArgumentException illegal xsd:boolean string
     */
    public static boolean parseBoolean(CharSequence xsdVal)
            throws IllegalArgumentException{
        boolean result;

        CharSequence trimmed = xsdTrim(xsdVal);

        if("true".contentEquals(trimmed)){
            result = true;
        }else if("false".contentEquals(trimmed)){
            result = false;
        }else if("0".contentEquals(trimmed)){
            result = false;
        }else if("1".contentEquals(trimmed)){
            result = true;
        }else{
            throw new IllegalArgumentException(trimmed.toString());
        }

        return result;
    }

    /**
     * Converts the xsd:byte string argument into a byte value.
     *
     * @param xsdVal xsd:byte string
     * @return byte value
     * @throws NumberFormatException illegal xsd:byte
     */
    public static byte parseByte(CharSequence xsdVal)
            throws NumberFormatException{
        CharSequence trimmed = xsdTrim(xsdVal);

        int iVal;
        iVal = Integer.parseInt(trimmed.toString());

        if(iVal < -128 || 127 < iVal){
            throw new NumberFormatException(xsdVal.toString());
        }

        byte result;
        result = (byte)iVal;

        return result;
    }

    /**
     * Converts the xsd:int string argument into a int value.
     *
     * @param xsdVal xsd:int string
     * @return int value
     * @throws NumberFormatException illegal xsd:int
     */
    public static int parseInt(CharSequence xsdVal)
            throws NumberFormatException{
        CharSequence trimmed = xsdTrim(xsdVal);
        int result;
        result = Integer.parseInt(trimmed.toString());
        return result;
    }

    /**
     * Converts the xsd:float string argument into a float value.
     *
     * @param xsdVal xsd:float string
     * @return float value
     * @throws NumberFormatException illegal xsd:float
     */
    public static float parseFloat(CharSequence xsdVal)
            throws NumberFormatException{
        String trimmed = xsdTrim(xsdVal).toString();

        float result;
        if(XSD_POS_INF.equals(trimmed)){
            result = Float.POSITIVE_INFINITY;
        }else if(XSD_NEG_INF.equals(trimmed)){
            result = Float.NEGATIVE_INFINITY;
        }else if(trimmed.endsWith(JAVA_INF)){
            throw new NumberFormatException(trimmed);
        }else if(trimmed.contains("x") || trimmed.contains("X")){
            // HexFloatingPointLiteral
            throw new NumberFormatException(trimmed);
        }else{
            // zero will happen when underflow.
            // infinite will happen when overflow.
            // NaN is NaN.
            // -0 is minus zero.
            result = Float.parseFloat(trimmed);
        }

        return result;
    }

}
