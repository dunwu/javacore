# Java 容器之 Queue

> **📦 本文以及示例源码已归档在 [javacore](https://dunwu.github.io/javacore/#/)**

## Queue 架构

<div align="center">
<img src="http://dunwu.test.upcdn.net/cs/java/javacore/container/Queue-diagrams.png" />
</div>

## Queue 接口

Queue 接口定义如下：

```java
public interface Queue<E> extends Collection<E> {}
```

## BlockingQueue 接口

BlockingQueue 接口定义如下：

```java
public interface BlockingQueue<E> extends Queue<E> {}
```

BlockingQueue 顾名思义，是一个阻塞队列。

在 BlockingQueue 中，如果获取队列元素但是队列为空时，会阻塞，等待队列中有元素再返回；如果添加元素时，如果队列已满，那么等到队列可以放入新元素时再放入。

BlockingQueue 对插入操作、移除操作、获取元素操作提供了四种不同的方法用于不同的场景中使用：

1.  抛出异常；
2.  返回特殊值（null 或 true/false，取决于具体的操作）；
3.  阻塞等待此操作，直到这个操作成功；
4.  阻塞等待此操作，直到成功或者超时指定时间。

总结如下：

|         | *Throws exception* | *Special value* | *Blocks*         | *Times out*          |
| ------- | ------------------ | --------------- | ---------------- | -------------------- |
| Insert  | add(e)             | offer(e)        | put(e)           | offer(e, time, unit) |
| Remove  | remove()           | poll()          | take()           | poll(time, unit)     |
| Examine | element()          | peek()          | *not applicable* | *not applicable*     |

BlockingQueue 的各个实现类都遵循了这些规则。

BlockingQueue 不接受 null 值元素。

## AbstractQueue 抽象类

AbstractQueue 抽象类定义如下：

```java
public abstract class AbstractQueue<E>
    extends AbstractCollection<E>
    implements Queue<E> {}
```

AbstractQueue 类提供 Queue 接口的骨干实现，以最大限度地减少实现 Queue 接口所需的工作。

## PriorityQueue 类

PriorityQueue 类定义如下：

```java
public class PriorityQueue<E> extends AbstractQueue<E>
    implements java.io.Serializable {}
```

### PriorityQueue 要点

1.  PriorityQueue 实现了 Serializable，支持序列化。
2.  PriorityQueue 类是基于优先级堆实现的无界优先级队列。
3.  PriorityQueue 中的元素根据自然顺序或 Comparator 提供的顺序排序。
4.  PriorityQueue 不接受 null 值元素。
5.  PriorityQueue 不是线程安全的。

## PriorityBlockingQueue 类

PriorityBlockingQueue 类定义如下：

```java
public class PriorityBlockingQueue<E> extends AbstractQueue<E>
    implements BlockingQueue<E>, java.io.Serializable {}
```

### PriorityBlockingQueue 要点

1.  PriorityBlockingQueue 实现了 BlockingQueue，也是一个阻塞队列。
2.  PriorityBlockingQueue 实现了 Serializable，支持序列化。
3.  PriorityBlockingQueue 可以视为 PriorityQueue 的线程安全版本。
4.  PriorityBlockingQueue 不接受 null 值元素。
5.  PriorityBlockingQueue 的插入操作 put 方法不会 block，因为它是无界队列（take 方法在队列为空的时候会阻塞）。

### PriorityBlockingQueue 原理

PriorityBlockingQueue 有两个重要成员：

```java
private transient Object[] queue;
private final ReentrantLock lock;
```

- queue 是一个 Object 数组，用于保存 PriorityBlockingQueue 的元素。
- 而可重入锁 lock 则用于在执行插入、删除操作时，保证这个方法在当前线程释放锁之前，其他线程不能访问。

PriorityBlockingQueue 的容量虽然有初始化大小，但是不限制大小，如果当前容量已满，插入新元素时会自动扩容。

## LinkedBlockingQueue 类

LinkedBlockingQueue 类定义如下：

```java
public class LinkedBlockingQueue<E> extends AbstractQueue<E>
        implements BlockingQueue<E>, java.io.Serializable {}
```

### LinkedBlockingQueue 要点

1.  LinkedBlockingQueue 实现了 BlockingQueue，也是一个阻塞队列。
2.  LinkedBlockingQueue 实现了 Serializable，支持序列化。
3.  LinkedBlockingQueue 是基于单链表实现的阻塞队列，可以当做无界队列也可以当做有界队列来使用。
4.  LinkedBlockingQueue 中元素按照插入顺序保存（FIFO）。

### LinkedBlockingQueue 原理

```java
// 队列容量
private final int capacity;

// 队列中的元素数量
private final AtomicInteger count = new AtomicInteger(0);

// 队头
private transient Node<E> head;

// 队尾
private transient Node<E> last;

// take, poll, peek 等读操作的方法需要获取到这个锁
private final ReentrantLock takeLock = new ReentrantLock();

// 如果读操作的时候队列是空的，那么等待 notEmpty 条件
private final Condition notEmpty = takeLock.newCondition();

// put, offer 等写操作的方法需要获取到这个锁
private final ReentrantLock putLock = new ReentrantLock();

// 如果写操作的时候队列是满的，那么等待 notFull 条件
private final Condition notFull = putLock.newCondition();
```

这里用了两个锁，两个 Condition，简单介绍如下：

- takeLock 和 notEmpty 搭配：如果要获取（take）一个元素，需要获取 takeLock 锁，但是获取了锁还不够，如果队列此时为空，还需要队列不为空（notEmpty）这个条件（Condition）。
- putLock 需要和 notFull 搭配：如果要插入（put）一个元素，需要获取 putLock 锁，但是获取了锁还不够，如果队列此时已满，还需要队列不是满的（notFull）这个条件（Condition）。

## ArrayBlockingQueue 类

ArrayBlockingQueue 类定义如下：

```java
public class ArrayBlockingQueue<E> extends AbstractQueue<E>
        implements BlockingQueue<E>, java.io.Serializable {}
```

### ArrayBlockingQueue 要点

1.  ArrayBlockingQueue 实现了 BlockingQueue，也是一个阻塞队列。
2.  ArrayBlockingQueue 实现了 Serializable，支持序列化。
3.  ArrayBlockingQueue 是基于数组实现的无界阻塞队列。

### ArrayBlockingQueue 原理

ArrayBlockingQueue 的重要成员如下：

```java
// 用于存放元素的数组
final Object[] items;
// 下一次读取操作的位置
int takeIndex;
// 下一次写入操作的位置
int putIndex;
// 队列中的元素数量
int count;

// 以下几个就是控制并发用的同步器
final ReentrantLock lock;
private final Condition notEmpty;
private final Condition notFull;
```

ArrayBlockingQueue 实现并发同步的原理就是，读操作和写操作都需要获取到 AQS 独占锁才能进行操作。

- 如果队列为空，这个时候读操作的线程进入到读线程队列排队，等待写线程写入新的元素，然后唤醒读线程队列的第一个等待线程。
- 如果队列已满，这个时候写操作的线程进入到写线程队列排队，等待读线程将队列元素移除，然后唤醒写线程队列的第一个等待线程。

对于 ArrayBlockingQueue，我们可以在构造的时候指定以下三个参数：

1.  队列容量，其限制了队列中最多允许的元素个数；
2.  指定独占锁是公平锁还是非公平锁。非公平锁的吞吐量比较高，公平锁可以保证每次都是等待最久的线程获取到锁；
3.  可以指定用一个集合来初始化，将此集合中的元素在构造方法期间就先添加到队列中。

## SynchronousQueue

SynchronousQueue 定义如下：

```java
public class SynchronousQueue<E> extends AbstractQueue<E>
    implements BlockingQueue<E>, java.io.Serializable {}
```

1.  SynchronousQueue 这个类，不过它在线程池的实现类 ScheduledThreadPoolExecutor 中得到了应用。
2.  SynchronousQueue 的队列其实是虚的，其不提供任何空间（一个都没有）来存储元素。数据必须从某个写线程交给某个读线程，而不是写到某个队列中等待被消费。
3.  SynchronousQueue 中不能使用 peek 方法（在这里这个方法直接返回 null），peek 方法的语义是只读取不移除，显然，这个方法的语义是不符合 SynchronousQueue 的特征的。
4.  SynchronousQueue 也不能被迭代，因为根本就没有元素可以拿来迭代的。
5.  虽然 SynchronousQueue 间接地实现了 Collection 接口，但是如果你将其当做 Collection 来用的话，那么集合是空的。
6.  当然，SynchronousQueue 也不允许传递 null 值的（并发包中的容器类好像都不支持插入 null 值，因为 null 值往往用作其他用途，比如用于方法的返回值代表操作失败）。

## 资料

[解读 Java 并发队列 BlockingQueue](http://www.importnew.com/28053.html)
