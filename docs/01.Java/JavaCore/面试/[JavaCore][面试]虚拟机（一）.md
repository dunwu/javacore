---
title: Java 虚拟机面试一
date: 2024-07-03 07:44:02
order: 10
categories:
  - Java
  - JavaCore
  - 面试
tags:
  - Java
  - JavaCore
  - 面试
  - JVM
permalink: /pages/a9f8d5df/
---

# Java 虚拟机面试一

## JVM 简介

### 【中等】说说 Java 的执行流程？⭐⭐⭐⭐

Java 程序的执行流程经历了从编译到字节码的生成，再到类加载和 JIT 编译的过程，最终在 JVM 中执行。并且在程序运行过程中，JVM 负责内存管理、垃圾回收和线程调度等工作。

主要流程如下：

1. **编码**：编写 `.java` 源码文件。
2. **编译**：Java 编译器（javac） 将 `.java` 文件编译为 `.class` 文件（字节码）。
3. **类加载**：JVM 通过类加载子系统加载 `.class` 文件到内存。
   1. **加载**：采用双亲委派机制，分层级加载字节码。
   2. **链接**
      1. **验证**：检查字节码合法性（如魔数 `0xCAFEBABE`）。
      2. **准备**：为静态变量分配内存并赋默认值（如 `static int a` 初始化为 `0`）。
      3. **解析**：将符号引用（如类名、方法名）转为直接引用（内存地址）。
   3. **初始化**：执行静态代码块（`static{}`）和静态变量赋值（如 `static int a = 1;`）。
4. **存储运行时数据区**：加载后的类信息存储到内存区域。
   - **方法区**：存储类结构（如 `HelloWorld` 的类名、方法定义、常量池）。
   - **堆**：存放对象实例（如 `String` 对象）。
   - **虚拟机栈**：线程私有，存储 `main()` 方法的栈帧（局部变量、操作数栈等）。
   - **程序计数器**：记录当前线程执行的字节码指令地址。
5. **执行阶段**
   - **解释执行**：逐行解释字节码指令（如 `invokestatic` 调用 `System.out.println`）。启动快，执行效率低。
   - **本地方法调用（JNI）**：若调用 `native` 方法（如 `Object.clone()`），通过 **JNI** 执行本地库（C/C++）代码。
   - **JIT 编译优化（可选）**：将热点代码（频繁执行的方法）编译为本地机器码。相关优化技术：**方法内联**、**逃逸分析**等。
6. **垃圾回收**：JVM 管理内存，并回收不再使用的对象。
7. **程序结束**：main 方法结束，退出程序。

### 【中等】JVM 由哪些部分组成？⭐⭐⭐⭐

**类加载→内存分配→执行引擎运行→GC 回收内存**，通过 JNI 与外部交互。

JVM（Java 虚拟机）主要由以下核心部分组成：

- **类加载子系统**：负责加载、验证、准备、解析和初始化类文件（.class）。
- **运行时数据区**：
  - **方法区**：存储类元数据、常量池等。
  - **堆**：存放对象实例（主 GC 区域）。
  - **虚拟机栈**：存储方法调用的栈帧（局部变量、操作数栈等）。
  - **本地方法栈**：为 Native 方法服务。
  - **程序计数器**：记录当前线程执行的字节码位置。
- **执行引擎**：解释或编译字节码为机器码执行（含 JIT 编译器）。
  - **解释器（Interpreter）**：逐行解释执行字节码（启动快，执行慢）。
  - **即时编译器（JIT Compiler）**：将热点代码（频繁执行的代码）编译为本地机器码（如 HotSpot 的 C1、C2 编译器）。
  - **垃圾回收器（GC）**：自动回收堆中无用的对象（如 Serial、Parallel、G1、ZGC 等算法）。
- **本地方法接口（JNI）**：调用 C/C++实现的 Native 方法。
- **本地方法库（Native Libraries）**：由其他语言（如 C/C++）编写的库，供 JNI 调用（如文件操作、网络通信等底层功能）。

![](https://raw.githubusercontent.com/dunwu/images/master/cs/java/javacore/jvm/jvm-hotspot-architecture.png)

### 【中等】Java 是如何实现跨平台的？⭐⭐

Java 实现跨平台的本质是：**源码 → 统一字节码 → JVM 按需转换为目标平台机器码**，通过分层抽象实现跨平台。

Java **【一次编写，到处执行（Write Once, Run Anywhere）】** 的要点：

- **JVM（Java 虚拟机）—— 统一运行环境**
  - 不同操作系统（Windows/Linux/macOS）安装对应的 JVM，**屏蔽底层硬件和系统差异**。
  - JVM 负责加载、验证并执行字节码，确保相同字节码在不同平台表现一致。
- **字节码（Bytecode）—— 平台无关的中间代码**
  - Java 代码编译成**平台无关的字节码（.class 文件）**，而非直接生成机器码。
  - 由 JVM 解释或 JIT 编译为当前平台的机器指令。
- **标准化的 Java API**：提供统一的 API（如 `java.io`、`java.net`），底层通过 JVM 适配不同操作系统的具体实现。
- **严格的规范与兼容性**：JVM 规范（如字节码格式、内存管理）和 Java 语言规范由 Oracle 统一制定，确保各厂商实现的 JVM 行为一致。

**例外情况（需注意）**

- **JNI（本地方法调用）**：依赖系统原生库时，需为不同平台编译对应的动态库（如 `.dll`、`.so`）。
- **平台相关细节**：如文件路径分隔符、字符编码、GUI 渲染等可能需要适配。

## 类加载

### 【中等】什么情况下 Java 类会被加载？⭐⭐⭐

Java 类采用 **懒加载** 机制，核心原则是 “**按需加载，节省内存**”：仅在发生「**主动引用**」时才触发加载（加载→链接→初始化），「**被动引用**」不会触发类初始化。

::: info 触发类加载的核心场景

:::

| 触发场景         | 示例代码                                                       | 关键词                                    |
| :--------------- | :------------------------------------------------------------- | :---------------------------------------- |
| **创建类实例**   | `new User()`、`new User[]{10}`（数组创建不初始化，但类需加载） | new 对象 / 数组（数组仅加载类，不初始化） |
| **访问静态方法** | `User.staticMethod()`                                          | 执行 static 方法                          |
| **访问静态字段** | `System.out.println(User.staticField)`（final 常量除外）       | 读 / 写 static 字段（常量不触发）         |
| **反射**         | `Class.forName("com.example.User")`                            | 反射获取 / 操作类                         |
| **初始化子类**   | 初始化子类时，先加载并初始化父类（接口除外）                   | 子类初始化 → 父类先加载                   |
| **启动类**       | 运行 `java Main` 时，加载并初始化 `Main` 类                    | 程序入口类必加载                          |
| **动态语言支持** | JDK 7+ `invokedynamic` 指令触发（如 Lambda 动态调用）          | 动态调用触发类加载                        |

【示例】触发类加载示例

```java
// 1. new 对象触发加载
User u = new User();

// 2. 调用静态方法触发加载
User.sayHello();

// 3. 反射触发加载
Class<?> clazz = Class.forName("com.example.User");

// 4. 子类初始化触发父类加载
class Parent {}
class Child extends Parent {}
Child c = new Child(); // 先加载 Parent，再加载 Child
```

::: info 不触发类加载的场景

:::

|      被动引用场景       |                            示例代码                             |                  原因说明                  |
| :---------------------: | :-------------------------------------------------------------: | :----------------------------------------: |
|     1. 访问静态常量     | `System.out.println(User.CONST_VAL)`（CONST_VAL 是 final 常量） | 常量编译期存入调用类常量池，无需加载定义类 |
| 2. 子类访问父类静态字段 |          `System.out.println(Child.parentStaticField)`          |          仅加载父类，子类不初始化          |
|      3. 数组引用类      |                  `User[] arr = new User[10];`                   |    仅创建数组对象，类仅加载（不初始化）    |
|    4. 类加载器加载类    |           `classLoader.loadClass("com.example.User")`           |     仅加载类（加载阶段），不执行初始化     |

【示例】不触发类加载示例

```java
// final 常量不触发 User 类初始化（编译期已存入当前类常量池）
public class Test {
    public static final String CONST = "hello";
    public static void main(String[] args) {
        System.out.println(User.CONST); // 不触发 User 类初始化
    }
}
```

### 【中等】Java 对象在虚拟机中怎样存储？⭐⭐

64 位 JVM 中，一个空`Object`占 16 字节（12 字节头 + 4 字节填充）。

每个 Java 对象在堆内存中分为 **3 个部分**：

- **对象头（Header）**
  - **Mark Word**：存储哈希码、GC 年龄、锁状态（如偏向锁信息）。
  - **类型指针**：指向类元数据的指针（压缩后占 4 字节，否则 8 字节）。
- **实例数据（Fields）**：对象的所有成员变量（包括继承的字段），按类型对齐存储。
- **对齐填充（Padding）**：确保对象大小为 8 字节的整数倍（优化 CPU 缓存行访问）。

**对象分配策略**

- **新生代分配**：大多数对象优先分配在 **Eden 区**（若开启 TLAB，线程先分配至私有缓冲区）。触发 Young GC 后，存活对象移至 Survivor 区或晋升老年代。
- **老年代分配**：大对象（如`-XX:PretenureSizeThreshold=1MB`）直接进入老年代。长期存活对象（年龄 > `MaxTenuringThreshold`）从 Survivor 晋升。

**分配方式**：

- **指针碰撞**（堆内存规整时，如 Serial 收集器）。
- **空闲列表**（堆内存碎片化时，如 CMS 收集器）。

### 【中等】Java 对象的创建过程是怎样的？⭐⭐⭐⭐

**对象创建经历五大步骤：类加载检查 → 分配内存 → 初始化零值 → 设置对象头 → 执行 `<init>` 方法**。

当 JVM 遇到 `new` 字节码指令时，按以下流程创建对象：

![](https://raw.githubusercontent.com/dunwu/images/master/cs/java/javacore/jvm/jvm-object-create.png)

#### 1. 类加载检查

- 首先去常量池中定位 `new` 指令的参数，检查该类的符号引用是否已被类加载器加载、解析和初始化。
- 若未加载，则先执行类加载过程（加载 → 验证 → 准备 → 解析 → 初始化）。

#### 2. 分配内存

在堆中为新对象分配内存，对象所需内存大小在类加载完成后即可确定。

**分配方式**：

| 分配方式     | 原理                                                               | 适用场景                     |
| :----------- | :----------------------------------------------------------------- | :--------------------------- |
| **指针碰撞** | 维护一个指针作为分界点，分配内存时将指针向空闲方向移动对象大小距离 | 堆内存规整（Serial、ParNew） |
| **空闲列表** | 维护一个记录可用内存块的列表，分配时找到足够大的块并更新列表       | 堆内存碎片化（CMS）          |

**线程安全**：

- **CAS + 失败重试**：分配内存时采用 CAS 保证原子性，失败则重试。
- **TLAB（Thread Local Allocation Buffer）**：每个线程在 Eden 区预分配一小块私有内存，线程在自己的 TLAB 上分配，避免同步开销（`-XX:+UseTLAB` 默认开启）。

#### 3. 初始化零值

- 分配内存后，JVM 将分配的内存空间（不含对象头）初始化为零值。
- 作用：保证对象实例字段无需赋初值即可直接使用（如 `int` 为 `0`、`boolean` 为 `false`、引用为 `null`）。

#### 4. 设置对象头

- **Mark Word**：设置哈希码、GC 分代年龄、锁状态等信息。
- **类型指针**：指向该对象的 Class 元数据，JVM 通过它确定对象属于哪个类。

#### 5. 执行 `<init>` 方法

- 执行构造函数（即 `<init>` 方法），为对象字段赋程序员定义的初始值。
- 此时一个真正可用的对象才算创建完成。

```java
// new 关键字对应的字节码
0: new           #2   // class User          → 步骤1-4（类加载检查、分配、零值、对象头）
3: dup                                 → 复制引用压入操作数栈
4: invokespecial #3   // Method User."<init>":()V  → 步骤5（执行构造方法）
7: astore_1                            → 存入局部变量表
```

::: info 跨语言对象创建对比（L4：演进视角）

:::

**C++ placement new 对比**

Java 的 `new` 关键字同时完成了内存分配和对象初始化，而 C++ 将这两个步骤解耦：

- **Java `new`**：`new User()` 一步完成内存分配（堆上）+ 零值初始化 + 构造函数调用。
- **C++ `new`**：`new User()` 调用 `operator new` 分配内存，然后调用构造函数。
- **C++ placement new**：`new (buffer) User()` 在已分配的内存上构造对象，不分配新内存。这在 C++ 中用于内存池、共享内存等场景。Java 中没有 placement new 的等价物——Java 的对象创建始终在堆上（或 TLAB 内），无法在自定义内存地址上构造对象。

```cpp
// C++ placement new：在预分配的内存上构造对象
char buffer[sizeof(User)];
User* u = new (buffer) User("张三");  // 不分配新内存，仅在 buffer 上构造
```

**Go 的逃逸分析（Escape Analysis）对比**

Go 的逃逸分析与 Java 的 TLAB 机制代表了两种不同的内存分配优化策略：

- **Java TLAB（Thread Local Allocation Buffer）**：每个线程在 Eden 区预分配一块私有缓冲区，线程在 TLAB 内分配对象无需同步。但对象**始终分配在堆上**（TLAB 是 Eden 的一部分），是否逃逸由 JIT 逃逸分析决定后续优化（如标量替换）。
- **Go 逃逸分析**：Go 编译器在**编译期**进行逃逸分析，自动决定对象分配在**栈上还是堆上**。如果编译器判断对象不会逃逸出当前 goroutine 的栈帧，则直接在栈上分配——这与 Java 的"标量替换等效栈上分配"有本质区别：Go 是真正的栈上分配完整对象。

```go
// Go 逃逸分析示例
func createUser() *User {
    u := User{Name: "张三"}  // 逃逸到堆（因为返回了指针）
    return &u
}

func processUser() {
    u := User{Name: "李四"}  // 未逃逸，分配在栈上
    fmt.Println(u.Name)
}
// 查看逃逸分析：go build -gcflags="-m"
```

| 维度     | Java TLAB               | Go 逃逸分析              | C++ placement new    |
| :------- | :---------------------- | :----------------------- | :------------------- |
| 分配位置 | 堆（TLAB 是 Eden 子集） | 栈或堆（编译器决定）     | 任意内存（灵活控制） |
| 决策时机 | 运行时（JIT）           | 编译期                   | 编译期（程序员决定） |
| 栈上分配 | 仅标量替换等效          | 完整对象栈分配           | 值类型自动栈分配     |
| 同步开销 | TLAB 内无锁             | 无锁（goroutine 栈私有） | 无锁                 |

### 【中等】Java 类的生命周期是怎样的？⭐⭐⭐

JVM 通过类加载子系统加载 `.class` 文件到内存。

Java 类的生命周期可以分为 7 个阶段：加载 → 链接（验证→准备→解析） → 初始化 → 使用 → （可能）卸载。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2025/05/02055428ea7649998581ed46519c8a9f.png)

- **加载（Loading）**：采用双亲委派机制，分层级加载字节码。
  - 读取 `.class` 文件，生成 `Class<?>` 对象。
  - 触发条件：`new`、访问静态成员、反射等。
- **链接（Linking）**
  - **验证（Verification）**：检查字节码合法性（如魔数、继承规则）。
  - **准备（Preparation）**：为静态变量分配内存并赋默认值（如 `static int a` 初始化为 `0`）。
  - **解析（Resolution）**：将符号引用（如类名、方法名）转为直接引用（内存地址）。
- **初始化（Initialization）**：执行静态代码块（`static{}`）和静态变量赋值（如 `static int a = 1;`）。
- **使用（Using）**：正常调用方法、创建实例。
- **卸载（Unloading）**
  - 条件：类无实例、`ClassLoader` 被回收、无 `Class<?>` 引用。
  - 典型场景：动态加载的类（如热部署）。

### 【困难】什么是类加载器？⭐⭐⭐

Java 类加载器是 **JVM（Java 虚拟机）** 的核心组件之一，负责在运行时动态加载 Java 类（`.class` 文件）到内存，并生成对应的 `Class<?>` 对象。

::: info 类加载器层次结构

:::

类加载器采用 **"双亲委派模型"** 进行层次化管理，确保类的唯一性和安全性。按层级自上而下有 4 种类加载器：

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2020/06/4e83752f655e439c80e9f04b746e37e5.png)

| 类加载器                                    | 加载范围                                     | 说明                                             |
| :------------------------------------------ | :------------------------------------------- | :----------------------------------------------- |
| **Bootstrap ClassLoader**（启动类加载器）   | `JRE/lib` 或 `-Xbootclasspath`               | 由 C++ 实现，是 JVM 的一部分，无 Java 父类加载器 |
| **Extension ClassLoader**（扩展类加载器）   | `JRE/lib/ext` 或 `-Djava.ext.dirs`           | 加载 Java 扩展库（如 `javax.*`）                 |
| **Application ClassLoader**（应用类加载器） | `-Djava.class.path` 或 `-cp` 或 `-classpath` | 默认加载用户编写的类（`main()` 方法所在类）      |
| **Custom ClassLoader**（自定义类加载器）    | 用户自定义路径（如网络、加密类）             | 可继承 `ClassLoader` 实现个性化加载逻辑          |

::: info 双亲委派模型

:::

双亲委派模型（Parents Delegation Model）要求除了顶层的 Bootstrap ClassLoader 外，其余的类加载器都应有自己的父类加载器。这里类加载器之间的父子关系一般通过组合（Composition）关系来实现，而不是通过继承（Inheritance）的关系实现。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2025/05/83b042a530424999a75d6d383aade1c0.png)

**工作原理**：**只有当父类加载器加载失败的情况下，才会用子类加载器去加载类**。

```mermaid
graph TD
    A[loadClass 调用] --> B{该类是否已加载?}
    B -->|是| C[返回已加载的 Class]
    B -->|否| D{是否有父加载器?}
    D -->|是| E[委托父加载器加载]
    D -->|否| F[Bootstrap ClassLoader 尝试加载]
    E --> G{父加载器加载成功?}
    F --> G
    G -->|是| C
    G -->|否| H[调用自身 findClass 加载]
    H --> I{加载成功?}
    I -->|是| C
    I -->|否| J[抛出 ClassNotFoundException]
```

**优势**

- **避免重复加载**：双亲委派模型使得 Java 类随着它的类加载器一起具有一种带有优先级的层次关系，从而确保类在 JVM 中唯一（如 `java.lang.Object` 只由 `Bootstrap` 加载）。
- **安全性**：防止用户伪造核心类（如自定义 `java.lang.String` 会被父类加载器拦截）。

以下是抽象类 `java.lang.ClassLoader` 的代码片段，其中的 `loadClass()` 方法运行过程如下：

```java
public abstract class ClassLoader {
    // The parent class loader for delegation
    private final ClassLoader parent;

    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return loadClass(name, false);
    }

    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            // 首先判断该类型是否已经被加载
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                // 如果没有被加载，就委托给父类加载或者委派给启动类加载器加载
                try {
                    if (parent != null) {
                        // 如果存在父类加载器，就委派给父类加载器加载
                        c = parent.loadClass(name, false);
                    } else {
                        // 如果不存在父类加载器，就检查是否是由启动类加载器加载的类，通过调用本地方法 native Class findBootstrapClass(String name)
                        c = findBootstrapClassOrNull(name);
                    }
                } catch (ClassNotFoundException e) {
                    // 如果父类加载器加载失败，会抛出 ClassNotFoundException
                }

                if (c == null) {
                    // 如果父类加载器和启动类加载器都不能完成加载任务，才调用自身的加载功能
                    c = findClass(name);
                }
            }
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        throw new ClassNotFoundException(name);
    }
}
```

【说明】

- 先检查类是否已经加载过，如果没有则让父类加载器去加载。
- 当父类加载器加载失败时抛出 `ClassNotFoundException`，此时尝试自己去加载。

### 【困难】有哪些场景破坏了双亲委派模型？⭐⭐⭐⭐

双亲委派模型并非强制约束，而是 Java 设计者推荐的最佳实践。在 JDK 历史演进中，有三次较大规模的"破坏"：

:::: info 第一次破坏：在双亲委派模型出现之前

::::

JDK 1.2 之前，自定义类加载器只需重写 `loadClass()`，而 `loadClass()` 正是双亲委派的核心逻辑所在，重写它就会破坏委派链。JDK 1.2 引入双亲委派模型后，为了向前兼容，新增了 `findClass()` 方法引导用户重写它而非 `loadClass()`。

:::: info 第二次破坏：基础类需要回调用户代码

::::

双亲委派模型解决了基础类统一加载的问题，但如果基础类又要回调用户的代码怎么办？典型场景：

- **SPI（Service Provider Interface）机制**：如 `JDBC` 的 `DriverManager`（由 BootstrapClassLoader 加载）需要加载各厂商实现的 `Driver` 接口，但 BootstrapClassLoader 无法加载 classpath 下的实现类。

- **解决方案**：引入**线程上下文类加载器（Thread Context ClassLoader）**。`DriverManager` 通过 `Thread.currentThread().getContextClassLoader()` 获取应用类加载器来加载 SPI 实现类，相当于"父委派给子加载"。

```java
// DriverManager 中的 SPI 加载逻辑（简化）
ServiceLoader<Driver> loaders = ServiceLoader.load(Driver.class);
// ServiceLoader.load 内部使用线程上下文类加载器
ClassLoader cl = Thread.currentThread().getContextClassLoader();
```

:::: info 第三次破坏：追求程序动态性

::::

- **OSGi（Open Service Gateway Initiative）**：提出"网状"类加载结构，每个模块（Bundle）有自己的类加载器，模块间按依赖关系形成有向图，不再遵循双亲委派的树状结构。
- **Tomcat**：每个 Web 应用有独立的 `WebAppClassLoader`，优先加载自身目录下的类（而非先委派给父加载器），实现应用间的类隔离。
- **JRebel / 热部署**：通过自定义类加载器重新加载修改后的类，实现不停机更新。

:::: info Tomcat 类加载器为什么"违反"双亲委派？

::::

Tomcat 的 `WebAppClassLoader` 加载策略：

1. 先查本地缓存（已加载过）
2. 查 JVM 缓存（`findLoadedClass`）
3. 加载 JVM 系统类（用 Bootstrap 加载，防止应用覆盖核心类如 `java.lang.String`）
4. **先用自己加载**（`findClass`，即 `WEB-INF/classes` 和 `WEB-INF/lib`）—— 此处违反双亲委派
5. 最后委派给父加载器（Common ClassLoader）

**原因**：Web 应用之间需要类隔离，同一台 Tomcat 上的两个应用可以有不同版本的 Spring，互不干扰。

::: info 跨语言类加载机制对比（L4：演进视角）

:::

**Go：无类加载器概念**

Go 是编译型语言，不存在运行时的类加载机制：

- **编译期链接**：Go 程序在编译时将所有依赖静态链接为单一二进制文件，所有类型信息在编译期已完全确定。Go 没有 `.class` 文件、没有 ClassLoader、没有双亲委派。
- **接口的隐式实现**：Go 的接口是鸭子类型（structural typing），无需显式声明 `implements`，编译期检查类型是否满足接口，运行时通过 `interface` 的 `itab` 表进行动态分发——这比 Java 的类加载+反射更轻量。
- **plugin 包（有限动态性）**：Go 1.8+ 提供了 `plugin` 包，支持在运行时加载 `.so` 动态库，这是 Go 最接近 Java 类加载的机制，但功能非常有限（仅 Linux 支持，且 API 简陋）。

**Rust：编译期链接 + trait 系统**

Rust 同样没有运行时类加载，但其 trait 系统提供了比 Go 更丰富的多态能力：

- **编译期单态化（Monomorphization）**：Rust 的泛型在编译期为每个具体类型生成独立代码，零运行时开销，与 Java 的类型擦除形成鲜明对比。
- **trait 对象（动态分发）**：`dyn Trait` 通过 vtable 实现运行时多态，类似 Java 的接口调用，但无需类加载。
- **无反射**：Rust 没有内置的运行时反射机制（`std::any::Any` 只提供有限的类型判断），所有类型信息在编译期处理。
- **过程宏（Procedural Macros）**：Rust 通过编译期代码生成（宏）实现类似 Java 注解处理器（APT）的功能，但发生在编译期而非运行时。

**JPMS（Java Platform Module System）与双亲委派的演进**

JDK 9 引入的 JPMS 模块化系统在双亲委派之上增加了一层模块级隔离：

- **模块化类加载**：JPMS 在双亲委派模型上叠加了模块可见性控制。即使类加载器能找到类，如果模块未 `exports` 或 `opens`，也无法访问。
- **与双亲委派的关系**：JPMS 并未替代双亲委派，而是在其基础上增加了"模块路径"（Module Path）的概念，Boot Layer 的类加载器在委派前先检查模块的可读性。
- **对破坏场景的影响**：
  - SPI 机制在 JPMS 中通过 `provides...with` 和 `uses` 关键字声明，比 ServiceLoader 更规范。
  - 强封装使得反射访问内部 API 受限（`--add-opens` 参数可临时开放）。

| 维度       | Java（双亲委派）     | Go                   | Rust                | JPMS（JDK 9+）        |
| :--------- | :------------------- | :------------------- | :------------------ | :-------------------- |
| 类加载时机 | 运行时懒加载         | 编译期静态链接       | 编译期静态链接      | 运行时（模块化）      |
| 隔离机制   | ClassLoader 层级     | 包级别（无类加载器） | crate 级别          | 模块（module）        |
| 动态性     | 高（反射、动态代理） | 低（plugin 有限）    | 极低（无反射）      | 高（受模块约束）      |
| 版本冲突   | 类加载器隔离         | 编译期解决           | 编译期解决（Cargo） | 模块版本管理          |
| 核心优势   | 运行时灵活性         | 简单、编译快         | 零成本抽象          | 强封装 + 兼容双亲委派 |

## JVM 内存管理

### 【困难】JVM 的内存区域是如何划分的？⭐⭐⭐⭐⭐

JDK7 和 JDK8 的 JVM 的内存区域划分有所不同，如下图所示：

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2025/05/9240b05d12a24954aaa2e25d232bc3e4.png)

**线程私有区域**

- **程序计数器**
  - 记录当前线程执行的字节码指令地址（Native 方法时为`undefined`）。
  - **JVM 中唯一无 OOM 的区域**。
- **虚拟机栈**
  - 存储方法调用的**栈帧**（局部变量表、操作数栈、动态链接、返回地址）。
    - **局部变量表**：用于存放方法参数和方法内部定义的局部变量。
    - **操作数栈**：主要作为方法调用的中转站使用，用于存放方法执行过程中产生的中间计算结果。另外，计算过程中产生的临时变量也会放在操作数栈中。
    - **动态连接** - 用于一个方法调用其他方法的场景。Class 文件的常量池中有大量的符号引用，字节码中的方法调用指令就以常量池中指向方法的符号引用为参数。这些符号引用一部分会在类加载阶段或第一次使用的时候转化为直接引用，这种转化称为**静态解析**；另一部分将在每一次的运行期间转化为直接应用，这部分称为**动态连接**。
    - **方法返回地址** - 用于返回方法被调用的位置，恢复上层方法的局部变量和操作数栈。Java 方法有两种返回方式，一种是 `return` 语句正常返回，一种是抛出异常。无论采用何种退出方式，都会导致栈帧被弹出。也就是说，栈帧随着方法调用而创建，随着方法结束而销毁。无论方法正常完成还是异常完成都算作方法结束。
  - 异常：`StackOverflowError`（栈深度超限）、`OOM`（扩展失败）。
  - 可以通过 `-Xss` 指定占内存大小
- **本地方法栈**：与虚拟机栈的作用非常相似，二者区别仅在于：**虚拟机栈为 Java 方法服务；本地方法栈为 Native 方法服务**。

**线程共享区域**

- **堆（Heap）**
  - 存放**所有对象实例和数组**，是 GC 主战场。
  - 分区：新生代（Eden+Survivor）、老年代。
  - 异常：OOM: Java heap space（对象过多或内存泄漏）。
- **字符串常量池**：用于存储字符串字面量，位于堆内存中的一块特殊区域。通过 String 类的 intern() 方法可以将字符串键入到字符串常量池。
- **方法区（JDK 8+：元空间）**
  - 存储类信息、常量、静态变量（JDK 7 后移至堆）。
  - **JDK 8 用元空间（本地内存）替代永久代**，默认无上限。
  - 异常：`OOM`（加载过多类）。
- **运行时常量池**：Class 文件中存储编译时生成的常量信息，并在类加载时进入 JVM 方法区。

**直接内存（非 JVM 规范）**

直接内存是 JVM 堆外的本地内存。具有读写快、无 GC 开销，需手动管理的特性。

- 分配：ByteBuffer.allocateDirect()
- 清理：DirectBuffer.cleaner().clean()
- 场景：高频 I/O（如 NIO、Netty、MMAP）
- 异常：Direct buffer memory
- JVM 参数：可以通过 `-XX:MaxDirectMemorySize` 设置直接内存大小，如果无设置，默认大小等于 `-Xmx` 值。

::: info 跨语言内存布局对比（L4：演进视角）

:::

**Go 的内存布局**

Go 的内存模型与 JVM 有显著差异，其设计哲学是"简单、可控"，没有 JVM 那样复杂的分代结构：

- **栈可动态增长**：Go 的 goroutine 初始栈只有 ~2KB，运行时根据需要自动扩缩容（通过栈拷贝机制），与 JVM 固定大小的线程栈（`-Xss`）完全不同。每个 goroutine 的栈是动态的，可以增长到 1GB。
- **无方法区/元空间概念**：Go 是编译型语言，程序的类型信息在编译期已确定并嵌入二进制文件，运行时无需维护类元数据区域。Go 的 `reflect` 包在运行时提供有限的类型信息，但这些信息分散在堆和全局数据段中。
- **堆**：Go 的堆统一管理所有动态分配的对象，不区分新生代/老年代。Go 的 GC 是并发三色标记清扫，没有分代假设。
- **无直接内存概念**：Go 没有 JVM 的 Direct Memory 概念，但可通过 `unsafe` 包或 cgo 操作堆外内存。

**Rust 的内存布局**

Rust 代表了"零成本抽象"的内存管理范式，没有 GC，依靠所有权系统在编译期保证内存安全：

- **栈 + 堆 + 无 GC**：Rust 默认在栈上分配（类似 C++），只有通过 `Box<T>`、`Vec<T>`、`Arc<T>` 等智能指针才会在堆上分配。对象的生命周期由所有权和借用规则在编译期确定，运行时无需 GC。
- **编译期内存管理**：Rust 的 `Drop` trait 提供确定性析构（类似 C++ 的 RAII），当对象离开作用域时立即释放内存，无需等待 GC 周期。
- **无运行时内存区域划分**：Rust 编译后直接生成机器码，没有 JVM 那样的运行时数据区（程序计数器、虚拟机栈等均由操作系统管理）。

| 维度          | JVM (HotSpot)      | Go                     | Rust                 |
| :------------ | :----------------- | :--------------------- | :------------------- |
| 栈            | 固定大小（`-Xss`） | 动态扩缩（~2KB → 1GB） | 固定大小（OS 管理）  |
| 堆分代        | 新生代 + 老年代    | 无分代                 | 无 GC，手动/RAII     |
| 方法区/元空间 | 有（类元数据）     | 无（编译期确定）       | 无（编译期确定）     |
| 内存管理      | GC 自动回收        | GC 自动回收            | 所有权系统（编译期） |
| 直接内存      | NIO DirectBuffer   | 无（unsafe/cgo）       | 无（unsafe/FFI）     |

### 【困难】JVM 产生 OOM 有哪几种情况？⭐⭐⭐⭐

JVM 发生 **OutOfMemoryError（OOM）** 的原因多种多样，主要与内存区域划分和对象分配机制相关。以下是所有可能的 OOM 类型及其触发条件、典型案例和排查方法：

#### Java heap space

- **触发条件**：**堆内存不足**，无法分配新对象。
- **常见原因**：

  - 内存泄漏（如静态容器持续增长、未关闭的资源）。
  - 堆内存设置过小（`-Xmx` 值不合理）。
  - 大对象（如一次性加载超大文件到内存）。

- **案例代码**：

  ```java
  List<byte[]> list = new ArrayList<>();
  while (true) {
      list.add(new byte[1024 * 1024]); // 持续分配 1MB 数组
  }
  ```

- **解决方向**：
  - 检查 `-Xmx` 和 `-Xms` 参数是否合理。
  - 使用 `jmap -histo:live <pid>` 或 **MAT（Memory Analyzer Tool）** 分析堆转储（`-XX:+HeapDumpOnOutOfMemoryError`）。

#### Metaspace（JDK 8 及以后）

- **触发条件**：**元空间（Metaspace）不足**，无法加载新的类信息。
- **常见原因**：
  - 动态生成大量类（如反射、CGLIB、动态代理）。
  - 未设置元空间上限（默认依赖本地内存，可能耗尽）。
- **案例代码**：
  ```java
  for (int i = 0; i < 1000000; i++) {
      Enhancer enhancer = new Enhancer(); // CGLIB 动态生成类
      enhancer.setSuperclass(OOM.class);
      enhancer.create();
  }
  ```
- **解决方向**：
  - 调整元空间大小：`-XX:MetaspaceSize=256M -XX:MaxMetaspaceSize=256M`。
  - 检查类加载器泄漏（如热部署未清理旧类）。

#### PermGen space（JDK 7 及以前）

- **类似 Metaspace**，但发生在永久代（PermGen），JDK 8 后被元空间取代。
- **常见原因**：大量字符串常量或类加载未卸载。

#### Direct buffer memory

- **触发条件**：**直接内存（堆外内存）耗尽**。
- **常见原因**：
  - NIO 的 `ByteBuffer.allocateDirect()` 未释放。
  - 直接内存上限过小（`-XX:MaxDirectMemorySize`）。
- **案例代码**：
  ```java
  List<ByteBuffer> buffers = new ArrayList<>();
  while (true) {
      buffers.add(ByteBuffer.allocateDirect(1024 * 1024)); // 1MB 直接内存
  }
  ```
- **解决方向**：
  - 显式调用 `((DirectBuffer) buffer).cleaner().clean()` 或复用缓冲区。
  - 增加 `-XX:MaxDirectMemorySize=1G`。

#### Unable to create new native thread

- **触发条件**：**线程数超过系统限制**（非堆内存问题）。

- **常见原因**：

  - 线程池配置不合理（如无界线程池）。
  - 系统级限制（`ulimit -u` 查看用户最大线程数）。

- **案例代码**：

  ```java
  while (true) {
      new Thread(() -> {
          try { Thread.sleep(100000); } catch (Exception e) {}
      }).start();
  }
  ```

- **解决方向**：
  - 改用线程池（如 `ThreadPoolExecutor`）。
  - 调整系统限制（Linux 下修改 `/etc/security/limits.conf`）。

#### GC overhead limit exceeded

- **触发条件**：GC 耗时超过 98% 且回收内存不足 2%（JVM 自我保护）。
- **本质原因**：堆内存几乎耗尽，GC 无效循环。
- **解决方向**：
  - 同 `heap space` 排查内存泄漏。
  - 关闭保护机制（不推荐）：`-XX:-UseGCOverheadLimit`。

#### CodeCache is full（JIT 编译代码缓存满）

- **触发条件**：JIT 编译的本地代码超出缓存区（`-XX:ReservedCodeCacheSize`）。
- **常见原因**：动态生成大量方法（如频繁调用反射）。
- **解决方向**：
  - 增加缓存：`-XX:ReservedCodeCacheSize=256M`。
  - 关闭分层编译：`-XX:-TieredCompilation`。

#### Requested array size exceeds VM limit

- **触发条件**：尝试分配超过 JVM 限制的数组（如 `Integer.MAX_VALUE - 2`）。

- **案例代码**：

  ```java
  int[] arr = new int[Integer.MAX_VALUE]; // 直接崩溃
  ```

- **解决方向**：检查代码中不合理的数组分配逻辑。

#### OOM 类型速查表

| OOM 类型                          | 关联内存区域  | 典型原因            |
| --------------------------------- | ------------- | ------------------- |
| `Java heap space`                 | 堆            | 内存泄漏/堆太小     |
| `Metaspace` / `PermGen space`     | 元空间/永久代 | 类加载爆炸          |
| `Unable to create native thread`  | 系统线程数    | 线程池失控/系统限制 |
| `Direct buffer memory`            | 堆外内存      | NIO Buffer 未释放   |
| `GC overhead limit exceeded`      | 堆            | GC 无效循环         |
| `CodeCache is full`               | JIT 代码缓存  | 动态方法过多        |
| `Requested array size exceeds VM` | 堆            | 超大数组分配        |

::: info 跨语言 OOM 场景对比（L4：演进视角）

:::

**Go 的 OOM 场景**

Go 没有 Metaspace/PermGen 概念，也没有 CodeCache，但有其独特的 OOM 风险：

- **goroutine 栈泄漏**：每个 goroutine 初始栈 ~2KB，但可动态增长到 1GB。如果 goroutine 泄漏（如 channel 阻塞未关闭），栈内存会持续增长导致 OOM。这是 Go 特有的 OOM 场景，与 Java 的线程泄漏类似但更隐蔽——goroutine 非常轻量，泄漏更难察觉。
- **slice 无限增长**：Go 的 slice 底层是动态数组，append 操作会自动扩容。如果代码逻辑导致 slice 无限 append（如循环中持续追加数据而不清理），会耗尽堆内存。Go 没有类似 JVM 的大对象直接进老年代的机制，所有对象统一在堆中分配。
- **cgo 内存泄漏**：通过 cgo 调用 C 代码分配的内存不受 Go GC 管理，必须手动释放。类似 JVM 的 JNI 内存泄漏。
- **无元空间 OOM**：Go 的类型信息在编译期确定，运行时不存在类似 Metaspace 的 OOM。

**Rust 的 OOM 场景**

Rust 的 OOM 行为与 JVM/Go 有本质不同——Rust 默认在分配失败时 **panic 而非返回 OOM 错误**：

- **Box/Arc 分配失败 → panic**：`Box::new()`、`Arc::new()`、`Vec::push()` 等标准库分配在 OOM 时直接 panic（`abort`），不会像 Java 抛出 `OutOfMemoryError`。这是因为 Rust 将 OOM 视为不可恢复错误。
- **编译期避免**：Rust 的所有权系统和 RAII 机制在编译期就确定了内存的分配和释放时机，大幅减少了运行时内存泄漏的可能。`no_std` 环境下甚至完全无堆分配。
- **fallible allocation（实验性）**：Rust 的 `alloc` crate 提供了 `try_reserve` 等 fallible API，允许在 OOM 时返回 `AllocError` 而非 panic，但标准库 `Vec`、`Box` 等默认未使用。
- **无 GC 意味着无 GC overhead 类 OOM**：Rust 没有 GC，不会出现 Java 的 "GC overhead limit exceeded"。

| OOM 场景            | JVM                              | Go                       | Rust                 |
| :------------------ | :------------------------------- | :----------------------- | :------------------- |
| 堆 OOM              | `Java heap space`                | slice 无限增长、内存泄漏 | `Box::new()` panic   |
| 元空间/类 OOM       | `Metaspace` / `PermGen`          | 无（编译期确定类型）     | 无（编译期确定类型） |
| 线程/goroutine 泄漏 | `Unable to create native thread` | goroutine 栈泄漏         | 线程栈溢出 → panic   |
| 直接内存/JNI        | `Direct buffer memory`           | cgo 内存泄漏             | `unsafe`/FFI 泄漏    |
| GC 相关             | `GC overhead limit exceeded`     | GC 延迟增加但不会 OOM    | 无 GC                |
| 编译缓存            | `CodeCache is full`              | 无 JIT                   | 无 JIT               |

### 【简单】字符串常量池有什么用？⭐⭐

字符串常量池是 JVM 的特殊内存区域，用于存储字符串字面量（如 `"abc"`），确保相同内容的字符串只存一份。

**字符串常量池通过复用相同字符串，节省内存并提升性能，直接赋值（`"abc"`）优先使用池，`new String()` 强制创建新对象。**

字符串常量池的作用有：

**节省内存**：相同字符串复用，避免重复创建（如 `String s1 = "hello"` 和 `String s2 = "hello"` 指向同一对象）。

**提升性能**：

- **快速比较**：直接通过 `==` 判断地址是否相同（比 `equals()` 更快）。
- **哈希优化**：如 `HashMap` 的键可复用缓存的 `hashCode`。

**实现规则**

- **直接赋值**（`String s = "abc"`）→ **优先从常量池引用**。
- **`new String("abc")`** → **强制在堆中创建新对象**（不推荐，除非需隔离实例）。
- **`intern()` 方法** → 将堆中的字符串对象添加到常量池（若池中不存在）。

**注意事项**

- **避免滥用 `new String()`**：无特殊需求时，直接用字面量赋值。
- **`intern()` 慎用**：可能增加常量池内存压力，需权衡性能。

### 【困难】为什么 Java 8 移除了永久代（PermGen）并引入了元空间（Metaspace）？⭐⭐⭐

Java 8 用元空间替代永久代，解决了 PermGen 固定大小易导致内存溢出和垃圾回收效率低的问题。元空间使用本地内存，具备更灵活的内存分配能力，提升了垃圾收集和内存管理的效率。

**永久代（PermGen）的主要问题**

- **固定大小限制**：永久代大小通过 `-XX:MaxPermSize` 设定，默认较小（64MB~128MB），易触发 `OutOfMemoryError: PermGen space`，尤其是动态加载类过多时（如频繁部署的 Web 应用）。
- **垃圾回收效率低**：永久代与老年代共用垃圾回收机制（Full GC 时才会回收），类卸载条件苛刻（需类加载器被回收）。
- **内存管理不灵活**：永久代在 JVM 堆内分配，与对象堆共享内存空间，易导致堆内存碎片化。

**元空间（Metaspace）的优势**

- **使用本地内存（Native Memory）**：元空间直接分配在操作系统的本地内存中，默认无上限（仅受系统物理内存限制），避免 `PermGen` 大小硬限制问题。可通过 `-XX:MaxMetaspaceSize` 设置上限（如不设置则动态扩展）。
- **自动调整大小**：元空间可以根据需要自动扩展大小，从而降低了 OOM 的风险。
- **性能优化**：元空间由于在堆外，因此减少了 Full GC 触发频率。避免了频繁回收 PermGen 时的停顿。

**永久代 vs. 元空间**

| **特性**     | **永久代（PermGen）**             | **元空间（Metaspace）**                 |
| ------------ | --------------------------------- | --------------------------------------- |
| **存储位置** | JVM 堆内存                        | 本地内存（Native Memory）               |
| **大小限制** | `-XX:MaxPermSize` 固定上限        | 默认无上限，可设 `-XX:MaxMetaspaceSize` |
| **垃圾回收** | 依赖 Full GC                      | 独立回收，条件更宽松                    |
| **OOM 错误** | `OutOfMemoryError: PermGen space` | `OutOfMemoryError: Metaspace`           |

## 字节码

### 【中等】Java 是编译型语言还是解释型语言？⭐⭐

结论：**Java 既是编译型语言，也是解释型语言**。

::: info 什么是编译型语言？什么是解释型语言？
:::

- [**编译型语言**](https://zh.wikipedia.org/wiki/編譯語言) - 程序在执行之前**需要一个专门的编译过程，把程序编译成为机器语言的文件**，运行时不需要重新翻译，直接使用编译的结果就行了。一般情况下，编译型语言的执行速度比较快，开发效率比较低。常见的编译型语言有 C、C++、Go 等。
- [**解释型语言**](https://zh.wikipedia.org/wiki/直譯語言） - 程序不需要编译，只是在程序运行时通过 [解释器](https://zh.wikipedia.org/wiki/直譯器) ，将代码一句一句解释为机器代码后再执行。一般情况下，解释型语言的执行速度比较慢，开发效率比较高。常见的解释型语言有 JavaScript、Python、Ruby 等。

::: info 为什么说 Java 既是编译型语言，也是解释型语言？
:::

Java 语言既具有编译型语言的特征，也具有解释型语言的特征。因此，我们说 Java 是编译和解释并存的。

- **解释器**：源码 → 字节码（`.java` → `.class`）。在 JVM 的解释器中，是逐行解释字节码并执行的。
- **JIT**：字节码 → 机器码（热点代码编译优化）；普通代码仍解释执行。

Java 的源代码，首先，**通过 Javac 编译成为字节码（bytecode）**，即 `*.java` 文件转为 `*.class` 文件；然后，在运行时，**通过 Java 虚拟机（JVM）内嵌的解释器将字节码转换成为最终的机器码来执行**。正是由于 JVM 这套机制，使得 Java 可以【**一次编写，到处执行（Write Once, Run Anywhere）**】。

为了改善解释语言的效率而发展出的 [即时编译](https://zh.wikipedia.org/wiki/即時編譯） 技术，已经缩小了这两种语言间的差距。这种技术混合了编译语言与解释型语言的优点，它像编译语言一样，先把程序源代码编译成 [字节码](https://zh.wikipedia.org/wiki/字节码）。到执行期时，再将字节码直译，之后执行。[Java](https://zh.wikipedia.org/wiki/Java) 与 [LLVM](https://zh.wikipedia.org/wiki/LLVM) 是这种技术的代表产物。常见的 JVM（如 Hotspot JVM），都提供了 JIT（Just-In-Time）编译器，JIT 能够在运行时将热点代码编译成机器码，这种情况下部分热点代码就属于**编译执行**，而不是解释执行了。

::: tip 扩展

[基本功 | Java 即时编译器原理解析及实践](https://tech.meituan.com/2020/10/22/java-jit-practice-in-meituan.html)

:::

### 【中等】什么是 Java 字节码？它与机器码有什么区别？⭐⭐

Java 字节码（Java Bytecode）是 Java 源代码编译后生成的中间代码，它是 Java 虚拟机（JVM）执行的指令集。**JVM 通过解释器或即时编译（JIT）将字节码转换为机器码执行**。字节码是 Java 实现【**一次编写，到处执行（Write Once, Run Anywhere）**】的核心技术之一。

机器码是直接由 CPU 执行的二进制指令。

**Java 字节码要点**：

- **基本概念**
  - 平台无关的中间代码，存储在 `.class` 文件中。
  - 包含类结构、字段、方法及对应的字节码指令。
- **指令集**：包含加载（`aload`/`iload`）、存储（`astore`）、运算（`iadd`）、控制流（`if_icmpgt`）等操作。
- **执行方式**
  - **解释执行**：JVM 逐条解释字节码。
  - **JIT 编译**：热点代码动态编译为机器码优化性能。
- **动态能力**
  - **反射**：运行时动态解析/修改字节码（如生成代理类）。
  - **字节码增强**：框架（Spring AOP 等）通过 ASM、Javassist 等工具修改字节码，实现 AOP 等功能。

::: tip 扩展

[美团 - 字节码增强技术探索](https://tech.meituan.com/2019/09/05/java-bytecode-enhancement.html)

:::

### 【中等】.class 文件的结构包含哪些主要部分？⭐⭐

`.class` 文件是一组以 8 位字节为基础单位的二进制流，各数据项按严格顺序紧凑排列。其结构如下：

| **组成部分**                   | **说明**                                                                         |
| :----------------------------- | :------------------------------------------------------------------------------- |
| **魔数（Magic Number）**       | 4 字节，固定为 `0xCAFEBABE`，用于标识这是 class 文件                             |
| **版本号**                     | 次版本号（2 字节）+ 主版本号（2 字节），如 JDK 8 = 52，JDK 11 = 55               |
| **常量池（Constant Pool）**    | 存放字面量（如字符串、final 常量）和符号引用（类名、方法名、字段名）             |
| **访问标志（Access Flags）**   | 标识类或接口的访问权限（`public`、`final`、`abstract`、`interface` 等）          |
| **类索引、父类索引、接口索引** | 描述类的继承关系，通过索引指向常量池中的符号引用                                 |
| **字段表（Fields）**           | 描述类中声明的变量（类变量和实例变量，不含局部变量）                             |
| **方法表（Methods）**          | 描述类中声明的方法，每个方法含「方法名、描述符、属性表（含 Code 属性即字节码）」 |
| **属性表（Attributes）**       | 存放额外的辅助信息（如源文件名、行号映射、注解等）                               |

**常量池中的常量类型**（主要分类）：

- **字面量**：如文本字符串、`final` 常量值。
- **符号引用**：
  - 类和接口的全限定名（Fully Qualified Name）
  - 字段的名称和描述符（Descriptor）
  - 方法的名称和描述符

```bash
# 使用 javap -v 查看常量池
javap -v HelloWorld.class
# 输出示例（节选）：
# Constant pool:
#    #1 = Methodref          #6.#20         // java/lang/Object."<init>":()V
#    #2 = Fieldref           #21.#22        // java/lang/System.out:Ljava/io/PrintStream;
#    #3 = String             #23            // Hello, World!
```

### 【中等】如何查看 Java 字节码？常用工具有哪些？⭐⭐

- **javap**（JDK 自带）：`javap -c -l -v HelloWorld.class` 查看反汇编的字节码、常量池、行号表。
  - `-c`：反汇编字节码指令
  - `-l`：显示行号和局部变量表
  - `-v`：显示详细信息（常量池、版本号、栈深度等）
- **ASM**：轻量级字节码操作框架，可用于分析和生成 class 文件（如 Spring AOP 底层）。
- **Javassist**：提供源码级别的字节码编辑 API，比 ASM 更易用。
- **ByteBuddy**：现代字节码操作库，广泛用于 Mock 框架（如 Mockito）、Agent 开发。
- **Bytecode Viewer**：GUI 工具，支持多款反编译器（Procyon、CFR、Fernflower）。
- **IDEA 插件**：`jclasslib`（查看 class 文件结构）、`Bytecode Editor`。

### 【中等】Java 字节码有哪些典型应用场景？⭐

Java 字节码（.class 文件）是连接源码与机器码的 “中间桥梁”，其核心价值在于**平台无关性**和**可操控性**，典型应用场景覆盖 Java 程序运行的基础支撑、开发提效、性能优化、安全管控等核心环节。

| 场景分类              | 具体应用（记忆关键词）                       | 原理 / 工具                             | 实际价值                                                          |
| :-------------------- | :------------------------------------------- | :-------------------------------------- | :---------------------------------------------------------------- |
| 跨平台运行（基础）    | Java 程序跨系统执行、跨 JVM 部署             | 字节码与平台无关，不同系统 JVM 解析执行 | 实现 “一次编写，到处运行”，核心支撑 Java 跨平台特性               |
| 动态增强 / 字节码插桩 | AOP 切面编程、日志埋点、性能监控、参数校验   | ASM、Javassist、ByteBuddy、Spring AOP   | 无需修改源码，运行时增强类功能（如 Spring 事务、SkyWalking 监控） |
| 编译优化（JIT/AOT）   | JIT 编译热点字节码、AOT 预编译字节码为机器码 | HotSpot JIT、GraalVM Native Image       | 提升程序执行性能（JIT 优化热点）、降低冷启动耗时（AOT）           |
| 安全校验 / 合规审计   | 字节码校验、恶意代码检测、代码合规检查       | JVM 类加载验证阶段、FindBugs、SonarQube | 防止非法字节码执行，保障代码安全与合规                            |
| 逆向分析 / 代码审计   | 反编译排查问题、第三方 jar 包审计、漏洞分析  | JD-GUI、Fernflower、Procyon             | 定位第三方组件问题、审计代码安全性、排查线上故障                  |
| 动态代理 / 框架核心   | 动态生成代理类、MyBatis/Mockito 底层实现     | JDK 动态代理（生成字节码）、CGLIB       | 框架解耦（如 MyBatis mapper 代理）、测试模拟（Mockito）           |
| 定制化执行 / 类加载   | 自定义类加载器、热部署、模块化打包           | 自定义 ClassLoader、OSGi、jlink         | 实现代码热更新（如 Tomcat 热部署）、轻量化模块化应用              |
| 代码混淆 / 防反编译   | 商业软件字节码混淆、防止源码泄露             | ProGuard、Allatori                      | 保护商业代码知识产权，增加逆向难度                                |

小结：

- 核心基础场景：跨平台运行（Java 核心特性）、编译优化（JIT/AOT 提升性能）；
- 核心高频场景：字节码插桩（AOP / 埋点）、动态代理（框架核心）、逆向分析（问题排查）；
- 核心价值：字节码的标准化和可操控性，支撑了 Java 生态的灵活扩展、性能优化与安全管控。

### 【困难】什么是 JIT？JIT 编译器是如何工作的？⭐⭐⭐⭐⭐

**JIT（Just-In-Time Compilation，即时编译）**在运行时将**热点代码**（频繁执行的字节码）动态编译为**本地机器码**，提升执行效率。

程序运行过程中，JIT 实时识别「热点代码」（高频执行的方法 / 循环），将这些字节码一次性编译为当前平台的本地机器码并缓存，后续执行时直接调用缓存的机器码，替代逐行解释执行，大幅提升 Java 程序运行效率。

::: info JIT 工作流程
:::

1. **初始阶段**：JVM 解释执行字节码（启动快但效率低）；
2. **热点识别**：统计代码执行次数，高频代码标记为 “热点”；
3. **编译优化**：JVM 后台异步将热点字节码编译为机器码并缓存，后续执行直接用机器码（效率提升 5-10 倍）。

::: info C1 vs C2 编译器对比

HotSpot JVM 的 JIT 采用**双层编译器架构**——C1（Client Compiler）和 C2（Server Compiler），与分层编译机制配合使用：

| 维度          | C1（Client Compiler）                | C2（Server Compiler）                        |
| ------------- | ------------------------------------ | -------------------------------------------- |
| **编译速度**  | 快（~1-5ms/方法）                    | 慢（~10-50ms/方法）                          |
| **优化深度**  | 浅（方法内联、去虚拟化、空值消除）   | 深（逃逸分析、标量替换、循环展开、代数简化） |
| **Profiling** | 采集基础 profiling（类型、调用计数） | 利用 C1 的 profiling 数据做推测优化          |
| **目标**      | 快速达到可接受的性能                 | 峰值性能                                     |
| **适用场景**  | GUI 应用、短生命周期服务             | 长运行服务端、批处理                         |
| **代码缓存**  | 编译产物较小                         | 编译产物较大                                 |

**为什么需要两层？** C1 快速编译让 JVM 快速脱离纯解释执行阶段（否则启动慢），C2 深度优化让热点代码达到接近 C/C++ 的性能，二者组合兼顾启动时间与峰值性能。

:::

::: info JIT 优化技术详解

- **方法内联（Inlining）**：将小方法调用替换为方法体代码，消除方法调用开销（栈帧、参数传递）。这是**最重要的 JIT 优化**——因为内联后其他优化（逃逸分析、常量折叠）才能生效。HotSpot 默认对 ≤35 字节的方法进行内联（`-XX:MaxInlineSize`）。
- **逃逸分析（Escape Analysis）**：判断对象作用域，将未逃逸对象进行标量替换等效栈上分配，详见下一题。
- **分层编译（Tiered Compilation）**：
  - **5 层模型**：
    - **0 层**：解释执行（采集 profiling 数据——类型分布、分支概率、调用计数）
    - **1 层**：C1 无 profiling（纯快速编译，启动用）
    - **2 层**：C1 + 基础 profiling（记录调用次数）
    - **3 层**：C1 + 完整 profiling（记录类型、分支、调用目标）
    - **4 层**：C2 深度优化（利用 2/3 层采集的 profiling 数据）
  - **JDK 8+ 默认启用**：`-XX:+TieredCompilation`
- **循环展开（Loop Unrolling）**：将 n 次循环体展开为 k 次迭代的代码块，减少循环控制开销。
- **去虚拟化（Devirtualization）**：将虚方法调用转为直接调用。分两种：
  - **单态内联缓存（Monomorphic IC）**：profiling 发现某虚方法调用位置 90%+ 都调用同一个实现类，C2 插入 `if (obj.getClass() == Expected) { 直接调用 } else { 虚调用 }`
  - **双态/多态（Bimorphic/Megamorphic）**：2~3 个常见类型也可内联缓存，超过则退化为虚调用
- **标量替换（Scalar Replacement）**：将未逃逸对象拆解为独立的标量成员变量，分别分配在寄存器或栈上，完全消除对象分配。
- **代数简化与常量折叠**：编译期计算常量表达式（如 `24*60*60` → `86400`），简化代数运算。

:::

::: info 去优化（Deoptimization）—— 为什么 JIT 编译的代码会被丢弃？

**去优化**是指 JVM 丢弃已经编译好的机器码，回退到解释执行的过程。这是 JIT 编译器**推测性优化**的代价：

**触发去优化的常见场景**：

| 场景              | 示例                             | 原因                                                       |
| ----------------- | -------------------------------- | ---------------------------------------------------------- |
| **类加载变化**    | 新加载的子类打破了之前的单态假设 | C2 基于「只有 1 个实现类」做了激进内联，新类出现后假设失效 |
| **Uncommon Trap** | 空指针、数组越界等罕见路径被触发 | C2 假设某路径不会执行（如 `obj != null`），一旦触发需回退  |
| **调试/热替换**   | JVMTI 重定义类、IDEA HotSwap     | 字节码已变，旧机器码无效                                   |
| **OSR 失败**      | 替换循环的执行状态失败           | 从解释执行切换到编译循环时状态不兼容                       |

**Deoptimization 的代价**：不仅丢弃编译产物，还要将寄存器/栈的状态还原为解释器可读的格式，开销较大。频繁去优化是性能杀手——如线上运行一段时间后变慢，可能原因就是大量去优化。

**排查方法**：`-XX:+PrintCompilation -XX:+UnlockDiagnosticVMOptions -XX:+LogCompilation` 可输出编译详情。

:::

::: info JIT 参数

| **参数**                                             | **作用**                                                                                                |
| ---------------------------------------------------- | ------------------------------------------------------------------------------------------------------- |
| `-XX:CompileThreshold=10000`                         | 触发 JIT 编译的方法调用阈值（C2，分层编译关闭时生效）                                                   |
| `-XX:+PrintCompilation`                              | 打印 JIT 编译日志（观察哪些方法被编译、去优化）                                                         |
| `-XX:ReservedCodeCacheSize=240M`                     | 设置代码缓存大小（默认 240MB）。**CodeCache 满会导致 JIT 停止编译**，全部回退到解释执行，服务吞吐暴跌！ |
| `-XX:+TieredCompilation`                             | 启用分层编译（JDK 8+ 默认开启）                                                                         |
| `-XX:+DoEscapeAnalysis`                              | 启用逃逸分析（JDK 1.7+ 默认开启）                                                                       |
| `-XX:+PrintCodeCache`                                | JVM 退出时打印 CodeCache 使用情况                                                                       |
| `-XX:+UnlockDiagnosticVMOptions -XX:+LogCompilation` | 输出详细编译日志（可用 JITWatch 可视化分析）                                                            |

> **重要陷阱**：CodeCache 默认只有 240MB。如果服务方法数量多（如大型 Spring 应用），CodeCache 可能被打满。一旦 `CodeCache full`，JIT 彻底停摆，吞吐量可能**下降 10~100 倍**！监控 `jstat -compiler` 中的 CodeCache 使用率是关键。

:::

::: info L4 扩展：跨语言 JIT 编译器对比

**（1）V8（JavaScript）的 TurboFan + Ignition**

Google V8 引擎的编译管线与 HotSpot 有惊人的结构相似性：

| 阶段         | HotSpot JVM    | V8 (JavaScript)          | 说明                              |
| ------------ | -------------- | ------------------------ | --------------------------------- |
| **解释器**   | 模板解释器     | Ignition（字节码解释器） | 启动时执行，采集 profiling        |
| **快速编译** | C1             | Sparkplug（基线 JIT）    | 无 profiling 开销，快速产出机器码 |
| **优化编译** | C2             | TurboFan（优化 JIT）     | 利用 profiling 数据做推测优化     |
| **去优化**   | Deoptimization | Deoptimization           | 推测失败时回退                    |

但 V8 的编译策略更激进——JavaScript 无静态类型，TurboFan 严重依赖 profiling 推测类型（如「这个变量 95% 是 int」），推测失败触发去优化的概率更高。

**（2）.NET（C#）的 RyuJIT + Tiered Compilation**

.NET Core 的 RyuJIT 也采用分层编译，但比 HotSpot 更进一步：

- **ReadyToRun（R2R）**：预编译到中间格式，部署时 JIT 只需少量编译，兼顾启动速度
- **Dynamic PGO**：Runtime 收集 profiling 数据，动态反馈给 JIT 重新编译热点
- **区别**：.NET 更强调「提前编译」（AOT via NativeAOT），JVM 更依赖运行时 JIT

**（3）Go —— 为什么没有 JIT？**

Go 完全不使用 JIT，所有代码在编译期直接生成静态机器码。原因：

- Go 编译速度极快（全量 AOT），无需 JIT 加速启动
- 静态编译产物无运行时依赖，部署简单
- 代价：无法利用运行时 profiling 做推测优化，纯静态优化的天花板低于 JIT 的动态优化

**（4）GraalVM —— JIT 的演进方向**

GraalVM 用 Java 重写了 JIT 编译器（Graal JIT），可同时用于 JVM 和 AOT 编译：

| 维度         | HotSpot C2             | Graal JIT                          |
| ------------ | ---------------------- | ---------------------------------- |
| **实现语言** | C++（维护困难）        | Java（易于迭代）                   |
| **优化策略** | 固定规则（启发式）     | 部分内联 (Partial Escape Analysis) |
| **AOT 支持** | 无（需 jaotc，已废弃） | 原生支持（Native Image）           |
| **向量化**   | 有限                   | SIMD 向量化支持更好                |

:::

### 【困难】什么是逃逸分析？⭐⭐⭐

**逃逸分析** 是 JVM 在 **JIT 阶段** 进行的一种优化技术：编译阶段分析对象的「作用域」，判断对象是否会 “逃逸” 出当前方法 / 线程（如对象被返回、被外部引用、跨线程访问）；若判定对象未逃逸，JVM 会对其做**栈上分配**、**标量替换**、**锁消除**等优化，减少 GC 开销、提升程序性能。

::: info 逃逸分析判定规则

:::

| 逃逸类型                     | 判定条件（记忆关键词）                             | 示例                                                                                      |
| :--------------------------- | :------------------------------------------------- | :---------------------------------------------------------------------------------------- |
| **全局逃逸**（GlobalEscape） | 对象被静态字段引用、被其他线程访问、作为方法返回值 | `static List<Object> list = new ArrayList<>();`<br/>`Object m() { return new Object(); }` |
| **参数逃逸**（ArgEscape）    | 对象作为参数传递给其他方法，但不会被全局引用       | `void m(Object o) { foo(o); }`                                                            |
| **无逃逸**（NoEscape）       | 对象仅在方法内创建和使用，未被外部引用 / 返回      | `void m() { Object o = new Object(); }`                                                   |

::: info 逃逸分析相关的 JIT 优化

:::

（1）**栈上分配**

**栈上分配**：非逃逸的对象直接在栈帧中分配内存，对象随方法调用结束自动销毁，无需 GC 回收。

默认情况下，对象分配在堆上，需 GC 回收，栈上分配可大幅减少堆内存占用和 GC 压力。

:::: warning 注意：HotSpot JVM 实际未实现真正的栈上分配

::::

> OpenJDK HotSpot 通过**标量替换**间接实现"栈上分配"的效果，而非将整个对象分配在栈帧上。标量替换后对象被拆解为标量，直接作为局部变量存储在寄存器或栈中。
>
> 因此面试时可以回答"通过逃逸分析+标量替换，让未逃逸对象不进入堆，等效于栈上分配"，这样更精确。

【示例】栈上分配示例

```java
// 未逃逸：对象仅在方法内使用，JIT 优化为栈上分配
public void test() {
    User u = new User("张三"); // 栈上分配，无需 GC
    System.out.println(u.getName());
}
```

（2）**标量替换**

**标量替换**：将非逃逸对象的字段拆解为局部变量（标量），避免创建完整对象。

【示例】标量替换示例

```java
// 原代码：创建 User 对象（包含 name/age 两个字段）
public void test() {
    User u = new User("张三", 20);
    System.out.println(u.getName() + u.getAge());
}
// JIT 优化后（标量替换）：直接分配两个局部变量，无需创建 User 对象
public void test() {
    String name = "张三";
    int age = 20;
    System.out.println(name + age);
}
```

（3）**锁消除**

**锁消除**：若判定锁保护的对象仅在当前线程访问（无线程逃逸），则自动移除不必要的锁，避免锁竞争开销。

```java
// 原代码：加了同步锁，但对象未逃逸（仅当前线程访问）
public void test() {
    Object lock = new Object();
    synchronized (lock) { // JIT 判定 lock 未逃逸，消除同步锁
        System.out.println("无竞争的同步块");
    }
}
```

::: info 逃逸分析启用与验证

:::

（1）启用条件

- JDK 1.7+ 逃逸分析默认开启，无需手动配置；

- 若需确认 / 调整，可通过 JVM 参数：

  ```
  -XX:+DoEscapeAnalysis # 开启（默认）
  -XX:-DoEscapeAnalysis # 关闭
  -XX:+PrintEscapeAnalysis # 打印逃逸分析日志（调试用）
  ```

（2）验证优化效果

通过 `-XX:+PrintGC` 观察 GC 次数：启用逃逸分析后，短生命周期、未逃逸的对象不会进入堆，GC 次数会明显减少。

### 【困难】什么是 AOT？⭐⭐

::: info 什么是 AOT？
:::

Java 9 引入 **AOT（Ahead of Time Compilation，提前编译）** 。AOT 模式下，**程序运行前直接编译为机器码**（类似 C/C++/Rust）。

在程序**运行之前**（部署 / 安装阶段），通过专用工具（如 `jaotc`、GraalVM Native Image）将 Java 字节码（`.class` / `.jar`）直接编译为与目标平台匹配的本地机器码文件，程序启动时无需解释 / 即时编译，直接执行机器码。

::: info AOT 工作流程
:::

1. **编译阶段**：部署时用 AOT 编译器（如 `jaotc`）将字节码编译为平台专属的机器码文件（.so/.dll）；
2. **启动阶段**：JVM 加载预编译的机器码文件，直接执行，无字节码解释 / JIT 编译开销；
3. **运行阶段**：全程执行机器码，无需运行时编译优化（部分实现支持与 JIT 混合）。

::: info AOT 和 JIT 对比
:::

| **维度**     | **AOT**                       | **JIT**                                |
| :----------- | :---------------------------- | :------------------------------------- |
| **编译时机** | 运行前（部署阶段）            | 运行中（热点代码缓存）                 |
| **启动速度** | ⭐⭐⭐极快（无编译开销）      | ⭐较慢（首次解释执行，热点编译有延迟） |
| **内存占用** | ⭐⭐⭐低（无需 JIT 编译缓存） | ⭐⭐较高（需缓存编译后的机器码）       |
| **峰值性能** | ⭐⭐（静态优化）              | ⭐⭐⭐（动态优化）                     |
| **动态支持** | ❌受限                        | ✔️完整支持                             |
| **适合场景** | 云原生 / 微服务               | 高吞吐 / 动态框架                      |

提到 AOT 就不得不提 [GraalVM](https://www.graalvm.org/) 了！GraalVM 是一种高性能的 JDK（完整的 JDK 发行版本），它可以运行 Java 和其他 JVM 语言，以及 JavaScript、Python 等非 JVM 语言。 GraalVM 不仅能提供 AOT 编译，还能提供 JIT 编译。感兴趣的同学，可以去看看 [GraalVM 的官方文档](https://www.graalvm.org/latest/docs/)。如果觉得官方文档看着比较难理解的话，也可以找一些文章来看看，比如：

::: tip 扩展

- [基于静态编译构建微服务应用](https://mp.weixin.qq.com/s/4haTyXUmh8m-dBQaEzwDJw)
- [走向 Native 化：Spring&Dubbo AOT 技术示例与原理讲解](https://cn.dubbo.apache.org/zh-cn/blog/2023/06/28/走向-native-化 springdubbo-aot-技术示例与原理讲解/)

:::

::: info AOT 的局限性
:::

既然 AOT 这么多优点，那为什么不全部使用这种编译方式呢？

**AOT 的局限性在于不支持动态特性**：

- 不支持反射、动态代理、运行时类加载、JNI 等
- 影响框架兼容性（如 Spring、CGLIB 依赖 ASM 技术生成动态字节码）

**AOT 的适用场景**：

- **适合**：启动敏感的微服务、云原生应用
- **不适合**：需动态特性的复杂框架或高频优化的长运行任务
