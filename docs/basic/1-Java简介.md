---
title: Java 简介
date: 2018/08/29
categories:
  - javacore
tags:
  - java
  - javacore
---

> :pushpin: **关键词：** JDK、JRE、JSR、JVM、Java SE、Java EE、Java ME

# Java 简介

<!-- TOC depthFrom:2 depthTo:3 -->

- [Java 历史](#java-历史)
- [Java 术语](#java-术语)
    - [JDK](#jdk)
    - [JRE](#jre)
    - [JCP](#jcp)
    - [JSR](#jsr)
    - [Java SE](#java-se)
    - [Java EE](#java-ee)
    - [Java ME](#java-me)
    - [JVM](#jvm)

<!-- /TOC -->

Java 是一门面向对象编程语言。

Java 具有简单性、面向对象、分布式、健壮性、安全性、平台独立与可移植性、多线程、动态性等特点。Java 可以编写桌面应用程序、Web 应用程序、分布式系统和嵌入式系统应用程序等。

## Java 历史


| 版本      | 描述                                                                                                       |
|:----------|:-----------------------------------------------------------------------------------------------------------|
| 1991/1/1  | Sun 公司成立了 Green 项目小组,专攻智能家电的嵌入式控制系统                                                 |
| 1991/2/1  | 放弃 C++,开发新语言，命名为“Oak”                                                                           |
| 1991/6/1  | JamesGosling 开发了 Oak 的解释器                                                                           |
| 1992/1/1  | Green 完成了 Green 操作系统、Oak 语言、类库等开发                                                          |
| 1992/11/1 | Green 计划转化成“FirstPerson”，一个 Sun 公司的全资母公司                                                   |
| 1993/2/1  | 获得时代华纳的电视机顶盒交互系统的订单，于是开发的重心从家庭消费电子产品转到了电视盒机顶盒的相关平台上。   |
| 1994/6/1  | FirstPerson 公司倒闭，员工都合并到 Sun 公司。Liveoak 计划启动了，目标是使用 Oak   语言设计出一个操作系统。 |
| 1994/7/1  | 第一个 Java 语言的 Web 浏览器 WebRunner（后来改名为 HotJava），Oak 更名为 Java。                           |
| 1994/10/1 | VanHoff 编写的 Java 编译器用于 Java 语言                                                                   |
| 1995/3/1  | 在 SunWorld 大会，Sun 公司正式介绍了 Java 和 HotJava。                                                     |
| 1996/1/1  | JDK1.0 发布                                                                                                |
| 1997/2/1  | J2SE1.1 发布                                                                                               |
| 1998/12/1 | J2SE1.2 发布                                                                                               |
| 1999/6/1  | 发布 Java 的三个版本：J2SE、J2EE、J2ME                                                                     |
| 2000/5/1  | J2SE1.3 发布                                                                                               |
| 2001/9/1  | J2EE1.3 发布                                                                                               |
| 2002/2/1  | J2SE1.4 发布                                                                                               |
| 2004/9/1  | J2SE1.5 发布，将 J2SE1.5 改名 JavaSE5.0                                                                    |
| 2005/6/1  | JavaSE6.0 发布,J2EE 更名为 JavaEE，J2SE 更名为 JavaSE，J2ME 更名为 JavaME                                  |
| 2006/12/1 | JRE6.0 发布                                                                                                |
| 2006/12/1 | JavaSE6 发布                                                                                               |
| 2009/12/1 | JavaEE6 发布                                                                                               |
| 2009/4/1  | Oracle 收购 Sun                                                                                            |
| 2011/7/1  | JavaSE7 发布                                                                                               |
| 2014/3/1  | JavaSE8 发布                                                                                               |
| 2017/9/23 | JavaSE9 发布                                                                                               |
| 2018/3/21 | JavaSE10 发布                                                                                              |

## Java 术语

初学 Java，常常会看到一些术语，却不知是何意义。这里，我们来了解一下：

### JDK

JDK（Java Development Kit）称为 Java 开发包或 Java 开发工具，是一个编写 Java 的 Applet 小程序和应用程序的程序开发环境。JDK 是整个 Java 的核心，包括了 Java 运行环境（Java Runtime Envirnment），一些 Java 工具和 Java 的核心类库（Java API）。不论什么 Java 应用服务器实质都是内置了某个版本的 JDK。主流的 JDK 是 Sun 公司发布的 JDK，除了 Sun 之外，还有很多公司和组织都开发了自己的 JDK，例如，IBM 公司开发的 JDK，BEA 公司的 Jrocket，还有 GNU 组织开发的 JDK。

### JRE

另外，可以把 Java API 类库中的 Java SE API 子集和 Java 虚拟机这两部分统称为 JRE（JAVA Runtime Environment），JRE 是支持 Java 程序运行的标准环境。

JRE 是个运行环境，JDK 是个开发环境。因此写 Java 程序的时候需要 JDK，而运行 Java 程序的时候就需要 JRE。而 JDK 里面已经包含了 JRE，因此只要安装了 JDK，就可以编辑 Java 程序，也可以正常运行 Java 程序。但由于 JDK 包含了许多与运行无关的内容，占用的空间较大，因此运行普通的 Java 程序无须安装 JDK，而只需要安装 JRE 即可。

### JCP

JCP（Java Community Process）是一个开放的国际组织，主要由 Java 开发者以及被授权者组成，职能是发展和更新 Java 技术规范、参考实现（RI）、技术兼容包（TCK）。
JCP 维护的规范包括 Java ME、Java SE、Java EE、XML、OSS、JAIN 等。组织成员可以提交 JSR（Java Specification Requests），通过特定程序以后，进入到下一版本的规范里面。

### JSR

JSR 是早期提议和最终发布的 Java 平台规范的具体描述。通常，一个新的 JSR 的提出是为了增加或者规范 Java 平台的功能。某个具体的 JSR 由专家组共同来制定，工作由组长协调。例如，CLDC1.0（Connected Limited Device Configuration，JSR30）

JSR 完成后，相关的规范及 JavaAPI 会在 JCP 的官方网站发布。设备制造商可以在自己的产品中实现某个 JSR，如 MIDP2.0（JSRll8）。但是这些都必须要通过 TCK（Technology Compatibility Kit）测试以确保技术兼容性。 [32]
按照技术类别可以分成以下几类：

- J2EE 平台规范
- J2SE 平台规范
- J2ME 平台规范
- 运营支持系统规范（OSS）
- 综合网络的 Java 应用（JAIN）
- XML 操作规范

### Java SE

Java SE 是 Java 平台标准版（Java Platform Standard Edition）的简称 。Java SE 用于开发和部署桌面、服务器以及嵌入设备和实时环境中的 Java 应用程序。Java SE 包括用于开发 Java Web 服务的类库，同时，Java SE 为 Java EE 和 Java ME 提供了基础。

### Java EE

Java EE 是 Java 平台企业版（Java Platform Enterprise Edition）的简称。Java EE 是一种简化企业解决方案的开发、部署和管理相关的复杂问题的体系结构。Java EE 技术的基础就是 Java SE，Java EE 不仅巩固了标准版中的许多优点，例如“编写一次、随处运行”的特性、方便存取数据库的 JDBC API、CORBA 技术以及能够在 Internet 应用中保护数据的安全模式等等，同时还提供了对 EJB（Enterprise JavaBeans）、Java Servlets API、JSP（Java Server Pages）以及 XML 技术的全面支持。其最终目的就是成为一个能够使企业开发者大幅缩短投放市场时间的体系结构。

### Java ME

Java ME 是 Java 微版（Java Platform Micro Edition）的简称，是一个技术和规范的集合，它为移动设备（包括消费类产品、嵌入式设备、高级移动设备等）提供了基于 Java 环境的开发与应用平台。Java ME 目前分为两类配置，一类是面向小型移动设备的 CLDC（Connected Limited Device Profile），一类是面向功能更强大的移动设备如智能手机和机顶盒，称为 CDC（Connected Device Profile CDC）。

### JVM

JVM 是 Java Virtual Machine（Java 虚拟机）的缩写，JVM 是一种用于计算设备的规范，它是一个虚构出来的计算机，是通过在实际的计算机上仿真模拟各种计算机功能来实现的。

Java 语言的一个非常重要的特点就是与平台的无关性。而使用 Java 虚拟机是实现这一特点的关键。一般的高级语言如果要在不同的平台上运行，至少需要编译成不同的目标代码。而引入 Java 语言虚拟机后，Java 语言在不同平台上运行时不需要重新编译。Java 语言使用 Java 虚拟机屏蔽了与具体平台相关的信息，使得 Java 语言编译程序只需生成在 Java 虚拟机上运行的目标代码（字节码），就可以在多种平台上不加修改地运行。Java 虚拟机在执行字节码时，把字节码解释成具体平台上的机器指令执行。这就是 Java 的能够“一次编译，到处运行”的原因。
