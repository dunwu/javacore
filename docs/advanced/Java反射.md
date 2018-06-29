---
title: Java 反射
date: 2018/06/05
categories:
- javase
tags:
- java
- javase
- advanced
---

# Java 反射

<!-- TOC -->

- [Java 反射](#java-反射)
    - [简介](#简介)
        - [什么是反射](#什么是反射)
        - [反射的主要用途](#反射的主要用途)
        - [反射的缺点](#反射的缺点)
    - [Class](#class)
        - [获得 Class 对象](#获得-class-对象)
        - [判断是否为某个类的实例](#判断是否为某个类的实例)
        - [创建实例](#创建实例)
    - [Field](#field)
    - [Method](#method)
    - [Constructor](#constructor)
    - [Array](#array)
    - [推荐阅读](#推荐阅读)
    - [参考资料](#参考资料)

<!-- /TOC -->

## 简介

### 什么是反射

反射(Reflection)是 Java 程序开发语言的特征之一，它允许运行中的 Java 程序获取自身的信息，并且可以操作类或对象的内部属性。

简而言之，通过反射，我们可以在运行时获得程序或程序集中每一个类型的成员和成员的信息。

反射通常被用于需要在 JVM 中检查或修改应用程序的运行时行为的程序。

### 反射的主要用途

* 开发通用框架

反射最重要的用途就是开发各种通用框架。很多框架（比如 Spring）都是配置化的（比如通过 XML 文件配置 JavaBean、Filter 等），为了保证框架的通用性，它们可能需要根据配置文件加载不同的对象或类，调用不同的方法，这个时候就必须用到反射——运行时动态加载需要加载的对象。

* 可扩展性功能

应用程序可以通过使用完全限定名称创建可扩展性对象实例来使用外部的用户定义类。

* 类浏览器和可视化开发环境

类浏览器需要能够枚举类的成员。可视化开发环境可以利用反射中可用的类型信息来帮助开发人员编写正确的代码。

* 调试器和测试工具

调试器需要能够检查类上的私有成员。测试工具可以利用反射来系统地调用在类中定义的可发现集 API，以确保测试套件中的高级代码覆盖率。

### 反射的缺点

* 性能开销

由于反射涉及动态解析的类型，因此无法执行某些 Java 虚拟机优化。因此，反射操作的性能要比非反射操作的性能要差，应该在性能敏感的应用程序中频繁调用的代码段中避免。

* 安全限制

反射需要在安全管理器下运行时可能不存在的运行时权限。对于必须在受限制的安全上下文中运行的代码（如 Applet 中），这是一个重要的考虑因素。

* 内部曝光

由于反射允许代码执行在非反射代码中非法的操作，例如访问私有字段和方法，所以反射的使用可能会导致意想不到的副作用，这可能会导致代码功能失常并可能破坏可移植性。反射代码打破了抽象，因此可能会随着平台的升级而改变行为。

## Class

对于每种类型的对象，JVM 实例化一个 `java.lang.Class` 的不可变实例，该实例提供了检查对象的运行时属性（包括其成员和类型信息）的方法。类还提供了创建新类和对象的功能。

最重要的是，**`java.lang.Class` 是所有反射 API 的入口点**。

java.lang.reflect 包中的类都没有 public 构造方法。

### 获得 Class 对象

获得 Class 的三种方法：

（1）**使用 Class 类的 `forName` 静态方法**，示例：

```java
Class c1 = Class.forName("io.github.dunwu.javase.reflect.ReflectClassTest");
Class c2 = Class.forName("[D");
Class c3 = Class.forName("[[Ljava.lang.String;");
```

使用类的完全限定名来反射对象的类。常见的应用场景为：在 JDBC 开发中常用此方法加载数据库驱动。

（2）**直接获取某一个对象的 `class`**，示例：

```java
Class c1 = boolean.class;
Class c2 = java.io.PrintStream.class;
Class c3 = int[][][].class;
```

（3）**调用某个对象的 `getClass()` 方法**，示例：

```java
Class c = "foo".getClass();

enum E {A, B}
Class c2 = E.A.getClass();

byte[] bytes = new byte[1024];
Class c3 = bytes.getClass();

Set<String> set = new HashSet<>();
Class c4 = set.getClass();
```

### 判断是否为某个类的实例

一般地，我们用 instanceof 关键字来判断是否为某个类的实例。同时我们也可以借助反射中 Class 对象的 isInstance()方法来判断是否为某个类的实例，它是一个 Native 方法：

```java
ArrayList arrayList = new ArrayList();
if (arrayList instanceof List) {
    System.out.println("ArrayList is List");
}
if (List.class.isInstance(arrayList)) {
    System.out.println("ArrayList is List");
}
```

### 创建实例

通过反射来生成对象主要有两种方式。

（1）使用 Class 对象的 newInstance() 方法来创建 Class 对象对应类的实例。

```java
Class<?> c1 = String.class;
Object str1 = c1.newInstance();
System.out.println(str1.getClass().getCanonicalName());
```

（2）先通过 Class 对象获取指定的 Constructor 对象，再调用 Constructor 对象的 newInstance() 方法来创建实例。这种方法可以用指定的构造器构造类的实例。

```java
//获取String所对应的Class对象
Class<?> c2 = String.class;
//获取String类带一个String参数的构造器
Constructor constructor = c2.getConstructor(String.class);
//根据构造器创建实例
Object obj = constructor.newInstance("bbb");
System.out.println(obj.getClass().getCanonicalName());
```

## Field

获取某个 Class 对象的成员变量的方法有：

* getFiled: 访问公有的成员变量
* getDeclaredField：所有已声明的成员变量。但不能得到其父类的成员变量

示例如下：

```java
class FieldSpy<T> {
    public boolean[][] b = {{ false, false }, { true, true } };
    public String name  = "Alice";
    public List<Integer> list;
    public T val;
}

Field f1 = FieldSpy.class.getField("b");
Field f2 = FieldSpy.class.getField("name");
Field f3 = FieldSpy.class.getField("list");
Field f4 = FieldSpy.class.getField("val");
```

输出：

```
Type: class [[Z
Type: class java.lang.String
Type: interface java.util.List
Type: class java.lang.Object
```

## Method

获取某个 Class 对象的方法集合的方法有：

（1）getDeclaredMethods()方法返回类或接口声明的所有方法，包括公共、保护、默认（包）访问和私有方法，但不包括继承的方法。

```java
Method[] methods1 = Thread.class.getDeclaredMethods();
System.out.println("Thread getDeclaredMethods 清单（数量 = " + methods1.length + "）：");
for (Method m : methods1) {
    System.out.println(m);
}
```

（2）getMethods() 方法返回某个类的所有公用（public）方法，包括其继承类的公用方法。

```java
Method[] methods2 = Thread.class.getMethods();
System.out.println("Thread getMethods 清单（数量 = " + methods2.length + "）：");
for (Method m : methods2) {
    System.out.println(m);
}
```

（3）getMethod() 方法返回一个特定的方法，其中第一个参数为方法名称，后面的参数为方法的参数对应 Class 的对象。

```java
Method method = Thread.class.getMethod("join", long.class, int.class);
System.out.println(method);
```

## Constructor

获取某个 Class 对象的构造方法集合的方法有：

（1）getDeclaredConstructor()方法返回类的所有构造方法，包括公共、保护、默认（包）访问和私有方法。

```java
Method[] methods1 = Thread.class.getDeclaredMethods();
System.out.println("Thread getDeclaredMethods 清单（数量 = " + methods1.length + "）：");
for (Method m : methods1) {
    System.out.println(m);
}
```

（2）getConstructors() 方法返回某个类的所有公用（public）构造方法。

```java
Constructor<?>[] constructors = FileOutputStream.class.getConstructors();
System.out.println("FileOutputStream 构造方法清单（数量 = " + constructors.length + "）：");
for (Constructor c : constructors) {
    System.out.println(c);
}
```

（3）getConstructor() 方法返回一个特定的方法，参数为方法的参数对应 Class 的对象。

```java
Constructor constructor = FileOutputStream.class.getConstructor(String.class, boolean.class);
System.out.println(constructor);
```

## Array

## 推荐阅读

本文示例代码见：[源码](https://github.com/dunwu/JavaSE/tree/master/codes/advanced/src/main/java/io/github/dunwu/javase)

本文同步维护在：[Java 系列教程](https://github.com/dunwu/JavaSE)

## 参考资料

* [Java 编程思想（Thinking in java）](https://item.jd.com/10058164.html)
* [深入解析 Java 反射（1） - 基础](https://www.sczyh30.com/posts/Java/java-reflection-1/)
* https://docs.oracle.com/javase/tutorial/reflect/index.html
