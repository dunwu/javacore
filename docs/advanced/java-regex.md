# Java 正则从入门到精通

> **📦 本文以及示例源码已归档在 [javacore](https://github.com/dunwu/javacore/)**
>
> 关键词：Pattern、Matcher、捕获与非捕获、反向引用、零宽断言、贪婪与懒惰、元字符、DFA、NFA

<!-- TOC depthFrom:2 depthTo:3 -->

- [1. 正则简介](#1-正则简介)
  - [1.1. 正则表达式是什么](#11-正则表达式是什么)
  - [1.2. 如何学习正则](#12-如何学习正则)
- [2. 正则工具类](#2-正则工具类)
  - [2.1. Pattern 类](#21-pattern-类)
  - [2.2. Matcher 类](#22-matcher-类)
- [3. 元字符](#3-元字符)
  - [3.1. 基本元字符](#31-基本元字符)
  - [3.2. 等价字符](#32-等价字符)
- [4. 分组构造](#4-分组构造)
  - [4.1. 捕获与非捕获](#41-捕获与非捕获)
  - [4.2. 反向引用](#42-反向引用)
  - [4.3. 非捕获组](#43-非捕获组)
  - [4.4. 零宽断言](#44-零宽断言)
- [5. 贪婪与懒惰](#5-贪婪与懒惰)
- [6. 正则附录](#6-正则附录)
  - [6.1. 匹配正则字符串的方法](#61-匹配正则字符串的方法)
  - [6.2. 速查元字符字典](#62-速查元字符字典)
- [7. 正则实战](#7-正则实战)
  - [7.1. 最实用的正则](#71-最实用的正则)
  - [7.2. 特定字符](#72-特定字符)
  - [7.3. 特定数字](#73-特定数字)
- [8. 正则表达式的性能](#8-正则表达式的性能)
  - [8.1. NFA 自动机的回溯](#81-nfa-自动机的回溯)
  - [8.2. 如何避免回溯](#82-如何避免回溯)
  - [8.3. 正则表达式的优化](#83-正则表达式的优化)
- [9. 参考资料](#9-参考资料)

<!-- /TOC -->

## 1. 正则简介

### 1.1. 正则表达式是什么

正则表达式（Regular Expression）是一个用正则符号写出的公式，程序对这个公式进行语法分析，建立一个语法分析树，再根据这个分析树结合正则表达式的引擎生成执行程序（这个执行程序我们把它称作状态机，也叫状态自动机），用于字符匹配。

### 1.2. 如何学习正则

正则表达式是一个强大的文本匹配工具，但是它的规则很复杂，理解起来较为困难，容易让人望而生畏。

刚接触正则时，我看了一堆正则的语义说明，但是仍然不明所以。后来，我多接触一些正则的应用实例，渐渐有了感觉，再结合语义说明，终有领悟。我觉得正则表达式和武侠修练武功差不多，应该先练招式，再练心法。如果一开始就直接看正则的规则，保证你会懵逼。当你熟悉基本招式（正则基本使用案例）后，也该修炼修炼心法（正则语法）了。真正的高手不能只靠死记硬背那么几招把式。就像张三丰教张无忌太极拳一样，领悟心法，融会贯通，少侠你就可以无招胜有招，成为传说中的绝世高手。

**以上闲话可归纳为一句：学习正则应该从实例去理解规则。**

## 2. 正则工具类

JDK 中的 `java.util.regex` 包提供了对正则表达式的支持。

`java.util.regex` 有三个核心类：

- **Pattern 类：**`Pattern` 是一个正则表达式的编译表示。
- **Matcher 类：**`Matcher` 是对输入字符串进行解释和匹配操作的引擎。
- **PatternSyntaxException：**`PatternSyntaxException` 是一个非强制异常类，它表示一个正则表达式模式中的语法错误。

**注：**需要格外注意一点，在 Java 中使用反斜杠"\\"时必须写成 `"\\"`。所以本文的代码出现形如 `String regex = "\\$\\{.*?\\}"` 其实就是 `\$\{.\*?\}`。

### 2.1. Pattern 类

`Pattern`类没有公共构造方法。要创建一个`Pattern`对象，你必须首先调用其**静态方法**`compile`，加载正则规则字符串，然后返回一个 Pattern 对象。

与`Pattern`类一样，`Matcher`类也没有公共构造方法。你需要调用`Pattern`对象的`matcher`方法来获得一个`Matcher`对象。

【示例】Pattern 和 Matcher 的初始化

```java
Pattern p = Pattern.compile(regex);
Matcher m = p.matcher(content);
```

### 2.2. Matcher 类

`Matcher` 类可以说是 `java.util.regex` 中的核心类，它有三类功能：校验、查找、替换。

#### 校验

为了校验文本是否与正则规则匹配，Matcher 提供了以下几个返回值为 `boolean` 的方法。

| **序号** | **方法及说明**                                                                                                    |
| -------- | ----------------------------------------------------------------------------------------------------------------- |
| 1        | **public boolean lookingAt() ** 尝试将从区域开头开始的输入序列与该模式匹配。                                      |
| 2        | **public boolean find() **尝试查找与该模式匹配的输入序列的下一个子序列。                                          |
| 3        | **public boolean find(int start）**重置此匹配器，然后尝试查找匹配该模式、从指定索引开始的输入序列的下一个子序列。 |
| 4        | **public boolean matches() **尝试将整个区域与模式匹配。                                                           |

如果你傻傻分不清上面的查找方法有什么区别，那么下面一个例子就可以让你秒懂。

【示例】lookingAt、find、matches

```java
public static void main(String[] args) {
	checkLookingAt("hello", "helloworld");
	checkLookingAt("world", "helloworld");

	checkFind("hello", "helloworld");
	checkFind("world", "helloworld");

	checkMatches("hello", "helloworld");
	checkMatches("world", "helloworld");
	checkMatches("helloworld", "helloworld");
}

private static void checkLookingAt(String regex, String content) {
	Pattern p = Pattern.compile(regex);
	Matcher m = p.matcher(content);
	if (m.lookingAt()) {
		System.out.println(content + "\tlookingAt： " + regex);
	} else {
		System.out.println(content + "\tnot lookingAt： " + regex);
	}
}

private static void checkFind(String regex, String content) {
	Pattern p = Pattern.compile(regex);
	Matcher m = p.matcher(content);
	if (m.find()) {
		System.out.println(content + "\tfind： " + regex);
	} else {
		System.out.println(content + "\tnot find： " + regex);
	}
}

private static void checkMatches(String regex, String content) {
	Pattern p = Pattern.compile(regex);
	Matcher m = p.matcher(content);
	if (m.matches()) {
		System.out.println(content + "\tmatches： " + regex);
	} else {
		System.out.println(content + "\tnot matches： " + regex);
	}
}
```

输出：

```
helloworld	lookingAt： hello
helloworld	not lookingAt： world
helloworld	find： hello
helloworld	find： world
helloworld	not matches： hello
helloworld	not matches： world
helloworld	matches： helloworld
```

**说明**

`regex = "world"` 表示的正则规则是以 world 开头的字符串，`regex = "hello"` 和 `regex = "helloworld"` 也是同理。

- `lookingAt`方法从头部开始，检查 content 字符串是否有子字符串于正则规则匹配。
- `find`方法检查 content 字符串是否有子字符串于正则规则匹配，不管字符串所在位置。
- `matches`方法检查 content 字符串整体是否与正则规则匹配。

#### 查找

为了查找文本匹配正则规则的位置，`Matcher`提供了以下方法：

| **序号** | **方法及说明**                                                                                      |
| -------- | --------------------------------------------------------------------------------------------------- |
| 1        | **public int start() **返回以前匹配的初始索引。                                                     |
| 2        | **public int start(int group)** 返回在以前的匹配操作期间，由给定组所捕获的子序列的初始索引          |
| 3        | **public int end()**返回最后匹配字符之后的偏移量。                                                  |
| 4        | **public int end(int group)**返回在以前的匹配操作期间，由给定组所捕获子序列的最后字符之后的偏移量。 |
| 5        | **public String group()**返回前一个符合匹配条件的子序列。                                           |
| 6        | **public String group(int group)**返回指定的符合匹配条件的子序列。                                  |

【示例】使用 start()、end()、group() 查找所有匹配正则条件的子序列

```java
public static void main(String[] args) {
	final String regex = "world";
	final String content = "helloworld helloworld";
	Pattern p = Pattern.compile(regex);
	Matcher m = p.matcher(content);
	System.out.println("content: " + content);

	int i = 0;
	while (m.find()) {
		i++;
		System.out.println("[" + i + "th] found");
		System.out.print("start: " + m.start() + ", ");
		System.out.print("end: " + m.end() + ", ");
		System.out.print("group: " + m.group() + "\n");
	}
}
```

**输出**

```
content: helloworld helloworld
[1th] found
start: 5, end: 10, group: world
[2th] found
start: 16, end: 21, group: world
```

**说明**

例子很直白，不言自明了吧。

#### 替换

替换方法是替换输入字符串里文本的方法：

| **序号** | **方法及说明**                                                                                                                                                                    |
| -------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1        | **public Matcher appendReplacement(StringBuffer sb, String replacement)**实现非终端添加和替换步骤。                                                                               |
| 2        | **public StringBuffer appendTail(StringBuffer sb)**实现终端添加和替换步骤。                                                                                                       |
| 3        | **public String replaceAll(String replacement) ** 替换模式与给定替换字符串相匹配的输入序列的每个子序列。                                                                          |
| 4        | **public String replaceFirst(String replacement)** 替换模式与给定替换字符串匹配的输入序列的第一个子序列。                                                                         |
| 5        | **public static String quoteReplacement(String s)**返回指定字符串的字面替换字符串。这个方法返回一个字符串，就像传递给 Matcher 类的 appendReplacement 方法一个字面字符串一样工作。 |

【示例】replaceFirst 和 replaceAll

```java
public static void main(String[] args) {
	String regex = "can";
	String replace = "can not";
	String content = "I can because I think I can.";

	Pattern p = Pattern.compile(regex);
	Matcher m = p.matcher(content);

	System.out.println("content: " + content);
	System.out.println("replaceFirst: " + m.replaceFirst(replace));
	System.out.println("replaceAll: " + m.replaceAll(replace));
}
```

**输出**

```
content: I can because I think I can.
replaceFirst: I can not because I think I can.
replaceAll: I can not because I think I can not.
```

**说明**

replaceFirst：替换第一个匹配正则规则的子序列。

replaceAll：替换所有匹配正则规则的子序列。

【示例】appendReplacement、appendTail 和 replaceAll

```java
public static void main(String[] args) {
	String regex = "can";
	String replace = "can not";
	String content = "I can because I think I can.";
	StringBuffer sb = new StringBuffer();
	StringBuffer sb2 = new StringBuffer();

	System.out.println("content: " + content);
	Pattern p = Pattern.compile(regex);
	Matcher m = p.matcher(content);
	while (m.find()) {
		m.appendReplacement(sb, replace);
	}
	System.out.println("appendReplacement: " + sb);
	m.appendTail(sb);
	System.out.println("appendTail: " + sb);
}
```

**输出**

```
content: I can because I think I can.
appendReplacement: I can not because I think I can not
appendTail: I can not because I think I can not.
```

**说明**

从输出结果可以看出，`appendReplacement`和`appendTail`方法组合起来用，功能和`replaceAll`是一样的。

如果你查看`replaceAll`的源码，会发现其内部就是使用`appendReplacement`和`appendTail`方法组合来实现的。

【示例】quoteReplacement 和 replaceAll，解决特殊字符替换问题

```java
public static void main(String[] args) {
	String regex = "\\$\\{.*?\\}";
	String replace = "${product}";
	String content = "product is ${productName}.";

	Pattern p = Pattern.compile(regex);
	Matcher m = p.matcher(content);
	String replaceAll = m.replaceAll(replace);

	System.out.println("content: " + content);
	System.out.println("replaceAll: " + replaceAll);
}
```

**输出**

```
Exception in thread "main" java.lang.IllegalArgumentException: No group with name {product}
	at java.util.regex.Matcher.appendReplacement(Matcher.java:849)
	at java.util.regex.Matcher.replaceAll(Matcher.java:955)
	at org.zp.notes.javase.regex.RegexDemo.wrongMethod(RegexDemo.java:42)
	at org.zp.notes.javase.regex.RegexDemo.main(RegexDemo.java:18)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at com.intellij.rt.execution.application.AppMain.main(AppMain.java:147)
```

**说明**

`String regex = "\\$\\{.*?\\}";`表示匹配类似`${name}`这样的字符串。由于`$`、`{` 、`}`都是特殊字符，需要用反义字符`\`来修饰才能被当做一个字符串字符来处理。

上面的例子是想将 `${productName}` 替换为 `${product}` ，然而`replaceAll`方法却将传入的字符串中的`$`当做特殊字符来处理了。结果产生异常。

如何解决这个问题?

JDK1.5 引入了`quoteReplacement`方法。它可以用来转换特殊字符。其实源码非常简单，就是判断字符串中如果有`\`或`$`，就为它加一个转义字符`\`

我们对上面的代码略作调整：

`m.replaceAll(replace)`改为`m.replaceAll(Matcher.quoteReplacement(replace))`，新代码如下：

```java
public static void main(String[] args) {
	String regex = "\\$\\{.*?\\}";
	String replace = "${product}";
	String content = "product is ${productName}.";

	Pattern p = Pattern.compile(regex);
	Matcher m = p.matcher(content);
	String replaceAll = m.replaceAll(Matcher.quoteReplacement(replace));

	System.out.println("content: " + content);
	System.out.println("replaceAll: " + replaceAll);
}
```

**输出**

```
content: product is ${productName}.
replaceAll: product is ${product}.
```

**说明**

字符串中如果有`\`或`$`，不能被正常解析的问题解决。

## 3. 元字符

元字符(metacharacters)就是正则表达式中具有特殊意义的专用字符。

### 3.1. 基本元字符

正则表达式的元字符难以记忆，很大程度上是因为有很多为了简化表达而出现的等价字符。而实际上最基本的元字符，并没有那么多。对于大部分的场景，基本元字符都可以搞定。让我们从一个个实例出发，由浅入深的去体会正则的奥妙。

#### 多选（`|`）

【示例】匹配一个确定的字符串

```java
checkMatches("abc", "abc");
```

如果要匹配一个确定的字符串，非常简单，如例 1 所示。但是，如果你不确定要匹配的字符串，希望有多个选择，怎么办？答案是：使用元字符`|` ，它的含义是或。

【示例】匹配多个可选的字符串

```java
// 测试正则表达式字符：|
Assert.assertTrue(checkMatches("yes|no", "yes"));
Assert.assertTrue(checkMatches("yes|no", "no"));
Assert.assertFalse(checkMatches("yes|no", "right"));

// 输出
// yes	matches： yes|no
// no	matches： yes|no
// right	not matches： yes|no
```

#### 分组（`()`）

如果你希望表达式由多个子表达式组成，你可以使用 `()`。

【示例】匹配组合字符串

```java
Assert.assertTrue(checkMatches("(play|end)(ing|ed)", "ended"));
Assert.assertTrue(checkMatches("(play|end)(ing|ed)", "ending"));
Assert.assertTrue(checkMatches("(play|end)(ing|ed)", "playing"));
Assert.assertTrue(checkMatches("(play|end)(ing|ed)", "played"));

// 输出
// ended	matches： (play|end)(ing|ed)
// ending	matches： (play|end)(ing|ed)
// playing	matches： (play|end)(ing|ed)
// played	matches： (play|end)(ing|ed)
```

#### 指定单字符有效范围（`[]`）

前面展示了如何匹配字符串，但是很多时候你需要精确的匹配一个字符，这时可以使用`[]` 。

【示例】字符在指定范围

```java
// 测试正则表达式字符：[]
Assert.assertTrue(checkMatches("[abc]", "b"));  // 字符只能是a、b、c
Assert.assertTrue(checkMatches("[a-z]", "m")); // 字符只能是a - z
Assert.assertTrue(checkMatches("[A-Z]", "O")); // 字符只能是A - Z
Assert.assertTrue(checkMatches("[a-zA-Z]", "K")); // 字符只能是a - z和A - Z
Assert.assertTrue(checkMatches("[a-zA-Z]", "k"));
Assert.assertTrue(checkMatches("[0-9]", "5")); // 字符只能是0 - 9

// 输出
// b	matches： [abc]
// m	matches： [a-z]
// O	matches： [A-Z]
// K	matches： [a-zA-Z]
// k	matches： [a-zA-Z]
// 5	matches： [0-9]
```

#### 指定单字符无效范围（ `[^]`）

【示例】字符不能在指定范围

如果需要匹配一个字符的逆操作，即字符不能在指定范围，可以使用`[^]`。

```java
// 测试正则表达式字符：[^]
Assert.assertFalse(checkMatches("[^abc]", "b")); // 字符不能是a、b、c
Assert.assertFalse(checkMatches("[^a-z]", "m")); // 字符不能是a - z
Assert.assertFalse(checkMatches("[^A-Z]", "O")); // 字符不能是A - Z
Assert.assertFalse(checkMatches("[^a-zA-Z]", "K")); // 字符不能是a - z和A - Z
Assert.assertFalse(checkMatches("[^a-zA-Z]", "k"));
Assert.assertFalse(checkMatches("[^0-9]", "5")); // 字符不能是0 - 9

// 输出
// b	not matches： [^abc]
// m	not matches： [^a-z]
// O	not matches： [^A-Z]
// K	not matches： [^a-zA-Z]
// k	not matches： [^a-zA-Z]
// 5	not matches： [^0-9]
```

#### 限制字符数量（`{}`）

如果想要控制字符出现的次数，可以使用 `{}`。

| 字符    | 描述                                                             |
| ------- | ---------------------------------------------------------------- |
| `{n}`   | n 是一个非负整数。匹配确定的 n 次。                              |
| `{n,}`  | n 是一个非负整数。至少匹配 n 次。                                |
| `{n,m}` | m 和 n 均为非负整数，其中 n <= m。最少匹配 n 次且最多匹配 m 次。 |

【示例】限制字符出现次数

```java
// {n}: n 是一个非负整数。匹配确定的 n 次。
checkMatches("ap{1}", "a");
checkMatches("ap{1}", "ap");
checkMatches("ap{1}", "app");
checkMatches("ap{1}", "apppppppppp");

// {n,}: n 是一个非负整数。至少匹配 n 次。
checkMatches("ap{1,}", "a");
checkMatches("ap{1,}", "ap");
checkMatches("ap{1,}", "app");
checkMatches("ap{1,}", "apppppppppp");

// {n,m}: m 和 n 均为非负整数，其中 n <= m。最少匹配 n 次且最多匹配 m 次。
checkMatches("ap{2,5}", "a");
checkMatches("ap{2,5}", "ap");
checkMatches("ap{2,5}", "app");
checkMatches("ap{2,5}", "apppppppppp");

// 输出
// a	not matches： ap{1}
// ap	matches： ap{1}
// app	not matches： ap{1}
// apppppppppp	not matches： ap{1}
// a	not matches： ap{1,}
// ap	matches： ap{1,}
// app	matches： ap{1,}
// apppppppppp	matches： ap{1,}
// a	not matches： ap{2,5}
// ap	not matches： ap{2,5}
// app	matches： ap{2,5}
// apppppppppp	not matches： ap{2,5}
```

#### 转义字符（`/`）

如果想要查找元字符本身，你需要使用转义符，使得正则引擎将其视作一个普通字符，而不是一个元字符去处理。

```
* 的转义字符：\*
+ 的转义字符：\+
? 的转义字符：\?
^ 的转义字符：\^
$ 的转义字符：\$
. 的转义字符：\.
```

如果是转义符 `\` 本身，你需要使用 `\\` 。

#### 指定表达式字符串的开始（`^`）和结尾（`$`）

如果希望匹配的字符串必须以特定字符串开头，可以使用 `^` 。

> 注意：请特别留意，这里的 `^` 一定要和 `[^]` 中的 `^` 区分。

【示例】限制字符串头部

```java
Assert.assertTrue(checkMatches("^app[a-z]{0,}", "apple")); // 字符串必须以app开头
Assert.assertFalse(checkMatches("^app[a-z]{0,}", "aplause"));

// 输出
// apple	matches： ^app[a-z]{0,}
// aplause	not matches： ^app[a-z]{0,}
```

如果希望匹配的字符串必须以特定字符串开头，可以使用 `$` 。

【示例】限制字符串尾部

```java
Assert.assertTrue(checkMatches("[a-z]{0,}ing$", "playing")); // 字符串必须以ing结尾
Assert.assertFalse(checkMatches("[a-z]{0,}ing$", "long"));

// 输出
// playing	matches： [a-z]{0,}ing$
// long	not matches： [a-z]{0,}ing$
```

### 3.2. 等价字符

等价字符，顾名思义，就是对于基本元字符表达的一种简化（等价字符的功能都可以通过基本元字符来实现）。

在没有掌握基本元字符之前，可以先不用理会，因为很容易把人绕晕。

等价字符的好处在于简化了基本元字符的写法。

#### 表示某一类型字符的等价字符

下表中的等价字符都表示某一类型的字符。

| 字符     | 描述                                                                                              |
| -------- | ------------------------------------------------------------------------------------------------- |
| **`.`**  | 匹配除“\n”之外的任何单个字符。                                                                    |
| **`\d`** | 匹配一个数字字符。等价于[0-9]。                                                                   |
| **`\D`** | 匹配一个非数字字符。等价于[^0-9]。                                                                |
| **`\w`** | 匹配包括下划线的任何单词字符。类似但不等价于“[A-Za-z0-9_]”，这里的单词字符指的是 Unicode 字符集。 |
| **`\W`** | 匹配任何非单词字符。                                                                              |
| **`\s`** | 匹配任何不可见字符，包括空格、制表符、换页符等等。等价于[ \f\n\r\t\v]。                           |
| **`\S`** | 匹配任何可见字符。等价于[ \f\n\r\t\v]。                                                           |

【示例】基本等价字符的用法

```java
// 匹配除“\n”之外的任何单个字符
Assert.assertTrue(checkMatches(".{1,}", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_"));
Assert.assertTrue(checkMatches(".{1,}", "~!@#$%^&*()+`-=[]{};:<>,./?|\\"));
Assert.assertFalse(checkMatches(".", "\n"));
Assert.assertFalse(checkMatches("[^\n]", "\n"));

// 匹配一个数字字符。等价于[0-9]
Assert.assertTrue(checkMatches("\\d{1,}", "0123456789"));
// 匹配一个非数字字符。等价于[^0-9]
Assert.assertFalse(checkMatches("\\D{1,}", "0123456789"));

// 匹配包括下划线的任何单词字符。类似但不等价于“[A-Za-z0-9_]”，这里的单词字符指的是Unicode字符集
Assert.assertTrue(checkMatches("\\w{1,}", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_"));
Assert.assertFalse(checkMatches("\\w{1,}", "~!@#$%^&*()+`-=[]{};:<>,./?|\\"));
// 匹配任何非单词字符
Assert.assertFalse(checkMatches("\\W{1,}", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_"));
Assert.assertTrue(checkMatches("\\W{1,}", "~!@#$%^&*()+`-=[]{};:<>,./?|\\"));

// 匹配任何不可见字符，包括空格、制表符、换页符等等。等价于[ \f\n\r\t\v]
Assert.assertTrue(checkMatches("\\s{1,}", " \f\r\n\t"));
// 匹配任何可见字符。等价于[^ \f\n\r\t\v]
Assert.assertFalse(checkMatches("\\S{1,}", " \f\r\n\t"));

// 输出
// ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_	matches： .{1,}
// ~!@#$%^&*()+`-=[]{};:<>,./?|\\	matches： .{1,}
// \n	not matches： .
// \n	not matches： [^\n]
// 0123456789	matches： \\d{1,}
// 0123456789	not matches： \\D{1,}
// ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_	matches： \\w{1,}
// ~!@#$%^&*()+`-=[]{};:<>,./?|\\	not matches： \\w{1,}
// ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_	not matches： \\W{1,}
// ~!@#$%^&*()+`-=[]{};:<>,./?|\\	matches： \\W{1,}
// \f\r\n\t	matches： \\s{1,}
// \f\r\n\t	not matches： \\S{1,}
```

#### 限制字符数量的等价字符

在基本元字符章节中，已经介绍了限制字符数量的基本元字符 - `{}` 。

此外，还有 `*`、`+`、`?` 这个三个为了简化写法而出现的等价字符，我们来认识一下。

| 字符 | 描述                                         |
| ---- | -------------------------------------------- |
| `*`  | 匹配前面的子表达式零次或多次。等价于{0,}。   |
| `+`  | 匹配前面的子表达式一次或多次。等价于{1,}。   |
| `?`  | 匹配前面的子表达式零次或一次。等价于 {0,1}。 |

**案例 限制字符数量的等价字符**

```java
// *: 匹配前面的子表达式零次或多次。* 等价于{0,}。
checkMatches("ap*", "a");
checkMatches("ap*", "ap");
checkMatches("ap*", "app");
checkMatches("ap*", "apppppppppp");

// +: 匹配前面的子表达式一次或多次。+ 等价于 {1,}。
checkMatches("ap+", "a");
checkMatches("ap+", "ap");
checkMatches("ap+", "app");
checkMatches("ap+", "apppppppppp");

// ?: 匹配前面的子表达式零次或一次。? 等价于 {0,1}。
checkMatches("ap?", "a");
checkMatches("ap?", "ap");
checkMatches("ap?", "app");
checkMatches("ap?", "apppppppppp");

// 输出
// a	matches： ap*
// ap	matches： ap*
// app	matches： ap*
// apppppppppp	matches： ap*
// a	not matches： ap+
// ap	matches： ap+
// app	matches： ap+
// apppppppppp	matches： ap+
// a	matches： ap?
// ap	matches： ap?
// app	not matches： ap?
// apppppppppp	not matches： ap?
```

#### 元字符优先级顺序

正则表达式从左到右进行计算，并遵循优先级顺序，这与算术表达式非常类似。

下表从最高到最低说明了各种正则表达式运算符的优先级顺序：

| 运算符                                | 说明         |
| ------------------------------------- | ------------ | ---- |
| `\`                                   | 转义符       |
| `()`、`(?:)`、`(?=)`、`[]`            | 括号和中括号 |
| `*`、`+`、`?`、`{n}`、`{n,}`、`{n,m}` | 限定符       |
| `^`、`$`、`*任何字符`、`任何字符*`    | 定位点和序列 |
| `                                     | `            | 替换 |

字符具有高于替换运算符的优先级，使得 `m|food` 匹配 `m` 或 `food` 。若要匹配 `mood` 或 `food` ，请使用括号创建子表达式，从而产生 `(m|f)ood` 。

## 4. 分组构造

在基本元字符章节，提到了 `()` 字符可以用来对表达式分组。实际上分组还有更多复杂的用法。

所谓分组构造，是用来描述正则表达式的子表达式，用于捕获字符串中的子字符串。

### 4.1. 捕获与非捕获

下表为分组构造中的捕获和非捕获分类。

| 表达式         | 描述                 | 捕获或非捕获 |
| -------------- | -------------------- | ------------ |
| `(exp)`        | 匹配的子表达式       | 捕获         |
| `(?<name>exp)` | 命名的反向引用       | 捕获         |
| `(?:exp)`      | 非捕获组             | 非捕获       |
| `(?=exp)`      | 零宽度正预测先行断言 | 非捕获       |
| `(?!exp)`      | 零宽度负预测先行断言 | 非捕获       |
| `(?<=exp)`     | 零宽度正回顾后发断言 | 非捕获       |
| `(?<!exp)`     | 零宽度负回顾后发断言 | 非捕获       |

> 注：Java 正则引擎不支持平衡组。

### 4.2. 反向引用

##### 带编号的反向引用

带编号的反向引用使用以下语法：`\number`

其中*number* 是正则表达式中捕获组的序号位置。 例如，\4 匹配第四个捕获组的内容。 如果正则表达式模式中未定义*number*，则将发生分析错误

**【示例】匹配重复的单词和紧随每个重复的单词的单词(不命名子表达式)**

```java
// (\w+)\s\1\W(\w+) 匹配重复的单词和紧随每个重复的单词的单词
Assert.assertTrue(findAll("(\\w+)\\s\\1\\W(\\w+)",
		"He said that that was the the correct answer.") > 0);

// 输出
// regex = (\w+)\s\1\W(\w+), content: He said that that was the the correct answer.
// [1th] start: 8, end: 21, group: that that was
// [2th] start: 22, end: 37, group: the the correct
```

说明：

- `(\w+)`：匹配一个或多个单词字符。
- `\s`：与空白字符匹配。
- `\1`：匹配第一个组，即(\w+)。
- `\W`：匹配包括空格和标点符号的一个非单词字符。 这样可以防止正则表达式模式匹配从第一个捕获组的单词开头的单词。

#### 命名的反向引用

命名后向引用通过使用下面的语法进行定义：`\k<name >`

**【示例】匹配重复的单词和紧随每个重复的单词的单词(命名子表达式)**

```java
// (?<duplicateWord>\w+)\s\k<duplicateWord>\W(?<nextWord>\w+) 匹配重复的单词和紧随每个重复的单词的单词
Assert.assertTrue(findAll("(?<duplicateWord>\\w+)\\s\\k<duplicateWord>\\W(?<nextWord>\\w+)",
		"He said that that was the the correct answer.") > 0);

// 输出
// regex = (?<duplicateWord>\w+)\s\k<duplicateWord>\W(?<nextWord>\w+), content: He said that that was the the correct answer.
// [1th] start: 8, end: 21, group: that that was
// [2th] start: 22, end: 37, group: the the correct
```

说明：

- `(?<duplicateWord>\w+)`：匹配一个或多个单词字符。 命名此捕获组 duplicateWord。
- `\s`: 与空白字符匹配。
- `\k<duplicateWord>`：匹配名为 duplicateWord 的捕获的组。
- `\W`：匹配包括空格和标点符号的一个非单词字符。 这样可以防止正则表达式模式匹配从第一个捕获组的单词开头的单词。
- `(?<nextWord>\w+)`：匹配一个或多个单词字符。 命名此捕获组 nextWord。

### 4.3. 非捕获组

`(?:exp)` 表示当一个限定符应用到一个组，但组捕获的子字符串并非所需时，通常会使用非捕获组构造。

**【示例】匹配以.结束的语句。**

```java
// 匹配由句号终止的语句。
Assert.assertTrue(findAll("(?:\\b(?:\\w+)\\W*)+\\.", "This is a short sentence. Never end") > 0);

// 输出
// regex = (?:\b(?:\w+)\W*)+\., content: This is a short sentence. Never end
// [1th] start: 0, end: 25, group: This is a short sentence.
```

### 4.4. 零宽断言

用于查找在某些内容(但并不包括这些内容)之前或之后的东西，也就是说它们像\b,^,\$那样用于指定一个位置，这个位置应该满足一定的条件(即断言)，因此它们也被称为零宽断言。

| 表达式     | 描述                        |
| ---------- | --------------------------- |
| `(?=exp)`  | 匹配 exp 前面的位置         |
| `(?<=exp)` | 匹配 exp 后面的位置         |
| `(?!exp)`  | 匹配后面跟的不是 exp 的位置 |
| `(?<!exp)` | 匹配前面不是 exp 的位置     |

#### 匹配 exp 前面的位置

`(?=exp)` 表示输入字符串必须匹配*子表达式*中的正则表达式模式，尽管匹配的子字符串未包含在匹配结果中。

```java
// \b\w+(?=\sis\b) 表示要捕获is之前的单词
Assert.assertTrue(findAll("\\b\\w+(?=\\sis\\b)", "The dog is a Malamute.") > 0);
Assert.assertFalse(findAll("\\b\\w+(?=\\sis\\b)", "The island has beautiful birds.") > 0);
Assert.assertFalse(findAll("\\b\\w+(?=\\sis\\b)", "The pitch missed home plate.") > 0);
Assert.assertTrue(findAll("\\b\\w+(?=\\sis\\b)", "Sunday is a weekend day.") > 0);

// 输出
// regex = \b\w+(?=\sis\b), content: The dog is a Malamute.
// [1th] start: 4, end: 7, group: dog
// regex = \b\w+(?=\sis\b), content: The island has beautiful birds.
// not found
// regex = \b\w+(?=\sis\b), content: The pitch missed home plate.
// not found
// regex = \b\w+(?=\sis\b), content: Sunday is a weekend day.
// [1th] start: 0, end: 6, group: Sunday
```

说明：

- `\b`：在单词边界处开始匹配。
- `\w+`：匹配一个或多个单词字符。
- `(?=\sis\b)`：确定单词字符是否后接空白字符和字符串“is”，其在单词边界处结束。 如果如此，则匹配成功。

#### 匹配 exp 后面的位置

`(?<=exp)` 表示子表达式不得在输入字符串当前位置左侧出现，尽管子表达式未包含在匹配结果中。零宽度正回顾后发断言不会回溯。

```java
// (?<=\b20)\d{2}\b 表示要捕获以20开头的数字的后面部分
Assert.assertTrue(findAll("(?<=\\b20)\\d{2}\\b", "2010 1999 1861 2140 2009") > 0);

// 输出
// regex = (?<=\b20)\d{2}\b, content: 2010 1999 1861 2140 2009
// [1th] start: 2, end: 4, group: 10
// [2th] start: 22, end: 24, group: 09
```

说明：

- `\d{2}`：匹配两个十进制数字。
- `{?<=\b20)`：如果两个十进制数字的字边界以小数位数“20”开头，则继续匹配。
- `\b`：在单词边界处结束匹配。

#### 匹配后面跟的不是 exp 的位置

`(?!exp)` 表示输入字符串不得匹配*子表达式*中的正则表达式模式，尽管匹配的子字符串未包含在匹配结果中。

【示例】捕获未以“un”开头的单词

```java
// \b(?!un)\w+\b 表示要捕获未以“un”开头的单词
Assert.assertTrue(findAll("\\b(?!un)\\w+\\b", "unite one unethical ethics use untie ultimate") > 0);

// 输出
// regex = \b(?!un)\w+\b, content: unite one unethical ethics use untie ultimate
// [1th] start: 6, end: 9, group: one
// [2th] start: 20, end: 26, group: ethics
// [3th] start: 27, end: 30, group: use
// [4th] start: 37, end: 45, group: ultimate
```

说明：

- `\b`：在单词边界处开始匹配。
- `(?!un)`：确定接下来的两个的字符是否为“un”。 如果没有，则可能匹配。
- `\w+`：匹配一个或多个单词字符。
- `\b`：在单词边界处结束匹配。

#### 匹配前面不是 exp 的位置

`(?<!exp)` 表示子表达式不得在输入字符串当前位置的左侧出现。 但是，任何不匹配子表达式 的子字符串不包含在匹配结果中。

【示例】捕获任意工作日

```java
// (?<!(Saturday|Sunday) )\b\w+ \d{1,2}, \d{4}\b 表示要捕获任意工作日（即周一到周五）
Assert.assertTrue(findAll("(?<!(Saturday|Sunday) )\\b\\w+ \\d{1,2}, \\d{4}\\b", "Monday February 1, 2010") > 0);
Assert.assertTrue(findAll("(?<!(Saturday|Sunday) )\\b\\w+ \\d{1,2}, \\d{4}\\b", "Wednesday February 3, 2010") > 0);
Assert.assertFalse(findAll("(?<!(Saturday|Sunday) )\\b\\w+ \\d{1,2}, \\d{4}\\b", "Saturday February 6, 2010") > 0);
Assert.assertFalse(findAll("(?<!(Saturday|Sunday) )\\b\\w+ \\d{1,2}, \\d{4}\\b", "Sunday February 7, 2010") > 0);
Assert.assertTrue(findAll("(?<!(Saturday|Sunday) )\\b\\w+ \\d{1,2}, \\d{4}\\b", "Monday, February 8, 2010") > 0);

// 输出
// regex = (?<!(Saturday|Sunday) )\b\w+ \d{1,2}, \d{4}\b, content: Monday February 1, 2010
// [1th] start: 7, end: 23, group: February 1, 2010
// regex = (?<!(Saturday|Sunday) )\b\w+ \d{1,2}, \d{4}\b, content: Wednesday February 3, 2010
// [1th] start: 10, end: 26, group: February 3, 2010
// regex = (?<!(Saturday|Sunday) )\b\w+ \d{1,2}, \d{4}\b, content: Saturday February 6, 2010
// not found
// regex = (?<!(Saturday|Sunday) )\b\w+ \d{1,2}, \d{4}\b, content: Sunday February 7, 2010
// not found
// regex = (?<!(Saturday|Sunday) )\b\w+ \d{1,2}, \d{4}\b, content: Monday, February 8, 2010
// [1th] start: 8, end: 24, group: February 8, 2010
```

## 5. 贪婪与懒惰

当正则表达式中包含能接受重复的限定符时，通常的行为是（在使整个表达式能得到匹配的前提下）匹配**尽可能多**的字符。以这个表达式为例：a.\*b，它将会匹配最长的以 a 开始，以 b 结束的字符串。如果用它来搜索 aabab 的话，它会匹配整个字符串 aabab。这被称为贪婪匹配。

有时，我们更需要懒惰匹配，也就是匹配**尽可能少**的字符。前面给出的限定符都可以被转化为懒惰匹配模式，只要在它后面加上一个问号?。这样.\*?就意味着匹配任意数量的重复，但是在能使整个匹配成功的前提下使用最少的重复。

| 表达式   | 描述                              |
| -------- | --------------------------------- |
| `*?`     | 重复任意次，但尽可能少重复        |
| `+?`     | 重复 1 次或更多次，但尽可能少重复 |
| `??`     | 重复 0 次或 1 次，但尽可能少重复  |
| `{n,m}?` | 重复 n 到 m 次，但尽可能少重复    |
| `{n,}?`  | 重复 n 次以上，但尽可能少重复     |

【示例】Java 正则中贪婪与懒惰的示例

```java
// 贪婪匹配
Assert.assertTrue(findAll("a\\w*b", "abaabaaabaaaab") > 0);

// 懒惰匹配
Assert.assertTrue(findAll("a\\w*?b", "abaabaaabaaaab") > 0);
Assert.assertTrue(findAll("a\\w+?b", "abaabaaabaaaab") > 0);
Assert.assertTrue(findAll("a\\w??b", "abaabaaabaaaab") > 0);
Assert.assertTrue(findAll("a\\w{0,4}?b", "abaabaaabaaaab") > 0);
Assert.assertTrue(findAll("a\\w{3,}?b", "abaabaaabaaaab") > 0);

// 输出
// regex = a\w*b, content: abaabaaabaaaab
// [1th] start: 0, end: 14, group: abaabaaabaaaab
// regex = a\w*?b, content: abaabaaabaaaab
// [1th] start: 0, end: 2, group: ab
// [2th] start: 2, end: 5, group: aab
// [3th] start: 5, end: 9, group: aaab
// [4th] start: 9, end: 14, group: aaaab
// regex = a\w+?b, content: abaabaaabaaaab
// [1th] start: 0, end: 5, group: abaab
// [2th] start: 5, end: 9, group: aaab
// [3th] start: 9, end: 14, group: aaaab
// regex = a\w??b, content: abaabaaabaaaab
// [1th] start: 0, end: 2, group: ab
// [2th] start: 2, end: 5, group: aab
// [3th] start: 6, end: 9, group: aab
// [4th] start: 11, end: 14, group: aab
// regex = a\w{0,4}?b, content: abaabaaabaaaab
// [1th] start: 0, end: 2, group: ab
// [2th] start: 2, end: 5, group: aab
// [3th] start: 5, end: 9, group: aaab
// [4th] start: 9, end: 14, group: aaaab
// regex = a\w{3,}?b, content: abaabaaabaaaab
// [1th] start: 0, end: 5, group: abaab
// [2th] start: 5, end: 14, group: aaabaaaab
```

说明：

本例中代码展示的是使用不同贪婪或懒惰策略去查找字符串 `abaabaaabaaaab` 中匹配**以 `a` 开头，以 `b` 结尾的所有子字符串**。请从输出结果中，细细体味使用不同的贪婪或懒惰策略，对于匹配子字符串有什么影响。

## 6. 正则附录

### 6.1. 匹配正则字符串的方法

由于正则表达式中很多元字符本身就是转义字符，在 Java 字符串的规则中不会被显示出来。

为此，可以使用一个工具类`org.apache.commons.lang3.StringEscapeUtils`来做特殊处理，使得转义字符可以打印。这个工具类提供的都是静态方法，从方法命名大致也可以猜出用法，这里不多做说明。

如果你了解 maven，可以直接引入依赖

```xml
<dependency>
  <groupId>org.apache.commons</groupId>
  <artifactId>commons-lang3</artifactId>
  <version>${commons-lang3.version}</version>
</dependency>
```

【示例】本文为了展示正则匹配规则用到的方法

```java
private boolean checkMatches(String regex, String content) {
	Pattern p = Pattern.compile(regex);
	Matcher m = p.matcher(content);
	boolean flag = m.matches();
	if (m.matches()) {
		System.out.println(StringEscapeUtils.escapeJava(content) + "\tmatches： " + StringEscapeUtils.escapeJava(regex));
	} else {
		System.out.println(StringEscapeUtils.escapeJava(content) + "\tnot matches： " + StringEscapeUtils.escapeJava(regex));
	}
	return flag;
}

public int findAll(String regex, String content) {
	Pattern p = Pattern.compile(regex);
	Matcher m = p.matcher(content);
	System.out.println("regex = " + regex + ", content: " + content);

	int count = 0;
	while (m.find()) {
		count++;
		System.out.println("[" + count + "th] " + "start: " + m.start() + ", end: " + m.end()
				+ ", group: " + m.group());
	}
	if (0 == count) {
		System.out.println("not found");
	}
	return count;
}
```

### 6.2. 速查元字符字典

为了方便快查正则的元字符含义，在本节根据元字符的功能集中罗列正则的各种元字符。

#### 限定符

| 字符    | 描述                                                                                                                                                                      |
| ------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `*`     | 匹配前面的子表达式零次或多次。例如，zo* 能匹配 "z" 以及 "zoo"。* 等价于{0,}。                                                                                             |
| `+`     | 匹配前面的子表达式一次或多次。例如，'zo+' 能匹配 "zo" 以及 "zoo"，但不能匹配 "z"。+ 等价于 {1,}。                                                                         |
| `?`     | 匹配前面的子表达式零次或一次。例如，"do(es)?" 可以匹配 "do" 或 "does" 中的"do" 。? 等价于 {0,1}。                                                                         |
| `{n}`   | n 是一个非负整数。匹配确定的 n 次。例如，'o{2}' 不能匹配 "Bob" 中的 'o'，但是能匹配 "food" 中的两个 o。                                                                   |
| `{n,}`  | n 是一个非负整数。至少匹配 n 次。例如，'o{2,}' 不能匹配 "Bob" 中的 'o'，但能匹配 "foooood" 中的所有 o。'o{1,}' 等价于 'o+'。'o{0,}' 则等价于 'o\*'。                      |
| `{n,m}` | m 和 n 均为非负整数，其中 n <= m。最少匹配 n 次且最多匹配 m 次。例如，"o{1,3}" 将匹配 "fooooood" 中的前三个 o。'o{0,1}' 等价于 'o?'。请注意在逗号和两个数之间不能有空格。 |

#### 定位符

| 字符 | 描述                                                                                                   |
| ---- | ------------------------------------------------------------------------------------------------------ |
| `^`  | 匹配输入字符串开始的位置。如果设置了 RegExp 对象的 Multiline 属性，^ 还会与 \n 或 \r 之后的位置匹配。  |
| `$`  | 匹配输入字符串结尾的位置。如果设置了 RegExp 对象的 Multiline 属性，\$ 还会与 \n 或 \r 之前的位置匹配。 |
| `\b` | 匹配一个字边界，即字与空格间的位置。                                                                   |
| `\B` | 非字边界匹配。                                                                                         |

#### 非打印字符

| 字符  | 描述                                                                                                                                |
| ----- | ----------------------------------------------------------------------------------------------------------------------------------- |
| `\cx` | 匹配由 x 指明的控制字符。例如， \cM 匹配一个 Control-M 或回车符。x 的值必须为 A-Z 或 a-z 之一。否则，将 c 视为一个原义的 'c' 字符。 |
| `\f`  | 匹配一个换页符。等价于 \x0c 和 \cL。                                                                                                |
| `\n`  | 匹配一个换行符。等价于 \x0a 和 \cJ。                                                                                                |
| `\r`  | 匹配一个回车符。等价于 \x0d 和 \cM。                                                                                                |
| `\s`  | 匹配任何空白字符，包括空格、制表符、换页符等等。等价于 [ \f\n\r\t\v]。                                                              |
| `\S`  | 匹配任何非空白字符。等价于 [ \f\n\r\t\v]。                                                                                          |
| `\t`  | 匹配一个制表符。等价于 \x09 和 \cI。                                                                                                |
| `\v`  | 匹配一个垂直制表符。等价于 \x0b 和 \cK。                                                                                            |

#### 分组

| 表达式         | 描述                                                                                           |
| -------------- | ---------------------------------------------------------------------------------------------- |
| `(exp)`        | 匹配的子表达式。()中的内容就是子表达式。                                                       |
| `(?<name>exp)` | 命名的子表达式（反向引用）。                                                                   |
| `(?:exp)`      | 非捕获组，表示当一个限定符应用到一个组，但组捕获的子字符串并非所需时，通常会使用非捕获组构造。 |
| `(?=exp)`      | 匹配 exp 前面的位置。                                                                          |
| `(?<=exp)`     | 匹配 exp 后面的位置。                                                                          |
| `(?!exp)`      | 匹配后面跟的不是 exp 的位置。                                                                  |
| `(?<!exp)`     | 匹配前面不是 exp 的位置。                                                                      |

#### 特殊符号

| 字符 | 描述                                                                                                                                                     |
| ---- | -------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `\`  | 将下一个字符标记为或特殊字符、或原义字符、或向后引用、或八进制转义符。例如， 'n' 匹配字符 'n'。'\n' 匹配换行符。序列 '\\' 匹配 "\"，而 '\(' 则匹配 "("。 |
| `\|` | 指明两项之间的一个选择。                                                                                                                                 |
| `[]` | 匹配方括号范围内的任意一个字符。形式如：[xyz]、[^xyz]、[a-z]、[^a-z]、[x,y,z]                                                                            |

## 7. 正则实战

虽然本系列洋洋洒洒的大谈特谈正则表达式。但是我还是要在这里建议，如果一个正则表达式没有经过充分测试，还是要谨慎使用。

正则是把双刃剑，它可以为你节省大量的代码行。但是由于它不易阅读，维护起来可是头疼的哦（你需要一个字符一个字符的去理解）。

### 7.1. 最实用的正则

#### 校验中文

校验字符串中只能有中文字符（不包括中文标点符号）。中文字符的 Unicode 编码范围是 `\u4e00` 到 `\u9fa5`。

> 如有兴趣，可以参考[**百度百科-Unicode**](http://baike.baidu.com/link?url=3xi0vmvCIGKQLJZdn_BYhQ1IDFsoSJMrya6_eOjCBb7A6cRIW-zhZFLC9Yh8wjxU6A_HCfNuP8FBBXU9CN3Wcq) 。

```
^[\u4e00-\u9fa5]+$
```

- **匹配：** 春眠不觉晓
- **不匹配：**春眠不觉晓，

#### 校验身份证号码

身份证为 15 位或 18 位。15 位是第一代身份证。从 1999 年 10 月 1 日起，全国实行公民身份证号码制度，居民身份证编号由原 15 位升至 18 位。

- **15 位身份证**：由 15 位数字组成。排列顺序从左至右依次为：六位数字地区码；六位数字出生日期；三位顺序号，其中 15 位男为单数，女为双数。
- **18 位身份证**：由十七位数字本体码和一位数字校验码组成。排列顺序从左至右依次为：六位数字地区码；八位数字出生日期；三位数字顺序码和一位数字校验码（也可能是 X）。

> 身份证号含义详情请见：[**百度百科-居民身份证号码**](http://baike.baidu.com/link?url=5mYlYNE0RsSe2D4tydajtiaR8hAm4pPZ0FHSPuQ05N4f6H-i7qPuw7sY5KfNuiOVJWVWZvU4gf3IY-vIcKdP1CU4Fv-9pKmFQB50qGv_hZT2dkGbkd9--8_saY7omV80vEw9ixVeEwda37fHswfmtyU4QSiBG5s3K5K-JnYr1dqNlPu0f3t008UcLh5-wyID)

**地区码（6 位）**

```
(1[1-5]|2[1-3]|3[1-7]|4[1-3]|5[0-4]|6[1-5])\d{4}
```

**出生日期（8 位）**

注：下面的是 18 位身份证的有效出生日期，如果是 15 位身份证，只要将第一个\d{4}改为\d{2}即可。

```
((\d{4}((0[13578]|1[02])(0[1-9]|[12]\d|3[01])|(0[13456789]|1[012])(0[1-9]|[12]\d|30)|02(0[1-9]|1\d|2[0-8])))|([02468][048]|[13579][26])0229)
```

**15 位有效身份证**

```
^((1[1-5]|2[1-3]|3[1-7]|4[1-3]|5[0-4]|6[1-5])\d{4})((\d{2}((0[13578]|1[02])(0[1-9]|[12]\d|3[01])|(0[13456789]|1[012])(0[1-9]|[12]\d|30)|02(0[1-9]|1\d|2[0-8])))|([02468][048]|[13579][26])0229)(\d{3})$
```

- **匹配：**110001700101031

- **不匹配：**110001701501031

**18 位有效身份证**

```
^((1[1-5]|2[1-3]|3[1-7]|4[1-3]|5[0-4]|6[1-5])\d{4})((\d{4}((0[13578]|1[02])(0[1-9]|[12]\d|3[01])|(0[13456789]|1[012])(0[1-9]|[12]\d|30)|02(0[1-9]|1\d|2[0-8])))|([02468][048]|[13579][26])0229)(\d{3}(\d|X))$
```

- **匹配：**110001199001010310 | 11000019900101015X

- **不匹配：**990000199001010310 | 110001199013010310

#### 校验有效用户名、密码

**描述：**长度为 6-18 个字符，允许输入字母、数字、下划线，首字符必须为字母。

```
^[a-zA-Z]\w{5,17}$
```

- **匹配：**he_llo@worl.d.com | hel.l-o@wor-ld.museum | h1ello@123.com

- **不匹配：**hello@worl_d.com | he&llo@world.co1 | .hello@wor#.co.uk

#### 校验邮箱

**描述：**不允许使用 IP 作为域名，如 : hello@154.145.68.12

`@`符号前的邮箱用户和`.`符号前的域名(domain)必须满足以下条件：

- 字符只能是英文字母、数字、下划线`_`、`.`、`-` ；
- 首字符必须为字母或数字；
- `_`、`.`、`-` 不能连续出现。

域名的根域只能为字母，且至少为两个字符。

```
^[A-Za-z0-9](([_\.\-]?[a-zA-Z0-9]+)*)@([A-Za-z0-9]+)(([\.\-]?[a-zA-Z0-9]+)*)\.([A-Za-z]{2,})$
```

- **匹配：**he_llo@worl.d.com | hel.l-o@wor-ld.museum | h1ello@123.com
- **不匹配：**hello@worl_d.com | he&llo@world.co1 | .hello@wor#.co.uk

#### 校验 URL

**描述：**校验 URL。支持 http、https、ftp、ftps。

```
^(ht|f)(tp|tps)\://[a-zA-Z0-9\-\.]+\.([a-zA-Z]{2,3})?(/\S*)?$
```

- **匹配：**http://google.com/help/me | http://www.google.com/help/me/ | https://www.google.com/help.asp | ftp://www.google.com | ftps://google.org

- **不匹配：**http://un/www.google.com/index.asp

#### 校验时间

**描述：**校验时间。时、分、秒必须是有效数字，如果数值不是两位数，十位需要补零。

```
^([0-1][0-9]|[2][0-3]):([0-5][0-9])$
```

- **匹配：**00:00:00 | 23:59:59 | 17:06:30

- **不匹配：**17:6:30 | 24:16:30

#### 校验日期

**描述：**校验日期。日期满足以下条件：

- 格式 yyyy-MM-dd 或 yyyy-M-d
- 连字符可以没有或是“-”、“/”、“.”之一
- 闰年的二月可以有 29 日；而平年不可以。
- 一、三、五、七、八、十、十二月为 31 日。四、六、九、十一月为 30 日。

```
^(?:(?!0000)[0-9]{4}([-/.]?)(?:(?:0?[1-9]|1[0-2])\1(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])\1(?:29|30)|(?:0?[13578]|1[02])\1(?:31))|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)([-/.]?)0?2\2(?:29))$
```

- **匹配：**2016/1/1 | 2016/01/01 | 20160101 | 2016-01-01 | 2016.01.01 | 2000-02-29
- **不匹配：**2001-02-29 | 2016/12/32 | 2016/6/31 | 2016/13/1 | 2016/0/1

#### 校验中国手机号码

**描述：**中国手机号码正确格式：11 位数字。

> 移动有 16 个号段：134、135、136、137、138、139、147、150、151、152、157、158、159、182、187、188。其中 147、157、188 是 3G 号段，其他都是 2G 号段。联通有 7 种号段：130、131、132、155、156、185、186。其中 186 是 3G（WCDMA）号段，其余为 2G 号段。电信有 4 个号段：133、153、180、189。其中 189 是 3G 号段（CDMA2000），133 号段主要用作无线网卡号。总结：13 开头手机号 0-9；15 开头手机号 0-3、5-9；18 开头手机号 0、2、5-9。
>
> 此外，中国在国际上的区号为 86，所以手机号开头有+86、86 也是合法的。
>
> 以上信息来源于 [**百度百科-手机号**](http://baike.baidu.com/link?url=Bia2K_f8rGcakOlP4d9m_-DNSgXU5-0NDP0pPavS0ZbhRHQcUFUTbMERjdO4u7cvkpTJaIDeUXq_EXWnMqXMdSuMQDX3NAbZXAlZYl_V18KATWF7y1EFzUyJ62rf3bAN)

```
^((\+)?86\s*)?((13[0-9])|(15([0-3]|[5-9]))|(18[0,2,5-9]))\d{8}$
```

- **匹配：**+86 18012345678 | 86 18012345678 | 15812345678

- **不匹配：**15412345678 | 12912345678 | 180123456789

#### 校验中国固话号码

**描述：**固话号码，必须加区号（以 0 开头）。
3 位有效区号：010、020\~029，固话位数为 8 位。
4 位有效区号：03xx 开头到 09xx，固话位数为 7。

> 如果想了解更详细的信息，请参考 [**百度百科-电话区号**](http://baike.baidu.com/link?url=sX8JoxK1ja5uM5pDYvQe27_QsyqAZ_78DLSeEvwjqtG_uXqU6p5Oh7CPbImNbnwu1ClOmD8udgDIswZfYzQIw0z3BYZO3eTplvVDzieuowTYqt7yHGDAqyT7o4vvGhg4) 。

```
^(010|02[0-9])(\s|-)\d{8}|(0[3-9]\d{2})(\s|-)\d{7}$
```

- **匹配：**010-12345678 | 010 12345678 | 0512-1234567 | 0512 1234567

- **不匹配：**1234567 | 12345678

#### 校验 IPv4 地址

**描述：**IP 地址是一个 32 位的二进制数，通常被分割为 4 个“8 位二进制数”（也就是 4 个字节）。IP 地址通常用“点分十进制”表示成（a.b.c.d）的形式，其中，a,b,c,d 都是 0\~255 之间的十进制整数。

```
^([01]?\d\d?|2[0-4]\d|25[0-5])\.([01]?\d\d?|2[0-4]\d|25[0-5])\.([01]?\d\d?|2[0-4]\d|25[0-5])\.([01]?\d\d?|2[0-4]\d|25[0-5])$
```

- **匹配：**0.0.0.0 | 255.255.255.255 | 127.0.0.1

- **不匹配：**10.10.10 | 10.10.10.256

#### 校验 IPv6 地址

**描述：**IPv6 的 128 位地址通常写成 8 组，每组为四个十六进制数的形式。

IPv6 地址可以表示为以下形式：

- IPv6 地址
- 零压缩 IPv6 地址([section 2.2 of rfc5952](https://tools.ietf.org/html/rfc5952#section-2.2))
- 带有本地链接区域索引的 IPv6 地址 ([section 11 of rfc4007](https://tools.ietf.org/html/rfc4007#section-11))
- 嵌入 IPv4 的 IPv6 地址([section 2 of rfc6052](https://tools.ietf.org/html/rfc6052#section-2)
- 映射 IPv4 的 IPv6 地址 ([section 2.1 of rfc2765](https://tools.ietf.org/html/rfc2765#section-2.1))
- 翻译 IPv4 的 IPv6 地址 ([section 2.1 of rfc2765](https://tools.ietf.org/html/rfc2765#section-2.1))

> 显然，IPv6 地址的表示方式很复杂。你也可以参考：
>
> [**百度百科-IPv6**](http://baike.baidu.com/link?url=D3nmh0q_G_ZVmxXFG79mjjNfT4hs9fwjqUgygh-tvhq43KYqx88HV27WEXmoT4nA4iGzXwXMm5L-j50C2gSL5q)
>
> [**Stack overflow 上的 IPv6 正则表达高票答案**](http://stackoverflow.com/questions/53497/regular-expression-that-matches-valid-ipv6-addresses)

```
(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))
```

- **匹配：**1:2:3:4:5:6:7:8 | 1:: | 1::8 | 1::6:7:8 | 1::5:6:7:8 | 1::4:5:6:7:8 | 1::3:4:5:6:7:8 | ::2:3:4:5:6:7:8 | 1:2:3:4:5:6:7:: | 1:2:3:4:5:6::8 | 1:2:3:4:5::8 | 1:2:3:4::8 | 1:2:3::8 | 1:2::8 | 1::8 | ::8 | fe80::7:8%1 | ::255.255.255.255 | 2001:db8:3:4::192.0.2.33 | 64:ff9b::192.0.2.33

- **不匹配：**1.2.3.4.5.6.7.8 | 1::2::3

### 7.2. 特定字符

- 匹配长度为 3 的字符串：`^.{3}$`。
- 匹配由 26 个英文字母组成的字符串：`^[A-Za-z]+$`。
- 匹配由 26 个大写英文字母组成的字符串：`^[A-Z]+$`。
- 匹配由 26 个小写英文字母组成的字符串：`^[a-z]+$`。
- 匹配由数字和 26 个英文字母组成的字符串：`^[A-Za-z0-9]+$`。
- 匹配由数字、26 个英文字母或者下划线组成的字符串：`^\w+$`。

### 7.3. 特定数字

- 匹配正整数：`^[1-9]\d*$`
- 匹配负整数：`^-[1-9]\d*$`
- 匹配整数：`^(-?[1-9]\d*)|0$`
- 匹配正浮点数：`^[1-9]\d*\.\d+|0\.\d+$`
- 匹配负浮点数：`^-([1-9]\d*\.\d*|0\.\d*[1-9]\d*)$`
- 匹配浮点数：`^-?([1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0)$`

## 8. 正则表达式的性能

目前实现正则表达式引擎的方式有两种：DFA 自动机（Deterministic Final Automata 确定有限状态自动机）和 NFA 自动机（Non deterministic Finite Automaton 非确定有限状态自动机）。对比来看，构造 DFA 自动机的代价远大于 NFA 自动机，但 DFA 自动机的执行效率高于 NFA 自动机。

假设一个字符串的长度是 n，如果用 DFA 自动机作为正则表达式引擎，则匹配的时间复杂度为 O(n)；如果用 NFA 自动机作为正则表达式引擎，由于 NFA 自动机在匹配过程中存在大量的分支和回溯，假设 NFA 的状态数为 s，则该匹配算法的时间复杂度为 O（ns）。

NFA 自动机的优势是支持更多功能。例如，捕获 group、环视、占有优先量词等高级功能。这些功能都是基于子表达式独立进行匹配，因此在编程语言里，使用的正则表达式库都是基于 NFA 实现的。

### 8.1. NFA 自动机的回溯

用 NFA 自动机实现的比较复杂的正则表达式，在匹配过程中经常会引起回溯问题。大量的回溯会长时间地占用 CPU，从而带来系统性能开销。

```
text=“abbc”
regex=“ab{1,3}c”
```

这个例子匹配目的是：匹配以 a 开头，以 c 结尾，中间有 1-3 个 b 字符的字符串。NFA 自动机对其解析的过程是这样的：

- 读取正则表达式第一个匹配符 a 和字符串第一个字符 a 进行比较，a 对 a，匹配。
- 然后，读取正则表达式第二个匹配符 `b{1,3}` 和字符串的第二个字符 b 进行比较，匹配。但因为 `b{1,3}` 表示 1-3 个 b 字符串，NFA 自动机又具有贪婪特性，所以此时不会继续读取正则表达式的下一个匹配符，而是依旧使用 `b{1,3}` 和字符串的第三个字符 b 进行比较，结果还是匹配。
- 接着继续使用 `b{1,3}` 和字符串的第四个字符 c 进行比较，发现不匹配了，此时就会发生回溯，已经读取的字符串第四个字符 c 将被吐出去，指针回到第三个字符 b 的位置。
- 那么发生回溯以后，匹配过程怎么继续呢？程序会读取正则表达式的下一个匹配符 c，和字符串中的第四个字符 c 进行比较，结果匹配，结束。

### 8.2. 如何避免回溯

#### 贪婪模式（Greedy）

顾名思义，就是在数量匹配中，如果单独使用 +、 ? 、\* 或{min,max} 等量词，正则表达式会匹配尽可能多的内容。

例如，上边那个例子：

```
text=“abbc”
regex=“ab{1,3}c”
```

就是在贪婪模式下，NFA 自动机读取了最大的匹配范围，即匹配 3 个 b 字符。匹配发生了一次失败，就引起了一次回溯。如果匹配结果是“abbbc”，就会匹配成功。

```
text=“abbbc”
regex=“ab{1,3}c”
```

#### 懒惰模式（Reluctant）

在该模式下，正则表达式会尽可能少地重复匹配字符。如果匹配成功，它会继续匹配剩余的字符串。

例如，在上面例子的字符后面加一个“？”，就可以开启懒惰模式。

```
text=“abc”
regex=“ab{1,3}?c”
```

匹配结果是“abc”，该模式下 NFA 自动机首先选择最小的匹配范围，即匹配 1 个 b 字符，因此就避免了回溯问题。

#### 独占模式（Possessive）

同贪婪模式一样，独占模式一样会最大限度地匹配更多内容；不同的是，在独占模式下，匹配失败就会结束匹配，不会发生回溯问题。

还是上边的例子，在字符后面加一个“+”，就可以开启独占模式。

```
text=“abbc”
regex=“ab{1,3}+bc”
```

结果是不匹配，结束匹配，不会发生回溯问题。

> 讲到这里，你应该非常清楚了，**避免回溯的方法就是：使用懒惰模式和独占模式。**

### 8.3. 正则表达式的优化

#### 少用贪婪模式，多用独占模式

贪婪模式会引起回溯问题，可以使用独占模式来避免回溯。

#### 减少分支选择

分支选择类型 `(X|Y|Z)` 的正则表达式会降低性能，我们在开发的时候要尽量减少使用。如果一定要用，我们可以通过以下几种方式来优化：

- 首先，我们需要考虑选择的顺序，将比较常用的选择项放在前面，使它们可以较快地被匹配；
- 其次，我们可以尝试提取共用模式，例如，将 `(abcd|abef)` 替换为 `ab(cd|ef)`，后者匹配速度较快，因为 NFA 自动机会尝试匹配 ab，如果没有找到，就不会再尝试任何选项；
- 最后，如果是简单的分支选择类型，我们可以用三次 index 代替 `(X|Y|Z)`，如果测试的话，你就会发现三次 index 的效率要比 `(X|Y|Z)` 高出一些。

#### 减少捕获嵌套

- 捕获组是指把正则表达式中，子表达式匹配的内容保存到以数字编号或显式命名的数组中，方便后面引用。一般一个 () 就是一个捕获组，捕获组可以进行嵌套。
- 非捕获组则是指参与匹配却不进行分组编号的捕获组，其表达式一般由 `(?:exp)` 组成。

在正则表达式中，每个捕获组都有一个编号，编号 0 代表整个匹配到的内容。我们可以看下面的例子：

```java
public static void main(String[] args) {
	String text = "<input high=\"20\" weight=\"70\">test</input>";
	String reg="(<input.*?>)(.*?)(</input>)";
	Pattern p = Pattern.compile(reg);
	Matcher m = p.matcher(text);
	while(m.find()) {
		System.out.println(m.group(0));// 整个匹配到的内容
		System.out.println(m.group(1));//(<input.*?>)
		System.out.println(m.group(2));//(.*?)
		System.out.println(m.group(3));//(</input>)
	}
}
```

运行结果：

```
<input high=\"20\" weight=\"70\">test</input>
<input high=\"20\" weight=\"70\">
test
</input>
```

如果你并不需要获取某一个分组内的文本，那么就使用非捕获分组。例如，使用“(?:X)”代替“(X)”，我们再看下面的例子：

```java
public static void main(String[] args) {
	String text = "<input high=\"20\" weight=\"70\">test</input>";
	String reg="(?:<input.*?>)(.*?)(?:</input>)";
	Pattern p = Pattern.compile(reg);
	Matcher m = p.matcher(text);
	while(m.find()) {
		System.out.println(m.group(0));// 整个匹配到的内容
		System.out.println(m.group(1));//(.*?)
	}
}
```

运行结果：

```
<input high=\"20\" weight=\"70\">test</input>
test
```

综上可知：减少不需要获取的分组，可以提高正则表达式的性能。

## 9. 参考资料

- [正则表达式 30 分钟入门教程](http://deerchao.net/tutorials/regex/regex.htm)
- [msdn 正则表达式教程](<https://msdn.microsoft.com/zh-cn/library/d9eze55x(v=vs.80).aspx>)
- [正则应用之——日期正则表达式](http://blog.csdn.net/lxcnn/article/details/4362500)
- [http://www.regexlib.com/](http://www.regexlib.com/)
- [《Java 性能调优实战》](https://time.geekbang.org/column/intro/100028001)
