package gitlet;

import java.io.File;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

/**
 * Represent the reference point of HEAD, REMOTE and so on;
 */
public class Refs {
    /* The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /* The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /* the objects directory */
    static final File OBJECTS_FOLDER = join(GITLET_DIR, "objects");
    static final File COMMIT_FOLDER = join(OBJECTS_FOLDER, "commits");
    static final File BLOBS_FOLDER = join(OBJECTS_FOLDER, "blobs");

    /* The refs directory. */
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    public static final File HEAD_DIR = join(REFS_DIR, "heads");

    /* the current .gitlet/HEAD file */
    public static final File HEAD_POINT = join(REFS_DIR, "HEAD");

    /* the stage directory */
    public static final File ADD_STAGE_DIR = join(GITLET_DIR, "addstage");
    public static final File REMOVE_STAGE_DIR = join(GITLET_DIR, "removestage");



    /**
     * Save HEAD file and <branch> file which contains the hash of current commit
     *
     * @param branchName: the name of current branch
     * @param hashName:   the uid of current commit
     */
    public static void saveBranch(String branchName, String hashName) {
        // save the file of the head of a given branch
        File branch_head = join(HEAD_DIR, branchName);
        writeContents(branch_head, hashName);

    }

    /**
     * Save the point to HEAD into .gitlet/refs/HEAD folder
     * @param branch_head 关于保存的.gitlet/refs/heads中的File对象
     */
    public static void saveHEAD(File branch_head) {
        String HEAD_content = branch_head.getName();
        writeContents(HEAD_POINT, HEAD_content);
    }
}
