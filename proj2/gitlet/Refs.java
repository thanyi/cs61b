package gitlet;

import java.io.File;

import static gitlet.Commit.COMMIT_FOLDER;
import static gitlet.Repository.*;
import static gitlet.Utils.*;

/**
 * Represent the reference point of HEAD, REMOTE and so on;
 */
public class Refs {
    /* The .gitlet/refs directory. */
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    /* The .gitlet/heads directory. */
    public static final File HEAD_DIR = join(REFS_DIR, "heads");
    /* the current .gitlet/HEAD file */
    public static final File HEAD_CONTENT_PATH = join(REFS_DIR, "HEAD");


    /**
     * Save HEAD file and <branch> file which contains the hash of current commit
     *
     * @param branchName: the name of current branch
     * @param hashName:   the uid of current commit
     */
    public static void saveHead(String branchName, String hashName) {
        // save the master(current branch) file
        File branch_head = join(HEAD_DIR, branchName);
        writeContents(branch_head, hashName);

        String HEAD_content = branch_head.getPath();
        writeContents(HEAD_CONTENT_PATH, "refs:", HEAD_content);
    }

}
