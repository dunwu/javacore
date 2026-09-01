---
title: Java 并发之 AQS
date: 2019-12-26 23:11:52
categories:
  - Java
  - JavaCore
  - 并发
tags:
  - Java
  - JavaCore
  - 并发
  - AQS
  - 独占锁
  - 共享锁
permalink: /pages/80608f0e/
---

# Java 并发之 AQS

## AQS 简介

**AQS** 是 `AbstractQueuedSynchronizer` 的缩写，即 **队列同步器**，顾名思义，其主要作用是处理同步。它是并发锁和很多同步工具类的实现基石（如 `ReentrantLock`、`ReentrantReadWriteLock`、`CountDownLatch`、`Semaphore`、`FutureTask` 等）。

**AQS 提供了对锁和同步器的通用能力支持 **。在 `java.util.concurrent.locks` 包中的相关锁（常用的有 `ReentrantLock`、 `ThreadPoolExecutor`）都是基于 AQS 来实现。这些锁都没有直接继承 AQS，而是定义了一个 `Sync` 类去继承 AQS。为什么要这样呢？因为锁面向的是使用用户，而同步器面向的则是线程控制，那么在锁的实现中聚合同步器而不是直接继承 AQS 就可以很好的隔离二者所关注的事情。

## AQS 的应用

AQS 定义两种资源共享方式：`Exclusive`（独占，只有一个线程能执行，如 `ReentrantLock`）和 `Share`（共享，多个线程可同时执行，如 `Semaphore` / `CountDownLatch`）。

### 独占锁 API

获取、释放独占锁的主要 API 如下：

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

### 共享锁 API

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

## AQS 的原理

AQS 核心思想是，如果被请求的共享资源空闲，则将当前请求资源的线程设置为有效的工作线程，并且将共享资源设置为锁定状态；如果被请求的共享资源被占用，那么就需要一套线程阻塞等待以及被唤醒时锁分配的机制。这个机制是基于 **CLH 锁** （Craig, Landin, and Hagersten locks） 的变体实现的，将暂时获取不到锁的线程加入到队列中。

CLH 本是一个单向队列，AQS 中的队列采用了 CLH 的变体，是一个虚拟的 FIFO 双向队列（虚拟的双向队列，是指不存在结点实例，仅存在结点之间的关联关系），暂时获取不到锁的线程将被加入到该队列中。AQS 将每条请求共享资源的线程封装成一个 CLH 队列锁的一个结点（Node）来实现锁的分配。在 CLH 队列锁中，一个节点表示一个线程，它保存着线程的引用（thread）、 当前节点在队列中的状态（waitStatus）、前驱节点（prev）、后继节点（next）。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2024/09/9344f54d09cd4b1daa2bdc6955ce1190.png)

AQS 的核心原理图：

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2024/09/bb71372467e243a3982038d11d9cc00e.png)

### AQS 的数据结构

先看一下 `AbstractQueuedSynchronizer` 的定义：

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

阅读 AQS 的源码，可以发现：AQS 继承自 `AbstractOwnableSynchronize`，它有以下核心属性：

- `state` - AQS 使用一个整型的 `volatile` 变量来 **维护同步状态**。这个整数状态的意义由子类来赋予，如 `ReentrantLock` 中该状态值表示所有者线程已经重复获取该锁的次数；`Semaphore` 中该状态值表示剩余的许可数量。
- `head` 和 `tail` - AQS **维护了一个 `Node` 类型（AQS 的内部类）的双向队列来完成同步状态的管理 **。这个双向队列是一个双向的 FIFO 队列，通过 `head` 和 `tail` 指针进行访问。当 **有线程获取锁失败后，就被添加到队列末尾 **。

再来看一下 `Node` 的源码，很显然，Node 是一个双向队列结构：

```java
static final class Node {
    /** 该等待同步的节点处于共享模式 */
    static final Node SHARED = new Node();
    /** 该等待同步的节点处于独占模式 */
    static final Node EXCLUSIVE = null;

    /** 线程等待状态，状态值有：0、1、-1、-2、-3 */
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

  	/** 和节点是否共享有关 */
    Node nextWaiter;
}
```

属性说明：

| 方法和属性值 | 含义                   |
| :----------- | :--------------------- |
| waitStatus   | 当前节点在队列中的状态 |
| thread       | 表示处于该节点的线程   |
| prev         | 前驱指针               |
| next         | 后继指针               |

`waitStatus` 是一个整型的 `volatile` 变量，用来维护 AQS 同步队列中线程节点的状态。`waitStatus` 有五个状态值：

- 0 - 一个 Node 被初始化的时候的默认值
- `CANCELLED(1)` - 表示线程获取锁的请求已经取消了
- `SIGNAL(-1)` - 表示线程已经准备好了，就等资源释放了
- `CONDITION(-2)` - 表示节点在等待队列中，节点线程等待唤醒
- `PROPAGATE(-3)` - 当前线程处在 SHARED 情况下，该字段才会使用

### 独占锁的获取和释放

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2024/09/f8b4d70e224945f7a25fd9ff4c976d3f.png)

#### 获取独占锁

AQS 中使用 `acquire(int arg)` 方法获取独占锁的相关源码如下：

```java
public final void acquire(int arg) {
	if (!tryAcquire(arg) &&
		acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
		selfInterrupt();
}

// 利用 CAS 操作将当前线程加入等待队列队尾
private Node addWaiter(Node mode) {
	Node node = new Node(Thread.currentThread(), mode);
	Node pred = tail;
	if (pred != null) {
		node.prev = pred;
		if (compareAndSetTail(pred, node)) {
			pred.next = node;
			return node;
		}
	}
	enq(node);
	return node;
}

// 自旋尝试为等待队列中的线程节点获取独占锁
final boolean acquireQueued(final Node node, int arg) {
	boolean failed = true;
	try {
		boolean interrupted = false;
		for (;;) {
			final Node p = node.predecessor();
            // 获取锁成功，退出
			if (p == head && tryAcquire(arg)) {
				setHead(node);
				p.next = null; // help GC
				failed = false;
				return interrupted;
			}
            // 线程中断
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

其大致流程如下：

1. 先通过 `tryAcquire` 尝试获取同步状态，如果获取同步状态成功，则结束方法，直接返回。
2. 若不成功，调用 `addWaiter` 方法，利用 CAS 操作将当前线程加入等待队列队尾。
3. 接着，自旋尝试为等待队列中的线程节点获取独占锁，直到获取成功或线程中断。

#### 释放独占锁

AQS 中使用 `acquire(int arg)` 方法获取独占锁的相关源码如下：

```java
public final boolean release(int arg) {
    // 尝试释放锁
	if (tryRelease(arg)) {
		Node h = head;
        // 如果队列不为空，唤醒下一个节点中的线程
		if (h != null && h.waitStatus != 0)
			unparkSuccessor(h);
		return true;
	}
	return false;
}

private void unparkSuccessor(Node node) {

	int ws = node.waitStatus;
	if (ws < 0)
		compareAndSetWaitStatus(node, ws, 0);

	Node s = node.next;
	if (s == null || s.waitStatus > 0) {
		s = null;
		for (Node t = tail; t != null && t != node; t = t.prev)
			if (t.waitStatus <= 0)
				s = t;
	}
	if (s != null)
		LockSupport.unpark(s.thread);
}
```

1. 先尝试获取解锁线程的同步状态，如果获取同步状态不成功，则结束方法，直接返回。
2. 如果获取同步状态成功且队列不为空，AQS 会尝试唤醒下一个节点中的线程。

#### 获取可中断的独占锁

AQS 中使用 `acquireInterruptibly(int arg)` 方法获取可中断的独占锁。

`acquireInterruptibly(int arg)` 实现方式 **相较于获取独占锁方法（ `acquire`）非常相似**，区别仅在于它会 **通过 `Thread.interrupted` 检测当前线程是否被中断**，如果是，则立即抛出中断异常（`InterruptedException`）。

#### 限时获取独占锁

AQS 中使用 `tryAcquireNanos(int arg)` 方法获取超时等待的独占锁。

doAcquireNanos 的实现方式 **相较于获取独占锁方法（ `acquire`）非常相似**，区别在于它会根据超时时间和当前时间计算出截止时间。在获取锁的流程中，会不断判断是否超时，如果超时，直接返回 false；如果没超时，则用 `LockSupport.parkNanos` 来阻塞当前线程。

### 共享锁的获取和释放

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2024/09/883fe5890caa430a819a538a69cac402.png)

#### 获取共享锁

AQS 中使用 `acquireShared(int arg)` 方法获取共享锁。

`acquireShared` 方法和 `acquire` 方法的逻辑很相似，区别仅在于自旋的条件以及节点出队的操作有所不同。

成功获得共享锁的条件如下：

- `tryAcquireShared(arg)` 返回值大于等于 0 （这意味着共享锁的 permit 还没有用完）。
- 当前节点的前驱节点是头结点。

#### 释放共享锁

AQS 中使用 `releaseShared(int arg)` 方法释放共享锁。

`releaseShared` 首先会尝试释放同步状态，如果成功，则解锁一个或多个后继线程节点。释放共享锁和释放独占锁流程大体相似，区别在于：

对于独占模式，如果需要 SIGNAL，释放仅相当于调用头节点的 `unparkSuccessor`。

#### 获取可中断的共享锁

AQS 中使用 `acquireSharedInterruptibly(int arg)` 方法获取可中断的共享锁。

`acquireSharedInterruptibly` 方法与 `acquireInterruptibly` 几乎一致，不再赘述。

#### 限时获取共享锁

AQS 中使用 `tryAcquireSharedNanos(int arg)` 方法获取超时等待式的共享锁。

`tryAcquireSharedNanos` 方法与 `tryAcquireNanos` 几乎一致，不再赘述。

## 自定义同步器

同步器的设计是基于模板方法模式的，如果需要自定义同步器一般的方式是这样（模板方法模式很经典的一个应用）：

1. 使用者继承 `AbstractQueuedSynchronizer` 并重写指定的方法。
2. 将 AQS 组合在自定义同步组件的实现中，并调用其模板方法，而这些模板方法会调用使用者重写的方法。

这和我们以往通过实现接口的方式有很大区别，这是模板方法模式很经典的一个运用。

**AQS 使用了模板方法模式，自定义同步器时需要重写下面几个 AQS 提供的钩子方法：**

```java
// 独占方式。尝试获取资源，成功则返回 true，失败则返回 false。
protected boolean tryAcquire(int)
// 独占方式。尝试释放资源，成功则返回 true，失败则返回 false。
protected boolean tryRelease(int)
// 共享方式。尝试获取资源。负数表示失败；0 表示成功，但没有剩余可用资源；正数表示成功，且有剩余资源。
protected int tryAcquireShared(int)
// 共享方式。尝试释放资源，成功则返回 true，失败则返回 false。
protected boolean tryReleaseShared(int)
// 该线程是否正在独占资源。只有用到 condition 才需要去实现它。
protected boolean isHeldExclusively()
```

**什么是钩子方法呢？** 钩子方法是一种被声明在抽象类中的方法，一般使用 `protected` 关键字修饰，它可以是空方法（由子类实现），也可以是默认实现的方法。模板设计模式通过钩子方法控制固定步骤的实现。

## 典型应用场景

### 场景一：自定义互斥锁

```java
public class MyLock extends AbstractQueuedSynchronizer {
    protected boolean tryAcquire(int arg) {
        return compareAndSetState(0, 1);
    }
    protected boolean tryRelease(int arg) {
        setState(0); return true;
    }
}
```

### 场景二：自定义共享锁（如 Semaphore）

AQS 的共享模式允许多个线程同时获取，适用于限流场景。

### 场景三：理解 J.U.C 工具底层原理

ReentrantLock、CountDownLatch、Semaphore 等都基于 AQS 实现。

## 最佳实践

1. **一般不需要直接使用 AQS**：使用基于 AQS 构建的高级工具即可。
2. **自定义同步器时覆写 tryAcquire/tryRelease**：不要直接操作 state。
3. **理解 CLH 队列**：这是 AQS 等待队列的核心算法。

## 常见问题

### Q1：AQS 的核心原理是什么？

AQS 维护一个 volatile int state 和一个 FIFO 等待队列（CLH 变体）。通过 CAS 修改 state 获取锁，失败则将线程加入队列等待。

### Q2：AQS 的独占模式和共享模式有什么区别？

独占模式同一时刻只有一个线程能获取锁（如 ReentrantLock）；共享模式允许多个线程同时获取（如 Semaphore、CountDownLatch）。

### Q3：为什么 AQS 使用 CLH 队列？

CLH 队列无锁且 FIFO 公平，自旋等待时只访问前驱节点，减少缓存一致性流量。

## 参考资料

- [《Java 并发编程实战》](https://book.douban.com/subject/10484692/)
- [《Java 并发编程的艺术》](https://book.douban.com/subject/26591326/)
- [极客时间教程 - Java 并发编程实战](https://time.geekbang.org/column/intro/100023901)
- [Java 并发编程：Lock](https://www.cnblogs.com/dolphin0520/p/3923167.html)
- [深入学习 java 同步器 AQS](https://zhuanlan.zhihu.com/p/27134110)
- [AbstractQueuedSynchronizer 框架](https://t.hao0.me/java/2016/04/01/aqs.html)
- [Java 中的锁分类](https://www.cnblogs.com/qifengshi/p/6831055.html)
