---
title: 深入理解 Java 基本数据类型
date: 2019-05-06 15:02:02
order: 02
categories:
  - Java
  - JavaCore
  - 基础
tags:
  - Java
  - JavaCore
  - 数据类型
permalink: /pages/cba76603/
---

# 深入理解 Java 基本数据类型

## 简介

Java 是一门强类型语言，每个变量和表达式都有一个明确的数据类型。Java 的数据类型体系是语言的基础，决定了数据在内存中的存储方式、取值范围以及可执行的操作。深入理解数据类型及其转换机制，是避免运行时错误和性能问题的关键。

Java 中的数据类型分为两大类：

- **值类型（基本数据类型）**：直接存储数值，存储在栈中，包括 8 种基本类型。
- **引用类型**：存储对象的内存地址引用，包括类、接口、数组、String 等。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2022/04/2870d86f26494e0f884ef53d67c7f660.png)

## 数据类型分类

Java 中的数据类型有两类：

- 值类型（又叫内置数据类型，基本数据类型）
- 引用类型（除值类型以外，都是引用类型，包括 `String`、数组等）

### 值类型

Java 语言提供了 **8** 种基本类型，大致分为 **4** 类：布尔型、字符型、整数型、浮点型。

| 基本数据类型 | 分类       | 大小   | 默认值    | 取值范围                                | 包装类    | 说明                                        |
| ------------ | ---------- | ------ | --------- | --------------------------------------- | --------- | ------------------------------------------- |
| `boolean`    | **布尔型** | -      | `false`   | false、true                             | Boolean   | `boolean` 的大小，是由具体的JVM实现来决定的 |
| `char`       | **字符型** | 16 bit | `'u0000'` | 0 ~ 65535($2^{16} - 1$)                 | Character | 存储 Unicode 码，用单引号赋值               |
| `byte`       | **整数型** | 8 bit  | `0`       | -128(-$2^7$) ~ 127($2^7 - 1$)           | Byte      |                                             |
| `short`      | **整数型** | 16 bit | `0`       | -32768(-$2^{15}$) ~ 32767($2^{15} - 1$) | Short     |                                             |
| `int`        | **整数型** | 32 bit | `0`       | -$2^{31}$ ~ $2^{31} - 1$                | Integer   |                                             |
| `long`       | **整数型** | 64 bit | `0L`      | -$2^{63}$ ~ $2^{63} - 1$                | Long      | 赋值时一般在数字后加上 `l` 或 `L`           |
| `float`      | **浮点型** | 32 bit | `0.0f`    | 1.4e-45f ~ 3.4028235e+38f               | Float     | 赋值时必须在数字后加上 `f` 或 `F`           |
| `double`     | **浮点型** | 64 bit | `0.0d`    | 4.9e-324 ~ 1.7976931348623157e+308      | Double    | 赋值时一般在数字后加 `d` 或 `D`             |

`byte`、`short`、`int`、`long` 的最高比特位都用于表示正负（0 为正，-1 为负）。

### 值类型和引用类型的区别

|          | 值类型                                                       | 引用类型                                                |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------- |
| 用途     | 一般用于常量和局部变量；不可用于泛型                         | 可用于泛型                                              |
| 存储方式 | 值类型的局部变量存放在 JVM 中的局部变量表中；值类型的成员变量（未被 `static` 修饰 ）存放在 JVM 中堆中 | 几乎所有引用类型的对象实例都存在于堆中                  |
| 默认值   | 有默认值且不为 `null`                                        | 默认值是 `null`                                         |
| 比较方式 | `==` 比较的是值                                              | `==` 比较的是对象的内存地址；使用 `equals()` 才是比较值 |

> 为什么说几乎所有引用类型的对象实例都存在于堆中？
>
> 因为 HotSpot 虚拟机引入了 JIT 优化之后，会对对象进行逃逸分析：如果发现某一个对象并没有逃逸到方法外部，那么就可能通过标量替换来实现栈上分配，而避免堆上分配内存。

> 👉 扩展阅读：[Java 基本数据类型和引用类型](https://juejin.im/post/59cd71835188255d3448faf6)

## 装箱和拆箱

### 包装类、装箱、拆箱

Java 中为每一种基本数据类型提供了相应的包装类，如下：

```
Byte <-> byte
Short <-> short
Integer <-> int
Long <-> long
Float <-> float
Double <-> double
Character <-> char
Boolean <-> boolean
```

**引入包装类的目的**就是：提供一种机制，使得**基本数据类型可以与引用类型互相转换**。

基本数据类型与包装类的转换被称为装箱和拆箱。

- **装箱（boxing）是将值类型转换为引用类型**。例如：`int` 转 `Integer`
  - 装箱过程是通过调用包装类的 `valueOf` 方法实现的。
- **拆箱（unboxing）是将引用类型转换为值类型**。例如：`Integer` 转 `int`
  - 拆箱过程是通过调用包装类的 `xxxValue` 方法实现的。（xxx 代表对应的基本数据类型）。

【示例】装箱、拆箱示例

```java
Integer i1 = 10; // 自动装箱
Integer i2 = new Integer(10); // 非自动装箱
Integer i3 = Integer.valueOf(10); // 非自动装箱
int i4 = new Integer(10); // 自动拆箱
int i5 = i2.intValue(); // 非自动拆箱
System.out.println("i1 = [" + i1 + "]");
System.out.println("i2 = [" + i2 + "]");
System.out.println("i3 = [" + i3 + "]");
System.out.println("i4 = [" + i4 + "]");
System.out.println("i5 = [" + i5 + "]");
System.out.println("i1 == i2 is [" + (i1 == i2) + "]");
System.out.println("i1 == i4 is [" + (i1 == i4) + "]"); // 自动拆箱
// Output:
// i1 = [10]
// i2 = [10]
// i3 = [10]
// i4 = [10]
// i5 = [10]
// i1 == i2 is [false]
// i1 == i4 is [true]
```

【说明】

上面的例子，虽然简单，但却隐藏了自动装箱、拆箱和非自动装箱、拆箱的应用。从例子中可以看到，明明所有变量都初始化为数值 10 了，但为何会出现 `i1 == i2 is [false]` 而 `i1 == i4 is [true]` ？

原因在于：

- i1、i2 都是包装类，使用 `==` 时，Java 将它们当做两个对象，而非两个 int 值来比较，所以两个对象自然是不相等的。正确的比较操作应该使用 `equals` 方法。
- i1 是包装类，i4 是基础数据类型，使用 `==` 时，Java 会将两个 i1 这个包装类对象自动拆箱为一个 `int` 值，再代入到 `==` 运算表达式中计算；最终，相当于两个 `int` 进行比较，由于值相同，所以结果相等。

## 包装类的缓存机制

Java 基本数据类型的包装类型的大部分都用到了缓存机制来提升性能。

`Byte`,`Short`,`Integer`,`Long` 这 4 种包装类默认创建了数值 **[-128，127]** 的相应类型的缓存数据，`Character` 创建了数值在 **[0,127]** 范围的缓存数据，`Boolean` 直接返回 `True` or `False`。

Long 缓存源码：

```java
public static Long valueOf(long l) {
    final int offset = 128;
    if (l >= -128 && l <= 127) { // will cache
        return LongCache.cache[(int)l + offset];
    }
    return new Long(l);
}

private static class LongCache {
    private LongCache(){}

    static final Long cache[] = new Long[-(-128) + 127 + 1];

    static {
        for(int i = 0; i < cache.length; i++)
            cache[i] = new Long(i - 128);
    }
}
```

从以上代码可知：装箱时，若数值不在包装类缓存范围内，就会创建一个新的包装类实例。由此，我们不难进一步得出以下结论：

1. 装箱操作可能会创建新对象，**频繁的装箱操作会造成不必要的内存消耗，影响性能**。
2. **基础数据类型的比较操作使用 `==`，包装类的比较操作使用 `equals` 方法**。

### 自动装箱/拆箱

JDK5 开始，支持自动装箱/拆箱功能机制。

**自动装箱/拆箱是一种简化程序代码的语法糖**，使得值类型和包装类之间的转换更加直接。

JDK 5 之前的形式：

```java
Integer i1 = new Integer(10); // 非自动装箱
```

JDK 5 之后：

```java
Integer i = 10;  // 自动装箱
int n = i;   // 自动拆箱
```

上面这两行代码对应的字节码为：

```
   L1

    LINENUMBER 8 L1

    ALOAD 0

    BIPUSH 10

    INVOKESTATIC java/lang/Integer.valueOf (I)Ljava/lang/Integer;

    PUTFIELD AutoBoxTest.i : Ljava/lang/Integer;

   L2

    LINENUMBER 9 L2

    ALOAD 0

    ALOAD 0

    GETFIELD AutoBoxTest.i : Ljava/lang/Integer;

    INVOKEVIRTUAL java/lang/Integer.intValue ()I

    PUTFIELD AutoBoxTest.n : I

    RETURN
```

从字节码示例，可以发现：

- 自动装箱过程是通过调用包装类的 `valueOf` 方法实现的。
- 自动拆箱过程是通过调用包装类的 `xxxValue` 方法实现的。

因此，

- `Integer i = 10` 等价于 `Integer i = Integer.valueOf(10)`
- `int n = i` 等价于 `int n = i.intValue()`;

Java 对于自动装箱和拆箱的设计，依赖于一种叫做享元模式的设计模式（有兴趣的朋友可以去了解一下源码，这里不对设计模式展开详述）。

> 👉 扩展阅读：[深入剖析 Java 中的装箱和拆箱](https://www.cnblogs.com/dolphin0520/p/3780005.html)
>
> 结合示例，一步步阐述装箱和拆箱原理。

## 判等问题

Java 中，通常使用 `equals` 或 `==` 进行判等操作。`equals` 是方法而 `==` 是操作符。此外，二者使用也是有区别的：

- 对**值类型**，比如 `int`、`long`，进行判等，**只能使用 `==`，比较的是字面值**。因为基本类型的值就是其数值。
- 对**引用类型**，比如 `Integer`、`Long` 和 `String`，进行判等，**需要使用 `equals` 进行内容判等**。因为引用类型的直接值是指针，使用 `==` 的话，比较的是指针，也就是两个对象在内存中的地址，即比较它们是不是同一个对象，而不是比较对象的内容。

### 包装类的判等

我们通过一个示例来深入研究一下判等问题。

【示例】包装类的判等

```java
Integer a = 127; //Integer.valueOf(127)
Integer b = 127; //Integer.valueOf(127)
log.info("\nInteger a = 127;\nInteger b = 127;\na == b ? {}", a == b);    // true

Integer c = 128; //Integer.valueOf(128)
Integer d = 128; //Integer.valueOf(128)
log.info("\nInteger c = 128;\nInteger d = 128;\nc == d ? {}", c == d);   //false
//设置-XX:AutoBoxCacheMax=1000再试试

Integer e = 127; //Integer.valueOf(127)
Integer f = new Integer(127); //new instance
log.info("\nInteger e = 127;\nInteger f = new Integer(127);\ne == f ? {}", e == f);   //false

Integer g = new Integer(127); //new instance
Integer h = new Integer(127); //new instance
log.info("\nInteger g = new Integer(127);\nInteger h = new Integer(127);\ng == h ? {}", g == h);  //false

Integer i = 128; //unbox
int j = 128;
log.info("\nInteger i = 128;\nint j = 128;\ni == j ? {}", i == j); //true
```

第一个案例中，编译器会把 Integer a = 127 转换为 Integer.valueOf(127)。查看源码可以发现，这个转换在内部其实做了缓存，使得两个 Integer 指向同一个对象，所以 == 返回 true。

```java
public static Integer valueOf(int i) {
    if (i >= IntegerCache.low && i <= IntegerCache.high)
        return IntegerCache.cache[i + (-IntegerCache.low)];
    return new Integer(i);
}
```

第二个案例中，之所以同样的代码 128 就返回 false 的原因是，默认情况下会缓存[-128,127]的数值，而 128 处于这个区间之外。设置 JVM 参数加上 -XX:AutoBoxCacheMax=1000 再试试，是不是就返回 true 了呢？

```java
private static class IntegerCache {
    static final int low = -128;
    static final int high;
    static final Integer cache[];

    static {
        // high value may be configured by property
        int h = 127;
        String integerCacheHighPropValue =
            sun.misc.VM.getSavedProperty("java.lang.Integer.IntegerCache.high");
        if (integerCacheHighPropValue != null) {
            try {
                int i = parseInt(integerCacheHighPropValue);
                i = Math.max(i, 127);
                // Maximum array size is Integer.MAX_VALUE
                h = Math.min(i, Integer.MAX_VALUE - (-low) -1);
            } catch( NumberFormatException nfe) {
                // If the property cannot be parsed into an int, ignore it.
            }
        }
        high = h;

        cache = new Integer[(high - low) + 1];
        int j = low;
        for(int k = 0; k < cache.length; k++)
            cache[k] = new Integer(j++);

        // range [-128, 127] must be interned (JLS7 5.1.7)
        assert IntegerCache.high >= 127;
    }

    private IntegerCache() {}
}
```

第三和第四个案例中，New 出来的 Integer 始终是不走缓存的新对象。比较两个新对象，或者比较一个新对象和一个来自缓存的对象，结果肯定不是相同的对象，因此返回 false。

第五个案例中，我们把装箱的 Integer 和基本类型 int 比较，前者会先拆箱再比较，比较的肯定是数值而不是引用，因此返回 true。

> 【总结】综上，我们可以得出结论：**包装类需要使用 `equals` 进行内容判等，而不能使用 `==`**。

### String 的判等

```java
String a = "1";
String b = "1";
log.info("\nString a = \"1\";\nString b = \"1\";\na == b ? {}", a == b); //true

String c = new String("2");
String d = new String("2");
log.info("\nString c = new String(\"2\");\nString d = new String(\"2\");\nc == d ? {}", c == d); //false

String e = new String("3").intern();
String f = new String("3").intern();
log.info("\nString e = new String(\"3\").intern();\nString f = new String(\"3\").intern();\ne == f ? {}", e == f); //true

String g = new String("4");
String h = new String("4");
log.info("\nString g = new String(\"4\");\nString h = new String(\"4\");\ng == h ? {}", g.equals(h)); //true
```

在 JVM 中，当代码中出现双引号形式创建字符串对象时，JVM 会先对这个字符串进行检查，如果字符串常量池中存在相同内容的字符串对象的引用，则将这个引用返回；否则，创建新的字符串对象，然后将这个引用放入字符串常量池，并返回该引用。这种机制，就是字符串驻留或池化。

第一个案例返回 true，因为 Java 的字符串驻留机制，直接使用双引号声明出来的两个 String 对象指向常量池中的相同字符串。

第二个案例，new 出来的两个 String 是不同对象，引用当然不同，所以得到 false 的结果。

第三个案例，使用 String 提供的 intern 方法也会走常量池机制，所以同样能得到 true。

第四个案例，通过 equals 对值内容判等，是正确的处理方式，当然会得到 true。

虽然使用 new 声明的字符串调用 intern 方法，也可以让字符串进行驻留，但在业务代码中滥用 intern，可能会产生性能问题。

【示例】String#intern 性能测试

```java
//-XX:+PrintStringTableStatistics
//-XX:StringTableSize=10000000
List<String> list = new ArrayList<>();
long begin = System.currentTimeMillis();
list = IntStream.rangeClosed(1, 10000000)
    .mapToObj(i -> String.valueOf(i).intern())
    .collect(Collectors.toList());
System.out.println("size:" + list.size());
System.out.println("time:" + (System.currentTimeMillis() - begin));
```

上面的示例执行时间会比较长。原因在于：字符串常量池是一个固定容量的 Map。如果容量太小（Number of
buckets=60013）、字符串太多（1000 万个字符串），那么每一个桶中的字符串数量会非常多，所以搜索起来就很慢。输出结果中的 Average bucket size=167，代表了 Map 中桶的平均长度是 167。

解决方法是：设置 JVM 参数 -XX:StringTableSize=10000000，指定更多的桶。

为了方便观察，可以在启动程序时设置 JVM 参数 -XX:+PrintStringTableStatistic，程序退出时可以打印出字符串常量表的统计信息。

执行结果比不设置 -XX:StringTableSize 要快很多。

> 【总结】**没事别轻易用 intern，如果要用一定要注意控制驻留的字符串的数量，并留意常量表的各项指标**。

### 实现 equals

如果看过 Object 类源码，你可能就知道，equals 的实现其实是比较对象引用

```java
public boolean equals(Object obj) {
    return (this == obj);
}
```

之所以 Integer 或 String 能通过 equals 实现内容判等，是因为它们都覆写了这个方法。

对于自定义类型，如果不覆写 equals 的话，默认就是使用 Object 基类的按引用的比较方式。

实现一个更好的 equals 应该注意的点：

- 考虑到性能，可以先进行指针判等，如果对象是同一个那么直接返回 true；
- 需要对另一方进行判空，空对象和自身进行比较，结果一定是 fasle；
- 需要判断两个对象的类型，如果类型都不同，那么直接返回 false；
- 确保类型相同的情况下再进行类型强制转换，然后逐一判断所有字段。

【示例】自定义 equals 示例

自定义类：

```java
class Point {
    private final int x;
    private final int y;
    private final String desc;
}
```

自定义 equals：

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Point that = (Point) o;
    return x == that.x && y == that.y;
}
```

### hashCode 和 equals 要配对实现

```java
Point p1 = new Point(1, 2, "a");
Point p2 = new Point(1, 2, "b");

HashSet<PointWrong> points = new HashSet<>();
points.add(p1);
log.info("points.contains(p2) ? {}", points.contains(p2));
```

按照改进后的 equals 方法，这 2 个对象可以认为是同一个，Set 中已经存在了 p1 就应该包含 p2，但结果却是 false。

出现这个 Bug 的原因是，散列表需要使用 hashCode 来定位元素放到哪个桶。如果自定义对象没有实现自定义的 hashCode 方法，就会使用 Object 超类的默认实现，得到的两个 hashCode 是不同的，导致无法满足需求。

要自定义 hashCode，我们可以直接使用 Objects.hash 方法来实现。

```java
@Override
public int hashCode() {
    return Objects.hash(x, y);
}
```

### compareTo 和 equals 的逻辑一致性

【示例】自定义 compareTo 出错示例

```java
@Data
@AllArgsConstructor
static class Student implements Comparable<Student> {

    private int id;
    private String name;

    @Override
    public int compareTo(Student other) {
        int result = Integer.compare(other.id, id);
        if (result == 0) { log.info("this {} == other {}", this, other); }
        return result;
    }

}
```

调用：

```java
List<Student> list = new ArrayList<>();
list.add(new Student(1, "zhang"));
list.add(new Student(2, "wang"));
Student student = new Student(2, "li");

log.info("ArrayList.indexOf");
int index1 = list.indexOf(student);
Collections.sort(list);
log.info("Collections.binarySearch");
int index2 = Collections.binarySearch(list, student);

log.info("index1 = " + index1);
log.info("index2 = " + index2);
```

binarySearch 方法内部调用了元素的 compareTo 方法进行比较；

- indexOf 的结果没问题，列表中搜索不到 id 为 2、name 是 li 的学生；
- binarySearch 返回了索引 1，代表搜索到的结果是 id 为 2，name 是 wang 的学生。

修复方式很简单，确保 compareTo 的比较逻辑和 equals 的实现一致即可。

```java
@Data
@AllArgsConstructor
static class StudentRight implements Comparable<StudentRight> {

    private int id;
    private String name;

    @Override
    public int compareTo(StudentRight other) {
        return Comparator.comparing(StudentRight::getName)
            .thenComparingInt(StudentRight::getId)
            .compare(this, other);
    }

}
```

### 小心 Lombok 生成代码的“坑”

Lombok 的 @Data 注解会帮我们实现 equals 和 hashcode 方法，但是有继承关系时，
Lombok 自动生成的方法可能就不是我们期望的了。

@EqualsAndHashCode 默认实现没有使用父类属性。为解决这个问题，我们可以手动设置 callSuper 开关为 true，来覆盖这种默认行为。

## 数据转换

Java 中，数据类型转换有两种方式：

- 自动转换
- 强制转换

### 自动转换

一般情况下，定义了某数据类型的变量，就不能再随意转换。但是 JAVA 允许用户对基本类型做**有限度**的类型转换。

如果符合以下条件，则 JAVA 将会自动做类型转换：

- **由小数据转换为大数据**

  显而易见的是，“小”数据类型的数值表示范围小于“大”数据类型的数值表示范围，即精度小于“大”数据类型。

  所以，如果“大”数据向“小”数据转换，会丢失数据精度。比如：long 转为 int，则超出 int 表示范围的数据将会丢失，导致结果的不确定性。

  反之，“小”数据向“大”数据转换，则不会存在数据丢失情况。由于这个原因，这种类型转换也称为**扩大转换**。

  这些类型由“小”到“大”分别为：(byte，short，char) < int < long < float < double。

  这里我们所说的“大”与“小”，并不是指占用字节的多少，而是指表示值的范围的大小。

- **转换前后的数据类型要兼容**

  由于 boolean 类型只能存放 true 或 false，这与整数或字符是不兼容的，因此不可以做类型转换。

- **整型类型和浮点型进行计算后，结果会转为浮点类型**

示例：

```java
long x = 30;
float y = 14.3f;
System.out.println("x/y = " + x/y);
```

输出：

```
x/y = 1.9607843
```

可见 long 虽然精度大于 float 类型，但是结果为浮点数类型。

### 强制转换

在不符合自动转换条件时或者根据用户的需要，可以对数据类型做强制的转换。

**强制转换使用括号 `()` 。**

引用类型也可以使用强制转换。

示例：

```java
float f = 25.5f;
int x = (int)f;
System.out.println("x = " + x);
```

## 丢失精度和数据溢出

### 为什么浮点数计算存在丢失精度的风险

计算机是把数值保存在了变量中，不同类型的数值变量能保存的数值范围不同，当数值超过类型能表达的数值上限则会发生溢出问题。

```java
System.out.println(0.1 + 0.2); // 0.30000000000000004
System.out.println(1.0 - 0.8); // 0.19999999999999996
System.out.println(4.015 * 100); // 401.49999999999994
System.out.println(123.3 / 100); // 1.2329999999999999
double amount1 = 2.15;
double amount2 = 1.10;
System.out.println(amount1 - amount2); // 1.0499999999999998
```

上面的几个示例，输出结果和我们预期的很不一样。为什么会是这样呢？

出现这种问题的主要原因是，计算机是以二进制存储数值的，浮点数也不例外。Java 采用了 IEEE 754 标准实现浮点数的表达和运算，你可以通过这里查看数值转化为二进制的结果。

比如，0.1 的二进制表示为 0.0 0011 0011 0011… （0011 无限循环)，再转换为十进制就是 0.1000000000000000055511151231257827021181583404541015625。对于计算机而言，0.1 无法精确表达，这是浮点数计算造成精度损失的根源。

### 如何解决浮点数丢失精度的问题

**浮点数无法精确表达和运算的场景，一定要使用 BigDecimal 类型**。

使用 BigDecimal 时，有个细节要格外注意。让我们来看一段代码：

```java
System.out.println(new BigDecimal(0.1).add(new BigDecimal(0.2)));
// Output: 0.3000000000000000166533453693773481063544750213623046875

System.out.println(new BigDecimal(1.0).subtract(new BigDecimal(0.8)));
// Output: 0.1999999999999999555910790149937383830547332763671875

System.out.println(new BigDecimal(4.015).multiply(new BigDecimal(100)));
// Output: 401.49999999999996802557689079549163579940795898437500

System.out.println(new BigDecimal(123.3).divide(new BigDecimal(100)));
// Output: 1.232999999999999971578290569595992565155029296875
```

为什么输出结果仍然不符合预期呢？

**使用 BigDecimal 表示和计算浮点数，且务必使用字符串的构造方法来初始化 BigDecimal**。

【示例】**浮点数的字符串格式化也要通过 BigDecimal 进行**。

```java
private static void wrong1() {
    double num1 = 3.35;
    float num2 = 3.35f;
    System.out.println(String.format("%.1f", num1)); // 3.4
    System.out.println(String.format("%.1f", num2)); // 3.3
}

private static void wrong2() {
    double num1 = 3.35;
    float num2 = 3.35f;
    DecimalFormat format = new DecimalFormat("#.##");
    format.setRoundingMode(RoundingMode.DOWN);
    System.out.println(format.format(num1)); // 3.35
    format.setRoundingMode(RoundingMode.DOWN);
    System.out.println(format.format(num2)); // 3.34
}

private static void right() {
    BigDecimal num1 = new BigDecimal("3.35");
    BigDecimal num2 = num1.setScale(1, BigDecimal.ROUND_DOWN);
    System.out.println(num2); // 3.3
    BigDecimal num3 = num1.setScale(1, BigDecimal.ROUND_HALF_UP);
    System.out.println(num3); // 3.4
}
```

### BigDecimal 判等问题

```java
private static void wrong() {
    System.out.println(new BigDecimal("1.0").equals(new BigDecimal("1")));
}

private static void right() {
    System.out.println(new BigDecimal("1.0").compareTo(new BigDecimal("1")) == 0);
}
```

BigDecimal 的 equals 方法的注释中说明了原因，equals 比较的是 BigDecimal 的 value 和 scale，1.0 的 scale 是 1，1 的 scale 是 0，所以结果一定是 false。

**如果我们希望只比较 BigDecimal 的 value，可以使用 compareTo 方法**。

BigDecimal 的 equals 和 hashCode 方法会同时考虑 value 和 scale，如果结合 HashSet 或 HashMap 使用的话就可能会出现麻烦。比如，我们把值为 1.0 的 BigDecimal 加入 HashSet，然后判断其是否存在值为 1 的 BigDecimal，得到的结果是 false。

```java
Set<BigDecimal> hashSet1 = new HashSet<>();
hashSet1.add(new BigDecimal("1.0"));
System.out.println(hashSet1.contains(new BigDecimal("1")));//返回false
```

解决办法有两个：

第一个方法是，使用 TreeSet 替换 HashSet。TreeSet 不使用 hashCode 方法，也不使用 equals 比较元素，而是使用 compareTo 方法，所以不会有问题。

第二个方法是，把 BigDecimal 存入 HashSet 或 HashMap 前，先使用 stripTrailingZeros 方法去掉尾部的零，比较的时候也去掉尾部的 0，确保 value 相同的 BigDecimal，scale 也是一致的。

```java
Set<BigDecimal> hashSet2 = new HashSet<>();
hashSet2.add(new BigDecimal("1.0").stripTrailingZeros());
System.out.println(hashSet2.contains(new BigDecimal("1.000").stripTrailingZeros()));//返回true

Set<BigDecimal> treeSet = new TreeSet<>();
treeSet.add(new BigDecimal("1.0"));
System.out.println(treeSet.contains(new BigDecimal("1")));//返回true
```

### 数值溢出

数值计算还有一个要小心的点是溢出，不管是 int 还是 long，所有的值类型都有超出表达范围的可能性。

```java
long l = Long.MAX_VALUE;
System.out.println(l + 1); // -9223372036854775808
System.out.println(l + 1 == Long.MIN_VALUE); // true
```

**显然这是发生了溢出，而且是默默的溢出，并没有任何异常**。这类问题非常容易被忽略，改进方式有下面 2 种。

方法一是，考虑使用 Math 类的 addExact、subtractExact 等 xxExact 方法进行数值运算，这些方法可以在数值溢出时主动抛出异常。

```java
try {
    long l = Long.MAX_VALUE;
    System.out.println(Math.addExact(l, 1));
} catch (Exception ex) {
    ex.printStackTrace();
}
```

方法二是，使用 BigInteger类。

`BigInteger` 内部使用 `int[]` 数组来存储任意大小的整形数据。

> BigDecimal 是处理浮点数的专家；而 BigInteger 则是对大数进行科学计算的专家。

```java
BigInteger i = new BigInteger(String.valueOf(Long.MAX_VALUE));
System.out.println(i.add(BigInteger.ONE).toString());

try {
    long l = i.add(BigInteger.ONE).longValueExact();
} catch (Exception ex) {
    ex.printStackTrace();
}
```

## 典型应用场景

### 场景一：金融计算

在涉及金额计算的业务场景中，必须使用 `BigDecimal` 而非 `double`/`float`，以避免精度丢失：

```java
// 订单金额计算
BigDecimal price = new BigDecimal("19.99");
BigDecimal quantity = new BigDecimal("3");
BigDecimal discount = new BigDecimal("0.95");
BigDecimal total = price.multiply(quantity).multiply(discount);
// total = 56.9715，再四舍五入保留两位小数
BigDecimal finalAmount = total.setScale(2, RoundingMode.HALF_UP);
// finalAmount = 56.97
```

### 场景二：计数器与统计

在高并发统计场景中，使用 `AtomicLong` 或 `LongAdder` 替代 `long` 包装类的自增操作：

```java
// 使用 LongAdder 实现高并发计数
LongAdder counter = new LongAdder();
// 多线程环境下安全自增
counter.increment();
counter.add(10);
long total = counter.sum();
```

### 场景三：数据传输与序列化

在网络传输和文件读写中，需要明确数据类型的大小和字节序：

```java
// 使用 byte 数组处理二进制数据
byte[] header = new byte[4];
header[0] = (byte) 0xFF;  // 魔数
header[1] = (byte) 0xD8;  // JPEG 标识

// int 转 byte 数组（大端序）
public static byte[] intToBytes(int value) {
    return new byte[] {
        (byte)(value >> 24), (byte)(value >> 16),
        (byte)(value >> 8),  (byte)(value)
    };
}
```

## 最佳实践

1. **优先使用基本数据类型**：在没有泛型需求时，优先使用 `int`、`long` 等基本类型，避免不必要的装箱/拆箱开销。
2. **金额计算用 BigDecimal**：浮点数运算存在精度丢失风险，涉及金额的场景务必使用 `BigDecimal` 并以字符串构造。
3. **包装类比较使用 equals**：`Integer`、`Long` 等包装类的比较操作必须使用 `equals()` 而非 `==`。
4. **避免频繁装箱**：装箱操作可能创建新对象，在循环或高频调用中应尽量避免。
5. **警惕数值溢出**：使用 `Math.addExact()` 等方法进行运算，可在溢出时主动抛出异常。
6. **大整数使用 BigInteger**：当数值可能超出 `long` 范围时，使用 `BigInteger` 进行科学计算。
7. **BigDecimal 判等用 compareTo**：`BigDecimal` 的 `equals` 同时比较值和精度（scale），判等应使用 `compareTo`。

## 常见问题

### Q1：为什么 `0.1 + 0.2 != 0.3`？

因为浮点数在计算机中以二进制存储，0.1 和 0.2 无法精确表示为二进制小数。IEEE 754 标准的近似存储导致运算结果存在微小误差。解决方案是使用 `BigDecimal` 并以字符串方式初始化。

### Q2：`Integer a = 127; Integer b = 127; a == b` 为 true，但 128 就为 false？

因为 `Integer` 内部缓存了 [-128, 127] 范围的对象。在这个范围内的自动装箱会复用缓存对象（同一引用），超出范围则每次都创建新对象。可通过 `-XX:AutoBoxCacheMax` 调整缓存上限。

### Q3：`long` 赋值为什么要加 `L` 后缀？

Java 编译器默认将整数字面量视为 `int` 类型。当赋值给 `long` 变量且数值超出 `int` 范围时（如 `long x = 9999999999;`），编译器会报错。加上 `L` 后缀（如 `long x = 9999999999L;`）明确告诉编译器这是一个 `long` 字面量。

### Q4：`char` 能存储中文吗？

可以。Java 的 `char` 使用 16 位 Unicode 编码，可以存储大部分常用中文字符（在 BMP 范围内）。但对于一些特殊字符（如部分 emoji，属于补充平面），需要使用两个 `char`（代理对）来表示。

## 参考资料

- [《Java 编程思想（Thinking in java）》](https://book.douban.com/subject/2130190/)
- [《Java 核心技术 卷 I 基础知识》](https://book.douban.com/subject/26880667/)
- [极客时间教程 - Java 业务开发常见错误 100 例](https://time.geekbang.org/column/intro/100047701)
- [Java 基本数据类型和引用类型](https://juejin.im/post/59cd71835188255d3448faf6)
- [深入剖析 Java 中的装箱和拆箱](https://www.cnblogs.com/dolphin0520/p/3780005.html)
