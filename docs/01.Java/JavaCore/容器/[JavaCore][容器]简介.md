---
title: Java 容器简介
date: 2019-12-29 21:49:58
order: 01
categories:
  - Java
  - JavaCore
  - 容器
tags:
  - Java
  - JavaCore
  - 容器
  - 泛型
  - Iterable
  - Iterator
  - Comparable
  - Comparator
  - Cloneable
  - fail-fast
permalink: /pages/2668216d/
---

# Java 容器简介

## 简介

Java 容器（Collections）是存储和管理对象的数据结构框架，位于 `java.util` 包中。容器框架提供了丰富的接口和实现类，包括 `List`、`Set`、`Queue`、`Map` 等，支持泛型、迭代器、比较器、并发安全等核心机制。深入理解容器框架是编写高质量 Java 程序的基础。

## 容器简介

### 数组与容器

Java 中常用的存储容器就是数组和容器，二者有以下区别：

- 存储大小是否固定
  - 数组的**长度固定**；
  - 容器的**长度可变**。
- 数据类型
  - **数组可以存储基本数据类型，也可以存储引用数据类型**；
  - **容器只能存储引用数据类型**，基本数据类型的变量要转换成对应的包装类才能放入容器类中。

> :bulb: 不了解什么是基本数据类型、引用数据类型、包装类这些概念，可以参考：[Java 基本数据类型](https://dunwu.github.io/waterdrop/pages/cba76603/)

### 容器框架

![](https://raw.githubusercontent.com/dunwu/images/master/cs/java/javacore/container/java-container-structure.png)

Java 容器框架主要分为 `Collection` 和 `Map` 两种。其中，`Collection` 又分为 `List`、`Set` 以及 `Queue`。

- `Collection` - 一个独立元素的序列，这些元素都服从一条或者多条规则。
  - `List` - 必须按照插入的顺序保存元素。常见 `List` 容器有：
    - `ArrayList` - `Object[]` 数组。
    - `LinkedList` - 双链表 (JDK1.6 之前为循环链表，JDK1.7 取消了循环）。
    - `Vector` - 通过 `synchronized` 修饰读写方法来保证并发安全。
    - `Vector` - `Object[]` 数组，通过 `synchronized` 修饰读写方法来保证并发安全。
  - `Set` - 不能有重复的元素。常见 `Set` 容器有：
    - `HashSet` - 无序，内部基于 `HashMap` 来实现的。
    - `LinkedHashSet` - 保证插入顺序，内部基于 `LinkedHashMap` 来实现的。
    - `TreeSet` - 保证自然序或用户指定的比较器顺序，内部基于红黑树实现。
  - `Queue` - 按照排队规则来确定对象产生的顺序。
    - `PriorityQueue` - 基于 `Object[]` 数组来实现小顶堆
    - `DelayQueue` - 延迟队列。
    - `ArrayQueue` - `ArrayDeque` 是 `Deque` 的顺序表实现。基于动态数组实现了栈和队列所需的所有操作。
    - `LinkedList` - `LinkedList` 是 `Deque` 的链表实现。
- `Map` - 一组成对的“键值对”对象，允许你使用键来查找值。常见的 Map 容器有：
  - `HashMap`：JDK1.8 之前 `HashMap` 由数组+链表组成的，数组是 `HashMap` 的主体，链表则是主要为了解决哈希冲突而存在的（“拉链法”解决冲突）。JDK1.8 以后在解决哈希冲突时有了较大的变化，当链表长度大于阈值（默认为 8）（将链表转换成红黑树前会判断，如果当前数组的长度小于 64，那么会选择先进行数组扩容，而不是转换为红黑树）时，将链表转化为红黑树，以减少搜索时间。
  - `LinkedHashMap`：`LinkedHashMap` 继承自 `HashMap`，所以它的底层仍然是基于拉链式散列结构即由数组和链表或红黑树组成。另外，`LinkedHashMap` 在上面结构的基础上，增加了一条双向链表，使得上面的结构可以保持键值对的插入顺序。同时通过对链表进行相应的操作，实现了访问顺序相关逻辑。
  - `Hashtable`：数组+链表组成的，数组是 `Hashtable` 的主体，链表则是主要为了解决哈希冲突而存在的。
  - `TreeMap`：红黑树（自平衡的排序二叉树）。

## 容器的基本机制

> Java 的容器具有一定的共性，它们或全部或部分依赖以下技术。所以，学习以下技术点，对于理解 Java 容器的特性和原理有很大的帮助。

### 泛型

Java 1.5 引入了泛型技术。

Java **容器通过泛型技术来保证其数据的类型安全**。什么是类型安全呢？

举例来说：如果有一个 `List<Object>` 容器，Java **编译器在编译时不会对原始类型进行类型安全检查**，却会对带参数的类型进行检查，通过使用 Object 作为类型，可以告知编译器该方法可以接受任何类型的对象，比如 String 或 Integer。

```java
List<Object> list = new ArrayList<Object>();
list.add("123");
list.add(123);
```

如果没有泛型技术，如示例中的代码那样，容器中就可能存储任意数据类型，这是很危险的行为。

```java
List<String> list = new ArrayList<String>();
list.add("123");
list.add(123);
```

> :bulb: 想深入了解 Java 泛型技术的用法和原理可以参考：[深入理解 Java 泛型](https://dunwu.github.io/waterdrop/pages/1c5e74da/)

### Iterable 和 Iterator

> Iterable 和 Iterator 目的在于遍历访问容器中的元素。

`Iterator` 接口定义：

```java
public interface Iterator<E> {

    boolean hasNext();

    E next();

    default void remove() {
        throw new UnsupportedOperationException("remove");
    }

    default void forEachRemaining(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        while (hasNext())
            action.accept(next());
    }
}
```

`Iterable` 接口定义：

```java
public interface Iterable<T> {

    Iterator<T> iterator();

    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            action.accept(t);
        }
    }

    default Spliterator<T> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }
}
```

`Collection` 接口扩展了 `Iterable` 接口。

迭代其实我们可以简单地理解为遍历，是一个标准化遍历各类容器里面的所有对象的接口。它是一个经典的设计模式——迭代器模式（Iterator）。

**迭代器模式** - **提供一种方法顺序访问一个聚合对象中各个元素，而又无须暴露该对象的内部表示**。

![](https://raw.githubusercontent.com/dunwu/images/master/cs/java/oop/design-patterns/iterator-pattern.png)

示例：迭代器遍历

```java
public class IteratorDemo {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

}
```

《阿里巴巴 Java 开发手册》的描述如下：

> **不要在 foreach 循环里进行元素的 `remove/add` 操作。remove 元素请使用 `Iterator` 方式，如果并发操作，需要对 `Iterator` 对象加锁。**

通过反编译你会发现 foreach 语法底层其实还是依赖 `Iterator` 。不过， `remove/add` 操作直接调用的是集合自己的方法，而不是 `Iterator` 的 `remove/add`方法

这就导致 `Iterator` 莫名其妙地发现自己有元素被 `remove/add` ，然后，它就会抛出一个 `ConcurrentModificationException` 来提示用户发生了并发修改异常。这就是单线程状态下产生的 **fail-fast 机制**。

> **fail-fast 机制**：多个线程对 fail-fast 集合进行修改的时候，可能会抛出`ConcurrentModificationException`。 即使是单线程下也有可能会出现这种情况，上面已经提到过。
>
> 相关阅读：[什么是 fail-fastopen in new window](https://www.cnblogs.com/54chensongxia/p/12470446.html) 。

Java8 开始，可以使用 `Collection#removeIf()`方法删除满足特定条件的元素，如：

```java
List<Integer> list = new ArrayList<>();
for (int i = 1; i <= 10; ++i) {
    list.add(i);
}
list.removeIf(filter -> filter % 2 == 0); /* 删除list中的所有偶数 */
System.out.println(list); /* [1, 3, 5, 7, 9] */
```

除了上面介绍的直接使用 `Iterator` 进行遍历操作之外，你还可以：

- 使用普通的 for 循环
- 使用 fail-safe 的集合类。`java.util`包下面的所有的集合类都是 fail-fast 的，而`java.util.concurrent`包下面的所有的类都是 fail-safe 的。
- ... ...

### Comparable 和 Comparator

`Comparable` 接口和 `Comparator` 接口一般用于实现容器中元素的比较及排序：

- `Comparable` 接口实际上是出自 `java.lang` 包 它有一个 `compareTo(Object obj) `方法用来排序
- `Comparator  ` 接口实际上是出自 `java.util` 包它有一个 `compare(Object obj1, Object obj2)` 方法用来排序

::: tabs#Comparable和Comparator接口定义

@tab `Comparable` 接口定义

`Comparable` 接口定义

```java
public interface Comparable<T> {
    public int compareTo(T o);
}
```

@tab `Comparator` 接口定义

`Comparator` 接口定义

```java
@FunctionalInterface
public interface Comparator<T> {

    int compare(T o1, T o2);

    boolean equals(Object obj);

    // 反转
    default Comparator<T> reversed() {
        return Collections.reverseOrder(this);
    }

    default Comparator<T> thenComparing(Comparator<? super T> other) {
        Objects.requireNonNull(other);
        return (Comparator<T> & Serializable) (c1, c2) -> {
            int res = compare(c1, c2);
            return (res != 0) ? res : other.compare(c1, c2);
        };
    }

    // thenComparingXXX 方法略

    // 静态方法略
}
```

:::

假设，有一个 `List` 容器，存储的是 `User` 类型对象。现在要根据 `User` 中的 `age` 属性进行排序。

User 定义如下：

```java
public class User {

    private String name;
    private int age;

    public User(String name, int age) {
        this.age = age;
        this.name = name;
    }
    // getter、setter 略
}
```

我们分别通过 `Comparable` 和 `Comparator` 来实现比较、排序，体会一下有何差异。

::: tabs#Comparable和Comparator使用示例

@tab `Comparable` 接口使用示例

`Comparable` 接口使用示例

```java
public class ComparableDemo {

    public static void main(String[] args) {
        User a = new User("A", 18);
        User b = new User("B", 17);
        User c = new User("C", 20);
        List<User> list = new ArrayList<>(Arrays.asList(a, b, c));
        Collections.sort(list);
        list.forEach(System.out::println);
    }
    // 输出：
    // User{age=17, name='B'}
    // User{age=18, name='A'}
    // User{age=20, name='C'}

    // 需要对被比较、排序的类进行改造，实现 Comparable 接口
    static class User implements Comparable<User> {

        private String name;
        private int age;

        public User(String name, int age) {
            this.age = age;
            this.name = name;
        }

        // getter、setter 略

        @Override
        public int compareTo(User o) {
            return this.age - o.age;
        }

        @Override
        public String toString() {
            return "User{" + "age=" + age + ", name='" + name + '\'' + '}';
        }
    }
}
```

从上例可以看出，使用 `Comparable` 接口，被排序对象类必须实现 `Comparable` 接口；并在类中定义 `compareTo` 方法的实现，即排序逻辑必须置于被排序对象类中。

@tab `Comparator` 接口使用示例

`Comparator` 接口使用示例

```java
public class ComparatorDemo {

    public static void main(String[] args) {
        User a = new User("A", 18);
        User b = new User("B", 17);
        User c = new User("C", 20);
        List<User> list = new ArrayList<>(Arrays.asList(a, b, c));
        Collections.sort(list, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.age - o2.age;
            }
        });
        list.forEach(System.out::println);
    }
    // 输出：
    // User{age=17, name='B'}
    // User{age=18, name='A'}
    // User{age=20, name='C'}

    static class User {

        private String name;
        private int age;

        public User(String name, int age) {
            this.age = age;
            this.name = name;
        }

        // getter、setter 略

        @Override
        public String toString() {
            return "User{" + "age=" + age + ", name='" + name + '\'' + '}';
        }
    }
}
```

从上例可以看出，使用 `Comparator` 接口和 `Comparable` 接口的不同点在于：被排序的对象类无需实现 `Comparator` 接口，排序逻辑置于被排序对象类的外部。

:::

### Cloneable

Java 中 一个类要实现 `clone` 功能 必须实现 `Cloneable` 接口，否则在调用 `clone()` 时会报 `CloneNotSupportedException` 异常。

Java 中所有类都默认继承 `java.lang.Object` 类，在 `java.lang.Object` 类中有一个方法 `clone()`，这个方法将返回 `Object` 对象的一个拷贝。`Object` 类里的 `clone()` 方法仅仅用于浅拷贝（拷贝基本成员属性，对于引用类型仅返回指向改地址的引用）。

如果 Java 类需要深拷贝，需要覆写 `clone()` 方法。

### fail-fast

#### fail-fast 的要点

Java 容器（如：ArrayList、HashMap、TreeSet 等待）的 javadoc 中常常提到类似的描述：

> 注意，迭代器的快速失败行为无法得到保证，因为一般来说，不可能对是否出现不同步并发修改做出任何硬性保证。快速失败（fail-fast）迭代器会尽最大努力抛出 `ConcurrentModificationException`。因此，为提高这类迭代器的正确性而编写一个依赖于此异常的程序是错误的做法：迭代器的快速失败行为应该仅用于检测 bug。

那么，我们不禁要问，什么是 fail-fast，为什么要有 fail-fast 机制？

**fail-fast 是 Java 容器的一种错误检测机制**。当多个线程对容器进行结构上的改变的操作时，就可能触发 fail-fast 机制。记住是有可能，而不是一定。

例如：假设存在两个线程（线程 1、线程 2），线程 1 通过 `Iterator` 在遍历容器 A 中的元素，在某个时候线程 2 修改了容器 A 的结构（是结构上面的修改，而不是简单的修改容器元素的内容），那么这个时候程序就会抛出 `ConcurrentModificationException` 异常，从而产生 fail-fast 机制。

**容器在迭代操作中改变元素个数（添加、删除元素）都可能会导致 fail-fast**。

示例：fail-fast 示例

```java
public class FailFastDemo {

    private static int MAX = 100;

    private static List<Integer> list = new ArrayList<>();

    public static void main(String[] args) {
        for (int i = 0; i < MAX; i++) {
            list.add(i);
        }
        new Thread(new MyThreadA()).start();
        new Thread(new MyThreadB()).start();
    }

    /** 迭代遍历容器所有元素 */
    static class MyThreadA implements Runnable {

        @Override
        public void run() {
            Iterator<Integer> iterator = list.iterator();
            while (iterator.hasNext()) {
                int i = iterator.next();
                System.out.println("MyThreadA 访问元素：" + i);
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /** 遍历删除指定范围内的所有偶数 */
    static class MyThreadB implements Runnable {

        @Override
        public void run() {
            int i = 0;
            while (i < MAX) {
                if (i % 2 == 0) {
                    System.out.println("MyThreadB 删除元素" + i);
                    list.remove(i);
                }
                i++;
            }
        }

    }

}
```

执行后，会抛出 `java.util.ConcurrentModificationException` 异常。

#### 解决 fail-fast

fail-fast 有两种解决方案：

- 在遍历过程中所有涉及到改变容器个数的地方全部加上 `synchronized` 或者直接使用 `Collections.synchronizedXXX` 容器，这样就可以解决。但是不推荐，因为增删造成的同步锁可能会阻塞遍历操作，影响吞吐。
- 使用并发容器，如：`CopyOnWriterArrayList`。

## 容器和线程安全

为了在并发环境下安全地使用容器，Java 提供了同步容器和并发容器。

> 同步容器和并发容器详情请参考：[Java 并发之容器](https://dunwu.github.io/waterdrop/pages/e987a43b/)

## 典型应用场景

### 场景一：使用泛型保证容器类型安全

泛型使编译器在编译期检查类型错误，避免运行时的 `ClassCastException`：

```java
// 使用泛型确保类型安全
List<String> names = new ArrayList<>();
names.add("Alice");
names.add("Bob");
// names.add(123); // 编译期报错

for (String name : names) {
    System.out.println(name.toUpperCase()); // 无需强制转换
}
```

### 场景二：使用 Iterator 安全删除元素

在遍历过程中删除元素，必须使用 `Iterator` 而非 foreach 循环：

```java
List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c", "d"));
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String element = it.next();
    if ("b".equals(element)) {
        it.remove(); // 安全删除
    }
}
// 结果：[a, c, d]
```

### 场景三：使用 Comparator 自定义排序

对复杂对象按多个字段排序：

```java
List<Employee> employees = getEmployees();
employees.sort(
    Comparator.comparing(Employee::getDepartment)
        .thenComparing(Comparator.comparingDouble(Employee::getSalary).reversed())
);
// 先按部门升序，同部门内按薪资降序
```

## 最佳实践

1. **优先使用接口类型声明容器**：如 `List<String> list = new ArrayList<>()`，方便后期切换实现。
2. **创建容器时指定初始容量**：如已知数据量，避免多次扩容的开销。
3. **不要在 foreach 循环中修改容器**：使用 `Iterator` 或 `removeIf` 方法。
4. **并发场景使用并发容器**：如 `ConcurrentHashMap`，而非手动包装的同步容器。
5. **使用 `Collections.unmodifiableXXX` 创建只读容器**：保护数据不被修改，适合 API 返回值。
6. **合理使用 `Arrays.asList`**：返回的 List 不支持 `add/remove`，如需可变列表应用 `new ArrayList<>(Arrays.asList(...))`。

## 常见问题

### Q1：`java.util` 和 `java.util.concurrent` 下的容器有什么区别？

`java.util` 下的容器（如 `ArrayList`、`HashMap`）都不是线程安全的；`java.util.concurrent` 下的容器（如 `ConcurrentHashMap`、`CopyOnWriteArrayList`）专门设计用于并发场景，性能优于简单的 `synchronized` 包装。

### Q2：容器中可以存储基本数据类型吗？

不可以直接存储。容器只能存储引用类型，基本数据类型需要自动装箱为对应的包装类（如 `int` → `Integer`）。自动装箱/拆箱会带来额外开销，在性能敏感场景下需注意。

### Q3：fail-fast 和 fail-safe 有什么区别？

- **fail-fast**：遍历过程中如果容器结构被修改，立即抛出 `ConcurrentModificationException`。`java.util` 包下的容器大多采用此机制。
- **fail-safe**：遍历的是容器的副本，不会抛出异常，但可能看不到最新修改。`java.util.concurrent` 包下的容器采用此机制。

## 参考资料

- [Java 编程思想（第 4 版）](https://item.jd.com/10058164.html)
- [《Effective Java》第 3 版](https://book.douban.com/subject/30412517/)
- [Java Collections 官方文档](https://docs.oracle.com/javase/8/docs/api/java/util/Collections.html)
- [由浅入深理解 java 集合（一）——集合框架 Collection、Map](https://www.jianshu.com/p/589d58033841)
- [由浅入深理解 java 集合（二）——集合 Set](https://www.jianshu.com/p/9081017a2d67)
- [Java 提高篇（三十）-----Iterator](https://www.cnblogs.com/chenssy/p/3821328.html)
- [Java 提高篇（三四）-----fail-fast 机制](https://blog.csdn.net/chenssy/article/details/38151189)
