# 深入理解 Java 反射和动态代理

> :notebook: 本文已归档到：「[javacore](https://github.com/dunwu/javacore)」
>
> :keyboard: 本文中的示例代码已归档到：「[javacore](https://github.com/dunwu/javacore/tree/master/codes/basics/src/main/java/io/github/dunwu/javacore/reflect)」

<!-- TOC depthFrom:2 depthTo:3 -->

- [简介](#简介)
    - [什么是反射](#什么是反射)
    - [反射的应用场景](#反射的应用场景)
    - [反射的缺点](#反射的缺点)
- [反射机制](#反射机制)
    - [类加载过程](#类加载过程)
    - [Class 对象](#class-对象)
- [使用反射](#使用反射)
    - [java.lang.reflect 包](#javalangreflect-包)
    - [获得 Class 对象](#获得-class-对象)
    - [判断是否为某个类的实例](#判断是否为某个类的实例)
    - [创建实例](#创建实例)
    - [Field](#field)
    - [Method](#method)
    - [Constructor](#constructor)
    - [Array](#array)
- [动态代理](#动态代理)
    - [静态代理](#静态代理)
    - [动态代理](#动态代理-1)
    - [InvocationHandler 接口](#invocationhandler-接口)
    - [Proxy 类](#proxy-类)
    - [动态代理实例](#动态代理实例)
- [小结](#小结)
- [参考资料](#参考资料)

<!-- /TOC -->

## 简介

### 什么是反射

反射(Reflection)是 Java 程序开发语言的特征之一，它允许运行中的 Java 程序获取自身的信息，并且可以操作类或对象的内部属性。

**通过反射机制，可以在运行时访问 Java 对象的属性，方法，构造方法等。**

### 反射的应用场景

反射的主要应用场景有：

- **开发通用框架** - 反射最重要的用途就是开发各种通用框架。很多框架（比如 Spring）都是配置化的（比如通过 XML 文件配置 JavaBean、Filter 等），为了保证框架的通用性，它们可能需要根据配置文件加载不同的对象或类，调用不同的方法，这个时候就必须用到反射——运行时动态加载需要加载的对象。
- **动态代理** - 在切面编程（AOP）中，需要拦截特定的方法，通常，会选择动态代理方式。这时，就需要反射技术来实现了。
- **注解** - 注解本身仅仅是起到标记作用，它需要利用反射机制，根据注解标记去调用注解解释器，执行行为。如果没有反射机制，注解并不比注释更有用。
- **可扩展性功能** - 应用程序可以通过使用完全限定名称创建可扩展性对象实例来使用外部的用户定义类。

### 反射的缺点

- **性能开销** - 由于反射涉及动态解析的类型，因此无法执行某些 Java 虚拟机优化。因此，反射操作的性能要比非反射操作的性能要差，应该在性能敏感的应用程序中频繁调用的代码段中避免。
- **破坏封装性** - 反射调用方法时可以忽略权限检查，因此可能会破坏封装性而导致安全问题。
- **内部曝光** - 由于反射允许代码执行在非反射代码中非法的操作，例如访问私有字段和方法，所以反射的使用可能会导致意想不到的副作用，这可能会导致代码功能失常并可能破坏可移植性。反射代码打破了抽象，因此可能会随着平台的升级而改变行为。

## 反射机制

### 类加载过程

<div align="center"><img src="http://dunwu.test.upcdn.net/snap/1553611895164.png!zp"/></div>

类加载的完整过程如下：

（1）在编译时，Java 编译器编译好 `.java` 文件之后，在磁盘中产生 `.class` 文件。`.class` 文件是二进制文件，内容是只有 JVM 能够识别的机器码。

（2）JVM 中的类加载器读取字节码文件，取出二进制数据，加载到内存中，解析.class 文件内的信息。类加载器会根据类的全限定名来获取此类的二进制字节流；然后，将字节流所代表的静态存储结构转化为方法区的运行时数据结构；接着，在内存中生成代表这个类的 `java.lang.Class` 对象。

（3）加载结束后，JVM 开始进行连接阶段（包含验证、准备、初始化）。经过这一系列操作，类的变量会被初始化。

### Class 对象

要想使用反射，首先需要获得待操作的类所对应的 Class 对象。**Java 中，无论生成某个类的多少个对象，这些对象都会对应于同一个 Class 对象。这个 Class 对象是由 JVM 生成的，通过它能够获悉整个类的结构**。所以，`java.lang.Class` 可以视为所有反射 API 的入口点。

**反射的本质就是：在运行时，把 Java 类中的各种成分映射成一个个的 Java 对象。**

举例来说，假如定义了以下代码：

```
User user = new User();
```

步骤说明：

1.  JVM 加载方法的时候，遇到 `new User()`，JVM 会根据 `User` 的全限定名去加载 `User.class` 。
2.  JVM 会去本地磁盘查找 `User.class` 文件并加载 JVM 内存中。
3.  JVM 通过调用类加载器自动创建这个类对应的 `Class` 对象，并且存储在 JVM 的方法区。注意：**一个类有且只有一个 `Class` 对象**。

## 使用反射

### java.lang.reflect 包

Java 中的 `java.lang.reflect` 包提供了反射功能。`java.lang.reflect` 包中的类都没有 `public` 构造方法。

`java.lang.reflect` 包的核心接口和类如下：

- `Member` 接口 - 反映关于单个成员(字段或方法)或构造函数的标识信息。
- `Field` 类 - 提供一个类的域的信息以及访问类的域的接口。
- `Method` 类 - 提供一个类的方法的信息以及访问类的方法的接口。
- `Constructor` 类 - 提供一个类的构造函数的信息以及访问类的构造函数的接口。
- `Array` 类 - 该类提供动态地生成和访问 JAVA 数组的方法。
- `Modifier` 类 - 提供了 static 方法和常量，对类和成员访问修饰符进行解码。
- `Proxy` 类 - 提供动态地生成代理类和类实例的静态方法。

### 获得 Class 对象

获得 Class 的三种方法：

（1）**使用 Class 类的 `forName` 静态方法**

示例：

```java
package io.github.dunwu.javacore.reflect;

public class ReflectClassDemo01 {
    public static void main(String[] args) throws ClassNotFoundException {
        Class c1 = Class.forName("io.github.dunwu.javacore.reflect.ReflectClassDemo01");
        System.out.println(c1.getCanonicalName());

        Class c2 = Class.forName("[D");
        System.out.println(c2.getCanonicalName());

        Class c3 = Class.forName("[[Ljava.lang.String;");
        System.out.println(c3.getCanonicalName());
    }
}
//Output:
//io.github.dunwu.javacore.reflect.ReflectClassDemo01
//double[]
//java.lang.String[][]
```

使用类的完全限定名来反射对象的类。常见的应用场景为：在 JDBC 开发中常用此方法加载数据库驱动。

（2）**直接获取某一个对象的 `class`**

示例：

```java
public class ReflectClassDemo02 {
    public static void main(String[] args) {
        boolean b;
        // Class c = b.getClass(); // 编译错误
        Class c1 = boolean.class;
        System.out.println(c1.getCanonicalName());

        Class c2 = java.io.PrintStream.class;
        System.out.println(c2.getCanonicalName());

        Class c3 = int[][][].class;
        System.out.println(c3.getCanonicalName());
    }
}
//Output:
//boolean
//java.io.PrintStream
//int[][][]
```

（3）**调用 Object 的 `getClass` 方法**，示例：

Object 类中有 getClass 方法，因为所有类都继承 Object 类。从而调用 Object 类来获取

示例：

```java
package io.github.dunwu.javacore.reflect;

import java.util.HashSet;
import java.util.Set;

public class ReflectClassDemo03 {
    enum E {A, B}

    public static void main(String[] args) {
        Class c = "foo".getClass();
        System.out.println(c.getCanonicalName());

        Class c2 = ReflectClassDemo03.E.A.getClass();
        System.out.println(c2.getCanonicalName());

        byte[] bytes = new byte[1024];
        Class c3 = bytes.getClass();
        System.out.println(c3.getCanonicalName());

        Set<String> set = new HashSet<>();
        Class c4 = set.getClass();
        System.out.println(c4.getCanonicalName());
    }
}
//Output:
//java.lang.String
//io.github.dunwu.javacore.reflect.ReflectClassDemo.E
//byte[]
//java.util.HashSet
```

### 判断是否为某个类的实例

判断是否为某个类的实例有两种方式：

1. **用 `instanceof` 关键字**
2. **用 `Class` 对象的 `isInstance` 方法**（它是一个 Native 方法）

示例：

```java
public class InstanceofDemo {
    public static void main(String[] args) {
        ArrayList arrayList = new ArrayList();
        if (arrayList instanceof List) {
            System.out.println("ArrayList is List");
        }
        if (List.class.isInstance(arrayList)) {
            System.out.println("ArrayList is List");
        }
    }
}
//Output:
//ArrayList is List
//ArrayList is List
```

### 创建实例

通过反射来创建实例对象主要有两种方式：

- 用 `Class` 对象的 `newInstance` 方法。
- 用 `Constructor` 对象的 `newInstance` 方法。

示例：

```java
public class NewInstanceDemo {
    public static void main(String[] args)
        throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?> c1 = StringBuilder.class;
        StringBuilder sb = (StringBuilder) c1.newInstance();
        sb.append("aaa");
        System.out.println(sb.toString());

        //获取String所对应的Class对象
        Class<?> c2 = String.class;
        //获取String类带一个String参数的构造器
        Constructor constructor = c2.getConstructor(String.class);
        //根据构造器创建实例
        String str2 = (String) constructor.newInstance("bbb");
        System.out.println(str2);
    }
}
//Output:
//aaa
//bbb
```

### Field

`Class` 对象提供以下方法获取对象的成员（`Field`）：

- `getFiled` - 根据名称获取公有的（public）类成员。
- `getDeclaredField` - 根据名称获取已声明的类成员。但不能得到其父类的类成员。
- `getFields` - 获取所有公有的（public）类成员。
- `getDeclaredFields` - 获取所有已声明的类成员。

示例如下：

```java
public class ReflectFieldDemo {
    class FieldSpy<T> {
        public boolean[][] b = { {false, false}, {true, true} };
        public String name = "Alice";
        public List<Integer> list;
        public T val;
    }

    public static void main(String[] args) throws NoSuchFieldException {
        Field f1 = FieldSpy.class.getField("b");
        System.out.format("Type: %s%n", f1.getType());

        Field f2 = FieldSpy.class.getField("name");
        System.out.format("Type: %s%n", f2.getType());

        Field f3 = FieldSpy.class.getField("list");
        System.out.format("Type: %s%n", f3.getType());

        Field f4 = FieldSpy.class.getField("val");
        System.out.format("Type: %s%n", f4.getType());
    }
}
//Output:
//Type: class [[Z
//Type: class java.lang.String
//Type: interface java.util.List
//Type: class java.lang.Object
```

### Method

`Class` 对象提供以下方法获取对象的方法（`Method`）：

- `getMethod` - 返回类或接口的特定方法。其中第一个参数为方法名称，后面的参数为方法参数对应 Class 的对象。
- `getDeclaredMethod` - 返回类或接口的特定声明方法。其中第一个参数为方法名称，后面的参数为方法参数对应 Class 的对象。
- `getMethods` - 返回类或接口的所有 public 方法，包括其父类的 public 方法。
- `getDeclaredMethods` - 返回类或接口声明的所有方法，包括 public、protected、默认（包）访问和 private 方法，但不包括继承的方法。

获取一个 `Method` 对象后，可以用 `invoke` 方法来调用这个方法。

`invoke` 方法的原型为:

```java
public Object invoke(Object obj, Object... args)
        throws IllegalAccessException, IllegalArgumentException,
           InvocationTargetException
```

示例：

```java
public class ReflectMethodDemo {
    public static void main(String[] args)
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        // 返回所有方法
        Method[] methods1 = System.class.getDeclaredMethods();
        System.out.println("System getDeclaredMethods 清单（数量 = " + methods1.length + "）：");
        for (Method m : methods1) {
            System.out.println(m);
        }

        // 返回所有 public 方法
        Method[] methods2 = System.class.getMethods();
        System.out.println("System getMethods 清单（数量 = " + methods2.length + "）：");
        for (Method m : methods2) {
            System.out.println(m);
        }

        // 利用 Method 的 invoke 方法调用 System.currentTimeMillis()
        Method method = System.class.getMethod("currentTimeMillis");
        System.out.println(method);
        System.out.println(method.invoke(null));
    }
}
```

### Constructor

`Class` 对象提供以下方法获取对象的构造方法（`Constructor`）：

- `getConstructor` - 返回类的特定 public 构造方法。参数为方法参数对应 Class 的对象。
- `getDeclaredConstructor` - 返回类的特定构造方法。参数为方法参数对应 Class 的对象。
- `getConstructors` - 返回类的所有 public 构造方法。
- `getDeclaredConstructors` - 返回类的所有构造方法。

获取一个 `Constructor` 对象后，可以用 `newInstance` 方法来创建类实例。

示例：

```java
public class ReflectMethodConstructorDemo {
    public static void main(String[] args)
        throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?>[] constructors1 = String.class.getDeclaredConstructors();
        System.out.println("String getDeclaredConstructors 清单（数量 = " + constructors1.length + "）：");
        for (Constructor c : constructors1) {
            System.out.println(c);
        }

        Constructor<?>[] constructors2 = String.class.getConstructors();
        System.out.println("String getConstructors 清单（数量 = " + constructors2.length + "）：");
        for (Constructor c : constructors2) {
            System.out.println(c);
        }

        System.out.println("====================");
        Constructor constructor = String.class.getConstructor(String.class);
        System.out.println(constructor);
        String str = (String) constructor.newInstance("bbb");
        System.out.println(str);
    }
}
```

### Array

数组在 Java 里是比较特殊的一种类型，它可以赋值给一个对象引用。下面我们看一看利用反射创建数组的例子：

```java
public class ReflectArrayDemo {
    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> cls = Class.forName("java.lang.String");
        Object array = Array.newInstance(cls, 25);
        //往数组里添加内容
        Array.set(array, 0, "Scala");
        Array.set(array, 1, "Java");
        Array.set(array, 2, "Groovy");
        Array.set(array, 3, "Scala");
        Array.set(array, 4, "Clojure");
        //获取某一项的内容
        System.out.println(Array.get(array, 3));
    }
}
//Output:
//Scala
```

其中的 Array 类为 `java.lang.reflect.Array` 类。我们通过 `Array.newInstance` 创建数组对象，它的原型是：

```java
public static Object newInstance(Class<?> componentType, int length)
    throws NegativeArraySizeException {
    return newArray(componentType, length);
}
```

## 动态代理

动态代理是反射的一个非常重要的应用场景。动态代理常被用于一些 Java 框架中。例如 Spring 的 AOP ，Dubbo 的 SPI 接口，就是基于 Java 动态代理实现的。

### 静态代理

> 静态代理其实就是指设计模式中的代理模式。
>
> **代理模式为其他对象提供一种代理以控制对这个对象的访问。**

<div align="center"><img src="https://upload-images.jianshu.io/upload_images/3101171-6269723ea61527bd.png"/></div>

**Subject** 定义了 RealSubject 和 Proxy 的公共接口，这样就在任何使用 RealSubject 的地方都可以使用 Proxy 。

```java
abstract class Subject {
    public abstract void Request();
}
```

**RealSubject** 定义 Proxy 所代表的真实实体。

```java
class RealSubject extends Subject {
    @Override
    public void Request() {
        System.out.println("真实的请求");
    }
}
```

**Proxy** 保存一个引用使得代理可以访问实体，并提供一个与 Subject 的接口相同的接口，这样代理就可以用来替代实体。

```java
class Proxy extends Subject {
    private RealSubject real;

    @Override
    public void Request() {
        if (null == real) {
            real = new RealSubject();
        }
        real.Request();
    }
}
```

> 说明：
>
> 静态代理模式固然在访问无法访问的资源，增强现有的接口业务功能方面有很大的优点，但是大量使用这种静态代理，会使我们系统内的类的规模增大，并且不易维护；并且由于 Proxy 和 RealSubject 的功能本质上是相同的，Proxy 只是起到了中介的作用，这种代理在系统中的存在，导致系统结构比较臃肿和松散。

### 动态代理

为了解决静态代理的问题，就有了创建动态代理的想法：

在运行状态中，需要代理的地方，根据 Subject 和 RealSubject，动态地创建一个 Proxy，用完之后，就会销毁，这样就可以避免了 Proxy 角色的 class 在系统中冗杂的问题了。

<div align="center"><img src="http://dunwu.test.upcdn.net/snap/1553614585028.png!zp"/></div>

Java 动态代理基于经典代理模式，引入了一个 InvocationHandler，InvocationHandler 负责统一管理所有的方法调用。

动态代理步骤：

1.  获取 RealSubject 上的所有接口列表；
2.  确定要生成的代理类的类名，默认为：`com.sun.proxy.$ProxyXXXX`；
3.  根据需要实现的接口信息，在代码中动态创建 该 Proxy 类的字节码；
4.  将对应的字节码转换为对应的 class 对象；
5.  创建 `InvocationHandler` 实例 handler，用来处理 `Proxy` 所有方法调用；
6.  Proxy 的 class 对象 以创建的 handler 对象为参数，实例化一个 proxy 对象。

从上面可以看出，JDK 动态代理的实现是基于实现接口的方式，使得 Proxy 和 RealSubject 具有相同的功能。

但其实还有一种思路：通过继承。即：让 Proxy 继承 RealSubject，这样二者同样具有相同的功能，Proxy 还可以通过重写 RealSubject 中的方法，来实现多态。CGLIB 就是基于这种思路设计的。

在 Java 的动态代理机制中，有两个重要的类（接口），一个是 `InvocationHandler` 接口、另一个则是 `Proxy` 类，这一个类和一个接口是实现我们动态代理所必须用到的。

### InvocationHandler 接口

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
- **method** - 所要调用真实对象的某个方法的 `Method` 对象
- **args** - 所要调用真实对象某个方法时接受的参数

如果不是很明白，等下通过一个实例会对这几个参数进行更深的讲解。

### Proxy 类

`Proxy` 这个类的作用就是用来动态创建一个代理对象的类，它提供了许多的方法，但是我们用的最多的就是 `newProxyInstance` 这个方法：

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

我们首先来看看 `com.sun.proxy.$Proxy0` 这东西，我们看到，这个东西是由 `System.out.println(subject.getClass().getName());` 这条语句打印出来的，那么为什么我们返回的这个代理对象的类名是这样的呢？

```java
Subject subject = (Subject)Proxy.newProxyInstance(handler.getClass().getClassLoader(), realSubject
                .getClass().getInterfaces(), handler);
```

可能我以为返回的这个代理对象会是 Subject 类型的对象，或者是 InvocationHandler 的对象，结果却不是，首先我们解释一下**为什么我们这里可以将其转化为 Subject 类型的对象？**

原因就是：在 newProxyInstance 这个方法的第二个参数上，我们给这个代理对象提供了一组什么接口，那么我这个代理对象就会实现了这组接口，这个时候我们当然可以将这个代理对象强制类型转化为这组接口中的任意一个，因为这里的接口是 Subject 类型，所以就可以将其转化为 Subject 类型了。

**同时我们一定要记住，通过 `Proxy.newProxyInstance` 创建的代理对象是在 jvm 运行时动态生成的一个对象，它并不是我们的 InvocationHandler 类型，也不是我们定义的那组接口的类型，而是在运行是动态生成的一个对象，并且命名方式都是这样的形式，以\$开头，proxy 为中，最后一个数字表示对象的标号**。

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

## 小结

<div align="center"><img src="http://dunwu.test.upcdn.net/snap/1553615203764.png!zp"/></div>

<div align="center"><img src="http://dunwu.test.upcdn.net/snap/1553615153731.png!zp"/></div>

## 参考资料

- [Java 编程思想](https://book.douban.com/subject/2130190/)
- [JAVA 核心技术（卷 1）](https://book.douban.com/subject/3146174/)
- [深入解析 Java 反射（1） - 基础](https://www.sczyh30.com/posts/Java/java-reflection-1/)
- [Java 基础之—反射（非常重要）](https://blog.csdn.net/sinat_38259539/article/details/71799078)
- [官方 Reflection API 文档](https://docs.oracle.com/javase/tutorial/reflect/index.html)
- [java 的动态代理机制详解](https://www.cnblogs.com/xiaoluo501395377/p/3383130.html)
- [Java 动态代理机制详解（JDK 和 CGLIB，Javassist，ASM）](https://blog.csdn.net/luanlouis/article/details/24589193)
