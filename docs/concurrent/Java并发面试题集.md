# Java 并发夺命连环问

> :notebook: 本文已归档到：「[javacore](https://github.com/dunwu/javacore)」

<!-- TOC depthFrom:2 depthTo:3 -->

- [1. 并发简介](#1-并发简介)
    - [1.1. 什么是进程？什么是线程？进程和线程的区别？](#11-什么是进程什么是线程进程和线程的区别)
    - [1.2. 并发（多线程）编程的好处是什么？](#12-并发多线程编程的好处是什么)
    - [1.3. 并发一定比串行更快吗？](#13-并发一定比串行更快吗)
    - [1.4. 如何让正在运行的线程暂停一段时间？](#14-如何让正在运行的线程暂停一段时间)
    - [1.5. 什么是线程调度器(Thread Scheduler)和时间分片(Time Slicing)？](#15-什么是线程调度器thread-scheduler和时间分片time-slicing)
    - [1.6. 在多线程中，什么是上下文切换(context-switching)？](#16-在多线程中什么是上下文切换context-switching)
    - [1.7. 如何确保线程安全？](#17-如何确保线程安全)
    - [1.8. 什么是死锁(Deadlock)？如何分析和避免死锁？](#18-什么是死锁deadlock如何分析和避免死锁)
- [2. 线程基础](#2-线程基础)
    - [2.1. Java 线程生命周期中有哪些状态？各状态之间如何切换？](#21-java-线程生命周期中有哪些状态各状态之间如何切换)
    - [2.2. 创建线程有哪些方式？这些方法各自利弊是什么？](#22-创建线程有哪些方式这些方法各自利弊是什么)
    - [2.3. 什么是 `Callable` 和 `Future`？什么是 `FutureTask`？](#23-什么是-callable-和-future什么是-futuretask)
    - [2.4. `start()` 和 `run()` 有什么区别？可以直接调用 `Thread` 类的 `run()` 方法么？](#24-start-和-run-有什么区别可以直接调用-thread-类的-run-方法么)
    - [2.5. `sleep()`、`yield()`、`join()` 方法有什么区别？为什么 `sleep()` 和 `yield()` 方法是静态（static）的？](#25-sleepyieldjoin-方法有什么区别为什么-sleep-和-yield-方法是静态static的)
    - [2.6. Java 的线程优先级如何控制？高优先级的 Java 线程一定先执行吗？](#26-java-的线程优先级如何控制高优先级的-java-线程一定先执行吗)
    - [2.7. 什么是守护线程？为什么要用守护线程？如何创建守护线程？](#27-什么是守护线程为什么要用守护线程如何创建守护线程)
    - [2.8. 线程间是如何通信的？](#28-线程间是如何通信的)
    - [2.9. 为什么线程通信的方法 `wait()`, `notify()` 和 `notifyAll()` 被定义在 Object 类里？](#29-为什么线程通信的方法-wait-notify-和-notifyall-被定义在-object-类里)
    - [2.10. 为什么 `wait()`, `notify()` 和 `notifyAll()` 必须在同步方法或者同步块中被调用？](#210-为什么-wait-notify-和-notifyall-必须在同步方法或者同步块中被调用)
- [3. 并发机制的底层实现](#3-并发机制的底层实现)
    - [3.1. `volatile` 有什么作用？它的实现原理是什么？](#31-volatile-有什么作用它的实现原理是什么)
    - [3.2. `synchronized` 有什么作用？它的实现原理是什么？同步方法和同步块，哪个更好？](#32-synchronized-有什么作用它的实现原理是什么同步方法和同步块哪个更好)
    - [3.3. `volatile` 和 `synchronized` 的区别？](#33-volatile-和-synchronized-的区别)
    - [3.4. `ThreadLocal` 有什么作用？`ThreadLocal` 的实现原理是什么？](#34-threadlocal-有什么作用threadlocal-的实现原理是什么)
- [4. 内存模型](#4-内存模型)
    - [4.1. 什么是 Java 内存模型](#41-什么是-java-内存模型)
- [5. 同步容器和并发容器](#5-同步容器和并发容器)
    - [5.1. 什么是同步容器？有哪些常见同步容器？它们是如何实现线程安全的？同步容器真的线程安全吗？](#51-什么是同步容器有哪些常见同步容器它们是如何实现线程安全的同步容器真的线程安全吗)
    - [5.2. 什么是并发容器的实现？](#52-什么是并发容器的实现)
- [6. 锁](#6-锁)
    - [6.1. 如何避免死锁？](#61-如何避免死锁)
    - [6.2. Lock 接口(Lock interface)是什么？对比同步它有什么优势？](#62-lock-接口lock-interface是什么对比同步它有什么优势)
    - [6.3. 什么是阻塞队列？如何使用阻塞队列来实现生产者-消费者模型？](#63-什么是阻塞队列如何使用阻塞队列来实现生产者-消费者模型)
- [7. 原子变量类](#7-原子变量类)
    - [7.1. 什么是原子操作？有哪些原子类？原子类的实现原理是什么？](#71-什么是原子操作有哪些原子类原子类的实现原理是什么)
- [8. 并发工具类](#8-并发工具类)
    - [8.1. CyclicBarrier 和 CountDownLatch 有什么不同？](#81-cyclicbarrier-和-countdownlatch-有什么不同)
- [9. 线程池](#9-线程池)
    - [9.1. 什么是线程池？如何创建一个 Java 线程池？](#91-什么是线程池如何创建一个-java-线程池)
    - [9.2. 什么是 Executors 框架？](#92-什么是-executors-框架)
    - [9.3. Executors 类是什么？](#93-executors-类是什么)
    - [9.4. ThreadPoolExecutor 有哪些参数，各自有什么用？](#94-threadpoolexecutor-有哪些参数各自有什么用)
    - [9.5. 线程数多少才合理](#95-线程数多少才合理)
- [10. 资料](#10-资料)

<!-- /TOC -->

## 1. 并发简介

### 1.1. 什么是进程？什么是线程？进程和线程的区别？

- 什么是进程？
  - 简言之，进程可视为一个正在运行的程序。
  - 进程是具有一定独立功能的程序关于某个数据集合上的一次运行活动。进程是操作系统进行资源分配的基本单位。
- 什么是线程？
  - 线程是操作系统进行调度的基本单位。
- 进程 vs. 线程
  - 一个程序至少有一个进程，一个进程至少有一个线程。
  - 线程比进程划分更细，所以执行开销更小，并发性更高。
  - 进程是一个实体，拥有独立的资源；而同一个进程中的多个线程共享进程的资源。

> :point_right: 参考阅读：[进程和线程关系及区别](https://blog.csdn.net/yaosiming2011/article/details/44280797)

### 1.2. 并发（多线程）编程的好处是什么？

- 更有效率的利用多处理器核心
- 更快的响应时间
- 更好的编程模型

### 1.3. 并发一定比串行更快吗？

答：否。

要点：**创建线程和线程上下文切换有一定开销**。

说明：即使是单核处理器也支持多线程。CPU 通过给每个线程分配时间切片的算法来循环执行任务，当前任务执行一个时间片后会切换到下一个任务。但是，在切换前会保持上一个任务的状态，以便下次切换回这个任务时，可以再加载这个任务的状态。所以**任务从保存到再加载的过程就是一次上下文切换**。

引申

- 如何减少上下文切换？
  - 尽量少用锁
  - CAS 算法
  - 线程数要合理
  - 协程：在单线程中实现多任务调度，并在单线程中维持多个任务的切换

### 1.4. 如何让正在运行的线程暂停一段时间？

我们可以使用 `Thread` 类的 Sleep() 方法让线程暂停一段时间。

需要注意的是，这并不会让线程终止，一旦从休眠中唤醒线程，线程的状态将会被改变为 Runnable，并且根据线程调度，它将得到执行。

### 1.5. 什么是线程调度器(Thread Scheduler)和时间分片(Time Slicing)？

线程调度器是一个操作系统服务，它负责为 `Runnable` 状态的线程分配 CPU 时间。一旦我们创建一个线程并启动它，它的执行便依赖于线程调度器的实现。

时间分片是指将可用的 CPU 时间分配给可用的 `Runnable` 线程的过程。

分配 CPU 时间可以基于线程优先级或者线程等待的时间。线程调度并不受到 Java 虚拟机控制，所以由应用程序来控制它是更好的选择（也就是说不要让你的程序依赖于线程的优先级）。

### 1.6. 在多线程中，什么是上下文切换(context-switching)？

上下文切换是存储和恢复 CPU 状态的过程，它使得线程执行能够从中断点恢复执行。上下文切换是多任务操作系统和多线程环境的基本特征。

### 1.7. 如何确保线程安全？

- 原子类(atomic concurrent classes)
- 锁
- `volatile` 关键字
- 不变类和线程安全类

### 1.8. 什么是死锁(Deadlock)？如何分析和避免死锁？

死锁是指两个以上的线程永远相互阻塞的情况，这种情况产生至少需要两个以上的线程和两个以上的资源。

分析死锁，我们需要查看 Java 应用程序的线程转储。我们需要找出那些状态为 BLOCKED 的线程和他们等待的资源。每个资源都有一个唯一的 id，用这个 id 我们可以找出哪些线程已经拥有了它的对象锁。

避免嵌套锁，只在需要的地方使用锁和避免无限期等待是避免死锁的通常办法。

## 2. 线程基础

### 2.1. Java 线程生命周期中有哪些状态？各状态之间如何切换？

<p align="center">
  <img src="https://gitee.com/turnon/images/raw/master/images/java/concurrent/thread-state.png">
</p>

`java.lang.Thread.State` 中定义了 **6** 种不同的线程状态，在给定的一个时刻，线程只能处于其中的一个状态。

以下是各状态的说明，以及状态间的联系：

- **开始（New）** - 还没有调用 `start()` 方法的线程处于此状态。
- **可运行（Runnable）** - 已经调用了 `start()` 方法的线程状态。此状态意味着，线程已经准备好了，一旦被线程调度器分配了 CPU 时间片，就可以运行线程。
- **阻塞（Blocked）** - 阻塞状态。线程阻塞的线程状态等待监视器锁定。处于阻塞状态的线程正在等待监视器锁定，以便在调用 `Object.wait()` 之后输入同步块/方法或重新输入同步块/方法。
- **等待（Waiting）** - 等待状态。一个线程处于等待状态，是由于执行了 3 个方法中的任意方法：
  - `Object.wait()`
  - `Thread.join()`
  - `LockSupport.park()`
- **定时等待（Timed waiting）** - 等待指定时间的状态。一个线程处于定时等待状态，是由于执行了以下方法中的任意方法：
  - `Thread.sleep(sleeptime)`
  - `Object.wait(timeout)`
  - `Thread.join(timeout)`
  - `LockSupport.parkNanos(timeout)`
  - `LockSupport.parkUntil(timeout)`
- **终止(Terminated)** - 线程 `run()` 方法执行结束，或者因异常退出了 `run()` 方法，则该线程结束生命周期。死亡的线程不可再次复生。

> :point_right: 参考阅读：[Java `Thread` Methods and `Thread` States](https://www.w3resource.com/java-tutorial/java-threadclass-methods-and-threadstates.php)
> :point_right: 参考阅读：[Java 线程的 5 种状态及切换(透彻讲解)](https://blog.csdn.net/pange1991/article/details/53860651)

### 2.2. 创建线程有哪些方式？这些方法各自利弊是什么？

创建线程主要有三种方式：

**1. 继承 `Thread` 类**

- 定义 `Thread` 类的子类，并重写该类的 `run()` 方法，该 `run()` 方法的方法体就代表了线程要完成的任务。因此把 `run()` 方法称为执行体。
- 创建 `Thread` 子类的实例，即创建了线程对象。
- 调用线程对象的 `start()` 方法来启动该线程。

**2. 实现 `Runnable` 接口**

- 定义 `Runnable` 接口的实现类，并重写该接口的 `run()` 方法，该 `run()` 方法的方法体同样是该线程的线程执行体。
- 创建 `Runnable` 实现类的实例，并以此实例作为 `Thread` 对象，该 `Thread` 对象才是真正的线程对象。
- 调用线程对象的 start() 方法来启动该线程。

**3. 通过 `Callable` 接口和 `Future` 接口**

- 创建 `Callable` 接口的实现类，并实现 `call()` 方法，该 `call()` 方法将作为线程执行体，并且有返回值。
- 创建 `Callable` 实现类的实例，使用 `FutureTask` 类来包装 `Callable` 对象，该 `FutureTask` 对象封装了该 `Callable` 对象的 `call()` 方法的返回值。
- 使用 `FutureTask` 对象作为 `Thread` 对象的 target 创建并启动新线程。
- 调用 `FutureTask` 对象的 `get()` 方法来获得子线程执行结束后的返回值

三种创建线程方式对比

- 实现 `Runnable` 接口优于继承 `Thread` 类，因为根据开放封闭原则——实现接口更便于扩展；
- 实现 `Runnable` 接口的线程没有返回值；而使用 `Callable` / `Future` 方式可以让线程有返回值。

> :point_right: 参考阅读：[java 创建线程的三种方式及其对比](https://blog.csdn.net/longshengguoji/article/details/41126119)

### 2.3. 什么是 `Callable` 和 `Future`？什么是 `FutureTask`？

**什么是 `Callable` 和 `Future`？**

Java 5 在 concurrency 包中引入了 `Callable` 接口，它和 `Runnable` 接口很相似，但它可以返回一个对象或者抛出一个异常。

`Callable` 接口使用泛型去定义它的返回类型。`Executors` 类提供了一些有用的方法去在线程池中执行 `Callable` 内的任务。由于 `Callable` 任务是并行的，我们必须等待它返回的结果。`Future` 对象为我们解决了这个问题。在线程池提交 `Callable` 任务后返回了一个 `Future` 对象，使用它我们可以知道 `Callable` 任务的状态和得到 `Callable` 返回的执行结果。`Future` 提供了 `get()` 方法让我们可以等待 `Callable` 结束并获取它的执行结果。

**什么是 `FutureTask`？**

`FutureTask` 是 `Future` 的一个基础实现，我们可以将它同 `Executors` 使用处理异步任务。通常我们不需要使用 `FutureTask` 类，单当我们打算重写 `Future` 接口的一些方法并保持原来基础的实现是，它就变得非常有用。我们可以仅仅继承于它并重写我们需要的方法。阅读 Java `FutureTask` 例子，学习如何使用它。

> :point_right: 参考阅读：[Java 并发编程：Callable、Future 和 FutureTask](http://www.cnblogs.com/dolphin0520/p/3949310.html)

### 2.4. `start()` 和 `run()` 有什么区别？可以直接调用 `Thread` 类的 `run()` 方法么？

- `run()` 方法是线程的执行体。
- `start()` 方法负责启动线程，然后 JVM 会让这个线程去执行 `run()` 方法。

可以直接调用 `Thread` 类的 `run()` 方法么？

- 可以。但是如果直接调用 `Thread` 的 `run()` 方法，它的行为就会和普通的方法一样。
- 为了在新的线程中执行我们的代码，必须使用 `start()` 方法。

### 2.5. `sleep()`、`yield()`、`join()` 方法有什么区别？为什么 `sleep()` 和 `yield()` 方法是静态（static）的？

**`yield()`**

- `yield()` 方法可以让当前正在执行的线程暂停，但它不会阻塞该线程，它只是将该线程从 **Running** 状态转入 `Runnable` 状态。
- 当某个线程调用了 `yield()` 方法暂停之后，只有优先级与当前线程相同，或者优先级比当前线程更高的处于就绪状态的线程才会获得执行的机会。

**`sleep()`**

- `sleep()` 方法需要指定等待的时间，它可以让当前正在执行的线程在指定的时间内暂停执行，进入 **Blocked** 状态。
- 该方法既可以让其他同优先级或者高优先级的线程得到执行的机会，也可以让低优先级的线程得到执行机会。
- 但是，`sleep()` 方法不会释放“锁标志”，也就是说如果有 `synchronized` 同步块，其他线程仍然不能访问共享数据。

**`join()`**

- `join()` 方法会使当前线程转入 **Blocked** 状态，等待调用 `join()` 方法的线程结束后才能继续执行。

**为什么 `sleep()` 和 `yield()` 方法是静态（static）的？**

- `Thread` 类的 `sleep()` 和 `yield()` 方法将处理 **Running** 状态的线程。所以在其他处于非 **Running** 状态的线程上执行这两个方法是没有意义的。这就是为什么这些方法是静态的。它们可以在当前正在执行的线程中工作，并避免程序员错误的认为可以在其他非运行线程调用这些方法。

> :point_right: 参考阅读：[Java 线程中 yield 与 join 方法的区别](http://www.importnew.com/14958.html)
> :point_right: 参考阅读：[sleep()，wait()，yield()和 join()方法的区别](https://blog.csdn.net/xiangwanpeng/article/details/54972952)

### 2.6. Java 的线程优先级如何控制？高优先级的 Java 线程一定先执行吗？

**Java 中的线程优先级如何控制**

- Java 中的线程优先级的范围是 `[1,10]`，一般来说，高优先级的线程在运行时会具有优先权。可以通过 `thread.setPriority(Thread.MAX_PRIORITY)` 的方式设置，默认优先级为 `5`。

**高优先级的 Java 线程一定先执行吗**

- 即使设置了线程的优先级，也**无法保证高优先级的线程一定先执行**。
- 原因：这是因为 **Java 线程优先级依赖于操作系统的支持**，然而，不同的操作系统支持的线程优先级并不相同，不能很好的和 Java 中线程优先级一一对应。
- 结论：Java 线程优先级控制并不可靠。

### 2.7. 什么是守护线程？为什么要用守护线程？如何创建守护线程？

**什么是守护线程**

- 守护线程（Daemon Thread）是在后台执行并且不会阻止 JVM 终止的线程。
- 与守护线程（Daemon Thread）相反的，叫用户线程（User Thread），也就是非守护线程。

**为什么要用守护线程**

- 守护线程的优先级比较低，用于为系统中的其它对象和线程提供服务。典型的应用就是垃圾回收器。

**如何创建守护线程**

- 使用 `thread.setDaemon(true)` 可以设置 thread 线程为守护线程。
- 注意点：
  - 正在运行的用户线程无法设置为守护线程，所以 `thread.setDaemon(true)` 必须在 `thread.start()` 之前设置，否则会抛出 `llegalThreadStateException` 异常；
  - 一个守护线程创建的子线程依然是守护线程。
  - 不要认为所有的应用都可以分配给守护线程来进行服务，比如读写操作或者计算逻辑。

> :point_right: 参考阅读：[Java 中守护线程的总结](https://blog.csdn.net/shimiso/article/details/8964414)

### 2.8. 线程间是如何通信的？

当线程间是可以共享资源时，线程间通信是协调它们的重要的手段。`Object` 类中 `wait()`, `notify()` 和 `notifyAll()` 方法可以用于线程间通信关于资源的锁的状态。

> :point_right: 参考阅读：[Java 并发编程：线程间协作的两种方式：wait、notify、notifyAll 和 Condition](http://www.cnblogs.com/dolphin0520/p/3920385.html)

### 2.9. 为什么线程通信的方法 `wait()`, `notify()` 和 `notifyAll()` 被定义在 Object 类里？

Java 的每个对象中都有一个锁(monitor，也可以成为监视器) 并且 `wait()`、`notify()` 等方法用于等待对象的锁或者通知其他线程对象的监视器可用。在 Java 的线程中并没有可供任何对象使用的锁和同步器。这就是为什么这些方法是 Object 类的一部分，这样 Java 的每一个类都有用于线程间通信的基本方法

### 2.10. 为什么 `wait()`, `notify()` 和 `notifyAll()` 必须在同步方法或者同步块中被调用？

当一个线程需要调用对象的 `wait()` 方法的时候，这个线程必须拥有该对象的锁，接着它就会释放这个对象锁并进入等待状态直到其他线程调用这个对象上的 `notify()` 方法。同样的，当一个线程需要调用对象的 `notify()` 方法时，它会释放这个对象的锁，以便其他在等待的线程就可以得到这个对象锁。

由于所有的这些方法都需要线程持有对象的锁，这样就只能通过同步来实现，所以他们只能在同步方法或者同步块中被调用。

## 3. 并发机制的底层实现

### 3.1. `volatile` 有什么作用？它的实现原理是什么？

作用：

- 被 `volatile` 关键字修饰的变量有两层含义：
  - 保证了不同线程对这个变量进行操作时的可见性，即一个线程修改了某个变量的值，这新值对其他线程来说是立即可见的。
  - 禁止进行指令重排序。

原理：

- 观察加入 `volatile` 关键字和没有加入 `volatile` 关键字时所生成的汇编代码发现，加入 `volatile` 关键字时，会多出一个 lock 前缀指令。lock 前缀指令实际上相当于一个内存屏障（也成内存栅栏），内存屏障会提供 3 个功能：
  - 它确保指令重排序时不会把其后面的指令排到内存屏障之前的位置，也不会把前面的指令排到内存屏障的后面；即在执行到内存屏障这句指令时，在它前面的操作已经全部完成；
  - 它会强制将对缓存的修改操作立即写入主存；
  - 如果是写操作，它会导致其他 CPU 中对应的缓存行无效。

> :point_right: 参考阅读：[Java 并发编程：`volatile` 关键字解析](http://www.cnblogs.com/dolphin0520/p/3920373.html)

### 3.2. `synchronized` 有什么作用？它的实现原理是什么？同步方法和同步块，哪个更好？

作用：

- 使用 `synchronized` 关键字来标记一个方法或者代码块，当某个线程调用该对象的 `synchronized` 方法或者访问 `synchronized` 代码块时，这个线程便获得了该对象的锁，其他线程暂时无法访问这个方法，只有等待这个方法执行完毕或者代码块执行完毕，这个线程才会释放该对象的锁，其他线程才能执行这个方法或者代码块。
- **JVM 基于进入和退出 Monitor 对象来实现方法同步和代码块同步**
  - 对于普通同步方法，锁就是实例对象
  - 对于静态同步方法，锁就是当前类的 Class 对象
  - 对于同步代码块，锁就是 Synchonized 括号里配置的对象

原理：

- `synchronized` 关键字会阻止其它线程获取当前对象的监控锁，这样就使得当前对象中被 `synchronized` 关键字保护的代码块无法被其它线程访问，也就无法并发执行。更重要的是，synchronized 还会创建一个内存屏障，内存屏障指令保证了所有 CPU 操作结果都会直接刷到主存中，从而保证了操作的内存可见性，同时也使得先获得这个锁的线程的所有操作，都 happens-before 于随后获得这个锁的线程的操作。
- JDK6 优化
  - 锁一共有 4 种状态，级别从低到高为：无锁状态、偏向锁状态、轻量级锁状态和重量级锁状态，这几个状态会随着竞争情况逐渐升级。**锁可以升级但不能降级**，目的是为了提高获得锁和释放锁的效率
    - 偏向锁：大多数情况下，锁不仅不存在多线程竞争，而且总是同一个线程多次获取，为了让线程获得锁的代价更低引入偏向锁。当某一线程访问同步块时，会在对象头和栈帧中的琐记录里存储锁偏向的线程 ID，以后该线程在进入该同步块的时候，不需要再次使用 CAS 原子操作进行加锁和解锁，只需要简单的测试一下对象头中的 Mark Word 是否存在指向当前线程的偏向锁。如果测试成功，则表示获得锁，否则检测是否设置有偏向锁，如果没有，则使用 CAS 竞争锁，否则偏向锁指向该线程。
    - 轻量级锁：线程执行同步块之前，会在线程私有的栈帧中开辟用于存储锁记录的空间，称为 Displaced Mark Word。然后线程尝试将对象 Mark Word 的替换为指向 Displaced Mark Word 记录的指针，如果成功，那么当前线程获得锁，如果失败，那么使用自旋获得锁。

同步方法和同步块，哪个更好？

- 同步块是更好的选择。
- 因为它不会锁住整个对象（当然你也可以让它锁住整个对象）。同步方法会锁住整个对象，哪怕这个类中有多个不相关联的同步块，这通常会导致他们停止执行并需要等待获得这个对象上的锁。

> :point_right: 参考阅读：[Java 并发编程：synchronized](http://www.cnblogs.com/dolphin0520/p/3923737.html)

### 3.3. `volatile` 和 `synchronized` 的区别？

- `volatile` 本质是在告诉 jvm 当前变量在寄存器（工作内存）中的值是不确定的，需要从主存中读取； `synchronized` 则是锁定当前变量，只有当前线程可以访问该变量，其他线程被阻塞住。
- `volatile` 仅能使用在变量级别；synchronized 则可以使用在变量、方法、和类级别的。
- `volatile` 仅能实现变量的修改可见性，不能保证原子性；而 `synchronized` 则可以保证变量的修改可见性和原子性
- `volatile` 不会造成线程的阻塞；synchronized 可能会造成线程的阻塞。
- `volatile` 标记的变量不会被编译器优化；synchronized 标记的变量可以被编译器优化。

> :point_right: 参考阅读：[volatile 和 `synchronized` 的区别](https://blog.csdn.net/suifeng3051/article/details/52611233)

### CAS 是指什么？CAS 的原理是什么？

### CAS 的三大问题？

1. ABA问题：因为CAS需要在操作值的时候检查下值有没有发生变化，如果没有发生变化则更新，但是如果一个值原来是A，变成了B，又变成了A，那么使用CAS进行检查时会发现它的值没有发生变化，但是实际上却变化了。ABA问题的解决思路就是使用版本号。在变量前面追加上版本号，每次变量更新的时候把版本号加一，那么A－B－A 就会变成1A-2B－3A。
2. 循环时间长开销大。自旋CAS如果长时间不成功，会给CPU带来非常大的执行开销。如果JVM能支持处理器提供的pause指令那么效率会有一定的提升，pause指令有两个作用，第一它可以延迟流水线执行指令（de-pipeline）,使CPU不会消耗过多的执行资源，延迟的时间取决于具体实现的版本，在一些处理器上延迟时间是零。第二它可以避免在退出循环的时候因内存顺序冲突（memory order violation）而引起CPU流水线被清空（CPU pipeline flush），从而提高CPU的执行效率。
3. 只能保证一个共享变量的原子操作。当对一个共享变量执行操作时，我们可以使用循环CAS的方式来保证原子操作，但是对多个共享变量操作时，循环CAS就无法保证操作的原子性，这个时候就可以用锁，或者有一个取巧的办法，就是把多个共享变量合并成一个共享变量来操作。比如有两个共享变量i＝2,j=a，合并一下ij=2a，然后用CAS来操作ij。从Java1.5开始JDK提供了AtomicReference类来保证引用对象之间的原子性，你可以把多个变量放在一个对象里来进行CAS操作。

### 3.4. `ThreadLocal` 有什么作用？`ThreadLocal` 的实现原理是什么？

**ThreadLocal 有什么作用？**

`ThreadLocal` 为变量在每个线程中都创建了一个副本，那么每个线程可以访问自己内部的副本变量。

**ThreadLocal 的实现原理是什么？**

首先，在每个线程 `Thread` 内部有一个 `ThreadLocal.ThreadLocalMap` 类型的成员变量 threadLocals，这个 threadLocals 就是用来存储实际的变量副本的，key 为当前 `ThreadLocal` 变量，value 为变量副本（即 T 类型的变量）。

初始时，在 `Thread` 里面，threadLocals 为空。当通过 `ThreadLocal` 变量调用 `get()` 方法或者 `set()` 方法，就会对 `Thread` 类中的 threadLocals 进行初始化，并且以当前 `ThreadLocal` 变量为键值，以 `ThreadLocal` 要保存的副本变量为 value，存到 threadLocals。

然后在当前线程里面，如果要使用副本变量，就可以通过 get 方法在 threadLocals 里面查找。

需要注意的是：`ThreadLocalMap.Entry` 继承了 `WeakReference`。ThreadLocalMap 使用它的目的是：当 threadLocal 实例可以被 GC 回收时，系统可以检测到该 threadLocal 对应的 Entry 是否已经过期（根据 `reference.get() == null` 来判断，如果为 true 则表示过期，程序内部称为 stale slots）来自动做一些清除工作，否则如果不清除的话容易产生内存无法释放的问题：value 对应的对象即使不再使用，但由于被 threadLocalMap 所引用导致无法被 GC 回收。实际代码中，ThreadLocalMap 会在 set，get 以及 resize 等方法中对 stale slots 做自动删除（set 以及 get 不保证所有过期 slots 会在操作中会被删除，而 resize 则会删除 threadLocalMap 中所有的过期 slots）。当然将 threadLocal 对象设置为 null 并不能完全避免内存泄露对象，最安全的办法仍然是调用 ThreadLocal 的 remove 方法，来彻底避免可能的内存泄露。

> :point_right: 参考阅读：[Java 并发编程：深入剖析 ThreadLocal](https://www.cnblogs.com/dolphin0520/p/3920407.html)

## 4. 内存模型

### 4.1. 什么是 Java 内存模型

- Java 内存模型即 Java Memory Model，简称 JMM。JMM 定义了 JVM 在计算机内存(RAM)中的工作方式。JMM 是隶属于 JVM 的。
- 并发编程领域两个关键问题：线程间通信和线程间同步
- 线程间通信机制
  - 共享内存 - 线程间通过写-读内存中的公共状态来隐式进行通信。
  - 消息传递 - java 中典型的消息传递方式就是 wait()和 notify()。
- 线程间同步机制
  - 在共享内存模型中，必须显示指定某个方法或某段代码在线程间互斥地执行。
  - 在消息传递模型中，由于发送消息必须在接收消息之前，因此同步是隐式进行的。
- Java 的并发采用的是共享内存模型
- JMM 决定一个线程对共享变量的写入何时对另一个线程可见。
- 线程之间的共享变量存储在主内存（main memory）中，每个线程都有一个私有的本地内存（local memory），本地内存中存储了该线程以读/写共享变量的副本。
- JMM 把内存分成了两部分：线程栈区和堆区
  - 线程栈
    - JVM 中运行的每个线程都拥有自己的线程栈，线程栈包含了当前线程执行的方法调用相关信息，我们也把它称作调用栈。随着代码的不断执行，调用栈会不断变化。
    - 线程栈还包含了当前方法的所有本地变量信息。线程中的本地变量对其它线程是不可见的。
  - 堆区
    - 堆区包含了 Java 应用创建的所有对象信息，不管对象是哪个线程创建的，其中的对象包括原始类型的封装类（如 Byte、Integer、Long 等等）。不管对象是属于一个成员变量还是方法中的本地变量，它都会被存储在堆区。
  - 一个本地变量如果是原始类型，那么它会被完全存储到栈区。
  - 一个本地变量也有可能是一个对象的引用，这种情况下，这个本地引用会被存储到栈中，但是对象本身仍然存储在堆区。
  - 对于一个对象的成员方法，这些方法中包含本地变量，仍需要存储在栈区，即使它们所属的对象在堆区。
  - 对于一个对象的成员变量，不管它是原始类型还是包装类型，都会被存储到堆区。

<p align="center">
  <img src="https://gitee.com/turnon/images/raw/master/images/java/concurrent/jmm-model.png" alt="thread-states">
</p>

> :point_right: 参考阅读：[全面理解 Java 内存模型](https://blog.csdn.net/suifeng3051/article/details/52611310)

## 5. 同步容器和并发容器

### 5.1. 什么是同步容器？有哪些常见同步容器？它们是如何实现线程安全的？同步容器真的线程安全吗？

- 同步容器是指 Java 中使用 `synchronized` 关键字修饰方法以保证方法线程安全的容器。
- 常见的同步容器有 Vector、HashTable、Stack，与之相对应的 ArrayList、HashMap、LinkedList 则是非线程安全的。
- 同步容器之所以说是线程安全的，是因为它们的方法被 `synchronized` 关键字修饰，从而保证了当有一个线程执行方法时，其他线程被阻塞。
- 同步容器中的所有自带方法都是线程安全的。但是，对这些集合类的复合操作无法保证其线程安全性。需要客户端通过主动加锁来保证。
  - 典型场景：使用同步容器做迭代操作时，如果不对外部做同步，就可能出现 ConcurrentModificationException 异常。
  - 结论：由于同步容器不能彻底保证线程安全，且性能不高，所以不建议使用。如果想使用线程安全的容器，可以考虑 juc 包中提供的 ConcurrentHashMap 等并发容器。

```java
for(int i=0;i<vector.size();i++)
    vector.remove(i);
```

> :point_right: 参考阅读：[Java 并发编程：同步容器](https://www.cnblogs.com/dolphin0520/p/3933404.html)

### 5.2. 什么是并发容器的实现？

Java 集合类都是快速失败的，这就意味着当集合被改变且一个线程在使用迭代器遍历集合的时候，迭代器的 next()方法将抛出 ConcurrentModificationException 异常。

并发容器支持并发的遍历和并发的更新。

主要的类有 ConcurrentHashMap, CopyOnWriteArrayList 和 CopyOnWriteArraySet，阅读这篇文章了解如何避免 ConcurrentModificationException。

## 6. 锁

### 6.1. 如何避免死锁？

- 避免一个线程同时获取多个锁
- 避免一个线程在锁内同时占用多个资源，尽量保证每个锁只占用一个资源
- 尝试使用定时锁 lock.tryLock(timeout)，避免锁一直不能释放
- 对于数据库锁，加锁和解锁必须在一个数据库连接中里，否则会出现解锁失败的情况。

### Java 中有哪些锁？

### 6.2. Lock 接口(Lock interface)是什么？对比同步它有什么优势？

Lock 接口比同步方法和同步块提供了更具扩展性的锁操作。他们允许更灵活的结构，可以具有完全不同的性质，并且可以支持多个相关类的条件对象。

它的优势有：

可以使锁更公平可以使线程在等待锁的时候响应中断可以让线程尝试获取锁，并在无法获取锁的时候立即返回或者等待一段时间可以在不同的范围，以不同的顺序获取和释放锁阅读更多关于锁的例子

### 6.3. 什么是阻塞队列？如何使用阻塞队列来实现生产者-消费者模型？

java.util.concurrent.BlockingQueue 的特性是：当队列是空的时，从队列中获取或删除元素的操作将会被阻塞，或者当队列是满时，往队列里添加元素的操作会被阻塞。

阻塞队列不接受空值，当你尝试向队列中添加空值的时候，它会抛出 NullPointerException。

阻塞队列的实现都是线程安全的，所有的查询方法都是原子的并且使用了内部锁或者其他形式的并发控制。

BlockingQueue 接口是 java collections 框架的一部分，它主要用于实现生产者-消费者问题。

阅读这篇文章了解如何使用阻塞队列实现生产者-消费者问题。

## 7. 原子变量类

### 7.1. 什么是原子操作？有哪些原子类？

原子操作是指一个不受其他操作影响的操作任务单元。原子操作是在多线程环境下避免数据不一致必须的手段。

int++并不是一个原子操作，所以当一个线程读取它的值并加 1 时，另外一个线程有可能会读到之前的值，这就会引发错误。

为了解决这个问题，必须保证增加操作是原子的，在 JDK1.5 之前我们可以使用同步技术来做到这一点。到 JDK1.5，java.util.concurrent.atomic 包提供了 int 和 long 类型的装类，它们可以自动的保证对于他们的操作是原子的并且不需要使用同步。可以阅读这篇文章来了解 Java 的 atomic 类。

### 原子类的实现原理是什么？

1. 处理器实现原子操作：使用总线锁保证原子性，使用缓存锁保证原子性（修改内存地址，缓存一致性机制：阻止同时修改由2个以上的处理器缓存的内存区域数据）
2. JAVA实现原子操作：循环使用CAS实现原子操作

## 8. 并发工具类

### 8.1. CyclicBarrier 和 CountDownLatch 有什么不同？

CyclicBarrier 和 CountDownLatch 都可以用来让一组线程等待其它线程。与 CyclicBarrier 不同的是，CountdownLatch 不能重用。

> :point_right: 参考阅读：[Java 并发编程：CountDownLatch、CyclicBarrier 和 Semaphore](http://www.cnblogs.com/dolphin0520/p/3920397.html)

## 9. 线程池

### 9.1. 什么是线程池？如何创建一个 Java 线程池？

一个线程池管理了一组工作线程，同时它还包括了一个用于放置等待执行的任务的队列。

java.util.concurrent.Executors 提供了一个 java.util.concurrent.Executor 接口的实现用于创建线程池。线程池例子展现了如何创建和使用线程池，或者阅读 ScheduledThreadPoolExecutor 例子，了解如何创建一个周期任务。

> :point_right: 参考阅读：[Java 并发编程：线程池的使用](http://www.cnblogs.com/dolphin0520/p/3932921.html)

### 9.2. 什么是 Executors 框架？

Executor 框架同 java.util.concurrent.Executor 接口在 Java 5 中被引入。Executor 框架是一个根据一组执行策略调用，调度，执行和控制的异步任务的框架。

无限制的创建线程会引起应用程序内存溢出。所以创建一个线程池是个更好的的解决方案，因为可以限制线程的数量并且可以回收再利用这些线程。利用 Executors 框架可以非常方便的创建一个线程池，阅读这篇文章可以了解如何使用 Executor 框架创建一个线程池。

### 9.3. Executors 类是什么？

Executors 为 Executor，ExecutorService，ScheduledExecutorService，ThreadFactory 和 `Callable` 类提供了一些工具方法。

Executors 可以用于方便的创建线程池。

### 9.4. ThreadPoolExecutor 有哪些参数，各自有什么用？

`java.uitl.concurrent.ThreadPoolExecutor` 类是 Executor 框架中最核心的一个类。

ThreadPoolExecutor 有四个构造方法，前三个都是基于第四个实现。第四个构造方法定义如下：

```java
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) {
```

#### 参数说明

- `corePoolSize`：默认情况下，在创建了线程池后，线程池中的线程数为 0，当有任务来之后，就会创建一个线程去执行任务，当线程池中的线程数目达到 corePoolSize 后，就会把到达的任务放到缓存队列当中。
- `maximumPoolSize`：线程池允许创建的最大线程数。如果缓存队列满了，并且已创建的线程数小于最大线程数，则线程池会再创建新的线程执行任务。值得注意的是如果使用了无界的任务队列这个参数就没什么效果。
- `keepAliveTime`：线程活动保持时间。线程池的工作线程空闲后，保持存活的时间。所以如果任务很多，并且每个任务执行的时间比较短，可以调大这个时间，提高线程的利用率。
- `unit`：参数 keepAliveTime 的时间单位，有 7 种取值。可选的单位有天（DAYS），小时（HOURS），分钟（MINUTES），毫秒(MILLISECONDS)，微秒(MICROSECONDS, 千分之一毫秒)和毫微秒(NANOSECONDS, 千分之一微秒)。
- `workQueue`：任务队列。用于保存等待执行的任务的阻塞队列。 可以选择以下几个阻塞队列。
  - ArrayBlockingQueue：是一个基于数组结构的有界阻塞队列，此队列按 FIFO（先进先出）原则对元素进行排序。
  - LinkedBlockingQueue：一个基于链表结构的阻塞队列，此队列按 FIFO （先进先出） 排序元素，吞吐量通常要高于 ArrayBlockingQueue。静态工厂方法 Executors.newFixedThreadPool()使用了这个队列。
  - SynchronousQueue：一个不存储元素的阻塞队列。每个插入操作必须等到另一个线程调用移除操作，否则插入操作一直处于阻塞状态，吞吐量通常要高于 LinkedBlockingQueue，静态工厂方法 Executors.newCachedThreadPool 使用了这个队列。
  - PriorityBlockingQueue：一个具有优先级的无限阻塞队列。
- `threadFactory`：创建线程的工厂。可以通过线程工厂给每个创建出来的线程设置更有意义的名字。
- `handler`：饱和策略。当队列和线程池都满了，说明线程池处于饱和状态，那么必须采取一种策略处理提交的新任务。这个策略默认情况下是 AbortPolicy，表示无法处理新任务时抛出异常。以下是 JDK1.5 提供的四种策略。
  - AbortPolicy：直接抛出异常。
  - CallerRunsPolicy：只用调用者所在线程来运行任务。
  - DiscardOldestPolicy：丢弃队列里最近的一个任务，并执行当前任务。
  - DiscardPolicy：不处理，丢弃掉。
  - 当然也可以根据应用场景需要来实现 RejectedExecutionHandler 接口自定义策略。如记录日志或持久化不能处理的任务。

### 9.5. 线程数多少才合理

#### 资源限制

资源限制指的是程序的执行速度受限于计算机硬件资源或软件资源，如服务器的带宽只有 2Mb/s,某个资源的下载速度为 1Mb/s,系统启动 10 个线程去下载资源，下载速度不会变成 10Mb/s，所以在进行并发的时候回考虑资源的限制。硬件资源限制有带宽的上传/下载速度、硬盘的读写速度和 CPU 的处理速度。软件资源限制有数据库的连接数和 socket 连接数等。

资源限制引来的问题：为了将代码执行速度加快将代码中串行执行的部分变成并发执行，因为资源受限，仍然在串行执行，这时候程序不仅不会加快，反而会变慢，因为增加了上下文切换和资源调度的时间。

如何解决资源限制问题：可以使用集群并行执行程序，既然单机的资源有限，那么可以让程序在多机上运行，比如使用 ODPS、Hadoop 或者自己搭个服务器集群，不同的机器处理不同的数据，可以通过“数据 ID%机器数”，计算得到一个机器编号，然后由对应编号的机器处理这个数据，对于软件资源受限，可以使用资源池来复用如使用连接池将数据库和 Socket 连接复用，或者在调用对方 webservice 接口获取数据只建立一个连接。

## 10. 资料

- **文章**
  - [Java 线程面试题 Top 50](http://www.importnew.com/12773.html)
  - [JAVA 多线程和并发基础面试问答](http://ifeve.com/java-multi-threading-concurrency-interview-questions-with-answers/)
  - [进程和线程关系及区别](https://blog.csdn.net/yaosiming2011/article/details/44280797)
  - [Java Thread Methods and Thread States](https://www.w3resource.com/java-tutorial/java-threadclass-methods-and-threadstates.php)
  - [Java 线程的 5 种状态及切换(透彻讲解)](https://blog.csdn.net/pange1991/article/details/53860651)
  - [Java 中守护线程的总结](https://blog.csdn.net/shimiso/article/details/8964414)
  - [java 创建线程的三种方式及其对比](https://blog.csdn.net/longshengguoji/article/details/41126119)
  - [Java 线程的 5 种状态及切换(透彻讲解)](https://blog.csdn.net/pange1991/article/details/53860651)
  - [java 线程方法 join 的简单总结](https://www.cnblogs.com/lcplcpjava/p/6896904.html)
  - [Java 并发编程：线程间协作的两种方式：wait、notify、notifyAll 和 Condition](http://www.cnblogs.com/dolphin0520/p/3920385.html)
  - [Java 并发编程：volatile 关键字解析](http://www.cnblogs.com/dolphin0520/p/3920373.html)
  - [Java 并发编程：Callable、Future 和 FutureTask](http://www.cnblogs.com/dolphin0520/p/3949310.html)
  - [Java 并发编程：线程池的使用](http://www.cnblogs.com/dolphin0520/p/3932921.html)
  - [Java并发编程](https://www.jianshu.com/p/0256c2995cec)
