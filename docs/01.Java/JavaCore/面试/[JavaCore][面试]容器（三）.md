---
title: Java 容器面试三
date: 2024-07-03 07:44:02
order: 6
categories:
  - Java
  - JavaCore
  - 面试
tags:
  - Java
  - JavaCore
  - 面试
  - 容器
permalink: /pages/ed0f8b4b/
---

# Java 容器面试三

## Stream API

### 【中等】Stream API 的中间操作和终端操作有什么区别？⭐⭐⭐

Stream 操作分为**中间操作**（返回 Stream，可链式）和**终端操作**（触发执行，返回结果）。

**核心区别**：

| **维度** | **中间操作** | **终端操作** |
| -------- | ------------ | ------------ |
| **返回类型** | `Stream<T>` | 非 Stream（值/集合/void） |
| **执行时机** | 惰性（不触发执行） | 立即触发整个流水线 |
| **链式调用** | 可继续接操作 | 流终止，不可再操作 |
| **短路** | 部分支持（如 `limit`） | 部分支持（如 `findFirst`） |

**常见中间操作**：

| **操作** | **说明** | **示例** |
| -------- | -------- | -------- |
| `filter` | 过滤 | `.filter(s -> s.length() > 3)` |
| `map` | 转换 | `.map(String::toUpperCase)` |
| `flatMap` | 扁平化 | `.flatMap(list -> list.stream())` |
| `distinct` | 去重 | `.distinct()` |
| `sorted` | 排序 | `.sorted(Comparator.reverseOrder())` |
| `limit` | 取前 N 个 | `.limit(10)` |
| `skip` | 跳过前 N 个 | `.skip(5)` |
| `peek` | 查看（调试用） | `.peek(System.out::println)` |

**常见终端操作**：

| **操作** | **说明** | **示例** |
| -------- | -------- | -------- |
| `collect` | 收集为集合 | `.collect(Collectors.toList())` |
| `forEach` | 遍历 | `.forEach(System.out::println)` |
| `reduce` | 归约 | `.reduce(0, Integer::sum)` |
| `count` | 计数 | `.count()` |
| `min`/`max` | 最值 | `.max(Comparator.naturalOrder())` |
| `anyMatch`/`allMatch`/`noneMatch` | 匹配 | `.anyMatch(s -> s.startsWith("a"))` |
| `findFirst`/`findAny` | 查找 | `.findFirst()` |
| `toArray` | 转数组 | `.toArray(String[]::new)` |

**惰性求值示例**：

```java
// 中间操作不执行，直到终端操作触发
Stream<String> stream = list.stream()
    .filter(s -> s.length() > 3)  // 未执行
    .map(String::toUpperCase);    // 未执行

stream.forEach(System.out::println);  // 此刻才执行全部流水线
```

### 【中等】什么是短路操作？⭐⭐

**短路操作**指无需处理所有元素即可返回结果，提升性能。

**短路中间操作**：

- `limit(n)`：取到 n 个后停止。
- `skip(n)` 虽不短路，但配合 `limit` 可实现"分页"。

**短路终端操作**：

| **操作** | **短路条件** |
| -------- | ------------ |
| `findFirst()` | 找到第一个即停止 |
| `findAny()` | 找到任一个即停止（并行流更快） |
| `anyMatch()` | 遇到 true 即停止，返回 true |
| `allMatch()` | 遇到 false 即停止，返回 false |
| `noneMatch()` | 遇到 true 即停止，返回 false |

**示例**：

```java
// 1. findFirst 短路：只处理到第一个匹配元素
Optional<String> first = list.stream()
    .filter(s -> s.length() > 3)
    .findFirst();  // 找到第一个就停

// 2. anyMatch 短路
boolean hasLong = list.stream()
    .peek(s -> System.out.println("检查: " + s))  // 仅打印到匹配为止
    .anyMatch(s -> s.length() > 10);
```

### 【中等】并行流（Parallel Stream）的原理和注意事项？⭐⭐⭐

并行流利用 **ForkJoinPool.commonPool()** 并行处理数据，适合**数据量大**且**无顺序要求**的场景。

**使用方式**：

```java
// 串行流
list.stream().filter(...).collect(...);

// 并行流
list.parallelStream().filter(...).collect(...);
list.stream().parallel().filter(...).collect(...);

// 切换回串行
stream.sequential();
```

**底层原理**：

- 基于 `ForkJoinPool.commonPool()`，默认线程数 = CPU 核心数 - 1。
- 使用**分治策略**：将源数据分割，并行处理，最后合并结果。
- 源数据需支持**高效分割**（如 `ArrayList` 支持，`LinkedList` 不支持）。

**适用场景**：

| **适合并行** | **不适合并行** |
| ------------ | -------------- |
| 数据量大（>1万） | 数据量小 |
| 元素处理耗时 | 元素处理简单 |
| 无顺序要求 | 严格顺序要求 |
| 无共享可变状态 | 有副作用（修改共享变量） |
| 源支持分割（ArrayList） | 源不支持分割（LinkedList） |

**常见陷阱**：

```java
// ❌ 陷阱1：线程安全问题（共享可变状态）
List<Integer> results = new ArrayList<>();
list.parallelStream().forEach(results::add);  // ArrayList 非线程安全

// ✔️ 解决：用 collect
List<Integer> results = list.parallelStream().collect(Collectors.toList());

// ❌ 陷阱2：顺序依赖操作
list.parallelStream()
    .limit(10)  // limit 在并行流中开销大
    .collect(Collectors.toList());

// ❌ 陷阱3：阻塞操作占用公共线程池
list.parallelStream().forEach(item -> {
    Thread.sleep(1000);  // 占用 commonPool，影响其他并行流
});
```

**性能对比**：

```java
// 数据量大时，并行流更快
long count = IntStream.range(0, 10_000_000)
    .parallel()
    .filter(x -> x % 2 == 0)
    .count();  // 并行更快
```

### 【中等】Collectors 工具类有哪些常用方法？⭐⭐⭐

`Collectors` 提供丰富的收集器，是 Stream API 的核心工具。

**归类汇总**：

| **收集器** | **作用** | **示例** |
| ---------- | -------- | -------- |
| `toList()` | 收集为 List | `.collect(Collectors.toList())` |
| `toSet()` | 收集为 Set（去重） | `.collect(Collectors.toSet())` |
| `toMap(k, v)` | 收集为 Map | `.collect(Collectors.toMap(User::getId, User::getName))` |
| `toCollection()` | 收集为指定集合 | `.collect(Collectors.toCollection(LinkedList::new))` |
| `joining()` | 拼接字符串 | `.collect(Collectors.joining(", "))` |

**分组分区**：

```java
// 分组（groupingBy）
Map<Department, List<Employee>> byDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDepartment));

// 多级分组
Map<Dept, Map<String, List<Employee>>> byDeptAndLevel = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.groupingBy(Employee::getLevel)));

// 分区（partitioningBy，key 为 boolean）
Map<Boolean, List<Integer>> parts = numbers.stream()
    .collect(Collectors.partitioningBy(n -> n > 0));  // 正数/非正数

// 分组后计数
Map<Dept, Long> countByDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDept, Collectors.counting()));

// 分组后求和
Map<Dept, Integer> sumByDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDept, Collectors.summingInt(Employee::getSalary)));
```

**归约统计**：

```java
// 求和
int sum = list.stream().collect(Collectors.summingInt(Integer::intValue));

// 平均值
double avg = list.stream().collect(Collectors.averagingInt(Integer::intValue));

// 统计摘要（count/sum/min/avg/max）
IntSummaryStatistics stats = list.stream()
    .collect(Collectors.summarizingInt(Integer::intValue));

// 归约
Optional<Integer> max = list.stream()
    .collect(Collectors.maxBy(Comparator.naturalOrder()));
```

**toMap 的常见坑**：

```java
// ❌ key 重复时抛 IllegalStateException
Map<Long, String> map = users.stream()
    .collect(Collectors.toMap(User::getId, User::getName));
// 若有两个用户 id 相同，抛异常

// ✔️ 指定 merge 函数处理冲突
Map<Long, String> map = users.stream()
    .collect(Collectors.toMap(
        User::getId,
        User::getName,
        (existing, replacement) -> existing));  // 保留旧值

// ✔️ 指定 Map 实现
Map<Long, String> map = users.stream()
    .collect(Collectors.toMap(
        User::getId, User::getName, (a, b) -> a, LinkedHashMap::new));
```

## Java 容器工具类

**`Collections` 工具类常用方法**:

- 排序
- 查找，替换操作
- 同步控制（不推荐，需要线程安全的集合类型时请考虑使用 JUC 包下的并发集合）

### 【简单】排序操作⭐

```
void reverse(List list)//反转
void shuffle(List list)//随机排序
void sort(List list)//按自然排序的升序排序
void sort(List list, Comparator c)//定制排序，由 Comparator 控制排序逻辑
void swap(List list, int i , int j)//交换两个索引位置的元素
void rotate(List list, int distance)//旋转。当 distance 为正数时，将 list 后 distance 个元素整体移到前面。当 distance 为负数时，将 list 的前 distance 个元素整体移到后面
```

### 【简单】查找，替换操作⭐

```
int binarySearch(List list, Object key)//对 List 进行二分查找，返回索引，注意 List 必须是有序的
int max(Collection coll)//根据元素的自然顺序，返回最大的元素。 类比 int min(Collection coll)
int max(Collection coll, Comparator c)//根据定制排序，返回最大元素，排序规则由 Comparatator 类控制。类比 int min(Collection coll, Comparator c)
void fill(List list, Object obj)//用指定的元素代替指定 list 中的所有元素
int frequency(Collection c, Object o)//统计元素出现次数
int indexOfSubList(List list, List target)//统计 target 在 list 中第一次出现的索引，找不到则返回-1，类比 int lastIndexOfSubList(List source, list target)
boolean replaceAll(List list, Object oldVal, Object newVal)//用新元素替换旧元素
```

### 【简单】同步控制⭐

`Collections` 提供了多个`synchronizedXxx()`方法·，该方法可以将指定集合包装成线程同步的集合，从而解决多线程并发访问集合时的线程安全问题。

我们知道 `HashSet`，`TreeSet`，`ArrayList`,`LinkedList`,`HashMap`,`TreeMap` 都是线程不安全的。`Collections` 提供了多个静态方法可以把他们包装成线程同步的集合。

**最好不要用下面这些方法，效率非常低，需要线程安全的集合类型时请考虑使用 JUC 包下的并发集合。**

方法如下：

```
synchronizedCollection(Collection<T>  c) //返回指定 collection 支持的同步（线程安全的）collection。
synchronizedList(List<T> list)//返回指定列表支持的同步（线程安全的）List。
synchronizedMap(Map<K,V> m) //返回由指定映射支持的同步（线程安全的）Map。
synchronizedSet(Set<T> s) //返回指定 set 支持的同步（线程安全的）set。
```

## 集合判空

《阿里巴巴 Java 开发手册》的描述如下：

> **判断所有集合内部的元素是否为空，使用 `isEmpty()` 方法，而不是 `size()==0` 的方式。**

这是因为 `isEmpty()` 方法的可读性更好，并且时间复杂度为 O(1)。

绝大部分我们使用的集合的 `size()` 方法的时间复杂度也是 O(1)，不过，也有很多复杂度不是 O(1) 的，比如 `java.util.concurrent` 包下的某些集合（`ConcurrentLinkedQueue`、`ConcurrentHashMap`...）。

下面是 `ConcurrentHashMap` 的 `size()` 方法和 `isEmpty()` 方法的源码。

```
public int size() {
    long n = sumCount();
    return ((n < 0L) ? 0 :
            (n > (long)Integer.MAX_VALUE) ? Integer.MAX_VALUE :
            (int)n);
}
final long sumCount() {
    CounterCell[] as = counterCells; CounterCell a;
    long sum = baseCount;
    if (as != null) {
        for (int i = 0; i < as.length; ++i) {
            if ((a = as[i]) != null)
                sum += a.value;
        }
    }
    return sum;
}
public boolean isEmpty() {
    return sumCount() <= 0L; // ignore transient negative values
}
```

## 集合转 Map

《阿里巴巴 Java 开发手册》的描述如下：

> **在使用 `java.util.stream.Collectors` 类的 `toMap()` 方法转为 `Map` 集合时，一定要注意当 value 为 null 时会抛 NPE 异常。**

```
class Person {
    private String name;
    private String phoneNumber;
     // getters and setters
}

List<Person> bookList = new ArrayList<>();
bookList.add(new Person("jack","18163138123"));
bookList.add(new Person("martin",null));
// 空指针异常
bookList.stream().collect(Collectors.toMap(Person::getName, Person::getPhoneNumber));
```

下面我们来解释一下原因。

首先，我们来看 `java.util.stream.Collectors` 类的 `toMap()` 方法 ，可以看到其内部调用了 `Map` 接口的 `merge()` 方法。

```
public static <T, K, U, M extends Map<K, U>>
Collector<T, ?, M> toMap(Function<? super T, ? extends K> keyMapper,
                            Function<? super T, ? extends U> valueMapper,
                            BinaryOperator<U> mergeFunction,
                            Supplier<M> mapSupplier) {
    BiConsumer<M, T> accumulator
            = (map, element) -> map.merge(keyMapper.apply(element),
                                          valueMapper.apply(element), mergeFunction);
    return new CollectorImpl<>(mapSupplier, accumulator, mapMerger(mergeFunction), CH_ID);
}
```

`Map` 接口的 `merge()` 方法如下，这个方法是接口中的默认实现。

> 如果你还不了解 Java 8 新特性的话，请看这篇文章：[《Java8 新特性总结》](https://mp.weixin.qq.com/s/ojyl7B6PiHaTWADqmUq2rw) 。

```
default V merge(K key, V value,
        BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
    Objects.requireNonNull(remappingFunction);
    Objects.requireNonNull(value);
    V oldValue = get(key);
    V newValue = (oldValue == null) ? value :
               remappingFunction.apply(oldValue, value);
    if(newValue == null) {
        remove(key);
    } else {
        put(key, newValue);
    }
    return newValue;
}
```

`merge()` 方法会先调用 `Objects.requireNonNull()` 方法判断 value 是否为空。

```
public static <T> T requireNonNull(T obj) {
    if (obj == null)
        throw new NullPointerException();
    return obj;
}
```

## 集合遍历

《阿里巴巴 Java 开发手册》的描述如下：

> **不要在 foreach 循环里进行元素的 `remove/add` 操作。remove 元素请使用 `Iterator` 方式，如果并发操作，需要对 `Iterator` 对象加锁。**

通过反编译你会发现 foreach 语法底层其实还是依赖 `Iterator` 。不过， `remove/add` 操作直接调用的是集合自己的方法，而不是 `Iterator` 的 `remove/add`方法

这就导致 `Iterator` 莫名其妙地发现自己有元素被 `remove/add` ，然后，它就会抛出一个 `ConcurrentModificationException` 来提示用户发生了并发修改异常。这就是单线程状态下产生的 **fail-fast 机制**。

> **fail-fast 机制**：多个线程对 fail-fast 集合进行修改的时候，可能会抛出`ConcurrentModificationException`。 即使是单线程下也有可能会出现这种情况，上面已经提到过。
>
> 相关阅读：[什么是 fail-fast](https://www.cnblogs.com/54chensongxia/p/12470446.html) 。

Java8 开始，可以使用 `Collection#removeIf()`方法删除满足特定条件的元素，如

```
List<Integer> list = new ArrayList<>();
for (int i = 1; i <= 10; ++i) {
    list.add(i);
}
list.removeIf(filter -> filter % 2 == 0); /* 删除 list 中的所有偶数 */
System.out.println(list); /* [1, 3, 5, 7, 9] */
```

除了上面介绍的直接使用 `Iterator` 进行遍历操作之外，你还可以：

- 使用普通的 for 循环
- 使用 fail-safe 的集合类。`java.util`包下面的所有的集合类都是 fail-fast 的，而`java.util.concurrent`包下面的所有的类都是 fail-safe 的。
- ……

## 集合去重

《阿里巴巴 Java 开发手册》的描述如下：

> **可以利用 `Set` 元素唯一的特性，可以快速对一个集合进行去重操作，避免使用 `List` 的 `contains()` 进行遍历去重或者判断包含操作。**

这里我们以 `HashSet` 和 `ArrayList` 为例说明。

```
// Set 去重代码示例
public static <T> Set<T> removeDuplicateBySet(List<T> data) {

    if (CollectionUtils.isEmpty(data)) {
        return new HashSet<>();
    }
    return new HashSet<>(data);
}

// List 去重代码示例
public static <T> List<T> removeDuplicateByList(List<T> data) {

    if (CollectionUtils.isEmpty(data)) {
        return new ArrayList<>();

    }
    List<T> result = new ArrayList<>(data.size());
    for (T current : data) {
        if (!result.contains(current)) {
            result.add(current);
        }
    }
    return result;
}
```

两者的核心差别在于 `contains()` 方法的实现。

`HashSet` 的 `contains()` 方法底部依赖的 `HashMap` 的 `containsKey()` 方法，时间复杂度接近于 O（1）（没有出现哈希冲突的时候为 O（1））。

```
private transient HashMap<E,Object> map;
public boolean contains(Object o) {
    return map.containsKey(o);
}
```

我们有 N 个元素插入进 Set 中，那时间复杂度就接近是 O (n)。

`ArrayList` 的 `contains()` 方法是通过遍历所有元素的方法来做的，时间复杂度接近是 O(n)。

```
public boolean contains(Object o) {
    return indexOf(o) >= 0;
}
public int indexOf(Object o) {
    if (o == null) {
        for (int i = 0; i < size; i++)
            if (elementData[i]==null)
                return i;
    } else {
        for (int i = 0; i < size; i++)
            if (o.equals(elementData[i]))
                return i;
    }
    return -1;
}
```

## 集合转数组

《阿里巴巴 Java 开发手册》的描述如下：

> **使用集合转数组的方法，必须使用集合的 `toArray(T[] array)`，传入的是类型完全一致、长度为 0 的空数组。**

`toArray(T[] array)` 方法的参数是一个泛型数组，如果 `toArray` 方法中没有传递任何参数的话返回的是 `Object`类 型数组。

```java
String [] s= new String[]{
    "dog", "lazy", "a", "over", "jumps", "fox", "brown", "quick", "A"
};
List<String> list = Arrays.asList(s);
Collections.reverse(list);
//没有指定类型的话会报错
s=list.toArray(new String[0]);
```

由于 JVM 优化，`new String[0]`作为`Collection.toArray()`方法的参数现在使用更好，`new String[0]`就是起一个模板的作用，指定了返回数组的类型，0 是为了节省空间，因为它只是为了说明返回的类型。详见：https://shipilev.net/blog/2016/arrays-wisdom-ancients/

## 使用 Arrays.asList 有什么注意点？

《阿里巴巴 Java 开发手册》的描述如下：

> **使用工具类 `Arrays.asList()` 把数组转换成集合时，不能使用其修改集合相关的方法， 它的 `add/remove/clear` 方法会抛出 `UnsupportedOperationException` 异常。**

::: info 不能直接使用 Arrays.asList 来转换基本类型数组

:::

```java
// ❌ 错误：基本类型数组会被视为单个元素
int[] intArray = {1, 2, 3};
List<int[]> wrongList = Arrays.asList(intArray);  // List<int[]> 不是 List<Integer>
System.out.println(wrongList.size());  // 输出 1（整个数组作为一个元素）

// ✔️ 正确：使用包装类型或流
Integer[] integerArray = {1, 2, 3};
List<Integer> correctList = Arrays.asList(integerArray);  // 正常：3个元素

// ✔️ Java 8+ 替代方案
List<Integer> streamList = Arrays.stream(intArray)
                                  .boxed()
                                  .collect(Collectors.toList());
```

::: info 使用集合的修改方法：add()、remove()、clear()会抛出异常

:::

Arrays.asList 返回的 List 并不是我们期望的 java.util.ArrayList，而是 Arrays 的内部类。

这个内部类继承自 AbstractList 类，但没有覆写父类的 add、remove、clear 方法，而父类中的这几个方法默认会抛出 UnsupportedOperationException。

```java
// ❌ 不是真正的 ArrayList
List<String> list = Arrays.asList("A", "B", "C");
list.add("D");  // 抛出 UnsupportedOperationException
list.remove(0); // 同样抛出异常
```

正确做法是：

【示例】使用 `new ArrayList<>(Arrays.asList(...))`

```java
// ✔️ 正确模式：创建真正的可变列表
List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));
list.add("D");  // 正常执行
```

【示例】使用 Java8 的 Stream

```
// ✔️ 正确模式（Java8+）：Stream
Integer [] myArray = { 1, 2, 3 };
List myList = Arrays.stream(myArray).collect(Collectors.toList());
// 基本类型也可以实现转换（依赖 boxed 的装箱操作）
int [] myArray2 = { 1, 2, 3 };
List myList = Arrays.stream(myArray2).boxed().collect(Collectors.toList());
```

【示例】使用 Lists.newArrayList

对于可变集合，你可以使用 [`Lists`](https://github.com/google/guava/blob/master/guava/src/com/google/common/collect/Lists.java) 类及其 [`newArrayList()`](https://github.com/google/guava/blob/master/guava/src/com/google/common/collect/Lists.java#L87) 工厂方法：

```java
List<String> l1 = Lists.newArrayList(anotherListOrCollection);    // from collection
List<String> l2 = Lists.newArrayList(aStringArray);               // from array
List<String> l3 = Lists.newArrayList("or", "string", "elements"); // from varargs
```

【示例】使用 Java9 的 `List.of()`方法

```java
Integer[] array = {1, 2, 3};
List<Integer> list = List.of(array);
```

【示例】使用 Guava

对于不可变集合，你可以使用 [`ImmutableList`](https://github.com/google/guava/blob/master/guava/src/com/google/common/collect/ImmutableList.java) 类及其 [`of()`](https://github.com/google/guava/blob/master/guava/src/com/google/common/collect/ImmutableList.java#L101) 与 [`copyOf()`](https://github.com/google/guava/blob/master/guava/src/com/google/common/collect/ImmutableList.java#L225) 工厂方法：（参数不能为空）

```java
List<String> il = ImmutableList.of("string", "elements");  // from varargs
List<String> il = ImmutableList.copyOf(aStringArray);      // from array
```

【示例】使用 Apache Commons Collections

```java
List<String> list = new ArrayList<String>();
CollectionUtils.addAll(list, str);
```

## 使用 List.subList 有什么注意点？

::: info List.subList 使用陷阱

:::

`List.subList` 返回的子 List 不是一个普通的 ArrayList，即**不是副本，是视图**。

子 List 和原 List 共享底层数据，会和原始 List 相互影响。如果不注意，很可能会因此产生 OOM 问题。

```java
List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
List<String> sub = list.subList(1, 3);  // [B, C]
// ❌ 常见误解：创建了独立副本
// ✔️ 实际：sub 是 list 的"视图窗口"，共享底层数据
```

【示例】结构修改异常（ConcurrentModificationException）

```java
List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));
List<String> sub = list.subList(0, 2);

list.add("D");  // 🔴 修改原列表结构
System.out.println(sub.get(0));  // 立即抛出 ConcurrentModificationException
```

【示例】作用范围陷阱

```java
List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
List<String> sub = list.subList(1, 3);  // sub: [B, C]

sub.add("X");
// sub: [B, C, X]
// list: [A, B, C, X, D]

sub.remove(0);
// sub: [C, X]
// list: [A, C, X, D]
```

::: info List.subList 正确使用模式

:::

```java
// 需要长期持有或独立修改
List<String> independentCopy = new ArrayList<>(list.subList(100, 200));

// 或使用流（Java 8+）
List<String> streamCopy = list.stream()
                               .skip(100)
                               .limit(100)
                               .collect(Collectors.toList());
```

