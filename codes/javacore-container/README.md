# JavaCore :: Container — Java 容器示例

> 本模块展示 Java 集合框架（Collection Framework）的用法：List、Set、Map、Queue 四大类容器，以及迭代器、排序等通用机制。
>
> 示例源码配合 [Java 容器教程](https://github.com/dunwu/javacore/tree/master/docs/01.Java/01.JavaSE/03.容器)。每个示例类的 `main` 逻辑抽取为独立的 `demo()` 方法，并在 `src/test` 下配套 JUnit 5 单元测试验证输出。

示例源码路径：`src/main/java/io/github/dunwu/javacore/container/<特性包>/`

---

## 迭代与遍历（base）

展示容器的通用遍历方式与 fail-fast 机制。

- `base/IteratorDemo`、`IteratorDemo2`、`IteratorDemo3` — 使用 `Iterator` 遍历集合、迭代中删除元素。
- `base/ForeachDemo01`、`ForeachDemo02` — 增强 for 循环遍历集合。
- `base/FailFastDemo`（反例） — 遍历中直接修改集合触发 `ConcurrentModificationException`（fail-fast）。
- `base/User` — 供遍历示例使用的实体类。

## List 列表（list）

展示 ArrayList、LinkedList、Vector 的增删改查与 `Collections` 工具方法。

- `list/ArrayListDemo01`~`ArrayListDemo05` — ArrayList 的创建、增删、遍历、扩容、常用操作。
- `list/LinkedListDemo01`~`LinkedListDemo03` — LinkedList 的链表操作、双端队列用法。
- `list/VectorDemo` — Vector 的基本用法（线程安全的遗留容器）。
- `list/ArraysAsListDemo` — `Arrays.asList` 返回固定长度视图的坑（不可 add/remove）。
- `list/ListSubListDemo` — `subList` 返回原列表视图，修改互相影响。
- `list/CollectionsDemo01`~`CollectionsDemo07` — `Collections` 工具类：排序、查找、填充、同步、不可变包装等。
- `list/CollectionDemo`、`CollectionDemo2` — Collection 接口通用方法演示。

## Set 集合（set）

展示无序去重（HashSet）与有序（TreeSet）集合。

- `set/HashSetDemo01` — HashSet 基于 hashCode 去重（需正确重写 equals/hashCode）。
- `set/TreeSetDemo`、`set/TreeSetDemo2` — TreeSet 自然排序与自定义 `Comparator` 排序。

## Map 映射（map）

展示 HashMap、TreeMap、Hashtable、WeakHashMap 等键值容器的用法与原理。

- `map/HashMapDemo01`~`HashMapDemo08` — HashMap 的存取、遍历、扩容、hash 扰动、以及键未重写 hashCode 导致的问题等。
- `map/TreeMapDemo`、`map/TreeMapDemo01`、`map/SortedMapDemo` — TreeMap 的排序特性与 SortedMap 接口。
- `map/HashtableDemo01` — Hashtable 的用法（线程安全的遗留容器）。
- `map/WeakHashMapDemo01` — WeakHashMap 的弱引用键与自动回收。
- `container/IdentityHashMapDemo01`、`IdentityHashMapDemo02` — IdentityHashMap 以引用相等（`==`）判定键。

## Queue 与栈（queue）

展示队列与栈结构的操作。

- `queue/LinkedListQueueDemo` — 用 LinkedList 实现 FIFO 队列（offer/poll/peek）。
- `queue/StackDemo` — 栈的 LIFO 操作（push/pop/peek）。
- `container/EnumerationDemo01` — 使用 `Enumeration` 遍历遗留容器。
- `container/ListIteratorDemo` — `ListIterator` 双向遍历列表。

## 排序（sort）

展示对象排序的两种方式。

- `sort/ComparableDemo` — 实现 `Comparable` 接口的自然排序（compareTo）。
- `sort/ComparatorDemo` — 使用 `Comparator` 的外部定制排序。

## 实体 Bean（bean）

供容器示例使用的数据对象：`bean/Person`、`bean/Student`、`bean/Course`、`bean/School`、`bean/Countries`。

---

## 单元测试

测试位于 `src/test/java/io/github/dunwu/javacore/container/`，按 List / Map / Set / Queue / Sort 等特性分类，通过捕获标准输出对示例结果做精确断言。运行：

```bash
mvn test -pl codes/javacore-container
```

> 反例（如 fail-fast）用 `assertThatThrownBy` 验证其抛出预期异常。所有 `@Test` 方法均带有中文 `@DisplayName` 说明测试意图。
