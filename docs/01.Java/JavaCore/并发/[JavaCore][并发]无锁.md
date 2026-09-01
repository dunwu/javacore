---
title: Java 并发之无锁
date: 2019-12-25 22:19:09
categories:
  - Java
  - JavaCore
  - 并发
tags:
  - Java
  - JavaCore
  - 并发
  - CAS
  - 原子类
  - ThreadLocal
  - Immutability
  - Copy-on-Write
permalink: /pages/3d79d585/
---

# Java 并发之无锁

## 简介

无锁（Lock-Free）编程是解决并发安全问题的另一种思路，与互斥同步（加锁）相对。无锁方案通过 **CAS（Compare-And-Swap）硬件指令**、**不可变对象**、**ThreadLocal 线程本地存储**、**Copy-on-Write** 等技术手段，在不加锁的前提下保证线程安全，避免了线程阻塞和上下文切换的开销，在高并发场景下能显著提升性能。

并发安全需要保证几个基本特性：

- **可见性** - 是一个线程修改了某个共享变量，其状态能够立即被其他线程知晓，通常被解释为将线程本地状态反映到主内存上，`volatile` 就是负责保证可见性的。
- **有序性** - 是保证线程内串行语义，避免指令重排等。
- **原子性** - 简单说就是相关操作不会中途被其他线程干扰，一般通过互斥机制（加锁：`sychronized`、`Lock`）实现。

互斥同步是最常见的原子性保障手段。**互斥同步最主要的问题是线程阻塞和唤醒所带来的性能问题**。因此，互斥同步也被称为阻塞同步。互斥同步属于一种悲观的并发策略，总是认为只要不去做正确的同步措施，那就肯定会出现问题。无论共享数据是否真的会出现竞争，它都要进行加锁（这里讨论的是概念模型，实际上虚拟机会优化掉很大一部分不必要的加锁）、用户态核心态转换、维护锁计数器和检查是否有被阻塞的线程需要唤醒等操作。

解决并发安全问题，还可以采用无锁方案。无锁方案相对互斥锁方案，最大的好处就是性能。互斥锁方案为了保证互斥性，需要执行加锁、解锁操作，而加锁、解锁操作本身就消耗性能；同时拿不到锁的线程还会进入阻塞状态，进而触发线程切换，线程切换对性能的消耗也很大。 相比之下，无锁方案则完全没有加锁、解锁的性能消耗，同时还能保证互斥性。

Java 中的无锁技术有：

- CAS
- 原子类
- ThreadLocal
- Copy-on-Write
- 不变模式

## CAS

### CAS 的要点

**CAS（Compare and Swap），字面意思为比较并交换。**

CAS 涉及三个操作数：

- V：需要读写的内存位置
- A：进行比较的值
- B：拟写入的新值

**当且仅当 V 的值等于 A 时，才会通过原子方式用新值 B 来更新 A 的值，否则什么都不做**。

CAS 实际是乐观锁的一种实现方式，因此，**CAS 只适用于线程冲突较少的情况**。

### CAS 的应用

CAS 的典型应用场景是：

- 原子类
- 自旋锁

#### 原子类

> 原子类是 CAS 在 Java 中最典型的应用。

我们先来看一个常见的代码片段。

```Java
if(a==b) {
    a++;
}
```

如果 `a++` 执行前， a 的值被修改了怎么办？还能得到预期值吗？出现该问题的原因是在并发环境下，以上代码片段不是原子操作，随时可能被其他线程所篡改。

解决这种问题的最经典方式是应用原子类的 `incrementAndGet` 方法。

```Java
public class AtomicIntegerDemo {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        final AtomicInteger count = new AtomicInteger(0);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    count.incrementAndGet();
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(3, TimeUnit.SECONDS);
        System.out.println("Final Count is : " + count.get());
    }

}
```

J.U.C 包中提供了 `AtomicBoolean`、`AtomicInteger`、`AtomicLong` 分别针对 `Boolean`、`Integer`、`Long` 执行原子操作，操作和上面的示例大体相似，不做赘述。

#### 自旋锁

利用原子类（本质上是 CAS），可以实现自旋锁。

所谓自旋锁，是指线程反复检查锁变量是否可用，直到成功为止。由于线程在这一过程中保持执行，因此是一种忙等待。一旦获取了自旋锁，线程会一直保持该锁，直至显式释放自旋锁。

示例：非线程安全示例

```java
public class AtomicReferenceDemo {

    private static int ticket = 10;

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
            while (ticket > 0) {
                System.out.println(Thread.currentThread().getName() + " 卖出了第 " + ticket + " 张票");
                ticket--;
            }
        }

    }

}
```

输出结果：

```
pool-1-thread-2 卖出了第 10 张票
pool-1-thread-1 卖出了第 10 张票
pool-1-thread-3 卖出了第 10 张票
pool-1-thread-1 卖出了第 8 张票
pool-1-thread-2 卖出了第 9 张票
pool-1-thread-1 卖出了第 6 张票
pool-1-thread-3 卖出了第 7 张票
pool-1-thread-1 卖出了第 4 张票
pool-1-thread-2 卖出了第 5 张票
pool-1-thread-1 卖出了第 2 张票
pool-1-thread-3 卖出了第 3 张票
pool-1-thread-2 卖出了第 1 张票
```

很明显，出现了重复售票的情况。

【示例】使用自旋锁来保证线程安全

可以通过自旋锁这种非阻塞同步来保证线程安全，下面使用 `AtomicReference` 来实现一个自旋锁。

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

输出结果：

```
pool-1-thread-2 卖出了第 10 张票
pool-1-thread-1 卖出了第 9 张票
pool-1-thread-3 卖出了第 8 张票
pool-1-thread-2 卖出了第 7 张票
pool-1-thread-3 卖出了第 6 张票
pool-1-thread-1 卖出了第 5 张票
pool-1-thread-2 卖出了第 4 张票
pool-1-thread-1 卖出了第 3 张票
pool-1-thread-3 卖出了第 2 张票
pool-1-thread-1 卖出了第 1 张票
```

### CAS 的原理

**在 Java 中，主要利用 `Unsafe` 这个类实现 CAS**。

`Unsafe` 类位于 `sun.misc` 包下，是一个提供低级别、不安全操作的类。由于其强大的功能和潜在的危险性，它通常用于 JVM 内部或一些需要极高性能和底层访问的库中，而不推荐普通开发者在应用程序中使用。

`Unsafe` 类提供了 `compareAndSwapObject`、`compareAndSwapInt`、`compareAndSwapLong`方法来实现的对 `Object`、`int`、`long ` 类型的 CAS 操作：

```java
/**
 * 以原子方式更新对象字段的值。
 */
boolean compareAndSwapObject(Object o, long offset, Object expected, Object x);

/**
 * 以原子方式更新 int 类型的对象字段的值。
 */
boolean compareAndSwapInt(Object o, long offset, int expected, int x);

/**
 * 以原子方式更新 long 类型的对象字段的值。
 */
boolean compareAndSwapLong(Object o, long offset, long expected, long x);
```

`Unsafe` 类中的 CAS 方法是 `native` 方法。`native `关键字表明这些方法是用本地代码（通常是 C 或 C++）实现的，而不是用 Java 实现的。这些方法直接调用底层的、具有原子性的 CPU 指令来实现。

由于 CAS 操作可能会因为并发冲突而失败，因此通常会伴随着自旋，而所谓自旋，其实就是循环尝试。

`Unsafe#getAndAddInt` 源码：

```java
// 原子地获取并增加整数值
public final int getAndAddInt(Object o, long offset, int delta) {
    int v;
    do {
        // 以 volatile 方式获取对象 o 在内存偏移量 offset 处的整数值
        v = getIntVolatile(o, offset);
    } while (!compareAndSwapInt(o, offset, v, v + delta));
    // 返回旧值
    return v;
}
```

### CAS 的问题

一般情况下，**CAS 比锁性能更高**。因为 CAS 是一种非阻塞算法，所以其避免了线程阻塞和唤醒的等待时间。但是，事物总会有利有弊，CAS 也存在三大问题：

- **ABA 问题**
- **循环时间长开销大**
- **只能保证一个共享变量的原子性**

#### ABA 问题

如果一个变量初次读取的时候是 A 值，它的值被改成了 B，后来又被改回为 A，那 CAS 操作就会误认为它从来没有被改变过，这就是 **ABA 问题**。

ABA 问题的解决思路是在变量前面追加上**版本号或者时间戳**。J.U.C 包提供了一个带有标记的**原子引用类 `AtomicStampedReference` 来解决这个问题**，它可以通过控制变量值的版本来保证 CAS 的正确性。大部分情况下 ABA 问题不会影响程序并发的正确性，如果需要解决 ABA 问题，改用**传统的互斥同步可能会比原子类更高效**。

#### 循环时间长开销大

**自旋 CAS （不断尝试，直到成功为止）如果长时间不成功，会给 CPU 带来非常大的执行开销**。

如果 JVM 能支持处理器提供的 `pause` 指令那么效率会有一定的提升，`pause` 指令有两个作用：

- 它可以延迟流水线执行指令（de-pipeline）, 使 CPU 不会消耗过多的执行资源，延迟的时间取决于具体实现的版本，在一些处理器上延迟时间是零。
- 它可以避免在退出循环的时候因内存顺序冲突（memory order violation）而引起 CPU 流水线被清空（CPU pipeline flush），从而提高 CPU 的执行效率。

比较花费 CPU 资源，即使没有任何用也会做一些无用功。

#### 只能保证一个共享变量的原子性

当对一个共享变量执行操作时，我们可以使用循环 CAS 的方式来保证原子操作，但是对多个共享变量操作时，循环 CAS 就无法保证操作的原子性，这个时候就可以用锁。

或者有一个取巧的办法，就是把多个共享变量合并成一个共享变量来操作。比如有两个共享变量 `i ＝ 2, j = a`，合并一下 `ij=2a`，然后用 CAS 来操作 `ij`。从 Java 1.5 开始 JDK 提供了 `AtomicReference` 类来保证引用对象之间的原子性，你可以把多个变量放在一个对象里来进行 CAS 操作。

## 原子类

### 原子类简介

原子性是确保并发安全三大特性之一。为了兼顾原子性以及锁带来的性能问题，Java 引入了 CAS （主要体现在 `Unsafe` 类）来实现非阻塞同步（也叫乐观锁），CAS 底层基于 CPU 指令（硬件支持）支持，具有原子性。并基于 CAS ，提供了一套原子工具类。

原子类**比锁的粒度更细，更轻量级**，并且对于在多处理器系统上实现高性能的并发代码来说是非常关键的。原子变量将发生竞争的范围缩小到单个变量上。

原子类相当于一种泛化的 `volatile` 变量，能够**支持原子的、有条件的读/改/写操**作。

原子类可以分为 5 个类别，这 5 个类别提供的方法基本上是相似的：

- **基本数据类型**
  - `AtomicBoolean` - 布尔类型原子类
  - `AtomicInteger` - 整型原子类
  - `AtomicLong` - 长整型原子类
- **引用数据类型**
  - `AtomicReference` - 引用类型原子类
  - `AtomicMarkableReference` - 带有标记位的引用类型原子类
  - `AtomicStampedReference` - 带有版本号的引用类型原子类
- **数组数据类型**
  - `AtomicIntegerArray` - 整形数组原子类
  - `AtomicLongArray` - 长整型数组原子类
  - `AtomicReferenceArray` - 引用类型数组原子类
- **属性更新器类型**
  - `AtomicIntegerFieldUpdater` - 整型字段的原子更新器
  - `AtomicLongFieldUpdater` - 长整型字段的原子更新器
  - `AtomicReferenceFieldUpdater` - 原子更新引用类型里的字段
- 累加器
  - `DoubleAdder` - 浮点型原子累加器
  - `LongAdder` - 长整型原子累加器
  - `DoubleAccumulator` - 更复杂的浮点型原子累加器
  - `LongAccumulator` - 更复杂的长整型原子累加器

### 原子类之基本数据类型

**基本数据类型原子类针对 Java 基本类型提供原子操作**。

- `AtomicBoolean` - 布尔类型原子类
- `AtomicInteger` - 整型原子类
- `AtomicLong` - 长整型原子类

以上类都支持 CAS（[compare-and-swap](https://en.wikipedia.org/wiki/Compare-and-swap)）技术，此外，`AtomicInteger`、`AtomicLong` 还支持算术运算。

> :bulb: 提示：
>
> 虽然 Java 只提供了 `AtomicBoolean` 、`AtomicInteger`、`AtomicLong`，但是可以模拟其他基本类型的原子变量。要想模拟其他基本类型的原子变量，可以将 `short` 或 `byte` 等类型与 `int` 类型进行转换，以及使用 `Float.floatToIntBits` 、`Double.doubleToLongBits` 来转换浮点数。
>
> 由于 `AtomicBoolean`、`AtomicInteger`、`AtomicLong` 实现方式、使用方式都相近，所以本文仅针对 `AtomicInteger` 进行介绍。

#### **`AtomicInteger` 用法**

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

#### **`AtomicInteger` 实现**

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

### 原子类之引用数据类型

Java 数据类型分为 **基本数据类型** 和 **引用数据类型** 两大类（不了解 Java 数据类型划分可以参考： [Java 基本数据类型](https://dunwu.github.io/waterdrop/pages/cba76603/) ）。

上一节中提到了针对基本数据类型的原子类，那么如果想针对引用类型做原子操作怎么办？Java 也提供了相关的原子类：

- `AtomicReference` - 引用类型原子类
- `AtomicMarkableReference` - 带有标记位的引用类型原子类
- `AtomicStampedReference` - 带有版本号的引用类型原子类

> `AtomicStampedReference` 类在引用类型原子类中，彻底地解决了 ABA 问题，其它的 CAS 能力与另外两个类相近，所以最具代表性。因此，本节只针对 `AtomicStampedReference` 进行说明。

::: tabs#原子类之引用类型示例

@tab `AtomicReference` 使用示例

【示例】基于 `AtomicReference` 实现一个简单的自旋锁

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

@tab `AtomicMarkableReference` 使用示例

【示例】`AtomicMarkableReference` 使用示例（解决 ABA 问题）

原子类的实现基于 CAS 机制，而 CAS 存在 ABA 问题（不了解 ABA 问题，可以参考：[Java 并发之内存模型](https://dunwu.github.io/waterdrop/pages/e98ae9d2)）。正是为了解决 ABA 问题，才有了 `AtomicMarkableReference` 和 `AtomicStampedReference`。

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

@tab `AtomicStampedReference` 使用示例

【示例】`AtomicStampedReference` 使用示例

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

:::

### 原子类之数组数据类型

Java 提供了以下针对数组的原子类：

- `AtomicIntegerArray` - 整形数组原子类
- `AtomicLongArray` - 长整型数组原子类
- `AtomicReferenceArray` - 引用类型数组原子类

已经有了针对基本类型和引用类型的原子类，为什么还要提供针对数组的原子类呢？

**数组类型的原子类为数组元素提供了 `volatile` 类型的访问语义**，这是普通数组所不具备的特性——**`volatile` 类型的数组仅在数组引用上具有 `volatile` 语义**。

【示例】`AtomicIntegerArray` 使用示例（`AtomicLongArray` 、`AtomicReferenceArray` 使用方式也类似）

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

### 原子类之属性更新器

**属性更新器支持基于反射机制的更新字段值的原子操作**。

- `AtomicIntegerFieldUpdater` - 整型字段的原子更新器。
- `AtomicLongFieldUpdater` - 长整型字段的原子更新器。
- `AtomicReferenceFieldUpdater` - 原子更新引用类型里的字段。

这些类的使用有一定限制：

- 因为对象的属性修改类型原子类都是抽象类，所以每次使用都必须使用静态方法 `newUpdater()` 创建一个更新器，并且需要设置想要更新的类和属性。
- 字段必须是 `volatile` 类型的；
- 不能作用于静态变量（`static`）；
- 不能作用于常量（`final`）；

【示例】`AtomicReferenceFieldUpdater` 使用示例

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

### 原子类之累加器

`DoubleAccumulator`、`DoubleAdder`、`LongAccumulator` 和 `LongAdder`，这四个类仅仅用来执行累加操作，相比原子化的基本数据类型，速度更快，但是不支持 `compareAndSet()` 方法。如果你仅仅需要累加操作，使用原子化的累加器性能会更好，代价就是会消耗更多的内存空间。

`LongAdder` 内部由一个 `base` 变量和一个 `cell[]` 数组组成。

- 当只有一个写线程，没有竞争的情况下，`LongAdder` 会直接使用 `base` 变量作为原子操作变量，通过 CAS 操作修改变量；
- 当有多个写线程竞争的情况下，除了占用 `base` 变量的一个写线程之外，其它各个线程会将修改的变量写入到自己的槽 `cell[]` 数组中。

我们可以发现，`LongAdder` 在操作后的返回值只是一个近似准确的数值，但是 `LongAdder` 最终返回的是一个准确的数值， 所以在一些对实时性要求比较高的场景下，`LongAdder` 并不能取代 `AtomicInteger` 或 `AtomicLong`。

## ThreadLocal

在多线程环境下，共享变量存在并发安全问题。换个思路，如果变量非共享，而是各个线程独享，就不会有并发安全问题。这种思想有个术语叫**线程封闭**，其本质上就是避免共享。没有共享，自然也就没有并发安全问题。在 Java 中，`ThreadLocal` 正是根据这个思路而设计的。

**`ThreadLocal` 为每个线程都创建了一个本地副本**，这个副本只能被当前线程访问，其他线程无法访问，那么自然是线程安全的。

### ThreadLocal 的应用

`ThreadLocal` 的方法：

```java
public class ThreadLocal<T> {
    public T get() {}
    public void set(T value) {}
    public void remove() {}
    public static <S> ThreadLocal<S> withInitial(Supplier<? extends S> supplier) {}
}
```

> 说明：
>
> - `get` - 用于获取 `ThreadLocal` 在当前线程中保存的变量副本。
> - `set` - 用于设置当前线程中变量的副本。
> - `remove` - 用于删除当前线程中变量的副本。如果此线程局部变量随后被当前线程读取，则其值将通过调用其 `initialValue` 方法重新初始化，除非其值由中间线程中的当前线程设置。 这可能会导致当前线程中多次调用 `initialValue` 方法。
> - `initialValue` - 为 ThreadLocal 设置默认的 `get` 初始值，需要重写 `initialValue` 方法 。

`ThreadLocal` 常用于防止对可变的单例（Singleton）变量或全局变量进行共享。典型应用场景有：管理数据库连接、Session 管理等。

::: tabs#ThreadLocal 应用示例

@tab 数据库连接

【示例】数据库连接

```java
private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>() {
    @Override
    public Connection initialValue() {
        return DriverManager.getConnection(DB_URL);
    }
};

public static Connection getConnection() {
    return connectionHolder.get();
}
```

@tab Session 管理

【示例】Session 管理

```java
private static final ThreadLocal<Session> sessionHolder = new ThreadLocal<>();

public static Session getSession() {
    Session session = (Session) sessionHolder.get();
    try {
        if (session == null) {
            session = createSession();
            sessionHolder.set(session);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return session;
}
```

@tab 线程安全的 `SimpleDateFormat`

【示例】线程安全的 `SimpleDateFormat`

SimpleDateFormat 不是线程安全的，如果要保证并发安全，可以使用 ThreadLocal 来解决。

```java
public class SafeDateFormat {

    //定义 ThreadLocal 变量
    static final ThreadLocal<DateFormat>
        tl = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    static DateFormat get() {
        return tl.get();
    }

    public static void main(String[] args) {
        //不同线程执行下面代码
        //返回的 df 是不同的
        DateFormat df = SafeDateFormat.get();
    }

}
```

@tab 完整使用 `ThreadLocal` 示例

【示例】完整使用 `ThreadLocal` 示例

```java
public class ThreadLocalDemo {

    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new MyThread());
        }
        executorService.shutdown();
    }

    static class MyThread implements Runnable {

        @Override
        public void run() {
            int count = threadLocal.get();
            for (int i = 0; i < 10; i++) {
                try {
                    count++;
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            threadLocal.set(count);
            threadLocal.remove();
            System.out.println(Thread.currentThread().getName() + " : " + count);
        }

    }

}
```

:::

### ThreadLocal 的原理

#### 存储结构

**`Thread` 类中维护着 2 个 `ThreadLocal.ThreadLocalMap` 类型的成员** `threadLocals` 和 inheritableThreadLocals 。这 2 个成员就是用来存储当前线程独占的变量副本。

`ThreadLocalMap` 是 `ThreadLocal` 的内部类，它维护着一个 `Entry` 数组，**`Entry` 继承了 `WeakReference`** ，所以是弱引用。 `Entry` 用于保存键值对，其中：

- `key` 是 `ThreadLocal` 对象
- `value` 是传递进来的对象（变量副本）

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2024/09/55b36b7961db43a0bedecd97cc01a945.png)

`ThreadLocal` 关键源码如下：

```java
public class Thread implements Runnable {
    // ...
    ThreadLocal.ThreadLocalMap threadLocals = null;
    // ...
    ThreadLocal.ThreadLocalMap inheritableThreadLocals = null;
}

static class ThreadLocalMap {
    // ...
    static class Entry extends WeakReference<ThreadLocal<?>> {
        /** The value associated with this ThreadLocal. */
        Object value;

        Entry(ThreadLocal<?> k, Object v) {
            super(k);
            value = v;
        }
    }
    // ...
}
```

#### 如何解决 Hash 冲突

`ThreadLocalMap` 虽然是类似 `Map` 结构的数据结构，但它并没有实现 `Map` 接口。它不支持 `Map` 接口中的 `next` 方法，这意味着 `ThreadLocalMap` 中解决 Hash 冲突的方式并非 **拉链表** 方式。

实际上，**`ThreadLocalMap` 采用线性探测的方式来解决 Hash 冲突**。所谓线性探测，就是根据初始 key 的 hashcode 值确定元素在 table 数组中的位置，如果发现这个位置上已经被其他的 key 值占用，则利用固定的算法寻找一定步长的下个位置，依次判断，直至找到能够存放的位置。

#### 内存泄漏问题

`ThreadLocal` 仅仅是一个代理工具类，内部并不持有任何与线程相关的数据，所有和线程相关的数据都存储在 `Thread` 里面，这样的设计容易理解。

当然还有一个更加深层次的原因，那就是**不容易产生内存泄露**。如果 `ThreadLocal` 和实际实现反其道而行之：将 `Thread` 的引用维护在一个 `Map` 中，就会出现这种情况——只要 `ThreadLocal` 对象存在，那么 Map 中的 Thread 对象就永远不会被回收。而 `ThreadLocal` 的生命周期往往都比线程要长，所以这种设计方案很容易导致内存泄露。而 Java 的实现中 `Thread` 持有 `ThreadLocalMap`，而且 `ThreadLocalMap` 里对 `ThreadLocal` 的引用还是弱引用（`WeakReference`），所以只要 `Thread` 对象可以被回收，那么 `ThreadLocalMap` 就能被回收。Java 的这种实现方案虽然看上去复杂一些，但是更加安全。

`ThreadLocalMap` 的 `Entry` 继承了 `WeakReference`，所以它的 **key （`ThreadLocal` 对象）是弱引用，而 value （变量副本）是强引用**。如果 `ThreadLocal` 对象没有外部强引用来引用它，那么 `ThreadLocal` 对象会在下次 GC 时被回收。此时，`Entry` 中的 key 已经被回收，但是 value 由于是强引用不会被垃圾收集器回收。如果创建 `ThreadLocal` 的线程一直持续运行，那么 value 就会一直得不到回收，从而导致**内存泄露**。

那么如何避免内存泄漏呢？方法就是：**使用 `ThreadLocal` 的 `set` 方法后，在 `try {} finally {} ` 中显示的调用 `remove` 方法** 。

```java
ExecutorService es;
ThreadLocal tl;
es.execute(() -> {
    //ThreadLocal 增加变量
    tl.set(obj);
    try {
        // 省略业务逻辑代码
    } finally {
        //手动清理 ThreadLocal
        tl.remove();
    }
});
```

### ThreadLocal 的误区

> 示例摘自：[极客时间教程 - Java 业务开发常见错误 100 例](https://time.geekbang.org/column/intro/100047701)

ThreadLocal 适用于变量在线程间隔离，而在方法或类间共享的场景。

前文提到，ThreadLocal 是线程隔离的，那么是不是使用 ThreadLocal 就一定高枕无忧呢？

#### ThreadLocal 错误案例

使用 Spring Boot 创建一个 Web 应用程序，使用 ThreadLocal 存放一个 Integer 的值，来暂且代表需要在线程中保存的用户信息，这个值初始是 null。

```java
    private ThreadLocal<Integer> currentUser = ThreadLocal.withInitial(() -> null);

    @GetMapping("wrong")
    public Map<String, String> wrong(@RequestParam("id") Integer userId) {
        //设置用户信息之前先查询一次 ThreadLocal 中的用户信息
        String before = Thread.currentThread().getName() + ":" + currentUser.get();
        //设置用户信息到 ThreadLocal
        currentUser.set(userId);
        //设置用户信息之后再查询一次 ThreadLocal 中的用户信息
        String after = Thread.currentThread().getName() + ":" + currentUser.get();
        //汇总输出两次查询结果
        Map<String, String> result = new HashMap<>();
        result.put("before", before);
        result.put("after", after);
        return result;
    }
```

【预期】从代码逻辑来看，我们预期第一次获取的值始终应该是 null。

【实际】

为了方便复现，将 Tomcat 工作线程设为 1：

```
server.tomcat.max-threads=1
```

当访问 id = 1 时，符合预期

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2020/07/bef40273f82c4db59a5418f3442da4aa.png)

当访问 id = 2 时，before 的应答不是 null，而是 1，不符合预期。

【分析】实际情况和预期存在偏差。Spring Boot 程序运行在 Tomcat 中，执行程序的线程是 Tomcat 的工作线程，而 Tomcat 的工作线程是基于线程池的。**线程池会重用固定的几个线程，一旦线程重用，那么很可能首次从**
**ThreadLocal 获取的值是之前其他用户的请求遗留的值。这时，ThreadLocal 中的用户信息就是其他用户的信息**。

**并不能认为没有显式开启多线程就不会有线程安全问题**。使用类似 ThreadLocal 工具来存放一些数据时，需要特别注意在代码运行完后，显式地去清空设置的数据。

#### ThreadLocal 错误案例修正

```java
    @GetMapping("right")
    public Map<String, String> right(@RequestParam("id") Integer userId) {
        String before = Thread.currentThread().getName() + ":" + currentUser.get();
        currentUser.set(userId);
        try {
            String after = Thread.currentThread().getName() + ":" + currentUser.get();
            Map<String, String> result = new HashMap<>();
            result.put("before", before);
            result.put("after", after);
            return result;
        } finally {
            //在 finally 代码块中删除 ThreadLocal 中的数据，确保数据不串
            currentUser.remove();
        }
    }
```

### InheritableThreadLocal

通过 `ThreadLocal` 创建的线程变量，其子线程是无法继承的。也就是说你在线程中通过 `ThreadLocal` 创建了线程变量 V，而后该线程创建了子线程，你在子线程中是无法通过 `ThreadLocal` 来访问父线程的线程变量 V 的。

如果你需要子线程继承父线程的线程变量，那该怎么办呢？其实很简单，Java 提供了 `InheritableThreadLocal` 来支持这种特性，`InheritableThreadLocal` 是 `ThreadLocal` 子类，所以用法和 `ThreadLocal` 相同。与 `ThreadLocal` 不同的是，`InheritableThreadLocal` 允许一个线程以及该线程创建的所有子线程都可以访问它保存的数据。

不过，完全不建议你在线程池中使用 `InheritableThreadLocal`，不仅仅是因为它具有 `ThreadLocal` 相同的缺点——可能导致内存泄露，更重要的原因是：线程池中线程的创建是动态的，很容易导致继承关系错乱，如果你的业务逻辑依赖 `InheritableThreadLocal`，那么很可能导致业务逻辑计算错误，而这个错误往往比内存泄露更要命。

> 原理参考：[Java 多线程：InheritableThreadLocal 实现原理](https://blog.csdn.net/ni357103403/article/details/51970748)

## Immutability 模式

解决并发问题，其实最简单的办法就是让共享变量只有读操作，而没有写操作。这个办法如此重要，以至于被上升到了一种解决并发问题的设计模式：**不变性（Immutability）模式**。所谓**不变性，是指：一旦创建，状态不再变化**。换句话说，就是变量一旦被赋值，就不允许修改了（没有写操作）；没有修改操作，也就是保持了不变性。

### 快速实现具备不可变性的类

**将一个类所有的属性都设置成 final 的，并且只允许存在只读方法，那么这个类基本上就具备不可变性了**。更严格的做法是**这个类本身也是 final 的**，也就是不允许继承。因为子类可以覆盖父类的方法，有可能改变不可变性，所以推荐你在实际工作中，使用这种更严格的做法。

在 Java 中，经常用到的 `String` 和 `Long`、`Integer`、`Double` 等基础类型的包装类都具备不可变性，这些对象的线程安全性都是靠不可变性来保证的。如果你仔细翻看这些类的声明、属性和方法，你会发现它们都严格遵守不可变类的三点要求：**类和属性都是 final 的，所有方法均是只读的**。

`String` 这个类虽然有替换操作，但实际仍是只读的。阅读 `String` 源码可以发现：`String` 这个类以及它的属性 `value[]` 都是 `final` 的；而 `replace()` 方法的实现，就的确没有修改 `value[]`，而是将替换后的字符串作为返回值返回了。

```java
public final class String {
  private final char value[];
  // 字符替换
  String replace(char oldChar,
      char newChar) {
    //无需替换，直接返回 this
    if (oldChar == newChar){
      return this;
    }

    int len = value.length;
    int i = -1;
    /* avoid getfield opcode */
    char[] val = value;
    //定位到需要替换的字符位置
    while (++i < len) {
      if (val[i] == oldChar) {
        break;
      }
    }
    //未找到 oldChar，无需替换
    if (i >= len) {
      return this;
    }
    //创建一个 buf[]，这是关键
    //用来保存替换后的字符串
    char buf[] = new char[len];
    for (int j = 0; j < i; j++) {
      buf[j] = val[j];
    }
    while (i < len) {
      char c = val[i];
      buf[i] = (c == oldChar) ?
        newChar : c;
      i++;
    }
    //创建一个新的字符串返回
    //原字符串不会发生任何变化
    return new String(buf, true);
  }
}
```

通过分析 `String` 的实现，你可能已经发现了，如果具备不可变性的类，需要提供类似修改的功能，具体该怎么操作呢？做法很简单，那就是**创建一个新的不可变对象**，这是与可变对象的一个重要区别，可变对象往往是修改自己的属性。

### 使用 Immutability 模式的注意事项

在使用 Immutability 模式的时候，需要注意以下两点：

1. 对象的所有属性都是 final 的，并不能保证不可变性；
2. 不可变对象也需要正确发布。

在 Java 语言中，final 修饰的属性一旦被赋值，就不可以再修改，但是如果属性的类型是普通对象，那么这个普通对象的属性是可以被修改的。例如下面的代码中，Bar 的属性 foo 虽然是 final 的，依然可以通过 setAge() 方法来设置 foo 的属性 age。所以，**在使用 Immutability 模式的时候一定要确认保持不变性的边界在哪里，是否要求属性对象也具备不可变性**。

```java
class Foo{
  int age=0;
  int name="abc";
}
final class Bar {
  final Foo foo;
  void setAge(int a){
    foo.age=a;
  }
}
```

不可变对象虽然是线程安全的，但是并不意味着引用这些不可变对象的对象就是线程安全的。例如在下面的代码中，Foo 具备不可变性，线程安全，但是类 Bar 并不是线程安全的，类 Bar 中持有对 Foo 的引用 foo，对 foo 这个引用的修改在多线程中并不能保证可见性和原子性。

```java
//Foo 线程安全
final class Foo{
  final int age=0;
  final int name="abc";
}
//Bar 线程不安全
class Bar {
  Foo foo;
  void setFoo(Foo f){
    this.foo=f;
  }
}
```

如果你的程序仅仅需要 foo 保持可见性，无需保证原子性，那么可以将 foo 声明为 volatile 变量，这样就能保证可见性。如果你的程序需要保证原子性，那么可以通过原子类来实现。下面的示例代码是合理库存的原子化实现，你应该很熟悉了，其中就是用原子类解决了不可变对象引用的原子性问题。

```java
public class SafeWM {

    class WMRange {
        final int upper;
        final int lower;
        WMRange(int upper, int lower) {
            //省略构造函数实现
        }
    }

    final AtomicReference<WMRange> rf = new AtomicReference<>(new WMRange(0, 0));

    // 设置库存上限
    void setUpper(int v) {
        while (true) {
            WMRange or = rf.get();
            // 检查参数合法性
            if (v < or.lower) {
                throw new IllegalArgumentException();
            }
            WMRange nr = new WMRange(v, or.lower);
            if (rf.compareAndSet(or, nr)) {
                return;
            }
        }
    }
}
```

## Copy-on-Write 模式

所谓 Copy-on-Write，经常被缩写为 CoW，顾名思义就是**写时复制**。

Java 支持 `CopyOnWriteArrayList` 和 `CopyOnWriteArraySet` 两种并发容器，其设计思想就是 CoW；通过 Copy-on-Write 这两个容器实现的读操作是无锁的，由于无锁，所以将读操作的性能发挥到了极致。

CoW 是一项非常通用的技术方案，在很多领域都有着广泛的应用。不过，它也有缺点的，那就是消耗内存，每次修改都需要复制一个新的副本出来。

## 典型应用场景

### 场景一：高并发计数器（AtomicLong/LongAdder）

统计网站访问量，使用原子类避免加锁：

```java
// 简单场景使用 AtomicLong
AtomicLong counter = new AtomicLong(0);
counter.incrementAndGet();

// 高并发场景使用 LongAdder（分段累加，减少竞争）
LongAdder adder = new LongAdder();
adder.increment();
long total = adder.sum(); // 获取当前总和
```

`LongAdder` 在高竞争场景下性能比 `AtomicLong` 高一个数量级，因为它将值分散到多个 Cell 中，最后汇总。

### 场景二：线程上下文传递（ThreadLocal）

保存用户登录信息，整个请求链路中任何地方都能获取，且线程间互不干扰：

```java
public class UserContext {
    private static final ThreadLocal<UserInfo> HOLDER = new ThreadLocal<>();

    public static void set(UserInfo user) { HOLDER.set(user); }
    public static UserInfo get() { return HOLDER.get(); }
    public static void clear() { HOLDER.remove(); }
}

// 拦截器中设置
UserContext.set(userService.getCurrentUser());
try {
    chain.doFilter(request, response);
} finally {
    UserContext.clear(); // 必须清理，防止线程池复用时污染
}
```

### 场景三：无锁队列/原子引用更新（CAS）

实现无锁的单链表入队操作：

```java
AtomicReference<Node<E>> tail = new AtomicReference<>(head);

public void enqueue(E item) {
    Node<E> newNode = new Node<>(item);
    while (true) {
        Node<E> currentTail = tail.get();
        newNode.next = currentTail;
        if (tail.compareAndSet(currentTail, newNode)) {
            return; // CAS 成功，入队完成
        }
        // CAS 失败，重试
    }
}
```

## 最佳实践

1. **高并发计数优先用 LongAdder** - 在统计、监控、限流等场景，`LongAdder` 比 `AtomicLong` 性能更高，但牺牲了实时性（`sum()` 是近似值）
2. **ThreadLocal 必须在 finally 中清理** - 线程池场景下线程被复用，不清理会导致数据污染和内存泄漏（ThreadLocalMap 的弱引用 Key 已解决部分问题，但仍建议主动清理）
3. **CAS 自旋要设置重试上限** - 高竞争场景下 CAS 自旋会大量消耗 CPU，应设置重试次数或退避策略，否则不如直接用锁
4. **优先使用 JDK 提供的原子类** - `AtomicReference`、`AtomicInteger`、`AtomicBoolean` 等已经封装了 CAS 操作，避免直接使用 `Unsafe` 类
5. **不可变对象用 final 修饰** - 确保对象的引用和状态都不会改变，无需任何同步机制即可保证线程安全

## 常见问题

**Q1：CAS 的 ABA 问题如何解决？**

CAS 只比较值是否相等，如果值从 A 变成 B 再变回 A，CAS 会认为没有变化。解决方案是使用 `AtomicStampedReference`，它同时比较值和版本号（时间戳），每次修改都会递增版本号。

**Q2：ThreadLocal 为什么会导致内存泄漏？**

`ThreadLocalMap` 的 key 是 `ThreadLocal` 的弱引用，如果 `ThreadLocal` 对象被回收，key 变为 null，但 value 是强引用不会被回收，导致 value 一直占用内存。解决方案：使用完毕后务必调用 `remove()` 方法。

**Q3：CAS 自旋和互斥锁如何选择？**

竞争不激烈时，CAS 自旋比互斥锁性能好（避免了线程切换开销）；竞争激烈时，CAS 自旋会大量消耗 CPU，此时应改用互斥锁。JDK 中的 `ConcurrentHashMap`、`AtomicInteger` 等都自动结合了两种策略。

## 参考资料

- [《Java 并发编程实战》](https://book.douban.com/subject/10484692/)
- [《Java 并发编程的艺术》](https://book.douban.com/subject/26591326/)
- [《深入理解 Java 虚拟机》](https://book.douban.com/subject/34907497/)
- [极客时间教程 - Java 业务开发常见错误 100 例](https://time.geekbang.org/column/intro/100047701)
- [Non-blocking Algorithms](http://tutorials.jenkov.com/java-concurrency/non-blocking-algorithms.html)
- [Java CAS 完全解读](https://www.jianshu.com/p/473e14d5ab2d)
- [Java 中 CAS 详解](https://blog.csdn.net/ls5718/article/details/52563959)
- [JUC 中的原子类](http://www.itsoku.com/article/182)
- [ThreadLocal 终极篇](https://juejin.im/post/5a64a581f265da3e3b7aa02d)
