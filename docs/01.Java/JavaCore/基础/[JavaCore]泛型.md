---
title: 深入理解 Java 泛型
date: 2020-10-17 19:13:25
order: 09
categories:
  - Java
  - JavaCore
  - 基础
tags:
  - Java
  - JavaCore
  - 泛型
permalink: /pages/1c5e74da/
---

# 深入理解 Java 泛型

## 什么是泛型

**Java 泛型（generics）是 JDK 5 中引入的特性**。

为什么要引入泛型机制呢？

回答这个问题前，先让我们来看一个示例。

```java
public class NoGenericsDemo {
    public static void main(String[] args) {
        List list = new ArrayList<>();
        list.add("abc");
        list.add(18);
        list.add(new double[] {1.0, 2.0});
        Object obj1 = list.get(0);
        Object obj2 = list.get(1);
        Object obj3 = list.get(2);
        System.out.println("obj1 = [" + obj1 + "]");
        System.out.println("obj2 = [" + obj2 + "]");
        System.out.println("obj3 = [" + obj3 + "]");

        int num1 = (int)list.get(0);
        int num2 = (int)list.get(1);
        int num3 = (int)list.get(2);
        System.out.println("num1 = [" + num1 + "]");
        System.out.println("num2 = [" + num2 + "]");
        System.out.println("num3 = [" + num3 + "]");
    }
}
// Output:
// obj1 = [abc]
// obj2 = [18]
// obj3 = [[D@47089e5f]
// Exception in thread "main" java.lang.ClassCastException: java.lang.String cannot be cast to java.lang.Integer
// at io.github.dunwu.javacore.generics.NoGenericsDemo.main(NoGenericsDemo.java:23)
```

示例说明：

在上面的示例中，`List` 容器没有指定存储数据类型，这种情况下，可以向 `List` 添加任意类型数据，编译器不会做类型检查，而是默默的将所有数据都转为 `Object`。

假设，最初我们希望向 `List` 存储的是整形数据，假设，某个家伙不小心存入了其他数据类型。当你试图从容器中取整形数据时，由于 `List` 当成 `Object` 类型来存储，你不得不使用类型强制转换。在运行时，才会发现 `List` 中数据不存储一致的问题，这就为程序运行带来了很大的风险（无形伤害最为致命）。

引入泛型机制，正是为了解决这种类型安全问题。**“泛型”提供了编译时类型安全检测机制，该机制会在编译时检测到非法的类型**。

泛型具有以下**优点**：

- **编译时的强类型检查** - 泛型要求在声明时指定实际数据类型，Java 编译器在编译时会对泛型代码做强类型检查，并在代码违反类型安全时发出告警。早发现，早治理，把隐患扼杀于摇篮，在编译时发现并修复错误所付出的代价远比在运行时小。

- **避免了类型转换**

未使用泛型：

```java
List list = new ArrayList();
list.add("hello");
String s = (String) list.get(0);
```

使用泛型：

```java
List<String> list = new ArrayList<String>();
list.add("hello");
String s = list.get(0);   // no cast
```

- **泛型编程可以实现通用算法** - 通过使用泛型，程序员可以实现通用算法，这些算法可以处理不同类型的集合，可以自定义，并且类型安全且易于阅读。

## 泛型声明

**“泛型类型”是被参数化的类或接口。**

### 泛型类

泛型类的语法形式：

```java
class name<T1, T2, ..., Tn> { /* ... */ }
```

泛型类的声明和非泛型类的声明类似，除了在类名后面添加了类型参数声明部分。由尖括号（`<>`）分隔的类型参数部分跟在类名后面。它指定类型参数（也称为类型变量）T1，T2，...和 Tn。

一般将泛型中的类名称为**原型**，而将 `<>` 指定的参数称为**类型参数**。

- 未应用泛型的类

在泛型出现之前，如果一个类想持有一个可以为任意类型的数据，只能使用 `Object` 做类型转换。示例如下：

```java
public class Info {
	private Object value;

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
```

- 单类型参数的泛型类

```java
public class Info<T> {
    private T value;

    public Info() { }

    public Info(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Info{" + "value=" + value + '}';
    }
}

public class GenericsClassDemo01 {
    public static void main(String[] args) {
        Info<Integer> info = new Info<>();
        info.setValue(10);
        System.out.println(info.getValue());

        Info<String> info2 = new Info<>();
        info2.setValue("xyz");
        System.out.println(info2.getValue());
    }
}
// Output:
// 10
// xyz
```

在上面的例子中，在初始化一个泛型类时，使用 `<>` 指定了内部具体类型，在编译时就会根据这个类型做强类型检查。

实际上，不使用 `<>` 指定内部具体类型，语法上也是支持的（不推荐这么做），如下所示：

```java
public static void main(String[] args) {
    Info info = new Info();
    info.setValue(10);
    System.out.println(info.getValue());
    info.setValue("abc");
    System.out.println(info.getValue());
}
```

> 示例说明：
>
> 上面的例子，不会产生编译错误，也能正常运行。但这样的调用就失去泛型类型的优势。

- 多个类型参数的泛型类

```java
public class MyMap<K,V> {
    private K key;
    private V value;

    public MyMap(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "MyMap{" + "key=" + key + ", value=" + value + '}';
    }
}

public class GenericsClassDemo02 {
    public static void main(String[] args) {
        MyMap<Integer, String> map = new MyMap<>(1, "one");
        System.out.println(map);
    }
}
// Output:
// MyMap{key=1, value=one}
```

- 泛型类的类型嵌套

```java
public class GenericsClassDemo03 {
    public static void main(String[] args) {
        Info<String> info = new Info("Hello");
        MyMap<Integer, Info<String>> map = new MyMap<>(1, info);
        System.out.println(map);
    }
}
// Output:
// MyMap{key=1, value=Info{value=Hello}}
```

### 泛型接口

接口也可以声明泛型。

泛型接口语法形式：

```java
public interface Content<T> {
    T text();
}
```

泛型接口有两种实现方式：

- 实现接口的子类明确声明泛型类型

```java


public class GenericsInterfaceDemo01 implements Content<Integer> {
    private int text;

    public GenericsInterfaceDemo01(int text) {
        this.text = text;
    }

    @Override
    public Integer text() { return text; }

    public static void main(String[] args) {
        GenericsInterfaceDemo01 demo = new GenericsInterfaceDemo01(10);
        System.out.print(demo.text());
    }
}
// Output:
// 10
```

- 实现接口的子类不明确声明泛型类型

```java
public class GenericsInterfaceDemo02<T> implements Content<T> {
    private T text;

    public GenericsInterfaceDemo02(T text) {
        this.text = text;
    }

    @Override
    public T text() { return text; }

    public static void main(String[] args) {
        GenericsInterfaceDemo02<String> gen = new GenericsInterfaceDemo02<>("ABC");
        System.out.print(gen.text());
    }
}
// Output:
// ABC
```

### 泛型方法

**泛型方法是引入其自己的类型参数的方法**。泛型方法可以是普通方法、静态方法以及构造方法。

泛型方法语法形式如下：

```java
public <T> T func(T obj) {}
```

**是否拥有泛型方法，与其所在的类是否是泛型没有关系。**

泛型方法的语法包括一个类型参数列表，在尖括号内，它出现在方法的返回类型之前。对于静态泛型方法，类型参数部分必须出现在方法的返回类型之前。类型参数能被用来声明返回值类型，并且能作为泛型方法得到的实际类型参数的占位符。

**使用泛型方法的时候，通常不必指明类型参数，因为编译器会为我们找出具体的类型，这称为“类型参数推断（type argument inference）”**。类型推断只对赋值操作有效，其他时候并不起作用。如果将一个返回类型为 `T` 的泛型方法调用的结果作为参数，传递给另一个方法，这时编译器并不会执行推断。编译器会认为：调用泛型方法后，其返回值被赋给一个 Object 类型的变量。

```java
public class GenericsMethodDemo01 {
    public static <T> void printClass(T obj) {
        System.out.println(obj.getClass().toString());
    }

    public static void main(String[] args) {
        printClass("abc");
        printClass(10);
    }
}
// Output:
// class java.lang.String
// class java.lang.Integer
```

泛型方法中也可以使用可变参数列表。示例如下：

```java
public class GenericVarargsMethodDemo {
    public static <T> List<T> makeList(T... args) {
        List<T> result = new ArrayList<T>();
        Collections.addAll(result, args);
        return result;
    }

    public static void main(String[] args) {
        List<String> ls = makeList("A");
        System.out.println(ls);
        ls = makeList("A", "B", "C");
        System.out.println(ls);
    }
}
// Output:
// [A]
// [A, B, C]
```

## 泛型要点

### 类型擦除

Java 语言引入泛型是为了在编译时提供更严格的类型检查，并支持泛型编程。不同于 C++ 的模板机制，**Java 泛型是使用“类型擦除”来实现的，使用泛型时，任何具体的类型信息都被擦除了**。

那么，类型擦除做了什么呢？它做了以下工作：

- 把泛型中的所有类型参数替换为 `Object`，如果指定类型边界，则使用类型边界来替换。因此，生成的字节码仅包含普通的类，接口和方法。
- 擦除出现的类型声明，即去掉 `<>` 的内容。比如 `T get()` 方法声明就变成了 `Object get()` ；`List<String>` 就变成了 `List`。如有必要，插入类型转换以保持类型安全。
- 生成桥接方法以保留扩展泛型类型中的多态性。类型擦除确保不为参数化类型创建新类；因此，泛型不会产生运行时开销。

让我们来看一个示例：

```java
public class GenericsErasureTypeDemo {
    public static void main(String[] args) {
        List<Object> list1 = new ArrayList<Object>();
        List<String> list2 = new ArrayList<String>();
        System.out.println(list1.getClass());
        System.out.println(list2.getClass());
    }
}
// Output:
// class java.util.ArrayList
// class java.util.ArrayList
```

示例说明：

上面的例子中，虽然指定了不同的类型参数，但是 list1 和 list2 的类信息却是一样的。

这是因为：**使用泛型时，任何具体的类型信息都被擦除了**。这意味着：`ArrayList<Object>` 和 `ArrayList<String>` 在运行时，JVM 将它们视为同一类型。

Java 泛型的实现方式不太优雅，但这是因为泛型是在 JDK5 时引入的，为了兼容老代码，必须在设计上做一定的折中。

### 泛型和继承

**泛型不能用于显式地引用运行时类型的操作之中**（例如：转型、`instanceof` 操作和 `new` 表达式），因为所有关于参数的类型信息都丢失了。当你在编写泛型代码时，必须时刻提醒自己，你只是看起来好像拥有有关参数的类型信息而已。

正是由于泛型时基于类型擦除实现的，所以，**泛型类型无法向上转型**。

> 向上转型是指用子类实例去初始化父类，这是面向对象中多态的重要表现。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2019/03/381b5d53ecd945e480f87a3076547955.png)

`Integer` 继承了 `Object`；`ArrayList` 继承了 `List`；但是 `List<Interger>` 却并非继承了 `List<Object>`。

这是因为，泛型类并没有自己独有的 `Class` 类对象。比如：并不存在 `List<Object>.class` 或是 `List<Interger>.class`，Java 编译器会将二者都视为 `List.class`。

```java
List<Integer> list = new ArrayList<>();
List<Object> list2 = list; // Erorr
```

### 类型边界

有时您可能希望限制可在参数化类型中用作类型参数的类型。**“类型边界”可以对泛型的类型参数设置限制条件**。例如，对数字进行操作的方法可能只想接受 `Number` 或其子类的实例。

要声明有界类型参数，请列出类型参数的名称，然后是 `extends` 关键字，后跟其限制类或接口。

类型边界的语法形式如下：

```
<T extends XXX>
```

示例：

```java
public class GenericsExtendsDemo01 {
    static <T extends Comparable<T>> T max(T x, T y, T z) {
        T max = x; // 假设x是初始最大值
        if (y.compareTo(max) > 0) {
            max = y; //y 更大
        }
        if (z.compareTo(max) > 0) {
            max = z; // 现在 z 更大
        }
        return max; // 返回最大对象
    }

    public static void main(String[] args) {
        System.out.println(max(3, 4, 5));
        System.out.println(max(6.6, 8.8, 7.7));
        System.out.println(max("pear", "apple", "orange"));
    }
}
// Output:
// 5
// 8.8
// pear
```

> 示例说明：
>
> 上面的示例声明了一个泛型方法，类型参数 `T extends Comparable<T>` 表明传入方法中的类型必须实现了 Comparable 接口。

类型边界可以设置多个，语法形式如下：

```
<T extends B1 & B2 & B3>
```

> 🔔 注意：extends 关键字后面的第一个类型参数可以是类或接口，其他类型参数只能是接口。

示例：

```java
public class GenericsExtendsDemo02 {
    static class A { /* ... */ }
    interface B { /* ... */ }
    interface C { /* ... */ }
    static class D1 <T extends A & B & C> { /* ... */ }
    static class D2 <T extends B & A & C> { /* ... */ } // 编译报错
    static class E extends A implements B, C { /* ... */ }

    public static void main(String[] args) {
        D1<E> demo1 = new D1<>();
        System.out.println(demo1.getClass().toString());
        D1<String> demo2 = new D1<>(); // 编译报错
    }
}
```

## 类型通配符

**“类型通配符”一般使用 `?` 代替具体的类型参数**。例如 `List<?>` 在逻辑上是 `List<String>` ，`List<Integer>` 等所有 `List<具体类型实参>` 的父类。

### 上界通配符

可以使用“上界通配符”来缩小类型参数的类型范围。

它的语法形式为：`<? extends Number>`

```java
public class GenericsUpperBoundedWildcardDemo {
    public static double sumOfList(List<? extends Number> list) {
        double s = 0.0;
        for (Number n : list) {
            s += n.doubleValue();
        }
        return s;
    }

    public static void main(String[] args) {
        List<Integer> li = Arrays.asList(1, 2, 3);
        System.out.println("sum = " + sumOfList(li));
    }
}
// Output:
// sum = 6.0
```

### 下界通配符

**“下界通配符”将未知类型限制为该类型的特定类型或超类类型**。

> 🔔 注意：**上界通配符和下界通配符不能同时使用**。

它的语法形式为：`<? super Number>`

```java
public class GenericsLowerBoundedWildcardDemo {
    public static void addNumbers(List<? super Integer> list) {
        for (int i = 1; i <= 5; i++) {
            list.add(i);
        }
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        addNumbers(list);
        System.out.println(Arrays.deepToString(list.toArray()));
    }
}
// Output:
// [1, 2, 3, 4, 5]
```

### 无界通配符

无界通配符有两种应用场景：

- 可以使用 Object 类中提供的功能来实现的方法。
- 使用不依赖于类型参数的泛型类中的方法。

语法形式：`<?>`

```java
public class GenericsUnboundedWildcardDemo {
    public static void printList(List<?> list) {
        for (Object elem : list) {
            System.out.print(elem + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        List<Integer> li = Arrays.asList(1, 2, 3);
        List<String> ls = Arrays.asList("one", "two", "three");
        printList(li);
        printList(ls);
    }
}
// Output:
// 1 2 3
// one two three
```

### 通配符和向上转型

前面，我们提到：**泛型不能直接向上转型；但是，我们可以通过使用通配符来间接向上转型**。

```java
public class GenericsWildcardDemo {
    public static void main(String[] args) {
        List<Integer> intList = new ArrayList<>();
        List<Number> numList = intList;  // Error

        List<? extends Integer> intList2 = new ArrayList<>();
        List<? extends Number> numList2 = intList2;  // OK
    }
}
```

> 扩展阅读：[Oracle 泛型文档](https://docs.oracle.com/javase/tutorial/java/generics/index.html)

## 泛型的约束

- [泛型类型的类型参数不能是值类型](https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html#instantiate)

```java
Pair<int, char> p = new Pair<>(8, 'a');  // 编译错误
```

- [不能创建类型参数的实例](https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html#createObjects)

```java
public static <E> void append(List<E> list) {
    E elem = new E();  // 编译错误
    list.add(elem);
}
```

- [不能声明类型为类型参数的静态成员](https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html#createStatic)

```java
public class MobileDevice<T> {
    private static T os; // error

    // ...
}
```

- [类型参数不能使用类型转换或 `instanceof`](https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html#cannotCast)

```java
public static <E> void rtti(List<E> list) {
    if (list instanceof ArrayList<Integer>) {  // 编译错误
        // ...
    }
}
```

```java
List<Integer> li = new ArrayList<>();
List<Number>  ln = (List<Number>) li;  // 编译错误
```

- [不能创建类型参数的数组](https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html#createArrays)

```java
List<Integer>[] arrayOfLists = new List<Integer>[2];  // 编译错误
```

- [不能创建、catch 或 throw 参数化类型对象](https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html#cannotCatch)

```java
// Extends Throwable indirectly
class MathException<T> extends Exception { /* ... */ }    // 编译错误

// Extends Throwable directly
class QueueFullException<T> extends Throwable { /* ... */ // 编译错误
```

```java
public static <T extends Exception, J> void execute(List<J> jobs) {
    try {
        for (J job : jobs)
            // ...
    } catch (T e) {   // compile-time error
        // ...
    }
}
```

- [仅仅是泛型类相同，而类型参数不同的方法不能重载](https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html#cannotOverload)

```java
public class Example {
    public void print(Set<String> strSet) { }
    public void print(Set<Integer> intSet) { } // 编译错误
}
```

## 典型应用场景

### 场景一：通用容器类

使用泛型实现类型安全的通用容器：

```java
// 通用响应包装器
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = 0;
        result.message = "success";
        result.data = data;
        return result;
    }

    public static <T> Result<T> fail(String message) {
        Result<T> result = new Result<>();
        result.code = -1;
        result.message = message;
        return result;
    }
}
```

### 场景二：通用工具方法

使用泛型方法实现通用的比较、转换等操作：

```java
// 通用比较方法
public static <T extends Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) >= 0 ? a : b;
}

// 通用集合转换
public static <T, R> List<R> map(List<T> list, Function<T, R> mapper) {
    List<R> result = new ArrayList<>(list.size());
    for (T item : list) {
        result.add(mapper.apply(item));
    }
    return result;
}
```

### 场景三：类型安全的 Builder 模式

结合泛型实现类型安全的 Builder 模式：

```java
public class QueryBuilder<T> {
    private Class<T> entityType;
    private List<Condition> conditions = new ArrayList<>();

    public QueryBuilder(Class<T> entityType) {
        this.entityType = entityType;
    }

    public QueryBuilder<T> where(String field, Object value) {
        conditions.add(new Condition(field, value));
        return this;
    }

    public List<T> execute() {
        // 根据条件查询并返回类型安全的结果
    }
}

// 使用
List<User> users = new QueryBuilder<>(User.class)
    .where("age", 25)
    .where("status", "ACTIVE")
    .execute();
```

## 常见问题

### Q1：为什么泛型不能用于基本类型？

因为 Java 泛型通过类型擦除实现，在运行时泛型参数都被替换为 Object。而基本类型不是 Object 的子类，所以不能直接用于泛型。Java 提供了包装类（如 Integer、Double）作为解决方案。Valhalla 项目（未来 Java 版本）可能会解决这个问题。

### Q2：List<?> 和 List\<Object\> 有什么区别？

- `List<?>`（无界通配符）：表示“任意类型的 List”，是只读的，不能向其中添加元素（除了 null）。它是所有 `List<T>` 的父类型。
- `List<Object>`：表示“元素类型是 Object 的 List”，可以向其中添加任何对象。但 `List<String>` 不是 `List<Object>` 的子类型。

### Q3：什么是 PECS 原则？

PECS 是 "Producer Extends, Consumer Super" 的缩写，是泛型通配符使用的指导原则：
- 如果参数是“生产者”（只读取），使用 `<? extends T>`
- 如果参数是“消费者”（只写入），使用 `<? super T>`
- 如果既读又写，不使用通配符

## 泛型最佳实践

### 泛型命名

Java 泛型有一些约定俗成的命名：

- `E` - Element
- `K` - Key
- `N` - Number
- `T` - Type
- `V` - Value
- `S`、`U`、`V` - 2nd, 3rd, 4th types

### 使用泛型的建议

- 消除类型检查告警
- List 优先于数组
- 优先考虑使用泛型来提高代码通用性
- 优先考虑泛型方法来限定泛型的范围
- 利用有限制通配符来提升 API 的灵活性
- 优先考虑类型安全的异构容器

## 参考资料

- [Java 编程思想](https://book.douban.com/subject/2130190/)
- [Java 核心技术（卷 1）](https://book.douban.com/subject/3146174/)
- [Effective java](https://book.douban.com/subject/3360807/)
- [Oracle 泛型文档](https://docs.oracle.com/javase/tutorial/java/generics/index.html)
- [Java 泛型详解](https://juejin.im/post/584d36f161ff4b006cccdb82)
