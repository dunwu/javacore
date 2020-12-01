# JDK8 入门指南

> JDK8 升级常见问题章节是我个人的经验整理。其他内容基本翻译自 [java8-tutorial](https://github.com/winterbe/java8-tutorial)
>
> **📦 本文以及示例源码已归档在 [javacore](https://github.com/dunwu/javacore/)**
>
> 关键词：`Stream`、`lambda`、`Optional`、`@FunctionalInterface`

## Default Methods for Interfaces(接口的默认方法)

Java 8 使我们能够通过使用 `default` 关键字将非抽象方法实现添加到接口。这个功能也被称为虚拟扩展方法。

这是我们的第一个例子：

```java
interface Formula {
    double calculate(int a);

    default double sqrt(int a) {
        return Math.sqrt(a);
    }
}
```

除了抽象方法 `calculate` ，接口 `Formula` 还定义了默认方法 `sqrt`。具体类只需要执行抽象方法计算。默认的方法 `sqrt` 可以用于开箱即用。

```java
Formula formula = new Formula() {
    @Override
    public double calculate(int a) {
        return sqrt(a * 100);
    }
};

formula.calculate(100);     // 100.0
formula.sqrt(16);           // 4.0
```

`Formula` 被实现为一个匿名对象。代码非常冗长：用于 `sqrt(a * 100)` 这样简单的计算的 6 行代码。正如我们将在下一节中看到的，在 Java 8 中实现单个方法对象有更好的方法。

## Lambda expressions(Lambda 表达式)

让我们从一个简单的例子来说明如何在以前版本的 Java 中对字符串列表进行排序：

```java
List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");

Collections.sort(names, new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return b.compareTo(a);
    }
});
```

静态工具方法 `Collections.sort` 为了对指定的列表进行排序，接受一个列表和一个比较器。您会发现自己经常需要创建匿名比较器并将其传递给排序方法。

Java 8 使用更简短的 **lambda 表达式**来避免常常创建匿名对象的问题：

```java
Collections.sort(names, (String a, String b) -> {
    return b.compareTo(a);
});
```

如您所见，这段代码比上段代码简洁很多。但是，还可以更加简洁：

```java
Collections.sort(names, (String a, String b) -> b.compareTo(a));
```

这行代码中，你省去了花括号 `{}` 和 return 关键字。但是，这还不算完，它还可以再进一步简洁：

```java
names.sort((a, b) -> b.compareTo(a));
```

列表现在有一个 `sort` 方法。此外，java 编译器知道参数类型，所以你可以不指定入参的数据类型。让我们深入探讨如何使用 lambda 表达式。

## Functional Interfaces(函数接口)

lambda 表达式如何适应 Java 的类型系统？每个 lambda 对应一个由接口指定的类型。一个所谓的*函数接口*必须包含一个**抽象方法声明**。该类型的每个 lambda 表达式都将与此抽象方法匹配。由于默认方法不是抽象的，所以你可以自由地添加默认方法到你的函数接口。

只要保证接口仅包含一个抽象方法，就可以使用任意的接口作为 lambda 表达式。为确保您的接口符合要求，您应该添加 `@FunctionalInterface` 注解。编译器注意到这个注解后，一旦您尝试在接口中添加第二个抽象方法声明，编译器就会抛出编译器错误。

示例：

```java
@FunctionalInterface
interface Converter<F, T> {
    T convert(F from);
}
```

```java
Converter<String, Integer> converter = (from) -> Integer.valueOf(from);
Integer converted = converter.convert("123");
System.out.println(converted);    // 123
```

请记住，如果 `@FunctionalInterface` 注解被省略，代码也是有效的。

## Method and Constructor References(方法和构造器引用)

上面的示例代码可以通过使用静态方法引用进一步简化：

```java
Converter<String, Integer> converter = Integer::valueOf;
Integer converted = converter.convert("123");
System.out.println(converted);   // 123
```

Java 8 允许您通过 `::` 关键字传递方法或构造函数的引用。上面的例子展示了如何引用一个静态方法。但是我们也可以引用对象方法：

```java
class Something {
    String startsWith(String s) {
        return String.valueOf(s.charAt(0));
    }
}
```

```java
Something something = new Something();
Converter<String, String> converter = something::startsWith;
String converted = converter.convert("Java");
System.out.println(converted);    // "J"
```

我们来观察一下 `::` 关键字是如何作用于构造器的。首先，我们定义一个有多个构造器的示例类。

```java
class Person {
    String firstName;
    String lastName;

    Person() {}

    Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
```

接着，我们指定一个用于创建 Person 对象的 PersonFactory 接口。

```java
interface PersonFactory<P extends Person> {
    P create(String firstName, String lastName);
}
```

我们不是手动实现工厂，而是通过构造引用将所有东西粘合在一起：

```java
PersonFactory<Person> personFactory = Person::new;
Person person = personFactory.create("Peter", "Parker");
```

我们通过 `Person::new` 来创建一个 Person 构造器的引用。Java 编译器会根据`PersonFactory.create` 的签名自动匹配正确的构造器。

## Lambda Scopes(Lambda 作用域)

从 lambda 表达式访问外部作用域变量与匿名对象非常相似。您可以访问本地外部作用域的常量以及实例的成员变量和静态变量。

### Accessing local variables(访问本地变量)

我们可以访问 lambda 表达式作用域外部的常量：

```java
final int num = 1;
Converter<Integer, String> stringConverter =
        (from) -> String.valueOf(from + num);

stringConverter.convert(2);     // 3
```

不同于匿名对象的是：这个变量 `num` 不是一定要被 `final` 修饰。下面的代码一样合法：

```java
int num = 1;
Converter<Integer, String> stringConverter =
        (from) -> String.valueOf(from + num);

stringConverter.convert(2);     // 3
```

但是，`num` 必须是隐式常量的。下面的代码不能编译通过：

```java
int num = 1;
Converter<Integer, String> stringConverter =
        (from) -> String.valueOf(from + num);
num = 3;
```

此外，在 lambda 表达式中对 `num` 做写操作也是被禁止的。

### Accessing fields and static variables(访问成员变量和静态变量)

与局部变量相比，我们既可以在 lambda 表达式中读写实例的成员变量，也可以读写实例的静态变量。这种行为在匿名对象中是众所周知的。

```java
class Lambda4 {
    static int outerStaticNum;
    int outerNum;

    void testScopes() {
        Converter<Integer, String> stringConverter1 = (from) -> {
            outerNum = 23;
            return String.valueOf(from);
        };

        Converter<Integer, String> stringConverter2 = (from) -> {
            outerStaticNum = 72;
            return String.valueOf(from);
        };
    }
}
```

### Accessing Default Interface Methods（访问默认的接口方法）

还记得第一节的 formula 例子吗？ `Formula` 接口定义了一个默认方法 `sqrt`，它可以被每个 formula 实例（包括匿名对象）访问。这个特性不适用于 lambda 表达式。

默认方法**不能**被 lambda 表达式访问。下面的代码不能编译通过：

```java
Formula formula = (a) -> sqrt(a * 100);
```

## Built-in Functional Interfaces(内置函数接口)

JDK 1.8 API 包含许多内置的功能接口。它们中的一些在较早的 Java 版本（比如 `Comparator` 或 `Runnable`）中是众所周知的。这些现有的接口通过 `@FunctionalInterfaceannotation` 注解被扩展为支持 Lambda。

但是，Java 8 API 也提供了不少新的函数接口。其中一些新接口在 [Google Guava](https://code.google.com/p/guava-libraries/) 库中是众所周知的。即使您熟悉这个库，也应该密切关注如何通过一些有用的方法扩展来扩展这些接口。

### Predicates

`Predicate` 是只有一个参数的布尔值函数。该接口包含各种默认方法，用于将谓词组合成复杂的逻辑术语（与、或、非）

```java
Predicate<String> predicate = (s) -> s.length() > 0;

predicate.test("foo");              // true
predicate.negate().test("foo");     // false

Predicate<Boolean> nonNull = Objects::nonNull;
Predicate<Boolean> isNull = Objects::isNull;

Predicate<String> isEmpty = String::isEmpty;
Predicate<String> isNotEmpty = isEmpty.negate();
```

### Functions

`Function` 接受一个参数并产生一个结果。可以使用默认方法将多个函数链接在一起（compose、andThen）。

```java
Function<String, Integer> toInteger = Integer::valueOf;
Function<String, String> backToString = toInteger.andThen(String::valueOf);

backToString.apply("123");     // "123"
```

### Suppliers

`Supplier` 产生一个泛型结果。与 `Function` 不同，`Supplier` 不接受参数。

```java
Supplier<Person> personSupplier = Person::new;
personSupplier.get();   // new Person
```

### Consumers

Consumer 表示要在一个输入参数上执行的操作。

```java
Consumer<Person> greeter = (p) -> System.out.println("Hello, " + p.firstName);
greeter.accept(new Person("Luke", "Skywalker"));
```

### Comparators

比较器在老版本的 Java 中是众所周知的。 Java 8 为接口添加了各种默认方法。

```java
Comparator<Person> comparator = (p1, p2) -> p1.firstName.compareTo(p2.firstName);

Person p1 = new Person("John", "Doe");
Person p2 = new Person("Alice", "Wonderland");

comparator.compare(p1, p2);             // > 0
comparator.reversed().compare(p1, p2);  // < 0
```

## Optionals

`Optional` 不是功能性接口，而是防止 `NullPointerException` 的好工具。这是下一节的一个重要概念，所以让我们快速看看 `Optional` 是如何工作的。

`Optional` 是一个简单的容器，其值可以是 null 或非 null。想想一个可能返回一个非空结果的方法，但有时候什么都不返回。不是返回 null，而是返回 Java 8 中的 `Optional`。

```java
Optional<String> optional = Optional.of("bam");

optional.isPresent();           // true
optional.get();                 // "bam"
optional.orElse("fallback");    // "bam"

optional.ifPresent((s) -> System.out.println(s.charAt(0)));     // "b"
```

## Streams

`java.util.Stream` 表示可以在其上执行一个或多个操作的元素序列。流操作是中间或终端。当终端操作返回一个特定类型的结果时，中间操作返回流本身，所以你可以链接多个方法调用。流在源上创建，例如一个 `java.util.Collection` 像列表或集合（不支持映射）。流操作既可以按顺序执行，也可以并行执行。

> 流是非常强大的，所以，我写了一个独立的 [Java8 Streams 教程](http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/) 。**您还应该查看 Sequent，将其作为 Web 的类似库。**

我们先来看看顺序流如何工作。首先，我们以字符串列表的形式创建一个示例源代码：

```java
List<String> stringCollection = new ArrayList<>();
stringCollection.add("ddd2");
stringCollection.add("aaa2");
stringCollection.add("bbb1");
stringCollection.add("aaa1");
stringCollection.add("bbb3");
stringCollection.add("ccc");
stringCollection.add("bbb2");
stringCollection.add("ddd1");
```

Java 8 中的集合已被扩展，因此您可以通过调用 `Collection.stream()` 或`Collection.parallelStream()` 来简单地创建流。以下各节介绍最常见的流操作。

### Filter

过滤器接受一个谓词来过滤流的所有元素。这个操作是中间的，使我们能够调用另一个流操作（`forEach`）的结果。 ForEach 接受一个消费者被执行的过滤流中的每个元素。 ForEach 是一个终端操作。它是无效的，所以我们不能调用另一个流操作。

```java
stringCollection
    .stream()
    .filter((s) -> s.startsWith("a"))
    .forEach(System.out::println);

// "aaa2", "aaa1"
```

### Sorted

排序是一个中间操作，返回流的排序视图。元素按自然顺序排序，除非您传递自定义比较器。

```java
stringCollection
    .stream()
    .sorted()
    .filter((s) -> s.startsWith("a"))
    .forEach(System.out::println);

// "aaa1", "aaa2"
```

请记住，排序只会创建流的排序视图，而不会操纵支持的集合的排序。 `stringCollection` 的排序是不变的：

```java
System.out.println(stringCollection);
// ddd2, aaa2, bbb1, aaa1, bbb3, ccc, bbb2, ddd1
```

### Map

中间操作映射通过给定函数将每个元素转换为另一个对象。以下示例将每个字符串转换为大写字母字符串。但是您也可以使用 `map` 将每个对象转换为另一种类型。结果流的泛型类型取决于您传递给 `map` 的函数的泛型类型。

```java
stringCollection
    .stream()
    .map(String::toUpperCase)
    .sorted((a, b) -> b.compareTo(a))
    .forEach(System.out::println);

// "DDD2", "DDD1", "CCC", "BBB3", "BBB2", "AAA2", "AAA1"
```

### Match

可以使用各种匹配操作来检查某个谓词是否与流匹配。所有这些操作都是终端并返回布尔结果。

```java
boolean anyStartsWithA =
    stringCollection
        .stream()
        .anyMatch((s) -> s.startsWith("a"));

System.out.println(anyStartsWithA);      // true

boolean allStartsWithA =
    stringCollection
        .stream()
        .allMatch((s) -> s.startsWith("a"));

System.out.println(allStartsWithA);      // false

boolean noneStartsWithZ =
    stringCollection
        .stream()
        .noneMatch((s) -> s.startsWith("z"));

System.out.println(noneStartsWithZ);      // true
```

#### Count

Count 是一个终端操作，返回流中元素的个数。

```java
long startsWithB =
    stringCollection
        .stream()
        .filter((s) -> s.startsWith("b"))
        .count();

System.out.println(startsWithB);    // 3
```

### Reduce

该终端操作使用给定的功能对流的元素进行缩减。结果是一个 `Optional` 持有缩小后的值。

```java
Optional<String> reduced =
    stringCollection
        .stream()
        .sorted()
        .reduce((s1, s2) -> s1 + "#" + s2);

reduced.ifPresent(System.out::println);
// "aaa1##aaa2##bbb1##bbb2##bbb3##ccc##ddd1##ddd2"
```

## Parallel Streams

如上所述，流可以是顺序的也可以是并行的。顺序流上的操作在单个线程上执行，而并行流上的操作在多个线程上同时执行。

以下示例演示了通过使用并行流提高性能是多么容易。

首先，我们创建一个较大的独特元素的列表：

```java
int max = 1000000;
List<String> values = new ArrayList<>(max);
for (int i = 0; i < max; i++) {
    UUID uuid = UUID.randomUUID();
    values.add(uuid.toString());
}
```

现在我们测量对这个集合进行排序所花费的时间。

### Sequential Sort

```java
long t0 = System.nanoTime();

long count = values.stream().sorted().count();
System.out.println(count);

long t1 = System.nanoTime();

long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
System.out.println(String.format("sequential sort took: %d ms", millis));

// sequential sort took: 899 ms
```

### Parallel Sort

```java
long t0 = System.nanoTime();

long count = values.parallelStream().sorted().count();
System.out.println(count);

long t1 = System.nanoTime();

long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
System.out.println(String.format("parallel sort took: %d ms", millis));

// parallel sort took: 472 ms
```

如你所见，两个代码段差不多，但是并行排序快了近 50%。你所需做的仅仅是将 `stream()` 改为 `parallelStream()` 。

## Maps

如前所述，map 不直接支持流。Map 接口本身没有可用的 `stream()` 方法，但是你可以通过 `map.keySet().stream()` 、 `map.values().stream()` 和 `map.entrySet().stream()` 创建指定的流。

此外，map 支持各种新的、有用的方法来处理常见任务。

```java
Map<Integer, String> map = new HashMap<>();

for (int i = 0; i < 10; i++) {
    map.putIfAbsent(i, "val" + i);
}

map.forEach((id, val) -> System.out.println(val));
```

上面的代码应该是自我解释的：`putIfAbsent` 阻止我们写入额外的空值检查；`forEach` 接受消费者为 map 的每个值实现操作。

这个例子展示了如何利用函数来计算 map 上的代码：

```java
map.computeIfPresent(3, (num, val) -> val + num);
map.get(3);             // val33

map.computeIfPresent(9, (num, val) -> null);
map.containsKey(9);     // false

map.computeIfAbsent(23, num -> "val" + num);
map.containsKey(23);    // true

map.computeIfAbsent(3, num -> "bam");
map.get(3);             // val33
```

接下来，我们学习如何删除给定键的条目，只有当前键映射到给定值时：

```java
map.remove(3, "val3");
map.get(3);             // val33

map.remove(3, "val33");
map.get(3);             // null
```

另一个有用方法：

```java
map.getOrDefault(42, "not found");  // not found
```

合并一个 map 的 entry 很简单：

```java
map.merge(9, "val9", (value, newValue) -> value.concat(newValue));
map.get(9);             // val9

map.merge(9, "concat", (value, newValue) -> value.concat(newValue));
map.get(9);             // val9concat
```

如果不存在该键的条目，合并或者将键/值放入 map 中；否则将调用合并函数来更改现有值。

## Date API

Java 8 在 `java.time` 包下新增了一个全新的日期和时间 API。新的日期 API 与 [Joda-Time](http://www.joda.org/joda-time/) 库相似，但不一样。以下示例涵盖了此新 API 的最重要部分。

### Clock

`Clock` 提供对当前日期和时间的访问。`Clock` 知道一个时区，可以使用它来代替 `System.currentTimeMillis()` ，获取从 **Unix EPOCH** 开始的以毫秒为单位的当前时间。时间线上的某一时刻也由类 `Instant` 表示。 Instants 可以用来创建遗留的 `java.util.Date` 对象。

```java
Clock clock = Clock.systemDefaultZone();
long millis = clock.millis();

Instant instant = clock.instant();
Date legacyDate = Date.from(instant);   // legacy java.util.Date
```

### Timezones

时区由 `ZoneId` 表示。他们可以很容易地通过静态工厂方法访问。时区定义了某一时刻和当地日期、时间之间转换的重要偏移量。

```java
System.out.println(ZoneId.getAvailableZoneIds());
// prints all available timezone ids

ZoneId zone1 = ZoneId.of("Europe/Berlin");
ZoneId zone2 = ZoneId.of("Brazil/East");
System.out.println(zone1.getRules());
System.out.println(zone2.getRules());

// ZoneRules[currentStandardOffset=+01:00]
// ZoneRules[currentStandardOffset=-03:00]
```

### LocalTime

`LocalTime` 代表没有时区的时间，例如晚上 10 点或 17:30:15。以下示例为上面定义的时区创建两个本地时间。然后我们比较两次，并计算两次之间的小时和分钟的差异。

```java
LocalTime now1 = LocalTime.now(zone1);
LocalTime now2 = LocalTime.now(zone2);

System.out.println(now1.isBefore(now2));  // false

long hoursBetween = ChronoUnit.HOURS.between(now1, now2);
long minutesBetween = ChronoUnit.MINUTES.between(now1, now2);

System.out.println(hoursBetween);       // -3
System.out.println(minutesBetween);     // -239
```

`LocalTime` 带有各种工厂方法，以简化新实例的创建，包括解析时间字符串。

```java
LocalTime late = LocalTime.of(23, 59, 59);
System.out.println(late);       // 23:59:59

DateTimeFormatter germanFormatter =
    DateTimeFormatter
        .ofLocalizedTime(FormatStyle.SHORT)
        .withLocale(Locale.GERMAN);

LocalTime leetTime = LocalTime.parse("13:37", germanFormatter);
System.out.println(leetTime);   // 13:37
```

### LocalDate

`LocalDate` 表示不同的日期，例如：2014 年 3 月 11 日。它是不可变的，并且与 `LocalTime` 完全类似。该示例演示如何通过加减日、月或年来计算新日期。请记住，每个操作都会返回一个新的实例。

```java
LocalDate today = LocalDate.now();
LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
LocalDate yesterday = tomorrow.minusDays(2);

LocalDate independenceDay = LocalDate.of(2014, Month.JULY, 4);
DayOfWeek dayOfWeek = independenceDay.getDayOfWeek();
System.out.println(dayOfWeek);    // FRIDAY
```

从一个字符串中解析出 LocalDate 对象，和解析 LocalTime 一样的简单：

```java
DateTimeFormatter germanFormatter =
    DateTimeFormatter
        .ofLocalizedDate(FormatStyle.MEDIUM)
        .withLocale(Locale.GERMAN);

LocalDate xmas = LocalDate.parse("24.12.2014", germanFormatter);
System.out.println(xmas);   // 2014-12-24
```

### LocalDateTime

LocalDateTime 表示日期时间。它将日期和时间组合成一个实例。 `LocalDateTime` 是不可变的，其作用类似于 `LocalTime` 和 `LocalDate`。我们可以利用方法去获取日期时间中某个单位的值。

```java
LocalDateTime sylvester = LocalDateTime.of(2014, Month.DECEMBER, 31, 23, 59, 59);

DayOfWeek dayOfWeek = sylvester.getDayOfWeek();
System.out.println(dayOfWeek);      // WEDNESDAY

Month month = sylvester.getMonth();
System.out.println(month);          // DECEMBER

long minuteOfDay = sylvester.getLong(ChronoField.MINUTE_OF_DAY);
System.out.println(minuteOfDay);    // 1439
```

通过一个时区的附加信息可以转为一个实例。这个实例很容易转为`java.util.Date` 类型。

```java
Instant instant = sylvester
        .atZone(ZoneId.systemDefault())
        .toInstant();

Date legacyDate = Date.from(instant);
System.out.println(legacyDate);     // Wed Dec 31 23:59:59 CET 2014
```

日期时间的格式化类似于 Date 或 Time。我们可以使用自定义模式创建格式化程序，而不是使用预定义的格式。

```java
DateTimeFormatter formatter =
    DateTimeFormatter
        .ofPattern("MMM dd, yyyy - HH:mm");

LocalDateTime parsed = LocalDateTime.parse("Nov 03, 2014 - 07:13", formatter);
String string = formatter.format(parsed);
System.out.println(string);     // Nov 03, 2014 - 07:13
```

不同于 `java.text.NumberFormat` ， `DateTimeFormatter` 是不可变且**线程安全的** 。

更多关于日期格式化的内容可以参考[这里](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html).

## Annotations

Java 8 中的注释是可重复的。让我们直接看一个例子来解决这个问题。

首先，我们定义一个包含实际注释数组的外层注释：

```java
@interface Hints {
    Hint[] value();
}

@Repeatable(Hints.class)
@interface Hint {
    String value();
}
```

Java8 允许我们通过使用 `@Repeatable` 注解来引入多个同类型的注解。

### Variant 1: 使用容器注解 (老套路)

```java
@Hints({@Hint("hint1"), @Hint("hint2")})
class Person {}
```

### Variant 2: 使用 repeatable 注解 (新套路)

```java
@Hint("hint1")
@Hint("hint2")
class Person {}
```

使用场景 2，Java 编译器隐式地设置了 `@Hints` 注解。

这对于通过反射来读取注解信息很重要。

```java
Hint hint = Person.class.getAnnotation(Hint.class);
System.out.println(hint);                   // null

Hints hints1 = Person.class.getAnnotation(Hints.class);
System.out.println(hints1.value().length);  // 2

Hint[] hints2 = Person.class.getAnnotationsByType(Hint.class);
System.out.println(hints2.length);          // 2
```

尽管，我们从没有在 Person 类上声明 `@Hints` 注解，但是仍可以通过`getAnnotation(Hints.class)` 读取它。然而，更便利的方式是 `getAnnotationsByType` ，它可以直接访问所有 `@Hint` 注解。

此外，Java 8 中的注释使用扩展了两个新的目标：

```java
@Target({ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@interface MyAnnotation {}
```

水平线以上为 [java8-tutorial](https://github.com/winterbe/java8-tutorial) 翻译内容。

------

## JDK8 升级常见问题

> JDK8 发布很久了，它提供了许多吸引人的新特性，能够提高编程效率。
>
> 如果是新的项目，使用 JDK8 当然是最好的选择。但是，对于一些老的项目，升级到 JDK8 则存在一些兼容性问题，是否升级需要酌情考虑。
>
> 近期，我在工作中遇到一个任务，将部门所有项目的 JDK 版本升级到 1.8 （老版本大多是 1.6）。在这个过程中，遇到一些问题点，并结合在网上看到的坑，在这里总结一下。

### Intellij 中的 JDK 环境设置

#### Settings

点击 **File > Settings > Java Compiler**

Project bytecode version 选择 1.8

点击 **File > Settings > Build Tools > Maven > Importing**

选择 JDK for importer 为 1.8

#### Projcet Settings

**Project SDK** 选择 1.8

#### Application

如果 web 应用的启动方式为 Application ，需要修改 JRE

点击 **Run/Debug Configurations > Configuration**

选择 JRE 为 1.8

### Linux 环境修改

#### 修改环境变量

修改 `/etc/profile` 中的 **JAVA_HOME**，设置 为 jdk8 所在路径。

修改后，执行 `source /etc/profile` 生效。

编译、发布脚本中如果有 `export JAVA_HOME` ，需要注意，需要使用 jdk8 的路径。

#### 修改 maven

settings.xml 中 profile 的激活条件如果是 jdk，需要修改一下 jdk 版本

```xml
<activation>
  <jdk>1.8</jdk> <!-- 修改为 1.8 -->
</activation>
```

#### 修改 server

修改 server 中的 javac 版本，以 resin 为例：

修改 resin 配置文件中的 javac 参数。

```xml
<javac compiler="internal" args="-source 1.8"/>
```

### sun.\* 包缺失问题

JDK8 不再提供 `sun.*` 包供开发者使用，因为这些接口不是公共接口，不能保证在所有 Java 兼容的平台上工作。

使用了这些 API 的程序如果要升级到 JDK 1.8 需要寻求替代方案。

虽然，也可以自己导入包含 `sun.*` 接口 jar 包到 classpath 目录，但这不是一个好的做法。

需要详细了解为什么不要使用 `sun.*` ，可以参考官方文档：[Why Developers Should Not Write Programs That Call 'sun' Packages](http://www.oracle.com/technetwork/java/faq-sun-packages-142232.html)

### 默认安全策略修改

升级后估计有些小伙伴在使用不安全算法时可能会发生错误，so，支持不安全算法还是有必要的

找到\$JAVA_HOME 下 `jre/lib/security/java.security` ，将禁用的算法设置为空：`jdk.certpath.disabledAlgorithms=` 。

### JVM 参数调整

在 jdk8 中，PermSize 相关的参数已经不被使用：

```
-XX:MaxPermSize=size

Sets the maximum permanent generation space size (in bytes). This option was deprecated in JDK 8, and superseded by the -XX:MaxMetaspaceSize option.

-XX:PermSize=size

Sets the space (in bytes) allocated to the permanent generation that triggers a garbage collection if it is exceeded. This option was deprecated un JDK 8, and superseded by the -XX:MetaspaceSize option.
```

JDK8 中再也没有 `PermGen` 了。其中的某些部分，如被 intern 的字符串，在 JDK7 中已经移到了普通堆里。**其余结构在 JDK8 中会被移到称作“Metaspace”的本机内存区中，该区域在默认情况下会自动生长，也会被垃圾回收。它有两个标记：MetaspaceSize 和 MaxMetaspaceSize。**

-XX:MetaspaceSize=size

> Sets the size of the allocated class metadata space that will trigger a garbage collection the first time it is exceeded. This threshold for a garbage collection is increased or decreased depending on the amount of metadata used. The default size depends on the platform.

-XX:MaxMetaspaceSize=size

> Sets the maximum amount of native memory that can be allocated for class metadata. By default, the size is not limited. The amount of metadata for an application depends on the application itself, other running applications, and the amount of memory available on the system.

以下示例显示如何将类类元数据的上限设置为 256 MB：

XX:MaxMetaspaceSize=256m

### 字节码问题

ASM 5.0 beta 开始支持 JDK8

**字节码错误**

```
Caused by: java.io.IOException: invalid constant type: 15
	at javassist.bytecode.ConstPool.readOne(ConstPool.java:1113)
```

- 查找组件用到了 mvel，mvel 为了提高效率进行了字节码优化，正好碰上 JDK8 死穴，所以需要升级。

```xml
<dependency>
  <groupId>org.mvel</groupId>
  <artifactId>mvel2</artifactId>
  <version>2.2.7.Final</version>
</dependency>
```

- javassist

```xml
<dependency>
  <groupId>org.javassist</groupId>
  <artifactId>javassist</artifactId>
  <version>3.18.1-GA</version>
</dependency>
```

> **注意**
>
> 有些部署工具不会删除旧版本 jar 包，所以可以尝试手动删除老版本 jar 包。

http://asm.ow2.org/history.html

### Java 连接 redis 启动报错 Error redis clients jedis HostAndPort cant resolve localhost address

错误环境:
本地 window 开发环境没有问题。上到 Linux 环境,启动出现问题。
错误信息:
Error redis clients jedis HostAndPort cant resolve localhost address

解决办法:

（1）查看 Linux 系统的主机名

```
# hostname
template
```

（2）查看/etc/hosts 文件中是否有 127.0.0.1 对应主机名，如果没有则添加

### Resin 容器指定 JDK 1.8

如果 resin 容器原来版本低于 JDK1.8，运行 JDK 1.8 编译的 web app 时，可能会提示错误：

```
java.lang.UnsupportedClassVersionError: PR/Sort : Unsupported major.minor version 52.0
```

解决方法就是，使用 JDK 1.8 要重新编译一下。然后，我在部署时出现过编译后仍报错的情况，重启一下服务器后，问题解决，不知是什么原因。

```
./configure --prefix=/usr/local/resin  --with-java=/usr/local/jdk1.8.0_121
make & make install
```

## 参考资料

- [java8-tutorial](https://github.com/winterbe/java8-tutorial)
- [Compatibility Guide for JDK 8](http://www.oracle.com/technetwork/java/javase/8-compatibility-guide-2156366.html)
- [Compatibility Guide for JDK 8 中文翻译](https://yq.aliyun.com/articles/236)
- [Why Developers Should Not Write Programs That Call 'sun' Packages](http://www.oracle.com/technetwork/java/faq-sun-packages-142232.html)
