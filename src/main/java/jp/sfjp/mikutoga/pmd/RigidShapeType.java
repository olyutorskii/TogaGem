/*
 * rigid shape type
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sfjp.mikutoga.pmd;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 剛体の形状種別。
 * <ul>
 * <li>0x00:球
 * <li>0x01:箱
 * <li>0x02:カプセル
 * </ul>
 */
public enum RigidShapeType {

    /** 球。 */
    SPHERE(0x00),
    /** 箱。 */
    BOX(0x01),
    /** カプセル。 */
    CAPSULE(0x02),
    ;

    private static final ResourceBundle.Control NOFALLBACK;
    private static final String FAMILY_NAME =
            "jp.sfjp.mikutoga.pmd.resources.RigidShapeTypeName";

    static{
        List<String> rbforms = ResourceBundle.Control.FORMAT_DEFAULT;
        NOFALLBACK = ResourceBundle.Control.getNoFallbackControl(rbforms);

        String name = SPHERE.getClass().getPackage().getName();
        assert FAMILY_NAME.startsWith(name);
    }


    private final byte encoded;

    /**
     * コンストラクタ。
     *
     * @param code 符号化int値
     */
    RigidShapeType(int code){
        this((byte)code);
        return;
    }

    /**
     * コンストラクタ。
     *
     * @param code 符号化byte値
     */
    RigidShapeType(byte code){
        this.encoded = code;
        return;
    }

    /**
     * byte値からデコードする。
     *
     * @param code byte値
     * @return デコードされた列挙子。該当するものがなければnull
     */
    public static RigidShapeType decode(byte code){
        RigidShapeType result = null;

        for(RigidShapeType type : values()){
            if(type.encode() == code){
                result = type;
                break;
            }
        }

        return result;
    }

    /**
     * byte値にエンコードする。
     *
     * @return byte値
     */
    public byte encode(){
        return this.encoded;
    }

    /**
     * デフォルトロケールでの表示名を返す。
     *
     * @return 表示名
     */
    public String getGuiName(){
        Locale locale = Locale.getDefault();
        assert locale != null;
        return getGuiName(locale);
    }

    /**
     * ロケールに準じた表示名を返す。
     *
     * @param locale ロケール。nullならデフォルトロケールと解釈される。
     * @return 表示名
     */
    public String getGuiName(Locale locale){
        if(locale == null) return getGuiName();
        ResourceBundle rb =
                ResourceBundle.getBundle(FAMILY_NAME, locale, NOFALLBACK);
        String key = name();
        String result = rb.getString(key);
        return result;
    }

}
