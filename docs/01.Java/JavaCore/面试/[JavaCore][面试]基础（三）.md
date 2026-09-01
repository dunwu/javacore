---
title: Java 基础面试三
cover: https://raw.githubusercontent.com/dunwu/images/master/archive/2025/03/020ab2bf4af8401590e0291a34f873f8.jpg
date: 2024-07-12 08:18:58
order: 3
categories:
  - Java
  - JavaCore
  - 面试
tags:
  - Java
  - JavaCore
  - 面试
permalink: /pages/7704a3fb/
---

# Java 基础面试三

## Java 泛型

### 【中等】Java 泛型的作用是什么？⭐⭐⭐

::: info Java 泛型是什么？

:::

泛型允许在**类、接口、方法**上使用**类型参数（如 `<T>`）**，使代码能适应多种数据类型，同时保证类型安全。

::: info Java 泛型有什么用？

:::

- **类型安全**：编译时检查类型，避免运行时 `ClassCastException`。
- **代码复用**：同一套逻辑可处理不同数据类型（如 `List<String>` 和 `List<Integer>`）。
- **消除强制转换**：直接使用泛型类型，无需手动转换（如 `(String) list.get(0)`）。

::: info Java 泛型有什么特性？

:::

- **类型擦除**：泛型仅在编译时有效，运行时类型信息会被擦除（`List<String>` 运行时变成 `List`）。
- **通配符 `<?>`**：表示未知类型（如 `List<?>` 可接受任意类型的 `List`）。
- **界限限定**：
  - `T extends Class`（限定类型范围，如 `<T extends Number>`）。
  - `<? super T>`（支持父类类型）。

**简单示例**

```java
// 泛型类
class Box<T> {
    private T content;
    public void set(T content) { this.content = content; }
    public T get() { return content; }
}

// 使用
Box<String> box = new Box<>();
box.set("Hello");
String value = box.get(); // 无需强制转换
```

**一句话总结**：泛型让代码更灵活、安全，减少冗余和运行时错误。

### 【中等】什么是 Java 泛型的上下界限定符？⭐⭐⭐

Java 泛型的上下界限定符用于**限制泛型类型参数的范围**，确保类型安全，提供更灵活的类型约束。

::: info Java 什么是上界限定符？有什么用？

:::

**上界限定符（`<? extends T>`）** 限定泛型类型必须是 `T` **或其子类**（`T` 可以是类或接口）。

**特点**：

- **只读安全**：能安全读取数据（因为元素至少是 `T` 类型）。
- **不能写入**：无法确定具体子类型，防止类型污染。

**示例**：

```java
// 接受 Number 或其子类（如 Integer, Double）
void printList(List<? extends Number> list) {
    for (Number num : list) {  // 安全读取
        System.out.println(num);
    }
    // list.add(1);  // 编译错误！无法安全写入
}
```

::: info Java 什么是下界限定符？有什么用？

:::

下界限定符（`<? super T>`）限定泛型类型必须是 `T` **或其父类**。

**特点**：

- **可写入**：能安全添加 `T` 及其子类的对象。
- **读取受限**：只能以 `Object` 类型读取（因为父类型不确定）。

**示例**：

```java
// 接受 Integer 或其父类（如 Number, Object）
void addNumbers(List<? super Integer> list) {
    list.add(1);     // 安全写入 Integer
    list.add(2);
    // Integer num = list.get(0);  // 编译错误！需强制转换
    Object obj = list.get(0);      // 只能以 Object 读取
}
```

**通配符限定对比**

| 类型 | 语法          | 读取           | 写入             | 应用           |
| :--- | :------------ | :------------- | :--------------- | :------------- |
| 上界 | `? extends T` | 安全（作为 T） | 禁止             | 生产者场景     |
| 下界 | `? super T`   | 需转 Object    | 安全（T 及子类） | 消费者场景     |
| 无界 | `?`           | 作为 Object    | 禁止             | 完全不确定类型 |

**小结**

- **`extends T`**：安全读取，限制类型上界。如遍历 `List<? extends Number>`。
- **`super T`**：安全写入，限制类型下界。如 `Collections.copy(dest<? super T>, src<? extends T>)`。
- **PECS 原则**（Producer-Extends, Consumer-Super）指导何时用哪种限定符。
  - **生产者（Producer）** 用 `extends`（输出数据）。
  - **消费者（Consumer）** 用 `super`（输入数据）。

### 【中等】泛型擦除的作用是什么？⭐⭐⭐

泛型擦除是 Java 在**编译时检查类型安全**、**运行时丢弃类型信息**的折中设计，平衡了兼容性、性能和类型安全，但牺牲了部分运行时灵活性。

**泛型擦除**是 Java 泛型的实现机制：

- **编译时**：泛型类型（如 `<T>`、`List<String>`）会被检查，确保类型安全。
- **运行时**：所有泛型类型信息会被擦除，替换为**原始类型（Raw Type）**或**边界类型（如 `Object`/`extends` 上限）**。

**泛型擦除规则**

| 泛型定义                           | 擦除后类型           | 示例                             |
| ---------------------------------- | -------------------- | -------------------------------- |
| **无界限 `<T>`**                   | `Object`             | `List<T>` → `List`               |
| **有界限 `<T extends Number>`**    | `Number`（边界类型） | `Box<T>` → `Box<Number>`         |
| **通配符 `<?>` / `<? extends T>`** | 边界类型             | `List<?>` → `List`               |
| **`<? super T>`**                  | `Object`             | `List<? super Integer>` → `List` |

**泛型擦除作用**

- **兼容性**：确保泛型代码能与旧版 Java（非泛型）字节码兼容。
- **运行时效率**：避免为每个泛型类型生成新类，减少 JVM 负担。
- **简化设计**：统一类型系统，避免 C++ 模板的复杂性。

**泛型擦除的问题**

- **类型信息丢失**：运行时无法获取泛型参数（如 `List<String>` 和 `List<Integer>` 运行时都是 `List`）。

  ```java
  List<String> list = new ArrayList<>();
  System.out.println(list.getClass());  // 输出 ArrayList，而非 ArrayList<String>
  ```

- **强制类型转换**：编译器自动插入类型转换代码。

  ```java
  List<String> list = new ArrayList<>();
  String s = list.get(0);  // 编译后实际为：(String) list.get(0)
  ```

- **不支持原生类型**：不能直接使用 `List<int>`，必须用包装类（如 `List<Integer>`）。

**绕过擦除的限制**

- **显式传递 `Class<T>`**：通过反射保留类型信息。

  ```java
  <T> void create(Class<T> clazz) {
      T instance = clazz.newInstance();  // 运行时知道具体类型
  }
  ```

- **类型令牌（Type Token）**：利用匿名子类捕获泛型类型。
  ```java
  new TypeToken<List<String>>() {};  // Guava 提供的方案
  ```

**典型问题与解决方案**

| 问题场景                                                                 | 解决方案                                          |
| ------------------------------------------------------------------------ | ------------------------------------------------- |
| 需要运行时获取泛型类型                                                   | 传递 `Class<T>` 参数或使用 Type Token             |
| 泛型数组创建（`new T[]`）                                                | 使用 `Object[]` 转换或反射（`Array.newInstance`） |
| 方法重载冲突（如 `void foo(List<String>)` 和 `void foo(List<Integer>)`） | 编译报错（擦除后方法签名相同）                    |

## Java 反射

### 【简单】什么是反射？反射有什么作用？⭐⭐⭐⭐

**反射（Reflection）是 Java 提供的动态机制**，允许程序在**运行时**：

- **获取类的信息**（类名、方法、字段、注解等）
- **操作类的成员**（调用方法、访问/修改字段、创建对象等）
- **绕过访问控制**（如调用私有方法）

**反射核心类**：

- `Class<T>`：表示类或接口
- `Method`：表示类的方法
- `Field`：表示类的字段
- `Constructor`：表示类的构造方法

**反射的主要用途**

- **动态代理**（如插件化开发）
- **依赖注入**（如 Spring 的依赖注入）
- **ORM**（Hibernate 的 ORM 映射）
- **测试工具**（如 Mockito 模拟对象）
- **绕过访问限制**（调试或特殊场景）

**如何使用反射？**

::: code-tabs#反射使用示例

@tab **获取 `Class` 对象**

```java
// 方式1：通过类名.class
Class<String> strClass = String.class;

// 方式2：通过对象.getClass()
String s = "Hello";
Class<?> strClass2 = s.getClass();

// 方式3：通过Class.forName("全限定类名")
Class<?> strClass3 = Class.forName("java.lang.String");  // 需处理ClassNotFoundException
```

@tab **创建对象**

```java
// 方式1：直接调用无参构造（需强制类型转换）
Class<?> clazz = Class.forName("com.example.User");
User user = (User) clazz.newInstance();  // 已过时，推荐用 getConstructor()

// 方式2：调用带参构造
Constructor<?> constructor = clazz.getConstructor(String.class, int.class);
User user = (User) constructor.newInstance("Alice", 25);
```

@tab **调用方法**

```java
// 获取方法（需方法名 + 参数类型）
Method method = clazz.getMethod("setName", String.class);

// 调用方法（需对象实例 + 参数值）
method.invoke(user, "Bob");  // 相当于 user.setName("Bob")

// 调用静态方法
Method staticMethod = clazz.getMethod("staticMethod");
staticMethod.invoke(null);  // 静态方法传 null
```

@tab **访问/修改字段**

```java
// 获取字段（包括私有字段）
Field field = clazz.getDeclaredField("name");

// 允许访问私有字段
field.setAccessible(true);  // 关闭访问检查

// 读取字段值
String name = (String) field.get(user);  // 相当于 user.name

// 修改字段值
field.set(user, "Charlie");  // 相当于 user.name = "Charlie"
```

@tab **获取注解信息**

```java
// 获取类/方法/字段上的注解
Annotation[] annotations = clazz.getAnnotations();
if (clazz.isAnnotationPresent(MyAnnotation.class)) {
    MyAnnotation anno = clazz.getAnnotation(MyAnnotation.class);
}
```

:::

#### 🔬 反射为什么慢？——性能开销的定量分析

`Method.invoke()` 比直接调用慢 **10~100 倍**（热点代码 JIT 优化后可缩小到 2~5 倍）。性能开销来自三个层面：

**1. 方法访问检查（Access Check）**

```java
// Method.invoke() 内部每次调用都需要：
// ① 检查方法修饰符（public/protected/private）
// ② 检查调用者是否有权限访问（Reflection.getCallerClass()）
// ③ 检查参数类型和数量是否匹配
```

**2. 参数装箱/拆箱（Auto-boxing）**

```java
// invoke() 的参数和返回值都是 Object 数组，每个基本类型参数都要装箱
method.invoke(target, 42, true);  // int→Integer, boolean→Boolean
// 返回 Object，调用方需要拆箱
int result = (int) method.invoke(target, 42);  // Integer→int
```

**3. JIT 内联困难**

```java
// 直接调用：JIT 可以轻松内联
target.setName("Bob");  // HotSpot 将方法体直接嵌入调用点

// 反射调用：JIT 无法内联（因为 invoke() 的目标在编译时不确定）
method.invoke(target, "Bob");  // 必须在运行时查找 MethodAccessor
```

#### 🔬 反射的 Inflation 优化机制

JDK 对反射做了 **Inflation**（膨胀）优化，让频繁调用的反射方法越来越快：

```
调用次数    →    访问器类型       →    性能
────────────────────────────────────────────
0~15 次    →    NativeMethodAccessor（JNI）   慢（每次跨 JNI 边界）
16+ 次     →    GeneratedMethodAccessor（字节码） 快（接近直接调用）
            （通过 ASM 动态生成一个 accessor 类，直接用 invokespecial 调用目标方法）
```

可通过 `-Dsun.reflect.inflationThreshold=0` 跳过 JNI 阶段，直接使用字节码 accessor。

> **📌 面试度量**：⭐⭐⭐⭐ 的原因——"反射慢"人人会说，但能讲出**慢的三个层次（访问检查、装箱、JIT 内联困难）** 和 **Inflation 优化机制**，说明不仅仅是"用过反射"，而是"理解 JVM 如何为反射做优化"。

#### 跨语言视角：反射的三种设计哲学

| 语言       | 反射机制                                              | 核心差异                                                |
| :--------- | :---------------------------------------------------- | :------------------------------------------------------ |
| **Java**   | `java.lang.reflect` + `MethodHandle`（JDK 7+）        | 编译期类型擦除 → 反射是恢复类型信息的唯一途径           |
| **Go**     | `reflect` 包                                          | 无继承/多态，反射主要用于序列化、ORM 等框架层           |
| **Python** | `getattr`/`setattr`/`hasattr`（内置）+ `inspect` 模块 | 动态类型语言，"反射"概念被弱化为普通操作                |
| **Rust**   | 无运行时反射                                          | 通过 `#[derive]` + trait + 宏在编译期生成，零运行时开销 |

**Go 的反思**：Go 有 `reflect` 但设计者 Rob Pike 曾公开表示"反射永远不应该是你代码的核心"——因为 Go 没有 Java 的 JIT 优化，每次 `reflect.Value.Call()` 都是纯解释执行，性能差距可达 100 倍以上。Java 的 Inflation 优化（JNI → 字节码 accessor）正是 Go 缺乏的。

**Rust 的零成本替代**：Rust 选择"编译期反射"——通过 `proc macro` 在编译时展开代码，完全消除运行时开销。代价是：任何反射需求必须在编译时声明（`#[derive(Serialize)]` 等）。这是一种哲学取舍：**Java 选择运行时灵活性，Rust 选择编译期安全性**。

#### GraalVM Native Image 对反射的限制

GraalVM 将 Java 编译为**原生可执行文件**时，采用的是 **closed-world assumption**（闭世界假设）——只有通过静态分析可达的代码才会被编译。反射的 `Class.forName("动态类名")` 在编译期无法确定目标类，导致：

1. 默认**不支持运行时反射**（运行时调用 `Class.forName` 会抛出异常）
2. 必须通过 `reflect-config.json` 预注册所有需要通过反射访问的类、方法、字段
3. 动态代理、CGLIB 等运行时生成字节码的技术在 Native Image 中**不可用**

这意味着：Spring 应用迁移到 GraalVM Native Image 时，所有 `@Autowired`、AOP 代理、MyBatis Mapper 代理等依赖反射/动态代理的功能，都必须在编译期通过 AOT 处理或配置注册——这是 Java 生态从"动态运行时"向"静态编译"转型的最大挑战。

### 【简单】反射有什么优缺点？⭐⭐⭐

| **优点**                   | **缺点**                 |
| -------------------------- | ------------------------ |
| 动态性高（运行时决定行为） | 性能较差（比直接调用慢） |
| 可访问私有成员（突破封装） | 代码可读性降低           |
| 支持泛型擦除后的类型操作   | 安全隐患（如破坏单例）   |

**性能优化建议**：

- **缓存 `Class`/`Method`/`Field` 对象**：避免重复反射调用。
- **优先使用 `getDeclaredXXX`**：比 `getXXX` 更高效（不检查继承链）。
- **限制 `setAccessible(true)`**：频繁调用影响性能。

**注意事项**：

- **反射可以破坏封装性**（如修改 `final` 字段、调用私有方法）。
- **慎用 `setAccessible(true)`**：可能导致安全漏洞（如绕过权限检查）。

::: tip 扩展

[Java Reflection: Why is it so slow?](https://stackoverflow.com/questions/1392351/java-reflection-why-is-it-so-slow) 。

:::

### 【中等】什么是 Java 中的动态代理？⭐⭐⭐⭐

动态代理是一种在**运行时**动态创建代理对象的技术，允许在不修改原始类代码的情况下，**增强或拦截**目标对象的方法调用。

Java 动态代理通过 `Proxy` 和 `InvocationHandler` 在运行时生成接口代理对象，**非侵入式**地实现方法拦截和功能增强，是 AOP 和框架设计的核心技术。

- **`java.lang.reflect.Proxy`**：提供静态方法创建代理对象（核心方法：`Proxy.newProxyInstance()`）。
- **`java.lang.reflect.InvocationHandler`**：接口，实现代理逻辑（核心方法：`invoke()`）。

【示例】动态代理示例

```java
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

// 1. 定义接口
interface Hello {
    void sayHello();
}

// 2. 实现接口
class HelloImpl implements Hello {
    public void sayHello() {
        System.out.println("Hello World!");
    }
}

public class SimpleProxyDemo {
    public static void main(String[] args) {
        // 3. 创建实际对象
        Hello realHello = new HelloImpl();

        // 4. 创建代理对象
        Hello proxyHello = (Hello) Proxy.newProxyInstance(
            Hello.class.getClassLoader(), // 类加载器
            new Class<?>[] { Hello.class }, // 代理的接口
            new InvocationHandler() { // 调用处理器
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("Before method call");
                    Object result = method.invoke(realHello, args); // 调用真实对象的方法
                    System.out.println("After method call");
                    return result;
                }
            });

        // 5. 通过代理对象调用方法
        proxyHello.sayHello();
    }
}
```

**动态代理的特点**

- **运行时生成**：代理类在运行时动态生成，无需手动编写。
- **基于接口**：只能代理接口（不能代理普通类）。
- **非侵入性**：无需修改原始代码即可增强功能。

**应用场景**

- **AOP（面向切面编程）**：如日志、事务管理（Spring AOP 基于动态代理）。
- **远程方法调用（RPC）**：如 Dubbo 的消费者代理。
- **权限控制**：拦截方法调用检查权限。

**动态代理 vs 静态代理**

| **对比项**   | **动态代理**           | **静态代理**             |
| ------------ | ---------------------- | ------------------------ |
| **生成时机** | 运行时动态生成         | 编译时手动编写           |
| **维护成本** | 低（自动适配接口）     | 高（需为每个类编写代理） |
| **灵活性**   | 高（通用逻辑集中处理） | 低（逻辑分散）           |

**局限性**

- **仅支持接口代理**：不能代理普通类（CGLIB 可弥补此问题）。
- **性能开销**：反射调用比直接调用略慢（现代 JVM 已优化）。

#### 跨语言视角：代理模式的三种实现

| 语言/环境      | 代理机制                      | 核心差异                                                             |
| :------------- | :---------------------------- | :------------------------------------------------------------------- |
| **Java**       | `Proxy` + `InvocationHandler` | 基于接口 + 反射，运行时动态生成                                      |
| **JavaScript** | `new Proxy(target, handler)`  | 原生语言支持，可拦截任意操作（属性访问、函数调用、构造器），无需接口 |
| **Python**     | `@decorator` 或 `__getattr__` | 装饰器是语法糖（函数级代理），`__getattr__` 是对象级代理             |
| **Go**         | 无原生动态代理                | 通过 `interface{}` + type assertion 实现，编译期类型检查强           |

JavaScript 的 `Proxy` 是最强大的实现——它能拦截 13 种操作（get、set、has、construct、apply 等），远超 Java 只能拦截方法调用。Vue 3 的响应式系统就是用 `Proxy` 替代了 Vue 2 的 `Object.defineProperty`。Python 的装饰器则更轻量——它是一个函数，接收函数返回新函数，常用于日志、权限、缓存等场景，比 Java 的代理更直观简洁。

**扩展：CGLIB 动态代理**

- **原理**：通过字节码技术生成目标类的子类代理。
- **特点**：可代理普通类，但无法代理 `final` 类/方法。

### 【中等】JDK 动态代理和 CGLIB 动态代理有什么区别？⭐⭐⭐⭐

JDK 动态代理 vs. CGLIB 动态代理：

| **代理类型**   | **JDK 动态代理**                            | **CGLIB 代理**                 |
| -------------- | ------------------------------------------- | ------------------------------ |
| **实现机制**   | 基于**接口**，运行时生成代理类（`$Proxy0`） | 基于**继承**，生成目标类的子类 |
| **技术依赖**   | Java 反射 API（`Proxy`类）                  | ASM 字节码操作库               |
| **限制条件**   | 目标类必须实现接口                          | 无法代理 `final` 类/方法       |
| **可代理目标** | 只能代理**接口**                            | 可代理**普通类**和接口         |

**性能对比**

| **维度**     | **JDK 动态代理** | **CGLIB 代理**       |
| ------------ | ---------------- | -------------------- |
| **生成速度** | 较快（反射生成） | 较慢（需操作字节码） |
| **调用速度** | 反射调用，略慢   | 直接方法调用，更快   |
| **内存占用** | 较小             | 较大（生成子类）     |

> **注**：现代 JVM 对反射做了优化，JDK 代理性能差距已不明显。

**使用示例**

::: code-tabs#反射使用示例

@tab **JDK 动态代理**

```java
// 要求：目标类必须实现接口
public interface UserService {
    void save();
}

// 代理逻辑
InvocationHandler handler = (proxy, method, args) -> {
    System.out.println("JDK 代理前置处理");
    Object result = method.invoke(target, args);
    System.out.println("JDK 代理后置处理");
    return result;
};

UserService proxy = (UserService) Proxy.newProxyInstance(
    target.getClass().getClassLoader(),
    target.getClass().getInterfaces(),  // 关键：需传入接口
    handler
);
```

@tab **CGLIB 代理**

```java
// 目标类无需实现接口
public class UserService {
    public void save() { System.out.println("保存用户"); }
}

// 代理逻辑
Enhancer enhancer = new Enhancer();
enhancer.setSuperclass(UserService.class);
enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
    System.out.println("CGLIB 代理前置处理");
    Object result = proxy.invokeSuper(obj, args);  // 直接调用父类方法
    System.out.println("CGLIB 代理后置处理");
    return result;
});

UserService proxy = (UserService) enhancer.create();  // 生成子类对象
```

:::

**如何选择？**

| **场景**                 | **推荐代理** | **理由**                  |
| ------------------------ | ------------ | ------------------------- |
| 目标对象实现了接口       | JDK 动态代理 | 轻量级，标准库支持        |
| 目标对象无接口           | CGLIB        | 唯一选择                  |
| 需要代理 `final` 方法    | JDK 动态代理 | CGLIB 无法代理 final 方法 |
| 高性能要求（如高频调用） | CGLIB        | 直接方法调用更快          |
| 避免额外依赖             | JDK 动态代理 | CGLIB 需引入第三方库      |

**主流框架的选择**

- **Spring AOP**：
  - 默认使用 **JDK 动态代理**（如果目标有接口）
  - 无接口时自动切换为 **CGLIB**
  - 可通过 `@EnableAspectJAutoProxy(proxyTargetClass=true)` 强制使用 CGLIB
- **MyBatis**：Mapper 接口代理使用 **JDK 动态代理**

**一句话总结**

- **JDK 动态代理**：基于接口，反射实现，轻量但功能有限。
- **CGLIB**：基于继承，字节码增强，功能强但有 `final` 限制。
- **选择依据**：目标是否有接口、性能需求、是否允许第三方依赖。

::: info CGLIB 的现状与 ByteBuddy 的崛起

:::

CGLIB 曾是 Java 生态中字节码增强的事实标准，但近年来已被 **ByteBuddy** 逐步取代：

| 对比            | CGLIB                                  | ByteBuddy                                  |
| :-------------- | :------------------------------------- | :----------------------------------------- |
| **活跃度**      | 2015 年后几乎停更                      | 持续活跃维护（最新版本 2024+）             |
| **API 易用性**  | 低（`Enhancer` + `MethodInterceptor`） | 高（流式 API + 类型安全）                  |
| **JDK 兼容性**  | JDK 17+ 反射限制导致报错               | 完美支持 JDK 8~21+                         |
| **Spring 选择** | Spring 4.x 之前默认                    | Spring 5+ / Spring Boot 3+ 转向 ByteBuddy  |
| **Hibernate**   | —                                      | Hibernate 5+ 使用 ByteBuddy 替代 Javassist |
| **Mockito**     | —                                      | Mockito 2+ 放弃 CGLIB，全面迁移 ByteBuddy  |

面试中如果能说出"Spring Boot 3.x 已经默认使用 ByteBuddy 而非 CGLIB"，表明你关注生态演进，而非停留在历史答案。

## Java 注解

### 【中等】Java 中的注解原理是什么？⭐⭐

**注解通过编译期处理（APT）或运行时反射实现元数据编程，其本质是特殊接口，由 JVM 或工具库按生命周期策略处理。**

**注解本质**

- **元数据标签**：注解本质是继承自 `java.lang.annotation.Annotation` 的接口
- **编译后保留策略**：通过 `@Retention` 指定生命周期
  - `SOURCE`：仅保留在源码（如 `@Override`）
  - `CLASS`：保留到字节码（默认）
  - `RUNTIME`：运行时可通过反射读取（如 `@SpringBootApplication`）

**核心处理机制**

- **编译期处理**：
  - **APT（Annotation Processing Tool）**：在编译时生成代码（如 Lombok）
  - **编译器检查**：如 `@Override` 验证方法重写
- **运行时处理**：
  - **反射读取**：通过 `getAnnotation()` 获取注解信息（如 Spring 扫描 `@Component`）
  - **动态代理**：结合 AOP 实现功能增强（如 `@Transactional`）

**关键技术点**

- **元注解**：修饰注解的注解（如 `@Target` 指定作用目标）
- **注解属性**：本质是接口方法（需编译时常量值）
- **字节码操作**：ASM 等工具可直接修改字节码中的注解信息

**应用场景**

- **框架配置**：Spring 的 `@Autowired`、`@RequestMapping`
- **代码生成**：Lombok 的 `@Data`
- **静态检查**：`@Nullable`、`@Deprecated`

### 【中等】如何自定义注解并使用注解处理器？⭐⭐

**自定义注解**：

```java
@Target(ElementType.METHOD)           // 作用目标
@Retention(RetentionPolicy.RUNTIME)   // 保留策略
@Documented
public @interface MyAnnotation {
    String value() default "";
    int priority() default 0;
}
```

**元注解详解**：

| **元注解**    | **作用**                                   |
| ------------- | ------------------------------------------ |
| `@Target`     | 作用目标（TYPE/FIELD/METHOD/PARAMETER 等） |
| `@Retention`  | 保留策略（SOURCE/CLASS/RUNTIME）           |
| `@Documented` | Javadoc 包含                               |
| `@Inherited`  | 子类继承（仅类级别）                       |
| `@Repeatable` | 可重复（Java 8+）                          |

**使用与读取**：

```java
@MyAnnotation(value = "test", priority = 1)
public void process() { ... }

// 反射读取
Method method = clazz.getMethod("process");
MyAnnotation anno = method.getAnnotation(MyAnnotation.class);
```

**注解处理器（Annotation Processor）**：编译期处理注解，生成代码（如 Lombok）。

```java
@SupportedAnnotationTypes("com.example.MyAnnotation")
public class MyProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {
        for (Element e : roundEnv.getElementsAnnotatedWith(MyAnnotation.class)) {
            // 用 Filer 生成源文件
        }
        return true;
    }
}
```

注册：`META-INF/services/javax.annotation.processing.Processor` 写入全限定名。

**典型应用**：Lombok、ButterKnife、MapStruct、Dagger。

## Java 枚举

### 【中等】Java 枚举的原理是什么？⭐⭐

Java 枚举（`enum`）从 JDK 5 引入，**本质是继承自 `java.lang.Enum` 的 final 类**，每个枚举常量是类的单例实例。

**编译前**：

```java
public enum Color {
    RED, GREEN, BLUE;
}
```

**编译后等价于**（伪代码）：

```java
public final class Color extends java.lang.Enum<Color> {
    public static final Color RED = new Color("RED", 0);
    public static final Color GREEN = new Color("GREEN", 1);
    public static final Color BLUE = new Color("BLUE", 2);

    private Color(String name, int ordinal) { super(name, ordinal); }

    public static Color[] values() { /* 返回所有常量 */ }
    public static Color valueOf(String name) { /* 按名称查找 */ }
}
```

**枚举的核心特性**：

| **特性**        | **说明**                                    |
| --------------- | ------------------------------------------- |
| **继承关系**    | 隐式继承 `java.lang.Enum`，无法再继承其他类 |
| **final 修饰**  | 枚举类不可被继承（防止破坏单例）            |
| **实例唯一性**  | 每个常量是 JVM 级别的单例（类加载时创建）   |
| **可定义成员**  | 字段、方法、构造器（仅 private 包访问）     |
| **可实现接口**  | 弥补无法继承的限制                          |
| **支持 switch** | 编译器优化为 `ordinal` 比较                 |

**带属性和方法的枚举**：

```java
public enum OrderStatus {
    PENDING(0, "待支付"),
    PAID(1, "已支付"),
    SHIPPED(2, "已发货"),
    COMPLETED(3, "已完成");

    private final int code;
    private final String desc;

    OrderStatus(int code, String desc) {  // 构造器默认 private
        this.code = code;
        this.desc = desc;
    }

    public int getCode() { return code; }
    public String getDesc() { return desc; }

    // 实现接口方法或抽象方法
    public boolean canCancel() {
        return this == PENDING;
    }
}
```

**枚举与反射**：

- `Constructor.newInstance()` **禁止创建枚举对象**（源码有强制检查）。
- `Enum.valueOf()` 是获取枚举实例的安全方式。

### 【中等】为什么说枚举是实现单例的最佳方式？⭐⭐

《Effective Java》Item 3 明确推荐：**单元素的枚举类型是实现 Singleton 的最佳方法**。

**枚举单例的优势**：

```java
public enum Singleton {
    INSTANCE;

    public void doSomething() { /* ... */ }
}

// 使用
Singleton.INSTANCE.doSomething();
```

| **对比维度**     | **枚举单例** | **饿汉式**        | **懒汉式（DCL）**        | **静态内部类**    |
| ---------------- | ------------ | ----------------- | ------------------------ | ----------------- |
| **线程安全**     | ✔️ JVM 保证  | ✔️ 类加载保证     | ✔️ volatile+synchronized | ✔️ 类加载保证     |
| **防反射攻击**   | ✔️ 强制禁止  | ❌ 可破坏         | ❌ 可破坏                | ❌ 可破坏         |
| **防序列化破坏** | ✔️ 自动处理  | ❌ 需 readResolve | ❌ 需 readResolve        | ❌ 需 readResolve |
| **懒加载**       | ❌ 否        | ❌ 否             | ✔️ 是                    | ✔️ 是             |
| **代码简洁**     | ⭐ 最简洁    | 简单              | 复杂                     | 较简单            |

**枚举单例的底层保证**：

1. **类加载线程安全**：枚举实例在类加载的 `<clinit>` 阶段创建，JVM 保证原子性。
2. **反射防御**：`Constructor.newInstance()` 源码中检查 `Enum`，直接抛异常。
3. **序列化特殊处理**：枚举的序列化/反序列化由 JVM 特殊处理，仅写入名称，反序列化时通过 `valueOf` 返回已有实例。

**枚举单例的局限**：

- **无法懒加载**：枚举类加载时即创建实例。
- **无法继承其他类**（枚举已继承 `Enum`）。

### 【简单】EnumMap 和 EnumSet 有什么用？⭐

`EnumMap` 和 `EnumSet` 是专为枚举优化的高性能容器，**基于序号（ordinal）的数组实现**。

**EnumMap**：

```java
enum Day { MON, TUE, WED, THU, FRI, SAT, SUN }

EnumMap<Day, String> schedule = new EnumMap<>(Day.class);
schedule.put(Day.MON, "开会");
schedule.put(Day.FRI, "周报");

// 内部实现：Object[] values = new Object[Day.values().length]
// 索引 = key.ordinal()
```

**EnumSet**：

```java
EnumSet<Day> weekend = EnumSet.of(Day.SAT, Day.SUN);
EnumSet<Day> workdays = EnumSet.range(Day.MON, Day.FRI);
EnumSet<Day> all = EnumSet.allOf(Day.class);

// 内部实现：当枚举数 ≤ 64 时用 RegularEnumSet（一个 long 位图）
// 否则用 JumboEnumSet（long[] 位图）
```

**性能对比**：

| **容器** | **底层**             | **时间复杂度** | **内存占用**         |
| -------- | -------------------- | -------------- | -------------------- |
| EnumMap  | 数组（索引=ordinal） | O(1)           | 极小（固定长度数组） |
| EnumSet  | 位图（bitmask）      | O(1)           | 极小（1 个 long）    |
| HashMap  | 哈希表               | O(1) 平均      | 较大（节点+桶）      |
| HashSet  | HashMap              | O(1) 平均      | 较大                 |

**适用场景**：

- 枚举作为键的 Map → 用 `EnumMap` 替代 `HashMap`。
- 枚举集合操作（权限、状态组合）→ 用 `EnumSet` 替代 `HashSet`。

## Java SPI

### 【中等】什么是 SPI，有什么用？⭐⭐⭐

SPI 通过`接口+配置文件`实现**运行时服务发现**，是解耦和扩展的利器，JDBC/日志等经典框架均基于此机制。

SPI 是 Java 提供的**服务发现机制**，通过**接口与实现分离**，实现：

- **运行时动态加载实现类**
- **解耦接口与实现**
- **可插拔式扩展**

**核心组成**

| 组件         | 作用         | 示例                             |
| ------------ | ------------ | -------------------------------- |
| **接口**     | 定义服务标准 | `java.sql.Driver`                |
| **实现类**   | 提供具体功能 | `com.mysql.cj.jdbc.Driver`       |
| **配置文件** | 声明实现类   | `META-INF/services/接口全限定名` |

**工作原理**

- 在`META-INF/services/`下创建以**接口全限定名**命名的文件
- 文件中写入**实现类全限定名**（每行一个）
- 通过`ServiceLoader`动态加载实现类

**主要应用场景**

- **JDBC 驱动加载**（`DriverManager`）
- **日志门面实现**（SLF4J → Logback/Log4j）
- **Spring Boot 自动配置**
- **Dubbo 扩展点机制**

**优势与局限**

| **优势**       | **局限**                                  |
| -------------- | ----------------------------------------- |
| 实现热插拔     | 配置文件需严格规范                        |
| 解耦接口与实现 | 原生 SPI 会加载所有实现类（可能浪费资源） |
| 扩展性强       | 无默认实现筛选机制                        |

**与 API 的区别**

| **维度** | **SPI**                  | **API**                  |
| -------- | ------------------------ | ------------------------ |
| 调用方向 | 由实现方提供，调用方选择 | 由提供方定义，调用方使用 |
| 控制权   | 调用方控制               | 提供方控制               |
| 典型场景 | JDBC 驱动、日志实现      | Java 标准库              |

**改进方案**

- **Dubbo SPI**：增加按需加载、扩展点缓存等优化
- **Spring Factories**：`META-INF/spring.factories`机制

## Java IO

### 【简单】什么是序列化？什么是反序列化？⭐⭐⭐

**基本概念**

- **序列化**：将对象转换为**字节流**（用于存储/传输）
- **反序列化**：将字节流恢复为对象

**核心用途**

- **持久化存储**（如保存到文件/数据库）
- **网络传输**（如 RPC 调用）
- **深拷贝实现**（通过序列化+反序列化）

**Java 实现方式**

| 方式                             | 特点                       | 示例                                   |
| -------------------------------- | -------------------------- | -------------------------------------- |
| **`Serializable`接口**           | 标记接口，默认 Java 序列化 | `class User implements Serializable`   |
| **`Externalizable`接口**         | 需手动实现读写逻辑         | 覆盖`writeExternal()`/`readExternal()` |
| **第三方库**（JSON/Protobuf 等） | 跨语言、高效               | Gson、Jackson、Protobuf                |

**关键注意事项**

- **`serialVersionUID`**：显式声明版本号，避免反序列化失败

  ```java
  private static final long serialVersionUID = 1L;
  ```

- **敏感字段处理**：用`transient`跳过序列化

  ```java
  private transient String password;  // 不会被序列化
  ```

- **性能优化**：

  - 避免序列化大对象
  - 第三方库（如 Protobuf）比 Java 原生序列化更快

**常见序列化协议对比**

| 协议          | 语言支持 | 可读性 | 性能 | 典型应用 |
| ------------- | -------- | ------ | ---- | -------- |
| **Java 原生** | 仅 Java  | 差     | 低   | Java RMI |
| **JSON**      | 多语言   | 好     | 中   | Web API  |
| **Protobuf**  | 多语言   | 差     | 高   | gRPC     |
| **Hessian**   | 多语言   | 差     | 中   | Dubbo    |

**安全风险**

- **反序列化漏洞**：恶意字节流可触发代码执行（需校验数据来源）
- **解决方案**：
  - 使用白名单控制反序列化类
  - 替换为 JSON 等文本协议

### 【中等】Java 提供了哪些 IO 方式？⭐⭐⭐

Java 提供了多种 I/O（输入输出）方式，主要分为 **传统 I/O（BIO）、NIO（New I/O）、AIO（异步 I/O）** 三大类，并支持 **文件操作、网络通信、序列化** 等场景。以下是主要 I/O 方式的概述及要点：

::: info 什么是 BIO？

:::

传统 I/O（BIO，Blocking I/O）是同步阻塞式 I/O，适用于连接数较少、延迟不敏感的场景。

**核心类**：

- **字节流**：`InputStream` / `OutputStream`（如 `FileInputStream`、`FileOutputStream`）
- **字符流**：`Reader` / `Writer`（如 `FileReader`、`FileWriter`）
- **缓冲流**：`BufferedReader`、`BufferedWriter`（提升性能）
- **标准 I/O**：`System.in`（输入）、`System.out`（输出）

**示例**：

```java
try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
    String line;
    while ((line = reader.readLine()) != null) {
        System.out.println(line);
    }
}
```

**缺点**：每个连接需要独立的线程，高并发时资源消耗大。

::: info 什么是 NIO？

:::

NIO（New I/O，Non-blocking I/O）是同步非阻塞 I/O，基于 **通道（Channel）** 和 **缓冲区（Buffer）**，支持多路复用（Selector）。

**核心类**：

- **Buffer**：`ByteBuffer`、`CharBuffer`（数据存储）
- **Channel**：`FileChannel`、`SocketChannel`、`ServerSocketChannel`（数据传输）
- **Selector**：监听多个通道的事件（如连接、读、写）

**示例（NIO 文件复制）**：

```java
try (FileChannel src = FileChannel.open(Paths.get("src.txt"));
     FileChannel dest = FileChannel.open(Paths.get("dest.txt"), StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
    src.transferTo(0, src.size(), dest);
}
```

- **优点**：单线程可处理多个连接，适合高并发（如 Netty 框架底层）。
- **缺点**：编程复杂度较高。

::: info 什么是 AIO？

:::

AIO（Asynchronous I/O）是异步非阻塞 I/O，基于回调或 Future 机制，适用于高吞吐场景。

**核心类**：

- `AsynchronousFileChannel`（文件操作）
- `AsynchronousSocketChannel`（网络通信）
- `CompletionHandler`（回调接口）

**示例（AIO 文件读取）**：

```java
AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(Paths.get("file.txt"));
ByteBuffer buffer = ByteBuffer.allocate(1024);
fileChannel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        System.out.println("Read " + result + " bytes");
    }
    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        exc.printStackTrace();
    }
});
```

- **优点**：真正异步，适合长连接、高吞吐场景（如大文件传输）。
- **缺点**：JDK 实现较少，Linux 支持有限（底层依赖 epoll）。

::: info 有哪些常见的 IO 工具？

:::

- **序列化**：`ObjectInputStream` / `ObjectOutputStream`（Java 原生序列化）
- **压缩流**：`GZIPInputStream`、`ZipOutputStream`
- **内存映射文件**：`MappedByteBuffer`（NIO 高性能文件访问）
- **Files 工具类**（Java 7+）：
  ```java
  Files.readAllLines(Paths.get("file.txt")); // 快速读取文件
  ```

::: info BIO vs. NIO vs. AIO？

:::

| 类型 | 模型       | 适用场景           | 典型框架          |
| ---- | ---------- | ------------------ | ----------------- |
| BIO  | 同步阻塞   | 低并发、简单 I/O   | Java Socket       |
| NIO  | 同步非阻塞 | 高并发、网络通信   | Netty、Tomcat NIO |
| AIO  | 异步非阻塞 | 高吞吐、大文件操作 | 较少使用          |

**选择建议**：

- **BIO**：简单文件操作或低并发场景。
- **NIO**：高并发网络编程（如 Netty）。
- **AIO**：需要真正异步 I/O 的场景（但实际使用较少）。

如果需要更高层次的封装，可以考虑 **Apache Commons IO**、**Guava** 等工具库。

### 【困难】NIO 如何实现多路复用？⭐⭐⭐

::: info Java NIO 的核心组件有哪些？

:::

Java NIO 多路复用的核心是通过 **Selector 轮询事件** + **非阻塞 Channel** + **Buffer 数据交换**，允许单线程管理多个通道的 I/O 操作。这是构建高性能网络应用的基础，也是 Netty 等框架的底层原理。

**Java NIO 核心组件**

- **Selector（选择器）**：核心多路复用器，可监控多个 `Channel` 的 I/O 事件（如连接、读、写）
  - 通过 `Selector.open()` 创建
  - 一个 `Selector` 可绑定多个 `Channel`
- **Channel（通道）**：非阻塞 I/O 操作的抽象，支持读写。主要类型：
  - `SocketChannel`：TCP 网络通信
  - `ServerSocketChannel`：监听 TCP 连接
  - `FileChannel`：文件 I/O（不支持 Selector）
- **Buffer（缓冲区）**：数据容器（如 `ByteBuffer`），`Channel` 通过 `Buffer` 读写数据。

::: info Java NIO 多路复用的实现步骤是怎样的？

:::

**多路复用实现步骤**

**(1) 创建 Selector 并注册 Channel**

```java
Selector selector = Selector.open();
ServerSocketChannel serverChannel = ServerSocketChannel.open();
serverChannel.configureBlocking(false); // 必须设为非阻塞
serverChannel.register(selector, SelectionKey.OP_ACCEPT); // 注册监听事件
```

**(2) 事件类型**

- `SelectionKey.OP_ACCEPT`：接受连接（`ServerSocketChannel`）
- `SelectionKey.OP_CONNECT`：连接就绪（`SocketChannel`）
- `SelectionKey.OP_READ`：数据可读
- `SelectionKey.OP_WRITE`：数据可写

**(3) 事件轮询**

```java
while (true) {
    int readyChannels = selector.select(); // 阻塞直到有事件就绪
    if (readyChannels == 0) continue;

    Set<SelectionKey> selectedKeys = selector.selectedKeys();
    Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

    while (keyIterator.hasNext()) {
        SelectionKey key = keyIterator.next();

        if (key.isAcceptable()) {
            // 处理新连接
        } else if (key.isReadable()) {
            // 处理读事件
        } else if (key.isWritable()) {
            // 处理写事件
        }

        keyIterator.remove(); // 必须移除已处理的键
    }
}
```

::: info Java NIO 的关键机制有哪些？

:::

**(1) 非阻塞模式**

- Channel 必须设置为非阻塞：`channel.configureBlocking(false)`
- 避免单线程因 I/O 操作阻塞

**(2) 事件驱动**

- Selector 通过操作系统级轮询（如 Linux 的 `epoll`）监听事件
- 仅处理活跃的 `Channel`，避免无效遍历

**(3) SelectionKey**

- 绑定 Channel 与 Selector 的关系
- 可通过 `key.attachment()` 附加自定义对象（如会话状态）

::: info Java NIO 的底层原理是什么？

:::

- **Linux**：基于 `epoll` 实现（高效监控大量文件描述符）
- **Windows**：基于 `IOCP`（完成端口）
- 相比传统 BIO 的线程池模型，NIO 单线程可处理数千连接

**NIO 优点**

- 单线程管理多连接，资源消耗低
- 高并发支持（如 Netty 框架底层依赖 NIO）
- 避免线程上下文切换开销

**NIO 适用场景**

- 高并发网络服务（如聊天服务器、API 网关）
- 需要长连接的应用（如 WebSocket）
- 大数据量、低延迟的 I/O 操作

### 【困难】Java 写入文件到磁盘会经历哪些过程？⭐⭐

::: important 要点

- **四级流水**：用户缓冲区 → 内核页缓存 → 磁盘缓存 → 物理介质。
- **两次复制**：默认路径下数据在用户和内核空间之间有一份拷贝。
- **持久化分水岭**：`write` 返回仅入内核缓存，`fsync` 才落盘。
- **零拷贝**：`transferTo` 消除用户空间拷贝，但数据仍可能在内核缓存停留。
- **刷盘策略**：异步定时 + 内存压力 + 显式同步。

:::

::: info 详细流程
:::

总体流程

```
[Java 代码] → [JVM 堆缓冲区] → [系统调用] → [内核页缓存] → [磁盘控制器] → [物理介质]
```

**应用层写入**

- **直接写入**：调用 `FileOutputStream.write(byte[])`，通过 JNI 进入 native 方法。
- **缓冲写入**：使用 `BufferedOutputStream`，数据先写入 JVM 堆内缓冲区（默认 8KB），满缓冲区时才触发系统调用，以减少频繁的上下文切换。

**系统调用与用户态→内核态切换**

- JVM 发起 `write()` 系统调用，CPU 从用户态切换到内核态。
- 数据从 JVM 堆内存（用户空间）复制到内核空间的 **页缓存**（Page Cache）。此复制是必须的，因为内核不能直接访问用户进程内存。

**内核页缓存管理**

- 写入的数据暂存在 Page Cache 中，对应内存页被标记为 **脏页**（Dirty）。
- **读优化**：后续读可直接命中缓存，避免磁盘 I/O。
- **刷盘触发时机**：
  - **定时回写**：内核线程（如 pdflush）周期性扫描脏页，默认 30 秒刷盘。
  - **内存压力**：可用内存低于阈值时强制刷盘。
  - **显式同步**：应用程序调用 `fsync()` 或 `fdatasync()`，立即将指定文件的脏页刷入磁盘。
  - **文件关闭**：`close()` 会隐含刷新，但不保证物理落盘（依赖于文件系统实现）。

**硬件层写入**

- 内核通过设备驱动程序向磁盘控制器发送指令。
- **磁盘缓存**：若磁盘启用了写缓存，数据可能先写入磁盘的易失性缓存，随后才真正写入盘片。此时系统调用返回成功，但数据仍未持久化。
- **物理写入**：最终数据磁化到机械盘片或写入闪存单元。

::: info 挑战
:::

**数据持久化保证**

- **`write()` 返回**：仅表示数据已复制到内核页缓存，**不保证落盘**。若系统崩溃，数据可能丢失。
- **`flush()` 作用**：仅刷新 JVM 用户缓冲区到内核，不触发 `fsync`，因此仍不能保证落盘。
- **强制落盘 API**：
  - `FileDescriptor.sync()`：调用 `fsync()`，同步文件数据和元数据。
  - `FileChannel.force(boolean metaData)`：参数为 `true` 时同时刷新文件元数据。

**零拷贝**

- **`FileChannel.transferTo()`**：数据直接从内核页缓存发送到目标通道（如 Socket），**避免一次用户空间拷贝**，显著提升性能。
- **内存映射文件 `MappedByteBuffer`**：将文件区域映射到进程地址空间，通过内存操作读写，缺页时由内核加载，修改后的数据由内核异步刷盘。

**直接 I/O 与标准 I/O**

- **标准 I/O**：通过页缓存，适合大多数应用。
- **直接 I/O**：绕过页缓存，直接与磁盘交互（需文件系统支持，如 Linux `O_DIRECT` 标志），适用于数据库等自管理缓存的系统，但要求用户缓冲区对齐。

**写放大与随机小写**

- 每次写入可能引发整个页（通常 4KB）的“读-修改-写”操作，称为写放大。小写入应尽量合并（如使用 BufferedOutputStream）。

**内核参数调优（Linux）**

- `/proc/sys/vm/dirty_ratio`：脏页占用内存百分比上限，触发刷盘。
- `/proc/sys/vm/dirty_expire_centisecs`：脏页最长存活时间（默认 30 秒）。
- `/proc/sys/vm/dirty_writeback_centisecs`：内核回写线程唤醒间隔。

## Java 语法糖

### 【中等】Java 中有哪些常见的语法糖？⭐⭐

**语法糖（Syntactic sugar）** 代指的是编程语言为了方便程序员开发程序而设计的一种特殊语法，这种语法对编程语言的功能并没有影响。实现相同的功能，基于语法糖写出来的代码往往更简单简洁且更易阅读。

Java 中最常用的语法糖主要有泛型、自动拆装箱、变长参数、枚举、内部类、增强 for 循环、try-with-resources 语法、lambda 表达式等。所有这些语法糖在编译阶段都会被"脱糖"(desugar)，即转换为更基础的 Java 语法结构。可以使用`javap -c`命令查看字节码来验证这一点。语法糖虽然不增加语言功能，但能显著提高代码的可读性和编写效率，是 Java 语言不断演进的重要组成部分。

**自动装箱与拆箱 (Autoboxing/Unboxing)**

```java
// 自动装箱
Integer i = 10;  // 实际编译为 Integer.valueOf(10)

// 自动拆箱
int n = i;      // 实际编译为 i.intValue()
```

**增强 for 循环 (foreach)**

```java
List<String> list = Arrays.asList("a", "b", "c");
// 语法糖形式
for (String s : list) {
    System.out.println(s);
}
// 实际编译为迭代器模式
for (Iterator<String> it = list.iterator(); it.hasNext();) {
    String s = it.next();
    System.out.println(s);
}
```

**变长参数 (Varargs)**

```java
public void print(String... args) {
    for (String arg : args) {
        System.out.println(arg);
    }
}
// 实际编译为数组参数
public void print(String[] args) { ... }
```

**数值字面量下划线**

```java
int million = 1_000_000;  // 编译后等同于 1000000
```

**字符串拼接**

```java
String s = "a" + "b" + "c";
// 编译优化为
String s = "abc";

// 变量拼接会转为 StringBuilder
String a = "a", b = "b";
String result = a + b;
// 编译为
String result = new StringBuilder().append(a).append(b).toString();
```

**switch 支持字符串 (Java 7+)**

```java
String fruit = "apple";
switch (fruit) {
    case "apple":
        System.out.println("It's an apple");
        break;
    // 实际编译为基于 hashCode() 和 equals() 的比较
}
```

**默认构造方法**

```java
public class Person {}
// 如果没有显式定义构造方法，编译器会自动添加无参构造方法
```

**枚举类 (Java 5+)**

```java
enum Color { RED, GREEN, BLUE }
// 实际编译为继承 java.lang.Enum 的类
```

**内部类访问外部类成员**

```java
class Outer {
    private int x = 10;
    class Inner {
        void print() {
            System.out.println(x);  // 实际通过 Outer.this.x 访问
        }
    }
}
```

**方法引用 (Java 8+)**

```java
List<String> list = Arrays.asList("a", "b", "c");
list.forEach(System.out::println);
// 编译为 lambda 表达式
list.forEach(s -> System.out.println(s));
```

**钻石操作符 (Diamond Operator, Java 7+)**

```java
List<String> list = new ArrayList<>();  // 类型推断
// Java 7 之前需要
List<String> list = new ArrayList<String>();
```

**集合字面量 (Java 9+ 的 List.of 等）**

```java
List<String> list = List.of("a", "b", "c");
Set<Integer> set = Set.of(1, 2, 3);
Map<String, Integer> map = Map.of("a", 1, "b", 2);
```

**Lambda 表达式 (Java 8+)**

```java
// Lambda 表达式
Runnable r = () -> System.out.println("Hello");
// 实际生成实现 Runnable 的匿名类
```

**try-with-resources (Java 7+)**

```java
try (InputStream is = new FileInputStream("file.txt")) {
    // 使用资源
}  // 自动调用 close()
// 编译为 try-finally 块
```

**接口中的默认方法和静态方法 (Java 8+)**

```java
interface MyInterface {
    default void defaultMethod() {
        System.out.println("Default method");
    }

    static void staticMethod() {
        System.out.println("Static method");
    }
}
```

**记录类 (Record, Java 14+)**

```java
record Point(int x, int y) {}
// 编译后自动生成：
// - 私有 final 字段 x 和 y
// - 公共构造方法
// - 访问器方法 x() 和 y()
// - equals(), hashCode(), toString()
```

**`instanceof` 模式匹配**

```java
if (obj instanceof String s) {
    // 可以直接使用 s
    System.out.println(s.length());
}
```

**文本块 (Text Blocks, Java 15+)**

```java
String html = """
    <html>
        <body>
            <p>Hello, world</p>
        </body>
    </html>
    """;
```

## Java 新特性

### 【中等】Java 8 的 Optional 的正确使用方式？⭐⭐⭐

`Optional` 是 Java 8 引入的**容器对象**，优雅处理可能为 `null` 的值。

**核心方法**：

| **方法**                     | **说明**                       |
| ---------------------------- | ------------------------------ |
| `of(T)`                      | 非 null 创建（null 抛 NPE）    |
| `ofNullable(T)`              | 允许 null                      |
| `isPresent()` / `isEmpty()`  | 是否有值（Java 11+ `isEmpty`） |
| `orElse(T)`                  | 无值返回默认                   |
| `orElseGet(Supplier)`        | 懒加载默认                     |
| `orElseThrow()`              | 无值抛异常                     |
| `map` / `flatMap` / `filter` | 链式转换                       |

**正确用法**：

```java
String name = Optional.ofNullable(user)
    .map(User::getProfile)
    .map(Profile::getName)
    .orElse("unknown");

// 方法返回类型
public Optional<Address> findAddress(String userId) { ... }
```

**反模式（应避免）**：

```java
// ❌ 字段类型（Optional 不可序列化）
private Optional<String> name;

// ❌ 方法参数
public void process(Optional<String> input) { ... }

// ❌ 直接 get
String s = optional.get();  // 可能 NPE
```

### 【中等】Java 8 的 Lambda 表达式和函数式接口是什么？⭐⭐⭐⭐

**Lambda 表达式**是 Java 8 引入的**匿名函数**，将行为作为参数传递，简化函数式编程。

```java
// 传统匿名类
Collections.sort(list, new Comparator<String>() {
    public int compare(String a, String b) { return a.length() - b.length(); }
});

// Lambda 表达式
Collections.sort(list, (a, b) -> a.length() - b.length());
// 方法引用
list.sort(Comparator.comparingInt(String::length));
```

**核心语法**：`(参数列表) -> { 方法体 }`

| 形式               | 示例                                     |
| :----------------- | :--------------------------------------- |
| 无参               | `() -> System.out.println("hello")`      |
| 单参（可省略括号） | `s -> s.length()`                        |
| 多参               | `(a, b) -> a + b`                        |
| 方法引用           | `String::valueOf`、`System.out::println` |

**函数式接口**：只有**一个抽象方法**的接口，用 `@FunctionalInterface` 注解。Lambda 本质是函数式接口的实例。

| 函数式接口          | 方法                | 用途                  |
| :------------------ | :------------------ | :-------------------- |
| `Function<T,R>`     | `R apply(T t)`      | 输入 T 输出 R（转换） |
| `Consumer<T>`       | `void accept(T t)`  | 消费 T（无返回）      |
| `Supplier<T>`       | `T get()`           | 生产 T（无输入）      |
| `Predicate<T>`      | `boolean test(T t)` | 判断 T（返回布尔）    |
| `BiFunction<T,U,R>` | `R apply(T t, U u)` | 双输入单输出          |

#### 🔬 Lambda 的底层实现：invokedynamic（关键区分）

**⚠️ 常见误区**：Lambda 是匿名内部类的语法糖。

**真相**：Lambda 和匿名内部类在 JVM 层面**完全不同**。匿名内部类在**编译期**生成 `ClassName$1.class` 文件（每次 `new` 创建一个新对象）；Lambda 在**运行期**通过 `invokedynamic` 指令动态链接，由 `LambdaMetafactory` 生成方法句柄，**不生成 `.class` 文件，也不保证每次创建新对象**。

```asm
; 匿名内部类字节码：编译期生成独立类
 0: new #7       // 编译期确定: new AnonymousClass$1
 3: dup
 4: invokespecial #9  // 调用 AnonymousClass$1.<init>

; Lambda 字节码：运行时动态链接
invokedynamic #14  // BootstrapMethod: LambdaMetafactory.metafactory()
                   // 静态参数: ()V, 函数式接口方法, lambda body 实现
```

**`invokedynamic` 的工作流程**：

```
1. JVM 首次遇到 invokedynamic 指令
   → 调用 Bootstrap Method: LambdaMetafactory.metafactory()

2. LambdaMetafactory 在运行时生成一个实现函数式接口的类
   → 通过 ASM 直接生成字节码
   → 通过 Unsafe.defineAnonymousClass 加载（不生成 .class 文件）

3. 返回一个 CallSite（调用点），后续调用直接使用该 CallSite
   → 返回的可能是一个新对象，也可能是缓存的单例（取决于是否捕获外部变量）
```

**Lambda 是否每次创建新对象？**

```java
// 不捕获外部变量 → JVM 可能复用同一个实例（单例）
Supplier<String> s1 = () -> "hello";
Supplier<String> s2 = () -> "hello";
System.out.println(s1 == s2);  // 可能为 true！（JVM 优化为常量）

// 捕获外部变量 → 每次创建新对象
String prefix = "msg: ";
Supplier<String> s3 = () -> prefix + "hello";  // 每次 new 一个对象

// 匿名内部类 → 每次一定 new 新对象
Supplier<String> s4 = new Supplier<>() {  // 每个 new 都是不同对象
    public String get() { return "hello"; }
};
```

> **📌 面试度量**：⭐⭐⭐⭐ 的原因——90% 的人认为 Lambda 就是匿名类的语法糖。能说出 `invokedynamic` 指令、`LambdaMetafactory` Bootstrap Method、以及"不捕获变量时返回单例 vs 捕获变量时返回新对象"的区别，直达到 L3 级别。

**记忆点**：Lambda = "行为参数化"，函数式接口 = "只有一个抽象方法的接口"，底层 = `invokedynamic` + `LambdaMetafactory`（区别于匿名内部类的编译期类生成）。

#### 跨语言视角：Lambda/Closure 的四种实现策略

| 语言       | 实现策略                            | 核心差异                                                      |
| :--------- | :---------------------------------- | :------------------------------------------------------------ |
| **Java**   | `invokedynamic` + 运行时生成 SAM 类 | 编译期不产生 `.class`，运行时动态链接                         |
| **C++**    | 编译期生成匿名仿函数类              | 零运行时开销，但每个 lambda 产生**不同**类型（即使签名相同）  |
| **Rust**   | 编译期生成匿名结构体 + trait impl   | `Fn`/`FnMut`/`FnOnce` 三种 trait 区分捕获方式，所有权融入闭包 |
| **Python** | 运行时创建 `function` 对象          | 简单但慢——每次 `def` 或 `lambda` 都是对象创建，无 JIT 优化    |

C++ lambda 的 `[]` 捕获列表（`[=]` 按值、`[&]` 按引用、`[this]` 等）是对**按值捕获 vs 按引用捕获**最精细的控制，而 Java lambda 默认是 **effectively final 变量的隐式按值捕获**。Rust 更进一步，将所有权模型带入闭包——`move` 关键字将变量所有权移入闭包，编译器保证 use-after-move 在编译期捕获。

**一个有趣的事实**：C++ 每个 lambda 产生**不同的类型**（即使签名完全一致），这使得两个签名相同的 lambda 不能互相赋值。Java 采用**目标类型推断**——lambda 的类型取决于赋值的函数式接口，不同 lambda 只要匹配同一接口就可以互换。这是 Java "更灵活"的思路 vs C++ "更静态安全" 思路的典型体现。

### 【困难】Java 8 的 Stream API 的核心操作有哪些？⭐⭐⭐⭐

**Stream API（Java 8）**提供对集合的**声明式、链式、并行化**数据处理能力。

**核心流程**：`数据源 → 中间操作（链式） → 终端操作（触发执行）`

```java
List<String> names = users.stream()           // 数据源
    .filter(u -> u.getAge() > 18)              // 中间操作：过滤
    .sorted(Comparator.comparing(User::getName)) // 中间操作：排序
    .map(User::getName)                        // 中间操作：映射
    .distinct()                                // 中间操作：去重
    .limit(10)                                 // 中间操作：截断
    .collect(Collectors.toList());              // 终端操作：收集
```

**中间操作 vs 终端操作**：

| 类型         | 特点                              | 常见操作                                                                   |
| :----------- | :-------------------------------- | :------------------------------------------------------------------------- |
| **中间操作** | 返回 Stream，**懒执行**，链式调用 | `filter`、`map`、`flatMap`、`sorted`、`distinct`、`limit`、`skip`、`peek`  |
| **终端操作** | 触发实际计算，返回结果或副作用    | `collect`、`forEach`、`reduce`、`count`、`findFirst`、`anyMatch`、`toList` |

**reduce vs collect**：

```java
// reduce：元素归约为单个值
int sum = list.stream().reduce(0, Integer::sum);

// collect：元素收集到容器
Map<String, List<User>> groupByCity = users.stream()
    .collect(Collectors.groupingBy(User::getCity));
```

**并行流**：

```java
long count = list.parallelStream()  // 利用多核 CPU 并行处理
    .filter(x -> x > 0)
    .count();
```

**注意事项**：

- 并行流不适用于小数据集（线程开销 > 计算收益）
- 避免在并行流中使用有副作用的操作
- `findFirst` 在并行流中代价高（需全局同步），优先用 `findAny`

#### 🔬 Stream 的惰性求值机制：Sink 链

Stream 中间操作**不会立即执行**，而是构建一条 **Sink 链**（责任链模式），直到终端操作才触发整条链的执行：

```
数据源 → filter Sink → map Sink → sorted Sink → 终端 Sink
         ↑ 每个中间操作返回一个新的 Sink 包装前一个 Sink
```

```java
// 这段代码不会执行任何操作（无终端操作）
users.stream()
    .filter(u -> { System.out.println("filter"); return true; });  // 不打印！

// 只有加了终端操作，filter 才会被调用
users.stream()
    .filter(u -> { System.out.println("filter"); return true; })
    .collect(Collectors.toList());  // 此时才打印 "filter"
```

**短路操作**会提前终止遍历——终端操作 `findFirst()` 与中间操作 `limit()` 配合，找到第一个匹配元素后立即停止：

```java
// 只需找到第一个 > 18 的用户，不会遍历整个集合
users.stream()
    .filter(u -> u.getAge() > 18)
    .findFirst();  // 短路终端操作
```

> **关键原则**：`filter` 放在 `sorted` 前面（先减数据量再排序），`limit` 放在 `peek` 前面（先截断再调试）。操作顺序直接影响性能。

#### 🔬 并行流的拆分原理：Spliterator

并行流的底层依赖 **Spliterator**（Splittable Iterator），它定义了如何**递归拆分**数据源给多个线程：

```
原始数据 [1,2,3,4,5,6,7,8]
    trySplit() → [1,2,3,4] + [5,6,7,8]
    trySplit() → [1,2] + [3,4] + [5,6] + [7,8]
    4 个线程并行处理 4 个子流
```

**拆分效率**：ArrayList 的 `ArrayListSpliterator` 基于数组索引拆分（O(1)），LinkedList 的拆分需要先遍历到中点（O(n)），因此 LinkedList 并行流性能很差——需要 `collect(toList())` 转换为 ArrayList 后再并行。

> **📌 面试度量**：⭐⭐⭐⭐ 的原因——会用 Stream 的人很多，但能讲清楚**惰性求值 Sink 链**和**短路操作的终止时机**，以及**为什么 LinkedList 不适合并行流**（Spliterator 拆分复杂度）的，才说明真正理解了 Stream 的设计哲学。

#### 跨语言视角：惰性集合处理的设计谱系

| 语言/框架       | 惰性集合机制                                     | 核心差异                                                                       |
| :-------------- | :----------------------------------------------- | :----------------------------------------------------------------------------- |
| **Java Stream** | Sink 链 + 终端触发                               | push-based，按元素驱动（每个元素走完整条链）                                   |
| **C# LINQ**     | `IEnumerable<T>` + 迭代器                        | pull-based，按需拉取（类似生成器模式）                                         |
| **Python**      | 生成器 `yield` / 列表推导式                      | 生成器是 pull-based，边计算边产出；推导式是 eager 的                           |
| **Rust**        | `Iterator` trait + `map`/`filter`/`collect`      | 编译期单态化，零抽象成本——`map().filter().collect()` 展开后等价于手写 for 循环 |
| **Kotlin**      | `Sequence`（惰性） vs `Collection` 扩展（eager） | 与 Java Stream 几乎一样的设计：`asSequence()` 开启惰性，终端操作触发           |

**Java Stream 的 push-based 设计**：每个元素"被推入"整条 Sink 链的处理管道。这意味着 `sorted()` 这样的操作必须先收集所有元素才能排序——它是**有状态中间操作**，在管道中形成了一个"屏障"。相比之下，C# LINQ 的 pull-based 迭代器天然是惰性的，不需要显式的"终端操作"概念。

**Rust 的零成本迭代器**：Rust 的迭代器链（`iter().filter().map().sum()`）在编译后展开为等价的手写循环。因为 Rust 没有运行时反射和 GC，编译器可以在编译期完成内联和优化，运行时没有任何虚函数调用开销。这是 Java Stream 做不到的——Java 的每个 filter/map 操作至少经过一次接口方法分派。

### 【中等】Java 8 的接口的默认方法和静态方法是什么？⭐⭐⭐

Java 8 允许接口定义**默认方法（`default`）**和**静态方法**，解决了接口演化问题。

```java
public interface Logger {
    void log(String msg);  // 抽象方法

    // 默认方法：提供默认实现，实现类可选择重写
    default void info(String msg) {
        log("[INFO] " + msg);
    }

    // 静态方法：通过接口名直接调用
    static Logger of(String name) {
        return msg -> System.out.println(name + ": " + msg);
    }
}
```

**默认方法的菱形冲突规则**：

| 场景                     | 规则                                        |
| :----------------------- | :------------------------------------------ |
| 类方法 vs 接口默认方法   | **类优先**：类的实例方法始终胜出            |
| 两个接口有同名默认方法   | **编译报错**，必须在子接口/实现类中显式重写 |
| 子接口重写父接口默认方法 | 子接口的版本生效                            |

```java
// 菱形冲突解决
interface A { default void hello() { System.out.println("A"); } }
interface B extends A { default void hello() { System.out.println("B"); } }
class C implements A, B {
    // 必须显式指定
    public void hello() { B.super.hello(); }  // 选择 B 的实现
}
```

### 【中等】Java 8 的 java.time API 解决了什么问题？⭐⭐⭐

`java.time`（JSR-310）解决了 `java.util.Date`/`Calendar` 的三大痛点：**非线程安全、设计混乱、时区处理复杂**。

| 类              | 用途                   | 示例                                            |
| :-------------- | :--------------------- | :---------------------------------------------- |
| `LocalDate`     | 日期（无时间、无时区） | `LocalDate.of(2024, 1, 1)`                      |
| `LocalTime`     | 时间（无日期、无时区） | `LocalTime.of(14, 30)`                          |
| `LocalDateTime` | 日期 + 时间（无时区）  | `LocalDateTime.now()`                           |
| `ZonedDateTime` | 日期 + 时间 + 时区     | `ZonedDateTime.now(ZoneId.of("Asia/Shanghai"))` |
| `Instant`       | 时间戳（UTC）          | `Instant.now()`                                 |
| `Duration`      | 时间间隔（时分秒）     | `Duration.between(t1, t2)`                      |
| `Period`        | 日期间隔（年月日）     | `Period.between(d1, d2)`                        |

**核心优势**：

- **不可变且线程安全**：所有类都是 `final` + `immutable`
- **API 设计清晰**：`plus`/`minus`/`with` 语义明确
- **时区支持完善**：`ZoneId` + `ZonedDateTime`

```java
// 计算两个日期之间的天数
long days = ChronoUnit.DAYS.between(startDate, endDate);

// 格式化
String formatted = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
```

### 【中等】Java 9 引入的模块化系统（JPMS）有什么用？⭐

Java 9 引入**Java 平台模块系统（JPMS，Project Jigsaw）**，解决长期以来的**JAR 地狱**和**封装不足**问题。

**核心目标**：

- **强封装**：模块可显式声明哪些包对外暴露，**非导出包无法被反射访问**（即使 `setAccessible(true)`）。
- **可靠配置**：编译期和启动期检查模块依赖，提前发现缺失。
- **精简 JRE**：`jlink` 可打包**仅含所需模块**的定制 JRE，体积大幅缩小。

**模块定义示例**（`module-info.java`）：

```java
module com.example.app {
    requires java.sql;              // 依赖 java.sql 模块
    requires transitive java.base;  // 传递依赖
    exports com.example.api;        // 导出包，对外可见
    // com.example.internal 不导出，外部无法访问
    opens com.example.pojo to jackson;  // 仅对 jackson 反射开放
}
```

**关键关键字**：

| **关键字**            | **作用**                                    |
| --------------------- | ------------------------------------------- |
| `requires`            | 声明依赖                                    |
| `requires transitive` | 传递依赖（下游模块自动可用）                |
| `exports`             | 导出包（编译期+运行时可见）                 |
| `opens`               | 仅运行时反射开放（给框架如 Spring/Jackson） |
| `uses` / `provides`   | 服务接口与实现（SPI）                       |

**实际影响**：

- **库开发者**：可真正隐藏内部实现，反射也访问不了。
- **应用开发者**：依赖更清晰，但升级到 Java 9+ 时需处理未命名模块兼容性问题。
- **JDK 自身**：JDK 本身被拆分为约 90 个模块（`java.base`、`java.sql` 等）。

### 【中等】Java 11 的 var 局部变量类型推断怎么用？有什么限制？⭐⭐

**`var`（JDK 10 引入，JDK 11 扩展）**让编译器自动推断局部变量类型，减少冗余代码。

```java
// JDK 8：冗长的类型声明
Map<String, List<String>> map = new HashMap<String, List<String>>();

// var：编译器自动推断
var map = new HashMap<String, List<String>>();  // 类型仍然是 Map<String, List<String>>

// JDK 11 扩展：Lambda 参数上使用 var（可加注解）
list.stream().filter((@NotNull var s) -> s.length() > 5);
```

**使用限制**：

| 场景               | 是否支持 | 示例                                         |
| :----------------- | :------- | :------------------------------------------- |
| 局部变量           | ✔️       | `var list = new ArrayList<String>();`        |
| for 循环           | ✔️       | `for (var item : collection)`                |
| try-with-resources | ✔️       | `try (var reader = new BufferedReader(...))` |
| 方法参数           | ❌       | `void method(var x)` — 不允许                |
| 返回值             | ❌       | `var method()` — 不允许                      |
| 字段               | ❌       | `private var name;` — 不允许                 |
| 无初始化           | ❌       | `var x;` — 不允许，无法推断                  |
| 赋 null            | ❌       | `var x = null;` — 不允许，无法推断           |

**最佳实践**：仅在类型明显时（如构造器右侧）使用 `var`，避免降低代码可读性。

### 【中等】Java 11 的 HTTP Client API 有什么特点？⭐⭐

**`java.net.http.HttpClient`（JDK 11 正式版）**是 Java 原生异步 HTTP 客户端，替代老旧的 `HttpURLConnection`。

```java
HttpClient client = HttpClient.newHttpClient();

// 同步请求
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com/users"))
    .header("Content-Type", "application/json")
    .POST(HttpRequest.BodyPublishers.ofString("{\"name\":\"Tom\"}"))
    .build();

HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

// 异步请求（返回 CompletableFuture）
client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
    .thenApply(HttpResponse::body)
    .thenAccept(System.out::println);
```

**核心特点**：

| 特性             | 说明                                                           |
| :--------------- | :------------------------------------------------------------- |
| **同步 + 异步**  | `send()` 同步、`sendAsync()` 异步（返回 `CompletableFuture`）  |
| **HTTP/2**       | 默认支持 HTTP/2（多路复用、头部压缩）                          |
| **WebSocket**    | 内置 WebSocket 客户端支持                                      |
| **BodyHandlers** | 灵活处理响应体：`ofString`、`ofFile`、`ofByteArray`、`ofLines` |

### 【中等】Java 11 的字符串 API 有哪些增强？⭐

Java 11 为 `String` 类新增了多个实用方法：

| 方法              | 说明                         | 示例                              |
| :---------------- | :--------------------------- | :-------------------------------- |
| `isBlank()`       | 是否为空或纯空白字符         | `" ".isBlank()` → `true`          |
| `strip()`         | 去除首尾空白（支持 Unicode） | `" hello ".strip()` → `"hello"`   |
| `stripLeading()`  | 去除前导空白                 |                                   |
| `stripTrailing()` | 去除尾部空白                 |                                   |
| `lines()`         | 按行分割返回 Stream          | `"a\nb\nc".lines().count()` → `3` |
| `repeat(int)`     | 重复拼接                     | `"ab".repeat(3)` → `"ababab"`     |

**`strip()` vs `trim()`**：`strip()` 基于 `Character.isWhitespace()`，支持 Unicode 空白字符；`trim()` 仅处理 ASCII ≤ 32 的字符。

### 【中等】Java 11 对 GC 有哪些重要更新？⭐⭐

JDK 11 是 GC 领域的重要里程碑，引入了两个新一代垃圾收集器：

| 收集器          | JDK 版本 | 核心特点                                                 |
| :-------------- | :------- | :------------------------------------------------------- |
| **ZGC**（实验） | JDK 11   | 亚毫秒停顿（<10ms），支持 TB 级堆，基于着色指针 + 读屏障 |
| **Shenandoah**  | JDK 12   | 低延迟（与 ZGC 竞争），基于转发指针，Red Hat 开发        |

**其他 GC 变更**：

- **G1 成为默认 GC**（JDK 9 起）
- **CMS 被标记为废弃**（JDK 9），JDK 14 正式移除
- **Epsilon GC**（JDK 11）：不做任何回收，仅用于性能测试基准

启用示例：

```bash
# JDK 11 启用 ZGC
java -XX:+UseZGC -Xmx4g YourApplication

# JDK 11 启用 Epsilon（不做 GC，堆满即 OOM）
java -XX:+UseEpsilonGC -Xmx256m YourApplication
```

### 【中等】Java 14 对 switch 有哪些增强？⭐⭐

JDK 14 引入**标准化的 switch 表达式**，支持**箭头语法**、**多值标签**、**yield 返回值**，大幅提升表达力。

**传统 switch 痛点**：

- 容易遗忘 `break` 导致**穿透（fall-through）**。
- 无法直接返回值（需借助中间变量）。
- 重复的 `case` 标签冗长。

**新特性对比**：

```java
// 旧写法
String result;
switch (day) {
    case MONDAY:
    case FRIDAY:
    case SUNDAY:
        result = "休息日";
        break;
    case TUESDAY:
        result = "工作日";
        break;
    default:
        result = "未知";
}
```

```java
// JDK 14+ 新写法（箭头语法 + 多值 + 直接返回）
String result = switch (day) {
    case MONDAY, FRIDAY, SUNDAY -> "休息日";  // 多值，无穿透
    case TUESDAY -> "工作日";
    default -> {
        // 复杂逻辑用 yield 返回
        log("未知日期: " + day);
        yield "未知";
    }
};
```

**核心改进**：

| **特性**     | **传统 switch**           | **JDK 14+ switch 表达式**              |
| ------------ | ------------------------- | -------------------------------------- |
| **穿透**     | 默认穿透，需 `break` 阻止 | 默认**无穿透**，每个分支独立           |
| **返回值**   | 不支持                    | 支持（`yield` 或箭头返回）             |
| **多值标签** | 需多个 `case`             | `case A, B, C ->` 一行搞定             |
| **default**  | 可选                      | 表达式形式**必须**穷尽（强制 default） |

### 【中等】Java 16 的 Record（记录类）有什么用？⭐⭐

`Record` 是 Java 16 引入的**不可变数据载体**，自动生成样板代码，是 Lombok `@Data` 的官方替代品。

**核心特点**：

- **不可变**：所有字段 `final`，无 setter。
- **自动生成**：构造方法、`getter`（无 `get` 前缀）、`equals()`、`hashCode()`、`toString()`。
- **可扩展**：可添加方法、实现接口、添加静态成员。

**定义与使用**：

```java
// 一行定义
public record Point(int x, int y) {}

// 等价的传统 Java 类需 60+ 行
public final class Point {
    private final int x;
    private final int y;
    public Point(int x, int y) { this.x = x; this.y = y; }
    public int x() { return x; }   // 注意：无 get 前缀
    public int y() { return y; }
    // equals, hashCode, toString 省略...
}

// 使用
Point p = new Point(3, 4);
System.out.println(p.x());          // 3
System.out.println(p);              // Point[x=3, y=4]
System.out.println(p.equals(new Point(3, 4)));  // true
```

**紧凑构造器（Compact Constructor）**：用于参数校验

```java
public record Range(int start, int end) {
    public Range {  // 紧凑构造器
        if (start > end) {
            throw new IllegalArgumentException("start 不能大于 end");
        }
    }
}
```

**Record 的限制**：

- **不能继承**其他类（隐式继承 `java.lang.Record`）。
- 字段**不可变**（无法修改）。
- 不能声明 `native` 方法。

**适用场景**：DTO、值对象、配置项、API 响应等"纯数据"场景。不适合需要可变状态或复杂继承的领域模型。

### 【中等】Java 17 的 Sealed Classes（密封类）是什么？⭐⭐⭐

**密封类**通过 `sealed` + `permits` 显式声明允许的子类，**精确控制继承层级**。

**核心价值**：在开放继承（普通类）和禁止继承（`final`）之间提供**第三种选择**——**有界继承**。

**定义示例**：

```java
// 密封类：明确指定允许的子类
public sealed class Shape permits Circle, Square, Triangle {}

// 子类必须是 final、sealed 或 non-sealed 之一
public final class Circle extends Shape { ... }       // 不再可继承
public final class Square extends Shape { ... }       // 不再可继承
public non-sealed class Triangle extends Shape { ... }  // 恢复开放继承
```

**与 Pattern Matching 结合（领域建模利器）**：

```java
public double area(Shape shape) {
    return switch (shape) {  // 编译器检查所有子类，无需 default
        case Circle c -> Math.PI * c.r() * c.r();
        case Square s -> s.side() * s.side();
        case Triangle t -> 0.5 * t.base() * t.height();
    };
}
```

**适用场景**：

- **领域建模**：限定业务概念的取值范围（如订单状态、支付方式）。
- **类型安全的代数数据类型（ADT）**：函数式编程中的和类型。
- **API 设计**：明确告知调用方“我有这几个实现”，配合 switch 穷尽检查。

### 【中等】Java 17 的 Record（记录类）是什么？⭐⭐⭐

**Record（JDK 16 正式版）**是 Java 的**不可变数据载体**，自动生成样板代码，是 Lombok `@Data` 的官方替代品。

```java
// 一行定义
public record Point(int x, int y) {}

// 等价的传统 Java 类需 60+ 行（构造器、getter、equals、hashCode、toString）

// 使用
Point p = new Point(3, 4);
System.out.println(p.x());          // 3（注意：无 get 前缀）
System.out.println(p);              // Point[x=3, y=4]
```

**紧凑构造器**：用于参数校验

```java
public record Range(int start, int end) {
    public Range {  // 紧凑构造器
        if (start > end) throw new IllegalArgumentException("start > end");
    }
}
```

**Record 的限制**：

- **不能继承**其他类（隐式继承 `java.lang.Record`）
- 字段**不可变**（`final`）
- 不能声明 `native` 方法

### 【中等】Java 17 的文本块（Text Blocks）是什么？⭐⭐

**文本块（Text Blocks，JDK 15 正式版）**用 `"""` 定义多行字符串，解决传统字符串拼接的可读性问题。

```java
// JDK 8：冗长的字符串拼接
String json = "{\n" +
    "  \"name\": \"Tom\",\n" +
    "  \"age\": 18\n" +
    "}";

// 文本块：清晰的多行格式
String json = """
    {
      "name": "Tom",
      "age": 18
    }
    """;
```

**特性**：

| 特性                   | 说明                                          |
| :--------------------- | :-------------------------------------------- |
| **自动缩进**           | 以公共缩进为基准，自动去除多余缩进            |
| **换行符**             | 统一为 `\n`（跨平台一致）                     |
| **转义字符**           | 支持 `\s`（保留尾部空格）、`\\`（行尾不换行） |
| **String.formatted()** | JDK 15+ 支持 `"""...""".formatted(args)`      |

```java
// 格式化文本块
String sql = """
    SELECT *
    FROM users
    WHERE age > %d AND city = '%s'
    """.formatted(18, "北京");
```

### 【中等】Java 17 的 instanceof 模式匹配是什么？⭐⭐⭐

**instanceof 模式匹配（JDK 16 正式版）**将类型检查和变量绑定合二为一，消除显式强制转换。

```java
// JDK 8：需要显式转换
if (obj instanceof String) {
    String s = (String) obj;
    System.out.println(s.length());
}

// Java 17：模式匹配，直接绑定变量
if (obj instanceof String s) {
    System.out.println(s.length());  // 无需转换
}

// 支持在条件中组合
if (obj instanceof String s && s.length() > 5) {
    System.out.println(s.toUpperCase());
}
```

**作用域规则**：绑定变量的作用域仅限于模式匹配为 `true` 的分支。

```java
if (!(obj instanceof String s)) {
    return;  // s 不可用
}
// s 在此处可用（因为只有匹配成功才能执行到这里）
System.out.println(s.length());
```

### 【中等】Java 17 的 switch 表达式增强是什么？⭐⭐⭐

**switch 表达式（JDK 14 正式版）**引入了 `->` 箭头语法和 `yield` 返回值，使 switch 可作为表达式使用。

```java
// JDK 8：传统 switch（需要 break，容易遗漏）
int days;
switch (month) {
    case JANUARY: case MARCH: case MAY: days = 31; break;
    case FEBRUARY: days = 28; break;
    default: days = 30;
}

// Java 17：switch 表达式（箭头语法，无需 break）
int days = switch (month) {
    case JANUARY, MARCH, MAY -> 31;
    case FEBRUARY -> 28;
    default -> 30;
};

// 多行代码块用 yield 返回值
String result = switch (code) {
    case 200 -> "OK";
    case 404 -> "Not Found";
    default -> {
        String msg = "Unknown: " + code;
        yield msg;  // 代码块中用 yield 返回值
    }
};
```

**核心优势**：

| 特性          | 传统 switch          | Java 17 switch 表达式  |
| :------------ | :------------------- | :--------------------- |
| **返回值**    | 不支持               | 可直接赋值给变量       |
| **case 穿透** | 需 `break`（易遗漏） | `->` 自动不穿透        |
| **多值合并**  | 每个 case 一行       | `case A, B, C ->`      |
| **穷尽检查**  | 无强制               | 表达式必须穷尽所有分支 |

### 【中等】Java 21 的 switch 模式匹配有什么增强？⭐⭐⭐

**switch 模式匹配（Java 21 正式版）**将 `switch` 从“值匹配”升级为“类型匹配 + 守卫条件 + null 处理”的强大模式匹配工具。

**核心增强**：

```java
// 1. 类型模式 + 守卫条件
Object obj = getShape();
String result = switch (obj) {
    case Circle c when c.radius() > 10 -> "大圆";
    case Circle c                       -> "小圆";
    case Square s                       -> "正方形，边长=" + s.side();
    case null                           -> "null 值";  // 显式处理 null
    default                             -> "其他";
};

// 2. 与密封类结合——编译器穷尽检查
sealed interface Shape permits Circle, Square {}
record Circle(double radius) implements Shape {}
record Square(double side) implements Shape {}

double area(Shape shape) {
    return switch (shape) {  // 无需 default，编译器确保穷尽
        case Circle c -> Math.PI * c.radius() * c.radius();
        case Square s -> s.side() * s.side();
    };
}
```

**与传统 switch 的区别**：

| 特性          | 传统 switch                     | Java 21 switch 模式匹配 |
| :------------ | :------------------------------ | :---------------------- |
| **匹配对象**  | 仅值（`int`、`String`、`enum`） | 任意类型 + 模式         |
| **null 处理** | 抛 NPE                          | `case null` 显式处理    |
| **守卫条件**  | 不支持                          | `when` 子句添加额外条件 |
| **穷尽检查**  | 仅 enum                         | 密封类 + enum 均可      |

### 【中等】Java 21 的记录模式（Record Patterns）是什么？⭐⭐

**记录模式（Record Patterns，Java 21 正式版）**允许在 `instanceof` 和 `switch` 中**解构 Record 的字段**，实现模式组合。

```java
record Point(int x, int y) {}
record Line(Point start, Point end) {}

// 1. instanceof 中解构
Object obj = new Point(3, 4);
if (obj instanceof Point(int x, int y)) {
    System.out.println("x=" + x + ", y=" + y);  // 直接访问解构字段
}

// 2. 嵌套解构
Object obj2 = new Line(new Point(0, 0), new Point(5, 5));
if (obj2 instanceof Line(Point(var x1, var y1), Point(var x2, var y2))) {
    double length = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
}

// 3. switch 中解构
String describe(Object obj) {
    return switch (obj) {
        case Point(int x, int y) when x == 0 && y == 0 -> "原点";
        case Point(int x, int y)                        -> "点(" + x + "," + y + ")";
        case Line(Point s, Point e)                     -> "线段";
        default                                          -> "未知";
    };
}
```

**核心价值**：实现了**代数数据类型的完整模式匹配**，使 Java 具备了类似 Scala/Kotlin 的解构能力。

### 【中等】Java 21 的未命名变量（Unnamed Variables）是什么？⭐

**未命名变量（Unnamed Variables，Java 21 预览）**用 `_` 表示“声明但不使用”的变量，提升代码可读性。

```java
// 1. 忽略不需要的变量
var _ = someExpensiveComputation();  // 只关心副作用，不用返回值

// 2. try-with-resources 中忽略资源
try (var _ = acquireLock()) {
    // 只关心锁的作用域，不使用锁对象
    doWork();
}

// 3. for 循环中忽略循环变量
for (var _ : collection) {
    count++;  // 只关心元素个数
}

// 4. catch 中忽略异常
try {
    riskyOperation();
} catch (Exception _) {  // 不关心异常对象
    log("操作失败");
}

// 5. switch 中忽略模式变量
switch (shape) {
    case Circle _ -> "这是一个圆";  // 不需要访问圆的字段
    case Square _ -> "这是一个正方形";
}
```

**核心价值**：明确表示“这个变量是故意不用的”，避免 IDE 警告，提升代码意图表达。

### 【中等】Java 21 的 Scoped Values 是什么？与 ThreadLocal 有什么区别？⭐⭐

**Scoped Values（Java 21 预览，JEP 446）** 是比 `ThreadLocal` 更安全、更高效的线程上下文传递方案，专为**虚拟线程**设计。

| 维度             | ThreadLocal                       | Scoped Values                  |
| :--------------- | :-------------------------------- | :----------------------------- |
| **可变性**       | 可任意修改（`set`/`remove`）      | **不可变**，作用域内只读       |
| **生命周期**     | 线程生命周期，需手动清理          | 作用域结束自动失效，无泄漏风险 |
| **虚拟线程友好** | 百万虚拟线程时内存开销巨大        | 轻量级，专为虚拟线程优化       |
| **继承性**       | InheritableThreadLocal 有性能问题 | 支持结构化并发中的安全传递     |

```java
// Scoped Values 用法
private static final ScopedValue<String> USER = ScopedValue.newInstance();

// 在作用域内绑定值
ScopedValue.where(USER, "admin").run(() -> {
    processRequest();  // 内部可读取 USER
});

// 在任意深度读取
void processRequest() {
    String user = USER.get();  // "admin"，无需参数传递
}
```

**适用场景**：HTTP 请求上下文、用户身份、分布式追踪 ID 等“请求级”上下文传递，替代 Spring 中常见的 `ThreadLocal` 方案。
