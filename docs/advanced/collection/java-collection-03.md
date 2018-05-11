# Java 集合深入理解

## ArrayList

> `ArrayList` 是 `List` 接口的动态数组实现，其容量能自动增长。它允许所有元素，包括 null。
>
> `ArrayList` 是非线程安全的，除此以外，大致和 `Vector` 差不多。

### 定义

![image.png](http://upload-images.jianshu.io/upload_images/3101171-c1b24a5267f70e85.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

`ArrayList` 的定义：

```java
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
```

> **说明**
>
> * `ArrayList` 扩展了 `AbstractList` 类，支持对于数据存储（如数组）的一些基本的操作，如（add、remove、get、set）
> * `ArrayList` 实现了 `List` 接口、底层使用数组保存所有元素。其操作基本上是对数组的操作。
> * `ArrayList` 实现了 `RandomAccess` 接口，支持快速随机访问，实际上就是通过下标序号进行快速访问。
> * `ArrayList` 实现了`Cloneable` 接口，支持克隆。
> * `ArrayList` 实现了 `Serializable` 接口，支持序列化。

### 动态数组

每个 `ArrayList` 实例都有一个容量，该容量是指用来存储列表元素的数组的大小。它总是至少等于列表的大小。

每当向数组中添加元素时，都要去检查添加后元素的个数是否会超出当前数组的长度，如果超出，数组将会进行扩容。

> 数组进行扩容时，会将老数组中的元素重新拷贝一份到新的数组中，每次数组容量的增长大约是其原容量的 1.5 倍。这种操作的代价是很高的，因此在实际使用时，我们应该尽量避免数组容量的扩张。当我们可预知要保存的元素的多少时，要在构造 `ArrayList` 实例时，就指定其容量，以避免数组扩容的发生。或者根据实际需求，通过调用 `ensureCapacity` 方法来手动增加`ArrayList` 实例的容量。

## LinkedList

> `LinkedList` 是 `List` 和 `Qeue` 接口的双链表实现。
>
> `LinkedList` 是非线程安全的。

### 定义

![image.png](http://upload-images.jianshu.io/upload_images/3101171-a60efa703191b6bd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

`LinkedList` 的定义：

```java
public class LinkedList<E>
     extends AbstractSequentialList<E>
     implements List<E>, Deque<E>, Cloneable, java.io.Serializable
```

> **说明**
>
> * `LinkedList` 是一个继承于`AbstractSequentialList` 的双链表。它也可以被当作堆栈、队列或双端队列进行操作。
> * `LinkedList` 实现 `List` 接口，能对它进行队列操作。
> * `LinkedList` 实现 `Deque` 接口，即能将 `LinkedList` 当作双链表使用。
> * `LinkedList` 实现了`Cloneable` 接口，支持克隆。
> * `LinkedList` 实现了 `Serializable` 接口，支持序列化。

## HashMap

`HashMap` 是 `Map` 接口的 Hash 表实现。它允许 key 或 value 为 null。

`HashMap` 大致等同于 `Hashtable`，除了它是不同步的并且允许空值的。

`HashMap` 是不保序的。

### 定义

![image.png](http://upload-images.jianshu.io/upload_images/3101171-9dd276c2691fe0a4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

`HashMap` 的定义：

```java
public class HashMap<K,V> extends AbstractMap<K,V>
    implements Map<K,V>, Cloneable, Serializable
```

> **说明**
>
> * `HashMap` 扩展了 `AbstractMap` 类，相当于默认实现了 `Map` 的基本操作。
> * `HashMap` 实现 `Map` 接口，支持 `Map` 操作。
> * `HashMap` 实现了`Cloneable` 接口，支持克隆。
> * `HashMap` 实现了 `Serializable` 接口，支持序列化。

### 原理

HashMap 的内部实现是采用 Hash 算法。

初始化 `HashMap` 时，系统会创建一个长度为 capacity 的 `Entry` 数组，这个数组里可以存储元素的位置被称为**桶（bucket）**，每个 bucket 都有其指定索引，系统可以根据其索引快速访问该 bucket 里存储的元素。

### 性能

`HashMap` 有两个影响其性能的参数：初始容量和加载因子。

容量表示哈希表中桶的数量，初始容量就是哈希表创建时的容量。

加载因子是散列表在其容量自动增加之前被允许得到的度量。它衡量的是一个散列表的空间的使用程度，负载因子越大表示散列表的装填程度越高，反之愈小。

**如果负载因子越大，对空间的利用更充分，然而后果是查找效率的降低；**

**如果负载因子太小，那么散列表的数据将过于稀疏，对空间造成严重浪费。**系统默认负载因子为 **0.75**，一般情况下我们是无需修改的。

### 扩容

随着 `HashMap` 中元素的数量越来越多，发生碰撞的概率就越来越大，所产生的链表长度就会越来越长，这样势必会影响 `HashMap` 的速度，

为了保证 `HashMap` 的效率，系统必须要在某个临界点进行扩容处理。

该临界点在当 `HashMap` 中元素的数量等于容量 \* 加载因子。

但是扩容是一个非常耗时的过程，因为它需要重新计算这些数据在新 table 数组中的位置并进行复制处理。

所以如果我们已经预知 `HashMap` 中元素的个数，那么预设元素的个数能够有效的提高`HashMap` 的性能。

## LinkedHashMap

`LinkedHashMap` 是 `Map` 接口的 Hash 表以及链表的实现。与 HashMap 不同的是，它维护了一个双链表，从而保证存入 Map 的数据按照插入顺序排列。

### 定义

![image.png](http://upload-images.jianshu.io/upload_images/3101171-c9cc0106796841d1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

`LinkedHashMap` 的定义：

```java
public class LinkedHashMap<K,V>
    extends HashMap<K,V>
    implements Map<K,V>
```

> **说明**
>
> * `HashMap` 扩展了 `HashMap` 类，相当于继承了 `HashMap` 的所有功能。
> * `HashMap` 实现 `Map` 接口，支持 `Map` 操作。

### 双链表

`LinkedHashMap` 和 `HashMap` 的区别在于它们的基本数据结构上。

`LinkedHashMap.Entry` 比 `HashMap.Entry` 多了两个元素：before、After，这两个元素分别表示当前元素的上一个节点和下一个节点。通过这样的设计，实际上等于根据元素插入顺序维护了一张双链表。

## TreeMap

> `TreeMap` 是 `NavigableMap` 接口的红黑树实现。
>
> 它根据键的自然顺序或者在创建 Map 时提供的比较器去进行排序。

### 定义

![image.png](http://upload-images.jianshu.io/upload_images/3101171-10e471130cb4c222.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

`TreeMap` 的定义：

```java
public class TreeMap<K,V>
    extends AbstractMap<K,V>
    implements NavigableMap<K,V>, Cloneable, java.io.Serializable
```

> **说明**
>
> * `HashMap` 扩展了 `AbstractMap` 类，相当于默认实现了 `Map` 的基本操作。
> * `TreeMap` 实现 `NavigableMap` 接口，支持 `Map` 操作。
> * `TreeMap` 实现了`Cloneable` 接口，支持克隆。
> * `TreeMap` 实现了 `Serializable` 接口，支持序列化。
