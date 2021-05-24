# Java 并发核心机制

> **📦 本文以及示例源码已归档在 [javacore](https://github.com/dunwu/javacore/)**
>
> Java 对于并发的支持主要汇聚在 `java.util.concurrent`，即 J.U.C。而 J.U.C 的核心是 `AQS`。

<!-- TOC depthFrom:2 depthTo:3 -->

- [1. J.U.C 简介](#1-juc-简介)
- [2. synchronized](#2-synchronized)
  - [2.1. synchronized 的应用](#21-synchronized-的应用)
  - [2.2. synchronized 的原理](#22-synchronized-的原理)
  - [2.3. synchronized 的优化](#23-synchronized-的优化)
  - [2.4. synchronized 的误区](#24-synchronized-的误区)
- [3. volatile](#3-volatile)
  - [3.1. volatile 的要点](#31-volatile-的要点)
  - [3.2. volatile 的应用](#32-volatile-的应用)
  - [3.3. volatile 的原理](#33-volatile-的原理)
  - [3.4. volatile 的问题](#34-volatile-的问题)
- [4. CAS](#4-cas)
  - [4.1. CAS 的要点](#41-cas-的要点)
  - [4.2. CAS 的应用](#42-cas-的应用)
  - [4.3. CAS 的原理](#43-cas-的原理)
  - [4.4. CAS 的问题](#44-cas-的问题)
- [5. ThreadLocal](#5-threadlocal)
  - [5.1. ThreadLocal 的应用](#51-threadlocal-的应用)
  - [5.2. ThreadLocal 的原理](#52-threadlocal-的原理)
  - [5.3. ThreadLocal 的误区](#53-threadlocal-的误区)
  - [5.4. InheritableThreadLocal](#54-inheritablethreadlocal)
- [6. 参考资料](#6-参考资料)

<!-- /TOC -->

## 1. J.U.C 简介

Java 的 `java.util.concurrent` 包（简称 J.U.C）中提供了大量并发工具类，是 Java 并发能力的主要体现（注意，不是全部，有部分并发能力的支持在其他包中）。从功能上，大致可以分为：

- 原子类 - 如：`AtomicInteger`、`AtomicIntegerArray`、`AtomicReference`、`AtomicStampedReference` 等。
- 锁 - 如：`ReentrantLock`、`ReentrantReadWriteLock` 等。
- 并发容器 - 如：`ConcurrentHashMap`、`CopyOnWriteArrayList`、`CopyOnWriteArraySet` 等。
- 阻塞队列 - 如：`ArrayBlockingQueue`、`LinkedBlockingQueue` 等。
- 非阻塞队列 - 如： `ConcurrentLinkedQueue` 、`LinkedTransferQueue` 等。
- `Executor` 框架（线程池）- 如：`ThreadPoolExecutor`、`Executors` 等。

我个人理解，Java 并发框架可以分为以下层次。

![img](https://raw.githubusercontent.com/dunwu/images/dev/cs/java/javacore/concurrent/java-concurrent-basic-mechanism.png)

由 Java 并发框架图不难看出，J.U.C 包中的工具类是基于 `synchronized`、`volatile`、`CAS`、`ThreadLocal` 这样的并发核心机制打造的。所以，要想深入理解 J.U.C 工具类的特性、为什么具有这样那样的特性，就必须先理解这些核心机制。

## 2. synchronized

> `synchronized` 是 Java 中的关键字，是 **利用锁的机制来实现互斥同步的**。
>
> **`synchronized` 可以保证在同一个时刻，只有一个线程可以执行某个方法或者某个代码块**。
>
> 如果不需要 `Lock` 、`ReadWriteLock` 所提供的高级同步特性，应该优先考虑使用 `synchronized` ，理由如下：
>
> - Java 1.6 以后，`synchronized` 做了大量的优化，其性能已经与 `Lock` 、`ReadWriteLock` 基本上持平。从趋势来看，Java 未来仍将继续优化 `synchronized` ，而不是 `ReentrantLock` 。
> - `ReentrantLock` 是 Oracle JDK 的 API，在其他版本的 JDK 中不一定支持；而 `synchronized` 是 JVM 的内置特性，所有 JDK 版本都提供支持。

### 2.1. synchronized 的应用

`synchronized` 有 3 种应用方式：

- **同步实例方法** - 对于普通同步方法，锁是当前实例对象
- **同步静态方法** - 对于静态同步方法，锁是当前类的 `Class` 对象
- **同步代码块** - 对于同步方法块，锁是 `synchonized` 括号里配置的对象

> 说明：
>
> 类似 `Vector`、`Hashtable` 这类同步类，就是使用 `synchonized` 修饰其重要方法，来保证其线程安全。
>
> 事实上，这类同步容器也非绝对的线程安全，当执行迭代器遍历，根据条件删除元素这种场景下，就可能出现线程不安全的情况。此外，Java 1.6 针对 `synchonized` 进行优化前，由于阻塞，其性能不高。
>
> 综上，这类同步容器，在现代 Java 程序中，已经渐渐不用了。

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

![img](https://raw.githubusercontent.com/dunwu/images/dev/snap/20200701135257.png)

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

### 2.2. synchronized 的原理

**`synchronized` 代码块是由一对 `monitorenter` 和 `monitorexit` 指令实现的，`Monitor` 对象是同步的基本实现单元**。在 Java 6 之前，`Monitor` 的实现完全是依靠操作系统内部的互斥锁，因为需要进行用户态到内核态的切换，所以同步操作是一个无差别的重量级操作。

如果 `synchronized` 明确制定了对象参数，那就是这个对象的引用；如果没有明确指定，那就根据 `synchronized` 修饰的是实例方法还是静态方法，去对对应的对象实例或 `Class` 对象来作为锁对象。

`synchronized` 同步块对同一线程来说是可重入的，不会出现锁死问题。

`synchronized` 同步块是互斥的，即已进入的线程执行完成前，会阻塞其他试图进入的线程。

【示例】

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

#### 同步代码块

`synchronized` 在修饰同步代码块时，是由 `monitorenter` 和 `monitorexit` 指令来实现同步的。进入 `monitorenter` 指令后，线程将持有 `Monitor` 对象，退出 `monitorenter` 指令后，线程将释放该 `Monitor` 对象。

#### 同步方法

`synchronized` 修饰同步方法时，会设置一个 `ACC_SYNCHRONIZED` 标志。当方法调用时，调用指令将会检查该方法是否被设置 `ACC_SYNCHRONIZED` 访问标志。如果设置了该标志，执行线程将先持有 `Monitor` 对象，然后再执行方法。在该方法运行期间，其它线程将无法获取到该 `Mointor` 对象，当方法执行完成后，再释放该 `Monitor` 对象。

#### Monitor

每个对象实例都会有一个 `Monitor`，`Monitor` 可以和对象一起创建、销毁。`Monitor` 是由 `ObjectMonitor` 实现，而 `ObjectMonitor` 是由 C++ 的 `ObjectMonitor.hpp` 文件实现。

当多个线程同时访问一段同步代码时，多个线程会先被存放在 EntryList 集合中，处于 block 状态的线程，都会被加入到该列表。接下来当线程获取到对象的 Monitor 时，Monitor 是依靠底层操作系统的 Mutex Lock 来实现互斥的，线程申请 Mutex 成功，则持有该 Mutex，其它线程将无法获取到该 Mutex。

如果线程调用 wait() 方法，就会释放当前持有的 Mutex，并且该线程会进入 WaitSet 集合中，等待下一次被唤醒。如果当前线程顺利执行完方法，也将释放 Mutex。

### 2.3. synchronized 的优化

> **Java 1.6 以后，`synchronized` 做了大量的优化，其性能已经与 `Lock` 、`ReadWriteLock` 基本上持平**。

#### Java 对象头

在 JDK1.6 JVM 中，对象实例在堆内存中被分为了三个部分：对象头、实例数据和对齐填充。其中 Java 对象头由 Mark Word、指向类的指针以及数组长度三部分组成。

Mark Word 记录了对象和锁有关的信息。Mark Word 在 64 位 JVM 中的长度是 64bit，我们可以一起看下 64 位 JVM 的存储结构是怎么样的。如下图所示：

![img](https://raw.githubusercontent.com/dunwu/images/dev/snap/20200629191250.png)

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

![img](https://raw.githubusercontent.com/dunwu/images/dev/snap/20200604105151.png)

#### 轻量级锁

**轻量级锁**是相对于传统的重量级锁而言，它 **使用 CAS 操作来避免重量级锁使用互斥量的开销**。对于绝大部分的锁，在整个同步周期内都是不存在竞争的，因此也就不需要都使用互斥量进行同步，可以先采用 CAS 操作进行同步，如果 CAS 失败了再改用互斥量进行同步。

当尝试获取一个锁对象时，如果锁对象标记为 `0|01`，说明锁对象的锁未锁定（unlocked）状态。此时虚拟机在当前线程的虚拟机栈中创建 Lock Record，然后使用 CAS 操作将对象的 Mark Word 更新为 Lock Record 指针。如果 CAS 操作成功了，那么线程就获取了该对象上的锁，并且对象的 Mark Word 的锁标记变为 00，表示该对象处于轻量级锁状态。

![img](https://raw.githubusercontent.com/dunwu/images/dev/snap/20200604105248.png)

#### 锁消除 / 锁粗化

除了锁升级优化，Java 还使用了编译器对锁进行优化。

**（1）锁消除**

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

**（2）锁粗化**

锁粗化同理，就是在 JIT 编译器动态编译时，如果发现几个相邻的同步块使用的是同一个锁实例，那么 JIT 编译器将会把这几个同步块合并为一个大的同步块，从而避免一个线程“反复申请、释放同一个锁“所带来的性能开销。

如果**一系列的连续操作都对同一个对象反复加锁和解锁**，频繁的加锁操作就会导致性能损耗。

上一节的示例代码中连续的 `append()` 方法就属于这类情况。如果**虚拟机探测到由这样的一串零碎的操作都对同一个对象加锁，将会把加锁的范围扩展（粗化）到整个操作序列的外部**。对于上一节的示例代码就是扩展到第一个 `append()` 操作之前直至最后一个 `append()` 操作之后，这样只需要加锁一次就可以了。

#### 自旋锁

互斥同步进入阻塞状态的开销都很大，应该尽量避免。在许多应用中，共享数据的锁定状态只会持续很短的一段时间。自旋锁的思想是让一个线程在请求一个共享数据的锁时执行忙循环（自旋）一段时间，如果在这段时间内能获得锁，就可以避免进入阻塞状态。

自旋锁虽然能避免进入阻塞状态从而减少开销，但是它需要进行忙循环操作占用 CPU 时间，它只适用于共享数据的锁定状态很短的场景。

在 Java 1.6 中引入了自适应的自旋锁。自适应意味着自旋的次数不再固定了，而是由前一次在同一个锁上的自旋次数及锁的拥有者的状态来决定。

### 2.4. synchronized 的误区

> 示例摘自：[《Java 业务开发常见错误 100 例》](https://time.geekbang.org/column/intro/100047701)

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
            //a始终等于b吗？
            if (a < b) {
                log.info("a:{},b:{},{}", a, b, a > b);
                //最后的a>b应该始终是false吗？
            }
        }
        log.info("compare done");
    }

}
```

【输出】

```
16:05:25.541 [Thread-0] INFO io.github.dunwu.javacore.concurrent.sync.synchronized使用范围不当 - add start
16:05:25.544 [Thread-0] INFO io.github.dunwu.javacore.concurrent.sync.synchronized使用范围不当 - add done
16:05:25.544 [Thread-1] INFO io.github.dunwu.javacore.concurrent.sync.synchronized使用范围不当 - compare start
16:05:25.544 [Thread-1] INFO io.github.dunwu.javacore.concurrent.sync.synchronized使用范围不当 - compare done
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
public class synchronized错误使用示例2 {

    public static void main(String[] args) {
        synchronized错误使用示例2 demo = new synchronized错误使用示例2();
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
public class synchronized锁粒度不当 {

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

## 3. volatile

### 3.1. volatile 的要点

`volatile` 是轻量级的 `synchronized`，它在多处理器开发中保证了共享变量的“可见性”。

被 `volatile` 修饰的变量，具备以下特性：

- **线程可见性** - 保证了不同线程对这个变量进行操作时的可见性，即一个线程修改了某个共享变量，另外一个线程能读到这个修改的值。
- **禁止指令重排序**
- **不保证原子性**

我们知道，线程安全需要具备：可见性、原子性、顺序性。`volatile` 不保证原子性，所以决定了它不能彻底地保证线程安全。

### 3.2. volatile 的应用

如果 `volatile` 变量修饰符使用恰当的话，它比 `synchronized` 的使用和执行成本更低，因为它不会引起线程上下文的切换和调度。但是，**`volatile` 无法替代 `synchronized` ，因为 `volatile` 无法保证操作的原子性**。

通常来说，**使用 `volatile` 必须具备以下 2 个条件**：

- **对变量的写操作不依赖于当前值**
- **该变量没有包含在具有其他变量的表达式中**

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

### 3.3. volatile 的原理

观察加入 volatile 关键字和没有加入 volatile 关键字时所生成的汇编代码发现，**加入 `volatile` 关键字时，会多出一个 `lock` 前缀指令**。**`lock` 前缀指令实际上相当于一个内存屏障**（也成内存栅栏），内存屏障会提供 3 个功能：

- 它确保指令重排序时不会把其后面的指令排到内存屏障之前的位置，也不会把前面的指令排到内存屏障的后面；即在执行到内存屏障这句指令时，在它前面的操作已经全部完成；
- 它会强制将对缓存的修改操作立即写入主存；
- 如果是写操作，它会导致其他 CPU 中对应的缓存行无效。

### 3.4. volatile 的问题

`volatile` 的要点中，已经提到，**`volatile` 不保证原子性，所以 volatile 并不能保证线程安全**。

那么，如何做到线程安全呢？有两种方案：

- `volatile` + `synchronized` - 可以参考：【示例】双重锁实现线程安全的单例模式
- 使用原子类替代 `volatile`

## 4. CAS

### 4.1. CAS 的要点

互斥同步是最常见的并发正确性保障手段。

**互斥同步最主要的问题是线程阻塞和唤醒所带来的性能问题**，因此互斥同步也被称为阻塞同步。互斥同步属于一种悲观的并发策略，总是认为只要不去做正确的同步措施，那就肯定会出现问题。无论共享数据是否真的会出现竞争，它都要进行加锁（这里讨论的是概念模型，实际上虚拟机会优化掉很大一部分不必要的加锁）、用户态核心态转换、维护锁计数器和检查是否有被阻塞的线程需要唤醒等操作。

随着硬件指令集的发展，我们可以使用基于冲突检测的乐观并发策略：先进行操作，如果没有其它线程争用共享数据，那操作就成功了，否则采取补偿措施（不断地重试，直到成功为止）。这种乐观的并发策略的许多实现都不需要将线程阻塞，因此这种同步操作称为非阻塞同步。

为什么说乐观锁需要 **硬件指令集的发展** 才能进行？因为需要操作和冲突检测这两个步骤具备原子性。而这点是由硬件来完成，如果再使用互斥同步来保证就失去意义了。硬件支持的原子性操作最典型的是：CAS。

**CAS（Compare and Swap），字面意思为比较并交换。CAS 有 3 个操作数，分别是：内存值 M，期望值 E，更新值 U。当且仅当内存值 M 和期望值 E 相等时，将内存值 M 修改为 U，否则什么都不做**。

### 4.2. CAS 的应用

**CAS 只适用于线程冲突较少的情况**。

CAS 的典型应用场景是：

- 原子类
- 自旋锁

#### 原子类

> 原子类是 CAS 在 Java 中最典型的应用。

我们先来看一个常见的代码片段。

```Java
if(a==b) {
    a++;
}
```

如果 `a++` 执行前， a 的值被修改了怎么办？还能得到预期值吗？出现该问题的原因是在并发环境下，以上代码片段不是原子操作，随时可能被其他线程所篡改。

解决这种问题的最经典方式是应用原子类的 `incrementAndGet` 方法。

```Java
public class AtomicIntegerDemo {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        final AtomicInteger count = new AtomicInteger(0);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    count.incrementAndGet();
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(3, TimeUnit.SECONDS);
        System.out.println("Final Count is : " + count.get());
    }

}
```

J.U.C 包中提供了 `AtomicBoolean`、`AtomicInteger`、`AtomicLong` 分别针对 `Boolean`、`Integer`、`Long` 执行原子操作，操作和上面的示例大体相似，不做赘述。

#### 自旋锁

利用原子类（本质上是 CAS），可以实现自旋锁。

所谓自旋锁，是指线程反复检查锁变量是否可用，直到成功为止。由于线程在这一过程中保持执行，因此是一种忙等待。一旦获取了自旋锁，线程会一直保持该锁，直至显式释放自旋锁。

示例：非线程安全示例

```java
public class AtomicReferenceDemo {

    private static int ticket = 10;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 5; i++) {
            executorService.execute(new MyThread());
        }
        executorService.shutdown();
    }

    static class MyThread implements Runnable {

        @Override
        public void run() {
            while (ticket > 0) {
                System.out.println(Thread.currentThread().getName() + " 卖出了第 " + ticket + " 张票");
                ticket--;
            }
        }

    }

}
```

输出结果：

```
pool-1-thread-2 卖出了第 10 张票
pool-1-thread-1 卖出了第 10 张票
pool-1-thread-3 卖出了第 10 张票
pool-1-thread-1 卖出了第 8 张票
pool-1-thread-2 卖出了第 9 张票
pool-1-thread-1 卖出了第 6 张票
pool-1-thread-3 卖出了第 7 张票
pool-1-thread-1 卖出了第 4 张票
pool-1-thread-2 卖出了第 5 张票
pool-1-thread-1 卖出了第 2 张票
pool-1-thread-3 卖出了第 3 张票
pool-1-thread-2 卖出了第 1 张票
```

很明显，出现了重复售票的情况。

【示例】使用自旋锁来保证线程安全

可以通过自旋锁这种非阻塞同步来保证线程安全，下面使用 `AtomicReference` 来实现一个自旋锁。

```java
public class AtomicReferenceDemo2 {

    private static int ticket = 10;

    public static void main(String[] args) {
        threadSafeDemo();
    }

    private static void threadSafeDemo() {
        SpinLock lock = new SpinLock();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 5; i++) {
            executorService.execute(new MyThread(lock));
        }
        executorService.shutdown();
    }

    static class SpinLock {

        private AtomicReference<Thread> atomicReference = new AtomicReference<>();

        public void lock() {
            Thread current = Thread.currentThread();
            while (!atomicReference.compareAndSet(null, current)) {}
        }

        public void unlock() {
            Thread current = Thread.currentThread();
            atomicReference.compareAndSet(current, null);
        }

    }

    static class MyThread implements Runnable {

        private SpinLock lock;

        public MyThread(SpinLock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            while (ticket > 0) {
                lock.lock();
                if (ticket > 0) {
                    System.out.println(Thread.currentThread().getName() + " 卖出了第 " + ticket + " 张票");
                    ticket--;
                }
                lock.unlock();
            }
        }

    }

}
```

输出结果：

```
pool-1-thread-2 卖出了第 10 张票
pool-1-thread-1 卖出了第 9 张票
pool-1-thread-3 卖出了第 8 张票
pool-1-thread-2 卖出了第 7 张票
pool-1-thread-3 卖出了第 6 张票
pool-1-thread-1 卖出了第 5 张票
pool-1-thread-2 卖出了第 4 张票
pool-1-thread-1 卖出了第 3 张票
pool-1-thread-3 卖出了第 2 张票
pool-1-thread-1 卖出了第 1 张票
```

### 4.3. CAS 的原理

Java 主要利用 `Unsafe` 这个类提供的 CAS 操作。`Unsafe` 的 CAS 依赖的是 JVM 针对不同的操作系统实现的硬件指令 **`Atomic::cmpxchg`**。`Atomic::cmpxchg` 的实现使用了汇编的 CAS 操作，并使用 CPU 提供的 `lock` 信号保证其原子性。

### 4.4. CAS 的问题

一般情况下，CAS 比锁性能更高。因为 CAS 是一种非阻塞算法，所以其避免了线程阻塞和唤醒的等待时间。

但是，事物总会有利有弊，CAS 也存在三大问题：

- ABA 问题
- 循环时间长开销大
- 只能保证一个共享变量的原子性

#### ABA 问题

**如果一个变量初次读取的时候是 A 值，它的值被改成了 B，后来又被改回为 A，那 CAS 操作就会误认为它从来没有被改变过**。

J.U.C 包提供了一个带有标记的**原子引用类 `AtomicStampedReference` 来解决这个问题**，它可以通过控制变量值的版本来保证 CAS 的正确性。大部分情况下 ABA 问题不会影响程序并发的正确性，如果需要解决 ABA 问题，改用**传统的互斥同步可能会比原子类更高效**。

#### 循环时间长开销大

**自旋 CAS （不断尝试，直到成功为止）如果长时间不成功，会给 CPU 带来非常大的执行开销**。

如果 JVM 能支持处理器提供的 `pause` 指令那么效率会有一定的提升，`pause` 指令有两个作用：

- 它可以延迟流水线执行指令（de-pipeline）,使 CPU 不会消耗过多的执行资源，延迟的时间取决于具体实现的版本，在一些处理器上延迟时间是零。
- 它可以避免在退出循环的时候因内存顺序冲突（memory order violation）而引起 CPU 流水线被清空（CPU pipeline flush），从而提高 CPU 的执行效率。

比较花费 CPU 资源，即使没有任何用也会做一些无用功。

#### 只能保证一个共享变量的原子性

当对一个共享变量执行操作时，我们可以使用循环 CAS 的方式来保证原子操作，但是对多个共享变量操作时，循环 CAS 就无法保证操作的原子性，这个时候就可以用锁。

或者有一个取巧的办法，就是把多个共享变量合并成一个共享变量来操作。比如有两个共享变量 `i ＝ 2, j = a`，合并一下 `ij=2a`，然后用 CAS 来操作 `ij`。从 Java 1.5 开始 JDK 提供了 `AtomicReference` 类来保证引用对象之间的原子性，你可以把多个变量放在一个对象里来进行 CAS 操作。

## 5. ThreadLocal

> **`ThreadLocal` 是一个存储线程本地副本的工具类**。
>
> 要保证线程安全，不一定非要进行同步。同步只是保证共享数据争用时的正确性，如果一个方法本来就不涉及共享数据，那么自然无须同步。
>
> Java 中的 **无同步方案** 有：
>
> - **可重入代码** - 也叫纯代码。如果一个方法，它的 **返回结果是可以预测的**，即只要输入了相同的数据，就能返回相同的结果，那它就满足可重入性，当然也是线程安全的。
> - **线程本地存储** - 使用 **`ThreadLocal` 为共享变量在每个线程中都创建了一个本地副本**，这个副本只能被当前线程访问，其他线程无法访问，那么自然是线程安全的。

### 5.1. ThreadLocal 的应用

`ThreadLocal` 的方法：

```java
public class ThreadLocal<T> {
    public T get() {}
    public void set(T value) {}
    public void remove() {}
    public static <S> ThreadLocal<S> withInitial(Supplier<? extends S> supplier) {}
}
```

> 说明：
>
> - `get` - 用于获取 `ThreadLocal` 在当前线程中保存的变量副本。
> - `set` - 用于设置当前线程中变量的副本。
> - `remove` - 用于删除当前线程中变量的副本。如果此线程局部变量随后被当前线程读取，则其值将通过调用其 `initialValue` 方法重新初始化，除非其值由中间线程中的当前线程设置。 这可能会导致当前线程中多次调用 `initialValue` 方法。
> - `initialValue` - 为 ThreadLocal 设置默认的 `get` 初始值，需要重写 `initialValue` 方法 。

`ThreadLocal` 常用于防止对可变的单例（Singleton）变量或全局变量进行共享。典型应用场景有：管理数据库连接、Session。

【示例】数据库连接

```java
private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>() {
    @Override
    public Connection initialValue() {
        return DriverManager.getConnection(DB_URL);
    }
};

public static Connection getConnection() {
    return connectionHolder.get();
}
```

【示例】Session 管理

```java
private static final ThreadLocal<Session> sessionHolder = new ThreadLocal<>();

public static Session getSession() {
    Session session = (Session) sessionHolder.get();
    try {
        if (session == null) {
            session = createSession();
            sessionHolder.set(session);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return session;
}
```

【示例】完整使用 `ThreadLocal` 示例

```java
public class ThreadLocalDemo {

    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new MyThread());
        }
        executorService.shutdown();
    }

    static class MyThread implements Runnable {

        @Override
        public void run() {
            int count = threadLocal.get();
            for (int i = 0; i < 10; i++) {
                try {
                    count++;
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            threadLocal.set(count);
            threadLocal.remove();
            System.out.println(Thread.currentThread().getName() + " : " + count);
        }

    }

}
```

全部输出 count = 10

### 5.2. ThreadLocal 的原理

#### 存储结构

**`Thread` 类中维护着一个 `ThreadLocal.ThreadLocalMap` 类型的成员** `threadLocals`。这个成员就是用来存储当前线程独占的变量副本。

`ThreadLocalMap` 是 `ThreadLocal` 的内部类，它维护着一个 `Entry` 数组，**`Entry` 继承了 `WeakReference`** ，所以是弱引用。 `Entry` 用于保存键值对，其中：

- `key` 是 `ThreadLocal` 对象；
- `value` 是传递进来的对象（变量副本）。

```java
public class Thread implements Runnable {
    // ...
    ThreadLocal.ThreadLocalMap threadLocals = null;
    // ...
}

static class ThreadLocalMap {
    // ...
    static class Entry extends WeakReference<ThreadLocal<?>> {
        /** The value associated with this ThreadLocal. */
        Object value;

        Entry(ThreadLocal<?> k, Object v) {
            super(k);
            value = v;
        }
    }
    // ...
}
```

#### 如何解决 Hash 冲突

`ThreadLocalMap` 虽然是类似 `Map` 结构的数据结构，但它并没有实现 `Map` 接口。它不支持 `Map` 接口中的 `next` 方法，这意味着 `ThreadLocalMap` 中解决 Hash 冲突的方式并非 **拉链表** 方式。

实际上，**`ThreadLocalMap` 采用线性探测的方式来解决 Hash 冲突**。所谓线性探测，就是根据初始 key 的 hashcode 值确定元素在 table 数组中的位置，如果发现这个位置上已经被其他的 key 值占用，则利用固定的算法寻找一定步长的下个位置，依次判断，直至找到能够存放的位置。

#### 内存泄漏问题

`ThreadLocalMap` 的 `Entry` 继承了 `WeakReference`，所以它的 **key （`ThreadLocal` 对象）是弱引用，而 value （变量副本）是强引用**。

- 如果 `ThreadLocal` 对象没有外部强引用来引用它，那么 `ThreadLocal` 对象会在下次 GC 时被回收。
- 此时，`Entry` 中的 key 已经被回收，但是 value 由于是强引用不会被垃圾收集器回收。如果创建 `ThreadLocal` 的线程一直持续运行，那么 value 就会一直得不到回收，产生**内存泄露**。

那么如何避免内存泄漏呢？方法就是：**使用 `ThreadLocal` 的 `set` 方法后，显示的调用 `remove` 方法** 。

```java
ThreadLocal<String> threadLocal = new ThreadLocal();
try {
    threadLocal.set("xxx");
    // ...
} finally {
    threadLocal.remove();
}
```

### 5.3. ThreadLocal 的误区

> 示例摘自：[《Java 业务开发常见错误 100 例》](https://time.geekbang.org/column/intro/100047701)

ThreadLocal 适用于变量在线程间隔离，而在方法或类间共享的场景。

前文提到，ThreadLocal 是线程隔离的，那么是不是使用 ThreadLocal 就一定高枕无忧呢？

#### ThreadLocal 错误案例

使用 Spring Boot 创建一个 Web 应用程序，使用 ThreadLocal 存放一个 Integer 的值，来暂且代表需要在线程中保存的用户信息，这个值初始是 null。

```java
    private ThreadLocal<Integer> currentUser = ThreadLocal.withInitial(() -> null);

    @GetMapping("wrong")
    public Map<String, String> wrong(@RequestParam("id") Integer userId) {
        //设置用户信息之前先查询一次ThreadLocal中的用户信息
        String before = Thread.currentThread().getName() + ":" + currentUser.get();
        //设置用户信息到ThreadLocal
        currentUser.set(userId);
        //设置用户信息之后再查询一次ThreadLocal中的用户信息
        String after = Thread.currentThread().getName() + ":" + currentUser.get();
        //汇总输出两次查询结果
        Map<String, String> result = new HashMap<>();
        result.put("before", before);
        result.put("after", after);
        return result;
    }
```

【预期】从代码逻辑来看，我们预期第一次获取的值始终应该是 null。

【实际】

为了方便复现，将 Tomcat 工作线程设为 1：

```
server.tomcat.max-threads=1
```

当访问 id = 1 时，符合预期

![img](https://raw.githubusercontent.com/dunwu/images/dev/snap/20200731111854.png)

当访问 id = 2 时，before 的应答不是 null，而是 1，不符合预期。

![image-20200731111921591](C:\Users\zp\AppData\Roaming\Typora\typora-user-images\image-20200731111921591.png)

【分析】实际情况和预期存在偏差。Spring Boot 程序运行在 Tomcat 中，执行程序的线程是 Tomcat 的工作线程，而 Tomcat 的工作线程是基于线程池的。**线程池会重用固定的几个线程，一旦线程重用，那么很可能首次从**
**ThreadLocal 获取的值是之前其他用户的请求遗留的值。这时，ThreadLocal 中的用户信息就是其他用户的信息**。

**并不能认为没有显式开启多线程就不会有线程安全问题**。使用类似 ThreadLocal 工具来存放一些数据时，需要特别注意在代码运行完后，显式地去清空设置的数据。

#### ThreadLocal 错误案例修正

```java
    @GetMapping("right")
    public Map<String, String> right(@RequestParam("id") Integer userId) {
        String before = Thread.currentThread().getName() + ":" + currentUser.get();
        currentUser.set(userId);
        try {
            String after = Thread.currentThread().getName() + ":" + currentUser.get();
            Map<String, String> result = new HashMap<>();
            result.put("before", before);
            result.put("after", after);
            return result;
        } finally {
            //在finally代码块中删除ThreadLocal中的数据，确保数据不串
            currentUser.remove();
        }
    }
```

### 5.4. InheritableThreadLocal

`InheritableThreadLocal` 类是 `ThreadLocal` 类的子类。

`ThreadLocal` 中每个线程拥有它自己独占的数据。与 `ThreadLocal` 不同的是，`InheritableThreadLocal` 允许一个线程以及该线程创建的所有子线程都可以访问它保存的数据。

> 原理参考：[Java 多线程：InheritableThreadLocal 实现原理](https://blog.csdn.net/ni357103403/article/details/51970748)

## 6. 参考资料

- [《Java 并发编程实战》](https://book.douban.com/subject/10484692/)
- [《Java 并发编程的艺术》](https://book.douban.com/subject/26591326/)
- [《深入理解 Java 虚拟机》](https://book.douban.com/subject/34907497/)
- [《Java 业务开发常见错误 100 例》](https://time.geekbang.org/column/intro/100047701)
- [Java 并发编程：volatile 关键字解析](http://www.cnblogs.com/dolphin0520/p/3920373.html)
- [Java 并发编程：synchronized](http://www.cnblogs.com/dolphin0520/p/3923737.html)
- [深入理解 Java 并发之 synchronized 实现原理](https://blog.csdn.net/javazejian/article/details/72828483)
- [Java CAS 完全解读](https://www.jianshu.com/p/473e14d5ab2d)
- [Java 中 CAS 详解](https://blog.csdn.net/ls5718/article/details/52563959)
- [ThreadLocal 终极篇](https://juejin.im/post/5a64a581f265da3e3b7aa02d)
- [synchronized 实现原理及锁优化](https://nicky-chen.github.io/2018/05/14/synchronized-principle/)
- [Non-blocking Algorithms](http://tutorials.jenkov.com/java-concurrency/non-blocking-algorithms.html)
