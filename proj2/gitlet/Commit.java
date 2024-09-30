package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

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
    private HashMap<String,String> blobMap = new HashMap<>();


    public Commit(String message, Date timestamp, String parent, String fileName, String blobName) {
        this.message = message;
        this.timestamp = timestamp;
        this.parent = parent;
        if(fileName == null || fileName.isEmpty()){
            this.blobMap = new HashMap<>();
        }else {
            this.blobMap.put(fileName, blobName) ;
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
     *
     * @param blobName blob的hashname
     */
    public void addBlob(String fileName, String blobName) {
        this.blobMap.put(fileName, blobName);
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




}
