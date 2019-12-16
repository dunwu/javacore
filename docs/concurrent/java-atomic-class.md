# 原子变量类

> 📓 本文已归档到：「[javacore](https://github.com/dunwu/javacore)」
>
> 本文内容基于 JDK1.8。

<!-- TOC depthFrom:2 depthTo:3 -->

- [原子更新基本类型](#原子更新基本类型)
- [原子更新数组](#原子更新数组)
- [原子更新引用类型](#原子更新引用类型)
- [原子更新字段类](#原子更新字段类)
- [资料](#资料)

<!-- /TOC -->

原子变量比锁的粒度更细，量级更轻，并且对于在多处理器系统上实现高性能的并发代码来说是非常关键的。

原子变量类相当于一种泛化的 volatile 变量，能够支持原子的和有条件的读-改-写操作。

原子类在内部使用现代 CPU 支持的 CAS 指令来实现同步。这些指令通常比锁更快。

## 原子更新基本类型

- AtomicBoolean - 原子更新布尔类型。
- AtomicInteger - 原子更新整型。
- AtomicLong - 原子更新长整型。

示例：

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

## 原子更新数组

- AtomicIntegerArray - 原子更新整型数组里的元素。
- AtomicLongArray - 原子更新长整型数组里的元素。
- AtomicReferenceArray - 原子更新引用类型数组的元素。
- AtomicBooleanArray - 原子更新布尔类型数组的元素。

示例：

```java
public class AtomicIntegerArrayDemo {

    private static AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(10);

    public static void main(final String[] arguments) throws InterruptedException {

        for (int i = 0; i < atomicIntegerArray.length(); i++) {
            atomicIntegerArray.set(i, i);
        }

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
    }

    static class Increment implements Runnable {

        public void run() {

            for (int i = 0; i < atomicIntegerArray.length(); i++) {
                int add = atomicIntegerArray.incrementAndGet(i);
                System.out.println(Thread.currentThread().getName() + ", index " + i + ", value: " + add);

            }
        }
    }

    static class Compare implements Runnable {

        public void run() {

            for (int i = 0; i < atomicIntegerArray.length(); i++) {
                boolean swapped = atomicIntegerArray.compareAndSet(i, 2, 3);

                if (swapped) {
                    System.out.println(Thread.currentThread().getName() + ", index " + i + ", value: 3");
                }
            }
        }
    }
}
```

## 原子更新引用类型

- AtomicReference - 原子更新引用类型。
- AtomicReferenceFieldUpdater - 原子更新引用类型里的字段。
- AtomicMarkableReference - 原子更新带有标记位的引用类型。可以原子更新一个布尔类型的标记位和应用类型。

```java
public class AtomicReferenceDemo {

    private static String message;
    private static Person person;
    private static AtomicReference<String> aRmessage;
    private static AtomicReference<Person> aRperson;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new MyRun1());
        Thread t2 = new Thread(new MyRun2());
        message = "hello";
        person = new Person("Phillip", 23);
        aRmessage = new AtomicReference<String>(message);
        aRperson = new AtomicReference<Person>(person);
        System.out.println("Message is: " + message
            + "\nPerson is " + person.toString());
        System.out.println("Atomic Reference of Message is: " + aRmessage.get()
            + "\nAtomic Reference of Person is " + aRperson.get().toString());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("\nNow Message is: " + message
            + "\nPerson is " + person.toString());
        System.out.println("Atomic Reference of Message is: " + aRmessage.get()
            + "\nAtomic Reference of Person is " + aRperson.get().toString());
    }

    static class MyRun1 implements Runnable {

        public void run() {
            aRmessage.compareAndSet(message, "Thread 1");
            message = message.concat("-Thread 1!");
            person.setAge(person.getAge() + 1);
            person.setName("Thread 1");
            aRperson.getAndSet(new Person("Thread 1", 1));
            System.out.println("\n" + Thread.currentThread().getName() + " Values "
                + message + " - " + person.toString());
            System.out.println("\n" + Thread.currentThread().getName() + " Atomic References "
                + message + " - " + person.toString());
        }
    }

    static class MyRun2 implements Runnable {

        public void run() {
            message = message.concat("-Thread 2");
            person.setAge(person.getAge() + 2);
            person.setName("Thread 2");
            aRmessage.lazySet("Thread 2");
            aRperson.set(new Person("Thread 2", 2));
            System.out.println("\n" + Thread.currentThread().getName() + " Values: "
                + message + " - " + person.toString());
            System.out.println("\n" + Thread.currentThread().getName() + " Atomic References: "
                + aRmessage.get() + " - " + aRperson.get().toString());
        }
    }

    static class Person {

        private String name;
        private int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        int getAge() {
            return age;
        }

        void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "[name " + this.name + ", age " + this.age + "]";
        }
    }
}
```

## 原子更新字段类

- AtomicIntegerFieldUpdater - 原子更新整型的字段的更新器。
- AtomicLongFieldUpdater - 原子更新长整型字段的更新器。
- AtomicStampedReference - 原子更新带有版本号的引用类型。该类将整型数值与引用关联起来，可用于原子的更新数据和数据的版本号，可以解决使用 CAS 进行原子更新时可能出现的 ABA 问题。

```java
public class AtomicStampedReferenceDemo {

    private final static String INIT_REF = "abc";

    public static void main(String[] args) throws InterruptedException {

        AtomicStampedReference<String> asr = new AtomicStampedReference<>(INIT_REF, 0);
        System.out.println("初始对象为：" + asr.getReference());
        final int stamp = asr.getStamp();

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                try {
                    Thread.sleep(Math.abs((int) (Math.random() * 100)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (asr.compareAndSet(INIT_REF, Thread.currentThread().getName(), stamp, stamp + 1)) {
                    System.out.println(Thread.currentThread().getName() + " 修改了对象！");
                    System.out.println("新的对象为：" + asr.getReference());
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(60, TimeUnit.SECONDS);
    }
}
```

## 资料

- [Java并发编程实战](https://item.jd.com/10922250.html)
- [Java并发编程的艺术](https://item.jd.com/11740734.html)
- http://tutorials.jenkov.com/java-util-concurrent/atomicinteger.html
- http://tutorials.jenkov.com/java-util-concurrent/atomicintegerarray.html
- http://tutorials.jenkov.com/java-util-concurrent/atomicreference.html
- http://tutorials.jenkov.com/java-util-concurrent/atomicstampedreference.html
