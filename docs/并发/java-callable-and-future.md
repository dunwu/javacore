---
title: Callable、Future 和 FutureTask
date: 2018/01/22
categories:
- javase
tags:
- javase
- concurrent
---
# Callable、Future 和 FutureTask

创建线程有两种方式，一种是直接继承 `Thread` ，另外一种就是实现 `Runnable` 接口。

这两种方式都有一个缺陷：在执行完任务之后无法获取执行结果。

如果需要获取执行结果，就必须通过共享变量或者使用线程通信的方式来达到效果，这样使用起来就比较麻烦。

而自从 Java 1.5 开始，就提供了 `Callable` 和 `Future`，通过它们可以在任务执行完毕之后得到任务执行结果。

## Callable 与 Runnable

`java.lang.Runnable` 的定义如下：

```java
public interface Runnable {
    public abstract void run();
}
```

由于 `run()` 方法返回值为 `void` 类型，所以在执行完任务之后无法返回任何结果。

`java.util.concurrent.Callable` 的定义如下：

```java
public interface Callable<V> {
    V call() throws Exception;
}
```

可以看到，这是一个泛型接口，`call()` 函数返回的类型就是传递进来的 `V` 类型。

那么怎么使用Callable呢？一般情况下是配合 `ExecutorService` 来使用的，在ExecutorService接口中声明了若干个submit方法的重载版本：

```java
<T> Future<T> submit(Callable<T> task);
<T> Future<T> submit(Runnable task, T result);
Future<?> submit(Runnable task);
```

第一个submit方法里面的参数类型就是 Callable。

暂时只需要知道 Callable 一般是和 ExecutorService 配合来使用的，具体的使用方法讲在后面讲述。

一般情况下我们使用第一个 submit 方法和第三个 submit 方法，第二个 submit 方法很少使用。

## Future

Future 就是对于具体的 Runnable 或者 Callable 任务的执行结果进行取消、查询是否完成、获取结果。必要时可以通过 get 方法获取执行结果，该方法会阻塞直到任务返回结果。

`java.util.concurrent.Future` 定义如下：

```java
public interface Future<V> {

    boolean cancel(boolean mayInterruptIfRunning);

    boolean isCancelled();

    boolean isDone();

    V get() throws InterruptedException, ExecutionException;

    V get(long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException;
}
```

**说明**

* cancel方法用来取消任务，如果取消任务成功则返回true，如果取消任务失败则返回false。参数mayInterruptIfRunning表示是否允许取消正在执行却没有执行完毕的任务，如果设置true，则表示可以取消正在执行过程中的任务。如果任务已经完成，则无论mayInterruptIfRunning为true还是false，此方法肯定返回false，即如果取消已经完成的任务会返回false；如果任务正在执行，若mayInterruptIfRunning设置为true，则返回true，若mayInterruptIfRunning设置为false，则返回false；如果任务还没有执行，则无论mayInterruptIfRunning为true还是false，肯定返回true。
* isCancelled方法表示任务是否被取消成功，如果在任务正常完成前被取消成功，则返回 true。
* isDone方法表示任务是否已经完成，若任务完成，则返回true；
* get()方法用来获取执行结果，这个方法会产生阻塞，会一直等到任务执行完毕才返回；
* get(long timeout, TimeUnit unit)用来获取执行结果，如果在指定时间内，还没获取到结果，就直接返回null。

也就是说Future提供了三种功能：

1. 判断任务是否完成；
2. 能够中断任务；
3. 能够获取任务执行结果。

因为Future只是一个接口，所以是无法直接用来创建对象使用的，因此就有了下面的FutureTask。

## FutureTask

我们先来看一下FutureTask的实现：

```java
public class FutureTask<V> implements RunnableFuture<V>
```

FutureTask类实现了RunnableFuture接口，我们看一下RunnableFuture接口的实现：

```java
public interface RunnableFuture<V> extends Runnable, Future<V> {
    void run();
}
```

可以看出 RunnableFuture 继承了 Runnable 接口和 Future 接口，而 FutureTask 实现了RunnableFuture 接口。所以它既可以作为 Runnable 被线程执行，又可以作为 Future 得到Callable的返回值。

FutureTask提供了2个构造器：

```java
public FutureTask(Callable<V> callable) {}
public FutureTask(Runnable runnable, V result) {}
```
事实上，FutureTask是Future接口的一个唯一实现类。

## 使用示例

示例 - Callable + Future

```java
public class FutureDemo {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        CallableDemo task = new CallableDemo();
        Future<Integer> result = executor.submit(task);
        executor.shutdown();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        System.out.println("主线程在执行任务");

        try {
            System.out.println("task运行结果" + result.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("所有任务执行完毕");
    }
}

class CallableDemo implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println("子线程在进行计算");
        Thread.sleep(3000);
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            sum += i;
        }
        return sum;
    }
}
```

示例 - Callable + FutureTask


```java
public class FutureTaskDemo {
    public static void main(String[] args) {
        // 第一种方式
        ExecutorService executor = Executors.newCachedThreadPool();
        CallableDemo task = new CallableDemo();
        FutureTask<Integer> futureTask = new FutureTask<>(task);
        executor.submit(futureTask);
        executor.shutdown();

        // 第二种方式，注意这种方式和第一种方式效果是类似的，只不过一个使用的是ExecutorService，一个使用的是Thread
        /*
         * Task task = new Task(); FutureTask<Integer> futureTask = new FutureTask<Integer>(task); Thread thread = new
         * Thread(futureTask); thread.start();
         */

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        System.out.println("主线程在执行任务");

        try {
            System.out.println("task运行结果" + futureTask.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("所有任务执行完毕");
    }
}

class CallableDemo implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println("子线程在进行计算");
        Thread.sleep(3000);
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            sum += i;
        }
        return sum;
    }
}
```
