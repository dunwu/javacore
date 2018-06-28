---
title: Java 容器初探
date: 2018/06/07
categories:
- javase
tags:
- javase
- container
---

# Java 容器初探

> 初学容器，我认为应该先了解这些要点：
>
> - 什么是 Java 容器？
> - Java 容器可以做什么？
> - Java 容器框架
> - Java 常用容器类的特性

## 简介

### 什么是 Java 容器？

### Java 容器可以做什么？

**Java 容器技术是 Java 语言对于数据结构的支持**。

使用数据结构可以更有效地存储、组织数据。

相比于数组，容器提供了更加丰富的功能。

## Java 容器框架

<div align="center">
<img src="https://raw.githubusercontent.com/dunwu/javase-notes/master/images/container/collection-structure.png" width="640"/>
</div>

Java 容器框架主要分为 Collection 和 Map 两种。其中，Collection 又分为 List、Set 以及 Queue。

- Collection：一个独立元素的序列，这些元素都服从一条或者多条规则。
  - List：必须按照插入的顺序保存元素。
  - Set：不能有重复的元素。
  - Queue：按照排队规则来确定对象产生的顺序（通常与它们被插入的顺序相同）。
- Map：一组成对的“键值对”对象，允许你使用键来查找值。

## 资料

- [Java 编程思想（第 4 版）](https://item.jd.com/10058164.html)
- https://www.jianshu.com/p/589d58033841
- https://www.jianshu.com/p/9081017a2d67
