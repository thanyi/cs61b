package gitlet;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Commit.*;
import static gitlet.Refs.*;
import static gitlet.Utils.*;
import static gitlet.Blob.*;
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




    public static boolean untrackFileExists(Commit commit){
        List<String> workFileNames = plainFilenamesIn(CWD);
        Set<String> currTrackSet = commit.getBlobMap().keySet();
        /* 先检测CWD中是否存在未被current branch跟踪的文件 */

        for(String workFile : workFileNames){
            if (!currTrackSet.contains(workFile)){
                return true;
            }
        }
        return false;
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

        File fileAdded = join(CWD, addFileName);
        /* 如果在工作目录中不存在此文件 */
        String fileAddedContent = readContentsAsString(fileAdded);
        if (!fileAdded.exists()) {
            System.out.println("File does not exist.");
            exit(0);
        }

        Commit headCommit = getHeadCommit();
        HashMap<String, String> headCommitBlobMap = headCommit.getBlobMap();
        /* 如果这个文件是已经被track中的 */
        if( headCommitBlobMap.containsKey(addFileName) ){
            String fileAddedInHash = headCommit.getBlobMap().get(addFileName);
            String commitContent = getBlobContentFromName(fileAddedInHash);

            /* 如果暂存内容和想要添加内容一致，则不将其纳入暂存区，同时将其从暂存区删除（如果存在）,同时将其从removal区移除 */
            if(commitContent.equals(fileAddedContent)){
                List<String> filesAdd = plainFilenamesIn(ADD_STAGE_DIR);
                List<String> filesRm = plainFilenamesIn(REMOVE_STAGE_DIR);
                /* 如果在暂存区存在,从暂存区删除 */
                if (filesAdd.contains(addFileName)){
                    join(ADD_STAGE_DIR,addFileName).delete();
                }
                /* 如果在removal area存在,从中删除 */
                if (filesRm.contains(addFileName)){
                    join(REMOVE_STAGE_DIR,addFileName).delete();
                }

                return; //直接退出
            }

        }

        /* 将文件放入暂存区，blob文件名是内容的hash值，内容是源文件内容 */
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
        newCommit.setBranchName(oldCommit.getBranchName()); // 在log或者status中需要展示本次commit的分支


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

        if(!blobMap.containsKey(removeFileName)){
            if (!addStageFiles.contains(removeFileName) ) {
                System.out.println("No reason to remove the file.");
                exit(0);
            }

        }

        /* 如果addStage中存在，则删除 */
        File addStageFile = join(ADD_STAGE_DIR, removeFileName);
        if (addStageFile.exists())
            addStageFile.delete();

        /* 当此文件正被track中 */
        if(blobMap.containsKey(removeFileName)){
            /* 添加进removeStage */
            File remoteFilePoint = new File(REMOVE_STAGE_DIR, removeFileName);
            writeContents(remoteFilePoint, "");

            /* 删除工作目录下文件,注意仅在这个文件被track的时候进行删除 */
            File fileDeleted = new File(CWD, removeFileName);
            restrictedDelete(fileDeleted);
        }



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
        Set<String> trackFileSet = blobMap.keySet();  // commit中跟踪着的文件名
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

        /* 在当前commit中跟踪，在工作目录中更改，但未暂存 */
        for (String trackFile : trackFileSet) {
            if (trackFile.isEmpty() || trackFile == null) {
                continue;
            }
            File workFile = join(CWD, trackFile);
            File fileInRmStage = join(REMOVE_STAGE_DIR, trackFile);

            if (!workFile.exists() ) {      // 当工作区文件直接不存在的情况
                if(!fileInRmStage.exists()){
                    modifiedFilesList.add(trackFile);       // 在rmStage中无此文件，同时工作区也没有这个文件
                }
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
            if (!filesInAdd.contains(workFile) && !filesInRm.contains(workFile) && !trackFileSet.contains(workFile)) {
                untrackFilesList.add(workFile);
                continue;
            }

            if (filesInRm.contains(workFile)) {
                untrackFilesList.add(workFile);
            }
        }

        printStatusPerField("Untracked Files", untrackFilesList, branchName);

    }


    /**
     * 1.   Takes the version of the file as it exists in the head commit and puts it in the working directory,
     *      overwriting the version of the file that’s already there if there is one.
     *      The new version of the file is not staged.
     *
     * 2.   Takes the version of the file as it exists in the commit with the given id,
     *      and puts it in the working directory, overwriting the version of the file
     *      that’s already there if there is one. The new version of the file is not staged.
     *
     *
     * 3.   Takes all files in the commit at the head of the given branch, and puts them in the working directory,
     *      overwriting the versions of the files that are already there if they exist.
     *      Also, at the end of this command, the given branch will now be considered the current branch (HEAD).
     *      Any files that are tracked in the current branch but are not present in the checked-out branch are deleted.
     *      The staging area is cleared, unless the checked-out branch is the current branch (see Failure cases below).
     * @param args
     */
    public static void checkOut(String[] args){
        String fileName ;
        if(args.length == 2){
            //  git checkout branchName
            /* 如果checkout的分支就是原分支 */
            String branchName = args[1];
            Commit headCommit = getHeadCommit();

            if (branchName.equals(headCommit.getBranchName())){
                System.out.println("No need to checkout the current branch.");
                exit(0);
            }

            Commit branchHeadCommit = getBranchHeadCommit(branchName,"No such branch exists");  // 获取branchName的head对应的commit
            HashMap<String, String> branchHeadBlobMap = branchHeadCommit.getBlobMap();
            Set<String> fileNameSet = branchHeadBlobMap.keySet();

            List<String> workFileNames = plainFilenamesIn(CWD);

            if(untrackFileExists(headCommit)){
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                exit(0);
            }

            /* 检测完后清空CWD文件夹 */
            for(String workFile : workFileNames){
                restrictedDelete(join(CWD,workFile));
            }

            /* 将fileNameSet中每一个跟踪的文件重写入工作文件夹中 */
            for(var trackedfileName : fileNameSet){
                // 每一个trackedfileName是一个commit中跟踪的fileName
                File workFile = join(CWD, trackedfileName);
                String blobHash = branchHeadBlobMap.get(trackedfileName);   // 文件对应的blobName
                String blobFromNameContent = getBlobContentFromName(blobHash);
                writeContents(workFile,blobFromNameContent);
            }

            /* 将目前给定的分支视作当前分支 */
            branchHeadCommit.setBranchName(branchName);
            saveBranch(branchName,branchHeadCommit.getHashName());
            saveHEAD(join(HEAD_DIR,branchName));

        } else if (args.length == 4) {
            //  git checkout [commit id] -- [file name]

            /* 获取到Blob对象 */
            fileName = args[3];
            String commitId = args[1];
            Commit commit = getHeadCommit();

            /* todo:是否可以进行对objects文件夹的重构，实现hashMap结构
                使得时间效率上不是线性, 而不是依靠链表查找 */
            if (getCommitFromId(commitId) == null){
                System.out.println("No commit with that id exists.");
                exit(0);
            }else {
                commit = getCommitFromId(commitId);
            }

            if(!commit.getBlobMap().containsKey(fileName) ){
                System.out.println("File does not exist in that commit.");
                exit(0);
            }
            String blobName = commit.getBlobMap().get(fileName);
            String targetBlobContent = getBlobContentFromName(blobName);

            /* 将Blob对象中的内容覆盖working directory中的内容 */
            File fileInWorkDir = join(CWD, fileName);
            overWriteFileWithBlob(fileInWorkDir,targetBlobContent);

        } else if (args.length == 3) {
            //  git checkout -- [file name]

            /* 获取到Blob对象中的内容 */
            fileName = args[2];
            Commit headCommit = getHeadCommit();
            if(!headCommit.getBlobMap().containsKey(fileName) ){
                System.out.println("File does not exist in that commit.");
                exit(0);
            }
            String blobName = headCommit.getBlobMap().get(fileName);
            String targetBlobContent = getBlobContentFromName(blobName);

            /* 将Blob对象中的内容覆盖working directory中的内容 */
            File fileInWorkDir = join(CWD, fileName);
            overWriteFileWithBlob(fileInWorkDir,targetBlobContent);

        }
    }


    /**
     * Creates a new branch with the given name, and points it at the current head commit.
     * A branch is nothing more than a name for a reference (a SHA-1 identifier) to a commit node.
     * This command does NOT immediately switch to the newly created branch (just as in real Git).
     * Before you ever call branch, your code should be running with a default branch called “master”.
     */
    public static void createBranch(String branchName){
        Commit headCommit = getHeadCommit();
        saveBranch(branchName, headCommit.getHashName());
        Commit commit = headCommit;
        /* 将此split point之前的所有commit的branch设置为"common" */
        while (!headCommit.getParent().isEmpty()) {
            commit.setBranchName("common");
            commit = getCommit(commit.getParent());
        }

    }


    /**
     * Deletes the branch with the given name.
     * This only means to delete the pointer associated with the branch;
     * it does not mean to delete all commits that were created under the branch, or anything like that.
     */
    public static void removeBranch(String branchName){
        /* 检测是否有相关Branch */
        File brancheFile = join(HEAD_DIR, branchName);
        if(!brancheFile.exists()){
            System.out.println("A branch with that name does not exist.");
            exit(0);
        }
        /* 检测Branch是否为curr branch */
        Commit headCommit = getHeadCommit();
        if(headCommit.getBranchName().equals(branchName)){
            System.out.println("Cannot remove the current branch.");
            exit(0);
        }
        /* 删除这个branch的指针文件 */
        File branchHeadPoint = join(HEAD_DIR, branchName);
        restrictedDelete(branchHeadPoint);
    }

    /**
     * Checks out all the files tracked by the given commit.
     * Removes tracked files that are not present in that commit.
     * Also moves the current branch’s head to that commit node.
     * See the intro for an example of what happens to the head pointer after using reset.
     * The [commit id] may be abbreviated as for checkout.
     * The staging area is cleared. The command is essentially checkout of an arbitrary commit that also changes the current branch head.
     *
     * @apiNote java gitlet.Main reset [commit id]      将文件内容全部转化为[commit id]中的文件
     */
    public static void reset(String commitId){

        if(getCommitFromId(commitId) == null){
            System.out.println("No commit with that id exists.");
            exit(0);
        }
        Commit headCommit = getHeadCommit();
        Commit commit = getCommitFromId(commitId);
        Set<String> fileNameSet = commit.getBlobMap().keySet();
        Set<String> currTrackSet = headCommit.getBlobMap().keySet();
        HashMap<String, String> commitBlobMap = commit.getBlobMap();

        /* 先检测CWD中是否存在未被current branch跟踪的文件 */
        List<String> workFileNames = plainFilenamesIn(CWD);

        if(untrackFileExists(headCommit)){
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            exit(0);
        }

        /* 检测完后清空CWD文件夹 */
        for(String workFile : workFileNames){
            restrictedDelete(join(CWD,workFile));
        }

        /* 将fileNameSet中每一个跟踪的文件重写入工作文件夹中 */
        for(var trackedfileName : fileNameSet){
            // 每一个trackedfileName是一个commit中跟踪的fileName
            File workFile = join(CWD, trackedfileName);
            String blobHash = commitBlobMap.get(trackedfileName);   // 文件对应的blobName
            String blobFromNameContent = getBlobContentFromName(blobHash);
            writeContents(workFile,blobFromNameContent);
        }

        /* 将目前给定的HEAD指针指向这个commit */
        writeContents(HEAD_POINT, commitId);
    }


    /**
     * Merges files from the given branch into the current branch.
     * If the split point is the same commit as the given branch,
     * then we do nothing; the merge is complete, and the operation ends with the message: Given branch is an ancestor of the current branch.
     *
     * If the split point is the current branch, then the effect is to check out the given branch,
     * and the operation ends after printing the message: Current branch fast-forwarded.
     * Otherwise, we continue with the steps below.
     *
     * 1. other：被修改      HEAD：未被修改 --->  working DIR: other, 并且需要被add
     * 2. other：未被修改    HEAD：被修改   --->  working DIR: HEAD
     * 3. other：被修改      HEAD：被修改   --->  （一致的修改）  working DIR: HEAD, 相当于什么都不做
     *                                   |->  （不一致的修改）  working DIR: Conflict
     * 4. split：不存在      other：不存在    HEAD：被添加   --->  working DIR: HEAD
     * 5. split：不存在      other：被添加    HEAD：不存在   --->  working DIR: other, 并且需要被add
     * 6. other：被删除      HEAD：未被修改   --->  working DIR: 被删除，同时被暂存于removal
     * 7. other：未被修改     HEAD：被删除   --->  working DIR: 被删除
     */
    public static void mergeBranch(String branchName){

        List<String> addStageFiles = plainFilenamesIn(ADD_STAGE_DIR);
        List<String> rmStageFiles = plainFilenamesIn(REMOVE_STAGE_DIR);
        /* 如果存在暂存，直接退出 */
        if(!addStageFiles.isEmpty() || !rmStageFiles.isEmpty()){
            System.out.println("You have uncommitted changes.");
            exit(0);
        }
        Commit headCommit = getHeadCommit();

        if(headCommit.getBranchName().equals(branchName)){
            System.out.println("Cannot merge a branch with itself.");
            exit(0);
        }
        /* 查看是否存在未被跟踪的文件 */
        if(untrackFileExists(headCommit)){
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            exit(0);
        }

        Commit otherHeadCommit = getBranchHeadCommit(branchName,"A branch with that name does not exist."); // 如果不存在这个branch，则报错
        Commit commit = headCommit;
        Commit splitCommit = null;
        /* 获取当前splitCommit对象 */
        while(!commit.getParent().isEmpty()){
            if(commit.getBranchName().equals("common")){
                splitCommit = commit;
                break;
            }
        }

        HashMap<String, String> splitCommitBolbMap = splitCommit.getBlobMap();
        HashMap<String, String> headCommitBolbMap = headCommit.getBlobMap();
        HashMap<String, String> otherHeadCommitBolbMap = otherHeadCommit.getBlobMap();

        /* 从split中的文件开始 */
        for (var splitTrackName :splitCommitBolbMap.keySet()){

                // 如果在HEAD中未被修改(包括未被删除）
                if (headCommitBolbMap.containsKey(splitTrackName) && headCommitBolbMap.get(splitTrackName).equals(splitCommitBolbMap.get(splitTrackName))){

                    // 如果other中存在此文件
                    if(otherHeadCommitBolbMap.containsKey(splitTrackName) ){
                        /* 情况1 HEAD中未被修改，other中被修改*/
                        if (!otherHeadCommitBolbMap.get(splitTrackName).equals(splitCommitBolbMap.get(splitTrackName))){
                            // 使用checkout将other的文件覆盖进工作区，同时将其add进暂存区
                            String[] checkOutArgs = {"checkout",otherHeadCommit.getHashName(),"--",splitTrackName};
                            checkOut(checkOutArgs);
                            addStage(splitTrackName);
                        }

                    }else {
                        /* 情况6: 当HEAD未修改，other中被删除 */
                        removeStage(splitTrackName);
                    }

                }else {
                    // 在HEAD中被修改（包括被删除）

                    if (otherHeadCommitBolbMap.containsKey(splitTrackName) && otherHeadCommitBolbMap.get(splitTrackName).equals(splitCommitBolbMap.get(splitTrackName))){
                        /* 情况2 HEAD中被修改，other中未被修改，则不修改任何事情 */
                        /* 情况7 HEAD中被删除，other中未被修改，则不修改任何事情 */
                        continue;
                    }else {
                        /* 情况3 HEAD中被修改，other中被修改(包括被删除)*/
                        if(otherHeadCommitBolbMap.get(splitTrackName).equals(headCommitBolbMap.get(splitTrackName))){
                            /* 情况3a 一致的修改 */
                            continue;
                        }else {
                            if(!otherHeadCommitBolbMap.containsKey(splitTrackName) && !headCommitBolbMap.containsKey(splitTrackName)){
                                /* 情况3a 一致的修改，都进行了删除 */
                                continue;
                            }else {
                                /* TODO：情况3b 不一致的修改，需要进行conflict冲突操作 */

                            }

                        }
                    }
                }
        }
        /* 从HEAD中的文件开始 */
        for (var headTrackName : headCommitBolbMap.keySet()){
            if (!otherHeadCommitBolbMap.containsKey(headTrackName) && !splitCommitBolbMap.containsKey(headTrackName)){
                /* 情况4：如果在other和split中都没有这个文件 */
                continue;
            }
        }
        /* 从other中的文件开始 */
        for (var otherTrackName : otherHeadCommitBolbMap.keySet()){
            if (!headCommitBolbMap.containsKey(otherTrackName) && !splitCommitBolbMap.containsKey(otherTrackName)){
                /* 情况5：如果在head和split中都没有这个文件 */
                String[] checkOutArgs = {"checkout",otherHeadCommit.getHashName(),"--",otherTrackName};
                checkOut(checkOutArgs);
                addStage(otherTrackName);
            }
        }

    }

}
