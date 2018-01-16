如何创建线程

## 程序和线程

在 Java 中，一个应用程序对应着一个 JVM 实例（也有地方称为JVM进程），一般来说名字默认为java.exe或者javaw.exe（windows下可以通过任务管理器查看）。

Java 采用的是单线程编程模型，即在我们自己的程序中如果没有主动创建线程的话，只会创建一个线程，通常称为主线程。但是要注意，虽然只有一个线程来执行任务，不代表 JVM 中只有一个线程，JVM 实例在创建的时候，同时会创建很多其他的线程（比如垃圾收集器线程）。

由于Java采用的是单线程编程模型，因此在进行UI编程时要注意将耗时的操作放在子线程中进行，以避免阻塞主线程（在UI编程时，主线程即UI线程，用来处理用户的交互事件）。

## 创建线程的方法

在 java 中如果要创建线程的话，一般有两种方式：

- 继承 Thread 类
- 实现 Runnable 接口

### 继承 Thread 类

```
public class ThreadDemo extends Thread {
    private int ticket = 5;

    public ThreadDemo(String name) {
        super(name);
    }

    /**
     * 覆写run()方法，作为线程 的操作主体
     */
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            if (this.ticket > 0) {
                System.out.println(this.getName() + " 卖票：ticket = " + ticket--);
            }
        }
    }
}
```

启动线程

```
ThreadDemo threadDemo1 = new ThreadDemo("线程A"); // 实例化对象
ThreadDemo threadDemo2 = new ThreadDemo("线程B"); // 实例化对象
ThreadDemo threadDemo3 = new ThreadDemo("线程C"); // 实例化对象
threadDemo1.run(); // 调用线程主体
threadDemo2.run(); // 调用线程主体
threadDemo3.run(); // 调用线程主体
```

### 实现 Runnable 接口

```
public class RunnableDemo implements Runnable {
    private int ticket = 5;
    private String name;

    public RunnableDemo() {

    }

    public RunnableDemo(String name) {
        this.name = name;
    }

    /**
     * 覆写run()方法，作为线程 的操作主体
     */
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            if (this.ticket > 0) {
                System.out.println(this.name + " 卖票：ticket = " + ticket--);
            }
        }
    }
}
```

启动线程

```
RunnableDemo runnableDemo = new RunnableDemo("Runnable 线程") ;    // 实例化对象
new Thread(runnableDemo).run() ;   // 调用线程主体
new Thread(runnableDemo).run() ;   // 调用线程主体
new Thread(runnableDemo).run() ;   // 调用线程主体
```