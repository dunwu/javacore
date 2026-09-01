---
title: Java 虚拟机之字节码
date: 2019-10-28 22:04:39
order: 05
categories:
  - Java
  - JavaCore
  - JVM
tags:
  - Java
  - JavaCore
  - JVM
  - 字节码
permalink: /pages/4f183431/
---

# Java 虚拟机之字节码

## 简介

Java 字节码是 Java 源代码编译后的中间表示形式，存储在 `.class` 文件中。字节码是一种平台无关的指令集，由 JVM 的解释器或 JIT 编译器执行。理解字节码结构有助于深入理解 Java 的编译、加载和执行机制，也是学习字节码增强技术（ASM、Javassist、ByteBuddy）的基础。

## 字节码简介

Java 字节码是 Java 虚拟机执行的一种指令格式。之所以被称之为字节码，是因为：**Java 字节码文件（`.class`）是一种以 8 位字节为基础单位的二进制流文件**，各个数据项严格按照顺序紧凑地排列在 .class 文件中，中间没有添加任何分隔符。**整个 .class 文件本质上就是一张表**。

Java 能做到 “**一次编译，到处运行**”，一是因为 JVM 针对各种操作系统、平台都进行了定制；二是因为无论在什么平台，都可以编译生成固定格式的 Java 字节码文件（`.class`）。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2024/08/a4f6a2dbf4ad4353995700c382ed16f3.png)

## 类文件结构

一个 Java 类编译后生成的 .class 文件内容如下图所示，是一堆十六进制数。

Class 文件是一组以 8 个字节为基础单位的二进制流，各个数据项目严格按照顺序紧凑地排列在文 件之中，中间没有添加任何分隔符，这使得整个 Class 文件中存储的内容几乎全部是程序运行的必要数 据，没有空隙存在。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2023/04/25941bcc52e74a5e84b6420f9ca9ecbf.png)

图来自 [字节码增强技术探索](https://tech.meituan.com/2019/09/05/java-bytecode-enhancement.html)

字节码看似杂乱无序，实际上是由严格的格式要求组成的。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2024/08/d5dbfd94883c48cca97e5693b6ae5d82.png)

### 魔数

每个 `.class` 文件的头 4 个字节称为 **`魔数（magic_number）`**，它的唯一作用是确定这个文件是否为一个能被虚拟机接收的 `.class` 文件。魔数的固定值为：`0xCAFEBABE`（咖啡宝贝）。

### 版本号

版本号（version）有 4 个字节，**前两个字节表示次版本号（Minor Version），后两个字节表示主版本号（Major Version）**。

Java 的版本号是从 45 开始的，JDK 1.1 之后 的每个 JDK 大版本发布主版本号向上加 1。举例来说，如果版本号为：“00 00 00 34”。那么，次版本号转化为十进制为 0，主版本号转化为十进制为 52，在 Oracle 官网中查询序号 52 对应的主版本号为 1.8，所以编译该文件的 Java 版本号为 1.8.0。

### 常量池

紧接着主版本号之后的字节为常量池（constant_pool），**常量池可以理解为 `.class` 文件中的资源仓库**。

常量池整体上分为两部分：常量池计数器以及常量池数据区

- **常量池计数器（constant_pool_count）** - 由于常量的数量不固定，所以需要先放置两个字节来表示常量池容量计数值。

- **常量池数据区** - 数据区的每一项常量都是一个表，且结构各不相同。

常量池主要存放两类常量：

- **字面量** - 如文本字符串、声明为 `final` 的常量值。
- **符号引用**
  - 类和接口的全限定名
  - 字段的名称和描述符
  - 方法的名称和描述符

### 访问标志

紧接着常量池的 2 个字节代表访问标志（access_flags），这个标志**用于识别一些类或者接口的访问信息**，描述该 Class 是类还是接口；以及是否被 `public`、`abstract`、`final` 等修饰符修饰。

### 类索引、父类索引、接口索引集合

类索引（this_class）和父类索引都是一个 u2 类型的数据，而接口索引集合是一组 u2 类型的数据的集合，**Class 文件中由这三项数据来确定该类型的继承关系**。

### 字段表集合

字段表（field_info）用于描述类和接口中声明的变量。Java 语言中的“字段”（Field）包括类级变 量以及实例级变量，但不包括在方法内部声明的局部变量。

字段可以包括的修饰符有字段的作用域（public、private、protected 修饰 符）、是实例变量还是类变量（static 修饰符）、可变性（final）、并发可见性（volatile 修饰符，是否 强制从主内存读写）、可否被序列化（transient 修饰符）、字段数据类型（基本类型、对象、数组）、 字段名称。

### 方法表集合

Class 文件存储 格式中对方法的描述与对字段的描述采用了几乎完全一致的方式，方法表的结构如同字段表一样，依 次包括访问标志（access_flags）、名称索引（name_index）、描述符索引（descriptor_index）、属性表 集合（attributes）几项

字段表结束后为方法表，方法表的结构如同字段表一样，依次包括了访问标志、名称索引、描述符索引、属性表集合几项。

### 属性表集合

属性表集合（attribute_info）存放了在该文件中类或接口所定义属性的基本信息。

## 字节码指令

字节码指令由一个字节长度的、代表着某种特定操作含义的数字（称为操作码，Opcode）以及跟随其后的零到多个代表此操作所需参数（Operands）而构成。由于 JVM 采用面向操作数栈架构而不是寄存器架构，所以大多数的指令都不包括操作数，只有一个操作码。

JVM 操作码的长度为 1 个字节，因此指令集的操作码最多只有 256 个。

字节码操作大致分为 9 类：

- 加载和存储指令
- 运算指令
- 类型转换指令
- 对象创建与访问指令
- 操作数栈管理指令
- 控制转移指令
- 方法调用和返回指令
- 异常处理指令
- 同步指令

## 字节码增强

### Asm

对于需要手动操纵字节码的需求，可以使用 Asm，它可以直接生产 `.class`字节码文件，也可以在类被加载入 JVM 之前动态修改类行为（如下图 17 所示）。

Asm 的应用场景有 AOP（Cglib 就是基于 Asm）、热部署、修改其他 jar 包中的类等。当然，涉及到如此底层的步骤，实现起来也比较麻烦。

Asm 有两类 API：核心 API 和树形 API

#### 核心 API

Asm Core API 可以类比解析 XML 文件中的 SAX 方式，不需要把这个类的整个结构读取进来，就可以用流式的方法来处理字节码文件。好处是非常节约内存，但是编程难度较大。然而出于性能考虑，一般情况下编程都使用 Core API。在 Core API 中有以下几个关键类：

- ClassReader：用于读取已经编译好的。class 文件。
- ClassWriter：用于重新构建编译后的类，如修改类名、属性以及方法，也可以生成新的类的字节码文件。
- 各种 Visitor 类：如上所述，CoreAPI 根据字节码从上到下依次处理，对于字节码文件中不同的区域有不同的 Visitor，比如用于访问方法的 MethodVisitor、用于访问类变量的 FieldVisitor、用于访问注解的 AnnotationVisitor 等。为了实现 AOP，重点要使用的是 MethodVisitor。

#### 树形 API

Asm Tree API 可以类比解析 XML 文件中的 DOM 方式，把整个类的结构读取到内存中，缺点是消耗内存多，但是编程比较简单。TreeApi 不同于 CoreAPI，TreeAPI 通过各种 Node 类来映射字节码的各个区域，类比 DOM 节点，就可以很好地理解这种编程方式。

### Javassist

利用 Javassist 实现字节码增强时，可以无须关注字节码刻板的结构，其优点就在于编程简单。直接使用 java 编码的形式，而不需要了解虚拟机指令，就能动态改变类的结构或者动态生成类。

其中最重要的是 ClassPool、CtClass、CtMethod、CtField 这四个类：

- `CtClass（compile-time class）` - 编译时类信息，它是一个 class 文件在代码中的抽象表现形式，可以通过一个类的全限定名来获取一个 CtClass 对象，用来表示这个类文件。
- `ClassPool` - 从开发视角来看，ClassPool 是一张保存 CtClass 信息的 HashTable，key 为类名，value 为类名对应的 CtClass 对象。当我们需要对某个类进行修改时，就是通过 pool.getCtClass("className") 方法从 pool 中获取到相应的 CtClass。
- `CtMethod`、`CtField` - 这两个比较好理解，对应的是类中的方法和属性。

## 典型应用场景

### 场景一：使用 ASM 生成类

在运行时动态生成新的类：

```java
ClassWriter cw = new ClassWriter(0);
cw.visit(V1_8, ACC_PUBLIC, "com/example/Hello", null, "java/lang/Object", null);
MethodVisitor mv = cw.visitMethod(ACC_PUBLIC | ACC_STATIC, "hello", "()V", null, null);
mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
mv.visitLdcInsn("Hello ASM!");
mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
mv.visitInsn(RETURN);
mv.visitEnd();
```

### 场景二：使用 Javassist 实现 AOP

在方法前后添加日志：

```java
ClassPool pool = ClassPool.getDefault();
CtClass cc = pool.get("com.example.UserService");
CtMethod method = cc.getDeclaredMethod("getUser");
method.insertBefore("System.out.println(\"Entering getUser\");");
method.insertAfter("System.out.println(\"Exiting getUser\");");
```

### 场景三：使用 ByteBuddy 实现热修复

在不重启 JVM 的情况下修改类行为：

```java
new ByteBuddy()
    .redefine(UserService.class)
    .method(named("getUser"))
    .intercept(FixedValue.value("mocked"))
    .make()
    .load(getClass().getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
```

## 最佳实践

1. **使用 `javap -c` 查看字节码**：理解编译器如何转换代码，发现隐藏的装箱/拆箱等开销。
2. **字节码增强优先使用高级框架**：Javassist 和 ByteBuddy 比 ASM 更容易使用和维护。
3. **避免过度使用字节码操作**：调试困难且容易出错，优先考虑反射或代理模式。
4. **注意 ClassLoader 隔离**：字节码增强的类必须在正确的 ClassLoader 中加载。

## 常见问题

### Q1：字节码和机器码有什么区别？

字节码是 JVM 指令集，平台无关，由 JVM 解释执行或 JIT 编译。机器码是 CPU 原生指令，平台相关，直接由 CPU 执行。字节码通过 JIT 编译可以转换为机器码。

### Q2：为什么 Java 使用字节码而不是直接编译为机器码？

字节码实现了“一次编译，到处运行”的跨平台能力。如果直接编译为机器码，每个平台都需要单独编译。字节码还便于在运行时进行优化（JIT）和动态加载。

### Q3：Kotlin/Scala 等 JVM 语言也生成字节码吗？

是的。所有 JVM 语言（Kotlin、Scala、Groovy、Clojure 等）都编译为 JVM 字节码，因此可以无缝互操作。

## 参考资料

- [《深入理解 Java 虚拟机》](https://book.douban.com/subject/34907497/)
- [字节码增强技术探索](https://tech.meituan.com/2019/09/05/java-bytecode-enhancement.html)
- [一文让你明白 Java 字节码](https://www.jianshu.com/p/252f381a6bc4)
- [Asm 4.0 官方文档](https://asm.ow2.io/asm4-guide.pdf)
- [Javassist Github](https://github.com/jboss-javassist/javassist)
