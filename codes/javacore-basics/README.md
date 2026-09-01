# JavaCore :: Basics — Java 基础特性示例

> 本模块汇集 Java 语言基础特性的可运行示例。每个示例类的 `main` 逻辑都抽取为独立的 `demo()` 方法，并在 `src/test` 下配套 JUnit 5 单元测试验证输出，既可直接运行，也可作为回归用例。
>
> 说明：部分示例是**故意演示错误用法或触发异常的反例**（下文标注「反例」），用于对比正确写法，请勿修改其行为。

示例源码路径：`src/main/java/io/github/dunwu/javacore/<特性包>/`

---

## 变量与数据类型（variable / datatype）

展示 Java 基本类型、包装类型的取值范围、判等陷阱与数值计算注意事项。

- `variable/VariableDemo` — 各类局部变量的声明与初始化。
- `datatype/值类型使用示例` — 通过反射依次演示整型溢出、字符、浮点、布尔等值类型用法。
- `datatype/包装类型使用示例` — 打印各包装类的位数、最小值、最大值。
- `datatype/包装类装箱拆箱` — 自动装箱/拆箱与手动装箱/拆箱的写法对比。
- `datatype/Integer判等` — Integer 缓存池（-128~127）对 `==` 判等结果的影响。
- `datatype/String判等` — 字符串常量池、`intern()` 与 `==`/`equals` 的差异。
- `datatype/String拼接` — 字符串常量拼接的编译期优化。
- `datatype/StringIntern性能测试` — 1000 万次 `intern()` 的性能基准（不纳入单测）。
- `datatype/枚举判等` — 枚举常量字段为同一实例，`==` 判等为 true。
- `datatype/BigDecimal判等` — `equals` 比较精度、`compareTo` 只比较数值大小的区别。
- `datatype/浮点数舍入` — double/float 舍入结果不一致的坑与 BigDecimal 正确舍入。
- `datatype/数值计算示例` — 浮点数精度丢失问题与 BigDecimal 字符串构造的正确用法。
- `datatype/数值溢出` — long 溢出为最小值，及 `Math.addExact`、`BigInteger` 的应对方案。
- `datatype/equals和CompareTo` — `equals` 与 `compareTo` 不一致导致 `indexOf`/`binarySearch` 结果矛盾。
- `datatype/Lombok生成Equals的问题`（反例对比） — `@Data` 生成 equals 时字段排除与继承 `callSuper` 的陷阱。
- `datatype/自定义equals` — 自定义 equals 的错误写法（未判空/判类型）与正确写法。

## 运算符（operator）

展示 Java 各类运算符的用法与运算结果。

- `operator/MathOperatorDemo` — 算术运算符 `+ - * / % ++ --`。
- `operator/RelationOperatorDemo` — 关系运算符 `== != > < >= <=`。
- `operator/LogicalOperatorDemo` — 逻辑运算符 `&& || !`。
- `operator/BitsOperatorDemo` — 位运算符 `& | ^ ~ << >> >>>`。
- `operator/AssignmentOperatorDemo` — 赋值运算符 `= += -= *=` 等。
- `operator/ConditionalOperatorDemo` — 三元条件运算符 `? :`。
- `operator/InstanceofOperatorDemo` — `instanceof` 类型判断。

## 控制语句（control）

展示分支与循环控制语句的用法。

- `control/IfDemo`、`IfElseDemo`、`IfElseifElseDemo`、`IfNestedDemo` — if 单分支、双分支、多分支与嵌套。
- `control/SwitchDemo01`~`SwitchDemo03` — switch 基本分支、范围判断与 default 默认分支。
- `control/ForDemo`、`WhileDemo`、`DoWhileDemo` — for、while、do-while 循环累加。
- `control/ForeachDemo` — 增强 for 遍历数组。
- `control/ForNestedDemo` — 嵌套循环输出九九乘法表。
- `control/BreakDemo`、`ContinueDemo`、`ReturnDemo` — break 跳出、continue 跳过、return 结束方法。

## 数组（array）

展示数组的声明、初始化、遍历、引用传递与常用操作。

- `array/ArrayDemo`~`ArrayDemo4` — 数组声明、默认值、长度、遍历输出。
- `array/MultiArrayDemo` — 多维数组的初始化与输出。
- `array/GenericArrayDemo` — 泛型数组的创建与默认值。
- `array/ArrayRefDemo`、`ArrayRefDemo2` — 数组引用传递，方法内修改影响原数组。
- `array/ArraysDemo` — `Arrays` 工具类的填充、排序、拷贝等操作。

## 方法（method）

展示方法定义、重载、重写、参数传递与特殊方法。

- `method/MethodDemo01`~`MethodDemo06` — 方法的基本定义、调用与计算示例。
- `method/MethodOverloadDemo` — 方法重载（同名不同参）。
- `method/MethodOverrideDemo` — 方法重写（子类覆盖父类）。
- `method/AbstractMethodDemo` — 抽象方法的定义与实现。
- `method/FinalMethodDemo` — final 方法不可被重写。
- `method/DefaultMethodDemo` — 接口默认方法。
- `method/ConstructorMethodDemo` — 构造方法初始化对象。
- `method/VarargsDemo` — 可变参数方法。
- `method/RecursionMethodDemo` — 递归（斐波那契数列）。
- `method/MethodParamDemo`、`MethodParamDemo2` — 基本类型（值传递）与引用类型参数的传递差异。
- `method/MainMethodDemo` — main 方法接收命令行参数。
- `method/MethodDemo04` — 无 main 的方法示例（供其他示例引用）。

## 面向对象基础（oop）

展示类、对象、继承、封装等面向对象基础概念。

- `oop/Person`、`oop/Programmer` — 类的定义、字段与方法。
- `oop/Test` — 继承与方法重写（动物/猫/狗叫声示例）。
- `oop/PackageDemo`、`PackageDemo2` — 包的定义与使用。

## 枚举（enumeration）

展示枚举的定义、方法与在集合、状态机等场景的用法。

- `enumeration/ColorEn`、`ErrorCodeEn` — 基本枚举与带字段/构造器的枚举。
- `enumeration/EnumMethodDemo` — 枚举的 `values()`、`valueOf()`、`ordinal()` 等方法。
- `enumeration/EnumInClassDemo`、`EnumInInterfaceDemo` — 枚举作为类/接口的成员。
- `enumeration/EnumSetDemo`、`EnumMapDemo` — 枚举专用集合 EnumSet / EnumMap 的用法。
- `enumeration/AddMethod2EnumDemo` — 为枚举添加抽象方法与常量特定实现。
- `enumeration/SingleEnumDemo` — 用枚举实现单例。
- `enumeration/StateMachineDemo` — 用枚举实现简单状态机（红绿灯）。
- `enumeration/ErrorCodeEnumDemo`、`PayrollDay`、`App` — 错误码枚举、薪资计算枚举等综合示例。

## 异常（exception）

展示异常处理机制的正确用法与常见反例。

- `exception/TryCatchDemo`、`TryCatchFinallyDemo` — try-catch、try-catch-finally 基本用法。
- `exception/ThrowDemo`、`ThrowsDemo` — throw 抛出异常与 throws 声明异常。
- `exception/ExceptionChainDemo` — 异常链（cause 传递）。
- `exception/ExceptionDemo` — 异常处理综合演示（空 main，仅作说明）。
- `exception/ExceptionOverrideDemo`、`FinallyOverrideExceptionDemo` — 重写方法异常约束、finally 覆盖返回值/异常。
- `exception/AssertDemo` — assert 断言（测试环境默认开启 -ea，会抛 AssertionError）。
- `exception/MyExceptionDemo`（反例） — 抛出未被捕获的自定义异常。
- `exception/RuntimeExceptionDemo`（反例） — 除零触发运行时异常。

## 泛型（generics）

展示泛型类、泛型接口、泛型方法、通配符与类型擦除。

- `generics/GenericsClassDemo01`~`03` — 泛型类的定义与使用。
- `generics/GenericsClassDemo04`（编译错误演示） — 泛型的错误用法（注释保留）。
- `generics/GenericsInterfaceDemo01`、`02` — 泛型接口的实现。
- `generics/GenericsMethodDemo01`、`GenericVarargsMethodDemo` — 泛型方法与泛型可变参数。
- `generics/GenericArrayDemo` — 泛型数组的限制与实现。
- `generics/GenericsExtendsDemo01`、`02`、`GenericsUpperBoundedWildcardDemo` — 上界通配符 `? extends`。
- `generics/GenericsSuperDemo01`、`GenericsLowerBoundedWildcardDemo` — 下界通配符 `? super`。
- `generics/GenericsUnboundedWildcardDemo`、`GenericsWildcardDemo` — 无界通配符 `?`。
- `generics/GenericsErasureTypeDemo` — 类型擦除现象。
- `generics/GenericsErasureTypeDemo02`（编译错误演示） — 擦除导致的桥方法/冲突问题。
- `generics/NoGenericsDemo`（反例） — 不使用泛型时运行抛 ClassCastException。
- `generics/NoGenericsDemo02`、`New`、`Content`、`Info`、`MyMap` — 泛型辅助/对比示例。

## 注解（annotation）

展示元注解、内置注解与自定义注解的用法。

- `annotation/OverrideAnnotationDemo` — `@Override` 编译期检查重写。
- `annotation/DeprecatedAnnotationDemo` — `@Deprecated` 标记过时 API。
- `annotation/SuppressWarningsAnnotationDemo` — `@SuppressWarnings` 抑制警告。
- `annotation/SafeVarargsAnnotationDemo` — `@SafeVarargs` 抑制堆污染警告。
- `annotation/FunctionalInterfaceAnnotationDemo` — `@FunctionalInterface` 校验函数式接口。
- `annotation/InternalAnnotationDemo` — 元注解（`@Retention`、`@Target` 等）用法。
- `annotation/custom/NotNull`、`NotNullDemo`、`NotNullUtil` — 自定义非空校验注解及处理器。
- `annotation/custom/RegexValid`、`RegexValidDemo`、`RegexValidUtil` — 自定义正则校验注解及处理器。

## 反射（reflect）

展示 Class 对象、构造器/字段/方法的反射调用、动态代理与性能对比。

- `reflect/ReflectDemo`、`ReflectClassDemo01`~`04` — 获取 Class 对象、类名、修饰符、父类接口等信息。
- `reflect/ReflectMethodConstructorDemo`、`NewInstanceDemo` — 反射创建对象、调用构造器。
- `reflect/ReflectFieldDemo` — 反射读写字段。
- `reflect/ReflectMethodDemo` — 反射调用方法。
- `reflect/ReflectArrayDemo` — 反射操作数组。
- `reflect/ReflectTypeDemo`、`InstanceofDemo` — 反射获取类型信息与 instanceof 判断。
- `reflect/InvocationHandlerDemo`、`reflect/proxy/*`（Amazon、Consumer、Purchaser、App） — 基于 `InvocationHandler` 的动态代理（代购场景）。
- `reflect/MethodDemo01`、`MethodDemo02` — 反射调用时的异常堆栈观察（输出到 stderr，不纳入单测）。
- `reflect/MethodPerformDemo01`~`04` — 反射 vs 直接调用的性能基准（20 亿次循环，不纳入单测）。

## 序列化（serial / serialize）

展示对象序列化、反序列化及各种特殊场景。示例使用相对路径临时文件，测试后自动清理。

- `serial/SerializeDemo`、`serial/Employee` — 序列化/反序列化基本流程，transient 字段不被序列化。
- `serialize/SerializeDemo01` — 实现 `Serializable` 的完整序列化。
- `serialize/SerializeDemo02` — `transient` 修饰字段反序列化后为 null。
- `serialize/SerializeDemo03` — 自定义 `writeObject`/`readObject` 使 transient 字段仍可序列化。
- `serialize/SerializeDemo04`（反例） — 反序列化破坏单例（输出 false）。
- `serialize/SerializeDemo05` — `readResolve` 保护单例（输出 true）。
- `serialize/ExternalizeDemo01`、`02` — `Externalizable` 接口自定义外部化序列化。
- `serialize/UnSerializeDemo`（反例） — 未实现 Serializable 抛 NotSerializableException。

## 加密与安全（crypto）

展示常见对称/非对称加密、摘要与签名算法的用法。

- `crypto/Base64Demo` — Base64 编解码。
- `crypto/MessageDigestDemo` — MD5/SHA 消息摘要。
- `crypto/HmacMessageDigest` — HMAC 消息认证码。
- `crypto/DESCoder`、`DESedeCoder` — DES / 3DES 对称加解密。
- `crypto/AesUtil` — AES 对称加解密工具。
- `crypto/RsaUtil` — RSA 非对称加解密与签名（BouncyCastle）。
- `crypto/DsaUtil` — DSA 数字签名。
- `crypto/PBECoder` — 基于口令的加密（PBE）。

## 国际化（i18n）

展示 Locale、资源束与格式化的国际化用法。

- `i18n/ResourceBundleDemo` — 通过 `ResourceBundle` 加载多语言资源（配合 `resources/locales/*.properties`）。
- `i18n/DateFormatDemo` — 日期时间的本地化格式化。
- `i18n/NumberFormatDemo` — 数字与货币的本地化格式化。
- `i18n/MessageFormatDemo` — 带占位符的消息格式化。

## SPI 服务发现（spi）

展示 Java SPI（Service Provider Interface）机制的用法。

- `spi/DataStorage` — 服务接口定义。
- `spi/MysqlStorage`、`spi/RedisStorage` — 服务的具体实现（提供者）。
- `spi/SpiDemo` — 通过 `ServiceLoader` 加载并遍历所有实现（配合 `resources/META-INF/services/`）。

## 常用工具（util / collection / bean）

展示通用工具类与简单容器的用法（由原 javacore-advanced 模块并入）。

- `util/TupleUtil` 与 `util/tuple/TwoTuple`~`FiveTuple` — 元组工具，一次返回多个值。
- `util/ParamFormatUtil` — 基于 Hutool 的参数格式化工具。
- `collection/CollectionDemo`、`Countries` — 集合遍历与享元式数据填充示例。
- `collection/TreeMapDemo`、`VectorDemo` — TreeMap 排序、Vector 用法。
- `bean/Query` — 简单查询参数 Bean。

---

## 单元测试

测试位于 `src/test/java/io/github/dunwu/javacore/`，按特性包组织，通过捕获标准输出对示例结果做精确断言。运行：

```bash
mvn test -pl codes/javacore-basics
```

> 反例（运行即抛异常）使用 `assertThatThrownBy` 验证其确实抛出预期异常；性能基准类示例不纳入自动化测试。所有 `@Test` 方法均带有中文 `@DisplayName` 说明测试意图。
