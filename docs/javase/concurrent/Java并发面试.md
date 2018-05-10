<!-- TOC depthFrom:2 depthTo:3 -->

- [线程](#线程)
  - [进程 vs. 线程](#进程-vs-线程)
  - [多线程编程的好处是什么？](#多线程编程的好处是什么)
  - [用户线程 vs. 守护线程](#用户线程-vs-守护线程)
  - [如何创建守护线程？](#如何创建守护线程)
  - [如何创建一个线程？](#如何创建一个线程)
  - [线程生命周期有哪些状态？](#线程生命周期有哪些状态)
  - [可以直接调用 Thread 类的 run()方法么？](#可以直接调用-thread-类的-run方法么)
  - [如何让正在运行的线程暂停一段时间？](#如何让正在运行的线程暂停一段时间)
  - [你对线程优先级的理解是什么？](#你对线程优先级的理解是什么)
  - [什么是线程调度器(Thread Scheduler)和时间分片(Time Slicing)？](#什么是线程调度器thread-scheduler和时间分片time-slicing)
  - [在多线程中，什么是上下文切换(context-switching)？](#在多线程中什么是上下文切换context-switching)
  - [你如何确保 main()方法所在的线程是 Java 程序最后结束的线程？](#你如何确保-main方法所在的线程是-java-程序最后结束的线程)
  - [为什么 Thread 类的 sleep()和 yield()方法是静态的？](#为什么-thread-类的-sleep和-yield方法是静态的)
- [线程间通信](#线程间通信)
  - [如何确保线程安全？](#如何确保线程安全)
  - [线程之间是如何通信的？](#线程之间是如何通信的)
  - [为什么线程通信的方法 wait(), notify()和 notifyAll()被定义在 Object 类里？](#为什么线程通信的方法-wait-notify和-notifyall被定义在-object-类里)
  - [为什么 wait(), notify()和 notifyAll()必须在同步方法或者同步块中被调用？](#为什么-wait-notify和-notifyall必须在同步方法或者同步块中被调用)
  - [volatile 关键字在 Java 中有什么作用？](#volatile-关键字在-java-中有什么作用)
  - [同步方法和同步块，哪个是更好的选择？](#同步方法和同步块哪个是更好的选择)
  - [什么是 ThreadLocal?](#什么是-threadlocal)
  - [什么是死锁(Deadlock)？如何分析和避免死锁？](#什么是死锁deadlock如何分析和避免死锁)
- [锁](#锁)
  - [Lock 接口(Lock interface)是什么？对比同步它有什么优势？](#lock-接口lock-interface是什么对比同步它有什么优势)
  - [什么是阻塞队列？如何使用阻塞队列来实现生产者-消费者模型？](#什么是阻塞队列如何使用阻塞队列来实现生产者-消费者模型)
- [并发容器](#并发容器)
  - [什么是并发容器的实现？](#什么是并发容器的实现)
- [原子操作](#原子操作)
  - [什么是原子操作？有哪些原子类？原子类的实现原理是什么？](#什么是原子操作有哪些原子类原子类的实现原理是什么)
- [线程池](#线程池)
  - [什么是线程池？如何创建一个 Java 线程池？](#什么是线程池如何创建一个-java-线程池)
  - [什么是 Executors 框架？](#什么是-executors-框架)
  - [Executors 类是什么？](#executors-类是什么)
  - [什么是 Callable 和 Future?](#什么是-callable-和-future)
  - [什么是 FutureTask?](#什么是-futuretask)
- [资料](#资料)

<!-- /TOC -->

## 线程

### 进程 vs. 线程

一个进程是一个独立的运行环境，它可以被看作一个程序或者一个应用。而线程是在进程中执行的一个任务。Java 运行环境是一个包含了不同的类和程序的单一进程。线程可以被称为轻量级进程。线程需要较少的资源来创建和驻留在进程中，并且可以共享进程中的资源。

### 多线程编程的好处是什么？

在多线程程序中，多个线程被并发的执行以提高程序的效率，CPU 不会因为某个线程需要等待资源而进入空闲状态。多个线程共享堆内存(heap memory)，因此创建多个线程去执行一些任务会比创建多个进程更好。举个例子，Servlets 比 CGI 更好，是因为 Servlets 支持多线程而 CGI 不支持。

### 用户线程 vs. 守护线程

* 守护线程是在后台执行并且不会阻止 JVM 终止的线程。
* 非守护线程，就被称为用户线程。
* 当没有用户线程在运行的时候，JVM 关闭程序并且退出。
* 一个守护线程创建的子线程依然是守护线程。

### 如何创建守护线程？

使用 Thread 类的 setDaemon(true)方法可以将线程设置为守护线程，需要注意的是，需要在调用 start()方法前调用这个方法，否则会抛出 llegalThreadStateException 异常。

### 如何创建一个线程？

有两种创建线程的方法：

* 实现 Runnable 接口，然后将它传递给 Thread 的构造函数，创建一个 Thread 对象；
* 继承 Thread 类。

> 详情参考：[如何创建并运行 java 线程](http://ifeve.com/creating-and-starting-java-threads/)

### 线程生命周期有哪些状态？

* 新建(NEW)：新创建了一个线程对象。
* 可运行(RUNNABLE)：线程对象创建后，其他线程(比如 main 线程）调用了该对象的 start()方法。该状态的线程位于可运行线程池中，等待被线程调度选中，获取 cpu 的使用权 。
* 运行(RUNNING)：可运行状态(runnable)的线程获得了 cpu 时间片（timeslice） ，执行程序代码。
* 阻塞(BLOCKED)：阻塞状态是指线程因为某种原因放弃了 cpu 使用权，也即让出了 cpu timeslice，暂时停止运行。直到线程进入可运行(runnable)状态，才有机会再次获得 cpu timeslice 转到运行(running)状态。阻塞的情况分三种：
  * 等待阻塞：运行(running)的线程执行 o.wait()方法，JVM 会把该线程放入等待队列(waitting queue)中。
  * 同步阻塞：运行(running)的线程在获取对象的同步锁时，若该同步锁被别的线程占用，则 JVM 会把该线程放入锁池(lock pool)中。
  * 其他阻塞：运行(running)的线程执行 Thread.sleep(long ms)或 t.join()方法，或者发出了 I/O 请求时，JVM 会把该线程置为阻塞状态。当 sleep()状态超时、join()等待线程终止或者超时、或者 I/O 处理完毕时，线程重新转入可运行(runnable)状态。
* 死亡(DEAD)：线程 run()、main() 方法执行结束，或者因异常退出了 run()方法，则该线程结束生命周期。死亡的线程不可再次复生。

> 详情参考：[Java 线程的 5 种状态及切换(透彻讲解)](https://blog.csdn.net/pange1991/article/details/53860651)

### 可以直接调用 Thread 类的 run()方法么？

可以。但是如果调用了 Thread 的 run()方法，它的行为就会和普通的方法一样。

为了在新的线程中执行我们的代码，必须使用 Thread.start()方法。

### 如何让正在运行的线程暂停一段时间？

我们可以使用 Thread 类的 Sleep() 方法让线程暂停一段时间。

需要注意的是，这并不会让线程终止，一旦从休眠中唤醒线程，线程的状态将会被改变为 Runnable，并且根据线程调度，它将得到执行。

### 你对线程优先级的理解是什么？

在 Java 中，线程优先级可以分为从 1 到 10，一般来说，高优先级的线程在运行时会具有优先权。

但是，这并不能保证高优先级的线程会在低优先级的线程前执行。因为 Java 线程优先级依赖于线程调度的实现，这个实现是和操作系统相关的(OS dependent)。

### 什么是线程调度器(Thread Scheduler)和时间分片(Time Slicing)？

线程调度器是一个操作系统服务，它负责为 Runnable 状态的线程分配 CPU 时间。一旦我们创建一个线程并启动它，它的执行便依赖于线程调度器的实现。

时间分片是指将可用的 CPU 时间分配给可用的 Runnable 线程的过程。

分配 CPU 时间可以基于线程优先级或者线程等待的时间。线程调度并不受到 Java 虚拟机控制，所以由应用程序来控制它是更好的选择（也就是说不要让你的程序依赖于线程的优先级）。

### 在多线程中，什么是上下文切换(context-switching)？

上下文切换是存储和恢复 CPU 状态的过程，它使得线程执行能够从中断点恢复执行。上下文切换是多任务操作系统和多线程环境的基本特征。

### 你如何确保 main()方法所在的线程是 Java 程序最后结束的线程？

我们可以使用 Thread 类的 joint()方法来确保所有程序创建的线程在 main()方法退出前结束。这里有一篇文章关于 Thread 类的 joint()方法。

> 详情参考：[java 线程方法 join 的简单总结](https://www.cnblogs.com/lcplcpjava/p/6896904.html)

### 为什么 Thread 类的 sleep()和 yield()方法是静态的？

Thread 类的 sleep()和 yield()方法将在当前正在执行的线程上运行。所以在其他处于等待状态的线程上调用这些方法是没有意义的。这就是为什么这些方法是静态的。它们可以在当前正在执行的线程中工作，并避免程序员错误的认为可以在其他非运行线程调用这些方法。

## 线程间通信

### 如何确保线程安全？

* 原子类(atomic concurrent classes)
* 锁
* volatile 关键字
* 不变类和线程安全类

### 线程之间是如何通信的？

当线程间是可以共享资源时，线程间通信是协调它们的重要的手段。Object 类中 wait()\notify()\notifyAll()方法可以用于线程间通信关于资源的锁的状态。点击这里有更多关于线程 wait, notify 和 notifyAll.

> 详情参考：[Java 并发编程：线程间协作的两种方式：wait、notify、notifyAll 和 Condition](http://www.cnblogs.com/dolphin0520/p/3920385.html)

### 为什么线程通信的方法 wait(), notify()和 notifyAll()被定义在 Object 类里？

Java 的每个对象中都有一个锁(monitor，也可以成为监视器) 并且 wait()，notify()等方法用于等待对象的锁或者通知其他线程对象的监视器可用。在 Java 的线程中并没有可供任何对象使用的锁和同步器。这就是为什么这些方法是 Object 类的一部分，这样 Java 的每一个类都有用于线程间通信的基本方法

### 为什么 wait(), notify()和 notifyAll()必须在同步方法或者同步块中被调用？

当一个线程需要调用对象的 wait()方法的时候，这个线程必须拥有该对象的锁，接着它就会释放这个对象锁并进入等待状态直到其他线程调用这个对象上的 notify()方法。同样的，当一个线程需要调用对象的 notify()方法时，它会释放这个对象的锁，以便其他在等待的线程就可以得到这个对象锁。由于所有的这些方法都需要线程持有对象的锁，这样就只能通过同步来实现，所以他们只能在同步方法或者同步块中被调用。

### volatile 关键字在 Java 中有什么作用？

使用 volatile 关键字去修饰变量的时候，所有线程都会直接读取该变量并且不缓存它。这就确保了线程读取到的变量是同内存中是一致的。

> 详情参考：[Java 并发编程：volatile 关键字解析](http://www.cnblogs.com/dolphin0520/p/3920373.html)

### 同步方法和同步块，哪个是更好的选择？

同步块是更好的选择。

因为它不会锁住整个对象（当然你也可以让它锁住整个对象）。同步方法会锁住整个对象，哪怕这个类中有多个不相关联的同步块，这通常会导致他们停止执行并需要等待获得这个对象上的锁。

### 什么是 ThreadLocal?

ThreadLocal 用于创建线程的本地变量，我们知道一个对象的所有线程会共享它的全局变量，所以这些变量不是线程安全的，我们可以使用同步技术。但是当我们不想使用同步的时候，我们可以选择 ThreadLocal 变量。

每个线程都会拥有他们自己的 Thread 变量，它们可以使用 get()\set()方法去获取他们的默认值或者在线程内部改变他们的值。ThreadLocal 实例通常是希望它们同线程状态关联起来是 private static 属性。在 ThreadLocal 例子这篇文章中你可以看到一个关于 ThreadLocal 的小程序。

### 什么是死锁(Deadlock)？如何分析和避免死锁？

死锁是指两个以上的线程永远相互阻塞的情况，这种情况产生至少需要两个以上的线程和两个以上的资源。

分析死锁，我们需要查看 Java 应用程序的线程转储。我们需要找出那些状态为 BLOCKED 的线程和他们等待的资源。每个资源都有一个唯一的 id，用这个 id 我们可以找出哪些线程已经拥有了它的对象锁。

避免嵌套锁，只在需要的地方使用锁和避免无限期等待是避免死锁的通常办法。

## 锁

### Lock 接口(Lock interface)是什么？对比同步它有什么优势？

Lock 接口比同步方法和同步块提供了更具扩展性的锁操作。他们允许更灵活的结构，可以具有完全不同的性质，并且可以支持多个相关类的条件对象。

它的优势有：

可以使锁更公平可以使线程在等待锁的时候响应中断可以让线程尝试获取锁，并在无法获取锁的时候立即返回或者等待一段时间可以在不同的范围，以不同的顺序获取和释放锁阅读更多关于锁的例子

### 什么是阻塞队列？如何使用阻塞队列来实现生产者-消费者模型？

java.util.concurrent.BlockingQueue 的特性是：当队列是空的时，从队列中获取或删除元素的操作将会被阻塞，或者当队列是满时，往队列里添加元素的操作会被阻塞。

阻塞队列不接受空值，当你尝试向队列中添加空值的时候，它会抛出 NullPointerException。

阻塞队列的实现都是线程安全的，所有的查询方法都是原子的并且使用了内部锁或者其他形式的并发控制。

BlockingQueue 接口是 java collections 框架的一部分，它主要用于实现生产者-消费者问题。

阅读这篇文章了解如何使用阻塞队列实现生产者-消费者问题。

## 并发容器

### 什么是并发容器的实现？

Java 集合类都是快速失败的，这就意味着当集合被改变且一个线程在使用迭代器遍历集合的时候，迭代器的 next()方法将抛出 ConcurrentModificationException 异常。

并发容器支持并发的遍历和并发的更新。

主要的类有 ConcurrentHashMap, CopyOnWriteArrayList 和 CopyOnWriteArraySet，阅读这篇文章了解如何避免 ConcurrentModificationException。

## 原子操作

### 什么是原子操作？有哪些原子类？原子类的实现原理是什么？

原子操作是指一个不受其他操作影响的操作任务单元。原子操作是在多线程环境下避免数据不一致必须的手段。

int++并不是一个原子操作，所以当一个线程读取它的值并加 1 时，另外一个线程有可能会读到之前的值，这就会引发错误。

为了解决这个问题，必须保证增加操作是原子的，在 JDK1.5 之前我们可以使用同步技术来做到这一点。到 JDK1.5，java.util.concurrent.atomic 包提供了 int 和 long 类型的装类，它们可以自动的保证对于他们的操作是原子的并且不需要使用同步。可以阅读这篇文章来了解 Java 的 atomic 类。

## 线程池

### 什么是线程池？如何创建一个 Java 线程池？

一个线程池管理了一组工作线程，同时它还包括了一个用于放置等待执行的任务的队列。

java.util.concurrent.Executors 提供了一个 java.util.concurrent.Executor 接口的实现用于创建线程池。线程池例子展现了如何创建和使用线程池，或者阅读 ScheduledThreadPoolExecutor 例子，了解如何创建一个周期任务。

> [Java 并发编程：线程池的使用](http://www.cnblogs.com/dolphin0520/p/3932921.html)

### 什么是 Executors 框架？

Executor 框架同 java.util.concurrent.Executor 接口在 Java 5 中被引入。Executor 框架是一个根据一组执行策略调用，调度，执行和控制的异步任务的框架。

无限制的创建线程会引起应用程序内存溢出。所以创建一个线程池是个更好的的解决方案，因为可以限制线程的数量并且可以回收再利用这些线程。利用 Executors 框架可以非常方便的创建一个线程池，阅读这篇文章可以了解如何使用 Executor 框架创建一个线程池。

### Executors 类是什么？

Executors 为 Executor，ExecutorService，ScheduledExecutorService，ThreadFactory 和 Callable 类提供了一些工具方法。

Executors 可以用于方便的创建线程池。

### 什么是 Callable 和 Future?

> [Java 并发编程：Callable、Future 和 FutureTask](http://www.cnblogs.com/dolphin0520/p/3949310.html)

Java 5 在 concurrency 包中引入了 java.util.concurrent.Callable 接口，它和 Runnable 接口很相似，但它可以返回一个对象或者抛出一个异常。

Callable 接口使用泛型去定义它的返回类型。Executors 类提供了一些有用的方法去在线程池中执行 Callable 内的任务。由于 Callable 任务是并行的，我们必须等待它返回的结果。java.util.concurrent.Future 对象为我们解决了这个问题。在线程池提交 Callable 任务后返回了一个 Future 对象，使用它我们可以知道 Callable 任务的状态和得到 Callable 返回的执行结果。Future 提供了 get()方法让我们可以等待 Callable 结束并获取它的执行结果。

阅读这篇文章了解更多关于 Callable，Future 的例子。

### 什么是 FutureTask?

FutureTask 是 Future 的一个基础实现，我们可以将它同 Executors 使用处理异步任务。通常我们不需要使用 FutureTask 类，单当我们打算重写 Future 接口的一些方法并保持原来基础的实现是，它就变得非常有用。我们可以仅仅继承于它并重写我们需要的方法。阅读 Java FutureTask 例子，学习如何使用它。

## 资料

* [Java 线程面试题 Top 50](http://www.importnew.com/12773.html)
* [JAVA 多线程和并发基础面试问答](http://ifeve.com/java-multi-threading-concurrency-interview-questions-with-answers/)
* [如何创建并运行 java 线程](http://ifeve.com/creating-and-starting-java-threads/)
* [Java 线程的 5 种状态及切换(透彻讲解)](https://blog.csdn.net/pange1991/article/details/53860651)
* [java 线程方法 join 的简单总结](https://www.cnblogs.com/lcplcpjava/p/6896904.html)
* [Java 并发编程：线程间协作的两种方式：wait、notify、notifyAll 和 Condition](http://www.cnblogs.com/dolphin0520/p/3920385.html)
* [Java 并发编程：volatile 关键字解析](http://www.cnblogs.com/dolphin0520/p/3920373.html)
* [Java 并发编程：Callable、Future 和 FutureTask](http://www.cnblogs.com/dolphin0520/p/3949310.html)
* [Java 并发编程：线程池的使用](http://www.cnblogs.com/dolphin0520/p/3932921.html)
