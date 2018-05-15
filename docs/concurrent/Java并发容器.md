---
title: Java并发容器
date: 2018/05/15
categories:
- javase
tags:
- javase
- concurrent
- juc
---

# Java 并发容器

> 本文内容基于 JDK1.8。

<!-- TOC depthFrom:2 depthTo:3 -->

- [概述](#概述)
- [ConcurrentHashMap](#concurrenthashmap)
- [CopyOnWriteArrayList](#copyonwritearraylist)
- [资料](#资料)

<!-- /TOC -->

## 概述

JDK 的 `java.util.concurrent` 包（即 juc）中提供了几个非常有用的并发容器。

| 类名                  | 类型  | 功能                                                                                                |
| --------------------- | ----- | --------------------------------------------------------------------------------------------------- |
| CopyOnWriteArrayList  | List  | 线程安全的 ArrayList                                                                                |
| CopyOnWriteArraySet   | Set   | 线程安全的 Set，它内部包含了一个 CopyOnWriteArrayList，因此本质上是由 CopyOnWriteArrayList 实现的。 |
| ConcurrentSkipListSet | Set   | 相当于线程安全的 TreeSet。它是有序的 Set。它由 ConcurrentSkipListMap 实现。                         |
| ConcurrentHashMap     | Map   | 线程安全的 HashMap。采用分段锁实现高效并发。                                                        |
| ConcurrentSkipListMap | Map   | 线程安全的有序 Map。使用跳表实现高效并发。                                                          |
| ConcurrentLinkedQueue | Queue | 线程安全的无界队列。底层采用单链表。支持 FIFO。                                                     |
| ConcurrentLinkedDeque | Queue | 线程安全的无界双端队列。底层采用双向链表。支持 FIFO 和 FILO。                                       |
| ArrayBlockingQueue    | Queue | 数组实现的阻塞队列。                                                                                |
| LinkedBlockingQueue   | Queue | 链表实现的阻塞队列。                                                                                |
| LinkedBlockingDeque   | Queue | 双向链表实现的双端阻塞队列。                                                                        |

## ConcurrentHashMap

* 作用：ConcurrentHashMap 是线程安全的 HashMap。
* 原理：ConcurrentHashMap 采用了分段锁机制实现高效的并发访问。

## CopyOnWriteArrayList

* 作用：CopyOnWrite 字面意思为写入时复制。CopyOnWriteArrayList 是线程安全的 ArrayList。
* 原理：写入时复制（CopyOnWrite）容器的迭代器保留一个指向底层基础数组的引用，这个数组当前位于迭代器的起始位置，由于它不会被修改，因此在对其进行同步时只需确保数组内容的可见性。

## 资料

* [Java 并发编程实战](https://item.jd.com/10922250.html)
* [Java 并发编程的艺术](https://item.jd.com/11740734.html)
* https://blog.csdn.net/u010425776/article/details/54890215
