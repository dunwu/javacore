---
title: Java 容器之 Set
date: 2018/06/28
categories:
- javacore
tags:
- java
- javacore
- container
---

# Java 容器之 Set

<!-- TOC depthFrom:2 depthTo:2 -->

- [Set 架构](#set-架构)
- [Set 接口](#set-接口)
- [SortedSet 接口](#sortedset-接口)
- [NavigableSet 接口](#navigableset-接口)
- [AbstractSet 抽象类](#abstractset-抽象类)
- [HashSet 类](#hashset-类)
- [TreeSet 类](#treeset-类)
- [LinkedHashSet 类](#linkedhashset-类)
- [EnumSet 类](#enumset-类)
- [资料](#资料)

<!-- /TOC -->

## Set 架构

<div align="center">
<img src="http://dunwu.test.upcdn.net/images/java/container/Set-diagrams.png" width="400" />
</div>

- Set 继承了 Collection 的接口。实际上 Set 就是 Collection，只是行为略有不同：Set 集合不允许有重复元素。
- SortedSet 继承了 Set 的接口。SortedSet 中的内容是排序的唯一值，排序的方法是通过比较器(Comparator)。
- NavigableSet 继承了 SortedSet 的接口。相比于 NavigableSet 有一系列的导航方法；如"获取大于/等于某值的元素"、“获取小于/等于某值的元素”等等。
- AbstractSet 是一个抽象类，它继承于 AbstractCollection，AbstractCollection 实现了 Set 中的绝大部分函数，为 Set 的实现类提供了便利。
- HashSet 类依赖于 HashMap，它实际上是通过 HashMap 实现的。HashSet 中的元素是无序的。
- TreeSet 类依赖于 TreeMap，它实际上是通过 TreeMap 实现的。TreeSet 中的元素是有序的。
- LinkedHashSet 类具有 HashSet 的查找效率，且内部使用链表维护元素的插入顺序。
- EnumSet 中所有元素都必须是指定枚举类型的枚举值。

## Set 接口

Set 接口定义如下：

```java
public interface Set<E> extends Collection<E> {}
```

Set 继承了 Collection 的接口。实际上，Set 就是 Collection，二者提供的方法完全相同。

## SortedSet 接口

SortedSet 接口定义如下：

```java
public interface SortedSet<E> extends Set<E> {}
```

SortedSet 接口新扩展的方法：

- comparator - 返回 Comparator
- subSet - 返回指定区间的子集
- headSet - 返回小于指定元素的子集
- tailSet - 返回大于指定元素的子集
- first - 返回第一个元素
- last - 返回最后一个元素
- spliterator

## NavigableSet 接口

NavigableSet 接口定义如下：

```java
public interface NavigableSet<E> extends SortedSet<E> {}
```

NavigableSet 接口新扩展的方法：

- lower - 返回小于指定值的元素中最接近的元素
- higher - 返回大于指定值的元素中最接近的元素
- floor - 返回小于或等于指定值的元素中最接近的元素
- ceiling - 返回大于或等于指定值的元素中最接近的元素
- pollFirst - 检索并移除第一个（最小的）元素
- pollLast - 检索并移除最后一个（最大的）元素
- descendingSet - 返回反序排列的 Set
- descendingIterator - 返回反序排列的 Set 的迭代器
- subSet - 返回指定区间的子集
- headSet - 返回小于指定元素的子集
- tailSet - 返回大于指定元素的子集

## AbstractSet 抽象类

AbstractSet 抽象类定义如下：

```
public abstract class AbstractSet<E> extends AbstractCollection<E> implements Set<E> {}
```

AbstractSet 类提供 Set 接口的骨干实现，以最大限度地减少实现 Set 接口所需的工作。

事实上，主要的实现已经在 AbstractCollection 中完成。

## HashSet 类

HashSet 类定义如下：

```java
public class HashSet<E>
    extends AbstractSet<E>
    implements Set<E>, Cloneable, java.io.Serializable {}
```

### HashSet 要点

1.  HashSet 类通过继承 AbstractSet 实现了 Set 接口中的骨干方法。
2.  HashSet 实现了 Cloneable，所以支持克隆。
3.  HashSet 实现了 Serializable，所以支持序列化。
4.  HashSet 中存储的元素是无序的。
5.  HashSet 允许 null 值的元素。
6.  HashSet 不是线程安全的。

### HashSet 原理

```java
// HashSet 的核心，通过维护一个 HashMap 实体来实现 HashSet 方法
private transient HashMap<E,Object> map;

// PRESENT 是用于关联 map 中当前操作元素的一个虚拟值
private static final Object PRESENT = new Object();
}
```

**HashSet 是基于 HashMap 实现的。**

HashSet 中维护了一个 HashMap 对象 map，HashSet 的重要方法，如 add、remove、iterator、clear、size 等都是围绕 map 实现的。

PRESENT 是用于关联 map 中当前操作元素的一个虚拟值。

HashSet 类中通过定义 `writeObject()` 和 `readObject()` 方法确定了其序列化和反序列化的机制。

## TreeSet 类

TreeSet 类定义如下：

```java
public class TreeSet<E> extends AbstractSet<E>
    implements NavigableSet<E>, Cloneable, java.io.Serializable {}
```

### TreeSet 要点

1.  TreeSet 类通过继承 AbstractSet 实现了 NavigableSet 接口中的骨干方法。
2.  TreeSet 实现了 Cloneable，所以支持克隆。
3.  TreeSet 实现了 Serializable，所以支持序列化。
4.  TreeSet 中存储的元素是有序的。排序规则是自然顺序或比较器（Comparator）中提供的顺序规则。
5.  TreeSet 不是线程安全的。

### TreeSet 源码

```java
// TreeSet 的核心，通过维护一个 NavigableMap 实体来实现 TreeSet 方法
private transient NavigableMap<E,Object> m;

// PRESENT 是用于关联 map 中当前操作元素的一个虚拟值
private static final Object PRESENT = new Object();
```

**TreeSet 是基于 TreeMap 实现的。**

TreeSet 中维护了一个 NavigableMap 对象 map（实际上是一个 TreeMap 实例），TreeSet 的重要方法，如 add、remove、iterator、clear、size 等都是围绕 map 实现的。

PRESENT 是用于关联 map 中当前操作元素的一个虚拟值。TreeSet 中的元素都被当成 TreeMap 的 key 存储，而 value 都填的是 PRESENT。

## LinkedHashSet 类

LinkedHashSet 类定义如下：

```java
public class LinkedHashSet<E>
    extends HashSet<E>
    implements Set<E>, Cloneable, java.io.Serializable {}
```

### LinkedHashSet 要点

1.  LinkedHashSet 类通过继承 HashSet 实现了 Set 接口中的骨干方法。
2.  LinkedHashSet 实现了 Cloneable，所以支持克隆。
3.  LinkedHashSet 实现了 Serializable，所以支持序列化。
4.  LinkedHashSet 中存储的元素是按照插入顺序保存的。
5.  LinkedHashSet 不是线程安全的。

### LinkedHashSet 原理

LinkedHashSet 有三个构造方法，无一例外，都是调用父类 HashSet 的构造方法。

```java
public LinkedHashSet(int initialCapacity, float loadFactor) {
    super(initialCapacity, loadFactor, true);
}
public LinkedHashSet(int initialCapacity) {
    super(initialCapacity, .75f, true);
}
public LinkedHashSet() {
    super(16, .75f, true);
}
```

需要强调的是：**LinkedHashSet 构造方法实际上调用的是父类 HashSet 的非 public 构造方法。**

```java
HashSet(int initialCapacity, float loadFactor, boolean dummy) {
    map = new LinkedHashMap<>(initialCapacity, loadFactor);
}
```

不同于 HashSet public 构造方法中初始化的 HashMap 实例，这个构造方法中，初始化了 LinkedHashMap 实例。

也就是说，实际上，LinkedHashSet 维护了一个双链表。由双链表的特性可以知道，它是按照元素的插入顺序保存的。所以，这就是 LinkedHashSet 中存储的元素是按照插入顺序保存的原理。

## EnumSet 类

EnumSet 类定义如下：

```java
public abstract class EnumSet<E extends Enum<E>> extends AbstractSet<E>
    implements Cloneable, java.io.Serializable {}
```

### EnumSet 要点

1.  EnumSet 类继承了 AbstractSet，所以有 Set 接口中的骨干方法。
2.  EnumSet 实现了 Cloneable，所以支持克隆。
3.  EnumSet 实现了 Serializable，所以支持序列化。
4.  EnumSet 通过 `<E extends Enum<E>>` 限定了存储元素必须是枚举值。
5.  EnumSet 没有构造方法，只能通过类中的 static 方法来创建 EnumSet 对象。
6.  EnumSet 是有序的。以枚举值在 EnumSet 类中的定义顺序来决定集合元素的顺序。
7.  EnumSet 不是线程安全的。

## 资料

- [Java 编程思想（Thinking in java）](https://item.jd.com/10058164.html)
