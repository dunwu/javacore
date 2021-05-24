## 基础

### 工具类

#### String

> String 类能被继承吗？
>
> String，StringBuffer，StringBuilder 的区别。

String 类不能被继承。因为其被 final 修饰，所以无法被继承。

StringBuffer，StringBuilder 拼接字符串，使用 append 比 String 效率高。因为 String 会隐式 new String 对象。

StringBuffer 主要方法都用 synchronized 修饰，是线程安全的；而 StringBuilder 不是。

### 面向对象

> 抽象类和接口的区别？
>
> 类可以继承多个类么？接口可以继承多个接口么？类可以实现多个接口么？

类只能继承一个类，但是可以实现多个接口。接口可以继承多个接口。

> 继承和聚合的区别在哪？

一般，能用聚合就别用继承。

### 反射

#### ⭐ 创建实例

> 反射创建实例有几种方式？

通过反射来创建实例对象主要有两种方式：

- 用 `Class` 对象的 `newInstance` 方法。
- 用 `Constructor` 对象的 `newInstance` 方法。

#### ⭐ 加载实例

> 加载实例有几种方式？
>
> Class.forName("className") 和 ClassLoader.laodClass("className") 有什么区别？

- `Class.forName("className")` 加载的是已经初始化到 JVM 中的类。
- `ClassLoader.laodClass("className")` 装载的是还没有初始化到 JVM 中的类。

#### ⭐⭐ 动态代理

> 动态代理有几种实现方式？有什么特点？
>
> JDK 动态代理和 CGLIB 动态代理有什么区别？

（1）JDK 方式

代理类与委托类实现同一接口，主要是通过代理类实现 `InvocationHandler` 并重写 `invoke` 方法来进行动态代理的，在 `invoke` 方法中将对方法进行处理。

JDK 动态代理特点：

- 优点：相对于静态代理模式，不需要硬编码接口，代码复用率高。
- 缺点：强制要求代理类实现 `InvocationHandler` 接口。

（2）CGLIB

CGLIB 底层，其实是借助了 ASM 这个强大的 Java 字节码框架去进行字节码增强操作。

CGLIB 动态代理特点：

优点：使用字节码增强，比 JDK 动态代理方式性能高。可以在运行时对类或者是接口进行增强操作，且委托类无需实现接口。

缺点：不能对 `final` 类以及 `final` 方法进行代理。

### JDK8

### 其他

#### ⭐ hashcode

> 有`==`运算符了，为什么还需要 equals 啊？
>
> 说一说你对 java.lang.Object 对象中 hashCode 和 equals 方法的理解。在什么场景下需
> 要重新实现这两个方法。
>
> 有没有可能 2 个不相等的对象有相同的 hashcode

（1）有`==`运算符了，为什么还需要 equals 啊？

equals 等价于`==`,而`==`运算符是判断两个对象是不是同一个对象，即他们的**地址是否相等**。而覆写 equals 更多的是追求两个对象在**逻辑上的相等**，你可以说是**值相等**，也可说是**内容相等**。

（2）说一说你对 java.lang.Object 对象中 hashCode 和 equals 方法的理解。在什么场景下需
要重新实现这两个方法。

在集合查找时，hashcode 能大大降低对象比较次数，提高查找效率！

（3）有没有可能 2 个不相等的对象有相同的 hashcode

有可能。

- 如果两个对象 equals，Java 运行时环境会认为他们的 hashcode 一定相等。
- 如果两个对象不 equals，他们的 hashcode 有可能相等。
- 如果两个对象 hashcode 相等，他们不一定 equals。
- 如果两个对象 hashcode 不相等，他们一定不 equals。

## IO

### NIO

> 什么是 NIO？
>
> NIO 和 BIO、AIO 有何差别？

### 序列化

#### ⭐ 序列化问题

> 序列化、反序列化有哪些问题？如何解决？

Java 的序列化能保证对象状态的持久保存，但是遇到一些对象结构复杂的情况还是难以处理，这里归纳一下：

- 当父类继承 `Serializable` 接口时，所有子类都可以被序列化。
- 子类实现了 `Serializable` 接口，父类没有，则父类的属性不会被序列化（不报错，数据丢失），子类的属性仍可以正确序列化。
- 如果序列化的属性是对象，则这个对象也必须实现 `Serializable` 接口，否则会报错。
- 在反序列化时，如果对象的属性有修改或删减，则修改的部分属性会丢失，但不会报错。
- 在反序列化时，如果 `serialVersionUID` 被修改，则反序列化时会失败。

## 容器

### List

#### ArrayList 和 LinkedList 有什么区别？

ArrayList 是数组链表，访问效率更高。

LinkedList 是双链表，数据有序存储。

### Map

请描述 HashMap 的实现原理？

## 并发

### 并发简介

#### 什么是进程？什么是线程？进程和线程的区别？

- 什么是进程？
  - 简言之，进程可视为一个正在运行的程序。
  - 进程是具有一定独立功能的程序关于某个数据集合上的一次运行活动。进程是操作系统进行资源分配的基本单位。
- 什么是线程？
  - 线程是操作系统进行调度的基本单位。
- 进程 vs. 线程
  - 一个程序至少有一个进程，一个进程至少有一个线程。
  - 线程比进程划分更细，所以执行开销更小，并发性更高。
  - 进程是一个实体，拥有独立的资源；而同一个进程中的多个线程共享进程的资源。

#### 并发（多线程）编程的好处是什么？

- 更有效率的利用多处理器核心
- 更快的响应时间
- 更好的编程模型

#### 并发一定比串行更快吗？

答：否。

要点：**创建线程和线程上下文切换有一定开销**。

说明：即使是单核处理器也支持多线程。CPU 通过给每个线程分配时间切片的算法来循环执行任务，当前任务执行一个时间片后会切换到下一个任务。但是，在切换前会保持上一个任务的状态，以便下次切换回这个任务时，可以再加载这个任务的状态。所以**任务从保存到再加载的过程就是一次上下文切换**。

引申

- 如何减少上下文切换？
  - 尽量少用锁
  - CAS 算法
  - 线程数要合理
  - 协程：在单线程中实现多任务调度，并在单线程中维持多个任务的切换

#### 如何让正在运行的线程暂停一段时间？

我们可以使用 `Thread` 类的 Sleep() 方法让线程暂停一段时间。

需要注意的是，这并不会让线程终止，一旦从休眠中唤醒线程，线程的状态将会被改变为 Runnable，并且根据线程调度，它将得到执行。

#### 什么是线程调度器(Thread Scheduler)和时间分片(Time Slicing)？

线程调度器是一个操作系统服务，它负责为 `Runnable` 状态的线程分配 CPU 时间。一旦我们创建一个线程并启动它，它的执行便依赖于线程调度器的实现。

时间分片是指将可用的 CPU 时间分配给可用的 `Runnable` 线程的过程。

分配 CPU 时间可以基于线程优先级或者线程等待的时间。线程调度并不受到 Java 虚拟机控制，所以由应用程序来控制它是更好的选择（也就是说不要让你的程序依赖于线程的优先级）。

#### 在多线程中，什么是上下文切换(context-switching)？

上下文切换是存储和恢复 CPU 状态的过程，它使得线程执行能够从中断点恢复执行。上下文切换是多任务操作系统和多线程环境的基本特征。

#### 如何确保线程安全？

- 原子类(atomic concurrent classes)
- 锁
- `volatile` 关键字
- 不变类和线程安全类

#### 什么是死锁(Deadlock)？如何分析和避免死锁？

死锁是指两个以上的线程永远相互阻塞的情况，这种情况产生至少需要两个以上的线程和两个以上的资源。

分析死锁，我们需要查看 Java 应用程序的线程转储。我们需要找出那些状态为 BLOCKED 的线程和他们等待的资源。每个资源都有一个唯一的 id，用这个 id 我们可以找出哪些线程已经拥有了它的对象锁。

避免嵌套锁，只在需要的地方使用锁和避免无限期等待是避免死锁的通常办法。

### 线程基础

#### Java 线程生命周期中有哪些状态？各状态之间如何切换？

![img](https://raw.githubusercontent.com/dunwu/images/dev/cs/java/javacore/concurrent/java-thread_1.png)

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

> 👉 参考阅读：[Java`Thread` Methods and `Thread` States](https://www.w3resource.com/java-tutorial/java-threadclass-methods-and-threadstates.php)
> 👉 参考阅读：[Java 线程的 5 种状态及切换(透彻讲解)](https://blog.csdn.net/pange1991/article/details/53860651)

#### 创建线程有哪些方式？这些方法各自利弊是什么？

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

> 👉 参考阅读：[Java 创建线程的三种方式及其对比](https://blog.csdn.net/longshengguoji/article/details/41126119)

#### 什么是 `Callable` 和 `Future`？什么是 `FutureTask`？

**什么是 `Callable` 和 `Future`？**

Java 5 在 concurrency 包中引入了 `Callable` 接口，它和 `Runnable` 接口很相似，但它可以返回一个对象或者抛出一个异常。

`Callable` 接口使用泛型去定义它的返回类型。`Executors` 类提供了一些有用的方法去在线程池中执行 `Callable` 内的任务。由于 `Callable` 任务是并行的，我们必须等待它返回的结果。`Future` 对象为我们解决了这个问题。在线程池提交 `Callable` 任务后返回了一个 `Future` 对象，使用它我们可以知道 `Callable` 任务的状态和得到 `Callable` 返回的执行结果。`Future` 提供了 `get()` 方法让我们可以等待 `Callable` 结束并获取它的执行结果。

**什么是 `FutureTask`？**

`FutureTask` 是 `Future` 的一个基础实现，我们可以将它同 `Executors` 使用处理异步任务。通常我们不需要使用 `FutureTask` 类，单当我们打算重写 `Future` 接口的一些方法并保持原来基础的实现是，它就变得非常有用。我们可以仅仅继承于它并重写我们需要的方法。阅读 Java `FutureTask` 例子，学习如何使用它。

> 👉 参考阅读：[Java 并发编程：Callable、Future 和 FutureTask](http://www.cnblogs.com/dolphin0520/p/3949310.html)

#### `start()` 和 `run()` 有什么区别？可以直接调用 `Thread` 类的 `run()` 方法么？

- `run()` 方法是线程的执行体。
- `start()` 方法负责启动线程，然后 JVM 会让这个线程去执行 `run()` 方法。

可以直接调用 `Thread` 类的 `run()` 方法么？

- 可以。但是如果直接调用 `Thread` 的 `run()` 方法，它的行为就会和普通的方法一样。
- 为了在新的线程中执行我们的代码，必须使用 `start()` 方法。

#### `sleep()`、`yield()`、`join()` 方法有什么区别？为什么 `sleep()` 和 `yield()` 方法是静态（static）的？

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

> 👉 参考阅读：[Java 线程中 yield 与 join 方法的区别](http://www.importnew.com/14958.html)
> 👉 参考阅读：[sleep()，wait()，yield()和 join()方法的区别](https://blog.csdn.net/xiangwanpeng/article/details/54972952)

#### Java 的线程优先级如何控制？高优先级的 Java 线程一定先执行吗？

**Java 中的线程优先级如何控制**

- Java 中的线程优先级的范围是 `[1,10]`，一般来说，高优先级的线程在运行时会具有优先权。可以通过 `thread.setPriority(Thread.MAX_PRIORITY)` 的方式设置，默认优先级为 `5`。

**高优先级的 Java 线程一定先执行吗**

- 即使设置了线程的优先级，也**无法保证高优先级的线程一定先执行**。
- 原因：这是因为 **Java 线程优先级依赖于操作系统的支持**，然而，不同的操作系统支持的线程优先级并不相同，不能很好的和 Java 中线程优先级一一对应。
- 结论：Java 线程优先级控制并不可靠。

#### 什么是守护线程？为什么要用守护线程？如何创建守护线程？

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

> 👉 参考阅读：[Java 中守护线程的总结](https://blog.csdn.net/shimiso/article/details/8964414)

#### 线程间是如何通信的？

当线程间是可以共享资源时，线程间通信是协调它们的重要的手段。`Object` 类中 `wait()`, `notify()` 和 `notifyAll()` 方法可以用于线程间通信关于资源的锁的状态。

> 👉 参考阅读：[Java 并发编程：线程间协作的两种方式：wait、notify、notifyAll 和 Condition](http://www.cnblogs.com/dolphin0520/p/3920385.html)

#### 为什么线程通信的方法 `wait()`, `notify()` 和 `notifyAll()` 被定义在 Object 类里？

Java 的每个对象中都有一个锁(monitor，也可以成为监视器) 并且 `wait()`、`notify()` 等方法用于等待对象的锁或者通知其他线程对象的监视器可用。在 Java 的线程中并没有可供任何对象使用的锁和同步器。这就是为什么这些方法是 Object 类的一部分，这样 Java 的每一个类都有用于线程间通信的基本方法

#### 为什么 `wait()`, `notify()` 和 `notifyAll()` 必须在同步方法或者同步块中被调用？

当一个线程需要调用对象的 `wait()` 方法的时候，这个线程必须拥有该对象的锁，接着它就会释放这个对象锁并进入等待状态直到其他线程调用这个对象上的 `notify()` 方法。同样的，当一个线程需要调用对象的 `notify()` 方法时，它会释放这个对象的锁，以便其他在等待的线程就可以得到这个对象锁。

由于所有的这些方法都需要线程持有对象的锁，这样就只能通过同步来实现，所以他们只能在同步方法或者同步块中被调用。

### 并发机制的底层实现

> 👉 参考阅读：[Java 并发核心机制](https://github.com/dunwu/javacore/blob/master/docs/concurrent/Java并发核心机制.md)

#### ⭐⭐⭐ `synchronized`

> `synchronized` 有什么作用？
>
> `synchronized` 的原理是什么？
>
> 同步方法和同步块，哪个更好？
>
> JDK1.6 对`synchronized` 做了哪些优化？
>
> 使用 `synchronized` 修饰静态方法和非静态方法有什么区别？

**作用**

**`synchronized` 可以保证在同一个时刻，只有一个线程可以执行某个方法或者某个代码块**。

`synchronized` 有 3 种应用方式：

- **同步实例方法** - 对于普通同步方法，锁是当前实例对象
- **同步静态方法** - 对于静态同步方法，锁是当前类的 `Class` 对象
- **同步代码块** - 对于同步方法块，锁是 `synchonized` 括号里配置的对象

**原理**

`synchronized` 经过编译后，会在同步块的前后分别形成 `monitorenter` 和 `monitorexit` 这两个字节码指令，这两个字节码指令都需要一个引用类型的参数来指明要锁定和解锁的对象。如果 `synchronized` 明确制定了对象参数，那就是这个对象的引用；如果没有明确指定，那就根据 `synchronized` 修饰的是实例方法还是静态方法，去对对应的对象实例或 `Class` 对象来作为锁对象。

`synchronized` 同步块对同一线程来说是可重入的，不会出现锁死问题。

`synchronized` 同步块是互斥的，即已进入的线程执行完成前，会阻塞其他试图进入的线程。

**优化**

Java 1.6 以后，`synchronized` 做了大量的优化，其性能已经与 `Lock` 、`ReadWriteLock` 基本上持平。

`synchronized` 的优化是将锁粒度分为不同级别，`synchronized` 会根据运行状态动态的由低到高调整锁级别（**偏向锁** -> **轻量级锁** -> **重量级锁**），以减少阻塞。

**同步方法 or 同步块？**

- 同步块是更好的选择。
- 因为它不会锁住整个对象（当然你也可以让它锁住整个对象）。同步方法会锁住整个对象，哪怕这个类中有多个不相关联的同步块，这通常会导致他们停止执行并需要等待获得这个对象上的锁。

#### ⭐ `volatile`

> `volatile` 有什么作用？
>
> `volatile` 的原理是什么？
>
> `volatile` 能代替锁吗？
>
> `volatile` 和 `synchronized` 的区别？

**`volatile` 无法替代 `synchronized` ，因为 `volatile` 无法保证操作的原子性**。

**作用**

被 `volatile` 关键字修饰的变量有两层含义：

- **保证了不同线程对这个变量进行操作时的可见性**，即一个线程修改了某个变量的值，这新值对其他线程来说是立即可见的。
- **禁止指令重排序**。

**原理**

观察加入 volatile 关键字和没有加入 volatile 关键字时所生成的汇编代码发现，**加入 `volatile` 关键字时，会多出一个 `lock` 前缀指令**。

**`lock` 前缀指令实际上相当于一个内存屏障**（也成内存栅栏），内存屏障会提供 3 个功能：

- 它确保指令重排序时不会把其后面的指令排到内存屏障之前的位置，也不会把前面的指令排到内存屏障的后面；即在执行到内存屏障这句指令时，在它前面的操作已经全部完成；
- 它会强制将对缓存的修改操作立即写入主存；
- 如果是写操作，它会导致其他 CPU 中对应的缓存行无效。

**`volatile` 和 `synchronized` 的区别？**

- `volatile` 本质是在告诉 jvm 当前变量在寄存器（工作内存）中的值是不确定的，需要从主存中读取； `synchronized` 则是锁定当前变量，只有当前线程可以访问该变量，其他线程被阻塞住。
- `volatile` 仅能使用在变量级别；synchronized 则可以使用在变量、方法、和类级别的。
- `volatile` 仅能实现变量的修改可见性，不能保证原子性；而 `synchronized` 则可以保证变量的修改可见性和原子性
- `volatile` 不会造成线程的阻塞；synchronized 可能会造成线程的阻塞。
- `volatile` 标记的变量不会被编译器优化；synchronized 标记的变量可以被编译器优化。

#### ⭐⭐ CAS

> 什么是 CAS？
>
> CAS 有什么作用？
>
> CAS 的原理是什么？
>
> CAS 的三大问题？

**作用**

**CAS（Compare and Swap）**，字面意思为**比较并交换**。CAS 有 3 个操作数，分别是：内存值 V，旧的预期值 A，要修改的新值 B。当且仅当预期值 A 和内存值 V 相同时，将内存值 V 修改为 B，否则什么都不做。

**原理**

Java 主要利用 `Unsafe` 这个类提供的 CAS 操作。`Unsafe` 的 CAS 依赖的是 JV M 针对不同的操作系统实现的 `Atomic::cmpxchg` 指令。

**三大问题**

1. **ABA 问题**：因为 CAS 需要在操作值的时候检查下值有没有发生变化，如果没有发生变化则更新，但是如果一个值原来是 A，变成了 B，又变成了 A，那么使用 CAS 进行检查时会发现它的值没有发生变化，但是实际上却变化了。ABA 问题的解决思路就是使用版本号。在变量前面追加上版本号，每次变量更新的时候把版本号加一，那么 A－B－A 就会变成 1A-2B－3A。
2. **循环时间长开销大**。自旋 CAS 如果长时间不成功，会给 CPU 带来非常大的执行开销。如果 JVM 能支持处理器提供的 pause 指令那么效率会有一定的提升，pause 指令有两个作用，第一它可以延迟流水线执行指令（de-pipeline）,使 CPU 不会消耗过多的执行资源，延迟的时间取决于具体实现的版本，在一些处理器上延迟时间是零。第二它可以避免在退出循环的时候因内存顺序冲突（memory order violation）而引起 CPU 流水线被清空（CPU pipeline flush），从而提高 CPU 的执行效率。
3. **只能保证一个共享变量的原子操作**。当对一个共享变量执行操作时，我们可以使用循环 CAS 的方式来保证原子操作，但是对多个共享变量操作时，循环 CAS 就无法保证操作的原子性，这个时候就可以用锁，或者有一个取巧的办法，就是把多个共享变量合并成一个共享变量来操作。比如有两个共享变量 i ＝ 2,j=a，合并一下 ij=2a，然后用 CAS 来操作 ij。从 Java1.5 开始 JDK 提供了 AtomicReference 类来保证引用对象之间的原子性，你可以把多个变量放在一个对象里来进行 CAS 操作。

#### ⭐ `ThreadLocal`

> `ThreadLocal` 有什么作用？
>
> `ThreadLocal` 的原理是什么？
>
> 如何解决 `ThreadLocal` 内存泄漏问题？

**作用**

**`ThreadLocal` 是一个存储线程本地副本的工具类**。

**原理**

`Thread` 类中维护着一个 `ThreadLocal.ThreadLocalMap` 类型的成员 `threadLocals`。这个成员就是用来存储当前线程独占的变量副本。

`ThreadLocalMap` 是 `ThreadLocal` 的内部类，它维护着一个 `Entry` 数组， `Entry` 用于保存键值对，其 key 是 `ThreadLocal` 对象，value 是传递进来的对象（变量副本）。 `Entry` 继承了 `WeakReference` ，所以是弱引用。

**内存泄漏问题**

ThreadLocalMap 的 `Entry` 继承了 `WeakReference`，所以它的 key （`ThreadLocal` 对象）是弱引用，而 value （变量副本）是强引用。

- 如果 `ThreadLocal` 对象没有外部强引用来引用它，那么 `ThreadLocal` 对象会在下次 GC 时被回收。
- 此时，`Entry` 中的 key 已经被回收，但是 value 由于是强引用不会被垃圾收集器回收。如果创建 `ThreadLocal` 的线程一直持续运行，那么 value 就会一直得不到回收，产生内存泄露。

那么如何避免内存泄漏呢？方法就是：**使用 `ThreadLocal` 的 `set` 方法后，显示的调用 `remove` 方法** 。

### 内存模型

#### 什么是 Java 内存模型

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

![img](https://raw.githubusercontent.com/dunwu/images/dev/cs/java/javacore/concurrent/java-memory-model_3.png)

> 👉 参考阅读：[全面理解 Java 内存模型](https://blog.csdn.net/suifeng3051/article/details/52611310)

### 同步容器和并发容器

> 👉 参考阅读：[Java 并发容器](https://github.com/dunwu/javacore/blob/master/docs/concurrent/Java并发和容器.md)

#### ⭐ 同步容器

> 什么是同步容器？
>
> 有哪些常见同步容器？
>
> 它们是如何实现线程安全的？
>
> 同步容器真的线程安全吗？

**类型**

`Vector`、`Stack`、`Hashtable`

**作用/原理**

同步容器的同步原理就是在方法上用 `synchronized` 修饰。 **`synchronized` 可以保证在同一个时刻，只有一个线程可以执行某个方法或者某个代码块**。

`synchronized` 的互斥同步会产生阻塞和唤醒线程的开销。显然，这种方式比没有使用 `synchronized` 的容器性能要差。

**线程安全**

同步容器真的绝对安全吗？

其实也未必。在做复合操作（非原子操作）时，仍然需要加锁来保护。常见复合操作如下：

- **迭代**：反复访问元素，直到遍历完全部元素；
- **跳转**：根据指定顺序寻找当前元素的下一个（下 n 个）元素；
- **条件运算**：例如若没有则添加等；

#### ⭐⭐⭐ ConcurrentHashMap

> 请描述 ConcurrentHashMap 的实现原理？
>
> ConcurrentHashMap 为什么放弃了分段锁？

基础数据结构原理和 `HashMap` 一样，JDK 1.7 采用 数组＋单链表；JDK 1.8 采用数组＋单链表＋红黑树。

并发安全特性的实现：

JDK 1.7：

- 使用分段锁，设计思路是缩小锁粒度，提高并发吞吐。
- 写数据时，会使用可重入锁去锁住分段（segment）。

JDK 1.8：

- 取消分段锁，直接采用 `transient volatile HashEntry<K,V>[] table` 保存数据，采用 table 数组元素作为锁，从而实现了对每一行数据进行加锁，进一步减少并发冲突的概率。
- 写数据时，使用是 CAS + `synchronized`。
  - 根据 key 计算出 hashcode 。
  - 判断是否需要进行初始化。
  - `f` 即为当前 key 定位出的 Node，如果为空表示当前位置可以写入数据，利用 CAS 尝试写入，失败则自旋保证成功。
  - 如果当前位置的 `hashcode == MOVED == -1`,则需要进行扩容。
  - 如果都不满足，则利用 synchronized 锁写入数据。
  - 如果数量大于 `TREEIFY_THRESHOLD` 则要转换为红黑树。

#### ⭐⭐ CopyOnWriteArrayList

> CopyOnWriteArrayList 的作用？
>
> CopyOnWriteArrayList 的原理？

**作用**

CopyOnWrite 字面意思为写入时复制。CopyOnWriteArrayList 是线程安全的 ArrayList。

**原理**

- 在 `CopyOnWriteAarrayList` 中，读操作不同步，因为它们在内部数组的快照上工作，所以多个迭代器可以同时遍历而不会相互阻塞（1,2,4）。
- 所有的写操作都是同步的。他们在备份数组（3）的副本上工作。写操作完成后，后备阵列将被替换为复制的阵列，并释放锁定。支持数组变得易变，所以替换数组的调用是原子（5）。
- 写操作后创建的迭代器将能够看到修改的结构（6,7）。
- 写时复制集合返回的迭代器不会抛出 ConcurrentModificationException，因为它们在数组的快照上工作，并且无论后续的修改（2,4）如何，都会像迭代器创建时那样完全返回元素。

<p align="center">
  <img src="https://raw.githubusercontent.com/dunwu/images/dev/cs/java/javacore/container/CopyOnWriteArrayList.png">
</p>

### 并发锁

> 👉 参考阅读：[Java 并发锁](https://github.com/dunwu/javacore/blob/master/docs/concurrent/Java锁.md)

#### ⭐⭐ 锁类型

> Java 中有哪些锁？
>
> 这些锁有什么特性？

**可重入锁**

- **`ReentrantLock` 、`ReentrantReadWriteLock` 是可重入锁**。这点，从其命名也不难看出。
- **`synchronized` 也是一个可重入锁**。

**公平锁与非公平锁**

- **`synchronized` 只支持非公平锁**。
- **`ReentrantLock` 、`ReentrantReadWriteLock`，默认是非公平锁，但支持公平锁**。

**独享锁与共享锁**

- **`synchronized` 、`ReentrantLock` 只支持独享锁**。
- **`ReentrantReadWriteLock` 其写锁是独享锁，其读锁是共享锁**。读锁是共享锁使得并发读是非常高效的，读写，写读 ，写写的过程是互斥的。

**悲观锁与乐观锁**

- 悲观锁在 Java 中的应用就是通过使用 `synchronized` 和 `Lock` 显示加锁来进行互斥同步，这是一种阻塞同步。

- 乐观锁在 Java 中的应用就是采用 CAS 机制（CAS 操作通过 `Unsafe` 类提供，但这个类不直接暴露为 API，所以都是间接使用，如各种原子类）。

**偏向锁、轻量级锁、重量级锁**

Java 1.6 以前，重量级锁一般指的是 `synchronized` ，而轻量级锁指的是 `volatile`。

Java 1.6 以后，针对 `synchronized` 做了大量优化，引入 4 种锁状态： 无锁状态、偏向锁、轻量级锁和重量级锁。锁可以单向的从偏向锁升级到轻量级锁，再从轻量级锁升级到重量级锁 。

**分段锁**

分段锁其实是一种锁的设计，并不是具体的一种锁。典型：JDK1.7 之前的 `ConcurrentHashMap`

**显示锁和内置锁**

- 内置锁：`synchronized`
- 显示锁：`ReentrantLock`、`ReentrantReadWriteLock` 等。

#### ⭐⭐ AQS

> 什么是 AQS？
>
> AQS 的作用是什么？
>
> AQS 的原理？

**作用**

`AbstractQueuedSynchronizer`（简称 **AQS**）是**队列同步器**，顾名思义，其主要作用是处理同步。它是并发锁和很多同步工具类的实现基石（如 `ReentrantLock`、`ReentrantReadWriteLock`、`Semaphore` 等）。

**AQS 提供了对独享锁与共享锁的支持**。

**原理**

（1）数据结构

- `state` - AQS 使用一个整型的 `volatile` 变量来 **维护同步状态**。
  - 这个整数状态的意义由子类来赋予，如`ReentrantLock` 中该状态值表示所有者线程已经重复获取该锁的次数，`Semaphore` 中该状态值表示剩余的许可数量。
- `head` 和 `tail` - AQS **维护了一个 `Node` 类型（AQS 的内部类）的双链表来完成同步状态的管理**。这个双链表是一个双向的 FIFO 队列，通过 `head` 和 `tail` 指针进行访问。当 **有线程获取锁失败后，就被添加到队列末尾**。

（2）获取独占锁

AQS 中使用 `acquire(int arg)` 方法获取独占锁，其大致流程如下：

1. 先尝试获取同步状态，如果获取同步状态成功，则结束方法，直接返回。
2. 如果获取同步状态不成功，AQS 会不断尝试利用 CAS 操作将当前线程插入等待同步队列的队尾，直到成功为止。
3. 接着，不断尝试为等待队列中的线程节点获取独占锁。

（3）释放独占锁

AQS 中使用 `release(int arg)` 方法释放独占锁，其大致流程如下：

1. 先尝试获取解锁线程的同步状态，如果获取同步状态不成功，则结束方法，直接返回。
2. 如果获取同步状态成功，AQS 会尝试唤醒当前线程节点的后继节点。

（4）获取共享锁

AQS 中使用 `acquireShared(int arg)` 方法获取共享锁。

`acquireShared` 方法和 `acquire` 方法的逻辑很相似，区别仅在于自旋的条件以及节点出队的操作有所不同。

成功获得共享锁的条件如下：

- `tryAcquireShared(arg)` 返回值大于等于 0 （这意味着共享锁的 permit 还没有用完）。
- 当前节点的前驱节点是头结点。

（5）释放共享锁

AQS 中使用 `releaseShared(int arg)` 方法释放共享锁。

`releaseShared` 首先会尝试释放同步状态，如果成功，则解锁一个或多个后继线程节点。释放共享锁和释放独享锁流程大体相似，区别在于：

对于独享模式，如果需要 SIGNAL，释放仅相当于调用头节点的 `unparkSuccessor`。

#### ⭐⭐ ReentrantLock

> 什么是 ReentrantLock？
>
> 什么是可重入锁？
>
> ReentrantLock 有什么用？
>
> ReentrantLock 原理？

**作用**

**`ReentrantLock` 提供了一组无条件的、可轮询的、定时的以及可中断的锁操作**

`ReentrantLock` 的特性如下：

- **`ReentrantLock` 提供了与 `synchronized` 相同的互斥性、内存可见性和可重入性**。
- `ReentrantLock` 支持公平锁和非公平锁（默认）两种模式。
- `ReentrantLock` 实现了 `Lock` 接口，支持了 `synchronized` 所不具备的**灵活性**。
  - `synchronized` 无法中断一个正在等待获取锁的线程
  - `synchronized` 无法在请求获取一个锁时无休止地等待

**原理**

`ReentrantLock` 基于其内部类 `ReentrantLock.Sync` 实现，`Sync` 继承自 AQS。它有两个子类：

- `ReentrantLock.FairSync` - 公平锁。
- `ReentrantLock.NonfairSync` - 非公平锁。

本质上，就是基于 AQS 实现。

#### ⭐ ReentrantReadWriteLock

> ReentrantReadWriteLock 是什么？
>
> ReentrantReadWriteLock 的作用？
>
> ReentrantReadWriteLock 的原理？

**作用**

`ReentrantReadWriteLock` 是一个**可重入的读写锁**。**`ReentrantReadWriteLock` 维护了一对读写锁，将读写锁分开，有利于提高并发效率**。

**原理**

`ReentrantReadWriteLock` 本质上也是基于 AQS 实现。有三个核心字段：

- `sync` - 内部类 `ReentrantReadWriteLock.Sync` 对象。与 `ReentrantLock` 类似，它有两个子类：`ReentrantReadWriteLock.FairSync` 和 `ReentrantReadWriteLock.NonfairSync` ，分别表示公平锁和非公平锁的实现。
- `readerLock` - 内部类 `ReentrantReadWriteLock.ReadLock` 对象，这是一把读锁。
- `writerLock` - 内部类 `ReentrantReadWriteLock.WriteLock` 对象，这是一把写锁。

#### ⭐ Condition

> Condition 有什么用？
>
> 使用 Lock 的线程，彼此如何通信？

**作用**

可以理解为，什么样的锁配什么样的钥匙。

**内置锁（`synchronized`）配合内置条件队列（`wait`、`notify`、`notifyAll` ），显式锁（`Lock`）配合显式条件队列（`Condition` ）**。

#### ⭐⭐ 死锁

> 如何避免死锁？

- 避免一个线程同时获取多个锁
- 避免一个线程在锁内同时占用多个资源，尽量保证每个锁只占用一个资源
- 尝试使用定时锁 lock.tryLock(timeout)，避免锁一直不能释放
- 对于数据库锁，加锁和解锁必须在一个数据库连接中里，否则会出现解锁失败的情况。

### 原子变量类

> 👉 参考阅读：[Java 原子类](https://github.com/dunwu/javacore/blob/master/docs/concurrent/Java原子类.md)

#### ⭐ 原子类简介

> 为什么要用原子类？
>
> 用过哪些原子类？

**作用**

常规的锁（`Lock`、`sychronized`）由于是阻塞式的，势必影响并发吞吐量。

`volatile` 号称轻量级的锁，但不能保证原子性。

为了兼顾原子性和锁的性能问题，所以引入了原子类。

**类型**

原子变量类可以分为 4 组：

- 基本类型
  - `AtomicBoolean` - 布尔类型原子类
  - `AtomicInteger` - 整型原子类
  - `AtomicLong` - 长整型原子类
- 引用类型
  - `AtomicReference` - 引用类型原子类
  - `AtomicMarkableReference` - 带有标记位的引用类型原子类
  - `AtomicStampedReference` - 带有版本号的引用类型原子类
- 数组类型
  - `AtomicIntegerArray` - 整形数组原子类
  - `AtomicLongArray` - 长整型数组原子类
  - `AtomicReferenceArray` - 引用类型数组原子类
- 属性更新器类型
  - `AtomicIntegerFieldUpdater` - 整型字段的原子更新器。
  - `AtomicLongFieldUpdater` - 长整型字段的原子更新器。
  - `AtomicReferenceFieldUpdater` - 原子更新引用类型里的字段。

#### ⭐ 原子类的原理

1. 处理器实现原子操作：使用总线锁保证原子性，使用缓存锁保证原子性（修改内存地址，缓存一致性机制：阻止同时修改由 2 个以上的处理器缓存的内存区域数据）
2. JAVA 实现原子操作：循环使用 CAS （自旋 CAS）实现原子操作

### 并发工具类

> 👉 参考阅读：[Java 并发工具类](https://github.com/dunwu/javacore/blob/master/docs/concurrent/Java并发工具类.md)

#### ⭐ CountDownLatch

> CountDownLatch 作用？
>
> CountDownLatch 原理？

**作用**

字面意思为 **递减计数锁**。用于控制一个或者多个线程等待多个线程。

`CountDownLatch` 维护一个计数器 count，表示需要等待的事件数量。`countDown` 方法递减计数器，表示有一个事件已经发生。调用 `await` 方法的线程会一直阻塞直到计数器为零，或者等待中的线程中断，或者等待超时。

![img](https://raw.githubusercontent.com/dunwu/images/dev/cs/java/javacore/concurrent/CountDownLatch.png)

**原理**

`CountDownLatch` 是基于 AQS(`AbstractQueuedSynchronizer`) 实现的。

#### ⭐ CyclicBarrier

> CyclicBarrier 有什么用？
>
> CyclicBarrier 的原理是什么？
>
> CyclicBarrier 和 CountDownLatch 有什么区别？

**作用**

字面意思是 **循环栅栏**。**`CyclicBarrier` 可以让一组线程等待至某个状态（遵循字面意思，不妨称这个状态为栅栏）之后再全部同时执行**。之所以叫循环栅栏是因为：当所有等待线程都被释放以后，`CyclicBarrier` 可以被重用。

`CyclicBarrier` 维护一个计数器 count。每次执行 `await` 方法之后，count 加 1，直到计数器的值和设置的值相等，等待的所有线程才会继续执行。

![img](https://raw.githubusercontent.com/dunwu/images/dev/cs/java/javacore/concurrent/CyclicBarrier.png)

**原理**

`CyclicBarrier` 是基于 `ReentrantLock` 和 `Condition` 实现的。

**区别**

`CyclicBarrier` 和 `CountDownLatch` 都可以用来让一组线程等待其它线程。与 `CyclicBarrier` 不同的是，`CountdownLatch` 不能重用。

#### ⭐ Semaphore

> Semaphore 作用？

**作用**

字面意思为 **信号量**。`Semaphore` 用来控制同时访问某个特定资源的操作数量，或者同时执行某个指定操作的数量。

`Semaphore` 管理着一组虚拟的许可（permit），permit 的初始数量可通过构造方法来指定。每次执行 `acquire` 方法可以获取一个 permit，如果没有就等待；而 `release` 方法可以释放一个 permit。

- `Semaphore` 可以用于实现资源池，如数据库连接池。
- `Semaphore` 可以用于将任何一种容器变成有界阻塞容器。

![img](https://raw.githubusercontent.com/dunwu/images/dev/cs/java/javacore/concurrent/Semaphore.png)

### 线程池

> 👉 参考阅读：[Java 线程池](https://github.com/dunwu/javacore/blob/master/docs/concurrent/Java线程池.md)

#### ⭐⭐ ThreadPoolExecutor

> `ThreadPoolExecutor` 有哪些参数，各自有什么用？
>
> `ThreadPoolExecutor` 工作原理？

**原理**

![img](https://raw.githubusercontent.com/dunwu/images/dev/cs/java/javacore/concurrent/java-thread-pool_1.png)

**参数**

`java.uitl.concurrent.ThreadPoolExecutor` 类是 `Executor` 框架中最核心的一个类。

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
  - `CallerRunsPolicy` - 只用调用者所在的线程来运行任务。
  - 如果以上策略都不能满足需要，也可以通过实现 `RejectedExecutionHandler` 接口来定制处理策略。如记录日志或持久化不能处理的任务。

#### ⭐ Executors

> Executors 提供了哪些内置的线程池？
>
> 这些线程池各自有什么特性？适合用于什么场景？

Executors 为 Executor，ExecutorService，ScheduledExecutorService，ThreadFactory 和 `Callable` 类提供了一些工具方法。

（1）newSingleThreadExecutor

**创建一个单线程的线程池**。

只会创建唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。 **如果这个唯一的线程因为异常结束，那么会有一个新的线程来替代它** 。

单工作线程最大的特点是：**可保证顺序地执行各个任务**。

（2）newFixedThreadPool

**创建一个固定大小的线程池**。

**每次提交一个任务就会新创建一个工作线程，如果工作线程数量达到线程池最大线程数，则将提交的任务存入到阻塞队列中**。

`FixedThreadPool` 是一个典型且优秀的线程池，它具有线程池提高程序效率和节省创建线程时所耗的开销的优点。但是，在线程池空闲时，即线程池中没有可运行任务时，它不会释放工作线程，还会占用一定的系统资源。

（3）newCachedThreadPool

**创建一个可缓存的线程池**。

- 如果线程池大小超过处理任务所需要的线程数，就会回收部分空闲的线程；
- 如果长时间没有往线程池中提交任务，即如果工作线程空闲了指定的时间（默认为 1 分钟），则该工作线程将自动终止。终止后，如果你又提交了新的任务，则线程池重新创建一个工作线程。
- 此线程池不会对线程池大小做限制，线程池大小完全依赖于操作系统（或者说 JVM）能够创建的最大线程大小。 因此，使用 `CachedThreadPool` 时，一定要注意控制任务的数量，否则，由于大量线程同时运行，很有会造成系统瘫痪。

（4）newScheduleThreadPool

创建一个大小无限的线程池。此线程池支持定时以及周期性执行任务的需求。

## JVM

### 内存管理

### OOM

## 参考资料

- **书籍**
  - [《Java 并发编程实战》](https://book.douban.com/subject/10484692/)
  - [《Java 并发编程的艺术》](https://book.douban.com/subject/26591326/)
  - [《深入理解 Java 虚拟机》](https://book.douban.com/subject/34907497/)
- **文章**
  - [Java 线程面试题 Top 50](http://www.importnew.com/12773.html)
  - [Java 多线程和并发基础面试问答](http://ifeve.com/java-multi-threading-concurrency-interview-questions-with-answers/)
  - [进程和线程关系及区别](https://blog.csdn.net/yaosiming2011/article/details/44280797)
  - [JavaThread Methods and Thread States](https://www.w3resource.com/java-tutorial/java-threadclass-methods-and-threadstates.php)
  - [Java 线程的 5 种状态及切换(透彻讲解)](https://blog.csdn.net/pange1991/article/details/53860651)
  - [Java 中守护线程的总结](https://blog.csdn.net/shimiso/article/details/8964414)
  - [Java 创建线程的三种方式及其对比](https://blog.csdn.net/longshengguoji/article/details/41126119)
  - [Java 线程的 5 种状态及切换(透彻讲解)](https://blog.csdn.net/pange1991/article/details/53860651)
  - [Java 线程方法 join 的简单总结](https://www.cnblogs.com/lcplcpjava/p/6896904.html)
  - [Java 并发编程：线程间协作的两种方式：wait、notify、notifyAll 和 Condition](http://www.cnblogs.com/dolphin0520/p/3920385.html)
  - [Java 并发编程：volatile 关键字解析](http://www.cnblogs.com/dolphin0520/p/3920373.html)
  - [Java 并发编程：Callable、Future 和 FutureTask](http://www.cnblogs.com/dolphin0520/p/3949310.html)
  - [Java 并发编程：线程池的使用](http://www.cnblogs.com/dolphin0520/p/3932921.html)
  - [Java 并发编程](https://www.jianshu.com/p/0256c2995cec)
