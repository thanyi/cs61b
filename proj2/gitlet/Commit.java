package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;
import java.util.List;

import static gitlet.Refs.*;
import static gitlet.Repository.*;
import static gitlet.Utils.*;
import static java.lang.System.exit;

/**
 * Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     * <p>
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /* The message of this Commit. */
    private String message;
    /* The parent of commit, null if it's the first commit */
    private String parent;
    /* the timestamp of commit*/
    private Date timestamp;
    /* the contents of commit files*/
//    private String blobNames;
    private HashMap<String, String> blobMap = new HashMap<>();
    private String branchName = "master";

    public Commit(String message, Date timestamp, String parent, String blobFileName, String blobHashName) {
        this.message = message;
        this.timestamp = timestamp;
        this.parent = parent;
        if (blobFileName == null || blobFileName.isEmpty()) {
            this.blobMap = new HashMap<>();
        } else {
            this.blobMap.put(blobFileName, blobHashName);
        }
    }

    public Commit(Commit parent) {
        this.message = parent.message;
        this.timestamp = parent.timestamp;
        this.parent = parent.parent;
        this.blobMap = parent.blobMap;
    }



    /* TODO: fill in the rest of this class. */

    /**
     * To save commit into files in COMMIT_FOLDER, persists the status of object.
     */
    public void saveCommit() {
        // get the uid of this
        String hashname = this.getHashName();

        // write obj to files
        File commitFile = new File(COMMIT_FOLDER, hashname);
        writeObject(commitFile, this);
    }


    /**
     * @param blobName blob的hashname
     */
    public void addBlob(String fileName, String blobName) {
        this.blobMap.put(fileName, blobName);
    }

    public void removeBlob(String fileName) {
        this.blobMap.remove(fileName);
    }


    public String getHashName() {
        return sha1(this.message, dateToTimeStamp(this.timestamp), this.parent);
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getParent() {
        return parent;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }


    public HashMap<String, String> getBlobMap() {
        return blobMap;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    /**
     * 用于获取HEAD指针指向的Commit对象
     *
     * @return
     */
    public static Commit getHeadCommit() {
        /* 获取HEAD指针,这个指针指向目前最新的commit */
        String headHashName = readContentsAsString(HEAD_POINT);
        File commitFile = join(COMMIT_FOLDER, headHashName);
        /* 获取commit文件 */
        Commit commit = readObject(commitFile, Commit.class);

        return commit;

    }

    /**
     * 用于获取branches文件夹中分支文件指向的Commit对象
     *
     * @return
     */
    public static Commit getBranchHeadCommit(String branchName,String error_msg) {
        File brancheFile = join(HEAD_DIR, branchName);
        if (!brancheFile.exists()) {
            System.out.println(error_msg);
            exit(0);
        }

        /* 获取HEAD指针,这个指针指向目前最新的commit */
        String headHashName = readContentsAsString(brancheFile);
        File commitFile = join(COMMIT_FOLDER, headHashName);
        /* 获取commit文件 */
        Commit commit = readObject(commitFile, Commit.class);

        return commit;

    }

    /**
     * 通过hashname来获取Commit对象
     *
     * @param hashName
     * @return
     */
    public static Commit getCommit(String hashName) {
        List<String> commitFiles = plainFilenamesIn(COMMIT_FOLDER);
        /* 如果在commit文件夹中不存在此文件 */
        if (!commitFiles.contains(hashName)) {
            System.out.println("No such file or directory in COMMIT_FOLDER：" + hashName.toString());
            exit(0);
        }
        File commitFile = join(COMMIT_FOLDER, hashName);
        Commit commit = readObject(commitFile, Commit.class);
        return commit;
    }


    /**
     * 给定一个commitId，返回一个相对应的commit对象，若是没有这个commit对象，则返回null
     *
     * @param commitId
     * @return commit或者null
     */
    public static Commit getCommitFromId(String commitId) {
        Commit headCommit = getHeadCommit();
        Commit commit = headCommit;
        /* 查找对应的commit */
        while (!commit.getHashName().equals(commitId)) {
            commit = getCommit(commit.getParent());
            if (commit.getParent().isEmpty() && !commit.getHashName().equals(commitId)) {
                return null;
            }
        }

        return commit;
    }
}
