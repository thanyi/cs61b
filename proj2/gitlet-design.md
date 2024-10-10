# Gitlet Design Document

**Name**: ethanyi9

目前init、add、rm指令已完成

commit、log、global-log需要修改

## Classes and Data Structures

### Class 1: Commit

对于需要**提交**的文件的类

#### Instance Variables

- String message: 保存commit提交的评论
- Date timestamp: 提交时间，第一个是 Date(0)，根据Date对象进行
- String parent: 这个commit的上一个commit。
- HashMap<String,String> blobMap: 文件内容的hashMap，key为修改文件的文件名，value是其对应的blob的hash名

#### Methods
- getHashName: 获取commit的sha-1 hash值，包括的内容是message, timestamp, parent
- saveCommit: 将对象保存进文件中，文件名为commit的hash名
- addBlob：保存blob键值对，

### Class 2 Refs

关于文件指针的类
#### Instance Variables

- REFS_DIR: ".gitlet/refs"文件夹
- HEAD_DIR: ".gitlet/refs/heads" 文件夹
- HEAD_CONTENT_PATH: ".gitlet/HEAD" 文件

...


#### Methods
- saveHead: 保存HEAD指针
- saveBranch: 保存在refs/heads文件夹中的分支的头指针

### Class 3 Blob
用于Blob存储相关的类
#### Instance Variables
- private String content：   blob中保存的内容
- public File filePath：     blob文件的自身路径
- private String hashName： blob文件名，以hash为值
#### Methods

- void saveBlob()： 将blob对象保存为文件形式




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

