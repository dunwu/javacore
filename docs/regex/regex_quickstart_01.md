# 导读

**正则表达式是什么？有什么用？**
***正则表达式(Regular Expression)***是一种文本规则，可以用来**校验**、**查找**、**替换**与规则匹配的文本。

**又爱又恨的正则**

正则表达式是一个强大的文本匹配工具，但是它的规则实在很繁琐，而且理解起来也颇为蛋疼，容易让人望而生畏。

**如何学习正则**

刚接触正则时，我看了一堆正则的语义说明，但是仍然不明所以。后来，我多接触一些正则的应用实例，渐渐有了感觉，再结合语义说明，终有领悟。我觉得正则表达式和武侠修练武功差不多，应该先练招式，再练心法。如果一开始就直接看正则的规则，保证你会懵逼。
当你熟悉基本招式（正则基本使用案例）后，也该修炼修炼心法（正则语法）了。真正的高手不能只靠死记硬背那么几招把式。就像张三丰教张无忌太极拳一样，领悟心法，融会贯通，少侠你就可以无招胜有招，成为传说中的绝世高手。

**以上闲话可归纳为一句：学习正则应该从实例去理解规则。**

![欲练神功，必先自宫](http://upload-images.jianshu.io/upload_images/3101171-e6b876043b7ba083.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
打开秘籍：欲练神功，必先自宫！没有蛋，也就不会蛋疼了。

**Java正则速成秘籍分三篇：**

- ***[Java正则速成秘籍（一）之招式篇](http://www.cnblogs.com/jingmoxukong/p/6026474.html)***

展示Java对于正则表达式的支持。

- [***Java正则速成秘籍（二）之心法篇***](http://www.cnblogs.com/jingmoxukong/p/6030197.html)

介绍正则表达式的语法规则。

- [***Java正则速成秘籍（三）之见招拆招篇***](http://www.cnblogs.com/jingmoxukong/p/6040572.html)

从实战出发，介绍正则的常用案例。


>本文是**Java正则速成秘籍的招式篇**。主要介绍JDK对于正则表达式的支持。



# 概述

JDK中的`java.util.regex`包提供了对正则表达式的支持。

`java.util.regex`有三个核心类：

- **Pattern类：**`Pattern`是一个正则表达式的编译表示。
- **Matcher类：**`Matcher`是对输入字符串进行解释和匹配操作的引擎。
- **PatternSyntaxException：**`PatternSyntaxException`是一个非强制异常类，它表示一个正则表达式模式中的语法错误。


***注：***需要格外注意一点，在Java中使用反斜杠"\\"时必须写成 `"\\"`。所以本文的代码出现形如`String regex = "\\$\\{.*?\\}"` 其实就是"\\$\\{.*?\\}"，不要以为是画风不对哦。



# Pattern类

`Pattern`类没有公共构造方法。要创建一个`Pattern`对象，你必须首先调用其**静态方法**`compile`，加载正则规则字符串，然后返回一个Pattern对象。

与`Pattern`类一样，`Matcher`类也没有公共构造方法。你需要调用`Pattern`对象的`matcher`方法来获得一个`Matcher`对象。

***案例：Pattern和Matcher的初始化***

```java
Pattern p = Pattern.compile(regex);
Matcher m = p.matcher(content);
```




# Matcher类

`Matcher`类可以说是`java.util.regex`核心类中的必杀技！

``Matcher``类有三板斧（三类功能）：

- 校验
- 查找
- 替换

下面我们来领略一下这三块的功能。

## 校验文本是否与正则规则匹配

为了检查文本是否与正则规则匹配，Matcher提供了以下几个返回值为`boolean`的方法。

| **序号** | **方法及说明**                                |
| ------ | ---------------------------------------- |
| 1      | **public boolean lookingAt() ** 尝试将从区域开头开始的输入序列与该模式匹配。 |
| 2      | **public boolean find() **尝试查找与该模式匹配的输入序列的下一个子序列。 |
| 3      | **public boolean find(int start）**重置此匹配器，然后尝试查找匹配该模式、从指定索引开始的输入序列的下一个子序列。 |
| 4      | **public boolean matches() **尝试将整个区域与模式匹配。 |

如果你傻傻分不清上面的查找方法有什么区别，那么下面一个例子就可以让你秒懂。

### 案例：lookingAt vs find vs matches

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

**输出**

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

regex = “world” 表示的正则规则是以world开头的字符串，regex = “hello” 和regex = “helloworld” 也是同理。

- `lookingAt`方法从头部开始，检查content字符串是否有子字符串于正则规则匹配。
- `find`方法检查content字符串是否有子字符串于正则规则匹配，不管字符串所在位置。
- `matches`方法检查content字符串整体是否与正则规则匹配。



## 查找匹配正则规则的文本位置

为了查找文本匹配正则规则的位置，`Matcher`提供了以下方法：

| **序号** | **方法及说明**                                |
| ------ | ---------------------------------------- |
| 1      | **public int start() **返回以前匹配的初始索引。      |
| 2      | **public int start(int group)** 返回在以前的匹配操作期间，由给定组所捕获的子序列的初始索引 |
| 3      | **public int end()**返回最后匹配字符之后的偏移量。      |
| 4      | **public int end(int group)**返回在以前的匹配操作期间，由给定组所捕获子序列的最后字符之后的偏移量。 |
| 5      | **public String group()**返回前一个符合匹配条件的子序列。 |
| 6      | **public String group(int group)**返回指定的符合匹配条件的子序列。 |



### 案例：使用start()、end()、group() 查找所有匹配正则条件的子序列

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



## 替换匹配正则规则的文本

替换方法是替换输入字符串里文本的方法：

| **序号** | **方法及说明**                                |
| ------ | ---------------------------------------- |
| 1      | **public Matcher appendReplacement(StringBuffer sb, String replacement)**实现非终端添加和替换步骤。 |
| 2      | **public StringBuffer appendTail(StringBuffer sb)**实现终端添加和替换步骤。 |
| 3      | **public String replaceAll(String replacement) ** 替换模式与给定替换字符串相匹配的输入序列的每个子序列。 |
| 4      | **public String replaceFirst(String replacement)** 替换模式与给定替换字符串匹配的输入序列的第一个子序列。 |
| 5      | **public static String quoteReplacement(String s)**返回指定字符串的字面替换字符串。这个方法返回一个字符串，就像传递给Matcher类的appendReplacement 方法一个字面字符串一样工作。 |



### 案例：replaceFirst vs replaceAll

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



### 案例：appendReplacement、appendTail和replaceAll

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



### 案例：quoteReplacement和replaceAll，解决特殊字符替换问题

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

JDK1.5引入了`quoteReplacement`方法。它可以用来转换特殊字符。其实源码非常简单，就是判断字符串中如果有`\`或`$`，就为它加一个转义字符`\`

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