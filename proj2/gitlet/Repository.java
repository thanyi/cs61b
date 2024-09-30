package gitlet;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Refs.*;
import static gitlet.Utils.*;
import static java.lang.System.exit;

// TODO: any imports you need here

/**
 * Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * @author ethanyi
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     * <p>
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     * <p>
     * <p>
     * the path we created as below:
     * <p>
     * .gitlet (folder)
     * |── objects (folder) // 存储commit对象文件
     * |-- commits
     * |-- blobs
     * |── refs (folder)
     * |── heads (folder) //指向目前的branch
     * |-- master (file)
     * |-- other file      //表示其他分支的路径
     * |-- HEAD (file)     // 保存HEAD指针的对应hashname
     * |-- addstage (folder)       // 暂存区文件夹
     * |-- removestage (folder)
     */

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



    /* TODO: fill in the rest of this class. */

    /**
     * initialize the folder instructure
     */
    public static void setupPersistence() {
        GITLET_DIR.mkdirs();
        COMMIT_FOLDER.mkdirs();
        BLOBS_FOLDER.mkdirs();
        REFS_DIR.mkdirs();
        HEAD_DIR.mkdirs();
        ADD_STAGE_DIR.mkdirs();
        REMOVE_STAGE_DIR.mkdirs();

    }

    /**
     * Check if the ARGS of java gitlet.Main is empty
     *
     * @param args
     */
    public static void checkArgsEmpty(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            exit(0);
        }
    }

    /**
     * To get Date obj a format to transform the object to String.
     *
     * @param date a Date obj
     * @return timestamp in standrad format
     */
    public static String dateToTimeStamp(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return dateFormat.format(date);
    }


    /**
     * To save some Commit objects into files .
     *
     * @apiNote 暂时没有用到
     */
    public void saveObject2File(File path, Commit obj) {
        // get the uid of this
        String hashname = obj.getHashName();

        // write obj to files
        File commitFile = new File(COMMIT_FOLDER, hashname);
        writeObject(commitFile, obj);
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
    public static void initPersistence() {
        // if .gitlet dir existed
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            exit(0);
        }
        // create the folders in need
        setupPersistence();
        // create timestamp,Commit and save commit into files
        Date timestamp_init = new Date(0);
        Commit initialCommit = new Commit("initial commit", timestamp_init, "", null,null);
        initialCommit.saveCommit();

        // save the hashname to heads dir
        String commitHashName = initialCommit.getHashName();
        saveBranch("master", commitHashName);

        // 将此时的HEAD指针指向commit
        saveHEAD(join(COMMIT_FOLDER, commitHashName));

    }

    /**
     * Adds a copy of the file as it currently exists to the staging area (see the description of the commit command).
     * For this reason, adding a file is also called staging the file for addition.
     * - Staging an already-staged file overwrites the previous entry in the staging area with the new contents.
     * - The staging area should be somewhere in .gitlet.
     * If the current working version of the file is identical to the version in the current commit, do not stage it to be added,
     * and remove it from the staging area if it is already there (as can happen when a file is changed, added, and then changed back to it’s original version).
     * The file will no longer be staged for removal (see gitlet rm), if it was at the time of the command.
     *
     * @param addFileName
     * @apiNote 这个函数用于实现git add
     */
    public static void addStage(String addFileName) {
        /* 如果文件名是空 */
        if (addFileName == null || addFileName.isEmpty()) {
            System.out.println("Please enter a file name.");
            exit(0);
        }
        /* 如果在工作目录中不存在此文件 */
        List<String> fileNames = plainFilenamesIn(CWD);
        if(!fileNames.contains(addFileName)) {
            System.out.println("File does not exist.");
            exit(0);
        }

        /* 将文件放入暂存区，文件名是内容的hash值，内容是源文件内容 */
        File fileAdded = join(CWD, addFileName);
        String fileContent = readContentsAsString(fileAdded);
        String blobName = sha1(fileContent);

        Blob blobAdd = new Blob(fileContent, blobName); // 使用blob进行对象化管理
        blobAdd.saveBlob();

        /* 不管原先是否存在，都会执行写逻辑*/
        /* addStage中写入指针,文件名是addFileName, 内容是暂存区保存的路径 */
        File blobPoint = join(ADD_STAGE_DIR, addFileName);
        writeContents(blobPoint, blobAdd.filePath.getName());
    }

    /**
     * Saves a snapshot of tracked files in the current commit and staging area so they can be restored at a later time,
     * creating a new commit. The commit is said to be tracking the saved files.
     *
     * By default, each commit’s snapshot of files will be exactly the same as its parent commit’s snapshot of files;
     * it will keep versions of files exactly as they are, and not update them.
     * A commit will only update the contents of files it is tracking that have been staged for addition at the time of commit,
     * in which case the commit will now include the version of the file that was staged instead of the version it got from its parent.
     * A commit will save and start tracking any files that were staged for addition but weren’t tracked by its parent.
     * Finally, files tracked in the current commit may be untracked in the new commit as a result being staged for removal by the rm command (below).
     */
    public static void commitFile(String commitMsg) {
        /* 获取addstage中的filename和hashname */
        List<String> fileNames = plainFilenamesIn(ADD_STAGE_DIR);
        /* 错误的情况，直接返回 */
        if(fileNames.isEmpty()){
            System.out.println("No changes added to the commit.");;
            exit(0);
        }

        if (commitMsg == null){
            System.out.println("Please enter a commit message.");
            exit(0);
        }


        /* 获取HEAD指针,这个指针指向目前最新的commit */
        String headHashName = readContentsAsString(HEAD_POINT);
        File commitFile = join(COMMIT_FOLDER, headHashName);

        /* 获取最新的commit*/
        Commit oldCommit = readObject(commitFile,Commit.class);

        /* 创建新的commit，newCommit根据oldCommit进行调整*/
        Commit newCommit = new Commit(oldCommit);
        newCommit.setParent(headHashName);  // 指定父节点
        newCommit.setTimestamp(new Date(System.currentTimeMillis())); // 修改新一次的commit的时间戳为目前时间

//        System.out.println("newCommit initialized! ");
        //todo:需要完成通过stage对commit的add操作

        /* 对每一个addstage中的fileName进行其路径的读取 */
        for(String stageFileName : fileNames) {
            String hashName = readContentsAsString(join(ADD_STAGE_DIR, stageFileName));
            newCommit.addBlob(stageFileName, hashName);     // 在newCommit中更新blob
            join(ADD_STAGE_DIR,stageFileName).delete();
        }

        newCommit.saveCommit();

        /* 更新HEAD指针和master指针 */
        saveHEAD(join(COMMIT_FOLDER, newCommit.getHashName()));
        saveBranch("master", newCommit.getHashName());

    }

}
