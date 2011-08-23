/*
 * motion & stage act
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import jp.sourceforge.mikutoga.vmd.FrameNumbered;
import jp.sourceforge.mikutoga.vmd.VmdConst;

/**
 * モーション及び演出情報。
 */
public class VmdMotion {

    private String modelName = VmdConst.MODELNAME_STAGEACT;

    private final NamedListMap<BoneMotion> bonePartMap;
    private final NamedListMap<MorphMotion> morphPartMap;

    private final List<CameraMotion>   cameraMotionList;
    private final List<LuminousMotion> luminousMotionList;
    private final List<ShadowMotion>   shadowMotionList;


    /**
     * コンストラクタ。
     */
    public VmdMotion(){
        super();

        this.bonePartMap  = new NamedListMap<BoneMotion>();
        this.morphPartMap = new NamedListMap<MorphMotion>();

        this.cameraMotionList   = new LinkedList<CameraMotion>();
        this.luminousMotionList = new LinkedList<LuminousMotion>();
        this.shadowMotionList   = new LinkedList<ShadowMotion>();

        return;
    }


    /**
     * モデル名を返す。
     * @return モデル名
     */
    public String getModelName(){
        return this.modelName;
    }

    /**
     * モデル名を設定する。
     * <p>このモーションがモデルモーションかステージ演出情報かは、
     * このモデル名で判別される。
     * @param modelName モデル名
     * @throws NullPointerException 引数がnull
     */
    public void setModelName(String modelName) throws NullPointerException{
        if(modelName == null) throw new NullPointerException();
        this.modelName = modelName;
        return;
    }

    /**
     * モデルモーションか否か判別する。
     * <p>判別は特殊なモデル名を持つか否かで決定される。
     * @return モデルモーションならtrue
     */
    public boolean isModelMotion(){
        if(VmdConst.isStageActName(this.modelName)){
            return false;
        }

        return true;
    }

    /**
     * 名前付きボーンモーションマップを返す。
     * @return 名前付きボーンモーションマップ
     */
    public NamedListMap<BoneMotion> getBonePartMap(){
        return this.bonePartMap;
    }

    /**
     * 名前付きモーフモーションマップを返す。
     * @return 名前付きモーフモーションマップ
     */
    public NamedListMap<MorphMotion> getMorphPartMap(){
        return this.morphPartMap;
    }

    /**
     * カメラモーションのリストを返す。
     * @return カメラモーションのリスト
     */
    public List<CameraMotion> getCameraMotionList(){
        return this.cameraMotionList;
    }

    /**
     * 照明モーションのリストを返す。
     * @return 照明モーションのリスト
     */
    public List<LuminousMotion> getLuminousMotionList(){
        return this.luminousMotionList;
    }

    /**
     * シャドウモーションのリストを返す。
     * @return シャドウモーションのリスト
     */
    public List<ShadowMotion> getShadowMotionList(){
        return this.shadowMotionList;
    }

    /**
     * ボーンモーションを追加する。
     * @param motion ボーンモーション
     */
    public void addBoneMotion(BoneMotion motion){
        String name = motion.getBoneName();
        this.bonePartMap.addNamedElement(name, motion);
        return;
    }

    /**
     * モーフモーションを追加する。
     * @param motion モーフモーション
     */
    public void addMorphMotion(MorphMotion motion){
        String name = motion.getMorphName();
        this.morphPartMap.addNamedElement(name, motion);
        return;
    }

    /**
     * 各データをフレーム番号順に昇順ソートする。
     */
    public void frameSort(){
        for(String name : this.bonePartMap.getNames()){
            List<BoneMotion> motionList =
                    this.bonePartMap.getNamedList(name);
            Collections.sort(motionList, FrameNumbered.COMPARATOR);
        }

        for(String name : this.morphPartMap.getNames()){
            List<MorphMotion> motionList =
                    this.morphPartMap.getNamedList(name);
            Collections.sort(motionList, FrameNumbered.COMPARATOR);
        }

        Collections.sort(this.cameraMotionList,   FrameNumbered.COMPARATOR);
        Collections.sort(this.luminousMotionList, FrameNumbered.COMPARATOR);
        Collections.sort(this.shadowMotionList,   FrameNumbered.COMPARATOR);

        return;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String toString(){
        String dash = "---";

        StringBuilder result = new StringBuilder();

        result.append("model name : ").append(this.modelName).append('\n');

        for(String boneName : this.bonePartMap.getNames()){
            List<BoneMotion> motionList =
                    this.bonePartMap.getNamedList(boneName);
            for(BoneMotion motion : motionList){
                result.append(dash).append('\n');
                result.append(motion.toString()).append('\n');
            }
        }

        for(String morphName : this.morphPartMap.getNames()){
            if(VmdConst.isBaseMorphName(morphName)) continue;
            List<MorphMotion> motionList =
                    this.morphPartMap.getNamedList(morphName);
            for(MorphMotion morph : motionList){
                result.append(morph.toString()).append('\n');
            }
        }

        for(CameraMotion camera : this.cameraMotionList){
            result.append(dash).append('\n');
            result.append(camera.toString()).append('\n');
        }

        for(LuminousMotion luminous : this.luminousMotionList){
            result.append(luminous.toString()).append('\n');
        }

        for(ShadowMotion shadow : this.shadowMotionList){
            result.append(shadow.toString()).append('\n');
        }

        return result.toString();
    }

}
