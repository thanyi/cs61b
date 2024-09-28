# Gitlet Design Document

**Name**: ethanyi9

## Classes and Data Structures

### Class 1: Commit

对于需要**提交**的文件的类

#### Instance Variables

- String message: 保存commit提交的评论
- timestamp: 提交时间，第一个是 Date(0)，根据Date对象进行
- parent: 这个commit的上一个commit。
- blobHashName: 将commit的内容进行的hash值，值表示的是内容的hash

#### Methods
- getUid: 获取commit的sha-1 hash值，包括上面的局部变量
- saveCommit: 将对象保存进文件中

### Class 2 Refs

About HEAD structure.
#### Instance Variables

- REFS_DIR: ".gitlet/refs"文件夹
- HEAD_DIR: ".gitlet/refs/heads" 文件夹
- HEAD_CONTENT_PATH: ".gitlet/HEAD" 文件

#### Methods
- saveHead: Save HEAD file and branch file which contains the hash of current commit

## Algorithms

## Persistence

