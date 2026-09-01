# JavaCore :: Effective —《Effective Java（第 2 版）》示例

> 本模块是《Effective Java（第 2 版）》书中各条目（Item）的配套源码，按主题分为若干板块。每个条目对应一个 `itemNN` 目录，展示该条目的推荐做法与反面示例。
>
> 示例源码来自 [Effective Java Examples](https://github.com/marhan/effective-java-examples) by marhan。

示例源码路径：`src/main/java/io/github/dunwu/javacore/effective/chapterNN/itemNN/`

---

## 创建和销毁对象（chapter02）

- `item01` — 考虑用静态工厂方法代替构造器：`Services` / `Service` / `Provider` 演示服务提供框架。
- `item02` — 遇到多个构造器参数时考虑用构建器：`NutritionFacts` 分别用伸缩构造器、JavaBeans、Builder 三种写法对比。
- `item03` — 用私有构造器或枚举强化 Singleton：`Elvis` 展示饿汉字段、静态工厂、枚举、可序列化四种单例写法。
- `item04` — 用私有构造器强化不可实例化的能力：`UtilityClass` 工具类。
- `item05` — 避免创建不必要的对象：`Person`（快/慢版本对比）、`Sum`（自动装箱导致的性能问题，反例）。
- `item06` — 消除过期的对象引用：`Stack` + `EmptyStackException`，`pop` 后未置空引用造成内存泄漏。

## 对于所有对象都通用的方法（chapter03）

- `item08` — 覆盖 equals 时遵守通用约定：`Point`、`ColorPoint`、`CaseInsensitiveString`、`CounterPoint`，以及用 `composition` 组合替代继承的正确写法。
- `item09` — 覆盖 equals 时始终覆盖 hashCode：`PhoneNumber`。
- `item10` — 始终覆盖 toString：`PhoneNumber`。
- `item11` — 谨慎地覆盖 clone：`PhoneNumber`、`Stack` + `EmptyStackException` 的克隆实现。
- `item12` — 考虑实现 Comparable：`PhoneNumber`、`WordList` 的排序对比。

## 类和接口（chapter04）

- `item14` — 使类和成员的可访问性最小化：`Point`、`Time` 的封装。
- `item15` — 使可变性最小化：`Complex` 不可变复数类。
- `item16` — 复合优于继承：`ForwardingSet` + `InstrumentedSet`（正确）对比 `InstrumentedHashSet`（继承的错误）。
- `item17` — 要么为继承设计并提供文档，要么禁止继承：`Super` / `Sub` 演示重写导致的意外行为。
- `item18` — 接口优于抽象类：`AbstractMapEntry`、`IntArrays`。
- `item19` — 只使用接口来定义类型：`PhysicalConstants`（反例：常量接口反模式）。
- `item20` — 类层次优于标签类：`Figure`（hierarchy 层次结构 vs taggedclass 标签类），`sample` 目录演示四种内部类（成员/静态/局部/匿名）。

## 泛型（chapter05）

- `item23` — 请不要在新代码中使用原生态类型：`Raw`。
- `item25` — 列表优于数组：`Function`、`Reduction` 对比数组协变与泛型不变。
- `item26` — 优先考虑泛型：`Stack`（firsttechnqiue 泛型数组 / secondtechnqiue 泛型集合两种改造）。
- `item27` — 优先考虑泛型方法：`Union`、`GenericSingletonFactory`、`GenericStaticFactory`、`UnaryFunction`、`RecursiveTypeBound`。
- `item28` — 利用限定通配符提升 API 灵活性：`Stack`、`Swap`、`Union`、`Function`、`Reduction`、`RecursiveTypeBound`（PECS 原则）。
- `item29` — 优先考虑类型安全的异构容器：`Favorites`、`PrintAnnotation`。

## 枚举和注解（chapter06）

- `item30` — 用 enum 代替 int 常量：`Operation`、`PayrollDay`、`Planet`、`WeightTable`。
- `item31` — 用实例域代替序数：`Ensemble` 枚举。
- `item32` — 用 EnumSet 代替位域：`Text`。
- `item33` — 用 EnumMap 代替序数索引：`Herb`、`Phase`。
- `item34` — 用接口模拟可扩展的枚举：`Operation` / `BasicOperation` / `ExtendedOperation`。
- `item35` — 注解优于命名模式：`Test`、`ExceptionTest` 注解及 `RunTests`、`Sample`、`Sample2` 反射驱动测试。
- `item36` — 坚持使用 Override 注解：`Bigram`（因缺少 @Override 导致的重载陷阱）。

## 方法（chapter07）

- `item39` — 必要时进行保护性拷贝：`Period`（对可变 Date 参数做保护性拷贝）与 `Attack`（攻击可变性，反例）。
- `item41` — 谨慎地使用重载：`CollectionClassifier`、`Overriding`、`SetList` 演示重载按静态类型分派的陷阱。
- `item42` — 谨慎地使用可变参数：`Varargs` 的性能与语义注意点。
- `item49` — 基本类型优于装箱基本类型：`BrokenComparator`（装箱比较缺陷）、`Unbelievable`（自动拆箱 NPE/结果异常）。
- `item53` — 接口优于具体类型：`MakeSet` 通过反射按类名实例化、以 `Set` 接口访问。

## 通用程序设计（chapter08）

- `item46` — for-each 优于传统 for：`DiceRolls`、`NestedIteration` 演示传统循环的隐蔽 bug。
- `item47` — 了解和使用类库：`RandomBug` 演示旧版 `Random.nextInt(n)` 的缺陷。
- `item48` — 如果需要精确答案就不要使用 float/double：`Arithmetic` 用 BigDecimal 精确计算。

## 并发（chapter10）

- `item66` — 同步访问共享的可变数据：`StopThread`（brokenstopthread 反例 / fixedstopthread1 用 synchronized / fixedstopthread2 用 volatile）。
- `item67` — 避免过度同步：`ObservableSet` + `ForwardingSet` + `SetObserver`，`Test1`~`Test3` 演示过度同步导致的死锁/异常。
- `item69` — 优先使用执行器、任务而非线程：`ConcurrentTimer` 并发计时框架、`Intern` 基于 ConcurrentMap 的规范映射。
- `item71` — 谨慎地使用延迟初始化：`Initialization` 演示普通/同步/双重检查/单重检查/静态持有者等延迟初始化方式。
- `item72` — 不要依赖于线程调度器：`SlowCountDownLatch`（忙等待的反面实现，反例）。

## 序列化（chapter11）

- `item74` — 谨慎地实现 Serializable：`Foo` / `AbstractFoo` 演示不实现 Serializable 对子类的影响。
- `item75` — 考虑使用自定义的序列化形式：`StringList` 的默认 vs 自定义序列化。
- `item76` — 保护性地编写 readObject：`Period` + `BogusPeriod`、`MutablePeriod` 演示反序列化攻击与防护。
- `item77` — 对于实例控制，枚举类型优于 readResolve：`Elvis` / `ElvisImpersonator` / `ElvisStealer`，及 `enumSingleton` 的枚举单例。
- `item78` — 考虑用序列化代理代替序列化实例：`Period` + `BogusPeriod`、`MutablePeriod` 的序列化代理模式。
