---
title: Java 容器之 Set
date: 2019-12-29 21:49:58
order: 04
categories:
  - Java
  - JavaCore
  - 容器
tags:
  - Java
  - JavaCore
  - 容器
  - Set
  - HashSet
  - TreeSet
  - LinkedHashSet
permalink: /pages/29303397/
---

# Java 容器之 Set

## 简介

Set 是 Java 容器框架中用于存储不重复元素的集合。它继承自 `Collection` 接口，通过内部机制保证元素的唯一性。Java 提供了多种 Set 实现：`HashSet`（无序、高性能）、`LinkedHashSet`（保持插入顺序）、`TreeSet`（有序、红黑树）和 `EnumSet`（枚举专用），各自适用于不同的场景。

## Set 简介

![](https://raw.githubusercontent.com/dunwu/images/master/cs/java/javacore/container/Set-diagrams.png)

Set 家族成员简介：

- `Set` 继承了 `Collection` 的接口。实际上 `Set` 就是 `Collection`，只是行为略有不同：`Set` 集合不允许有重复元素。
- `SortedSet` 继承了 `Set` 的接口。`SortedSet` 中的内容是排序的唯一值，排序的方法是通过比较器(Comparator)。
- `NavigableSet` 继承了 `SortedSet` 的接口。它提供了丰富的查找方法：如"获取大于/等于某值的元素"、“获取小于/等于某值的元素”等等。
- `AbstractSet` 是一个抽象类，它继承于 `AbstractCollection`，`AbstractCollection` 实现了 Set 中的绝大部分方法，为实现 `Set` 的实例类提供了便利。
- `HashSet` 类依赖于 `HashMap`，它实际上是通过 `HashMap` 实现的。`HashSet` 中的元素是无序的、散列的。
- `TreeSet` 类依赖于 `TreeMap`，它实际上是通过 `TreeMap` 实现的。`TreeSet` 中的元素是有序的，它是按自然排序或者用户指定比较器排序的 Set。
- `LinkedHashSet` 是按插入顺序排序的 Set。
- `EnumSet` 是只能存放 Emum 枚举类型的 Set。

### Set 接口

`Set` 继承了 `Collection` 的接口。实际上，`Set` 就是 `Collection`，二者提供的方法完全相同。

`Set` 接口定义如下：

```java
public interface Set<E> extends Collection<E> {}
```

### SortedSet 接口

继承了 `Set` 的接口。`SortedSet` 中的内容是排序的唯一值，排序的方法是通过比较器(Comparator)。

`SortedSet` 接口定义如下：

```java
public interface SortedSet<E> extends Set<E> {}
```

`SortedSet` 接口新扩展的方法：

- `comparator` - 返回 Comparator
- `subSet` - 返回指定区间的子集
- `headSet` - 返回小于指定元素的子集
- `tailSet` - 返回大于指定元素的子集
- `first` - 返回第一个元素
- `last` - 返回最后一个元素
- spliterator

### NavigableSet 接口

`NavigableSet` 继承了 `SortedSet`。它提供了丰富的查找方法。

`NavigableSet` 接口定义如下：

```java
public interface NavigableSet<E> extends SortedSet<E> {}
```

`NavigableSet` 接口新扩展的方法：

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

### AbstractSet 抽象类

`AbstractSet` 类提供 `Set` 接口的核心实现，以最大限度地减少实现 `Set` 接口所需的工作。

`AbstractSet` 抽象类定义如下：

```java
public abstract class AbstractSet<E> extends AbstractCollection<E> implements Set<E> {}
```

事实上，主要的实现已经在 `AbstractCollection` 中完成。

## HashSet 类

`HashSet` 类依赖于 `HashMap`，它实际上是通过 `HashMap` 实现的。`HashSet` 中的元素是无序的、散列的。

`HashSet` 类定义如下：

```java
public class HashSet<E>
    extends AbstractSet<E>
    implements Set<E>, Cloneable, java.io.Serializable {}
```

### HashSet 要点

- `HashSet` 通过继承 `AbstractSet` 实现了 `Set` 接口中的骨干方法。
- `HashSet` 实现了 `Cloneable`，所以支持克隆。
- `HashSet` 实现了 `Serializable`，所以支持序列化。
- `HashSet` 中存储的元素是无序的。
- `HashSet` 允许 null 值的元素。
- `HashSet` 不是线程安全的。

### HashSet 原理

**`HashSet` 是基于 `HashMap` 实现的。**

```java
// HashSet 的核心，通过维护一个 HashMap 实体来实现 HashSet 方法
private transient HashMap<E,Object> map;

// PRESENT 是用于关联 map 中当前操作元素的一个虚拟值
private static final Object PRESENT = new Object();
}
```

- `HashSet` 中维护了一个 `HashMap` 对象 map，`HashSet` 的重要方法，如 `add`、`remove`、`iterator`、`clear`、`size` 等都是围绕 map 实现的。
  - `HashSet` 类中通过定义 `writeObject()` 和 `readObject()` 方法确定了其序列化和反序列化的机制。
- PRESENT 是用于关联 map 中当前操作元素的一个虚拟值。

## TreeSet 类

`TreeSet` 类依赖于 `TreeMap`，它实际上是通过 `TreeMap` 实现的。`TreeSet` 中的元素是有序的，它是按自然排序或者用户指定比较器排序的 Set。

`TreeSet` 类定义如下：

```java
public class TreeSet<E> extends AbstractSet<E>
    implements NavigableSet<E>, Cloneable, java.io.Serializable {}
```

### TreeSet 要点

- `TreeSet` 通过继承 `AbstractSet` 实现了 `NavigableSet` 接口中的骨干方法。
- `TreeSet` 实现了 `Cloneable`，所以支持克隆。
- `TreeSet` 实现了 `Serializable`，所以支持序列化。
- `TreeSet` 中存储的元素是有序的。排序规则是自然顺序或比较器（`Comparator`）中提供的顺序规则。
- `TreeSet` 不是线程安全的。

### TreeSet 源码

**TreeSet 是基于 TreeMap 实现的。**

```java
// TreeSet 的核心，通过维护一个 NavigableMap 实体来实现 TreeSet 方法
private transient NavigableMap<E,Object> m;

// PRESENT 是用于关联 map 中当前操作元素的一个虚拟值
private static final Object PRESENT = new Object();
```

- `TreeSet` 中维护了一个 `NavigableMap` 对象 map（实际上是一个 TreeMap 实例），`TreeSet` 的重要方法，如 `add`、`remove`、`iterator`、`clear`、`size` 等都是围绕 map 实现的。
- `PRESENT` 是用于关联 `map` 中当前操作元素的一个虚拟值。`TreeSet` 中的元素都被当成 `TreeMap` 的 key 存储，而 value 都填的是 `PRESENT`。

## LinkedHashSet 类

`LinkedHashSet` 是按插入顺序排序的 Set。

`LinkedHashSet` 类定义如下：

```java
public class LinkedHashSet<E>
    extends HashSet<E>
    implements Set<E>, Cloneable, java.io.Serializable {}
```

### LinkedHashSet 要点

- `LinkedHashSet` 通过继承 `HashSet` 实现了 `Set` 接口中的骨干方法。
- `LinkedHashSet` 实现了 `Cloneable`，所以支持克隆。
- `LinkedHashSet` 实现了 `Serializable`，所以支持序列化。
- `LinkedHashSet` 中存储的元素是按照插入顺序保存的。
- `LinkedHashSet` 不是线程安全的。

### LinkedHashSet 原理

`LinkedHashSet` 有三个构造方法，无一例外，都是调用父类 `HashSet` 的构造方法。

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

不同于 `HashSet` `public` 构造方法中初始化的 `HashMap` 实例，这个构造方法中，初始化了 `LinkedHashMap` 实例。

也就是说，实际上，`LinkedHashSet` 维护了一个双链表。由双链表的特性可以知道，它是按照元素的插入顺序保存的。所以，这就是 `LinkedHashSet` 中存储的元素是按照插入顺序保存的原理。

## EnumSet 类

`EnumSet` 类定义如下：

```java
public abstract class EnumSet<E extends Enum<E>> extends AbstractSet<E>
    implements Cloneable, java.io.Serializable {}
```

### EnumSet 要点

- `EnumSet` 继承了 `AbstractSet`，所以有 `Set` 接口中的骨干方法。
- `EnumSet` 实现了 `Cloneable`，所以支持克隆。
- `EnumSet` 实现了 `Serializable`，所以支持序列化。
- `EnumSet` 通过 `<E extends Enum<E>>` 限定了存储元素必须是枚举值。
- `EnumSet` 没有构造方法，只能通过类中的 `static` 方法来创建 `EnumSet` 对象。
- `EnumSet` 是有序的。以枚举值在 `EnumSet` 类中的定义顺序来决定集合元素的顺序。
- `EnumSet` 不是线程安全的。

## HashSet vs. LinkedHashSet vs. TreeSet

| 特性 | HashSet | LinkedHashSet | TreeSet |
| --- | --- | --- | --- |
| 底层结构 | 哈希表（HashMap） | 链表+哈希表 | 红黑树（TreeMap） |
| 元素顺序 | 无序 | 插入顺序 | 自然序/比较器序 |
| null 元素 | 允许 1 个 | 允许 1 个 | 不允许 |
| 时间复杂度 | O(1) | O(1) | O(log n) |
| 线程安全 | 不安全 | 不安全 | 不安全 |
| 适用场景 | 快速去重 | 去重且保持顺序 | 排序场景 |

## 典型应用场景

### 场景一：数据去重

快速去除列表中的重复元素：

```java
List<String> names = Arrays.asList("Alice", "Bob", "Alice", "Charlie", "Bob");
Set<String> uniqueNames = new HashSet<>(names);
// 结果：[Alice, Bob, Charlie]
```

### 场景二：集合运算（交集、并集、差集）

利用 Set 的数学集合特性进行交并差运算：

```java
Set<Integer> set1 = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
Set<Integer> set2 = new HashSet<>(Arrays.asList(4, 5, 6, 7, 8));

// 交集
Set<Integer> intersection = new HashSet<>(set1);
intersection.retainAll(set2); // [4, 5]

// 并集
Set<Integer> union = new HashSet<>(set1);
union.addAll(set2); // [1, 2, 3, 4, 5, 6, 7, 8]

// 差集
Set<Integer> difference = new HashSet<>(set1);
difference.removeAll(set2); // [1, 2, 3]
```

### 场景三：使用 TreeSet 实现范围查询

利用 `NavigableSet` 的丰富方法进行范围查询：

```java
TreeSet<Integer> scores = new TreeSet<>(Arrays.asList(55, 72, 88, 91, 63, 45, 78));

// 查找大于等于 80 分的学生
NavigableSet<Integer> highScores = scores.tailSet(80, true); // [88, 91]

// 查找分数在 60-80 之间的
NavigableSet<Integer> midScores = scores.subSet(60, true, 80, false); // [63, 72, 78]
```

## 最佳实践

1. **默认选择 `HashSet`**：如果没有特殊需求，`HashSet` 是最佳选择，O(1) 的时间复杂度最优。
2. **需要保持插入顺序时用 `LinkedHashSet`**：适合需要按插入顺序输出的场景，如配置项管理。
3. **需要排序时用 `TreeSet`**：当元素需要自然序或自定义排序时使用，注意时间复杂度为 O(log n)。
4. **枚举类型用 `EnumSet`**：性能远优于 `HashSet`，底层用位向量实现。
5. **注意自定义对象的 `hashCode` 和 `equals`**：使用 `HashSet` 时，必须正确覆写这两个方法，否则无法正确去重。
6. **并发环境使用 `ConcurrentHashMap.newKeySet()`**：比 `Collections.synchronizedSet` 性能更好。

## 常见问题

### Q1：HashSet 如何保证元素唯一性？

`HashSet` 基于 `HashMap` 实现。添加元素时，先计算 `hashCode` 确定存储位置，再用 `equals` 比较是否已存在。如果 `hashCode` 和 `equals` 判断相同，则不会重复添加。这就是为什么自定义对象必须正确覆写这两个方法。

### Q2：TreeSet 可以存储 null 吗？

不可以。`TreeSet` 基于 `TreeMap`（红黑树），在插入时会调用 `compareTo` 或 `Comparator` 进行比较，`null` 会导致 `NullPointerException`。

### Q3：HashSet、LinkedHashSet、TreeSet 如何选择？

- 只需去重，不关心顺序 → `HashSet`
- 需要保持插入顺序 → `LinkedHashSet`
- 需要自然序或自定义排序 → `TreeSet`
- 存储枚举类型 → `EnumSet`

## 参考资料

- [Java 编程思想（Thinking in java）](https://item.jd.com/10058164.html)
- [《Effective Java》第 3 版](https://book.douban.com/subject/30412517/)
- [Java Set 官方文档](https://docs.oracle.com/javase/8/docs/api/java/util/Set.html)
