---
title: Java 并发之同步工具
date: 2019-12-24 23:52:25
categories:
  - Java
  - JavaCore
  - 并发
tags:
  - Java
  - JavaCore
  - 并发
  - CountDownLatch
  - CyclicBarrier
  - Semaphore
permalink: /pages/43b6c7f6/
---

# Java 并发之同步工具

## CountDownLatch

`CountDownLatch` 通过计数器实现线程间的“等待-通知”机制，适用于分阶段任务同步，但不可重复使用。`CountDownLatch` 允许一个或多个线程等待，直到其他线程完成一组操作后再继续执行。

典型场景：主线程等待多个子线程完成任务后再汇总结果。

### CountDownLatch 原理

**核心机制**

- **计数器初始化**：创建时指定初始计数值（如 `new CountDownLatch(3)`）。
- **计数递减**：子线程完成任务后调用 `countDown()`，计数器减 1（线程不会阻塞）。
- **等待阻塞**：主线程调用 `await()` 会阻塞，直到计数器归零（或超时）。

**关键特性**

- **一次性**：计数器归零后无法重置，需重新创建实例。
- **非中断递减**：`countDown()` 不受线程中断影响，但 `await()` 可被中断。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2025/10/4050a64b7ad141ecb59cdbda6abf0bcb.png)

`CountDownLatch` 是共享锁的一种实现，它默认构造 AQS 的 `state` 值为 `count`。当线程使用 `countDown()` 方法时，其实使用了`tryReleaseShared`方法以 CAS 的操作来减少 `state`，直至 `state` 为 0 。当调用 `await()` 方法的时候，如果 `state` 不为 0，那就证明任务还没有执行完毕，`await()` 方法就会一直阻塞，也就是说 `await()` 方法之后的语句不会被执行。直到`count` 个线程调用了`countDown()`使 state 值被减为 0，或者调用`await()`的线程被中断，该线程才会从阻塞中被唤醒，`await()` 方法之后的语句得到执行。

### CountDownLatch 核心方法

`CountDownLatch` 唯一的构造方法：

```java
// 初始化计数器
public CountDownLatch(int count) {};
```

`CountDownLatch` 的重要方法：

```java
public void await() throws InterruptedException { };
public boolean await(long timeout, TimeUnit unit) throws InterruptedException { };
public void countDown() { };
```

说明：

- `await()` - 调用 `await()` 方法的线程会被挂起，它会等待直到 count 值为 0 才继续执行。
- `await(long timeout, TimeUnit unit)` - 和 `await()` 类似，只不过等待一定的时间后 count 值还没变为 0 的话就会继续执行
- `countDown()` - 将统计值 count 减 1

### CountDownLatch 使用示例

【示例】CountDownLatch 使用示例

```java
public class CountDownLatchDemo {

    public static void main(String[] args) {
        final CountDownLatch latch = new CountDownLatch(2);

        new Thread(new MyThread(latch)).start();

        try {
            System.out.println("等待 2 个子线程执行完毕。..");
            latch.await();
            System.out.println("2 个子线程已经执行完毕");
            System.out.println("继续执行主线程");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class MyThread implements Runnable {

        private CountDownLatch latch;

        public MyThread(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            System.out.println("子线程" + Thread.currentThread().getName() + "正在执行");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("子线程" + Thread.currentThread().getName() + "执行完毕");
            latch.countDown();
        }

    }

}
```

## CyclicBarrier

`CyclicBarrier` 通过“线程互相等待”实现协同，适合需要**多轮同步**的场景，且具备更高的灵活性。

- 让**一组线程互相等待**，直到所有线程都到达某个屏障点（Barrier）后，再一起继续执行。
- 适用于**分阶段并行任务**（如多线程计算后合并结果）。

### CyclicBarrier 工作原理

| **机制**       | **说明**                                                     |
| -------------- | ------------------------------------------------------------ |
| **屏障初始化** | 创建时指定参与线程数 `N`（如 `new CyclicBarrier(3)`）及可选的**回调任务**（触发后执行）。 |
| **线程等待**   | 每个线程调用 `await()` 时会被阻塞，直到**所有 `N` 个线程都调用 `await()`**。 |
| **屏障突破**   | 当所有线程到达屏障点后：<br> 1. 执行回调任务（若设置）；<br> 2. 所有线程被唤醒，继续执行后续逻辑。 |
| **重置能力**   | 屏障被突破后，**自动重置**，可重复使用（区别于 `CountDownLatch`）。 |

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2025/10/eb0b20ab653b47e09fd3cea6ca186e78.png)

### CyclicBarrier 核心方法

`CyclicBarrier` 提供了 2 个构造方法

```java
public CyclicBarrier(int parties) {}
public CyclicBarrier(int parties, Runnable barrierAction) {}
```

说明：

- `parties` - `parties` 数相当于一个阈值，当有 `parties` 数量的线程在等待时， `CyclicBarrier` 处于栅栏状态。
- `barrierAction` - 当 `CyclicBarrier` 处于栅栏状态时执行的动作。

`CyclicBarrier` 的重要方法：

```java
public int await() throws InterruptedException, BrokenBarrierException {}
public int await(long timeout, TimeUnit unit)
        throws InterruptedException,
               BrokenBarrierException,
               TimeoutException {}
// 将屏障重置为初始状态
public void reset() {}
```

说明：

- `await()` - 等待调用 `await()` 的线程数达到屏障数。如果当前线程是最后一个到达的线程，并且在构造函数中提供了非空屏障操作，则当前线程在允许其他线程继续之前运行该操作。如果在屏障动作期间发生异常，那么该异常将在当前线程中传播并且屏障被置于断开状态。
- `await(long timeout, TimeUnit unit)` - 相比于 `await()` 方法，这个方法让这些线程等待至一定的时间，如果还有线程没有到达栅栏状态就直接让到达栅栏状态的线程执行后续任务。
- `reset()` - 将屏障重置为初始状态。

### CyclicBarrier 使用示例

【示例】CyclicBarrier 使用示例

```java
public class CyclicBarrierDemo {

    final static int N = 4;

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(N,
            new Runnable() {
                @Override
                public void run() {
                    System.out.println("当前线程" + Thread.currentThread().getName());
                }
            });

        for (int i = 0; i < N; i++) {
            MyThread myThread = new MyThread(barrier);
            new Thread(myThread).start();
        }
    }

    static class MyThread implements Runnable {

        private CyclicBarrier cyclicBarrier;

        MyThread(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            System.out.println("线程" + Thread.currentThread().getName() + "正在写入数据。..");
            try {
                Thread.sleep(3000); // 以睡眠来模拟写入数据操作
                System.out.println("线程" + Thread.currentThread().getName() + "写入数据完毕，等待其他线程写入完毕");
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

    }

}
```

## Semaphore

**`Semaphore` 译为信号量，是一种同步机制，用于控制多线程对共享资源的访问**。信号量是由计算机科学家 Edsger Dijkstra 于 1965 年提出的，用于解决所谓的“临界区”问题，即多个进程或线程试图同时访问共享资源（如打印机、内存缓冲区等）时可能出现的问题。

### 信号量模型

信号量模型还是很简单的，可以简单概括为：**一个计数器，一个等待队列，三个方法**。在信号量模型里，计数器和等待队列对外是透明的，所以只能通过信号量模型提供的三个方法来访问它们，这三个方法分别是：init()、down() 和 up()。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2025/10/27ee310c93bf4ab48ec62ec57c4e7cfb.png)

- 这三个方法详细的语义具体如下所示。

  - init()：设置计数器的初始值。
  - down()：计数器的值减 1；如果此时计数器的值小于 0，则当前线程将被阻塞，否则当前线程可以继续执行。
  - up()：计数器的值加 1；如果此时计数器的值小于或者等于 0，则唤醒等待队列中的一个线程，并将其从等待队列中移除。

  这里提到的 init()、down() 和 up() 三个方法都是原子性的，并且这个原子性是由信号量模型的实现方保证的。在 Java 中，信号量模型是由 java.util.concurrent.Semaphore 实现的，Semaphore 这个类能够保证这三个方法都是原子操作。

  信号量模型里面，down()、up() 这两个操作历史上最早称为 P 操作和 V 操作，所以信号量模型也被称为 PV 原语。

### Semaphore 使用

`Semaphore` 提供了 2 个构造方法：

```java
// 参数 permits 表示许可数目，即同时可以允许多少线程进行访问
public Semaphore(int permits) {}
// 参数 fair 表示是否是公平的，即等待时间越久的越先获取许可
public Semaphore(int permits, boolean fair) {}
```

说明：

- `permits` - 初始化固定数量的 permit。
- `fair` - 设置是否为公平模式。所谓公平，是指等待久的优先获取 permit。

`Semaphore` 的重要方法：

```java
// 获取 1 个许可
public void acquire() throws InterruptedException {}
//获取 permits 个许可
public void acquire(int permits) throws InterruptedException {}
// 释放 1 个许可
public void release() {}
//释放 permits 个许可
public void release(int permits) {}
```

说明：

- `acquire()` - 获取 1 个 permit。
- `acquire(int permits)` - 获取 permits 数量的 permit。
- `release()` - 释放 1 个 permit。
- `release(int permits)` - 释放 permits 数量的 permit。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2025/10/27ee310c93bf4ab48ec62ec57c4e7cfb.png)

【示例】Semaphore 使用示例

```java
public class SemaphoreDemo {

    private static final int THREAD_COUNT = 30;

    private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT);

    private static Semaphore semaphore = new Semaphore(10);

    public static void main(String[] args) {
        for (int i = 0; i < THREAD_COUNT; i++) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        System.out.println("save data");
                        semaphore.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        threadPool.shutdown();
    }

}
```

### Semaphore 原理

`Semaphore` 是共享锁的一种实现，它默认构造 AQS 的 `state` 值为 `permits`，你可以将 `permits` 的值理解为许可证的数量，只有拿到许可证的线程才能执行。

调用`semaphore.acquire()` ，线程尝试获取许可证，如果 `state >= 0` 的话，则表示可以获取成功。如果获取成功的话，使用 CAS 操作去修改 `state` 的值 `state=state-1`。如果 `state<0` 的话，则表示许可证数量不足。此时会创建一个 Node 节点加入阻塞队列，挂起当前线程。

```java
/**
 *  获取 1 个许可证
 */
public void acquire() throws InterruptedException {
    sync.acquireSharedInterruptibly(1);
}
/**
 * 共享模式下获取许可证，获取成功则返回，失败则加入阻塞队列，挂起线程
 */
public final void acquireSharedInterruptibly(int arg)
    throws InterruptedException {
    if (Thread.interrupted())
      throw new InterruptedException();
        // 尝试获取许可证，arg 为获取许可证个数，当可用许可证数减当前获取的许可证数结果小于 0, 则创建一个节点加入阻塞队列，挂起当前线程。
    if (tryAcquireShared(arg) < 0)
      doAcquireSharedInterruptibly(arg);
}
```

调用`semaphore.release();` ，线程尝试释放许可证，并使用 CAS 操作去修改 `state` 的值 `state=state+1`。释放许可证成功之后，同时会唤醒同步队列中的一个线程。被唤醒的线程会重新尝试去修改 `state` 的值 `state=state-1` ，如果 `state>=0` 则获取令牌成功，否则重新进入阻塞队列，挂起线程。

```java
// 释放一个许可证
public void release() {
    sync.releaseShared(1);
}

// 释放共享锁，同时会唤醒同步队列中的一个线程。
public final boolean releaseShared(int arg) {
    //释放共享锁
    if (tryReleaseShared(arg)) {
      //唤醒同步队列中的一个线程
      doReleaseShared();
      return true;
    }
    return false;
}
```

### 实现一个限流器

Semaphore 最重要的特性是：**Semaphore 可以允许多个线程访问一个临界区**。

Semaphore 在现实中有很多应用场景：

- 各种池化资源，例如连接池、对象池、线程池等；
- 信号量限流（例如 Hystrix 就支持信号量限流模式）；

【示例】一个基于信号量实现的简单对象限流器

```java
public class SemaphoreRateLimit {

    public static void main(String[] args) {
        // 创建对象池，大小为 10
        ObjectPool<Long, String> pool = new ObjectPool<>(10, 2L);
        // 通过对象池获取 t，之后执行
        pool.exec(t -> {
            System.out.println(t);
            return t.toString();
        });
    }

    static class ObjectPool<T, R> {

        final List<T> pool;
        // 用信号量实现限流器
        final Semaphore sem;

        // 构造函数
        ObjectPool(int size, T t) {
            pool = new Vector<T>() { };
            for (int i = 0; i < size; i++) {
                pool.add(t);
            }
            sem = new Semaphore(size);
        }

        // 利用对象池的对象，调用 func
        R exec(Function<T, R> func) {
            T t = null;
            try {
                sem.acquire();
                t = pool.remove(0);
                return func.apply(t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                pool.add(t);
                sem.release();
                return null;
            }
        }

    }

}
```

在这个方法里面，我们首先调用 acquire() 方法（与之匹配的是在 finally 里面调用 release() 方法），假设对象池的大小是 10，信号量的计数器初始化为 10，那么前 10 个线程调用 acquire() 方法，都能继续执行，相当于通过了信号量，而其他线程则会阻塞在 acquire() 方法上。对于通过信号量的线程，我们为每个线程分配了一个对象 t（这个分配工作是通过 pool.remove(0) 实现的），分配完之后会执行一个回调函数 func，而函数的参数正是前面分配的对象 t ；执行完回调函数之后，它们就会释放对象（这个释放工作是通过 pool.add(t) 实现的），同时调用 release() 方法来更新信号量的计数器。如果此时信号量里计数器的值小于等于 0，那么说明有线程在等待，此时会自动唤醒等待的线程。

## CountDownLatch vs. CyclicBarrier vs. Semaphore

在 Java 并发编程中，`CountDownLatch`、`CyclicBarrier` 和 `Semaphore` 均用于线程协作，但设计目标、可重用性与适用场景差异明显。核心对比如下：

| 特性             | CountDownLatch                 | CyclicBarrier              | Semaphore                                  |
| ---------------- | ------------------------------ | -------------------------- | ------------------------------------------ |
| **可重用性**     | 一次性，不可重置               | 可重复使用                 | 可重复使用                                 |
| **核心用途**     | 主线程等待 N 个子任务完成      | 多线程在屏障点同步         | 控制并发访问资源的线程数                   |
| **计数器方向**   | 递减（`countDown()`）          | 递减（`await()`）          | 获取 / 释放许可（`acquire()`/`release()`） |
| **协作关系**     | 主从线程同步                   | 线程间相互等待             | 线程与资源协调                             |
| **是否支持回调** | 否                             | 是（屏障达成触发）         | 否                                         |
| **典型场景**     | 多任务并行后汇总、压测并发起点 | 多阶段并行计算、回合制同步 | 限流、数据库连接池、信号量                 |
| **底层机制**     | AQS                            | ReentrantLock + Condition  | AQS                                        |

**原理简述**

- `CountDownLatch` 内部维护计数器，`countDown()` 递减，`await()` 阻塞至计数器为 0 再释放所有等待线程。
- `CyclicBarrier` 基于锁与条件变量，`await()` 使线程阻塞，当所有线程到达屏障点时统一唤醒，并可触发回调，随后自动重置，支持循环使用。
- `Semaphore` 基于 AQS，用 “许可” 控制并发访问，`acquire()` 获取许可，无许可则阻塞；`release()` 释放许可并唤醒等待线程，可设置公平或非公平模式。

**选择建议**

- **主线程等待子任务全部完成** → `CountDownLatch`
- **多阶段并发任务需在阶段间同步** → `CyclicBarrier`
- **控制资源的最大并发访问数** → `Semaphore`

## 典型应用场景

### 场景一：CountDownLatch 等待多个任务完成

```java
CountDownLatch latch = new CountDownLatch(3);
executor.submit(() -> { doTask1(); latch.countDown(); });
executor.submit(() -> { doTask2(); latch.countDown(); });
executor.submit(() -> { doTask3(); latch.countDown(); });
latch.await(); // 等待所有任务完成
```

### 场景二：CyclicBarrier 多线程同步点

多个线程到达屏障后同时继续执行，适用于并行计算的分阶段同步。

### 场景三：Semaphore 限流

```java
Semaphore semaphore = new Semaphore(10); // 最多 10 个并发
semaphore.acquire();
try { accessResource(); } finally { semaphore.release(); }
```

## 最佳实践

1. **CountDownLatch 用一次性的场景**：它不能重置，需重复用改用 CyclicBarrier。
2. **Semaphore 用于限流**：如控制数据库连接数、API 并发限制。
3. **Exchanger 用于线程间数据交换**：如生产者-消费者的数据传递。

## 常见问题

### Q1：CountDownLatch 和 CyclicBarrier 有什么区别？

CountDownLatch 一次性使用，等待多个线程完成；CyclicBarrier 可重复使用，等待所有线程到达屏障。

### Q2：Semaphore 的公平模式是什么？

fair=true 时按等待顺序获取许可，避免饥饿；fair=false 时允许抢占，性能更高。

### Q3：Phaser 是什么？

Phaser 是 JDK 7 引入的更灵活的同步工具，可以动态注册/注销参与者，适合分阶段并行计算。

## 参考资料

- [《Java 并发编程实战》](https://book.douban.com/subject/10484692/)
- [《Java 并发编程的艺术》](https://book.douban.com/subject/26591326/)
- [Java 并发编程：CountDownLatch、CyclicBarrier 和 Semaphore](https://www.cnblogs.com/dolphin0520/p/3920397.html)
