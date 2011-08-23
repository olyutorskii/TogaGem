/*
 * output stream with feedable byte array
 *
 * License : The MIT License
 * Copyright(c) 2011 MikuToga Partners
 */

package jp.sourceforge.mikutoga.binio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 蓄積された出力結果の他ストリームへの転送を可能とする
 * {@link java.io.ByteArrayOutputStream}。
 */
public class FeedableOutputStream extends ByteArrayOutputStream {

    /**
     * コンストラクタ。
     * @see java.io.ByteArrayOutputStream#ByteArrayOutputStream()
     */
    public FeedableOutputStream(){
        super();
        return;
    }

    /**
     * コンストラクタ。
     * @param size 初期バッファ長(byte単位)。
     * @see java.io.ByteArrayOutputStream#ByteArrayOutputStream(int)
     */
    public FeedableOutputStream(int size){
        super(size);
        return;
    }

    /**
     * 蓄積されたストリームデータを別ストリームへ転送する。
     * <p>何も蓄積されていなければ何も転送されない。
     * <p>蓄積されたストリームデータに変更は生じない。
     * @param os 別ストリーム
     * @return 転送量。
     * @throws IOException 転送先の出力エラー
     */
    public int feedStored(OutputStream os) throws IOException {
        if(this.count <= 0) return 0;
        os.write(this.buf, 0, this.count);
        return this.count;
    }

}
