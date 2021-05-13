# 深入理解 Java 枚举

> **📦 本文以及示例源码已归档在 [javacore](https://github.com/dunwu/javacore/)**

<!-- TOC depthFrom:2 depthTo:3 -->

- [1. 简介](#1-简介)
- [2. 枚举的本质](#2-枚举的本质)
- [3. 枚举的方法](#3-枚举的方法)
- [4. 枚举的特性](#4-枚举的特性)
  - [4.1. 基本特性](#41-基本特性)
  - [4.2. 枚举可以添加方法](#42-枚举可以添加方法)
  - [4.3. 枚举可以实现接口](#43-枚举可以实现接口)
  - [4.4. 枚举不可以继承](#44-枚举不可以继承)
- [5. 枚举的应用](#5-枚举的应用)
  - [5.1. 组织常量](#51-组织常量)
  - [5.2. switch 状态机](#52-switch-状态机)
  - [5.3. 错误码](#53-错误码)
  - [5.4. 组织枚举](#54-组织枚举)
  - [5.5. 策略枚举](#55-策略枚举)
  - [5.6. 枚举实现单例模式](#56-枚举实现单例模式)
- [6. 枚举工具类](#6-枚举工具类)
  - [6.1. EnumSet](#61-enumset)
  - [6.2. EnumMap](#62-enummap)
- [7. 小结](#7-小结)
- [8. 参考资料](#8-参考资料)

<!-- /TOC -->

## 1. 简介

`enum` 的全称为 enumeration， 是 JDK5 中引入的特性。

在 Java 中，被 `enum` 关键字修饰的类型就是枚举类型。形式如下：

```java
enum ColorEn { RED, GREEN, BLUE }
```

**枚举的好处**：可以将常量组织起来，统一进行管理。

**枚举的典型应用场景**：错误码、状态机等。

## 2. 枚举的本质

`java.lang.Enum`类声明

```java
public abstract class Enum<E extends Enum<E>>
        implements Comparable<E>, Serializable { ... }
```

新建一个 ColorEn.java 文件，内容如下：

```java
package io.github.dunwu.javacore.enumeration;

public enum ColorEn {
    RED,YELLOW,BLUE
}
```

执行 `javac ColorEn.java` 命令，生成 ColorEn.class 文件。

然后执行 `javap ColorEn.class` 命令，输出如下内容：

```java
Compiled from "ColorEn.java"
public final class io.github.dunwu.javacore.enumeration.ColorEn extends java.lang.Enum<io.github.dunwu.javacore.enumeration.ColorEn> {
  public static final io.github.dunwu.javacore.enumeration.ColorEn RED;
  public static final io.github.dunwu.javacore.enumeration.ColorEn YELLOW;
  public static final io.github.dunwu.javacore.enumeration.ColorEn BLUE;
  public static io.github.dunwu.javacore.enumeration.ColorEn[] values();
  public static io.github.dunwu.javacore.enumeration.ColorEn valueOf(java.lang.String);
  static {};
}
```

> 💡 说明：
>
> 从上面的例子可以看出：
>
> **枚举的本质是 `java.lang.Enum` 的子类。**
>
> 尽管 `enum` 看起来像是一种新的数据类型，事实上，**enum 是一种受限制的类，并且具有自己的方法**。枚举这种特殊的类因为被修饰为 `final`，所以不能继承其他类。
>
> 定义的枚举值，会被默认修饰为 `public static final` ，从修饰关键字，即可看出枚举值本质上是静态常量。

## 3. 枚举的方法

在 enum 中，提供了一些基本方法：

- `values()`：返回 enum 实例的数组，而且该数组中的元素严格保持在 enum 中声明时的顺序。
- `name()`：返回实例名。
- `ordinal()`：返回实例声明时的次序，从 0 开始。
- `getDeclaringClass()`：返回实例所属的 enum 类型。
- `equals()` ：判断是否为同一个对象。

可以使用 `==` 来比较`enum`实例。

此外，`java.lang.Enum`实现了`Comparable`和 `Serializable` 接口，所以也提供 `compareTo()` 方法。

**例：展示 enum 的基本方法**

```java
public class EnumMethodDemo {
    enum Color {RED, GREEN, BLUE;}
    enum Size {BIG, MIDDLE, SMALL;}
    public static void main(String args[]) {
        System.out.println("=========== Print all Color ===========");
        for (Color c : Color.values()) {
            System.out.println(c + " ordinal: " + c.ordinal());
        }
        System.out.println("=========== Print all Size ===========");
        for (Size s : Size.values()) {
            System.out.println(s + " ordinal: " + s.ordinal());
        }

        Color green = Color.GREEN;
        System.out.println("green name(): " + green.name());
        System.out.println("green getDeclaringClass(): " + green.getDeclaringClass());
        System.out.println("green hashCode(): " + green.hashCode());
        System.out.println("green compareTo Color.GREEN: " + green.compareTo(Color.GREEN));
        System.out.println("green equals Color.GREEN: " + green.equals(Color.GREEN));
        System.out.println("green equals Size.MIDDLE: " + green.equals(Size.MIDDLE));
        System.out.println("green equals 1: " + green.equals(1));
        System.out.format("green == Color.BLUE: %b\n", green == Color.BLUE);
    }
}
```

**输出**

```
=========== Print all Color ===========
RED ordinal: 0
GREEN ordinal: 1
BLUE ordinal: 2
=========== Print all Size ===========
BIG ordinal: 0
MIDDLE ordinal: 1
SMALL ordinal: 2
green name(): GREEN
green getDeclaringClass(): class org.zp.javase.enumeration.EnumDemo$Color
green hashCode(): 460141958
green compareTo Color.GREEN: 0
green equals Color.GREEN: true
green equals Size.MIDDLE: false
green equals 1: false
green == Color.BLUE: false
```

## 4. 枚举的特性

枚举的特性，归结起来就是一句话：

> **除了不能继承，基本上可以将 `enum` 看做一个常规的类**。

但是这句话需要拆分去理解，让我们细细道来。

### 4.1. 基本特性

**如果枚举中没有定义方法，也可以在最后一个实例后面加逗号、分号或什么都不加。**

如果枚举中没有定义方法，**枚举值默认为从 0 开始的有序数值**。以 Color 枚举类型举例，它的枚举常量依次为 `RED：0，GREEN：1，BLUE：2`。

### 4.2. 枚举可以添加方法

在概念章节提到了，**枚举值默认为从 0 开始的有序数值** 。那么问题来了：如何为枚举显式的赋值。

（1）**Java 不允许使用 `=` 为枚举常量赋值**

如果你接触过 C/C++，你肯定会很自然的想到赋值符号 `=` 。在 C/C++语言中的 enum，可以用赋值符号`=`显式的为枚举常量赋值；但是 ，很遗憾，**Java 语法中却不允许使用赋值符号 `=` 为枚举常量赋值**。

**例：C/C++ 语言中的枚举声明**

```c
typedef enum {
    ONE = 1,
    TWO,
    THREE = 3,
    TEN = 10
} Number;
```

（2）**枚举可以添加普通方法、静态方法、抽象方法、构造方法**

Java 虽然不能直接为实例赋值，但是它有更优秀的解决方案：**为 enum 添加方法来间接实现显式赋值**。

创建 `enum` 时，可以为其添加多种方法，甚至可以为其添加构造方法。

**注意一个细节：如果要为 enum 定义方法，那么必须在 enum 的最后一个实例尾部添加一个分号。此外，在 enum 中，必须先定义实例，不能将字段或方法定义在实例前面。否则，编译器会报错。**

**例：全面展示如何在枚举中定义普通方法、静态方法、抽象方法、构造方法**

```java
public enum ErrorCodeEn {
    OK(0) {
        @Override
        public String getDescription() {
            return "成功";
        }
    },
    ERROR_A(100) {
        @Override
        public String getDescription() {
            return "错误A";
        }
    },
    ERROR_B(200) {
        @Override
        public String getDescription() {
            return "错误B";
        }
    };

    private int code;

    // 构造方法：enum的构造方法只能被声明为private权限或不声明权限
    private ErrorCodeEn(int number) { // 构造方法
        this.code = number;
    }

    public int getCode() { // 普通方法
        return code;
    } // 普通方法

    public abstract String getDescription(); // 抽象方法

    public static void main(String args[]) { // 静态方法
        for (ErrorCodeEn s : ErrorCodeEn.values()) {
            System.out.println("code: " + s.getCode() + ", description: " + s.getDescription());
        }
    }
}
// Output:
// code: 0, description: 成功
// code: 100, description: 错误A
// code: 200, description: 错误B
```

注：上面的例子并不可取，仅仅是为了展示枚举支持定义各种方法。正确的例子情况[错误码示例](#错误码)

### 4.3. 枚举可以实现接口

**`enum` 可以像一般类一样实现接口。**

同样是实现上一节中的错误码枚举类，通过实现接口，可以约束它的方法。

```java
public interface INumberEnum {
    int getCode();
    String getDescription();
}

public enum ErrorCodeEn2 implements INumberEnum {
    OK(0, "成功"),
    ERROR_A(100, "错误A"),
    ERROR_B(200, "错误B");

    ErrorCodeEn2(int number, String description) {
        this.code = number;
        this.description = description;
    }

    private int code;
    private String description;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
```

### 4.4. 枚举不可以继承

**enum 不可以继承另外一个类，当然，也不能继承另一个 enum 。**

因为 `enum` 实际上都继承自 `java.lang.Enum` 类，而 Java 不支持多重继承，所以 `enum` 不能再继承其他类，当然也不能继承另一个 `enum`。

## 5. 枚举的应用

### 5.1. 组织常量

在 JDK5 之前，在 Java 中定义常量都是`public static final TYPE a;` 这样的形式。有了枚举，你可以将有关联关系的常量组织起来，使代码更加易读、安全，并且还可以使用枚举提供的方法。

下面三种声明方式是等价的：

```java
enum Color { RED, GREEN, BLUE }
enum Color { RED, GREEN, BLUE, }
enum Color { RED, GREEN, BLUE; }
```

### 5.2. switch 状态机

我们经常使用 switch 语句来写状态机。JDK7 以后，switch 已经支持 `int`、`char`、`String`、`enum` 类型的参数。这几种类型的参数比较起来，使用枚举的 switch 代码更具有可读性。

```java
public class StateMachineDemo {
    public enum Signal {
        GREEN, YELLOW, RED
    }

    public static String getTrafficInstruct(Signal signal) {
        String instruct = "信号灯故障";
        switch (signal) {
            case RED:
                instruct = "红灯停";
                break;
            case YELLOW:
                instruct = "黄灯请注意";
                break;
            case GREEN:
                instruct = "绿灯行";
                break;
            default:
                break;
        }
        return instruct;
    }

    public static void main(String[] args) {
        System.out.println(getTrafficInstruct(Signal.RED));
    }
}
// Output:
// 红灯停
```

### 5.3. 错误码

枚举常被用于定义程序错误码。下面是一个简单示例：

```java
public class ErrorCodeEnumDemo {
    enum ErrorCodeEn {
        OK(0, "成功"),
        ERROR_A(100, "错误A"),
        ERROR_B(200, "错误B");

        ErrorCodeEn(int number, String msg) {
            this.code = number;
            this.msg = msg;
        }

        private int code;
        private String msg;

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        @Override
        public String toString() {
            return "ErrorCodeEn{" + "code=" + code + ", msg='" + msg + '\'' + '}';
        }

        public static String toStringAll() {
            StringBuilder sb = new StringBuilder();
            sb.append("ErrorCodeEn All Elements: [");
            for (ErrorCodeEn code : ErrorCodeEn.values()) {
                sb.append(code.getCode()).append(", ");
            }
            sb.append("]");
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        System.out.println(ErrorCodeEn.toStringAll());
        for (ErrorCodeEn s : ErrorCodeEn.values()) {
            System.out.println(s);
        }
    }
}
// Output:
// ErrorCodeEn All Elements: [0, 100, 200, ]
// ErrorCodeEn{code=0, msg='成功'}
// ErrorCodeEn{code=100, msg='错误A'}
// ErrorCodeEn{code=200, msg='错误B'}
```

### 5.4. 组织枚举

可以将类型相近的枚举通过接口或类组织起来，但是一般用接口方式进行组织。

原因是：Java 接口在编译时会自动为 enum 类型加上`public static`修饰符；Java 类在编译时会自动为 `enum` 类型加上 static 修饰符。看出差异了吗？没错，就是说，在类中组织 `enum`，如果你不给它修饰为 `public`，那么只能在本包中进行访问。

**例：在接口中组织 enum**

```java
public class EnumInInterfaceDemo {
    public interface INumberEnum {
        int getCode();
        String getDescription();
    }


    public interface Plant {
        enum Vegetable implements INumberEnum {
            POTATO(0, "土豆"),
            TOMATO(0, "西红柿");

            Vegetable(int number, String description) {
                this.code = number;
                this.description = description;
            }

            private int code;
            private String description;

            @Override
            public int getCode() {
                return this.code;
            }

            @Override
            public String getDescription() {
                return this.description;
            }
        }


        enum Fruit implements INumberEnum {
            APPLE(0, "苹果"),
            ORANGE(0, "桔子"),
            BANANA(0, "香蕉");

            Fruit(int number, String description) {
                this.code = number;
                this.description = description;
            }

            private int code;
            private String description;

            @Override
            public int getCode() {
                return this.code;
            }

            @Override
            public String getDescription() {
                return this.description;
            }
        }
    }

    public static void main(String[] args) {
        for (Plant.Fruit f : Plant.Fruit.values()) {
            System.out.println(f.getDescription());
        }
    }
}
// Output:
// 苹果
// 桔子
// 香蕉
```

**例：在类中组织 enum**

本例和上例效果相同。

```java
public class EnumInClassDemo {
    public interface INumberEnum {
        int getCode();
        String getDescription();
    }

    public static class Plant2 {
        enum Vegetable implements INumberEnum {
            // 略，与上面完全相同
        }
        enum Fruit implements INumberEnum {
            // 略，与上面完全相同
        }
    }

    // 略
}
// Output:
// 土豆
// 西红柿
```

### 5.5. 策略枚举

Effective Java 中展示了一种策略枚举。这种枚举通过枚举嵌套枚举的方式，将枚举常量分类处理。

这种做法虽然没有 switch 语句简洁，但是更加安全、灵活。

**例：EffectvieJava 中的策略枚举范例**

```java
enum PayrollDay {
    MONDAY(PayType.WEEKDAY), TUESDAY(PayType.WEEKDAY), WEDNESDAY(
            PayType.WEEKDAY), THURSDAY(PayType.WEEKDAY), FRIDAY(PayType.WEEKDAY), SATURDAY(
            PayType.WEEKEND), SUNDAY(PayType.WEEKEND);

    private final PayType payType;

    PayrollDay(PayType payType) {
        this.payType = payType;
    }

    double pay(double hoursWorked, double payRate) {
        return payType.pay(hoursWorked, payRate);
    }

    // 策略枚举
    private enum PayType {
        WEEKDAY {
            double overtimePay(double hours, double payRate) {
                return hours <= HOURS_PER_SHIFT ? 0 : (hours - HOURS_PER_SHIFT)
                        * payRate / 2;
            }
        },
        WEEKEND {
            double overtimePay(double hours, double payRate) {
                return hours * payRate / 2;
            }
        };
        private static final int HOURS_PER_SHIFT = 8;

        abstract double overtimePay(double hrs, double payRate);

        double pay(double hoursWorked, double payRate) {
            double basePay = hoursWorked * payRate;
            return basePay + overtimePay(hoursWorked, payRate);
        }
    }
}
```

**测试**

```java
System.out.println("时薪100的人在周五工作8小时的收入：" + PayrollDay.FRIDAY.pay(8.0, 100));
System.out.println("时薪100的人在周六工作8小时的收入：" + PayrollDay.SATURDAY.pay(8.0, 100));
```

### 5.6. 枚举实现单例模式

单例模式是最常用的设计模式。

单例模式在并发环境下存在线程安全问题。

为了线程安全问题，传统做法有以下几种：

- 饿汉式加载
- 懒汉式 synchronize 和双重检查
- 利用 java 的静态加载机制

相比上述的方法，使用枚举也可以实现单例，而且还更加简单：

```java
public class SingleEnumDemo {
    public enum SingleEn {

        INSTANCE;

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) {
        SingleEn.INSTANCE.setName("zp");
        System.out.println(SingleEn.INSTANCE.getName());
    }
}
```

> 扩展阅读：[深入理解 Java 枚举类型(enum)](https://blog.csdn.net/javazejian/article/details/71333103#enumset%E5%AE%9E%E7%8E%B0%E5%8E%9F%E7%90%86%E5%89%96%E6%9E%90)
>
> 这篇文章对于 Java 枚举的特性讲解很仔细，其中对于枚举实现单例和传统单例实现方式说的尤为细致。

## 6. 枚举工具类

Java 中提供了两个方便操作 enum 的工具类——`EnumSet` 和 `EnumMap`。

### 6.1. EnumSet

`EnumSet` 是枚举类型的高性能 `Set` 实现。它要求放入它的枚举常量必须属于同一枚举类型。

主要接口：

- `noneOf` - 创建一个具有指定元素类型的空 EnumSet
- `allOf` - 创建一个指定元素类型并包含所有枚举值的 EnumSet
- `range` - 创建一个包括枚举值中指定范围元素的 EnumSet
- `complementOf` - 初始集合包括指定集合的补集
- `of` - 创建一个包括参数中所有元素的 EnumSet
- `copyOf` - 创建一个包含参数容器中的所有元素的 EnumSet

示例：

```java
public class EnumSetDemo {
    public static void main(String[] args) {
        System.out.println("EnumSet展示");
        EnumSet<ErrorCodeEn> errSet = EnumSet.allOf(ErrorCodeEn.class);
        for (ErrorCodeEn e : errSet) {
            System.out.println(e.name() + " : " + e.ordinal());
        }
    }
}
```

### 6.2. EnumMap

`EnumMap` 是专门为枚举类型量身定做的 `Map` 实现。虽然使用其它的 Map 实现（如 HashMap）也能完成枚举类型实例到值得映射，但是使用 EnumMap 会更加高效：它只能接收同一枚举类型的实例作为键值，并且由于枚举类型实例的数量相对固定并且有限，所以 EnumMap 使用数组来存放与枚举类型对应的值。这使得 EnumMap 的效率非常高。

主要接口：

- `size` - 返回键值对数
- `containsValue` - 是否存在指定的 value
- `containsKey` - 是否存在指定的 key
- `get` - 根据指定 key 获取 value
- `put` - 取出指定的键值对
- `remove` - 删除指定 key
- `putAll` - 批量取出键值对
- `clear` - 清除数据
- `keySet` - 获取 key 集合
- `values` - 返回所有

示例：

```java
public class EnumMapDemo {
    public enum Signal {
        GREEN, YELLOW, RED
    }

    public static void main(String[] args) {
        System.out.println("EnumMap展示");
        EnumMap<Signal, String> errMap = new EnumMap(Signal.class);
        errMap.put(Signal.RED, "红灯");
        errMap.put(Signal.YELLOW, "黄灯");
        errMap.put(Signal.GREEN, "绿灯");
        for (Iterator<Map.Entry<Signal, String>> iter = errMap.entrySet().iterator(); iter.hasNext();) {
            Map.Entry<Signal, String> entry = iter.next();
            System.out.println(entry.getKey().name() + " : " + entry.getValue());
        }
    }
}
```

> 扩展阅读：[深入理解 Java 枚举类型(enum)](https://blog.csdn.net/javazejian/article/details/71333103#enumset%E5%AE%9E%E7%8E%B0%E5%8E%9F%E7%90%86%E5%89%96%E6%9E%90)
>
> 这篇文章中对 EnumSet 和 EnumMap 原理做了较为详细的介绍。

## 7. 小结

![img](https://raw.githubusercontent.com/dunwu/images/dev/snap/1553002212154.png)

## 8. 参考资料

- [Java 编程思想](https://book.douban.com/subject/2130190/)
- [Java 核心技术（卷 1）](https://book.douban.com/subject/3146174/)
- [Effective java](https://book.douban.com/subject/3360807/)
- [深入理解 Java 枚举类型(enum)](https://blog.csdn.net/javazejian/article/details/71333103#enumset%E5%AE%9E%E7%8E%B0%E5%8E%9F%E7%90%86%E5%89%96%E6%9E%90)
- https://droidyue.com/blog/2016/11/29/dive-into-enum/
