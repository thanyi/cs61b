package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Refs.*;
import static gitlet.Repository.*;
import static gitlet.Utils.*;

public class Blob implements Serializable {
    private String content;
    public File filePath;
    private String hashName;

    public Blob(String content, String hashName) {
        this.content = content;
        this.hashName = hashName;
        this.filePath = join(BLOBS_FOLDER,hashName);
    }

    /**
     * To save commit into files in BLOB_FOLDER, persists the status of object.
     */
    public void saveBlob() {
        // write contents to files
        writeContents(filePath, this.content);
    }


    public boolean compareContents(String content) {
        if(this.content.equals(content)) return true;
        else return false;
    }

    public String getContent() {
        return content;
    }


    /**
     * 根据blobName获取到Blob的内容，其中blobName是一个hash值
     *
     * @return Blob的内容
     */
    public static String getBlobContentFromName(String blobName) {

        File blobFile = join(BLOBS_FOLDER, blobName);
        /* 获取commit文件 */
        String blobContent = readContentsAsString(blobFile);

        return blobContent;

    }

    /**
     * 将blob.content中的内容覆盖进file文件中
     */
    public static void overWriteFileWithBlob(File file, String content){
        writeContents(file,content);
    }

}

