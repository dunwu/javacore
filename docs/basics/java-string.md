# 深入理解 Java String 类型

> **📦 本文以及示例源码已归档在 [javacore](https://github.com/dunwu/javacore/)**
>
> String 类型可能是 Java 中应用最频繁的引用类型，但它的性能问题却常常被忽略。高效的使用字符串，可以提升系统的整体性能。当然，要做到高效使用字符串，需要深入了解其特性。

思考题：结果是什么？

```
String str1= "abc";
String str2= new String("abc");
String str3= str2.intern();
assertSame(str1==str2);
assertSame(str2==str3);
assertSame(str1==str3)
```

<!-- TOC depthFrom:2 depthTo:3 -->

- [String 的不可变性](#string-的不可变性)
- [String 的优化](#string-的优化)
  - [字符串拼接](#字符串拼接)
  - [如何使用 String.intern 节省内存](#如何使用-stringintern-节省内存)
- [参考资料](#参考资料)

<!-- /TOC -->

## 一、String 的不可变性

我们先来看下 `String` 的定义：

```java
public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {
    /** The value is used for character storage. */
    private final char value[];
```

`String` 类被 `final` 关键字修饰，表示**不可继承 `String` 类**。

`String` 类的数据存储于 `char[]` 数组，这个数组被 `final` 关键字修饰，表示 **`String` 对象不可被更改**。

为什么 Java 要这样设计？

（1）**保证 String 对象安全性**。避免 String 被篡改。

（2）**保证 hash 值不会频繁变更**。

（3）**可以实现字符串常量池**。通常有两种创建字符串对象的方式，一种是通过字符串常量的方式创建，如 `String str="abc";` 另一种是字符串变量通过 new 形式的创建，如 `String str = new String("abc")`。

使用第一种方式创建字符串对象时，JVM 首先会检查该对象是否在字符串常量池中，如果在，就返回该对象引用，否则新的字符串将在常量池中被创建。这种方式可以减少同一个值的字符串对象的重复创建，节约内存。

`String str = new String("abc")` 这种方式，首先在编译类文件时，`"abc"` 常量字符串将会放入到常量结构中，在类加载时，`"abc"` 将会在常量池中创建；其次，在调用 new 时，JVM 命令将会调用 `String` 的构造函数，同时引用常量池中的 `"abc"` 字符串，在堆内存中创建一个 `String` 对象；最后，str 将引用 `String` 对象。

## 二、String 的优化

### 字符串拼接

如果需要使用**字符串拼接，应该优先考虑 `StringBuilder` 或 `StringBuffer`（线程安全） 的 `append` 方法替代使用 `+` 号**。

【示例】错误示例

```
String str= "ab" + "cd" + "ef";
```

程序会先生成 ab 对象，再生成 abcd 对象，最后生成 abcdef 对象。

即使使用 `+` 号作为字符串的拼接，也一样可以被编译器优化成 `StringBuilder` 的方式。但再细致些，你会发现在编译器优化的代码中，每次循环都会生成一个新的 `StringBuilder` 实例，同样也会降低系统的性能。

### 如何使用 String.intern 节省内存

在每次赋值的时候使用 `String` 的 `intern` 方法，如果常量池中有相同值，就会重复使用该对象，返回对象引用，这样一开始的对象就可以被回收掉。

在字符串常量中，默认会将对象放入常量池；在字符串变量中，对象是会创建在堆内存中，同时也会在常量池中创建一个字符串对象，复制到堆内存对象中，并返回堆内存对象引用。

如果调用 intern 方法，会去查看字符串常量池中是否有等于该对象的字符串，如果没有，就在常量池中新增该对象，并返回该对象引用；如果有，就返回常量池中的字符串引用。堆内存中原有的对象由于没有引用指向它，将会通过垃圾回收器回收。

【示例】

```java
public class SharedLocation {

	private String city;
	private String region;
	private String countryCode;
}

SharedLocation sharedLocation = new SharedLocation();
sharedLocation.setCity(messageInfo.getCity().intern());		sharedLocation.setCountryCode(messageInfo.getRegion().intern());
sharedLocation.setRegion(messageInfo.getCountryCode().intern());
```

虽然使用 new 声明的字符串调用 intern 方法，也可以让字符串进行驻留，但在业务代码中滥用 intern，可能会产生性能问题。

## 参考资料

- [《Java 编程思想（Thinking in java）》](https://item.jd.com/10058164.html)
- [《Java 核心技术 卷 I 基础知识》](https://item.jd.com/12759308.html)
- [Java 基本数据类型和引用类型](https://juejin.im/post/59cd71835188255d3448faf6)
- [深入剖析 Java 中的装箱和拆箱](https://www.cnblogs.com/dolphin0520/p/3780005.html)
