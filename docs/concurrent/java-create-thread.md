---
title: Java 中线程的实现
date: 2018/01/16
categories:
- javase
tags:
- javase
- concurrent
---
# Java 中线程的实现

Java 中实现线程有两种方式：

- 继承 Thread 类
- 实现 Runnable 接口

## 继承 Thread 类

一个类只要继承了 `java.lang.Thread` 类，此类就成为多线程类。

继承 `Thread` 的子类，必须明确地覆写 `Thread` 类中的 `run()` 方法，此方法为线程的主体。

**继承 Thread 类语法**

```java
public class ThreadDemo extends Thread {
    // 属性
    // 方法

    /**
     * 覆写 Thread 类中的 run() 方法，此方法为线程的主体
     */
    @Override
    public void run() {
        // 线程主体
    }
}
```

**示例**

```java
public class MyThread01 extends Thread {
    private String name;

    /**
     * 通过构造方法配置name属性
     */
    public MyThread01(String name) {
        this.name = name;
    }

    /**
     * 覆写run()方法，作为线程 的操作主体
     */
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(name + "运行，i = " + i);
        }
    }
};

// 错误的启动线程方式
MyThread01 mt1 = new MyThread01("线程A "); // 实例化对象
MyThread01 mt2 = new MyThread01("线程B "); // 实例化对象
mt1.run(); // 调用线程主体
mt2.run(); // 调用线程主体

// 正确的启动线程方式
MyThread01 mt1 = new MyThread01("线程A "); // 实例化对象
MyThread01 mt2 = new MyThread01("线程B "); // 实例化对象
mt1.start(); // 调用线程主体
mt2.start(); // 调用线程主体

// 重复调用 start() 会抛出 java.lang.IllegalThreadStateException 异常
MyThread01 mt1 = new MyThread01("线程A "); // 实例化对象
mt1.start(); // 调用线程主体
mt1.start(); // 调用线程主体
```

> **说明**
>
> **启动继承了 Thread 类的线程类，必须使用 start() 方法，并且每个实例只能执行一次。**
>
> 为什么不能使用 run() 方法启动线程？
>
> 我们先看一下 Thread 的源码片段：
>
> ```java
> public class Thread implements Runnable {
>     ...
> 	private native void start0();
>     ...
>     
> 	public synchronized void start() {
> 		if (threadStatus != 0)
> 			throw new IllegalThreadStateException();
>
> 		group.add(this);
>
> 		boolean started = false;
> 		try {
> 			start0();
> 			started = true;
> 		} finally {
> 		   ...
> 		}
> 	}
> }
> ```
>
> `start()` 方法先判断线程状态，如果重复调用会抛出异常。然后调用 `start0()` 方法，`start0()` 方法有一个 `native` 修饰符，这表明调用本机的操作系统函数。

## 实现 Runnable 接口

在 Java 中，还有一种实现线程的方式：实现 `Runnable` 接口。

**Runnable 接口声明**

`Runnable` 接口只定义了一个 `run()` 方法。

```java
public interface Runnable {
    public abstract void run();
}
```

**实现 Runnable 接口语法**

细心的朋友不难发现，实现 Runnable 接口和继承 Thread 类的关键性一步都是覆写 `run()` 方法。我们查看 `Thread` 类的源码就会明白这是为什么。

因为 `Thread` 类其实也是实现了`Runnable` 接口，这就决定了定义线程主体都是在 `run()` 方法中。

```java
public class RunnableDemo implements Runnable {
    // 属性
    // 方法

    /**
     * 覆写 Thread 类中的 run() 方法，此方法为线程的主体
     */
    @Override
    public void run() {
        // 线程主体
    }
}
```

**示例**

```java
public class MyThread02 implements Runnable {
    private String name;

    /**
     * 通过构造方法配置name属性
     */
    public MyThread02(String name) {
        this.name = name;
    }

    /**
     * 覆写run()方法，作为线程 的操作主体
     */
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(name + "运行，i = " + i);
        }
    }
}

// 启动线程
MyThread02 mt1 = new MyThread02("线程A "); // 实例化对象
MyThread02 mt2 = new MyThread02("线程B "); // 实例化对象
Thread thread1 = new Thread(mt1); // 实例化 Thread 对象
Thread thread2 = new Thread(mt2); // 实例化 Thread 对象
thread1.start(); // 调用线程主体
thread2.start(); // 调用线程主体
```



