<p align="center">
    <a href="https://dunwu.github.io/javacore/" target="_blank" rel="noopener noreferrer">
        <img src="https://raw.githubusercontent.com/dunwu/images/dev/common/dunwu-logo-200.png" alt="logo" width="150px"/>
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

|               1️⃣                |               2️⃣                |           3️⃣            |           4️⃣            |         5️⃣         |             6️⃣              |
| :-----------------------------: | :-----------------------------: | :---------------------: | :---------------------: | :----------------: | :-------------------------: |
| [Java 基础特性](#java-基础特性) | [Java 高级特性](#java-高级特性) | [Java 容器](#java-容器) | [Java 并发](#java-并发) | [JavaIO](#java-io) | [Java 虚拟机](#java-虚拟机) |

## 📖 内容

> [Java 面试题集 💯](docs/java-interview.md)

### [Java 基础特性](docs/basics)

- [Java 开发环境](docs/basics/java-develop-env.md)
- [Java 基础语法特性](docs/basics/java-basic-grammar.md)
- [Java 基本数据类型](docs/basics/java-data-type.md)
- [Java String 类型](docs/basics/java-string.md)
- [Java 面向对象](docs/basics/java-oop.md)
- [Java 方法](docs/basics/java-method.md)
- [Java 数组](docs/basics/java-array.md)
- [Java 枚举](docs/basics/java-enum.md)
- [Java 控制语句](docs/basics/java-control-statement.md)
- [Java 异常](docs/basics/java-exception.md)
- [Java 泛型](docs/basics/java-generic.md)
- [Java 反射](docs/basics/java-reflection.md)
- [Java 注解](docs/basics/java-annotation.md)

### [Java 高级特性](docs/advanced)

- [Java 正则从入门到精通](docs/advanced/java-regex.md) - 关键词：`Pattern`、`Matcher`、`捕获与非捕获`、`反向引用`、`零宽断言`、`贪婪与懒惰`、`元字符`、`DFA`、`NFA`
- [Java 编码和加密](docs/advanced/java-crypto.md) - 关键词：`Base64`、`消息摘要`、`数字签名`、`对称加密`、`非对称加密`、`MD5`、`SHA`、`HMAC`、`AES`、`DES`、`DESede`、`RSA`
- [Java 本地化](docs/advanced/java-locale.md)
- [Java JDK8](docs/advanced/jdk8.md) - 关键词：`Stream`、`lambda`、`Optional`、`@FunctionalInterface`

### [Java 容器](docs/container)

![img](https://raw.githubusercontent.com/dunwu/images/dev/snap/20200221175550.png)

- [Java 容器简介](docs/container/java-container.md) - 关键词：`Collection`、`泛型`、`Iterable`、`Iterator`、`Comparable`、`Comparator`、`Cloneable`、`fail-fast`
- [Java 容器之 List](docs/container/java-container-list.md) - 关键词：`List`、`ArrayList`、`LinkedList`
- [Java 容器之 Map](docs/container/java-container-map.md) - 关键词：`Map`、`HashMap`、`TreeMap`、`LinkedHashMap`、`WeakHashMap`
- [Java 容器之 Set](docs/container/java-container-set.md) - 关键词：`Set`、`HashSet`、`TreeSet`、`LinkedHashSet`、`EmumSet`
- [Java 容器之 Queue](docs/container/java-container-queue.md) - 关键词：`Queue`、`Deque`、`ArrayDeque`、`LinkedList`、`PriorityQueue`

### [Java 并发](docs/concurrent)

![img](https://raw.githubusercontent.com/dunwu/images/dev/snap/20200221175827.png)

- [Java 并发简介](docs/concurrent/Java并发简介.md) - 关键词：`进程`、`线程`、`安全性`、`活跃性`、`性能`、`死锁`、`饥饿`、`上下文切换`
- [Java 线程基础](docs/concurrent/Java线程基础.md) - 关键词：`Thread`、`Runnable`、`Callable`、`Future`、`wait`、`notify`、`notifyAll`、`join`、`sleep`、`yeild`、`线程状态`、`线程通信`
- [Java 并发核心机制](docs/concurrent/Java并发核心机制.md) - 关键词：`synchronized`、`volatile`、`CAS`、`ThreadLocal`
- [Java 并发锁](docs/concurrent/Java锁.md) - 关键词：`AQS`、`ReentrantLock`、`ReentrantReadWriteLock`、`Condition`
- [Java 原子类](docs/concurrent/Java原子类.md) - 关键词：`CAS`、`Atomic`
- [Java 并发容器](docs/concurrent/Java并发和容器.md) - 关键词：`ConcurrentHashMap`、`CopyOnWriteArrayList`
- [Java 线程池](docs/concurrent/Java线程池.md) - 关键词：`Executor`、`ExecutorService`、`ThreadPoolExecutor`、`Executors`
- [Java 并发工具类](docs/concurrent/Java并发工具类.md) - 关键词：`CountDownLatch`、`CyclicBarrier`、`Semaphore`
- [Java 内存模型](docs/concurrent/Java内存模型.md) - 关键词：`JMM`、`volatile`、`synchronized`、`final`、`Happens-Before`、`内存屏障`
- [Java Fork Join](docs/concurrent/ForkJoin框架.md) - 关键词：

### [Java IO](docs/io)

![img](https://raw.githubusercontent.com/dunwu/images/dev/snap/20200630205329.png)

- [Java IO 模型](docs/io/java-io.md) - 关键词：`InputStream`、`OutputStream`、`Reader`、`Writer`、`阻塞`
- [Java NIO](docs/io/java-nio.md) - 关键词：`Channel`、`Buffer`、`Selector`、`非阻塞`、`多路复用`
- [Java 序列化](docs/io/java-serialization.md) - 关键词：`Serializable`、`serialVersionUID`、`transient`、`Externalizable`、`writeObject`、`readObject`
- [Java 网络编程](docs/io/java-net.md) - 关键词：`Socket`、`ServerSocket`、`DatagramPacket`、`DatagramSocket`
- [Java IO 工具类](docs/io/java-io-tool.md) - 关键词：`File`、`RandomAccessFile`、`System`、`Scanner`

### [Java 虚拟机](docs/jvm)

![img](https://raw.githubusercontent.com/dunwu/images/dev/snap/20200628154803.png)

- [JVM 体系结构](docs/jvm/jvm-architecture.md)
- [JVM 内存区域](docs/jvm/jvm-memory.md) - 关键词：`程序计数器`、`虚拟机栈`、`本地方法栈`、`堆`、`方法区`、`运行时常量池`、`直接内存`、`OutOfMemoryError`、`StackOverflowError`
- [JVM 垃圾收集](docs/jvm/jvm-gc.md) - 关键词：`GC Roots`、`Serial`、`Parallel`、`CMS`、`G1`、`Minor GC`、`Full GC`
- [JVM 字节码](docs/jvm/jvm-bytecode.md) - 关键词：`bytecode`、`asm`、`javassist`
- [JVM 类加载](docs/jvm/jvm-class-loader.md) - 关键词：`ClassLoader`、`双亲委派`
- [JVM 实战](docs/jvm/jvm-action.md) - 关键词：`配置`、`调优`
- [JVM 命令行工具](docs/jvm/jvm-cli-tools.md) - 关键词：`jps`、`jstat`、`jmap` 、`jstack`、`jhat`、`jinfo`
- [JVM GUI 工具](docs/jvm/jvm-gui-tools.md) - 关键词：`jconsole`、`jvisualvm`、`MAT`、`JProfile`、`Arthas`
- [TroubleShooting](docs/jvm/trouble-shooting.md) - 关键词：`CPU`、`内存`、`磁盘`、`网络`、`GC`

## 📚 资料

- **书籍**
  - Java 四大名著
    - [《Java 编程思想（Thinking in java）》](https://book.douban.com/subject/2130190/)
    - [《Java 核心技术 卷 I 基础知识》](https://book.douban.com/subject/26880667/)
    - [《Java 核心技术 卷 II 高级特性》](https://book.douban.com/subject/27165931/)
    - [《Effective Java》](https://book.douban.com/subject/30412517/)
  - Java 并发
    - [《Java 并发编程实战》](https://book.douban.com/subject/10484692/)
    - [《Java 并发编程的艺术》](https://book.douban.com/subject/26591326/)
  - Java 虚拟机
    - [《深入理解 Java 虚拟机》](https://book.douban.com/subject/34907497/)
  - Java 入门
    - [《O'Reilly：Head First Java》](https://book.douban.com/subject/2000732/)
    - [《疯狂 Java 讲义》](https://book.douban.com/subject/3246499/)
  - 其他
    - [《Head First 设计模式》](https://book.douban.com/subject/2243615/)
    - [《Java 网络编程》](https://book.douban.com/subject/1438754/)
    - [《Java 加密与解密的艺术》](https://book.douban.com/subject/25861566/)
    - [《阿里巴巴 Java 开发手册》](https://book.douban.com/subject/27605355/)
- **教程、社区**
  - [Runoob Java 教程](https://www.runoob.com/java/java-tutorial.html)
  - [java-design-patterns](https://github.com/iluwatar/java-design-patterns)
  - [Java](https://github.com/TheAlgorithms/Java)
  - [《Java 核心技术面试精讲》](https://time.geekbang.org/column/intro/82)
  - [《Java 性能调优实战》](https://time.geekbang.org/column/intro/100028001)
  - [《Java 业务开发常见错误 100 例》](https://time.geekbang.org/column/intro/100047701)
  - [深入拆解 Java 虚拟机](https://time.geekbang.org/column/intro/100010301)
  - [《Java 并发编程实战》](https://time.geekbang.org/column/intro/100023901)
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
