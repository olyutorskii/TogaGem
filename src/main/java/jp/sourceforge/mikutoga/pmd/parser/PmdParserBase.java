/*
 * pmd file parser
 *
 * License : The MIT License
 * Copyright(c) 2010 MikuToga Partners
 */

package jp.sourceforge.mikutoga.pmd.parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import jp.sfjp.mikutoga.bin.parser.CommonParser;
import jp.sfjp.mikutoga.bin.parser.MmdEofException;
import jp.sfjp.mikutoga.bin.parser.MmdFormatException;
import jp.sfjp.mikutoga.bin.parser.TextDecoder;

/**
 * PMDモデルファイルのパーサ基本部。
 */
public class PmdParserBase extends CommonParser {

    /**
     * PMDで用いられる文字エンコーディング(windows-31j)。
     * ほぼShift_JISのスーパーセットと思ってよい。
     * デコード結果はUCS-2集合に収まるはず。
     */
    public static final Charset CS_WIN31J = Charset.forName("windows-31j");

    /** 改行文字列 CR。 */
    protected static final String CR = "\r";       // 0x0d
    /** 改行文字列 LF。 */
    protected static final String LF = "\n";       // 0x0a
    /** 改行文字列 CRLF。 */
    protected static final String CRLF = CR + LF;  // 0x0d, 0x0a

    private static final int HEADER_LENGTH = 7;

    private static final byte[] MAGIC_BYTES = {
        (byte)0x50, (byte)0x6d, (byte)0x64,               // "Pmd"
        (byte)0x00, (byte)0x00, (byte)0x80, (byte)0x3f,   // 1.0f
    };

    private static final int VERTEX_DATA_SZ      = 38;
    private static final int SURFACE_DATA_SZ     =  6;
    private static final int MATERIAL_DATA_SZ    = 70;
    private static final int BONE_DATA_SZ        = 39;
    private static final int MORPHVERTEX_DATA_SZ = 16;
    private static final int MORPHORDER_DATA_SZ  =  2;
    private static final int BONEGROUP_DATA_SZ   = 50;
    private static final int GROUPEDBONE_DATA_SZ =  3;


    static{
        assert MAGIC_BYTES.length == HEADER_LENGTH;
    }


    private final TextDecoder decoderWin31j  = new TextDecoder(CS_WIN31J);

    private PmdBasicHandler basicHandler = null;
    private PmdShapeHandler shapeHandler = null;
    private PmdMaterialHandler materialHandler = null;
    private PmdBoneHandler boneHandler = null;
    private PmdMorphHandler morphHandler = null;

    private int boneCount      = -1;
    private int morphCount     = -1;
    private int boneGroupCount = -1;


    /**
     * コンストラクタ。
     * @param source 入力ソース
     */
    public PmdParserBase(InputStream source){
        super(source);
        this.decoderWin31j.setZeroChopMode(true);
        return;
    }

    /**
     * 文字列の最後がLF(0x0a)の場合削除する。
     * @param name 文字列
     * @return 末尾LFが削除された文字列
     */
    public static String chopLastLF(String name){
        String result;

        if(name.endsWith(LF)){
            result = name.substring(0, name.length() - 1);
        }else{
            result = name;
        }

        return result;
    }

    /**
     * シェーディング用ファイル情報から
     * テクスチャファイル名とスフィアマップファイル名を分離する。
     * @param shadingFile シェーディング用ファイル情報
     * @return [0]:テクスチャファイル名 [1]:スフィアマップファイル名。
     * 該当ファイル名が無い場合は空文字列。
     */
    public static String[] splitShadingFileInfo(String shadingFile){
        String[] result;

        result = shadingFile.split('\\'+"*", 2);
        assert result.length == 1 || result.length == 2;

        if(result.length == 1){
            String onlyFile = result[0];
            result = new String[2];
            result[0] = "";
            result[1] = "";
            if(onlyFile.endsWith(".sph") || onlyFile.endsWith(".spa")){
                result[1] = onlyFile;
            }else{
                result[0] = onlyFile;
            }
        }

        assert result.length == 2;

        return result;
    }

    /**
     * 基本情報通知ハンドラを登録する。
     * @param handler ハンドラ
     */
    public void setBasicHandler(PmdBasicHandler handler){
        this.basicHandler = handler;
        return;
    }

    /**
     * 形状情報通知ハンドラを登録する。
     * @param handler ハンドラ
     */
    public void setShapeHandler(PmdShapeHandler handler){
        this.shapeHandler = handler;
        return;
    }

    /**
     * 材質情報通知ハンドラを登録する。
     * @param handler ハンドラ
     */
    public void setMaterialHandler(PmdMaterialHandler handler){
        this.materialHandler = handler;
        return;
    }

    /**
     * ボーン情報通知ハンドラを登録する。
     * @param handler ハンドラ
     */
    public void setBoneHandler(PmdBoneHandler handler){
        this.boneHandler = handler;
        return;
    }

    /**
     * モーフ情報通知ハンドラを登録する。
     * @param handler ハンドラ
     */
    public void setMorphHandler(PmdMorphHandler handler){
        this.morphHandler = handler;
        return;
    }

    /**
     * パースによって得られたボーン数を返す。
     * @return ボーン数
     */
    protected int getBoneCount(){
        return this.boneCount;
    }

    /**
     * パースによって得られたモーフ数を返す。
     * @return モーフ数
     */
    protected int getMorphCount(){
        return this.morphCount;
    }

    /**
     * パースによって得られたボーングループ数を返す。
     * @return ボーングループ数
     */
    protected int getBoneGroupCount(){
        return this.boneGroupCount;
    }

    /**
     * 指定されたバイト長に収まるゼロ終端(0x00)文字列を読み込む。
     * 入力バイト列はwindows-31jエンコーディングとして解釈される。
     * ゼロ終端以降のデータは無視されるが、
     * IO入力は指定バイト数だけ読み進められる。
     * ゼロ終端が見つからないまま指定バイト数が読み込み終わった場合、
     * そこまでのデータから文字列を構成する。
     * @param byteLen 読み込みバイト数
     * @return デコードされた文字列
     * @throws IOException IOエラー
     * @throws MmdEofException 読み込む途中でストリーム終端に達した。
     * @throws MmdFormatException 不正な文字エンコーディングが検出された。
     */
    protected String parsePmdText(int byteLen)
            throws IOException,
                   MmdEofException,
                   MmdFormatException {
        String result = parseString(this.decoderWin31j, byteLen);
        return result;
    }

    /**
     * PMDファイルのパースを開始する。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    public void parsePmd()
            throws IOException, MmdFormatException {
        if(this.basicHandler != null){
            this.basicHandler.pmdParseStart();
        }

        parseBody();

        boolean hasMoreData = hasMore();
        if(this.basicHandler != null){
            this.basicHandler.pmdParseEnd(hasMoreData);
        }

        return;
    }

    /**
     * PMDファイル本体のパースを開始する。
     * パーサを拡張する場合はこのメソッドをオーバーライドする。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    protected void parseBody() throws IOException, MmdFormatException{
        parsePmdHeader();

        parseVertexList();
        parseSurfaceList();
        parseMaterialList();
        parseBoneList();
        parseIKList();
        parseMorphList();
        parseMorphOrderList();
        parseBoneGroupList();
        parseGroupedBoneList();

        return;
    }

    /**
     * PMDファイルヘッダ部のパースと通知。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parsePmdHeader() throws IOException, MmdFormatException{
        byte[] header = new byte[HEADER_LENGTH];
        parseByteArray(header);

        if( ! Arrays.equals(header, MAGIC_BYTES) ){
            throw new MmdFormatException("unknown PMD-header type");
        }

        String modelName   =
                parsePmdText(PmdLimits.MAXBYTES_MODELNAME);
        String description =
                parsePmdText(PmdLimits.MAXBYTES_MODELDESC);
        description = description.replace(CRLF, LF);

        if(this.basicHandler != null){
            this.basicHandler.pmdHeaderInfo(header);
            this.basicHandler.pmdModelInfo(modelName, description);
        }

        return;
    }

    /**
     * 頂点情報のパースと通知。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseVertexList() throws IOException, MmdFormatException{
        int vertexNum = parseLeInt();

        if(this.shapeHandler == null){
            skip(VERTEX_DATA_SZ * vertexNum);
            return;
        }

        this.shapeHandler.loopStart(PmdShapeHandler.VERTEX_LIST, vertexNum);

        for(int ct = 0; ct < vertexNum; ct++){
            float xPos = parseLeFloat();
            float yPos = parseLeFloat();
            float zPos = parseLeFloat();
            this.shapeHandler.pmdVertexPosition(xPos, yPos, zPos);

            float xVec = parseLeFloat();
            float yVec = parseLeFloat();
            float zVec = parseLeFloat();
            this.shapeHandler.pmdVertexNormal(xVec, yVec, zVec);

            float uVal = parseLeFloat();
            float vVal = parseLeFloat();
            this.shapeHandler.pmdVertexUV(uVal, vVal);

            int boneId1 = parseLeUShortAsInt();
            int boneId2 = parseLeUShortAsInt();
            int weightForB1 = parseUByteAsInt();
            this.shapeHandler.pmdVertexWeight(boneId1, boneId2, weightForB1);

            boolean hideEdge = parseBoolean();
            this.shapeHandler.pmdVertexEdge(hideEdge);

            this.shapeHandler.loopNext(PmdShapeHandler.VERTEX_LIST);
        }

        this.shapeHandler.loopEnd(PmdShapeHandler.VERTEX_LIST);

        return;
    }

    /**
     * 面情報のパースと通知。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseSurfaceList() throws IOException, MmdFormatException{
        int vertexNum = parseLeInt();
        if(vertexNum % 3 != 0) throw new MmdFormatException();
        int surfaceNum = vertexNum / 3;

        if(this.shapeHandler == null){
            skip(SURFACE_DATA_SZ * surfaceNum);
            return;
        }

        this.shapeHandler.loopStart(PmdShapeHandler.SURFACE_LIST, surfaceNum);

        for(int ct = 0; ct < surfaceNum; ct++){
            int vertexId1 = parseLeUShortAsInt();
            int vertexId2 = parseLeUShortAsInt();
            int vertexId3 = parseLeUShortAsInt();
            this.shapeHandler.pmdSurfaceTriangle(vertexId1,
                                                 vertexId2,
                                                 vertexId3 );
            this.shapeHandler.loopNext(PmdShapeHandler.SURFACE_LIST);
        }

        this.shapeHandler.loopEnd(PmdShapeHandler.SURFACE_LIST);

        return;
    }

    /**
     * 材質情報のパースと通知。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseMaterialList() throws IOException, MmdFormatException{
        int materialNum = parseLeInt();

        if(this.materialHandler == null){
            skip(MATERIAL_DATA_SZ * materialNum);
            return;
        }

        this.materialHandler.loopStart(PmdMaterialHandler.MATERIAL_LIST,
                                       materialNum );

        for(int ct = 0; ct < materialNum; ct++){
            float red;
            float green;
            float blue;

            red   = parseLeFloat();
            green = parseLeFloat();
            blue  = parseLeFloat();
            float alpha = parseLeFloat();
            this.materialHandler.pmdMaterialDiffuse(red, green, blue, alpha);

            float shininess = parseLeFloat();
            red   = parseLeFloat();
            green = parseLeFloat();
            blue  = parseLeFloat();
            this.materialHandler.pmdMaterialSpecular(red, green, blue,
                                                     shininess);

            red   = parseLeFloat();
            green = parseLeFloat();
            blue  = parseLeFloat();
            this.materialHandler.pmdMaterialAmbient(red, green, blue);

            int toonidx = parseUByteAsInt();
            boolean hasEdge = parseBoolean();
            int surfaceCount = parseLeInt();
            String shadingFile =
                    parsePmdText(PmdLimits.MAXBYTES_TEXTUREFILENAME);
            String[] splitted = splitShadingFileInfo(shadingFile);
            String textureFile = splitted[0];
            String sphereFile = splitted[1];

            this.materialHandler.pmdMaterialShading(toonidx,
                                                    textureFile, sphereFile );
            this.materialHandler.pmdMaterialInfo(hasEdge, surfaceCount);

            this.materialHandler.loopNext(PmdMaterialHandler.MATERIAL_LIST);
        }

        this.materialHandler.loopEnd(PmdMaterialHandler.MATERIAL_LIST);

        return;
    }

    /**
     * ボーン情報のパースと通知。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseBoneList() throws IOException, MmdFormatException{
        this.boneCount = parseLeUShortAsInt();

        if(this.boneHandler == null){
            skip(BONE_DATA_SZ * this.boneCount);
            return;
        }

        this.boneHandler.loopStart(PmdBoneHandler.BONE_LIST, this.boneCount);

        for(int ct = 0; ct < this.boneCount; ct++){
            String boneName =
                    parsePmdText(PmdLimits.MAXBYTES_BONENAME);
            int parentId = parseLeUShortAsInt();
            int tailId = parseLeUShortAsInt();
            byte boneKind = parseByte();
            int srcId = parseLeUShortAsInt();

            this.boneHandler.pmdBoneInfo(boneName, boneKind);
            this.boneHandler.pmdBoneLink(parentId, tailId, srcId);

            float xPos = parseLeFloat();
            float yPos = parseLeFloat();
            float zPos = parseLeFloat();

            this.boneHandler.pmdBonePosition(xPos, yPos, zPos);

            this.boneHandler.loopNext(PmdBoneHandler.BONE_LIST);
        }

        this.boneHandler.loopEnd(PmdBoneHandler.BONE_LIST);

        return;
    }

    /**
     * IKリスト情報のパースと通知。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseIKList() throws IOException, MmdFormatException{
        int ikCount = parseLeUShortAsInt();

        if(this.boneHandler != null){
            this.boneHandler.loopStart(PmdBoneHandler.IK_LIST, ikCount);
        }

        for(int ct = 0; ct < ikCount; ct++){
            int boneId = parseLeUShortAsInt();
            int targetId = parseLeUShortAsInt();
            int chainLength = parseUByteAsInt();
            int depth = parseLeUShortAsInt();
            float weight = parseLeFloat();

            parseIKChainList(chainLength);

            if(this.boneHandler != null){
                this.boneHandler.pmdIKInfo(boneId, targetId, depth, weight);
                this.boneHandler.loopNext(PmdBoneHandler.IK_LIST);
            }
        }

        if(this.boneHandler != null){
            this.boneHandler.loopEnd(PmdBoneHandler.IK_LIST);
        }

        return;
    }

    /**
     * IKチェーン情報のパースと通知。
     * @param chainLength チェーン長
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseIKChainList(int chainLength)
            throws IOException, MmdFormatException{
        if(this.boneHandler != null){
            this.boneHandler.loopStart(PmdBoneHandler.IKCHAIN_LIST,
                                       chainLength);
        }

        for(int ct = 0; ct < chainLength; ct++){
            int childId = parseLeUShortAsInt();
            if(this.boneHandler != null){
                this.boneHandler.pmdIKChainInfo(childId);
                this.boneHandler.loopNext(PmdBoneHandler.IKCHAIN_LIST);
            }
        }

        if(this.boneHandler != null){
            this.boneHandler.loopEnd(PmdBoneHandler.IKCHAIN_LIST);
        }

        return;
    }

    /**
     * モーフ情報のパースと通知。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseMorphList() throws IOException, MmdFormatException{
        this.morphCount = parseLeUShortAsInt();

        if(this.morphHandler != null){
            this.morphHandler.loopStart(PmdMorphHandler.MORPH_LIST,
                                        this.morphCount );
        }

        for(int ct = 0; ct < this.morphCount; ct++){
            String morphName =
                    parsePmdText(PmdLimits.MAXBYTES_MORPHNAME);
            int vertexCount = parseLeInt();
            byte morphType = parseByte();

            if(this.morphHandler != null){
                this.morphHandler.pmdMorphInfo(morphName, morphType);
            }

            parseMorphVertexList(vertexCount);

            if(this.morphHandler != null){
                this.morphHandler.loopNext(PmdMorphHandler.MORPH_LIST);
            }
        }

        if(this.morphHandler != null){
            this.morphHandler.loopEnd(PmdMorphHandler.MORPH_LIST);
        }

        return;
    }

    /**
     * モーフ形状のパースと通知。
     * @param vertexCount 頂点数
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseMorphVertexList(int vertexCount)
            throws IOException, MmdFormatException{
        if(this.morphHandler == null){
            skip(MORPHVERTEX_DATA_SZ * vertexCount);
            return;
        }

        this.morphHandler.loopStart(PmdMorphHandler.MORPHVERTEX_LIST,
                                    vertexCount );

        for(int ct = 0; ct < vertexCount; ct++){
            int vertexId = parseLeInt();
            float xPos = parseLeFloat();
            float yPos = parseLeFloat();
            float zPos = parseLeFloat();
            this.morphHandler.pmdMorphVertexInfo(vertexId, xPos, yPos, zPos);
            this.morphHandler.loopNext(PmdMorphHandler.MORPHVERTEX_LIST);
        }

        this.morphHandler.loopEnd(PmdMorphHandler.MORPHVERTEX_LIST);

        return;
    }

    /**
     * モーフGUI表示順のパースと通知。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseMorphOrderList()
            throws IOException, MmdFormatException{
        int morphOrderCount = parseUByteAsInt();

        if(this.morphHandler == null){
            skip(MORPHORDER_DATA_SZ * morphOrderCount);
            return;
        }

        this.morphHandler.loopStart(PmdMorphHandler.MORPHORDER_LIST,
                                    morphOrderCount );

        for(int ct = 0; ct < morphOrderCount; ct++){
            int morphId = parseLeUShortAsInt();
            this.morphHandler.pmdMorphOrderInfo(morphId);

            this.morphHandler.loopNext(PmdMorphHandler.MORPHORDER_LIST);
        }

        this.morphHandler.loopEnd(PmdMorphHandler.MORPHORDER_LIST);

        return;
    }

    /**
     * ボーングループ名のパースと通知。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseBoneGroupList()
            throws IOException, MmdFormatException{
        this.boneGroupCount = parseUByteAsInt();

        if(this.boneHandler == null){
            skip(BONEGROUP_DATA_SZ * this.boneGroupCount);
            return;
        }

        this.boneHandler.loopStart(PmdBoneHandler.BONEGROUP_LIST,
                                   this.boneGroupCount);

        for(int ct = 0; ct < this.boneGroupCount; ct++){
            String groupName =
                    parsePmdText(PmdLimits.MAXBYTES_BONEGROUPNAME);
            groupName = chopLastLF(groupName);
            this.boneHandler.pmdBoneGroupInfo(groupName);

            this.boneHandler.loopNext(PmdBoneHandler.BONEGROUP_LIST);
        }

        this.boneHandler.loopEnd(PmdBoneHandler.BONEGROUP_LIST);

        return;
    }

    /**
     * ボーングループ内訳のパースと通知。
     * @throws IOException IOエラー
     * @throws MmdFormatException フォーマットエラー
     */
    private void parseGroupedBoneList()
            throws IOException, MmdFormatException{
        int groupedBoneCount = parseLeInt();

        if(this.boneHandler == null){
            skip(GROUPEDBONE_DATA_SZ * groupedBoneCount);
            return;
        }

        this.boneHandler.loopStart(PmdBoneHandler.GROUPEDBONE_LIST,
                                   groupedBoneCount);

        for(int ct = 0; ct < groupedBoneCount; ct++){
            int boneId = parseLeUShortAsInt();
            int groupId = parseUByteAsInt();
            this.boneHandler.pmdGroupedBoneInfo(boneId, groupId);

            this.boneHandler.loopNext(PmdBoneHandler.GROUPEDBONE_LIST);
        }

        this.boneHandler.loopEnd(PmdBoneHandler.GROUPEDBONE_LIST);

        return;
    }

}
