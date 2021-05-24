# Java 原子变量类

> **📦 本文以及示例源码已归档在 [javacore](https://github.com/dunwu/javacore/)**

<!-- TOC depthFrom:2 depthTo:3 -->

- [1. 原子变量类简介](#1-原子变量类简介)
  - [1.1. 为何需要原子变量类](#11-为何需要原子变量类)
  - [1.2. 原子变量类的作用](#12-原子变量类的作用)
- [2. 基本类型](#2-基本类型)
  - [2.1. **`AtomicInteger` 用法**](#21-atomicinteger-用法)
  - [2.2. **`AtomicInteger` 实现**](#22-atomicinteger-实现)
- [3. 引用类型](#3-引用类型)
- [4. 数组类型](#4-数组类型)
- [5. 属性更新器类型](#5-属性更新器类型)
- [6. 原子化的累加器](#6-原子化的累加器)
- [7. 参考资料](#7-参考资料)

<!-- /TOC -->

## 1. 原子变量类简介

### 1.1. 为何需要原子变量类

保证线程安全是 Java 并发编程必须要解决的重要问题。Java 从原子性、可见性、有序性这三大特性入手，确保多线程的数据一致性。

- 确保线程安全最常见的做法是利用锁机制（`Lock`、`sychronized`）来对共享数据做互斥同步，这样在同一个时刻，只有一个线程可以执行某个方法或者某个代码块，那么操作必然是原子性的，线程安全的。互斥同步最主要的问题是线程阻塞和唤醒所带来的性能问题。
- `volatile` 是轻量级的锁（自然比普通锁性能要好），它保证了共享变量在多线程中的可见性，但无法保证原子性。所以，它只能在一些特定场景下使用。
- 为了兼顾原子性以及锁带来的性能问题，Java 引入了 CAS （主要体现在 `Unsafe` 类）来实现非阻塞同步（也叫乐观锁）。并基于 CAS ，提供了一套原子工具类。

### 1.2. 原子变量类的作用

原子变量类 **比锁的粒度更细，更轻量级**，并且对于在多处理器系统上实现高性能的并发代码来说是非常关键的。原子变量将发生竞争的范围缩小到单个变量上。

原子变量类相当于一种泛化的 `volatile` 变量，能够**支持原子的、有条件的读/改/写操**作。

原子类在内部使用 CAS 指令（基于硬件的支持）来实现同步。这些指令通常比锁更快。

原子变量类可以分为 4 组：

- 基本类型
  - `AtomicBoolean` - 布尔类型原子类
  - `AtomicInteger` - 整型原子类
  - `AtomicLong` - 长整型原子类
- 引用类型
  - `AtomicReference` - 引用类型原子类
  - `AtomicMarkableReference` - 带有标记位的引用类型原子类
  - `AtomicStampedReference` - 带有版本号的引用类型原子类
- 数组类型
  - `AtomicIntegerArray` - 整形数组原子类
  - `AtomicLongArray` - 长整型数组原子类
  - `AtomicReferenceArray` - 引用类型数组原子类
- 属性更新器类型
  - `AtomicIntegerFieldUpdater` - 整型字段的原子更新器。
  - `AtomicLongFieldUpdater` - 长整型字段的原子更新器。
  - `AtomicReferenceFieldUpdater` - 原子更新引用类型里的字段。

> 这里不对 CAS、volatile、互斥同步做深入探讨。如果想了解更多细节，不妨参考：[Java 并发核心机制](https://github.com/dunwu/javacore/blob/master/docs/concurrent/Java并发核心机制.md)

## 2. 基本类型

这一类型的原子类是针对 Java 基本类型进行操作。

- `AtomicBoolean` - 布尔类型原子类
- `AtomicInteger` - 整型原子类
- `AtomicLong` - 长整型原子类

以上类都支持 CAS（[compare-and-swap](https://en.wikipedia.org/wiki/Compare-and-swap)）技术，此外，`AtomicInteger`、`AtomicLong` 还支持算术运算。

> :bulb: 提示：
>
> 虽然 Java 只提供了 `AtomicBoolean` 、`AtomicInteger`、`AtomicLong`，但是可以模拟其他基本类型的原子变量。要想模拟其他基本类型的原子变量，可以将 `short` 或 `byte` 等类型与 `int` 类型进行转换，以及使用 `Float.floatToIntBits` 、`Double.doubleToLongBits` 来转换浮点数。
>
> 由于 `AtomicBoolean`、`AtomicInteger`、`AtomicLong` 实现方式、使用方式都相近，所以本文仅针对 `AtomicInteger` 进行介绍。

### 2.1. **`AtomicInteger` 用法**

```java
public final int get() // 获取当前值
public final int getAndSet(int newValue) // 获取当前值，并设置新值
public final int getAndIncrement()// 获取当前值，并自增
public final int getAndDecrement() // 获取当前值，并自减
public final int getAndAdd(int delta) // 获取当前值，并加上预期值
boolean compareAndSet(int expect, int update) // 如果输入值（update）等于预期值，将该值设置为输入值
public final void lazySet(int newValue) // 最终设置为 newValue，使用 lazySet 设置之后可能导致其他线程在之后的一小段时间内还是可以读到旧的值。
```

`AtomicInteger` 使用示例：

```java
public class AtomicIntegerDemo {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        AtomicInteger count = new AtomicInteger(0);
        for (int i = 0; i < 1000; i++) {
            executorService.submit((Runnable) () -> {
                System.out.println(Thread.currentThread().getName() + " count=" + count.get());
                count.incrementAndGet();
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS);
        System.out.println("Final Count is : " + count.get());
    }
}
```

### 2.2. **`AtomicInteger` 实现**

阅读 `AtomicInteger` 源码，可以看到如下定义：

```java
private static final Unsafe unsafe = Unsafe.getUnsafe();
private static final long valueOffset;

static {
	try {
		valueOffset = unsafe.objectFieldOffset
			(AtomicInteger.class.getDeclaredField("value"));
	} catch (Exception ex) { throw new Error(ex); }
}

private volatile int value;
```

> 说明：
>
> - `value` - value 属性使用 `volatile` 修饰，使得对 value 的修改在并发环境下对所有线程可见。
> - `valueOffset` - value 属性的偏移量，通过这个偏移量可以快速定位到 value 字段，这个是实现 AtomicInteger 的关键。
> - `unsafe` - Unsafe 类型的属性，它为 `AtomicInteger` 提供了 CAS 操作。

## 3. 引用类型

Java 数据类型分为 **基本数据类型** 和 **引用数据类型** 两大类（不了解 Java 数据类型划分可以参考： [Java 基本数据类型](https://github.com/dunwu/javacore/blob/master/docs/basics/java-data-type.md) ）。

上一节中提到了针对基本数据类型的原子类，那么如果想针对引用类型做原子操作怎么办？Java 也提供了相关的原子类：

- `AtomicReference` - 引用类型原子类
- `AtomicMarkableReference` - 带有标记位的引用类型原子类
- `AtomicStampedReference` - 带有版本号的引用类型原子类

> `AtomicStampedReference` 类在引用类型原子类中，彻底地解决了 ABA 问题，其它的 CAS 能力与另外两个类相近，所以最具代表性。因此，本节只针对 `AtomicStampedReference` 进行说明。

示例：基于 `AtomicReference` 实现一个简单的自旋锁

```java
public class AtomicReferenceDemo2 {

    private static int ticket = 10;

    public static void main(String[] args) {
        threadSafeDemo();
    }

    private static void threadSafeDemo() {
        SpinLock lock = new SpinLock();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 5; i++) {
            executorService.execute(new MyThread(lock));
        }
        executorService.shutdown();
    }

    /**
     * 基于 {@link AtomicReference} 实现的简单自旋锁
     */
    static class SpinLock {

        private AtomicReference<Thread> atomicReference = new AtomicReference<>();

        public void lock() {
            Thread current = Thread.currentThread();
            while (!atomicReference.compareAndSet(null, current)) {}
        }

        public void unlock() {
            Thread current = Thread.currentThread();
            atomicReference.compareAndSet(current, null);
        }

    }

    /**
     * 利用自旋锁 {@link SpinLock} 并发处理数据
     */
    static class MyThread implements Runnable {

        private SpinLock lock;

        public MyThread(SpinLock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            while (ticket > 0) {
                lock.lock();
                if (ticket > 0) {
                    System.out.println(Thread.currentThread().getName() + " 卖出了第 " + ticket + " 张票");
                    ticket--;
                }
                lock.unlock();
            }
        }

    }

}
```

原子类的实现基于 CAS 机制，而 CAS 存在 ABA 问题（不了解 ABA 问题，可以参考：[Java 并发基础机制 - CAS 的问题](https://github.com/dunwu/javacore/blob/master/docs/concurrent/Java并发核心机制.md#cas-%E7%9A%84%E9%97%AE%E9%A2%98)）。正是为了解决 ABA 问题，才有了 `AtomicMarkableReference` 和 `AtomicStampedReference`。

`AtomicMarkableReference` 使用一个布尔值作为标记，修改时在 true / false 之间切换。这种策略不能根本上解决 ABA 问题，但是可以降低 ABA 发生的几率。常用于缓存或者状态描述这样的场景。

```java
public class AtomicMarkableReferenceDemo {

    private final static String INIT_TEXT = "abc";

    public static void main(String[] args) throws InterruptedException {

        final AtomicMarkableReference<String> amr = new AtomicMarkableReference<>(INIT_TEXT, false);

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(Math.abs((int) (Math.random() * 100)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    String name = Thread.currentThread().getName();
                    if (amr.compareAndSet(INIT_TEXT, name, amr.isMarked(), !amr.isMarked())) {
                        System.out.println(Thread.currentThread().getName() + " 修改了对象！");
                        System.out.println("新的对象为：" + amr.getReference());
                    }
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(3, TimeUnit.SECONDS);
    }

}
```

**`AtomicStampedReference` 使用一个整型值做为版本号，每次更新前先比较版本号，如果一致，才进行修改**。通过这种策略，可以根本上解决 ABA 问题。

```java
public class AtomicStampedReferenceDemo {

    private final static String INIT_REF = "pool-1-thread-3";

    private final static AtomicStampedReference<String> asr = new AtomicStampedReference<>(INIT_REF, 0);

    public static void main(String[] args) throws InterruptedException {

        System.out.println("初始对象为：" + asr.getReference());

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            executorService.execute(new MyThread());
        }

        executorService.shutdown();
        executorService.awaitTermination(3, TimeUnit.SECONDS);
    }

    static class MyThread implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(Math.abs((int) (Math.random() * 100)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final int stamp = asr.getStamp();
            if (asr.compareAndSet(INIT_REF, Thread.currentThread().getName(), stamp, stamp + 1)) {
                System.out.println(Thread.currentThread().getName() + " 修改了对象！");
                System.out.println("新的对象为：" + asr.getReference());
            }
        }

    }

}
```

## 4. 数组类型

Java 提供了以下针对数组的原子类：

- `AtomicIntegerArray` - 整形数组原子类
- `AtomicLongArray` - 长整型数组原子类
- `AtomicReferenceArray` - 引用类型数组原子类

已经有了针对基本类型和引用类型的原子类，为什么还要提供针对数组的原子类呢？

数组类型的原子类为 **数组元素** 提供了 `volatile` 类型的访问语义，这是普通数组所不具备的特性——**`volatile` 类型的数组仅在数组引用上具有 `volatile` 语义**。

示例：`AtomicIntegerArray` 使用示例（`AtomicLongArray` 、`AtomicReferenceArray` 使用方式也类似）

```java
public class AtomicIntegerArrayDemo {

    private static AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(10);

    public static void main(final String[] arguments) throws InterruptedException {

        System.out.println("Init Values: ");
        for (int i = 0; i < atomicIntegerArray.length(); i++) {
            atomicIntegerArray.set(i, i);
            System.out.print(atomicIntegerArray.get(i) + " ");
        }
        System.out.println();

        Thread t1 = new Thread(new Increment());
        Thread t2 = new Thread(new Compare());
        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Final Values: ");
        for (int i = 0; i < atomicIntegerArray.length(); i++) {
            System.out.print(atomicIntegerArray.get(i) + " ");
        }
        System.out.println();
    }

    static class Increment implements Runnable {

        @Override
        public void run() {

            for (int i = 0; i < atomicIntegerArray.length(); i++) {
                int value = atomicIntegerArray.incrementAndGet(i);
                System.out.println(Thread.currentThread().getName() + ", index = " + i + ", value = " + value);
            }
        }

    }

    static class Compare implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < atomicIntegerArray.length(); i++) {
                boolean swapped = atomicIntegerArray.compareAndSet(i, 2, 3);
                if (swapped) {
                    System.out.println(Thread.currentThread().getName() + " swapped, index = " + i + ", value = 3");
                }
            }
        }

    }

}
```

## 5. 属性更新器类型

更新器类支持基于反射机制的更新字段值的原子操作。

- `AtomicIntegerFieldUpdater` - 整型字段的原子更新器。
- `AtomicLongFieldUpdater` - 长整型字段的原子更新器。
- `AtomicReferenceFieldUpdater` - 原子更新引用类型里的字段。

这些类的使用有一定限制：

- 因为对象的属性修改类型原子类都是抽象类，所以每次使用都必须使用静态方法 `newUpdater()` 创建一个更新器，并且需要设置想要更新的类和属性。
- 字段必须是 `volatile` 类型的；
- 不能作用于静态变量（`static`）；
- 不能作用于常量（`final`）；

```java
public class AtomicReferenceFieldUpdaterDemo {

    static User user = new User("begin");

    static AtomicReferenceFieldUpdater<User, String> updater =
        AtomicReferenceFieldUpdater.newUpdater(User.class, String.class, "name");

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 5; i++) {
            executorService.execute(new MyThread());
        }
        executorService.shutdown();
    }

    static class MyThread implements Runnable {

        @Override
        public void run() {
            if (updater.compareAndSet(user, "begin", "end")) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " 已修改 name = " + user.getName());
            } else {
                System.out.println(Thread.currentThread().getName() + " 已被其他线程修改");
            }
        }

    }

    static class User {

        volatile String name;

        public User(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public User setName(String name) {
            this.name = name;
            return this;
        }

    }

}
```

## 6. 原子化的累加器

`DoubleAccumulator`、`DoubleAdder`、`LongAccumulator` 和 `LongAdder`，这四个类仅仅用来执行累加操作，相比原子化的基本数据类型，速度更快，但是不支持 `compareAndSet()` 方法。如果你仅仅需要累加操作，使用原子化的累加器性能会更好，代价就是会消耗更多的内存空间。

`LongAdder` 内部由一个 `base` 变量和一个 `cell[]` 数组组成。

- 当只有一个写线程，没有竞争的情况下，`LongAdder` 会直接使用 `base` 变量作为原子操作变量，通过 CAS 操作修改变量；
- 当有多个写线程竞争的情况下，除了占用 `base` 变量的一个写线程之外，其它各个线程会将修改的变量写入到自己的槽 `cell[]` 数组中。

我们可以发现，`LongAdder` 在操作后的返回值只是一个近似准确的数值，但是 `LongAdder` 最终返回的是一个准确的数值， 所以在一些对实时性要求比较高的场景下，`LongAdder` 并不能取代 `AtomicInteger` 或 `AtomicLong`。

## 7. 参考资料

- [《Java 并发编程实战》](https://book.douban.com/subject/10484692/)
- [《Java 并发编程的艺术》](https://book.douban.com/subject/26591326/)
- [JUC 中的原子类](http://www.itsoku.com/article/182)
- http://tutorials.jenkov.com/java-util-concurrent/atomicinteger.html
- http://tutorials.jenkov.com/java-util-concurrent/atomicintegerarray.html
- http://tutorials.jenkov.com/java-util-concurrent/atomicreference.html
- http://tutorials.jenkov.com/java-util-concurrent/atomicstampedreference.htm
