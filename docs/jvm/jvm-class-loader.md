# JVM 类加载

> **📦 本文以及示例源码已归档在 [javacore](https://github.com/dunwu/javacore/)**

![img](https://raw.githubusercontent.com/dunwu/images/dev/snap/20200617145849.png)

<!-- TOC depthFrom:2 depthTo:3 -->

- [1. 类加载机制](#1-类加载机制)
- [2. 类的生命周期](#2-类的生命周期)
  - [2.1. （一）加载](#21-一加载)
  - [2.2. （二）验证](#22-二验证)
  - [2.3. （三）准备](#23-三准备)
  - [2.4. （四）解析](#24-四解析)
  - [2.5. （五）初始化](#25-五初始化)
- [3. ClassLoader](#3-classloader)
  - [3.1. 类与类加载器](#31-类与类加载器)
  - [3.2. 类加载器分类](#32-类加载器分类)
  - [3.3. 双亲委派](#33-双亲委派)
  - [3.4. ClassLoader 参数](#34-classloader-参数)
- [4. 类的加载](#4-类的加载)
  - [4.1. 类加载方式](#41-类加载方式)
  - [4.2. 加载类错误](#42-加载类错误)
- [5. 参考资料](#5-参考资料)

<!-- /TOC -->

## 1. 类加载机制

> 类是在运行期间动态加载的。

类的加载指的是将类的 `.class` 文件中的二进制数据读入到内存中，将其放在运行时数据区的方法区内，然后在堆区创建一个`java.lang.Class`对象，用来封装类在方法区内的数据结构。类的加载的最终产品是位于堆区中的`Class`对象，`Class`对象封装了类在方法区内的数据结构，并且向 Java 程序员提供了访问方法区内的数据结构的接口。

类加载器并不需要等到某个类被“首次主动使用”时再加载它，JVM 规范允许类加载器在预料某个类将要被使用时就预先加载它，如果在预先加载的过程中遇到了.class 文件缺失或存在错误，类加载器必须在程序首次主动使用该类时才报告错误（LinkageError 错误）如果这个类一直没有被程序主动使用，那么类加载器就不会报告错误

## 2. 类的生命周期

![img](https://raw.githubusercontent.com/dunwu/images/dev/snap/20200617115110.png)

Java 类的完整生命周期包括以下几个阶段：

- **加载（Loading）**
- **链接（Linking）**
  - **验证（Verification）**
  - **准备（Preparation）**
  - **解析（Resolution）**
- **初始化（Initialization）**
- **使用（Using）**
- **卸载（Unloading）**

加载、验证、准备、初始化和卸载这 5 个阶段的顺序是确定的，类的加载过程必须按照这种顺序按部就班地开始。而**解析过程在某些情况下可以在初始化阶段之后再开始，这是为了支持 Java 的动态绑定**。

类加载过程是指加载、验证、准备、解析和初始化这 5 个阶段。

### 2.1. （一）加载

加载是类加载的一个阶段，注意不要混淆。

**加载，是指查找字节流，并且据此创建类的过程**。

加载过程完成以下三件事：

- 通过一个类的全限定名来获取定义此类的二进制字节流。
- 将这个字节流所代表的静态存储结构转化为方法区的运行时存储结构。
- 在内存中生成一个代表这个类的 `Class` 对象，作为方法区这个类的各种数据的访问入口。

其中二进制字节流可以从以下方式中获取：

- 从 ZIP 包读取，这很常见，最终成为日后 JAR、EAR、WAR 格式的基础。
- 从网络中获取，这种场景最典型的应用是 Applet。
- 运行时计算生成，这种场景使用得最多得就是动态代理技术，在 `java.lang.reflect.Proxy` 中，就是用了 `ProxyGenerator.generateProxyClass` 的代理类的二进制字节流。
- 由其他文件生成，典型场景是 JSP 应用，即由 JSP 文件生成对应的 Class 类。
- 从数据库读取，这种场景相对少见，例如有些中间件服务器（如 SAP Netweaver）可以选择把程序安装到数据库中来完成程序代码在集群间的分发。
  ...

> 更详细内容会在 [3. ClassLoader](#3-classloader) 介绍。

### 2.2. （二）验证

验证是链接阶段的第一步。**验证的目标是确保 Class 文件的字节流中包含的信息符合当前虚拟机的要求**，并且不会危害虚拟机自身的安全。

验证阶段大致会完成 4 个阶段的检验动作：

- **文件格式验证** - 验证字节流是否符合 Class 文件格式的规范，并且能被当前版本的虚拟机处理。
- **元数据验证** - 对字节码描述的信息进行语义分析，以保证其描述的信息符合 Java 语言规范的要求。
- **字节码验证** - 通过数据流和控制流分析，确保程序语义是合法、符合逻辑的。
- **符号引用验证** - 发生在虚拟机将符号引用转换为直接引用的时候，对类自身以外（常量池中的各种符号引用）的信息进行匹配性校验。

验证阶段是非常重要的，但不是必须的，它对程序运行期没有影响，如果所引用的类经过反复验证，那么可以考虑采用 `-Xverifynone` 参数来关闭大部分的类验证措施，以缩短虚拟机类加载的时间。

### 2.3. （三）准备

**类变量是被 static 修饰的变量，准备阶段为 static 变量在方法区分配内存并初始化为默认值，使用的是方法区的内存。**

实例变量不会在这阶段分配内存，它将会在对象实例化时随着对象一起分配在 Java 堆中。（实例化不是类加载的一个过程，类加载发生在所有实例化操作之前，并且类加载只进行一次，实例化可以进行多次）

初始值一般为 0 值，例如下面的类变量 value 被初始化为 0 而不是 123。

```java
public static int value = 123;
```

如果类变量是常量，那么会按照表达式来进行初始化，而不是赋值为 0。

```java
public static final int value = 123;
```

准备阶段有以下几点需要注意：

- 这时候进行内存分配的仅包括类变量（static），而不包括实例变量，实例变量会在对象实例化时随着对象一块分配在 Java 堆中。
- 这里所设置的初始值通常情况下是数据类型默认的零值（如 `0`、`0L`、`null`、`false` 等），而不是被在 Java 代码中被显式地赋予的值。

假设一个类变量的定义为：`public static int value = 3`；

那么变量 value 在准备阶段过后的初始值为 0，而不是 3，因为这时候尚未开始执行任何 Java 方法，而把 value 赋值为 3 的`public static`指令是在程序编译后，存放于类构造器`（）`方法之中的，所以把 value 赋值为 3 的动作将在初始化阶段才会执行。

> 这里还需要注意如下几点：
>
> - 对基本数据类型来说，对于类变量（static）和全局变量，如果不显式地对其赋值而直接使用，则系统会为其赋予默认的零值，而对于局部变量来说，在使用前必须显式地为其赋值，否则编译时不通过。
> - 对于同时被 static 和 final 修饰的常量，必须在声明的时候就为其显式地赋值，否则编译时不通过；而只被 final 修饰的常量则既可以在声明时显式地为其赋值，也可以在类初始化时显式地为其赋值，总之，在使用前必须为其显式地赋值，系统不会为其赋予默认零值。
> - 对于引用数据类型 reference 来说，如数组引用、对象引用等，如果没有对其进行显式地赋值而直接使用，系统都会为其赋予默认的零值，即 null。
> - 如果在数组初始化时没有对数组中的各元素赋值，那么其中的元素将根据对应的数据类型而被赋予默认的零值。

- 如果类字段的字段属性表中存在`ConstantValue`属性，即同时被 final 和 static 修饰，那么在准备阶段变量 value 就会被初始化为 ConstValue 属性所指定的值。

假设上面的类变量 value 被定义为： `public static final int value = 3`；

编译时 Javac 将会为 value 生成 ConstantValue 属性，在准备阶段虚拟机就会根据`ConstantValue`的设置将 value 赋值为 3。我们可以理解为 static final 常量在编译期就将其结果放入了调用它的类的常量池中

### 2.4. （四）解析

在 class 文件被加载至 Java 虚拟机之前，这个类无法知道其他类及其方法、字段所对应的具体地址，甚至不知道自己方法、字段的地址。因此，每当需要引用这些成员时，Java 编译器会生成一个符号引用。在运行阶段，这个符号引用一般都能够无歧义地定位到具体目标上。

举例来说，对于一个方法调用，编译器会生成一个包含目标方法所在类的名字、目标方法的名字、接收参数类型以及返回值类型的符号引用，来指代所要调用的方法。

**解析阶段目标是将常量池的符号引用替换为直接引用的过程**。解析动作主要针对类或接口、字段、类方法、接口方法、方法类型、方法句柄和调用点限定符 7 类符号引用进行。

- **符号引用（Symbolic References）** - 符号引用以一组符号来描述所引用的目标，符号可以是任何形式的字面量，只要使用时能无歧义地定位到目标即可。
- **直接引用（Direct Reference）** - 直接引用可以是直接指向目标的指针、相对偏移量或是一个能间接定位到目标的句柄。

### 2.5. （五）初始化

在 Java 代码中，如果要初始化一个静态字段，可以在声明时直接赋值，也可以在静态代码块中对其赋值。

如果直接赋值的静态字段被 `final` 所修饰，并且它的类型是基本类型或字符串时，那么该字段便会被 Java 编译器标记成常量值（ConstantValue），其初始化直接由 Java 虚拟机完成。除此之外的直接赋值操作，以及所有静态代码块中的代码，则会被 Java 编译器置于同一方法中，并把它命名为 `< clinit >`。

初始化阶段才真正开始执行类中的定义的 Java 程序代码。初始化，**为类的静态变量赋予正确的初始值，JVM 负责对类进行初始化，主要对类变量进行初始化**。

#### 类初始化方式

- 声明类变量时指定初始值
- 使用静态代码块为类变量指定初始值

> 在准备阶段，类变量已经赋过一次系统要求的初始值，而在初始化阶段，根据程序员通过程序制定的主观计划去初始化类变量和其它资源。

#### 类初始化步骤

1. 如果类还没有被加载和链接，开始加载该类。
2. 如果该类的直接父类还没有被初始化，先初始化其父类。
3. 如果该类有初始化语句，则依次执行这些初始化语句。

#### 类初始化时机

只有主动引用类的时候才会导致类的初始化。

**（1）主动引用**

类的主动引用包括以下六种：

- **创建类的实例** - 也就是 `new` 对象
- **访问静态变量** - 访问某个类或接口的静态变量，或者对该静态变量赋值
- **访问静态方法**
- **反射** - 如`Class.forName(“com.shengsiyuan.Test”)`
- **初始化子类** - 初始化某个类的子类，则其父类也会被初始化
- **启动类** - Java 虚拟机启动时被标明为启动类的类（`Java Test`），直接使用`java.exe`命令来运行某个主类

**（2）被动引用**

以上 5 种场景中的行为称为对一个类进行主动引用。除此之外，所有引用类的方式都不会触发初始化，称为被动引用。被动引用的常见例子包括：

- **通过子类引用父类的静态字段，不会导致子类初始化**。

```java
System.out.println(SubClass.value); // value 字段在 SuperClass 中定义
```

- **通过数组定义来引用类，不会触发此类的初始化**。该过程会对数组类进行初始化，数组类是一个由虚拟机自动生成的、直接继承自 `Object` 的子类，其中包含了数组的属性和方法。

```java
SuperClass[] sca = new SuperClass[10];
```

- 常量在编译阶段会存入调用类的常量池中，本质上并没有直接引用到定义常量的类，因此不会触发**定义常量的类的初始化**。

```java
System.out.println(ConstClass.HELLOWORLD);
```

#### 类初始化细节

类初始化 `<clinit>()` 方法的细节：

- 是由编译器自动收集类中所有类变量的赋值动作和静态语句块（static{} 块）中的语句合并产生的，编译器收集的顺序由语句在源文件中出现的顺序决定。特别注意的是，静态语句块只能访问到定义在它之前的类变量，定义在它之后的类变量只能赋值，不能访问。例如以下代码：

```java
public class Test {
    static {
        i = 0;                // 给变量赋值可以正常编译通过
        System.out.print(i);  // 这句编译器会提示“非法向前引用”
    }
    static int i = 1;
}
```

- 与类的构造函数（或者说实例构造器 `<init>()`）不同，不需要显式的调用父类的构造器。虚拟机会自动保证在子类的 `<clinit>()` 方法运行之前，父类的 `<clinit>()` 方法已经执行结束。因此虚拟机中第一个执行 `<clinit>()` 方法的类肯定为 `java.lang.Object`。
- 由于父类的 `<clinit>()` 方法先执行，也就意味着父类中定义的静态语句块要优于子类的变量赋值操作。例如以下代码：

```java
static class Parent {
    public static int A = 1;
    static {
        A = 2;
    }
}

static class Sub extends Parent {
    public static int B = A;
}

public static void main(String[] args) {
     System.out.println(Sub.B);  // 输出结果是父类中的静态变量 A 的值，也就是 2。
}
```

- `<clinit>()` 方法对于类或接口不是必须的，如果一个类中不包含静态语句块，也没有对类变量的赋值操作，编译器可以不为该类生成 `<clinit>()` 方法。
- 接口中不可以使用静态语句块，但仍然有类变量初始化的赋值操作，因此接口与类一样都会生成 `<clinit>()` 方法。但接口与类不同的是，执行接口的 `<clinit>()` 方法不需要先执行父接口的 `<clinit>()` 方法。只有当父接口中定义的变量使用时，父接口才会初始化。另外，接口的实现类在初始化时也一样不会执行接口的 `<clinit>()` 方法。
- 虚拟机会保证一个类的 `<clinit>()` 方法在多线程环境下被正确的加锁和同步，如果多个线程同时初始化一个类，只会有一个线程执行这个类的 `<clinit>()` 方法，其它线程都会阻塞等待，直到活动线程执行 `<clinit>()` 方法完毕。如果在一个类的 `<clinit>()` 方法中有耗时的操作，就可能造成多个线程阻塞，在实际过程中此种阻塞很隐蔽。

## 3. ClassLoader

`ClassLoader` 即类加载器，负责将类加载到 JVM。在 Java 虚拟机外部实现，以便让应用程序自己决定如何去获取所需要的类。

JVM 加载 `class` 文件到内存有两种方式：

- 隐式加载 - JVM 自动加载需要的类到内存中。
- 显示加载 - 通过使用 `ClassLoader` 来加载一个类到内存中。

### 3.1. 类与类加载器

如何判断两个类是否相等：类本身相等，并且使用同一个类加载器进行加载。这是因为**每一个 `ClassLoader` 都拥有一个独立的类名称空间**。

这里的相等，包括类的 `Class` 对象的 `equals()` 方法、`isAssignableFrom()` 方法、`isInstance()` 方法的返回结果为 true，也包括使用 `instanceof` 关键字做对象所属关系判定结果为 true。

### 3.2. 类加载器分类

![img](https://raw.githubusercontent.com/dunwu/images/dev/snap/20200617115936.png)

#### Bootstrap ClassLoader

`Bootstrap ClassLoader` ，即启动类加载器 ，**负责加载 JVM 自身工作所需要的类**。

**`Bootstrap ClassLoader` 会将存放在 `<JAVA_HOME>\lib` 目录中的，或者被 `-Xbootclasspath` 参数所指定的路径中的，并且是虚拟机识别的（仅按照文件名识别，如 rt.jar，名字不符合的类库即使放在 lib 目录中也不会被加载）类库加载到虚拟机内存中**。

`Bootstrap ClassLoader` 是由 C++ 实现的，它完全由 JVM 自己控制的，启动类加载器无法被 Java 程序直接引用，用户在编写自定义类加载器时，如果需要把加载请求委派给启动类加载器，直接使用 `null` 代替即可。

#### ExtClassLoader

`ExtClassLoader`，即扩展类加载器，这个类加载器是由 `ExtClassLoader(sun.misc.Launcher\$ExtClassLoader)`实现的。

**`ExtClassLoader` 负责将 `<JAVA_HOME>\lib\ext` 或者被 `java.ext.dir` 系统变量所指定路径中的所有类库加载到内存中，开发者可以直接使用扩展类加载器**。

#### AppClassLoader

`AppClassLoader`，即应用程序类加载器，这个类加载器是由 `AppClassLoader(sun.misc.Launcher\$AppClassLoader)` 实现的。由于这个类加载器是 `ClassLoader` 中的 `getSystemClassLoader()` 方法的返回值，因此一般称为系统类加载器。

**`AppClassLoader` 负责加载用户类路径（即 `classpath`）上所指定的类库**，开发者可以直接使用这个类加载器，如果应用程序中没有自定义过自己的类加载器，一般情况下这个就是程序中默认的类加载器。

#### 自定义类加载器

自定义类加载器可以做到如下几点：

- 在执行非置信代码之前，自动验证数字签名。
- 动态地创建符合用户特定需要的定制化构建类。
- 从特定的场所取得 java class，例如数据库中和网络中。

假设，我们需要自定义一个名为 `FileSystemClassLoader` 的类加载器，继承自 `java.lang.ClassLoader`，用于加载文件系统上的类。它首先根据类的全名在文件系统上查找类的字节代码文件（`.class` 文件），然后读取该文件内容，最后通过 `defineClass()` 方法来把这些字节代码转换成 `java.lang.Class` 类的实例。

`java.lang.ClassLoader` 类的方法 `loadClass()` 实现了双亲委派模型的逻辑，因此自定义类加载器一般不去覆写它，而是通过覆写 `findClass()` 方法。

`ClassLoader` 常用的场景：

- 容器 - 典型应用：Servlet 容器（如：Tomcat、Jetty）、udf （Mysql、Hive）等。加载解压 jar 包或 war 包后，加载其 Class 到指定的类加载器中运行（通常需要考虑空间隔离）。
- 热部署、热插拔 - 应用启动后，动态获得某个类信息，然后加载到 JVM 中工作。很多著名的容器软件、框架（如：Spring 等），都使用 `ClassLoader` 来实现自身的热部署。

【示例】自定义一个类加载器

```java
public class FileSystemClassLoader extends ClassLoader {

    private String rootDir;

    public FileSystemClassLoader(String rootDir) {
        this.rootDir = rootDir;
    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classData = getClassData(name);
        if (classData == null) {
            throw new ClassNotFoundException();
        } else {
            return defineClass(name, classData, 0, classData.length);
        }
    }

    private byte[] getClassData(String className) {
        String path = classNameToPath(className);
        try {
            InputStream ins = new FileInputStream(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int bufferSize = 4096;
            byte[] buffer = new byte[bufferSize];
            int bytesNumRead;
            while ((bytesNumRead = ins.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesNumRead);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String classNameToPath(String className) {
        return rootDir + File.separatorChar
                + className.replace('.', File.separatorChar) + ".class";
    }
}
```

### 3.3. 双亲委派

理解双亲委派之前，先让我们看一个示例。

【示例】寻找类加载示例

```java
public static void main(String[] args) {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    System.out.println(loader);
    System.out.println(loader.getParent());
    System.out.println(loader.getParent().getParent());
}
```

输出：

```
sun.misc.Launcher$AppClassLoader@18b4aac2
sun.misc.Launcher$ExtClassLoader@19e1023e
null
```

从上面的结果可以看出，并没有获取到 `ExtClassLoader` 的父 Loader，原因是 `Bootstrap Loader`（引导类加载器）是用 C 语言实现的，找不到一个确定的返回父 Loader 的方式，于是就返回 null。

下图展示的类加载器之间的层次关系，称为类加载器的**双亲委派模型（Parents Delegation Model）**。**该模型要求除了顶层的 Bootstrap ClassLoader 外，其余的类加载器都应有自己的父类加载器**。**这里类加载器之间的父子关系一般通过组合（Composition）关系来实现，而不是通过继承（Inheritance）的关系实现**。

<div align="center">
<img src="https://raw.githubusercontent.com/dunwu/images/dev/cs/java/javacore/jvm/jmm-类加载-双亲委派.png" width="500" />
</div>

**（1）工作过程**

**一个类加载器首先将类加载请求传送到父类加载器，只有当父类加载器无法完成类加载请求时才尝试加载**。

**（2）好处**

**使得 Java 类随着它的类加载器一起具有一种带有优先级的层次关系**，从而使得基础类得到统一：

- 系统类防止内存中出现多份同样的字节码
- 保证 Java 程序安全稳定运行

例如： `java.lang.Object` 存放在 rt.jar 中，如果编写另外一个 `java.lang.Object` 的类并放到 `classpath` 中，程序可以编译通过。因为双亲委派模型的存在，所以在 rt.jar 中的 `Object` 比在 `classpath` 中的 `Object` 优先级更高，因为 rt.jar 中的 `Object` 使用的是启动类加载器，而 `classpath` 中的 `Object` 使用的是应用程序类加载器。正因为 rt.jar 中的 `Object` 优先级更高，因为程序中所有的 `Object` 都是这个 `Object`。

**（3）实现**

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
                        // 如果不存在父类加载器，就检查是否是由启动类加载器加载的类，通过调用本地方法native Class findBootstrapClass(String name)
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

### 3.4. ClassLoader 参数

在生产环境上启动 java 应用时，通常会指定一些 `ClassLoader` 参数，以加载应用所需要的 lib：

```shell
java -jar xxx.jar -classpath lib/*
```

`ClassLoader` 相关参数选项：

| 参数选项                                     | ClassLoader 类型        | 说明                                                                  |
| -------------------------------------------- | ----------------------- | --------------------------------------------------------------------- |
| `-Xbootclasspath`                            | `Bootstrap ClassLoader` | 设置 `Bootstrap ClassLoader` 搜索路径。【不常用】                     |
| `-Xbootclasspath/a`                          | `Bootstrap ClassLoader` | 把路径添加到已存在的 `Bootstrap ClassLoader` 搜索路径后面。【常用】   |
| `-Xbootclasspath/p`                          | `Bootstrap ClassLoader` | 把路径添加到已存在的 `Bootstrap ClassLoader` 搜索路径前面。【不常用】 |
| `-Djava.ext.dirs`                            | `ExtClassLoader`        | 设置 `ExtClassLoader` 搜索路径。                                      |
| `-Djava.class.path` 或 `-cp` 或 `-classpath` | `AppClassLoader`        | 设置 `AppClassLoader` 搜索路径。                                      |

## 4. 类的加载

### 4.1. 类加载方式

类加载有三种方式：

- 命令行启动应用时候由 JVM 初始化加载
- 通过 `Class.forName()` 方法动态加载
- 通过 `ClassLoader.loadClass()` 方法动态加载

**`Class.forName()` 和 `ClassLoader.loadClass()` 区别**

- `Class.forName()` 将类的 `.class` 文件加载到 jvm 中之外，还会对类进行解释，执行类中的 `static` 块；
- `ClassLoader.loadClass()` 只干一件事情，就是将 `.class` 文件加载到 jvm 中，不会执行 `static` 中的内容，只有在 `newInstance` 才会去执行 `static` 块。
- `Class.forName(name, initialize, loader)` 带参函数也可控制是否加载 `static` 块。并且只有调用了 `newInstance()` 方法采用调用构造函数，创建类的对象 。

### 4.2. 加载类错误

#### ClassNotFoundException

`ClassNotFoundException` 异常出镜率极高。**`ClassNotFoundException` 表示当前 `classpath` 下找不到指定类**。

常见问题原因：

- 调用 `Class` 的 `forName()` 方法，未找到类。
- 调用 `ClassLoader` 中的 `loadClass()` 方法，未找到类。
- 调用 `ClassLoader` 中的 `findSystemClass()` 方法，未找到类。

【示例】执行以下代码，会抛出 `ClassNotFoundException` 异常：

```java
public class ClassNotFoundExceptionDemo {
    public static void main(String[] args) {
        try {
            Class.forName("NotFound");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
```

解决方法：检查 `classpath` 下有没有相应的 class 文件。

#### NoClassDefFoundError

常见问题原因：

- 类依赖的 Class 或者 jar 不存在。
- 类文件存在，但是存在不同的域中。

解决方法：现代 Java 项目，一般使用 `maven`、`gradle` 等构建工具管理项目，仔细检查找不到的类所在的 jar 包是否已添加为依赖。

#### UnsatisfiedLinkError

这个异常倒不是很常见，但是出错的话，通常是在 JVM 启动的时候如果一不小心将在 JVM 中的某个 lib 删除了，可能就会报这个错误了。

【示例】执行以下代码，会抛出 `UnsatisfiedLinkError` 错误。

```java
public class UnsatisfiedLinkErrorDemo {

    public native void nativeMethod();

    static {
        System.loadLibrary("NoLib");
    }

    public static void main(String[] args) {
        new UnsatisfiedLinkErrorDemo().nativeMethod();
    }

}
```

【输出】

```java
java.lang.UnsatisfiedLinkError: no NoLib in java.library.path
	at java.lang.ClassLoader.loadLibrary(ClassLoader.java:1867)
	at java.lang.Runtime.loadLibrary0(Runtime.java:870)
	at java.lang.System.loadLibrary(System.java:1122)
	at io.github.dunwu.javacore.jvm.classloader.exception.UnsatisfiedLinkErrorDemo.<clinit>(UnsatisfiedLinkErrorDemo.java:12)
```

#### ClassCastException

`ClassCastException` 异常通常是在程序中强制类型转换失败时出现。

【示例】执行以下代码，会抛出 `ClassCastException` 异常。

```java
public class ClassCastExceptionDemo {

    public static void main(String[] args) {
        Object obj = new Object();
        EmptyClass newObj = (EmptyClass) obj;
    }

    static class EmptyClass {}

}
```

【输出】

```java
Exception in thread "main" java.lang.ClassCastException: java.lang.Object cannot be cast to io.github.dunwu.javacore.jvm.classloader.exception.ClassCastExceptionDemo$EmptyClass
	at io.github.dunwu.javacore.jvm.classloader.exception.ClassCastExceptionDemo.main(ClassCastExceptionDemo.java:11)
```

## 5. 参考资料

- [《深入理解 Java 虚拟机》](https://book.douban.com/subject/34907497/)
- [深入拆解 Java 虚拟机](https://time.geekbang.org/column/intro/100010301)
- [一篇图文彻底弄懂类加载器与双亲委派机制](https://juejin.im/post/5e479c2cf265da575f4e65e4)
- [Jvm 系列(一):Java 类的加载机制](http://www.ityouknow.com/jvm/2017/08/19/class-loading-principle.html)
