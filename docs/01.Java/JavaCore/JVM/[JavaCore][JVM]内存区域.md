---
title: Java 虚拟机之内存区域
date: 2020-06-28 16:19:00
categories:
  - Java
  - JavaCore
  - JVM
tags:
  - Java
  - JavaCore
  - JVM
permalink: /pages/d2b4f7d2/
---

# Java 虚拟机之内存区域

## 简介

JVM 运行时数据区是 Java 程序执行时的内存布局。包括线程私有的程序计数器、虚拟机栈、本地方法栈，以及线程共享的 Java 堆和方法区。理解内存区域的划分是进行内存调优、排查 OOM 和 StackOverflow 等问题的基础。

## 运行时数据区域

JVM 在执行 Java 程序的过程中会把它所管理的内存划分为若干个不同的数据区域。这些区域都有各自的用途，以及创建和销毁的时间，有的区域随着虚拟机进程的启动而存在，有些区域则依赖用户线程的启动和结束而建立和销毁。如下图所示：

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2025/05/9240b05d12a24954aaa2e25d232bc3e4.png)

### 程序计数器

**程序计数器（Program Counter Register）** 是一块较小的内存空间，它可以看做是**当前线程所执行的字节码的行号指示器**。字节码解释器工作时就是通过改变这个计数器的值来选取下一条需要执行的字节码指令，它是程序控制流的指示器，分支、循环、跳转、异常处理、线程恢复等基础功能都需要依赖这个计数器来完成。

由于 Java 虚拟机的多线程是通过线程轮流切换、分配处理器执行时间的方式来实现的，在任何一个确定的时刻，一个处理器都只会执行一条线程中的指令。因此，为了线程切换后能恢复到正确的执行位置，每条线程都需要有一个独立的程序计数器，各线程之间计数器互不影响，独立存储，我们称这类内存区域为“线程私有”的内存。

如果线程正在执行的是一个 Java 方法，这个计数器记录的是正在执行的虚拟机字节码指令的地址；如果正在执行的是本地（Native）方法，这个计数器值则应为空（Undefined）。

> 🔔 注意：程序计数器是 JVM 中没有规定任何 `OutOfMemoryError` 情况的唯一区域。

### Java 虚拟机栈

**Java 虚拟机栈（Java Virtual Machine Stacks）** 也是线程私有的，它的生命周期与线程相同。**Java 虚拟机栈以方法作为最基本的执行单元，描述的是 Java 方法执行的线程内存模型**。**每个方法被执行的时候，JVM 都会同步创建一个栈帧（Stack Frame），栈帧是用于支持虚拟机进行方法调用和方法执行背后的数据结构。栈帧存储了局部变量表、操作数栈、动态连接、方法返回地址等信息**。每一个方法从调用开始至执行结束的过程，都对应着一个栈帧在虚拟机栈里面从入栈到出栈的过程。

一个线程中的方法调用链可能会很长，以 Java 程序的角度来看，同一时刻、同一条线程里面，在 调用堆栈的所有方法都同时处于执行状态。而对于执行引擎来讲，在活动线程中，只有位于栈顶的方 法才是在运行的，只有位于栈顶的栈帧才是生效的，其被称为“当前栈帧”（Current Stack Frame），与 这个栈帧所关联的方法被称为“当前方法”（Current Method）。执行引擎所运行的所有字节码指令都只 针对当前栈帧进行操作。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2024/08/9ed9a4d1afa64196863ea4aabffa0b8e.png)

- **局部变量表** - 用于存放方法参数和方法内部定义的局部变量。
- **操作数栈** - 主要作为方法调用的中转站使用，用于存放方法执行过程中产生的中间计算结果。另外，计算过程中产生的临时变量也会放在操作数栈中。
- **动态连接** - 用于一个方法调用其他方法的场景。Class 文件的常量池中有大量的符号引用，字节码中的方法调用指令就以常量池中指向方法的符号引用为参数。这些符号引用一部分会在类加载阶段或第一次使用的时候转化为直接引用，这种转化称为**静态解析**；另一部分将在每一次的运行期间转化为直接应用，这部分称为**动态连接**。
- **方法返回地址** - 用于返回方法被调用的位置，恢复上层方法的局部变量和操作数栈。Java 方法有两种返回方式，一种是 `return` 语句正常返回，一种是抛出异常。无论采用何种退出方式，都会导致栈帧被弹出。也就是说，栈帧随着方法调用而创建，随着方法结束而销毁。无论方法正常完成还是异常完成都算作方法结束。

> 🔔 注意：
>
> 该区域可能抛出以下异常：
>
> - 如果线程请求的栈深度大于虚拟机所允许的深度，将抛出 `StackOverflowError` 异常；
> - 如果虚拟机栈进行动态扩展时，无法申请到足够内存，就会抛出 `OutOfMemoryError` 异常。
>
> 💡 提示：
>
> 可以通过 `-Xss` 这个虚拟机参数来指定一个程序的 Java 虚拟机栈内存大小：
>
> ```java
> java -Xss=512M HackTheJava
> ```

#### 局部变量表

**局部变量表（Local Variables Table）**是一组变量值的存储空间，**用于存放方法参数和方法内部定义的局部变量**。在 Java 程序被编译为 Class 文件时，就在方法的 Code 属性的 max_locals 数据项中确定了该方法所需分配的局部变量表的最大容量。

局部变量表存放了编译期可知的各种 Java 虚拟机基本数据类型、对象引用（`reference` 类型，它不同于对象本身，可能是一个指向对象起始地址的引用指针，也可能是指向一个代表对象的句柄或其他与此对象相关的位置）和 `returnAddress` 类型（指向了一条字节码指令的地址）。这些数据类型在局部变量表中的存储空间以局部变量槽（Variable Slot）来表示，其中 64 位长度的 `long` 和 `double` 类型的数据会占用两个变量槽，其余的数据类型只占用一个。局部变量表所需的内存空间在编译期间完成分配，当进入一个方法时，这个方法需要在栈帧中分配多大的局部变量空间是完全确定的，在方法运行期间不会改变局部变量表的大小。

#### 操作数栈

**操作数栈（Operand Stack）**也常被称为操作栈，它是一个后入先出（Last In First Out，LIFO） 栈。**操作数栈主要作为方法调用的中转站使用，用于存放方法执行过程中产生的中间计算结果**。另外，计算过程中产生的临时变量也会放在操作数栈中。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2024/08/e546003105fd4776968c14b2f9976bb7.png)

当一个方法刚刚开始执行的时候，这个方法的操作数栈是空的，在方法的执行过程中，会有各种字节码指令往操作数栈中写入和提取内容，也就是出栈和入栈操作。譬如在做算术运算的时候是通过将运算涉及的操作数栈压入栈顶后调用运算指令来进行的，又譬如在调用其他方法的时候是通过操作数栈来进行方法参数的传递。

操作数栈中元素的数据类型必须与字节码指令的序列严格匹配，在编译程序代码的时候，编译器 必须要严格保证这一点，在类校验阶段的数据流分析中还要再次验证这一点。

另外在概念模型中，两个不同栈帧作为不同方法的虚拟机栈的元素，是完全相互独立的。但是在大多虚拟机的实现里都会进行一些优化处理，令两个栈帧出现一部分重叠。让下面栈帧的部分操作数栈与上面栈帧的部分局部变量表重叠在一起，这样做不仅节约了一些空间，更重要的是在进行方法调用时就可以直接共用一部分数据，无须进行额外的参数复制传递了。

#### 动态连接

用于一个方法调用其他方法的场景。Class 文件的常量池中有大量的符号引用，字节码中的方法调用指令就以常量池中指向方法的符号引用为参数。这些符号引用一部分会在类加载阶段或第一次使用的时候转化为直接引用，这种转化称为**静态解析**；另一部分将在每一次的运行期间转化为直接应用，这部分称为**动态连接**。

每个栈帧都包含一个指向运行时常量池中该栈帧所属方法的引用，持有这个引用是为了支持方 法调用过程中的**动态连接（Dynamic Linking）**。通过第 6 章的讲解，我们知道 Class 文件的常量池中存 有大量的符号引用，字节码中的方法调用指令就以常量池里指向方法的符号引用作为参数。这些符号 引用一部分会在类加载阶段或者第一次使用的时候就被转化为直接引用，这种转化被称为静态解析。 另外一部分将在每一次运行期间都转化为直接引用，这部分就称为动态连接。关于这两个转化过程的 具体过程，将在 8.3 节中再详细讲解。

#### 方法返回地址

方法返回地址用于返回方法被调用的位置，恢复上层方法的局部变量和操作数栈。

Java 方法有两种返回方式，一种是 `return` 语句正常返回，这时候可能会有返回值传递给上层的方法调用者，方法是否有返回值以及返回值的类型将根据遇到何种方法返回指令来决定；一种是遇到了异常，并且这个异常没有在方法体内得到妥善处理。无论采用何种退出方式，都会导致栈帧被弹出。也就是说，栈帧随着方法调用而创建，随着方法结束而销毁。无论方法正常完成还是异常完成都算作方法结束。

方法返回时可能需要在栈帧中保存一些信息，用来帮助恢复它的上层主调方法的执行状态。 一般来说，方法正常退出时，主调方法的 PC 计数器的值就可以作为返回地址，栈帧中很可能会保存这个计数器值。而方法异常退出时，返回地址是要通过异常处理器表来确定的，栈帧中就一般不会保存这部分信息。方法退出的过程实际上等同于把当前栈帧出栈，因此退出时可能执行的操作有：恢复上层方法的局部变量表和操作数栈，把返回值（如果有的话）压入调用者栈帧的操作数栈中，调整 PC 计数器的值以指向方法调用指令后面的一条指令等。

### 本地方法栈

**本地方法栈（Native Method Stack）** 与虚拟机栈的作用非常相似，二者区别仅在于：**虚拟机栈为 Java 方法服务；本地方法栈为 Native 方法服务**。

> 🔔 注意：本地方法栈也会在栈深度溢出或者栈扩展失败时分别抛出 `StackOverflowError` 和 `OutOfMemoryError` 异常。

### Java 堆

**Java 堆（Java Heap） 的作用就是存放对象实例，几乎所有的对象实例都是在这里分配内存**。

> 注：由于即时编译技术的进步，尤其是逃逸分析技术的日渐强大，栈上分配、标量替换优化手段已经导致一些微妙的变化悄然发生，所以说 Java 对象实例都分配在堆上也渐渐变得不是那么绝对了。

Java 堆是垃圾收集器管理的内存区域（因此也被叫做"GC 堆"）。现代的垃圾收集器大部分都是采用**分代收集理论**设计的，该力量的思想是针对不同的对象采取不同的垃圾回收算法。

在 JDK 7 及之前版本，堆内存被通常分为下面三部分：

- **`新生代（Young Generation）`**
- **`老年代（Old Generation）`**
- **`永久代（Permanent Generation）`**

**JDK 8 版本之后 PermGen（永久代） 已被 Metaspace（元空间） 取代，元空间使用的是本地内存**。

大部分情况，对象都会首先在 Eden 区域分配，在一次新生代垃圾回收后，如果对象还存活，则会进入 S0 或者 S1，并且对象的年龄还会加 1(Eden 区->Survivor 区后对象的初始年龄变为 1)，当它的年龄增加到一定程度（默认为 15 岁），就会被晋升到老年代中。对象晋升到老年代的年龄阈值，可以通过参数 `-XX:MaxTenuringThreshold` 来设置。不过，设置的值应该在 0-15，否则会爆出以下错误：

```bash
MaxTenuringThreshold of 20 is invalid; must be between 0 and 15
```

**为什么年龄只能是 0-15?**

因为记录年龄的区域在对象头中，这个区域的大小通常是 4 位。这 4 位可以表示的最大二进制数字是 1111，即十进制的 15。因此，对象的年龄被限制为 0 到 15。

这里我们简单结合对象布局来详细介绍一下。

在 HotSpot 虚拟机中，对象在内存中存储的布局可以分为 3 块区域：对象头（Header）、实例数据（Instance Data）和对齐填充（Padding）。其中，对象头包括两部分：标记字段（Mark Word）和类型指针（Klass Word）。关于对象内存布局的详细介绍，后文会介绍到，这里就不重复提了。

这个年龄信息就是在标记字段中存放的（标记字段还存放了对象自身的其他信息比如哈希码、锁状态信息等等）。

如果从分配内存的角度看，所有线程共享的 Java 堆中可以划分出多个线程私有的分配缓冲区 （Thread Local Allocation Buffer，TLAB），以提升对象分配效率。不过无论从什么角度，无论如何划分，都不会改变 Java 堆中存储内容的共性，无论是哪个区域，存储的都只能是对象的实例，将 Java 堆细分的目的只是为了更好地回收内存，或者更快地分配内存。

> 🔔 注意：Java 堆既可以被实现成固定大小的，也可以是可扩展的，不过当前主流的 Java 虚拟机都是按照可扩展来实现的，扩展失败会抛出 `OutOfMemoryError` 异常。
>
> 可以通过 `-Xms` 和 `-Xmx` 两个虚拟机参数来指定一个程序的 Java 堆内存大小，第一个参数设置初始值，第二个参数设置最大值。
>
> ```java
> java -Xms=1M -Xmx=2M HackTheJava
> ```

### 方法区

方法区（Method Area）是各个线程共享的内存区域。**方法区用于存放已被加载的类信息、常量、静态变量、即时编译器编译后的代码等数据**。

在 JDK8 以前，方法区常被称为**永久代**，但这种说法是不准确的：仅仅是因为当时的 HotSpot 虚拟机使用永久代来实现方法区而已。对于其他虚拟机而言，是不存在永久代概念的。**永久代这种设计，导致了 Java 应用更容易遇到内存溢出的问题**（永久代有 `-XX:MaxPermSize` 的上限，即使不设置也有默认大小）。

- JDK7 之前，HotSpot 虚拟机把它当成永久代来进行垃圾回收，可通过参数 `-XX:PermSize` 和 `-XX:MaxPermSize` 设置。
- JDK8 之后，取消了永久代，用 **`metaspace（元空间）`**替代，可通过参数 `-XX:MaxMetaspaceSize` 设置。

方法区的内存回收目标主要是针对常量池的回收和对类型的卸载，一般来说这个区域的回收效果比较难令人满意，尤其是类型的卸载，条件相当苛刻。

> 🔔 注意：方法区和 Java 堆一样不需要连续的内存，并且可以动态扩展，动态扩展失败一样会抛出 `OutOfMemoryError` 异常。

### 运行时常量池

**`运行时常量池（Runtime Constant Pool）` 是方法区的一部分**，Class 文件中除了有类的版本、字段、方法、接口等描述信息，还有一项信息是常量池表（Constant Pool Table），**用于存放编译器生成的各种字面量和符号引用**，这部分内容将在类加载后写入。

- **字面量** - 文本字符串、声明为 `final` 的常量值等。
- **符号引用** - 类和接口的完全限定名（Fully Qualified Name）、字段的名称和描述符（Descriptor）、方法的名称和描述符。

运行时常量池相对于 Class 文件常量池的另外一个重要特征是具备动态性，Java 语言并不要求常量 一定只有编译期才能产生，也就是说，并非预置入 Class 文件中常量池的内容才能进入方法区运行时常量池，运行期间也可以将新的常量放入池中，这种特性被开发人员利用得比较多的便是 `String` 类的 `intern()` 方法。

> 🔔 注意：当常量池无法再申请到内存时会抛出 `OutOfMemoryError` 异常。

### 直接内存

直接内存（Direct Memory）并不是虚拟机运行时数据区的一部分，也不是 JVM 规范中定义的内存区域。

JDK4 中新加入了 NIO，它可以使用 Native 函数库直接分配堆外内存，然后通过一个存储在 Java 堆里的 `DirectByteBuffer` 对象作为这块内存的引用进行操作。这样能在一些场景中显著提高性能，因为避免了在 Java 堆和 Native 堆中来回复制数据。

直接内存容量可通过 `-XX:MaxDirectMemorySize` 指定，如果不指定，则默认与 Java 堆最大值（`-Xmx` 指定）一样。

> 🔔 注意：直接内存这部分也被频繁的使用，且也可能导致 `OutOfMemoryError` 异常。

### Java 内存区域对比

| 内存区域      | 内存作用范围   | 异常                                       |
| ------------- | -------------- | ------------------------------------------ |
| 程序计数器    | 线程私有       | 无                                         |
| Java 虚拟机栈 | 线程私有       | `StackOverflowError` 和 `OutOfMemoryError` |
| 本地方法栈    | 线程私有       | `StackOverflowError` 和 `OutOfMemoryError` |
| Java 堆       | 线程共享       | `OutOfMemoryError`                         |
| 方法区        | 线程共享       | `OutOfMemoryError`                         |
| 运行时常量池  | 线程共享       | `OutOfMemoryError`                         |
| 直接内存      | 非运行时数据区 | `OutOfMemoryError`                         |

## 虚拟机对象

### 对象的创建

当 Java 虚拟机**遇到一条字节码 `new` 指令时，首先在常量池中尝试定位类的符号引用，并检查这个类是否已被类加载，如果没有，则必须先执行相应的类加载过程**。

在类加载检查通过后，**接下来虚拟机将为新生对象分配内存**。对象所需内存的大小在类加载完成后便可完全确定，为对象分配空间的任务实际上便等同于把一块确定大小的内存块从 Java 堆中划分出来。分配对象内存有两种方式：

**指针碰撞（Bump The Pointer）** - 如果 Java 堆中**内存是规整的**，所有被使用过的内存都被放在一 边，空闲的内存被放在另一边，中间放着一个指针作为分界点的指示器，那所分配内存就仅仅是把那个指针向空闲空间方向挪动一段与对象大小相等的距离。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2024/08/823753f373ad4ea98a5d4c749956b231.png)

**空闲列表（Free List）** - 如果 Java 堆中的**内存是不规整的**，已被使用的内存和空闲的内存相互交错在一起，那就没有办法简单地进行指针碰撞了，虚拟机就必须维护一个列表，记录上哪些内存块是可用的，在分配的时候从列表中找到一块足够大的空间划分给对象实例，并更新列表上的记录。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2024/08/9604526516774939a9c9564f34d92efc.png)

选择哪种分配方式由 Java 堆是否规整决定，而 Java 堆是否规整又由所采用的垃圾收集器是否采用**标记-压缩算法**决定。因此，当使用 Serial、ParNew 等带压缩整理过程的收集器时，系统采用的分配算法是指针碰撞，既简单又高效；而当使用 CMS 这种基于清除 （Sweep）算法的收集器时，理论上就只能采用较为复杂的空闲列表来分配内存。

对象创建在虚拟机中是非常频繁的行为，因此还需要考虑分配内存空间的并发安全问题。一般有两种方案：

- CAS 同步 - 对分配内存空间的动作进行同步处理——实际上虚拟机是采用 CAS 配上失败 重试的方式保证更新操作的原子性；
- TLAB - 另外一种是把内存分配的动作按照线程划分在不同的空间之中进行，即每个线程在 Java 堆中预先分配一小块内存，称为**本地线程分配缓冲（Thread Local Allocation Buffer，TLAB）**，哪个线程要分配内存，就在哪个线程的本地缓冲区中分配，只有本地缓冲区用完了，分配新的缓存区时才需要同步锁定。

接下来，需要执行类的构造函数（即 `<init>()` 方法）对对象进行初始化。

### 对象的内存布局

在 HotSpot 虚拟机里，对象在堆内存中的存储布局可以划分为三个部分：

- **对象头（Header）** - HotSpot 虚拟机对象的对象头部分包括两类信息。
  - **Mark Word** - **用于存储对象自身的运行时数据**。如哈 希码（HashCode）、GC 分代年龄、锁状态标志、线程持有的锁、偏向线程 ID、偏向时间戳等，这部分数据的长度在 32 位和 64 位的虚拟机（未开启压缩指针）中分别为 32 个比特和 64 个比特。
  - **类型指针** - **对象指向它的类型元数据的指针**，Java 虚拟机通过这个指针来确定该对象是哪个类的实例。并不是所有的虚拟机实现都必须在对象数据上保留类型指针，换句话说，查找对象的元数据信息并不一定要经过对象本身。此外，如果对象是一个 Java 数组，那在对象头中还必须有一块用于记录数组长度的数据，因为虚拟机可以通过普通 Java 对象的元数据信息确定 Java 对象的大小，但是如果数组的长度是不确定的，将无法通过元数据中的信息推断出数组的大小。
- **实例数据（Instance Data）** - **对象真正存储的有效信息**，即我们在程序代码里面所定义的各种类型的字段内容，无论是从父类继承下来的，还是在子类中定义的字段都必须记录起来。
- **对齐填充（Padding）** - 并不是必然存在的，也没有特别的含义，它仅仅起着占位符的作用。

### 对象的访问定位

Java 程序会通过栈上的 reference 数据来操作堆上的具体对象。主流的对象访问方式主要有使用句柄和直接指针两种：使用句柄访问和使用直接指针访问。

#### 使用句柄访问

Java 堆中将可能会划分出一块内存来作为句柄池，reference 中存储的就是对象的句柄地址，而句柄中包含了对象实例数据与类型数据各自具体的地址信息。使用句柄来访问的最大好处就是 reference 中存储的是稳定句柄地 址，在对象被移动（垃圾收集时移动对象是非常普遍的行为）时只会改变句柄中的实例数据指针，而 reference 本身不需要被修改。

#### 使用直接指针访问

Java 堆中对象的内存布局就必须考虑如何放置访问类型数据的相关信息，reference 中存储的直接就是对象地址，如果只是访问对象本身的话，就不需要多一次间接访问的开销。使用直接指针来访问最大的好处就是速度更快，它节省了一次指针定位的时间开销。HotSpot 主要使用第二种方式进行对象访问。

## 内存分配

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

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2020/06/86cd92c518354736a9b81b59bf9b5382.png)

（4）完成上一个步骤后，将会进行最后一个初始化阶段。在这个阶段中，JVM 首先会执行构造器 `<clinit>` 方法，编译器会在 `.java` 文件被编译成 `.class` 文件时，收集所有类的初始化代码，包括静态变量赋值语句、静态代码块、静态方法，收集在一起成为 `<clinit>()` 方法。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2020/06/5679266c95774a7c96091bfb0b7500d1.png)

（5）执行方法。启动 main 线程，执行 main 方法，开始执行第一行代码。此时堆内存中会创建一个 student 对象，对象引用 student 就存放在栈中。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2020/06/296e8c01f3bf40199a511ed39dd040d2.png)

（6）此时再次创建一个 JVMCase 对象，调用 sayHello 非静态方法，sayHello 方法属于对象 JVMCase，此时 sayHello 方法入栈，并通过栈中的 student 引用调用堆中的 Student 对象；之后，调用静态方法 print，print 静态方法属于 JVMCase 类，是从静态方法中获取，之后放入到栈中，也是通过 student 引用调用堆中的 student 对象。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2020/06/a51b2a1e963a4130a54c3ad8377a6945.png)

## 内存溢出

### OutOfMemoryError

`OutOfMemoryError` 简称为 OOM。Java 中对 OOM 的解释是，没有空闲内存，并且垃圾收集器也无法提供更多内存。通俗的解释是：JVM 内存不足了。

在 JVM 规范中，**除了程序计数器区域外，其他运行时区域都可能发生 `OutOfMemoryError` 异常（简称 OOM）**。

下面逐一介绍 OOM 发生场景。

#### 堆空间溢出

**java.lang.OutOfMemoryError: Java heap space 意味着：堆空间溢出**。

更细致的说法是：Java 堆内存已经达到 `-Xmx` 设置的最大值。Java 堆用于存储对象实例，只要不断地创建对象，并且保证 GC Roots 到对象之间有可达路径来避免垃圾收集器回收这些对象，那么当堆空间到达最大容量限制后就会产生 OOM。

堆空间溢出有可能是**内存泄漏（Memory Leak）** 或 **内存溢出（Memory Overflow）** 。

##### Java heap space 分析步骤

1. 使用 `jmap` 或 `-XX:+HeapDumpOnOutOfMemoryError` 获取堆快照。
2. 使用内存分析工具（VisualVM、MAT、JProfile 等）对堆快照文件进行分析。
3. 根据分析图，重点是确认内存中的对象是否是必要的，分清究竟是是内存泄漏还是内存溢出。

##### 内存泄漏

**内存泄漏（Memory Leak）是指由于疏忽或错误造成程序未能释放已经不再使用的内存的情况**。

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
public class HeapMemoryLeakOOM {

    public static void main(String[] args) {
        List<OomObject> list = new ArrayList<>();
        while (true) {
            list.add(new OomObject());
        }
    }

    static class OomObject {}

}
```

##### 内存溢出

如果不存在内存泄漏，即内存中的对象确实都必须存活着，则应当检查虚拟机的堆参数（`-Xmx` 和 `-Xms`），与机器物理内存进行对比，看看是否可以调大。并从代码上检查是否存在某些对象生命周期过长、持有时间过长的情况，尝试减少程序运行期的内存消耗。

【示例】

```java
/**
 * 堆溢出示例
 * <p>
 * 错误现象：java.lang.OutOfMemoryError: Java heap space
 * <p>
 * VM Args：-verbose:gc -Xms10M -Xmx10M
 */
public class HeapOutOfMemoryOOM {

    public static void main(String[] args) {
        Double[] array = new Double[999999999];
        System.out.println("array length = [" + array.length + "]");
    }

}
```

上面的例子是一个极端的例子，试图创建一个维度很大的数组，堆内存无法分配这么大的内存，从而报错：`Java heap space`。

但如果在现实中，代码并没有问题，仅仅是因为堆内存不足，可以通过 `-Xms` 和 `-Xmx` 适当调整堆内存大小。

#### GC 开销超过限制

`java.lang.OutOfMemoryError: GC overhead limit exceeded` 这个错误，官方给出的定义是：**超过 `98%` 的时间用来做 GC 并且回收了不到 `2%` 的堆内存时会抛出此异常**。这意味着，发生在 GC 占用大量时间为释放很小空间的时候发生的，是一种保护机制。导致异常的原因：一般是因为堆太小，没有足够的内存。

【示例】

```java
/**
 * GC overhead limit exceeded 示例
 * 错误现象：java.lang.OutOfMemoryError: GC overhead limit exceeded
 * 发生在 GC 占用大量时间为释放很小空间的时候发生的，是一种保护机制。导致异常的原因：一般是因为堆太小，没有足够的内存。
 * 官方对此的定义：超过 98%的时间用来做 GC 并且回收了不到 2%的堆内存时会抛出此异常。
 * VM Args: -Xms10M -Xmx10M
 */
public class GcOverheadLimitExceededOOM {

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

#### 永久代空间不足

【错误】

```
java.lang.OutOfMemoryError: PermGen space
```

【原因】

Perm （永久代）空间主要用于存放 `Class` 和 Meta 信息，包括类的名称和字段，带有方法字节码的方法，常量池信息，与类关联的对象数组和类型数组以及即时编译器优化。GC 在主程序运行期间不会对永久代空间进行清理，默认是 64M 大小。

根据上面的定义，可以得出 **PermGen 大小要求取决于加载的类的数量以及此类声明的大小**。因此，可以说造成该错误的主要原因是永久代中装入了太多的类或太大的类。

在 JDK8 之前的版本中，可以通过 `-XX:PermSize` 和 `-XX:MaxPermSize` 设置永久代空间大小，从而限制方法区大小，并间接限制其中常量池的容量。

##### 初始化时永久代空间不足

【示例】

```java
/**
 * 永久代内存空间不足示例
 * <p>
 * 错误现象：
 * <ul>
 * <li>java.lang.OutOfMemoryError: PermGen space (JDK8 以前版本）</li>
 * <li>java.lang.OutOfMemoryError: Metaspace (JDK8 及以后版本）</li>
 * </ul>
 * VM Args:
 * <ul>
 * <li>-Xmx100M -XX:MaxPermSize=16M (JDK8 以前版本）</li>
 * <li>-Xmx100M -XX:MaxMetaspaceSize=16M (JDK8 及以后版本）</li>
 * </ul>
 */
public class PermGenSpaceOOM {

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

##### 重部署时永久代空间不足

对于更复杂，更实际的示例，让我们逐步介绍一下在应用程序重新部署期间发生的 PermGen 空间错误。重新部署应用程序时，你希望垃圾回收会摆脱引用所有先前加载的类的加载器，并被加载新类的类加载器取代。

不幸的是，许多第三方库以及对线程，JDBC 驱动程序或文件系统句柄等资源的不良处理使得无法卸载以前使用的类加载器。反过来，这意味着在每次重新部署期间，所有先前版本的类仍将驻留在 PermGen 中，从而在每次重新部署期间生成数十兆的垃圾。

让我们想象一个使用 JDBC 驱动程序连接到关系数据库的示例应用程序。启动应用程序时，初始化代码将加载 JDBC 驱动程序以连接到数据库。对应于规范，JDBC 驱动程序向 `java.sql.DriverManager` 进行注册。该注册包括将对驱动程序实例的引用存储在 `DriverManager` 的静态字段中。

现在，当从应用程序服务器取消部署应用程序时，`java.sql.DriverManager` 仍将保留该引用。我们最终获得了对驱动程序类的实时引用，而驱动程序类又保留了用于加载应用程序的 `java.lang.Classloader` 实例的引用。反过来，这意味着垃圾回收算法无法回收空间。

而且该 `java.lang.ClassLoader` 实例仍引用应用程序的所有类，通常在 PermGen 中占据数十兆字节。这意味着只需少量重新部署即可填充通常大小的 PermGen。

##### PermGen space 解决方案

（1）解决初始化时的 `OutOfMemoryError`

在应用程序启动期间触发由于 PermGen 耗尽导致的 `OutOfMemoryError` 时，解决方案很简单。该应用程序仅需要更多空间才能将所有类加载到 PermGen 区域，因此我们只需要增加其大小即可。为此，更改你的应用程序启动配置并添加（或增加，如果存在）`-XX:MaxPermSize` 参数，类似于以下示例：

```
java -XX:MaxPermSize=512m com.yourcompany.YourClass
```

上面的配置将告诉 JVM，PermGen 可以增长到 512MB。

清理应用程序中 `WEB-INF/lib` 下的 jar，用不上的 jar 删除掉，多个应用公共的 jar 移动到 Tomcat 的 lib 目录，减少重复加载。

🔔 注意：`-XX:PermSize` 一般设为 64M

（2）解决重新部署时的 `OutOfMemoryError`

重新部署应用程序后立即发生 `OutOfMemoryError` 时，应用程序会遭受类加载器泄漏的困扰。在这种情况下，解决问题的最简单，继续进行堆转储分析–使用类似于以下命令的重新部署后进行堆转储：

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

#### 元数据区空间不足

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

#### 无法新建本地线程

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
public class UnableCreateNativeThreadOOM {

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

#### 直接内存溢出

直接内存（Direct Memory）的容量大小可通过 `-XX:MaxDirectMemorySize` 参数来指定，如果不指定，则默认与 Java 堆最大值（由 `-Xmx` 指定）一致。

由直接内存导致的内存溢出，一个明显的特征是在 Heap Dump 文件中不会看见有什么明显的异常情况，如果读者发现内存溢出之后产生的 Dump 文件很小，而程序中又直接或间接使用了 DirectMemory（典型的间接使用就是 NIO），那就可以考虑重点检查一下直接内存方面的原因了。

由直接内存导致的内存溢出，一个明显的特征是在 Heapdump 文件中不会看见明显的异常，如果发现 OOM 之后 Dump 文件很小，而程序中又直接或间接使用了 NIO，就可以考虑检查一下是不是这方面的原因。

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

### StackOverflowError

HotSpot 虚拟机中并不区分虚拟机栈和本地方法栈。

对于 HotSpot 虚拟机来说，栈容量只由 `-Xss` 参数来决定。

栈溢出的常见原因：

- **递归函数调用层数太深** - 线程请求的栈深度大于虚拟机所允许的最大深度，将抛出 `StackOverflowError` 异常。

- **大量循环或死循环** - 虚拟机的栈内存允许动态扩展，当扩展栈容量无法申请到足够的内存时，将抛出

  `OutOfMemoryError` 异常。

【示例】递归函数调用层数太深导致 `StackOverflowError`

```java
/**
 * 以一个无限递归的示例方法来展示栈溢出
 * <p>
 * 栈溢出时，Java 会抛出 StackOverflowError ，出现此种情况是因为方法运行的时候栈的大小超过了虚拟机的上限所致。
 * <p>
 * Java 应用程序唤起一个方法调用时就会在调用栈上分配一个栈帧，这个栈帧包含引用方法的参数，本地参数，以及方法的返回地址。
 * <p>
 * 这个返回地址是被引用的方法返回后，程序能够继续执行的执行点。
 * <p>
 * 如果没有一个新的栈帧所需空间，Java 就会抛出 StackOverflowError。
 * <p>
 * VM 参数：
 * <ul>
 * <li>-Xss228k - 设置栈大小为 228k</li>
 * </ul>
 * <p>
 *
 */
public class StackOverflowErrorDemo {

    private int stackLength = 1;

    public static void main(String[] args) {
        StackOverflowErrorDemo obj = new StackOverflowErrorDemo();
        try {
            obj.recursion();
        } catch (Throwable e) {
            System.out.println("栈深度：" + obj.stackLength);
            e.printStackTrace();
        }
    }

    public void recursion() {
        stackLength++;
        recursion();
    }

}
```

【示例】大量循环或死循环导致 `StackOverflowError`

```java
/**
 * 类成员循环依赖，导致 StackOverflowError
 *
 * VM 参数：
 *
 * -Xss228k - 设置栈大小为 228k
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-06-25
 */
public class StackOverflowErrorDemo2 {

    public static void main(String[] args) {
        A obj = new A();
        System.out.println(obj.toString());
    }

    static class A {

        private int value;

        private B instance;

        public A() {
            value = 0;
            instance = new B();
        }

        @Override
        public String toString() {
            return "<" + value + ", " + instance + ">";
        }

    }

    static class B {

        private int value;

        private A instance;

        public B() {
            value = 10;
            instance = new A();
        }

        @Override
        public String toString() {
            return "<" + value + ", " + instance + ">";
        }

    }

}
```

## 典型应用场景

### 场景一：排查 StackOverflowError

方法递归过深导致栈溢出，通过 jstack 分析栈帧：

```bash
jstack -l <pid> | grep -A 20 "StackOverflowError"
```

### 场景二：分析堆内存泄漏

使用 MAT 工具分析 HeapDump 中的 GC Roots 引用链：

```bash
jmap -dump:format=b,file=heap.hprof <pid>
# 用 Eclipse MAT 打开 heap.hprof 分析内存泄漏
```

### 场景三：元空间配置优化

配置元空间大小避免类加载过多导致 OOM：

```bash
-XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m
```

## 最佳实践

1. **设置相同的 -Xms 和 -Xmx**：避免堆内存动态扩容的抖动。
2. **合理配置元空间**：根据应用加载的类数量设置合适的 Metaspace 大小。
3. **使用 -XX:+UseCompressedOops**：开启指针压缩，减少对象引用的内存占用（默认开启）。
4. **监控各区域使用率**：通过 JMX 或 jstat 监控堆、栈、元空间的使用情况。

## 常见问题

### Q1：堆和栈的区别？

- **堆**：存储对象实例，被所有线程共享，由 GC 管理。
- **栈**：存储局部变量和方法调用帧，线程私有，自动回收。

### Q2：什么是方法区/元空间？

方法区存储类的元数据、常量、静态变量等。JDK 8 之前用永久代实现，JDK 8 后改为元空间（使用本地内存而非堆内存）。

### Q3：直接内存是什么？

直接内存是 JVM 之外通过 `DirectByteBuffer` 分配的堆外内存，不受 GC 直接管理，但受 `-XX:MaxDirectMemorySize` 限制。常用于 NIO 的高性能场景。

## 参考资料

- [《深入理解 Java 虚拟机》](https://book.douban.com/subject/34907497/)
- [极客时间教程 - Java 性能调优实战](https://time.geekbang.org/column/intro/100028001)
- [从表到里学习 JVM 实现](https://www.douban.com/doulist/2545443/)
- [作为测试你应该知道的 JAVA OOM 及定位分析](https://www.jianshu.com/p/28935cbfbae0)
- [异常、堆内存溢出、OOM 的几种情况](https://blog.csdn.net/sinat_29912455/article/details/51125748)
- [介绍 JVM 中 OOM 的 8 种类型](https://tianmingxing.com/2019/11/17/%E4%BB%8B%E7%BB%8DJVM%E4%B8%ADOOM%E7%9A%848%E7%A7%8D%E7%B1%BB%E5%9E%8B/)
