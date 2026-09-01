# JavaCore :: Concurrent — Java 并发编程示例

> 本模块展示 Java 并发编程的核心特性：线程基础、同步机制、锁、原子类、并发容器、线程池、JMM 与并发工具类。示例均可直接运行 `main` 方法观察行为。
>
> 说明：模块中相当一部分示例是**故意演示错误并发用法的反例**（类名以 `Wrong` / `Error` / `NotThreadSafe` / `死锁` / `活锁` / `饥饿` 标识，或文件名直接标注问题），用于对比正确写法，请勿修改其行为。

示例源码路径：`src/main/java/io/github/dunwu/javacore/concurrent/<特性包>/`

---

## 线程基础（thread）

展示线程的创建、生命周期状态与基本控制。

- `thread/ThreadDemo`、`thread/RunnableDemo`、`thread/CallableDemo` — 继承 Thread、实现 Runnable、实现 Callable 三种创建线程的方式。
- `thread/CurrentThreadDemo`、`thread/ThreadNameDemo` — 获取当前线程、设置/获取线程名。
- `thread/ThreadSleepDemo`、`thread/ThreadJoinDemo`、`thread/ThreadYieldDemo` — sleep 休眠、join 等待、yield 让步。
- `thread/ThreadPriorityDemo` — 线程优先级。
- `thread/ThreadDaemonDemo` — 守护线程（随用户线程结束而结束）。
- `thread/ThreadAliveDemo`、`thread/ThreadInterruptDemo` — 线程存活判断与中断协作机制。
- `thread/ThreadWaitNotifyDemo`、`thread/ThreadWaitNotifyDemo02` — `wait`/`notify` 线程间通信。
- `thread/ConditionDemo`、`thread/Piped` — Condition 条件队列、管道流线程通信。
- `thread/ThreadStopDemo`、`ThreadStopDemo2`、`ThreadStopDemo3` — 停止线程的方式（含已废弃的 `stop` 反例与推荐的中断/标志位方式）。
- `thread/ThreadOperatorDemo` — 线程调度综合演示。
- `thread/ThreadErrorDemo01`、`ThreadErrorDemo02`（反例） — 线程使用中的典型错误。

## 同步机制（sync）

展示 `synchronized`、`volatile` 关键字的用法与常见误用。

- `sync/SynchronizedDemo2`、`SynchronizedDemo3`、`SynchronizedDemo05`、`SynchronizedDemo06` — synchronized 修饰实例方法、静态方法、代码块的不同锁对象与效果。
- `sync/VolatileDemo` — volatile 保证可见性与禁止指令重排（但不保证原子性）。
- `sync/ThreadSafeCounter`、`ThreadSafeCounter2` — 线程安全计数器的正确实现。
- `sync/NotThreadSafeCounter`、`NotThreadSafeCounter2`（反例） — 非线程安全计数器在并发下丢失更新。
- `sync/ThreadDeadLockDemo`、`sync/synchronized死锁示例`（反例） 与 `sync/synchronized死锁示例修正` — 死锁的产生与修正。
- `sync/synchronized保护对象不对`、`synchronized使用范围不当`、`synchronized锁粒度不当`（反例） — 锁对象错误、同步范围不当、锁粒度过粗/过细等问题。

## 锁（lock）

展示 `ReentrantLock`、`ReadWriteLock`、`Condition` 及死锁/活锁/饥饿。

- `lock/ReentrantLockDemo`~`ReentrantLockDemo4` — ReentrantLock 的加解锁、可中断锁、超时锁、公平锁。
- `lock/ReentrantLock可重入示例` — 锁的可重入特性。
- `lock/LockConditionDemo` — 使用 `Condition` 实现精准唤醒。
- `lock/ReentrantReadWriteLock实现缓存`、`ReentrantReadWriteLock实现缓存2` — 读写锁实现线程安全缓存（读读并行、读写互斥）。
- `lock/ReentrantLock死锁`、`lock/LivelockDemo`、`lock/ReentrantLock活锁示例`（反例） — 死锁与活锁的产生场景。
- `lock/StarvationDemo`（反例） 与 `lock/StarvationFixDemo` — 线程饥饿问题及其修正。

## 原子类（atomic）

展示 `java.util.concurrent.atomic` 包基于 CAS 的无锁原子操作。

- `atomic/AtomicIntegerDemo`、`atomic/AtomicIntegerArrayDemo` — 原子整型与原子数组。
- `atomic/AtomicReferenceDemo`、`AtomicReferenceDemo2`、`AtomicReferenceDemo3` — 原子引用更新。
- `atomic/AtomicStampedReferenceDemo`、`atomic/AtomicMarkableReferenceDemo` — 带版本号/标记的原子引用，解决 ABA 问题。
- `atomic/AtomicReferenceFieldUpdaterDemo` — 原子更新对象的某个字段。
- `atomic/RateLimiter` — 基于原子类实现的简单限流器。

## 并发容器（container）

展示线程安全容器与其误用。

- `container/ConcurrentHashMapDemo`、`ConcurrentHashMapDemo2` — ConcurrentHashMap 的并发读写与复合操作。
- `container/CopyOnWriteArrayListDemo` — 写时复制列表，适用于读多写少。
- `container/ArrayBlockingQueueDemo` — 有界阻塞队列。
- `container/VectorDemo`、`VectorDemo2`、`VectorDemo3` — Vector 的线程安全边界（复合操作仍需外部同步）。
- `container/WrongConcurrentHashMapDemo`、`WrongConcurrentHashMapDemo2`、`WrongConcurrentHashMapDemo3`、`WrongCopyOnWriteList`（反例） — 误以为并发容器的单个操作线程安全就整体安全（复合操作非原子）。

## 线程池与执行器（executor / threadpool）

展示 `ExecutorService` 各类线程池与自定义线程池参数。

- `executor/FixedThreadPoolDemo`、`executor/CachedThreadPoolDemo`、`executor/SingleThreadExecutorDemo`、`executor/ScheduledThreadPoolDemo` — 四种常见线程池。
- `executor/ThreadPoolExecutorDemo` — 自定义 `ThreadPoolExecutor` 核心参数。
- `executor/ExecutorCompletionServiceDemo` — 按完成顺序获取任务结果。
- `executor/ExecutorServiceShutdownDemo` — 线程池的优雅关闭（shutdown / shutdownNow / awaitTermination）。
- `threadpool/ThreadPoolMixuseController` — 线程池混用控制示例。
- `threadpool/ThreadPoolOOM`（反例） — 无界队列导致线程池 OOM。

## 并发工具类（tool）

展示 JUC 提供的同步器与异步编排工具。

- `tool/sync/CountDownLatchDemo`、`CountDownLatchDemo02` — 倒计时门闩（等待多个任务完成）。
- `tool/sync/CyclicBarrierDemo`、`CyclicBarrierDemo02` — 循环栅栏（多线程互相等待到齐）。
- `tool/sync/SemaphoreDemo`、`tool/SemaphoreRateLimit` — 信号量控制并发数、实现限流。
- `tool/sync/ExchangerDemo` — 两个线程间交换数据。
- `tool/FutureTaskDemo`、`FutureTaskDemo2`、`FutureTaskDemo3` — FutureTask 获取异步结果。
- `tool/division/CompletableFuture_创建`、`CompletableFuture_组合`、`CompletableFuture_多任务组合`、`CompletableFuture_结果处理`、`CompletableFuture_结果转换`、`CompletableFuture异步处理`、`CompletableFuture_完成控制_超时结束`、`CompletableFuture_完成控制_快速结束` — CompletableFuture 的创建、编排、结果处理与完成控制。
- `tool/division/ForkJoinPool_数组求和`、`ForkJoinPool_计算斐波那契数列`、`ForkJoinPool_统计词频` — Fork/Join 分治框架。
- `tool/division/FutureDemo`、`FutureTaskDemo2`、`FutureTaskDemo3` — Future 相关补充示例。

## Java 内存模型（jmm）

展示 JMM 相关的可见性、重排序与安全的惰性初始化。

- `jmm/UnsafeLazyInitialization`（反例） 与 `jmm/SafeLazyInitialization`、`jmm/EagerInitialization`、`jmm/DoubleCheckedLocking` — 惰性初始化的线程安全问题与各种正确解法（含双重检查锁）。
- `jmm/PossibleReordering`（反例） — 指令重排序导致的结果不确定性。
- `jmm/SafeStates` — 安全发布对象状态。

## 线程安全与错误示例（error / annotation）

展示线程安全的基本判定与 JCIP 注解标注。

- `error/ThreadSafeCounter`（正确） 与 `error/NotThreadSafeCounter`（反例） — 竞态条件对比。
- `error/WrongResult`、`error/WrongInit`（反例） — 复合操作非原子、对象未安全发布导致的问题。
- `annotation/ThreadSafe`、`NotThreadSafe`、`Immutable`、`GuardedBy` — JCIP（Java Concurrency in Practice）线程安全标注注解。
- `annotation/Right`、`annotation/Error` — 用于在源码中标注「正确/错误」示范的注解。

## 生产者-消费者与实战（example / leetcode）

- `example/ProducerConsumerDemo01`~`ProducerConsumerDemo03` — 用 wait/notify、BlockingQueue、Lock+Condition 三种方式实现生产者-消费者模型。
- `leetcode/PrintInOrder` — 经典并发题：保证三个线程按序打印。
