---
title: Java 线程主要操作方法
date: 2018/01/20
categories:
- javase
tags:
- javase
- concurrent
---
# Java 线程主要操作方法

## set、get 线程名称

在 Thread 类中可以通过 `setName()`、 `getName()` 来设置、获取线程名称。

```java
class ThreadName implements Runnable { // 实现Runnable接口
    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            System.out.println(Thread.currentThread().getName() + "运行，i = " + i); // 取得当前线程的名字
        }
    }
};

public class ThreadNameDemo {
    public static void main(String[] args) {
        ThreadName mt = new ThreadName(); // 实例化Runnable子类对象
        new Thread(mt).start(); // 系统自动设置线程名称
        new Thread(mt, "线程-A").start(); // 手工设置线程名称
        new Thread(mt, "线程-B").start(); // 手工设置线程名称
        new Thread(mt).start(); // 系统自动设置线程名称
        new Thread(mt).start(); // 系统自动设置线程名称
    }
};
```

## 判断线程是否启动

在 Thread 类中可以通过 `isAlive()` 来判断线程是否启动。

```java
class MyThread implements Runnable { // 实现Runnable接口
    public void run() { // 覆写run()方法
        for (int i = 0; i < 3; i++) {
            System.out.println(Thread.currentThread().getName() + "运行，i = " + i); // 取得当前线程的名字
        }
    }
};

public class ThreadAliveDemo {
    public static void main(String[] args) {
        MyThread mt = new MyThread(); // 实例化Runnable子类对象
        Thread t = new Thread(mt, "线程"); // 实例化Thread对象
        System.out.println("线程开始执行之前 --> " + t.isAlive()); // 判断是否启动
        t.start(); // 启动线程
        System.out.println("线程开始执行之后 --> " + t.isAlive()); // 判断是否启动
        for (int i = 0; i < 3; i++) {
            System.out.println(" main运行 --> " + i);
        }
        // 以下的输出结果不确定
        System.out.println("代码执行之后 --> " + t.isAlive()); // 判断是否启动

    }
};
```

## 线程的强制执行

在线程操作中，可以使用 `join()` 方法让一个线程强制运行，线程强制运行期间，其他线程无法运行，必须等待此线程完成之后才可以继续执行。

```java
class ThreadJoin implements Runnable { // 实现Runnable接口
    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            System.out.println(Thread.currentThread().getName() + "运行，i = " + i); // 取得当前线程的名字
        }
    }
};

public class ThreadJoinDemo {
    public static void main(String[] args) {
        ThreadJoin mt = new ThreadJoin(); // 实例化Runnable子类对象
        Thread t = new Thread(mt, "线程"); // 实例化Thread对象
        t.start(); // 启动线程
        for (int i = 0; i < 50; i++) {
            if (i > 10) {
                try {
                    t.join(); // 线程强制运行
                } catch (InterruptedException e) {}
            }
            System.out.println("Main线程运行 --> " + i);
        }
    }
};
```

## 线程的休眠

直接使用 `Thread.sleep()` 方法即可实现休眠。

```java
public class ThreadSleepDemo implements Runnable {
    private String name;
    private int time;

    public ThreadSleepDemo(String name, int time) {
        this.name = name; // 设置线程名称
        this.time = time; // 设置休眠时间
    }

    @Override
    public void run() {
        try {
            Thread.sleep(this.time); // 休眠指定的时间
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.name + "线程，休眠" + this.time + "毫秒。");
    }

    public static void main(String[] args) {
        ThreadSleepDemo mt1 = new ThreadSleepDemo("线程A", 1000); // 定义线程对象，指定休眠时间
        ThreadSleepDemo mt2 = new ThreadSleepDemo("线程B", 2000); // 定义线程对象，指定休眠时间
        ThreadSleepDemo mt3 = new ThreadSleepDemo("线程C", 3000); // 定义线程对象，指定休眠时间
        new Thread(mt1).start(); // 启动线程
        new Thread(mt2).start(); // 启动线程
        new Thread(mt3).start(); // 启动线程
    }
};
```

## 中断线程

当一个线程运行时，另一个线程可以直接通过 `interrupt()` 方法中断其运行状态。

```java
class ThreadInterrupt implements Runnable { // 实现Runnable接口
    public void run() { // 覆写run()方法
        System.out.println("1、进入run()方法");
        try {
            Thread.sleep(10000); // 线程休眠10秒
            System.out.println("2、已经完成了休眠");
        } catch (InterruptedException e) {
            System.out.println("3、休眠被终止");
            return; // 返回调用处
        }
        System.out.println("4、run()方法正常结束");
    }
};

public class ThreadInterruptDemo {
    public static void main(String[] args) {
        ThreadInterrupt mt = new ThreadInterrupt(); // 实例化Runnable子类对象
        Thread t = new Thread(mt, "线程"); // 实例化Thread对象
        t.start(); // 启动线程
        try {
            Thread.sleep(2000); // 线程休眠2秒
        } catch (InterruptedException e) {
            System.out.println("3、休眠被终止");
        }
        t.interrupt(); // 中断线程执行
    }
};

```

## 守护线程

在 Java 程序中，只要前台有一个线程在运行，则整个 Java 进程就不会消失，所以此时可以设置一个守护线程，这样即使 Java 进程结束了，此守护线程依然会继续执行。要想实现这样的操作，直接使用 `setDaemon()` 方法即可。

```java
class ThreadDaemon implements Runnable { // 实现Runnable接口
    @Override
    public void run() {
        while (true) {
            System.out.println(Thread.currentThread().getName() + "在运行。");
        }
    }
};

public class ThreadDaemonDemo {
    public static void main(String[] args) {
        ThreadDaemon mt = new ThreadDaemon(); // 实例化Runnable子类对象
        Thread t = new Thread(mt, "线程"); // 实例化Thread对象
        t.setDaemon(true); // 此线程在后台运行
        t.start(); // 启动线程
    }
};
```

## 线程优先级

在 Java 中，所有线程在运行前都会保持在就绪状态，那么此时，哪个线程优先级高，哪个线程就有可能被先执行。

```java
class ThreadPriority implements Runnable { // 实现Runnable接口
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(500); // 线程休眠
            } catch (InterruptedException e) {}
            System.out.println(Thread.currentThread().getName() + "运行，i = " + i); // 取得当前线程的名字
        }
    }
};

public class ThreadPriorityDemo {
    public static void main(String[] args) {
        Thread t1 = new Thread(new ThreadPriority(), "线程A"); // 实例化线程对象
        Thread t2 = new Thread(new ThreadPriority(), "线程B"); // 实例化线程对象
        Thread t3 = new Thread(new ThreadPriority(), "线程C"); // 实例化线程对象
        t1.setPriority(Thread.MIN_PRIORITY); // 优先级最低
        t2.setPriority(Thread.MAX_PRIORITY); // 优先级最低
        t3.setPriority(Thread.NORM_PRIORITY); // 优先级最低
        t1.start(); // 启动线程
        t2.start(); // 启动线程
        t3.start(); // 启动线程
    }
};
```

## 线程的礼让

在线程操作中，可以使用 `yeild()` 方法将一个线程的操作暂时让给其他线程执行。

```java
public class ThreadYieldDemo implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "运行，i = " + i);
            if (i == 2) {
                System.out.print("线程礼让：");
                Thread.currentThread().yield();
            }
        }
    }

    public static void main(String[] args) {
        ThreadYieldDemo thread = new ThreadYieldDemo();
        Thread t1 = new Thread(thread, "线程A");
        Thread t2 = new Thread(thread, "线程B");
        t1.start();
        t2.start();
    }
};
```
