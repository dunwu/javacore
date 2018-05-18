---
title: Java 锁
date: 2018/05/16
categories:
- javase
tags:
- javase
- concurrent
- juc
---

# Java 锁

> 本文内容基于 JDK1.8。

<!-- TOC depthFrom:2 depthTo:4 -->

- [概述](#概述)
  - [为什么会出现 Lock？](#为什么会出现-lock)
  - [synchronized](#synchronized)
- [Lock 和 ReentrantLock](#lock-和-reentrantlock)
  - [要点](#要点)
  - [源码](#源码)
    - [Lock](#lock)
    - [ReentrantLock](#reentrantlock)
  - [示例](#示例)
- [ReadWriteLock 和 ReentrantReadWriteLock](#readwritelock-和-reentrantreadwritelock)
  - [要点](#要点-1)
  - [源码](#源码-1)
    - [ReadWriteLock](#readwritelock)
  - [示例](#示例-1)
- [AQS](#aqs)
  - [要点](#要点-2)
  - [源码](#源码-2)
    - [同步队列](#同步队列)
    - [获取独占锁](#获取独占锁)
    - [释放独占锁](#释放独占锁)
    - [获取可中断的独占锁](#获取可中断的独占锁)
    - [获取超时等待式的独占锁](#获取超时等待式的独占锁)
    - [获取共享锁](#获取共享锁)
    - [释放共享锁](#释放共享锁)
    - [获取可中断的共享锁](#获取可中断的共享锁)
    - [获取超时等待式的共享锁](#获取超时等待式的共享锁)
- [资料](#资料)

<!-- /TOC -->

## 概述

### 为什么会出现 Lock？

### synchronized

synchronized 是 Java 的内置锁。

如果一个代码块被 synchronized 修饰了，当一个线程获取了对应的锁，并执行该代码块时，其他线程便只能一直等待，等待获取锁的线程释放锁，而这里获取锁的线程释放锁只会有两种情况：

1.  获取锁的线程执行完了该代码块，然后线程释放对锁的占有；
2.  线程执行发生异常，此时 JVM 会让线程自动释放锁。

如果这个获取锁的线程被阻塞了，但是又没有释放锁，其他线程就只能一直等待。

使用 synchronized，如果多个线程都只是进行读操作，所以当一个线程在进行读操作时，其他线程只能等待无法进行读操作。

可重入锁可中断锁公平锁读写锁

## Lock 和 ReentrantLock

### 要点

ReentrantLock 实现了 Lock 接口，所以支持 Lock 的所有方法。

ReentrantLock 字面意为可重入锁。

### 源码

#### Lock

```java
public interface Lock {
    /** 获取锁，如果锁已被其他线程获取，则进行等待。 */
    void lock();
    /**
     * 获取锁时，如果线程正在等待获取锁，则这个线程能够响应中断，即中断线程的等待状态。
     * 当两个线程同时通过lock.lockInterruptibly()想获取某个锁时，假若此时线程A获取到了锁，
     * 而线程B只有在等待，那么对线程B调用threadB.interrupt()* 方法能够中断线程B的等待过程。
     */
    void lockInterruptibly() throws InterruptedException;
    /**
     * tryLock()方法是有返回值的，它表示用来尝试获取锁，如果获取成功，则返回 true。
     * 如果获取失败（即锁已被其他线程获取），则返回 false，也就说这个方法无论如何都会立即返回。在拿不到锁时不会一直在那等待。
     */
    boolean tryLock();
    /**
     * 这个方法在拿不到锁时会等待一定的时间，在时间期限之内如果还拿不到锁，就返回false。
     * 如果如果一开始拿到锁或者在等待期间内拿到了锁，则返回true。
     */
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
    /** 释放锁 */
    void unlock();
    /**
     * 返回绑定到此Lock实例的新的Condition实例。
     * 在等待条件之前，锁必须由当前线程保持。对 Condition.await 的调用将在等待之前以原子方式释放锁，
     * 并在等待返回之前重新获取锁。
     */
    Condition newCondition();
}
```

#### ReentrantLock

##### Sync

* `Sync` 这个类是 `ReentrantLock` 的同步控制核心。使用 AQS 状态来表示锁的保留数。
* `Sync` 是一个抽象类，有两个子类：
  * `FairSync` - 公平锁版本。
  * `NonfairSync` - 非公平锁版本。

##### 重要属性

`Sync` 的实例。

```java
private final Sync sync;
```

##### 重要方法

**构造方法**

```java
// 默认初始化一个非公平的重入锁
public ReentrantLock() {}
// 根据 boolean 值选择初始化一个公平的或不公平的重入锁
public ReentrantLock(boolean fair) {}
```

**实现 Lock 接口的方法**

以下方法的功能可以参考 [Lock](#lock-接口) 中的描述。具体实现完全基于 `Sync` 类中提供的方法。

```java
void lock();
void lockInterruptibly() throws InterruptedException;
boolean tryLock();
boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
void unlock();
Condition newCondition();
```

### 示例

```java
public class ReentrantLockDemo {

    private ArrayList<Integer> arrayList = new ArrayList<Integer>();
    private Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        final ReentrantLockDemo demo = new ReentrantLockDemo();
        new Thread(() -> demo.insert(Thread.currentThread())).start();
        new Thread(() -> demo.insert(Thread.currentThread())).start();
    }

    private void insert(Thread thread) {
        lock.lock();
        try {
            System.out.println(thread.getName() + "得到了锁");
            for (int i = 0; i < 5; i++) {
                arrayList.add(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(thread.getName() + "释放了锁");
            lock.unlock();
        }
    }
}
```

## ReadWriteLock 和 ReentrantReadWriteLock

### 要点

作用

对于特定的资源，ReadWriteLock 允许多个线程同时对其执行读操作，但是只允许一个线程对其执行写操作。

ReentrantReadWriteLock 实现了 ReadWriteLock 接口，所以它是一个读写锁。

原理

“读-读”线程之间不存在互斥关系。

“读-写”线程、“写-写”线程之间存在互斥关系。

ReadWriteLock 维护一对相关的锁。一个是读锁；一个是写锁。将读写锁分开，有利于提高并发效率。

<p align="center">
  <img src="https://raw.githubusercontent.com/dunwu/javase-notes/master/images/concurrent/ReadWriteLock.jpg">
</p>

### 源码

#### ReadWriteLock

```java
public interface ReadWriteLock {
    /**
     * 返回用于读操作的锁
     */
    Lock readLock();

    /**
     * 返回用于写操作的锁
     */
    Lock writeLock();
}
```

### 示例

```
public class ReentrantReadWriteLockDemo {

    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    public static void main(String[] args) {
        final ReentrantReadWriteLockDemo demo = new ReentrantReadWriteLockDemo();
        new Thread(() -> demo.get(Thread.currentThread())).start();
        new Thread(() -> demo.get(Thread.currentThread())).start();
    }

    public synchronized void get(Thread thread) {
        rwl.readLock().lock();
        try {
            long start = System.currentTimeMillis();

            while (System.currentTimeMillis() - start <= 1) {
                System.out.println(thread.getName() + "正在进行读操作");
            }
            System.out.println(thread.getName() + "读操作完毕");
        } finally {
            rwl.readLock().unlock();
        }
    }
}
```

## AQS

> AQS 作为构建锁或者其他同步组件的基础框架

### 要点

作用：AQS，AbstractQueuedSynchronizer，即队列同步器。它是构建锁或者其他同步组件的基础框架（如 ReentrantLock、ReentrantReadWriteLock、Semaphore 等）。

场景：在 LOCK 包中的相关锁(常用的有 ReentrantLock、 ReadWriteLock)都是基于 AQS 来构建。然而这些锁都没有直接来继承 AQS，而是定义了一个 Sync 类去继承 AQS。那么为什么要这样呢?because:锁面向的是使用用户，而同步器面向的则是线程控制，那么在锁的实现中聚合同步器而不是直接继承 AQS 就可以很好的隔离二者所关注的事情。

原理：AQS 在内部定义了一个 int 变量 state，用来表示同步状态。AQS 通过一个双向的 FIFO 同步队列来完成同步状态的管理，当有线程获取锁失败后，就被添加到队列末尾。

### 源码

AbstractQueuedSynchronizer 继承自 AbstractOwnableSynchronize。

#### 同步队列

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

AQS 维护了一个 Node 类型双链表，通过 head 和 tail 指针进行访问。

<p align="center">
  <img src="http://www.liuhaihua.cn/wp-content/uploads/2018/05/7zei6fI.png">
</p>

##### Node

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

很显然，Node 是一个双链表结构。

waitStatus 5 个状态值的含义：

1.  CANCELLED（1） - 该节点的线程可能由于超时或被中断而处于被取消(作废)状态，一旦处于这个状态，节点状态将一直处于 CANCELLED(作废)，因此应该从队列中移除.
2.  SIGNAL（-1） - 当前节点为 SIGNAL 时，后继节点会被挂起，因此在当前节点释放锁或被取消之后必须被唤醒(unparking)其后继结点.
3.  CONDITION（-2） - 该节点的线程处于等待条件状态，不会被当作是同步队列上的节点,直到被唤醒(signal)，设置其值为 0,重新进入阻塞状态。
4.  PROPAGATE（-3） - 下一个 acquireShared 应无条件传播。
5.  0 - 非以上状态。

#### 获取独占锁

##### acquire

```java
/**
 * 先调用 tryAcquire 查看同步状态。
 * 如果成功获取同步状态，则结束方法，直接返回；
 * 反之，则先调用 addWaiter，再调用 acquireQueued。
 */
public final void acquire(int arg) {
        if (!tryAcquire(arg) &&
            acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
}
```

##### addWaiter

`addWaiter` 方法的作用是将当前线程插入等待同步队列的队尾。

```java
private Node addWaiter(Node mode) {
    // 1. 将当前线程构建成 Node 类型
    Node node = new Node(Thread.currentThread(), mode);
    // 2. 判断尾指针是否为 null
    Node pred = tail;
    if (pred != null) {
        // 2.2 将当前节点插入队列尾部
        node.prev = pred;
        if (compareAndSetTail(pred, node)) {
            pred.next = node;
            return node;
        }
    }
    // 2.1. 尾指针为 null，说明当前节点是第一个加入队列的节点
    enq(node);
    return node;
}
```

##### enq

`enq` 方法的作用是通过自旋（死循环），不断尝试利用 CAS 操作将节点插入队列尾部，直到成功为止。

```java
private Node enq(final Node node) {
    // 设置死循环，是为了不断尝试 CAS 操作，直到成功为止
    for (;;) {
        Node t = tail;
        if (t == null) {
            // 1. 构造头结点（必须初始化，需要领会双链表的精髓）
            if (compareAndSetHead(new Node()))
                tail = head;
        } else {
            // 2. 通过 CAS 操作将节点插入队列尾部
            node.prev = t;
            if (compareAndSetTail(t, node)) {
                t.next = node;
                return t;
            }
        }
    }
}
```

##### acquireQueued

`acquireQueued` 方法的作用是通过自旋（死循环），不断尝试为等待队列中线程获取独占锁。

```java
final boolean acquireQueued(final Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                // 1. 获得当前节点的上一个节点
                final Node p = node.predecessor();
                // 2. 当前节点能否获取独占式锁
                // 2.1 如果当前节点是队列中第一个节点，并且成功获取同步状态，即可以获得独占式锁
                // 说明：当前节点的上一个节点是头指针，即意味着当前节点是队列中第一个节点。
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return interrupted;
                }
                // 2.2 获取锁失败，线程进入等待状态等待获取独占式锁
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
```

acquireQueued Before

<p align="center">
  <img src="https://raw.githubusercontent.com/dunwu/javase-notes/master/images/concurrent/aqs-acquireQueued-before.png">
</p>

`setHead` 方法

```java
private void setHead(Node node) {
    head = node;
    node.thread = null;
    node.prev = null;
}
```

将当前节点通过 setHead 方法设置为队列的头结点，然后将之前的头结点的 next 域设置为 null，并且 pre 域也为 null，即与队列断开，无任何引用方便 GC 时能够将内存进行回收。

<p align="center">
  <img src="https://raw.githubusercontent.com/dunwu/javase-notes/master/images/concurrent/aqs-acquireQueued-after.png">
</p>

##### shouldParkAfterFailedAcquire

`shouldParkAfterFailedAcquire` 方法的作用是使用 `compareAndSetWaitStatus(pred, ws, Node.SIGNAL)` 将节点状态由 INITIAL 设置成 SIGNAL，表示当前线程阻塞。

当 compareAndSetWaitStatus 设置失败，则说明 shouldParkAfterFailedAcquire 方法返回 false，重新进入外部方法 acquireQueued。由于 acquireQueued 方法中是死循环，会再一次执行 shouldParkAfterFailedAcquire，直至 compareAndSetWaitStatus 设置节点状态位为 SIGNAL。

```java
private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
    int ws = pred.waitStatus;
    if (ws == Node.SIGNAL)
        return true;
    if (ws > 0) {
        do {
            node.prev = pred = pred.prev;
        } while (pred.waitStatus > 0);
        pred.next = node;
    } else {
        compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
    }
    return false;
}
```

##### parkAndCheckInterrupt

`parkAndCheckInterrupt` 方法的作用是调用 `LookSupport.park` 方法，该方法是用来阻塞当前线程的。

```
private final boolean parkAndCheckInterrupt() {
    LockSupport.park(this);
    return Thread.interrupted();
}
```

##### acquire 流程

综上所述，就是 acquire 的完整流程。可以以一幅图来说明：

<p align="center">
  <img src="https://raw.githubusercontent.com/dunwu/javase-notes/master/images/concurrent/aqs-acquire-flow.png">
</p>

#### 释放独占锁

##### release

release 方法以独占模式发布。如果 tryRelease 返回 true，则通过解锁一个或多个线程来实现。这个方法可以用来实现 Lock.unlock 方法。

```java
public final boolean release(int arg) {
    // 判断同步状态释放是否成功
    if (tryRelease(arg)) {
        Node h = head;
        if (h != null && h.waitStatus != 0)
            unparkSuccessor(h);
        return true;
    }
    return false;
}
```

##### unparkSuccessor

unparkSuccessor 方法作用是唤醒 node 的下一个节点。

头指针的后继节点

```java
private void unparkSuccessor(Node node) {
    /*
     * 如果状态为负值（即可能需要信号），请尝试清除信号。
     * 如果失败或状态由于等待线程而改变也是正常的。
     */
    int ws = node.waitStatus;
    if (ws < 0)
        compareAndSetWaitStatus(node, ws, 0);

    /**
     * 释放后继节点的线程。
     * 如果状态为 CANCELLED 放或节点明显为空，
     * 则从尾部向后遍历以找到状态不是 CANCELLED 的后继节点。
     */
    Node s = node.next;
    if (s == null || s.waitStatus > 0) {
        s = null;
        for (Node t = tail; t != null && t != node; t = t.prev)
            if (t.waitStatus <= 0)
                s = t;
    }
    // 后继节点不为 null 时唤醒该线程
    if (s != null)
        LockSupport.unpark(s.thread);
}
```

##### 总结

* 线程获取锁失败，线程被封装成 Node 进行入队操作，核心方法在于 addWaiter()和 enq()，同时 enq()完成对同步队列的头结点初始化工作以及 CAS 操作失败的重试 ;
* 线程获取锁是一个自旋的过程，当且仅当 当前节点的前驱节点是头结点并且成功获得同步状态时，节点出队即该节点引用的线程获得锁，否则，当不满足条件时就会调用 LookSupport.park()方法使得线程阻塞 ；
* 释放锁的时候会唤醒后继节点；

#### 获取可中断的独占锁

##### acquireInterruptibly

Lock 能响应中断，这是相较于 synchronized 的一个显著优点。

那么 Lock 响应中断的特性是如何实现的？答案就在 acquireInterruptibly 方法中。

```java
public final void acquireInterruptibly(int arg)
        throws InterruptedException {
    if (Thread.interrupted())
        throw new InterruptedException();
    if (!tryAcquire(arg))
        // 线程获取锁失败
        doAcquireInterruptibly(arg);
}
```

##### doAcquireInterruptibly

获取同步状态失败后就会调用 doAcquireInterruptibly 方法

```java
private void doAcquireInterruptibly(int arg)
    throws InterruptedException {
	// 将节点插入到同步队列中
    final Node node = addWaiter(Node.EXCLUSIVE);
    boolean failed = true;
    try {
        for (;;) {
            final Node p = node.predecessor();
            // 获取锁出队
			if (p == head && tryAcquire(arg)) {
                setHead(node);
                p.next = null; // help GC
                failed = false;
                return;
            }
            if (shouldParkAfterFailedAcquire(p, node) &&
                parkAndCheckInterrupt())
				// 线程中断抛异常
                throw new InterruptedException();
        }
    } finally {
        if (failed)
            cancelAcquire(node);
    }
}
```

与 acquire 方法逻辑几乎一致，唯一的区别是当 parkAndCheckInterrupt 返回 true 时（即线程阻塞时该线程被中断），代码抛出被中断异常。

#### 获取超时等待式的独占锁

##### tryAcquireNanos

通过调用 lock.tryLock(timeout,TimeUnit) 方式达到超时等待获取锁的效果，该方法会在三种情况下才会返回：

1.  在超时时间内，当前线程成功获取了锁；
2.  当前线程在超时时间内被中断；
3.  超时时间结束，仍未获得锁返回 false。

我们仍然通过采取阅读源码的方式来学习底层具体是怎么实现的，该方法会调用 AQS 的方法 tryAcquireNanos

```java
public final boolean tryAcquireNanos(int arg, long nanosTimeout)
        throws InterruptedException {
    if (Thread.interrupted())
        throw new InterruptedException();
    return tryAcquire(arg) ||
		// 实现超时等待的效果
        doAcquireNanos(arg, nanosTimeout);
}
```

##### doAcquireNanos

```java
private boolean doAcquireNanos(int arg, long nanosTimeout)
        throws InterruptedException {
    if (nanosTimeout <= 0L)
        return false;
	// 1. 根据超时时间和当前时间计算出截止时间
    final long deadline = System.nanoTime() + nanosTimeout;
    final Node node = addWaiter(Node.EXCLUSIVE);
    boolean failed = true;
    try {
        for (;;) {
            final Node p = node.predecessor();
			// 2. 当前线程获得锁出队列
            if (p == head && tryAcquire(arg)) {
                setHead(node);
                p.next = null; // help GC
                failed = false;
                return true;
            }
			// 3.1 重新计算超时时间
            nanosTimeout = deadline - System.nanoTime();
            // 3.2 超时返回 false
			if (nanosTimeout <= 0L)
                return false;
			// 3.3 线程阻塞等待
            if (shouldParkAfterFailedAcquire(p, node) &&
                nanosTimeout > spinForTimeoutThreshold)
                LockSupport.parkNanos(this, nanosTimeout);
            // 3.4 线程被中断抛出被中断异常
			if (Thread.interrupted())
                throw new InterruptedException();
        }
    } finally {
        if (failed)
            cancelAcquire(node);
    }
}
```

<p align="center">
  <img src="https://raw.githubusercontent.com/dunwu/javase-notes/master/images/concurrent/aqs-doAcquireNanos-flow.png">
</p>

#### 获取共享锁

##### acquireShared

```java
public final void acquireShared(int arg) {
    if (tryAcquireShared(arg) < 0)
        doAcquireShared(arg);
}
```

尝试获取共享锁失败，调用 doAcquireShared

```java
private void doAcquireShared(int arg) {
    final Node node = addWaiter(Node.SHARED);
    boolean failed = true;
    try {
        boolean interrupted = false;
        for (;;) {
            final Node p = node.predecessor();
            if (p == head) {
                int r = tryAcquireShared(arg);
                if (r >= 0) {
					// 当该节点的前驱节点是头结点且成功获取同步状态
                    setHeadAndPropagate(node, r);
                    p.next = null; // help GC
                    if (interrupted)
                        selfInterrupt();
                    failed = false;
                    return;
                }
            }
            if (shouldParkAfterFailedAcquire(p, node) &&
                parkAndCheckInterrupt())
                interrupted = true;
        }
    } finally {
        if (failed)
            cancelAcquire(node);
    }
}
```

以上代码和 acquireQueued 的代码逻辑十分相似，区别仅在于自旋的条件以及节点出队的操作有所不同。

#### 释放共享锁

##### releaseShared

```java
public final boolean releaseShared(int arg) {
    if (tryReleaseShared(arg)) {
        doReleaseShared();
        return true;
    }
    return false;
}
```

##### doReleaseShared

当成功释放同步状态之后即 tryReleaseShared 会继续执行 doReleaseShared 方法

发送后继信号并确保传播。 （注意：对于独占模式，如果需要信号，释放就相当于调用头的 unparkSuccessor。）

```java
private void doReleaseShared() {
    for (;;) {
        Node h = head;
        if (h != null && h != tail) {
            int ws = h.waitStatus;
            if (ws == Node.SIGNAL) {
                if (!compareAndSetWaitStatus(h, Node.SIGNAL, 0))
                    continue;            // loop to recheck cases
                unparkSuccessor(h);
            }
            else if (ws == 0 &&
                     !compareAndSetWaitStatus(h, 0, Node.PROPAGATE))
                // 如果 CAS 失败，继续自旋
                continue;
        }
        // 如果头指针变化，break
        if (h == head)
            break;
    }
}
```

#### 获取可中断的共享锁

acquireSharedInterruptibly 方法与 acquireInterruptibly 几乎一致，不再赘述。

#### 获取超时等待式的共享锁

tryAcquireSharedNanos 方法与 tryAcquireNanos 几乎一致，不再赘述。

## 资料

* [Java 并发编程实战](https://item.jd.com/10922250.html)
* [Java 并发编程的艺术](https://item.jd.com/11740734.html)
* http://www.cnblogs.com/dolphin0520/p/3923167.html
* https://zhuanlan.zhihu.com/p/27134110
* https://t.hao0.me/java/2016/04/01/aqs.html
* http://ju.outofmemory.cn/entry/353762
