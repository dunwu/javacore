---
title: Java 基础面试一
cover: https://raw.githubusercontent.com/dunwu/images/master/archive/2025/03/020ab2bf4af8401590e0291a34f873f8.jpg
date: 2024-06-18 22:46:20
order: 1
categories:
  - Java
  - JavaCore
  - 面试
tags:
  - Java
  - JavaCore
  - 面试
permalink: /pages/6ca01ab7/
---

# Java 基础面试一

## Java 常识

### 【简单】Java 语言有什么优势？⭐⭐⭐

- **跨平台**：【**一次编写，到处执行（Write Once, Run Anywhere）**】——JVM 执行字节码。
- **自动垃圾回收**：垃圾回收（GC）减少内存泄漏风险。
- **强大生态**：Spring、Hadoop、Android 等广泛支持。
- **面向对象**：支持封装、继承、多态，代码结构清晰易维护。
- **高性能**：JIT 编译优化，多线程支持高并发。
- **健壮安全**：强类型检查、异常处理、JVM 安全机制。

### 【简单】Oracle JDK 和 Open JDK 有什么区别？⭐

|          | OpenJDK                                           | Oracle JDK                                             |
| -------- | ------------------------------------------------- | ------------------------------------------------------ |
| 是否开源 | 完全开源                                          | 闭源                                                   |
| 是否免费 | 完全免费                                          | JDK8u221 之后存在限制                                  |
| 更新频率 | 一般每 3 个月发布一个版本；不提供 LTS 服务        | 一般每 6 个月发布一个版本；大概每三年推出一个 LTS 版本 |
| 功能性   | Java 11 之后，OracleJDK 和 OpenJDK 的功能基本一致 |                                                        |
| 协议     | GPL v2                                            | BCL/OTN                                                |

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2025/04/d1f144f5b1bd4d46a526fb4f2a889e26.png)

### 【简单】Java SE 和 Java EE 有什么区别？⭐

Java 技术既是一种编程语言，又是一种平台。Java 编程语言是一种具有特定语法和风格的高级面向对象语言。Java 平台是 Java 编程语言应用程序运行的特定环境。

- **Java SE**（Java Platform, Standard Edition） - **Java 平台标准版**。Java SE 的 API 提供了 Java 编程语言的核心功能。它定义了从 Java 编程语言的基本类型和对象到用于网络、安全、数据库访问、图形用户界面 (GUI) 开发和 XML 解析的高级类的所有内容。除了核心 API 之外，Java SE 平台还包括虚拟机、开发工具、部署技术以及 Java 技术应用程序中常用的其他类库和工具包。
- **Java EE**（Java Platform, Enterprise Edition） - **Java 平台企业版**。Java EE 构建在 Java SE 基础之上。 Java EE 定义了企业级应用程序开发和部署的标准和规范，如：Servlet、JSP、EJB、JDBC、JPA、JTA、JavaMail、JMS。

::: tip 扩展

[**Your First Cup**](https://docs.oracle.com/javaee/6/firstcup/doc/gkhoy.html)

:::

### 【简单】JDK、JRE、JVM 之间有什么关系？⭐⭐

JDK、JRE、JVM 的定义和简介：

- **JVM** - Java Virtual Machine 的缩写，即 Java 虚拟机。JVM 是运行 Java 字节码的虚拟机。JVM 不理解 Java 源代码，这就是为什么要将 `*.java` 文件编译为 JVM 可理解的 `*.class` 文件（字节码）。Java 有一句著名的口号：“**Write Once, Run Anywhere（一次编写，随处运行）**”，JVM 正是其核心所在。实际上，JVM 针对不同的系统（Windows、Linux、MacOS）有不同的实现，目的在于用相同的字节码执行同样的结果。
- **JRE** - Java Runtime Environment 的缩写，即 Java 运行时环境。它是运行已编译 Java 程序所需的一切的软件包，主要包括 JVM、Java 类库（Class Library）、Java 命令和其他基础结构。但是，它不能用于创建新程序。
- **JDK** - Java Development Kit 的缩写，即 Java SDK。它不仅包含 JRE 的所有功能，还包含编译器 (javac) 和工具（如 javadoc 和 jdb）。它能够创建和编译程序。

总结来说，JDK、JRE、JVM 三者的关系是：JDK > JRE > JVM

```
JDK = JRE + 开发/调试工具
JRE = JVM + Java 类库 + Java 运行库
JVM = 类加载系统 + 运行时内存区域 + 执行引擎
```

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2025/04/1713e34f9ed8477a8faf3feeb2d00335.png)

::: tip 扩展

[stackoverflow 高票问题 - What is the difference between JDK and JRE?](https://stackoverflow.com/questions/1906445/what-is-the-difference-between-jdk-and-jre)

:::

### 【中等】Java 如何调用外部可执行程序或系统命令？⭐

Java 提供了两种调用外部可执行程序或系统命令的方式：

- `ProcessBuilder`
- `Runtime.exec()`

::: tip 扩展

https://blog.csdn.net/m0_46487331/article/details/128827908

:::

### 【中等】Java 和 C++、Go 语言的区别，各自的优缺点？⭐⭐

- Java 跨平台支持好、生态完善、支持 GC
- C++ 性能高、手动创建释放内存
- Go 并发能力强（轻量级线程）

### 【中等】Java 里程碑版本中的核心特性有哪些？⭐⭐

- **Java 8（2014）**
  - **Lambda 表达式**：函数式编程支持（`(a, b) -> a + b`）
  - **Stream API**：链式数据流操作（`filter/map/reduce`）
  - **默认方法**：接口支持默认实现（`default void foo() {}`）
  - **新的日期时间 API**：`java.time`（`LocalDate`、`ZonedDateTime`）
  - **Optional**：优雅处理 `null`（`Optional.ofNullable(x)`）
  - **默认 GC 设为 G1**
- **Java 9（2017）**
  - **JPMS 模块化系统**：通过 `module-info.java` 声明模块依赖，实现类库级别的封装
  - **Reactive Streams**：`java.util.concurrent.Flow` 定义响应式编程标准接口
  - **接口私有方法**：接口中可定义 `private` 方法，复用默认方法中的逻辑
  - **集合工厂方法**：`List.of()`、`Map.of()` 快速创建不可变集合
- **Java 11（2018）**
  - **局部变量类型推断**：`var list = new ArrayList<String>()`
  - **HTTP Client API**：标准化的异步 HTTP 客户端（`HttpClient`）
  - **字符串 API 增强**：`isBlank()`、`lines()`、`repeat()`
  - **新垃圾收集器**：**ZGC**（低延迟）和 **Shenandoah**（并发回收）成为标准功能
- **Java 14（2020）**
  - **switch 表达式增强（正式版）**：支持 `->` 箭头语法和 `yield` 返回值
  - **Record（预览）**：不可变数据载体类（`record Point(int x, int y) {}`）
  - **Helpful NullPointerExceptions**：NPE 信息精确到具体变量（`Cannot invoke "String.length()" because "s" is null`）
  - **instanceof 模式匹配（预览）**：`if (obj instanceof String s)` 直接绑定变量
- **Java 17（2021）**
  - **密封类（Sealed Classes）**：限制类继承（`permits` 子类）
  - **instanceof 模式匹配（正式版）**：类型检查与转换合二为一
  - **文本块（正式版）**：多行字符串（`"""..."""`)
  - **Record（正式版）**：不可变数据类
  - **switch 模式匹配（预览）**：`case Point p -> ...`
  - **移除实验性 AOT/JIT**：删除 **GraalVM** 相关实验性特性
- **Java 21（2023）**
  - **虚拟线程（Virtual Threads）**：轻量级线程（`Thread.startVirtualThread()`），M:N 调度模型
  - **结构化并发（预览）**：简化多线程任务管理（`StructuredTaskScope`）
  - **记录模式（Record Patterns）**：解构记录类（`if (obj instanceof Point(int x, int y))`）
  - **switch 模式匹配（正式版）**：`switch` 中支持类型模式、守卫条件、`null` 分支
  - **序列集合（Sequenced Collections）**：新增 `SequencedCollection`/`SequencedSet`/`SequencedMap` 接口，统一有序集合的首尾访问
  - **未命名变量和模式（预览）**：用 `_` 表示不使用的变量（`var _ = compute();`）
  - **字符串模板（预览）**：`STR."Hello \{name}"` 安全高效的字符串插值
  - **作用域值 Scoped Values（预览）**：比 `ThreadLocal` 更安全高效的线程上下文传递方案，专为虚拟线程设计
  - **分代 ZGC**：针对年轻代优化的 ZGC，大幅降低 GC 开销
  - **弃用 Windows 32-bit**：正式放弃对 32 位 Windows 的支持

## Java 基础语法

### 【简单】Java 有几种注释形式？⭐

注释用于在源代码中解释代码的作用，可以增强程序的可读性，可维护性。 空白行，或者注释的内容，都会被 Java 编译器忽略掉。

Java 注释主要有三种类型：

- 单行注释
- 多行注释
- 文档注释（JavaDoc）

```java
public class HelloWorld {
    /**
     * 文档注释
     */
    public static void main(String[] args) {
        // 单行注释
        /*
        多行注释
        */
        System.out.println("Hello World");
    }

}
```

### 【简单】Java 有哪些标识符命名规则？⭐

Java 所有的组成部分都需要名字。类名、变量名以及方法名都被称为标识符。

**标识符基本规则**

- **组成元素**：类名、变量名、方法名等统称为标识符
- **允许字符**：可包含字母、数字、`$`、`_`
- **首字符要求**：不能以数字开头
- **禁止关键字**：如 `class`、`public` 等保留字不可作为标识符
- **大小写敏感**：`age` 和 `Age` 被视为不同标识符

**命名规范**

在 Java 中，标识符通常遵循 [驼峰命名法](https://zh.wikipedia.org/wiki/%E9%A7%9D%E5%B3%B0%E5%BC%8F%E5%A4%A7%E5%B0%8F%E5%AF%AB)。

| **类型**        | **命名法**                | **示例**                      |
| :-------------- | :------------------------ | :---------------------------- |
| **类/接口名**   | 大驼峰（Upper CamelCase） | `StudentInfo`、`UserService`  |
| **方法/变量名** | 小驼峰（Lower CamelCase） | `getUserName()`、`studentAge` |
| **常量名**      | 全大写蛇形（SNAKE_CASE）  | `MAX_SIZE`、`DEFAULT_TIMEOUT` |

**注意事项**

- **避免使用 `$`**：虽然合法，但通常用于编译器生成代码
- **无长度限制**：但应保持简洁且语义明确（如用 `count` 而非 `c`）
- **Unicode 支持**：可使用中文等字符（但不推荐）

### 【简单】Java 中有哪些关键字？⭐

下面列出了 Java 保留字，这些保留字不能用于常量、变量、和任何标识符的名称。

| 分类                 | 关键字                                                                                                                         |
| -------------------- | ------------------------------------------------------------------------------------------------------------------------------ |
| 访问级别修饰符       | private、protected、public、default                                                                                            |
| 类，方法和变量修饰符 | abstract、class、extends、final、implements、interface、native、new、static、strictfp、synchronized、transient、volatile、enum |
| 程序控制语句         | break、continue、return、do、while、if、else、for、instanceof、switch、case                                                    |
| 错误处理             | assert、try、catch、throw、throws、finally                                                                                     |
| 包相关               | import、package                                                                                                                |
| 数据类型             | boolean、byte、char、short、int、long、float、double、enum                                                                     |
| 变量引用             | super、this、void                                                                                                              |
| 其他保留字           | goto、const                                                                                                                    |

::: warning

Java 的 `null` 不是关键字，类似于 `true` 和 `false`，它是一个字面常量，不允许作为标识符使用。

**官方文档**：https://docs.oracle.com/javase/tutorial/java/nutsandbolts/_keywords.html

:::

### 【简单】Java 中 `static` 关键字有什么用？⭐⭐

`static` 关键字用于声明**类级别的成员**，**不属于任何实例**，在类加载时初始化，所有实例共享。

**四种用法**：

| **修饰目标**   | **特点**                             | **示例**                                 |
| -------------- | ------------------------------------ | ---------------------------------------- |
| **静态变量**   | 类共享，所有实例访问同一份           | `static int count = 0;`                  |
| **静态方法**   | 可直接通过类名调用，不能访问实例成员 | `static int add(int a, int b)`           |
| **静态代码块** | 类加载时执行一次，用于初始化         | `static { init(); }`                     |
| **静态内部类** | 不依赖外部实例，可独立创建           | `Outer.Inner inner = new Outer.Inner();` |

**关键细节**：

- **初始化时机**：静态变量和静态代码块按**声明顺序**执行，在类加载的 `<clinit>` 阶段。
- **静态方法限制**：
  - 不能使用 `this`/`super`（无实例上下文）。
  - 只能直接访问静态成员，访问实例成员需先创建对象。
  - 不能被 `@Override`（重写属于实例方法的多态机制），但能被**隐藏（hide）**。
- **静态导入**（Java 5+）：`import static java.lang.Math.*;` 可直接使用 `sqrt(2)`。

```java
public class Counter {
    private static int count = 0;  // 静态变量

    static {  // 静态代码块，类加载时执行一次
        System.out.println("Counter 类已加载");
    }

    public Counter() {
        count++;
    }

    public static int getCount() {  // 静态方法
        return count;
    }
}
```

### 【中等】`transient` 关键字有什么用？⭐⭐

`transient` 用于**修饰不应被序列化的字段**，常用于敏感数据或可派生的字段。

**核心作用**：

- 序列化对象时，被 `transient` 修饰的字段**不会被持久化**。
- 反序列化时，该字段恢复为**类型默认值**（如 `int` → 0，对象 → `null`）。

**典型场景**：

```java
public class User implements Serializable {
    private String username;
    private transient String password;  // 密码不参与序列化
    private transient int age;          // 临时字段不持久化

    // 反序列化后，password=null, age=0
}
```

**注意事项**：

- **仅适用于 `Serializable` 接口**，对 `Externalizable` 无效（需手动实现 `writeExternal`/`readExternal`）。
- **静态字段**默认不被序列化（无论是否加 `transient`），因为序列化针对的是对象状态而非类状态。
- **`ArrayList` 中的巧妙用法**：`ArrayList` 用 `transient Object[] elementData` 修饰底层数组，自定义 `writeObject`/`readObject` 仅序列化有效元素，避免序列化 `null` 浪费空间。

### 【简单】`native` 关键字有什么用？⭐

`native` 修饰的方法称为**本地方法**，表示该方法由**非 Java 语言（如 C/C++）** 实现，通过 JNI（Java Native Interface）调用。

**特点**：

- **无方法体**：只有声明，如 `public native int hashCode();`。
- **跨平台桥梁**：用于调用操作系统 API 或底层库（如 `System.arraycopy` 底层是 `native`）。
- **性能考量**：调用涉及 Java 到本地的上下文切换，频繁调用有性能开销。

**典型应用**：

- `Object.hashCode()`：与 JVM 内存布局相关，由 native 实现。
- `Thread.start0()`：调用操作系统线程创建 API。
- `System.currentTimeMillis()`：调用系统时间 API。
- `Unsafe` 类：直接内存操作、CAS 等。

### 【中等】移位操作中 `<<`、`>>`、`>>>` 有什么区别？⭐

**移位位数处理机制**

Java 对移位位数超限的处理采用**隐式取模运算**：

- **`int` 类型（32 位）**：实际移位位数 = `指定位数 % 32`
  - 例如：`x << 42` → 实际左移 `42 % 32 = 10` 位
- **`long` 类型（64 位）**：实际移位位数 = `指定位数 % 64`
  - 例如：`x << 100` → 实际左移 `100 % 64 = 36` 位

**位操作统一规则**

| **操作符** | **示例**   | **等效操作**        | **说明**                       |
| :--------- | :--------- | :------------------ | :----------------------------- |
| `<<`       | `x << 35`  | `x << 3` (35%32=3)  | 左移，低位补 0                 |
| `>>`       | `x >> 35`  | `x >> 3` (35%32=3)  | 右移，高位补符号位（算术右移） |
| `>>>`      | `x >>> 35` | `x >>> 3` (35%32=3) | 无符号右移，高位补 0           |

**底层原理**

- **硬件优化**：CPU 执行移位指令时，实际只使用指定位数的低 5 位（int）或低 6 位（long），与 Java 的取模规则一致。
- **安全设计**：避免无效的大位数移位（如 `x << 1000`）导致不可预测行为。

**示例**

```java
int i = -1; // 二进制全 1（32 个 1）
System.out.println(i << 10);  // 左移 10 位，输出 -1024
System.out.println(i << 42);  // 等效左移 10 位（42%32=10），同样输出 -1024

long l = -1L;
System.out.println(l << 70);  // 等效左移 6 位（70%64=6），输出 -64
```

**特殊情况**

- **移位 0 位**：任何 `x << 32` 或 `x >> 64` 等效不移位（因 `32%32=0`，`64%64=0`）。
- **负数移位**：移位位数可为负数，但会通过取模转为正数（如 `x << -6` → `x << 26`，因 `-6 % 32 = 26`）。

::: info 为什么这样设计？
:::

- **兼容性**：与 C/C++的移位行为一致。
- **性能**：直接映射到 CPU 指令，无需额外检查。
- **确定性**：保证结果可预测，避免未定义行为。

## Java 数据类型

### 【简单】Java 是否只支持值传递？⭐⭐⭐

**Java 只支持值传递**。

- **值传递**：方法参数传递的是实参的副本
- **引用传递**：方法参数传递的是实参的地址；因此，修改形参会同步影响实参

### 【简单】Java 有哪些值类型？⭐⭐

Java 中的数据类型有两类：

- 值类型（又叫内置数据类型，基本数据类型）
- 引用类型（除值类型以外，都是引用类型，包括 `String`、数组等）

Java 语言提供了 **8** 种基本类型，大致分为 **4** 类：布尔型、字符型、整数型、浮点型。

| 基本数据类型 | 分类       | 大小   | 默认值     | 取值范围                | 包装类    | 说明                                          |
| ------------ | ---------- | ------ | ---------- | ----------------------- | --------- | --------------------------------------------- |
| `boolean`    | **布尔型** | -      | `false`    | `false, true`           | Boolean   | `boolean` 的大小，是由具体的 JVM 实现来决定的 |
| `char`       | **字符型** | 16 bit | `'\u0000'` | `[0, 2^16 - 1]`         | Character | 存储 Unicode 码，用单引号赋值                 |
| `byte`       | **整数型** | 8 bit  | `0`        | `[-2^7, 2^7 - 1]`       | Byte      |                                               |
| `short`      | **整数型** | 16 bit | `0`        | `[-2^15, 2^15 - 1]`     | Short     |                                               |
| `int`        | **整数型** | 32 bit | `0`        | `[-2^31, 2^31 - 1]`     | Integer   |                                               |
| `long`       | **整数型** | 64 bit | `0L`       | `[-2^63, 2^63 - 1]`     | Long      | 赋值时一般在数字后加上 `l` 或 `L`             |
| `float`      | **浮点型** | 32 bit | `0.0f`     | `[2^-149, 2^128 - 1]`   | Float     | 赋值时必须在数字后加上 `f` 或 `F`             |
| `double`     | **浮点型** | 64 bit | `0.0d`     | `[2^-1074, 2^1024 - 1]` | Double    | 赋值时一般在数字后加 `d` 或 `D`               |

::: tip 扩展

[菜鸟教程 - Java 基本数据类型](https://www.runoob.com/java/java-basic-datatypes.html)

:::

### 【简单】什么是装箱、拆箱？⭐⭐⭐

::: info 什么是装箱、拆箱？
:::

Java 中为每一种基本数据类型提供了相应的包装类，如下：

```java
Byte <-> byte
Short <-> short
Integer <-> int
Long <-> long
Float <-> float
Double <-> double
Character <-> char
Boolean <-> boolean
```

**引入包装类的目的**就是：提供一种机制，使得**基本数据类型可以与引用类型互相转换**。

基本数据类型与包装类的转换被称为装箱和拆箱。

- **装箱（boxing）是将值类型转换为引用类型**。例如：`int` 转 `Integer`
  - **装箱过程是通过调用包装类的 `valueOf` 方法实现的**。
- **拆箱（unboxing）是将引用类型转换为值类型**。例如：`Integer` 转 `int`
  - **拆箱过程是通过调用包装类的 `xxxValue` 方法实现的**。（xxx 代表对应的基本数据类型）。

::: info 什么是自动装箱与拆箱？
:::

```java
Integer a = 10;  //装箱
int b = a;   //拆箱
```

上面这两行代码对应的字节码为：

```java
   L1

    LINENUMBER 8 L1

    ALOAD 0

    BIPUSH 10

    INVOKESTATIC java/lang/Integer.valueOf (I)Ljava/lang/Integer;

    PUTFIELD AutoBoxTest.i : Ljava/lang/Integer;

   L2

    LINENUMBER 9 L2

    ALOAD 0

    ALOAD 0

    GETFIELD AutoBoxTest.i : Ljava/lang/Integer;

    INVOKEVIRTUAL java/lang/Integer.intValue ()I

    PUTFIELD AutoBoxTest.n : I

    RETURN
```

通过字节码代码，不难发现，装箱其实就是调用了 包装类的 `valueOf()` 方法；而拆箱其实就是调用了 `xxxValue()` 方法。再次印证前文的内容：

- **装箱过程是通过调用包装类的 `valueOf` 方法实现的**。
- **拆箱过程是通过调用包装类的 `xxxValue` 方法实现的**。

因此，

- `Integer a = 10` 等价于 `Integer a = Integer.valueOf(10)`
- `int b = a` 等价于 `int b = a.intValue()`;

::: tip 扩展

[深入剖析 Java 中的装箱和拆箱](https://www.cnblogs.com/dolphin0520/p/3780005.html)

:::

### 【中等】包装类型的缓存机制了解么？⭐⭐⭐

Java 基本数据类型的包装类型的大部分都用到了缓存机制来提升性能。

`Byte`、`Short`、`Integer`、`Long` 这 4 种包装类默认创建了数值 **[-128，127]** 的相应类型的缓存数据，`Character` 创建了数值在 **[0, 127]** 范围的缓存数据，`Boolean` 直接返回 `True` or `False`。

如果超出对应范围仍然会去创建新的对象，缓存的范围区间的大小只是在性能和资源之间的权衡。

**（1）Integer 缓存上限可调**

`Integer` 的缓存上限可通过 JVM 参数 `-XX:AutoBoxCacheMax=<size>` 调整（仅影响 `Integer`，不影响 `Long` 等其他包装类）。该参数在 `IntegerCache` 静态初始化时读取：

```java
// Integer.IntegerCache 源码（JDK 8+）
private static class IntegerCache {
    static final int low = -128;
    static final int high;
    static final Integer cache[];
    static {
        int h = 127;
        String integerCacheHighPropValue =
            sun.misc.VM.getSavedProperty("java.lang.Integer.IntegerCache.high");
        if (integerCacheHighPropValue != null) {
            int i = parseInt(integerCacheHighPropValue);
            i = Math.max(i, 127);
            h = Math.min(i, Integer.MAX_VALUE - (-low) -1);
        }
        high = h;
        cache = new Integer[(high - low) + 1];
        // 初始化 cache[] 数组...
    }
}
```

关键细节：

- 参数名是 `-XX:AutoBoxCacheMax`，但实际读取的系统属性是 `java.lang.Integer.IntegerCache.high`。
- 仅影响 `Integer` 的缓存上限，`Long`/`Short`/`Byte` 的上限 **固定为 127 不可调**。

**（2）高并发场景的内存影响（定量分析）**

频繁装箱在并发场景下会产生大量临时对象，引发 GC 压力：

```
假设 QPS = 10,000，每次装箱 Long 对象（24 字节对象头 + 8 字节数据 = 32 字节）：
- 每秒产生 10,000 × 32 = 320 KB 临时对象
- 每分钟 = 19.2 MB，每小时 ≈ 1.15 GB 对象分配
- 若未命中缓存（Long 缓存仅 -128~127），所有装箱都是 new Long()
- 年轻代频繁 Minor GC → 晋升老年代 → 可能触发 Full GC
```

**优化策略**：

- 高并发路径上避免装箱，优先用基本类型。
- 若必须用包装类型，确保数值在缓存范围内。
- 使用 `-XX:AutoBoxCacheMax=4096` 扩大 Integer 缓存（按需，注意堆内存开销）。

**（3）缓存数据结构**

包装类缓存在堆中的结构是一个**静态 final 数组**，在类加载的 `<clinit>` 阶段一次性分配：

```java
// IntegerCache 初始化时在堆中分配一个 Integer[high - low + 1]
// 例如默认 high=127, low=-128 → 分配 256 个 Integer 对象
// 每个 Integer（开启指针压缩时）：16 字节对象头 + 4 字节 int = 20 字节
// 256 × 20 ≈ 5 KB 内存，对堆几乎无感知
```

`Float` 和 `Double` 没有缓存，因为浮点数在任意区间都有无穷多个值，缓存命中率极低，设计上放弃了缓存。

::: tabs

@tab **`Integer` 缓存**

```java
public static Integer valueOf(int i) {
    if (i >= IntegerCache.low && i <= IntegerCache.high)
        return IntegerCache.cache[i + (-IntegerCache.low)];
    return new Integer(i);
}
private static class IntegerCache {
    static final int low = -128;
    static final int high;
    static {
        // high value may be configured by property
        int h = 127;
    }
}
```

@tab **`Character` 缓存**

```java
public static Character valueOf(char c) {
    if (c <= 127) { // must cache
      return CharacterCache.cache[(int)c];
    }
    return new Character(c);
}

private static class CharacterCache {
    private CharacterCache(){}
    static final Character cache[] = new Character[127 + 1];
    static {
        for (int i = 0; i < cache.length; i++)
            cache[i] = new Character((char)i);
    }

}
```

@tab **`Boolean` 缓存**

```java
public static Boolean valueOf(boolean b) {
    return (b ? TRUE : FALSE);
}
```

@tab `Float` 和 `Double` 无缓存

两种浮点数类型的包装类 `Float`、`Double` 并没有实现缓存机制。

```java
Integer i1 = 33;
Integer i2 = 33;
System.out.println(i1 == i2);// 输出 true

Float i11 = 333f;
Float i22 = 333f;
System.out.println(i11 == i22);// 输出 false

Double i3 = 1.2;
Double i4 = 1.2;
System.out.println(i3 == i4);// 输出 false
```

:::

下面我们来看一个问题：下面的代码的输出结果是 `true` 还是 `false` 呢？

```java
Integer i1 = 40;
Integer i2 = new Integer(40);
System.out.println(i1==i2);
```

`Integer i1=40` 这一行代码会发生装箱，也就是说这行代码等价于 `Integer i1=Integer.valueOf(40)` 。因此，`i1` 直接使用的是缓存中的对象。而`Integer i2 = new Integer(40)` 会直接创建新的对象。

因此，答案是 `false` 。你答对了吗？

值得一提的是，包装类通过缓存一定范围的常用数值，避免重复创建对象，以减少内存使用的思想，正是采用了**享元模式**（设计模式之一）。

记住：**所有整型包装类对象之间值的比较，全部使用 equals 方法比较**。

### 【简单】比较包装类型为什么不能用 ==？⭐⭐⭐

Java 值类型的包装类大部分都使用了缓存机制来提升性能：

- `Byte`、`Short`、`Integer`、`Long` 这 4 种包装类，默认都创建了数值在 **[-128，127]** 范围之间的相应类型缓存数据；
- `Character` 创建了数值在 **[0,127]** 范围之间的缓存数据；
- `Boolean` 直接返回 `True` or `False`；

试图装箱的数值，如果超出缓存范围，则会创建新的对象。

以 `Long.valueOf` 方法为例：

```java
public static Long valueOf(long l) {
    final int offset = 128;
    if (l >= -128 && l <= 127) { // will cache
        return LongCache.cache[(int)l + offset];
    }
    return new Long(l);
}
```

### 【中等】为什么浮点数运算的时候会有精度丢失的风险？⭐⭐⭐

浮点数运算精度丢失的根本原因是 **IEEE 754 标准用有限的二进制位表示实数**，很多十进制小数无法精确转换为二进制小数（就像 1/3 无法精确表示为十进制小数 0.333...）。

**（1）IEEE 754 浮点数结构**

Java 的 `float`（32 位）和 `double`（64 位）遵循 IEEE 754 标准，格式为：

```
float (32位):  | 符号 S (1bit) | 指数 E (8bit) | 尾数 M (23bit) |
double (64位): | 符号 S (1bit) | 指数 E (11bit) | 尾数 M (52bit) |

数值 = (-1)^S × (1.M) × 2^(E - bias)
- float 的 bias = 127，double 的 bias = 1023
- 尾数隐含 1，实际精度为 24bit (float) / 53bit (double)
```

以 0.1 为例，它无法用二进制精确表示，转换成二进制是一个无限循环小数：

```
0.1 × 2 = 0.2 → 0
0.2 × 2 = 0.4 → 0
0.4 × 2 = 0.8 → 0
0.8 × 2 = 1.6 → 1
0.6 × 2 = 1.2 → 1
0.2 × 2 = 0.4 → 0（开始循环）
...
0.1₁₀ = 0.0001100110011...₂（无限循环）
```

由于尾数位数有限（float 23bit，double 52bit），超出部分被**舍入（rounding）**，造成精度损失。

**（2）精度丢失的三种典型场景**

**场景一：十进制小数无法精确表示**

```java
float a = 2.0f - 1.9f;
float b = 1.8f - 1.7f;
System.out.println(a);      // 0.100000024（非 0.1）
System.out.println(b);      // 0.099999905（非 0.1）
System.out.println(a == b); // false
```

**场景二：大数吃小数** — 数量级差距过大时，加法可能被忽略：

```java
double big = 1.0e16;
double small = 1.0;
System.out.println(big + small == big);  // true！small 被"吃掉"
// 原因：big 和 small 数量级差 10^16，small 的尾数在对阶时全部移出，变成 0
```

**场景三：累加误差放大** — 大量浮点数累加时误差逐步累积：

```java
double sum = 0.0;
for (int i = 0; i < 100000; i++) {
    sum += 0.1;  // 每次加 0.1 都有微小误差
}
System.out.println(sum);  // 10000.000000018848（非精确 10000.0）
```

**（3）为什么这个问题在 Java 中无法避免？**

- Java 没有内置的 decimal 类型（MySQL 有 DECIMAL，C# 有 decimal），`float`/`double` 就是 IEEE 754 二进制浮点数。
- `BigDecimal` 是类库方案，语言层面不做特殊处理。
- JVM 字节码 `fadd`/`dadd` 直接映射到 CPU 浮点指令，CPU 本身就是按 IEEE 754 运算的。

### 【简单】如何解决浮点数运算的精度丢失问题？⭐⭐⭐

**方案一：`BigDecimal`（推荐，金融计算首选）**

`BigDecimal` 内部结构是一个 `BigInteger`（无精度损失的整数）加上一个 `scale`（小数位数），本质是用整数运算替代浮点运算：

```java
// BigDecimal 内部表示：unscaledValue × 10^(-scale)
// new BigDecimal("0.1") → unscaledValue=1, scale=1
// new BigDecimal("1.5")  → unscaledValue=15, scale=1
```

**三条铁律**：

- 构造必用字符串或整数：`new BigDecimal("0.1")` 而非 `new BigDecimal(0.1)`（后者先转 double，误差已产生）。
- 运算用 `add/subtract/multiply/divide` 方法。
- 除法必须指定精度和舍入模式（`RoundingMode`），否则遇除不尽抛 `ArithmeticException`。

```java
BigDecimal a = new BigDecimal("1.0");
BigDecimal b = new BigDecimal("0.9");
BigDecimal c = new BigDecimal("0.8");

BigDecimal x = a.subtract(b);   // 0.1（精确）
BigDecimal y = b.subtract(c);   // 0.1（精确）
x.compareTo(y) == 0;             // true（精确比较，用 compareTo 不用 equals）

// 除法必须指定精度和舍入模式
BigDecimal d = new BigDecimal("1");
BigDecimal e = new BigDecimal("3");
d.divide(e, 4, RoundingMode.HALF_UP);  // 0.3333
```

**常用舍入模式**：

| 模式        | 规则                   |      示例 (保留 2 位)      |
| ----------- | ---------------------- | :------------------------: |
| `HALF_UP`   | 四舍五入               |        2.345 → 2.35        |
| `HALF_EVEN` | 银行家舍入（统计更准） | 2.345 → 2.34, 2.355 → 2.36 |
| `CEILING`   | 向正无穷取整           |        2.341 → 2.35        |
| `FLOOR`     | 向负无穷取整           |        2.349 → 2.34        |

::: warning BigDecimal 的性能代价

- `BigDecimal` 运算比基本类型 `double` 慢 **100-300 倍**（对象分配 + 高精度运算）。
- 高并发场景避免大量创建 `BigDecimal`，考虑用 `long` 放大单位（如金额用"分"表示）。
- 比较时用 `compareTo()`，不用 `equals()`：`equals()` 同时比较值和 scale（`2.0` ≠ `2.00`），而 `compareTo()` 只比较值。

:::

**方案二：放大整数运算（高并发 + 精度敏感场景）**

```java
// 金融场景：金额用 long 表示"分"，避免浮点和 BigDecimal 开销
long price1 = 199;  // 1.99 元
long price2 = 299;  // 2.99 元
long total = price1 + price2;  // 498 分 = 4.98 元，精确不丢精度
```

**方案选择决策树**：

- 科学计算 / 允许微小误差 → `double`（性能最好）
- 金融 / 需要精确小数 → `BigDecimal`（精度最高）
- 高并发 + 固定精度（如金额）→ `long` 放大单位（性能最好 + 精度不丢）

### 【简单】超过 long 整型的数据应该如何表示？⭐

基本数值类型都有一个表达范围，如果超过这个范围就会有数值溢出的风险。

在 Java 中，64 位 long 整型是最大的整数类型。

```java
long l = Long.MAX_VALUE;
System.out.println(l + 1); // -9223372036854775808
System.out.println(l + 1 == Long.MIN_VALUE); // true
```

**`BigInteger` 内部使用 `int[]` 数组来存储任意大小的整形数据**。

相对于常规整数类型的运算来说，`BigInteger` 运算的效率会相对较低。

### 【中等】自动装箱拆箱有哪些陷阱？⭐⭐

自动装箱/拆箱看似优雅，实则隐藏多个易踩的坑：

**陷阱一：Integer 缓存范围导致 `==` 结果不一致**

```java
Integer a = 100;  // 装箱，命中缓存 [-128, 127]
Integer b = 100;
System.out.println(a == b);  // true

Integer c = 200;  // 装箱，超出缓存范围，new 新对象
Integer d = 200;
System.out.println(c == d);  // false（不同对象）
```

**核心结论**：整型包装类对象比较**必须用 `equals()`**，禁止用 `==`。

**陷阱二：自动拆箱导致 `NullPointerException`**

```java
Integer count = null;
int sum = count + 1;  // 拆箱时 NPE！count.intValue() 抛出
```

**陷阱三：循环中频繁装箱的性能损耗**

```java
Integer sum = 0;
for (int i = 0; i < 10000; i++) {
    sum += i;  // 每次循环：拆箱 sum → 相加 → 装箱新 Integer
}
// 应改为：int sum = 0;
```

**陷阱四：三元运算符的类型提升**

```java
Integer a = 1;
Integer b = 2;
Integer c = null;
Integer result = true ? a : c;       // 安全，result = 1
int r2 = false ? b : c;              // NPE！c 被拆箱
```

**陷阱五：方法重载选择歧义**

```java
void test(Integer i) { System.out.println("Integer"); }
void test(int i)     { System.out.println("int"); }

test(1);      // 输出 "int"（更精确匹配，优先选基本类型）
test(null);   // 编译错误：null 无法匹配 int，但匹配 Integer（需显式指定类型）
```

### 【中等】Java 是如何处理整数溢出的？⭐⭐

Java **不会自动检测整数溢出**，溢出后结果按二进制**静默回绕（wrap-around）**，不抛异常。

**溢出示例**：

```java
int max = Integer.MAX_VALUE;  // 2147483647
System.out.println(max + 1);  // -2147483648（回绕到 MIN_VALUE）

int min = Integer.MIN_VALUE;  // -2147483648
System.out.println(min - 1);  // 2147483647（回绕到 MAX_VALUE）
```

**安全的算术运算方法（Java 8+ `Math` 类）**：

| **方法**                   | **行为**             | **溢出时**               |
| -------------------------- | -------------------- | ------------------------ |
| `Math.addExact(a, b)`      | 加法                 | 抛 `ArithmeticException` |
| `Math.subtractExact(a, b)` | 减法                 | 抛 `ArithmeticException` |
| `Math.multiplyExact(a, b)` | 乘法                 | 抛 `ArithmeticException` |
| `Math.toIntExact(long)`    | long 转 int          | 抛 `ArithmeticException` |
| `Math.floorDiv(a, b)`      | 除法（向负无穷取整） | 不抛异常                 |

```java
try {
    int result = Math.addExact(Integer.MAX_VALUE, 1);  // 抛异常
} catch (ArithmeticException e) {
    System.out.println("溢出: " + e.getMessage());
}
```

**最佳实践**：

- 涉及大数运算用 `BigInteger`。
- 关键路径用 `Math.xxxExact()` 方法显式检查。
- 单元测试覆盖边界值（`MIN_VALUE`/`MAX_VALUE`）。

## Java 变量

### 【简单】静态变量、成员变量、局部变量的区别？⭐⭐

**静态变量、成员变量、局部变量的主要区别**

| **特性**     | **静态变量（static）**         | **成员变量（非 static）**      | **局部变量**               |
| ------------ | ------------------------------ | ------------------------------ | -------------------------- |
| **所属**     | 类（所有实例共享）             | 对象（每个实例独立）           | 方法/代码块内              |
| **生命周期** | 类加载时创建，程序结束时销毁   | 对象创建时存在，垃圾回收时销毁 | 方法调用时创建，执行完销毁 |
| **存储位置** | 方法区（JDK8+在元空间/堆）     | 堆（对象内部）                 | 栈（方法栈帧）             |
| **默认值**   | 有（如`int`默认为 0）          | 有（同静态变量）               | **无**（必须手动初始化）   |
| **访问方式** | `类名.变量名` 或 `对象.变量名` | `对象.变量名`                  | 只能在声明的方法/块内使用  |

**一句话总结**：

- **静态变量**：全局唯一，类共享。
- **成员变量**：对象私有，每个实例独立。
- **局部变量**：临时使用，方法内有效。

### 【简单】为什么成员变量有默认值？⭐⭐

**成员变量有默认值的核心原因是：防止随机值风险**。

- **内存安全**：未初始化的变量会指向内存中的随机值，可能导致程序行为异常或崩溃。
- **稳定运行**：自动赋默认值（如 `int`→`0`，`boolean`→`false`）确保程序逻辑可预测。

**编译器设计的权衡**

- **成员变量**：**自动赋默认值是内存安全与灵活性的平衡**。
  - 运行时可能通过反射、构造器等动态赋值，编译器无法完全静态检测。
  - 为避免误报错误，统一自动赋默认值。
- **局部变量**：**严格编译检查确保代码可靠性**。
  - 作用域限于方法内，编译器可严格检查是否赋值。
  - 强制手动初始化以规避潜在风险。

### 【简单】字符型常量和字符串常量的区别？⭐

| **场景**     | **字符常量**                              | **字符串常量**                      |
| :----------- | :---------------------------------------- | :---------------------------------- |
| **表示形式** | 单引号括起的**单个字符**（`'A'`）         | 双引号括起的**字符序列**（`"ABC"`） |
| **数据类型** | `char`（基本类型）                        | `String`（引用类型）                |
| **内存占用** | 2 字节（Unicode 字符，如 `'中'`、`'\n'`） | 对象开销+字符数据（可变长度）       |
| **转义字符** | 支持（`'\t'`、`'\\'`）                    | 同样支持（`"\t"`、`"\\"`）          |
| **空值表示** | 不可为空（至少 1 字符）                   | 可为空（`""`）                      |
| **运算行为** | 按 Unicode 值运算                         | 重载`+`为拼接                       |

## Java 方法

### 【简单】Java 方法有哪些类型？⭐

Java 方法的类型可以从不同维度分类。

::: tabs

@tab **按从属划分**

| **类型**     | **关键字** | **调用方式**       | **特点**                     | **示例**              |
| ------------ | ---------- | ------------------ | ---------------------------- | --------------------- |
| **实例方法** | 无         | `对象名.方法名 ()` | 依赖对象实例，可访问实例成员 | `list.add("item")`    |
| **静态方法** | `static`   | `类名.方法名 ()`   | 不依赖实例，只能访问静态成员 | `Math.abs(-1)`        |
| **构造方法** | 无         | `new 类名 ()`      | 用于对象初始化，无返回值类型 | `new String("hello")` |

@tab **按能否 `override` 划分**

| **类型**       | **关键字** | **特点**                    | **示例**                   |
| -------------- | ---------- | --------------------------- | -------------------------- |
| **普通方法**   | 无         | 可被重写（除非`final`修饰） | `public void show()`       |
| **final 方法** | `final`    | 禁止子类重写                | `public final void lock()` |
| **抽象方法**   | `abstract` | 无实现，需子类重写          | `abstract void draw();`    |
| **默认方法**   | `default`  | Java 8 接口中的默认实现     | `default void log()`       |

@tab **按参数与返回值划分**

| **类型**         | **特点**                  | **示例**                     |
| ---------------- | ------------------------- | ---------------------------- |
| **无参方法**     | 不需要参数                | `String getName()`           |
| **有参方法**     | 可接受基本类型/对象参数   | `void setAge(int age)`       |
| **可变参方法**   | 参数数量可变（`...`语法） | `void print(String... strs)` |
| **无返回值方法** | 返回类型为`void`          | `void shutdown()`            |
| **有返回值方法** | 必须返回指定类型值        | `int calculate()`            |

@tab **特殊方法**

| **类型**              | **特点**                       | **示例**                          |
| --------------------- | ------------------------------ | --------------------------------- |
| **native 方法**       | 用`native`声明，由本地代码实现 | `public native void start()`      |
| **synchronized 方法** | 用`synchronized`修饰，线程安全 | `public synchronized void save()` |
| **递归方法**          | 方法内部调用自身               | `int factorial(int n)`            |
| **泛型方法**          | 声明类型参数                   | `<T> T getData()`                 |

@tab **接口中的方法**

| **类型**     | **关键字** | **特点**                      |
| ------------ | ---------- | ----------------------------- |
| **抽象方法** | 无         | 默认`public abstract`         |
| **默认方法** | `default`  | Java 8 引入，提供默认实现     |
| **静态方法** | `static`   | Java 8 引入，接口直接调用     |
| **私有方法** | `private`  | Java 9 引入，仅供接口内部使用 |

:::

**代码示例**

```java
// 实例方法 vs 静态方法
class Calculator {
    // 实例方法
    public int add(int a, int b) { return a + b; }

    // 静态方法
    public static int staticAdd(int a, int b) { return a + b; }
}

// 抽象方法
abstract class Shape {
    abstract void draw(); // 必须由子类实现
}

// 默认方法
interface Logger {
    default void log(String msg) { System.out.println(msg); }
}

// 泛型方法
class Box {
    public <T> T wrap(T item) { return item; }
}
```

::: info 如何选择方法类型？
:::

- **需要操作对象状态** → 实例方法（如`user.getName()`）
- **工具类操作** → 静态方法（如`Collections.sort()`）
- **强制子类实现** → 抽象方法（如`Animal.eat()`）
- **接口功能扩展** → 默认方法（Java 8+）
- **线程安全控制** → `synchronized`方法

### 【简单】静态方法和实例方法有何不同？⭐⭐

**静态方法和实例方法主要区别**：

| **维度**     | **静态方法 (Static Method)**                    | **实例方法 (Instance Method)**       |
| ------------ | ----------------------------------------------- | ------------------------------------ |
| **归属**     | 属于类                                          | 属于对象实例                         |
| **关键字**   | 使用 `static` 修饰                              | 无 `static` 修饰                     |
| **调用方式** | `类名.方法名 ()`                                | `对象名.方法名 ()`                   |
| **内存分配** | 类加载时分配，永久代（JDK8 前）/元空间（JDK8+） | 对象实例化时分配，堆内存             |
| **生命周期** | 与类相同（从类加载到 JVM 退出）                 | 与对象相同（从对象创建到被 GC 回收） |

**访问权限对比**：

| **维度**         | **静态方法**                    | **实例方法**  |
| ---------------- | ------------------------------- | ------------- |
| **访问静态成员** | ✔️ 可直接访问                   | ✔️ 可直接访问 |
| **访问实例成员** | ❌ 不能直接访问（需先创建对象） | ✔️ 可直接访问 |
| **this/super**   | ❌ 不可使用                     | ✔️ 可使用     |

**代码示例**：

```java
class Calculator {
    // 静态方法
    public static int add(int a, int b) {
        return a + b;  // 不依赖对象状态
    }

    // 实例方法
    private int base;
    public void setBase(int base) {
        this.base = base;  // 依赖对象状态
    }
    public int calculate(int x) {
        return base + x;  // 访问实例变量
    }
}

// 调用示例
public class Main {
    public static void main(String[] args) {
        // 静态方法调用
        int sum = Calculator.add(3, 5);  // 无需创建对象

        // 实例方法调用
        Calculator calc = new Calculator();
        calc.setBase(10);
        int result = calc.calculate(5);  // 需要对象实例
    }
}
```

### 【简单】重载和重写有什么区别？⭐⭐⭐

**Java 重载（Overload）与重写（Override）的核心区别**：

| **特性**     | **重载（Overload）**               | **重写（Override）**                                    |
| ------------ | ---------------------------------- | ------------------------------------------------------- |
| **定义**     | 同一类中方法名相同但参数不同       | 子类重新实现父类的方法                                  |
| **目的**     | 处理不同类型/数量的参数            | 修改或扩展父类方法的行为                                |
| **多态类型** | 编译时多态（静态绑定）             | 运行时多态（动态绑定）                                  |
| **作用范围** | 同一类中（或父子类间）             | 子类与父类之间                                          |
| **方法签名** | **必须不同参数**（类型/数量/顺序） | **必须完全相同**（方法名+参数）                         |
| **返回值**   | 可自由修改                         | 基本类型/void：必须相同；引用类型：可协变（子类更具体） |
| **异常**     | 可自由声明                         | 子类异常 ≤ 父类异常范围                                 |
| **访问权限** | 可自由修改                         | 子类权限 ≥ 父类（不能更严格）                           |
| **限制方法** | 无                                 | 不能重写 `private`/`final`/`static` 方法                |

::: code-tabs#重载和重写的示例

@tab 重载示例

```java
class Calculator {
    // 参数类型不同
    int add(int a, int b) { return a + b; }
    double add(double a, double b) { return a + b; }

    // 参数数量不同
    int add(int a, int b, int c) { return a + b + c; }
}
```

@tab 重写示例

```java
class Animal {
    protected String sound() { return "Unknown sound"; }
}

class Cat extends Animal {
    @Override
    public String sound() {  // 访问权限扩大，返回值相同
        return "Meow";
    }
}
```

:::

::: note 关键区别总结

- **绑定时机**
  - 重载：编译时根据参数决定调用的方法（`Calculator.add(int)` vs `Calculator.add(double)`）
  - 重写：运行时根据对象实际类型决定方法（`Animal.sound()` 实际调用 `Cat.sound()`）
- **设计目的**
  - 重载：**横向扩展**（同一功能的不同参数版本）
  - 重写：**纵向覆盖**（子类定制父类行为）
- **验证阶段**
  - 重载：编译器检查参数差异
  - 重写：编译器检查方法签名 + JVM 运行时验证

:::

### 【简单】什么是可变长参数？⭐⭐

从 Java5 开始，Java 支持定义可变长参数，所谓可变长参数就是允许在调用方法时传入不定长度的参数。就比如下面这个方法就可以接受 0 个或者多个参数。

```java
public static void method1(String... args) {
   //......
}
```

另外，可变参数只能作为函数的最后一个参数，但其前面可以有也可以没有任何其他参数。

```java
public static void method2(String arg1, String... args) {
   //......
}
```

**遇到方法重载的情况怎么办呢？会优先匹配固定参数还是可变参数的方法呢？**

答案是会优先匹配固定参数的方法，因为固定参数的方法匹配度更高。

我们通过下面这个例子来证明一下。

```java
public class VariableLengthArgument {

    public static void printVariable(String... args) {
        for (String s : args) {
            System.out.println(s);
        }
    }

    public static void printVariable(String arg1, String arg2) {
        System.out.println(arg1 + arg2);
    }

    public static void main(String[] args) {
        printVariable("a", "b");
        printVariable("a", "b", "c", "d");
    }
}
```

输出：

```
ab
a
b
c
d
```

另外，Java 的可变参数编译后实际会被转换成一个数组，我们看编译后生成的 `class` 文件就可以看出来了。

```java
public class VariableLengthArgument {

    public static void printVariable(String... args) {
        String[] var1 = args;
        int var2 = args.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            String s = var1[var3];
            System.out.println(s);
        }

    }
    // ......
}
```

## Java 异常

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2025/04/211503dd66164d2d94ba14bd5ed56c26.webp)

### 【简单】Exception 和 Error 有什么区别？⭐⭐⭐

在 Java 中，所有的异常都有一个共同的祖先 `java.lang` 包中的 `Throwable` 类。`Throwable` 类有两个重要的子类：

- **`Exception`** - 程序本身可以处理的异常，可以通过 `catch` 来进行捕获。`Exception` 又分为**检查**（checked）异常和**非检查**（unchecked）异常，检查异常在源代码里必须显式地进行捕获处理，这是编译期检查的一部分。
- **`Error`** - `Error` 属于程序无法处理的错误。例如 Java 虚拟机运行错误（`VirtualMachineError`）、虚拟机内存不够错误（`OutOfMemoryError`）、类定义错误（`NoClassDefFoundError`）等 。这些异常发生时，Java 虚拟机（JVM）一般会选择线程终止。

**（1）Throwable 异常体系全景**

```
Throwable
├── Error（程序无法处理，不应 catch）
│   ├── VirtualMachineError
│   │   ├── OutOfMemoryError（堆/元空间/直接内存耗尽）
│   │   └── StackOverflowError（递归过深或栈空间不足）
│   ├── NoClassDefFoundError（运行时找不到类定义）
│   └── AssertionError（断言失败）
└── Exception（程序可处理）
    ├── RuntimeException（非检查异常，编译期不强制处理）
    │   ├── NullPointerException
    │   ├── IllegalArgumentException
    │   ├── IndexOutOfBoundsException
    │   └── ConcurrentModificationException
    └── 其他 Exception（检查异常，编译期强制处理）
        ├── IOException
        ├── SQLException
        └── InterruptedException
```

**（2）JVM 层面：异常表（Exception Table）**

每个方法的字节码中都包含一个**异常表**，记录了 `try-catch` 块的范围映射：

```java
// 源码
try { a = 1; } catch (Exception e) { a = 2; }

// 字节码异常表（Code 属性中的 exception_table）
// from   to   target   type
//   0     4      7     java/lang/Exception
// 含义：字节码偏移 0~3 之间若抛出 Exception，跳转到偏移 7 执行 catch 块
```

这体现了 JVM 的**"零成本异常"哲学**：无异常时不产生任何额外开销（不像 C++ 需要维护异常处理数据结构）。但一旦抛出异常，JVM 需要遍历调用栈构建 `StackTraceElement[]`，成本极高。

**（3）异常的性能代价（L3 标准：能量化分析）**

`Throwable.fillInStackTrace()` 是核心瓶颈——需要遍历当前线程的调用栈帧，逐帧构建 `StackTraceElement[]` 数组：

```
正常流程：约 0.001 μs
抛异常并捕获：约 50-100 μs（慢 5 万-10 万倍）
核心开销 = fillInStackTrace() 遍历调用栈 + 创建 StackTraceElement[] 数组
```

**优化策略**：

- 不要用异常做流程控制（如用返回值而非抛异常表示"未找到"）。
- 高频异常可重写 `fillInStackTrace()` 为空方法（不记录栈，但定位困难）。
- 复用静态异常实例（如 Spring 的 `NestedRuntimeException` 模式），但注意栈信息会错乱。

**（4）异常风暴 → Full GC 因果链**

```
高频异常 → 大量 StackTraceElement[] 数组分配 → 年轻代快速填满
→ 对象晋升老年代 → 老年代达到阈值 → CMS Concurrent Mode Failure / G1 Full GC
```

生产案例：某系统日志框架 Bug 导致每秒 10 万次 NPE，每次 NPE 的 `fillInStackTrace()` 分配约 2KB 的 `StackTraceElement[]`，每秒 200MB 对象分配，3 秒一次 Full GC，CPU 打满。

**（5）JEP 358 Helpful NPE（JDK 14+）**

JDK 14 引入 Helpful NullPointerExceptions，在 NPE message 中嵌入具体变量名：

```java
// JDK 14 前
a.b.c.d();  // NullPointerException（不知道哪个是 null）

// JDK 14 后（-XX:+ShowCodeDetailsInExceptionMessages，默认开启）
a.b.c.d();  // NullPointerException: Cannot invoke "X.c" because "a.b" is null
```

JVM 通过字节码分析定位 null 变量，无需修改代码即可获得精确信息。

### 【简单】Checked Exception 和 Unchecked Exception 有什么区别？⭐⭐

**差异对比**：

| **特性**     | **Checked Exception**                              | **Unchecked Exception**          |
| ------------ | -------------------------------------------------- | -------------------------------- |
| **编译检查** | 必须显式处理（`catch`/`throws`），否则编译失败     | 不强制处理，编译可通过           |
| **继承体系** | 继承自 `Exception`（非 `RuntimeException` 分支）   | 继承自 `RuntimeException`        |
| **设计目的** | 处理**可预见的、可恢复的**异常情况（如文件不存在） | 处理**程序逻辑错误**（如空指针） |

::: tabs#Checked Exception 和 Unchecked Exception 示例对比

@tab **Checked Exception 示例**

```java
// 必须处理 IOException（受检异常）
try {
    Files.readAllBytes(Paths.get("file.txt"));
} catch (IOException e) {  // 或声明 throws IOException
    System.err.println("文件读取失败：" + e.getMessage());
}
```

@tab **Unchecked Exception 示例**

```java
// 可不处理 NullPointerException（非受检异常）
String str = null;
System.out.println(str.length());  // 运行时抛出 NullPointerException
```

:::

**常见异常类型**

| **Checked Exception**    | **Unchecked Exception**          |
| ------------------------ | -------------------------------- |
| `IOException`            | `NullPointerException`           |
| `SQLException`           | `IllegalArgumentException`       |
| `ClassNotFoundException` | `ArrayIndexOutOfBoundsException` |
| `InterruptedException`   | `ClassCastException`             |

**选择原则**

- **用 Checked Exception**：

  - 调用方**必须处理**该异常（如文件不存在、网络断开）
  - 异常是业务逻辑的**合法流程**（如用户输入校验）

- **用 Unchecked Exception**：
  - 表示**程序错误**（如参数为 null、数组越界）
  - 调用方**无法合理恢复**（如内存溢出）

### 【简单】Throwable 类常用方法有哪些？⭐⭐

- `String getMessage()`: 返回异常发生时的简要描述
- `String toString()`: 返回异常发生时的详细信息
- `String getLocalizedMessage()`: 返回异常对象的本地化信息。使用 `Throwable` 的子类覆盖这个方法，可以生成本地化信息。如果子类没有覆盖该方法，则该方法返回的信息与 `getMessage()` 返回的结果相同
- `void printStackTrace()`: 在控制台上打印 `Throwable` 对象封装的异常信息

### 【简单】try-catch-finally 如何使用？⭐⭐⭐

- `try`块：用于捕获异常。其后可接零个或多个 `catch` 块，如果没有 `catch` 块，则必须跟一个 `finally` 块。
- `catch`块：用于处理 try 捕获到的异常。
- `finally` 块：无论是否捕获或处理异常，`finally` 块里的语句都会被执行。当在 `try` 块或 `catch` 块中遇到 `return` 语句时，`finally` 语句块将在方法返回之前被执行。

代码示例：

```java
try {
    System.out.println("Try to do something");
    throw new RuntimeException("RuntimeException");
} catch (Exception e) {
    System.out.println("Catch Exception -> " + e.getMessage());
} finally {
    System.out.println("Finally");
}
```

输出：

```
Try to do something
Catch Exception -> RuntimeException
Finally
```

**注意：不要在 finally 语句块中使用 return!** 当 try 语句和 finally 语句中都有 return 语句时，try 语句块中的 return 语句会被忽略。这是因为 try 语句中的 return 返回值会先被暂存在一个本地变量中，当执行到 finally 语句中的 return 之后，这个本地变量的值就变为了 finally 语句中的 return 返回值。

[jvm 官方文档](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.10.2.5) 中有明确提到：

> If the `try` clause executes a _return_, the compiled code does the following:
>
> 1. Saves the return value (if any) in a local variable.
> 2. Executes a _jsr_ to the code for the `finally` clause.
> 3. Upon return from the `finally` clause, returns the value saved in the local variable.

代码示例：

```java
public static void main(String[] args) {
    System.out.println(f(2));
}

public static int f(int value) {
    try {
        return value * value;
    } finally {
        if (value == 2) {
            return 0;
        }
    }
}
```

输出：

```
0
```

### 【简单】finally 中的代码一定会执行吗？⭐⭐

不一定的！在某些情况下，finally 中的代码不会被执行。

就比如说 finally 之前虚拟机被终止运行的话，finally 中的代码就不会被执行。

```java
try {
    System.out.println("Try to do something");
    throw new RuntimeException("RuntimeException");
} catch (Exception e) {
    System.out.println("Catch Exception -> " + e.getMessage());
    // 终止当前正在运行的 Java 虚拟机
    System.exit(1);
} finally {
    System.out.println("Finally");
}
```

输出：

```
Try to do something
Catch Exception -> RuntimeException
```

另外，在以下 2 种特殊情况下，`finally` 块的代码也不会被执行：

1. 程序所在的线程死亡。
2. 关闭 CPU。

### 【简单】如何使用 `try-with-resources` 代替`try-catch-finally`？⭐⭐⭐

1. **适用范围（资源的定义）：** 任何实现 `java.lang.AutoCloseable`或者 `java.io.Closeable` 的对象
2. **关闭资源和 finally 块的执行顺序：** 在 `try-with-resources` 语句中，任何 `catch` 或 `finally` 块在声明的资源关闭后运行

> 《Effective Java》中明确指出：
>
> 面对必须要关闭的资源，我们总是应该优先使用 `try-with-resources` 而不是`try-finally`。随之产生的代码更简短，更清晰，产生的异常对我们也更有用。`try-with-resources`语句让我们更容易编写必须要关闭的资源的代码，若采用`try-finally`则几乎做不到这点。

Java 中类似于`InputStream`、`OutputStream`、`Scanner`、`PrintWriter`等的资源都需要我们调用`close()`方法来手动关闭，一般情况下我们都是通过`try-catch-finally`语句来实现这个需求，如下：

```java
//读取文本文件的内容
Scanner scanner = null;
try {
    scanner = new Scanner(new File("D://read.txt"));
    while (scanner.hasNext()) {
        System.out.println(scanner.nextLine());
    }
} catch (FileNotFoundException e) {
    e.printStackTrace();
} finally {
    if (scanner != null) {
        scanner.close();
    }
}
```

使用 Java 7 之后的 `try-with-resources` 语句改造上面的代码：

```java
try (Scanner scanner = new Scanner(new File("test.txt"))) {
    while (scanner.hasNext()) {
        System.out.println(scanner.nextLine());
    }
} catch (FileNotFoundException fnfe) {
    fnfe.printStackTrace();
}
```

当然多个资源需要关闭的时候，使用 `try-with-resources` 实现起来也非常简单，如果你还是用`try-catch-finally`可能会带来很多问题。

通过使用分号分隔，可以在`try-with-resources`块中声明多个资源。

```java
try (BufferedInputStream bin = new BufferedInputStream(new FileInputStream(new File("test.txt")));
     BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(new File("out.txt")))) {
    int b;
    while ((b = bin.read()) != -1) {
        bout.write(b);
    }
}
catch (IOException e) {
    e.printStackTrace();
}
```

### 【简单】NoClassDefFoundError 和 ClassNotFoundException 有什么区别⭐⭐

`NoClassDefFoundError`是一个 Error，而 `ClassNotFoundException` 是一个 Exception。

`ClassNotFoundException` 产生的原因：

- 使用 `Class.forName`、`ClassLoader.loadClass`、`ClassLOader.findSystemClass` 方法动态加载类，如果这个类没有被找到，那么就会在运行时抛出 `ClassNotFoundException` 异常；
- 当一个类已经被某个类加载器加载到内存中了，此时另一个类加载器又尝试着动态地从同一个包中加载这个类。

`NoClassDefFoundError` 产生的原因：当 JVM 或 `ClassLoader` 试图加载类，却找不到类的定义时（编译时存在，运行时找不到），抛出异常。

### 【简单】异常使用有哪些需要注意的地方？⭐⭐

- 不要把异常定义为静态变量，因为这样会导致异常栈信息错乱。每次手动抛出异常，我们都需要手动 new 一个异常对象抛出。
- 抛出的异常信息一定要有意义。
- 建议抛出更加具体的异常比如字符串转换为数字格式错误的时候应该抛出`NumberFormatException`而不是其父类`IllegalArgumentException`。
- 避免重复记录日志：如果在捕获异常的地方已经记录了足够的信息（包括异常类型、错误信息和堆栈跟踪等），那么在业务代码中再次抛出这个异常时，就不应该再次记录相同的错误信息。重复记录日志会使得日志文件膨胀，并且可能会掩盖问题的实际原因，使得问题更难以追踪和解决。
- ……

### 【中等】Java 中 final、finally 和 finalize 有什么区别？⭐⭐⭐

| 特性         | final                                                | finally                                          | finalize                   |
| :----------- | :--------------------------------------------------- | :----------------------------------------------- | :------------------------- |
| **类型**     | 关键字                                               | 代码块                                           | 方法                       |
| **作用域**   | 变量/方法/类                                         | 异常处理块                                       | Object 类方法              |
| **作用**     | 声明不可变性                                         | 即使有异常也必然执行，确保资源释放               | 对象回收前的清理（已废弃） |
| **特点**     | 可修饰变量（常量）、方法（不可重写）、类（不可继承） | 与`try-catch`搭配，**必然执行**（除非 JVM 退出） | 不推荐用，执行时机不可控   |
| **使用场景** | 定义常量/限制继承                                    | 资源清理                                         | 历史遗留的清理逻辑         |

**一句话总结**：`final`管**不变性**，`finally`管**必执行**，`finalize`是**过时的清理机制**。

**（1）finalize 为什么被废弃？**

JDK 9 标记 `finalize()` 为 `@Deprecated`，JDK 18 标记为 `@Deprecated(forRemoval=true)`。原因：

- **执行时机不可控**：对象从"可回收"到 `finalize()` 实际执行可能间隔数秒甚至更久，GC 不会为执行 `finalize()` 而等待。
- **性能代价巨大**：覆盖了 `finalize()` 的对象，GC 需要两次回收才能清除（第一次进入 `Finalizer` 队列，第二次才能真正回收）。
- **可能导致 OOM**：`Finalizer` 线程优先级低，若队列积压过多，未回收对象持续占用内存。
- **安全风险**：`finalize()` 中可能"复活"对象（重新赋值给静态变量），导致安全漏洞。

**（2）替代方案：Cleaner API（JDK 9+）**

```java
// 旧方案：覆盖 finalize()
class LegacyResource {
    @Override
    protected void finalize() { /* 清理 */ }
}

// 新方案：Cleaner + PhantomReference
class ModernResource implements AutoCloseable {
    private static final Cleaner cleaner = Cleaner.create();
    private final Cleaner.Cleanable cleanable;

    public ModernResource() {
        this.cleanable = cleaner.register(this, () -> {
            // 清理逻辑（不持有 this 引用，避免复活）
            System.out.println("资源已清理");
        });
    }

    @Override
    public void close() {
        cleanable.clean();  // 显式清理
    }
}
```

**（3）实际开发建议**

- 永远不要重写 `finalize()`。
- 资源清理用 `try-with-resources`（实现 `AutoCloseable`）+ 显式 `close()`。
- 堆外内存清理用 `Cleaner` + `PhantomReference`（如 Netty 的 `ByteBuf`）。
- 如果维护老代码遇到 `finalize()`，逐步迁移到 `Cleaner`。

### 【简单】`instanceof` 关键字的作用？⭐⭐

`instanceof` 用于**运行时类型检查**，判断对象是否是某个类、子类或接口的实例。

**基本语法**：

```java
obj instanceof Type  // 返回 boolean
```

**核心规则**：

- **`null` 永远返回 `false`**：`null instanceof Object` → `false`。
- **检查的是实际运行时类型**，不受声明类型影响。
- **接口/抽象类也能用**：`"abc" instanceof CharSequence` → `true`。
- **数组类型检查**：`new int[0] instanceof Object` → `true`（数组本质是 Object）。

**Java 16+ 模式匹配增强**：

```java
// 旧写法：先判断再强制转换（冗长）
if (obj instanceof String) {
    String s = (String) obj;
    System.out.println(s.length());
}

// Java 16+：模式匹配，自动绑定变量
if (obj instanceof String s) {
    System.out.println(s.length());  // s 已自动声明
}

// 结合流式判断
if (obj instanceof String s && s.length() > 5) {
    System.out.println("长字符串: " + s);
}
```

**性能说明**：

- `instanceof` 比 `getClass() == X.class` 更宽松（前者考虑继承关系，后者要求精确匹配）。
- 现代 JVM 已优化 `instanceof` 性能，无需过度担心开销。
