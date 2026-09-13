# JavaCore :: Concurrent — Java 并发编程示例

> 本模块展示 Java 并发编程的核心特性：线程基础、同步机制、锁、原子类、ThreadLocal、并发容器、线程池、JMM 与并发工具类。示例均可直接运行 `main` 方法观察行为。
>
> 说明：模块中相当一部分示例是**故意演示错误并发用法的反例**（类名以 `Wrong` / `Error` / `NotThreadSafe` / `死锁` / `活锁` / `饥饿` 标识，或文件名直接标注问题），用于对比正确写法，请勿把它们「修正」成正确写法，否则示例就失去了对比价值。

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
- `sync/ThreadDeadLockDemo`、`sync/SynchronizedDeadlockDemo`（反例） 与 `sync/SynchronizedDeadlockFixDemo` — 死锁的产生与修正。
- `sync/SynchronizedWrongLockDemo`、`SynchronizedScopePitfallDemo`、`SynchronizedGranularityPitfallDemo`（反例） — 锁对象错误、同步范围不当、锁粒度过粗/过细等问题。

## 锁（lock）

展示 `ReentrantLock`、`ReadWriteLock`、`Condition` 及死锁/活锁/饥饿。

- `lock/ReentrantLockDemo`~`ReentrantLockDemo4` — ReentrantLock 的加解锁、可中断锁、超时锁、公平锁。
- `lock/ReentrantLockReentrantDemo` — 锁的可重入特性。
- `lock/LockConditionDemo` — 使用 `Condition` 实现精准唤醒。
- `lock/ReentrantReadWriteLockCacheDemo`、`ReentrantReadWriteLockCacheDemo2` — 读写锁实现线程安全缓存（读读并行、读写互斥）。
- `lock/ReentrantLockDeadlockDemo`、`lock/LivelockDemo`、`lock/ReentrantLockLivelockDemo`（反例） — 死锁与活锁的产生场景。
- `lock/StarvationDemo`（反例） 与 `lock/StarvationFixDemo` — 线程饥饿问题及其修正。

## 原子类（atomic）

展示 `java.util.concurrent.atomic` 包基于 CAS 的无锁原子操作。

- `atomic/AtomicIntegerDemo`、`atomic/AtomicIntegerArrayDemo` — 原子整型与原子数组。
- `atomic/AtomicReferenceDemo`、`AtomicReferenceDemo2`、`AtomicReferenceDemo3` — 原子引用更新。
- `atomic/AtomicStampedReferenceDemo`、`atomic/AtomicMarkableReferenceDemo` — 带版本号/标记的原子引用，解决 ABA 问题。
- `atomic/AtomicReferenceFieldUpdaterDemo` — 原子更新对象的某个字段。
- `atomic/RateLimiter` — 基于原子类实现的简单限流器。

## 线程本地变量（threadlocal）

展示 `ThreadLocal` 的线程隔离语义、默认值设置与典型误用。

- `threadlocal/ThreadLocalDemo`（正确） — 重写 `initialValue()` 提供默认值，10 个线程各自累加自己的副本，输出稳定为 10 行 `count = 10`。
- `threadlocal/ThreadLocalErrorDemo`（反例） — 本该做线程隔离却用了共享静态变量，10 个线程互相干扰，打印值远大于 10 且各不相同。
- `threadlocal/ThreadLocalDemo02` — 不带初始值的 `ThreadLocal`：子线程的 `set` 对主线程不可见，主线程未 `set` 就 `get` 得到 `null`（若接收方是基本类型，拆箱会抛 NPE）。
- `threadlocal/ThreadLocalDemo03` — 用 `ThreadLocal.withInitial(...)` 提供默认值规避上述 NPE，并验证子线程的写入不会影响主线程。

> Web 场景下 ThreadLocal 不清理导致的「用户串号」问题，另见 `codes/javacore-in-web` 模块的 `ThreadLocalErrorDemo` 与其 `ThreadLocalErrorDemoTest`。

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
- `tool/division/CompletableFutureCreateDemo`、`CompletableFutureCombineDemo`、`CompletableFutureMultiTaskDemo`、`CompletableFutureResultHandleDemo`、`CompletableFutureResultTransformDemo`、`CompletableFutureAsyncDemo`、`CompletableFutureCompleteTimeoutDemo`、`CompletableFutureCompleteFastDemo` — CompletableFuture 的创建、编排、结果处理与完成控制。
- `tool/division/ForkJoinPoolArraySumDemo`、`ForkJoinPoolFibonacciDemo`、`ForkJoinPoolWordCountDemo` — Fork/Join 分治框架。
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

---

## 示例类的入口约定（全仓通用）

这一节写在并发模块的 README 里，但约定适用于 `codes/` 下的**全部**示例模块，不只是本模块。

### 核心不变式：main() 不放逻辑

`main()` 只做一件事——按顺序调用本类中**具名的** `static` 方法，自身不承载示例逻辑：

```java
public static void main(String[] args) {
    localVarInference();
    varInLoops();
    varInTryWithResources();
}
```

三个理由：

1. **可测**。测试要在测试 JVM 里捕获一个示例的输出，就必须能调用它。`main()` 不能被安全地重复调用（`System.exit`、静态状态、`args` 数组），具名方法可以。
2. **可读**。方法名本身就是小标题，`varInTryWithResources()` 比「`main()` 里第 30 行那段」更能说明演示的是什么。
3. **可引用**。`// Output:` 注释、测试断言、文档都能按方法名指向某一个侧面，而不是指向整个文件。

### 两种派发形态

调一个还是调多个，取决于这个类演示的是一个侧面还是多个侧面。两种形态仓库里都在用。

**单一 `demo()`**——整个类只演示一件事，或者各步骤必须按固定顺序跑完才有意义：

```java
public static void demo() throws InterruptedException {
    // 示例逻辑
}

public static void main(String[] args) throws InterruptedException {
    demo();
}
```

**多个具名子方法**——类里并列演示若干互不依赖的侧面，每个侧面配一段 `示例 N：...` 的 Javadoc。`javacore-newjdk` 全模块采用此形态：

```java
/**
 * 示例 1：局部变量声明——编译器自动推断 String、集合等类型
 */
public static void localVarInference() {
    // ...
}
```

判断标准：**测试是否需要单独调用其中一部分**。需要，就拆成多个具名方法；不需要，就用单一 `demo()`。

### 期望输出注释

被测试精确断言过输出的类，在**类结束 `}` 之后、列 0** 追加 `// Output:` 块，逐行原样转录真实输出：

```java
}
// Output:
// Hello, Java 10
// list 类型: ArrayList
```

三条硬性要求：

- 位置固定在类外列 0：不缩进，与 `}` 之间不留空行。全仓 186 个带此标记的文件中，185 个是这一形态。
- 内容必须是**实跑或测试断言验证过**的真实输出，不能凭读代码手写。
- 唯一允许的例外是**语句级标注**：缩进跟随代码、紧跟单条 `println`，只标注那一条语句的输出（`javacore-basics` 的 `NumericCalculationDemo` 有 4 处）。同一个文件里不要混用类级块和语句级标注。

### 当前符合度（实测）

下表是一次性脚本扫描 `codes/` 下 899 个 `.java` 文件的结果（排除 `target/`），只作现状快照——脚本未纳入仓库，数字会随示例类增减而漂移，无需随之更新：

| 模块 | 文件 | 有 main | 单次调用 | 多次调用 | 逻辑内联 | 带 `// Output:` |
| --- | ---: | ---: | ---: | ---: | ---: | ---: |
| javacore-basics | 230 | 169 | 161 | 0 | 7 | 114 |
| javacore-concurrent | 140 | 115 | 29 | 3 | 83 | 6 |
| javacore-container | 69 | 57 | 53 | 0 | 4 | 13 |
| javacore-effective | 130 | 72 | 0 | 0 | 72 | 0 |
| javacore-io | 63 | 49 | 35 | 0 | 14 | 3 |
| javacore-jvm | 45 | 37 | 1 | 0 | 36 | 1 |
| javacore-newjdk | 110 | 55 | 2 | 51 | 2 | 49 |
| javacore-oop | 39 | 28 | 27 | 0 | 1 | 0 |
| javacore-utils | 52 | 44 | 44 | 0 | 0 | 0 |
| javacore-in-web | 6 | 1 | 0 | 0 | 1 | 0 |
| bytecode（5 个子模块） | 15 | 6 | 0 | 0 | 6 | 0 |
| **合计** | **899** | **633** | **352** | **54** | **226** | **186** |

- **单次调用** = `main()` 体内只有一条具名方法调用，其中 347 条调的是 `demo()`
- **多次调用** = 两条以上具名方法调用，即 newjdk 形态
- **逻辑内联** = `main()` 自己承载了逻辑
- 633 个「有 main」中另有 1 个 `main()` 体为空，未计入后三列

读这张表要注意四点：

1. **存量不追溯**。226 个「逻辑内联」的类保持原样，不为统一风格而改动。本约定只约束**新增**的示例类，以及被大幅重写的类。
2. **`javacore-effective` 的 72 个全部内联是刻意的**。那是《Effective Java》的书中示例，逐条对应 item，照抄原书结构比套用本仓约定更有价值。
3. **`javacore-jvm` 的 36 个内联多属合理**。OOM / GC 示例的价值在于配合特定 VM 参数手动运行观察，逻辑本身就短，硬拆方法反而增加噪音；该模块的约定重点是类级 Javadoc 里的 `VM Args:` 模板。
4. **本模块（concurrent）只有 29/115 采用单一 `demo()`**。大量并发示例一旦运行就会留下永不结束的线程或耗尽内存，无法进测试 JVM，也就没有必要为可测性改造入口；下面「刻意未覆盖的示例」列出了这批类。

---

## 单元测试

测试源码路径：`src/test/java/io/github/dunwu/javacore/concurrent/`

```bash
mvn test -pl codes/javacore-concurrent
```

现有 **26 个测试**，覆盖 `atomic`、`threadlocal`、`tool`、`tool.sync`、`error` 五个包共 26 个示例类。

并发示例的输出天生混杂了「确定不变的部分」与「取决于线程调度的部分」，因此测试遵循下面几条约定：

- **只断言由并发语义保证的性质**，不断言具体线程名或行顺序。例如 `CountDownLatchDemo` 断言「`await()` 之后的两行必然排在最后」，`CyclicBarrierDemo` 断言「barrierAction 必然排在最后一行」，`AtomicReferenceDemo2` 断言「自旋锁下 10 张票恰好各卖一次」。
- **反例只做弱断言**。`NotThreadSafeCounter` / `WrongResult` 丢失多少次更新完全取决于调度，断言「必然小于预期值」在理论上也可能失败，所以只断言结果落在必然成立的区间内，具体量级写在注释里。
- **正反例的断言强度差异本身就是结论**：`ThreadSafeCounter` 可以精确断言 `count = 200000`，而 `NotThreadSafeCounter` 不能 —— 这直观体现了同步措施是否生效。
- 输出通过 `DemoOutputCapture.capture(...)` 捕获：临时把 `System.out` 换成内存流，执行完再还原。

### 本模块对可测性的额外要求

被测试覆盖的示例类都采用上文「单一 `demo()`」形态。在并发场景下，`demo()` 除了承载原有逻辑，还必须满足两个条件，否则测试会不稳定：

1. **返回前等待自己创建的所有线程结束**（`join()` 或 `shutdown()` + `awaitTermination()`）。只调 `shutdown()` 是不够的 —— 它仅仅表示不再接受新任务，不会等已提交的任务跑完，方法一返回，`System.out` 就被还原了，子线程的输出会漏到捕获范围之外。
2. **重置内部静态状态**，使方法可以重复调用。例如卖票示例需要在开头把 `ticket` 重置为 10，否则第二次调用时票已卖完，不会有任何输出。

### 刻意未覆盖的示例

以下示例执行后会留下永不结束的线程，或会耗尽内存，一旦在测试 JVM 中运行将挂起或拖垮整个测试进程，因此只能单独运行它们的 `main` 方法观察：

- 死锁：`sync/ThreadDeadLockDemo`、`sync/SynchronizedDeadlockDemo`、`lock/ReentrantLockDeadlockDemo`
- 活锁：`lock/LivelockDemo`、`lock/ReentrantLockLivelockDemo`
- 饥饿：`lock/StarvationDemo`、`lock/StarvationFixDemo`
- 内存溢出：`threadpool/ThreadPoolOOM`
- 无限循环 / 长时运行：`example/ProducerConsumerDemo01`~`03`、`atomic/RateLimiter`、`container/VectorDemo` 系列、`thread/ThreadDaemonDemo`、`thread/ThreadStopDemo` 系列、`leetcode/PrintInOrder`（输出达 3000 行）
