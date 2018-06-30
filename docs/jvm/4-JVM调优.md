---
title: JVM 调优
date: 2018/05/29
categories:
- javase
tags:
- java
- javase
- jvm
---

# JVM 调优

<!-- TOC depthFrom:2 depthTo:3 -->

- [命令](#命令)
    - [jmap](#jmap)
    - [jstack](#jstack)
    - [jps](#jps)
    - [jstat](#jstat)
    - [jhat](#jhat)
    - [jinfo](#jinfo)
- [GC 优化配置](#gc-优化配置)
- [GC 类型设置](#gc-类型设置)
- [资料](#资料)

<!-- /TOC -->

## 命令

### jmap

jmap 即 JVM Memory Map。

**jmap 用于生成 heap dump 文件**。

如果不使用这个命令，还可以使用 `-XX:+HeapDumpOnOutOfMemoryError` 参数来让虚拟机出现 OOM 的时候，自动生成 dump 文件。

jmap 不仅能生成 dump 文件，还可以查询 finalize 执行队列、Java 堆和永久代的详细信息，如当前使用率、当前使用的是哪种收集器等。

命令格式：

```
jmap [option] LVMID
```

option 参数：

- dump - 生成堆转储快照
- finalizerinfo - 显示在 F-Queue 队列等待 Finalizer 线程执行 finalizer 方法的对象
- heap - 显示 Java 堆详细信息
- histo - 显示堆中对象的统计信息
- permstat - to print permanent generation statistics
- F - 当-dump 没有响应时，强制生成 dump 快照

示例：

dump 堆到文件，format 指定输出格式，live 指明是活着的对象，file 指定文件名

```
$ jmap -dump:live,format=b,file=dump.hprof 28920
Dumping heap to /home/xxx/dump.hprof ...
Heap dump file created
```

dump.hprof 这个后缀是为了后续可以直接用 MAT(Memory Anlysis Tool)打开。

### jstack

**jstack 用于生成 java 虚拟机当前时刻的线程快照。**

线程快照是当前 java 虚拟机内每一条线程正在执行的方法堆栈的集合，生成线程快照的主要目的是定位线程出现长时间停顿的原因，如线程间死锁、死循环、请求外部资源导致的长时间等待等。

线程出现停顿的时候通过 jstack 来查看各个线程的调用堆栈，就可以知道没有响应的线程到底在后台做什么事情，或者等待什么资源。 如果 java 程序崩溃生成 core 文件，jstack 工具可以用来获得 core 文件的 java stack 和 native stack 的信息，从而可以轻松地知道 java 程序是如何崩溃和在程序何处发生问题。另外，jstack 工具还可以附属到正在运行的 java 程序中，看到当时运行的 java 程序的 java stack 和 native stack 的信息, 如果现在运行的 java 程序呈现 hung 的状态，jstack 是非常有用的。

命令格式：

```
jstack [option] LVMID
```

option 参数：

- `-F` - 当正常输出请求不被响应时，强制输出线程堆栈
- `-l` - 除堆栈外，显示关于锁的附加信息
- `-m` - 如果调用到本地方法的话，可以显示 C/C++的堆栈

### jps

jps(JVM Process Status Tool)，显示指定系统内所有的 HotSpot 虚拟机进程。

命令格式：

```
jps [options] [hostid]
```

option 参数：

- `-l` - 输出主类全名或 jar 路径
- `-q` - 只输出 LVMID
- `-m` - 输出 JVM 启动时传递给 main()的参数
- `-v` - 输出 JVM 启动时显示指定的 JVM 参数

其中[option]、[hostid]参数也可以不写。

```
$ jps -l -m
28920 org.apache.catalina.startup.Bootstrap start
11589 org.apache.catalina.startup.Bootstrap start
25816 sun.tools.jps.Jps -l -m
```

### jstat

jstat(JVM statistics Monitoring)，是用于监视虚拟机运行时状态信息的命令，它可以显示出虚拟机进程中的类装载、内存、垃圾收集、JIT 编译等运行数据。

命令格式：

```
jstat [option] LVMID [interval] [count]
```

参数：

- [option] - 操作参数
- LVMID - 本地虚拟机进程 ID
- [interval] - 连续输出的时间间隔
- [count] - 连续输出的次数

### jhat

jhat(JVM Heap Analysis Tool)，是与 jmap 搭配使用，用来分析 jmap 生成的 dump，jhat 内置了一个微型的 HTTP/HTML 服务器，生成 dump 的分析结果后，可以在浏览器中查看。

注意：一般不会直接在服务器上进行分析，因为 jhat 是一个耗时并且耗费硬件资源的过程，一般把服务器生成的 dump 文件复制到本地或其他机器上进行分析。

命令格式：

```
jhat [dumpfile]
```

### jinfo

jinfo(JVM Configuration info)，用于实时查看和调整虚拟机运行参数。

之前的 jps -v 口令只能查看到显示指定的参数，如果想要查看未被显示指定的参数的值就要使用 jinfo 口令

命令格式：

```
jinfo [option] [args] LVMID
```

option 参数：

> - -flag : 输出指定 args 参数的值
> - -flags : 不需要 args 参数，输出所有 JVM 参数的值
> - -sysprops : 输出系统属性，等同于 System.getProperties()

## GC 优化配置

| 配置            | 描述             |
| --------------- | ---------------- |
| -Xms            | 初始化堆内存大小 |
| -Xmx            | 堆内存最大值     |
| -Xmn            | 新生代大小       |
| -XX:PermSize    | 初始化永久代大小 |
| -XX:MaxPermSize | 永久代最大容量   |

## GC 类型设置

| 配置                    | 描述                                      |
| ----------------------- | ----------------------------------------- |
| -XX:+UseSerialGC        | 串行垃圾回收器                            |
| -XX:+UseParallelGC      | 并行垃圾回收器                            |
| -XX:+UseConcMarkSweepGC | 并发标记扫描垃圾回收器                    |
| -XX:ParallelCMSThreads= | 并发标记扫描垃圾回收器 = 为使用的线程数量 |
| -XX:+UseG1GC            | G1 垃圾回收器                             |

```java
java -Xmx12m -Xms3m -Xmn1m -XX:PermSize=20m -XX:MaxPermSize=20m -XX:+UseSerialGC -jar java-application.jar
```

## 资料

[JVM（4）：Jvm 调优-命令篇](http://www.importnew.com/23761.html)
http://www.hollischuang.com/archives/110
https://segmentfault.com/a/1190000005174819
http://www.importnew.com/19264.html
https://blog.csdn.net/lxhandlbb/article/details/76695607