<p align="center">
    <a href="https://dunwu.github.io/javacore/" target="_blank" rel="noopener noreferrer">
        <img src="http://dunwu.test.upcdn.net/common/logo/dunwu-logo.png" alt="logo" width="150px"/>
    </a>
</p>

<p align="center">
    <img src="https://badgen.net/github/license/dunwu/javacore" alt="license">
    <img src="https://travis-ci.com/dunwu/javacore.svg?branch=master" alt="build">
</p>

<h1 align="center">JavaCore</h1>

> ☕ **JavaCore** 是一个 Java 核心技术教程。
>
> - 🔁 项目同步维护：[Github](https://github.com/dunwu/javacore/) | [Gitee](https://gitee.com/turnon/javacore/)
> - 📖 电子书阅读：[Github Pages](https://dunwu.github.io/javacore/) | [Gitee Pages](http://turnon.gitee.io/javacore/)

|           1️⃣            |           2️⃣            |           3️⃣            |           4️⃣            |         5️⃣         |             6️⃣              |
| :---------------------: | :---------------------: | :---------------------: | :---------------------: | :----------------: | :-------------------------: |
| [Java 基础](#java-基础) | [Java 高级](#java-高级) | [Java 容器](#java-容器) | [Java 并发](#java-并发) | [JavaIO](#java-io) | [Java 虚拟机](#java-虚拟机) |

## 📖 内容

> [Java 面试题集 💯](docs/java-interview.md)

### Java 基础

> [Java 基础](docs/basics) 总结 Java 的一些基础特性。

- [Java 开发环境](docs/basics/java-develop-env.md)
- [Java 基础语法特性](docs/basics/java-basic-grammar.md)
- [Java 基本数据类型](docs/basics/java-data-type.md)
- [Java 类和对象](docs/basics/java-class.md)
- [Java 方法](docs/basics/java-method.md)
- [Java 数组](docs/basics/java-array.md)
- [Java 枚举](docs/basics/java-enum.md)
- [Java 控制语句](docs/basics/java-control-statement.md)
- [Java 异常](docs/basics/java-exception.md)
- [Java 泛型](docs/basics/java-generic.md)
- [Java 反射](docs/basics/java-reflection.md)
- [Java 注解](docs/basics/java-annotation.md)

### Java 高级

> [Java 高级](docs/advanced) 总结 Java 的一些高级特性。

- [Java 正则](docs/advanced/java-regex.md)
- [Java 编码和加密](docs/advanced/java-crypto.md)
- [Java 本地化](docs/advanced/java-locale.md)
- [Java JDK8](docs/advanced/jdk8.md)

### Java 容器

> [Java 容器](docs/container) 涉及许多数据结构知识点，所以设立专题进行总结。

![img](http://dunwu.test.upcdn.net/snap/20200221175550.png)

- [Java 容器简介](docs/container/java-container.md) - 关键词：`Collection`、`泛型`、`Iterable`、`Iterator`、`Comparable`、`Comparator`、`Cloneable`、`fail-fast`
- [Java 容器之 List](docs/container/java-container-list.md) - 关键词：`List`、`ArrayList`、`LinkedList`
- [Java 容器之 Map](docs/container/java-container-map.md) - 关键词：`Map`、`HashMap`、`TreeMap`、`LinkedHashMap`、`WeakHashMap`
- [Java 容器之 Set](docs/container/java-container-set.md) - 关键词：`Set`、`HashSet`、`TreeSet`、`LinkedHashSet`、`EmumSet`
- [Java 容器之 Queue](docs/container/java-container-queue.md) - 关键词：`Queue`、`Deque`、`ArrayDeque`、`LinkedList`、`PriorityQueue`

### Java 并发

> [Java 并发](docs/concurrent) 总结、整理 Java 并发编程相关知识点。

![img](http://dunwu.test.upcdn.net/snap/20200221175827.png)

- [Java 并发简介](docs/concurrent/java-concurrent-introduction.md) - 关键词：`进程`、`线程`
- [Java 线程基础](docs/concurrent/java-thread.md) - 关键词：`Thread`、`Runnable`、`Callable`、`Future`
- [Java 并发核心机制](docs/concurrent/java-concurrent-basic-mechanism.md) - 关键词：`synchronized`、`volatile`、`CAS`、`ThreadLocal`
- [Java 并发锁](docs/concurrent/java-lock.md) - 关键词：`AQS`、`ReentrantLock`、`ReentrantReadWriteLock`、`Condition`
- [Java 原子类](docs/concurrent/java-atomic-class.md) - 关键词：`CAS`、`Atomic`
- [Java 并发容器](docs/concurrent/java-concurrent-container.md) - 关键词：`ConcurrentHashMap`、`CopyOnWriteArrayList`
- [Java 线程池](docs/concurrent/java-thread-pool.md) - 关键词：`Executor`、`ExecutorService`、`ThreadPoolExecutor`、`Executors`
- [Java 并发工具类](docs/concurrent/java-concurrent-tools.md) - 关键词：`CountDownLatch`、`CyclicBarrier`、`Semaphore`
- [Java 内存模型](docs/concurrent/java-memory-model.md) - 关键词：`JMM`、`原子性`、`可见性`、`有序性`、`Happens-Before`、`内存屏障`

### Java IO

> Java IO 知识点

- [Java 基础 IO 类](docs/io/java-io-base.md) - 关键词：`File`、`RandomAccessFile`、`System`、`Scanner`
- [Java BIO](docs/io/java-bio.md) - 关键词：`InputStream`、`OutputStream`、`Reader`、`Writer`
- [Java NIO](docs/io/java-nio.md) - 关键词：`Channel`、`Buffer`、`Selector`、`多路复用`
- [Java 序列化](docs/io/java-serialization.md) - 关键词：`Serializable`、`Externalizable`、`ObjectInputStream`、`ObjectOutputStream`、`transient`
- [Java 网络编程](docs/io/java-net.md) - 关键词：`Socket`、`ServerSocket`、`DatagramPacket`、`DatagramSocket`

### Java 虚拟机

> [Java 虚拟机](docs/jvm) 记录了 JVM 的基本机制。

![img](http://dunwu.test.upcdn.net/snap/20200221180028.png)

- [JVM 内存区域](docs/jvm/jvm-memory.md) - 关键词：程序计数器、虚拟机栈、本地方法栈、堆、方法区、运行时常量池、直接内存、`OutOfMemoryError`、`StackOverflowError`
- [JVM 垃圾收集](docs/jvm/jvm-gc.md) - 关键词：`GC Roots`、`Serial`、`Parallel`、`CMS`、`G1`、`Minor GC`、`Full GC`
- [JVM 字节码](docs/jvm/jvm-bytecode.md) - 关键词：`bytecode`、`asm`、`javassist`
- [JVM 类加载](docs/jvm/jvm-class-loader.md) - 关键词：类加载过程、`ClassLoader`、双亲委派、
- [JVM 工具](docs/jvm/jvm-tools.md) - 关键词：`jps`、`jstat`、`jmap` 、`jstack`、`jhat`、`jinfo`、`jconsole`、`jvisualvm`、`MAT`
- [JVM 实战](docs/jvm/jvm-action.md) - 关键词：VM 参数、分析、调优
- [Java 故障排查](docs/jvm/troubleshooting.md) - 关键词：CPU、内存、磁盘、网络、GC

## 📚 资料

- **书籍**
  - Java 四大名著
    - [《Java 编程思想（Thinking in java）》](https://item.jd.com/10058164.html)
    - [《Java 核心技术 卷 I 基础知识》](https://item.jd.com/12759308.html)
    - [《Java 核心技术 卷 II 高级特性》](https://item.jd.com/12791368.html)
    - [《Effective Java》](https://item.jd.com/12507084.html)
  - Java 并发
    - [《Java 并发编程实战》](https://item.jd.com/10922250.html)
    - [《Java 并发编程的艺术》](https://item.jd.com/11740734.html)
  - Java 虚拟机
    - [《深入理解 Java 虚拟机》](https://item.jd.com/11252778.html)
  - Java 入门
    - [《O'Reilly：Head First Java》](https://item.jd.com/10100190.html)
    - [《Java 从入门到精通》](https://item.jd.com/12555860.html)
    - [《疯狂 Java 讲义》](https://item.jd.com/12518025.html)
  - 其他
    - 《Head First 设计模式》
    - [《 Java 网络编程》](https://item.jd.com/11544991.html)
    - [《Java 加密与解密的艺术》](https://item.jd.com/26122568270.html)
    - [《Java 程序员面试宝典》](https://item.jd.com/11772823.html)
- **教程、社区**
  - [Runoob Java 教程](https://www.runoob.com/java/java-tutorial.html)
  - [JavaGuide](https://github.com/Snailclimb/JavaGuide)
  - [Java](https://github.com/TheAlgorithms/Java)
  - [java-design-patterns](https://github.com/iluwatar/java-design-patterns)
  - [advanced-java](https://github.com/doocs/advanced-java)

## 🚪 传送

◾ 🏠 [JAVACORE 首页](https://github.com/dunwu/javacore) ◾ 🎯 [我的博客](https://github.com/dunwu/blog) ◾

> 你可能会感兴趣：

- [Java 教程](https://github.com/dunwu/java-tutorial) 📚
- [JavaCore 教程](https://dunwu.github.io/javacore/) 📚
- [JavaTech 教程](https://dunwu.github.io/javatech/) 📚
- [Spring 教程](https://dunwu.github.io/spring-tutorial/) 📚
- [Spring Boot 教程](https://dunwu.github.io/spring-boot-tutorial/) 📚
- [数据库教程](https://dunwu.github.io/db-tutorial/) 📚
- [数据结构和算法教程](https://dunwu.github.io/algorithm-tutorial/) 📚
- [Linux 教程](https://dunwu.github.io/linux-tutorial/) 📚
- [Nginx 教程](https://github.com/dunwu/nginx-tutorial/) 📚
