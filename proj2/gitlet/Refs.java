package gitlet;

import java.io.File;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

/**
 * Represent the reference point of HEAD, REMOTE and so on;
 */
public class Refs {


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
     * Save the point to HEAD into .gitlet/HEAD folder
     * @param branch_head File obj of the HEAD point path
     */
    public static void saveHEAD(File branch_head) {
        String HEAD_content = branch_head.getPath();
        writeContents(HEAD_CONTENT_PATH, HEAD_content);
    }
}
