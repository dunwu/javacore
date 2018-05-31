---
title: 容器之 Set
date: 2018/05/31
categories:
- javase
tags:
- javase
- collection
---

# 容器之 Set

<!-- TOC -->

- [HashSet](#hashset)
    - [HashSet 要点](#hashset-要点)
    - [HashSet 源码](#hashset-源码)
- [TreeSet](#treeset)
    - [TreeSet 要点](#treeset-要点)
    - [TreeSet 源码](#treeset-源码)
- [EnumSet](#enumset)
    - [EnumSet 要点](#enumset-要点)

<!-- /TOC -->


Set 集合与 Collection 集合基本相同，没有提供任何额外的方法。

实际上 Set 就是 Collection，只是行为略有不同：Set 集合不允许包含相同的元素，如果试图把两个相同的元素加入同一个 Set 集合中，则添加操作失败，add() 方法返回 false，且新元素不会被加入。

* HashSet：基于哈希实现，支持快速查找，但不支持有序性操作，例如根据一个范围查找元素的操作。并且失去了元素的插入顺序信息，也就是说使用 Iterator 遍历 HashSet 得到的结果是不确定的；

* TreeSet：基于红黑树实现，支持有序性操作，但是查找效率不如 HashSet，HashSet 查找时间复杂度为 O(1)，TreeSet 则为 O(logN)；

* LinkedHashSet：具有 HashSet 的查找效率，且内部使用链表维护元素的插入顺序。

## HashSet

### HashSet 要点

HashSet 不保证元素的迭代访问顺序与插入顺序相同；并且元素的顺序随着时间推移可能发生改变。

HashSet 允许 null 值的元素。

HashSet 的基本操作（添加，删除，包含和大小）提供了恒定的时间性能，假设散列函数在桶之间正确地分散元素。迭代此集合需要的时间与HashSet实例的大小（元素数量）加上支持HashMap实例的“容量”（桶的数量）的总和成正比。因此，如果迭代性能很重要，不要将初始容量设置得太高（或者负载因子太低）是非常重要的。

HashSet 不是并发安全的，如果在并发状态下对其做迭代操作，会抛出 ConcurrentModificationException 异常。

### HashSet 源码

```java
public class HashSet<E>
    extends AbstractSet<E>
    implements Set<E>, Cloneable, java.io.Serializable {

    // HashSet 的核心，通过维护一个 HashMap 实体来实现 HashSet 方法
    private transient HashMap<E,Object> map;

    // PRESENT 是用于关联 map 中当前操作元素的一个虚拟值
    private static final Object PRESENT = new Object();
}
```

HashSet 中维护了一个 HashMap 对象 map，它是 HashSet 的核心。实际上，HashSet 的重要方法，如 add、remove、iterator、clear、size 等都是围绕 map 实现的。所以，可以说，HashSet 的实现本质上依赖于 HashMap。

PRESENT 是用于关联 map 中当前操作元素的一个虚拟值。

## TreeSet

### TreeSet 要点

基于 TreeMap 的 NavigableSet 实现。这些元素使用它们的自然顺序或者在创建集合时提供的比较器进行排序，具体取决于使用哪个构造函数。

TreeSet 会对元素进行排序，排序规则是自然顺序或比较器（Comparator）中提供的顺序规则。

TreeSet 不是并发安全的。如果在并发环境下使用，需要在外部调用处保证同步。

### TreeSet 源码

```java
public class TreeSet<E> extends AbstractSet<E>
    implements NavigableSet<E>, Cloneable, java.io.Serializable
{
    // TreeSet 的核心，通过维护一个 NavigableMap 实体来实现 TreeSet 方法
    private transient NavigableMap<E,Object> m;

    // PRESENT 是用于关联 map 中当前操作元素的一个虚拟值
    private static final Object PRESENT = new Object();
```

## EnumSet

### EnumSet 要点

EnumSet 是用于枚举类型的 Set 实现。

EnumSet 不允许加入null元素。EnumSet 中的所有元素必须来自单个枚举类型，该集合类型在创建集合时显式或隐式指定。

EnumSet 没有构造方法，只能通过类中的 static 方法来创建 EnumSet 对象。

EnumSet 是有序的。以枚举值在 EnumSet 类中的定义顺序来决定集合元素的顺序。

EnumSet 不是并发安全的。

