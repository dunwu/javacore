---
title: Java 虚拟机简介
date: 2021-05-24 15:41:47
order: 01
categories:
  - Java
  - JavaCore
  - JVM
tags:
  - Java
  - JavaCore
  - JVM
permalink: /pages/700204ee/
---

# Java 虚拟机简介

## 简介

Java 虚拟机（JVM）是运行 Java 程序的抽象计算引擎。它屏蔽了底层硬件和操作系统的差异，使得 Java 程序具有“一次编写，到处运行”的跨平台能力。JVM 的主要组件包括类加载器、运行时数据区和执行引擎。Hotspot 是目前最流行的 JVM 实现。

> JVM 能跨平台工作，主要是由于 JVM 屏蔽了与各个计算机平台相关的软件、硬件之间的差异。

## JVM 简介

### 计算机体系结构

真实的计算机体系结构的核心部分包含：

- 指令集
- 计算单元（CPU）
- 寻址方式
- 寄存器
- 存储单元

### JVM 体系结构简介

JVM 体系结构与计算机体系结构相似，它的核心部分包含：

- JVM 指令集
- 类加载器
- 执行引擎 - 相当于 JVM 的 CPU
- 内存区 - JVM 的存储
- 本地方法调用 - 调用 C/C++ 实现的本地方法

## Hotspot 架构

Hotspot 是最流行的 JVM。

Java 虚拟机的主要组件，包括**类加载器**、**运行时数据区**和**执行引擎**。

Hotspot 虚拟机拥有一个架构，它支持强大特性和能力的基础平台，支持实现高性能和强大的可伸缩性的能力。举个例子，Hotspot 虚拟机 JIT 编译器生成动态的优化，换句话说，它们在 Java 应用执行期做出优化，为底层系统架构生成高性能的本地机器指令。另外，经过它的运行时环境和多线程垃圾回收成熟的进化和连续的设计， Hotspot 虚拟机在高可用计算系统上产出了高伸缩性。

![](https://raw.githubusercontent.com/dunwu/images/master/cs/java/javacore/jvm/jvm-hotspot-architecture.png)

### Hotspot 关键组件

Java 虚拟机有三个组件关注着什么时候进行性能优化，堆空间是对象所存储的地方，这个区域被启动时选择的垃圾回收器管理，大部分调优选项与调整堆大小和根据你的情况选择最适当的垃圾收集器相关。即时编译器对性能也有很大的影响，但是使用新版本的 Java 虚拟机时很少需要调整。

![](https://raw.githubusercontent.com/dunwu/images/master/cs/java/javacore/jvm/jvm-hotspot-key-components.png)

### 性能指标

Java 虚拟机的性能指标主要有两点：

- **停顿时间** - 响应延迟是指一个应用回应一个请求的速度有多快。对关注响应能力的应用来说，长暂停时间是不可接受的，重点是在短的时间周期内能做出响应。
  - 桌面 UI 响应事件的速度
  - 网站返回网页的速度
  - 数据查询返回的速度
- **吞吐量** - 吞吐量关注在特定的时间周期内一个应用的工作量的最大值。对关注吞吐量的应用来说长暂停时间是可以接受的。由于高吞吐量的应用关注的基准在更长周期时间上，所以快速响应时间不在考虑之内。
  - 给定时间内完成事务的数量
  - 一小时内批处理程序完成的工作数量
  - 一小时内数据查询完成的数量

## JVM 内存简介

### 物理内存和虚拟内存

所谓物理内存就是通常所说的 RAM（随机存储器）。

虚拟内存使得多个进程在同时运行时可以共享物理内存，这里的共享只是空间上共享，在逻辑上彼此仍然是隔离的。

### 内核空间和用户空间

一个计算通常有固定大小的内存空间，但是程序并不能使用全部的空间。因为这些空间被划分为内核空间和用户空间，而程序只能使用用户空间的内存。

### 使用内存的 Java 组件

Java 启动后，作为一个进程运行在操作系统中。

有哪些 Java 组件需要占用内存呢？

- 堆内存：Java 堆、类和类加载器
- 栈内存：线程
- 本地内存：NIO、JNI

## 典型应用场景

### 场景一：选择合适的垃圾收集器

根据应用特点选择合适的 GC：

- **低延迟应用**（如 Web 服务）：选择 G1 或 ZGC，减少 GC 停顿时间
- **高吞吐量应用**（如批处理）：选择 Parallel GC，最大化 CPU 利用率
- **内存受限环境**（如嵌入式）：选择 Serial GC，减少资源开销

### 场景二：JVM 内存配置优化

根据服务器资源合理配置 JVM 参数：

```bash
# 典型的生产环境 JVM 配置
java -Xms4g -Xmx4g 
     -XX:+UseG1GC 
     -XX:MaxGCPauseMillis=200 
     -XX:+HeapDumpOnOutOfMemoryError 
     -XX:HeapDumpPath=/tmp/heapdump.hprof 
     -jar app.jar
```

### 场景三：诊断线上性能问题

使用 JVM 工具定位性能瓶颈：

```bash
# 查看 GC 状态
jstat -gcutil <pid> 1000

# 导出堆快照分析内存泄漏
jmap -dump:format=b,file=heap.hprof <pid>

# 查看线程死锁
jstack -l <pid> | grep -A 20 "Found one Java-level deadlock"
```

## 最佳实践

1. **设置相同的 -Xms 和 -Xmx**：避免堆内存动态扩容带来的性能波动。
2. **生产环境开启 HeapDump**：`-XX:+HeapDumpOnOutOfMemoryError` 方便事后分析 OOM。
3. **选择合适的 GC**：JDK 11+ 优先考虑 G1 或 ZGC，JDK 8 可考虑 CMS 或 G1。
4. **记录 GC 日志**：`-Xlog:gc*:file=gc.log` 便于分析 GC 行为。
5. **监控 JVM 指标**：通过 JMX 或 Prometheus 监控堆内存、GC、线程等关键指标。

## 常见问题

### Q1：JVM 和 JRE、JDK 有什么区别？

- **JVM**：运行 Java 字节码的虚拟机引擎
- **JRE**：JVM + Java 核心类库，可以运行但不能编译 Java 程序
- **JDK**：JRE + 编译器（javac）+ 工具（jstack/jmap 等），完整的开发环境

### Q2：什么是 JIT 编译器？

JIT（Just-In-Time）编译器在程序运行时将热点代码编译为本地机器码，提高执行速度。C1 编译器优化启动速度，C2 编译器优化峰值性能。JDK 9+ 的 AOT 编译可以在启动前预编译。

### Q3：为什么 Java 被称为“跨平台”语言？

Java 源码编译为字节码（.class 文件），字节码由 JVM 解释执行。不同平台有各自的 JVM 实现，但都支持相同的字节码格式，因此同一份字节码可以在不同平台运行。

## 参考资料

- [《深入理解 Java 虚拟机》](https://book.douban.com/subject/34907497/)
- [《Java 性能调优实战》](https://time.geekbang.org/column/intro/100028001)
- [Oracle JVM 官方文档](https://docs.oracle.com/en/java/javase/17/gctuning/)
