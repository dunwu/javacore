# 深入理解 Java 注解

> 本文内容基于 JDK8。注解是 JDK5 引入的，后续 JDK 版本扩展了一些内容，本文中没有明确指明版本的注解都是 JDK5 就已经支持的注解。
>
> **📦 本文以及示例源码已归档在 [javacore](https://github.com/dunwu/javacore/)**

<!-- TOC depthFrom:2 depthTo:3 -->

- [1. 简介](#1-简介)
  - [1.1. 注解的形式](#11-注解的形式)
  - [1.2. 什么是注解](#12-什么是注解)
  - [1.3. 注解的作用](#13-注解的作用)
  - [1.4. 注解的代价](#14-注解的代价)
  - [1.5. 注解的应用范围](#15-注解的应用范围)
- [2. 内置注解](#2-内置注解)
  - [2.1. @Override](#21-override)
  - [2.2. @Deprecated](#22-deprecated)
  - [2.3. @SuppressWarnnings](#23-suppresswarnnings)
  - [2.4. @SafeVarargs](#24-safevarargs)
  - [2.5. @FunctionalInterface](#25-functionalinterface)
- [3. 元注解](#3-元注解)
  - [3.1. @Retention](#31-retention)
  - [3.2. @Documented](#32-documented)
  - [3.3. @Target](#33-target)
  - [3.4. @Inherited](#34-inherited)
  - [3.5. @Repeatable](#35-repeatable)
- [4. 自定义注解](#4-自定义注解)
  - [4.1. 注解的定义](#41-注解的定义)
  - [4.2. 注解属性](#42-注解属性)
  - [4.3. 注解处理器](#43-注解处理器)
  - [4.4. 使用注解](#44-使用注解)
- [5. 小结](#5-小结)
- [6. 参考资料](#6-参考资料)

<!-- /TOC -->

## 1. 简介

### 1.1. 注解的形式

Java 中，注解是以 `@` 字符开始的修饰符。如下：

```java
@Override
void mySuperMethod() { ... }
```

注解可以包含命名或未命名的属性，并且这些属性有值。

```java
@Author(
   name = "Benjamin Franklin",
   date = "3/27/2003"
)
class MyClass() { ... }
```

如果只有一个名为 value 的属性，那么名称可以省略，如：

```java
@SuppressWarnings("unchecked")
void myMethod() { ... }
```

如果注解没有属性，则称为`标记注解`。如：`@Override`。

### 1.2. 什么是注解

从本质上来说，**注解是一种标签，其实质上可以视为一种特殊的注释，如果没有解析它的代码，它并不比普通注释强。**

解析一个注解往往有两种形式：

- **编译期直接的扫描** - 编译器的扫描指的是编译器在对 java 代码编译字节码的过程中会检测到某个类或者方法被一些注解修饰，这时它就会对于这些注解进行某些处理。这种情况只适用于 JDK 内置的注解类。
- **运行期的反射** - 如果要自定义注解，Java 编译器无法识别并处理这个注解，它只能根据该注解的作用范围来选择是否编译进字节码文件。如果要处理注解，必须利用反射技术，识别该注解以及它所携带的信息，然后做相应的处理。

### 1.3. 注解的作用

注解有许多用途：

- 编译器信息 - 编译器可以使用注解来检测错误或抑制警告。
- 编译时和部署时的处理 - 程序可以处理注解信息以生成代码，XML 文件等。
- 运行时处理 - 可以在运行时检查某些注解并处理。

作为 Java 程序员，多多少少都曾经历过被各种配置文件（xml、properties）支配的恐惧。过多的配置文件会使得项目难以维护。个人认为，使用注解以减少配置文件或代码，是注解最大的用处。

### 1.4. 注解的代价

凡事有得必有失，注解技术同样如此。使用注解也有一定的代价：

- 显然，它是一种侵入式编程，那么，自然就存在着增加程序耦合度的问题。
- 自定义注解的处理需要在运行时，通过反射技术来获取属性。如果注解所修饰的元素是类的非 public 成员，也可以通过反射获取。这就违背了面向对象的封装性。
- 注解所产生的问题，相对而言，更难以 debug 或定位。

但是，正所谓瑕不掩瑜，注解所付出的代价，相较于它提供的功能而言，还是可以接受的。

### 1.5. 注解的应用范围

注解可以应用于类、字段、方法和其他程序元素的声明。

JDK8 开始，注解的应用范围进一步扩大，以下是新的应用范围：

类实例初始化表达式：

```java
new @Interned MyObject();
```

类型转换：

```java
myString = (@NonNull String) str;
```

实现接口的声明：

```java
class UnmodifiableList<T> implements
    @Readonly List<@Readonly T> {}
```

抛出异常声明：

```java
void monitorTemperature()
    throws @Critical TemperatureException {}
```

## 2. 内置注解

JDK 中内置了以下注解：

- `@Override`
- `@Deprecated`
- `@SuppressWarnnings`
- `@SafeVarargs`（JDK7 引入）
- `@FunctionalInterface`（JDK8 引入）

### 2.1. @Override

**[`@Override`](https://docs.oracle.com/javase/8/docs/api/java/lang/Override.html) 用于表明被修饰方法覆写了父类的方法。**

如果试图使用 `@Override` 标记一个实际上并没有覆写父类的方法时，java 编译器会告警。

`@Override` 示例：

```java
public class OverrideAnnotationDemo {

    static class Person {
        public String getName() {
            return "getName";
        }
    }


    static class Man extends Person {
        @Override
        public String getName() {
            return "override getName";
        }

        /**
         *  放开下面的注释，编译时会告警
         */
       /*
        @Override
        public String getName2() {
            return "override getName2";
        }
        */
    }

    public static void main(String[] args) {
        Person per = new Man();
        System.out.println(per.getName());
    }
}
```

### 2.2. @Deprecated

**`@Deprecated` 用于标明被修饰的类或类成员、类方法已经废弃、过时，不建议使用。**

`@Deprecated` 有一定的**延续性**：如果我们在代码中通过继承或者覆盖的方式使用了过时的类或类成员，即使子类或子方法没有标记为 `@Deprecated`，但编译器仍然会告警。

> 🔔 注意： `@Deprecated` 这个注解类型和 javadoc 中的 `@deprecated` 这个 tag 是有区别的：前者是 java 编译器识别的；而后者是被 javadoc 工具所识别用来生成文档（包含程序成员为什么已经过时、它应当如何被禁止或者替代的描述）。

`@Deprecated` 示例：

```java
public class DeprecatedAnnotationDemo {
    static class DeprecatedField {
        @Deprecated
        public static final String DEPRECATED_FIELD = "DeprecatedField";
    }


    static class DeprecatedMethod {
        @Deprecated
        public String print() {
            return "DeprecatedMethod";
        }
    }


    @Deprecated
    static class DeprecatedClass {
        public String print() {
            return "DeprecatedClass";
        }
    }

    public static void main(String[] args) {
        System.out.println(DeprecatedField.DEPRECATED_FIELD);

        DeprecatedMethod dm = new DeprecatedMethod();
        System.out.println(dm.print());


        DeprecatedClass dc = new DeprecatedClass();
        System.out.println(dc.print());
    }
}
//Output:
//DeprecatedField
//DeprecatedMethod
//DeprecatedClass
```

### 2.3. @SuppressWarnnings

**[`@SuppressWarnings`](https://docs.oracle.com/javase/8/docs/api/java/lang/SuppressWarnings.html) 用于关闭对类、方法、成员编译时产生的特定警告。**

`@SuppressWarning` 不是一个标记注解。它有一个类型为 `String[]` 的数组成员，这个数组中存储的是要关闭的告警类型。对于 javac 编译器来讲，对 `-Xlint` 选项有效的警告名也同样对 `@SuppressWarings` 有效，同时编译器会忽略掉无法识别的警告名。

`@SuppressWarning` 示例：

```java
@SuppressWarnings({"rawtypes", "unchecked"})
public class SuppressWarningsAnnotationDemo {
    static class SuppressDemo<T> {
        private T value;

        public T getValue() {
            return this.value;
        }

        public void setValue(T var) {
            this.value = var;
        }
    }

    @SuppressWarnings({"deprecation"})
    public static void main(String[] args) {
        SuppressDemo d = new SuppressDemo();
        d.setValue("南京");
        System.out.println("地名：" + d.getValue());
    }
}
```

`@SuppressWarnings` 注解的常见参数值的简单说明：

- `deprecation` - 使用了不赞成使用的类或方法时的警告；
- `unchecked` - 执行了未检查的转换时的警告，例如当使用集合时没有用泛型 (Generics) 来指定集合保存的类型;
- `fallthrough` - 当 Switch 程序块直接通往下一种情况而没有 Break 时的警告;
- `path` - 在类路径、源文件路径等中有不存在的路径时的警告;
- `serial` - 当在可序列化的类上缺少 serialVersionUID 定义时的警告;
- `finally` - 任何 finally 子句不能正常完成时的警告;
- `all` - 所有的警告。

```java
@SuppressWarnings({"uncheck", "deprecation"})
public class InternalAnnotationDemo {

    /**
     * @SuppressWarnings 标记消除当前类的告警信息
     */
    @SuppressWarnings({"deprecation"})
    static class A {
        public void method1() {
            System.out.println("call method1");
        }

        /**
         * @Deprecated 标记当前方法为废弃方法，不建议使用
         */
        @Deprecated
        public void method2() {
            System.out.println("call method2");
        }
    }

    /**
     * @Deprecated 标记当前类为废弃类，不建议使用
     */
    @Deprecated
    static class B extends A {
        /**
         * @Override 标记显示指明当前方法覆写了父类或接口的方法
         */
        @Override
        public void method1() { }
    }

    public static void main(String[] args) {
        A obj = new B();
        obj.method1();
        obj.method2();
    }
}
```

### 2.4. @SafeVarargs

`@SafeVarargs` 在 JDK7 中引入。

**[`@SafeVarargs`](https://docs.oracle.com/javase/8/docs/api/java/lang/SafeVarargs.html) 的作用是：告诉编译器，在可变长参数中的泛型是类型安全的。可变长参数是使用数组存储的，而数组和泛型不能很好的混合使用。**

简单的说，数组元素的数据类型在编译和运行时都是确定的，而泛型的数据类型只有在运行时才能确定下来。因此，当把一个泛型存储到数组中时，编译器在编译阶段无法确认数据类型是否匹配，因此会给出警告信息；即如果泛型的真实数据类型无法和参数数组的类型匹配，会导致 `ClassCastException` 异常。

`@SafeVarargs` 注解使用范围：

- `@SafeVarargs` 注解可以用于构造方法。
- `@SafeVarargs` 注解可以用于 `static` 或 `final` 方法。

`@SafeVarargs` 示例：

```java
public class SafeVarargsAnnotationDemo {
    /**
     * 此方法实际上并不安全，不使用此注解，编译时会告警
     */
    @SafeVarargs
    static void wrongMethod(List<String>... stringLists) {
        Object[] array = stringLists;
        List<Integer> tmpList = Arrays.asList(42);
        array[0] = tmpList; // 语法错误，但是编译不告警
        String s = stringLists[0].get(0); // 运行时报 ClassCastException
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");

        List<String> list2 = new ArrayList<>();
        list.add("1");
        list.add("2");

        wrongMethod(list, list2);
    }
}
```

以上代码，如果不使用 `@SafeVarargs` ，编译时会告警

```
[WARNING] /D:/Codes/ZP/Java/javacore/codes/basics/src/main/java/io/github/dunwu/javacore/annotation/SafeVarargsAnnotationDemo.java: 某些输入文件使用了未经检查或不安全的操作。
[WARNING] /D:/Codes/ZP/Java/javacore/codes/basics/src/main/java/io/github/dunwu/javacore/annotation/SafeVarargsAnnotationDemo.java: 有关详细信息, 请使用 -Xlint:unchecked 重新编译。
```

### 2.5. @FunctionalInterface

`@FunctionalInterface` 在 JDK8 引入。

**[`@FunctionalInterface`](https://docs.oracle.com/javase/8/docs/api/java/lang/FunctionalInterface.html) 用于指示被修饰的接口是函数式接口。**

需要注意的是，如果一个接口符合"函数式接口"定义，不加 `@FunctionalInterface` 也没关系；但如果编写的不是函数式接口，却使用 `@FunctionInterface`，那么编译器会报错。

什么是函数式接口？

**函数式接口(Functional Interface)就是一个有且仅有一个抽象方法，但是可以有多个非抽象方法的接口**。函数式接口可以被隐式转换为 lambda 表达式。

函数式接口的特点：

- 接口有且只能有个一个抽象方法（抽象方法只有方法定义，没有方法体）。
- 不能在接口中覆写 Object 类中的 public 方法（写了编译器也会报错）。
- 允许有 default 实现方法。

示例：

```java
public class FunctionalInterfaceAnnotationDemo {

    @FunctionalInterface
    public interface Func1<T> {
        void printMessage(T message);
    }

    /**
     * @FunctionalInterface 修饰的接口中定义两个抽象方法，编译时会报错
     * @param <T>
     */
    /*@FunctionalInterface
    public interface Func2<T> {
        void printMessage(T message);
        void printMessage2(T message);
    }*/

    public static void main(String[] args) {
        Func1 func1 = message -> System.out.println(message);
        func1.printMessage("Hello");
        func1.printMessage(100);
    }
}
```

## 3. 元注解

JDK 中虽然内置了几个注解，但这远远不能满足开发过程中遇到的千变万化的需求。所以我们需要自定义注解，而这就需要用到元注解。

**元注解的作用就是用于定义其它的注解**。

Java 中提供了以下元注解类型：

- `@Retention`
- `@Target`
- `@Documented`
- `@Inherited`（JDK8 引入）
- `@Repeatable`（JDK8 引入）

这些类型和它们所支持的类在 `java.lang.annotation` 包中可以找到。下面我们看一下每个元注解的作用和相应分参数的使用说明。

### 3.1. @Retention

**[`@Retention`](https://docs.oracle.com/javase/8/docs/api/java/lang/annotation/Retention.html) 指明了注解的保留级别。**

`@Retention` 源码：

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Retention {
    RetentionPolicy value();
}
```

`RetentionPolicy` 是一个枚举类型，它定义了被 `@Retention` 修饰的注解所支持的保留级别：

- `RetentionPolicy.SOURCE` - 标记的注解仅在源文件中有效，编译器会忽略。
- `RetentionPolicy.CLASS` - 标记的注解在 class 文件中有效，JVM 会忽略。
- `RetentionPolicy.RUNTIME` - 标记的注解在运行时有效。

`@Retention` 示例：

```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    public String name() default "fieldName";
    public String setFuncName() default "setField";
    public String getFuncName() default "getField";
    public boolean defaultDBValue() default false;
}
```

### 3.2. @Documented

[`@Documented`](https://docs.oracle.com/javase/8/docs/api/java/lang/annotation/Documented.html) 表示无论何时使用指定的注解，都应使用 Javadoc（默认情况下，注释不包含在 Javadoc 中）。更多内容可以参考：[Javadoc tools page](https://docs.oracle.com/javase/8/docs/technotes/guides/javadoc/index.html)。

`@Documented` 示例：

```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
    public String name() default "fieldName";
    public String setFuncName() default "setField";
    public String getFuncName() default "getField";
    public boolean defaultDBValue() default false;
}
```

### 3.3. @Target

**[`@Target`](https://docs.oracle.com/javase/8/docs/api/java/lang/annotation/Target.html) 指定注解可以修饰的元素类型。**

`@Target` 源码：

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Target {
    ElementType[] value();
}
```

`ElementType` 是一个枚举类型，它定义了被 `@Target` 修饰的注解可以应用的范围：

- `ElementType.ANNOTATION_TYPE` - 标记的注解可以应用于注解类型。
- `ElementType.CONSTRUCTOR` - 标记的注解可以应用于构造函数。
- `ElementType.FIELD` - 标记的注解可以应用于字段或属性。
- `ElementType.LOCAL_VARIABLE` - 标记的注解可以应用于局部变量。
- `ElementType.METHOD` - 标记的注解可以应用于方法。
- `ElementType.PACKAGE` - 标记的注解可以应用于包声明。
- `ElementType.PARAMETER` - 标记的注解可以应用于方法的参数。
- `ElementType.TYPE` - 标记的注解可以应用于类的任何元素。

`@Target` 示例：

```java
@Target(ElementType.TYPE)
public @interface Table {
    /**
     * 数据表名称注解，默认值为类名称
     * @return
     */
    public String tableName() default "className";
}

@Target(ElementType.FIELD)
public @interface NoDBColumn {}
```

### 3.4. @Inherited

**[`@Inherited`](https://docs.oracle.com/javase/8/docs/api/java/lang/annotation/Inherited.html) 表示注解类型可以被继承（默认情况下不是这样）**。

表示自动继承注解类型。 如果注解类型声明中存在 `@Inherited` 元注解，则注解所修饰类的所有子类都将会继承此注解。

> 🔔 注意：`@Inherited` 注解类型是被标注过的类的子类所继承。类并不从它所实现的接口继承注解，方法并不从它所覆写的方法继承注解。
>
> 此外，当 `@Inherited` 类型标注的注解的 `@Retention` 是 `RetentionPolicy.RUNTIME`，则反射 API 增强了这种继承性。如果我们使用 `java.lang.reflect` 去查询一个 `@Inherited` 类型的注解时，反射代码检查将展开工作：检查类和其父类，直到发现指定的注解类型被发现，或者到达类继承结构的顶层。

```java
@Inherited
public @interface Greeting {
    public enum FontColor{ BULE,RED,GREEN};
    String name();
    FontColor fontColor() default FontColor.GREEN;
}
```

### 3.5. @Repeatable

**[`@Repeatable`](https://docs.oracle.com/javase/8/docs/api/java/lang/annotation/Repeatable.html) 表示注解可以重复使用。**

以 Spring `@Scheduled` 为例：

```java
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Schedules {
	Scheduled[] value();
}

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(Schedules.class)
public @interface Scheduled {
  // ...
}
```

应用示例：

```java
public class TaskRunner {

    @Scheduled("0 0/15 * * * ?")
    @Scheduled("0 0 12 * ?")
    public void task1() {}
}
```

## 4. 自定义注解

使用 `@interface` 自定义注解时，自动继承了 `java.lang.annotation.Annotation` 接口，由编译程序自动完成其他细节。在定义注解时，不能继承其他的注解或接口。`@interface` 用来声明一个注解，其中的每一个方法实际上是声明了一个配置参数。方法的名称就是参数的名称，返回值类型就是参数的类型（返回值类型只能是基本类型、Class、String、enum）。可以通过 `default` 来声明参数的默认值。

这里，我会通过实现一个名为 `RegexValid` 的正则校验注解工具来展示自定义注解的全步骤。

### 4.1. 注解的定义

注解的语法格式如下：

```java
public @interface 注解名 {定义体}
```

我们来定义一个注解：

```java
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RegexValid {}
```

> 说明：
>
> 通过上一节对于元注解 [`@Target`](#target)、[`@Retention`](#retention)、[`@Documented`](#documented) 的说明，这里就很容易理解了。
>
> - 上面的代码中定义了一个名为 `@RegexValid` 的注解。
> - `@Documented` 表示 `@RegexValid` 应该使用 javadoc。
> - `@Target({ElementType.FIELD, ElementType.PARAMETER})` 表示 `@RegexValid` 可以在类成员或方法参数上修饰。
> - @Retention(RetentionPolicy.RUNTIME) 表示 `@RegexValid` 在运行时有效。

此时，我们已经定义了一个没有任何属性的注解，如果到此为止，它仅仅是一个标记注解。作为正则工具，没有属性可什么也做不了。接下来，我们将为它添加注解属性。

### 4.2. 注解属性

注解属性的语法形式如下：

```
[访问级别修饰符] [数据类型] 名称() default 默认值;
```

例如，我们要定义在注解中定义一个名为 value 的字符串属性，其默认值为空字符串，访问级别为默认级别，那么应该定义如下：

```
String value() default "";
```

> 🔔 注意：**在注解中，我们定义属性时，属性名后面需要加 `()`**。

定义注解属性有以下要点：

- **注解属性只能使用 `public` 或默认访问级别（即不指定访问级别修饰符）修饰**。
- **注解属性的数据类型有限制要求**。支持的数据类型如下：

  - 所有基本数据类型（byte、char、short、int、long、float、double、boolean）
  - String 类型
  - Class 类
  - enum 类型
  - Annotation 类型
  - 以上所有类型的数组

- **注解属性必须有确定的值，建议指定默认值**。注解属性只能通过指定默认值或使用注解时指定属性值，相较之下，指定默认值的方式更为可靠。注解属性如果是引用类型，不可以为 null。这个约束使得注解处理器很难判断注解属性是默认值，或是使用注解时所指定的属性值。为此，我们设置默认值时，一般会定义一些特殊的值，例如空字符串或者负数。
- 如果注解中只有一个属性值，最好将其命名为 value。因为，指定属性名为 value，在使用注解时，指定 value 的值可以不指定属性名称。

```java
// 这两种方式效果相同
@RegexValid("^((\\+)?86\\s*)?((13[0-9])|(15([0-3]|[5-9]))|(18[0,2,5-9]))\\d{8}$")
@RegexValid(value = "^((\\+)?86\\s*)?((13[0-9])|(15([0-3]|[5-9]))|(18[0,2,5-9]))\\d{8}$")
```

示例：

了解了注解属性的定义要点，让我们来为 `@RegexValid` 注解定义几个属性。

```java
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RegexValid {
    enum Policy {
        // @formatter:off
        EMPTY(null),
        DATE("^(?:(?!0000)[0-9]{4}([-/.]?)(?:(?:0?[1-9]|1[0-2])\\1(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])\\1"
            + "(?:29|30)|(?:0?[13578]|1[02])\\1(?:31))|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|"
            + "(?:0[48]|[2468][048]|[13579][26])00)([-/.]?)0?2\\2(?:29))$"),
        MAIL("^[A-Za-z0-9](([_\\.\\-]?[a-zA-Z0-9]+)*)@([A-Za-z0-9]+)(([\\.\\-]?[a-zA-Z0-9]+)*)\\.([A-Za-z]{2,})$");
        // @formatter:on

        private String policy;

        Policy(String policy) {
            this.policy = policy;
        }

        public String getPolicy() {
            return policy;
        }
    }

    String value() default "";
    Policy policy() default Policy.EMPTY;
}
```

> 说明：
>
> 在上面的示例代码中，我们定义了两个注解属性：`String` 类型的 value 属性和 `Policy` 枚举类型的 policy 属性。`Policy` 枚举中定义了几个默认的正则表达式，这是为了直接使用这几个常用表达式去正则校验。考虑到，我们可能需要自己传入一些自定义正则表达式去校验其他场景，所以定义了 value 属性，允许使用者传入正则表达式。

至此，`@RegexValid` 的声明已经结束。但是，程序仍不知道如何处理 `@RegexValid` 这个注解。我们还需要定义注解处理器。

### 4.3. 注解处理器

如果没有用来读取注解的方法和工作，那么注解也就不会比注释更有用处了。使用注解的过程中，很重要的一部分就是创建于使用注解处理器。JDK5 扩展了反射机制的 API，以帮助程序员快速的构造自定义注解处理器。

**`java.lang.annotation.Annotation` 是一个接口，程序可以通过反射来获取指定程序元素的注解对象，然后通过注解对象来获取注解里面的元数据**。

`Annotation` 接口源码如下：

```java
public interface Annotation {
    boolean equals(Object obj);

    int hashCode();

    String toString();

    Class<? extends Annotation> annotationType();
}
```

除此之外，Java 中支持**注解处理器接口 `java.lang.reflect.AnnotatedElement`** ，该接口代表程序中可以接受注解的程序元素，该接口主要有如下几个实现类：

- `Class` - 类定义
- `Constructor` - 构造器定义
- `Field` - 累的成员变量定义
- `Method` - 类的方法定义
- `Package` - 类的包定义

`java.lang.reflect` 包下主要包含一些实现反射功能的工具类。实际上，`java.lang.reflect` 包所有提供的反射 API 扩充了读取运行时注解信息的能力。当一个注解类型被定义为运行时的注解后，该注解才能是运行时可见，当 class 文件被装载时被保存在 class 文件中的注解才会被虚拟机读取。
`AnnotatedElement` 接口是所有程序元素（Class、Method 和 Constructor）的父接口，所以程序通过反射获取了某个类的`AnnotatedElement` 对象之后，程序就可以调用该对象的如下四个个方法来访问注解信息：

- `getAnnotation` - 返回该程序元素上存在的、指定类型的注解，如果该类型注解不存在，则返回 null。
- `getAnnotations` - 返回该程序元素上存在的所有注解。
- `isAnnotationPresent` - 判断该程序元素上是否包含指定类型的注解，存在则返回 true，否则返回 false。
- `getDeclaredAnnotations` - 返回直接存在于此元素上的所有注释。与此接口中的其他方法不同，该方法将忽略继承的注释。（如果没有注释直接存在于此元素上，则返回长度为零的一个数组。）该方法的调用者可以随意修改返回的数组；这不会对其他调用者返回的数组产生任何影响。

了解了以上内容，让我们来实现 `@RegexValid` 的注解处理器：

```java
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexValidUtil {
    public static boolean check(Object obj) throws Exception {
        boolean result = true;
        StringBuilder sb = new StringBuilder();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 判断成员是否被 @RegexValid 注解所修饰
            if (field.isAnnotationPresent(RegexValid.class)) {
                RegexValid valid = field.getAnnotation(RegexValid.class);

                // 如果 value 为空字符串，说明没有注入自定义正则表达式，改用 policy 属性
                String value = valid.value();
                if ("".equals(value)) {
                    RegexValid.Policy policy = valid.policy();
                    value = policy.getPolicy();
                }

                // 通过设置 setAccessible(true) 来访问私有成员
                field.setAccessible(true);
                Object fieldObj = null;
                try {
                    fieldObj = field.get(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (fieldObj == null) {
                    sb.append("\n")
                        .append(String.format("%s 类中的 %s 字段不能为空！", obj.getClass().getName(), field.getName()));
                    result = false;
                } else {
                    if (fieldObj instanceof String) {
                        String text = (String) fieldObj;
                        Pattern p = Pattern.compile(value);
                        Matcher m = p.matcher(text);
                        result = m.matches();
                        if (!result) {
                            sb.append("\n").append(String.format("%s 不是合法的 %s ！", text, field.getName()));
                        }
                    } else {
                        sb.append("\n").append(
                            String.format("%s 类中的 %s 字段不是字符串类型，不能使用此注解校验！", obj.getClass().getName(), field.getName()));
                        result = false;
                    }
                }
            }
        }

        if (sb.length() > 0) {
            throw new Exception(sb.toString());
        }
        return result;
    }
}
```

> 说明：
>
> 以上示例中的注解处理器，执行步骤如下：
>
> 1. 通过 getDeclaredFields 反射方法获取传入对象的所有成员。
> 2. 遍历成员，使用 isAnnotationPresent 判断成员是否被指定注解所修饰，如果不是，直接跳过。
> 3. 如果成员被注解所修饰，通过 `RegexValid valid = field.getAnnotation(RegexValid.class);` 这样的形式获取，注解实例化对象，然后，就可以使用 `valid.value()` 或 `valid.policy()` 这样的形式获取注解中设定的属性值。
> 4. 根据属性值，进行逻辑处理。

### 4.4. 使用注解

完成了以上工作，我们就可以使用自定义注解了，示例如下：

```java
public class RegexValidDemo {
    static class User {
        private String name;
        @RegexValid(policy = RegexValid.Policy.DATE)
        private String date;
        @RegexValid(policy = RegexValid.Policy.MAIL)
        private String mail;
        @RegexValid("^((\\+)?86\\s*)?((13[0-9])|(15([0-3]|[5-9]))|(18[0,2,5-9]))\\d{8}$")
        private String phone;

        public User(String name, String date, String mail, String phone) {
            this.name = name;
            this.date = date;
            this.mail = mail;
            this.phone = phone;
        }

        @Override
        public String toString() {
            return "User{" + "name='" + name + '\'' + ", date='" + date + '\'' + ", mail='" + mail + '\'' + ", phone='"
                + phone + '\'' + '}';
        }
    }

    static void printDate(@RegexValid(policy = RegexValid.Policy.DATE) String date){
        System.out.println(date);
    }

    public static void main(String[] args) throws Exception {
        User user = new User("Tom", "1990-01-31", "xxx@163.com", "18612341234");
        User user2 = new User("Jack", "2019-02-29", "sadhgs", "183xxxxxxxx");
        if (RegexValidUtil.check(user)) {
            System.out.println(user + "正则校验通过");
        }
        if (RegexValidUtil.check(user2)) {
            System.out.println(user2 + "正则校验通过");
        }
    }
}
```

## 5. 小结

![img](https://raw.githubusercontent.com/dunwu/images/dev/cs/java/javacore/xmind/注解简介.svg)

![img](https://raw.githubusercontent.com/dunwu/images/dev/cs/java/javacore/xmind/元注解.svg)

![img](https://raw.githubusercontent.com/dunwu/images/dev/cs/java/javacore/xmind/内置注解.svg)

![img](https://raw.githubusercontent.com/dunwu/images/dev/cs/java/javacore/xmind/自定义注解.svg)

## 6. 参考资料

- [Java 编程思想](https://book.douban.com/subject/2130190/)
- [Java 核心技术（卷 1）](https://book.douban.com/subject/3146174/)
- [Effective java](https://book.douban.com/subject/3360807/)
- [Oracle 官方文档之注解篇](https://docs.oracle.com/javase/tutorial/java/annotations/)
- [深入理解 Java：注解（Annotation）自定义注解入门](https://www.cnblogs.com/peida/archive/2013/04/24/3036689.html)
- https://blog.csdn.net/briblue/article/details/73824058
