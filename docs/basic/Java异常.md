---
title: Java 异常
date: 2015/02/15
categories:
- javacore
tags:
- javacore
- basics
---

# Java 异常

## 知识点

## Throwable

### Throwable 家族

在 Java 中，根据错误性质将运行错误分为两类：错误（Error）和异常（Exception）。

* `Throwable` 是 Java 中所有错误或异常的祖先（超类）。
* `Error` 是 `Throwable` 的一个子类。表示合理的应用程序不应该尝试捕获的严重问题。大多数此类错误都是异常情况。
* `Exception` 是 `Throwable` 的一个子类。表示合理的应用程序可能想要捕获的条件。

![image.png](https://upload-images.jianshu.io/upload_images/3101171-5783f3592a2327f0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 主要方法

* fillInStackTrace：用当前的调用栈层次填充 Throwable 对象栈层次，添加到栈层次任何先前信息中。
* getMessage：返回关于发生的异常的详细信息。这个消息在 Throwable 类的构造函数中初始化了。
* getCause：返回一个 Throwable 对象代表异常原因。
* getStackTrace：返回一个包含堆栈层次的数组。下标为 0 的元素代表栈顶，最后一个元素代表方法调用堆栈的栈底。
* printStackTrace：打印 toString()结果和栈层次到 System.err，即错误输出流。
* toString：使用 getMessage 的结果返回类的串级名字。
