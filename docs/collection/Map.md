---
title: 容器之 Map
date: 2018/05/31
categories:
- javase
tags:
- javase
- collection
---

# 容器之 Map

<!-- TOC -->

- [容器之 Map](#map)
    - [Map](#map)
        - [Map 要点](#map)
    - [HashMap](#hashmap)
        - [HashMap 要点](#hashmap)
        - [HashMap 源码](#hashmap)
            - [HashMap 定义](#hashmap)
            - [构造方法](#)
            - [put 函数的实现](#put)
            - [get 函数的实现](#get)
            - [hash 函数的实现](#hash)
            - [resize 的实现](#resize)
        - [小结](#)
    - [LinkedHashMap](#linkedhashmap)
        - [LinkedHashMap 要点](#linkedhashmap)
        - [LinkedHashMap 源码](#linkedhashmap)
            - [LinkedHashMap 定义](#linkedhashmap)
    - [TreeMap](#treemap)
        - [TreeMap 要点](#treemap)
        - [TreeMap 源码](#treemap)
            - [put 函数](#put)
            - [get 函数](#get)
        - [remove 函数](#remove)
        - [TreeMap 示例](#treemap)
    - [资料](#)

<!-- /TOC -->

## Map

### Map 要点

Map 是 Map 框架中的根接口。

<div align="center">
<img src="https://raw.githubusercontent.com/dunwu/javase-notes/master/images/collection/map-api.png" />
</div>

Map.Entry 一般用于迭代访问 Map。

<div align="center">
<img src="https://raw.githubusercontent.com/dunwu/javase-notes/master/images/collection/map-entry-api.png" />
</div>

## HashMap

### HashMap 要点

HashMap 是一个散列表，它存储的内容是键值对(key-value)映射。

基于哈希表的 Map 接口实现。该实现提供了所有可选的 Map 操作，并允许使用空值和空键。 （HashMap 类大致等同于 Hashtable，除了它是不同步的并且允许为空值。）这个类不保序；特别是，它的元素顺序可能会随着时间的推移变化。

HashMap 的一个实例有两个影响其性能的参数：初始容量和负载因子。

容量是哈希表中桶的数量，初始容量就是哈希表创建时的容量。

加载因子是散列表在其容量自动扩容之前被允许的最大饱和量。当哈希表中的 entry 数量超过负载因子和当前容量的乘积时，散列表就会被重新映射（即重建内部数据结构），一般散列表大约是存储桶数量的两倍。

通常，默认加载因子（0.75）在时间和空间成本之间提供了良好的平衡。较高的值会减少空间开销，但会增加查找成本（反映在大部分 HashMap 类的操作中，包括 get 和 put）。在设置初始容量时，应考虑映射中的条目数量及其负载因子，以尽量减少重新运行操作的次数。如果初始容量大于最大入口数除以负载因子，则不会发生重新刷新操作。

如果许多映射要存储在 HashMap 实例中，使用足够大的容量创建映射将允许映射存储的效率高于根据需要执行自动重新散列以增长表。请注意，使用多个具有相同 hashCode() 的密钥是降低任何散列表性能的一个可靠方法。为了改善影响，当键是 Comparable 时，该类可以使用键之间的比较顺序来帮助断开关系。

HashMap 不是并发安全的。

### HashMap 源码

#### HashMap 定义

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

#### 构造方法

```java
public HashMap(); // 默认加载因子0.75
public HashMap(int initialCapacity); // 默认加载因子0.75；以 initialCapacity 初始化容量
public HashMap(int initialCapacity, float loadFactor); // 以 initialCapacity 初始化容量；以 loadFactor 初始化加载因子
public HashMap(Map<? extends K, ? extends V> m) // 默认加载因子0.75
```

#### put 函数的实现

put 函数大致的思路为：

1.  对 key 的 hashCode()做 hash，然后再计算 index;

2.  如果没碰撞直接放到 bucket 里；

3.  如果碰撞了，以链表的形式存在 buckets 后；

4.  如果碰撞导致链表过长(大于等于 TREEIFY_THRESHOLD)，就把链表转换成红黑树；

5.  如果节点已经存在就替换 old value(保证 key 的唯一性)

6.  如果 bucket 满了(超过 load factor \* current capacity)，就要 resize。

具体代码的实现如下：

```java
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
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

#### get 函数的实现

在理解了 put 之后，get 就很简单了。大致思路如下：

1.  bucket 里的第一个节点，直接命中；

2.  如果有冲突，则通过 key.equals(k)去查找对应的 entry

    * 若为树，则在树中通过 key.equals(k)查找，O(logn)；

    * 若为链表，则在链表中通过 key.equals(k)查找，O(n)。

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

#### hash 函数的实现

在 get 和 put 的过程中，计算下标时，先对 hashCode 进行 hash 操作，然后再通过 hash 值进一步计算下标，如下图所示：

<div align="center">
<img src="https://raw.githubusercontent.com/dunwu/javase-notes/master/images/collection/HashMap-hash.png" />
</div>

在对 hashCode() 计算 hash 时具体实现是这样的：

```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

可以看到这个函数大概的作用就是：高 16bit 不变，低 16bit 和高 16bit 做了一个异或。

在设计 hash 函数时，因为目前的 table 长度 n 为 2 的幂，而计算下标的时候，是这样实现的(使用&位操作，而非%求余)：

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

当 put 时，如果发现目前的 bucket 占用程度已经超过了 Load Factor 所希望的比例，那么就会发生 resize。在 resize 的过程，简单的说就是把 bucket 扩充为 2 倍，之后重新计算 index，把节点再放到新的 bucket 中。

当超过限制的时候会 resize，然而又因为我们使用的是 2 次幂的扩展(指长度扩为原来 2 倍)，所以，元素的位置要么是在原位置，要么是在原位置再移动 2 次幂的位置。

怎么理解呢？例如我们从 16 扩展为 32 时，具体的变化如下所示：

<div align="center">
<img src="https://raw.githubusercontent.com/dunwu/javase-notes/master/images/collection/HashMap-resize-01.png" />
</div>

因此元素在重新计算 hash 之后，因为 n 变为 2 倍，那么 n-1 的 mask 范围在高位多 1bit(红色)，因此新的 index 就会发生这样的变化：

<div align="center">
<img src="https://raw.githubusercontent.com/dunwu/javase-notes/master/images/collection/HashMap-resize-02.png" />
</div>

因此，我们在扩充 HashMap 的时候，不需要重新计算 hash，只需要看看原来的 hash 值新增的那个 bit 是 1 还是 0 就好了，是 0 的话索引没变，是 1 的话索引变成“原索引+oldCap”。可以看看下图为 16 扩充为 32 的 resize 示意图：

<div align="center">
<img src="https://raw.githubusercontent.com/dunwu/javase-notes/master/images/collection/HashMap-resize-03.png" />
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

### 小结

我们现在可以回答开始的几个问题，加深对 HashMap 的理解：

1.  什么时候会使用 HashMap？他有什么特点？

    是基于 Map 接口的实现，存储键值对时，它可以接收 null 的键值，是非同步的，HashMap 存储着 Entry(hash, key, value, next)对象。

2.  你知道 HashMap 的工作原理吗？

    通过 hash 的方法，通过 put 和 get 存储和获取对象。存储对象时，我们将 K/V 传给 put 方法时，它调用 hashCode 计算 hash 从而得到 bucket 位置，进一步存储，HashMap 会根据当前 bucket 的占用情况自动调整容量(超过 Load Facotr 则 resize 为原来的 2 倍)。获取对象时，我们将 K 传给 get，它调用 hashCode 计算 hash 从而得到 bucket 位置，并进一步调用 equals()方法确定键值对。如果发生碰撞的时候，Hashmap 通过链表将产生碰撞冲突的元素组织起来，在 Java 8 中，如果一个 bucket 中碰撞冲突的元素超过某个限制(默认是 8)，则使用红黑树来替换链表，从而提高速度。

3.  你知道 get 和 put 的原理吗？equals()和 hashCode()的都有什么作用？

    通过对 key 的 hashCode()进行 hashing，并计算下标( n-1 & hash)，从而获得 buckets 的位置。如果产生碰撞，则利用 key.equals()方法去链表或树中去查找对应的节点

4.  你知道 hash 的实现吗？为什么要这样实现？

    在 Java 1.8 的实现中，是通过 hashCode()的高 16 位异或低 16 位实现的：(h = k.hashCode()) ^ (h >>> 16)，主要是从速度、功效、质量来考虑的，这么做可以在 bucket 的 n 比较小的时候，也能保证考虑到高低 bit 都参与到 hash 的计算中，同时不会有太大的开销。

5.  如果 HashMap 的大小超过了负载因子(load factor)定义的容量，怎么办？

    如果超过了负载因子(默认 0.75)，则会重新 resize 一个原来长度两倍的 HashMap，并且重新调用 hash 方法。

## LinkedHashMap

### LinkedHashMap 要点

LinkedHashMap 通过维护一个运行于所有条目的双向链表，保证了元素迭代的顺序。

| 关注点                              | 结论                           |
| ----------------------------------- | ------------------------------ |
| LinkedHashMap 是否允许键值对为 null | Key 和 Value 都允许 null       |
| LinkedHashMap 是否允许重复数据      | Key 重复会覆盖、Value 允许重复 |
| LinkedHashMap 是否有序              | 有序                           |
| LinkedHashMap 是否线程安全          | 非线程安全                     |

### LinkedHashMap 源码

#### LinkedHashMap 定义

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

LinkedHashMap 继承了 HashMap 的 put 函数，本身没有实现 put 函数。

## TreeMap

### TreeMap 要点

TreeMap 基于红黑树实现。

TreeMap 是有序的。它的排序规则是：根据 map 中的 key 的自然顺序或提供的比较器的比较顺序。

TreeMap 不是并发安全的。

### TreeMap 源码

#### put 函数

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

#### get 函数

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

### remove 函数

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

    if (replacement != null) {
        // Link replacement to parent
        replacement.parent = p.parent;
        if (p.parent == null)
            root = replacement;
        else if (p == p.parent.left)
            p.parent.left  = replacement;
        else
            p.parent.right = replacement;

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

### TreeMap 示例

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

## 资料

* https://yikun.github.io/2015/04/01/Java-HashMap%E5%B7%A5%E4%BD%9C%E5%8E%9F%E7%90%86%E5%8F%8A%E5%AE%9E%E7%8E%B0/
* https://blog.csdn.net/justloveyou_/article/details/71713781