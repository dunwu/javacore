---
title: Java 原子操作类
date: 2018/05/18
categories:
- javase
tags:
- javase
- concurrent
- juc
---

# Java 原子操作类

> 本文内容基于 JDK1.8。

<!-- TOC depthFrom:2 depthTo:4 -->

- [原子更新基本类型](#原子更新基本类型)
- [原子更新数组](#原子更新数组)
- [原子更新引用类型](#原子更新引用类型)
- [原子更新字段类](#原子更新字段类)
- [资料](#资料)

<!-- /TOC -->

## 原子更新基本类型

* AtomicBoolean - 原子更新布尔类型。
* AtomicInteger - 原子更新整型。
* AtomicLong - 原子更新长整型。

## 原子更新数组

* AtomicIntegerArray - 原子更新整型数组里的元素。
* AtomicLongArray - 原子更新长整型数组里的元素。
* AtomicReferenceArray - 原子更新引用类型数组的元素。
* AtomicBooleanArray - 原子更新布尔类型数组的元素。

## 原子更新引用类型

* AtomicReference - 原子更新引用类型。
* AtomicReferenceFieldUpdater - 原子更新引用类型里的字段。
* AtomicMarkableReference - 原子更新带有标记位的引用类型。可以原子更新一个布尔类型的标记位和应用类型。

## 原子更新字段类

* AtomicIntegerFieldUpdater - 原子更新整型的字段的更新器。
* AtomicLongFieldUpdater - 原子更新长整型字段的更新器。
* AtomicStampedReference - 原子更新带有版本号的引用类型。该类将整型数值与引用关联起来，可用于原子的更新数据和数据的版本号，可以解决使用 CAS 进行原子更新时可能出现的 ABA 问题。

## 资料

* [Java 并发编程实战](https://item.jd.com/10922250.html)
* [Java 并发编程的艺术](https://item.jd.com/11740734.html)