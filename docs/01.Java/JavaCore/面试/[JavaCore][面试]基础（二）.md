---
title: Java 基础面试二
cover: https://raw.githubusercontent.com/dunwu/images/master/archive/2025/03/020ab2bf4af8401590e0291a34f873f8.jpg
date: 2024-07-03 07:44:02
order: 2
categories:
  - Java
  - JavaCore
  - 面试
tags:
  - Java
  - JavaCore
  - 面试
permalink: /pages/e04a6099/
---

# Java 基础面试二

## Java 面向对象

### 【简单】public、private、protected，以及无修饰符有什么区别？⭐⭐⭐

- `private` 只允许当前类可以访问。
- 无修饰只允许同一个包中的类访问。
- `protected` 只允许当前类、子类和同一个包中的类访问。
- `public` 允许任意类和对象访问。

### 【简单】对象实体与对象引用有何不同？⭐⭐

（1）**对象是用来描述客观事物的一个抽象**。一个对象由一组属性和对这组属性进行操作的一组服务组成。

（2）**类是具有相同属性和方法的一组对象的集合**，它为属于该类的所有对象提供了统一的抽象描述，其内部包括属性和方法两个主要部分。

（3）对象实体与对象引用的不同之处在于：

- `new` 创建对象实例（对象实例在堆内存中），对象引用指向对象实例（对象引用存放在栈内存中）
- 一个对象引用可以指向 0 个或 1 个对象（一根绳子可以不系气球，也可以系一个气球）；
- 一个对象可以有 n 个引用指向它（可以用 n 条绳子系住一个气球）。

### 【简单】接口和抽象类有什么区别？⭐⭐⭐⭐

**核心结论**：Java 8 之前选型边界清晰（接口=行为契约，抽象类=代码复用），**Java 8 `default` 方法使接口也能提供实现，选型边界模糊化**。面试要从"背区别"升级到"讲设计决策"。

#### 一、基础对比

| **维度**           | **接口（Interface）**                   | **抽象类（Abstract Class）**      |
| ------------------ | --------------------------------------- | --------------------------------- |
| **本质**           | 行为契约（What to do）                  | 代码复用 + 行为契约（How + What） |
| **实例化**         | ❌ 不可                                 | ❌ 不可                           |
| **构造器**         | ❌ 无（无实例状态）                     | ✔️ 有（子类 `super()` 调用）      |
| **成员变量**       | 仅 `public static final` 常量           | 任意（private/protected/public）  |
| **方法（JDK 7-）** | 仅抽象方法                              | 抽象 + 具体方法                   |
| **方法（JDK 8+）** | 抽象 + `default` + `static`             | 同 JDK 7                          |
| **方法（JDK 9+）** | + `private` 方法（供 default 复用）     | 同 JDK 7                          |
| **继承/实现**      | 类可 `implements` 多个接口              | 类只能 `extends` 一个抽象类       |
| **访问修饰符**     | 方法默认 `public`（JDK 9 private 除外） | 任意                              |
| **设计目的**       | 定义**跨层次**的可选行为                | 提取**同一层次**的共性代码        |

#### 二、Java 8 带来的范式转变：default 方法

JDK 8 引入 `default` 方法的**根本原因**：**二进制兼容性**。

**问题的起点**——Java 8 要给 `Collection` 接口新增 `stream()` 方法，但 `Collection` 有大量第三方实现（如 Guava、Apache Commons、用户自定义集合类）。如果 `stream()` 是抽象方法，**所有未重新编译的旧代码运行时会直接抛出 `AbstractMethodError`，整个生态瞬间崩溃**。

```java
// JDK 8 对 Collection 接口的改动
public interface Collection<E> extends Iterable<E> {
    // 旧接口中的抽象方法（实现类需自行实现）
    int size();
    boolean isEmpty();

    // JDK 8 新增 —— 必须用 default，否则破坏所有第三方实现
    default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default Stream<E> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    // 甚至可以在接口中提供 removeIf 的默认实现
    default boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        Iterator<E> each = iterator();
        while (each.hasNext()) {
            if (filter.test(each.next())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }
}
```

> **📌 度量标准**：`AbstractMethodError` 是 JVM 层的致命错误——已编译的 `.class` 文件的 `invokevirtual` 指令指向不存在的方法，直接导致进程崩溃。这就是为什么"接口演进"在 JDK 8 前是 Java 生态的头号痛点。

#### 三、设计决策框架：何时用接口 vs 抽象类？

**首选接口的三个理由**（《Effective Java》Item 20）：

1. **灵活性**：一个类可实现多个接口，但只能继承一个类。接口不消耗"唯一的继承位置"。
2. **可演进性**：`default` 方法允许接口持续演进而不破坏实现者。
3. **可组合性**：通过接口组合（`implements A, B, C`）组装能力，比继承层次更灵活。

**仍需抽象类的场景**：

| **场景**                                        | **为什么抽象类更好**                                                 |
| ----------------------------------------------- | -------------------------------------------------------------------- |
| 需要**非 static 成员变量**时                    | 接口只能有常量。如 `AbstractMap` 持有 `size` 字段                    |
| 需要在**构造器中执行初始化逻辑**                | 接口无构造器。如 `AbstractList` 的 `modCount` 初始化                 |
| 需要 `protected` 成员（内部可访问、外部不可见） | 接口方法只能是 `public`（JDK 9+ 支持 private，但仍不支持 protected） |
| 骨架实现（Skeletal Implementation）             | 同时提供接口 + 抽象骨架类，如 `AbstractList` + `List` 接口           |

**经典骨架实现模式**：

```java
// 接口定义契约
public interface List<E> extends Collection<E> { ... }

// 抽象骨架类提供通用实现（减小实现者的工作量）
public abstract class AbstractList<E> extends AbstractCollection<E> implements List<E> {
    // 提供了 iterator、size 以外的所有 List 方法实现
    // 子类只需实现 iterator() 和 size() 两个方法
}

// 用户自定义列表只需继承骨架类
class MyList<E> extends AbstractList<E> {
    // 只需实现 get() 和 size()，其他方法从 AbstractList 继承
}
```

#### 四、default 方法的层叠规则（菱形继承的解决方案）

当类实现多个接口且它们有同名 default 方法时，Java 采用**类优先 + 显式覆盖**策略：

```java
interface A { default void hello() { System.out.println("A"); } }
interface B extends A { default void hello() { System.out.println("B"); } }
interface C { default void hello() { System.out.println("C"); } }

// 情况1：子接口覆盖父接口 → 优先子接口
class D implements B { }
new D().hello();  // 输出 "B"（B 覆盖了 A）

// 情况2：两个无关接口的 default 冲突 → 编译强制覆盖
class E implements A, C {
    // ❌ 不覆盖则编译错误："class E inherits unrelated defaults for hello()"
    @Override
    public void hello() {
        A.super.hello();  // 或 C.super.hello()，显式选择
    }
}
```

#### 五、常见误区

| **误区**                 | **真相**                                                              |
| ------------------------ | --------------------------------------------------------------------- |
| "接口只能有抽象方法"     | JDK 8+ 支持 default/static 方法，JDK 9+ 支持 private 方法             |
| "抽象类必须有抽象方法"   | 可以所有方法都是具体的（如工具类模板）                                |
| "接口 = 纯抽象类"        | JDK 8 以前相近，之后接口能力大幅增强，两者定位分化                    |
| "default 方法破坏单继承" | default 只影响行为继承，不影响状态继承（无成员变量），Java 仍是单继承 |

#### 六、跨语言视角：Scala trait vs Java Interface

Java `default` 方法最常被与 Scala 的 `trait` 对比——但二者有一个**核心差异**：

**Scala trait 可以持有状态（`val` 成员），Java 接口不能。**

```scala
// Scala trait — 可以拥有具体的 val 成员变量
trait Logger {
  val prefix: String = "[LOG]"      // ← Scala trait 有状态！
  def log(msg: String) = println(s"$prefix $msg")
}
```

这导致 Java 接口在某些设计场景下不得不退回到**抽象类 + 接口骨架**模式（如 `AbstractList`），而 Scala trait 可以直接承载一切。Java 设计者刻意不引入 trait 式的状态能力，是为了保持接口语义的纯粹性：**接口 = 行为契约，不应有实例状态**。这是哲学选择，不是技术限制。

#### 七、Sealed Classes + Pattern Matching：代数数据类型（ADT）的 Java 表达

JDK 17 引入的 `sealed class`/`sealed interface` + JDK 21 的 Pattern Matching for switch 组合后，可以实现函数式编程中经典的**代数数据类型（ADT）**模式：

```java
// 密封接口 + record 实现类 = ADT
sealed interface Expr permits Const, Add, Mul {}
record Const(int val) implements Expr {}
record Add(Expr left, Expr right) implements Expr {}
record Mul(Expr left, Expr right) implements Expr {}

// 编译器检查穷举性——遗漏任何一个子类型就报错
int eval(Expr e) {
    return switch (e) {
        case Const c -> c.val();
        case Add  a -> eval(a.left()) + eval(a.right());
        case Mul  m -> eval(m.left()) * eval(m.right());
        // 无需 default！编译器已穷举所有子类型
    };
}
```

这与 Kotlin 的 `sealed class` + `when`、Scala 的 `sealed trait` + `match`、Rust 的 `enum` + `match` 是同一模式。JDK 17 以前的 Java 只能用 Visitor 模式模拟 ADT，代码量是天壤之别。

### 【中等】什么是 Java 内部类？内部类有什么作用？⭐⭐

::: info 什么是内部类？
:::

内部类 (Inner Class) 是定义在另一个类内部的类 .java 中有四种类型的内部类：

- **成员内部类**：作为外部类的成员存在
- **局部内部类**：定义在方法或作用域内的类
- **匿名内部类**：没有名字的局部内部类
- **静态嵌套类**：用 static 修饰的嵌套类

::: info 内部类有什么作用？
:::

- **逻辑聚合**：当某个类只对另一个类有用时，可以将其嵌入使用它的类中，保持代码在一起
- **强化封装**：内部类可以访问外部类的私有成员，同时自身也可以对外部完全隐藏
- **间接实现多重继承**：通过内部类可以间接实现多重继承的效果
- **回调机制**：常用于事件处理和监听器实现
- **代码简化**：特别是匿名内部类可以减少代码量

::: info 内部类有哪些特点？
:::

- 内部类可以访问外部类的所有成员（包括 private)
- 外部类需要通过实例化内部类来访问其成员
- 内部类编译后会生成独立的 .class 文件（格式：`OuterClass$InnerClass.class`)
- 非静态内部类不能有静态成员（静态内部类可以）
- 内部类可以继承其他类或实现接口

### 【中等】四种内部类有什么区别？⭐

| **类型**       | **声明位置**  | **static** | **访问外部成员**      | **创建方式**        | **典型用途**    |
| -------------- | ------------- | ---------- | --------------------- | ------------------- | --------------- |
| **成员内部类** | 类中成员位置  | 否         | 全部（含 private）    | `outer.new Inner()` | 关联外部实例    |
| **静态嵌套类** | 类中成员位置  | 是         | 仅静态成员            | `new Outer.Inner()` | Builder、工具类 |
| **局部内部类** | 方法/代码块中 | 否         | 全部 + final 局部变量 | 方法内 new          | 临时封装        |
| **匿名内部类** | 表达式中      | -          | 全部 + final 局部变量 | `new Type() {...}`  | 回调、监听      |

**关键细节**：

- **非静态内部类隐式持有外部类引用**：可能导致内存泄漏（如 Android Handler 持有 Activity）。
- **局部/匿名内部类访问的局部变量必须 final（或 effectively final）**：因局部变量在栈上，内部类对象生命周期可能超出方法。
- **Lambda 可替代匿名内部类**：函数式接口场景优先用 Lambda。

### 【简单】为什么 Java 不支持多重继承？⭐⭐

Java 不支持多重继承的核心原因是**为了避免【菱形继承问题（Diamond Problem）】**。

::: info 什么是菱形继承问题？
:::

菱形继承存在歧义性：

- 如果类 C 继承自类 A 和类 B，而 A 和 B 都有同名方法 `method()`
- 调用 `C.method()` 时无法确定应该调用 A 还是 B 的版本

由于菱形继承歧义性而引发的复杂性增加问题：

- 多重继承会显著增加编译器和 JVM 的实现复杂度
- 方法调用、构造函数调用顺序变得难以确定

::: info Java 如何解决多重继承？
:::

在 Java 中，类可以实现多个接口。接口提供多重继承的行为规范，但不包含具体实现。

JDK8 之后，接口支持默认方法（default），是不是又出现了菱形继承问题？

为了规避这个问题，Java 强制规定，如果多个接口存在相同的默认方法，子类必须重写这个方法。否则，编译器会报错。

### 【中等】深拷贝和浅拷贝有什么区别？⭐⭐⭐

::: info 深拷贝和浅拷贝有什么区别？
:::

| **关键点**       | **浅拷贝**                           | **深拷贝**                       |
| :--------------- | :----------------------------------- | :------------------------------- |
| **复制对象**     | 只复制对象本身（基本类型值拷贝）     | 递归复制对象及其引用的所有子对象 |
| **引用类型字段** | 新旧对象共享同一引用（修改相互影响） | 创建全新引用对象（修改完全隔离） |
| **内存开销**     | 小（仅复制一层）                     | 大（递归复制所有关联对象）       |
| **实现方式**     | 默认`Object.clone()`                 | 需手动实现递归克隆/序列化/工具类 |
| **适用场景**     | 对象无可变引用字段                   | 对象含可变引用字段且需完全独立   |

**本质区别**：浅拷贝是"复制钥匙"，深拷贝是"复制钥匙+保险箱"。

**注意事项**：

- 深拷贝需处理循环引用问题
- 推荐使用`SerializationUtils.clone()`或 JSON 序列化实现深拷贝
- 不可变对象（如 String）的浅拷贝是安全的

::: info 深拷贝和浅拷贝实现方式有什么区别？
:::

**实现方式对比**

| **方法**                     | **浅拷贝** | **深拷贝** | **说明**                      |
| :--------------------------- | :--------- | :--------- | :---------------------------- |
| `Object.clone()`             | ✓          | ✗          | 默认浅拷贝                    |
| **手动递归克隆**             | ✗          | ✓          | 需所有引用类型实现`Cloneable` |
| **序列化反序列化**           | ✗          | ✓          | 通过`ObjectOutputStream`实现  |
| **工具类（Apache Commons）** | ✗          | ✓          | `SerializationUtils.clone()`  |

::: code-tabs#深拷贝和浅拷贝实现示例

@tab 浅拷贝实现

```java
class Person implements Cloneable {
    String name;
    Address address; // 引用类型字段

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone(); // 默认浅拷贝
    }
}

// 测试
Person p1 = new Person("Alice", new Address("北京"));
Person p2 = (Person)p1.clone();
p2.address.city = "上海"; // p1.address.city 也会变成"上海"
```

@tab 深拷贝实现

```java
@Override
protected Object clone() throws CloneNotSupportedException {
    Person cloned = (Person)super.clone();
    cloned.address = (Address)address.clone(); // 手动复制引用对象
    return cloned;
}

// Address 类也需实现 Cloneable
class Address implements Cloneable {
    String city;
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
```

:::

### 【简单】面向对象和面向过程有什么区别？⭐⭐

面向对象和面向过程的主要区别：

| 维度         | 面向对象（OOP）               | 面向过程（POP）          |
| ------------ | ----------------------------- | ------------------------ |
| **核心思想** | 以**对象**为中心              | 以**步骤**为中心         |
| **代码组织** | 按**现实实体**抽象为类        | 按**功能流程**拆分为函数 |
| **数据管理** | 数据与行为封装在对象中        | 数据与函数独立           |
| **扩展方式** | 通过继承/多态扩展（开闭原则） | 需修改函数逻辑           |
| **典型特性** | 封装、继承、多态三大特性      | 无三大特性               |
| **典型语言** | Java, Python, C++             | C, Pascal                |

### 【中等】面向对象三大特征和五大原则是什么？⭐⭐⭐

::: info 面向对象三大特征是什么？
:::

**面向对象三大特征：**

- **封装（Encapsulation）** ：**隐藏内部细节，暴露安全接口**。

  - 用 `private` 保护数据，通过 `getter/setter` 控制访问
  - 示例：`BankAccount` 类隐藏余额，提供 `deposit()`/`withdraw()` 方法

- **继承（Inheritance）** ：**子类复用父类属性和方法**。

  - 通过 `extends` 实现（如 `Dog extends Animal`）
  - 注意：Java 是单继承（一个子类只能有一个父类）

- **多态（Polymorphism）** ：**同一行为的不同实现方式**。
  - **编译时多态**：方法重载（`Overload`）
  - **运行时多态**：方法重写（`Override`）+ 向上转型（子类对象转为父类对象，如 `Animal a = new Dog(); a.sound();`）

**一言以概之**：**封装保证安全性，继承提高复用性，多态增强扩展性**。

::: info 面向对象的五大原则是什么？
:::

面向对象的五大原则是 **SOLID** 原则：

- **单一职责原则 (SRP)**：**一个类只负责一个功能**，避免职责过多导致代码臃肿。
- **开闭原则 (OCP)**：**对扩展开放，对修改关闭**。通过抽象和继承扩展功能，而非直接修改原有代码。
- **里氏替换原则 (LSP)**：**子类必须能替换父类**，确保继承关系不会破坏程序逻辑。
- **接口隔离原则 (ISP)**：**接口应当小而专**，避免臃肿接口强制实现不必要的方法。
- **依赖倒置原则 (DIP)**：**依赖抽象而非具体**，高层模块不直接依赖低层模块，而是通过接口或抽象类交互。

**一言以概之**：SOLID 原则让代码更灵活、可维护、易扩展。

### 【简单】Java 中 final 关键字有什么用？⭐⭐

`final` 关键字表示**不可变性约束**，可修饰类、方法、变量，是 Java 设计"不可变"的基础。

**三种用法详解**：

| **修饰目标** | **效果**                         | **典型示例**                     |
| ------------ | -------------------------------- | -------------------------------- |
| **类**       | 不能被继承（防止子类破坏不变性） | `String`、`Integer`、`LocalDate` |
| **方法**     | 不能被子类重写（防止行为被篡改） | `Object.getClass()`              |
| **变量**     | 只能赋值一次（常量）             | `static final int MAX = 100;`    |

**变量修饰的细节**：

- **基本类型**：值不可变。
- **引用类型**：**引用不可变，但对象内容可变**（易踩坑）。

```java
final List<String> list = new ArrayList<>();
list.add("A");      // ✔️ 合法，修改的是对象内容
list = new ArrayList<>();  // ❌ 编译错误，引用不可重新赋值
```

- **局部变量**：使用前必须赋值（可在声明后赋值一次）。
- **方法参数**：`final` 参数在方法内不可重新赋值（常用于匿名内部类捕获变量）。

**final 与 JVM 优化**：

- **内存语义**：JDK 5+ 的 Java 内存模型（JMM）保证 `final` 字段的写操作在构造函数返回前完成，且对所有线程可见（**安全发布**）。
- **内联优化**：JIT 编译器可对 `final` 方法进行更激进的**内联优化**（无需动态分派）。

**final vs finally vs finalize**：三者**毫无关联**，仅命名相似。详见异常章节。

### 【中等】`this` 和 `super` 关键字有什么用？⭐⭐

`this` 和 `super` 是 Java 用于**引用当前对象**和**父类成员**的关键字。

**`this` 的用途**：

| **场景**                 | **示例**                  | **说明**                 |
| ------------------------ | ------------------------- | ------------------------ |
| 区分成员变量与局部变量   | `this.name = name;`       | 形参名与字段名相同时     |
| 调用本类其他构造器       | `this(args);`             | 必须在构造器首行         |
| 返回当前对象引用         | `return this;`            | 链式调用（Builder 模式） |
| 作为参数传递             | `service.register(this);` | 传递当前实例             |
| 访问外部类（内部类场景） | `Outer.this.field`        | 内部类引用外部类         |

**`super` 的用途**：

| **场景**             | **示例**          | **说明**             |
| -------------------- | ----------------- | -------------------- |
| 调用父类构造器       | `super(args);`    | 必须在子类构造器首行 |
| 访问父类被遮蔽的字段 | `super.field`     | 子类有同名字段时     |
| 调用父类被覆盖的方法 | `super.method();` | 显式调父类实现       |

**关键规则**：

```java
public class Animal {
    public Animal() { this("default"); }  // 调用另一个构造器
    public Animal(String name) { /* ... */ }
}

public class Dog extends Animal {
    public Dog() {
        super();  // ✔️ 显式调父类无参构造（必须在首行）
        // this(); // ❌ 与 super 冲突，不能同时存在
    }
}
```

**隐式调用规则**：

- 子类构造器**默认第一行**调用 `super()`（无参），若父类无无参构造则编译错误。
- 解决：父类必须显式声明无参构造，或子类用 `super(args)` 调用有参构造。

### 【中等】接口的默认方法冲突如何解决？⭐⭐

Java 8 引入接口默认方法后，类实现多个接口时可能出现**默认方法冲突**（Diamond Problem 变种）。

**冲突场景一：两个接口有相同默认方法**

```java
interface A {
    default void hello() { System.out.println("A"); }
}
interface B {
    default void hello() { System.out.println("B"); }
}

// ❌ 编译错误：必须覆盖解决冲突
class C implements A, B {}

// ✔️ 解决方案1：覆盖
class C implements A, B {
    @Override
    public void hello() { System.out.println("C"); }
}

// ✔️ 解决方案2：显式指定调用某个接口
class C implements A, B {
    @Override
    public void hello() {
        A.super.hello();  // 调用 A 的默认方法
    }
}
```

**冲突场景二：父类方法 vs 接口默认方法**

```java
class Parent {
    public void hello() { System.out.println("Parent"); }
}
interface I {
    default void hello() { System.out.println("Interface"); }
}

class Child extends Parent implements I {}
new Child().hello();  // 输出 "Parent"（类方法优先于接口默认方法）
```

**核心规则（优先级）**：

1. **类方法 > 接口默认方法**（类优先原则，保证向后兼容）。
2. **子接口 > 父接口**（更具体的接口胜出）。
3. **冲突时必须显式覆盖**（编译器不替你选择）。

### 【中等】Java 多态的实现原理是什么？⭐⭐⭐

多态是 OOP 的核心特性之一，Java 通过**动态分派**实现运行时多态。

**多态分类**：

| **类型**       | **机制**                       | **示例**                           | **判定时机**   |
| -------------- | ------------------------------ | ---------------------------------- | -------------- |
| **编译时多态** | 方法重载（Overload）           | `print(int)` vs `print(String)`    | 编译期静态绑定 |
| **运行时多态** | 方法重写（Override）+ 向上转型 | `Animal a = new Dog(); a.sound();` | 运行期动态绑定 |

**运行时多态的三个前提**：

1. **继承关系**（extends 或 implements）。
2. **方法重写**（子类覆盖父类方法）。
3. **父类引用指向子类对象**（向上转型）。

**底层实现原理（基于虚方法表 vtable）**：

```java
Animal a = new Dog();
a.sound();  // 实际调用 Dog.sound()
```

JVM 实现动态分派的机制：

- **类加载时**：JVM 为每个类生成**虚方法表（vtable）**，存储该类所有虚方法的直接引用。
- **方法调用时**：通过对象的**运行时类型**查找其 vtable，定位实际方法。
- **非虚方法**：`static`、`final`、`private` 方法不进入 vtable（静态绑定）。

**字节码层面**：

```java
// 字节码使用 invokevirtual 指令
invokevirtual #16 // Method Animal.sound:()V
```

`invokevirtual` 在运行时根据对象的实际类型（`Dog`）查找 vtable，调用 `Dog.sound()`。

**字段不参与多态**：

```java
class Father { int num = 1; }
class Son extends Father { int num = 2; }

Father f = new Son();
System.out.println(f.num);  // 输出 1（字段访问基于编译时类型，静态绑定）
```

**多态的经典陷阱：构造器中的多态调用**

```java
class Base {
    Base() { init(); }  // 构造器调用虚方法，危险！
    void init() { System.out.println("Base.init"); }
}
class Sub extends Base {
    void init() { System.out.println("Sub.init"); }
}

new Sub();  // 输出 "Sub.init"（父类构造器中调用了子类的 init）
```

**教训**：构造器中**避免调用可重写的方法**，可能导致子类字段尚未初始化就被访问。

## Object

### 【简单】Object 类的常见方法有哪些？⭐⭐⭐

Object 类是一个特殊的类，是所有类的父类。它主要提供了以下 11 个方法：

| **方法签名**                         | **作用**                           | **默认行为**                                  |
| :----------------------------------- | :--------------------------------- | :-------------------------------------------- |
| `String toString()`                  | 返回对象的字符串表示               | `类名@十六进制哈希码`（如 `Person@1b6d3586`） |
| `boolean equals(Object obj)`         | 比较两个对象是否逻辑相等           | 比较内存地址（`==`）                          |
| `int hashCode()`                     | 返回对象的哈希码                   | 基于内存地址生成                              |
| `Class<?> getClass()`                | 返回对象的运行时类（`Class` 对象） | 由 JVM 提供                                   |
| `protected Object clone()`           | 创建并返回对象的副本               | 浅拷贝（需实现 `Cloneable` 接口）             |
| `protected void finalize()`          | 已废弃，对象被 GC 回收前调用       | 空实现（不推荐使用）                          |
| `void notify()`                      | 唤醒一个等待该对象监视器的线程     | 依赖 JVM 实现                                 |
| `void notifyAll()`                   | 唤醒所有等待该对象监视器的线程     | 依赖 JVM 实现                                 |
| `void wait()`                        | 让当前线程等待，直到被唤醒         | 必须在同步代码块中调用                        |
| `void wait(long timeout)`            | 让线程等待，最多 `timeout` 毫秒    | 超时后自动唤醒                                |
| `void wait(long timeout, int nanos)` | 更精确的等待（纳秒级）             | 实际精度依赖系统                              |

### 【简单】== 和 equals() 有什么区别？⭐⭐⭐⭐⭐

| **对比项**       | **`==`**                                     | **`equals()`**                                                        |
| :--------------- | :------------------------------------------- | :-------------------------------------------------------------------- |
| **基本类型比较** | 比较**值**                                   | 不能比较                                                              |
| **引用类型比较** | 比较**内存地址**                             | 默认比较**内存地址**（同 `==`），但可重写为逻辑比较（如内容是否相同） |
| **是否可重写**   | 否（运算符，行为固定）                       | 是（可自定义比较逻辑）                                                |
| **用途**         | 快速判断基本类型值相等或引用是否指向同一对象 | 判断对象逻辑是否相等（如内容、属性等）                                |

#### ⚠️ 包装类型与 `==` 的经典陷阱

`==` 对包装类型比较的是引用地址，但自动装箱会触发**缓存机制**，导致反直觉的结果：

```java
// Integer 缓存 [-128, 127]，这个范围内的 == 返回 true！
Integer a = 127, b = 127;
System.out.println(a == b);  // true  ← 反直觉！因为走的是 IntegerCache

Integer c = 128, d = 128;
System.out.println(c == d);  // false ← 超出缓存范围，new 了两个对象

// 同理，String 常量池也会让 == "看起来能用"
String s1 = "hello";         // 指向常量池
String s2 = "hello";         // 复用常量池的同一对象
System.out.println(s1 == s2); // true  ← 但这是常量池的"假象"

String s3 = new String("hello");
System.out.println(s1 == s3); // false ← new 在堆上创建了新对象
```

> **📌 面试度量**：此题 ⭐⭐⭐⭐⭐ 不是因为难，而是因为"包装类型缓存"和"String 常量池"让 `==` 在特定场景下"看起来能用"，实际依赖它会导致上线后偶发 bug（比如某天用户 ID 超过 127 后缓存击穿）。**规则：引用类型比较一律用 `equals()`，基本类型用 `==`**。

#### JDK 16+ 新增：Value-Based 类的警告

JDK 16 将 `Integer`、`Long` 等包装类标记为 **value-based**，并明确警告：

> "Use of identity-sensitive operations (such as `==`) on value-based classes may have unpredictable effects."

这意味着对包装类使用 `==` 不仅是不推荐的，在未来的 Valhalla 项目中（引入值类型后），`Integer` 可能会变成无引用地址的值类型，届时 `==` 的行为将彻底变化。所以现在养成习惯非常重要。

#### 跨语言视角：identity vs equality 的设计哲学

| 语言           | 身份比较               | 内容比较                   | 核心差异                                                           |
| :------------- | :--------------------- | :------------------------- | :----------------------------------------------------------------- |
| **Java**       | `==`（引用地址）       | `equals()`（可重写）       | 所有对象都有 identity，基本类型无 identity                         |
| **Python**     | `is`（对象 ID）        | `==`（`__eq__` 方法）      | Java 的 `==` ≈ Python 的 `is`；Python 的 `==` ≈ Java 的 `equals()` |
| **C#**         | `ReferenceEquals()`    | `==`（可重载）+ `Equals()` | struct（值类型）无 identity，`==` 默认比较内容                     |
| **Rust**       | `std::ptr::eq`（地址） | `==`（`PartialEq` trait）  | 所有权模型下"同一性"意义被弱化，默认只需内容比较                   |
| **JavaScript** | `===`（严格相等）      | `==`（带类型转换）         | 对象比较始终是引用比较，没有内置 deep equals                       |

**关键洞察**：Java 是唯一一个"`==` 在基本类型比较值、引用类型比较地址"的主流语言——这种设计源于 Java 的**对象-基本类型二元模型**。对比 C# 可以在 struct 上重载 `==` 使其比较内容，Java 的基本类型不使用 `equals()`、引用类型的 `==` 又不能重载，导致了永恒的心智负担。Valhalla 项目的终极目标之一就是消除这种不一致性。

### 【简单】为什么重写 equals() 时必须重写 hashCode() 方法？⭐⭐⭐⭐⭐

- `hashCode()` 方法返回对象的哈希值，常用于存储结构中快速比较对象是否相同。
- `equals()` 方法比较对象内容是否相同，需自行实现逻辑。

Java 规定：**两个对象若`equals()`相等，它们的`hashCode()`必须相同**。如果违背，则哈希集合（如 `HashMap`、`HashSet`）无法正确去重或查找。

- `HashMap`/`HashSet` 先通过 `hashCode()` 快速定位数据，再用 `equals()` 精确匹配。
- 若 `hashCode()` 不一致，即使 `equals()` 为 `true`，集合会误判为不同对象。

#### HashMap 内部如何用 hashCode 定位元素？

```java
// HashMap 的 put() 核心步骤
public V put(K key, V value) {
    int hash = hash(key);             // 1. 计算 hash 值
    int i = (n - 1) & hash;           // 2. 用位运算定位桶下标（等价于 hash % n）
    // 3. 在桶 i 中遍历链表/红黑树，用 equals() 比较 key
    for (Node<K,V> e = tab[i]; e != null; e = e.next) {
        if (e.hash == hash && (e.key == key || key.equals(e.key)))
            return e;  // 找到
    }
    return null;
}

// HashMap 的 hash() 扰动函数（JDK 8）
static final int hash(Object key) {
    int h;
    return key == null ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    // 高 16 位与低 16 位异或，让高位参与取模，减少碰撞
}
```

**关键洞察**：`(n - 1) & hash` 定位桶的位置（当 n 是 2 的幂次时等价于 `hash % n`，但快一个数量级）。**两个对象如果 `hashCode()` 不同，它们被分配到同一个桶的概率极低（≈ 1/n），HashMap 直接在另一个桶里找不到这个"逻辑相等"的对象**。

#### 违反契约的具体后果（可用代码演示）

```java
class BrokenKey {
    String id;
    BrokenKey(String id) { this.id = id; }

    @Override
    public boolean equals(Object o) {
        return o instanceof BrokenKey && this.id.equals(((BrokenKey) o).id);
    }
    // ❌ 没有重写 hashCode() → 继承 Object 的默认实现（基于地址）
}

Map<BrokenKey, String> map = new HashMap<>();
BrokenKey k1 = new BrokenKey("A");
BrokenKey k2 = new BrokenKey("A");

map.put(k1, "value");
System.out.println(k1.equals(k2));  // true  ← 逻辑相等
System.out.println(map.get(k2));    // null  ← HashMap 找不到！
// 原因：k1.hashCode() ≠ k2.hashCode() → 分到不同桶 → 找不到
```

> **📌 面试度量**：⭐⭐⭐⭐⭐ 的原因——几乎每个 Java 程序员都知道"重写 equals 要重写 hashCode"，但能讲清楚 **HashMap 桶定位公式 `(n-1) & hash`** 和 **扰动函数为何是高 16 位异或低 16 位** 的人不到 10%。这是区分"背过八股文"和"看过源码"的分水岭。

::: info 如何正确重写 `hashCode()`？
:::

- **`equals()`**：比较所有关键字段（如 `name`、`age`）。
- **`hashCode()`**：用 `Objects.hash(字段1, 字段2)` 生成（确保与 `equals()` 字段一致）。

> **性能提示**：`Objects.hash()` 内部每次调用都会创建数组（`new Object[]{a, b, c}` + 数组遍历），对高频调用场景可用手动计算代替。Spring 的 `ObjectUtils.hash()` 和 Lombok `@EqualsAndHashCode` 对此做了优化。

::: info Hash Flooding 攻击 —— 为什么 HashMap 需要红黑树？

:::

2011 年，安全研究人员发表了一篇著名论文，展示了对 Java Web 服务器发起 **Hash Flooding（哈希洪水）DDoS 攻击**的原理：

1. 构造一批精心挑选的字符串，使它们的 `hashCode()` 全部碰撞到同一个桶
2. 向服务器提交大量这类字符串作为 HTTP 参数名
3. 服务器将这些参数存入 `HashMap` → 所有 key 落在同一桶 → 链表从 O(1) 退化到 O(n)
4. 攻击者只需几万个精心构造的参数就能使服务器 CPU 100%，拒绝服务

**JDK 7 的临时修复**：`String.hashCode()` 引入随机种子（`-Djdk.map.althashing.threshold`），但治标不治本。

**JDK 8 的根本修复**：HashMap 在链表长度 ≥ 8 且桶数组长度 ≥ 64 时**自动将链表转为红黑树**，将最坏 O(n) 查找降为 O(log n)，彻底消除了 Hash Flooding 的攻击面。这条规则反过来催生了 hashCode/equals 契约的重要性：**如果 hashCode 设计糟糕（如始终返回常量 0），HashMap 会频繁触发树化，不仅浪费 O(n) 的 equals 遍历，还额外付出红黑树节点维护开销**。

::: info 跨语言视角：Python 的 `__hash__` + `__eq__`

:::

Python 的哈希契约与 Java 惊人一致：

```python
class Person:
    def __eq__(self, other):
        return isinstance(other, Person) and self.id == other.id

    def __hash__(self):
        return hash(self.id)  # 必须与 __eq__ 一致的字段

# Python 会强制检查：如果重写 __eq__ 不重写 __hash__，对象不可哈希（TypeError）
```

与 Java 的关键差异：

- **Python 强制不可哈希**：重写 `__eq__` 不重写 `__hash__` → 类变为 `unhashable`（不能放入 `dict` 的 key），错误在运行时而不是像 Java 那样"能放但找不到"
- **Python 无哈希攻击问题**：CPython 3.3+ 对 `dict` 的 key 哈希值加了随机扰动（per-process hash seed），使得攻击者无法预测碰撞
- **Rust**：`Hash` trait 与 `Eq` trait 完全独立——不需要重写 `Hash` 就必须重写 `Eq`，但 HashMap 内部使用 `Eq`（而非 `==` / `equals`）来判断相等

::: tip 扩展

[Java hashCode() 和 equals() 的若干问题解答](https://www.cnblogs.com/skywang12345/p/3324958.html)

:::

### 【简单】finalize 有什么用？⭐⭐

一言以概之，**`finalize` 可用于对象销毁前的清理，但不可靠且性能差，现代 Java 开发应避免使用，改用 `AutoCloseable` 或 `Cleaner`。**

**Java 9+ 已弃用 `finalize`**，推荐使用：

- `try-with-resources`（实现 `AutoCloseable` 接口）
- `Cleaner` 或 `PhantomReference`（更可控的清理机制）。

**`finalize` 的作用（Java）** ：

- **对象被垃圾回收前的清理**：在对象被 GC 回收前，`finalize()` 会被调用，可用于释放非内存资源（如文件句柄、数据库连接等）。
- **最后的补救机会**：如果对象未被正确关闭，`finalize` 提供最后一次资源释放的机会。

**`finalize` 的问题** ：

- **不保证执行**：JVM 不保证 `finalize` 一定会执行（如程序突然终止时）。即使对象可达性失效，GC 可能延迟回收，导致 `finalize` 延迟调用。
- **性能开销**：覆写 `finalize` 的对象会被 JVM 放入特殊队列，垃圾回收变慢。可能引发内存泄漏（如果 `finalize` 阻塞或执行过久）。
- **安全问题**：在 `finalize` 中抛出异常会导致清理中断，且异常被忽略。可能被恶意代码利用（如通过重写 `finalize` 复活对象，干扰 GC）。

### 【中等】Object#clone() 方法和 Cloneable 接口如何使用？⭐⭐

`Object#clone()` 是 Java 提供的**原生克隆机制**，但设计上有诸多坑点。

**基本用法**：

```java
public class Person implements Cloneable {  // 必须实现 Cloneable 接口
    private String name;
    private int age;

    @Override
    protected Person clone() throws CloneNotSupportedException {
        return (Person) super.clone();  // 浅拷贝
    }
}
```

**关键规则**：

- **必须实现 `Cloneable` 接口**（标记接口，无方法），否则调用 `super.clone()` 抛出 `CloneNotSupportedException`。
- **访问权限是 `protected`**：子类需重写为 `public` 才能被外部调用。
- **默认是浅拷贝**：基本类型复制值，引用类型复制引用。

**浅拷贝的隐患**：

```java
Person p1 = new Person("Alice", 25, new Address("Beijing"));
Person p2 = p1.clone();
p2.getAddress().setCity("Shanghai");
System.out.println(p1.getAddress().getCity());  // Shanghai（被影响！）
```

**深拷贝的正确实现**：

```java
@Override
protected Person clone() throws CloneNotSupportedException {
    Person cloned = (Person) super.clone();
    cloned.address = address.clone();  // 递归克隆引用字段
    return cloned;
}
```

**《Effective Java》的建议**：

> **Item 13：谨慎地覆盖 clone**。更好的替代方案是**提供拷贝构造器或拷贝工厂方法**。

```java
// 推荐替代方案：拷贝构造器
public Person(Person other) {
    this.name = other.name;
    this.age = other.age;
    this.address = new Address(other.address);  // 深拷贝
}
```

**clone 机制的问题总结**：

| **问题**                   | **说明**                                             |
| -------------------------- | ---------------------------------------------------- |
| **设计矛盾**               | `Cloneable` 接口无方法，却改变 `Object.clone()` 行为 |
| **浅拷贝陷阱**             | 默认浅拷贝易引发共享引用 bug                         |
| **final 字段无法重新赋值** | 深拷贝时 `final` 引用字段无法重新指向                |
| **不可用于单例**           | 反射调用 `clone()` 可破坏单例模式                    |

## String

### 【简单】String、StringBuffer、StringBuilder 有什么区别？⭐⭐⭐⭐⭐

| **特性**     | **String**                | **StringBuffer**    | **StringBuilder**            |
| :----------- | :------------------------ | :------------------ | :--------------------------- |
| **可变性**   | ❌ 不可变                 | ✔️ 可变             | ✔️ 可变                      |
| **线程安全** | ✔️（由于不可变）          | ✔️（同步方法）      | ❌（非线程安全）             |
| **性能**     | ⚠️ 最差（频繁创建新对象） | ⚠️ 中等（同步开销） | ✔️ 最高（无同步开销）        |
| **适用场景** | 常量、少量拼接            | 多线程字符串操作    | **单线程字符串操作（推荐）** |

**概括**

- **用 `String` 存储常量**，**用 `StringBuilder` 高效拼接（单线程）**，**用 `StringBuffer` 保证线程安全（多线程）**。
- **优先选 `StringBuilder`**（90%场景适用）。

::: info 跨语言视角：不可变字符串 vs 可变字符串 vs 借用的世界

:::

Java 的 `String`（不可变）/ `StringBuilder`（可变）/ `StringBuffer`（可变+线程安全）三元模型在其他语言中有完全不同的表达：

| 语言       | 不可变                   | 可变              | 线程安全       | 核心差异                                   |
| :--------- | :----------------------- | :---------------- | :------------- | :----------------------------------------- |
| **Java**   | `String`                 | `StringBuilder`   | `StringBuffer` | 三种类型，手动选择                         |
| **Rust**   | `&str`（借用）           | `String`（拥有）  | 编译期保证     | 所有权系统消除运行时线程安全开销           |
| **Go**     | `string`（不可变）       | `strings.Builder` | 不保证         | 简洁优先，`strings.Builder` 零分配优化     |
| **Python** | `str`（不可变）          | `io.StringIO`     | GIL 保证       | `''.join(list)` 惯用模式替代 StringBuilder |
| **C++**    | `std::string_view`(视图) | `std::string`     | 不保证         | 拷贝语义 vs 移动语义，无 GC 关怀           |

**Rust 的激进方案**：Rust 不需要 `StringBuffer`——`String` 的可变引用在编译期就通过 `&mut` 借用规则保证了独占访问，不存在"多线程同时修改一个 String"的场景。如果确实需要共享，使用 `Arc<Mutex<String>>`，线程安全是显式的、零成本的（编译期检查）。

**Go 的简洁方案**：Go 的 `strings.Builder` 在内部使用 `unsafe.Pointer` 将 `[]byte` 直接转换为 `string` 而无需内存拷贝（零分配），这是 Java 做不到的——Java 的 `StringBuilder.toString()` 必然涉及 `new String(char[])` 的拷贝。

**Python 的反模式**：Python 初学者常用 `s += chunk` 拼接字符串，每次都是新建 `str` 对象（因为 str 不可变），性能极差。惯用方案是 `''.join(list)`——一次性分配最终大小。这与 Java 用 `new StringBuilder()` 循环 append 的动机相同，但语法更简洁。

### 【简单】String 为什么是不可变的？⭐⭐⭐

`String` 的不可变性是 Java 为安全、性能、线程安全做的核心设计。

**String 不可变的核心原因**：

- **`final` 修饰的 `char[]` 数组**：Java 中 `String` 内部用 `private final char[]`（JDK 9+ 改为 `byte[]`）存储数据，数组引用和内容均不可修改。
- **无修改内部状态的方法**：所有看似“修改”的方法（如 `concat()`、`substring()`）都返回**新 `String` 对象**，原对象不变。

**为什么 String 被设计为 final？**

- **安全**
  - **并发安全**：不可变天然线程安全，无需同步；
  - **类加载安全**：类加载时通常按类的全限定名字符串进行加载，不可变保证了其安全性。
- **性能**
  - **hashCode 缓存**：`String` 的 `hashCode()` 计算结果可缓存（因内容不变），提升性能（如 `HashMap` 的键）。
  - **常量池**：如 `String s = "abc"` 会复用常量池中的相同字符串，减少内存开销。
- **避免混淆**：避免子类覆写父类方法，导致意想不到的结果。

**示例验证不可变性**：

```java
String s1 = "Hello";
String s2 = s1.concat(" World");
System.out.println(s1); // 输出 "Hello"（原字符串未变）
System.out.println(s2); // 输出 "Hello World"（新对象）
```

### 【简单】字符串拼接用"+" 还是 StringBuilder?⭐⭐⭐

**循环/动态拼接 → `StringBuilder`；简单常量拼接 → "+"；多线程 → `StringBuffer`（极少用）。**  
**`StringBuilder` 是默认推荐选择！**

**优先用 `StringBuilder`（大多数场景）**

- **适用情况**：循环、动态拼接、大量字符串操作。
- **原因**：
  - **高性能**：直接修改缓冲区，避免 `+` 频繁创建新对象。
  - **低内存开销**：减少临时对象和 GC 压力。

**简单拼接可用 "+"（编译期优化）**

- **适用情况**：少量**固定字符串**拼接（如 `"a" + "b"`）。
- **原因**：
  - **代码简洁**：可读性更好。
  - **编译器优化**：JVM 自动合并为常量（如 `"ab"`），无性能损失。
  - 通过“+”的字符串拼接方式，实际上是通过 `StringBuilder` 调用 `append()` 方法实现的。
  - 在循环内使用“+”，会导致创建过多的 `StringBuilder` 对象。JDK9 中，优化了这个问题，字符串相加 “+” 改为了用动态方法 `makeConcatWithConstants()` 来实现，而不是大量的 `StringBuilder` 了。

**多线程拼接用 `StringBuffer`（极少需要）**

- **适用情况**：多线程环境且需线程安全（通常局部变量仍可用 `StringBuilder`）。

::: tip 扩展

[StringBuilder？来重温一下字符串拼接吧](https://juejin.cn/post/7182872058743750715) 。

:::

### 【简单】String#equals() 和 Object#equals() 有何区别？⭐⭐⭐

| **对比项**   | **`Object#equals()`**              | **`String#equals()`**                      |
| :----------- | :--------------------------------- | :----------------------------------------- |
| **默认行为** | 比较**内存地址**（`==`）           | 比较**字符串内容**（逐字符对比）           |
| **重写目的** | 需子类自行重写以实现逻辑相等       | 已优化为内容比较，满足字符串业务需求       |
| **性能影响** | 无额外开销                         | 需遍历字符数组，但优先检查地址和长度       |
| **使用场景** | 通用对象比较（默认不满足内容相等） | 字符串内容对比（如 `"abc".equals("abc")`） |

### 【简单】字符串常量池有什么用？⭐⭐⭐

字符串常量池是 JVM 的特殊内存区域，用于存储字符串字面量（如 `"abc"`），确保相同内容的字符串只存一份。

**字符串常量池通过复用相同字符串，节省内存并提升性能，直接赋值（`"abc"`）优先使用池，`new String()` 强制创建新对象。**

字符串常量池的作用有：

**节省内存**：相同字符串复用，避免重复创建（如 `String s1 = "hello"` 和 `String s2 = "hello"` 指向同一对象）。

**提升性能**：

- **快速比较**：直接通过 `==` 判断地址是否相同（比 `equals()` 更快）。
- **哈希优化**：如 `HashMap` 的键可复用缓存的 `hashCode`。

**实现规则**

- **直接赋值**（`String s = "abc"`）→ **优先从常量池引用**。
- **`new String("abc")`** → **强制在堆中创建新对象**（不推荐，除非需隔离实例）。
- **`intern()` 方法** → 将堆中的字符串对象添加到常量池（若池中不存在）。

**注意事项**

- **避免滥用 `new String()`**：无特殊需求时，直接用字面量赋值。
- **`intern()` 慎用**：可能增加常量池内存压力，需权衡性能。

### 【简单】`String s = new String("abc")` 创建了几个字符串对象？⭐⭐⭐⭐

`new String("abc")` 可能创建 1~2 个对象（取决于常量池是否已存在"abc"），但堆中的新对象必定创建。

- **常量池已存在"abc"**：**1 个对象**（仅堆中的 `new String`）
- **常量池不存在"abc"**：**2 个对象**（常量池的"abc" + 堆中的 `new String`）

#### 字节码层面的验证

```java
// 源代码
String s = new String("abc");
```

编译后字节码：

```asm
 0: new #2          // ① 在堆上创建 String 对象（此时 value 尚未赋值）
 3: dup             // ② 复制栈顶引用（供 invokespecial 消耗一个）
 4: ldc #3          // ③ 从常量池加载字符串常量 "abc" → 若常量池无则创建
 6: invokespecial #4 // ④ 调用 String.<init>(String) 构造器
 9: astore_1        // ⑤ 将引用存入局部变量 s
```

**指令详解**：

| **指令**           | **操作**                                                             | **涉及内存区域**              | **是否创建对象**                                |
| ------------------ | -------------------------------------------------------------------- | ----------------------------- | ----------------------------------------------- |
| `new #2`           | 在堆上分配 String 实例内存                                           | 堆                            | ✔️（1 个 String 对象，其 `value[]` 尚未初始化） |
| `ldc #3`           | 将常量池中的 `"abc"` 压栈，若不存在则在 **运行时常量池** 中创建      | 运行时常量池（JDK 7+ 在堆中） | 可能（若常量池尚未有此字面量）                  |
| `invokespecial #4` | 调用 `<init>(String)` 构造器，将 `ldc` 加载的常量赋值给 `this.value` | —                             | ❌（仅赋值引用）                                |

> **关键点**：`ldc` 指令在类加载的**解析阶段**将符号引用替换为直接引用，如果运行时常量池中不存在 `"abc"`，则在此阶段创建。这就是"可能创建 2 个对象"的字节码级根因。

#### 与 `String s = "abc"` 的对比

```java
String s = "abc";  // 字节码：仅 ldc #3 → astore_1
// 0 或 1 个对象：常量池有则 0（复用），无则 1 个（在常量池创建）
```

> **📌 面试度量**：⭐⭐⭐⭐ 的原因——初级回答是"1 个或 2 个"，能讲到 `ldc` 指令和运行时常量池的是中级，能**结合类加载的解析阶段、JDK 7 常量池移入堆、以及 `invokespecial` 构造器的 value 赋值时机**说明整个过程的才是高级。

#### 跨语言视角：C/C++ 的字符串拷贝陷阱

```cpp
// C++ — std::string 默认是深拷贝（非引用语义）
std::string s1 = "abc";
std::string s2 = s1;              // 深拷贝！s1 和 s2 是两个独立对象
std::string s3 = std::move(s1);   // 移动语义：s1 的内容转移到 s3，s1 变为空

// Java — String 是引用语义
String s1 = "abc";
String s2 = s1;   // 浅拷贝！s1 和 s2 指向同一对象（不可变所以安全）
```

C++ 的 `std::string` 默认深拷贝，需要显式使用 `std::move` 或 `std::string_view` 来规避拷贝开销。Java 的 `String` 由于不可变性天然引用语义安全，不需要 `StringView` 概念。

Rust 则走向另一个极端：`String` 是唯一定义的"拥有者"，`s2 = s1` 会使 `s1` 失效（move semantics），编译器强制保证没有 use-after-move。三种语言对"字符串是谁的"这个问题给出了三种不同回答：C++（拷贝是我的）、Java（引用分享）、Rust（只有一个拥有者）。

### 【简单】String#intern 方法有什么用？⭐⭐⭐

String#intern 方法的**作用**有：

- **强制字符串入池**：将堆中的 `String` 对象添加到字符串常量池（若池中不存在）
- **返回池中引用**：保证相同内容的字符串始终返回同一内存地址

**注意**

- **JDK7+ 优化**：常量池从方法区移至堆内存，减少内存溢出风险。
- **慎用场景**：
  - 避免对动态生成的短生命周期字符串使用（可能导致池膨胀）
  - 优先用于高频使用的静态字符串（如配置键值）

### 【简单】String 类型的变量和常量做“+”运算时会发生什么？⭐⭐

**常量相加编译期优化，变量相加隐式转 `StringBuilder`，循环拼接必须显式使用 `StringBuilder` 避免性能损耗。**

**常量折叠（编译期优化）**

- **纯常量运算**（如 `"a"+"b"`）→ 直接合并为 `"ab"`，仅存于常量池
- **final 变量** 视为常量，同样触发优化

**变量拼接（运行时行为）**

- **含变量的运算**（如 `str + "b"`）→ 隐式转换为 `StringBuilder` 操作
  ```java
  // 实际执行逻辑
  new StringBuilder().append(str).append("b").toString()
  ```
- **每次运算** 生成临时 `StringBuilder` 和最终 `String` 对象

**性能关键差异**

| 场景           | 内存/性能表现                        | 优化建议                       |
| -------------- | ------------------------------------ | ------------------------------ |
| 常量+常量      | 零运行时开销                         | 无需处理                       |
| 单次变量+常量  | 1 次 `StringBuilder` 创建            | 可接受                         |
| **循环内拼接** | 多次创建 `StringBuilder`（性能陷阱） | **必须显式用 `StringBuilder`** |

**最佳实践**

- **简单拼接**：直接使用 `+`（可读性优先）
- **循环/批量拼接**：

  ```java
  // ✔️ 正确写法
  StringBuilder sb = new StringBuilder();
  for (String str : list) sb.append(str);
  String result = sb.toString();

  // ❌ 错误写法（低效）
  String s = "";
  for (String str : list) s += str; // 每次循环隐式新建 StringBuilder
  ```

### 【中等】JDK 9 对 String 做了哪些优化（Compact Strings）？⭐⭐

JDK 9 引入 **Compact Strings（紧凑字符串）** 优化，将 String 内部存储从 `char[]` 改为 `byte[]`，大幅降低内存占用。

**优化背景**：

- **JDK 8 及之前**：`String` 内部用 `char[]` 存储，每个字符占 **2 字节**（UTF-16）。
- **问题**：大多数应用场景的字符串是**拉丁字符（ASCII）**，1 字节即可表示，浪费一半内存。

**JDK 9+ 的改进**：

```java
// JDK 8: private final char[] value;
// JDK 9+:
private final byte[] value;     // 字节数组存储
private final byte coder;        // 编码标识（LATIN1=0, UTF16=1）
```

**编码策略**：

- **LATIN1（ISO-8859-1）**：所有字符 ≤ 0xFF 时使用，每个字符 **1 字节**。
- **UTF-16**：包含非拉丁字符时自动升级，每个字符 **2 字节**。

**性能影响**：

| **维度**     | **JDK 8（char[]）** | **JDK 9+（byte[]）**    |
| ------------ | ------------------- | ----------------------- |
| **内存占用** | 每字符 2 字节       | 拉丁字符 1 字节，省 50% |
| **GC 压力**  | 较大                | 显著降低                |
| **方法性能** | 直接操作 char       | 需根据 coder 分支处理   |

**验证方法**：

```bash
# 查看 String 对象的内存布局
java -XX:+UnlockDiagnosticVMOptions -XX:+PrintStringDeduplicationStatistics
```

**配套优化：String Deduplication（G1 GC）**

```bash
-XX:+UseStringDeduplication  # 启用 G1 字符串去重，相同内容字符串共享底层 byte[]
```

### 【中等】StringJoiner 和 String.join 有什么用？⭐⭐

Java 8 引入 `StringJoiner` 和 `String.join()`，简化**分隔符拼接**场景。

**StringJoiner**：

```java
StringJoiner joiner = new StringJoiner(",", "[", "]");  // 分隔符、前缀、后缀
joiner.add("a").add("b").add("c");
System.out.println(joiner);  // [a,b,c]
```

**String.join（便捷方法）**：

```java
String result = String.join("-", "2024", "07", "03");  // 2024-07-03

List<String> list = Arrays.asList("a", "b", "c");
String joined = String.join(",", list);  // a,b,c
```

**Stream 配合 Collectors.joining**：

```java
String result = list.stream()
    .map(String::toUpperCase)
    .collect(Collectors.joining(", ", "{", "}"));  // {A, B, C}
```

**适用场景对比**：

| **方式**              | **适用场景**         | **特点**               |
| --------------------- | -------------------- | ---------------------- |
| `+` / `StringBuilder` | 简单拼接             | 灵活但需手动处理分隔符 |
| `String.join`         | 已有集合，简单分隔符 | 最简洁                 |
| `StringJoiner`        | 流式构建，需前后缀   | 支持 prefix/suffix     |
| `Collectors.joining`  | Stream 收集阶段      | 函数式风格             |

### 【简单】String 的 equals 方法是如何实现的？⭐⭐⭐

`String.equals()` 是重写过的内容比较方法，包含多重优化：

```java
public boolean equals(Object anObject) {
    if (this == anObject) {        // 1. 引用相等，直接返回 true
        return true;
    }
    if (anObject instanceof String) {  // 2. 类型检查
        String anotherString = (String) anObject;
        int n = value.length;
        if (n == anotherString.value.length) {  // 3. 长度不同直接 false
            char v1[] = value;
            char v2[] = anotherString.value;
            int i = 0;
            while (n-- != 0) {                  // 4. 逐字符比较
                if (v1[i] != v2[i])
                    return false;
                i++;
            }
            return true;
        }
    }
    return false;
}
```

**优化要点**：

1. **引用相等检查**：`==` 比较地址，相同对象直接返回，避免字符比较开销。
2. **长度预检**：长度不同直接返回 false，无需逐字符比较。
3. **直接访问数组**：避免方法调用开销。

**性能对比**：`equals` 平均 O(n)，但实际因优化远快于理论值。

### 【中等】String、StringBuilder、StringBuffer 的扩容机制？⭐⭐

**String**：不可变，无扩容概念，每次修改创建新对象。

**StringBuilder / StringBuffer**（继承自 `AbstractStringBuilder`）：

```java
// 默认初始容量
public StringBuilder() { super(16); }  // 容量 16

// 扩容核心逻辑
private int newCapacity(int minCapacity) {
    int oldCapacity = value.length;
    // 新容量 = (旧容量 + 2) * 2，相当于 2 倍 + 2
    int newCapacity = (oldCapacity << 1) + 2;
    if (newCapacity - minCapacity < 0) {
        newCapacity = minCapacity;
    }
    return (newCapacity <= 0 || MAX_ARRAY_SIZE - newCapacity < 0)
        ? hugeCapacity(minCapacity) : newCapacity;
}
```

**扩容策略**：`(oldCapacity * 2) + 2`，即**2 倍 + 2**。

**与 ArrayList 的对比**：

| **容器**      | **初始容量** | **扩容倍数** |
| ------------- | ------------ | ------------ |
| ArrayList     | 10           | 1.5 倍       |
| StringBuilder | 16           | 2 倍 + 2     |

**预分配建议**：预估最终长度时，构造器指定初始容量，避免多次扩容。

```java
StringBuilder sb = new StringBuilder(1024);  // 预分配 1KB
```
