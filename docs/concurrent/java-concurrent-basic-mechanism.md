# Java 并发基础机制

> Java 对于并发的支持主要汇聚在 `java.util.concurrent`，即 J.U.C。而 J.U.C 的核心是 AQS。

<!-- TOC depthFrom:2 depthTo:3 -->

- [concurrent 包的实现](#concurrent-包的实现)
- [synchronized](#synchronized)
  - [synchronized 的要点](#synchronized-的要点)
  - [synchronized 的原理](#synchronized-的原理)
- [volatile](#volatile)
  - [volatile 的要点](#volatile-的要点)
  - [volatile 的原理](#volatile-的原理)
  - [volatile 的应用场景](#volatile-的应用场景)
- [CAS](#cas)
  - [简介](#简介)
  - [操作](#操作)
  - [应用](#应用)
  - [原理](#原理)
  - [特点](#特点)
  - [总结](#总结)
- [ThreadLocal](#threadlocal)
- [资料](#资料)

<!-- /TOC -->

## concurrent 包的实现

由于 Java 的 CAS 同时具有 volatile 读和 volatile 写的内存语义，因此 Java 线程之间的通信现在有了下面四种方式：

1.  A 线程写 volatile 变量，随后 B 线程读这个 volatile 变量。
2.  A 线程写 volatile 变量，随后 B 线程用 CAS 更新这个 volatile 变量。
3.  A 线程用 CAS 更新一个 volatile 变量，随后 B 线程用 CAS 更新这个 volatile 变量。
4.  A 线程用 CAS 更新一个 volatile 变量，随后 B 线程读这个 volatile 变量。

同时，volatile 变量的读/写和 CAS 可以实现线程之间的通信。把这些特性整合在一起，就形成了整个 concurrent 包得以实现的基石。如果我们仔细分析 concurrent 包的源代码实现，会发现一个通用化的实现模式：

首先，声明共享变量为 volatile；

然后，使用 CAS 的原子条件更新来实现线程之间的同步；

同时，配合以 volatile 的读/写和 CAS 所具有的 volatile 读和写的内存语义来实现线程之间的通信。

AQS，非阻塞数据结构和原子变量类（Java.util.concurrent.atomic 包中的类），这些 concurrent 包中的基础类都是使用这种模式来实现的，而 concurrent 包中的高层类又是依赖于这些基础类来实现的。从整体来看，concurrent 包的实现示意图如下：

<p align="center">
  <img src="http://dunwu.test.upcdn.net/cs/java/concurrent/juc-architecture.png">
</p>

## synchronized

`synchronized` 是 Java 中的关键字，是利用锁的机制来实现同步的。 

**关键字 `synchronized` 可以保证在同一个时刻，只有一个线程可以执行某个方法或者某个代码块。**

### synchronized 的原理

**`synchronized` 是利用锁的机制来实现同步的**。

#### 锁的机制

锁具备以下两种特性：

- **互斥性**：即在同一时间只允许一个线程持有某个对象锁，通过这种特性来实现多线程中的协调机制，这样在同一时间只有一个线程对需同步的代码块(复合操作)进行访问。互斥性我们也往往称为操作的原子性。
- **可见性**：必须确保在锁被释放之前，对共享变量所做的修改，对于随后获得该锁的另一个线程是可见的（即在获得锁时应获得最新共享变量的值），否则另一个线程可能是在本地缓存的某个副本上继续操作从而引起不一致。

#### 对象锁

在 Java 中，每个对象都会有一个 monitor 对象，这个对象其实就是 Java 对象的锁，通常会被称为“内置锁”或“对象锁”。类的对象可以有多个，所以每个对象有其独立的对象锁，互不干扰。 

#### 类锁

在 Java 中，针对每个类也有一个锁，可以称为“类锁”，类锁实际上是通过对象锁实现的，即类的 Class 对象锁。每个类只有一个 Class 对象，所以每个类只有一个类锁。 

### synchronized 的用法

synchronized 有 3 种应用方式：

1.  同步实例方法 - 对于普通同步方法，锁是当前实例对象
2.  同步静态方法 - 对于静态同步方法，锁是当前类的 Class 对象
3.  同步代码块 - 对于同步方法块，锁是 Synchonized 括号里配置的对象

#### 同步实例方法

❌ 错误示例 - 未同步的示例

```java
public class NoSynchronizedDemo implements Runnable {

    public static final int MAX = 100000;

    private static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        NoSynchronizedDemo instance = new NoSynchronizedDemo();
        Thread t1 = new Thread(instance);
        Thread t2 = new Thread(instance);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(count);
    }

    @Override
    public void run() {
        for (int i = 0; i < MAX; i++) {
            increase();
        }
    }

    public void increase() {
        count++;
    }

}
// 输出结果: 小于 200000 的随机数字
```

Java 实例方法同步是同步在拥有该方法的对象上。这样，每个实例其方法同步都同步在不同的对象上，即该方法所属的实例。只有一个线程能够在实例方法同步块中运行。如果有多个实例存在，那么一个线程一次可以在一个实例同步块中执行操作。一个实例一个线程。

```java
public class SynchronizedDemo implements Runnable {

    private static final int MAX = 100000;

    private static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        SynchronizedDemo instance = new SynchronizedDemo();
        Thread t1 = new Thread(instance);
        Thread t2 = new Thread(instance);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(count);
    }

    @Override
    public void run() {
        for (int i = 0; i < MAX; i++) {
            increase();
        }
    }

    /**
     * synchronized 修饰普通方法
     */
    public synchronized void increase() {
        count++;
    }

}
```

#### 同步静态方法

静态方法的同步是指同步在该方法所在的类对象上。因为在 JVM 中一个类只能对应一个类对象，所以同时只允许一个线程执行同一个类中的静态同步方法。

对于不同类中的静态同步方法，一个线程可以执行每个类中的静态同步方法而无需等待。不管类中的那个静态同步方法被调用，一个类只能由一个线程同时执行。

```java
public class SynchronizedDemo2 implements Runnable {

    private static final int MAX = 100000;

    private static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        SynchronizedDemo2 instance = new SynchronizedDemo2();
        Thread t1 = new Thread(instance);
        Thread t2 = new Thread(instance);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(count);
    }

    @Override
    public void run() {
        for (int i = 0; i < MAX; i++) {
            increase();
        }
    }

    /**
     * synchronized 修饰静态方法
     */
    public synchronized static void increase() {
        count++;
    }

}
```

#### 同步代码块

有时你不需要同步整个方法，而是同步方法中的一部分。Java 可以对方法的一部分进行同步。

注意 Java 同步块构造器用括号将对象括起来。在上例中，使用了 `this`，即为调用 add 方法的实例本身。在同步构造器中用括号括起来的对象叫做监视器对象。上述代码使用监视器对象同步，同步实例方法使用调用方法本身的实例作为监视器对象。

一次只有一个线程能够在同步于同一个监视器对象的 Java 方法内执行。

```java
public class SynchronizedDemo3 implements Runnable {

    private static final int MAX = 100000;

    private static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        SynchronizedDemo3 instance = new SynchronizedDemo3();
        Thread t1 = new Thread(instance);
        Thread t2 = new Thread(instance);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(count);
    }

    @Override
    public void run() {
        for (int i = 0; i < MAX; i++) {
            increase();
        }
    }

    /**
     * synchronized 修饰代码块
     */
    public static void increase() {
        synchronized (SynchronizedDemo3.class) {
            count++;
        }
    }

}
```

### synchronized 的优化

#### 自旋锁

互斥同步进入阻塞状态的开销都很大，应该尽量避免。在许多应用中，共享数据的锁定状态只会持续很短的一段时间。自旋锁的思想是让一个线程在请求一个共享数据的锁时执行忙循环（自旋）一段时间，如果在这段时间内能获得锁，就可以避免进入阻塞状态。

自旋锁虽然能避免进入阻塞状态从而减少开销，但是它需要进行忙循环操作占用 CPU 时间，它只适用于共享数据的锁定状态很短的场景。

在 JDK 1.6 中引入了自适应的自旋锁。自适应意味着自旋的次数不再固定了，而是由前一次在同一个锁上的自旋次数及锁的拥有者的状态来决定。

#### 锁消除

锁消除是指对于被检测出不可能存在竞争的共享数据的锁进行消除。

锁消除主要是通过逃逸分析来支持，如果堆上的共享数据不可能逃逸出去被其它线程访问到，那么就可以把它们当成私有数据对待，也就可以将它们的锁进行消除。

对于一些看起来没有加锁的代码，其实隐式的加了很多锁。例如下面的字符串拼接代码就隐式加了锁：

```
public static String concatString(String s1, String s2, String s3) {
    return s1 + s2 + s3;
}
```

String 是一个不可变的类，编译器会对 String 的拼接自动优化。在 JDK 1.5 之前，会转化为 StringBuffer 对象的连续 append() 操作：

```
public static String concatString(String s1, String s2, String s3) {
    StringBuffer sb = new StringBuffer();
    sb.append(s1);
    sb.append(s2);
    sb.append(s3);
    return sb.toString();
}
```

每个 append() 方法中都有一个同步块。虚拟机观察变量 sb，很快就会发现它的动态作用域被限制在 concatString() 方法内部。也就是说，sb 的所有引用永远不会逃逸到 concatString() 方法之外，其他线程无法访问到它，因此可以进行消除。

#### 锁粗化

如果一系列的连续操作都对同一个对象反复加锁和解锁，频繁的加锁操作就会导致性能损耗。

上一节的示例代码中连续的 append() 方法就属于这类情况。如果虚拟机探测到由这样的一串零碎的操作都对同一个对象加锁，将会把加锁的范围扩展（粗化）到整个操作序列的外部。对于上一节的示例代码就是扩展到第一个 append() 操作之前直至最后一个 append() 操作之后，这样只需要加锁一次就可以了。

#### 轻量级锁

JDK 1.6 引入了偏向锁和轻量级锁，从而让锁拥有了四个状态：无锁状态（unlocked）、偏向锁状态（biasble）、轻量级锁状态（lightweight locked）和重量级锁状态（inflated）。

以下是 HotSpot 虚拟机对象头的内存布局，这些数据被称为 Mark Word。其中 tag bits 对应了五个状态，这些状态在右侧的 state 表格中给出。除了 marked for gc 状态，其它四个状态已经在前面介绍过了。

[![img](D:\Codes\ZPTutorial\images\snap\68747470733a2f2f63732d6e6f7465732d313235363130393739362e636f732e61702d6775616e677a686f752e6d7971636c6f75642e636f6d2f62623661343962652d303066322d346632372d613063652d3465643736346263363035632e706e67.png)](https://camo.githubusercontent.com/deab9b2c52091554cc095249fd7a97fc1ca41521/68747470733a2f2f63732d6e6f7465732d313235363130393739362e636f732e61702d6775616e677a686f752e6d7971636c6f75642e636f6d2f62623661343962652d303066322d346632372d613063652d3465643736346263363035632e706e67)



下图左侧是一个线程的虚拟机栈，其中有一部分称为 Lock Record 的区域，这是在轻量级锁运行过程创建的，用于存放锁对象的 Mark Word。而右侧就是一个锁对象，包含了 Mark Word 和其它信息。

[![img](D:\Codes\ZPTutorial\images\snap\68747470733a2f2f63732d6e6f7465732d313235363130393739362e636f732e61702d6775616e677a686f752e6d7971636c6f75642e636f6d2f30353165343336632d306534362d346335392d386636372d3532643839643635363138322e706e67.png)](https://camo.githubusercontent.com/c1a0307ca4be2bc4b5a3492c634553b4255759bd/68747470733a2f2f63732d6e6f7465732d313235363130393739362e636f732e61702d6775616e677a686f752e6d7971636c6f75642e636f6d2f30353165343336632d306534362d346335392d386636372d3532643839643635363138322e706e67)



轻量级锁是相对于传统的重量级锁而言，它使用 CAS 操作来避免重量级锁使用互斥量的开销。对于绝大部分的锁，在整个同步周期内都是不存在竞争的，因此也就不需要都使用互斥量进行同步，可以先采用 CAS 操作进行同步，如果 CAS 失败了再改用互斥量进行同步。

当尝试获取一个锁对象时，如果锁对象标记为 0 01，说明锁对象的锁未锁定（unlocked）状态。此时虚拟机在当前线程的虚拟机栈中创建 Lock Record，然后使用 CAS 操作将对象的 Mark Word 更新为 Lock Record 指针。如果 CAS 操作成功了，那么线程就获取了该对象上的锁，并且对象的 Mark Word 的锁标记变为 00，表示该对象处于轻量级锁状态。

[![img](D:\Codes\ZPTutorial\images\snap\68747470733a2f2f63732d6e6f7465732d313235363130393739362e636f732e61702d6775616e677a686f752e6d7971636c6f75642e636f6d2f62616161363831662d376335322d343139382d613561652d3330336239333836636634372e706e67.png)](https://camo.githubusercontent.com/740f6e8b42fda5eaca9b83b919a1729059653705/68747470733a2f2f63732d6e6f7465732d313235363130393739362e636f732e61702d6775616e677a686f752e6d7971636c6f75642e636f6d2f62616161363831662d376335322d343139382d613561652d3330336239333836636634372e706e67)



如果 CAS 操作失败了，虚拟机首先会检查对象的 Mark Word 是否指向当前线程的虚拟机栈，如果是的话说明当前线程已经拥有了这个锁对象，那就可以直接进入同步块继续执行，否则说明这个锁对象已经被其他线程线程抢占了。如果有两条以上的线程争用同一个锁，那轻量级锁就不再有效，要膨胀为重量级锁。

#### 偏向锁

偏向锁的思想是偏向于让第一个获取锁对象的线程，这个线程在之后获取该锁就不再需要进行同步操作，甚至连 CAS 操作也不再需要。

当锁对象第一次被线程获得的时候，进入偏向状态，标记为 1 01。同时使用 CAS 操作将线程 ID 记录到 Mark Word 中，如果 CAS 操作成功，这个线程以后每次进入这个锁相关的同步块就不需要再进行任何同步操作。

当有另外一个线程去尝试获取这个锁对象时，偏向状态就宣告结束，此时撤销偏向（Revoke Bias）后恢复到未锁定状态或者轻量级锁状态。

[![img](D:\Codes\ZPTutorial\images\snap\68747470733a2f2f63732d6e6f7465732d313235363130393739362e636f732e61702d6775616e677a686f752e6d7971636c6f75642e636f6d2f33393063393133622d356633312d343434662d626264622d3262383862363838653763652e6a7067.jpg)](https://camo.githubusercontent.com/8369c6bebffb9617694168381f5d7a62bb099e62/68747470733a2f2f63732d6e6f7465732d313235363130393739362e636f732e61702d6775616e677a686f752e6d7971636c6f75642e636f6d2f33393063393133622d356633312d343434662d626264622d3262383862363838653763652e6a7067)

## volatile

### volatile 的要点

volatile 是轻量级的 synchronized，它在多处理器开发中保证了共享变量的“可见性”。

可见性的意思是当一个线程修改一个共享变量时，另外一个线程能读到这个修改的值。

一旦一个共享变量（类的成员变量、类的静态成员变量）被 volatile 修饰之后，那么就具备了两层语义：

1.  保证了不同线程对这个变量进行操作时的可见性，即一个线程修改了某个变量的值，这新值对其他线程来说是立即可见的。
2.  禁止进行指令重排序。

如果一个字段被声明成 volatile，Java 线程内存模型确保所有线程看到这个变量的值是一致的。

### volatile 的原理

观察加入 volatile 关键字和没有加入 volatile 关键字时所生成的汇编代码发现，加入 volatile 关键字时，会多出一个 lock 前缀指令。

lock 前缀指令实际上相当于一个内存屏障（也成内存栅栏），内存屏障会提供 3 个功能：

- 它确保指令重排序时不会把其后面的指令排到内存屏障之前的位置，也不会把前面的指令排到内存屏障的后面；即在执行到内存屏障这句指令时，在它前面的操作已经全部完成；
- 它会强制将对缓存的修改操作立即写入主存；
- 如果是写操作，它会导致其他 CPU 中对应的缓存行无效。

### volatile 的应用场景

如果 volatile 变量修饰符使用恰当的话，它比 synchronized 的使用和执行成本更低，因为它不会引起线程上下文的切换和调度。

但是，volatile 无法替代 synchronized ，因为 volatile 无法保证操作的原子性。通常来说，使用 volatile 必须具备以下 2 个条件：

1.  对变量的写操作不依赖于当前值
2.  该变量没有包含在具有其他变量的不变式中

应用场景：

**状态标记量**

```java
volatile boolean flag = false;

while(!flag) {
    doSomething();
}

public void setFlag() {
    flag = true;
}
```

**double check**

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

> 👉 参考阅读：[Java 并发编程：volatile 关键字解析](https://www.cnblogs.com/dolphin0520/p/3920373.html)

## CAS

**互斥同步最主要的问题就是线程阻塞和唤醒所带来的性能问题**，因此这种同步也称为阻塞同步。

互斥同步属于一种悲观的并发策略，总是认为只要不去做正确的同步措施，那就肯定会出现问题。无论共享数据是否真的会出现竞争，它都要进行加锁（这里讨论的是概念模型，实际上虚拟机会优化掉很大一部分不必要的加锁）、用户态核心态转换、维护锁计数器和检查是否有被阻塞的线程需要唤醒等操作。

随着硬件指令集的发展，我们可以使用基于冲突检测的乐观并发策略：先进行操作，如果没有其它线程争用共享数据，那操作就成功了，否则采取补偿措施（不断地重试，直到成功为止）。这种乐观的并发策略的许多实现都不需要将线程阻塞，因此这种同步操作称为非阻塞同步。

乐观锁需要操作和冲突检测这两个步骤具备原子性，这里就不能再使用互斥同步来保证了，只能靠硬件来完成。硬件支持的原子性操作最典型的是：比较并交换（Compare-and-Swap，CAS）。CAS 指令需要有 3 个操作数，分别是内存地址 V、旧的预期值 A 和新值 B。当执行操作时，只有当 V 的值等于 A，才将 V 的值更新为 B。 

CAS（Compare and Swap），字面意思为比较并交换。CAS 有 3 个操作数，内存值 V，旧的预期值 A，要修改的新值 B。当且仅当预期值 A 和内存值 V 相同时，将内存值 V 修改为 B，否则什么都不做。

Java 代码如何确保处理器执行 CAS 操作？

CAS 通过调用 JNI（JNI:Java Native Interface 为 Java 本地调用，允许 Java 调用其他语言。）的代码实现的。JVM 将 CAS 操作编译为底层提供的最有效方法。在支持 CAS 的处理器上，JVM 将它们编译为相应的机器指令；在不支持 CAS 的处理器上，JVM 将使用自旋锁。

### 操作

我们常常做这样的操作

```Java
if(a==b) {
    a++;
}
```

试想一下如果在做 a++之前 a 的值被改变了怎么办？a++还执行吗？出现该问题的原因是在多线程环境下，a 的值处于一种不定的状态。采用锁可以解决此类问题，但 CAS 也可以解决，而且可以不加锁。

```Java
int expect = a;
if(a.compareAndSet(expect,a+1)) {
    doSomeThing1();
} else {
    doSomeThing2();
}
```

这样如果 a 的值被改变了 a++就不会被执行。按照上面的写法，a!=expect 之后,a++就不会被执行，如果我们还是想执行 a++操作怎么办，没关系，可以采用 while 循环

```Java
while(true) {
    int expect = a;
    if (a.compareAndSet(expect, a + 1)) {
        doSomeThing1();
        return;
    } else {
        doSomeThing2();
    }
}
```

采用上面的写法，在没有锁的情况下实现了 a++操作，这实际上是一种非阻塞算法。

### 应用

非阻塞算法 （nonblocking algorithms）

一个线程的失败或者挂起不应该影响其他线程的失败或挂起的算法。

现代的 CPU 提供了特殊的指令，可以自动更新共享数据，而且能够检测到其他线程的干扰，而 compareAndSet() 就用这些代替了锁定。

拿出 AtomicInteger 来研究在没有锁的情况下是如何做到数据正确性的。

```Java
private volatile int value;
```

首先毫无疑问，在没有锁的机制下可能需要借助 volatile 原语，保证线程间的数据是可见的（共享的）。

这样才获取变量的值的时候才能直接读取。

```Java
public final int get() {
    return value;
}
```

然后来看看++i 是怎么做到的。

```Java
public final int incrementAndGet() {
    for (;;) {
        int current = get();
        int next = current + 1;
            if (compareAndSet(current, next))
                return next;
    }
}
```

在这里采用了 CAS 操作，每次从内存中读取数据然后将此数据和+1 后的结果进行 CAS 操作，如果成功就返回结果，否则重试直到成功为止。

而 compareAndSet 利用 JNI 来完成 CPU 指令的操作。

```Java
public final boolean compareAndSet(int expect, int update) {
    return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
}
```

整体的过程就是这样子的，利用 CPU 的 CAS 指令，同时借助 JNI 来完成 Java 的非阻塞算法。其它原子操作都是利用类似的特性完成的。

其中 unsafe.compareAndSwapInt(this, valueOffset, expect, update)类似：

```Java
if (this == expect) {
    this = update
    return true;
} else {
    return false;
}
```

那么问题就来了，成功过程中需要 2 个步骤：比较 this == expect，替换 this = update，compareAndSwapInt 如何这两个步骤的原子性呢？ 参考 CAS 的原理

### 特点

#### 优点

一般情况下，比锁性能更高。因为 CAS 是一种非阻塞算法，所以其避免了线程被阻塞时的等待时间。

#### 缺点

##### ABA 问题

因为 CAS 需要在操作值的时候检查下值有没有发生变化，如果没有发生变化则更新，但是如果一个值原来是 A，变成了 B，又变成了 A，那么使用 CAS 进行检查时会发现它的值没有发生变化，但是实际上却变化了。ABA 问题的解决思路就是使用版本号。在变量前面追加上版本号，每次变量更新的时候把版本号加一，那么 A－B－A 就会变成 1A-2B－3A。

从 Java1.5 开始 JDK 的 atomic 包里提供了一个类 AtomicStampedReference 来解决 ABA 问题。这个类的 compareAndSet 方法作用是首先检查当前引用是否等于预期引用，并且当前标志是否等于预期标志，如果全部相等，则以原子方式将该引用和该标志的值设置为给定的更新值。

##### 循环时间长开销大

自旋 CAS 如果长时间不成功，会给 CPU 带来非常大的执行开销。如果 JVM 能支持处理器提供的 pause 指令那么效率会有一定的提升，pause 指令有两个作用，第一它可以延迟流水线执行指令（de-pipeline）,使 CPU 不会消耗过多的执行资源，延迟的时间取决于具体实现的版本，在一些处理器上延迟时间是零。第二它可以避免在退出循环的时候因内存顺序冲突（memory order violation）而引起 CPU 流水线被清空（CPU pipeline flush），从而提高 CPU 的执行效率。

比较花费 CPU 资源，即使没有任何用也会做一些无用功。

##### 只能保证一个共享变量的原子操作

当对一个共享变量执行操作时，我们可以使用循环 CAS 的方式来保证原子操作，但是对多个共享变量操作时，循环 CAS 就无法保证操作的原子性，这个时候就可以用锁，或者有一个取巧的办法，就是把多个共享变量合并成一个共享变量来操作。比如有两个共享变量 i ＝ 2,j=a，合并一下 ij=2a，然后用 CAS 来操作 ij。从 Java1.5 开始 JDK 提供了 AtomicReference 类来保证引用对象之间的原子性，你可以把多个变量放在一个对象里来进行 CAS 操作。

### 总结

可以用 CAS 在无锁的情况下实现原子操作，但要明确应用场合，非常简单的操作且又不想引入锁可以考虑使用 CAS 操作，当想要非阻塞地完成某一操作也可以考虑 CAS。不推荐在复杂操作中引入 CAS，会使程序可读性变差，且难以测试，同时会出现 ABA 问题。

## ThreadLocal

ThreadLocal，很多地方叫做线程本地变量，也有些地方叫做线程本地存储，其实意思差不多。可能很多朋友都知道 ThreadLocal 为变量在每个线程中都创建了一个副本，那么每个线程可以访问自己内部的副本变量。

#### 源码

ThreadLocal 的主要方法：

```java
public class ThreadLocal<T> {
    public T get() {}
	public void remove() {}
	public void set(T value) {}
	public static <S> ThreadLocal<S> withInitial(Supplier<? extends S> supplier) {}
}
```

- get()方法是用来获取 ThreadLocal 在当前线程中保存的变量副本。
- set()用来设置当前线程中变量的副本。
- remove()用来移除当前线程中变量的副本。
- initialValue()是一个 protected 方法，一般是用来在使用时进行覆写的，它是一个延迟加载方法，下面会详细说明。

##### get() 源码实现

**get 源码**

```java
public T get() {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null) {
        ThreadLocalMap.Entry e = map.getEntry(this);
        if (e != null) {
            @SuppressWarnings("unchecked")
            T result = (T)e.value;
            return result;
        }
    }
    return setInitialValue();
}
```

1.  取得当前线程。
2.  通过 getMap() 方法获取 ThreadLocalMap。
3.  成功，返回 value；失败，返回 setInitialValue()。

##### ThreadLocalMap 源码实现

**ThreadLocalMap 源码**

ThreadLocalMap 是 ThreadLocal 的一个内部类。

ThreadLocalMap 的 Entry 继承了 WeakReference，并且使用 ThreadLocal 作为键值。

##### setInitialValue 源码实现

```java
private T setInitialValue() {
    T value = initialValue();
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null)
        map.set(this, value);
    else
        createMap(t, value);
    return value;
}
```

如果 map 不为空，就设置键值对；为空，再创建 Map，看一下 createMap 的实现：

```java
void createMap(Thread t, T firstValue) {
    t.threadLocals = new ThreadLocalMap(this, firstValue);
}
```

##### ThreadLocal 源码小结

至此，可能大部分朋友已经明白了 ThreadLocal 是如何为每个线程创建变量的副本的：

1.  在每个线程 Thread 内部有一个 ThreadLocal.ThreadLocalMap 类型的成员变量 threadLocals，这个 threadLocals 就是用来存储实际的变量副本的，键值为当前 ThreadLocal 变量，value 为变量副本（即 T 类型的变量）。
2.  在 Thread 里面，threadLocals 为空，当通过 ThreadLocal 变量调用 get()方法或者 set()方法，就会对 Thread 类中的 threadLocals 进行初始化，并且以当前 ThreadLocal 变量为键值，以 ThreadLocal 要保存的副本变量为 value，存到 threadLocals。
3.  在当前线程里面，如果要使用副本变量，就可以通过 get 方法在 threadLocals 里面查找。

#### 示例

ThreadLocal 最常见的应用场景为用于解决数据库连接、Session 管理等问题。

示例 - 数据库连接

```java
private static ThreadLocal<Connection> connectionHolder
= new ThreadLocal<Connection>() {
public Connection initialValue() {
    return DriverManager.getConnection(DB_URL);
}
};

public static Connection getConnection() {
return connectionHolder.get();
}
```

示例 - Session 管理

```java
private static final ThreadLocal threadSession = new ThreadLocal();

public static Session getSession() throws InfrastructureException {
    Session s = (Session) threadSession.get();
    try {
        if (s == null) {
            s = getSessionFactory().openSession();
            threadSession.set(s);
        }
    } catch (HibernateException ex) {
        throw new InfrastructureException(ex);
    }
    return s;
}
```

## 参考资料

- [《Java 并发编程实战》](https://item.jd.com/10922250.html)
- [《Java 并发编程的艺术》](https://item.jd.com/11740734.html)
- [Java 并发编程：volatile 关键字解析](http://www.cnblogs.com/dolphin0520/p/3920373.html)
- [Java 并发编程：synchronized](http://www.cnblogs.com/dolphin0520/p/3923737.html)
- [深入理解 Java 并发之 synchronized 实现原理](https://blog.csdn.net/javazejian/article/details/72828483)
- https://www.jianshu.com/p/473e14d5ab2d
- https://blog.csdn.net/ls5718/article/details/52563959
- http://tutorials.jenkov.com/java-concurrency/non-blocking-algorithms.html
- [synchronized 实现原理及锁优化](https://nicky-chen.github.io/2018/05/14/synchronized-principle/)
