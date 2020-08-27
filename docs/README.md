---
home: true
heroImage: http://dunwu.test.upcdn.net/common/logo/dunwu-logo.png
heroText: JAVACORE
tagline: ☕ JavaCore 是一个 Java 核心技术教程。
actionLink: /
footer: CC-BY-SA-4.0 Licensed | Copyright © 2018-Now Dunwu
---

# JavaCore

> ☕ **JavaCore** 是一个 Java 核心技术教程。
>
> - 🔁 项目同步维护：[Github](https://github.com/dunwu/javacore/) | [Gitee](https://gitee.com/turnon/javacore/)
> - 📖 电子书阅读：[Github Pages](https://dunwu.github.io/javacore/) | [Gitee Pages](http://turnon.gitee.io/javacore/)

|           1️⃣            |           2️⃣            |           3️⃣            |           4️⃣            |         5️⃣         |             6️⃣              |
| :---------------------: | :---------------------: | :---------------------: | :---------------------: | :----------------: | :-------------------------: |
| [Java 基础](#java-基础) | [Java 高级](#java-高级) | [Java 容器](#java-容器) | [Java 并发](#java-并发) | [JavaIO](#java-io) | [Java 虚拟机](#java-虚拟机) |

## 📖 内容

> [Java 面试题集 💯](java-interview.md)

### [Java 基础](basics)

- [Java 开发环境](basics/java-develop-env.md)
- [Java 基础语法特性](basics/java-basic-grammar.md)
- [Java 基本数据类型](basics/java-data-type.md)
- [Java String 类型](basics/java-string.md)
- [Java 面向对象](basics/java-oop.md)
- [Java 方法](basics/java-method.md)
- [Java 数组](basics/java-array.md)
- [Java 枚举](basics/java-enum.md)
- [Java 控制语句](basics/java-control-statement.md)
- [Java 异常](basics/java-exception.md)
- [Java 泛型](basics/java-generic.md)
- [Java 反射](basics/java-reflection.md)
- [Java 注解](basics/java-annotation.md)

### [Java 高级](advanced)

- [Java 正则](advanced/java-regex.md)
- [Java 编码和加密](advanced/java-crypto.md)
- [Java 本地化](advanced/java-locale.md)
- [JavaJDK8](advanced/jdk8.md) - 关键词：`Stream`、`lambda`、`Optional`、`@FunctionalInterface`

### [Java 容器](container)

![img](http://dunwu.test.upcdn.net/snap/20200221175550.png)

- [Java 容器简介](container/java-container.md) - 关键词：`Collection`、`泛型`、`Iterable`、`Iterator`、`Comparable`、`Comparator`、`Cloneable`、`fail-fast`
- [Java 容器之 List](container/java-container-list.md) - 关键词：`List`、`ArrayList`、`LinkedList`
- [Java 容器之 Map](container/java-container-map.md) - 关键词：`Map`、`HashMap`、`TreeMap`、`LinkedHashMap`、`WeakHashMap`
- [Java 容器之 Set](container/java-container-set.md) - 关键词：`Set`、`HashSet`、`TreeSet`、`LinkedHashSet`、`EmumSet`
- [Java 容器之 Queue](container/java-container-queue.md) - 关键词：`Queue`、`Deque`、`ArrayDeque`、`LinkedList`、`PriorityQueue`

### [Java 并发](concurrent)

![img](http://dunwu.test.upcdn.net/snap/20200221175827.png)

- [Java 并发简介](concurrent/java-concurrent-introduction.md) - 关键词：`进程`、`线程`
- [Java 线程基础](concurrent/java-thread.md) - 关键词：`Thread`、`Runnable`、`Callable`、`Future`
- [Java 并发核心机制](concurrent/java-concurrent-basic-mechanism.md) - 关键词：`synchronized`、`volatile`、`CAS`、`ThreadLocal`
- [Java 并发锁](concurrent/java-lock.md) - 关键词：`AQS`、`ReentrantLock`、`ReentrantReadWriteLock`、`Condition`
- [Java 原子类](concurrent/java-atomic-class.md) - 关键词：`CAS`、`Atomic`
- [Java 并发容器](concurrent/java-concurrent-container.md) - 关键词：`ConcurrentHashMap`、`CopyOnWriteArrayList`
- [Java 线程池](concurrent/java-thread-pool.md) - 关键词：`Executor`、`ExecutorService`、`ThreadPoolExecutor`、`Executors`
- [Java 并发工具类](concurrent/java-concurrent-tools.md) - 关键词：`CountDownLatch`、`CyclicBarrier`、`Semaphore`
- [Java 内存模型](concurrent/java-memory-model.md) - 关键词：`JMM`、`原子性`、`可见性`、`有序性`、`Happens-Before`

### [Java IO](io)

![img](http://dunwu.test.upcdn.net/snap/20200630195043.png)

- [Java BIO](io/java-bio.md) - 关键词：`InputStream`、`OutputStream`、`Reader`、`Writer`
- [Java NIO](io/java-nio.md) - 关键词：`Channel`、`Buffer`、`Selector`、`多路复用`
- [Java 序列化](io/java-serialization.md) - 关键词：`Serializable`、`Externalizable`、`ObjectInputStream`、`ObjectOutputStream`、`transient`
- [Java 网络编程](io/java-net.md) - 关键词：`Socket`、`ServerSocket`、`DatagramPacket`、`DatagramSocket`
- [Java IO 工具类](io/java-io-tool.md) - 关键词：`File`、`RandomAccessFile`、`System`、`Scanner`

### [Java 虚拟机](jvm)

![img](http://dunwu.test.upcdn.net/snap/20200628154803.png)

- [JVM 体系结构](jvm/jvm-architecture.md)
- [JVM 内存区域](jvm/jvm-memory.md) - 关键词：`程序计数器`、`虚拟机栈`、`本地方法栈`、`堆`、`方法区`、`运行时常量池`、`直接内存`、`OutOfMemoryError`、`StackOverflowError`
- [JVM 垃圾收集](jvm/jvm-gc.md) - 关键词：`GC Roots`、`Serial`、`Parallel`、`CMS`、`G1`、`Minor GC`、`Full GC`
- [JVM 字节码](jvm/jvm-bytecode.md) - 关键词：`bytecode`、`asm`、`javassist`
- [JVM 类加载](jvm/jvm-class-loader.md) - 关键词：`ClassLoader`、`双亲委派`
- [JVM 实战](jvm/jvm-action.md) - 关键词：`配置`、`调优`
- [JVM 命令行工具](jvm/jvm-cli-tools.md) - 关键词：`jps`、`jstat`、`jmap` 、`jstack`、`jhat`、`jinfo`
- [JVM GUI 工具](jvm/jvm-gui-tools.md) - 关键词：`jconsole`、`jvisualvm`、`MAT`、`JProfile`、`Arthas`
- [TroubleShooting](jvm/trouble-shooting.md) - 关键词：`CPU`、`内存`、`磁盘`、`网络`、`GC`

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
    - [《Head First 设计模式》](https://item.jd.com/10100236.html)
    - [《Java 网络编程》](https://item.jd.com/11544991.html)
    - [《Java 加密与解密的艺术》](https://item.jd.com/26122568270.html)
    - [《Java 程序员面试宝典》](https://item.jd.com/11772823.html)
- **教程、社区**
  - [Runoob Java 教程](https://www.runoob.com/java/java-tutorial.html)
  - [java-design-patterns](https://github.com/iluwatar/java-design-patterns)
  - [Java](https://github.com/TheAlgorithms/Java)
  - [Java 核心技术面试精讲](https://time.geekbang.org/column/intro/82)
  - [Java 性能调优实战](https://time.geekbang.org/column/intro/100028001)
  - [Java 业务开发常见错误 100 例](https://time.geekbang.org/column/intro/100047701)
  - [深入拆解 Java 虚拟机](https://time.geekbang.org/column/intro/100010301)
  - [Java 并发编程实战](https://time.geekbang.org/column/intro/100023901)
- **面试**
  - [CS-Notes](https://github.com/CyC2018/CS-Notes)
  - [JavaGuide](https://github.com/Snailclimb/JavaGuide)
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
