<!-- TOC depthFrom:2 depthTo:3 -->

- [一、简介](#一简介)
  - [锁分类](#锁分类)
  - [synchronized 和 Lock、ReadWriteLock](#synchronized-和-lockreadwritelock)
- [二、AQS](#二aqs)
  - [AQS 的要点](#aqs-的要点)
  - [AQS 的原理](#aqs-的原理)
- [三、Lock](#三lock)
  - [Lock 的要点](#lock-的要点)
  - [ReentrantLock 的用法](#reentrantlock-的用法)
  - [ReentrantLock 的原理](#reentrantlock-的原理)
- [四、ReadWriteLock](#四readwritelock)
  - [ReadWriteLock 的要点](#readwritelock-的要点)
  - [ReentrantReadWriteLock 的用法](#reentrantreadwritelock-的用法)
  - [ReentrantReadWriteLock 的原理](#reentrantreadwritelock-的原理)
- [五、总结](#五总结)
- [参考资料](#参考资料)

<!-- /TOC -->

# 深入理解 Java 并发锁

> **📦 本文以及示例源码已归档在 [javacore](https://dunwu.github.io/javacore/#/)**

![](http://dunwu.test.upcdn.net/snap/20200107002233.png)

## 一、简介

确保线程安全最常见的做法是利用锁机制（`Lock`、`sychronized`）来对共享数据做互斥同步，这样在同一个时刻，只有一个线程可以执行某个方法或者某个代码块，那么操作必然是原子性的，线程安全的。

### 锁分类

> :bulb: 在工作、面试中，经常会听到各种五花八门的锁，听的人云里雾里。锁的概念术语很多，它们是针对不同的问题所提出的，通过简单的梳理，也不难理解。

#### 可重入锁

可重入锁又名递归锁，是指 **同一个线程在外层方法获取了锁，在进入内层方法会自动获取锁**。

**可重入锁可以在一定程度上避免死锁**。

- **`ReentrantLock` 、`ReentrantReadWriteLock` 是可重入锁**。这点，从其命名也不难看出。
- **`synchronized` 也是一个可重入锁**。

```java
synchronized void setA() throws Exception{
    Thread.sleep(1000);
    setB();
}

synchronized void setB() throws Exception{
    Thread.sleep(1000);
}
```

上面的代码就是一个典型场景：如果使用的锁不是可重入锁的话，`setB` 可能不会被当前线程执行，从而造成死锁。

#### 公平锁与非公平锁

- **公平锁** - 公平锁是指 **多线程按照申请锁的顺序来获取锁**。
- **非公平锁** - 非公平锁是指 **多线程不按照申请锁的顺序来获取锁** 。这就可能会出现优先级反转（后来者居上）或者饥饿现象（某线程总是抢不过别的线程，导致始终无法执行）。

公平锁为了保证线程申请顺序，势必要付出一定的性能代价，因此其吞吐量一般低于非公平锁。

公平锁与非公平锁 在 Java 中的典型实现：

- **`synchronized` 只支持非公平锁**。
- **`ReentrantLock` 、`ReentrantReadWriteLock`，默认是非公平锁，但支持公平锁**。

#### 独享锁与共享锁

独享锁与共享锁是一种广义上的说法，从实际用途上来看，也常被称为互斥锁与读写锁。

- **独享锁** - 独享锁是指 **锁一次只能被一个线程所持有**。
- **共享锁** - 共享锁是指 **锁可被多个线程所持有**。

独享锁与共享锁在 Java 中的典型实现：

- **`synchronized` 、`ReentrantLock` 只支持独享锁**。
- **`ReentrantReadWriteLock` 其写锁是独享锁，其读锁是共享锁**。读锁是共享锁使得并发读是非常高效的，读写，写读 ，写写的过程是互斥的。

#### 悲观锁与乐观锁

乐观锁与悲观锁不是指具体的什么类型的锁，而是**处理并发同步的策略**。

- **悲观锁** - 悲观锁对于并发采取悲观的态度，认为：**不加锁的并发操作一定会出问题**。**悲观锁适合写操作频繁的场景**。
- **乐观锁** - 乐观锁对于并发采取乐观的态度，认为：**不加锁的并发操作也没什么问题。对于同一个数据的并发操作，是不会发生修改的**。在更新数据的时候，会采用不断尝试更新的方式更新数据。**乐观锁适合读多写少的场景**。

悲观锁与乐观锁在 Java 中的典型实现：

- 悲观锁在 Java 中的应用就是通过使用 `synchronized` 和 `Lock` 显示加锁来进行互斥同步，这是一种阻塞同步。

- 乐观锁在 Java 中的应用就是采用 CAS 机制（CAS 操作通过 `Unsafe` 类提供，但这个类不直接暴露为 API，所以都是间接使用，如各种原子类）。

#### 轻量级锁、重量级锁与偏向锁

所谓轻量级锁与重量级锁，指的是锁控制粒度的粗细。显然，控制粒度越细，阻塞开销越小，并发性也就越高。

Java 1.6 以前，重量级锁一般指的是 `synchronized` ，而轻量级锁指的是 `volatile`。

Java 1.6 以后，针对 `synchronized` 做了大量优化，引入 4 种锁状态： 无锁状态、偏向锁、轻量级锁和重量级锁。锁可以单向的从偏向锁升级到轻量级锁，再从轻量级锁升级到重量级锁 。

- **偏向锁** - 偏向锁是指一段同步代码一直被一个线程所访问，那么该线程会自动获取锁。降低获取锁的代价。
- **轻量级锁** - 是指当锁是偏向锁的时候，被另一个线程所访问，偏向锁就会升级为轻量级锁，其他线程会通过自旋的形式尝试获取锁，不会阻塞，提高性能。

- **重量级锁** - 是指当锁为轻量级锁的时候，另一个线程虽然是自旋，但自旋不会一直持续下去，当自旋一定次数的时候，还没有获取到锁，就会进入阻塞，该锁膨胀为重量级锁。重量级锁会让其他申请的线程进入阻塞，性能降低。

#### 分段锁

分段锁其实是一种锁的设计，并不是具体的一种锁。所谓分段锁，就是把锁的对象分成多段，每段独立控制，使得锁粒度更细，减少阻塞开销，从而提高并发性。这其实很好理解，就像高速公路上的收费站，如果只有一个收费口，那所有的车只能排成一条队缴费；如果有多个收费口，就可以分流了。

`Hashtable` 使用 `synchronized` 修饰方法来保证线程安全性，那么面对线程的访问，Hashtable 就会锁住整个对象，所有的其它线程只能等待，这种阻塞方式的吞吐量显然很低。

Java 1.7 以前的 `ConcurrentHashMap` 就是分段锁的典型案例。`ConcurrentHashMap` 维护了一个 `Segment` 数组，一般称为分段桶。

```java
final Segment<K,V>[] segments;
```

当有线程访问 `ConcurrentHashMap` 的数据时，`ConcurrentHashMap` 会先根据 hashCode 计算出数据在哪个桶（即哪个 Segment），然后锁住这个 `Segment`。

### 显示锁和隐式锁

在 [锁分类](#锁分类) 中，我们零零散散也提到了，`synchronized` 锁的限制比较多。 这里，汇总一下 `Lock` 、`ReadWriteLock` 和 `synchronized` 的区别：

- 主动获取锁和释放锁
  - `synchronized` 不能主动获取锁和释放锁。获取锁和释放锁都是 JVM 控制的。
  - `Lock` 可以主动获取锁和释放锁。（如果忘记释放锁，就可能产生死锁）。
- 响应中断
  - `synchronized` 不能响应中断。
  - `Lock` 可以响应中断。
- 超时机制
  - `synchronized` 没有超时机制。
  - `Lock` 有超时机制。`Lock` 可以设置超时时间，超时后自动释放锁，避免一直等待。
- 支持公平锁
  - `synchronized` 只支持非公平锁。
  - `Lock` 支持非公平锁和公平锁。
- 是否支持共享
  - 被 `synchronized` 修饰的方法或代码块，只能被一个线程访问（独享）。如果这个线程被阻塞，其他线程也只能等待
  - `Lock` 可以基于 `Condition` 灵活的控制同步条件。
- 是否支持读写分离
  - `synchronized` 不支持读写锁分离；
  - `ReadWriteLock` 支持读写锁，从而使阻塞读写的操作分开，有效提高并发性。

> 💡 `synchronized` 的用法和原理可以参考：[Java 并发基础机制 - synchronized](https://github.com/dunwu/javacore/blob/master/docs/concurrent/java-concurrent-basic-mechanism.md#%E4%BA%8Csynchronized) 。
>
> 如果不需要 `Lock` 、`ReadWriteLock` 所提供的高级同步特性，应该优先考虑使用 `synchronized` ，理由如下：
>
> - Java 1.6 以后，`synchronized` 做了大量的优化，其性能已经与 `Lock` 、`ReadWriteLock` 基本上持平。从趋势来看，Java 未来仍将继续优化 `synchronized` ，而不是 `ReentrantLock` 。
> - `ReentrantLock` 是 Oracle JDK 的 API，在其他版本的 JDK 中不一定支持；而 `synchronized` 是 JVM 的内置特性，所有 JDK 版本都提供支持。

## 二、AQS

> `AbstractQueuedSynchronizer`（简称 **AQS**）是**队列同步器**，顾名思义，其主要作用是处理同步。它是并发锁和很多同步工具类的实现基石（如 `ReentrantLock`、`ReentrantReadWriteLock`、`Semaphore` 等）。
>
> 因此，要想深入理解 `ReentrantLock`、`ReentrantReadWriteLock` 等并发锁和同步工具，必须先理解 AQS 的要点和原理。

### AQS 的要点

在 `java.util.concurrent.locks` 包中的相关锁(常用的有 `ReentrantLock`、 `ReadWriteLock`)都是基于 AQS 来实现。这些锁都没有直接继承 AQS，而是定义了一个 `Sync` 类去继承 AQS。为什么要这样呢？因为锁面向的是使用用户，而同步器面向的则是线程控制，那么在锁的实现中聚合同步器而不是直接继承 AQS 就可以很好的隔离二者所关注的事情。

**AQS 提供了对独享锁与共享锁的支持**。

#### 独享锁 API

获取、释放独享锁的主要 API 如下：

```java
public final void acquire(int arg)
public final void acquireInterruptibly(int arg)
public final boolean tryAcquireNanos(int arg, long nanosTimeout)
public final boolean release(int arg)
```

- `acquire` - 获取独占锁。
- `acquireInterruptibly` - 获取可中断的独占锁。
- `tryAcquireNanos` - 尝试在指定时间内获取可中断的独占锁。在以下三种情况下回返回：
  - 在超时时间内，当前线程成功获取了锁；
  - 当前线程在超时时间内被中断；
  - 超时时间结束，仍未获得锁返回 false。
- `release` - 释放独占锁。

#### 共享锁 API

获取、释放共享锁的主要 API 如下：

```java
public final void acquireShared(int arg)
public final void acquireSharedInterruptibly(int arg)
public final boolean tryAcquireSharedNanos(int arg, long nanosTimeout)
public final boolean releaseShared(int arg)
```

- `acquireShared` - 获取共享锁。
- `acquireSharedInterruptibly` - 获取可中断的共享锁。
- `tryAcquireSharedNanos` - 尝试在指定时间内获取可中断的共享锁。
- `release` - 释放共享锁。

### AQS 的原理

#### AQS 的数据结构

阅读 AQS 的源码，可以发现：AQS 继承自 `AbstractOwnableSynchronize`。

```java
public abstract class AbstractQueuedSynchronizer
    extends AbstractOwnableSynchronizer
    implements java.io.Serializable {

    /** 等待队列的队头，懒加载。只能通过 setHead 方法修改。 */
    private transient volatile Node head;
    /** 等待队列的队尾，懒加载。只能通过 enq 方法添加新的等待节点。*/
    private transient volatile Node tail;
    /** 同步状态 */
    private volatile int state;
}
```

- `state` - AQS 使用一个整型的 `volatile` 变量来 **维护同步状态**。
  - 这个整数状态的意义由子类来赋予，如`ReentrantLock` 中该状态值表示所有者线程已经重复获取该锁的次数，`Semaphore` 中该状态值表示剩余的许可数量。
- `head` 和 `tail` - AQS **维护了一个 `Node` 类型（AQS 的内部类）的双链表来完成同步状态的管理**。这个双链表是一个双向的 FIFO 队列，通过 `head` 和 `tail` 指针进行访问。当 **有线程获取锁失败后，就被添加到队列末尾**。

![img](http://dunwu.test.upcdn.net/cs/java/javacore/concurrent/aqs_1.png!zp)

再来看一下 `Node` 的源码

```java
static final class Node {
    /** 该等待同步的节点处于共享模式 */
    static final Node SHARED = new Node();
    /** 该等待同步的节点处于独占模式 */
    static final Node EXCLUSIVE = null;

    /** 等待状态,这个和 state 是不一样的:有 1,0,-1,-2,-3 五个值 */
    volatile int waitStatus;
    static final int CANCELLED =  1;
    static final int SIGNAL    = -1;
    static final int CONDITION = -2;
    static final int PROPAGATE = -3;

    /** 前驱节点 */
    volatile Node prev;
    /** 后继节点 */
    volatile Node next;
    /** 等待锁的线程 */
    volatile Thread thread;
}
```

很显然，Node 是一个双链表结构。重点关注一下 `volatile` 修饰的 `waitStatus` 属性，它用于维护 AQS 同步队列中线程节点的状态。`waitStatus` 有五个状态值：

- `CANCELLED(1)` - 此状态表示：该节点的线程可能由于超时或被中断而 **处于被取消(作废)状态**，一旦处于这个状态，表示这个节点应该从队列中移除。
- `SIGNAL(-1)` - 此状态表示：**后继节点会被挂起**，因此在当前节点释放锁或被取消之后，必须唤醒(`unparking`)其后继结点。
- `CONDITION(-2)` - 此状态表示：该节点的线程 **处于等待条件状态**，不会被当作是同步队列上的节点，直到被唤醒(`signal`)，设置其值为 0，再重新进入阻塞状态。
- `PROPAGATE(-3) -` - 此状态表示：下一个 `acquireShared` 应无条件传播。
- 0 - 非以上状态。

#### 独占锁的获取和释放

##### 获取独占锁

AQS 中使用 `acquire(int arg)` 方法获取独占锁，其大致流程如下：

1. 先尝试获取同步状态，如果获取同步状态成功，则结束方法，直接返回。
2. 如果获取同步状态不成功，AQS 会不断尝试利用 CAS 操作将当前线程插入等待同步队列的队尾，直到成功为止。
3. 接着，不断尝试为等待队列中的线程节点获取独占锁。

![img](http://dunwu.test.upcdn.net/cs/java/javacore/concurrent/aqs_2.png!zp)

![img](http://dunwu.test.upcdn.net/cs/java/javacore/concurrent/aqs_3.png!zp)

详细流程可以用下图来表示，请结合源码来理解（一图胜千言）：

![img](http://dunwu.test.upcdn.net/cs/java/javacore/concurrent/aqs_4.png!zp)

##### 释放独占锁

AQS 中使用 `release(int arg)` 方法释放独占锁，其大致流程如下：

1. 先尝试获取解锁线程的同步状态，如果获取同步状态不成功，则结束方法，直接返回。
2. 如果获取同步状态成功，AQS 会尝试唤醒当前线程节点的后继节点。

##### 获取可中断的独占锁

AQS 中使用 `acquireInterruptibly(int arg)` 方法获取可中断的独占锁。

`acquireInterruptibly(int arg)` 实现方式**相较于获取独占锁方法（ `acquire`）非常相似**，区别仅在于它会**通过 `Thread.interrupted` 检测当前线程是否被中断**，如果是，则立即抛出中断异常（`InterruptedException`）。

##### 获取超时等待式的独占锁

AQS 中使用 `tryAcquireNanos(int arg)` 方法获取超时等待的独占锁。

doAcquireNanos 的实现方式 **相较于获取独占锁方法（ `acquire`）非常相似**，区别在于它会根据超时时间和当前时间计算出截止时间。在获取锁的流程中，会不断判断是否超时，如果超时，直接返回 false；如果没超时，则用 `LockSupport.parkNanos` 来阻塞当前线程。

#### 共享锁的获取和释放

##### 获取共享锁

AQS 中使用 `acquireShared(int arg)` 方法获取共享锁。

`acquireShared` 方法和 `acquire` 方法的逻辑很相似，区别仅在于自旋的条件以及节点出队的操作有所不同。

成功获得共享锁的条件如下：

- `tryAcquireShared(arg)` 返回值大于等于 0 （这意味着共享锁的 permit 还没有用完）。
- 当前节点的前驱节点是头结点。

##### 释放共享锁

AQS 中使用 `releaseShared(int arg)` 方法释放共享锁。

`releaseShared` 首先会尝试释放同步状态，如果成功，则解锁一个或多个后继线程节点。释放共享锁和释放独享锁流程大体相似，区别在于：

对于独享模式，如果需要 SIGNAL，释放仅相当于调用头节点的 `unparkSuccessor`。

##### 获取可中断的共享锁

AQS 中使用 `acquireSharedInterruptibly(int arg)` 方法获取可中断的共享锁。

`acquireSharedInterruptibly` 方法与 `acquireInterruptibly` 几乎一致，不再赘述。

##### 获取超时等待式的共享锁

AQS 中使用 `tryAcquireSharedNanos(int arg)` 方法获取超时等待式的共享锁。

`tryAcquireSharedNanos` 方法与 `tryAcquireNanos` 几乎一致，不再赘述。

## 三、ReentrantLock

> 与内置锁 `synchronized` 不同，`Lock` 提供了一组无条件的、可轮询的、定时的以及可中断的锁操作，所有获取锁、释放锁操作都是显示的。

### Lock 的要点

`Lock` 的接口定义：

```java
public interface Lock {
    void lock();
    void lockInterruptibly() throws InterruptedException;
    boolean tryLock();
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
    void unlock();
    Condition newCondition();
}
```

:bell: 注意：

- 如果采用 `Lock`，**必须主动去释放锁** 。发生异常时，Java 并不会自动释放锁。因此一般来说，使用 `Lock` 必须在 `try catch` 块中进行，并且将释放锁的操作放在 `finally` 块中进行，以保证锁一定被被释放，防止死锁的发生。
- 当一个线程获取了锁之后，是不会被 `interrupt()` 方法中断的。单独调用 `interrupt()` 方法不能中断正在运行过程中的线程，只能中断阻塞过程中的线程。因此当通过 `lockInterruptibly()` 方法获取某个锁时，如果不能获取到，只有进行等待的情况下，是可以响应中断的。

### ReentrantLock 的用法

`ReentrantLock` 实现了 `Lock` 接口，除了 `Lock` 接口所定义的能力，它还有以下特性：

- 提供了与 `synchronized` 相同的互斥性和内存可见性。
- 提供了与 `synchronized` 相同的可重入性。
- 支持公平锁和非公平锁（默认）两种模式。

#### 构造方法

`ReentrantLock` 有两个构造方法：

```java
// 默认初始化 sync 的实例为非公平锁（NonfairSync）
public ReentrantLock() {}
// 根据 boolean 值选择初始化 sync 的实例为公平的锁（FairSync）或不公平锁（NonfairSync）
public ReentrantLock(boolean fair) {}
```

- `ReentrantLock()` - 默认构造方法会初始化一个非公平锁；
- `ReentrantLock(boolean)` - `new ReentrantLock(true)` 会初始化一个公平锁。

#### lock 和 unlock 方法

- `lock()` - 用于 **获取锁**。如果锁已被其他线程获取，则等待。
- `unlock()` - 用于**释放锁**。

示例：`ReentrantLock` 的基本使用

```java
public class ReentrantLockDemo {

    public static void main(String[] args) {
        Task task = new Task();
        MyThread tA = new MyThread("Thread-A", task);
        MyThread tB = new MyThread("Thread-B", task);
        MyThread tC = new MyThread("Thread-C", task);
        tA.start();
        tB.start();
        tC.start();
    }

    static class MyThread extends Thread {

        private Task task;

        public MyThread(String name, Task task) {
            super(name);
            this.task = task;
        }

        @Override
        public void run() {
            task.execute();
        }

    }

    static class Task {

        private ReentrantLock lock = new ReentrantLock();

        public void execute() {
            lock.lock();
            try {
                for (int i = 0; i < 3; i++) {
                    System.out.println(lock.toString());

                    // 查询当前线程 hold 住此锁的次数
                    System.out.println("\t holdCount: " + lock.getHoldCount());

                    // 查询正等待获取此锁的线程数
                    System.out.println("\t queuedLength: " + lock.getQueueLength());

                    // 是否为公平锁
                    System.out.println("\t isFair: " + lock.isFair());

                    // 是否被锁住
                    System.out.println("\t isLocked: " + lock.isLocked());

                    // 是否被当前线程持有锁
                    System.out.println("\t isHeldByCurrentThread: " + lock.isHeldByCurrentThread());

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                lock.unlock();
            }
        }

    }

}
```

输出结果：

```java
java.util.concurrent.locks.ReentrantLock@64fcd88a[Locked by thread Thread-A]
	 holdCount: 1
	 queuedLength: 2
	 isFair: false
	 isLocked: true
	 isHeldByCurrentThread: true
java.util.concurrent.locks.ReentrantLock@64fcd88a[Locked by thread Thread-C]
	 holdCount: 1
	 queuedLength: 1
	 isFair: false
	 isLocked: true
	 isHeldByCurrentThread: true
// ...
```

#### tryLock 方法

- `tryLock()` - 用于 **尝试获取锁，如果成功，则返回 true；如果失败，则返回 false**。也就是说，这个方法无论如何都会立即返回，获取不到锁（锁已被其他线程获取）时不会一直等待。
- `tryLock(long time, TimeUnit unit)` - 和 `tryLock()` 类似，区别仅在于这个方法在**获取不到锁时会等待一定的时间**，在时间期限之内如果还获取不到锁，就返回 false。如果如果一开始拿到锁或者在等待期间内拿到了锁，则返回 true。

示例：`ReentrantLock` 的 `tryLock()` 操作

修改“`ReentrantLock` 的基本使用”示例中的 `execute()` 方法

```java
public void execute() {
    if (lock.tryLock()) {
        try {
            for (int i = 0; i < 3; i++) {
               // 略...
            }
        } finally {
            lock.unlock();
        }
    } else {
        System.out.println(Thread.currentThread().getName() + " 获取锁失败");
    }
}
```

示例：`ReentrantLock` 的 `tryLock(long time, TimeUnit unit)` 操作

修改“`ReentrantLock` 的基本使用”示例中的 `execute()` 方法

```java
public void execute() {
    try {
        if (lock.tryLock(2, TimeUnit.SECONDS)) {
            try {
                for (int i = 0; i < 3; i++) {
                    // 略...
                }
            } finally {
                lock.unlock();
            }
        } else {
            System.out.println(Thread.currentThread().getName() + " 获取锁失败");
        }
    } catch (InterruptedException e) {
        System.out.println(Thread.currentThread().getName() + " 获取锁超时");
        e.printStackTrace();
    }
}
```

#### lockInterruptibly 方法

- `lockInterruptibly()` - 当通过这个方法去获取锁时，如果线程正在等待获取锁，则这个线程能够响应中断，即**中断线程的等待状态**。也就使说，当两个线程同时通过 `lock.lockInterruptibly()` 想获取某个锁时，假若此时线程 A 获取到了锁，而线程 B 只有在等待，那么对线程 B 调用 `threadB.interrupt()` 方法能够中断线程 B 的等待过程。由于 `lockInterruptibly()` 的声明中抛出了异常，所以 `lock.lockInterruptibly()` 必须放在 try 块中或者在调用 `lockInterruptibly()` 的方法外声明抛出 `InterruptedException`。

示例：`ReentrantLock` 的 `lockInterruptibly()` 操作

修改“`ReentrantLock` 的基本使用”示例中的 `execute()` 方法

```java
public void execute() {
    try {
        lock.lockInterruptibly();

        for (int i = 0; i < 3; i++) {
            // 略...
        }
    } catch (InterruptedException e) {
        System.out.println(Thread.currentThread().getName() + "被中断");
        e.printStackTrace();
    } finally {
        lock.unlock();
    }
}
```

### ReentrantLock 的原理

#### ReentrantLock 的数据结构

阅读 `ReentrantLock` 的源码，可以发现它有一个核心字段：

```java
private final Sync sync;
```

- `sync` - 内部抽象类 `ReentrantLock.Sync` 对象，`Sync` 继承自 AQS。它有两个子类：
- `ReentrantLock.FairSync` - 公平锁。
- `ReentrantLock.NonfairSync` - 非公平锁。

查看源码可以发现，`ReentrantLock` 实现 `Lock` 接口其实是调用 `ReentrantLock.FairSync` 或 `ReentrantLock.NonfairSync` 中各自的实现，这里不一一列举。

#### ReentrantLock 的获取锁和释放锁

ReentrantLock 获取锁和释放锁的接口，从表象看，是调用 `ReentrantLock.FairSync` 或 `ReentrantLock.NonfairSync` 中各自的实现；从本质上看，是基于 AQS 的实现。

仔细阅读源码很容易发现：

- `void lock()` 调用 Sync 的 lock() 方法。
- `void lockInterruptibly()` 直接调用 AQS 的 [获取可中断的独占锁](#获取可中断的独占锁) 方法 `lockInterruptibly()`。

- `boolean tryLock()` 调用 Sync 的 `nonfairTryAcquire()` 。
- `boolean tryLock(long time, TimeUnit unit)` 直接调用 AQS 的 [获取超时等待式的独占锁](#获取超时等待式的独占锁) 方法 `tryAcquireNanos(int arg, long nanosTimeout)`。
- `void unlock()` 直接调用 AQS 的 [释放独占锁](#释放独占锁) 方法 `release(int arg)` 。

直接调用 AQS 接口的方法就不再赘述了，其原理在 [AQS 的原理](#AQS 的原理) 中已经用很大篇幅进行过讲解。

`nonfairTryAcquire` 方法源码如下：

```java
// 公平锁和非公平锁都会用这个方法区尝试获取锁
final boolean nonfairTryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    int c = getState();
    if (c == 0) {
        if (compareAndSetState(0, acquires)) {
         // 如果同步状态为0，将其设为 acquires，并设置当前线程为排它线程
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    else if (current == getExclusiveOwnerThread()) {
        int nextc = c + acquires;
        if (nextc < 0) // overflow
            throw new Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
    }
    return false;
}
```

处理流程很简单：

- 如果同步状态为 0，设置同步状态设为 acquires，并设置当前线程为排它线程，然后返回 true，获取锁成功。
- 如果同步状态不为 0 且当前线程为排它线程，设置同步状态为当前状态值+acquires 值，然后返回 true，获取锁成功。
- 否则，返回 false，获取锁失败。

lock 方法在公平锁和非公平锁中的实现：

二者的区别仅在于申请非公平锁时，如果同步状态为 0，尝试将其设为 1，如果成功，直接将当前线程置为排它线程；否则和公平锁一样，调用 AQS 获取独占锁方法 `acquire`。

```java
// 非公平锁实现
final void lock() {
    if (compareAndSetState(0, 1))
    // 如果同步状态为0，将其设为1，并设置当前线程为排它线程
        setExclusiveOwnerThread(Thread.currentThread());
    else
    // 调用 AQS 获取独占锁方法 acquire
        acquire(1);
}

// 公平锁实现
final void lock() {
    // 调用 AQS 获取独占锁方法 acquire
    acquire(1);
}
```

## 四、ReentrantReadWriteLock

### ReadWriteLock 的要点

`ReadWriteLock` 接口定义如下：

```java
public interface ReadWriteLock {
    Lock readLock();
    Lock writeLock();
}
```

- `readLock` - 返回用于读操作的锁。
- `writeLock` - 返回用于写操作的锁。

`ReadWriteLock` 维护一对相关的锁。一个是读锁；一个是写锁。将读写锁分开，有利于提高并发效率。对于特定的资源，`ReadWriteLock` **允许多个线程同时对其执行读操作，但是只允许一个线程对其执行写操作**。概括来说：

- 读读线程间不互斥。
- 读写、写写线程间互斥。

### ReentrantReadWriteLock 的用法

`ReentrantReadWriteLock` 类是 `ReadWriteLock` 的具体实现。它是一个**可重入的读写锁**。

`ReentrantReadWriteLock` 实现了 `ReadWriteLock` 接口，除了 `ReadWriteLock` 接口所定义的能力，它还有以下特性：

- 提供了与 `synchronized` 相同的可重入性。
- 支持公平锁和非公平锁（默认）两种模式。

示例：基于 `ReentrantReadWriteLock` 实现一个简单的本地缓存

```java
/**
 * 简单的本地缓存实现
 * <p>
 * 使用 WeakHashMap 存储键值对。WeakHashMap 中存储的对象是弱引用，JVM GC 时会自动清除没有被引用的弱引用对象。
 */
static class MyCache<K, V> {

    private final Map<K, V> cacheMap = new WeakHashMap<>();

    private final ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();

    public V get(K key) {
        cacheLock.readLock().lock();
        V value;
        try {
            value = cacheMap.get(key);
            String log = String.format("%s 读数据 %s:%s", Thread.currentThread().getName(), key, value);
            System.out.println(log);
        } finally {
            cacheLock.readLock().unlock();
        }
        return value;
    }

    public V put(K key, V value) {
        cacheLock.writeLock().lock();
        try {
            cacheMap.put(key, value);
            String log = String.format("%s 写入数据 %s:%s", Thread.currentThread().getName(), key, value);
            System.out.println(log);
        } finally {
            cacheLock.writeLock().unlock();
        }
        return value;
    }

    public V remove(K key) {
        cacheLock.writeLock().lock();
        try {
            return cacheMap.remove(key);
        } finally {
            cacheLock.writeLock().unlock();
        }
    }

    public void clear() {
        cacheLock.writeLock().lock();
        try {
            this.cacheMap.clear();
        } finally {
            cacheLock.writeLock().unlock();
        }
    }

}
```

说明：

- 使用 `WeakHashMap` 而不是 `HashMap` 来存储键值对。`WeakHashMap` 中存储的对象是弱引用，JVM GC 时会自动清除没有被引用的弱引用对象。
- 向 Map 写数据前加写锁，写完后，释放写锁。
- 向 Map 读数据前加读锁，读完后，释放读锁。

测试其线程安全性：

```java
public class ReentrantReadWriteLockDemo {

    static MyCache<Integer, Integer> cache = new MyCache<>();

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 20; i++) {
            executorService.execute(new MyThread());
            cache.get(0);
        }
        executorService.shutdown();
    }

    /** 线程任务每次向缓存中写入 3 个随机值，key 固定 */
    static class MyThread implements Runnable {

        @Override
        public void run() {
            Random random = new Random();
            for (int i = 0; i < 3; i++) {
                cache.put(i, random.nextInt(100));
            }
        }

    }

}
```

说明：示例中，通过线程池启动 20 个并发任务。任务每次向缓存中写入 3 个随机值，key 固定；然后主线程每次固定读取缓存中第一个 key 的值。

输出结果：

```
main 读数据 0:null
pool-1-thread-1 写入数据 0:16
pool-1-thread-1 写入数据 1:58
pool-1-thread-1 写入数据 2:50
main 读数据 0:16
pool-1-thread-1 写入数据 0:85
pool-1-thread-1 写入数据 1:76
pool-1-thread-1 写入数据 2:46
pool-1-thread-2 写入数据 0:21
pool-1-thread-2 写入数据 1:41
pool-1-thread-2 写入数据 2:63
main 读数据 0:21
main 读数据 0:21
// ...
```

### ReentrantReadWriteLock 的原理

前面了解了 `ReentrantLock` 的原理，理解 `ReentrantReadWriteLock` 就容易多了。

#### ReentrantReadWriteLock 的数据结构

阅读 ReentrantReadWriteLock 的源码，可以发现它有三个核心字段：

```java
/** Inner class providing readlock */
private final ReentrantReadWriteLock.ReadLock readerLock;
/** Inner class providing writelock */
private final ReentrantReadWriteLock.WriteLock writerLock;
/** Performs all synchronization mechanics */
final Sync sync;

public ReentrantReadWriteLock.WriteLock writeLock() { return writerLock; }
public ReentrantReadWriteLock.ReadLock  readLock()  { return readerLock; }
```

- `sync` - 内部类 `ReentrantReadWriteLock.Sync` 对象。与 `ReentrantLock` 类似，它有两个子类：`ReentrantReadWriteLock.FairSync` 和 `ReentrantReadWriteLock.NonfairSync` ，分别表示公平锁和非公平锁的实现。
- `readerLock` - 内部类 `ReentrantReadWriteLock.ReadLock` 对象，这是一把读锁。
- `writerLock` - 内部类 `ReentrantReadWriteLock.WriteLock` 对象，这是一把写锁。

#### ReentrantReadWriteLock 的获取锁和释放锁

```java
public static class ReadLock implements Lock, java.io.Serializable {

    // 调用 AQS 获取共享锁方法
    public void lock() {
        sync.acquireShared(1);
    }

    // 调用 AQS 释放共享锁方法
    public void unlock() {
        sync.releaseShared(1);
    }
}

public static class WriteLock implements Lock, java.io.Serializable {

    // 调用 AQS 获取独占锁方法
    public void lock() {
        sync.acquire(1);
    }

    // 调用 AQS 释放独占锁方法
    public void unlock() {
        sync.release(1);
    }
}
```

## 五、总结

![](http://dunwu.test.upcdn.net/snap/20200105234810.png)

## 参考资料

- [《Java 并发编程实战》](https://item.jd.com/10922250.html)
- [《Java 并发编程的艺术》](https://item.jd.com/11740734.html)
- [Java 并发编程：Lock](https://www.cnblogs.com/dolphin0520/p/3923167.html)
- [深入学习 java 同步器 AQS](https://zhuanlan.zhihu.com/p/27134110)
- [AbstractQueuedSynchronizer 框架](https://t.hao0.me/java/2016/04/01/aqs.html)
- [Java 中的锁分类](https://www.cnblogs.com/qifengshi/p/6831055.html)
