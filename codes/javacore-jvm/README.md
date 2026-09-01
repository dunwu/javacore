# JavaCore :: JVM — Java 虚拟机示例

> 本模块展示 JVM 核心机制的可运行示例：类加载、内存区域溢出（OOM）、垃圾回收（GC）。
>
> ⚠️ 注意：`memory` 与 `error` 包中的示例是**故意触发内存溢出的反例**，需在运行时配置特定 JVM 参数（如 `-Xmx`、`-XX:MaxDirectMemorySize` 等）才能复现对应错误，请勿修改其触发行为。

示例源码路径：`src/main/java/io/github/dunwu/javacore/jvm/<特性包>/`

---

## 类加载机制（classloader）

展示类的加载时机、双亲委派、主动/被动引用与解析规则。

- `classloader/类加载过程` — 演示类加载「加载 → 验证 → 准备 → 解析 → 初始化」的过程。
- `classloader/ClassLoaderDemo` — 打印类加载器层次结构（Bootstrap / Ext / App），验证双亲委派。
- `classloader/PassiveRefDemo01`~`PassiveRefDemo03` — 被动引用不触发类初始化的场景（通过子类引用父类静态字段、定义类数组、引用编译期常量）。
- `classloader/SuperClass`、`classloader/SubClass`、`classloader/ConstClass` — 配合被动引用示例的父类/子类/常量类。
- `classloader/Singleton` — 类初始化顺序对单例的影响（静态字段初始化次序陷阱）。
- `classloader/ParentAndSon` — 父子类静态代码块/构造器的执行顺序。
- `classloader/FieldResolution` — 字段解析（同名子接口/父类字段的解析优先级）。
- `classloader/IllegalForwardDemo`（反例） — 非法向前引用静态字段导致的编译错误。
- `classloader/DeadLoopClassDemo` — 类初始化死循环（多线程下 static 块死锁）演示。
- `classloader/exception/ClassNotFoundExceptionDemo`、`ClassCastExceptionDemo`、`UnsatisfiedLinkErrorDemo`（反例） — 类加载相关异常的触发场景。

## 内存区域与溢出（memory）

展示 JVM 各运行时内存区域以及对应的溢出/内存泄漏。**均需配置特定 JVM 参数运行。**

- `memory/JvmXmxArgs` — 打印 JVM 启动参数与堆内存配置（-Xms/-Xmx 等）。
- `memory/HeapOutOfMemoryOOM` — 堆内存溢出（`java heap space`），需限制 `-Xmx`。
- `memory/HeapMemoryLeakOOM`、`HeapMemoryLeakOOM2` — 内存泄漏（对象被静态集合长期持有无法回收）。
- `memory/GcOverheadLimitExceededOOM` — GC 开销超限（`GC overhead limit exceeded`）。
- `memory/StackOverflowErrorDemo`、`StackOverflowErrorDemo2`、`StackOverflowErrorDemo3` — 栈溢出（递归过深），需限制 `-Xss`。
- `memory/StackOutOfMemoryError` — 栈内存溢出。
- `memory/MethodAreaOOM`、`memory/PermGenSpaceOOM` — 方法区/永久代溢出（动态生成大量类，如 CGLIB）。
- `memory/ConstantPoolOutOfMemoryDemo` — 运行时常量池溢出（`String.intern` 大量驻留）。
- `memory/DirectOutOfMemoryDemo` — 直接内存（堆外）溢出，需配置 `-XX:MaxDirectMemorySize`。
- `memory/UnableCreateNativeThreadOOM` — 无法创建本地线程（`unable to create new native thread`）。

## 内存溢出补充（error）

`error` 包是另一组聚焦特定 OOM 的示例：

- `error/VMStackOOM` — 虚拟机栈和本地方法栈溢出（需 `-Xss`）。
- `error/RuntimeConstantPoolOOM_1`、`RuntimeConstantPoolOOM_2` — 运行时常量池溢出（不同 JDK 版本行为差异）。
- `error/DirectMemoryOOM` — 直接内存溢出。

## 垃圾回收（gc）

展示 GC 判定、对象晋升、内存分配担保等回收相关行为。**多数需配合特定 GC 参数与 `-verbose:gc` 观察输出。**

- `gc/ReferenceCountingGC` — 引用计数法的循环引用问题（说明 JVM 不用引用计数，而用可达性分析）。
- `gc/FinalizeEscapeGC` — 对象在 `finalize()` 中「自救」重新关联引用从而逃脱 GC（只会生效一次）。
- `gc/MinorGCDemo` — 触发 Minor GC（新生代回收）。
- `gc/PretenureSizeThresholdDemo` — 大对象直接进入老年代（`-XX:PretenureSizeThreshold`）。
- `gc/TenuringThresholdDemo`、`TenuringThresholdDemo2` — 对象年龄达到阈值（`-XX:MaxTenuringThreshold`）后晋升老年代。
- `gc/HandlePromotionFailureDemo` — 空间分配担保（老年代剩余空间不足时的处理）。
- `gc/ParNewGCDemo` — ParNew + 老年代收集器的组合演示。
