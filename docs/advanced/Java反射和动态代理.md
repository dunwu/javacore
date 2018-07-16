---
title: Java 反射
date: 2018/06/05
categories:
- javacore
tags:
- java
- javacore
- advanced
---

# Java 反射和动态代理

<!-- TOC depthFrom:2 depthTo:3 -->

- [反射](#反射)
    - [反射简介](#反射简介)
    - [反射机制](#反射机制)
    - [Class](#class)
    - [Field](#field)
    - [Method](#method)
    - [Constructor](#constructor)
    - [Array](#array)
- [动态代理](#动态代理)
    - [动态代理简介](#动态代理简介)
    - [代理模式](#代理模式)
    - [动态代理机制](#动态代理机制)
    - [InvocationHandler](#invocationhandler)
    - [Proxy](#proxy)
    - [动态代理实例](#动态代理实例)
- [推荐阅读](#推荐阅读)
- [参考资料](#参考资料)

<!-- /TOC -->

## 反射

### 反射简介

#### 什么是反射

反射(Reflection)是 Java 程序开发语言的特征之一，它允许运行中的 Java 程序获取自身的信息，并且可以操作类或对象的内部属性。

简而言之：**通过反射，我们可以在运行时获得程序或程序集中每一个类的成员和方法。**

#### 反射的主要用途

- **开发通用框架** - 反射最重要的用途就是开发各种通用框架。很多框架（比如 Spring）都是配置化的（比如通过 XML 文件配置 JavaBean、Filter 等），为了保证框架的通用性，它们可能需要根据配置文件加载不同的对象或类，调用不同的方法，这个时候就必须用到反射——运行时动态加载需要加载的对象。
- **可扩展性功能** - 应用程序可以通过使用完全限定名称创建可扩展性对象实例来使用外部的用户定义类。
- **类浏览器和可视化开发环境** - 类浏览器需要能够枚举类的成员。可视化开发环境可以利用反射中可用的类型信息来帮助开发人员编写正确的代码。
- **调试器和测试工具** - 调试器需要能够检查类上的私有成员。测试工具可以利用反射来系统地调用在类中定义的可发现集 API，以确保测试套件中的高级代码覆盖率。

#### 反射的缺点

- **性能开销** - 由于反射涉及动态解析的类型，因此无法执行某些 Java 虚拟机优化。因此，反射操作的性能要比非反射操作的性能要差，应该在性能敏感的应用程序中频繁调用的代码段中避免。
- **安全限制** - 反射调用方法时可以忽略权限检查，因此可能会破坏封装性而导致安全问题。
- **内部曝光** - 由于反射允许代码执行在非反射代码中非法的操作，例如访问私有字段和方法，所以反射的使用可能会导致意想不到的副作用，这可能会导致代码功能失常并可能破坏可移植性。反射代码打破了抽象，因此可能会随着平台的升级而改变行为。

### 反射机制

反射的本质就是：在运行时，把 Java 类中的各种成分映射成一个个的 Java 对象。

先了解一下 JVM 的类加载过程：

![20140427160344203.png](https://upload-images.jianshu.io/upload_images/3101171-65434596b5da72f9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

Java 编译器编译好 `.java` 文件之后，产生 `.class` 文件在磁盘中。`.class` 文件是二进制文件，内容是只有 JVM 虚拟机能够识别的机器码。JVM 虚拟机读取字节码文件，取出二进制数据，加载到内存中，解析.class 文件内的信息，生成对应的 Class 对象。

> Tip:
>
> 较为流行的字节码库：
>
> - **Javassist** - 主要的优点，在于简单，而且快速。直接使用 java 编码的形式，而不需要了解虚拟机指令，就能动态改变类的结构，或者动态生成类。
> - **ASM** - 能够以二进制形式修改已有类或者动态生成类。ASM 可以直接产生二进制 class 文件，也可以在类被加载入 Java 虚拟机之前动态改变类行为。ASM 从类文件中读入信息后，能够改变类行为，分析类信息，甚至能够根据用户要求生成新类。

下图为反射机制的步骤：

![20170513133210763.png](https://upload-images.jianshu.io/upload_images/3101171-fa2c56240e5f7215.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

步骤说明：

1.  JVM 加载方法的时候，遇到 `new Student()`，JVM 会根据 Student 的全限定名去加载 Student.class
2.  JVM 会去本地磁盘查找 Student.class 文件并加载 JVM 内存中
3.  JVM 通过调用类加载器自动创建这个类对应的 Class 对象，并且存储在 JVM 的方法区。注意：一个类有且只有一个 Class 对象。

### Class

对于每种类型的对象，JVM 实例化一个 `java.lang.Class` 的不可变实例，该实例提供了检查对象的运行时属性（包括其成员和类型信息）的方法。类还提供了创建新类和对象的功能。

最重要的是，**`java.lang.Class` 是所有反射 API 的入口点**。

java.lang.reflect 包中的类都没有 public 构造方法。

#### 获得 Class 对象

获得 Class 的三种方法：

（1）**使用 Class 类的 `forName` 静态方法**，示例：

```java
Class c1 = Class.forName("io.github.dunwu.javacore.reflect.ReflectClassTest");
Class c2 = Class.forName("[D");
Class c3 = Class.forName("[[Ljava.lang.String;");
```

使用类的完全限定名来反射对象的类。常见的应用场景为：在 JDBC 开发中常用此方法加载数据库驱动。

（2）**直接获取某一个对象的 `class`**，示例：

```java
Class c1 = boolean.class;
Class c2 = java.io.PrintStream.class;
Class c3 = int[][][].class;
```

（3）**调用 Object 的 `getClass()` 方法**，示例：

Object 类中有 getClass 方法，因为所有类都继承 Object 类。从而调用 Object 类来获取

```java
Class c = "foo".getClass();

enum E {A, B}
Class c2 = E.A.getClass();

byte[] bytes = new byte[1024];
Class c3 = bytes.getClass();

Set<String> set = new HashSet<>();
Class c4 = set.getClass();
```

#### 判断是否为某个类的实例

一般地，我们用 instanceof 关键字来判断是否为某个类的实例。同时我们也可以借助反射中 Class 对象的 isInstance()方法来判断是否为某个类的实例，它是一个 Native 方法：

```java
ArrayList arrayList = new ArrayList();
if (arrayList instanceof List) {
    System.out.println("ArrayList is List");
}
if (List.class.isInstance(arrayList)) {
    System.out.println("ArrayList is List");
}
```

#### 创建实例

通过反射来生成对象主要有两种方式。

（1）使用 Class 对象的 newInstance() 方法来创建 Class 对象对应类的实例。

```java
Class<?> c1 = String.class;
Object str1 = c1.newInstance();
System.out.println(str1.getClass().getCanonicalName());
```

（2）先通过 Class 对象获取指定的 Constructor 对象，再调用 Constructor 对象的 newInstance() 方法来创建实例。这种方法可以用指定的构造器构造类的实例。

```java
//获取String所对应的Class对象
Class<?> c2 = String.class;
//获取String类带一个String参数的构造器
Constructor constructor = c2.getConstructor(String.class);
//根据构造器创建实例
Object obj = constructor.newInstance("bbb");
System.out.println(obj.getClass().getCanonicalName());
```

### Field

获取某个 Class 对象的成员变量的方法有：

- getFiled: 访问公有的成员变量
- getDeclaredField：所有已声明的成员变量。但不能得到其父类的成员变量

示例如下：

```java
class FieldSpy<T> {
    public boolean[][] b = {{ false, false }, { true, true } };
    public String name  = "Alice";
    public List<Integer> list;
    public T val;
}

Field f1 = FieldSpy.class.getField("b");
Field f2 = FieldSpy.class.getField("name");
Field f3 = FieldSpy.class.getField("list");
Field f4 = FieldSpy.class.getField("val");
```

输出：

```
Type: class [[Z
Type: class java.lang.String
Type: interface java.util.List
Type: class java.lang.Object
```

### Method

获取某个 Class 对象的方法集合的方法有：

（1）getDeclaredMethods()方法返回类或接口声明的所有方法，包括公共、保护、默认（包）访问和私有方法，但不包括继承的方法。

```java
Method[] methods1 = Thread.class.getDeclaredMethods();
System.out.println("Thread getDeclaredMethods 清单（数量 = " + methods1.length + "）：");
for (Method m : methods1) {
    System.out.println(m);
}
```

（2）getMethods() 方法返回某个类的所有公用（public）方法，包括其继承类的公用方法。

```java
Method[] methods2 = Thread.class.getMethods();
System.out.println("Thread getMethods 清单（数量 = " + methods2.length + "）：");
for (Method m : methods2) {
    System.out.println(m);
}
```

（3）getMethod() 方法返回一个特定的方法，其中第一个参数为方法名称，后面的参数为方法的参数对应 Class 的对象。

```java
Method method = Thread.class.getMethod("join", long.class, int.class);
System.out.println(method);
```

### Constructor

获取某个 Class 对象的构造方法集合的方法有：

（1）getDeclaredConstructor()方法返回类的所有构造方法，包括公共、保护、默认（包）访问和私有方法。

```java
Method[] methods1 = Thread.class.getDeclaredMethods();
System.out.println("Thread getDeclaredMethods 清单（数量 = " + methods1.length + "）：");
for (Method m : methods1) {
    System.out.println(m);
}
```

（2）getConstructors() 方法返回某个类的所有公用（public）构造方法。

```java
Constructor<?>[] constructors = FileOutputStream.class.getConstructors();
System.out.println("FileOutputStream 构造方法清单（数量 = " + constructors.length + "）：");
for (Constructor c : constructors) {
    System.out.println(c);
}
```

（3）getConstructor() 方法返回一个特定的方法，参数为方法的参数对应 Class 的对象。

```java
Constructor constructor = FileOutputStream.class.getConstructor(String.class, boolean.class);
System.out.println(constructor);
```

### Array

数组在 Java 里是比较特殊的一种类型，它可以赋值给一个 Object Reference。下面我们看一看利用反射创建数组的例子：

```java
public static void testArray() throws ClassNotFoundException {
    Class<?> cls = Class.forName("java.lang.String");
    Object array = Array.newInstance(cls,25);
    //往数组里添加内容
    Array.set(array,0,"hello");
    Array.set(array,1,"Java");
    Array.set(array,2,"fuck");
    Array.set(array,3,"Scala");
    Array.set(array,4,"Clojure");
    //获取某一项的内容
    System.out.println(Array.get(array,3));
}
```

其中的 Array 类为 java.lang.reflect.Array 类。我们通过 Array.newInstance()创建数组对象，它的原型是：

```java
public static Object newInstance(Class<?> componentType, int length)
    throws NegativeArraySizeException {
    return newArray(componentType, length);
}
```

## 动态代理

### 动态代理简介

Java 动态代理常被用于一些 Java 框架中。例如 Spring 的 AOP ，Dubbo 的 SPI 接口，就是基于 Java 动态代理实现的。

### 代理模式

动态代理是设计模式中代理模式的巧妙应用。

先介绍一下代理模式，如下图：

![image.png](https://upload-images.jianshu.io/upload_images/3101171-6269723ea61527bd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

当在代码阶段规定这种代理关系，Proxy 类通过编译器编译成 class 文件，当系统运行时，此 class 已经存在了。这种静态的代理模式固然在访问无法访问的资源，增强现有的接口业务功能方面有很大的优点，但是大量使用这种静态代理，会使我们系统内的类的规模增大，并且不易维护；并且由于 Proxy 和 RealSubject 的功能本质上是相同的，Proxy 只是起到了中介的作用，这种代理在系统中的存在，导致系统结构比较臃肿和松散。

### 动态代理机制

为了解决经典代理模式的问题，就有了创建动态代理的想法：在运行状态中，需要代理的地方，根据 Subject 和 RealSubject，动态地创建一个 Proxy，用完之后，就会销毁，这样就可以避免了 Proxy 角色的 class 在系统中冗杂的问题了。

![20140515134257500.jpg](https://upload-images.jianshu.io/upload_images/3101171-6e282fabb28e4dd8.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

Java 动态代理基于经典代理模式，引入了一个 InvocationHandler，InvocationHandler 负责统一管理所有的方法调用。

动态代理步骤：

1.  获取 RealSubject 上的所有接口列表；
2.  确定要生成的代理类的类名，默认为：`com.sun.proxy.$ProxyXXXX`；
3.  根据需要实现的接口信息，在代码中动态创建 该 Proxy 类的字节码；
4.  将对应的字节码转换为对应的 class 对象；
5.  创建 InvocationHandler 实例 handler，用来处理 Proxy 所有方法调用；
6.  Proxy 的 class 对象 以创建的 handler 对象为参数，实例化一个 proxy 对象。

从上面可以看出，JDK 动态代理的实现是基于实现接口的方式，使得 Proxy 和 RealSubject 具有相同的功能。

但其实还有一种思路：通过继承。即：让 Proxy 继承 RealSubject，这样二者同样具有相同的功能，Proxy 还可以通过重写 RealSubject 中的方法，来实现多态。CGLIB 就是基于这种思路设计的。

在 Java 的动态代理机制中，有两个重要的类或接口，一个是 InvocationHandler(Interface)、另一个则是 Proxy(Class)，这一个类和接口是实现我们动态代理所必须用到的。

### InvocationHandler

`InvocationHandler` 接口定义：

```java
public interface InvocationHandler {
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable;
}
```

每一个动态代理类都必须要实现 `InvocationHandler` 这个接口，并且每个代理类的实例都关联到了一个 Handler，当我们通过代理对象调用一个方法的时候，这个方法的调用就会被转发为由 `InvocationHandler` 这个接口的 `invoke` 方法来进行调用。

我们来看看 InvocationHandler 这个接口的唯一一个方法 invoke 方法：

```java
Object invoke(Object proxy, Method method, Object[] args) throws Throwable
```

参数说明：

- **proxy** - 代理的真实对象。
- **method** - 所要调用真实对象的某个方法的 Method 对象
- **args** - 所要调用真实对象某个方法时接受的参数

如果不是很明白，等下通过一个实例会对这几个参数进行更深的讲解。

### Proxy

Proxy 这个类的作用就是用来动态创建一个代理对象的类，它提供了许多的方法，但是我们用的最多的就是 `newProxyInstance` 这个方法：

```java
public static Object newProxyInstance(ClassLoader loader, Class<?>[] interfaces,  InvocationHandler h)  throws IllegalArgumentException
```

这个方法的作用就是得到一个动态的代理对象。

参数说明：

- **loader** - 一个 ClassLoader 对象，定义了由哪个 ClassLoader 对象来对生成的代理对象进行加载。
- **interfaces** - 一个 Interface 对象的数组，表示的是我将要给我需要代理的对象提供一组什么接口，如果我提供了一组接口给它，那么这个代理对象就宣称实现了该接口(多态)，这样我就能调用这组接口中的方法了
- **h** - 一个 InvocationHandler 对象，表示的是当我这个动态代理对象在调用方法的时候，会关联到哪一个 InvocationHandler 对象上

### 动态代理实例

上面的内容介绍完这两个接口(类)以后，我们来通过一个实例来看看我们的动态代理模式是什么样的：

首先我们定义了一个 Subject 类型的接口，为其声明了两个方法：

```java
public interface Subject {

    void hello(String str);

    String bye();
}
```

接着，定义了一个类来实现这个接口，这个类就是我们的真实对象，RealSubject 类：

```java
public class RealSubject implements Subject {

    @Override
    public void hello(String str) {
        System.out.println("Hello  " + str);
    }

    @Override
    public String bye() {
        System.out.println("Goodbye");
        return "Over";
    }
}
```

下一步，我们就要定义一个动态代理类了，前面说个，每一个动态代理类都必须要实现 InvocationHandler 这个接口，因此我们这个动态代理类也不例外：

```java
public class InvocationHandlerDemo implements InvocationHandler {
    // 这个就是我们要代理的真实对象
    private Object subject;

    // 构造方法，给我们要代理的真实对象赋初值
    public InvocationHandlerDemo(Object subject) {
        this.subject = subject;
    }

    @Override
    public Object invoke(Object object, Method method, Object[] args)
        throws Throwable {
        // 在代理真实对象前我们可以添加一些自己的操作
        System.out.println("Before method");

        System.out.println("Call Method: " + method);

        // 当代理对象调用真实对象的方法时，其会自动的跳转到代理对象关联的handler对象的invoke方法来进行调用
        Object obj = method.invoke(subject, args);

        // 在代理真实对象后我们也可以添加一些自己的操作
        System.out.println("After method");
        System.out.println();

        return obj;
    }
}
```

最后，来看看我们的 Client 类：

```java
public class Client {
    public static void main(String[] args) {
        // 我们要代理的真实对象
        Subject realSubject = new RealSubject();

        // 我们要代理哪个真实对象，就将该对象传进去，最后是通过该真实对象来调用其方法的
        InvocationHandler handler = new InvocationHandlerDemo(realSubject);

        /*
         * 通过Proxy的newProxyInstance方法来创建我们的代理对象，我们来看看其三个参数
         * 第一个参数 handler.getClass().getClassLoader() ，我们这里使用handler这个类的ClassLoader对象来加载我们的代理对象
         * 第二个参数realSubject.getClass().getInterfaces()，我们这里为代理对象提供的接口是真实对象所实行的接口，表示我要代理的是该真实对象，这样我就能调用这组接口中的方法了
         * 第三个参数handler， 我们这里将这个代理对象关联到了上方的 InvocationHandler 这个对象上
         */
        Subject subject = (Subject)Proxy.newProxyInstance(handler.getClass().getClassLoader(), realSubject
                .getClass().getInterfaces(), handler);

        System.out.println(subject.getClass().getName());
        subject.hello("World");
        String result = subject.bye();
        System.out.println("Result is: " + result);
    }
}
```

我们先来看看控制台的输出：

```
com.sun.proxy.$Proxy0
Before method
Call Method: public abstract void io.github.dunwu.javacore.reflect.InvocationHandlerDemo$Subject.hello(java.lang.String)
Hello  World
After method

Before method
Call Method: public abstract java.lang.String io.github.dunwu.javacore.reflect.InvocationHandlerDemo$Subject.bye()
Goodbye
After method

Result is: Over
```

我们首先来看看 `com.sun.proxy.$Proxy0` 这东西，我们看到，这个东西是由  `System.out.println(subject.getClass().getName());` 这条语句打印出来的，那么为什么我们返回的这个代理对象的类名是这样的呢？

```java
Subject subject = (Subject)Proxy.newProxyInstance(handler.getClass().getClassLoader(), realSubject
                .getClass().getInterfaces(), handler);
```

可能我以为返回的这个代理对象会是 Subject 类型的对象，或者是 InvocationHandler 的对象，结果却不是，首先我们解释一下**为什么我们这里可以将其转化为 Subject 类型的对象？**

原因就是：在 newProxyInstance 这个方法的第二个参数上，我们给这个代理对象提供了一组什么接口，那么我这个代理对象就会实现了这组接口，这个时候我们当然可以将这个代理对象强制类型转化为这组接口中的任意一个，因为这里的接口是 Subject 类型，所以就可以将其转化为 Subject 类型了。

**同时我们一定要记住，通过 `Proxy.newProxyInstance` 创建的代理对象是在 jvm 运行时动态生成的一个对象，它并不是我们的 InvocationHandler 类型，也不是我们定义的那组接口的类型，而是在运行是动态生成的一个对象，并且命名方式都是这样的形式，以$开头，proxy 为中，最后一个数字表示对象的标号**。

接着我们来看看这两句

```
subject.hello("World");
String result = subject.bye();
```

这里是通过代理对象来调用实现的那种接口中的方法，这个时候程序就会跳转到由这个代理对象关联到的 handler 中的 invoke 方法去执行，而我们的这个 handler 对象又接受了一个 RealSubject 类型的参数，表示我要代理的就是这个真实对象，所以此时就会调用 handler 中的 invoke 方法去执行。

我们看到，在真正通过代理对象来调用真实对象的方法的时候，我们可以在该方法前后添加自己的一些操作，同时我们看到我们的这个 method 对象是这样的：

```java
public abstract void io.github.dunwu.javacore.reflect.InvocationHandlerDemo$Subject.hello(java.lang.String)
public abstract java.lang.String io.github.dunwu.javacore.reflect.InvocationHandlerDemo$Subject.bye()
```

正好就是我们的 Subject 接口中的两个方法，这也就证明了当我通过代理对象来调用方法的时候，起实际就是委托由其关联到的 handler 对象的 invoke 方法中来调用，并不是自己来真实调用，而是通过代理的方式来调用的。

## 推荐阅读

本文示例代码见：[源码](https://github.com/dunwu/JavaCore/tree/master/codes/advanced/src/main/java/io/github/dunwu/javacore)

本文同步维护在：[Java 系列教程](https://github.com/dunwu/JavaCore)

## 参考资料

- [Java 编程思想（Thinking in java）](https://item.jd.com/10058164.html)
- [深入解析 Java 反射（1） - 基础](https://www.sczyh30.com/posts/Java/java-reflection-1/)
- [Java 基础之—反射（非常重要）](https://blog.csdn.net/sinat_38259539/article/details/71799078)
- [官方 Reflection API 文档](https://docs.oracle.com/javase/tutorial/reflect/index.html)
- [java 的动态代理机制详解](https://www.cnblogs.com/xiaoluo501395377/p/3383130.html)
- [Java 动态代理机制详解（JDK 和 CGLIB，Javassist，ASM）](https://blog.csdn.net/luanlouis/article/details/24589193)
