# JavaCore :: Utils — Java 常用工具类示例

> 本模块展示 JDK 中常用工具类的用法：字符串处理、日期时间、数学运算、正则表达式、国际化格式、系统/运行时信息与定时任务。
>
> 每个示例类的 `main` 逻辑抽取为独立的 `demo()` 方法，并在 `src/test` 下配套 JUnit 5 单元测试验证输出。

示例源码路径：`src/main/java/io/github/dunwu/javacore/util/<特性包>/`

---

## 字符串处理（string）

展示 `StringBuffer` / `StringBuilder` 的可变字符串操作。

- `string/StringBufferDemo01`~`StringBufferDemo10` — 字符串追加、插入、删除、替换、反转、查找子串位置、设置长度、字符提取等常用操作。
- `util/ArraysDemo` — `Arrays` 工具类的排序、填充、拷贝、比较、转字符串等。

## 日期时间（date）

展示 `Date`、`Calendar`、`SimpleDateFormat` 等日期时间 API 的用法。

- `date/DateDemo01`~`DateDemo07` — 获取当前时间、日期格式化与解析、`Calendar` 字段读写、日期计算、时间戳转换等。

## 数学运算（math）

展示 `Math`、`BigDecimal`、`BigInteger`、`Random` 与数字格式化。

- `math/MathDemo01` — `Math` 的绝对值、取整、幂、开方、三角函数、最大最小值等。
- `math/BigDecimalDemo01` — `BigDecimal` 高精度十进制运算与舍入模式。
- `math/BigIntegerDemo01` — `BigInteger` 大整数运算。
- `math/RandomDemo01` — `Random` 生成随机数。
- `math/NumberFormatDemo01`、`math/NumberFormatDemo02` — 数字与百分比、货币格式化。

## 正则表达式（regex）

展示 `Pattern` / `Matcher` 的正则匹配、查找、分组与替换。

- `regex/RegexDemo01`~`RegexDemo07` — 匹配校验、查找子串、捕获分组、替换、分割、贪婪与非贪婪、常用元字符等用法。
- `regex/RegexUtil` — 正则校验工具类（手机号、邮箱、身份证等）。

## 国际化格式（locale）

展示 `Locale` 与本地化格式化器。

- `locale/LoaleDemo` — `Locale` 语言/国家/地区的表示与默认区域设置。
- `locale/DateFormatDemo` — 不同 Locale 下的日期格式化。
- `locale/NumberFormatDemo` — 不同 Locale 下的数字与货币格式化。
- `locale/MessageFormatDemo` — 带占位符参数的消息格式化。

## 系统与运行时（System / Runtime）

展示 `System` 与 `Runtime` 提供的系统级能力。

- `util/SystemDemo01`~`SystemDemo04` — 获取环境变量/系统属性、`currentTimeMillis`、`arraycopy` 数组拷贝、`exit` 退出等。
- `util/RuntimeDemo01`~`RuntimeDemo03` — 获取内存信息（总内存/空闲内存）、可用处理器数、执行外部命令。

## 定时任务（task）

展示 `Timer` / `TimerTask` 的简单任务调度。

- `task/TimerTaskDemo` — 定时执行/周期执行任务。

---

## 单元测试

测试位于 `src/test/java/io/github/dunwu/`，通过捕获标准输出对可确定性断言的示例做校验。运行：

```bash
mvn test -pl codes/javacore-utils
```

> 依赖控制台输入、执行外部进程或调度定时任务的示例具有环境/时序不确定性，不纳入自动化断言。所有 `@Test` 方法均带有中文 `@DisplayName` 说明测试意图。
