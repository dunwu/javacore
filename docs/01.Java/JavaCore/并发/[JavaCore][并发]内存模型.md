---
title: Java 并发之内存模型
date: 2020-12-25 18:43:11
categories:
  - Java
  - JavaCore
  - 并发
tags:
  - Java
  - JavaCore
  - 并发
  - JMM
  - Happens-Before
  - 内存屏障
  - volatile
  - synchronized
  - final
  - 指令重排序
permalink: /pages/f824f527/
---

# Java 并发之内存模型

Java 内存模型（Java Memory Model），简称 **JMM**。Java 内存模型的目标是为了解决由可见性和有序性导致的并发安全问题。Java 内存模型通过 **屏蔽各种硬件和操作系统的内存访问差异，以实现让 Java 程序在各种平台下都能达到一致的内存访问效果**。

## 物理内存模型

物理机遇到的并发问题与虚拟机中的情况有不少相似之处，物理机对并发的处理方案对于虚拟机的实现也有相当大的参考意义。

### 硬件处理效率存在很大差异

技术在进步，CPU、内存、I/O 设备的性能也在不断提高。但是，始终存在一个核心矛盾：**CPU、内存、I/O 设备存在很大的速度差异** - CPU 远快于内存，内存远快于 I/O 设备。木桶短板理论告诉我们：一只木桶能装多少水，取决于最短的那块木板。同理，程序整体性能取决于最慢的操作（即 I/O 操作），所以单方面提高 CPU、内存的性能是无效的。

为了合理利用 CPU 的高性能，平衡这三者的速度差异，计算机体系机构、操作系统、编译程序都做出了贡献，主要体现为：

- **CPU 增加了缓存**，以均衡与 CPU 内存的速度差异；
- **编译程序优化指令执行次序**，使得缓存能够得到更加合理地利用。
- **操作系统增加了进程、线程**，以分时复用 CPU，进而均衡 CPU 与 I/O 的速度差异；

**缓存**导致的可见性问题，**编译优化**带来的有序性问题，**线程切换**带来的原子性问题。

### 缓存一致性

高速缓存解决了 **硬件效率问题**，但是引入了一个新的问题：**缓存一致性（Cache Coherence）**。

在多处理器系统中，每个处理器都有自己的高速缓存，而它们又共享同一主内存。当多个处理器的运算任务都涉及同一块主内存区域时，将可能导致各自的缓存数据不一致。

为了解决缓存一致性问题，**需要各个处理器访问缓存时都遵循一些协议，在读写时要根据协议来进行操作**。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2024/08/fb22bbdce2e94b4999d82a3750f00589.png)

### 指令重排序

为了使缓存得到更加合理地使用，计算机在执行程序代码的时候，会对指令进行重排序。

**什么是指令重排序？** 简单来说就是系统在执行代码的时候并不一定是按照你写的代码的顺序依次执行。

常见的指令重排序有下面 2 种情况：

- **编译器优化重排**：编译器（包括 JVM、JIT 编译器等）在不改变单线程程序语义的前提下，重新安排语句的执行顺序。
- **指令并行重排**：现代处理器采用了指令级并行技术 (Instruction-Level Parallelism，ILP) 来将多条指令重叠执行。如果不存在数据依赖性，处理器可以改变语句对应机器指令的执行顺序。

另外，内存系统也会有“重排序”，但又不是真正意义上的重排序。在 JMM 里表现为主存和本地内存的内容可能不一致，进而导致程序在多线程下执行可能出现问题。

Java 源代码会经历 **编译器优化重排 —> 指令并行重排 —> 内存系统重排** 的过程，最终才变成操作系统可执行的指令序列。

**指令重排序可以保证串行语义一致，但是没有义务保证多线程间的语义也一致** ，所以在多线程下，指令重排序可能会导致一些问题。

对于编译器优化重排和处理器的指令重排序（指令并行重排和内存系统重排都属于是处理器级别的指令重排序），处理该问题的方式不一样。

- 对于编译器，通过禁止特定类型的编译器重排序的方式来禁止重排序。
- 对于处理器，通过插入内存屏障（Memory Barrier，或有时叫做内存栅栏，Memory Fence）的方式来禁止特定类型的处理器重排序。

## Java 内存模型

**内存模型** 这个概念。我们可以理解为：**在特定的操作协议下，对特定的内存或高速缓存进行读写访问的过程抽象**。不同架构的物理计算机可以有不一样的内存模型，JVM 也有自己的内存模型。

JVM 中试图定义一种 Java 内存模型（Java Memory Model, JMM）来**屏蔽各种硬件和操作系统的内存访问差异**，以实现让 Java 程序 **在各种平台下都能达到一致的内存访问效果**。

在 [Java 并发简介](https://dunwu.github.io/waterdrop/pages/8503f00b/) 中已经介绍了，并发安全需要满足可见性、有序性、原子性。其中，导致可见性的原因是缓存，导致有序性的原因是编译优化。那解决可见性、有序性最直接的办法就是**禁用缓存和编译优化** 。但这么做，性能就堪忧了。

合理的方案应该是**按需禁用缓存以及编译优化**。那么，如何做到呢？，Java 内存模型规范了 JVM 如何提供按需禁用缓存和编译优化的方法。具体来说，这些方法包括 **volatile**、**synchronized** 和 **final** 三个关键字，以及 **Happens-Before 规则**。

### 主内存和工作内存

JMM 的主要目标是 **定义程序中各个变量的访问规则，即在虚拟机中将变量存储到内存和从内存中取出变量这样的底层细节**。此处的变量（Variables）与 Java 编程中所说的变量有所区别，它包括了实例字段、静态字段和构成数值对象的元素，但不包括局部变量与方法参数，因为后者是线程私有的，不会被共享，自然就不会存在竞争问题。为了获得较好的执行效能，JMM 并没有限制执行引擎使用处理器的特定寄存器或缓存来和主存进行交互，也没有限制即使编译器进行调整代码执行顺序这类优化措施。

JMM 规定了**所有的变量都存储在主内存（Main Memory）中**。

每条线程还有自己的工作内存（Working Memory），**工作内存中保留了该线程使用到的变量的主内存的副本**。工作内存是 JMM 的一个抽象概念，并不真实存在，它涵盖了缓存，写缓冲区，寄存器以及其他的硬件和编译器优化。

线程对变量的所有操作都必须在工作内存中进行，而不能直接读写主内存中的变量。不同的线程间也无法直接访问对方工作内存中的变量，**线程间变量值的传递均需要通过主内存来完成**。

> 说明：
>
> 这里说的主内存、工作内存与 Java 内存区域中的堆、栈、方法区等不是同一个层次的内存划分。

### JMM 内存操作的问题

类似于物理内存模型面临的问题，JMM 存在以下两个问题：

- **工作内存数据一致性** - 各个线程操作数据时会保存使用到的主内存中的共享变量副本，当多个线程的运算任务都涉及同一个共享变量时，将导致各自的的共享变量副本不一致。如果真的发生这种情况，数据同步回主内存以谁的副本数据为准？ Java 内存模型主要通过一系列的数据同步协议、规则来保证数据的一致性。
- **指令重排序优化** - Java 中重排序通常是编译器或运行时环境为了优化程序性能而采取的对指令进行重新排序执行的一种手段。重排序分为两类：**编译期重排序和运行期重排序**，分别对应编译时和运行时环境。 同样的，指令重排序不是随意重排序，它需要满足以下两个条件：
  - 在单线程环境下不能改变程序运行的结果。即时编译器（和处理器）需要保证程序能够遵守 `as-if-serial` 属性。通俗地说，就是在单线程情况下，要给程序一个顺序执行的假象。即经过重排序的执行结果要与顺序执行的结果保持一致。
  - 存在数据依赖关系的不允许重排序。
  - 多线程环境下，如果线程处理逻辑之间存在依赖关系，有可能因为指令重排序导致运行结果与预期不同。

### 内存间交互操作

JMM 定义了 8 个操作来完成主内存和工作内存之间的交互操作。JVM 实现时必须保证下面介绍的每种操作都是 **原子的**（对于 double 和 long 型的变量来说，load、store、read、和 write 操作在某些平台上允许有例外 ）。

- `lock` （锁定） - 作用于**主内存**的变量，它把一个变量标识为一条线程独占的状态。
- `unlock` （解锁） - 作用于**主内存**的变量，它把一个处于锁定状态的变量释放出来，释放后的变量才可以被其他线程锁定。
- `read` （读取） - 作用于**主内存**的变量，它把一个变量的值从主内存**传输**到线程的工作内存中，以便随后的 `load` 动作使用。
- `write` （写入） - 作用于**主内存**的变量，它把 store 操作从工作内存中得到的变量的值放入主内存的变量中。
- `load` （载入） - 作用于**工作内存**的变量，它把 read 操作从主内存中得到的变量值放入工作内存的变量副本中。
- `use` （使用） - 作用于**工作内存**的变量，它把工作内存中一个变量的值传递给执行引擎，每当虚拟机遇到一个需要使用到变量的值得字节码指令时就会执行这个操作。
- `assign` （赋值） - 作用于**工作内存**的变量，它把一个从执行引擎接收到的值赋给工作内存的变量，每当虚拟机遇到一个给变量赋值的字节码指令时执行这个操作。
- `store` （存储） - 作用于**工作内存**的变量，它把工作内存中一个变量的值传送到主内存中，以便随后 `write` 操作使用。

如果要把一个变量从主内存中复制到工作内存，就**需要按序执行 `read` 和 `load` 操作**；如果把变量从工作内存中同步回主内存中，就**需要按序执行 `store` 和 `write` 操作**。但 Java 内存模型只要求上述操作必须按顺序执行，而没有保证必须是连续执行。

JMM 还规定了上述 8 种基本操作，需要满足以下规则：

- **read 和 load 必须成对出现**；**store 和 write 必须成对出现**。即不允许一个变量从主内存读取了但工作内存不接受，或从工作内存发起回写了但主内存不接受的情况出现。
- **不允许一个线程丢弃它的最近 assign 的操作**，即变量在工作内存中改变了之后必须把变化同步到主内存中。
- **不允许一个线程无原因的（没有发生过任何 assign 操作）把数据从工作内存同步回主内存中**。
- 一个新的变量只能在主内存中诞生，不允许在工作内存中直接使用一个未被初始化（load 或 assign ）的变量。换句话说，就是对一个变量实施 use 和 store 操作之前，必须先执行过了 load 或 assign 操作。
- 一个变量在同一个时刻只允许一条线程对其进行 lock 操作，但 lock 操作可以被同一条线程重复执行多次，多次执行 lock 后，只有执行相同次数的 unlock 操作，变量才会被解锁。所以 **lock 和 unlock 必须成对出现**。
- 如果对一个变量执行 lock 操作，将会清空工作内存中此变量的值，在执行引擎使用这个变量前，需要重新执行 load 或 assign 操作初始化变量的值。
- 如果一个变量事先没有被 lock 操作锁定，则不允许对它执行 unlock 操作，也不允许去 unlock 一个被其他线程锁定的变量。
- 对一个变量执行 unlock 操作之前，必须先把此变量同步到主内存中（执行 store 和 write 操作）

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2024/08/2cbd12a4220d4374bd1f0f77fcbdcf6b.png)

### 并发安全特性

并发最重要的问题是并发安全问题。所谓**并发安全**，是指保证程序的正确性，使得并发处理结果符合预期。

并发安全需要保证几个基本特性：

- **可见性** - 是一个线程修改了某个共享变量，其状态能够立即被其他线程知晓，通常被解释为将线程本地状态反映到主内存上，`volatile` 就是负责保证可见性的。
- **有序性** - 是保证线程内串行语义，避免指令重排等。
- **原子性** - 简单说就是相关操作不会中途被其他线程干扰，一般通过互斥机制（加锁：`sychronized`、`Lock`）实现。

而这三大特性，归根结底，是为了实现多线程的 **数据一致性**，使得程序在多线程并发，指令重排序优化的环境中能如预期运行。上文介绍了 Java 内存交互的 8 种基本操作，它们都保证可见性、有序性、原子性。

#### 原子性

**原子性即一个操作或者多个操作，要么全部执行（执行的过程不会被任何因素打断），要么就都不执行**。即使在多个线程一起执行的时候，一个操作一旦开始，就不会被其他线程所干扰。

在 Java 中，为了保证原子性，提供了两个高级的字节码指令 `monitorenter` 和 `monitorexit`。这两个字节码，在 Java 中对应的关键字就是 `synchronized`。

因此，在 Java 中可以使用 `synchronized` 来保证方法和代码块内的操作是原子性的。

#### 可见性

**可见性是指当多个线程访问同一个变量时，一个线程修改了这个变量的值，其他线程能够立即看得到修改的值**。

JMM 是通过 **"变量修改后将新值同步回主内存**， **变量读取前从主内存刷新变量值"** 这种依赖主内存作为传递媒介的方式来实现的。

Java 实现多线程可见性的方式有：

- `volatile`
- `synchronized`
- `final`

#### 有序性

有序性规则表现在以下两种场景：线程内和线程间

- 线程内 - 从某个线程的角度看方法的执行，指令会按照一种叫“串行”（`as-if-serial`）的方式执行，此种方式已经应用于顺序编程语言。
- 线程间 - 这个线程“观察”到其他线程并发地执行非同步的代码时，由于指令重排序优化，任何代码都有可能交叉执行。唯一起作用的约束是：对于同步方法，同步块（`synchronized` 关键字修饰）以及 `volatile` 字段的操作仍维持相对有序。

在 Java 中，可以使用 `synchronized` 和 `volatile` 来保证多线程之间操作的有序性。实现方式有所区别：

- `volatile` 关键字会禁止指令重排序。
- `synchronized` 关键字通过互斥保证同一时刻只允许一条线程操作。

## Happens-Before

JMM 为程序中所有的操作定义了一个偏序关系，称之为 **`先行发生原则（Happens-Before）`**。**Happens-Before** 是指 **前面一个操作的结果对后续操作是可见的**。

> 1978 年，Lamport 在论文 [**Time, Clocks, and the Ordering of Events in a Distributed System**](https://lamport.azurewebsites.net/pubs/time-clocks.pdf) （[**译文**](https://cloud.tencent.com/developer/article/1163428)，[**解读**](https://zhuanlan.zhihu.com/p/56146800) ）中第一次提出了 Happens-Before，阐述了偏序关系（partial ordering）、逻辑时钟（Logical Clocks）概念，提出解决分布式系统中区分事件发生的时序问题的方法。Happens-Before 的语义是一种因果关系：如果 A 事件是导致 B 事件的起因，那么 A 事件一定是先于（Happens-Before）B 事件发生的。

**Happens-Before** 非常重要，它是判断数据是否存在竞争、线程是否安全的主要依据，依靠这个原则，我们可以通过几条规则一揽子地解决并发环境下两个操作间是否可能存在冲突的所有问题。

- **程序顺序规则** - 在一个线程中，按照程序顺序，前面的操作 Happens-Before 于后续的任意操作。
- **锁定规则** - 一个 `unLock` 操作 Happens-Before 于后面对同一个锁的 `lock` 操作。
- **volatile 变量规则** - 对一个 `volatile` 变量的写操作 Happens-Before 于后面对这个变量的读操作。
- **线程启动规则** - `Thread` 对象的 `start()` 方法 Happens-Before 于此线程的每个一个动作。
- **线程终止规则** - 线程中所有的操作都 Happens-Before 于线程的终止检测，我们可以通过 `Thread.join()` 方法是否结束、`Thread.isAlive()` 的返回值手段检测到线程已经终止执行。
- **线程中断规则** - 对线程 `interrupt()` 方法的调用 Happens-Before 于被中断线程的代码检测到中断事件的发生，可以通过 `Thread.interrupted()` 方法检测到是否有中断发生。
- **对象终结规则** - 一个对象的初始化完成 Happens-Before 于它的 `finalize()` 方法的开始。
- **传递性** - 如果 A Happens-Before B，且 B Happens-Before C，那么 A Happens-Before C。

## 内存屏障

Java 中如何保证底层操作的有序性和可见性？可以通过内存屏障（memory barrier）。

内存屏障是被插入两个 CPU 指令之间的一种指令，用来禁止处理器指令发生重排序（像屏障一样），从而保障**有序性**的。另外，为了达到屏障的效果，它也会使处理器写入、读取值之前，将主内存的值写入高速缓存，清空无效队列，从而保障**可见性**。

举个例子：

```
Store1;
Store2;
Load1;
StoreLoad;  //内存屏障
Store3;
Load2;
Load3;
复制代码
```

对于上面的一组 CPU 指令（Store 表示写入指令，Load 表示读取指令），StoreLoad 屏障之前的 Store 指令无法与 StoreLoad 屏障之后的 Load 指令进行交换位置，即**重排序**。但是 StoreLoad 屏障之前和之后的指令是可以互换位置的，即 Store1 可以和 Store2 互换，Load2 可以和 Load3 互换。

常见有 4 种屏障

- `LoadLoad` 屏障 - 对于这样的语句 `Load1; LoadLoad; Load2`，在 Load2 及后续读取操作要读取的数据被访问前，保证 Load1 要读取的数据被读取完毕。
- `StoreStore` 屏障 - 对于这样的语句 `Store1; StoreStore; Store2`，在 Store2 及后续写入操作执行前，保证 Store1 的写入操作对其它处理器可见。
- `LoadStore` 屏障 - 对于这样的语句 `Load1; LoadStore; Store2`，在 Store2 及后续写入操作被执行前，保证 Load1 要读取的数据被读取完毕。
- `StoreLoad` 屏障 - 对于这样的语句 `Store1; StoreLoad; Load2`，在 Load2 及后续所有读取操作执行前，保证 Store1 的写入对所有处理器可见。它的开销是四种屏障中最大的（冲刷写缓冲器，清空无效化队列）。在大多数处理器的实现中，这个屏障是个万能屏障，兼具其它三种内存屏障的功能。

Java 中对内存屏障的使用在一般的代码中不太容易见到，常见的有 `volatile` 和 `synchronized` 关键字修饰的代码块（后面再展开介绍），还可以通过 `Unsafe` 这个类来使用内存屏障。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2026/02/91c041f6660542489de6ba2c2a2f5cf8.png)

## Synchronized 内存语义

`synchronized` 是 Java 中的关键字，是 **利用锁的机制来实现互斥同步的**。

**`synchronized` 可以保证在同一个时刻，只有一个线程可以执行某个方法或者某个代码块**。

- **线程释放锁时内存语义：JMM 会把该线程对应的工作内存中的共享变量刷新到主内存中**
- **线程获取锁时内存语义：JMM 会把该线程对应的工作内存置为无效**

如果不需要 `Lock` 、`ReadWriteLock` 所提供的高级同步特性，应该优先考虑使用 `synchronized` ，理由如下：

- Java 1.6 以后，`synchronized` 做了大量的优化，其性能已经与 `Lock` 、`ReadWriteLock` 基本上持平。从趋势来看，Java 未来仍将继续优化 `synchronized` ，而不是 `ReentrantLock` 。
- `ReentrantLock` 是 Oracle JDK 的 API，在其他版本的 JDK 中不一定支持；而 `synchronized` 是 JVM 的内置特性，所有 JDK 版本都提供支持。

### synchronized 的应用

`synchronized` 有 3 种应用方式：

- **同步实例方法** - 对于普通同步方法，锁是当前实例对象
- **同步静态方法** - 对于静态同步方法，锁是当前类的 `Class` 对象
- **同步代码块** - 对于同步方法块，锁是 `synchonized` 括号里配置的对象

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2024/09/4175cd3e336f4ac489f3f0e328f907aa.png)

【示例】`synchronized` 的使用语法

```java
class Test {

    // 修饰成员方法
    synchronized void sync1() {
        // 临界区
    }

    // 修饰静态方法
    synchronized static void sync2() {
        // 临界区
    }

    // 对象锁
    Object obj = new Object();
    // 修饰代码块，使用对象锁
    void sync3() {
        synchronized (obj) {
            // 临界区
        }
    }

    // 修饰代码块，使用类锁（Class）
    void sync4() {
        synchronized (Test.class) {
            //临界区
        }
    }

}
```

#### 用 synchronized 实现线程安全的计数器

我们先来看一个简单的示例，这段代码维护了一个计数器变量 `count`，并通过 get() 和 add() 分别实现了读写方法。

```java
@NotThreadSafe
public class NotThreadSafeCounter {

    private static int count = 0;

    public int get() {
        return count;
    }

    public void add() {
        count++;
    }

    public static void main(String[] args) throws InterruptedException {
        final int MAX = 100000;
        NotThreadSafeCounter instance = new NotThreadSafeCounter();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < MAX; i++) {
                instance.add();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < MAX; i++) {
                instance.add();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("count = " + instance.get());
    }

}
// 输出：
// count = 117626
//
```

启动两个线程并行执行，期望最终值为 200000，但实际值为小于 200000 的随机数字。显然，上面的示例是线程不安全的。究其原因，在于 count++ 不是原子操作，不满足并发安全的原子性要求。

要解决此问题，可以用 `synchronized` 修饰方法， `synchronized` 可以保证同一时刻只有一个线程执行临界区的代码。

我们针对上面的示例来进行改造，将 `add()` 方法用 `synchronized` 修饰，如下所示。这下是不是就可以高枕无忧了呢？

```java
@NotThreadSafe
public class NotThreadSafeCounter2 {

    private static int count = 0;

    public int get() {
        return count;
    }

    public synchronized void add() {
        count++;
    }
}
```

首先，`add()` 方法本身是线程安全的。但是，这个示例忽略了 `get()` 方法。因为 `get()` 方法未加锁，一个线程调用 `add()` 方法后，无法保证另一个线程调用 `get()` 时能立刻获取到更新后的结果，不满足并发安全的可见性要求。

如何彻底解决 get() 并发不安全的问题呢？很简单，就是 `get()` 方法也用 `synchronized` 修饰一下。最终的线程安全示例如下：

```java
@ThreadSafe
public class ThreadSafeCounter {

    private int count = 0;

    public synchronized long get() {
        return count;
    }

    public synchronized void add() {
        count++;
    }
}
```

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2024/09/c46f008fb46042fe84753cfc98110f0f.png)

#### 静态 `synchronized` 方法和非静态 `synchronized` 是否互斥

静态方法的同步是指同步在该方法所在的类对象上。因为在 JVM 中一个类只能对应一个类对象，所以同时只允许一个线程执行同一个类中的静态同步方法。

静态 `synchronized` 方法和非静态 `synchronized` 方法之间的调用互斥么？

答案是：不互斥，但可能存在并发问题！如果一个线程 A 调用一个实例对象的非静态 `synchronized` 方法；而线程 B 需要调用这个实例对象所属类的静态 `synchronized` 方法，是允许的，不会发生互斥现象，因为访问静态 `synchronized` 方法占用的锁是当前类的锁，而访问非静态 `synchronized` 方法占用的锁是当前实例对象锁。

```java
@ThreadSafe
public class ThreadSafeCounter2 {

    private static int count = 0;

    public synchronized long get() {
        return count;
    }

    public synchronized static void add() {
        count++;
    }
}
```

上面这段代码实际上是用两个锁保护同一个资源。这个受保护的资源就是静态变量 count，两个锁分别是 this 和 ThreadSafeCounter2.class。我们可以用下面这幅图来形象描述这个关系。由于临界区 get() 和 add() 是用两个锁保护的，因此这两个临界区没有互斥关系，临界区 add() 对 value 的修改对临界区 get() 也没有可见性保证，这就导致并发问题了。

#### 用 synchronized 保护多个资源

【示例】错误示例

```java
class Account {
  private int balance;
  // 转账
  synchronized void transfer(
      Account target, int amt){
    if (this.balance > amt) {
      this.balance -= amt;
      target.balance += amt;
    }
  }
}
```

在这段代码中，临界区内有两个资源，分别是转出账户的余额 this.balance 和转入账户的余额 target.balance，并且用的是一把锁 this，符合我们前面提到的，多个资源可以用一把锁来保护，这看上去完全正确呀。真的是这样吗？可惜，这个方案仅仅是看似正确，为什么呢？

问题就出在 this 这把锁上，this 这把锁可以保护自己的余额 this.balance，却保护不了别人的余额 target.balance，就像你不能用自家的锁来保护别人家的资产，也不能用自己的票来保护别人的座位一样。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2024/09/78f1f62d279c455aa862db8f83808b52.png)

应该保证使用的**锁能覆盖所有受保护资源**。

【示例】正确姿势

```java
class Account {
  private Object lock；
  private int balance;
  private Account();
  // 创建 Account 时传入同一个 lock 对象
  public Account(Object lock) {
    this.lock = lock;
  }
  // 转账
  void transfer(Account target, int amt){
    // 此处检查所有对象共享的锁
    synchronized(lock) {
      if (this.balance > amt) {
        this.balance -= amt;
        target.balance += amt;
      }
    }
  }
}
```

这个办法确实能解决问题，但是有点小瑕疵，它要求在创建 Account 对象的时候必须传入同一个对象，如果创建 Account 对象时，传入的 lock 不是同一个对象，那可就惨了，会出现锁自家门来保护他家资产的荒唐事。在真实的项目场景中，创建 Account 对象的代码很可能分散在多个工程中，传入共享的 lock 真的很难。

上面的方案缺乏实践的可行性，我们需要更好的方案。还真有，就是**用 Account.class 作为共享的锁**。Account.class 是所有 Account 对象共享的，而且这个对象是 Java 虚拟机在加载 Account 类的时候创建的，所以我们不用担心它的唯一性。使用 Account.class 作为共享的锁，我们就无需在创建 Account 对象时传入了，代码更简单。

【示例】正确姿势

```java
class Account {
  private int balance;
  // 转账
  void transfer(Account target, int amt){
    synchronized(Account.class) {
      if (this.balance > amt) {
        this.balance -= amt;
        target.balance += amt;
      }
    }
  }
}
```

### synchronized 的原理

**`synchronized` 代码块是由一对 `monitorenter` 和 `monitorexit` 指令实现的，`Monitor` 对象是同步的基本实现单元**。在 Java 6 之前，`Monitor` 的实现完全是依靠操作系统内部的互斥锁，因为需要进行用户态到内核态的切换，所以同步操作是一个无差别的重量级操作。

如果 `synchronized` 明确制定了对象参数，那就是这个对象的引用；如果没有明确指定，那就根据 `synchronized` 修饰的是实例方法还是静态方法，去对对应的对象实例或 `Class` 对象来作为锁对象。

`synchronized` 同步块对同一线程来说是可重入的，不会出现锁死问题。

`synchronized` 同步块是互斥的，即已进入的线程执行完成前，会阻塞其他试图进入的线程。

```java
public void foo(Object lock) {
    synchronized (lock) {
      lock.hashCode();
    }
  }
  // 上面的 Java 代码将编译为下面的字节码
  public void foo(java.lang.Object);
    Code:
       0: aload_1
       1: dup
       2: astore_2
       3: monitorenter
       4: aload_1
       5: invokevirtual java/lang/Object.hashCode:()I
       8: pop
       9: aload_2
      10: monitorexit
      11: goto          19
      14: astore_3
      15: aload_2
      16: monitorexit
      17: aload_3
      18: athrow
      19: return
    Exception table:
       from    to  target type
           4    11    14   any
          14    17    14   any

```

`synchronized` 在修饰同步代码块时，是由 `monitorenter` 和 `monitorexit` 指令来实现同步的。进入 `monitorenter` 指令后，线程将持有 `Monitor` 对象，退出 `monitorenter` 指令后，线程将释放该 `Monitor` 对象。

`synchronized` 修饰同步方法时，会设置一个 `ACC_SYNCHRONIZED` 标志。当方法调用时，调用指令将会检查该方法是否被设置 `ACC_SYNCHRONIZED` 访问标志。如果设置了该标志，执行线程将先持有 `Monitor` 对象，然后再执行方法。在该方法运行期间，其它线程将无法获取到该 `Mointor` 对象，当方法执行完成后，再释放该 `Monitor` 对象。

每个对象实例都会有一个 `Monitor`，`Monitor` 可以和对象一起创建、销毁。`Monitor` 是由 `ObjectMonitor` 实现，而 `ObjectMonitor` 是由 C++ 的 `ObjectMonitor.hpp` 文件实现。

当多个线程同时访问一段同步代码时，多个线程会先被存放在 EntryList 集合中，处于 block 状态的线程，都会被加入到该列表。接下来当线程获取到对象的 Monitor 时，Monitor 是依靠底层操作系统的 Mutex Lock 来实现互斥的，线程申请 Mutex 成功，则持有该 Mutex，其它线程将无法获取到该 Mutex。

如果线程调用 wait() 方法，就会释放当前持有的 Mutex，并且该线程会进入 WaitSet 集合中，等待下一次被唤醒。如果当前线程顺利执行完方法，也将释放 Mutex。

### synchronized 的优化

**Java 1.6 以后，`synchronized` 做了大量的优化，其性能已经与 `Lock` 、`ReadWriteLock` 基本上持平**。

在 JDK1.6 JVM 中，对象实例在堆内存中被分为了三个部分：对象头、实例数据和对齐填充。其中 Java 对象头由 Mark Word、指向类的指针以及数组长度三部分组成。

Mark Word 记录了对象和锁有关的信息。Mark Word 在 64 位 JVM 中的长度是 64bit，我们可以一起看下 64 位 JVM 的存储结构是怎么样的。如下图所示：

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2020/06/a2dc15c84410441883de9c6ccf8d57ae.png)

锁升级功能主要依赖于 Mark Word 中的锁标志位和释放偏向锁标志位，`synchronized` 同步锁就是从偏向锁开始的，随着竞争越来越激烈，偏向锁升级到轻量级锁，最终升级到重量级锁。

Java 1.6 引入了偏向锁和轻量级锁，从而让 `synchronized` 拥有了四个状态：

- **无锁状态（unlocked）**
- **偏向锁状态（biasble）**
- **轻量级锁状态（lightweight locked）**
- **重量级锁状态（inflated）**

当 JVM 检测到不同的竞争状况时，会自动切换到适合的锁实现。

当没有竞争出现时，默认会使用偏向锁。JVM 会利用 CAS 操作（compare and swap），在对象头上的 Mark Word 部分设置线程 ID，以表示这个对象偏向于当前线程，所以并不涉及真正的互斥锁。这样做的假设是基于在很多应用场景中，大部分对象生命周期中最多会被一个线程锁定，使用偏斜锁可以降低无竞争开销。

如果有另外的线程试图锁定某个已经被偏斜过的对象，JVM 就需要撤销（revoke）偏向锁，并切换到轻量级锁实现。轻量级锁依赖 CAS 操作 Mark Word 来试图获取锁，如果重试成功，就使用普通的轻量级锁；否则，进一步升级为重量级锁。

#### 偏向锁

偏向锁的思想是偏向于**第一个获取锁对象的线程，这个线程在之后获取该锁就不再需要进行同步操作，甚至连 CAS 操作也不再需要**。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2020/06/84a8cc69adb94e6c891ef70f133ea222.png)

#### 轻量级锁

**轻量级锁**是相对于传统的重量级锁而言，它 **使用 CAS 操作来避免重量级锁使用互斥量的开销**。对于绝大部分的锁，在整个同步周期内都是不存在竞争的，因此也就不需要都使用互斥量进行同步，可以先采用 CAS 操作进行同步，如果 CAS 失败了再改用互斥量进行同步。

当尝试获取一个锁对象时，如果锁对象标记为 `0|01`，说明锁对象的锁未锁定（unlocked）状态。此时虚拟机在当前线程的虚拟机栈中创建 Lock Record，然后使用 CAS 操作将对象的 Mark Word 更新为 Lock Record 指针。如果 CAS 操作成功了，那么线程就获取了该对象上的锁，并且对象的 Mark Word 的锁标记变为 00，表示该对象处于轻量级锁状态。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2020/06/97343466833b451480a11f206183c694.png)

#### 锁消除 / 锁粗化

除了锁升级优化，Java 还使用了编译器对锁进行优化。

##### 锁消除

**锁消除是指对于被检测出不可能存在竞争的共享数据的锁进行消除**。

JIT 编译器在动态编译同步块的时候，借助了一种被称为逃逸分析的技术，来判断同步块使用的锁对象是否只能够被一个线程访问，而没有被发布到其它线程。

确认是的话，那么 JIT 编译器在编译这个同步块的时候不会生成 synchronized 所表示的锁的申请与释放的机器码，即消除了锁的使用。在 Java7 之后的版本就不需要手动配置了，该操作可以自动实现。

对于一些看起来没有加锁的代码，其实隐式的加了很多锁。例如下面的字符串拼接代码就隐式加了锁：

```java
public static String concatString(String s1, String s2, String s3) {
    return s1 + s2 + s3;
}
```

`String` 是一个不可变的类，编译器会对 String 的拼接自动优化。在 Java 1.5 之前，会转化为 `StringBuffer` 对象的连续 `append()` 操作：

```java
public static String concatString(String s1, String s2, String s3) {
    StringBuffer sb = new StringBuffer();
    sb.append(s1);
    sb.append(s2);
    sb.append(s3);
    return sb.toString();
}
```

每个 `append()` 方法中都有一个同步块。虚拟机观察变量 sb，很快就会发现它的动态作用域被限制在 `concatString()` 方法内部。也就是说，sb 的所有引用永远不会逃逸到 `concatString()` 方法之外，其他线程无法访问到它，因此可以进行消除。

##### 锁粗化

锁粗化同理，就是在 JIT 编译器动态编译时，如果发现几个相邻的同步块使用的是同一个锁实例，那么 JIT 编译器将会把这几个同步块合并为一个大的同步块，从而避免一个线程“反复申请、释放同一个锁“所带来的性能开销。

如果**一系列的连续操作都对同一个对象反复加锁和解锁**，频繁的加锁操作就会导致性能损耗。

上一节的示例代码中连续的 `append()` 方法就属于这类情况。如果**虚拟机探测到由这样的一串零碎的操作都对同一个对象加锁，将会把加锁的范围扩展（粗化）到整个操作序列的外部**。对于上一节的示例代码就是扩展到第一个 `append()` 操作之前直至最后一个 `append()` 操作之后，这样只需要加锁一次就可以了。

#### 自旋锁

互斥同步进入阻塞状态的开销都很大，应该尽量避免。在许多应用中，共享数据的锁定状态只会持续很短的一段时间。自旋锁的思想是让一个线程在请求一个共享数据的锁时执行忙循环（自旋）一段时间，如果在这段时间内能获得锁，就可以避免进入阻塞状态。

自旋锁虽然能避免进入阻塞状态从而减少开销，但是它需要进行忙循环操作占用 CPU 时间，它只适用于共享数据的锁定状态很短的场景。

在 Java 1.6 中引入了自适应的自旋锁。自适应意味着自旋的次数不再固定了，而是由前一次在同一个锁上的自旋次数及锁的拥有者的状态来决定。

### synchronized 的误区

> 示例摘自：[极客时间教程 - Java 业务开发常见错误 100 例](https://time.geekbang.org/column/intro/100047701)

#### synchronized 使用范围不当导致的错误

```java
public class Interesting {

    volatile int a = 1;
    volatile int b = 1;

    public static void main(String[] args) {
        Interesting interesting = new Interesting();
        new Thread(() -> interesting.add()).start();
        new Thread(() -> interesting.compare()).start();
    }

    public synchronized void add() {
        log.info("add start");
        for (int i = 0; i < 10000; i++) {
            a++;
            b++;
        }
        log.info("add done");
    }

    public void compare() {
        log.info("compare start");
        for (int i = 0; i < 10000; i++) {
            //a 始终等于 b 吗？
            if (a < b) {
                log.info("a:{},b:{},{}", a, b, a > b);
                //最后的 a>b 应该始终是 false 吗？
            }
        }
        log.info("compare done");
    }

}
```

【输出】

```
16:05:25.541 [Thread-0] INFO io.github.dunwu.javacore.concurrent.sync.synchronized 使用范围不当 - add start
16:05:25.544 [Thread-0] INFO io.github.dunwu.javacore.concurrent.sync.synchronized 使用范围不当 - add done
16:05:25.544 [Thread-1] INFO io.github.dunwu.javacore.concurrent.sync.synchronized 使用范围不当 - compare start
16:05:25.544 [Thread-1] INFO io.github.dunwu.javacore.concurrent.sync.synchronized 使用范围不当 - compare done
```

之所以出现这种错乱，是因为两个线程是交错执行 add 和 compare 方法中的业务逻辑，而且这些业务逻辑不是原子性的：a++ 和 b++ 操作中可以穿插在 compare 方法的比较代码中；更需要注意的是，a<b 这种比较操作在字节码层面是加载 a、加载 b 和比较三步，代码虽然是一行但也不是原子性的。

所以，正确的做法应该是，为 add 和 compare 都加上方法锁，确保 add 方法执行时，compare 无法读取 a 和 b：

```
public synchronized void add()
public synchronized void compare()
```

所以，使用锁解决问题之前一定要理清楚，我们要保护的是什么逻辑，多线程执行的情况又是怎样的。

#### synchronized 保护对象不对导致的错误

加锁前要清楚锁和被保护的对象是不是一个层面的。

静态字段属于类，类级别的锁才能保护；而非静态字段属于类实例，实例级别的锁就可以保护。

```java
public class synchronized 错误使用示例 2 {

    public static void main(String[] args) {
        synchronized 错误使用示例 2 demo = new synchronized 错误使用示例 2();
        System.out.println(demo.wrong(1000000));
        System.out.println(demo.right(1000000));
    }

    public int wrong(int count) {
        Data.reset();
        IntStream.rangeClosed(1, count).parallel().forEach(i -> new Data().wrong());
        return Data.getCounter();
    }

    public int right(int count) {
        Data.reset();
        IntStream.rangeClosed(1, count).parallel().forEach(i -> new Data().right());
        return Data.getCounter();
    }

    private static class Data {

        @Getter
        private static int counter = 0;
        private static Object locker = new Object();

        public static int reset() {
            counter = 0;
            return counter;
        }

        public synchronized void wrong() {
            counter++;
        }

        public void right() {
            synchronized (locker) {
                counter++;
            }
        }

    }

}
```

wrong 方法中试图对一个静态对象加对象级别的 synchronized 锁，并不能保证线程安全。

#### 锁粒度导致的问题

要尽可能的缩小加锁的范围，这可以提高并发吞吐。

如果精细化考虑了锁应用范围后，性能还无法满足需求的话，我们就要考虑另一个维度的粒度问题了，即：区分读写场景以及资源的访问冲突，考虑使用悲观方式的锁还是乐观方式的锁。

```java
public class synchronized 锁粒度不当 {

    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.wrong();
        demo.right();
    }

    private static class Demo {

        private List<Integer> data = new ArrayList<>();

        private void slow() {
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
            }
        }

        public int wrong() {
            long begin = System.currentTimeMillis();
            IntStream.rangeClosed(1, 1000).parallel().forEach(i -> {
                synchronized (this) {
                    slow();
                    data.add(i);
                }
            });
            log.info("took:{}", System.currentTimeMillis() - begin);
            return data.size();
        }

        public int right() {
            long begin = System.currentTimeMillis();
            IntStream.rangeClosed(1, 1000).parallel().forEach(i -> {
                slow();
                synchronized (data) {
                    data.add(i);
                }
            });
            log.info("took:{}", System.currentTimeMillis() - begin);
            return data.size();
        }

    }

}
```

## volatile 内存语义

`volatile` 是 JVM 提供的 **最轻量级的同步机制**。

被 `volatile` 修饰的变量，具备以下特性：

- **线程可见性**
- **禁止指令重排序**
- **不保证原子性**

线程安全需要具备：可见性、有序性、原子性。然而，`volatile` 不保证原子性，因此它不能彻底地保证线程安全。这也正如其命名，`volatile` 的中文意思是不稳定的，易变的。

### 保证线程可见性

这里的**可见性是指当一条线程修改了 volatile 变量的值，新值对于其他线程来说是可以立即得知的**。而普通变量不能做到这一点，普通变量的值在线程间传递均需要通过主内存来完成。

**线程写 volatile 变量的过程：**

1. 改变线程工作内存中 volatile 变量副本的值
2. 将改变后的副本的值从工作内存刷新到主内存

**线程读 volatile 变量的过程：**

1. 从主内存中读取 volatile 变量的最新值到线程的工作内存中
2. 从工作内存中读取 volatile 变量的副本

> 注意：**保证可见性不等同于 volatile 变量保证并发操作的安全性**
>
> 在不符合以下两点的场景中，仍然要通过枷锁来保证原子性：
>
> - 运算结果并不依赖变量的当前值，或者能够确保只有单一的线程修改变量的值。
> - 变量不需要与其他状态变量共同参与不变约束。

但是如果多个线程同时把更新后的变量值同时刷新回主内存，可能导致得到的值不是预期结果：

举个例子： 定义 `volatile int count = 0`，2 个线程同时执行 count++ 操作，每个线程都执行 500 次，最终结果小于 1000，原因是每个线程执行 count++ 需要以下 3 个步骤：

1. 线程从主内存读取最新的 count 的值
2. 执行引擎把 count 值加 1，并赋值给线程工作内存
3. 线程工作内存把 count 值保存到主内存 有可能某一时刻 2 个线程在步骤 1 读取到的值都是 100，执行完步骤 2 得到的值都是 101，最后刷新了 2 次 101 保存到主内存。

### 禁止指令重排序

观察加入 `volatile` 关键字和没有加入 `volatile` 关键字时所生成的汇编代码发现，**加入 `volatile` 关键字时，会多出一个 `lock` 前缀指令**。

**`lock` 前缀指令实际上相当于一个内存屏障**（也成内存栅栏），内存屏障会提供 3 个功能：

- 它确保指令重排序时不会把其后面的指令排到内存屏障之前的位置，也不会把前面的指令排到内存屏障的后面；即在执行到内存屏障这句指令时，在它前面的操作已经全部完成；
- 它会强制将对缓存的修改操作立即写入主存；
- 如果是写操作，它会导致其他 CPU 中对应的缓存行无效。

在 Java 中，`Unsafe` 类提供了三个开箱即用的内存屏障相关的方法，屏蔽了操作系统底层的差异：

```java
public native void loadFence();
public native void storeFence();
public native void fullFence();
```

理论上来说，你通过这个三个方法也可以实现和 `volatile` 禁止重排序一样的效果，只是会麻烦一些。

下面我以一个常见的面试题为例讲解一下 `volatile` 关键字禁止指令重排序的效果。

【示例】双重校验锁实现单例模式

```java
public class Singleton {

    private volatile static Singleton instance;

    private Singleton() { }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

}
```

`instance` 采用 `volatile` 关键字修饰也是很有必要的， `instance = new Singleton();` 这段代码其实是分为三步执行：

1. 为 `instance` 分配内存空间
2. 初始化 `instance`
3. 将 `instance` 指向分配的内存地址

但是由于 JVM 具有指令重排的特性，执行顺序有可能变成 1->3->2。指令重排在单线程环境下不会出现问题，但是在多线程环境下会导致一个线程获得还没有初始化的实例。例如，线程 T1 执行了 1 和 3，此时 T2 调用 `getInstance`() 后发现 `instance` 不为空，因此返回 `instance`，但此时 `instance` 还未被初始化。

### volatile 不保证原子性

线程安全需要具备：可见性、有序性、原子性。然而，**`volatile` 不保证原子性，所以决定了它不能彻底地保证线程安全**。

我们通过下面的代码即可证明：

```java
public class VolatileAtomicityDemo {
    public volatile static int inc = 0;

    public void increase() {
        inc++;
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        VolatileAtomicityDemo volatileAtomicityDemo = new VolatileAtomicityDemo();
        for (int i = 0; i < 5; i++) {
            threadPool.execute(() -> {
                for (int j = 0; j < 500; j++) {
                    volatileAtomicityDemo.increase();
                }
            });
        }
        // 等待 1.5 秒，保证上面程序执行完成
        Thread.sleep(1500);
        System.out.println(inc);
        threadPool.shutdown();
    }
}
```

正常情况下，运行上面的代码理应输出 `2500`。但你真正运行了上面的代码之后，你会发现每次输出结果都小于 `2500`。

为什么会出现这种情况呢？不是说好了，`volatile` 可以保证变量的可见性嘛！

也就是说，如果 `volatile` 能保证 `inc++` 操作的原子性的话。每个线程中对 `inc` 变量自增完之后，其他线程可以立即看到修改后的值。5 个线程分别进行了 500 次操作，那么最终 inc 的值应该是 5\*500=2500。

很多人会误认为自增操作 `inc++` 是原子性的，实际上，`inc++` 其实是一个复合操作，包括三步：

1. 读取 inc 的值。
2. 对 inc 加 1。
3. 将 inc 的值写回内存。

`volatile` 是无法保证这三个操作是具有原子性的，有可能导致下面这种情况出现：

1. 线程 1 对 `inc` 进行读取操作之后，还未对其进行修改。线程 2 又读取了 `inc` 的值并对其进行修改（+1），再将 `inc` 的值写回内存。
2. 线程 2 操作完毕后，线程 1 对 `inc` 的值进行修改（+1），再将 `inc` 的值写回内存。

这也就导致两个线程分别对 `inc` 进行了一次自增操作后，`inc` 实际上只增加了 1。

其实，如果想要保证上面的代码运行正确也非常简单，利用 `synchronized`、`Lock` 或者 `AtomicInteger` 都可以。

::: tabs#线程安全的计数器

@tab `synchronized` 改进

使用 `synchronized` 改进：

```java
public synchronized void increase() {
    inc++;
}
```

@tab `AtomicInteger` 改进

使用 `AtomicInteger` 改进：

```java
public AtomicInteger inc = new AtomicInteger();

public void increase() {
    inc.getAndIncrement();
}
```

@tab `ReentrantLock` 改进

使用 `ReentrantLock` 改进：

```java
Lock lock = new ReentrantLock();
public void increase() {
    lock.lock();
    try {
        inc++;
    } finally {
        lock.unlock();
    }
}
```

:::

### volatile 的应用

如果 `volatile` 变量修饰符使用恰当的话，它比 `synchronized` 的使用和执行成本更低，因为它不会引起线程上下文的切换和调度。但是，**`volatile` 无法替代 `synchronized` ，因为 `volatile` 无法保证操作的原子性**。

`volatile` 和 `synchronized` 的区别在于：

- `volatile` 本质是在告诉 JVM 当前变量在寄存器（工作内存）中的值是不确定的，需要从主存中读取；`synchronized` 则是锁定当前变量，只有当前线程可以访问该变量，其他线程被阻塞住。
- `volatile` 仅能修饰变量；`synchronized` 可以修饰方法和代码块。
- `volatile` 仅能实现变量的修改可见性，不能保证原子性；而 `synchronized` 则可以保证变量的修改可见性和原子性
- `volatile` 不会造成线程的阻塞；`synchronized` 可能会造成线程的阻塞。
- `volatile` 标记的变量不会被编译器优化；`synchronized` 标记的变量可以被编译器优化。

通常来说，**使用 `volatile` 必须具备以下 2 个条件**：

- **对变量的写操作不依赖于当前值**
- **该变量没有包含在具有其他变量的表达式中**

::: tabs#volatile 的应用

@tab 状态标记量

【示例】状态标记量

```java
volatile boolean flag = false;

while(!flag) {
    doSomething();
}

public void setFlag() {
    flag = true;
}
```

@tab 双重锁实现线程安全的单例模式

【示例】双重锁实现线程安全的单例模式

```java
class Singleton {
    private volatile static Singleton instance = null;

    private Singleton() {}

    public static Singleton getInstance() {
        if(instance==null) {
            synchronized (Singleton.class) {
                if(instance==null)
                    instance = new Singleton();
            }
        }
        return instance;
    }
}
```

:::

## final 内存语义

我们知道，`final` 成员变量必须在声明的时候初始化或者在构造器中初始化，否则就会报编译错误。 `final` 关键字的可见性是指：**final 字段一旦在声明时或构造器中初始化完成，则其他线程无需同步就能正确看见字段值**。这是因为一旦初始化完成，final 变量的值立刻回写到主内存。

## long 和 double 的特殊规则

JMM 要求 `lock`、`unlock`、`read`、`load`、`assign`、`use`、`store`、`write` 这 8 种操作都具有原子性，但是对于 64 位的数据类型（long 和 double），在模型中特别定义相对宽松的规定：**允许虚拟机将没有被 `volatile` 修饰的 64 位数据的读写操作分为 2 次 32 位的操作来进行**，即允许虚拟机可选择不保证 64 位数据类型的 `load`、`store`、`read` 和 `write` 这 4 个操作的原子性。由于这种非原子性，有可能导致其他线程读到同步未完成的“32 位的半个变量”的值。

不过实际开发中，Java 内存模型强烈建议虚拟机把 64 位数据的读写实现为具有原子性，目前各种平台下的商用虚拟机都选择把 64 位数据的读写操作作为原子操作来对待，因此我们在编写代码时一般不需要把用到的 `long` 和 `double` 变量专门声明为 `volatile`。

## 典型应用场景

### 场景一：使用 volatile 保证可见性

```java
volatile boolean running = true;
// 工作线程检查标志位
while (running) { doWork(); }
// 主线程停止
running = false;
```

### 场景二：双重检查锁定单例

```java
private static volatile Singleton instance;
public static Singleton getInstance() {
    if (instance == null) {
        synchronized (Singleton.class) {
            if (instance == null) instance = new Singleton();
        }
    }
    return instance;
}
```

### 场景三：使用 final 保证安全发布

```java
class Config {
    final Map<String, String> data; // final 保证构造完成后对其他线程可见
    Config() { data = loadConfig(); }
}
```

## 最佳实践

1. **优先使用 volatile 保护简单标志位**：比 synchronized 轻量。
2. **双重检查锁定必须加 volatile**：防止指令重排导致半初始化对象。
3. **使用 final 字段保证安全发布**：构造完成后不可变的对象。
4. **理解 happens-before 规则**：它是判断可见性的理论基础。

## 常见问题

### Q1：volatile 能保证原子性吗？

不能。`volatile` 只保证可见性和有序性。`volatile int count; count++` 仍然不是原子操作，需要使用 AtomicInteger。

### Q2：happens-before 规则有哪些？

程序顺序规则、锁规则、volatile 变量规则、线程启动/终止规则、中断规则、终结器规则、传递性。

### Q3：什么是指令重排序？

编译器和 CPU 可能重新排列指令顺序以优化性能，只要不改变单线程语义。多线程下可能导致意外行为，用 volatile 或 synchronized 禁止。

## 参考资料

- [《Java 并发编程实战》](https://book.douban.com/subject/10484692/)
- [《Java 并发编程的艺术》](https://book.douban.com/subject/26591326/)
- [《深入理解 Java 虚拟机》](https://book.douban.com/subject/34907497/)
- [极客时间教程 - Java 并发编程实战](https://time.geekbang.org/column/intro/100023901)
- [极客时间教程 - Java 业务开发常见错误 100 例](https://time.geekbang.org/column/intro/100047701)
- [理解 Java 内存模型](https://juejin.im/post/5bf2977751882505d840321d)
- [Java 并发编程：volatile 关键字解析](http://www.cnblogs.com/dolphin0520/p/3920373.html)
- [Java 并发编程：synchronized](http://www.cnblogs.com/dolphin0520/p/3923737.html)
- [深入理解 Java 并发之 synchronized 实现原理](https://blog.csdn.net/javazejian/article/details/72828483)
- [synchronized 实现原理及锁优化](https://nicky-chen.github.io/2018/05/14/synchronized-principle/)
- [Time, Clocks, and the Ordering of Events in a Distributed System](https://lamport.azurewebsites.net/pubs/time-clocks.pdf)，[译文](https://cloud.tencent.com/developer/article/1163428)，[解读](https://zhuanlan.zhihu.com/p/56146800) - Lamport 介绍 happened before、偏序关系（partial ordering）、逻辑时钟（Logical Clocks）概念，提出解决分布式系统中区分事件发生的时序问题的方法。
