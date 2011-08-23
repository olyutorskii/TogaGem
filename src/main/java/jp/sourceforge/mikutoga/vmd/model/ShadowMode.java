/*
 * shadow mode
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.model;

/**
 * セルフシャドウ描画モード。
 */
public enum ShadowMode {

    /**
     * シャドウを描画しないモード。
     */
    NONE(0x00),

    /**
     * シャドウ描画の質がカメラからの距離の影響をあまり受けないモード。
     * <p>※MMDのUIにおけるデフォルト。
     */
    MODE_1(0x01),

    /**
     * シャドウ描画の質をカメラからの距離に応じて劣化させることにより、
     * カメラに近いオブジェクトのシャドウ描画の質を向上させるモード。
     */
    MODE_2(0x02),

    ;


    private static final int MASK_8 = 0xff;


    private final byte code;


    /**
     * コンストラクタ。
     * @param code 割り当て番号(byte)
     */
    private ShadowMode(byte code){
        this.code = code;
        return;
    }

    /**
     * コンストラクタ。
     * @param iVal 割り当て番号(int)
     * ※上位24bitは捨てられる。
     */
    private ShadowMode(int iVal){
        this((byte)(iVal & MASK_8));
        return;
    }


    /**
     * byte型値に符号化された時の値を返す。
     * @return byte型値に符号化された時の値
     */
    public byte getEncodedByte(){
        return this.code;
    }

    /**
     * byte型値から描画モードを復号する。
     * @param bVal byte型値
     * @return 描画モード。未定義の値が与えられた場合はnull。
     */
    public static ShadowMode decode(byte bVal){
        ShadowMode result = null;

        if     (bVal == NONE  .code) result = NONE;
        else if(bVal == MODE_1.code) result = MODE_1;
        else if(bVal == MODE_2.code) result = MODE_2;

        return result;
    }

    /**
     * int型値から描画モードを復号する。
     * <p>上位24bitは無視される。
     * @param iVal int型値
     * @return 描画モード。未定義の値が与えられた場合はnull。
     */
    public static ShadowMode decode(int iVal){
        return decode((byte)(iVal & MASK_8));
    }

}
