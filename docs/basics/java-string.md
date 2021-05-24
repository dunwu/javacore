# 深入理解 Java String 类型

> **📦 本文以及示例源码已归档在 [javacore](https://github.com/dunwu/javacore/)**
>
> String 类型可能是 Java 中应用最频繁的引用类型，但它的性能问题却常常被忽略。高效的使用字符串，可以提升系统的整体性能。当然，要做到高效使用字符串，需要深入了解其特性。

<!-- TOC depthFrom:2 depthTo:3 -->

- [1. String 的不可变性](#1-string-的不可变性)
- [2. String 的性能考量](#2-string-的性能考量)
  - [2.1. 字符串拼接](#21-字符串拼接)
  - [2.2. 字符串分割](#22-字符串分割)
  - [2.3. String.intern](#23-stringintern)
- [3. String、StringBuffer、StringBuilder 有什么区别](#3-stringstringbufferstringbuilder-有什么区别)
- [4. 参考资料](#4-参考资料)

<!-- /TOC -->

## 1. String 的不可变性

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

## 2. String 的性能考量

### 2.1. 字符串拼接

**字符串常量的拼接，编译器会将其优化为一个常量字符串**。

【示例】字符串常量拼接

```java
public static void main(String[] args) {
    // 本行代码在 class 文件中，会被编译器直接优化为：
    // String str = "abc";
    String str = "a" + "b" + "c";
    System.out.println("str = " + str);
}
```

**字符串变量的拼接，编译器会优化成 `StringBuilder` 的方式**。

【示例】字符串变量的拼接

```java
public static void main(String[] args) {
    String str = "";
    for(int i=0; i<1000; i++) {
        // 本行代码会被编译器优化为：
        // str = (new StringBuilder(String.valueOf(str))).append(i).toString();
        str = str + i;
    }
}
```

但是，每次循环都会生成一个新的 `StringBuilder` 实例，同样也会降低系统的性能。

字符串拼接的正确方案：

- 如果需要使用**字符串拼接，应该优先考虑 `StringBuilder` 的 `append` 方法替代使用 `+` 号**。
- 如果在并发编程中，`String` 对象的拼接涉及到线程安全，可以使用 `StringBuffer`。但是要注意，由于 `StringBuffer` 是线程安全的，涉及到锁竞争，所以从性能上来说，要比 `StringBuilder` 差一些。

### 2.2. 字符串分割

**`String` 的 `split()` 方法使用正则表达式实现其强大的分割功能**。而正则表达式的性能是非常不稳定的，使用不恰当会引起回溯问题，很可能导致 CPU 居高不下。

所以，应该慎重使用 `split()` 方法，**可以考虑用 `String.indexOf()` 方法代替 `split()` 方法完成字符串的分割**。如果实在无法满足需求，你就在使用 Split() 方法时，对回溯问题加以重视就可以了。

### 2.3. String.intern

**在每次赋值的时候使用 `String` 的 `intern` 方法，如果常量池中有相同值，就会重复使用该对象，返回对象引用，这样一开始的对象就可以被回收掉**。

在字符串常量中，默认会将对象放入常量池；在字符串变量中，对象是会创建在堆内存中，同时也会在常量池中创建一个字符串对象，复制到堆内存对象中，并返回堆内存对象引用。

如果调用 `intern` 方法，会去查看字符串常量池中是否有等于该对象的字符串，如果没有，就在常量池中新增该对象，并返回该对象引用；如果有，就返回常量池中的字符串引用。堆内存中原有的对象由于没有引用指向它，将会通过垃圾回收器回收。

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

> 使用 `intern` 方法需要注意：一定要结合实际场景。因为常量池的实现是类似于一个 HashTable 的实现方式，HashTable 存储的数据越大，遍历的时间复杂度就会增加。如果数据过大，会增加整个字符串常量池的负担。

## 3. String、StringBuffer、StringBuilder 有什么区别

`String` 是 Java 语言非常基础和重要的类，提供了构造和管理字符串的各种基本逻辑。它是典型的 `Immutable` 类，被声明成为 `final class`，所有属性也都是 `final` 的。也由于它的不可变性，类似拼接、裁剪字符串等动作，都会产生新的 `String` 对象。由于字符串操作的普遍性，所以相关操作的效率往往对应用性能有明显影响。

`StringBuffer` 是为解决上面提到拼接产生太多中间对象的问题而提供的一个类，我们可以用 `append` 或者 `add` 方法，把字符串添加到已有序列的末尾或者指定位置。`StringBuffer` 是一个**线程安全的**可修改字符序列。`StringBuffer` 的线程安全是通过在各种修改数据的方法上用 `synchronized` 关键字修饰实现的。

`StringBuilder` 是 Java 1.5 中新增的，在能力上和 StringBuffer 没有本质区别，但是它去掉了线程安全的部分，有效减小了开销，是绝大部分情况下进行字符串拼接的首选。

`StringBuffer` 和 `StringBuilder` 底层都是利用可修改的（char，JDK 9 以后是 byte）数组，二者都继承了 `AbstractStringBuilder`，里面包含了基本操作，区别仅在于最终的方法是否加了 `synchronized`。构建时初始字符串长度加 16（这意味着，如果没有构建对象时输入最初的字符串，那么初始值就是 16）。我们如果确定拼接会发生非常多次，而且大概是可预计的，那么就可以指定合适的大小，避免很多次扩容的开销。扩容会产生多重开销，因为要抛弃原有数组，创建新的（可以简单认为是倍数）数组，还要进行 `arraycopy`。

**除非有线程安全的需要，不然一般都使用 `StringBuilder`**。

## 4. 参考资料

- [《Java 编程思想（Thinking in java）》](https://book.douban.com/subject/2130190/)
- [《Java 核心技术 卷 I 基础知识》](https://book.douban.com/subject/26880667/)
- [《Java 性能调优实战》](https://time.geekbang.org/column/intro/100028001)
- [《Java 核心技术面试精讲》](https://time.geekbang.org/column/intro/82)
- [Java 基本数据类型和引用类型](https://juejin.im/post/59cd71835188255d3448faf6)
- [深入剖析 Java 中的装箱和拆箱](https://www.cnblogs.com/dolphin0520/p/3780005.html)
