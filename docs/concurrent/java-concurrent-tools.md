# 并发工具类

> 📓 本文已归档到：「[javacore](https://github.com/dunwu/javacore)」
>
> 本文内容基于 JDK1.8。

<!-- TOC depthFrom:2 depthTo:3 -->

- [CountDownLatch](#countdownlatch)
    - [要点](#要点)
    - [源码](#源码)
    - [示例](#示例)
- [CyclicBarrier](#cyclicbarrier)
    - [要点](#要点-1)
    - [源码](#源码-1)
    - [示例](#示例-1)
- [Semaphore](#semaphore)
    - [要点](#要点-2)
    - [源码](#源码-2)
    - [示例](#示例-2)
- [资料](#资料)

<!-- /TOC -->

JDK 的 `java.util.concurrent` 包（即 juc）中提供了几个非常有用的并发工具类。

## CountDownLatch

### 要点

- 作用：字面意思为递减计数锁。它允许一个或多个线程等待，直到在其他线程中执行的一组操作完成。
- 原理：`CountDownLatch` 维护一个计数器 count，表示需要等待的事件数量。`countDown` 方法递减计数器，表示有一个事件已经发生。调用 `await` 方法的线程会一直阻塞直到计数器为零，或者等待中的线程中断，或者等待超时。

<p align="center">
  <img src="http://dunwu.test.upcdn.net/cs/java/concurrent/CountdownLatch.png" alt="CountdownLatch">
</p>

### 源码

`CountDownLatch` 唯一的构造方法：

```java
// 初始化计数器
public CountDownLatch(int count) {};
```

`CountDownLatch` 的重要方法：

```java
// 调用 await() 方法的线程会被挂起，它会等待直到 count 值为 0 才继续执行
public void await() throws InterruptedException { };
// 和 await() 类似，只不过等待一定的时间后 count 值还没变为 0 的话就会继续执行
public boolean await(long timeout, TimeUnit unit) throws InterruptedException { };
// count 减 1
public void countDown() { };
```

### 示例

```java
public class CountDownLatchDemo {

    public static void main(String[] args) {
        final CountDownLatch latch = new CountDownLatch(2);

        new Thread(() -> {
            try {
                System.out.println("子线程" + Thread.currentThread().getName() + "正在执行");
                Thread.sleep(3000);
                System.out.println("子线程" + Thread.currentThread().getName() + "执行完毕");
                latch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                System.out.println("子线程" + Thread.currentThread().getName() + "正在执行");
                Thread.sleep(3000);
                System.out.println("子线程" + Thread.currentThread().getName() + "执行完毕");
                latch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        try {
            System.out.println("等待2个子线程执行完毕...");
            latch.await();
            System.out.println("2个子线程已经执行完毕");
            System.out.println("继续执行主线程");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

## CyclicBarrier

### 要点

- 作用：字面意思循环栅栏。它可以让一组线程等待至某个状态之后再全部同时执行。叫做循环是因为当所有等待线程都被释放以后，`CyclicBarrier` 可以被重用。
- 原理：`CyclicBarrier` 维护一个计数器 count。每次执行 `await` 方法之后，count 加 1，直到计数器的值和设置的值相等，等待的所有线程才会继续执行。
- 场景：`CyclicBarrier` 在并行迭代算法中非常有用。

<p align="center">
  <img src="http://dunwu.test.upcdn.net/cs/java/concurrent/CyclicBarrier.png" alt="CyclicBarrier">
</p>

### 源码

`CyclicBarrier` 提供了 2 个构造方法

```java
// parties 数相当于一个屏障，当 parties 数量的线程在等待时会跳闸，并且在跳闸时不执行预定义的动作。
public CyclicBarrier(int parties) {}
// parties 数相当于一个屏障，当 parties 数量的线程在等待时会跳闸，并且在跳闸时执行给定的动作 barrierAction。
public CyclicBarrier(int parties, Runnable barrierAction) {}
```

`CyclicBarrier` 的重要方法：

```java
// 等待调用 await 的线程数达到屏障数。如果当前线程是最后一个到达的线程，并且在构造函数中提供了非空屏障操作，则当前线程在允许其他线程继续之前运行该操作。如果在屏障动作期间发生异常，那么该异常将在当前线程中传播并且屏障被置于断开状态。
public int await() throws InterruptedException, BrokenBarrierException {}
// 相比于上个方法，这个方法让这些线程等待至一定的时间，如果还有线程没有到达 barrier 状态就直接让到达 barrier 的线程执行后续任务。
public int await(long timeout, TimeUnit unit)
        throws InterruptedException,
               BrokenBarrierException,
               TimeoutException {}
// 将屏障重置为初始状态
public void reset() {}
```

### 示例

```java
public class CyclicBarrierDemo02 {

    static class CyclicBarrierRunnable implements Runnable {

        CyclicBarrier barrier1 = null;
        CyclicBarrier barrier2 = null;

        CyclicBarrierRunnable(CyclicBarrier barrier1, CyclicBarrier barrier2) {
            this.barrier1 = barrier1;
            this.barrier2 = barrier2;
        }

        public void run() {
            try {
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + " waiting at barrier 1");
                this.barrier1.await();

                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + " waiting at barrier 2");
                this.barrier2.await();

                System.out.println(Thread.currentThread().getName() + " done!");

            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Runnable barrier1Action = () -> System.out.println("BarrierAction 1 executed ");
        Runnable barrier2Action = () -> System.out.println("BarrierAction 2 executed ");

        CyclicBarrier barrier1 = new CyclicBarrier(2, barrier1Action);
        CyclicBarrier barrier2 = new CyclicBarrier(2, barrier2Action);

        CyclicBarrierRunnable barrierRunnable1 = new CyclicBarrierRunnable(barrier1, barrier2);

        CyclicBarrierRunnable barrierRunnable2 = new CyclicBarrierRunnable(barrier1, barrier2);

        new Thread(barrierRunnable1).start();
        new Thread(barrierRunnable2).start();
    }
}
```

## Semaphore

### 要点

- 作用：字面意思为信号量。`Semaphore` 用来控制同时访问某个特定资源的操作数量，或者同时执行某个指定操作的数量。
- 原理：`Semaphore` 管理着一组虚拟的许可（permit），permit 的初始数量可通过构造方法来指定。每次执行 `acquire` 方法可以获取一个 permit，如果没有就等待；而 `release` 方法可以释放一个 permit。
- 场景：
  - `Semaphore` 可以用于实现资源池，如数据库连接池。
  - `Semaphore` 可以用于将任何一种容器变成有界阻塞容器。

<p align="center">
  <img src="http://dunwu.test.upcdn.net/cs/java/concurrent/semaphore.png" alt="semaphore">
</p>

### 源码

`Semaphore`提供了 2 个构造方法：

```java
// 初始化固定数量的 permit，并且默认为非公平模式
public Semaphore(int permits) {}
// 初始化固定数量的 permit，第二个参数设置是否为公平模式。所谓公平，是指等待久的优先获取许可
public Semaphore(int permits, boolean fair) {}
```

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

### 示例

```java
public class SemaphoreDemo {

    private static final int THREAD_COUNT = 30;

    private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT);

    private static Semaphore s = new Semaphore(10);

    public static void main(String[] args) {
        for (int i = 0; i < THREAD_COUNT; i++) {
            threadPool.execute(() -> {
                try {
                    s.acquire();
                    System.out.println("save data");
                    s.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        threadPool.shutdown();
    }
}
```

## 资料

- [Java并发编程实战](https://item.jd.com/10922250.html)
- [Java并发编程的艺术](https://item.jd.com/11740734.html)
- http://www.cnblogs.com/dolphin0520/p/3920397.html
