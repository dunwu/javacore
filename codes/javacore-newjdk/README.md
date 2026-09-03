# JavaCore :: NewJDK — JDK 8~21 新特性示例

> 本模块按 **JDK 版本** 组织，逐个展示 JDK 8 到 JDK 21 引入的重要语言与 API 特性。
>
> 每个示例类的 `main` 只做聚合调用，具体逻辑拆分为若干**具名静态方法**（如 `LambdaBasicDemo` 的 `runnableComparison()`、`singleParameterLambda()`、`sortAndForEach()`），一个方法对应一个独立用法；并在 `src/test` 下配套 JUnit 5 单元测试（`XxxDemoTest`）逐个验证输出。

示例源码路径：`src/main/java/io/github/dunwu/javacore/<jdkNN>/<特性包>/`

---

## JDK 8

Lambda、函数式接口、Stream、Optional、新日期时间 API 等里程碑特性。

- `jdk8/lambda/LambdaBasicDemo` — Lambda 表达式基本语法；`EffectivelyFinalDemo` — Lambda 捕获「事实上 final」的变量。
- `jdk8/funcinterface/FunctionalInterfaceDemo`、`BuiltinFunctionalInterfaceDemo` — 自定义函数式接口与内置 `Function`/`Predicate`/`Consumer`/`Supplier`。
- `jdk8/methodref/MethodReferenceDemo` — 方法引用（静态/实例/构造器/数组）四种形式。
- `jdk8/stream/StreamCreateDemo`、`StreamOperationDemo`、`StreamCollectDemo`、`StreamReduceDemo`、`StreamGroupingDemo`、`StreamParallelDemo` — 流的创建、中间/终止操作、收集、归约、分组与并行流。
- `jdk8/optional/OptionalBasicDemo`、`OptionalChainDemo` — Optional 的基本用法与链式调用避免 NPE。
- `jdk8/time/LocalDateDemo`、`InstantDurationDemo`、`DateTimeFormatterDemo`、`TemporalAdjusterDemo` — 新日期时间 API（LocalDate/Instant/Duration/格式化/时间调整器）。
- `jdk8/iface/DefaultMethodDemo` — 接口默认方法。
- `jdk8/annotation/RepeatableAnnotationDemo` — 可重复注解。
- `jdk8/concurrent/CompletableFutureDemo`、`LongAdderDemo`、`StampedLockDemo` — 异步编排、高并发计数、读写乐观锁。
- `jdk8/util/Base64Demo`、`StringJoinerDemo`、`MapNewApiDemo`、`ArraysCollectionDemo` — Base64、字符串拼接器、Map 新方法、Arrays/集合增强。

## JDK 9

- `jdk9/collection/CollectionFactoryDemo` — `List.of` / `Set.of` / `Map.of` 不可变集合工厂。
- `jdk9/iface/InterfacePrivateMethodDemo` — 接口私有方法。
- `jdk9/optional/OptionalEnhanceDemo` — Optional 新增 `ifPresentOrElse`、`or`、`stream`。
- `jdk9/stream/StreamEnhanceDemo` — Stream 新增 `takeWhile`、`dropWhile`、`ofNullable`、`iterate` 重载。
- `jdk9/trywith/ImprovedTryWithResourcesDemo` — 改进的 try-with-resources（可直接用已声明的 final 资源）。
- `jdk9/process/ProcessHandleDemo` — 进程句柄 API，查询/管理进程。
- `jdk9/module/ModuleApiDemo` — 模块系统（JPMS）相关 API 演示。
- `jdk9/concurrent/CompletableFutureEnhanceDemo` — CompletableFuture 增强（超时、延迟执行）。

## JDK 10

- `jdk10/var/VarDemo` — `var` 局部变量类型推断；`VarLimitDemo` — `var` 的使用限制（反例：不能用于字段/返回值/初始化 null 等）。
- `jdk10/collection/UnmodifiableCollectionDemo` — `List.copyOf` 等创建不可变集合副本。

## JDK 11（LTS）

- `jdk11/string/StringEnhanceDemo` — String 新增 `isBlank`、`strip`、`lines`、`repeat`。
- `jdk11/lambda/LambdaVarDemo` — Lambda 参数使用 `var`。
- `jdk11/optional/OptionalEnhanceDemo` — Optional 新增 `isEmpty`。
- `jdk11/file/FileReadWriteDemo` — `Files.readString` / `writeString` 一行读写文本文件。
- `jdk11/http/HttpClientDemo` — 标准化 `HttpClient`，支持同步/异步请求与 HTTP/2。

## JDK 14

- `jdk14/switchstmt/SwitchExpressionDemo` — switch 表达式（箭头语法、`yield` 返回值）。
- `jdk14/npe/NullPointerExceptionDemo` — 更精确的 NullPointerException 提示信息。

## JDK 15

- `jdk15/textblock/TextBlockDemo` — 文本块（三引号多行字符串）。

## JDK 16

- `jdk16/record/RecordDemo`、`RecordAdvancedDemo` — record 记录类基本用法与进阶（紧凑构造器、静态方法、实现接口）。
- `jdk16/pattern/InstanceofPatternDemo` — instanceof 模式匹配。
- `jdk16/stream/StreamToListDemo` — `Stream.toList()` 直接收集为不可变列表。

## JDK 17（LTS）

- `jdk17/sealed/SealedClassDemo`、`SealedShapeDemo` — sealed 密封类/接口，限定可继承的类型。

## JDK 21（LTS）

- `jdk21/collection/SequencedCollectionDemo` — 有序集合接口（SequencedCollection，统一首尾元素与反转视图）。
- `jdk21/record/RecordPatternDemo` — record 模式匹配（解构）。
- `jdk21/switchstmt/SwitchPatternDemo` — switch 模式匹配与 `when` 守卫。
- `jdk21/thread/VirtualThreadDemo` — 虚拟线程（Project Loom）轻量级并发。

---

## 单元测试

测试位于 `src/test/java/io/github/dunwu/javacore/`，与主代码一一对应（`XxxDemoTest`），通过捕获标准输出对示例结果做精确断言。运行：

```bash
mvn test -pl codes/javacore-newjdk
```

> 反例（如 `VarLimitDemo` 展示 `var` 的限制）以注释保留不可编译/受限写法，或以断言验证其预期行为；依赖网络的 `HttpClientDemo` 等以可控方式验证。所有 `@Test` 方法均带有中文 `@DisplayName` 说明测试意图。
