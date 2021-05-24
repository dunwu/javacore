# Java 并发工具类

> **📦 本文以及示例源码已归档在 [javacore](https://github.com/dunwu/javacore/)**
>
> JDK 的 `java.util.concurrent` 包（即 J.U.C）中提供了几个非常有用的并发工具类。

<!-- TOC depthFrom:2 depthTo:3 -->

- [1. CountDownLatch](#1-countdownlatch)
- [2. CyclicBarrier](#2-cyclicbarrier)
- [3. Semaphore](#3-semaphore)
- [4. 总结](#4-总结)
- [5. 参考资料](#5-参考资料)

<!-- /TOC -->

## 1. CountDownLatch

> 字面意思为 **递减计数锁**。用于**控制一个线程等待多个线程**。
>
> `CountDownLatch` 维护一个计数器 count，表示需要等待的事件数量。`countDown` 方法递减计数器，表示有一个事件已经发生。调用 `await` 方法的线程会一直阻塞直到计数器为零，或者等待中的线程中断，或者等待超时。

![img](https://raw.githubusercontent.com/dunwu/images/dev/cs/java/javacore/concurrent/CountDownLatch.png)

`CountDownLatch` 是基于 AQS(`AbstractQueuedSynchronizer`) 实现的。

`CountDownLatch` 唯一的构造方法：

```java
// 初始化计数器
public CountDownLatch(int count) {};
```

说明：

- count 为统计值。

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

示例：

```java
public class CountDownLatchDemo {

    public static void main(String[] args) {
        final CountDownLatch latch = new CountDownLatch(2);

        new Thread(new MyThread(latch)).start();
        new Thread(new MyThread(latch)).start();

        try {
            System.out.println("等待2个子线程执行完毕...");
            latch.await();
            System.out.println("2个子线程已经执行完毕");
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

## 2. CyclicBarrier

> 字面意思是 **循环栅栏**。**`CyclicBarrier` 可以让一组线程等待至某个状态（遵循字面意思，不妨称这个状态为栅栏）之后再全部同时执行**。之所以叫循环栅栏是因为：**当所有等待线程都被释放以后，`CyclicBarrier` 可以被重用**。
>
> `CyclicBarrier` 维护一个计数器 count。每次执行 `await` 方法之后，count 加 1，直到计数器的值和设置的值相等，等待的所有线程才会继续执行。

`CyclicBarrier` 是基于 `ReentrantLock` 和 `Condition` 实现的。

`CyclicBarrier` 应用场景：`CyclicBarrier` 在并行迭代算法中非常有用。

![img](https://raw.githubusercontent.com/dunwu/images/dev/cs/java/javacore/concurrent/CyclicBarrier.png)

`CyclicBarrier` 提供了 2 个构造方法

```java
public CyclicBarrier(int parties) {}
public CyclicBarrier(int parties, Runnable barrierAction) {}
```

> 说明：
>
> - `parties` - `parties` 数相当于一个阈值，当有 `parties` 数量的线程在等待时， `CyclicBarrier` 处于栅栏状态。
> - `barrierAction` - 当 `CyclicBarrier` 处于栅栏状态时执行的动作。

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

> 说明：
>
> - `await()` - 等待调用 `await()` 的线程数达到屏障数。如果当前线程是最后一个到达的线程，并且在构造函数中提供了非空屏障操作，则当前线程在允许其他线程继续之前运行该操作。如果在屏障动作期间发生异常，那么该异常将在当前线程中传播并且屏障被置于断开状态。
> - `await(long timeout, TimeUnit unit)` - 相比于 `await()` 方法，这个方法让这些线程等待至一定的时间，如果还有线程没有到达栅栏状态就直接让到达栅栏状态的线程执行后续任务。
> - `reset()` - 将屏障重置为初始状态。

示例：

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
            System.out.println("线程" + Thread.currentThread().getName() + "正在写入数据...");
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

## 3. Semaphore

> 字面意思为 **信号量**。`Semaphore` 用来控制某段代码块的并发数。
>
> `Semaphore` 管理着一组虚拟的许可（permit），permit 的初始数量可通过构造方法来指定。每次执行 `acquire` 方法可以获取一个 permit，如果没有就等待；而 `release` 方法可以释放一个 permit。

`Semaphore` 应用场景：

- `Semaphore` 可以用于实现资源池，如数据库连接池。
- `Semaphore` 可以用于将任何一种容器变成有界阻塞容器。

![img](https://raw.githubusercontent.com/dunwu/images/dev/cs/java/javacore/concurrent/Semaphore.png)

`Semaphore` 提供了 2 个构造方法：

```java
// 参数 permits 表示许可数目，即同时可以允许多少线程进行访问
public Semaphore(int permits) {}
// 参数 fair 表示是否是公平的，即等待时间越久的越先获取许可
public Semaphore(int permits, boolean fair) {}
```

> 说明：
>
> - `permits` - 初始化固定数量的 permit，并且默认为非公平模式。
> - `fair` - 设置是否为公平模式。所谓公平，是指等待久的优先获取 permit。

`Semaphore`的重要方法：

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

示例：

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

## 4. 总结

- `CountDownLatch` 和 `CyclicBarrier` 都能够实现线程之间的等待，只不过它们侧重点不同：
  - `CountDownLatch` 一般用于某个线程 A 等待若干个其他线程执行完任务之后，它才执行；
  - `CyclicBarrier` 一般用于一组线程互相等待至某个状态，然后这一组线程再同时执行；
  - 另外，`CountDownLatch` 是不可以重用的，而 `CyclicBarrier` 是可以重用的。
- `Semaphore` 其实和锁有点类似，它一般用于控制对某组资源的访问权限。

## 5. 参考资料

- [《Java 并发编程实战》](https://book.douban.com/subject/10484692/)
- [《Java 并发编程的艺术》](https://book.douban.com/subject/26591326/)
- [Java 并发编程：CountDownLatch、CyclicBarrier 和 Semaphore](https://www.cnblogs.com/dolphin0520/p/3920397.html)
