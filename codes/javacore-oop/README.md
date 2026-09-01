# JavaCore :: OOP — Java 面向对象示例

> 本模块聚焦 Java **面向对象编程（OOP）** 特性：类与对象、构造器、引用传递、包与访问权限、导入机制。
>
> 每个示例类的 `main` 逻辑都抽取为独立的 `demo()` 方法，并在 `src/test` 下配套 JUnit 5 单元测试验证输出。

示例源码路径：`src/main/java/io/github/dunwu/javacore/<特性包>/`

---

## 类与对象（object）

展示类的定义、字段与方法、静态成员、`this` 引用以及对象的创建与引用传递。

- `object/ClassDemo01`~`ClassDemo06` — 类的定义、实例字段与实例方法、静态字段与静态方法、`this` 的用法、访问权限修饰、类成员综合演示。
- `object/ConstructorDemo01`~`ConstructorDemo03` — 无参/有参构造器、构造器重载、构造器中用 `this(...)` 调用其他构造器。
- `object/ObjectRefDemo01`~`ObjectRefDemo06` — 对象作为引用类型在方法间传递时的行为（修改对象状态会影响原对象，重新赋值不影响原引用）。
- `object/Person`、`object/Person2`、`object/Book` — 供上述示例使用的实体类（封装字段与 getter/setter）。

## 包与访问权限（access）

展示 package 声明、访问修饰符（重点是 `protected` 跨包继承）以及类的可见性规则。

- `access/PackageDemo01` — package 声明与包的组织。
- `access/Hello` — 简单类，用于访问权限演示。
- `access/Operate` — 被其他包导入使用的操作类。
- `access/ProtectedDemo01`、`access/ProtectedDemo02` — `protected` 成员在不同包中通过继承访问的规则对比。

## 导入机制（access）

展示 `import` 导入类、导入 jar 包中的类以及静态导入。

- `access/ImportDemo01`、`access/ImportDemo02` — 使用 `import` 导入其他包中的类并调用。
- `access/ImportJarDemo` — 导入并使用第三方 jar 包（Hutool）中的工具类。
- `access/StaticImportDemo` — 使用 `import static` 静态导入类的静态方法/字段，直接以短名调用。

---

## 单元测试

测试位于 `src/test/java/io/github/dunwu/javacore/oop/ObjectOrientedTest.java`，通过捕获标准输出对各示例结果做精确断言。运行：

```bash
mvn test -pl codes/javacore-oop
```

> 所有 `@Test` 方法均带有中文 `@DisplayName` 说明测试意图。
