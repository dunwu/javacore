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

<!-- TOC depthFrom:2 depthTo:3 -->

- [概述](#概述)
  - [为什么会出现 Lock？](#为什么会出现-lock)
  - [synchronized](#synchronized)
- [Lock 接口](#lock-接口)
  - [要点](#要点)
  - [源码](#源码)
- [ReentrantLock](#reentrantlock)
  - [要点](#要点-1)
  - [源码](#源码-1)
  - [示例](#示例)
- [ReadWriteLock](#readwritelock)
  - [要点](#要点-2)
  - [源码](#源码-2)
- [ReentrantReadWriteLock](#reentrantreadwritelock)
  - [要点](#要点-3)
  - [示例](#示例-1)
- [AQS](#aqs)
  - [要点](#要点-4)
  - [源码](#源码-3)
  - [独占锁](#独占锁)
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

## Lock 接口

### 要点

### 源码

Lock 接口

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

## ReentrantLock

### 要点

作用：字面意为可重入锁。原理

### 源码

ReentrantLock 实现了 Lock 接口，所以支持 Lock 的所有方法。

#### 内部类

* `Sync` 这个类是 `ReentrantLock` 的同步控制核心。使用 AQS 状态来表示锁的保留数。
* `Sync` 是一个抽象类，有两个子类：
  * `FairSync` - 公平锁版本。
  * `NonfairSync` - 非公平锁版本。

#### 重要属性

`Sync` 的实例。

```java
private final Sync sync;
```

#### 重要方法

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

## ReadWriteLock

### 要点

作用：对于特定的资源，ReadWriteLock 允许多个线程同时对其执行读操作，但是只允许一个线程对其执行写操作。

原理

“读-读”线程之间不存在互斥关系。

“读-写”线程、“写-写”线程之间存在互斥关系。

ReadWriteLock 维护一对相关的锁。一个是读锁；一个是写锁。将读写锁分开，有利于提高并发效率。

<p align="center">
  <img src="https://raw.githubusercontent.com/dunwu/javase-notes/master/images/concurrent/ReadWriteLock.jpg">
</p>

### 源码

ReadWriteLock 接口

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

## ReentrantReadWriteLock

### 要点

作用：ReentrantReadWriteLock 实现了 ReadWriteLock 接口，所以它是一个读写锁。

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

1. CANCELLED（1） - 该节点的线程可能由于超时或被中断而处于被取消(作废)状态，一旦处于这个状态，节点状态将一直处于 CANCELLED(作废)，因此应该从队列中移除.
2. SIGNAL（-1） - 当前节点为 SIGNAL 时，后继节点会被挂起，因此在当前节点释放锁或被取消之后必须被唤醒(unparking)其后继结点.
3. CONDITION（-2） - 该节点的线程处于等待条件状态，不会被当作是同步队列上的节点,直到被唤醒(signal)，设置其值为 0,重新进入阻塞状态。
4. PROPAGATE（-3） - 下一个 acquireShared 应无条件传播。
5. 0 - 非以上状态。

```java
public final void acquire(int arg) {
    if (!tryAcquire(arg) &&
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
        selfInterrupt();
}
```

### 独占锁

独占锁的获取（acquire方法）

## 资料

* [Java 并发编程实战](https://item.jd.com/10922250.html)
* [Java 并发编程的艺术](https://item.jd.com/11740734.html)
* http://www.cnblogs.com/dolphin0520/p/3923167.html
* https://zhuanlan.zhihu.com/p/27134110
* https://t.hao0.me/java/2016/04/01/aqs.html
* http://ju.outofmemory.cn/entry/353762
