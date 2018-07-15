---
title: Java 动态代理
date: 2018/07/15
categories:
- javacore
tags:
- java
- javacore
- advanced
---

# Java 动态代理

<!-- TOC depthFrom:2 depthTo:4 -->

- [1. InvocationHandler 接口](#1-invocationhandler-接口)
- [2. Proxy 类](#2-proxy-类)
- [3. 动态代理实例](#3-动态代理实例)
- [4. 资料](#4-资料)

<!-- /TOC -->

Java 动态代理常被用于一些 Java 框架中。例如 Spring 的 AOP ，Dubbo 的 SPI 接口，就是基于 Java 动态代理实现的。

在 Java 的动态代理机制中，有两个重要的类或接口，一个是 InvocationHandler(Interface)、另一个则是 Proxy(Class)，这一个类和接口是实现我们动态代理所必须用到的。首先我们先来看看 java 的 API 帮助文档是怎么样对这两个类进行描述的：

## 1. InvocationHandler 接口

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

## 2. Proxy 类

Proxy 这个类的作用就是用来动态创建一个代理对象的类，它提供了许多的方法，但是我们用的最多的就是 `newProxyInstance` 这个方法：

```java
public static Object newProxyInstance(ClassLoader loader, Class<?>[] interfaces,  InvocationHandler h)  throws IllegalArgumentException
```

这个方法的作用就是得到一个动态的代理对象。

参数说明：

- **loader** - 一个 ClassLoader 对象，定义了由哪个 ClassLoader 对象来对生成的代理对象进行加载。
- **interfaces** - 一个 Interface 对象的数组，表示的是我将要给我需要代理的对象提供一组什么接口，如果我提供了一组接口给它，那么这个代理对象就宣称实现了该接口(多态)，这样我就能调用这组接口中的方法了
- **h** - 一个 InvocationHandler 对象，表示的是当我这个动态代理对象在调用方法的时候，会关联到哪一个 InvocationHandler 对象上

## 3. 动态代理实例

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

## 4. 资料

[java 的动态代理机制详解](https://www.cnblogs.com/xiaoluo501395377/p/3383130.html)
