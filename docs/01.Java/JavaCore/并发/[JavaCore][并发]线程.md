---
title: Java 并发之线程
date: 2019-12-24 23:52:25
categories:
  - Java
  - JavaCore
  - 并发
tags:
  - Java
  - JavaCore
  - 并发
  - 线程
  - Thread
  - Runnable
  - Callable
  - Future
  - FutureTask
  - 线程生命周期
permalink: /pages/98284130/
---

# Java 并发之线程

## 线程简介

- **进程（Process）** - 进程是具有一定独立功能的程序关于某个数据集合上的一次运行活动。进程是操作系统进行资源分配的基本单位。**进程可视为一个正在运行的程序**。
- **线程（Thread）** - **线程是操作系统进行调度的基本单位**。
- **管程（Monitor）** - **管程是指管理共享变量以及对共享变量的操作过程，让他们支持并发**。
  - Java 通过 synchronized 关键字及 wait()、notify()、notifyAll() 这三个方法来实现管程技术。
  - **管程和信号量是等价的，所谓等价指的是用管程能够实现信号量，也能用信号量实现管程**。
- **协程（Coroutine）** - **协程可以理解为一种轻量级的线程**。
  - 从操作系统的角度来看，线程是在内核态中调度的，而协程是在用户态调度的，所以相对于线程来说，协程切换的成本更低。
  - 协程虽然也有自己的栈，但是相比线程栈要小得多，典型的线程栈大小差不多有 1M，而协程栈的大小往往只有几 K 或者几十 K。所以，无论是从时间维度还是空间维度来看，协程都比线程轻量得多。
  - Go、Python、Lua、Kotlin 等语言都支持协程；Java OpenSDK 中的 Loom 项目目标就是支持协程。

进程和线程的差异：

- 一个程序至少有一个进程，一个进程至少有一个线程。
- 线程比进程划分更细，所以执行开销更小，并发性更高
- 进程是一个实体，拥有独立的资源；而同一个进程中的多个线程共享进程的资源。

![](https://raw.githubusercontent.com/dunwu/images/master/cs/java/javacore/concurrent/processes-vs-threads.jpg)

JVM 在单个进程中运行，JVM 中的线程共享属于该进程的堆。这就是为什么几个线程可以访问同一个对象。线程共享堆并拥有自己的堆栈空间。这是一个线程如何调用一个方法以及它的局部变量是如何保持线程安全的。但是堆不是线程安全的并且为了线程安全必须进行同步。

## 线程创建

一般来说，创建线程有很多种方式，例如：

- 实现 `Runnable` 接口
- 实现 `Callable` 接口
- 继承 `Thread` 类
- 通过线程池创建线程
- 使用 `CompletableFuture` 创建线程
- ...

下面是几种创建线程的示例：

::: tabs#创建线程

@tab Thread

### Thread

【示例】继承 `Thread` 类创建线程

1. 定义 `Thread` 类的子类，并覆写该类的 `run` 方法。`run` 方法的方法体就代表了线程要完成的任务，因此把 `run` 方法称为执行体。
2. 创建 `Thread` 子类的实例，即创建了线程对象。
3. 调用线程对象的 `start` 方法来启动该线程。

```java
public class ThreadDemo {

    public static void main(String[] args) {
        // 实例化对象
        MyThread tA = new MyThread("Thread 线程-A");
        MyThread tB = new MyThread("Thread 线程-B");
        // 调用线程主体
        tA.start();
        tB.start();
    }

    static class MyThread extends Thread {

        private int ticket = 5;

        MyThread(String name) {
            super(name);
        }

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

@tab Runnable

### Runnable

**实现 `Runnable` 接口优于继承 `Thread` 类**，因为：

- Java 不支持多重继承，所有的类都只允许继承一个父类，但可以实现多个接口。如果继承了 `Thread` 类就无法继承其它类，这不利于扩展。
- 类可能只要求可执行就行，继承整个 `Thread` 类开销过大。

【示例】实现 `Runnable` 接口创建线程

1. 定义 `Runnable` 接口的实现类，并覆写该接口的 `run` 方法。该 `run` 方法的方法体同样是该线程的线程执行体。
2. 创建 `Runnable` 实现类的实例，并以此实例作为 `Thread` 的 target 来创建 `Thread` 对象，该 `Thread` 对象才是真正的线程对象。
3. 调用线程对象的 `start` 方法来启动该线程。

```java
public class RunnableDemo {

    public static void main(String[] args) {
        // 实例化对象
        Thread tA = new Thread(new MyThread(), "Runnable 线程-A");
        Thread tB = new Thread(new MyThread(), "Runnable 线程-B");
        // 调用线程主体
        tA.start();
        tB.start();
    }

    static class MyThread implements Runnable {

        private int ticket = 5;

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

@tab Callable

### Callable、Future、FutureTask

**继承 Thread 类和实现 Runnable 接口这两种创建线程的方式都没有返回值**。所以，线程执行完后，无法得到执行结果。但如果期望得到执行结果该怎么做？

为了解决这个问题，Java 1.5 后，提供了 `Callable` 接口和 `Future` 接口，通过它们，可以在线程执行结束后，返回执行结果。

#### Callable

`Callable` 接口只声明了一个 `call` 方法：

```java
public interface Callable<V> {
    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    V call() throws Exception;
}
```

那么怎么使用 `Callable` 呢？一般情况下是配合 `ExecutorService` 来使用的，在 `ExecutorService` 接口中声明了若干个 `submit` 方法的重载版本：

```java
<T> Future<T> submit(Callable<T> task);
<T> Future<T> submit(Runnable task, T result);
Future<?> submit(Runnable task);
```

第一个 `submit` 方法里面的参数类型就是 `Callable`。

#### Future

`Future` 就是对于具体的 `Callable` 任务的执行结果进行取消、查询是否完成、获取结果。必要时可以通过 `get` 方法获取执行结果，该方法会阻塞直到任务返回结果。

```java
public interface Future<V> {
    boolean cancel(boolean mayInterruptIfRunning);
    boolean isCancelled();
    boolean isDone();
    V get() throws InterruptedException, ExecutionException;
    V get(long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException;
}
```

#### FutureTask

`FutureTask` 类实现了 `RunnableFuture` 接口，`RunnableFuture` 继承了 `Runnable` 接口和 `Future` 接口。

所以，`FutureTask` 既可以作为 `Runnable` 被线程执行，又可以作为 `Future` 得到 `Callable` 的返回值。

```java
public class FutureTask<V> implements RunnableFuture<V> {
    // ...
    public FutureTask(Callable<V> callable) {}
    public FutureTask(Runnable runnable, V result) {}
}

public interface RunnableFuture<V> extends Runnable, Future<V> {
    void run();
}
```

事实上，`FutureTask` 是 `Future` 接口的一个唯一实现类。

#### Callable + Future + FutureTask 示例

通过实现 `Callable` 接口创建线程的步骤：

1. 创建 `Callable` 接口的实现类，并实现 `call` 方法。该 `call` 方法将作为线程执行体，并且有返回值。
2. 创建 `Callable` 实现类的实例，使用 `FutureTask` 类来包装 `Callable` 对象，该 `FutureTask` 对象封装了该 `Callable` 对象的 `call` 方法的返回值。
3. 使用 `FutureTask` 对象作为 `Thread` 对象的 target 创建并启动新线程。
4. 调用 `FutureTask` 对象的 `get` 方法来获得线程执行结束后的返回值。

```java
public class CallableDemo {

    public static void main(String[] args) {
        Callable<Long> callable = new MyThread();
        FutureTask<Long> future = new FutureTask<>(callable);
        new Thread(future, "Callable 线程").start();
        try {
            System.out.println("任务耗时：" + (future.get() / 1000000) + "毫秒");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    static class MyThread implements Callable<Long> {

        private int ticket = 10000;

        @Override
        public Long call() {
            long begin = System.nanoTime();
            while (ticket > 0) {
                System.out.println(Thread.currentThread().getName() + " 卖出了第 " + ticket + " 张票");
                ticket--;
            }

            long end = System.nanoTime();
            return (end - begin);
        }

    }

}
```

:::

虽然，看似有多种多样的创建线程方式。但是，**从本质上来说，Java 就只有一种方式可以创建线程，那就是通过 `new Thread().start() ` 创建**。不管是哪种方式，最终还是依赖于 `new Thread().start()`。

> 👉 扩展阅读：[大家都说 Java 有三种创建线程的方式！并发编程中的惊天骗局！](https://mp.weixin.qq.com/s/NspUsyhEmKnJ-4OprRFp9g)。

## 线程终止

### 如何正确停止线程

通常情况下，我们不会手动停止一个线程，而是允许线程运行到结束，然后让它自然停止。但是依然会有许多特殊的情况需要我们提前停止线程，比如：用户突然关闭程序，或程序运行出错重启等。

**对于 Java 而言，最正确的停止线程的方式是：通过 `Thread.interrupt` 和 `Thread.isInterrupted` 配合来控制线程终止**。但 `Thread.interrupt` 仅仅起到通知被停止线程的作用。而对于被停止的线程而言，它拥有完全的自主权，它既可以选择立即停止，也可以选择一段时间后停止，也可以选择压根不停止。

事实上，Java 希望程序间能够相互通知、相互协作地管理线程，因为如果不了解对方正在做的工作，贸然强制停止线程就可能会造成一些安全的问题，为了避免造成问题就需要给对方一定的时间来整理收尾工作。比如：线程正在写入一个文件，这时收到终止信号，它就需要根据自身业务判断，是选择立即停止，还是将整个文件写入成功后停止，而如果选择立即停止就可能造成数据不完整，不管是中断命令发起者，还是接收者都不希望数据出现问题。

一旦调用某个线程的 `Thread.interrupt` 之后，这个线程的中断标记位就会被设置成 `true`。每个线程都有这样的标记位，当线程执行时，应该定期检查这个标记位，如果标记位被设置成 `true`，就说明有程序想终止该线程。回到源码，可以看到在 `while` 循环体判断语句中，首先通过 `Thread.currentThread().isInterrupt()` 判断线程是否被中断，随后检查是否还有工作要做。&& 逻辑表示只有当两个判断条件同时满足的情况下，才会去执行下面的工作。

需要留意一个特殊场景：**`Thread.sleep` 后，线程依然可以感知 `Thread.interrupt`**。

【示例】正确停止线程的方式——`Thread.interrupt`

```java
public class ThreadStopDemo {

    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(new MyTask(), "MyTask");
        thread.start();
        TimeUnit.MILLISECONDS.sleep(10);
        thread.interrupt();
    }

    private static class MyTask implements Runnable {

        private long count = 0L;

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " 线程启动");
            // 通过 Thread.interrupted 和 interrupt 配合来控制线程终止
            while (!Thread.currentThread().isInterrupted() && count < 10000) {
                System.out.println("count = " + count++);
            }
            System.out.println(Thread.currentThread().getName() + " 线程终止");
        }

    }

}
// 输出（count 未到 10000，线程就主动结束）：
// MyTask 线程启动
// count = 0
// count = 1
// ...
// count = 840
// count = 841
// count = 842
// MyTask 线程终止
```

### 可以使用 `Thread.stop`，`Thread.suspend` 和 `Thread.resume` 停止线程吗？

`Thread.stop`，`Thread.suspend` 和 `Thread.resume` 方法已经被 Java 标记为 `@Deprecated`。为什么废弃呢？

- **`Thread.stop` 会直接把线程停止，这样就没有给线程足够的时间来处理想要在停止前保存数据的逻辑，任务戛然而止，会导致出现数据完整性等问题**。
- 而对于`Thread.suspend` 和 `Thread.resume` 而言，它们的问题在于：**如果线程调用 `Thread.suspend`，它并不会释放锁，就开始进入休眠，但此时有可能仍持有锁，这样就容易导致死锁问题**。因为这把锁在线程被 `Thread.resume` 之前，是不会被释放的。假设线程 A 调用了 `Thread.suspend` 方法让线程 B 挂起，线程 B 进入休眠，而线程 B 又刚好持有一把锁，此时假设线程 A 想访问线程 B 持有的锁，但由于线程 B 并没有释放锁就进入休眠了，所以对于线程 A 而言，此时拿不到锁，也会陷入阻塞，那么线程 A 和线程 B 就都无法继续向下执行。

【示例】`Thread.stop` 终止线程，导致线程任务戛然而止

```java
public class ThreadStopErrorDemo {

    public static void main(String[] args) {
        MyTask thread = new MyTask();
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 终止线程
        thread.stop();
        // 确保线程终止后，才执行下面的代码
        while (thread.isAlive()) { }
        // 输出两个计数器的最终状态
        thread.print();
    }

    /**
     * 持有两个计数器，run 方法中每次执行都会使计数器自增
     */
    private static class MyTask extends Thread {

        private int i = 0;

        private int j = 0;

        @Override
        public void run() {
            synchronized (this) {
                ++i;
                try {
                    // 模拟耗时操作
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ++j;
            }
        }

        public void print() {
            System.out.println("i=" + i + " j=" + j);
        }

    }

}
```

### 使用 `volatile` 标记方式停止线程正确吗？

使用 `volatile` 标记方式停止线程并不总是正确的。虽然 `volatile` 变量可以确保可见性，即当一个线程修改了 `volatile` 变量的值，其他线程能够立即看到最新的值，但它并不能保证原子性，也就是说并不能保证多个线程对 `volatile` 变量的操作是互斥的。

当我们使用 `volatile` 变量来控制线程的停止，通常是通过设置一个 `volatile` 标志位来告诉线程停止执行。例如：

```java
public class MyTask extends Thread {
    private volatile boolean canceled = false;

    public void run() {
        while (!canceled) {
            // 执行任务
        }
    }

    public void stopTask() {
        canceled = true;
    }
}
```

在上述例子中，`canceled` 是一个 `volatile` 变量，用来控制线程的停止。虽然这种方式在某些情况下可以工作，但它并**不是一个可靠的停止线程的方式，因为在多线程环境中，其他线程修改 `canceled` 的值时，可能会出现竞态条件，导致线程无法正确停止**。

## 线程基本方法

线程（`Thread`）基本方法清单：

| 方法            | 描述                                                                                                                                                                                                         |
| --------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `run`           | 线程的执行实体。                                                                                                                                                                                             |
| `start`         | 线程的启动方法。                                                                                                                                                                                             |
| `currentThread` | 返回对当前正在执行的线程对象的引用。                                                                                                                                                                         |
| `setName`       | 设置线程名称。                                                                                                                                                                                               |
| `getName`       | 获取线程名称。                                                                                                                                                                                               |
| `setPriority`   | 设置线程优先级。Java 中的线程优先级的范围是 [1,10]，一般来说，高优先级的线程在运行时会具有优先权。可以通过 `thread.setPriority(Thread.MAX_PRIORITY)` 的方式设置，默认优先级为 5。                            |
| `getPriority`   | 获取线程优先级。                                                                                                                                                                                             |
| `setDaemon`     | 设置线程为守护线程。                                                                                                                                                                                         |
| `isDaemon`      | 判断线程是否为守护线程。                                                                                                                                                                                     |
| `isAlive`       | 判断线程是否启动。                                                                                                                                                                                           |
| `interrupt`     | 中断另一个线程的运行状态。                                                                                                                                                                                   |
| `interrupted`   | 测试当前线程是否已被中断。通过此方法可以清除线程的中断状态。换句话说，如果要连续调用此方法两次，则第二次调用将返回 false（除非当前线程在第一次调用清除其中断状态之后且在第二次调用检查其状态之前再次中断）。 |
| `join`          | 可以使一个线程强制运行，线程强制运行期间，其他线程无法运行，必须等待此线程完成之后才可以继续执行。                                                                                                           |
| `Thread.sleep`  | 静态方法。将当前正在执行的线程休眠。                                                                                                                                                                         |
| `Thread.yield`  | 静态方法。将当前正在执行的线程暂停，让其他线程执行。                                                                                                                                                         |

### 线程休眠

**使用 `Thread.sleep` 方法可以使得当前正在执行的线程进入休眠状态。**

使用 `Thread.sleep` 需要向其传入一个整数值，这个值表示线程将要休眠的毫秒数。

`Thread.sleep` 方法可能会抛出 `InterruptedException`，因为异常不能跨线程传播回 `main` 中，因此必须在本地进行处理。线程中抛出的其它异常也同样需要在本地进行处理。

```java
public class ThreadSleepDemo {

    public static void main(String[] args) {
        new Thread(new MyThread("线程 A", 500)).start();
        new Thread(new MyThread("线程 B", 1000)).start();
        new Thread(new MyThread("线程 C", 1500)).start();
    }

    static class MyThread implements Runnable {

        /** 线程名称 */
        private String name;

        /** 休眠时间 */
        private int time;

        private MyThread(String name, int time) {
            this.name = name;
            this.time = time;
        }

        @Override
        public void run() {
            try {
                // 休眠指定的时间
                Thread.sleep(this.time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(this.name + "休眠" + this.time + "毫秒。");
        }

    }

}
```

### 线程礼让

`Thread.yield` 方法的调用声明了当前线程已经完成了生命周期中最重要的部分，可以切换给其它线程来执行 。该方法只是对线程调度器的一个建议，而且也只是建议具有相同优先级的其它线程可以运行。

```java
public class ThreadYieldDemo {

    public static void main(String[] args) {
        MyThread t = new MyThread();
        new Thread(t, "线程 A").start();
        new Thread(t, "线程 B").start();
    }

    static class MyThread implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "运行，i = " + i);
                if (i == 2) {
                    System.out.print("线程礼让：");
                    Thread.yield();
                }
            }
        }
    }
}
```

### 守护线程

什么是守护线程？

- **守护线程（Daemon Thread）是在后台执行并且不会阻止 JVM 终止的线程**。**当所有非守护线程结束时，程序也就终止，同时会杀死所有守护线程**。
- 与守护线程（Daemon Thread）相反的，叫用户线程（User Thread），也就是非守护线程。

为什么需要守护线程？

- 守护线程的优先级比较低，用于为系统中的其它对象和线程提供服务。典型的应用就是垃圾回收器。

如何使用守护线程？

- 可以使用 `isDaemon` 方法判断线程是否为守护线程。
- 可以使用 `setDaemon` 方法设置线程为守护线程。
  - 正在运行的用户线程无法设置为守护线程，所以 `setDaemon` 必须在 `thread.start` 方法之前设置，否则会抛出 `llegalThreadStateException` 异常；
  - 一个守护线程创建的子线程依然是守护线程。
  - 不要认为所有的应用都可以分配给守护线程来进行服务，比如读写操作或者计算逻辑。

```java
public class ThreadDaemonDemo {

    public static void main(String[] args) {
        Thread t = new Thread(new MyThread(), "线程");
        t.setDaemon(true); // 此线程在后台运行
        System.out.println("线程 t 是否是守护进程：" + t.isDaemon());
        t.start(); // 启动线程
    }

    static class MyThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                System.out.println(Thread.currentThread().getName() + "在运行。");
            }
        }
    }
}
```

> 参考阅读：[Java 中守护线程的总结](https://blog.csdn.net/shimiso/article/details/8964414)

## 线程通信

> 当多个线程可以一起工作去解决某个问题时，如果某些部分必须在其它部分之前完成，那么就需要对线程进行协调。

### wait/notify/notifyAll

- `wait` - `wait` 会自动释放当前线程占有的对象锁，并请求操作系统挂起当前线程，**让线程从 `RUNNING` 状态转入 `WAITING` 状态**，等待 `notify` / `notifyAll` 来唤醒。如果没有释放锁，那么其它线程就无法进入对象的同步方法或者同步控制块中，那么就无法执行 `notify` 或者 `notifyAll` 来唤醒挂起的线程，造成死锁。
- `notify` - 唤醒一个正在 `WAITING` 状态的线程，并让它拿到对象锁，具体唤醒哪一个线程由 JVM 控制 。
- `notifyAll` - 唤醒所有正在 `WAITING` 状态的线程，接下来它们需要竞争对象锁。

> 注意：
>
> - **`wait`、`notify`、`notifyAll` 都是 `Object` 类中的方法**，而非 `Thread`。
> - **`wait`、`notify`、`notifyAll` 只能用在 `synchronized` 方法或者 `synchronized` 代码块中使用，否则会在运行时抛出 `IllegalMonitorStateException`**。

生产者、消费者模式是 `wait`、`notify`、`notifyAll` 的一个经典使用案例：

```java
public class ThreadWaitNotifyDemo02 {

    private static final int QUEUE_SIZE = 10;
    private static final PriorityQueue<Integer> queue = new PriorityQueue<>(QUEUE_SIZE);

    public static void main(String[] args) {
        new Producer("生产者 A").start();
        new Producer("生产者 B").start();
        new Consumer("消费者 A").start();
        new Consumer("消费者 B").start();
    }

    static class Consumer extends Thread {

        Consumer(String name) {
            super(name);
        }

        @Override
        public void run() {
            while (true) {
                synchronized (queue) {
                    while (queue.size() == 0) {
                        try {
                            System.out.println("队列空，等待数据");
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            queue.notifyAll();
                        }
                    }
                    queue.poll(); // 每次移走队首元素
                    queue.notifyAll();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " 从队列取走一个元素，队列当前有：" + queue.size() + "个元素");
                }
            }
        }
    }

    static class Producer extends Thread {

        Producer(String name) {
            super(name);
        }

        @Override
        public void run() {
            while (true) {
                synchronized (queue) {
                    while (queue.size() == QUEUE_SIZE) {
                        try {
                            System.out.println("队列满，等待有空余空间");
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            queue.notifyAll();
                        }
                    }
                    queue.offer(1); // 每次插入一个元素
                    queue.notifyAll();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " 向队列取中插入一个元素，队列当前有：" + queue.size() + "个元素");
                }
            }
        }
    }
}
```

### join

在线程操作中，可以使用 `join` 方法让一个线程强制运行，线程强制运行期间，其他线程无法运行，必须等待此线程完成之后才可以继续执行。

```java
public class ThreadJoinDemo {

    public static void main(String[] args) {
        MyThread mt = new MyThread(); // 实例化 Runnable 子类对象
        Thread t = new Thread(mt, "mythread"); // 实例化 Thread 对象
        t.start(); // 启动线程
        for (int i = 0; i < 50; i++) {
            if (i > 10) {
                try {
                    t.join(); // 线程强制运行
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Main 线程运行 --> " + i);
        }
    }

    static class MyThread implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 50; i++) {
                System.out.println(Thread.currentThread().getName() + " 运行，i = " + i); // 取得当前线程的名字
            }
        }
    }
}
```

### 管道

管道输入/输出流和普通的文件输入/输出流或者网络输入/输出流不同之处在于，它主要用于线程之间的数据传输，而传输的媒介为内存。
管道输入/输出流主要包括了如下 4 种具体实现：`PipedOutputStream`、`PipedInputStream`、`PipedReader` 和 `PipedWriter`，前两种面向字节，而后两种面向字符。

```java
public class Piped {

    public static void main(String[] args) throws Exception {
        PipedWriter out = new PipedWriter();
        PipedReader in = new PipedReader();
        // 将输出流和输入流进行连接，否则在使用时会抛出 IOException
        out.connect(in);
        Thread printThread = new Thread(new Print(in), "PrintThread");
        printThread.start();
        int receive = 0;
        try {
            while ((receive = System.in.read()) != -1) {
                out.write(receive);
            }
        } finally {
            out.close();
        }
    }

    static class Print implements Runnable {

        private PipedReader in;

        Print(PipedReader in) {
            this.in = in;
        }

        public void run() {
            int receive = 0;
            try {
                while ((receive = in.read()) != -1) {
                    System.out.print((char) receive);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```

## 线程生命周期

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2024/08/bbb471da0cb743b088dc9fe58ec57993.png)

`java.lang.Thread.State` 中定义了 **6** 种不同的线程状态，在给定的一个时刻，线程只能处于其中的一个状态。

以下是各状态的说明，以及状态间的联系：

- **开始（NEW）** - 尚未调用 `start` 方法的线程处于此状态。此状态意味着：**创建的线程尚未启动**。
- **可运行（RUNNABLE）** - 已经调用了 `start` 方法的线程处于此状态。此状态意味着，**线程已经准备好了**，一旦被线程调度器分配了 CPU 时间片，就可以运行线程。
  - 在操作系统层面，线程有 READY 和 RUNNING 状态；而在 JVM 层面，只能看到 RUNNABLE 状态，所以 Java 系统一般将这两个状态统称为 RUNNABLE（运行中） 状态 。
- **阻塞（BLOCKED）** - 此状态意味着：**线程处于被阻塞状态**。表示线程在等待 `synchronized` 的隐式锁（Monitor lock）。`synchronized` 修饰的方法、代码块同一时刻只允许一个线程执行，其他线程只能等待，即处于阻塞状态。当占用 `synchronized` 隐式锁的线程释放锁，并且等待的线程获得 `synchronized` 隐式锁时，就又会从 `BLOCKED` 转换到 `RUNNABLE` 状态。
- **等待（WAITING）** - 此状态意味着：**线程无限期等待，直到被其他线程显式地唤醒**。 阻塞和等待的区别在于，阻塞是被动的，它是在等待获取 `synchronized` 的隐式锁。而等待是主动的，通过调用 `Object.wait` 等方法进入。
  - 进入：`Object.wait()`；退出：`Object.notify` / `Object.notifyAll`
  - 进入：`Thread.join()`；退出：被调用的线程执行完毕
  - 进入：`LockSupport.park()`；退出：`LockSupport.unpark`
- **定时等待（TIMED_WAITING）** - 等待指定时间的状态。一个线程处于定时等待状态，是由于执行了以下方法中的任意方法：
  - 进入：`Thread.sleep(long)`；退出：时间结束
  - 进入：`Object.wait(long)`；退出：时间结束 / `Object.notify` / `Object.notifyAll`
  - 进入：`Thread.join(long)`；退出：时间结束 / 被调用的线程执行完毕
  - 进入：`LockSupport.parkNanos(long)`；退出：`LockSupport.unpark`
  - 进入：`LockSupport.parkUntil(long)`；退出：`LockSupport.unpark`
- **终止 (TERMINATED)** - 线程 `run()` 方法执行结束，或者因异常退出了 `run()` 方法，则该线程结束生命周期。死亡的线程不可再次复生。

> 👉 扩展阅读：
>
> - [Java Thread Methods and Thread States](https://www.w3resource.com/java-tutorial/java-threadclass-methods-and-threadstates.php)
> - [Java 线程的 5 种状态及切换（透彻讲解）](https://blog.csdn.net/pange1991/article/details/53860651)
> - [Java 线程运行怎么有第六种状态？ - Dawell 的回答](https://www.zhihu.com/question/56494969/answer/154053599)

## 线程常见问题

### 线程启动

**典型问题**

（1）`Thread.start()` 和 `Thread.run()` 有什么区别？

（2）可以直接调用 `Thread.run()` 方法么？

（3）一个线程两次调用 `Thread.start()` 方法会怎样

**知识点**

（1）`Thread.start()` 和 `Thread.run()` 的区别：

- `run()` 方法是线程的执行体。
- `start()` 方法负责启动线程，然后 JVM 会让这个线程去执行 `run()` 方法。

（2）可以直接调用 `Thread.run()` 方法，但是它的行为和普通方法一样，不会启动新线程去执行。**调用 `start()` 方法方可启动线程并使线程进入就绪状态，直接执行 `run()` 方法的话不会以多线程的方式执行。**

（3）Java 的线程是不允许启动两次的，第二次调用必然会抛出 `IllegalThreadStateException`。

### 线程等待

**典型问题**

（1）`Thread.sleep()`、`Thread.yield()`、`Thread.join()`、`Object.wait()` 方法有什么区别？

（2）为什么 `Thread.sleep()`、`Thread.yield()` 设计为静态方法？

**知识点**

（1）`Thread.sleep()`、`Thread.yield()`、`Thread.join()` 方法的区别：

- `Thread.sleep()`
  - `Thread.sleep()` 方法需要指定等待的时间，它可以让当前正在执行的线程在指定的时间内暂停执行，进入 **TIMED_WAITING** 状态。
  - 该方法既可以让其他同优先级或者高优先级的线程得到执行的机会，也可以让低优先级的线程得到执行机会。
  - 但是，`Thread.sleep()` 方法不会释放“锁标志”，也就是说如果有 `synchronized` 同步块，其他线程仍然不能访问共享数据。
- `Thread.yield()`
  - `Thread.yield()` 方法可以让当前正在执行的线程暂停，但它不会阻塞该线程，它只是将该线程从 **RUNNING** 状态转入 **RUNNABLE** 状态。
  - 当某个线程调用了 `Thread.yield()` 方法暂停之后，只有优先级大于等于当前线程的处于就绪状态的线程才会获得执行的机会。
- `Thread.join()`
  - `Thread.join()` 方法会使当前线程转入 **WAITING** 或 **TIMED_WAITING** 状态，等待调用 `Thread.join()` 方法的线程结束后才能继续执行。
- `Object.wait()`
  - `Object.wait()` 用于使当前线程等待，直到其他线程调用相同对象的 `Object.notify()` 或 `Object.notifyAll()` 方法唤醒它。
  - 调用 `Object.wait()` 时，线程会释放对象锁，并进入等待状态。

（2）为什么 `Thread.sleep()`、`Thread.yield()` 设计为静态方法？

`Thread.sleep()`、`Thread.yield()` 针对的是 **RUNNING** 状态的线程，也就是说在非 **RUNNING** 状态的线程上执行这两个方法没有意义。这就是为什么这两个方法被设计为静态的。它们只针对正在 **RUNNING** 状态的线程工作，避免程序员错误的认为可以在其他非 **RUNNING** 状态线程上调用。

> 👉 扩展阅读：[Java 线程中 yield 与 join 方法的区别](http://www.importnew.com/14958.html)
> 👉 扩展阅读：[sleep()，wait()，yield() 和 join() 方法的区别](https://blog.csdn.net/xiangwanpeng/article/details/54972952)

### 线程通信

线程间通信是线程间共享资源的一种方式。`Object.wait()`, `Object.notify()` 和 `Object.notifyAll()` 是用于线程之间协作和通信的方法，它们通常与`synchronized` 关键字一起使用来实现线程的同步。

**典型问题**

（1）为什么线程通信的方法 `Object.wait()`、`Object.notify()` 和 `Object.notifyAll()` 被定义在 `Object` 类里？

（2）为什么 `Object.wait()`、`Object.notify()` 和 `Object.notifyAll()` 必须在 `synchronized` 方法/块中被调用？

（3） `Object.wait()` 和 `Thread.sleep` 有什么区别？

**知识点**

（1）为什么线程通信的方法 `Object.wait()`、`Object.notify()` 和 `Object.notifyAll()` 被定义在 `Object` 类里？

Java 的每个对象中都有一个称之为 monitor 监视器的锁，由于每个对象都可以上锁，这就要求在对象头中有一个用来保存锁信息的位置。这个锁是对象级别的，而非线程级别的，wait/notify/notifyAll 也都是锁级别的操作，它们的锁属于对象，所以把它们定义在 Object 类中是最合适，因为 Object 类是所有对象的父类。

如果把 wait/notify/notifyAll 方法定义在 Thread 类中，会带来很大的局限性，比如一个线程可能持有多把锁，以便实现相互配合的复杂逻辑，假设此时 wait 方法定义在 Thread 类中，如何实现让一个线程持有多把锁呢？又如何明确线程等待的是哪把锁呢？既然我们是让当前线程去等待某个对象的锁，自然应该通过操作对象来实现，而不是操作线程。

- `Object.wait()`
  - `Object.wait()` 方法用于使当前线程进入等待状态，直到其他线程调用相同对象的 `notify()` 或 `notifyAll()` 方法唤醒它。
  - 在调用 `wait()` 方法时，线程会释放对象的锁，并进入等待状态。通常在使用 `wait()` 方法时需要放在一个循环中，以避免虚假唤醒（spurious wakeups）。
- `Object.notify()`
  - `Object.notify()` 方法用于唤醒正在等待该对象的锁的一个线程。
  - 被唤醒的线程将会尝试重新获取对象的锁，一旦获取到锁，它将继续执行。
- `Object.notifyAll()`
  - `Object.notifyAll()` 方法用于唤醒正在等待该对象的锁的所有线程。
  - 所有被唤醒的线程将会竞争对象的锁，一旦获取到锁，它们将继续执行。

（2）为什么 `Object.wait()`、`Object.notify()` 和 `Object.notifyAll()` 必须在 `synchronized` 方法/块中被调用？

当一个线程需要调用对象的 `wait()` 方法的时候，这个线程必须拥有该对象的锁，接着它就会释放这个对象锁并进入等待状态直到其他线程调用这个对象上的 `notify()` 方法。同样的，当一个线程需要调用对象的 `notify()` 方法时，它会释放这个对象的锁，以便其他在等待的线程就可以得到这个对象锁。

由于所有的这些方法都需要线程持有对象的锁，这样就只能通过 `synchronized` 来实现，所以他们只能在 `synchronized` 方法/块中被调用。

（3） `Object.wait()` 和 `Thread.sleep` 有什么区别？

相同点：

1. 它们都可以让线程阻塞。
2. 它们都可以响应 interrupt 中断：在等待的过程中如果收到中断信号，都可以进行响应，并抛出 InterruptedException 异常。

不同点：

1. wait 方法必须在 synchronized 保护的代码中使用，而 sleep 方法并没有这个要求。
2. 在同步代码中执行 sleep 方法时，并不会释放 monitor 锁，但执行 wait 方法时会主动释放 monitor 锁。
3. sleep 方法中会要求必须定义一个时间，时间到期后会主动恢复，而对于没有参数的 wait 方法而言，意味着永久等待，直到被中断或被唤醒才能恢复，它并不会主动恢复。
4. wait/notify 是 Object 类的方法，而 sleep 是 Thread 类的方法。

> 👉 扩展阅读：[Java 并发编程：线程间协作的两种方式：wait、notify、notifyAll 和 Condition](http://www.cnblogs.com/dolphin0520/p/3920385.html)

### 线程优先级

**典型问题**

（1）Java 的线程优先级如何控制？

（2）高优先级的 Java 线程一定先执行吗？

**知识点**

（1）Java 中的线程优先级的范围是 `[1,10]`，一般来说，高优先级的线程在运行时会具有优先权。可以通过 `thread.setPriority(Thread.MAX_PRIORITY)` 的方式设置，默认优先级为 `5`。

（2）即使设置了线程的优先级，也**无法保证高优先级的线程一定先执行**。

这是因为 **Java 线程优先级依赖于操作系统的支持**，然而，不同的操作系统支持的线程优先级并不相同，不能很好的和 Java 中线程优先级一一对应。因此，Java 线程优先级控制并不可靠。

### 守护线程

**典型问题**

（1）什么是守护线程？

（2）如何创建守护线程？

**知识点**

（1）什么是守护线程？

守护线程（Daemon Thread）是在后台执行并且不会阻止 JVM 终止的线程。与守护线程（Daemon Thread）相反的，叫用户线程（User Thread），也就是非守护线程。

守护线程的优先级比较低，一般用于为系统中的其它对象和线程提供服务。典型的应用就是垃圾回收器。

（2）创建守护线程的方式：

- 使用 `thread.setDaemon(true)` 可以设置 thread 线程为守护线程。
- 正在运行的用户线程无法设置为守护线程，所以 `thread.setDaemon(true)` 必须在 `thread.start()` 之前设置，否则会抛出 `llegalThreadStateException` 异常；
- 一个守护线程创建的子线程依然是守护线程。
- 不要认为所有的应用都可以分配给守护线程来进行服务，比如读写操作或者计算逻辑。

> 👉 扩展阅读：[Java 中守护线程的总结](https://blog.csdn.net/shimiso/article/details/8964414)

### 线程数

**典型问题**

（1）线程数是不是越多越好？

（2）创建多少线程才合适？

**知识点**

使用多线程，初衷是为了提升程序性能。度量性能的核心指标是**延迟**和**吞吐量**。所谓提升性能，从度量的角度，主要是**降低延迟，提高吞吐量**。在并发编程领域，提升性能本质上就是提升硬件的利用率，再具体点来说，就是提升 I/O 的利用率和 CPU 的利用率。

多线程并非越多越好，过多的线程可能会导致过多的上下文切换，反而降低系统性能。 通常需要根据服务器硬件资源和预期负载来合理设定线程数大小。

程序一般都是 CPU 计算和 I/O 操作交叉执行的，由于 I/O 设备的速度相对于 CPU 来说都很慢，所以大部分情况下，I/O 操作执行的时间相对于 CPU 计算来说都非常长，这种场景我们一般都称为 I/O 密集型计算；和 I/O 密集型计算相对的就是 CPU 密集型计算了，CPU 密集型计算大部分场景下都是纯 CPU 计算。I/O 密集型程序和 CPU 密集型程序，计算最佳线程数的方法是不同的。

**对于 CPU 密集型的计算场景，理论上“线程的数量=CPU 核数”就是最合适的**。不过在工程上，**线程的数量一般会设置为“CPU 核数+1”**，这样的话，当线程因为偶尔的内存页失效或其他原因导致阻塞时，这个额外的线程可以顶上，从而保证 CPU 的利用率。

对于 I/O 密集型计算场景，最佳的线程数是与程序中 CPU 计算和 I/O 操作的耗时比相关的，我们可以总结出这样一个公式：

> 最佳线程数=1 +（I/O 耗时 / CPU 耗时）

## 典型应用场景

### 场景一：使用 Callable 和 Future 获取异步结果

```java
ExecutorService executor = Executors.newSingleThreadExecutor();
Future<String> future = executor.submit(() -> fetchDataFromRemote());
String result = future.get(5, TimeUnit.SECONDS); // 带超时的获取
```

### 场景二：线程中断协作

```java
Thread worker = new Thread(() -> {
    while (!Thread.currentThread().isInterrupted()) {
        // 执行任务
    }
});
worker.start();
worker.interrupt(); // 协作式停止
```

### 场景三：守护线程

```java
Thread daemon = new Thread(() -> {
    while (true) { monitor(); }
});
daemon.setDaemon(true); // 主线程结束时自动退出
daemon.start();
```

## 最佳实践

1. **不要直接操作 Thread**：使用 ExecutorService 管理线程生命周期。
2. **使用中断机制停止线程**：不要使用 `stop()`（已废弃），用 `interrupt()` + 检查中断状态。
3. **合理设置线程名称**：便于排查问题时识别线程。
4. **理解线程状态转换**：NEW/RUNNABLE/BLOCKED/WAITING/TIMED_WAITING/TERMINATED。

## 常见问题

### Q1：Runnable 和 Callable 有什么区别？

Runnable 没有返回值，不能抛受检异常；Callable 有返回值（通过 Future），可以抛受检异常。

### Q2：什么是守护线程？

守护线程（Daemon Thread）是为其他线程提供服务的线程（如 GC 线程）。当所有非守护线程结束时，守护线程自动退出。

### Q3：Thread.sleep 和 Object.wait 有什么区别？

sleep 不释放锁，wait 释放锁。sleep 是 Thread 的静态方法，wait 是 Object 的实例方法，必须在 synchronized 块中调用。

## 参考资料

- [《Java 并发编程实战》](https://book.douban.com/subject/10484692/)
- [《Java 并发编程的艺术》](https://book.douban.com/subject/26591326/)
- [进程和线程关系及区别](https://blog.csdn.net/yaosiming2011/article/details/44280797)
- [Java 线程中 yield 与 join 方法的区别](http://www.importnew.com/14958.html)
- [sleep()，wait()，yield() 和 join() 方法的区别](https://blog.csdn.net/xiangwanpeng/article/details/54972952)
- [Java 并发编程：线程间协作的两种方式：wait、notify、notifyAll 和 Condition](https://www.cnblogs.com/dolphin0520/p/3920385.html)
- [Java 并发编程：Callable、Future 和 FutureTask](https://www.cnblogs.com/dolphin0520/p/3949310.html)
- [StackOverflow VisualVM - Thread States](https://stackoverflow.com/questions/27406200/visualvm-thread-states)
- [Java 中守护线程的总结](https://blog.csdn.net/shimiso/article/details/8964414)
- [Java 并发](https://github.com/CyC2018/CS-Notes/blob/master/notes/Java%20%E5%B9%B6%E5%8F%91.md)
- [Why must wait() always be in synchronized block](https://stackoverflow.com/questions/2779484/why-must-wait-always-be-in-synchronized-block)
- [Java Thread Methods and Thread States](https://www.w3resource.com/java-tutorial/java-threadclass-methods-and-threadstates.php)
- [Java 线程的 5 种状态及切换（透彻讲解）](https://blog.csdn.net/pange1991/article/details/53860651)
- [Java 线程运行怎么有第六种状态？ - Dawell 的回答](https://www.zhihu.com/question/56494969/answer/154053599)
