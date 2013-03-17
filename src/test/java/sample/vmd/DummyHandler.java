/*
 * sample handler
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package sample.vmd;

import jp.sfjp.mikutoga.bin.parser.MmdFormatException;
import jp.sfjp.mikutoga.bin.parser.ParseStage;
import jp.sourceforge.mikutoga.vmd.VmdConst;
import jp.sourceforge.mikutoga.vmd.parser.VmdBasicHandler;
import jp.sourceforge.mikutoga.vmd.parser.VmdCameraHandler;
import jp.sourceforge.mikutoga.vmd.parser.VmdLightingHandler;

/**
 * サンプルのハンドラ。
 * これはユニットテストではない。
 * 必要に応じて要所でデバッガのブレークポイントを設定しておくと便利。
 */
public class DummyHandler implements VmdBasicHandler,
                                       VmdLightingHandler,
                                       VmdCameraHandler {

    public DummyHandler(){
        super();
        return;
    }

    @Override
    public void vmdParseStart(){
        println("VMD parse start");
        return;
    }

    @Override
    public void vmdParseEnd(boolean hasMoreData){
        System.out.println("VMD parse end");
        if(hasMoreData){
            println("there is unknown data below");
        }
        return;
    }

    @Override
    public void loopStart(ParseStage stage, int loops){
        println("===== Loop start ===== * "+ loops);
        return;
    }

    @Override
    public void loopNext(ParseStage stage){
        return;
    }

    @Override
    public void loopEnd(ParseStage stage){
        println("===== Loop end =====");
        return;
    }

    @Override
    public void vmdHeaderInfo(byte[] header) throws MmdFormatException{
        println("header length = " + header.length);
    }

    @Override
    public void vmdModelName(String modelName) throws MmdFormatException{
        println("modelname = " + modelName);
        println("isStageAct="+VmdConst.isStageActName(modelName));
    }

    @Override
    public void vmdBoneMotion(String boneName, int keyFrameNo){
        println(boneName+":"+keyFrameNo);
        return;
    }

    @Override
    public void vmdBonePosition(float xPos, float yPos, float zPos){
        println("motion : x="+xPos+" y="+yPos+" z="+zPos);
        return;
    }

    @Override
    public void vmdBoneRotationQt(float qx, float qy, float qz, float qw){
        println("rotation : qx="+qx+" qy="+qy+" qz="+qz+" qw="+qw);
        return;
    }

    @Override
    public void vmdBoneIntpltXpos(byte xP1x, byte xP1y, byte xP2x, byte xP2y){
        println("interpolate(X) : P1=("+xP1x+","+xP1y+") P2=("+xP2x+","+xP2y+")");
        return;
    }

    @Override
    public void vmdBoneIntpltYpos(byte yP1x, byte yP1y, byte yP2x, byte yP2y){
        println("interpolate(Y) : P1=("+yP1x+","+yP1y+") P2=("+yP2x+","+yP2y+")");
        return;
    }

    @Override
    public void vmdBoneIntpltZpos(byte zP1x, byte zP1y, byte zP2x, byte zP2y){
        println("interpolate(Z) : P1=("+zP1x+","+zP1y+") P2=("+zP2x+","+zP2y+")");
        return;
    }

    @Override
    public void vmdBoneIntpltRot(byte rP1x, byte rP1y, byte rP2x, byte rP2y){
        println("interpolate(R) : P1=("+rP1x+","+rP1y+") P2=("+rP2x+","+rP2y+")");
        return;
    }

    @Override
    public void vmdMorphMotion(String morphName, int keyFrameNo, float flex){
        if(VmdConst.isBaseMorphName(morphName)) return;
        println(morphName+":"+keyFrameNo+" flex="+flex);
        return;
    }

    @Override
    public void vmdCameraMotion(int keyFrameNo){
        println("camera : frame#="+keyFrameNo);
        return;
    }

    @Override
    public void vmdCameraRange(float distance){
        println("camera : range="+distance);
        return;
    }

    @Override
    public void vmdCameraPosition(float xPos, float yPos, float zPos){
        println("camera : x="+xPos+" y="+yPos+" z="+zPos);
        return;
    }

    @Override
    public void vmdCameraRotation(float latitude, float longitude, float roll){
        println("camera : latitude="+latitude+" longtitude="+longitude+" roll="+roll);
        return;
    }

    @Override
    public void vmdCameraProjection(int angle, boolean hasPerspective){
        println("camera : anglet="+angle+" perspective="+hasPerspective);
        return;
    }

    @Override
    public void vmdCameraIntpltXpos(byte p1x, byte p1y, byte p2x, byte p2y){
        println("cameraX : P1=("+p1x+","+p1y+") P2=("+p2x+","+p2y+")");
        return;
    }

    @Override
    public void vmdCameraIntpltYpos(byte p1x, byte p1y, byte p2x, byte p2y){
        println("cameraY : P1=("+p1x+","+p1y+") P2=("+p2x+","+p2y+")");
        return;
    }

    @Override
    public void vmdCameraIntpltZpos(byte p1x, byte p1y, byte p2x, byte p2y){
        println("cameraZ : P1=("+p1x+","+p1y+") P2=("+p2x+","+p2y+")");
        return;
    }

    @Override
    public void vmdCameraIntpltRotation(byte p1x, byte p1y, byte p2x, byte p2y){
        println("cameraRot : P1=("+p1x+","+p1y+") P2=("+p2x+","+p2y+")");
        return;
    }

    @Override
    public void vmdCameraIntpltRange(byte p1x, byte p1y, byte p2x, byte p2y){
        println("cameraRange : P1=("+p1x+","+p1y+") P2=("+p2x+","+p2y+")");
        return;
    }

    @Override
    public void vmdCameraIntpltProjection(byte p1x, byte p1y, byte p2x, byte p2y){
        println("cameraProjection : P1=("+p1x+","+p1y+") P2=("+p2x+","+p2y+")");
        return;
    }

    @Override
    public void vmdLuminousMotion(int keyFrameNo){
        println("light : frame#="+keyFrameNo);
        return;
    }

    @Override
    public void vmdLuminousColor(float rVal, float gVal, float bVal){
        println("light : color="+rVal+","+gVal+","+bVal);
        return;
    }

    @Override
    public void vmdLuminousDirection(float xVec, float yVec, float zVec){
        println("light : direction="+xVec+","+yVec+","+zVec);
        return;
    }

    @Override
    public void vmdShadowMotion(int keyFrameNo){
        println("shadow : frame#="+keyFrameNo);
        return;
    }

    @Override
    public void vmdShadowMode(byte shadowMode){
        println("shadow : mode="+shadowMode);
        return;
    }

    @Override
    public void vmdShadowScopeRaw(float shadowScope){
        println("shadow : scope="+shadowScope);
        return;
    }

    private void println(String msg){
        System.out.println(msg);
        return;
    }

}
