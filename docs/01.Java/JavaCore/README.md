---
title: JavaCore
date: 2022-05-06 09:19:33
categories:
  - Java
  - JavaCore
tags:
  - Java
  - JavaCore
permalink: /pages/9d112a4f/
hidden: true
index: false
---

# JavaCore

> JavaCore 专题总结、整理 Java 核心技术知识，涵盖 Java 基础、高级特性、容器、IO、并发编程、JVM 虚拟机等内容。
>
> Java 核心知识是 Java 工程师的内功修养，深入理解这些内容有助于在日常开发、问题排查、性能优化中游刃有余。

## 📖 内容

### [Java 面试](面试)

> 【Java 面试】专题精选 Java 核心技术各方向的经典面试题，覆盖基础语法、容器框架、并发编程、JVM 虚拟机四大板块。题目源自真实面试场景，侧重考察原理理解与实战应用能力，适合系统性备战 Java 技术面试。

- [Java 基础面试一](面试/[JavaCore][面试]基础（一）.md) 💯
- [Java 基础面试二](面试/[JavaCore][面试]基础（二）.md) 💯
- [Java 基础面试三](面试/[JavaCore][面试]基础（三）.md) 💯
- [Java 容器面试一](面试/[JavaCore][面试]容器（一）.md) 💯
- [Java 容器面试二](面试/[JavaCore][面试]容器（二）.md) 💯
- [Java 容器面试三](面试/[JavaCore][面试]容器（三）.md) 💯
- [Java 并发面试一](面试/[JavaCore][面试]并发（一）.md) 💯
- [Java 并发面试二](面试/[JavaCore][面试]并发（二）.md) 💯
- [Java 并发面试三](面试/[JavaCore][面试]并发（三）.md) 💯
- [Java 虚拟机面试一](面试/[JavaCore][面试]虚拟机（一）.md) 💯
- [Java 虚拟机面试二](面试/[JavaCore][面试]虚拟机（二）.md) 💯

### [Java 基础](基础)

> 【Java 基础】专题涵盖语言核心语法、类型系统、面向对象三大范式、控制流与异常体系，以及泛型、反射、注解等运行时机制。这些是 Java 开发的基石，深入理解有助于编写健壮、可维护的代码，也是面试高频考察点。

- [Java 基础语法特性](基础/[JavaCore]基础语法.md) - 关键词：`强类型`、`访问修饰符`、`final`、`操作符`、`序列化`
- [Java 基本数据类型](基础/[JavaCore]数据类型.md) - 关键词：`包装类`、`装箱拆箱`、`缓存机制`、`BigDecimal`、`精度丢失`
- [Java 面向对象](基础/[JavaCore]面向对象.md) - 关键词：`封装`、`继承`、`多态`、`接口`、`抽象类`
- [Java 方法](基础/[JavaCore]方法.md) - 关键词：`值传递`、`重载`、`覆写`、`static`、`可变参数`
- [Java 数组](基础/[JavaCore]数组.md) - 关键词：`引用类型`、`多维数组`、`Arrays`、`长度固定`、`下标`
- [Java 枚举](基础/[JavaCore]枚举.md) - 关键词：`enum`、`EnumSet`、`EnumMap`、`单例模式`、`状态机`
- [Java 控制语句](基础/[JavaCore]控制语句.md) - 关键词：`if`、`switch`、`while`、`for`、`break`
- [Java 异常](基础/[JavaCore]异常.md) - 关键词：`Throwable`、`RuntimeException`、`try-catch-finally`、`Checked Exception`、`异常链`
- [Java 泛型](基础/[JavaCore]泛型.md) - 关键词：`类型擦除`、`通配符`、`PECS`、`泛型类`、`类型边界`
- [Java 反射](基础/[JavaCore]反射.md) - 关键词：`Class`、`invoke`、`动态代理`、`CGLIB`、`InvocationHandler`
- [Java 注解](基础/[JavaCore]注解.md) - 关键词：`元注解`、`@Retention`、`@Target`、`自定义注解`、`反射解析`
- [Java String 类型](基础/[JavaCore]String.md) - 关键词：`不可变`、`字符串常量池`、`intern`、`StringBuilder`、`StringBuffer`
- [Java 正则](基础/[JavaCore]正则.md) - 关键词：`Pattern`、`Matcher`、`捕获与非捕获`、`零宽断言`、`贪婪与懒惰`
- [Java 编码和加密](基础/[JavaCore]编码和加密.md) - 关键词：`Base64`、`消息摘要`、`数字签名`、`对称加密`、`非对称加密`
- [Java 国际化](基础/[JavaCore]国际化.md) - 关键词：`Locale`、`ResourceBundle`、`NumberFormat`、`DateFormat`、`MessageFormat`
- [Java SPI](基础/[JavaCore]SPI.md) - 关键词：`SPI`、`ServiceLoader`、`ClassLoader`、`扩展点`
- [Java Agent](基础/[JavaCore]Agent.md) - 关键词：`Instrumentation`、`premain`、`ClassFileTransformer`、`字节码增强`、`JVMTI`
- [Java JDK8](基础/[JavaCore]JDK8.md) - 关键词：`Stream`、`Lambda`、`Optional`、`@FunctionalInterface`

### [Java 容器](容器)

> 【Java 容器】专题以 Collection 和 Map 两大体系为核心，涵盖 List、Set、Queue 等经典数据结构及其底层实现原理（数组、链表、红黑树、哈希表）。结合 Stream API 的声明式数据处理能力，构成 Java 日常开发中数据组织、操作与转换的完整工具链，是面试中考察数据结构与算法功底的核心阵地。

- [Java 容器简介](容器/[JavaCore][容器]简介.md) - 关键词：`Collection`、`Map`、`Iterator`、`Comparator`、`fail-fast`
- [Java 容器之 List](容器/[JavaCore][容器]List.md) - 关键词：`ArrayList`、`LinkedList`、`动态扩容`、`RandomAccess`、`subList`
- [Java 容器之 Map](容器/[JavaCore][容器]Map.md) - 关键词：`HashMap`、`LinkedHashMap`、`TreeMap`、`红黑树`、`负载因子`
- [Java 容器之 Set](容器/[JavaCore][容器]Set.md) - 关键词：`HashSet`、`LinkedHashSet`、`TreeSet`、`去重`、`NavigableSet`
- [Java 容器之 Queue](容器/[JavaCore][容器]Queue.md) - 关键词：`Deque`、`ArrayDeque`、`PriorityQueue`、`BlockingQueue`、`二叉堆`
- [Java 容器之 Stream](容器/[JavaCore][容器]Stream.md) - 关键词：`Stream`、`中间操作`、`终结操作`、`Collector`、`惰性求值`

### [Java IO](IO)

> 【Java IO】专题涵盖 Java 输入输出体系的核心知识，包括 BIO（同步阻塞）、NIO（多路复用）、AIO（异步非阻塞）三种 IO 模型的原理与实践。同时深入序列化与反序列化机制，涉及 JDK 原生序列化、Protobuf、Hessian 等主流方案的对比与应用。

- [Java IO 之 简介](IO/[JavaCore][IO]简介.md) - 关键词：`BIO`、`NIO`、`AIO`、`I/O 多路复用`、`Reactor`
- [Java IO 之 BIO](IO/[JavaCore][IO]BIO.md) - 关键词：`BIO`、`字节流`、`字符流`、`InputStream`、`Socket`
- [Java IO 之 NIO](IO/[JavaCore][IO]NIO.md) - 关键词：`NIO`、`Channel`、`Buffer`、`Selector`、`零拷贝`
- [Java IO 之序列化](IO/[JavaCore][IO]序列化.md) - 关键词：`Serializable`、`serialVersionUID`、`transient`、`Protobuf`、`JSON`

### [Java 并发](并发)

> 【Java 并发】专题系统梳理多线程并发编程的核心知识体系，涵盖 Java 内存模型（JMM）、线程生命周期与同步机制、锁机制（synchronized/ReentrantLock/AQS）、无锁并发（CAS/原子类/ThreadLocal）等核心主题。同时深入线程池原理、并发容器、同步与分工工具类等实战内容，帮助理解并发编程的本质问题与解决方案。

- [Java 并发简介](并发/[JavaCore][并发]简介.md) - 关键词：`原子性`、`可见性`、`有序性`、`死锁`、`上下文切换`
- [Java 并发之内存模型](并发/[JavaCore][并发]内存模型.md) - 关键词：`JMM`、`Happens-Before`、`volatile`、`synchronized`、`锁升级`
- [Java 并发之线程](并发/[JavaCore][并发]线程.md) - 关键词：`Thread`、`Runnable`、`Callable`、`线程生命周期`、`interrupt`
- [Java 并发之锁](并发/[JavaCore][并发]锁.md) - 关键词：`ReentrantLock`、`AQS`、`CAS`、`悲观锁`、`乐观锁`
- [Java 并发之无锁](并发/[JavaCore][并发]无锁.md) - 关键词：`CAS`、`原子类`、`ThreadLocal`、`ABA 问题`、`Copy-on-Write`
- [Java 并发之 AQS](并发/[JavaCore][并发]AQS.md) - 关键词：`AQS`、`CLH 队列`、`独占锁`、`共享锁`、`Condition`
- [Java 并发之容器](并发/[JavaCore][并发]容器.md) - 关键词：`ConcurrentHashMap`、`CopyOnWriteArrayList`、`BlockingQueue`、`分段锁`、`CAS`
- [Java 并发之线程池](并发/[JavaCore][并发]线程池.md) - 关键词：`ThreadPoolExecutor`、`corePoolSize`、`拒绝策略`、`workQueue`、`Executors`
- [Java 并发之同步工具](并发/[JavaCore][并发]同步工具.md) - 关键词：`CountDownLatch`、`Semaphore`、`CyclicBarrier`、`信号量`、`AQS`
- [Java 并发之分工工具](并发/[JavaCore][并发]分工工具.md) - 关键词：`CompletableFuture`、`ForkJoinPool`、`FutureTask`、`工作窃取`、`异步编排`

### [Java 虚拟机](JVM)

> 【Java 虚拟机】专题深入剖析 JVM 的核心运行机制，涵盖运行时数据区（堆、栈、方法区）、垃圾收集算法与主流收集器（CMS、G1、ZGC）、字节码格式与类加载机制（双亲委派模型）等关键主题。同时涉及 JVM 性能调优方法论与常用诊断工具（Arthas、jstat、jmap、MAT），助力生产环境问题排查与性能优化。

- [Java 虚拟机简介](JVM/[JavaCore][JVM]简介.md) - 关键词：`JVM`、`Hotspot`、`运行时数据区`、`类加载器`、`JIT`
- [Java 虚拟机之内存区域](JVM/[JavaCore][JVM]内存区域.md) - 关键词：`堆`、`方法区`、`元空间`、`虚拟机栈`、`OutOfMemoryError`
- [Java 虚拟机之垃圾收集](JVM/[JavaCore][JVM]垃圾收集.md) - 关键词：`GC Roots`、`可达性分析`、`分代收集`、`CMS`、`G1`
- [Java 虚拟机之字节码](JVM/[JavaCore][JVM]字节码.md) - 关键词：`字节码`、`常量池`、`字节码指令`、`ASM`、`ByteBuddy`
- [Java 虚拟机之类加载](JVM/[JavaCore][JVM]类加载.md) - 关键词：`类加载机制`、`双亲委派`、`ClassLoader`、`热部署`、`SPI`
- [Java 虚拟机之工具](JVM/[JavaCore][JVM]工具.md) - 关键词：`Arthas`、`jstat`、`jmap`、`jstack`、`MAT`
- [Java 虚拟机之故障处理](JVM/[JavaCore][JVM]故障处理.md) - 关键词：`CPU 飙升`、`内存泄漏`、`OOM`、`GC 频繁`、`死锁`
- [Java 虚拟机之调优](JVM/[JavaCore][JVM]调优.md) - 关键词：`-Xms`、`-Xmx`、`GC 日志`、`吞吐量`、`停顿时间`

## 📚 资料

- Java 综合
  - [极客时间教程 - Java 业务开发常见错误 100 例](https://time.geekbang.org/column/intro/100047701) - 极客时间教程——基于 Java 生产环境的真实案例，讲解"避坑"的手段，很硬核
  - [极客时间教程 - Java 性能调优实战](https://time.geekbang.org/column/intro/100028001) - 极客时间教程——覆盖 80% 以上 Java 应用调优场景
  - [极客时间教程 - Java 核心技术面试精讲](https://time.geekbang.org/column/intro/82) - 极客时间教程——从面试官视角梳理如何解答常见 Java 面试问题
  - [CS-Notes](https://github.com/CyC2018/CS-Notes) - Github 上的 Java 基础级面试教程，行文清晰简洁
  - [JavaGuide](https://github.com/Snailclimb/JavaGuide) - Github 上的 Java 面试教程，Java 基础部分讲解较为细致
  - [advanced-java](https://github.com/doocs/advanced-java) - Github 上的 Java 面试教程，分布式部分从面试官视角讲解核心考察点
- Java 基础
  - [《Java 编程思想》](https://book.douban.com/subject/2130190/) - Thinking in java，典中典！由于成书较早，部分内容已经多少有点过时
  - [《Java 核心技术 卷 I 开发基础》](https://book.douban.com/subject/35920145/) - 第 12 版，涵盖 Java 17 的新特性
  - [《Java 核心技术 卷 II 高级特性》](https://book.douban.com/subject/36337685/) - 第 12 版，涵盖 Java 17 的新特性
  - [《Head First Java》](https://book.douban.com/subject/2000732/) - 图文并茂，对新手非常友好的入门级教程
  - [《疯狂 Java 讲义》](https://book.douban.com/subject/3246499/) - 入门级教程
  - [Runoob Java 教程](https://www.runoob.com/java/java-tutorial.html) - 入门级在线教程
- Java 并发
  - [《Java 并发编程实战》](https://book.douban.com/subject/10484692/) - 深入浅出地介绍 Java 线程和并发
  - [《Java 并发编程的艺术》](https://book.douban.com/subject/26591326/)
  - [极客时间教程 - Java 并发编程实战](https://time.geekbang.org/column/intro/100023901) - 极客时间教程——图文并茂，系统性讲解并发编程知识
  - [拉勾教育教程 - Java 并发编程 78 讲](https://kaiwu.lagou.com/course/courseInfo.htm?courseId=16) - 拉勾教育教程——针对并发场景问题，讲解的通俗易懂
- Java 虚拟机
  - [《深入理解 Java 虚拟机》](https://book.douban.com/subject/34907497/) - 第 3 版，国内最好的 JVM 书籍
  - [极客时间教程 - 深入拆解 Java 虚拟机](https://time.geekbang.org/column/intro/100010301) - 极客时间教程
  - [从表到里学习 JVM 实现](https://www.douban.com/doulist/2545443/)
- Java IO
  - [《Netty 实战》](https://book.douban.com/subject/27038538/)
  - [《Java 网络编程》](https://book.douban.com/subject/1438754/)
- Java 编程规范
  - [《Effective Java》](https://book.douban.com/subject/36818907/) - 第 3 版，涵盖 Java 9 的新特性
  - [《阿里巴巴 Java 开发手册》](https://github.com/alibaba/p3c/blob/master/阿里巴巴Java开发手册（详尽版）.pdf)
  - [Google Java 编程指南](https://google.github.io/styleguide/javaguide.html)
- 其他
  - [《Head First 设计模式》](https://book.douban.com/subject/2243615/)
  - [《Java 加密与解密的艺术》](https://book.douban.com/subject/25861566/)
  - [java-design-patterns](https://github.com/iluwatar/java-design-patterns) - Github 上的 Java 版设计模式教程
  - [Java](https://github.com/TheAlgorithms/Java) - Github 上的 Java 算法教程

## 🚪 传送

◾ 💧 [钝悟的 IT 知识图谱](https://dunwu.github.io/waterdrop/) ◾ 🎯 [钝悟的博客](https://dunwu.github.io/blog/) ◾ 🏠 [JAVACORE 首页](https://github.com/dunwu/javacore) ◾
