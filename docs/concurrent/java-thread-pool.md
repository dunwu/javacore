# Java 线程池

> **📦 本文以及示例源码已归档在 [javacore](https://github.com/dunwu/javacore/)**

<!-- TOC depthFrom:2 depthTo:3 -->

- [一、简介](#一简介)
  - [什么是线程池](#什么是线程池)
  - [为什么要用线程池](#为什么要用线程池)
- [二、Executor 框架](#二executor-框架)
  - [核心 API 概述](#核心-api-概述)
  - [Executor](#executor)
  - [ExecutorService](#executorservice)
  - [ScheduledExecutorService](#scheduledexecutorservice)
- [三、ThreadPoolExecutor](#三threadpoolexecutor)
  - [重要字段](#重要字段)
  - [构造方法](#构造方法)
  - [execute 方法](#execute-方法)
  - [其他重要方法](#其他重要方法)
  - [使用示例](#使用示例)
- [四、Executors](#四executors)
  - [newSingleThreadExecutor](#newsinglethreadexecutor)
  - [newFixedThreadPool](#newfixedthreadpool)
  - [newCachedThreadPool](#newcachedthreadpool)
  - [newScheduleThreadPool](#newschedulethreadpool)
- [五、线程池优化](#五线程池优化)
  - [计算线程数量](#计算线程数量)
- [参考资料](#参考资料)

<!-- /TOC -->

## 一、简介

### 什么是线程池

线程池是一种多线程处理形式，处理过程中将任务添加到队列，然后在创建线程后自动启动这些任务。

### 为什么要用线程池

如果并发请求数量很多，但每个线程执行的时间很短，就会出现频繁的创建和销毁线程。如此一来，会大大降低系统的效率，可能频繁创建和销毁线程的时间、资源开销要大于实际工作的所需。

正是由于这个问题，所以有必要引入线程池。使用 **线程池的好处** 有以下几点：

- **降低资源消耗** - 通过重复利用已创建的线程降低线程创建和销毁造成的消耗。
- **提高响应速度** - 当任务到达时，任务可以不需要等到线程创建就能立即执行。
- **提高线程的可管理性** - 线程是稀缺资源，如果无限制的创建，不仅会消耗系统资源，还会降低系统的稳定性，使用线程池可以进行统一的分配，调优和监控。但是要做到合理的利用线程池，必须对其原理了如指掌。

## 二、Executor 框架

> Executor 框架是一个根据一组执行策略调用，调度，执行和控制的异步任务的框架，目的是提供一种将”任务提交”与”任务如何运行”分离开来的机制。

### 核心 API 概述

Executor 框架核心 API 如下：

- `Executor` - 运行任务的简单接口。
- `ExecutorService` - 扩展了 `Executor` 接口。扩展能力：
  - 支持有返回值的线程；
  - 支持管理线程的生命周期。
- `ScheduledExecutorService` - 扩展了 `ExecutorService` 接口。扩展能力：支持定期执行任务。
- `AbstractExecutorService` - `ExecutorService` 接口的默认实现。
- `ThreadPoolExecutor` - Executor 框架最核心的类，它继承了 `AbstractExecutorService` 类。
- `ScheduledThreadPoolExecutor` - `ScheduledExecutorService` 接口的实现，一个可定时调度任务的线程池。
- `Executors` - 可以通过调用 `Executors` 的静态工厂方法来创建线程池并返回一个 `ExecutorService` 对象。

![img](http://dunwu.test.upcdn.net/cs/java/javacore/concurrent/exexctor-uml.png)

### Executor

`Executor` 接口中只定义了一个 `execute` 方法，用于接收一个 `Runnable` 对象。

```java
public interface Executor {
    void execute(Runnable command);
}
```

### ExecutorService

`ExecutorService` 接口继承了 `Executor` 接口，它还提供了 `invokeAll`、`invokeAny`、`shutdown`、`submit` 等方法。

```java
public interface ExecutorService extends Executor {

    void shutdown();

    List<Runnable> shutdownNow();

    boolean isShutdown();

    boolean isTerminated();

    boolean awaitTermination(long timeout, TimeUnit unit)
        throws InterruptedException;

    <T> Future<T> submit(Callable<T> task);

    <T> Future<T> submit(Runnable task, T result);

    Future<?> submit(Runnable task);

    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
        throws InterruptedException;

    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks,
                                  long timeout, TimeUnit unit)
        throws InterruptedException;

    <T> T invokeAny(Collection<? extends Callable<T>> tasks)
        throws InterruptedException, ExecutionException;

    <T> T invokeAny(Collection<? extends Callable<T>> tasks,
                    long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException;
}
```

从其支持的方法定义，不难看出：相比于 `Executor` 接口，`ExecutorService` 接口主要的扩展是：

- 支持有返回值的线程 - `sumbit`、`invokeAll`、`invokeAny` 方法中都支持传入`Callable` 对象。
- 支持管理线程生命周期 - `shutdown`、`shutdownNow`、`isShutdown` 等方法。

### ScheduledExecutorService

`ScheduledExecutorService` 接口扩展了 `ExecutorService` 接口。

它除了支持前面两个接口的所有能力以外，还支持定时调度线程。

```java
public interface ScheduledExecutorService extends ExecutorService {

    public ScheduledFuture<?> schedule(Runnable command,
                                       long delay, TimeUnit unit);

    public <V> ScheduledFuture<V> schedule(Callable<V> callable,
                                           long delay, TimeUnit unit);

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command,
                                                  long initialDelay,
                                                  long period,
                                                  TimeUnit unit);

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,
                                                     long initialDelay,
                                                     long delay,
                                                     TimeUnit unit);

}
```

其扩展的接口提供以下能力：

- `schedule` 方法可以在指定的延时后执行一个 `Runnable` 或者 `Callable` 任务。
- `scheduleAtFixedRate` 方法和 `scheduleWithFixedDelay` 方法可以按照指定时间间隔，定期执行任务。

## 三、ThreadPoolExecutor

`java.uitl.concurrent.ThreadPoolExecutor` 类是 `Executor` 框架中最核心的类。所以，本文将着重讲述一下这个类。

### 重要字段

`ThreadPoolExecutor` 有以下重要字段：

```java
private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
private static final int COUNT_BITS = Integer.SIZE - 3;
private static final int CAPACITY   = (1 << COUNT_BITS) - 1;
// runState is stored in the high-order bits
private static final int RUNNING    = -1 << COUNT_BITS;
private static final int SHUTDOWN   =  0 << COUNT_BITS;
private static final int STOP       =  1 << COUNT_BITS;
private static final int TIDYING    =  2 << COUNT_BITS;
private static final int TERMINATED =  3 << COUNT_BITS;
```

参数说明：

- `ctl` - **用于控制线程池的运行状态和线程池中的有效线程数量**。它包含两部分的信息：
  - 线程池的运行状态 (`runState`)
  - 线程池内有效线程的数量 (`workerCount`)
  - 可以看到，`ctl` 使用了 `Integer` 类型来保存，高 3 位保存 `runState`，低 29 位保存 `workerCount`。`COUNT_BITS` 就是 29，`CAPACITY` 就是 1 左移 29 位减 1（29 个 1），这个常量表示 `workerCount` 的上限值，大约是 5 亿。
- 运行状态 - 线程池一共有五种运行状态：
  - `RUNNING` - **运行状态**。接受新任务，并且也能处理阻塞队列中的任务。
  - `SHUTDOWN` - **关闭状态**。不接受新任务，但可以处理阻塞队列中的任务。
    - 在线程池处于 `RUNNING` 状态时，调用 `shutdown` 方法会使线程池进入到该状态。
    - `finalize` 方法在执行过程中也会调用 `shutdown` 方法进入该状态。
  - `STOP` - **停止状态**。不接受新任务，也不处理队列中的任务。会中断正在处理任务的线程。在线程池处于 `RUNNING` 或 `SHUTDOWN` 状态时，调用 `shutdownNow` 方法会使线程池进入到该状态。
  - `TIDYING` - **整理状态**。如果所有的任务都已终止了，`workerCount` (有效线程数) 为 0，线程池进入该状态后会调用 `terminated` 方法进入 `TERMINATED` 状态。
  - `TERMINATED` - **已终止状态**。在 `terminated` 方法执行完后进入该状态。默认 `terminated` 方法中什么也没有做。进入 `TERMINATED` 的条件如下：
    - 线程池不是 `RUNNING` 状态；
    - 线程池状态不是 `TIDYING` 状态或 `TERMINATED` 状态；
    - 如果线程池状态是 `SHUTDOWN` 并且 `workerQueue` 为空；
    - `workerCount` 为 0；
    - 设置 `TIDYING` 状态成功。

![img](http://dunwu.test.upcdn.net/cs/java/javacore/concurrent/java-thread-pool_2.png)

### 构造方法

`ThreadPoolExecutor` 有四个构造方法，前三个都是基于第四个实现。第四个构造方法定义如下：

```java
public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) {
```

参数说明：

- `corePoolSize` - **核心线程数量**。当有新任务通过 `execute` 方法提交时 ，线程池会执行以下判断：
  - 如果运行的线程数少于 `corePoolSize`，则创建新线程来处理任务，即使线程池中的其他线程是空闲的。
  - 如果线程池中的线程数量大于等于 `corePoolSize` 且小于 `maximumPoolSize`，则只有当 `workQueue` 满时才创建新的线程去处理任务；
  - 如果设置的 `corePoolSize` 和 `maximumPoolSize` 相同，则创建的线程池的大小是固定的。这时如果有新任务提交，若 `workQueue` 未满，则将请求放入 `workQueue` 中，等待有空闲的线程去从 `workQueue` 中取任务并处理；
  - 如果运行的线程数量大于等于 `maximumPoolSize`，这时如果 `workQueue` 已经满了，则使用 `handler` 所指定的策略来处理任务；
  - 所以，任务提交时，判断的顺序为 `corePoolSize` => `workQueue` => `maximumPoolSize`。
- `maximumPoolSize` - **最大线程数量**。
  - 如果队列满了，并且已创建的线程数小于最大线程数，则线程池会再创建新的线程执行任务。
  - 值得注意的是：如果使用了无界的任务队列这个参数就没什么效果。
- `keepAliveTime`：**线程保持活动的时间**。
  - 当线程池中的线程数量大于 `corePoolSize` 的时候，如果这时没有新的任务提交，核心线程外的线程不会立即销毁，而是会等待，直到等待的时间超过了 `keepAliveTime`。
  - 所以，如果任务很多，并且每个任务执行的时间比较短，可以调大这个时间，提高线程的利用率。
- `unit` - **`keepAliveTime` 的时间单位**。有 7 种取值。可选的单位有天（DAYS），小时（HOURS），分钟（MINUTES），毫秒(MILLISECONDS)，微秒(MICROSECONDS, 千分之一毫秒)和毫微秒(NANOSECONDS, 千分之一微秒)。
- `workQueue` - **等待执行的任务队列**。用于保存等待执行的任务的阻塞队列。 可以选择以下几个阻塞队列。
  - `ArrayBlockingQueue` - **有界阻塞队列**。
    - 此队列是**基于数组的先进先出队列（FIFO）**。
    - 此队列创建时必须指定大小。
  - `LinkedBlockingQueue` - **无界阻塞队列**。
    - 此队列是**基于链表的先进先出队列（FIFO）**。
    - 如果创建时没有指定此队列大小，则默认为 `Integer.MAX_VALUE`。
    - 吞吐量通常要高于 `ArrayBlockingQueue`。
    - 使用 `LinkedBlockingQueue` 意味着： `maximumPoolSize` 将不起作用，线程池能创建的最大线程数为 `corePoolSize`，因为任务等待队列是无界队列。
    - `Executors.newFixedThreadPool` 使用了这个队列。
  - `SynchronousQueue` - **不会保存提交的任务，而是将直接新建一个线程来执行新来的任务**。
    - 每个插入操作必须等到另一个线程调用移除操作，否则插入操作一直处于阻塞状态。
    - 吞吐量通常要高于 `LinkedBlockingQueue`。
    - `Executors.newCachedThreadPool` 使用了这个队列。
  - `PriorityBlockingQueue` - **具有优先级的无界阻塞队列**。
- `threadFactory` - **线程工厂**。可以通过线程工厂给每个创建出来的线程设置更有意义的名字。
- `handler` - **饱和策略**。它是 `RejectedExecutionHandler` 类型的变量。当队列和线程池都满了，说明线程池处于饱和状态，那么必须采取一种策略处理提交的新任务。线程池支持以下策略：
  - `AbortPolicy` - 丢弃任务并抛出异常。这也是默认策略。
  - `DiscardPolicy` - 丢弃任务，但不抛出异常。
  - `DiscardOldestPolicy` - 丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）。
  - `CallerRunsPolicy` - 直接调用 `run` 方法并且阻塞执行。
  - 如果以上策略都不能满足需要，也可以通过实现 `RejectedExecutionHandler` 接口来定制处理策略。如记录日志或持久化不能处理的任务。

### execute 方法

默认情况下，创建线程池之后，线程池中是没有线程的，需要提交任务之后才会创建线程。

提交任务可以使用 `execute` 方法，它是 `ThreadPoolExecutor` 的核心方法，通过这个方法可以**向线程池提交一个任务，交由线程池去执行**。

`execute` 方法工作流程如下：

1. 如果 `workerCount < corePoolSize`，则创建并启动一个线程来执行新提交的任务；
2. 如果 `workerCount >= corePoolSize`，且线程池内的阻塞队列未满，则将任务添加到该阻塞队列中；
3. 如果 `workerCount >= corePoolSize && workerCount < maximumPoolSize`，且线程池内的阻塞队列已满，则创建并启动一个线程来执行新提交的任务；
4. 如果`workerCount >= maximumPoolSize`，并且线程池内的阻塞队列已满，则根据拒绝策略来处理该任务, 默认的处理方式是直接抛异常。

![img](http://dunwu.test.upcdn.net/cs/java/javacore/concurrent/java-thread-pool_1.png)

### 其他重要方法

在 `ThreadPoolExecutor` 类中还有一些重要的方法：

- `submit` - 类似于 `execute`，但是针对的是有返回值的线程。`submit` 方法是在 `ExecutorService` 中声明的方法，在 `AbstractExecutorService` 就已经有了具体的实现。`ThreadPoolExecutor` 直接复用 `AbstractExecutorService` 的 `submit` 方法。
- `shutdown` - 不会立即终止线程池，而是要等所有任务缓存队列中的任务都执行完后才终止，但再也不会接受新的任务。
  - 将线程池切换到 `SHUTDOWN` 状态；
  - 并调用 `interruptIdleWorkers` 方法请求中断所有空闲的 worker；
  - 最后调用 `tryTerminate` 尝试结束线程池。
- `shutdownNow` - 立即终止线程池，并尝试打断正在执行的任务，并且清空任务缓存队列，返回尚未执行的任务。与 `shutdown` 方法类似，不同的地方在于：
  - 设置状态为 `STOP`；
  - 中断所有工作线程，无论是否是空闲的；
  - 取出阻塞队列中没有被执行的任务并返回。
- `isShutdown` - 调用了 `shutdown` 或 `shutdownNow` 方法后，`isShutdown` 方法就会返回 true。
- `isTerminaed` - 当所有的任务都已关闭后，才表示线程池关闭成功，这时调用 `isTerminaed` 方法会返回 true。
- `setCorePoolSize` - 设置核心线程数大小。
- `setMaximumPoolSize` - 设置最大线程数大小。
- `getTaskCount` - 线程池已经执行的和未执行的任务总数；
- `getCompletedTaskCount` - 线程池已完成的任务数量，该值小于等于 `taskCount`；
- `getLargestPoolSize` - 线程池曾经创建过的最大线程数量。通过这个数据可以知道线程池是否满过，也就是达到了 `maximumPoolSize`；
- `getPoolSize` - 线程池当前的线程数量；
- `getActiveCount` - 当前线程池中正在执行任务的线程数量。

### 使用示例

```java
public class ThreadPoolExecutorDemo {

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 500, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

        for (int i = 0; i < 100; i++) {
            threadPoolExecutor.execute(new MyThread());
            String info = String.format("线程池中线程数目：%s，队列中等待执行的任务数目：%s，已执行玩别的任务数目：%s",
                threadPoolExecutor.getPoolSize(),
                threadPoolExecutor.getQueue().size(),
                threadPoolExecutor.getCompletedTaskCount());
            System.out.println(info);
        }
        threadPoolExecutor.shutdown();
    }

    static class MyThread implements Runnable {

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " 执行");
        }

    }

}
```

## 四、Executors

JDK 的 `Executors` 类中提供了几种具有代表性的线程池，这些线程池 **都是基于 `ThreadPoolExecutor` 的定制化实现**。

在实际使用线程池的场景中，我们往往不是直接使用 `ThreadPoolExecutor` ，而是使用 JDK 中提供的具有代表性的线程池实例。

### newSingleThreadExecutor

**创建一个单线程的线程池**。

只会创建唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。 **如果这个唯一的线程因为异常结束，那么会有一个新的线程来替代它** 。

单工作线程最大的特点是：**可保证顺序地执行各个任务**。

示例：

```java
public class SingleThreadExecutorDemo {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 100; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " 执行");
                }
            });
        }
        executorService.shutdown();
    }

}
```

### newFixedThreadPool

**创建一个固定大小的线程池**。

**每次提交一个任务就会新创建一个工作线程，如果工作线程数量达到线程池最大线程数，则将提交的任务存入到阻塞队列中**。

`FixedThreadPool` 是一个典型且优秀的线程池，它具有线程池提高程序效率和节省创建线程时所耗的开销的优点。但是，在线程池空闲时，即线程池中没有可运行任务时，它不会释放工作线程，还会占用一定的系统资源。

示例：

```java
public class FixedThreadPoolDemo {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 100; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " 执行");
                }
            });
        }
        executorService.shutdown();
    }

}
```

### newCachedThreadPool

**创建一个可缓存的线程池**。

- 如果线程池大小超过处理任务所需要的线程数，就会回收部分空闲的线程；
- 如果长时间没有往线程池中提交任务，即如果工作线程空闲了指定的时间（默认为 1 分钟），则该工作线程将自动终止。终止后，如果你又提交了新的任务，则线程池重新创建一个工作线程。
- 此线程池不会对线程池大小做限制，线程池大小完全依赖于操作系统（或者说 JVM）能够创建的最大线程大小。 因此，使用 `CachedThreadPool` 时，一定要注意控制任务的数量，否则，由于大量线程同时运行，很有会造成系统瘫痪。

示例：

```java
public class CachedThreadPoolDemo {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " 执行");
                }
            });
        }
        executorService.shutdown();
    }

}
```

### newScheduleThreadPool

创建一个大小无限的线程池。此线程池支持定时以及周期性执行任务的需求。

```java
public class ScheduledThreadPoolDemo {

    public static void main(String[] args) {
        schedule();
        scheduleAtFixedRate();
    }

    private static void schedule() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
        for (int i = 0; i < 100; i++) {
            executorService.schedule(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " 执行");
                }
            }, 1, TimeUnit.SECONDS);
        }
        executorService.shutdown();
    }

    private static void scheduleAtFixedRate() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
        for (int i = 0; i < 100; i++) {
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " 执行");
                }
            }, 1, 1, TimeUnit.SECONDS);
        }
        executorService.shutdown();
    }

}
```

## 五、线程池最佳实践

### 计算线程数量

一般多线程执行的任务类型可以分为 CPU 密集型和 I/O 密集型，根据不同的任务类型，我们计算线程数的方法也不一样。

**CPU 密集型任务：**这种任务消耗的主要是 CPU 资源，可以将线程数设置为 N（CPU 核心数）+1，比 CPU 核心数多出来的一个线程是为了防止线程偶发的缺页中断，或者其它原因导致的任务暂停而带来的影响。一旦任务暂停，CPU 就会处于空闲状态，而在这种情况下多出来的一个线程就可以充分利用 CPU 的空闲时间。

**I/O 密集型任务：**这种任务应用起来，系统会用大部分的时间来处理 I/O 交互，而线程在处理 I/O 的时间段内不会占用 CPU 来处理，这时就可以将 CPU 交出给其它线程使用。因此在 I/O 密集型任务的应用中，我们可以多配置一些线程，具体的计算方法是 2N。

### 线程池使用注意点

不建议使用 Executors 的最重要的原因是：Executors 提供的很多方法默认使用的都是无界的 LinkedBlockingQueue，高负载情境下，无界队列很容易导致 OOM，而 OOM 会导致所有请求都无法处理，这是致命问题。所以**强烈建议使用有界队列**。

使用有界队列，当任务过多时，线程池会触发执行拒绝策略，线程池默认的拒绝策略会 throw RejectedExecutionException 这是个运行时异常，对于运行时异常编译器并不强制 catch 它，所以开发人员很容易忽略。因此**默认拒绝策略要慎重使用**。如果线程池处理的任务非常重要，建议自定义自己的拒绝策略；并且在实际工作中，自定义的拒绝策略往往和降级策略配合使用。

## 六、线程池使用误区

《阿里巴巴 Java 开发手册》中提到，禁止使用这些方法来创建线程池，而应该手动 `new ThreadPoolExecutor` 来创建线程池。制订这条规则是因为容易导致生产事故，最典型的就是 newFixedThreadPool 和 newCachedThreadPool，可能因为资源耗尽导致 OOM 问题。

【示例】newFixedThreadPool OOM

```java
ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
printStats(threadPool);
for (int i = 0; i < 100000000; i++) {
	threadPool.execute(() -> {
		String payload = IntStream.rangeClosed(1, 1000000)
			.mapToObj(__ -> "a")
			.collect(Collectors.joining("")) + UUID.randomUUID().toString();
		try {
			TimeUnit.HOURS.sleep(1);
		} catch (InterruptedException e) {
		}
		log.info(payload);
	});
}

threadPool.shutdown();
threadPool.awaitTermination(1, TimeUnit.HOURS);
```

newFixedThreadPool 使用的工作队列是 `LinkedBlockingQueue` ，而默认构造方法的 `LinkedBlockingQueue` 是一个 `Integer.MAX_VALUE` 长度的队列，可以认为是无界的。如果任务较多并且执行较慢的话，队列可能会快速积压，撑爆内存导致 OOM。

【示例】newCachedThreadPool OOM

```java
ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
printStats(threadPool);
for (int i = 0; i < 100000000; i++) {
	threadPool.execute(() -> {
		String payload = UUID.randomUUID().toString();
		try {
			TimeUnit.HOURS.sleep(1);
		} catch (InterruptedException e) {
		}
		log.info(payload);
	});
}
threadPool.shutdown();
threadPool.awaitTermination(1, TimeUnit.HOURS);
```

`newCachedThreadPool` 的最大线程数是 `Integer.MAX_VALUE`，可以认为是没有上限的，而其工作队列 `SynchronousQueue` 是一个没有存储空间的阻塞队列。这意味着，只要有请求到来，就必须找到一条工作线程来处理，如果当前没有空闲的线程就再创建一条新的。

如果大量的任务进来后会创建大量的线程。我们知道线程是需要分配一定的内存空间作为线程栈的，比如 1MB，因此无限制创建线程必然会导致 OOM。

## 参考资料

- [《Java 并发编程实战》](https://item.jd.com/10922250.html)
- [《Java 并发编程的艺术》](https://item.jd.com/11740734.html)
- [深入理解 Java 线程池：ThreadPoolExecutor](https://www.jianshu.com/p/d2729853c4da)
- [java 并发编程--Executor 框架](https://www.cnblogs.com/MOBIN/p/5436482.html)
