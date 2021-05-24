# Java 内存管理

> **📦 本文以及示例源码已归档在 [javacore](https://github.com/dunwu/javacore/)**

<!-- TOC depthFrom:2 depthTo:3 -->

- [1. 内存简介](#1-内存简介)
  - [1.1. 物理内存和虚拟内存](#11-物理内存和虚拟内存)
  - [1.2. 内核空间和用户空间](#12-内核空间和用户空间)
  - [1.3. 使用内存的 Java 组件](#13-使用内存的-java-组件)
- [2. 运行时数据区域](#2-运行时数据区域)
  - [2.1. 程序计数器](#21-程序计数器)
  - [2.2. Java 虚拟机栈](#22-java-虚拟机栈)
  - [2.3. 本地方法栈](#23-本地方法栈)
  - [2.4. Java 堆](#24-java-堆)
  - [2.5. 方法区](#25-方法区)
  - [2.6. 运行时常量池](#26-运行时常量池)
  - [2.7. 直接内存](#27-直接内存)
  - [2.8. Java 内存区域对比](#28-java-内存区域对比)
- [3. JVM 运行原理](#3-jvm-运行原理)
- [4. OutOfMemoryError](#4-outofmemoryerror)
  - [4.1. 什么是 OutOfMemoryError](#41-什么是-outofmemoryerror)
  - [4.2. 堆空间溢出](#42-堆空间溢出)
  - [4.3. GC 开销超过限制](#43-gc-开销超过限制)
  - [4.4. 永久代空间不足](#44-永久代空间不足)
  - [4.5. 元数据区空间不足](#45-元数据区空间不足)
  - [4.6. 无法新建本地线程](#46-无法新建本地线程)
  - [4.7. 直接内存溢出](#47-直接内存溢出)
- [5. StackOverflowError](#5-stackoverflowerror)
- [6. 参考资料](#6-参考资料)

<!-- /TOC -->

## 1. 内存简介

### 1.1. 物理内存和虚拟内存

所谓物理内存就是通常所说的 RAM（随机存储器）。

虚拟内存使得多个进程在同时运行时可以共享物理内存，这里的共享只是空间上共享，在逻辑上彼此仍然是隔离的。

### 1.2. 内核空间和用户空间

一个计算通常有固定大小的内存空间，但是程序并不能使用全部的空间。因为这些空间被划分为内核空间和用户空间，而程序只能使用用户空间的内存。

### 1.3. 使用内存的 Java 组件

Java 启动后，作为一个进程运行在操作系统中。

有哪些 Java 组件需要占用内存呢？

- 堆内存：Java 堆、类和类加载器
- 栈内存：线程
- 本地内存：NIO、JNI

## 2. 运行时数据区域

JVM 在执行 Java 程序的过程中会把它所管理的内存划分为若干个不同的数据区域。这些区域都有各自的用途，以及创建和销毁的时间，有的区域随着虚拟机进程的启动而存在，有些区域则依赖用户线程的启动和结束而建立和销毁。如下图所示：

![img](https://raw.githubusercontent.com/dunwu/images/dev/cs/java/javacore/jvm/jvm-memory-runtime-data-area.png)

### 2.1. 程序计数器

**`程序计数器（Program Counter Register）`** 是一块较小的内存空间，它可以看做是**当前线程所执行的字节码的行号指示器**。例如，分支、循环、跳转、异常、线程恢复等都依赖于计数器。

当执行的线程数量超过 CPU 数量时，线程之间会根据时间片轮询争夺 CPU 资源。如果一个线程的时间片用完了，或者是其它原因导致这个线程的 CPU 资源被提前抢夺，那么这个退出的线程就需要单独的一个程序计数器，来记录下一条运行的指令，从而在线程切换后能恢复到正确的执行位置。各条线程间的计数器互不影响，独立存储，我们称这类内存区域为 “线程私有” 的内存。

- 如果线程正在执行的是一个 Java 方法，这个计数器记录的是正在执行的虚拟机字节码指令的地址；
- 如果正在执行的是 Native 方法，这个计数器值则为空（Undefined）。

> 🔔 注意：此内存区域是唯一一个在 JVM 中没有规定任何 `OutOfMemoryError` 情况的区域。

### 2.2. Java 虚拟机栈

**`Java 虚拟机栈（Java Virtual Machine Stacks）`** 也**是线程私有的，它的生命周期与线程相同**。

每个 Java 方法在执行的同时都会创建一个栈帧（Stack Frame）用于存储 **局部变量表**、**操作数栈**、**常量池引用** 等信息。每一个方法从调用直至执行完成的过程，就对应着一个栈帧在 Java 虚拟机栈中入栈和出栈的过程。

![img](https://raw.githubusercontent.com/dunwu/images/dev/cs/java/javacore/jvm/jvm-stack.png!w640)

- **局部变量表** - 32 位变量槽，存放了编译期可知的各种基本数据类型、对象引用、`ReturnAddress` 类型。
- **操作数栈** - 基于栈的执行引擎，虚拟机把操作数栈作为它的工作区，大多数指令都要从这里弹出数据、执行运算，然后把结果压回操作数栈。
- **动态链接** - 每个栈帧都包含一个指向运行时常量池（方法区的一部分）中该栈帧所属方法的引用。持有这个引用是为了支持方法调用过程中的动态连接。Class 文件的常量池中有大量的符号引用，字节码中的方法调用指令就以常量池中指向方法的符号引用为参数。这些符号引用一部分会在类加载阶段或第一次使用的时候转化为直接引用，这种转化称为静态解析。另一部分将在每一次的运行期间转化为直接应用，这部分称为动态链接。
- **方法出口** - 返回方法被调用的位置，恢复上层方法的局部变量和操作数栈，如果无返回值，则把它压入调用者的操作数栈。

> 🔔 注意：
>
> 该区域可能抛出以下异常：
>
> - 如果线程请求的栈深度超过最大值，就会抛出 `StackOverflowError` 异常；
> - 如果虚拟机栈进行动态扩展时，无法申请到足够内存，就会抛出 `OutOfMemoryError` 异常。
>
> 💡 提示：
>
> 可以通过 `-Xss` 这个虚拟机参数来指定一个程序的 Java 虚拟机栈内存大小：
>
> ```java
> java -Xss=512M HackTheJava
> ```

### 2.3. 本地方法栈

**`本地方法栈（Native Method Stack）`** 与虚拟机栈的作用相似。

二者的区别在于：**虚拟机栈为 Java 方法服务；本地方法栈为 Native 方法服务**。本地方法并不是用 Java 实现的，而是由 C 语言实现的。

![img](https://raw.githubusercontent.com/dunwu/images/dev/cs/java/javacore/jvm/jvm-native-method-stack.gif!w640)

> 🔔 注意：本地方法栈也会抛出 `StackOverflowError` 异常和 `OutOfMemoryError` 异常。

### 2.4. Java 堆

**`Java 堆（Java Heap）` 的作用就是存放对象实例，几乎所有的对象实例都是在这里分配内存**。

Java 堆是垃圾收集的主要区域（因此也被叫做"GC 堆"）。现代的垃圾收集器基本都是采用**分代收集算法**，该算法的思想是针对不同的对象采取不同的垃圾回收算法。

因此虚拟机把 Java 堆分成以下三块：

- **`新生代（Young Generation）`**
  - `Eden` - Eden 和 Survivor 的比例为 8:1
  - `From Survivor`
  - `To Survivor`
- **`老年代（Old Generation）`**
- **`永久代（Permanent Generation）`**

当一个对象被创建时，它首先进入新生代，之后有可能被转移到老年代中。新生代存放着大量的生命很短的对象，因此新生代在三个区域中垃圾回收的频率最高。

![img](https://raw.githubusercontent.com/dunwu/images/dev/cs/java/javacore/jvm/jvm-heap.gif!w640)

> 🔔 注意：Java 堆不需要连续内存，并且可以动态扩展其内存，扩展失败会抛出 `OutOfMemoryError` 异常。
>
> 💡 提示：可以通过 `-Xms` 和 `-Xmx` 两个虚拟机参数来指定一个程序的 Java 堆内存大小，第一个参数设置初始值，第二个参数设置最大值。
>
> ```java
> java -Xms=1M -Xmx=2M HackTheJava
> ```

### 2.5. 方法区

方法区（Method Area）也被称为永久代。**方法区用于存放已被加载的类信息、常量、静态变量、即时编译器编译后的代码等数据**。

对这块区域进行垃圾回收的主要目标是对常量池的回收和对类的卸载，但是一般比较难实现。

> 🔔 注意：和 Java 堆一样不需要连续的内存，并且可以动态扩展，动态扩展失败一样会抛出 `OutOfMemoryError` 异常。
>
> 💡 提示：
>
> - JDK 1.7 之前，HotSpot 虚拟机把它当成永久代来进行垃圾回收。可通过参数 `-XX:PermSize` 和 `-XX:MaxPermSize` 设置。
> - JDK 1.8 之后，取消了永久代，用 **`metaspace（元数据）`**区替代。可通过参数 `-XX:MaxMetaspaceSize` 设置。

### 2.6. 运行时常量池

**`运行时常量池（Runtime Constant Pool）` 是方法区的一部分**，Class 文件中除了有类的版本、字段、方法、接口等描述信息，还有一项信息是常量池（Constant Pool Table），**用于存放编译器生成的各种字面量和符号引用**，这部分内容会在类加载后被放入这个区域。

- **字面量** - 文本字符串、声明为 `final` 的常量值等。
- **符号引用** - 类和接口的完全限定名（Fully Qualified Name）、字段的名称和描述符（Descriptor）、方法的名称和描述符。

除了在编译期生成的常量，还允许动态生成，例如 `String` 类的 `intern()`。这部分常量也会被放入运行时常量池。

> 🔔 注意：当常量池无法再申请到内存时会抛出 `OutOfMemoryError` 异常。

### 2.7. 直接内存

直接内存（Direct Memory）并不是虚拟机运行时数据区的一部分，也不是 JVM 规范中定义的内存区域。

在 JDK 1.4 中新加入了 NIO 类，它可以使用 Native 函数库直接分配堆外内存，然后通过一个存储在 Java 堆里的 `DirectByteBuffer` 对象作为这块内存的引用进行操作。这样能在一些场景中显著提高性能，因为避免了在 Java 堆和 Native 堆中来回复制数据。

> 🔔 注意：直接内存这部分也被频繁的使用，且也可能导致 `OutOfMemoryError` 异常。
>
> 💡 提示：直接内存容量可通过 `-XX:MaxDirectMemorySize` 指定，如果不指定，则默认与 Java 堆最大值（`-Xmx` 指定）一样。

### 2.8. Java 内存区域对比

| 内存区域      | 内存作用范围   | 异常                                       |
| ------------- | -------------- | ------------------------------------------ |
| 程序计数器    | 线程私有       | 无                                         |
| Java 虚拟机栈 | 线程私有       | `StackOverflowError` 和 `OutOfMemoryError` |
| 本地方法栈    | 线程私有       | `StackOverflowError` 和 `OutOfMemoryError` |
| Java 堆       | 线程共享       | `OutOfMemoryError`                         |
| 方法区        | 线程共享       | `OutOfMemoryError`                         |
| 运行时常量池  | 线程共享       | `OutOfMemoryError`                         |
| 直接内存      | 非运行时数据区 | `OutOfMemoryError`                         |

## 3. JVM 运行原理

```java
public class JVMCase {

	// 常量
	public final static String MAN_SEX_TYPE = "man";

	// 静态变量
	public static String WOMAN_SEX_TYPE = "woman";

	public static void main(String[] args) {

		Student stu = new Student();
		stu.setName("nick");
		stu.setSexType(MAN_SEX_TYPE);
		stu.setAge(20);

		JVMCase jvmcase = new JVMCase();

		// 调用静态方法
		print(stu);
		// 调用非静态方法
		jvmcase.sayHello(stu);
	}


	// 常规静态方法
	public static void print(Student stu) {
		System.out.println("name: " + stu.getName() + "; sex:" + stu.getSexType() + "; age:" + stu.getAge());
	}


	// 非静态方法
	public void sayHello(Student stu) {
		System.out.println(stu.getName() + "say: hello");
	}
}

class Student{
	String name;
	String sexType;
	int age;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getSexType() {
		return sexType;
	}
	public void setSexType(String sexType) {
		this.sexType = sexType;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
}
```

运行以上代码时，JVM 处理过程如下：

（1）JVM 向操作系统申请内存，JVM 第一步就是通过配置参数或者默认配置参数向操作系统申请内存空间，根据内存大小找到具体的内存分配表，然后把内存段的起始地址和终止地址分配给 JVM，接下来 JVM 就进行内部分配。

（2）JVM 获得内存空间后，会根据配置参数分配堆、栈以及方法区的内存大小。

（3）class 文件加载、验证、准备以及解析，其中准备阶段会为类的静态变量分配内存，初始化为系统的初始值（这部分我在第 21 讲还会详细介绍）。

![img](https://raw.githubusercontent.com/dunwu/images/dev/snap/20200630094250.png)

（4）完成上一个步骤后，将会进行最后一个初始化阶段。在这个阶段中，JVM 首先会执行构造器 `<clinit>` 方法，编译器会在 `.java` 文件被编译成 `.class` 文件时，收集所有类的初始化代码，包括静态变量赋值语句、静态代码块、静态方法，收集在一起成为 `<clinit>()` 方法。

![img](https://raw.githubusercontent.com/dunwu/images/dev/snap/20200630094329.png)

（5）执行方法。启动 main 线程，执行 main 方法，开始执行第一行代码。此时堆内存中会创建一个 student 对象，对象引用 student 就存放在栈中。

![img](https://raw.githubusercontent.com/dunwu/images/dev/snap/20200630094651.png)

（6）此时再次创建一个 JVMCase 对象，调用 sayHello 非静态方法，sayHello 方法属于对象 JVMCase，此时 sayHello 方法入栈，并通过栈中的 student 引用调用堆中的 Student 对象；之后，调用静态方法 print，print 静态方法属于 JVMCase 类，是从静态方法中获取，之后放入到栈中，也是通过 student 引用调用堆中的 student 对象。

![img](https://raw.githubusercontent.com/dunwu/images/dev/snap/20200630094714.png)

## 4. OutOfMemoryError

### 4.1. 什么是 OutOfMemoryError

`OutOfMemoryError` 简称为 OOM。Java 中对 OOM 的解释是，没有空闲内存，并且垃圾收集器也无法提供更多内存。通俗的解释是：JVM 内存不足了。

在 JVM 规范中，**除了程序计数器区域外，其他运行时区域都可能发生 `OutOfMemoryError` 异常（简称 OOM）**。

下面逐一介绍 OOM 发生场景。

### 4.2. 堆空间溢出

`java.lang.OutOfMemoryError: Java heap space` 这个错误意味着：**堆空间溢出**。

更细致的说法是：Java 堆内存已经达到 `-Xmx` 设置的最大值。Java 堆用于存储对象实例，只要不断地创建对象，并且保证 GC Roots 到对象之间有可达路径来避免垃圾收集器回收这些对象，那么当堆空间到达最大容量限制后就会产生 OOM。

堆空间溢出有可能是**`内存泄漏（Memory Leak）`** 或 **`内存溢出（Memory Overflow）`** 。需要使用 jstack 和 jmap 生成 threaddump 和 heapdump，然后用内存分析工具（如：MAT）进行分析。

#### Java heap space 分析步骤

1. 使用 `jmap` 或 `-XX:+HeapDumpOnOutOfMemoryError` 获取堆快照。
2. 使用内存分析工具（visualvm、mat、jProfile 等）对堆快照文件进行分析。
3. 根据分析图，重点是确认内存中的对象是否是必要的，分清究竟是是内存泄漏（Memory Leak）还是内存溢出（Memory Overflow）。

#### 内存泄漏

**内存泄漏是指由于疏忽或错误造成程序未能释放已经不再使用的内存的情况**。

内存泄漏并非指内存在物理上的消失，而是应用程序分配某段内存后，由于设计错误，失去了对该段内存的控制，因而造成了内存的浪费。内存泄漏随着被执行的次数不断增加，最终会导致内存溢出。

内存泄漏常见场景：

- 静态容器
  - 声明为静态（`static`）的 `HashMap`、`Vector` 等集合
  - 通俗来讲 A 中有 B，当前只把 B 设置为空，A 没有设置为空，回收时 B 无法回收。因为被 A 引用。
- 监听器
  - 监听器被注册后释放对象时没有删除监听器
- 物理连接
  - 各种连接池建立了连接，必须通过 `close()` 关闭链接
- 内部类和外部模块等的引用
  - 发现它的方式同内存溢出，可再加个实时观察
  - `jstat -gcutil 7362 2500 70`

重点关注：

- `FGC` — 从应用程序启动到采样时发生 Full GC 的次数。
- `FGCT` — 从应用程序启动到采样时 Full GC 所用的时间（单位秒）。
- `FGC` 次数越多，`FGCT` 所需时间越多，越有可能存在内存泄漏。

如果是内存泄漏，可以进一步查看泄漏对象到 GC Roots 的对象引用链。这样就能找到泄漏对象是怎样与 GC Roots 关联并导致 GC 无法回收它们的。掌握了这些原因，就可以较准确的定位出引起内存泄漏的代码。

导致内存泄漏的常见原因是使用容器，且不断向容器中添加元素，但没有清理，导致容器内存不断膨胀。

【示例】

```java
/**
 * 内存泄漏示例
 * 错误现象：java.lang.OutOfMemoryError: Java heap space
 * VM Args：-verbose:gc -Xms10M -Xmx10M -XX:+HeapDumpOnOutOfMemoryError
 */
public class HeapOutOfMemoryDemo {

    public static void main(String[] args) {
        List<OomObject> list = new ArrayList<>();
        while (true) {
            list.add(new OomObject());
        }
    }

    static class OomObject {}

}
```

#### 内存溢出

如果不存在内存泄漏，即内存中的对象确实都必须存活着，则应当检查虚拟机的堆参数（`-Xmx` 和 `-Xms`），与机器物理内存进行对比，看看是否可以调大。并从代码上检查是否存在某些对象生命周期过长、持有时间过长的情况，尝试减少程序运行期的内存消耗。

【示例】

```java
/**
 * 堆溢出示例
 * <p>
 * 错误现象：java.lang.OutOfMemoryError: Java heap space
 * <p>
 * VM Args：-verbose:gc -Xms10M -Xmx10M
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-06-25
 */
public class HeapOutOfMemoryDemo {

    public static void main(String[] args) {
        Double[] array = new Double[999999999];
        System.out.println("array length = [" + array.length + "]");
    }

}
```

执行 `java -verbose:gc -Xms10M -Xmx10M -XX:+HeapDumpOnOutOfMemoryError io.github.dunwu.javacore.jvm.memory.HeapMemoryLeakMemoryErrorDemo`

上面的例子是一个极端的例子，试图创建一个维度很大的数组，堆内存无法分配这么大的内存，从而报错：`Java heap space`。

但如果在现实中，代码并没有问题，仅仅是因为堆内存不足，可以通过 `-Xms` 和 `-Xmx` 适当调整堆内存大小。

### 4.3. GC 开销超过限制

`java.lang.OutOfMemoryError: GC overhead limit exceeded` 这个错误，官方给出的定义是：**超过 `98%` 的时间用来做 GC 并且回收了不到 `2%` 的堆内存时会抛出此异常**。这意味着，发生在 GC 占用大量时间为释放很小空间的时候发生的，是一种保护机制。导致异常的原因：一般是因为堆太小，没有足够的内存。

【示例】

```java
/**
 * GC overhead limit exceeded 示例
 * 错误现象：java.lang.OutOfMemoryError: GC overhead limit exceeded
 * 发生在GC占用大量时间为释放很小空间的时候发生的，是一种保护机制。导致异常的原因：一般是因为堆太小，没有足够的内存。
 * 官方对此的定义：超过98%的时间用来做GC并且回收了不到2%的堆内存时会抛出此异常。
 * VM Args: -Xms10M -Xmx10M
 */
public class GcOverheadLimitExceededDemo {

    public static void main(String[] args) {
        List<Double> list = new ArrayList<>();
        double d = 0.0;
        while (true) {
            list.add(d++);
        }
    }

}
```

【处理】

与 **Java heap space** 错误处理方法类似，先判断是否存在内存泄漏。如果有，则修正代码；如果没有，则通过 `-Xms` 和 `-Xmx` 适当调整堆内存大小。

### 4.4. 永久代空间不足

【错误】

```
java.lang.OutOfMemoryError: PermGen space
```

【原因】

Perm （永久代）空间主要用于存放 `Class` 和 Meta 信息，包括类的名称和字段，带有方法字节码的方法，常量池信息，与类关联的对象数组和类型数组以及即时编译器优化。GC 在主程序运行期间不会对永久代空间进行清理，默认是 64M 大小。

根据上面的定义，可以得出 **PermGen 大小要求取决于加载的类的数量以及此类声明的大小**。因此，可以说造成该错误的主要原因是永久代中装入了太多的类或太大的类。

在 JDK8 之前的版本中，可以通过 `-XX:PermSize` 和 `-XX:MaxPermSize` 设置永久代空间大小，从而限制方法区大小，并间接限制其中常量池的容量。

#### 初始化时永久代空间不足

【示例】

```java
/**
 * 永久代内存空间不足示例
 * <p>
 * 错误现象：
 * <ul>
 * <li>java.lang.OutOfMemoryError: PermGen space (JDK8 以前版本)</li>
 * <li>java.lang.OutOfMemoryError: Metaspace (JDK8 及以后版本)</li>
 * </ul>
 * VM Args:
 * <ul>
 * <li>-Xmx100M -XX:MaxPermSize=16M (JDK8 以前版本)</li>
 * <li>-Xmx100M -XX:MaxMetaspaceSize=16M (JDK8 及以后版本)</li>
 * </ul>
 */
public class PermOutOfMemoryErrorDemo {

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 100_000_000; i++) {
            generate("eu.plumbr.demo.Generated" + i);
        }
    }

    public static Class generate(String name) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        return pool.makeClass(name).toClass();
    }

}
```

在此示例中，源代码遍历循环并在运行时生成类。javassist 库正在处理类生成的复杂性。

#### 重部署时永久代空间不足

对于更复杂，更实际的示例，让我们逐步介绍一下在应用程序重新部署期间发生的 Permgen 空间错误。重新部署应用程序时，你希望垃圾回收会摆脱引用所有先前加载的类的加载器，并被加载新类的类加载器取代。

不幸的是，许多第三方库以及对线程，JDBC 驱动程序或文件系统句柄等资源的不良处理使得无法卸载以前使用的类加载器。反过来，这意味着在每次重新部署期间，所有先前版本的类仍将驻留在 PermGen 中，从而在每次重新部署期间生成数十兆的垃圾。

让我们想象一个使用 JDBC 驱动程序连接到关系数据库的示例应用程序。启动应用程序时，初始化代码将加载 JDBC 驱动程序以连接到数据库。对应于规范，JDBC 驱动程序向 java.sql.DriverManager 进行注册。该注册包括将对驱动程序实例的引用存储在 DriverManager 的静态字段中。

现在，当从应用程序服务器取消部署应用程序时，java.sql.DriverManager 仍将保留该引用。我们最终获得了对驱动程序类的实时引用，而驱动程序类又保留了用于加载应用程序的 java.lang.Classloader 实例的引用。反过来，这意味着垃圾回收算法无法回收空间。

而且该 java.lang.ClassLoader 实例仍引用应用程序的所有类，通常在 PermGen 中占据数十兆字节。这意味着只需少量重新部署即可填充通常大小的 PermGen。

#### PermGen space 解决方案

（1）解决初始化时的 `OutOfMemoryError`

在应用程序启动期间触发由于 PermGen 耗尽导致的 `OutOfMemoryError` 时，解决方案很简单。该应用程序仅需要更多空间才能将所有类加载到 PermGen 区域，因此我们只需要增加其大小即可。为此，更改你的应用程序启动配置并添加（或增加，如果存在）`-XX:MaxPermSize` 参数，类似于以下示例：

```
java -XX:MaxPermSize=512m com.yourcompany.YourClass
```

上面的配置将告诉 JVM，PermGen 可以增长到 512MB。

清理应用程序中 `WEB-INF/lib` 下的 jar，用不上的 jar 删除掉，多个应用公共的 jar 移动到 Tomcat 的 lib 目录，减少重复加载。

🔔 注意：`-XX:PermSize` 一般设为 64M

（2）解决重新部署时的 `OutOfMemoryError`

重新部署应用程序后立即发生 OutOfMemoryError 时，应用程序会遭受类加载器泄漏的困扰。在这种情况下，解决问题的最简单，继续进行堆转储分析–使用类似于以下命令的重新部署后进行堆转储：

```
jmap -dump:format=b,file=dump.hprof <process-id>
```

然后使用你最喜欢的堆转储分析器打开转储（Eclipse MAT 是一个很好的工具）。在分析器中可以查找重复的类，尤其是那些正在加载应用程序类的类。从那里，你需要进行所有类加载器的查找，以找到当前活动的类加载器。

对于非活动类加载器，你需要通过从非活动类加载器收集到 GC 根的最短路径来确定阻止它们被垃圾收集的引用。有了此信息，你将找到根本原因。如果根本原因是在第三方库中，则可以进入 Google/StackOverflow 查看是否是已知问题以获取补丁/解决方法。

（3）解决运行时 `OutOfMemoryError`

第一步是检查是否允许 GC 从 PermGen 卸载类。在这方面，标准的 JVM 相当保守-类是天生的。因此，一旦加载，即使没有代码在使用它们，类也会保留在内存中。当应用程序动态创建许多类并且长时间不需要生成的类时，这可能会成为问题。在这种情况下，允许 JVM 卸载类定义可能会有所帮助。这可以通过在启动脚本中仅添加一个配置参数来实现：

```
-XX:+CMSClassUnloadingEnabled
```

默认情况下，此选项设置为 false，因此要启用此功能，你需要在 Java 选项中显式设置。如果启用 CMSClassUnloadingEnabled，GC 也会扫描 PermGen 并删除不再使用的类。请记住，只有同时使用 UseConcMarkSweepGC 时此选项才起作用。

```
-XX:+UseConcMarkSweepGC
```

在确保可以卸载类并且问题仍然存在之后，你应该继续进行堆转储分析–使用类似于以下命令的方法进行堆转储：

```
jmap -dump:file=dump.hprof,format=b <process-id>
```

然后，使用你最喜欢的堆转储分析器（例如 Eclipse MAT）打开转储，然后根据已加载的类数查找最昂贵的类加载器。从此类加载器中，你可以继续提取已加载的类，并按实例对此类进行排序，以使可疑对象排在首位。

然后，对于每个可疑者，就需要你手动将根本原因追溯到生成此类的应用程序代码。

### 4.5. 元数据区空间不足

【错误】

```
Exception in thread "main" java.lang.OutOfMemoryError: Metaspace
```

【原因】

Java8 以后，JVM 内存空间发生了很大的变化。取消了永久代，转而变为元数据区。

**元数据区的内存不足，即方法区和运行时常量池的空间不足**。

方法区用于存放 Class 的相关信息，如类名、访问修饰符、常量池、字段描述、方法描述等。

一个类要被垃圾收集器回收，判定条件是比较苛刻的。在经常动态生成大量 Class 的应用中，需要特别注意类的回收状况。这类常见除了 CGLib 字节码增强和动态语言以外，常见的还有：大量 JSP 或动态产生 JSP 文件的应用（JSP 第一次运行时需要编译为 Java 类）、基于 OSGi 的应用（即使是同一个类文件，被不同的加载器加载也会视为不同的类）等。

【示例】方法区出现 `OutOfMemoryError`

```java
public class MethodAreaOutOfMemoryDemo {

    public static void main(String[] args) {
        while (true) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(Bean.class);
            enhancer.setUseCache(false);
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                    return proxy.invokeSuper(obj, args);
                }
            });
            enhancer.create();
        }
    }

    static class Bean {}

}
```

【解决】

当由于元空间而面临 `OutOfMemoryError` 时，第一个解决方案应该是显而易见的。如果应用程序耗尽了内存中的 Metaspace 区域，则应增加 Metaspace 的大小。更改应用程序启动配置并增加以下内容：

```
-XX:MaxMetaspaceSize=512m
```

上面的配置示例告诉 JVM，允许 Metaspace 增长到 512 MB。

另一种解决方案甚至更简单。你可以通过删除此参数来完全解除对 Metaspace 大小的限制，JVM 默认对 Metaspace 的大小没有限制。但是请注意以下事实：这样做可能会导致大量交换或达到本机物理内存而分配失败。

### 4.6. 无法新建本地线程

`java.lang.OutOfMemoryError: Unable to create new native thread` 这个错误意味着：**Java 应用程序已达到其可以启动线程数的限制**。

【原因】

当发起一个线程的创建时，虚拟机会在 JVM 内存创建一个 `Thread` 对象同时创建一个操作系统线程，而这个系统线程的内存用的不是 JVM 内存，而是系统中剩下的内存。

那么，究竟能创建多少线程呢？这里有一个公式：

```
线程数 = (MaxProcessMemory - JVMMemory - ReservedOsMemory) / (ThreadStackSize)
```

【参数】

- `MaxProcessMemory` - 一个进程的最大内存
- `JVMMemory` - JVM 内存
- `ReservedOsMemory` - 保留的操作系统内存
- `ThreadStackSize` - 线程栈的大小

**给 JVM 分配的内存越多，那么能用来创建系统线程的内存就会越少，越容易发生 `unable to create new native thread`**。所以，JVM 内存不是分配的越大越好。

但是，通常导致 `java.lang.OutOfMemoryError` 的情况：无法创建新的本机线程需要经历以下阶段：

1. JVM 内部运行的应用程序请求新的 Java 线程
2. JVM 本机代码代理为操作系统创建新本机线程的请求
3. 操作系统尝试创建一个新的本机线程，该线程需要将内存分配给该线程
4. 操作系统将拒绝本机内存分配，原因是 32 位 Java 进程大小已耗尽其内存地址空间（例如，已达到（2-4）GB 进程大小限制）或操作系统的虚拟内存已完全耗尽
5. 引发 `java.lang.OutOfMemoryError: Unable to create new native thread` 错误。

【示例】

```java
public class UnableCreateNativeThreadErrorDemo {

    public static void main(String[] args) {
        while (true) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.MINUTES.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
```

【处理】

可以通过增加操作系统级别的限制来绕过无法创建新的本机线程问题。例如，如果限制了 JVM 可在用户空间中产生的进程数，则应检查出并可能增加该限制：

```shell
[root@dev ~]# ulimit -a
core file size          (blocks, -c) 0
--- cut for brevity ---
max user processes              (-u) 1800
```

通常，`OutOfMemoryError` 对新的本机线程的限制表示编程错误。当应用程序产生数千个线程时，很可能出了一些问题—很少有应用程序可以从如此大量的线程中受益。

解决问题的一种方法是开始进行线程转储以了解情况。

### 4.7. 直接内存溢出

由直接内存导致的内存溢出，一个明显的特征是在 Head Dump 文件中不会看见明显的异常，如果发现 OOM 之后 Dump 文件很小，而程序中又直接或间接使用了 NIO，就可以考虑检查一下是不是这方面的原因。

【示例】直接内存 `OutOfMemoryError`

```java
/**
 * 本机直接内存溢出示例
 * 错误现象：java.lang.OutOfMemoryError
 * VM Args：-Xmx20M -XX:MaxDirectMemorySize=10M
 */
public class DirectOutOfMemoryDemo {

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) throws IllegalAccessException {
        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        while (true) {
            unsafe.allocateMemory(_1MB);
        }
    }

}
```

## 5. StackOverflowError

对于 HotSpot 虚拟机来说，栈容量只由 `-Xss` 参数来决定如果线程请求的栈深度大于虚拟机所允许的最大深度，将抛出 `StackOverflowError` 异常。

从实战来说，栈溢出的常见原因：

- **递归函数调用层数太深**
- **大量循环或死循环**

【示例】递归函数调用层数太深导致 `StackOverflowError`

```java
public class StackOverflowDemo {

    private int stackLength = 1;

    public void recursion() {
        stackLength++;
        recursion();
    }

    public static void main(String[] args) {
        StackOverflowDemo obj = new StackOverflowDemo();
        try {
            obj.recursion();
        } catch (Throwable e) {
            System.out.println("栈深度：" + obj.stackLength);
            e.printStackTrace();
        }
    }

}
```

## 6. 参考资料

- [《深入理解 Java 虚拟机》](https://book.douban.com/subject/34907497/)
- [《Java 性能调优实战》](https://time.geekbang.org/column/intro/100028001)
- [从表到里学习 JVM 实现](https://www.douban.com/doulist/2545443/)
- [作为测试你应该知道的 JAVA OOM 及定位分析](https://www.jianshu.com/p/28935cbfbae0)
- [异常、堆内存溢出、OOM 的几种情况](https://blog.csdn.net/sinat_29912455/article/details/51125748)
- [介绍 JVM 中 OOM 的 8 种类型](https://tianmingxing.com/2019/11/17/%E4%BB%8B%E7%BB%8DJVM%E4%B8%ADOOM%E7%9A%848%E7%A7%8D%E7%B1%BB%E5%9E%8B/)
