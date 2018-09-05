---
title: Java 基本数据类型
date: 2017/11/14
categories:
  - javacore
tags:
  - javacore
  - basics
---

> :pushpin: **关键词：** `byte`、`short`、`int`、`long`、`float`、`double`、`char`、`boolean`、包装类

# Java 基本数据类型

目录
---

<!-- TOC depthFrom:2 depthTo:3 -->

- [数据类型分类](#数据类型分类)
    - [值类型和引用类型的区别](#值类型和引用类型的区别)
    - [值类型](#值类型)
- [数据转换](#数据转换)
    - [自动转换](#自动转换)
    - [强制转换](#强制转换)
- [装箱和拆箱](#装箱和拆箱)
    - [包装类](#包装类)
    - [装箱](#装箱)
    - [拆箱](#拆箱)
    - [使用装箱的场景](#使用装箱的场景)
    - [自动装箱、自动拆箱](#自动装箱自动拆箱)

<!-- /TOC -->

![数据类型.png](https://upload-images.jianshu.io/upload_images/3101171-58d48d5d2e4a1e92.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 数据类型分类

Java 中的数据类型有两类：

- 值类型（又叫内置数据类型，基本数据类型）
- 引用类型

### 值类型和引用类型的区别

- 从概念方面来说
  - 基本类型：变量名指向具体的数值。
  - 引用类型：变量名指向存数据对象的内存地址。
- 从内存构建方面来说
  - 基本类型：变量在声明之后，Java 就会立刻分配给他内存空间。
  - 引用类型：它以特殊的方式（类似 C 指针）向对象实体（具体的值），这类变量声明时不会分配内存，只是存储了一个内存地址。
- 从使用方面来说
  - 基本类型：使用时需要赋具体值,判断时使用 `==` 号。
  - 引用类型：使用时可以赋 null，判断时使用 `equals` 方法。

### 值类型

​Java 的每种基本类型所占存储空间的大小是固定的。它们的大小不像其他大多数语言那样随机器硬件架构的变化而变化。这种不变性是 Java 程序相对其他大多数语言而言，更容易移植的原因之一。

所有数值类型都有正负号，所以不要去寻找无符号的数值类型。

与绝大多数编程语言类似，Java 也支持数值型、字符型、布尔型数据。

Java 语言提供了 **8** 种基本类型，大致分为 **4** 类

- **整数型**
  - `byte` - 8 位，最大存储数据量是 255，存放的数据范围是 -128 ~ 127 之间。
  - `short` - 16 位，最大数据存储量是 65536，数据范围是 -32768 ~ 32767 之间。
  - `int` - 32 位，最大数据存储容量是 2 的 32 次方减 1，数据范围是负的 2 的 31 次方到正的 2 的 31 次方减 1。
  - `long` - 64 位，最大数据存储容量是 2 的 64 次方减 1，数据范围为负的 2 的 63 次方到正的 2 的 63 次方减 1。
- **浮点型**
  - `float` - 32 位，数据范围在 3.4e-45 ~ 1.4e38，直接赋值时必须在数字后加上 f 或 F。
  - `double` - 64 位，数据范围在 4.9e-324 ~ 1.8e308，赋值时可以加 d 或 D 也可以不加。
- **字符型**
  - `char` - 16 位，存储 Unicode 码，用单引号赋值。
- **布尔型**
  - `boolean` - 只有 true 和 false 两个取值。

对于数值类型的基本类型的取值范围，我们无需强制去记忆，因为它们的值都已经以常量的形式定义在对应的包装类中了。

示例：

```java
public class DataTypeScopeDemo {

    public static void main(String[] args) {
        // byte
        System.out.println("基本类型：byte 二进制位数：" + Byte.SIZE);
        System.out.println("包装类：java.lang.Byte");
        System.out.println("最小值：Byte.MIN_VALUE=" + Byte.MIN_VALUE);
        System.out.println("最大值：Byte.MAX_VALUE=" + Byte.MAX_VALUE);
        System.out.println();
    }
};
```

输出：

```
基本类型：byte 二进制位数：8
包装类：java.lang.Byte
最小值：Byte.MIN_VALUE=-128
最大值：Byte.MAX_VALUE=127
```

## 数据转换

### 自动转换

一般情况下，定义了某数据类型的变量，就不能再随意转换。但是 JAVA 允许用户对基本类型做**有限度**的类型转换。

如果符合以下条件，则 JAVA 将会自动做类型转换：

- 由“小”数据转换为“大”数据

  显而易见的是，“小”数据类型的数值表示范围小于“大”数据类型的数值表示范围，即精度小于“大”数据类型。

  所以，如果“大”数据向“小”数据转换，会丢失数据精度。比如：long 转为 int，则超出 int 表示范围的数据将会丢失，导致结果的不确定性。

  反之，“小”数据向“大”数据转换，则不会存在数据丢失情况。由于这个原因，这种类型转换也称为**扩大转换**。

  这些类型由“小”到“大”分别为：(byte，short，char) < int < long < float < double。

  这里我们所说的“大”与“小”，并不是指占用字节的多少，而是指表示值的范围的大小。

- 转换前后的数据类型要兼容

  由于 boolean 类型只能存放 true 或 false，这与整数或字符是不兼容的，因此不可以做类型转换。

- 整型类型和浮点型进行计算后，结果会转为浮点类型

示例：

```java
long x = 30;
float y = 14.3f;
System.out.println("x/y = " + x/y);
```

输出：

```
x/y = 1.9607843
```

可见 long 虽然精度大于 float 类型，但是结果为浮点数类型。

### 强制转换

在不符合自动转换条件时或者根据用户的需要，可以对数据类型做强制的转换。

转换方式为：在数值的前面用一个括号 `()` 把要强制转换的类型标注出来。

示例：

```java
float f = 25.5f;
int x = (int)f;
System.out.println("x = " + x);
```

## 装箱和拆箱

### 包装类

Java 中基本类型的包装类如下：

- Byte <-> byte
- Short <-> short
- Integer <-> int
- Long <-> long
- Float <-> float
- Double <-> double
- Character <-> char
- Boolean <-> boolean

### 装箱

装箱是将值类型转换为引用类型。

例：

```java
Integer i1 = new Integer(10); // 非自动装箱
Integer i2 = 10; // 自动装箱
System.out.println("i1 = " + i1);
System.out.println("i2 = " + i2);
```

### 拆箱

拆箱是将引用类型转换为值类型。

例：

```java
int i1 = new Integer(10); // 自动拆箱
Integer tmp = new Integer(20);
int i2 = tmp.intValue(); // 非自动拆箱
System.out.println("i1 = " + i1);
System.out.println("i2 = " + i2);
```

### 使用装箱的场景

一种最普通的场景是：调用一个含类型为 Object 参数的方法，该 Object 可支持任意类型（因为 Object 是所有类的父类），以便通用。当你需要将一个值类型（如 Integer）传入时，需要装箱。

另一种用法是，一个非泛型的容器，同样是为了保证通用，而将元素类型定义为 Object。于是，要将值类型数据加入容器时，需要装箱。

### 自动装箱、自动拆箱

基本数据（Primitive）型的自动装箱（boxing）拆箱（unboxing）自 JDK 5 开始提供的功能。

JDK 5 之前的形式：

```java
Integer i1 = new Integer(10); // 非自动装箱
```

JDK 5 之后：

```java
Integer i2 = 10; // 自动装箱
```

Java 对于自动装箱和拆箱的设计，依赖于一种叫做享元模式的设计模式（有兴趣的朋友可以去了解一下源码，这里不对设计模式展开详述）。
