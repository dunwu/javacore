---
title: Java 数组
date: 2014/10/14
categories:
- javacore
tags:
- javacore
- basics
---

# Java 数组

## 知识点

![数组.png](https://upload-images.jianshu.io/upload_images/3101171-4936f1c528747983.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 概念

### 定义

数组代表一系列对象或者基本数据类型，所有相同的类型都封装到一起——采用一个统一的标识符名称。

数组的定义和使用是通过方括号索引运算符进行的（[]）。

> 在 Java 中，数组类型是一种引用类型。
>
> 在 Java 中，数组是用来存储固定大小的同类型元素。
>
> 数组对于每一门编程语言来说都是重要的数据结构之一，当然不同语言对数组的实现及处理也不尽相同。

几乎所有程序设计语言都支持数组。

## 数组操作

### 声明数组变量

声明数组变量的语法

```java
int[] arr; // 首选的风格
int arr[]; // 效果相同，但不是首选方法
```

### 创建数组

Java 语言使用 `new` 操作符来创建数组。

```java
int[] array = new int[5];
int[] score = { 91, 92, 93, 94, 95, 96 };
```

### 将一个数组分配给另一个

```java
int[] a1 = { 1, 2, 3, 4, 5 };
int[] a2;
a2 = a1;
```

示例：

```java
public class Arrays {

    public static void main(String[] args) {
        int[] a1 = {1, 2, 3, 4, 5};
        int[] a2;
        a2 = a1;
        for (int i = 0; i < a2.length; i++) { a2[i]++; }
        for (int i = 0; i < a1.length; i++) { print("a1[" + i + "] = " + a1[i]); }
    }

    static void print(String s) {
        System.out.println(s);
    }
}
//Output:
//a1[0] = 2
//a1[1] = 3
//a1[2] = 4
//a1[3] = 5
//a1[4] = 6
```

说明：

a1 获得了一个初始值，而 a2 没有；a2 将在以后赋值——这种情况下是赋给另一个数组。

### 普通遍历

```java
public static void main(String[] args) {
    // 使用静态初始化声明数组
    int[] score = {91, 92, 93, 94, 95, 96};
    // 循环输出
    for (int x = 0; x < score.length; x++) {
        System.out.println("score[" + x + "] = " + score[x]);
    }
}
```

### foreach 遍历

```java
public class ArrayForeach {

    public static void main(String[] args) {
        // 使用静态初始化声明数组
        int[] score = {91, 92, 93, 94, 95, 96};
        // 循环输出
        for (int item : score) {
            System.out.println(item);
        }
    }
}
```

## 多维数组

多维数组可以看成是数组的数组，比如二维数组就是一个特殊的一维数组，其每一个元素都是一个一维数组。

```java
public class MultiDimWrapperArray {

    public static void main(String[] args) {
        Integer[][] a1 = { // 自动装箱
            {1, 2, 3,},
            {4, 5, 6,},
        };
        Double[][][] a2 = { // 自动装箱
            {{1.1, 2.2}, {3.3, 4.4}},
            {{5.5, 6.6}, {7.7, 8.8}},
            {{9.9, 1.2}, {2.3, 3.4}},
        };
        String[][] a3 = {
            {"The", "Quick", "Sly", "Fox"},
            {"Jumped", "Over"},
            {"The", "Lazy", "Brown", "Dog", "and", "friend"},
        };
        System.out.println("a1: " + java.util.Arrays.deepToString(a1));
        System.out.println("a2: " + java.util.Arrays.deepToString(a2));
        System.out.println("a3: " + Arrays.deepToString(a3));
    }
}
```

## Arrays 类

* binarySearch - 查找数组元素
* equals - 比较数组
* fill - 给数组赋值
* sort - 对数组排序
