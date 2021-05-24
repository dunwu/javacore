# Java 线程基础

> **📦 本文以及示例源码已归档在 [javacore](https://github.com/dunwu/javacore/)**
>
> **关键词：`Thread`、`Runnable`、`Callable`、`Future`、`wait`、`notify`、`notifyAll`、`join`、`sleep`、`yeild`、`线程状态`、`线程通信`**

<!-- TOC depthFrom:2 depthTo:3 -->

- [1. 线程简介](#1-线程简介)
  - [1.1. 什么是进程](#11-什么是进程)
  - [1.2. 什么是线程](#12-什么是线程)
  - [1.3. 进程和线程的区别](#13-进程和线程的区别)
- [2. 创建线程](#2-创建线程)
  - [2.1. Thread](#21-thread)
  - [2.2. Runnable](#22-runnable)
  - [2.3. Callable、Future、FutureTask](#23-callablefuturefuturetask)
- [3. 线程基本用法](#3-线程基本用法)
  - [3.1. 线程休眠](#31-线程休眠)
  - [3.2. 线程礼让](#32-线程礼让)
  - [3.3. 终止线程](#33-终止线程)
  - [3.4. 守护线程](#34-守护线程)
- [4. 线程通信](#4-线程通信)
  - [4.1. wait/notify/notifyAll](#41-waitnotifynotifyall)
  - [4.2. join](#42-join)
  - [4.3. 管道](#43-管道)
- [5. 线程生命周期](#5-线程生命周期)
- [6. 线程常见问题](#6-线程常见问题)
  - [6.1. sleep、yield、join 方法有什么区别](#61-sleepyieldjoin-方法有什么区别)
  - [6.2. 为什么 sleep 和 yield 方法是静态的](#62-为什么-sleep-和-yield-方法是静态的)
  - [6.3. Java 线程是否按照线程优先级严格执行](#63-java-线程是否按照线程优先级严格执行)
  - [6.4. 一个线程两次调用 start()方法会怎样](#64-一个线程两次调用-start方法会怎样)
  - [6.5. `start` 和 `run` 方法有什么区别](#65-start-和-run-方法有什么区别)
  - [6.6. 可以直接调用 `Thread` 类的 `run` 方法么](#66-可以直接调用-thread-类的-run-方法么)
- [7. 参考资料](#7-参考资料)

<!-- /TOC -->

## 1. 线程简介

### 1.1. 什么是进程

简言之，**进程可视为一个正在运行的程序**。它是系统运行程序的基本单位，因此进程是动态的。进程是具有一定独立功能的程序关于某个数据集合上的一次运行活动。进程是操作系统进行资源分配的基本单位。

### 1.2. 什么是线程

线程是操作系统进行调度的基本单位。线程也叫轻量级进程（Light Weight Process），在一个进程里可以创建多个线程，这些线程都拥有各自的计数器、堆栈和局部变量等属性，并且能够访问共享的内存变量。

### 1.3. 进程和线程的区别

- 一个程序至少有一个进程，一个进程至少有一个线程。
- 线程比进程划分更细，所以执行开销更小，并发性更高。
- 进程是一个实体，拥有独立的资源；而同一个进程中的多个线程共享进程的资源。

## 2. 创建线程

创建线程有三种方式：

- 继承 `Thread` 类
- 实现 `Runnable` 接口
- 实现 `Callable` 接口

### 2.1. Thread

通过继承 `Thread` 类创建线程的步骤：

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

### 2.2. Runnable

**实现 `Runnable` 接口优于继承 `Thread` 类**，因为：

- Java 不支持多重继承，所有的类都只允许继承一个父类，但可以实现多个接口。如果继承了 `Thread` 类就无法继承其它类，这不利于扩展。
- 类可能只要求可执行就行，继承整个 `Thread` 类开销过大。

通过实现 `Runnable` 接口创建线程的步骤：

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

### 2.3. Callable、Future、FutureTask

**继承 Thread 类和实现 Runnable 接口这两种创建线程的方式都没有返回值**。所以，线程执行完后，无法得到执行结果。但如果期望得到执行结果该怎么做？

为了解决这个问题，Java 1.5 后，提供了 `Callable` 接口和 `Future` 接口，通过它们，可以在线程执行结束后，返回执行结果。

#### Callable

Callable 接口只声明了一个方法，这个方法叫做 call()：

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

那么怎么使用 Callable 呢？一般情况下是配合 ExecutorService 来使用的，在 ExecutorService 接口中声明了若干个 submit 方法的重载版本：

```java
<T> Future<T> submit(Callable<T> task);
<T> Future<T> submit(Runnable task, T result);
Future<?> submit(Runnable task);
```

第一个 submit 方法里面的参数类型就是 Callable。

#### Future

Future 就是对于具体的 Callable 任务的执行结果进行取消、查询是否完成、获取结果。必要时可以通过 get 方法获取执行结果，该方法会阻塞直到任务返回结果。

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

FutureTask 类实现了 RunnableFuture 接口，RunnableFuture 继承了 Runnable 接口和 Future 接口。

所以，FutureTask 既可以作为 Runnable 被线程执行，又可以作为 Future 得到 Callable 的返回值。

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

事实上，FutureTask 是 Future 接口的一个唯一实现类。

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

## 3. 线程基本用法

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

### 3.1. 线程休眠

**使用 `Thread.sleep` 方法可以使得当前正在执行的线程进入休眠状态。**

使用 `Thread.sleep` 需要向其传入一个整数值，这个值表示线程将要休眠的毫秒数。

`Thread.sleep` 方法可能会抛出 `InterruptedException`，因为异常不能跨线程传播回 `main` 中，因此必须在本地进行处理。线程中抛出的其它异常也同样需要在本地进行处理。

```java
public class ThreadSleepDemo {

    public static void main(String[] args) {
        new Thread(new MyThread("线程A", 500)).start();
        new Thread(new MyThread("线程B", 1000)).start();
        new Thread(new MyThread("线程C", 1500)).start();
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

### 3.2. 线程礼让

`Thread.yield` 方法的调用声明了当前线程已经完成了生命周期中最重要的部分，可以切换给其它线程来执行 。

该方法只是对线程调度器的一个建议，而且也只是建议具有相同优先级的其它线程可以运行。

```java
public class ThreadYieldDemo {

    public static void main(String[] args) {
        MyThread t = new MyThread();
        new Thread(t, "线程A").start();
        new Thread(t, "线程B").start();
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

### 3.3. 终止线程

> **`Thread` 中的 `stop` 方法有缺陷，已废弃**。
>
> 使用 `Thread.stop` 停止线程会导致它解锁所有已锁定的监视器（由于未经检查的 `ThreadDeath` 异常会在堆栈中传播，这是自然的结果）。 如果先前由这些监视器保护的任何对象处于不一致状态，则损坏的对象将对其他线程可见，从而可能导致任意行为。
>
> stop() 方法会真的杀死线程，不给线程喘息的机会，如果线程持有 ReentrantLock 锁，被 stop() 的线程并不会自动调用 ReentrantLock 的 unlock() 去释放锁，那其他线程就再也没机会获得 ReentrantLock 锁，这实在是太危险了。所以该方法就不建议使用了，类似的方法还有 suspend() 和 resume() 方法，这两个方法同样也都不建议使用了，所以这里也就不多介绍了。`Thread.stop` 的许多用法应由仅修改某些变量以指示目标线程应停止运行的代码代替。 目标线程应定期检查此变量，如果该变量指示要停止运行，则应按有序方式从其运行方法返回。如果目标线程等待很长时间（例如，在条件变量上），则应使用中断方法来中断等待。

当一个线程运行时，另一个线程可以直接通过 `interrupt` 方法中断其运行状态。

```java
public class ThreadInterruptDemo {

    public static void main(String[] args) {
        MyThread mt = new MyThread(); // 实例化Runnable子类对象
        Thread t = new Thread(mt, "线程"); // 实例化Thread对象
        t.start(); // 启动线程
        try {
            Thread.sleep(2000); // 线程休眠2秒
        } catch (InterruptedException e) {
            System.out.println("3、休眠被终止");
        }
        t.interrupt(); // 中断线程执行
    }

    static class MyThread implements Runnable {

        @Override
        public void run() {
            System.out.println("1、进入run()方法");
            try {
                Thread.sleep(10000); // 线程休眠10秒
                System.out.println("2、已经完成了休眠");
            } catch (InterruptedException e) {
                System.out.println("3、休眠被终止");
                return; // 返回调用处
            }
            System.out.println("4、run()方法正常结束");
        }
    }
}
```

如果一个线程的 `run` 方法执行一个无限循环，并且没有执行 `sleep` 等会抛出 `InterruptedException` 的操作，那么调用线程的 `interrupt` 方法就无法使线程提前结束。

但是调用 `interrupt` 方法会设置线程的中断标记，此时调用 `interrupted` 方法会返回 `true`。因此可以在循环体中使用 `interrupted` 方法来判断线程是否处于中断状态，从而提前结束线程。

安全地终止线程有两种方法：

- 定义 `volatile` 标志位，在 `run` 方法中使用标志位控制线程终止
- 使用 `interrupt` 方法和 `Thread.interrupted` 方法配合使用来控制线程终止

【示例】使用 `volatile` 标志位控制线程终止

```java
public class ThreadStopDemo2 {

    public static void main(String[] args) throws Exception {
        MyTask task = new MyTask();
        Thread thread = new Thread(task, "MyTask");
        thread.start();
        TimeUnit.MILLISECONDS.sleep(50);
        task.cancel();
    }

    private static class MyTask implements Runnable {

        private volatile boolean flag = true;

        private volatile long count = 0L;

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " 线程启动");
            while (flag) {
                System.out.println(count++);
            }
            System.out.println(Thread.currentThread().getName() + " 线程终止");
        }

        /**
         * 通过 volatile 标志位来控制线程终止
         */
        public void cancel() {
            flag = false;
        }

    }

}
```

【示例】使用 `interrupt` 方法和 `Thread.interrupted` 方法配合使用来控制线程终止

```java
public class ThreadStopDemo3 {

    public static void main(String[] args) throws Exception {
        MyTask task = new MyTask();
        Thread thread = new Thread(task, "MyTask");
        thread.start();
        TimeUnit.MILLISECONDS.sleep(50);
        thread.interrupt();
    }

    private static class MyTask implements Runnable {

        private volatile long count = 0L;

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " 线程启动");
            // 通过 Thread.interrupted 和 interrupt 配合来控制线程终止
            while (!Thread.interrupted()) {
                System.out.println(count++);
            }
            System.out.println(Thread.currentThread().getName() + " 线程终止");
        }
    }
}
```

### 3.4. 守护线程

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

## 4. 线程通信

> 当多个线程可以一起工作去解决某个问题时，如果某些部分必须在其它部分之前完成，那么就需要对线程进行协调。

### 4.1. wait/notify/notifyAll

- `wait` - `wait` 会自动释放当前线程占有的对象锁，并请求操作系统挂起当前线程，**让线程从 `Running` 状态转入 `Waiting` 状态**，等待 `notify` / `notifyAll` 来唤醒。如果没有释放锁，那么其它线程就无法进入对象的同步方法或者同步控制块中，那么就无法执行 `notify` 或者 `notifyAll` 来唤醒挂起的线程，造成死锁。
- `notify` - 唤醒一个正在 `Waiting` 状态的线程，并让它拿到对象锁，具体唤醒哪一个线程由 JVM 控制 。
- `notifyAll` - 唤醒所有正在 `Waiting` 状态的线程，接下来它们需要竞争对象锁。

> 注意：
>
> - **`wait`、`notify`、`notifyAll` 都是 `Object` 类中的方法**，而非 `Thread`。
> - **`wait`、`notify`、`notifyAll` 只能用在 `synchronized` 方法或者 `synchronized` 代码块中使用，否则会在运行时抛出 `IllegalMonitorStateException`**。
>
> 为什么 `wait`、`notify`、`notifyAll` 不定义在 `Thread` 中？为什么 `wait`、`notify`、`notifyAll` 要配合 `synchronized` 使用？
>
> 首先，需要了解几个基本知识点：
>
> - 每一个 Java 对象都有一个与之对应的 **监视器（monitor）**
> - 每一个监视器里面都有一个 **对象锁** 、一个 **等待队列**、一个 **同步队列**
>
> 了解了以上概念，我们回过头来理解前面两个问题。
>
> 为什么这几个方法不定义在 `Thread` 中？
>
> 由于每个对象都拥有对象锁，让当前线程等待某个对象锁，自然应该基于这个对象（`Object`）来操作，而非使用当前线程（`Thread`）来操作。因为当前线程可能会等待多个线程的锁，如果基于线程（`Thread`）来操作，就非常复杂了。
>
> 为什么 `wait`、`notify`、`notifyAll` 要配合 `synchronized` 使用？
>
> 如果调用某个对象的 `wait` 方法，当前线程必须拥有这个对象的对象锁，因此调用 `wait` 方法必须在 `synchronized` 方法和 `synchronized` 代码块中。

生产者、消费者模式是 `wait`、`notify`、`notifyAll` 的一个经典使用案例：

```java
public class ThreadWaitNotifyDemo02 {

    private static final int QUEUE_SIZE = 10;
    private static final PriorityQueue<Integer> queue = new PriorityQueue<>(QUEUE_SIZE);

    public static void main(String[] args) {
        new Producer("生产者A").start();
        new Producer("生产者B").start();
        new Consumer("消费者A").start();
        new Consumer("消费者B").start();
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

### 4.2. join

在线程操作中，可以使用 `join` 方法让一个线程强制运行，线程强制运行期间，其他线程无法运行，必须等待此线程完成之后才可以继续执行。

```java
public class ThreadJoinDemo {

    public static void main(String[] args) {
        MyThread mt = new MyThread(); // 实例化Runnable子类对象
        Thread t = new Thread(mt, "mythread"); // 实例化Thread对象
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

### 4.3. 管道

管道输入/输出流和普通的文件输入/输出流或者网络输入/输出流不同之处在于，它主要用于线程之间的数据传输，而传输的媒介为内存。
管道输入/输出流主要包括了如下 4 种具体实现：`PipedOutputStream`、`PipedInputStream`、`PipedReader` 和 `PipedWriter`，前两种面向字节，而后两种面向字符。

```java
public class Piped {

    public static void main(String[] args) throws Exception {
        PipedWriter out = new PipedWriter();
        PipedReader in = new PipedReader();
        // 将输出流和输入流进行连接，否则在使用时会抛出IOException
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

## 5. 线程生命周期

![](https://raw.githubusercontent.com/dunwu/images/dev/snap/20210102103928.png)

`java.lang.Thread.State` 中定义了 **6** 种不同的线程状态，在给定的一个时刻，线程只能处于其中的一个状态。

以下是各状态的说明，以及状态间的联系：

- **新建（New）** - 尚未调用 `start` 方法的线程处于此状态。此状态意味着：**创建的线程尚未启动**。

- **就绪（Runnable）** - 已经调用了 `start` 方法的线程处于此状态。此状态意味着：**线程已经在 JVM 中运行**。但是在操作系统层面，它可能处于运行状态，也可能等待资源调度（例如处理器资源），资源调度完成就进入运行状态。所以该状态的可运行是指可以被运行，具体有没有运行要看底层操作系统的资源调度。

- **阻塞（Blocked）** - 此状态意味着：**线程处于被阻塞状态**。表示线程在等待 `synchronized` 的隐式锁（Monitor lock）。`synchronized` 修饰的方法、代码块同一时刻只允许一个线程执行，其他线程只能等待，即处于阻塞状态。当占用 `synchronized` 隐式锁的线程释放锁，并且等待的线程获得 `synchronized` 隐式锁时，就又会从 `BLOCKED` 转换到 `RUNNABLE` 状态。

- **等待（Waiting）** - 此状态意味着：**线程无限期等待，直到被其他线程显式地唤醒**。 阻塞和等待的区别在于，阻塞是被动的，它是在等待获取 `synchronized` 的隐式锁。而等待是主动的，通过调用 `Object.wait` 等方法进入。

  | 进入方法                                                       | 退出方法                             |
  | -------------------------------------------------------------- | ------------------------------------ |
  | 没有设置 Timeout 参数的 `Object.wait` 方法                     | `Object.notify` / `Object.notifyAll` |
  | 没有设置 Timeout 参数的 `Thread.join` 方法                     | 被调用的线程执行完毕                 |
  | `LockSupport.park` 方法（Java 并发包中的锁，都是基于它实现的） | `LockSupport.unpark`                 |

- **定时等待（Timed waiting）** - 此状态意味着：**无需等待其它线程显式地唤醒，在一定时间之后会被系统自动唤醒**。

  | 进入方法                                                                       | 退出方法                                        |
  | ------------------------------------------------------------------------------ | ----------------------------------------------- |
  | `Thread.sleep` 方法                                                            | 时间结束                                        |
  | 获得 `synchronized` 隐式锁的线程，调用设置了 Timeout 参数的 `Object.wait` 方法 | 时间结束 / `Object.notify` / `Object.notifyAll` |
  | 设置了 Timeout 参数的 `Thread.join` 方法                                       | 时间结束 / 被调用的线程执行完毕                 |
  | `LockSupport.parkNanos` 方法                                                   | `LockSupport.unpark`                            |
  | `LockSupport.parkUntil` 方法                                                   | `LockSupport.unpark`                            |

- **终止(Terminated)** - 线程执行完 `run` 方法，或者因异常退出了 `run` 方法。此状态意味着：线程结束了生命周期。

## 6. 线程常见问题

### 6.1. sleep、yield、join 方法有什么区别

- `yield` 方法
  - `yield` 方法会 **让线程从 `Running` 状态转入 `Runnable` 状态**。
  - 当调用了 `yield` 方法后，只有**与当前线程相同或更高优先级的`Runnable` 状态线程才会获得执行的机会**。
- `sleep` 方法
  - `sleep` 方法会 **让线程从 `Running` 状态转入 `Waiting` 状态**。
  - `sleep` 方法需要指定等待的时间，**超过等待时间后，JVM 会将线程从 `Waiting` 状态转入 `Runnable` 状态**。
  - 当调用了 `sleep` 方法后，**无论什么优先级的线程都可以得到执行机会**。
  - `sleep` 方法不会释放“锁标志”，也就是说如果有 `synchronized` 同步块，其他线程仍然不能访问共享数据。
- `join`
  - `join` 方法会 **让线程从 `Running` 状态转入 `Waiting` 状态**。
  - 当调用了 `join` 方法后，**当前线程必须等待调用 `join` 方法的线程结束后才能继续执行**。

### 6.2. 为什么 sleep 和 yield 方法是静态的

`Thread` 类的 `sleep` 和 `yield` 方法将处理 `Running` 状态的线程。

所以在其他处于非 `Running` 状态的线程上执行这两个方法是没有意义的。这就是为什么这些方法是静态的。它们可以在当前正在执行的线程中工作，并避免程序员错误的认为可以在其他非运行线程调用这些方法。

### 6.3. Java 线程是否按照线程优先级严格执行

即使设置了线程的优先级，也**无法保证高优先级的线程一定先执行**。

原因在于线程优先级依赖于操作系统的支持，然而，不同的操作系统支持的线程优先级并不相同，不能很好的和 Java 中线程优先级一一对应。

### 6.4. 一个线程两次调用 start()方法会怎样

Java 的线程是不允许启动两次的，第二次调用必然会抛出 IllegalThreadStateException，这是一种运行时异常，多次调用 start 被认为是编程错误。

### 6.5. `start` 和 `run` 方法有什么区别

- `run` 方法是线程的执行体。
- `start` 方法会启动线程，然后 JVM 会让这个线程去执行 `run` 方法。

### 6.6. 可以直接调用 `Thread` 类的 `run` 方法么

- 可以。但是如果直接调用 `Thread` 的 `run` 方法，它的行为就会和普通的方法一样。
- 为了在新的线程中执行我们的代码，必须使用 `Thread` 的 `start` 方法。

## 7. 参考资料

- [《Java 并发编程实战》](https://book.douban.com/subject/10484692/)
- [《Java 并发编程的艺术》](https://book.douban.com/subject/26591326/)
- [进程和线程关系及区别](https://blog.csdn.net/yaosiming2011/article/details/44280797)
- [Java 线程中 yield 与 join 方法的区别](http://www.importnew.com/14958.html)
- [sleep()，wait()，yield()和 join()方法的区别](https://blog.csdn.net/xiangwanpeng/article/details/54972952)
- [Java 并发编程：线程间协作的两种方式：wait、notify、notifyAll 和 Condition](https://www.cnblogs.com/dolphin0520/p/3920385.html)
- [Java 并发编程：Callable、Future 和 FutureTask](https://www.cnblogs.com/dolphin0520/p/3949310.html)
- [StackOverflow VisualVM - Thread States](https://stackoverflow.com/questions/27406200/visualvm-thread-states)
- [Java 中守护线程的总结](https://blog.csdn.net/shimiso/article/details/8964414)
- [Java 并发](https://github.com/CyC2018/CS-Notes/blob/master/notes/Java%20%E5%B9%B6%E5%8F%91.md)
- [Why must wait() always be in synchronized block](https://stackoverflow.com/questions/2779484/why-must-wait-always-be-in-synchronized-block)
