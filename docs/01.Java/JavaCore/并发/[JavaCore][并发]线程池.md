---
title: Java 并发之线程池
date: 2019-12-24 23:52:25
categories:
  - Java
  - JavaCore
  - 并发
tags:
  - Java
  - JavaCore
  - 并发
  - 线程池
permalink: /pages/62a6d73f/
---

# Java 并发之线程池

## 线程池简介

线程池就是管理一系列线程的资源池，其提供了一种限制和管理线程资源的方式。每个线程池还维护一些基本统计信息，例如已完成任务的数量。

如果并发请求数量很多，但每个线程执行的时间很短，就会出现频繁的创建和销毁线程。如此一来，会大大降低系统的效率，可能频繁创建和销毁线程的时间、资源开销要大于实际工作的所需。

使用 **线程池的好处** 有以下几点：

- **降低资源消耗** - 通过重复利用已创建的线程降低线程创建和销毁造成的消耗。
- **提高响应速度** - 当任务到达时，任务可以不需要等到线程创建就能立即执行。
- **提高线程的可管理性** - 线程是稀缺资源，如果无限制的创建，不仅会消耗系统资源，还会降低系统的稳定性，使用线程池可以进行统一的分配，调优和监控。

## Executor 框架

Executor 框架是一个根据一组执行策略调用，调度，执行和控制的异步任务的框架，目的是提供一种将”任务提交”与”任务如何运行”分离开来的机制。

通过 `Executor` 来启动线程比使用 `Thread` 的 `start` 方法更好，除了更易管理，效率更好（用线程池实现，节约开销）外，还有关键的一点：有助于避免 this 逃逸问题。

> this 逃逸是指在构造函数返回之前其他线程就持有该对象的引用，调用尚未构造完全的对象的方法可能引发令人疑惑的错误。

### 核心 API 概述

Executor 框架核心 API 如下：

- `Executor` - 运行任务的接口。
- `ExecutorService` - 扩展了 `Executor` 接口。扩展能力：
  - 支持有返回值的线程；
  - 支持管理线程的生命周期。
- `ScheduledExecutorService` - 扩展了 `ExecutorService` 接口，支持定时调度任务。
- `AbstractExecutorService` - `ExecutorService` 接口的默认实现。
- `ThreadPoolExecutor` - Executor 框架最核心的类，它继承了 `AbstractExecutorService` 类。
- `ScheduledThreadPoolExecutor` - `ScheduledExecutorService` 接口的实现，一个可定时调度任务的线程池。
- `Executors` - 可以通过调用 `Executors` 的静态工厂方法来创建线程池并返回一个 `ExecutorService` 对象。

![](https://raw.githubusercontent.com/dunwu/images/master/cs/java/javacore/concurrent/exexctor-uml.png)

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

## ThreadPoolExecutor

`java.uitl.concurrent.ThreadPoolExecutor` 类是 `Executor` 框架中最核心的类。

### 构造方法

`ThreadPoolExecutor` 有四个构造方法，前三个都是基于第四个实现。第四个构造方法定义如下：

```java
public ThreadPoolExecutor(int corePoolSize,// 线程池的核心线程数量
						  int maximumPoolSize,// 线程池的最大线程数
						  long keepAliveTime,// 当线程数大于核心线程数时，多余的空闲线程存活的最长时间
						  TimeUnit unit,// 时间单位
						  BlockingQueue<Runnable> workQueue,// 任务队列，用来储存等待执行任务的队列
						  ThreadFactory threadFactory,// 线程工厂，用来创建线程，一般默认即可
						  RejectedExecutionHandler handler// 拒绝策略，当提交的任务过多而不能及时处理时，我们可以定制策略来处理任务
) {// 略}
```

参数说明：

- **`corePoolSize`** - **表示线程池保有的最小线程数**。
- **`maximumPoolSize`** - **表示线程池允许创建的最大线程数**。
  - 如果队列满了，并且已创建的线程数小于最大线程数，则线程池会再创建新的线程执行任务。
  - 值得注意的是：如果使用了无界的任务队列这个参数就没什么效果。
- **`keepAliveTime & unit`** - **表示线程保持活动的时间**。如果一个线程空闲了`keepAliveTime & unit` 这么久，而且线程池的线程数大于 `corePoolSize` ，那么这个空闲的线程就要被回收了。
- **`workQueue`** - **等待执行的任务队列**。用于保存等待执行的任务的阻塞队列。 可以选择以下几个阻塞队列。
  - **`ArrayBlockingQueue`** - **有界阻塞队列**。
  - **`LinkedBlockingQueue`** - **无界阻塞队列**。
  - **`SynchronousQueue`** - **不会保存提交的任务，而是将直接新建一个线程来执行新来的任务**。
  - **`DelayedWorkQueue`** - 延迟阻塞队列。
  - **`PriorityBlockingQueue`** - **具有优先级的无界阻塞队列**。
- **`threadFactory`** - **线程工厂**。线程工程用于自定义如何创建线程。
- **`handler`** - **拒绝策略**。它是 `RejectedExecutionHandler` 类型的变量。当队列和线程池都满了，说明线程池处于饱和状态，那么必须采取一种策略处理提交的新任务。线程池支持以下策略：
  - **`AbortPolicy`** - **丢弃任务并抛出异常**。这也是默认策略，会抛出 `RejectedExecutionException`。
  - **`DiscardPolicy`** - **丢弃任务但不抛出异常**。
  - **`DiscardOldestPolicy`** - **丢弃队列最老的任务**，其实就是把最早进入工作队列的任务丢弃，然后把新任务加入到工作队列。
  - **`CallerRunsPolicy`** - 提交任务的线程自己去执行该任务。
  - 如果以上策略都不能满足需要，也可以通过实现 `RejectedExecutionHandler` 接口来定制处理策略。如记录日志或持久化不能处理的任务。

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
  - `TIDYING` - **整理状态**。如果所有的任务都已终止了，`workerCount` （有效线程数） 为 0，线程池进入该状态后会调用 `terminated` 方法进入 `TERMINATED` 状态。
  - `TERMINATED` - **已终止状态**。在 `terminated` 方法执行完后进入该状态。默认 `terminated` 方法中什么也没有做。进入 `TERMINATED` 的条件如下：
    - 线程池不是 `RUNNING` 状态；
    - 线程池状态不是 `TIDYING` 状态或 `TERMINATED` 状态；
    - 如果线程池状态是 `SHUTDOWN` 并且 `workerQueue` 为空；
    - `workerCount` 为 0；
    - 设置 `TIDYING` 状态成功。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2024/09/e926fa2451744708b1bcaee5b301c6ad.png)

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

## 线程池原理

默认情况下，创建线程池之后，线程池中是没有线程的，需要提交任务之后才会创建线程。提交任务可以使用 `execute` 方法，它是 `ThreadPoolExecutor` 的核心方法，通过这个方法可以**向线程池提交一个任务，交由线程池去执行**。

```java
// 用于控制线程池的运行状态和线程池中的有效线程数量
private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));

public void execute(Runnable command) {
	if (command == null)
		throw new NullPointerException();

    // 获取 ctl 中存储的线程池状态信息
	int c = ctl.get();

    // 线程池执行可以分为 3 个步骤
    // 1. 若工作线程数小于核心线程数，则尝试启动一个新的线程来执行任务
	if (workerCountOf(c) < corePoolSize) {
		if (addWorker(command, true))
			return;
		c = ctl.get();
	}

    // 2. 如果任务可以成功地加入队列，还需要再次确认是否需要添加新的线程（因为可能自从上次检查以来已经有线程死亡）或者检查线程池是否已经关闭
    // 	-> 如果是后者，则可能需要回滚入队操作；
    // 	-> 如果是前者，则可能需要启动新的线程
	if (isRunning(c) && workQueue.offer(command)) {
		int recheck = ctl.get();
		if (!isRunning(recheck) && remove(command))
			reject(command);
		else if (workerCountOf(recheck) == 0)
			addWorker(null, false);
	}
    // 如果任务无法加入队列，则尝试添加一个新的线程
    // 如果添加新线程失败，说明线程池已经关闭或者达到了容量上限，此时将拒绝该任务
	else if (!addWorker(command, false))
		reject(command);
}
```

`execute` 方法工作流程如下：

1. 如果 `workerCount < corePoolSize`，则创建并启动一个线程来执行新提交的任务；
2. 如果 `workerCount >= corePoolSize`，且线程池内的阻塞队列未满，则将任务添加到该阻塞队列中；
3. 如果 `workerCount >= corePoolSize && workerCount < maximumPoolSize`，且线程池内的阻塞队列已满，则创建并启动一个线程来执行新提交的任务；
4. 如果`workerCount >= maximumPoolSize`，并且线程池内的阻塞队列已满，则根据拒绝策略来处理该任务，默认的处理方式是直接抛异常。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2024/09/495409421cd54f0289115628bf09ec32.png)

在 `execute` 方法中，多次调用 `addWorker` 方法。`addWorker` 这个方法主要用来创建新的工作线程，如果返回 true 说明创建和启动工作线程成功，否则的话返回的就是 false。

```java
// 全局锁，并发操作必备
private final ReentrantLock mainLock = new ReentrantLock();
// 跟踪线程池的最大大小，只有在持有全局锁 mainLock 的前提下才能访问此集合
private int largestPoolSize;
// 工作线程集合，存放线程池中所有的（活跃的）工作线程，只有在持有全局锁 mainLock 的前提下才能访问此集合
private final HashSet<Worker> workers = new HashSet<>();
//获取线程池状态
private static int runStateOf(int c)     { return c & ~CAPACITY; }
//判断线程池的状态是否为 Running
private static boolean isRunning(int c) {
	return c < SHUTDOWN;
}

/**
 * 添加新的工作线程到线程池
 * @param firstTask 要执行
 * @param core 参数为 true 的话表示使用线程池的基本大小，为 false 使用线程池最大大小
 * @return 添加成功就返回 true 否则返回 false
 */
private boolean addWorker(Runnable firstTask, boolean core) {
	retry:
	for (;;) {
		//这两句用来获取线程池的状态
		int c = ctl.get();
		int rs = runStateOf(c);

		// Check if queue empty only if necessary.
		if (rs >= SHUTDOWN &&
			! (rs == SHUTDOWN &&
			   firstTask == null &&
			   ! workQueue.isEmpty()))
			return false;

		for (;;) {
		   //获取线程池中工作的线程的数量
			int wc = workerCountOf(c);
			// core 参数为 false 的话表明队列也满了，线程池大小变为 maximumPoolSize
			if (wc >= CAPACITY ||
				wc >= (core ? corePoolSize : maximumPoolSize))
				return false;
		   //原子操作将 workcount 的数量加 1
			if (compareAndIncrementWorkerCount(c))
				break retry;
			// 如果线程的状态改变了就再次执行上述操作
			c = ctl.get();
			if (runStateOf(c) != rs)
				continue retry;
			// else CAS failed due to workerCount change; retry inner loop
		}
	}
	// 标记工作线程是否启动成功
	boolean workerStarted = false;
	// 标记工作线程是否创建成功
	boolean workerAdded = false;
	Worker w = null;
	try {

		w = new Worker(firstTask);
		final Thread t = w.thread;
		if (t != null) {
		  // 加锁
			final ReentrantLock mainLock = this.mainLock;
			mainLock.lock();
			try {
			   //获取线程池状态
				int rs = runStateOf(ctl.get());
			   //rs < SHUTDOWN 如果线程池状态依然为 RUNNING, 并且线程的状态是存活的话，就会将工作线程添加到工作线程集合中
			  //(rs=SHUTDOWN && firstTask == null) 如果线程池状态小于 STOP，也就是 RUNNING 或者 SHUTDOWN 状态下，同时传入的任务实例 firstTask 为 null，则需要添加到工作线程集合和启动新的 Worker
			   // firstTask == null 证明只新建线程而不执行任务
				if (rs < SHUTDOWN ||
					(rs == SHUTDOWN && firstTask == null)) {
					if (t.isAlive()) // precheck that t is startable
						throw new IllegalThreadStateException();
					workers.add(w);
				   //更新当前工作线程的最大容量
					int s = workers.size();
					if (s > largestPoolSize)
						largestPoolSize = s;
				  // 工作线程是否启动成功
					workerAdded = true;
				}
			} finally {
				// 释放锁
				mainLock.unlock();
			}
			//// 如果成功添加工作线程，则调用 Worker 内部的线程实例 t 的 Thread#start() 方法启动真实的线程实例
			if (workerAdded) {
				t.start();
			  /// 标记线程启动成功
				workerStarted = true;
			}
		}
	} finally {
	   // 线程启动失败，需要从工作线程中移除对应的 Worker
		if (! workerStarted)
			addWorkerFailed(w);
	}
	return workerStarted;
}
```

## Executors

`Executors` 类中提供了几种内置的 `ThreadPoolExecutor` 实现：

- `FixedThreadPool`：固定线程数量的线程池。该线程池中的线程数量始终不变。当有一个新的任务提交时，线程池中若有空闲线程，则立即执行。若没有，则新的任务会被暂存在一个任务队列中，待有线程空闲时，便处理在任务队列中的任务。
- `SingleThreadExecutor`： 只有一个线程的线程池。若多余一个任务被提交到该线程池，任务会被保存在一个任务队列中，待线程空闲，按先入先出的顺序执行队列中的任务。
- `CachedThreadPool`： 可根据实际情况调整线程数量的线程池。线程池的线程数量不确定，但若有空闲线程可以复用，则会优先使用可复用的线程。若所有线程均在工作，又有新的任务提交，则会创建新的线程处理任务。所有线程在当前任务执行完毕后，将返回线程池进行复用。
- `ScheduledThreadPool`：给定的延迟后运行任务或者定期执行任务的线程池。

> 注意：
>
> 《阿里巴巴 Java 开发手册》中明确要求不要使用 `Executors` 中的内置化线程池。
>
> 【强制】线程池不允许使用 `Executors` 去创建，而是通过 `ThreadPoolExecutor` 的方式，这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。
>
> 说明：`Executors` 返回的线程池对象的弊端如下：
>
> 1. `FixedThreadPool` 和 `SingleThreadPool`： 允许的请求队列长度为 `Integer.MAX_VALUE`，可能会堆积大量的请求，从而导致 OOM。
> 2. `CachedThreadPool`：允许的创建线程数量为 `Integer.MAX_VALUE`，可能会创建大量的线程，从而导致 OOM。
> 3. `ScheduledThreadPool`： 允许的请求队列长度为 `Integer.MAX_VALUE`，可能会堆积大量的请求，从而导致 OOM。

### FixedThreadPool

**FixedThreadPool 是一个可重用的、线程数固定的线程池**。`Executors` 类中的相关源码：

```java
public static ExecutorService newFixedThreadPool(int nThreads) {
	return new ThreadPoolExecutor(nThreads, nThreads,
								  0L, TimeUnit.MILLISECONDS,
								  new LinkedBlockingQueue<Runnable>());
}

public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
	return new ThreadPoolExecutor(nThreads, nThreads,
								  0L, TimeUnit.MILLISECONDS,
								  new LinkedBlockingQueue<Runnable>(),
								  threadFactory);
}
```

`FixedThreadPool` 的 `corePoolSize` 和 `maximumPoolSize` 都被设置为 `nThreads`，这个 `nThreads` 参数是我们使用的时候自己传递的。

即使 `maximumPoolSize` 的值比 `corePoolSize` 大，也至多只会创建 `corePoolSize` 个线程。这是因为`FixedThreadPool` 使用的是容量为 `Integer.MAX_VALUE` 的 `LinkedBlockingQueue`（无界队列），队列永远不会被放满。

FixedThreadPool 的问题：

`FixedThreadPool` 使用无界队列 `LinkedBlockingQueue`（队列的容量为 Integer.MAX_VALUE）作为线程池的工作队列会对线程池带来如下影响：

1. 当线程池中的线程数达到 `corePoolSize` 后，新任务将在无界队列中等待，因此线程池中的线程数不会超过 `corePoolSize`；
2. 由于使用无界队列时 `maximumPoolSize` 将是一个无效参数，因为不可能存在任务队列满的情况。所以，通过创建 `FixedThreadPool`的源码可以看出创建的 `FixedThreadPool` 的 `corePoolSize` 和 `maximumPoolSize` 被设置为同一个值。
3. 由于 1 和 2，使用无界队列时 `keepAliveTime` 将是一个无效参数；
4. 运行中的 `FixedThreadPool`（未执行 `shutdown()`或 `shutdownNow()`）不会拒绝任务，在任务比较多的时候会导致 OOM（内存溢出）。

### SingleThreadExecutor

**`SingleThreadExecutor` 是只有一个线程的线程池**。SingleThreadExecutor 只会创建唯一的工作线程来执行任务，保证所有任务按照指定顺序 (FIFO, LIFO, 优先级）执行。 **如果这个唯一的线程因为异常结束，那么会有一个新的线程来替代它** 。

`Executors` 类中的相关源码：

```java
public static ExecutorService newSingleThreadExecutor() {
	return new FinalizableDelegatedExecutorService
		(new ThreadPoolExecutor(1, 1,
								0L, TimeUnit.MILLISECONDS,
								new LinkedBlockingQueue<Runnable>()));
}

public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
	return new FinalizableDelegatedExecutorService
		(new ThreadPoolExecutor(1, 1,
								0L, TimeUnit.MILLISECONDS,
								new LinkedBlockingQueue<Runnable>(),
								threadFactory));
}
```

`SingleThreadExecutor` 的问题：

`SingleThreadExecutor` 和 `FixedThreadPool` 一样，使用的都是容量为 `Integer.MAX_VALUE` 的 `LinkedBlockingQueue`（无界队列）作为线程池的工作队列。`SingleThreadExecutor` 使用无界队列作为线程池的工作队列会对线程池带来的影响与 `FixedThreadPool` 相同。说简单点，就是可能会导致 OOM。

### CachedThreadPool

`CachedThreadPool` 是一个会根据需要创建新线程的线程池。

- 如果线程池大小超过处理任务所需要的线程数，就会回收部分空闲的线程；
- 如果长时间没有往线程池中提交任务，即如果工作线程空闲了指定的时间（默认为 1 分钟），则该工作线程将自动终止。终止后，如果你又提交了新的任务，则线程池重新创建一个工作线程。
- 此线程池不会对线程池大小做限制，线程池大小完全依赖于操作系统（或者说 JVM）能够创建的最大线程大小。 因此，使用 `CachedThreadPool` 时，一定要注意控制任务的数量，否则，由于大量线程同时运行，很有会造成系统瘫痪。

```java
public static ExecutorService newCachedThreadPool() {
	return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
								  60L, TimeUnit.SECONDS,
								  new SynchronousQueue<Runnable>());
}

public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
	return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
								  60L, TimeUnit.SECONDS,
								  new SynchronousQueue<Runnable>(),
								  threadFactory);
}
```

`CachedThreadPool` 的`corePoolSize` 被设置为空（0），`maximumPoolSize`被设置为 `Integer.MAX.VALUE`，即它是无界的，这也就意味着如果主线程提交任务的速度高于 `maximumPool` 中线程处理任务的速度时，`CachedThreadPool` 会不断创建新的线程。极端情况下，这样会导致耗尽 cpu 和内存资源。

`CachedThreadPool` 的执行流程：

1. 首先执行 `SynchronousQueue.offer(Runnable task)` 提交任务到任务队列。如果当前 `maximumPool` 中有闲线程正在执行 `SynchronousQueue.poll(keepAliveTime,TimeUnit.NANOSECONDS)`，那么主线程执行 offer 操作与空闲线程执行的 `poll` 操作配对成功，主线程把任务交给空闲线程执行，`execute()`方法执行完成，否则执行下面的步骤 2；
2. 当初始 `maximumPool` 为空，或者 `maximumPool` 中没有空闲线程时，将没有线程执行 `SynchronousQueue.poll(keepAliveTime,TimeUnit.NANOSECONDS)`。这种情况下，步骤 1 将失败，此时 `CachedThreadPool` 会创建新线程执行任务，execute 方法执行完成；

`CachedThreadPool` 的问题：

`CachedThreadPool` 使用的是同步队列 `SynchronousQueue`, 允许创建的线程数量为 `Integer.MAX_VALUE` ，可能会创建大量线程，从而导致 OOM。

### ScheduleThreadPool

`ScheduledThreadPool` 用来在给定的延迟后运行任务或者定期执行任务。这个在实际项目中基本不会被用到，也不推荐使用。

```java
public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
    return new ScheduledThreadPoolExecutor(corePoolSize);
}

public ScheduledThreadPoolExecutor(int corePoolSize) {
    super(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS,
          new DelayedWorkQueue());
}
```

`ScheduledThreadPool` 是通过 `ScheduledThreadPoolExecutor` 创建的，使用的`DelayedWorkQueue`（延迟阻塞队列）作为线程池的任务队列。

`DelayedWorkQueue` 的内部元素并不是按照放入的时间排序，而是会按照延迟的时间长短对任务进行排序，内部采用的是“堆”的数据结构，可以保证每次出队的任务都是当前队列中执行时间最靠前的。`DelayedWorkQueue` 添加元素满了之后会自动扩容原来容量的 1/2，即永远不会阻塞，最大扩容可达 `Integer.MAX_VALUE`，所以最多只能创建核心线程数的线程。

`ScheduledThreadPoolExecutor` 继承了 `ThreadPoolExecutor`，所以创建 `ScheduledThreadExecutor` 本质也是创建一个 `ThreadPoolExecutor` 线程池，只是传入的参数不相同。

#### ScheduledThreadPoolExecutor 和 Timer 对比

- `Timer` 对系统时钟的变化敏感，`ScheduledThreadPoolExecutor`不是；
- `Timer` 只有一个执行线程，因此长时间运行的任务可以延迟其他任务。 `ScheduledThreadPoolExecutor` 可以配置任意数量的线程。 此外，如果你想（通过提供 `ThreadFactory`），你可以完全控制创建的线程；
- 在`TimerTask` 中抛出的运行时异常会杀死一个线程，从而导致 `Timer` 死机即计划任务将不再运行。`ScheduledThreadExecutor` 不仅捕获运行时异常，还允许您在需要时处理它们（通过重写 `afterExecute` 方法`ThreadPoolExecutor`）。抛出异常的任务将被取消，但其他任务将继续运行。

### WorkStealingPool

> WorkStealingPool 是 JDK8 才引入的。

其内部会构建 `ForkJoinPool`，利用 [Work-Stealing](https://en.wikipedia.org/wiki/Work_stealing) 算法，并行地处理任务，不保证处理顺序。

## 线程池最佳实践

### 计算线程数量

一般多线程执行的任务类型可以分为 CPU 密集型和 I/O 密集型，根据不同的任务类型，我们计算线程数的方法也不一样。

**CPU 密集型任务：**这种任务消耗的主要是 CPU 资源，可以将线程数设置为 N（CPU 核心数）+1，比 CPU 核心数多出来的一个线程是为了防止线程偶发的缺页中断，或者其它原因导致的任务暂停而带来的影响。一旦任务暂停，CPU 就会处于空闲状态，而在这种情况下多出来的一个线程就可以充分利用 CPU 的空闲时间。

**I/O 密集型任务：**这种任务应用起来，系统会用大部分的时间来处理 I/O 交互，而线程在处理 I/O 的时间段内不会占用 CPU 来处理，这时就可以将 CPU 交出给其它线程使用。因此在 I/O 密集型任务的应用中，我们可以多配置一些线程，具体的计算方法是 2N。

### 建议使用有界阻塞队列

不建议使用 `Executors` 的最重要的原因是：`Executors` 提供的很多方法默认使用的都是无界的 `LinkedBlockingQueue`，高负载情境下，无界队列很容易导致 OOM，而 OOM 会导致所有请求都无法处理，这是致命问题。所以**强烈建议使用有界队列**。

《阿里巴巴 Java 开发手册》中提到，禁止使用这些方法来创建线程池，而应该手动 `new ThreadPoolExecutor` 来创建线程池。制订这条规则是因为容易导致生产事故，最典型的就是 `newFixedThreadPool` 和 `newCachedThreadPool`，可能因为资源耗尽导致 OOM 问题。

【示例】`newFixedThreadPool` OOM

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

`newFixedThreadPool` 使用的工作队列是 `LinkedBlockingQueue` ，而默认构造方法的 `LinkedBlockingQueue` 是一个 `Integer.MAX_VALUE` 长度的队列，可以认为是无界的。如果任务较多并且执行较慢的话，队列可能会快速积压，撑爆内存导致 OOM。

【示例】`newCachedThreadPool` OOM

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

### 监测线程池运行状态

可以通过一些手段来检测线程池的运行状态比如 SpringBoot 中的 Actuator 组件。

除此之外，我们还可以利用 `ThreadPoolExecutor` 的相关 API 做一个简陋的监控。从下图可以看出， `ThreadPoolExecutor`提供了获取线程池当前的线程数和活跃线程数、已经执行完成的任务数、正在排队中的任务数等等。

下面是一个简单的 Demo。`printThreadPoolStatus()`会每隔一秒打印出线程池的线程数、活跃线程数、完成的任务数、以及队列中的任务数。

```java
public static void printThreadPoolStatus(ThreadPoolExecutor threadPool) {
    ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, createThreadFactory("print-images/thread-pool-status", false));
    scheduledExecutorService.scheduleAtFixedRate(() -> {
        log.info("=========================");
        log.info("ThreadPool Size: [{}]", threadPool.getPoolSize());
        log.info("Active Threads: {}", threadPool.getActiveCount());
        log.info("Number of Tasks : {}", threadPool.getCompletedTaskCount());
        log.info("Number of Tasks in Queue: {}", threadPool.getQueue().size());
        log.info("=========================");
    }, 0, 1, TimeUnit.SECONDS);
}
```

### 线程池和 ThreadLocal

线程池和 `ThreadLocal`共用，可能会导致线程从`ThreadLocal`获取到的是旧值/脏数据。这是因为线程池会复用线程对象，与线程对象绑定的类的静态属性 `ThreadLocal` 变量也会被重用，这就导致一个线程可能获取到其他线程的`ThreadLocal` 值。

不要以为代码中没有显示使用线程池就不存在线程池了，像常用的 Web 服务器 Tomcat 处理任务为了提高并发量，就使用到了线程池，并且使用的是基于原生 Java 线程池改进完善得到的自定义线程池。

当然了，你可以将 Tomcat 设置为单线程处理任务。不过，这并不合适，会严重影响其处理任务的速度。

```properties
server.tomcat.max-threads=1
```

解决上述问题比较建议的办法是使用阿里巴巴开源的 `TransmittableThreadLocal`(`TTL`)。`TransmittableThreadLocal`类继承并加强了 JDK 内置的`InheritableThreadLocal`类，在使用线程池等会池化复用线程的执行组件情况下，提供`ThreadLocal`值的传递功能，解决异步执行时上下文传递的问题。

`TransmittableThreadLocal` 项目地址：[https://github.com/alibaba/transmittable-thread-localopen in new window](https://github.com/alibaba/transmittable-thread-local) 。

### 重要任务应该自定义拒绝策略

使用有界队列，当任务过多时，线程池会触发执行拒绝策略，线程池默认的拒绝策略会 throw `RejectedExecutionException` 这是个运行时异常，对于运行时异常编译器并不强制 `catch` 它，所以开发人员很容易忽略。因此**默认拒绝策略要慎重使用**。如果线程池处理的任务非常重要，建议自定义自己的拒绝策略；并且在实际工作中，自定义的拒绝策略往往和降级策略配合使用。

### 动态线程池

美团技术团队在 [《Java 线程池实现原理及其在美团业务中的实践》](https://tech.meituan.com/2020/04/02/java-pooling-pratice-in-meituan.html) 这篇文章中介绍到对线程池参数实现可自定义配置的思路和方法。

美团技术团队的思路是主要对线程池的核心参数实现自定义可配置。这三个核心参数是：

- **`corePoolSize`** - 核心线程数线程数定义了最小可以同时运行的线程数量。
- **`maximumPoolSize`** - 当队列中存放的任务达到队列容量的时候，当前可以同时运行的线程数量变为最大线程数。
- **`workQueue`** - 当新任务来的时候会先判断当前运行的线程数量是否达到核心线程数，如果达到的话，新任务就会被存放在队列中。

JDK 原生线程池 `ThreadPoolExecutor` 提供了如下几个 public 的 setter 方法，如下图所示：

![图 19 JDK 线程池参数设置接口](https://raw.githubusercontent.com/dunwu/images/master/archive/2026/02/55b3b306167a4c0a84b27bd86e409da7.png)

JDK 允许线程池使用方通过 `ThreadPoolExecutor` 的实例来动态设置线程池的核心策略。

重点是基于这几个 `public` 方法，我们只需要维护 `ThreadPoolExecutor` 的实例，并且在需要修改的时候拿到实例修改其参数即可。基于以上的思路，美团实现了线程池参数的动态化、线程池参数在管理平台可配置可修改，其效果图如下图所示：

![图 21 可动态修改线程池参数](https://raw.githubusercontent.com/dunwu/images/master/archive/2026/02/9be2af0749aa4d9f8daa515fa612d738.png)

如果我们的项目也想要实现这种效果的话，可以借助现成的开源项目：

- **[Hippo4jopen](https://github.com/opengoofy/hippo4j)** - 异步线程池框架，支持线程池动态变更&监控&报警，无需修改代码轻松引入。支持多种使用模式，轻松引入，致力于提高系统运行保障能力。
- **[Dynamic TPopen](https://github.com/dromara/dynamic-tp)** - 轻量级动态线程池，内置监控告警功能，集成三方中间件线程池管理，基于主流配置中心（已支持 Nacos、Apollo，Zookeeper、Consul、Etcd，可通过 SPI 自定义实现）。

## 典型应用场景

### 场景一：IO 密集型服务线程池

```java
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    50, 100, 60L, TimeUnit.SECONDS,
    new LinkedBlockingQueue<>(1000),
    new ThreadPoolExecutor.CallerRunsPolicy());
```

### 场景二：定时任务调度

```java
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
scheduler.scheduleAtFixedRate(() -> cleanup(), 0, 1, TimeUnit.HOURS);
```

### 场景三：异步任务编排与线程池隔离

不同类型任务使用不同线程池，避免互相影响。

## 最佳实践

1. **不要用 Executors 工厂方法**：newFixedThreadPool 和 newCachedThreadPool 的队列/线程数无限制，有 OOM 风险。
2. **明确设置拒绝策略**：CallerRunsPolicy 或自定义策略。
3. **合理配置核心参数**：corePoolSize、maxPoolSize、keepAliveTime 根据业务特点设置。
4. **监控线程池状态**：getActiveCount、getCompletedTaskCount 等。
5. **优雅关闭**：shutdown + awaitTermination。

## 常见问题

### Q1：ThreadPoolExecutor 的 7 个参数分别是什么？

corePoolSize、maxPoolSize、keepAliveTime、unit、workQueue、threadFactory、rejectedHandler。

### Q2：任务提交后线程池的执行流程是什么？

1. 核心线程未满 → 创建核心线程执行
2. 核心线程满 → 任务入队列
3. 队列满 → 创建非核心线程
4. 非核心线程也满 → 触发拒绝策略

### Q3：为什么不建议使用 Executors.newCachedThreadPool？

maxPoolSize 为 Integer.MAX_VALUE，高并发时会创建大量线程导致 OOM。

## 参考资料

- [《Java 并发编程实战》](https://book.douban.com/subject/10484692/)
- [《Java 并发编程的艺术》](https://book.douban.com/subject/26591326/)
- [极客时间教程 - Java 并发编程实战](https://time.geekbang.org/column/intro/100023901)
- [深入理解 Java 线程池：ThreadPoolExecutor](https://www.jianshu.com/p/d2729853c4da)
- [java 并发编程--Executor 框架](https://www.cnblogs.com/MOBIN/p/5436482.html)
- [Java 线程池实现原理及其在美团业务中的实践](https://tech.meituan.com/2020/04/02/java-pooling-pratice-in-meituan.html)
