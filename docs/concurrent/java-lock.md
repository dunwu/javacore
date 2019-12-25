# 锁

> **📦 本文以及示例源码已归档在 [javacore](https://github.com/dunwu/javacore)**
>
> 本文内容基于 JDK1.8。

<!-- TOC depthFrom:2 depthTo:3 -->

- [概述](#概述)
    - [概念](#概念)
    - [为什么用 Lock、ReadWriteLock](#为什么用-lockreadwritelock)
- [Lock 和 ReentrantLock](#lock-和-reentrantlock)
    - [要点](#要点)
    - [源码](#源码)
    - [示例](#示例)
- [ReadWriteLock 和 ReentrantReadWriteLock](#readwritelock-和-reentrantreadwritelock)
    - [要点](#要点-1)
    - [源码](#源码-1)
    - [示例](#示例-1)
- [AQS](#aqs)
    - [要点](#要点-2)
    - [源码](#源码-2)
- [资料](#资料)

<!-- /TOC -->

## 概述

### 概念

#### 公平锁/非公平锁

公平锁是指多个线程按照申请锁的顺序来获取锁。

非公平锁是指多个线程获取锁的顺序并不是按照申请锁的顺序，有可能后申请的线程比先申请的线程优先获取锁。有可能，会造成优先级反转或者饥饿现象。

对于 Java `ReentrantLock`而言，通过构造函数指定该锁是否是公平锁，默认是非公平锁。非公平锁的优点在于吞吐量比公平锁大。

对于`Synchronized`而言，也是一种非公平锁。由于其并不像`ReentrantLock`是通过 AQS 的来实现线程调度，所以并没有任何办法使其变成公平锁。

#### 可重入锁

可重入锁又名递归锁，是指在同一个线程在外层方法获取锁的时候，在进入内层方法会自动获取锁。

说的有点抽象，下面会有一个代码的示例。对于 Java `ReentrantLock`而言, 他的名字就可以看出是一个可重入锁，其名字是`Re entrant Lock`重新进入锁。对于`Synchronized`而言,也是一个可重入锁。可重入锁的一个好处是可一定程度避免死锁。

```
synchronized void setA() throws Exception{
    Thread.sleep(1000);
    setB();
}

synchronized void setB() throws Exception{
    Thread.sleep(1000);
}
```

上面的代码就是一个可重入锁的一个特点，如果不是可重入锁的话，setB 可能不会被当前线程执行，可能造成死锁。

#### 独享锁/共享锁

独享锁是指该锁一次只能被一个线程所持有。

共享锁是指该锁可被多个线程所持有。

对于 Java `ReentrantLock`而言，其是独享锁。但是对于 Lock 的另一个实现类`ReadWriteLock`，其读锁是共享锁，其写锁是独享锁。读锁的共享锁可保证并发读是非常高效的，读写，写读 ，写写的过程是互斥的。独享锁与共享锁也是通过 AQS 来实现的，通过实现不同的方法，来实现独享或者共享。对于`Synchronized`而言，当然是独享锁。

#### 互斥锁/读写锁

上面讲的独享锁/共享锁就是一种广义的说法，互斥锁/读写锁就是具体的实现。互斥锁在 Java 中的具体实现就是`ReentrantLock`
读写锁在 Java 中的具体实现就是`ReadWriteLock`

#### 乐观锁/悲观锁

乐观锁与悲观锁不是指具体的什么类型的锁，而是指看待并发同步的角度。悲观锁认为对于同一个数据的并发操作，一定是会发生修改的，哪怕没有修改，也会认为修改。因此对于同一个数据的并发操作，悲观锁采取加锁的形式。悲观的认为，不加锁的并发操作一定会出问题。乐观锁则认为对于同一个数据的并发操作，是不会发生修改的。在更新数据的时候，会采用尝试更新，不断重新的方式更新数据。乐观的认为，不加锁的并发操作是没有事情的。

从上面的描述我们可以看出，悲观锁适合写操作非常多的场景，乐观锁适合读操作非常多的场景，不加锁会带来大量的性能提升。悲观锁在 Java 中的使用，就是利用各种锁。乐观锁在 Java 中的使用，是无锁编程，常常采用的是 CAS 算法，典型的例子就是原子类，通过 CAS 自旋实现原子操作的更新。

#### 分段锁

分段锁其实是一种锁的设计，并不是具体的一种锁，对于`ConcurrentHashMap`而言，其并发的实现就是通过分段锁的形式来实现高效的并发操作。我们以`ConcurrentHashMap`来说一下分段锁的含义以及设计思想，`ConcurrentHashMap`中的分段锁称为 Segment，它即类似于 HashMap（JDK7 与 JDK8 中 HashMap 的实现）的结构，即内部拥有一个 Entry 数组，数组中的每个元素既是一个链表；同时又是一个 ReentrantLock（Segment 继承了 ReentrantLock)。当需要 put 元素的时候，并不是对整个 hashmap 进行加锁，而是先通过 hashcode 来知道他要放在那一个分段中，然后对这个分段进行加锁，所以当多线程 put 的时候，只要不是放在一个分段中，就实现了真正的并行的插入。但是，在统计 size 的时候，可就是获取 hashmap 全局信息的时候，就需要获取所有的分段锁才能统计。分段锁的设计目的是细化锁的粒度，当操作不需要更新整个数组的时候，就仅仅针对数组中的一项进行加锁操作。

#### 偏向锁/轻量级锁/重量级锁

这三种锁是指锁的状态，并且是针对`Synchronized`。在 Java 5 通过引入锁升级的机制来实现高效`Synchronized`。

这三种锁的状态是通过对象监视器在对象头中的字段来表明的。

偏向锁是指一段同步代码一直被一个线程所访问，那么该线程会自动获取锁。降低获取锁的代价。

轻量级锁是指当锁是偏向锁的时候，被另一个线程所访问，偏向锁就会升级为轻量级锁，其他线程会通过自旋的形式尝试获取锁，不会阻塞，提高性能。

重量级锁是指当锁为轻量级锁的时候，另一个线程虽然是自旋，但自旋不会一直持续下去，当自旋一定次数的时候，还没有获取到锁，就会进入阻塞，该锁膨胀为重量级锁。重量级锁会让其他申请的线程进入阻塞，性能降低。

#### 自旋锁

在 Java 中，自旋锁是指尝试获取锁的线程不会立即阻塞，而是采用循环的方式去尝试获取锁，这样的好处是减少线程上下文切换的消耗，缺点是循环会消耗 CPU。

### 为什么用 Lock、ReadWriteLock

- synchronized 的缺陷

  - 被 synchronized 修饰的方法或代码块，只能被一个线程访问。如果这个线程被阻塞，其他线程也只能等待。
  - synchronized 不能响应中断。
  - synchronized 没有超时机制。
  - synchronized 只能是非公平锁。

- Lock、ReadWriteLock 相较于 synchronized，解决了以上的缺陷：
  - Lock 可以手动释放锁（synchronized 获取锁和释放锁都是自动的），以避免死锁。
  - Lock 可以响应中断
  - Lock 可以设置超时时间，避免一致等待
  - Lock 可以选择公平锁或非公平锁两种模式
  - ReadWriteLock 将读写锁分离，从而使读写操作分开，有效提高并发性。

## Lock 和 ReentrantLock

### 要点

如果采用 Lock，必须主动去释放锁，并且在发生异常时，不会自动释放锁。因此一般来说，使用 Lock 必须在 try catch 块中进行，并且将释放锁的操作放在 finally 块中进行，以保证锁一定被被释放，防止死锁的发生。

`lock()` 方法的作用是获取锁。如果锁已被其他线程获取，则进行等待。

`tryLock()` 方法的作用是尝试获取锁，如果成功，则返回 true；如果失败（即锁已被其他线程获取），则返回 false。也就是说，这个方法无论如何都会立即返回，获取不到锁时不会一直等待。

`tryLock(long time, TimeUnit unit)` 方法和 `tryLock()` 方法是类似的，区别仅在于这个方法在获取不到锁时会等待一定的时间，在时间期限之内如果还获取不到锁，就返回 false。如果如果一开始拿到锁或者在等待期间内拿到了锁，则返回 true。

`lockInterruptibly()` 方法比较特殊，当通过这个方法去获取锁时，如果线程正在等待获取锁，则这个线程能够响应中断，即中断线程的等待状态。也就使说，当两个线程同时通过 `lock.lockInterruptibly()` 想获取某个锁时，假若此时线程 A 获取到了锁，而线程 B 只有在等待，那么对线程 B 调用 `threadB.interrupt()` 方法能够中断线程 B 的等待过程。由于 `lockInterruptibly()` 的声明中抛出了异常，所以 `lock.lockInterruptibly()` 必须放在 try 块中或者在调用 `lockInterruptibly()` 的方法外声明抛出 `InterruptedException`。

> 🔔 注意：当一个线程获取了锁之后，是不会被 interrupt() 方法中断的。因为本身在前面的文章中讲过单独调用 interrupt() 方法不能中断正在运行过程中的线程，只能中断阻塞过程中的线程。因此当通过 lockInterruptibly() 方法获取某个锁时，如果不能获取到，只有进行等待的情况下，是可以响应中断的。

`unlock()` 方法的作用是释放锁。

ReentrantLock 是唯一实现了 Lock 接口的类。

ReentrantLock 字面意为可重入锁。

### 源码

#### Lock 接口定义

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

#### ReentrantLock 属性和方法

ReentrantLock 的核心方法当然是 Lock 中的方法（具体实现完全基于 `Sync` 类中提供的方法）。

此外，ReentrantLock 有两个构造方法，功能参考下面源码片段中的注释。

```java
// 同步机制完全依赖于此
private final Sync sync;
// 默认初始化 sync 的实例为非公平锁（NonfairSync）
public ReentrantLock() {}
// 根据 boolean 值选择初始化 sync 的实例为公平的锁（FairSync）或不公平锁（NonfairSync）
public ReentrantLock(boolean fair) {}
```

#### Sync

- `Sync` 类是 `ReentrantLock` 的内部类，也是一个抽象类。
- `ReentrantLock` 的同步机制几乎完全依赖于`Sync`。使用 AQS 状态来表示锁的保留数（详细介绍参见 [AQS](#aqs)）。
- `Sync` 是一个抽象类，有两个子类：
  - `FairSync` - 公平锁版本。
  - `NonfairSync` - 非公平锁版本。

<p align="center">
  <img src="http://dunwu.test.upcdn.net/cs/java/javacore/concurrent/ReentrantLock-diagram.png">
</p>

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

👉 [更多示例](https://github.com/dunwu/javacore/tree/master/codes/javacore-concurrent/src/main/java/io/github/dunwu/javacore/concurrent/lock)

## ReadWriteLock 和 ReentrantReadWriteLock

### 要点

对于特定的资源，ReadWriteLock 允许多个线程同时对其执行读操作，但是只允许一个线程对其执行写操作。

ReadWriteLock 维护一对相关的锁。一个是读锁；一个是写锁。将读写锁分开，有利于提高并发效率。

ReentrantReadWriteLock 实现了 ReadWriteLock 接口，所以它是一个读写锁。

“读-读”线程之间不存在互斥关系。

“读-写”线程、“写-写”线程之间存在互斥关系。

<p align="center">
  <img src="http://dunwu.test.upcdn.net/cs/java/javacore/concurrent/ReadWriteLock.jpg">
</p>

### 源码

#### ReadWriteLock 接口定义

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

```java
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

> AQS 作为构建锁或者其他同步组件的基础框架，有必要好好了解一下其原理。

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
  <img src="http://dunwu.test.upcdn.net/cs/java/javacore/concurrent/aqs-acquireQueued-before.png">
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
  <img src="http://dunwu.test.upcdn.net/cs/java/javacore/concurrent/aqs-acquireQueued-after.png">
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
  <img src="http://dunwu.test.upcdn.net/cs/java/javacore/concurrent/aqs-acquire-flow.png">
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

- 线程获取锁失败，线程被封装成 Node 进行入队操作，核心方法在于 addWaiter()和 enq()，同时 enq()完成对同步队列的头结点初始化工作以及 CAS 操作失败的重试 ;
- 线程获取锁是一个自旋的过程，当且仅当 当前节点的前驱节点是头结点并且成功获得同步状态时，节点出队即该节点引用的线程获得锁，否则，当不满足条件时就会调用 LookSupport.park()方法使得线程阻塞 ；
- 释放锁的时候会唤醒后继节点；

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
  <img src="http://dunwu.test.upcdn.net/cs/java/javacore/concurrent/aqs-doAcquireNanos-flow.png">
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

- [Java并发编程实战](https://item.jd.com/10922250.html)
- [Java并发编程的艺术](https://item.jd.com/11740734.html)
- http://www.cnblogs.com/dolphin0520/p/3923167.html
- https://zhuanlan.zhihu.com/p/27134110
- https://t.hao0.me/java/2016/04/01/aqs.html
- http://ju.outofmemory.cn/entry/353762
- https://blog.csdn.net/u012403290/article/details/64910926?locationNum=11&fps=1
- https://www.cnblogs.com/qifengshi/p/6831055.html
