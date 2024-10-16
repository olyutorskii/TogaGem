/*
 * pmd parser extension 3
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sfjp.mikutoga.pmd.parser;

import java.io.IOException;
import java.io.InputStream;
import jp.sfjp.mikutoga.bin.parser.MmdFormatException;
import jp.sfjp.mikutoga.pmd.PmdConst;

/**
 * PMDモデルファイルのパーサ拡張その3。
 *
 * <p>※ 剛体情報対応
 */
public class PmdParserExt3 extends PmdParserExt2 {

    private PmdRigidHandler rigidHandler = PmdUnifiedHandler.EMPTY;
    private PmdJointHandler jointHandler = PmdUnifiedHandler.EMPTY;

    /**
     * コンストラクタ。
     *
     * @param source 入力ソース
     */
    public PmdParserExt3(InputStream source){
        super(source);
        return;
    }

    /**
     * 剛体ハンドラを登録する。
     *
     * @param handler 剛体ハンドラ
     */
    public void setRigidHandler(PmdRigidHandler handler){
        if(handler == null){
            this.rigidHandler = PmdUnifiedHandler.EMPTY;
        }else{
            this.rigidHandler = handler;
        }
        return;
    }

    /**
     * ジョイントハンドラを登録する。
     *
     * @param handler ジョイントハンドラ
     */
    public void setJointHandler(PmdJointHandler handler){
        if(handler == null){
            this.jointHandler = PmdUnifiedHandler.EMPTY;
        }else{
            this.jointHandler = handler;
        }
        return;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException {@inheritDoc}
     * @throws MmdFormatException {@inheritDoc}
     */
    @Override
    protected void parseBody()
            throws IOException, MmdFormatException {
        super.parseBody();

        if(hasMore()){
            parseRigidList();
            parseJointList();
        }

        return;
    }

    /**
     * 剛体情報のパースと通知。
     *
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseRigidList() throws IOException, MmdFormatException{
        int rigidNum = parseLeInt();

        this.rigidHandler.loopStart(PmdRigidHandler.RIGID_LIST, rigidNum);

        for(int ct = 0; ct < rigidNum; ct++){
            String rigidName =
                    parsePmdText(PmdConst.MAXBYTES_RIGIDNAME);
            this.rigidHandler.pmdRigidName(rigidName);

            int linkedBoneId   = parseLeUShortAsInt();
            int rigidGroupId   = parseUByteAsInt();
            short collisionMap = parseLeShort();
            this.rigidHandler.pmdRigidInfo(rigidGroupId, linkedBoneId);

            parseRigidGeom();
            parseRigidDynamics();

            byte behaveType = parseByte();
            this.rigidHandler.pmdRigidBehavior(behaveType, collisionMap);

            this.rigidHandler.loopNext(PmdRigidHandler.RIGID_LIST);
        }

        this.rigidHandler.loopEnd(PmdRigidHandler.RIGID_LIST);

        return;
    }

    /**
     * 剛体ジオメトリのパースと通知。
     *
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseRigidGeom() throws IOException, MmdFormatException{
        byte shapeType = parseByte();
        float width  = parseLeFloat();
        float height = parseLeFloat();
        float depth  = parseLeFloat();

        this.rigidHandler.pmdRigidShape(shapeType, width, height, depth);

        float posX = parseLeFloat();
        float posY = parseLeFloat();
        float posZ = parseLeFloat();

        this.rigidHandler.pmdRigidPosition(posX, posY, posZ);

        float rotX = parseLeFloat();
        float rotY = parseLeFloat();
        float rotZ = parseLeFloat();

        this.rigidHandler.pmdRigidRotation(rotX, rotY, rotZ);

        return;
    }

    /**
     * 剛体力学のパースと通知。
     *
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseRigidDynamics()
        throws IOException, MmdFormatException{
        float mass        = parseLeFloat();
        float dampingPos  = parseLeFloat();
        float dampingRot  = parseLeFloat();
        float restitution = parseLeFloat();
        float friction    = parseLeFloat();

        this.rigidHandler.pmdRigidPhysics(
            mass, dampingPos, dampingRot, restitution, friction
        );

        return;
    }

    /**
     * ジョイント情報のパースと通知。
     *
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseJointList() throws IOException, MmdFormatException{
        int jointNum = parseLeInt();

        this.jointHandler.loopStart(PmdJointHandler.JOINT_LIST, jointNum);

        for(int ct = 0; ct < jointNum; ct++){
            String jointName =
                    parsePmdText(PmdConst.MAXBYTES_JOINTNAME);
            this.jointHandler.pmdJointName(jointName);

            int rigidIdA = parseLeInt();
            int rigidIdB = parseLeInt();
            this.jointHandler.pmdJointLink(rigidIdA, rigidIdB);

            parseJointGeom();
            parseJointLimit();
            parseJointElastic();

            this.jointHandler.loopNext(PmdJointHandler.JOINT_LIST);
        }

        this.jointHandler.loopEnd(PmdJointHandler.JOINT_LIST);

        return;
    }

    /**
     * ジョイントジオメトリのパースと通知。
     *
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseJointGeom() throws IOException, MmdFormatException{
        float posX = parseLeFloat();
        float posY = parseLeFloat();
        float posZ = parseLeFloat();

        this.jointHandler.pmdJointPosition(posX, posY, posZ);

        float rotX = parseLeFloat();
        float rotY = parseLeFloat();
        float rotZ = parseLeFloat();

        this.jointHandler.pmdJointRotation(rotX, rotY, rotZ);

        return;
    }

    /**
     * ジョイント制約のパースと通知。
     *
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseJointLimit() throws IOException, MmdFormatException{
        float posXlim1 = parseLeFloat();
        float posYlim1 = parseLeFloat();
        float posZlim1 = parseLeFloat();
        float posXlim2 = parseLeFloat();
        float posYlim2 = parseLeFloat();
        float posZlim2 = parseLeFloat();

        this.jointHandler.pmdPositionLimit(
                posXlim1, posXlim2,
                posYlim1, posYlim2,
                posZlim1, posZlim2
        );

        float rotXlim1 = parseLeFloat();
        float rotYlim1 = parseLeFloat();
        float rotZlim1 = parseLeFloat();
        float rotXlim2 = parseLeFloat();
        float rotYlim2 = parseLeFloat();
        float rotZlim2 = parseLeFloat();

        this.jointHandler.pmdRotationLimit(
                rotXlim1, rotXlim2,
                rotYlim1, rotYlim2,
                rotZlim1, rotZlim2
        );

        return;
    }

    /**
     * ジョイント弾性のパースと通知。
     *
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseJointElastic()
            throws IOException, MmdFormatException{
        float elasticPosX = parseLeFloat();
        float elasticPosY = parseLeFloat();
        float elasticPosZ = parseLeFloat();

        this.jointHandler.pmdElasticPosition(
                elasticPosX,
                elasticPosY,
                elasticPosZ
        );

        float elasticRotX = parseLeFloat();
        float elasticRotY = parseLeFloat();
        float elasticRotZ = parseLeFloat();

        this.jointHandler.pmdElasticRotation(
                elasticRotX,
                elasticRotY,
                elasticRotZ
        );

        return;
    }

}
