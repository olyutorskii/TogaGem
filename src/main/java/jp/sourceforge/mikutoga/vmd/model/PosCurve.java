/*
 * position curve
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.model;

/**
 * 3次元位置移動の補間情報。
 * <p>XYZ3軸それぞれに対応するベジェ曲線を3本持つ。
 */
public class PosCurve {

    private final BezierParam intpltXpos = new BezierParam();
    private final BezierParam intpltYpos = new BezierParam();
    private final BezierParam intpltZpos = new BezierParam();


    /**
     * コンストラクタ。
     */
    public PosCurve(){
        super();
        return;
    }


    /**
     * ボーンX軸移動の補間曲線情報を返す。
     * @return ボーンX軸移動の補間曲線情報
     */
    public BezierParam getIntpltXpos(){
        return this.intpltXpos;
    }

    /**
     * ボーンY軸移動の補間曲線情報を返す。
     * @return ボーンY軸移動の補間曲線情報
     */
    public BezierParam getIntpltYpos(){
        return this.intpltYpos;
    }

    /**
     * ボーンZ軸移動の補間曲線情報を返す。
     * @return ボーンZ軸移動の補間曲線情報
     */
    public BezierParam getIntpltZpos(){
        return this.intpltZpos;
    }

    /**
     * インデックス指定された各軸の補間曲線情報を返す。
     * <p>インデックス値0がX軸、1がY軸、2がZ軸に対応する。
     * @param no インデックス値[0-2]
     * @return 各軸の補間曲線情報
     * @throws IllegalArgumentException インデックス値が範囲外
     */
    public BezierParam item(int no) throws IllegalArgumentException {
        BezierParam result;
        switch(no){
        case 0: result = this.intpltXpos; break;
        case 1: result = this.intpltYpos; break;
        case 2: result = this.intpltZpos; break;
        default:
            throw new IllegalArgumentException();
        }
        return result;
    }

    /**
     * 3軸ともMMDデフォルトの直線補間か判定する。
     * @return 3軸ともMMDデフォルトの直線補間ならtrue
     */
    public boolean isDefaultLinear(){
        if( ! this.intpltXpos.isDefaultLinear() ) return false;
        if( ! this.intpltYpos.isDefaultLinear() ) return false;
        if( ! this.intpltZpos.isDefaultLinear() ) return false;

        return true;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();

        result.append("X-Bezier ").append(this.intpltXpos).append('\n');
        result.append("Y-Bezier ").append(this.intpltYpos).append('\n');
        result.append("Z-Bezier ").append(this.intpltZpos);

        return result.toString();
    }

}
