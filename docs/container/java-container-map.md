# Java 容器之 Map

> **📦 本文以及示例源码已归档在 [javacore](https://github.com/dunwu/javacore/)**

<!-- TOC depthFrom:2 depthTo:3 -->

- [1. Map 简介](#1-map-简介)
  - [1.1. Map 架构](#11-map-架构)
  - [1.2. Map 接口](#12-map-接口)
  - [1.3. Map.Entry 接口](#13-mapentry-接口)
  - [1.4. AbstractMap 抽象类](#14-abstractmap-抽象类)
  - [1.5. SortedMap 接口](#15-sortedmap-接口)
  - [1.6. NavigableMap 接口](#16-navigablemap-接口)
  - [1.7. Dictionary 抽象类](#17-dictionary-抽象类)
- [2. HashMap 类](#2-hashmap-类)
  - [2.1. HashMap 要点](#21-hashmap-要点)
  - [2.2. HashMap 原理](#22-hashmap-原理)
- [3. LinkedHashMap 类](#3-linkedhashmap-类)
  - [3.1. LinkedHashMap 要点](#31-linkedhashmap-要点)
  - [3.2. LinkedHashMap 要点](#32-linkedhashmap-要点)
- [4. TreeMap 类](#4-treemap-类)
  - [4.1. TreeMap 要点](#41-treemap-要点)
  - [4.2. TreeMap 原理](#42-treemap-原理)
  - [4.3. remove 方法](#43-remove-方法)
  - [4.4. TreeMap 示例](#44-treemap-示例)
- [5. WeakHashMap](#5-weakhashmap)
- [6. 总结](#6-总结)
  - [6.1. Map 简介](#61-map-简介)
  - [6.2. HashMap](#62-hashmap)
  - [6.3. 其他 Map](#63-其他-map)
- [7. 参考资料](#7-参考资料)

<!-- /TOC -->

## 1. Map 简介

### 1.1. Map 架构

<div align="center">
<img src="https://raw.githubusercontent.com/dunwu/images/dev/cs/java/javacore/container/Map-diagrams.png" />
</div>

Map 家族主要成员功能如下：

- `Map` 是 Map 容器家族的祖先，Map 是一个用于保存键值对(key-value)的接口。**Map 中不能包含重复的键；每个键最多只能映射到一个值。**
- `AbstractMap` 继承了 `Map` 的抽象类，它实现了 `Map` 中的核心 API。其它 `Map` 的实现类可以通过继承 `AbstractMap` 来减少重复编码。
- `SortedMap` 继承了 `Map` 的接口。`SortedMap` 中的内容是排序的键值对，排序的方法是通过实现比较器(`Comparator`)完成的。
- `NavigableMap` 继承了 `SortedMap` 的接口。相比于 `SortedMap`，`NavigableMap` 有一系列的“导航”方法；如"获取大于/等于某对象的键值对"、“获取小于/等于某对象的键值对”等等。
- `HashMap` 继承了 `AbstractMap`，但没实现 `NavigableMap` 接口。`HashMap` 的主要作用是储存无序的键值对，而 `Hash` 也体现了它的查找效率很高。`HashMap` 是使用最广泛的 `Map`。
- `Hashtable` 虽然没有继承 `AbstractMap`，但它继承了 `Dictionary`（`Dictionary` 也是键值对的接口），而且也实现 `Map` 接口。因此，`Hashtable` 的主要作用是储存无序的键值对。和 HashMap 相比，`Hashtable` 在它的主要方法中使用 `synchronized` 关键字修饰，来保证线程安全。但是，由于它的锁粒度太大，非常影响读写速度，所以，现代 Java 程序几乎不会使用 `Hashtable` ，如果需要保证线程安全，一般会用 `ConcurrentHashMap` 来替代。
- `TreeMap` 继承了 `AbstractMap`，且实现了 `NavigableMap` 接口。`TreeMap` 的主要作用是储存有序的键值对，排序依据根据元素类型的 `Comparator` 而定。
- `WeakHashMap` 继承了 `AbstractMap`。`WeakHashMap` 的键是**弱引用** （即 `WeakReference`），它的主要作用是当 GC 内存不足时，会自动将 `WeakHashMap` 中的 key 回收，这避免了 `WeakHashMap` 的内存空间无限膨胀。很明显，`WeakHashMap` 适用于作为缓存。

### 1.2. Map 接口

Map 的定义如下：

```java
public interface Map<K,V> { }
```

Map 是一个用于保存键值对(key-value)的接口。**Map 中不能包含重复的键；每个键最多只能映射到一个值。**

Map 接口提供三种 `Collection` 视图，允许以**键集**、**值集**或**键-值映射关系集**的形式访问数据。

Map 有些实现类，可以有序的保存元素，如 `TreeMap`；另一些实现类则不保证顺序，如 `HashMap` 类。

Map 的实现类应该提供 2 个“标准的”构造方法：

- void（无参数）构造方法，用于创建空 Map；
- 带有单个 Map 类型参数的构造方法，用于创建一个与其参数具有相同键-值映射关系的新 Map。

实际上，后一个构造方法允许用户复制任意 Map，生成所需类的一个等价 Map。尽管无法强制执行此建议（因为接口不能包含构造方法），但是 JDK 中所有通用的 Map 实现都遵从它。

### 1.3. Map.Entry 接口

`Map.Entry` 一般用于通过迭代器（`Iterator`）访问问 `Map`。

`Map.Entry` 是 Map 中内部的一个接口，`Map.Entry` 代表了 **键值对** 实体，Map 通过 `entrySet()` 获取 `Map.Entry` 集合，从而通过该集合实现对键值对的操作。

### 1.4. AbstractMap 抽象类

`AbstractMap` 的定义如下：

```java
public abstract class AbstractMap<K,V> implements Map<K,V> {}
```

`AbstractMap` 抽象类提供了 `Map` 接口的核心实现，以最大限度地减少实现 `Map` 接口所需的工作。

要实现不可修改的 Map，编程人员只需扩展此类并提供 `entrySet()` 方法的实现即可，该方法将返回 `Map` 的映射关系 Set 视图。通常，返回的 set 将依次在 `AbstractSet` 上实现。此 Set 不支持 `add()` 或 `remove()` 方法，其迭代器也不支持 `remove()` 方法。

要实现可修改的 `Map`，编程人员必须另外重写此类的 `put` 方法（否则将抛出 `UnsupportedOperationException`），`entrySet().iterator()` 返回的迭代器也必须另外实现其 `remove()` 方法。

### 1.5. SortedMap 接口

`SortedMap` 的定义如下：

```java
public interface SortedMap<K,V> extends Map<K,V> { }
```

`SortedMap` 继承了 `Map` ，它是一个有序的 `Map`。

`SortedMap` 的排序方式有两种：**自然排序**或者**用户指定比较器**。**插入有序 `SortedMap` 的所有元素都必须实现 `Comparable` 接口（或者被指定的比较器所接受）**。

另外，所有 `SortedMap` 实现类都应该提供 4 个“标准”构造方法：

1.  `void`（无参数）构造方法，它创建一个空的有序 `Map`，按照键的自然顺序进行排序。
2.  带有一个 `Comparator` 类型参数的构造方法，它创建一个空的有序 `Map`，根据指定的比较器进行排序。
3.  带有一个 `Map` 类型参数的构造方法，它创建一个新的有序 `Map`，其键-值映射关系与参数相同，按照键的自然顺序进行排序。
4.  带有一个 `SortedMap` 类型参数的构造方法，它创建一个新的有序 `Map`，其键-值映射关系和排序方法与输入的有序 Map 相同。无法保证强制实施此建议，因为接口不能包含构造方法。

### 1.6. NavigableMap 接口

`NavigableMap` 的定义如下：

```
public interface NavigableMap<K,V> extends SortedMap<K,V> { }
```

`NavigableMap` 继承了 `SortedMap` ，它提供了丰富的查找方法。

NavigableMap 分别提供了获取“键”、“键-值对”、“键集”、“键-值对集”的相关方法。

`NavigableMap` 提供的功能可以分为 4 类：

- **获取键-值对**
  - `lowerEntry`、`floorEntry`、`ceilingEntry` 和 `higherEntry` 方法，它们分别返回与小于、小于等于、大于等于、大于给定键的键关联的 Map.Entry 对象。
  - `firstEntry`、`pollFirstEntry`、`lastEntry` 和 `pollLastEntry` 方法，它们返回和/或移除最小和最大的映射关系（如果存在），否则返回 null。
- **获取键**。这个和第 1 类比较类似。
  - `lowerKey`、`floorKey`、`ceilingKey` 和 `higherKey` 方法，它们分别返回与小于、小于等于、大于等于、大于给定键的键。
- **获取键的集合**
  - `navigableKeySet`、`descendingKeySet` 分别获取正序/反序的键集。
- **获取键-值对的子集**

### 1.7. Dictionary 抽象类

`Dictionary` 的定义如下：

```java
public abstract class Dictionary<K,V> {}
```

`Dictionary` 是 JDK 1.0 定义的操作键值对的抽象类，它包括了操作键值对的基本方法。

## 2. HashMap 类

`HashMap` 类是最常用的 `Map`。

### 2.1. HashMap 要点

从 `HashMap` 的命名，也可以看出：**`HashMap` 以散列方式存储键值对**。

**`HashMap` 允许使用空值和空键**。（`HashMap` 类大致等同于 `Hashtable`，除了它是不同步的并且允许为空值。）这个类不保序；特别是，它的元素顺序可能会随着时间的推移变化。

**`HashMap` 有两个影响其性能的参数：初始容量和负载因子**。

- 容量是哈希表中桶的数量，初始容量就是哈希表创建时的容量。
- 加载因子是散列表在其容量自动扩容之前被允许的最大饱和量。当哈希表中的 entry 数量超过负载因子和当前容量的乘积时，散列表就会被重新映射（即重建内部数据结构），一般散列表大约是存储桶数量的两倍。

通常，默认加载因子（0.75）在时间和空间成本之间提供了良好的平衡。较高的值会减少空间开销，但会增加查找成本（反映在大部分 `HashMap` 类的操作中，包括 `get` 和 `put`）。在设置初始容量时，应考虑映射中的条目数量及其负载因子，以尽量减少重新运行操作的次数。如果初始容量大于最大入口数除以负载因子，则不会发生重新刷新操作。

如果许多映射要存储在 `HashMap` 实例中，使用足够大的容量创建映射将允许映射存储的效率高于根据需要执行自动重新散列以增长表。请注意，使用多个具有相同 `hashCode()` 的密钥是降低任何散列表性能的一个可靠方法。为了改善影响，当键是 `Comparable` 时，该类可以使用键之间的比较顺序来帮助断开关系。

`HashMap` 不是线程安全的。

### 2.2. HashMap 原理

#### HashMap 数据结构

`HashMap` 的核心字段：

- `table` - `HashMap` 使用一个 `Node<K,V>[]` 类型的数组 `table` 来储存元素。
- `size` - 初始容量。 初始为 16，每次容量不够自动扩容
- `loadFactor` - 负载因子。自动扩容之前被允许的最大饱和量，默认 0.75。

```java
public class HashMap<K,V> extends AbstractMap<K,V>
    implements Map<K,V>, Cloneable, Serializable {

    // 该表在初次使用时初始化，并根据需要调整大小。分配时，长度总是2的幂。
    transient Node<K,V>[] table;
    // 保存缓存的 entrySet()。请注意，AbstractMap 字段用于 keySet() 和 values()。
    transient Set<Map.Entry<K,V>> entrySet;
    // map 中的键值对数
    transient int size;
    // 这个HashMap被结构修改的次数结构修改是那些改变HashMap中的映射数量或者修改其内部结构（例如，重新散列）的修改。
    transient int modCount;
    // 下一个调整大小的值（容量*加载因子）。
    int threshold;
    // 散列表的加载因子
    final float loadFactor;
}
```

#### HashMap 构造方法

```java
public HashMap(); // 默认加载因子0.75
public HashMap(int initialCapacity); // 默认加载因子0.75；以 initialCapacity 初始化容量
public HashMap(int initialCapacity, float loadFactor); // 以 initialCapacity 初始化容量；以 loadFactor 初始化加载因子
public HashMap(Map<? extends K, ? extends V> m) // 默认加载因子0.75
```

#### put 方法的实现

put 方法大致的思路为：

- 对 key 的 `hashCode()` 做 hash 计算，然后根据 hash 值再计算 Node 的存储位置;
- 如果没有哈希碰撞，直接放到桶里；如果有哈希碰撞，以链表的形式存在桶后。
- 如果哈希碰撞导致链表过长(大于等于 `TREEIFY_THRESHOLD`，数值为 8)，就把链表转换成红黑树；
- 如果节点已经存在就替换旧值
- 桶数量超过容量\*负载因子（即 load factor \* current capacity），HashMap 调用 `resize` 自动扩容一倍

具体代码的实现如下：

```java
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}

// hashcode 无符号位移 16 位
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}

final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    // tab 为空则创建
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    // 计算 index，并对 null 做处理
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    else {
        Node<K,V> e; K k;
        // 节点存在
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        // 该链为树
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        // 该链为链表
        else {
            for (int binCount = 0; ; ++binCount) {
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        treeifyBin(tab, hash);
                    break;
                }
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }
        // 写入
        if (e != null) { // existing mapping for key
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
    ++modCount;
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict);
    return null;
}
```

为什么计算 hash 使用 hashcode 无符号位移 16 位。

假设要添加两个对象 a 和 b，如果数组长度是 16，这时对象 a 和 b 通过公式 (n - 1) & hash 运算，也就是 (16-1)＆a.hashCode 和 (16-1)＆b.hashCode，15 的二进制为 0000000000000000000000000001111，假设对象 A 的 hashCode 为 1000010001110001000001111000000，对象 B 的 hashCode 为 0111011100111000101000010100000，你会发现上述与运算结果都是 0。这样的哈希结果就太让人失望了，很明显不是一个好的哈希算法。

但如果我们将 hashCode 值右移 16 位（h >>> 16 代表无符号右移 16 位），也就是取 int 类型的一半，刚好可以将该二进制数对半切开，并且使用位异或运算（如果两个数对应的位置相反，则结果为 1，反之为 0），这样的话，就能避免上面的情况发生。这就是 hash() 方法的具体实现方式。**简而言之，就是尽量打乱 hashCode 真正参与运算的低 16 位。**

#### get 方法的实现

在理解了 put 之后，get 就很简单了。大致思路如下：

- 对 key 的 hashCode() 做 hash 计算，然后根据 hash 值再计算桶的 index
- 如果桶中的第一个节点命中，直接返回；
- 如果有冲突，则通过 `key.equals(k)` 去查找对应的 entry

  - 若为树，则在红黑树中通过 key.equals(k) 查找，O(logn)；

  - 若为链表，则在链表中通过 key.equals(k) 查找，O(n)。

具体代码的实现如下：

```java
public V get(Object key) {
    Node<K,V> e;
    return (e = getNode(hash(key), key)) == null ? null : e.value;
}

final Node<K,V> getNode(int hash, Object key) {
        Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (first = tab[(n - 1) & hash]) != null) {
        // 直接命中
        if (first.hash == hash && // always check first node
            ((k = first.key) == key || (key != null && key.equals(k))))
            return first;
        // 未命中
        if ((e = first.next) != null) {
            // 在树中 get
            if (first instanceof TreeNode)
                return ((TreeNode<K,V>)first).getTreeNode(hash, key);
            // 在链表中 get
            do {
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    return e;
            } while ((e = e.next) != null);
        }
    }
    return null;
}
```

#### hash 方法的实现

HashMap **计算桶下标（index）公式：`key.hashCode() ^ (h >>> 16)`**。

下面针对这个公式来详细讲解。

在 `get` 和 `put` 的过程中，计算下标时，先对 `hashCode` 进行 `hash` 操作，然后再通过 `hash` 值进一步计算下标，如下图所示：

<div align="center">
<img src="https://raw.githubusercontent.com/dunwu/images/dev/cs/java/javacore/container/HashMap-hash.png" />
</div>

在对 `hashCode()` 计算 hash 时具体实现是这样的：

```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

可以看到这个方法大概的作用就是：高 16bit 不变，低 16bit 和高 16bit 做了一个异或。

在设计 hash 方法时，因为目前的 table 长度 n 为 2 的幂，而计算下标的时候，是这样实现的(使用 `&` 位操作，而非 `%` 求余)：

```java
(n - 1) & hash
```

设计者认为这方法很容易发生碰撞。为什么这么说呢？不妨思考一下，在 n - 1 为 15(0x1111) 时，其实散列真正生效的只是低 4bit 的有效位，当然容易碰撞了。

因此，设计者想了一个顾全大局的方法(综合考虑了速度、作用、质量)，就是把高 16bit 和低 16bit 异或了一下。设计者还解释到因为现在大多数的 hashCode 的分布已经很不错了，就算是发生了碰撞也用 O(logn)的 tree 去做了。仅仅异或一下，既减少了系统的开销，也不会造成的因为高位没有参与下标的计算(table 长度比较小时)，从而引起的碰撞。

如果还是产生了频繁的碰撞，会发生什么问题呢？作者注释说，他们使用树来处理频繁的碰撞(we use trees to handle large sets of collisions in bins)，在 [JEP-180](http://openjdk.java.net/jeps/180) 中，描述了这个问题：

> Improve the performance of java.util.HashMap under high hash-collision conditions by using balanced trees rather than linked lists to store map entries. Implement the same improvement in the LinkedHashMap class.

之前已经提过，在获取 HashMap 的元素时，基本分两步：

1.  首先根据 hashCode()做 hash，然后确定 bucket 的 index；

2.  如果 bucket 的节点的 key 不是我们需要的，则通过 keys.equals()在链中找。

在 JDK8 之前的实现中是用链表解决冲突的，在产生碰撞的情况下，进行 get 时，两步的时间复杂度是 O(1)+O(n)。因此，当碰撞很厉害的时候 n 很大，O(n)的速度显然是影响速度的。

因此在 JDK8 中，利用红黑树替换链表，这样复杂度就变成了 O(1)+O(logn)了，这样在 n 很大的时候，能够比较理想的解决这个问题，在 JDK8：HashMap 的性能提升一文中有性能测试的结果。

#### resize 的实现

当 `put` 时，如果发现目前的 bucket 占用程度已经超过了 Load Factor 所希望的比例，那么就会发生 resize。在 resize 的过程，简单的说就是把 bucket 扩充为 2 倍，之后重新计算 index，把节点再放到新的 bucket 中。

当超过限制的时候会 resize，然而又因为我们使用的是 2 次幂的扩展(指长度扩为原来 2 倍)，所以，元素的位置要么是在原位置，要么是在原位置再移动 2 次幂的位置。

怎么理解呢？例如我们从 16 扩展为 32 时，具体的变化如下所示：

<div align="center">
<img src="https://raw.githubusercontent.com/dunwu/images/dev/cs/java/javacore/container/HashMap-resize-01.png" />
</div>

因此元素在重新计算 hash 之后，因为 n 变为 2 倍，那么 n-1 的 mask 范围在高位多 1bit(红色)，因此新的 index 就会发生这样的变化：

<div align="center">
<img src="https://raw.githubusercontent.com/dunwu/images/dev/cs/java/javacore/container/HashMap-resize-02.png" />
</div>

因此，我们在扩充 HashMap 的时候，不需要重新计算 hash，只需要看看原来的 hash 值新增的那个 bit 是 1 还是 0 就好了，是 0 的话索引没变，是 1 的话索引变成“原索引+oldCap”。可以看看下图为 16 扩充为 32 的 resize 示意图：

<div align="center">
<img src="https://raw.githubusercontent.com/dunwu/images/dev/cs/java/javacore/container/HashMap-resize-03.png" />
</div>

这个设计确实非常的巧妙，既省去了重新计算 hash 值的时间，而且同时，由于新增的 1bit 是 0 还是 1 可以认为是随机的，因此 resize 的过程，均匀的把之前的冲突的节点分散到新的 bucket 了。

```java
final Node<K,V>[] resize() {
    Node<K,V>[] oldTab = table;
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    int oldThr = threshold;
    int newCap, newThr = 0;
    if (oldCap > 0) {
        // 超过最大值就不再扩充了，就只好随你碰撞去吧
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        // 没超过最大值，就扩充为原来的 2 倍
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                    oldCap >= DEFAULT_INITIAL_CAPACITY)
            newThr = oldThr << 1; // double threshold
    }
    else if (oldThr > 0) // initial capacity was placed in threshold
        newCap = oldThr;
    else {               // zero initial threshold signifies using defaults
        newCap = DEFAULT_INITIAL_CAPACITY;
        newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }

    // 计算新的 resize 上限
    if (newThr == 0) {
        float ft = (float)newCap * loadFactor;
        newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                    (int)ft : Integer.MAX_VALUE);
    }
    threshold = newThr;
    @SuppressWarnings({"rawtypes","unchecked"})
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
    table = newTab;
    if (oldTab != null) {
        // 把每个 bucket 都移动到新的 buckets 中
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            if ((e = oldTab[j]) != null) {
                oldTab[j] = null;
                if (e.next == null)
                    newTab[e.hash & (newCap - 1)] = e;
                else if (e instanceof TreeNode)
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                else { // preserve order
                    Node<K,V> loHead = null, loTail = null;
                    Node<K,V> hiHead = null, hiTail = null;
                    Node<K,V> next;
                    do {
                        next = e.next;
                        // 原索引
                        if ((e.hash & oldCap) == 0) {
                            if (loTail == null)
                                loHead = e;
                            else
                                loTail.next = e;
                            loTail = e;
                        }
                        // 原索引+oldCap
                        else {
                            if (hiTail == null)
                                hiHead = e;
                            else
                                hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);
                    // 原索引放到bucket里
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[j] = loHead;
                    }
                    // 原索引+oldCap放到bucket里
                    if (hiTail != null) {
                        hiTail.next = null;
                        newTab[j + oldCap] = hiHead;
                    }
                }
            }
        }
    }
    return newTab;
}
```

## 3. LinkedHashMap 类

### 3.1. LinkedHashMap 要点

**`LinkedHashMap` 通过维护一个保存所有条目（Entry）的双向链表，保证了元素迭代的顺序（即插入顺序）**。

| 关注点                | 结论                           |
| --------------------- | ------------------------------ |
| 是否允许键值对为 null | Key 和 Value 都允许 null       |
| 是否允许重复数据      | Key 重复会覆盖、Value 允许重复 |
| 是否有序              | 按照元素插入顺序存储           |
| 是否线程安全          | 非线程安全                     |

### 3.2. LinkedHashMap 要点

#### LinkedHashMap 数据结构

**`LinkedHashMap` 通过维护一对 `LinkedHashMap.Entry<K,V>` 类型的头尾指针，以双链表形式，保存所有数据**。

学习过数据结构的双链表，就能理解其元素存储以及访问必然是有序的。

```java
public class LinkedHashMap<K,V>
    extends HashMap<K,V>
    implements Map<K,V> {

    // 双链表的头指针
    transient LinkedHashMap.Entry<K,V> head;
    // 双链表的尾指针
    transient LinkedHashMap.Entry<K,V> tail;
    // 迭代排序方法：true 表示访问顺序；false 表示插入顺序
    final boolean accessOrder;
}
```

`LinkedHashMap` 继承了 `HashMap` 的 `put` 方法，本身没有实现 `put` 方法。

## 4. TreeMap 类

### 4.1. TreeMap 要点

`TreeMap` 基于红黑树实现。

`TreeMap` 是有序的。它的排序规则是：根据 map 中的 key 的自然语义顺序或提供的比较器（`Comparator`）的自定义比较顺序。

TreeMap 不是线程安全的。

### 4.2. TreeMap 原理

#### put 方法

```java
public V put(K key, V value) {
    Entry<K,V> t = root;
    // 如果根节点为 null，插入第一个节点
    if (t == null) {
        compare(key, key); // type (and possibly null) check

        root = new Entry<>(key, value, null);
        size = 1;
        modCount++;
        return null;
    }
    int cmp;
    Entry<K,V> parent;
    // split comparator and comparable paths
    Comparator<? super K> cpr = comparator;
    // 每个节点的左孩子节点的值小于它；右孩子节点的值大于它
    // 如果有比较器，使用比较器进行比较
    if (cpr != null) {
        do {
            parent = t;
            cmp = cpr.compare(key, t.key);
            if (cmp < 0)
                t = t.left;
            else if (cmp > 0)
                t = t.right;
            else
                return t.setValue(value);
        } while (t != null);
    }
    // 没有比较器，使用 key 的自然顺序进行比较
    else {
        if (key == null)
            throw new NullPointerException();
        @SuppressWarnings("unchecked")
            Comparable<? super K> k = (Comparable<? super K>) key;
        do {
            parent = t;
            cmp = k.compareTo(t.key);
            if (cmp < 0)
                t = t.left;
            else if (cmp > 0)
                t = t.right;
            else
                return t.setValue(value);
        } while (t != null);
    }
    // 通过上面的遍历未找到 key 值，则新插入节点
    Entry<K,V> e = new Entry<>(key, value, parent);
    if (cmp < 0)
        parent.left = e;
    else
        parent.right = e;
    // 插入后，为了维持红黑树的平衡需要调整
    fixAfterInsertion(e);
    size++;
    modCount++;
    return null;
}
```

#### get 方法

```java
public V get(Object key) {
    Entry<K,V> p = getEntry(key);
    return (p==null ? null : p.value);
}

final Entry<K,V> getEntry(Object key) {
    // Offload comparator-based version for sake of performance
    if (comparator != null)
        return getEntryUsingComparator(key);
    if (key == null)
        throw new NullPointerException();
    @SuppressWarnings("unchecked")
        Comparable<? super K> k = (Comparable<? super K>) key;
    Entry<K,V> p = root;
    // 按照二叉树搜索的方式进行搜索，搜到返回
    while (p != null) {
        int cmp = k.compareTo(p.key);
        if (cmp < 0)
            p = p.left;
        else if (cmp > 0)
            p = p.right;
        else
            return p;
    }
    return null;
}
```

### 4.3. remove 方法

```java
public V remove(Object key) {
    Entry<K,V> p = getEntry(key);
    if (p == null)
        return null;

    V oldValue = p.value;
    deleteEntry(p);
    return oldValue;
}
private void deleteEntry(Entry<K,V> p) {
    modCount++;
    size--;

    // 如果当前节点有左右孩子节点，使用后继节点替换要删除的节点
    // If strictly internal, copy successor's element to p and then make p
    // point to successor.
    if (p.left != null && p.right != null) {
        Entry<K,V> s = successor(p);
        p.key = s.key;
        p.value = s.value;
        p = s;
    } // p has 2 children

    // Start fixup at replacement node, if it exists.
    Entry<K,V> replacement = (p.left != null ? p.left : p.right);

    if (replacement != null) { // 要删除的节点有一个孩子节点
        // Link replacement to parent
        replacement.parent = p.parent;
        if (p.parent == null)
            root = replacement;
        else if (p == p.parent.left)
            p.parent.left  = replacement;
        else
 D:\codes\zp\java\database\docs\redis\分布式锁.md           p.parent.right = replacement;

        // Null out links so they are OK to use by fixAfterDeletion.
        p.left = p.right = p.parent = null;

        // Fix replacement
        if (p.color == BLACK)
            fixAfterDeletion(replacement);
    } else if (p.parent == null) { // return if we are the only node.
        root = null;
    } else { //  No children. Use self as phantom replacement and unlink.
        if (p.color == BLACK)
            fixAfterDeletion(p);

        if (p.parent != null) {
            if (p == p.parent.left)
                p.parent.left = null;
            else if (p == p.parent.right)
                p.parent.right = null;
            p.parent = null;
        }
    }
}
```

### 4.4. TreeMap 示例

```java
public class TreeMapDemo {

    private static final String[] chars = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z".split(" ");

    public static void main(String[] args) {
        TreeMap<Integer, String> treeMap = new TreeMap<>();
        for (int i = 0; i < chars.length; i++) {
            treeMap.put(i, chars[i]);
        }
        System.out.println(treeMap);
        Integer low = treeMap.firstKey();
        Integer high = treeMap.lastKey();
        System.out.println(low);
        System.out.println(high);
        Iterator<Integer> it = treeMap.keySet().iterator();
        for (int i = 0; i <= 6; i++) {
            if (i == 3) { low = it.next(); }
            if (i == 6) { high = it.next(); } else { it.next(); }
        }
        System.out.println(low);
        System.out.println(high);
        System.out.println(treeMap.subMap(low, high));
        System.out.println(treeMap.headMap(high));
        System.out.println(treeMap.tailMap(low));
    }
}
```

## 5. WeakHashMap

WeakHashMap 的定义如下：

```java
public class WeakHashMap<K,V>
    extends AbstractMap<K,V>
    implements Map<K,V> {}
```

WeakHashMap 继承了 AbstractMap，实现了 Map 接口。

和 HashMap 一样，WeakHashMap 也是一个散列表，它存储的内容也是键值对(key-value)映射，而且键和值都可以是 null。

不过 WeakHashMap 的键是**弱键**。在 WeakHashMap 中，当某个键不再被其它对象引用，会被从 WeakHashMap 中被自动移除。更精确地说，对于一个给定的键，其映射的存在并不阻止垃圾回收器对该键的丢弃，这就使该键成为可终止的，被终止，然后被回收。某个键被终止时，它对应的键值对也就从映射中有效地移除了。

这个**弱键**的原理呢？大致上就是，通过 WeakReference 和 ReferenceQueue 实现的。

WeakHashMap 的 key 是**弱键**，即是 WeakReference 类型的；ReferenceQueue 是一个队列，它会保存被 GC 回收的**弱键**。实现步骤是：

1.  新建 WeakHashMap，将**键值对**添加到 WeakHashMap 中。实际上，WeakHashMap 是通过数组 table 保存 Entry(键值对)；每一个 Entry 实际上是一个单向链表，即 Entry 是键值对链表。
2.  当某**弱键**不再被其它对象引用，并被 GC 回收时。在 GC 回收该**弱键**时，这个**弱键**也同时会被添加到 ReferenceQueue(queue)队列中。
3.  当下一次我们需要操作 WeakHashMap 时，会先同步 table 和 queue。table 中保存了全部的键值对，而 queue 中保存被 GC 回收的键值对；同步它们，就是删除 table 中被 GC 回收的键值对。

这就是**弱键**如何被自动从 WeakHashMap 中删除的步骤了。

和 HashMap 一样，WeakHashMap 是不同步的。可以使用 Collections.synchronizedMap 方法来构造同步的 WeakHashMap。

## 6. 总结

### 6.1. Map 简介

![img](https://raw.githubusercontent.com/dunwu/images/dev/snap/20200221162002.png)

### 6.2. HashMap

![img](https://raw.githubusercontent.com/dunwu/images/dev/snap/20200221162111.png)

### 6.3. 其他 Map

![img](https://raw.githubusercontent.com/dunwu/images/dev/snap/20200221161913.png)

## 7. 参考资料

- [Java-HashMap 工作原理及实现](https://yikun.github.io/2015/04/01/Java-HashMap工作原理及实现)
- [Map 综述（二）：彻头彻尾理解 LinkedHashMap](https://blog.csdn.net/justloveyou_/article/details/71713781)
- [Java 集合系列 09 之 Map 架构](http://www.cnblogs.com/skywang12345/p/3308931.html)
- [Java 集合系列 13 之 WeakHashMap 详细介绍(源码解析)和使用示例](http://www.cnblogs.com/skywang12345/p/3311092.html)
