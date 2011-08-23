/*
 * basic information builder
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.vmd.model.binio;

import jp.sourceforge.mikutoga.math.MkPos3D;
import jp.sourceforge.mikutoga.math.MkQuat;
import jp.sourceforge.mikutoga.parser.MmdFormatException;
import jp.sourceforge.mikutoga.parser.ParseStage;
import jp.sourceforge.mikutoga.vmd.model.BezierParam;
import jp.sourceforge.mikutoga.vmd.model.BoneMotion;
import jp.sourceforge.mikutoga.vmd.model.MorphMotion;
import jp.sourceforge.mikutoga.vmd.model.PosCurve;
import jp.sourceforge.mikutoga.vmd.model.VmdMotion;
import jp.sourceforge.mikutoga.vmd.parser.VmdBasicHandler;

/**
 * ボーンモーション、モーフ情報のビルダ。
 */
class BasicLoader implements VmdBasicHandler {

    private final VmdMotion vmdMotion;

    private BoneMotion currentBoneMotion;
    private MorphMotion currentMorphMotion;

    private boolean hasMoreData;


    /**
     * コンストラクタ。
     * @param vmdMotion モーション情報
     */
    BasicLoader(VmdMotion vmdMotion){
        super();
        this.vmdMotion = vmdMotion;

        this.currentBoneMotion  = null;
        this.currentMorphMotion = null;

        this.hasMoreData = false;

        return;
    }


    /**
     * ボーンモーションループか否か判定する。
     * @param stage 判定対象
     * @return モーションループならtrue
     */
    private static boolean isBoneMotionList(ParseStage stage){
        if(stage == VmdBasicHandler.BONEMOTION_LIST) return true;
        return false;
    }

    /**
     * モーフループか否か判定する。
     * @param stage 判定対象
     * @return モーフループならtrue
     */
    private static boolean isMorphList(ParseStage stage){
        if(stage == VmdBasicHandler.MORPH_LIST) return true;
        return false;
    }


    /**
     * {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void vmdParseStart() throws MmdFormatException{
        // NOTHING
        return;
    }

    /**
     * {@inheritDoc}
     * @param hasMoreDataArg {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void vmdParseEnd(boolean hasMoreDataArg)
            throws MmdFormatException{
        this.hasMoreData = hasMoreDataArg;
        return;
    }

    /**
     * {@inheritDoc}
     * @param stage {@inheritDoc}
     * @param loops {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void loopStart(ParseStage stage, int loops)
            throws MmdFormatException{
        if(isBoneMotionList(stage)){
            this.currentBoneMotion = new BoneMotion();
        }else if(isMorphList(stage)){
            this.currentMorphMotion = new MorphMotion();
        }
        return;
    }

    /**
     * {@inheritDoc}
     * @param stage {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void loopNext(ParseStage stage)
            throws MmdFormatException{
        if(isBoneMotionList(stage)){
            this.vmdMotion.addBoneMotion(this.currentBoneMotion);
            this.currentBoneMotion = new BoneMotion();
        }else if(isMorphList(stage)){
            this.vmdMotion.addMorphMotion(this.currentMorphMotion);
            this.currentMorphMotion = new MorphMotion();
        }
        return;
    }

    /**
     * {@inheritDoc}
     * @param stage {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void loopEnd(ParseStage stage)
            throws MmdFormatException{
        if(isBoneMotionList(stage)){
            this.currentBoneMotion = null;
        }else if(isMorphList(stage)){
            this.currentMorphMotion = null;
        }
        return;
    }

    /**
     * {@inheritDoc}
     * @param header {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void vmdHeaderInfo(byte[] header) throws MmdFormatException{
        // NOTHING
        return;
    }

    /**
     * {@inheritDoc}
     * @param modelName {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void vmdModelName(String modelName) throws MmdFormatException{
        this.vmdMotion.setModelName(modelName);
        return;
    }

    /**
     * {@inheritDoc}
     * @param boneName {@inheritDoc}
     * @param keyFrameNo {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void vmdBoneMotion(String boneName, int keyFrameNo)
            throws MmdFormatException{
        this.currentBoneMotion.setBoneName(boneName);
        this.currentBoneMotion.setFrameNumber(keyFrameNo);
        return;
    }

    /**
     * {@inheritDoc}
     * @param qx {@inheritDoc}
     * @param qy {@inheritDoc}
     * @param qz {@inheritDoc}
     * @param qw {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void vmdBoneRotationQt(float qx, float qy, float qz, float qw)
            throws MmdFormatException{
        MkQuat rotation = this.currentBoneMotion.getRotation();
        rotation.setQ1(qx);
        rotation.setQ2(qy);
        rotation.setQ3(qz);
        rotation.setQW(qw);
        return;
    }

    /**
     * {@inheritDoc}
     * @param xPos {@inheritDoc}
     * @param yPos {@inheritDoc}
     * @param zPos {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void vmdBonePosition(float xPos, float yPos, float zPos)
            throws MmdFormatException{
        MkPos3D position = this.currentBoneMotion.getPosition();
        position.setPosition(xPos, yPos, zPos);
        return;
    }

    /**
     * {@inheritDoc}
     * @param rP1x {@inheritDoc}
     * @param rP1y {@inheritDoc}
     * @param rP2x {@inheritDoc}
     * @param rP2y {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void vmdBoneIntpltRot(byte rP1x, byte rP1y,
                                      byte rP2x, byte rP2y )
            throws MmdFormatException{
        BezierParam bezier = this.currentBoneMotion.getIntpltRotation();
        bezier.setP1(rP1x, rP1y);
        bezier.setP2(rP2x, rP2y);
        return;
    }

    /**
     * {@inheritDoc}
     * @param xP1x {@inheritDoc}
     * @param xP1y {@inheritDoc}
     * @param xP2x {@inheritDoc}
     * @param xP2y {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void vmdBoneIntpltXpos(byte xP1x, byte xP1y,
                                       byte xP2x, byte xP2y )
            throws MmdFormatException{
        PosCurve posCurve = this.currentBoneMotion.getPosCurve();
        BezierParam bezier = posCurve.getIntpltXpos();
        bezier.setP1(xP1x, xP1y);
        bezier.setP2(xP2x, xP2y);
        return;
    }

    /**
     * {@inheritDoc}
     * @param yP1x {@inheritDoc}
     * @param yP1y {@inheritDoc}
     * @param yP2x {@inheritDoc}
     * @param yP2y {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void vmdBoneIntpltYpos(byte yP1x, byte yP1y,
                                       byte yP2x, byte yP2y )
            throws MmdFormatException{
        PosCurve posCurve = this.currentBoneMotion.getPosCurve();
        BezierParam bezier = posCurve.getIntpltYpos();
        bezier.setP1(yP1x, yP1y);
        bezier.setP2(yP2x, yP2y);
        return;
    }

    /**
     * {@inheritDoc}
     * @param zP1x {@inheritDoc}
     * @param zP1y {@inheritDoc}
     * @param zP2x {@inheritDoc}
     * @param zP2y {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void vmdBoneIntpltZpos(byte zP1x, byte zP1y,
                                       byte zP2x, byte zP2y )
            throws MmdFormatException{
        PosCurve posCurve = this.currentBoneMotion.getPosCurve();
        BezierParam bezier = posCurve.getIntpltZpos();
        bezier.setP1(zP1x, zP1y);
        bezier.setP2(zP2x, zP2y);
        return;
    }

    /**
     * {@inheritDoc}
     * @param morphName {@inheritDoc}
     * @param keyFrameNo {@inheritDoc}
     * @param flex {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    public void vmdMorphMotion(String morphName, int keyFrameNo, float flex)
            throws MmdFormatException{
        this.currentMorphMotion.setMorphName(morphName);
        this.currentMorphMotion.setFrameNumber(keyFrameNo);
        this.currentMorphMotion.setFlex(flex);
        return;
    }

    /**
     * 読み残したデータがあるか判定する。
     * @return 読み残したデータがあればtrue
     */
    boolean hasMoreData(){
        return this.hasMoreData;
    }

}
