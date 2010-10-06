/*
 * sample handler
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package sample.pmd;

import jp.sourceforge.mikutoga.parser.ParseStage;
import jp.sourceforge.mikutoga.parser.pmd.PmdBasicHandler;
import jp.sourceforge.mikutoga.parser.pmd.PmdBoneHandler;
import jp.sourceforge.mikutoga.parser.pmd.PmdEngHandler;
import jp.sourceforge.mikutoga.parser.pmd.PmdJointHandler;
import jp.sourceforge.mikutoga.parser.pmd.PmdMaterialHandler;
import jp.sourceforge.mikutoga.parser.pmd.PmdMorphHandler;
import jp.sourceforge.mikutoga.parser.pmd.PmdRigidHandler;
import jp.sourceforge.mikutoga.parser.pmd.PmdShapeHandler;
import jp.sourceforge.mikutoga.parser.pmd.PmdToonHandler;

/**
 * サンプルのハンドラ。
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

    public void pmdParseStart(){
        System.out.println("PMD parse start");
        return;
    }

    public void pmdParseEnd(boolean hasMoreData){
        System.out.println("PMD parse end");
        if(hasMoreData){
            System.out.println("thre is unknown data below");
        }
        return;
    }

    public void loopStart(ParseStage stage, int loops){
        System.out.println("===== Loop start ===== * " + loops);
        return;
    }

    public void loopNext(ParseStage stage){
        return;
    }

    public void loopEnd(ParseStage stage){
        System.out.println("===== Loop end =====");
        return;
    }

    public void pmdHeaderInfo(float ver){
        System.out.println("ver=" + ver);
        return;
    }

    public void pmdModelInfo(String modelName, String description){
        System.out.println("modelName=" + modelName);
        System.out.println("comment=" + description);
        return;
    }

    public void pmdVertexPosition(float xPos, float yPos, float zPos){
//        System.out.println("x,y,z = " + xPos + "," + yPos + "," + zPos);
        return;
    }

    public void pmdVertexNormal(float xVec, float yVec, float zVec){
//        System.out.println("x,y,z = " + xVec + "," + yVec + "," + zVec);
    }

    public void pmdVertexUV(float uVal, float vVal){
        return;
    }

    public void pmdVertexWeight(int boneId_1, int boneId_2,
                                  int weightForB1){
//        System.out.println("x,y,z = "
//        + boneId_1 + "," + boneId_2 + "," + weightForB1);
        return;
    }

    public void pmdVertexEdge(boolean hideEdge){
//        System.out.println("x,y,z = " + hideEdge);
        return;
    }

    public void pmdSurfaceTriangle(int vertexId_1,
                                      int vertexId_2,
                                      int vertexId_3){
//        System.out.println("v1,v2,v3 = "
//                + vertexId_1 + "," + vertexId_2 + "," + vertexId_3);
    }

    public void pmdMaterialDiffuse(float red, float green, float blue,
                                      float alpha ){
//        System.out.println("diffuse rgba="
//        + red + "," + green + "," + blue + "," + alpha);
        return;
    }

    public void pmdMaterialSpecular(float red, float green, float blue,
                                       float shininess){
//        System.out.println("specular rgbs="
//        + red + "," + green + "," + blue + "," + shininess);
        return;
    }

    public void pmdMaterialAmbient(float red, float green, float blue){
//        System.out.println("ambient rgb=" + red + "," + green + "," + blue);
        return;
    }

    public void pmdMaterialShading(int toon_idx,
                                      String textureFile, String sphereFile){
//        System.out.println("toon idx=" + toon_idx);
//        System.out.println("texfile=" + textureFile);
//        System.out.println("spherefile=" + sphereFile);
        return;
    }

    public void pmdMaterialInfo(boolean hasEdge, int surfaceNum){
//        System.out.println("surfaces=" + surfaceNum);
        return;
    }

    public void pmdBoneInfo(String boneName, byte boneKind){
//        System.out.println("bonename = " + boneName);
//        System.out.println("kind = " + boneKind);
        return;
    }

    public void pmdBoneLink(int parentId, int tailId, int ikId){
//        System.out.println("parent = " + parentId);
//        System.out.println("tail = " + tailId);
//        System.out.println("ik = " + ikId);
        return;
    }

    public void pmdBonePosition(float xPos, float yPos, float zPos){
//        System.out.println("x="+xPos+" y="+yPos+" z="+zPos);
        return;
    }

    public void pmdIKInfo(int boneId, int targetId,
                           int depth, float weight ){
        return;
    }

    public void pmdIKChainInfo(int childId){
//        System.out.println("chained bone = " + childId);
        return;
    }

    public void pmdMorphInfo(String morphName, byte morphType){
//        System.out.println("morph name = " + morphName);
        return;
    }

    public void pmdMorphVertexInfo(int vertexId,
                                 float xPos, float yPos, float zPos){
//        System.out.println("id="+vertexId+",x="+xPos+",y="+yPos+",z="+zPos);
        return;
    }

    public void pmdMorphOrderInfo(int morphId){
//        System.out.println("morph idx = " + morphId);
        return;
    }

    public void pmdBoneGroupInfo(String groupName){
//        System.out.println("group name = " + groupName);
        return;
    }

    public void pmdGroupedBoneInfo(int boneId, int groupId){
//        System.out.println("bone index="
//        + boneId + " groupIndex=" + groupId);
        return;
    }

    public void pmdEngEnabled(boolean hasEnglishInfo){
        return;
    }

    public void pmdEngModelInfo(String modelName, String description){
        return;
    }

    public void pmdEngBoneInfo(String boneName){
//        System.out.println("bone eng name = " + boneName);
        return;
    }

    public void pmdEngMorphInfo(String morphName){
//        System.out.println("morph eng name = " + morphName);
        return;
    }

    public void pmdEngBoneGroupInfo(String groupName){
//        System.out.println("group eng name = " + groupName);
        return;
    }

    public void pmdToonFileInfo(String toonName){
//        System.out.println("toon file name = " + toonName);
        return;
    }

    public void pmdRigidName(String rigidName){
//        System.out.println("rigid name = " + rigidName);
        return;
    }

    public void pmdRigidInfo(int rigidGroupId, int linkedBoneId){
//        System.out.println("rigid group = "
//        + rigidGroupId + ",linked Bone =" + linkedBoneId);
        return;
    }

    public void pmdRigidShape(byte shapeType,
                                float width, float height, float depth){
//        System.out.println("type="+shapeType);
//        System.out.println("w="+width);
//        System.out.println("h="+height);
//        System.out.println("d="+depth);
        return;
    }

    public void pmdRigidPosition(float posX, float posY, float posZ){
//        System.out.println("x="+posX);
//        System.out.println("y="+posY);
//        System.out.println("z="+posZ);
        return;
    }

    public void pmdRigidRotation(float rotX, float rotY, float rotZ){
//        System.out.println("x="+rotX);
//        System.out.println("y="+rotY);
//        System.out.println("z="+rotZ);
        return;
    }

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

    public void pmdRigidBehavior(byte behaveType, short collisionMap){
//      System.out.println("type="+behaveType);
//        System.out.println("map="+collisionMap);
        return;
    }

    public void pmdJointName(String jointName){
//        System.out.println("joint name = " + jointName);
        return;
    }

    public void pmdJointLink(int rigidId_A, int rigidId_B){
//        System.out.println("rigid1 = " + rigidId_A+",rigidB = " + rigidId_B);
        return;
    }

    public void pmdJointPosition(float posX, float posY, float posZ){
//        System.out.println("posX = " + posX);
//        System.out.println("posY = " + posY);
//        System.out.println("posZ = " + posZ);
        return;
    }

    public void pmdJointRotation(float rotX, float rotY, float rotZ){
//        System.out.println("rotX = " + rotX);
//        System.out.println("rotY = " + rotY);
//        System.out.println("rotZ = " + rotZ);
        return;
    }

    public void pmdPositionLimit(float posX_lim1, float posX_lim2,
                                   float posY_lim1, float posY_lim2,
                                   float posZ_lim1, float posZ_lim2 ){
//        System.out.println("limX = " + posX_lim1+"-"+posX_lim2);
//        System.out.println("limY = " + posY_lim1+"-"+posY_lim2);
//        System.out.println("limZ = " + posZ_lim1+"-"+posZ_lim2);
        return;
    }

    public void pmdRotationLimit(float rotX_lim1, float rotX_lim2,
                                   float rotY_lim1, float rotY_lim2,
                                   float rotZ_lim1, float rotZ_lim2 ){
//        System.out.println("limX = " + rotX_lim1+"-"+rotX_lim2);
//        System.out.println("limY = " + rotY_lim1+"-"+rotY_lim2);
//        System.out.println("limZ = " + rotZ_lim1+"-"+rotZ_lim2);
        return;
    }

    public void pmdElasticPosition(float elasticPosX,
                                  float elasticPosY,
                                  float elasticPosZ ){
//        System.out.println("posX="+elasticPosX);
//        System.out.println("posY="+elasticPosY);
//        System.out.println("posZ="+elasticPosZ);
        return;
    }

    public void pmdElasticRotation(float elasticRotX,
                                  float elasticRotY,
                                  float elasticRotZ ){
//        System.out.println("posX="+elasticRotX);
//        System.out.println("posY="+elasticRotY);
//        System.out.println("posZ="+elasticRotZ);
        return;
    }

}
