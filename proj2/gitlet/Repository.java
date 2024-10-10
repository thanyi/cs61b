package gitlet;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Commit.*;
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
     *      |── objects (folder) // 存储commit对象文件
     *          |-- commits
     *          |-- blobs
     *      |── refs (folder)
     *          |── heads (folder) //指向目前的branch
     *              |-- master (file)
     *              |-- other file      //表示其他分支的路径
     *          |-- HEAD (file)     // 保存HEAD指针的对应hashname
     *      |-- addstage (folder)       // 暂存区文件夹
     *      |-- removestage (folder)
     */

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


//    /**
//     * To save some Commit objects into files .
//     *
//     * @apiNote 暂时没有用到
//     */
//    public void saveObject2File(File path, Commit obj) {
//        // get the uid of this
//        String hashname = obj.getHashName();
//
//        // write obj to files
//        File commitFile = new File(COMMIT_FOLDER, hashname);
//        writeObject(commitFile, obj);
//    }


    public static void printCommitLog(Commit commit) {
        System.out.println("===");
        System.out.println("commit " + commit.getHashName());
        System.out.println("Date: " + dateToTimeStamp(commit.getTimestamp()));
        System.out.println(commit.getMessage());
        System.out.print("\n");
    }

    /**
     * @param field      打印的标题区域
     * @param files      文件夹中的所有文件
     * @param branchName 指定的branchName
     */
    public static void printStatusPerField(String field, Collection<String> files, String branchName) {
        System.out.println("=== " + field + " ===");
        if (field.equals("Branches")) {
            for (var file : files) {
                // 如果是head文件
                if (file.equals(branchName)) {
                    System.out.println("*" + file);
                } else {
                    System.out.println(file);
                }
            }
        } else {
            for (var file : files) {
                System.out.println(file);
            }
        }

        System.out.print("\n");
    }


    /* ---------------------- 功能函数实现 --------------------- */

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
        Commit initialCommit = new Commit("initial commit", timestamp_init, "", null, null);
        initialCommit.saveCommit();

        // save the hashname to heads dir
        String commitHashName = initialCommit.getHashName();
        String branchName = "master";
        saveBranch(branchName, commitHashName);

        // 将此时的HEAD指针指向commit中的代表head的文件
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
//        List<String> fileNames = plainFilenamesIn(CWD);
//        if (!fileNames.contains(addFileName)) {
//            System.out.println("File does not exist.");
//            exit(0);
//        }
        File fileAdded = join(CWD, addFileName);
        if (!fileAdded.exists()) {
            System.out.println("File does not exist.");
            exit(0);
        }

        /* 将文件放入暂存区，文件名是内容的hash值，内容是源文件内容 */
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
     * <p>
     * By default, each commit’s snapshot of files will be exactly the same as its parent commit’s snapshot of files;
     * it will keep versions of files exactly as they are, and not update them.
     * A commit will only update the contents of files it is tracking that have been staged for addition at the time of commit,
     * in which case the commit will now include the version of the file that was staged instead of the version it got from its parent.
     * A commit will save and start tracking any files that were staged for addition but weren’t tracked by its parent.
     * Finally, files tracked in the current commit may be untracked in the new commit as a result being staged for removal by the rm command (below).
     */
    public static void commitFile(String commitMsg) {
        /* 获取addstage中的filename和hashname */
        List<String> addStageFiles = plainFilenamesIn(ADD_STAGE_DIR);
        List<String> removeStageFiles = plainFilenamesIn(REMOVE_STAGE_DIR);
        /* 错误的情况，直接返回 */
        if (addStageFiles.isEmpty() && removeStageFiles.isEmpty()) {
            System.out.println("No changes added to the commit.");
            ;
            exit(0);
        }

        if (commitMsg == null) {
            System.out.println("Please enter a commit message.");
            exit(0);
        }


        /* 获取最新的commit*/
        Commit oldCommit = getHeadCommit();

        /* 创建新的commit，newCommit根据oldCommit进行调整*/
        Commit newCommit = new Commit(oldCommit);
        newCommit.setParent(oldCommit.getHashName());  // 指定父节点
        newCommit.setTimestamp(new Date(System.currentTimeMillis())); // 修改新一次的commit的时间戳为目前时间
        newCommit.setMessage(commitMsg); // 修改新一次的commit的时间戳为目前时间


        /* 对每一个addstage中的fileName进行其路径的读取，保存进commit的blobMap */
        for (String stageFileName : addStageFiles) {
            String hashName = readContentsAsString(join(ADD_STAGE_DIR, stageFileName));
            newCommit.addBlob(stageFileName, hashName);     // 在newCommit中更新blob
            join(ADD_STAGE_DIR, stageFileName).delete();
        }

        HashMap<String, String> blobMap = newCommit.getBlobMap();

        /* 对每一个rmstage中的fileName进行其路径的读取，删除commit的blobMap中对应的值 */
        for (String stageFileName : removeStageFiles) {
            if (blobMap.containsKey(stageFileName)) {
                join(BLOBS_FOLDER, blobMap.get(stageFileName)).delete(); // 删除blobs中的文件
                newCommit.removeBlob(stageFileName);   // 在newCommit中删除removeStage中的blob
            }
            join(REMOVE_STAGE_DIR, stageFileName).delete();
        }

        newCommit.saveCommit();

        /* 更新HEAD指针和master指针 */
        saveHEAD(join(COMMIT_FOLDER, newCommit.getHashName()));
        saveBranch("master", newCommit.getHashName());

    }

    /**
     * Unstage the file if it is currently staged for addition.
     * If the file is tracked in the current commit, stage it for removal and remove the file from
     * the working directory if the user has not already done so (do not remove it unless it is tracked in the current commit).
     *
     * @param removeFileName 指定删除的文件名
     */
    public static void removeStage(String removeFileName) {
        /* 如果文件名是空或者如果工作区没有这个文件 */
        if (removeFileName == null || !join(CWD, removeFileName).exists()) {
            System.out.println("Please enter a file name.");
            exit(0);
        }


        /* 如果在暂存目录中不存在此文件,同时在在commit中不存在此文件 */
        Commit headCommit = getHeadCommit();
        HashMap<String, String> blobMap = headCommit.getBlobMap();
        List<String> addStageFiles = plainFilenamesIn(ADD_STAGE_DIR);
        if (!addStageFiles.contains(removeFileName) && !blobMap.containsKey(removeFileName)) {
            System.out.println("No reason to remove the file.");
            exit(0);
        }

        /* 如果addStage中存在，则删除 */
        File addStageFile = join(ADD_STAGE_DIR, removeFileName);
        if (addStageFile.exists())
            addStageFile.delete();

        /* 添加进removeStage */
        File remoteFilePoint = new File(REMOVE_STAGE_DIR, removeFileName);
        writeContents(remoteFilePoint, "");

        /* 删除工作目录下文件 */
        File fileDeleted = new File(CWD, removeFileName);
        fileDeleted.delete();
    }

    /**
     * Starting at the current head commit, display information about each commit backwards along the commit tree until the initial commit, following the first parent commit links, ignoring any second parents found in merge commits. (In regular Git, this is what you get with git log --first-parent). This set of commit nodes is called the commit’s history. For every node in this history, the information it should display is the commit id, the time the commit was made, and the commit message.
     */
    public static void printLog() {

        Commit headCommit = getHeadCommit();
        Commit commit = headCommit;

        while (!commit.getParent().equals("")) {
            printCommitLog(commit);
            commit = getCommit(commit.getParent());
        }
        /* 打印最开始的一项*/
        printCommitLog(commit);
    }


    /**
     * Like log, except displays information about all commits ever made. The order of the commits does not matter. Hint: there is a useful method in gitlet.Utils that will help you iterate over files within a directory.
     *
     * @apiNote 这是不关注分支，只是把文件夹中的内容都打印出来了
     */
    public static void printGlobalLog() {

        List<String> commitFiles = plainFilenamesIn(COMMIT_FOLDER);
        for (String commitFileName : commitFiles) {
            Commit commit = getCommit(commitFileName);
            printCommitLog(commit);
        }
    }

    /**
     * Prints out the ids of all commits that have the given commit message, one per line. If there are multiple such commits, it prints the ids out on separate lines. The commit message is a single operand; to indicate a multiword message, put the operand in quotation marks, as for the commit command below.
     */
    public static void findCommit(String commitMsg) {
        Commit headCommit = getHeadCommit();
        Commit commit = headCommit;
        boolean found = false;
        /* 如果msg相等就break，或者是到达初始提交就退出 */
        while (!commit.getParent().isEmpty()) {

            if (commit.getMessage().equals(commitMsg)) {
                found = true;
                System.out.println("commit " + commit.getHashName());
            }
            commit = getCommit(commit.getParent());
        }
        /* 检查最后一个提交 */
        if (commit.getMessage().equals(commitMsg)) {
            found = true;
            System.out.println("commit " + commit.getHashName());
        }

        if (!found) {
            System.out.println("Found no commit with that message.");
        }

    }


    /**
     * Displays what branches currently exist, and marks the current branch with a *. Also displays what files have been staged for addition or removal.
     */
    public static void showStatus() {
        /* 获取当前分支名 */
        Commit headCommit = getHeadCommit();
        String branchName = headCommit.getBranchName();

        List<String> filesInHead = plainFilenamesIn(HEAD_DIR);
        List<String> filesInAdd = plainFilenamesIn(ADD_STAGE_DIR);
        List<String> filesInRm = plainFilenamesIn(REMOVE_STAGE_DIR);

        HashMap<String, String> blobMap = headCommit.getBlobMap();
        Set<String> trackFiles = blobMap.keySet();  // commit中跟踪着的文件名
        LinkedList<String> modifiedFilesList = new LinkedList<>();
        LinkedList<String> untrackFilesList = new LinkedList<>();

        printStatusPerField("Branches", filesInHead, branchName);
        printStatusPerField("Staged Files", filesInAdd, branchName);
        printStatusPerField("Removed Files", filesInRm, branchName);

        /* 开始进行：Modifications Not Staged For Commit */

        /* 暂存已经添加，但内容与工作目录中的内容不同 */
        for (String fileAdd : filesInAdd) {
            /* 如果文件在暂存区存在，但是在工作区不存在，则直接加入modifiedFilesList */
            if (!join(CWD, fileAdd).exists()) {
                modifiedFilesList.add(fileAdd);
                continue;
            }

            String workFileContent = readContentsAsString(join(CWD, fileAdd));
            String addStageBlobName = readContentsAsString(join(ADD_STAGE_DIR, fileAdd));
            String addStageFileContent = readContentsAsString(join(BLOBS_FOLDER, addStageBlobName));

            if (!workFileContent.equals(addStageFileContent)) {
                modifiedFilesList.add(fileAdd);       // 当工作区和addStage中文件内容不一致，则进入modifiedFilesList
            }
        }

        /* 在当前提交中跟踪，在工作目录中更改，但未暂存 */
        for (String trackFile : trackFiles) {
            if (trackFile.isEmpty() || trackFile == null) {
                continue;
            }
            File workFile = join(CWD, trackFile);
            File fileInRmStage = join(REMOVE_STAGE_DIR, trackFile);

            if (!workFile.exists() && !fileInRmStage.exists()) {      // 当工作区文件直接不存在的情况
                modifiedFilesList.add(trackFile);       // 在rmStage中无此文件，同时工作区也没有这个文件
                continue;
            }
            if (!filesInAdd.contains(trackFile)) { // 当addStage中没有此文件

                String workFileContent = readContentsAsString(workFile);
                String blobFileContent = readContentsAsString(join(BLOBS_FOLDER, blobMap.get(trackFile)));

                if (!workFileContent.equals(blobFileContent)) {
                    modifiedFilesList.add(trackFile);       // 当正在track的文件被修改，但addStage中无此文件，则进入modifiedFilesList
                }

            }
        }

        printStatusPerField("Modifications Not Staged For Commit", modifiedFilesList, branchName);

        /* 开始进行：Untracked Files */
        List<String> workFiles = plainFilenamesIn(CWD);
        for (String workFile : workFiles) {
            if (!filesInAdd.contains(workFile) && !filesInRm.contains(workFile) && !trackFiles.contains(workFile)) {
                untrackFilesList.add(workFile);
                continue;
            }

            if (filesInRm.contains(workFile)) {
                untrackFilesList.add(workFile);
            }
        }

        printStatusPerField("Untracked Files", untrackFilesList, branchName);

    }




    public static void checkOut(String[] args){

        if(args.length == 2){
            //  git checkout [branchName]

        } else if (args.length == 4) {
            //  git checkout [commit id] -- [file name]

        } else if (args.length == 3) {
            //  git checkout -- [file name]


        }
    }

}
