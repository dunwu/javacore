---
title: Java 数组
date: 2014/10/14
categories:
- javase
tags:
- javase
- basics
---

# Java 数组

## 简介

> 在 Java 中，数组类型是一种引用类型。
>
> 在 Java 中，数组是用来存储固定大小的同类型元素。
>
> 数组对于每一门编程语言来说都是重要的数据结构之一，当然不同语言对数组的实现及处理也不尽相同。

## 声明数组变量

声明数组变量的语法

```
int[] arr; // 首选的风格
int arr[]; // 效果相同，但不是首选方法
```

## 创建数组

Java语言使用 `new` 操作符来创建数组，语法如下：

```
arrayRefVar = new dataType[arraySize];
```

上面的语法语句做了两件事：

- 一、使用dataType[arraySize]创建了一个数组。
- 二、把新创建的数组的引用赋值给变量 arrayRefVar。

声明数组变量和创建数组可以用一条语句完成，如下所示：

```
dataType[] arrayRefVar = new dataType[arraySize];
```

另外，你还可以使用如下的方式创建数组。

```
dataType[] arrayRefVar = {value0, value1, ..., valuek};
```

数组的元素是通过索引访问的。数组索引从0开始，所以索引值从0到arrayRefVar.length-1。

下面的语句首先声明了一个数组变量myList，接着创建了一个包含10个double类型元素的数组，并且把它的引用赋值给myList变量。

```
double[] myList = new double[10];
```

下面的图片描绘了数组myList。这里myList数组里有10个double元素，它的下标从0到9。

## 数组操作

## 遍历数组

## 多维数组

## Arrays 类