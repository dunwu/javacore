# 运行时数据区域

> :notebook: 本文已归档到：「[javacore](https://github.com/dunwu/javacore)」

<!-- TOC depthFrom:2 depthTo:3 -->

- [程序计数器](#程序计数器)
- [虚拟机栈](#虚拟机栈)
- [本地方法栈](#本地方法栈)
- [堆](#堆)
- [方法区](#方法区)
- [运行时常量池](#运行时常量池)
- [直接内存](#直接内存)
- [参考资料](#参考资料)

<!-- /TOC -->


<div align="center">
<img src="https://gitee.com/turnon/images/raw/master/images/java/jvm/jmm-运行时数据区域.png" width="450"/>
</div>

## 程序计数器

记录正在执行的虚拟机字节码指令的地址（如果正在执行的是本地方法则为空）。

## 虚拟机栈

每个 Java 方法在执行的同时会创建一个栈帧用于存储局部变量表、操作数栈、常量池引用等信息。每一个方法从调用直至执行完成的过程，就对应着一个栈帧在 Java 虚拟机栈中入栈和出栈的过程。

<div align="center">
<img src="https://gitee.com/turnon/images/raw/master/images/java/jvm/jmm-虚拟机栈.png" />
</div>

可以通过 -Xss 这个虚拟机参数来指定一个程序的 Java 虚拟机栈内存大小：

```java
java -Xss=512M HackTheJava
```

该区域可能抛出以下异常：

- 当线程请求的栈深度超过最大值，会抛出 StackOverflowError 异常；
- 栈进行动态扩展时如果无法申请到足够内存，会抛出 OutOfMemoryError 异常。

## 本地方法栈

本地方法不是用 Java 实现，对待这些方法需要特别处理。

与 Java 虚拟机栈类似，它们之间的区别只不过是本地方法栈为本地方法服务。

<div align="center">
<img src="https://gitee.com/turnon/images/raw/master/images/java/jvm/jmm-本地方法栈.gif" width="350" />
</div>

## 堆

所有对象实例都在这里分配内存。

是垃圾收集的主要区域（"GC 堆"），现代的垃圾收集器基本都是采用分代收集算法，该算法的思想是针对不同的对象采取不同的垃圾回收算法，因此虚拟机把 Java 堆分成以下三块：

- 新生代（Young Generation）
- 老年代（Old Generation）
- 永久代（Permanent Generation）

当一个对象被创建时，它首先进入新生代，之后有可能被转移到老年代中。新生代存放着大量的生命很短的对象，因此新生代在三个区域中垃圾回收的频率最高。为了更高效地进行垃圾回收，把新生代继续划分成以下三个空间：

- Eden
- From Survivor
- To Survivor

<div align="center">
<img src="https://gitee.com/turnon/images/raw/master/images/java/jvm/jmm-堆.gif" />
</div>

Java 堆不需要连续内存，并且可以动态增加其内存，增加失败会抛出 OutOfMemoryError 异常。

可以通过 -Xms 和 -Xmx 两个虚拟机参数来指定一个程序的 Java 堆内存大小，第一个参数设置初始值，第二个参数设置最大值。

```java
java -Xms=1M -Xmx=2M HackTheJava
```

## 方法区

用于存放已被加载的类信息、常量、静态变量、即时编译器编译后的代码等数据。

和 Java 堆一样不需要连续的内存，并且可以动态扩展，动态扩展失败一样会抛出 OutOfMemoryError 异常。

对这块区域进行垃圾回收的主要目标是对常量池的回收和对类的卸载，但是一般比较难实现。

JDK 1.7 之前，HotSpot 虚拟机把它当成永久代来进行垃圾回收，JDK 1.8 之后，取消了永久代，用 metaspace（元数据）区替代。

## 运行时常量池

运行时常量池是方法区的一部分。

Class 文件中的常量池（编译器生成的各种字面量和符号引用）会在类加载后被放入这个区域。

除了在编译期生成的常量，还允许动态生成，例如 String 类的 intern()。这部分常量也会被放入运行时常量池。

## 直接内存

在 JDK 1.4 中新加入了 NIO 类，它可以使用 Native 函数库直接分配堆外内存，然后通过一个存储在 Java 堆里的 DirectByteBuffer 对象作为这块内存的引用进行操作。这样能在一些场景中显著提高性能，因为避免了在 Java 堆和 Native 堆中来回复制数据。

## 内存问题

如果线程请求的栈深度大于虚拟机所允许的最大深度，将抛出 StackOverflowError 异常。

如果虚拟机在扩展栈时无法申请到足够的内存空间，则抛出 OutOfMemoryError 异常。

## 参考资料

- [深入理解 Java 虚拟机：JVM 高级特性与最佳实践（第 2 版）](https://item.jd.com/11252778.html)
- [从表到里学习 JVM 实现](https://www.douban.com/doulist/2545443/)
