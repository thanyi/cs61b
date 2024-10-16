# Gitlet Design Document

**Name**: ethanyi9

目前所有指令构建均已完成


## Classes and Data Structures

### Class 1: Commit

对于需要**提交**的文件的类，此类使用最多，每次进行处理的时候都会构建commit对象

#### Instance Variables

- String message: 保存commit提交的评论
- Date timestamp: 提交时间，第一个是 Date(0)，根据Date对象进行
- String directParent: 这个commit的上一个commit。
- String otherParent：若是存在merge操作，则会使用此变量，记录merge [branchName] 中的branch commit为上一个节点
- HashMap<String,String> blobMap: 文件内容的hashMap，key为track文件的文件名，value是其对应的blob的hash名

#### Methods
getter和setter方法不做讲解，讲解其中其余的方法以及其他类可用的静态方法

成员方法：
- getHashName: 获取commit的sha-1 hash值，sha-1包括的内容是message, timestamp, directParent
- saveCommit(): 将对象保存进join(COMMIT_FOLDER, hashname)中，文件名为commit的hash名
- addBlob(String fileName, String blobName)：保存blobMap中键值对
- removeBlob(String fileName)：删除blobMap中的指定键值对

静态方法：
- getHeadCommit()：用于获取HEAD指针指向的Commit对象
- getBranchHeadCommit(String branchName, String error_msg)：用于获取branches文件夹中分支文件指向的Commit对象，error_msg参数是当不存在此branch时需要提供的错误信息
- getCommit(String hashName)：通过hashname来获取Commit对象，如果在commit文件夹中不存在此文件则返回null
- getCommitFromId(String commitId)：给定一个commitId，返回一个相对应的commit对象，若是没有这个commit对象，则返回null，与getCommit()的区别是支持前缀搜索
- getSplitCommit(Commit commitA, Commit commitB)：使用BFS方法查找commitA和commitB的最近的split Commit，不知道什么是split Commit的请翻阅文档

### Class 2 Refs

关于文件指针的类
#### Instance Variables

- REFS_DIR: ".gitlet/refs"文件夹
- HEAD_DIR: ".gitlet/refs/heads" 文件夹
- HEAD_CONTENT_PATH: ".gitlet/HEAD" 文件

...


#### Methods
- `saveBranch(String branchName, String hashName)`: 创建一个文件：路径是`join(HEAD_DIR, branchName)`，向其中写入`hashName`，也就是`commitId`
- `saveHEAD(String branchName, String branchHeadCommitHash)`: 在HEAD文件中写入当前branch的hash值,格式是`branchName + ":" + branchHeadCommitHash`
- getHeadBranchName():从HEAD文件中直接获取当前branch的名字

### Class 3 Blob
用于Blob存储相关的类
#### Instance Variables
- private String content：   blob中保存的内容
- public File filePath：     blob文件的自身路径
- private String hashName：  blob文件名，以hash为值
#### Methods

- saveBlob()： 将blob对象保存进 BLOB_FOLDER文件，内容就是blob文件的content

静态方法：
- getBlobContentFromName(String blobName)：根据blobName获取到Blob的内容，其中blobName是一个hash值，若是没有这个文件，返回null
- overWriteFileWithBlob(File file, String content)：将blob.content中的内容覆盖进file文件中




## Algorithms

### init
创建一个文件夹环境

```
.gitlet (folder)
    |── objects (folder) // 存储commit对象文件
        |-- commits
        |-- blobs
    |── refs (folder)
        |── heads (folder) //指向目前的branch
            |-- master (file)
            |-- other file      //表示其他分支的路径
        |-- HEAD (file)     // 保存HEAD指针的对应hashname
    |-- addstage (folder)       // 暂存区文件夹
    |-- removestage (folder)
```
    
### add
将指定的文件放入addstage文件夹中，将文件内容创建为blob文件，以内容的hash值作为文件名保存在objects/blobs文件夹中
```
.gitlet (folder)
    |── objects (folder) 
        |-- commits
        |-- blobs
            |-- <hash>  <----- 加入的file.txt文件内容
    |── refs (folder)
        |── heads (folder) 
            |-- master (file)
            |-- other file     
        |-- HEAD (file)     
    |-- addstage (folder)       
        |-- file.txt  <----- 保存blob文件的路径
    |-- removestage (folder)

file.txt  <----- 加入的文件
```

### commit
将addstage和removestage中的文件一个个进行响应操作，addStage中的进行添加，removeStage中的进行删除


### rm





## Persistence

