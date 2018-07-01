---
title: 泛型
date: 2018/06/02
categories:
- javacore
tags:
- javacore
---

# 泛型

<!-- TOC depthFrom:2 depthTo:3 -->

- [泛型简介](#泛型简介)
- [泛型接口](#泛型接口)
- [泛型方法](#泛型方法)
    - [泛型方法与可变参数](#泛型方法与可变参数)
- [类型擦除](#类型擦除)
    - [擦除的问题](#擦除的问题)
- [擦除补偿](#擦除补偿)
- [边界](#边界)
- [通配符](#通配符)
- [资料](#资料)

<!-- /TOC -->

## 泛型简介

一般的类和方法，只能使用具体的类型：要么是基本类型，要么是自定义的类。如果要编写可以应用于多种类型的代码，这种刻板的限制对代码的束缚就会很大。

JDK5 引入了泛型机制。泛型实现了参数化类型的概念，使代码可以应用于多种类型。泛型的意思是：适用于各种各样的类型。

泛型是实现容器类的基石。泛型的主要作用之一就是用来指定容器要持有什么类型的对象，而且由编译器来保证类型的正确性。

基本类型无法作为类型参数。

## 泛型接口

泛型可以应用于接口。示例如下：

```java
public interface Generator<T> {
    T next();
}
public class FibonacciGenerator implements Generator<Integer> {
    private int count = 0;

    @Override
    public Integer next() { return fib(count++); }

    private int fib(int n) {
        if (n < 2) {
            return 1;
        }
        return fib(n - 2) + fib(n - 1);
    }

    public static void main(String[] args) {
        FibonacciGenerator gen = new FibonacciGenerator();
        for (int i = 0; i < 18; i++) {
            System.out.print(gen.next() + " ");
        }
    }
}
```

## 泛型方法

是否拥有泛型方法，与其所在的类是否是泛型没有关系。

使用泛型类时，必须在创建对象的时候指定类型参数的值，而使用泛型方法的时候，通常不必指明参数类型，因为编译器会为我们找出具体的类型。这称为类型参数推断（type argument inference）。

```java
public class GenericMethodDemo {
    public <T> void f(T x) {
        System.out.println(x.getClass().getName());
    }

    public static void main(String[] args) {
        GenericMethodDemo gm = new GenericMethodDemo();
        gm.f("");
        gm.f(1);
        gm.f(1.0);
        gm.f(1.0F);
        gm.f('c');
        gm.f(gm);
    }
}
```

类型推断只对赋值操作有效，其他时候并不起作用。如果将一个泛型方法调用的结果作为 参数，传递给另一个方法，这是编译器并不会执行推断。编译器会认为：调用泛型方法后，其返回值被赋给一个 Object 类型的变量。

建议：尽量使用泛型方法，而不是将整个类泛型化。这样，有利于明确泛型化的范围。

### 泛型方法与可变参数

泛型方法与可变参数列表能够很好地共存：

```java
public class GenericVarargs {
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
        ls = makeList("ABCDEFFHIJKLMNOPQRSTUVWXYZ".split(""));
        System.out.println(ls);
    }
}
```

## 类型擦除

先来看一个例子：

```java
public class ErasedTypeEquivalence {
    public static void main(String[] args) {
        Class c1 = new ArrayList<String>().getClass();
        Class c2 = new ArrayList<Integer>().getClass();
        System.out.println(c1 == c2);
    }
}
```

输出结果是 true。

这是因为：**Java 泛型是使用擦除来实现的，使用泛型时，任何具体的类型信息都被擦除了**。这意味着：`ArrayList<String>` 和 `ArrayList<Integer>` 在运行时，JVM 将它们视为同一类型。

Java 泛型中最令人苦恼的地方或许就是类型擦除了，特别是对于有 C++经验的程序员。类型擦除就是说 Java 泛型只能用于在编译期间的静态类型检查，然后编译器生成的代码会擦除相应的类型信息，这样到了运行期间实际上 JVM 根本就不知道泛型所代表的具体类型。这样做的目的是因为 Java 泛型是 JDK5 之后才被引入的，为了保持向下的兼容性，所以只能做类型擦除来兼容以前的非泛型代码。

### 擦除的问题

擦除的代价是显著的。泛型不能用于显式地引用运行时类型的操作之中，例如：转型、instanceof 操作和 new 表达式。因为所有关于参数的类型信息都丢失了，无论如何，当你在编写泛型代码时，必须时刻提醒自己，你只是看起来好像拥有有关参数的类型信息而已。

举例来说：

```java
class Foo<T> { T var; }
```

当创建 Foo 实例时：

```java
Foo<Cat> foo = new Foo<>();
```

虽然，看似是 Cat 替代了 T。但实际上，在运行时，Foo 中的 T 被视为的是 Object。

## 擦除补偿

## 边界

边界使得你可以在用于泛型的参数类型上设置限制条件。

因为擦除移除了类型信息，所以，可以用无界泛型参数调用的方法只是那些可以用 Ojbect 调用的方法。如果能够将这个参数限制为某个类型子集，那么你就可以用这些类型子集来调用方法。为了执行这种限制，Java 泛型重用了 extends 关键字。

## 通配符

## 资料

* [Java 编程思想（Thinking in java）](https://item.jd.com/10058164.html)
