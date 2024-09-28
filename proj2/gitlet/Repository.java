package gitlet;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import static gitlet.Commit.COMMIT_FOLDER;
import static gitlet.Refs.*;
import static gitlet.Utils.*;
import static java.lang.System.exit;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author ethanyi
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /* The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /* The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");



    /* TODO: fill in the rest of this class. */

    /**
     *  initialize the folder instructure
     */
    public static void setupPersistence() {

        GITLET_DIR.mkdirs();
        COMMIT_FOLDER.mkdirs();
        REFS_DIR.mkdirs();
        HEAD_DIR.mkdirs();

    }

    /**
     * Check if the ARGS of java gitlet.Main is empty
     * @param args
     */
    public static void checkArgsEmpty(String[] args){
        if (args.length == 0){
            System.out.println("Please enter a command.");
            exit(0);
        }
    }

    /**
     * To get Date obj a format to transform the object to String.
     * @param date
     * @return
     */
    public static String dateToTimeStamp(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return dateFormat.format(date);
    }

    /**
     * Creates a new Gitlet version-control system in the current directory. This system will automatically start with one
     * commit: a commit that contains no files and has the commit message initial commit (just like that, with no
     * punctuation). It will have a single branch: master, which initially points to this initial commit, and master will
     * be the current branch. The timestamp for this initial commit will be 00:00:00 UTC, Thursday, 1 January 1970 in
     * whatever format you choose for dates (this is called The (Unix) Epoch, represented internally by the time 0.)
     * Since the initial commit in all repositories created by Gitlet will have exactly the same content, it follows that
     * all repositories will automatically share this commit (they will all have the same UID) and all commits in all
     * repositories will trace back to it.
     */
    public static void initPersistence(){
        // if .gitlet already exists
        if(GITLET_DIR.exists()){
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            exit(0);
        }
        // initialize the directories
        setupPersistence();
        // initialize and save the object
        Date timestamp_init = new Date(0);
        Commit initialCommit = new Commit("initial commit", timestamp_init, "","");
        initialCommit.saveCommit();

        // get the hashName of the object
        String commitHashName = initialCommit.getUid();
        saveHead("master",commitHashName);


        //TODO: branches: the master point which points to commit

    }

    /**
     *  Adds a copy of the file as it currently exists to the staging area (see the description of the commit command).
     *  For this reason, adding a file is also called staging the file for addition. Staging an already-staged file
     *  overwrites the previous entry in the staging area with the new contents. The staging area should be somewhere in
     *  .gitlet. If the current working version of the file is identical to the version in the current commit, do not
     *  stage it to be added, and remove it from the staging area if it is already there (as can happen when a file is
     *  changed, added, and then changed back to it’s original version). The file will no longer be staged for removal
     *  (see gitlet rm), if it was at the time of the command.
     *
     *  @param addFileName:
     */
    public static void addFile(String addFileName){


    }


}
