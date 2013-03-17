/*
 * sample handler
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package sample.pmd;

import jp.sfjp.mikutoga.bin.parser.ParseStage;
import jp.sourceforge.mikutoga.pmd.parser.PmdBasicHandler;
import jp.sourceforge.mikutoga.pmd.parser.PmdBoneHandler;
import jp.sourceforge.mikutoga.pmd.parser.PmdEngHandler;
import jp.sourceforge.mikutoga.pmd.parser.PmdJointHandler;
import jp.sourceforge.mikutoga.pmd.parser.PmdMaterialHandler;
import jp.sourceforge.mikutoga.pmd.parser.PmdMorphHandler;
import jp.sourceforge.mikutoga.pmd.parser.PmdRigidHandler;
import jp.sourceforge.mikutoga.pmd.parser.PmdShapeHandler;
import jp.sourceforge.mikutoga.pmd.parser.PmdToonHandler;

/**
 * サンプルのハンドラ。
 * これはユニットテストではない。
 * 必要に応じて要所でデバッガのブレークポイントを設定しておくと便利。
 */
public class DummyHandler
        implements PmdBasicHandler,
                   PmdBoneHandler,
                   PmdShapeHandler,
                   PmdMorphHandler,
                   PmdMaterialHandler,
                   PmdEngHandler,
                   PmdToonHandler,
                   PmdRigidHandler,
                   PmdJointHandler {

    public DummyHandler(){
        super();
        return;
    }

    @Override
    public void pmdParseStart(){
        System.out.println("PMD parse start");
        return;
    }

    @Override
    public void pmdParseEnd(boolean hasMoreData){
        System.out.println("PMD parse end");
        if(hasMoreData){
            System.out.println("thre is unknown data below");
        }
        return;
    }

    @Override
    public void loopStart(ParseStage stage, int loops){
        System.out.println("===== Loop start ===== * " + loops);
        return;
    }

    @Override
    public void loopNext(ParseStage stage){
        return;
    }

    @Override
    public void loopEnd(ParseStage stage){
        System.out.println("===== Loop end =====");
        return;
    }

    @Override
    public void pmdHeaderInfo(byte[] header){
        System.out.println("header length=" + header.length);
        return;
    }

    @Override
    public void pmdModelInfo(String modelName, String description){
        System.out.println("modelName=" + modelName);
        System.out.println("comment=" + description);
        return;
    }

    @Override
    public void pmdVertexPosition(float xPos, float yPos, float zPos){
//        System.out.println("x,y,z = " + xPos + "," + yPos + "," + zPos);
        return;
    }

    @Override
    public void pmdVertexNormal(float xVec, float yVec, float zVec){
//        System.out.println("x,y,z = " + xVec + "," + yVec + "," + zVec);
    }

    @Override
    public void pmdVertexUV(float uVal, float vVal){
        return;
    }

    @Override
    public void pmdVertexWeight(int boneId_1, int boneId_2,
                                  int weightForB1){
//        System.out.println("x,y,z = "
//        + boneId_1 + "," + boneId_2 + "," + weightForB1);
        return;
    }

    @Override
    public void pmdVertexEdge(boolean hideEdge){
//        System.out.println("x,y,z = " + hideEdge);
        return;
    }

    @Override
    public void pmdSurfaceTriangle(int vertexId_1,
                                      int vertexId_2,
                                      int vertexId_3){
//        System.out.println("v1,v2,v3 = "
//                + vertexId_1 + "," + vertexId_2 + "," + vertexId_3);
    }

    @Override
    public void pmdMaterialDiffuse(float red, float green, float blue,
                                      float alpha ){
//        System.out.println("diffuse rgba="
//        + red + "," + green + "," + blue + "," + alpha);
        return;
    }

    @Override
    public void pmdMaterialSpecular(float red, float green, float blue,
                                       float shininess){
//        System.out.println("specular rgbs="
//        + red + "," + green + "," + blue + "," + shininess);
        return;
    }

    @Override
    public void pmdMaterialAmbient(float red, float green, float blue){
//        System.out.println("ambient rgb=" + red + "," + green + "," + blue);
        return;
    }

    @Override
    public void pmdMaterialShading(int toon_idx,
                                      String textureFile, String sphereFile){
//        System.out.println("toon idx=" + toon_idx);
//        System.out.println("texfile=" + textureFile);
//        System.out.println("spherefile=" + sphereFile);
        return;
    }

    @Override
    public void pmdMaterialInfo(boolean hasEdge, int surfaceNum){
//        System.out.println("surfaces=" + surfaceNum);
        return;
    }

    @Override
    public void pmdBoneInfo(String boneName, byte boneKind){
//        System.out.println("bonename = " + boneName);
//        System.out.println("kind = " + boneKind);
        return;
    }

    @Override
    public void pmdBoneLink(int parentId, int tailId, int ikId){
//        System.out.println("parent = " + parentId);
//        System.out.println("tail = " + tailId);
//        System.out.println("ik = " + ikId);
        return;
    }

    @Override
    public void pmdBonePosition(float xPos, float yPos, float zPos){
//        System.out.println("x="+xPos+" y="+yPos+" z="+zPos);
        return;
    }

    @Override
    public void pmdIKInfo(int boneId, int targetId,
                           int depth, float weight ){
        return;
    }

    @Override
    public void pmdIKChainInfo(int childId){
//        System.out.println("chained bone = " + childId);
        return;
    }

    @Override
    public void pmdMorphInfo(String morphName, byte morphType){
//        System.out.println("morph name = " + morphName);
        return;
    }

    @Override
    public void pmdMorphVertexInfo(int vertexId,
                                 float xPos, float yPos, float zPos){
//        System.out.println("id="+vertexId+",x="+xPos+",y="+yPos+",z="+zPos);
        return;
    }

    @Override
    public void pmdMorphOrderInfo(int morphId){
//        System.out.println("morph idx = " + morphId);
        return;
    }

    @Override
    public void pmdBoneGroupInfo(String groupName){
//        System.out.println("group name = " + groupName);
        return;
    }

    @Override
    public void pmdGroupedBoneInfo(int boneId, int groupId){
//        System.out.println("bone index="
//        + boneId + " groupIndex=" + groupId);
        return;
    }

    @Override
    public void pmdEngEnabled(boolean hasEnglishInfo){
        return;
    }

    @Override
    public void pmdEngModelInfo(String modelName, String description){
        return;
    }

    @Override
    public void pmdEngBoneInfo(String boneName){
//        System.out.println("bone eng name = " + boneName);
        return;
    }

    @Override
    public void pmdEngMorphInfo(String morphName){
//        System.out.println("morph eng name = " + morphName);
        return;
    }

    @Override
    public void pmdEngBoneGroupInfo(String groupName){
//        System.out.println("group eng name = " + groupName);
        return;
    }

    @Override
    public void pmdToonFileInfo(String toonName){
//        System.out.println("toon file name = " + toonName);
        return;
    }

    @Override
    public void pmdRigidName(String rigidName){
//        System.out.println("rigid name = " + rigidName);
        return;
    }

    @Override
    public void pmdRigidInfo(int rigidGroupId, int linkedBoneId){
//        System.out.println("rigid group = "
//        + rigidGroupId + ",linked Bone =" + linkedBoneId);
        return;
    }

    @Override
    public void pmdRigidShape(byte shapeType,
                                float width, float height, float depth){
//        System.out.println("type="+shapeType);
//        System.out.println("w="+width);
//        System.out.println("h="+height);
//        System.out.println("d="+depth);
        return;
    }

    @Override
    public void pmdRigidPosition(float posX, float posY, float posZ){
//        System.out.println("x="+posX);
//        System.out.println("y="+posY);
//        System.out.println("z="+posZ);
        return;
    }

    @Override
    public void pmdRigidRotation(float rotX, float rotY, float rotZ){
//        System.out.println("x="+rotX);
//        System.out.println("y="+rotY);
//        System.out.println("z="+rotZ);
        return;
    }

    @Override
    public void pmdRigidPhysics(float mass,
                                  float fadePos, float fadeRot,
                                  float restitution, float friction ){
//        System.out.println("mass="+mass);
//        System.out.println("fadePos="+fadePos);
//        System.out.println("fadeRot="+fadeRot);
//        System.out.println("recoil="+restitution);
//        System.out.println("friction="+friction);
        return;
    }

    @Override
    public void pmdRigidBehavior(byte behaveType, short collisionMap){
//      System.out.println("type="+behaveType);
//        System.out.println("map="+collisionMap);
        return;
    }

    @Override
    public void pmdJointName(String jointName){
//        System.out.println("joint name = " + jointName);
        return;
    }

    @Override
    public void pmdJointLink(int rigidId_A, int rigidId_B){
//        System.out.println("rigid1 = " + rigidId_A+",rigidB = " + rigidId_B);
        return;
    }

    @Override
    public void pmdJointPosition(float posX, float posY, float posZ){
//        System.out.println("posX = " + posX);
//        System.out.println("posY = " + posY);
//        System.out.println("posZ = " + posZ);
        return;
    }

    @Override
    public void pmdJointRotation(float rotX, float rotY, float rotZ){
//        System.out.println("rotX = " + rotX);
//        System.out.println("rotY = " + rotY);
//        System.out.println("rotZ = " + rotZ);
        return;
    }

    @Override
    public void pmdPositionLimit(float posX_lim1, float posX_lim2,
                                   float posY_lim1, float posY_lim2,
                                   float posZ_lim1, float posZ_lim2 ){
//        System.out.println("limX = " + posX_lim1+"-"+posX_lim2);
//        System.out.println("limY = " + posY_lim1+"-"+posY_lim2);
//        System.out.println("limZ = " + posZ_lim1+"-"+posZ_lim2);
        return;
    }

    @Override
    public void pmdRotationLimit(float rotX_lim1, float rotX_lim2,
                                   float rotY_lim1, float rotY_lim2,
                                   float rotZ_lim1, float rotZ_lim2 ){
//        System.out.println("limX = " + rotX_lim1+"-"+rotX_lim2);
//        System.out.println("limY = " + rotY_lim1+"-"+rotY_lim2);
//        System.out.println("limZ = " + rotZ_lim1+"-"+rotZ_lim2);
        return;
    }

    @Override
    public void pmdElasticPosition(float elasticPosX,
                                  float elasticPosY,
                                  float elasticPosZ ){
//        System.out.println("posX="+elasticPosX);
//        System.out.println("posY="+elasticPosY);
//        System.out.println("posZ="+elasticPosZ);
        return;
    }

    @Override
    public void pmdElasticRotation(float elasticRotX,
                                  float elasticRotY,
                                  float elasticRotZ ){
//        System.out.println("posX="+elasticRotX);
//        System.out.println("posY="+elasticRotY);
//        System.out.println("posZ="+elasticRotZ);
        return;
    }

}
