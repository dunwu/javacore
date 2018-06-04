---
title: Java开发环境配置
date: 2017/12/06
categories:
- javase
tags:
- java
- javase
---

# Java 开发环境配置

## 下载

进入 [JDK 官方下载地址](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) ，根据自己的环境选择下载所需版本。

## 安装

windows 环境的 jdk 包是 exe 安装文件，启动后根据安装向导安装即可。

Linux 环境的 jdk 包，解压到本地即可。

## 环境变量

### Windows

计算机 > 属性 > 高级系统设置 > 环境变量

添加以下环境变量：

`JAVA_HOME`：`C:\Program Files (x86)\Java\jdk1.8.0_91` （根据自己的实际路径配置）

`CLASSPATH`：`.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar;` （注意前面有个"."）

`Path`：`%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin;`
__
### Linux

执行 `vi /etc/profile` ，编辑环境变量文件

添加两行：

```sh
export JAVA_HOME=path/to/java
export PATH=JAVA_HOME/bin:JAVA_HOME/jre/bin:
```

执行 `source /etc/profile` ，立即生效。

## 测试安装成功

执行命令 `java -version` ，如果安装成功，会打印当前 java 的版本信息。
