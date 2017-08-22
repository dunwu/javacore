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



> 在 [Java正则速成秘籍（一）之招式篇](http://www.cnblogs.com/jingmoxukong/p/6026474.html) 一文，我们学习了Java支持正则功能的API。
>
> 本文是**Java正则速成秘籍的心法篇**。主要介绍正则表达式的语法规则。正则语法规则是一种标准，主流开发语言对于正则语法的支持大体相同。
>
> 分组构造、贪婪与懒惰属于正则表达式中较为复杂的应用，建议理解完基本元字符后再去了解。
>
> 本文案例中使用的checkMatches、findAll方法请见附录。
>
> 本文涉及的所有案例代码，可以在 [我的github](https://github.com/atlantis1024/JavaSENotes/tree/master/src/test/java/org/zp/notes/javase/regex) 找到，如有需要，可以参考。



# 概述

为了理解下面章节的内容，你需要先了解一些基本概念。

**正则表达式**

正则表达式是对字符串操作的一种逻辑公式，就是用事先定义好的一些特定字符、及这些特定字符的组合，组成一个“规则字符串”，这个“规则字符串”用来表达对字符串的一种过滤逻辑。

**元字符**

元字符(metacharacters)就是正则表达式中具有特殊意义的专用字符。

**普通字符**

普通字符包括没有显式指定为元字符的所有可打印和不可打印字符。这包括所有大写和小写字母、所有数字、所有标点符号和一些其他符号。



# 元字符

## 基本元字符

正则表达式的元字符难以记忆，很大程度上是因为有很多为了简化表达而出现的等价字符。

而实际上最基本的元字符，并没有那么多。对于大部分的场景，基本元字符都可以搞定。

让我们从一个个实例出发，由浅入深的去体会正则的奥妙。

### 多选 - |

**例 匹配一个确定的字符串**

```java
checkMatches("abc", "abc");
```

如果要匹配一个确定的字符串，非常简单，如例1所示。

如果你不确定要匹配的字符串，希望有多个选择，怎么办？

答案是：使用元字符`|` ，它的含义是或。

**例 匹配多个可选的字符串**

```java
// 测试正则表达式字符：|
Assert.assertTrue(checkMatches("yes|no", "yes"));
Assert.assertTrue(checkMatches("yes|no", "no"));
Assert.assertFalse(checkMatches("yes|no", "right"));
```

**输出**

```
yes	matches： yes|no
no	matches： yes|no
right	not matches： yes|no
```



### 分组 - ()

如果你希望表达式由多个子表达式组成，你可以使用 `()`。

**例 匹配组合字符串**

```java
Assert.assertTrue(checkMatches("(play|end)(ing|ed)", "ended"));
Assert.assertTrue(checkMatches("(play|end)(ing|ed)", "ending"));
Assert.assertTrue(checkMatches("(play|end)(ing|ed)", "playing"));
Assert.assertTrue(checkMatches("(play|end)(ing|ed)", "played"));
```

**输出**

```
ended	matches： (play|end)(ing|ed)
ending	matches： (play|end)(ing|ed)
playing	matches： (play|end)(ing|ed)
played	matches： (play|end)(ing|ed)
```



### 指定单字符有效范围 - []

前面展示了如何匹配字符串，但是很多时候你需要精确的匹配一个字符，这时可以使用`[]` 。

**例 字符在指定范围**

```java
// 测试正则表达式字符：[]
Assert.assertTrue(checkMatches("[abc]", "b"));  // 字符只能是a、b、c
Assert.assertTrue(checkMatches("[a-z]", "m")); // 字符只能是a - z
Assert.assertTrue(checkMatches("[A-Z]", "O")); // 字符只能是A - Z
Assert.assertTrue(checkMatches("[a-zA-Z]", "K")); // 字符只能是a - z和A - Z
Assert.assertTrue(checkMatches("[a-zA-Z]", "k"));
Assert.assertTrue(checkMatches("[0-9]", "5")); // 字符只能是0 - 9
```

**输出**

```
b	matches： [abc]
m	matches： [a-z]
O	matches： [A-Z]
K	matches： [a-zA-Z]
k	matches： [a-zA-Z]
5	matches： [0-9]
```



### 指定单字符无效范围 - [^]

**例 字符不能在指定范围**

如果需要匹配一个字符的逆操作，即字符不能在指定范围，可以使用`[^]`。

```java
// 测试正则表达式字符：[^]
Assert.assertFalse(checkMatches("[^abc]", "b")); // 字符不能是a、b、c
Assert.assertFalse(checkMatches("[^a-z]", "m")); // 字符不能是a - z
Assert.assertFalse(checkMatches("[^A-Z]", "O")); // 字符不能是A - Z
Assert.assertFalse(checkMatches("[^a-zA-Z]", "K")); // 字符不能是a - z和A - Z
Assert.assertFalse(checkMatches("[^a-zA-Z]", "k"));
Assert.assertFalse(checkMatches("[^0-9]", "5")); // 字符不能是0 - 9
```

**输出**

```
b	not matches： [^abc]
m	not matches： [^a-z]
O	not matches： [^A-Z]
K	not matches： [^a-zA-Z]
k	not matches： [^a-zA-Z]
5	not matches： [^0-9]
```



### 限制字符数量 - {}

如果想要控制字符出现的次数，可以使用`{}`。

| 字符      | 描述                                       |
| ------- | ---------------------------------------- |
| `{n}`   | n 是一个非负整数。匹配确定的 n 次。                     |
| `{n,}`  | n 是一个非负整数。至少匹配 n 次。                      |
| `{n,m}` | m 和 n 均为非负整数，其中n <= m。最少匹配 n 次且最多匹配 m 次。 |

**例 限制字符出现次数**

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
```

**输出**

```
a	not matches： ap{1}
ap	matches： ap{1}
app	not matches： ap{1}
apppppppppp	not matches： ap{1}
a	not matches： ap{1,}
ap	matches： ap{1,}
app	matches： ap{1,}
apppppppppp	matches： ap{1,}
a	not matches： ap{2,5}
ap	not matches： ap{2,5}
app	matches： ap{2,5}
apppppppppp	not matches： ap{2,5}
```



### 转义字符 - /

如果想要查找元字符本身，你需要使用转义符，使得正则引擎将其视作一个普通字符，而不是一个元字符去处理。

```
* 的转义字符：\*
+ 的转义字符：\+
? 的转义字符：\?
^ 的转义字符：\^
$ 的转义字符：\$
. 的转义字符：\.
```

如果是转义符`\`本身，你也需要使用`\\` 。



### 指定表达式字符串的开始和结尾 - ^、$

如果希望匹配的字符串必须以特定字符串开头，可以使用`^` 。

注：请特别留意，这里的`^` 一定要和 `[^]` 中的 “^” 区分。

**例 限制字符串头部**

```java
Assert.assertTrue(checkMatches("^app[a-z]{0,}", "apple")); // 字符串必须以app开头
Assert.assertFalse(checkMatches("^app[a-z]{0,}", "aplause"));
```

**输出**

```
apple	matches： ^app[a-z]{0,}
aplause	not matches： ^app[a-z]{0,}
```



如果希望匹配的字符串必须以特定字符串开头，可以使用`$` 。

**例 限制字符串尾部**

```java
Assert.assertTrue(checkMatches("[a-z]{0,}ing$", "playing")); // 字符串必须以ing结尾
Assert.assertFalse(checkMatches("[a-z]{0,}ing$", "long"));
```

**输出**

```
playing	matches： [a-z]{0,}ing$
long	not matches： [a-z]{0,}ing$
```




## 等价字符
等价字符，顾名思义，就是对于基本元字符表达的一种简化（等价字符的功能都可以通过基本元字符来实现）。

在没有掌握基本元字符之前，可以先不用理会，因为很容易把人绕晕。

等价字符的好处在于简化了基本元字符的写法。

### 表示某一类型字符的等价字符

下表中的等价字符都表示某一类型的字符。

| 字符       | 描述                                       |
| -------- | ---------------------------------------- |
| **`.`**  | 匹配除“\n”之外的任何单个字符。                        |
| **`\d`** | 匹配一个数字字符。等价于[0-9]。                       |
| **`\D`** | 匹配一个非数字字符。等价于[^0-9]。                     |
| **`\w`** | 匹配包括下划线的任何单词字符。类似但不等价于“[A-Za-z0-9_]”，这里的单词字符指的是Unicode字符集。 |
| **`\W`** | 匹配任何非单词字符。                               |
| **`\s`** | 匹配任何不可见字符，包括空格、制表符、换页符等等。等价于[ \f\n\r\t\v]。 |
| **`\S`** | 匹配任何可见字符。等价于[ \f\n\r\t\v]。               |

**案例 基本等价字符的用法**

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
```

**输出**

```
ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_	matches： .{1,}
~!@#$%^&*()+`-=[]{};:<>,./?|\\	matches： .{1,}
\n	not matches： .
\n	not matches： [^\n]
0123456789	matches： \\d{1,}
0123456789	not matches： \\D{1,}
ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_	matches： \\w{1,}
~!@#$%^&*()+`-=[]{};:<>,./?|\\	not matches： \\w{1,}
ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_	not matches： \\W{1,}
~!@#$%^&*()+`-=[]{};:<>,./?|\\	matches： \\W{1,}
 \f\r\n\t	matches： \\s{1,}
 \f\r\n\t	not matches： \\S{1,}
```



### 限制字符数量的等价字符

在基本元字符章节中，已经介绍了限制字符数量的基本元字符 - `{}` 。

此外，还有 `*`、`+`、`?` 这个三个为了简化写法而出现的等价字符，我们来认识一下。

| 字符   | 描述                        |
| ---- | ------------------------- |
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

```

**输出**

```
a	matches： ap*
ap	matches： ap*
app	matches： ap*
apppppppppp	matches： ap*
a	not matches： ap+
ap	matches： ap+
app	matches： ap+
apppppppppp	matches： ap+
a	matches： ap?
ap	matches： ap?
app	not matches： ap?
apppppppppp	not matches： ap?
```



## 元字符优先级顺序

正则表达式从左到右进行计算，并遵循优先级顺序，这与算术表达式非常类似。

下表从最高到最低说明了各种正则表达式运算符的优先级顺序：

| 运算符                       | 说明     |
| ------------------------- | ------ |
| \\                        | 转义符    |
| (), (?:), (?=), []        | 括号和中括号 |
| *, +, ?, {n}, {n,}, {n,m} | 限定符    |
| ^, $, \*任何元字符、任何字符*       | 定位点和序列 |
| \|                        | 替换     |

字符具有高于替换运算符的优先级，使得“m|food”匹配“m”或“food”。若要匹配“mood”或“food”，请使用括号创建子表达式，从而产生“(m|f)ood”。



# 分组构造

在基本元字符章节，提到了 `()` 字符可以用来对表达式分组。实际上分组还有更多复杂的用法。

所谓分组构造，是用来描述正则表达式的子表达式，用于捕获字符串中的子字符串。

## 捕获与非捕获

下表为分组构造中的捕获和非捕获分类。

| 表达式            | 描述         | 捕获或非捕获 |
| -------------- | ---------- | ------ |
| `(exp)`        | 匹配的子表达式    | 捕获     |
| `(?<name>exp)` | 命名的反向引用    | 捕获     |
| `(?:exp)`      | 非捕获组       | 非捕获    |
| `(?=exp)`      | 零宽度正预测先行断言 | 非捕获    |
| `(?!exp)`      | 零宽度负预测先行断言 | 非捕获    |
| `(?<=exp)`     | 零宽度正回顾后发断言 | 非捕获    |
| `(?<!exp)`     | 零宽度负回顾后发断言 | 非捕获    |

注：Java正则引擎不支持平衡组。



## 反向引用

### 带编号的反向引用

带编号的反向引用使用以下语法：`\number`

其中*number* 是正则表达式中捕获组的序号位置。 例如，\4 匹配第四个捕获组的内容。 如果正则表达式模式中未定义*number*，则将发生分析错误

**例 匹配重复的单词和紧随每个重复的单词的单词(不命名子表达式)**

```java
// (\w+)\s\1\W(\w+) 匹配重复的单词和紧随每个重复的单词的单词
Assert.assertTrue(findAll("(\\w+)\\s\\1\\W(\\w+)",
		"He said that that was the the correct answer.") > 0);
```

**输出**

```
regex = (\w+)\s\1\W(\w+), content: He said that that was the the correct answer.
[1th] start: 8, end: 21, group: that that was
[2th] start: 22, end: 37, group: the the correct
```

**说明**

(\w+): 匹配一个或多个单词字符。
\s: 与空白字符匹配。
\1: 匹配第一个组，即(\w+)。
\W: 匹配包括空格和标点符号的一个非单词字符。 这样可以防止正则表达式模式匹配从第一个捕获组的单词开头的单词。



### 命名的反向引用

命名后向引用通过使用下面的语法进行定义：`\k<name >`

**例 匹配重复的单词和紧随每个重复的单词的单词(命名子表达式)**

```java
// (?<duplicateWord>\w+)\s\k<duplicateWord>\W(?<nextWord>\w+) 匹配重复的单词和紧随每个重复的单词的单词
Assert.assertTrue(findAll("(?<duplicateWord>\\w+)\\s\\k<duplicateWord>\\W(?<nextWord>\\w+)",
		"He said that that was the the correct answer.") > 0);
```

**输出**

```
regex = (?<duplicateWord>\w+)\s\k<duplicateWord>\W(?<nextWord>\w+), content: He said that that was the the correct answer.
[1th] start: 8, end: 21, group: that that was
[2th] start: 22, end: 37, group: the the correct
```

**说明**

(?<duplicateWord>\w+): 匹配一个或多个单词字符。 命名此捕获组 duplicateWord。
\s: 与空白字符匹配。
\k<duplicateWord>: 匹配名为 duplicateWord 的捕获的组。
\W: 匹配包括空格和标点符号的一个非单词字符。 这样可以防止正则表达式模式匹配从第一个捕获组的单词开头的单词。
(?<nextWord>\w+): 匹配一个或多个单词字符。 命名此捕获组 nextWord。



## 非捕获组

`(?:exp)` 表示当一个限定符应用到一个组，但组捕获的子字符串并非所需时，通常会使用非捕获组构造。

**例 匹配以.结束的语句。**

```java
// 匹配由句号终止的语句。
Assert.assertTrue(findAll("(?:\\b(?:\\w+)\\W*)+\\.", "This is a short sentence. Never end") > 0);
```

**输出**

```
regex = (?:\b(?:\w+)\W*)+\., content: This is a short sentence. Never end
[1th] start: 0, end: 25, group: This is a short sentence.
```



## 零宽断言

用于查找在某些内容(但并不包括这些内容)之前或之后的东西，也就是说它们像\b,^,$那样用于指定一个位置，这个位置应该满足一定的条件(即断言)，因此它们也被称为零宽断言。

| 表达式        | 描述             |
| ---------- | -------------- |
| `(?=exp)`  | 匹配exp前面的位置     |
| `(?<=exp)` | 匹配exp后面的位置     |
| `(?!exp)`  | 匹配后面跟的不是exp的位置 |
| `(?<!exp)` | 匹配前面不是exp的位置   |



### 匹配exp前面的位置

`(?=exp)` 表示输入字符串必须匹配*子表达式*中的正则表达式模式，尽管匹配的子字符串未包含在匹配结果中。

```java
// \b\w+(?=\sis\b) 表示要捕获is之前的单词
Assert.assertTrue(findAll("\\b\\w+(?=\\sis\\b)", "The dog is a Malamute.") > 0);
Assert.assertFalse(findAll("\\b\\w+(?=\\sis\\b)", "The island has beautiful birds.") > 0);
Assert.assertFalse(findAll("\\b\\w+(?=\\sis\\b)", "The pitch missed home plate.") > 0);
Assert.assertTrue(findAll("\\b\\w+(?=\\sis\\b)", "Sunday is a weekend day.") > 0);
```

输出

```
regex = \b\w+(?=\sis\b), content: The dog is a Malamute.
[1th] start: 4, end: 7, group: dog
regex = \b\w+(?=\sis\b), content: The island has beautiful birds.
not found
regex = \b\w+(?=\sis\b), content: The pitch missed home plate.
not found
regex = \b\w+(?=\sis\b), content: Sunday is a weekend day.
[1th] start: 0, end: 6, group: Sunday
```

**说明**

\b: 在单词边界处开始匹配。

\w+: 匹配一个或多个单词字符。

(?=\sis\b): 确定单词字符是否后接空白字符和字符串“is”，其在单词边界处结束。 如果如此，则匹配成功。



### 匹配exp后面的位置

`(?<=exp)` 表示子表达式不得在输入字符串当前位置左侧出现，尽管子表达式未包含在匹配结果中。零宽度正回顾后发断言不会回溯。

```java
// (?<=\b20)\d{2}\b 表示要捕获以20开头的数字的后面部分
Assert.assertTrue(findAll("(?<=\\b20)\\d{2}\\b", "2010 1999 1861 2140 2009") > 0);
```

输出

```
regex = (?<=\b20)\d{2}\b, content: 2010 1999 1861 2140 2009
[1th] start: 2, end: 4, group: 10
[2th] start: 22, end: 24, group: 09
```

**说明**

\d{2}: 匹配两个十进制数字。

{?<=\b20): 如果两个十进制数字的字边界以小数位数“20”开头，则继续匹配。

\b: 在单词边界处结束匹配。



### 匹配后面跟的不是exp的位置

`(?!exp)` 表示输入字符串不得匹配*子表达式*中的正则表达式模式，尽管匹配的子字符串未包含在匹配结果中。

**例 捕获未以“un”开头的单词**

```java
// \b(?!un)\w+\b 表示要捕获未以“un”开头的单词
Assert.assertTrue(findAll("\\b(?!un)\\w+\\b", "unite one unethical ethics use untie ultimate") > 0);
```

**输出**

```
regex = \b(?!un)\w+\b, content: unite one unethical ethics use untie ultimate
[1th] start: 6, end: 9, group: one
[2th] start: 20, end: 26, group: ethics
[3th] start: 27, end: 30, group: use
[4th] start: 37, end: 45, group: ultimate
```

**说明**

\b: 在单词边界处开始匹配。

(?!un): 确定接下来的两个的字符是否为“un”。 如果没有，则可能匹配。

\w+: 匹配一个或多个单词字符。

\b: 在单词边界处结束匹配。



### 匹配前面不是exp的位置

`(?<!exp)` 表示子表达式不得在输入字符串当前位置的左侧出现。 但是，任何不匹配子表达式 的子字符串不包含在匹配结果中。

**例 捕获任意工作日**

```java
// (?<!(Saturday|Sunday) )\b\w+ \d{1,2}, \d{4}\b 表示要捕获任意工作日（即周一到周五）
Assert.assertTrue(findAll("(?<!(Saturday|Sunday) )\\b\\w+ \\d{1,2}, \\d{4}\\b", "Monday February 1, 2010") > 0);
Assert.assertTrue(findAll("(?<!(Saturday|Sunday) )\\b\\w+ \\d{1,2}, \\d{4}\\b", "Wednesday February 3, 2010") > 0);
Assert.assertFalse(findAll("(?<!(Saturday|Sunday) )\\b\\w+ \\d{1,2}, \\d{4}\\b", "Saturday February 6, 2010") > 0);
Assert.assertFalse(findAll("(?<!(Saturday|Sunday) )\\b\\w+ \\d{1,2}, \\d{4}\\b", "Sunday February 7, 2010") > 0);
Assert.assertTrue(findAll("(?<!(Saturday|Sunday) )\\b\\w+ \\d{1,2}, \\d{4}\\b", "Monday, February 8, 2010") > 0);
```

**输出**

```
regex = (?<!(Saturday|Sunday) )\b\w+ \d{1,2}, \d{4}\b, content: Monday February 1, 2010
[1th] start: 7, end: 23, group: February 1, 2010
regex = (?<!(Saturday|Sunday) )\b\w+ \d{1,2}, \d{4}\b, content: Wednesday February 3, 2010
[1th] start: 10, end: 26, group: February 3, 2010
regex = (?<!(Saturday|Sunday) )\b\w+ \d{1,2}, \d{4}\b, content: Saturday February 6, 2010
not found
regex = (?<!(Saturday|Sunday) )\b\w+ \d{1,2}, \d{4}\b, content: Sunday February 7, 2010
not found
regex = (?<!(Saturday|Sunday) )\b\w+ \d{1,2}, \d{4}\b, content: Monday, February 8, 2010
[1th] start: 8, end: 24, group: February 8, 2010
```



# 贪婪与懒惰

当正则表达式中包含能接受重复的限定符时，通常的行为是（在使整个表达式能得到匹配的前提下）匹配**尽可能多**的字符。以这个表达式为例：a.*b，它将会匹配最长的以a开始，以b结束的字符串。如果用它来搜索aabab的话，它会匹配整个字符串aabab。这被称为贪婪匹配。

有时，我们更需要懒惰匹配，也就是匹配**尽可能少**的字符。前面给出的限定符都可以被转化为懒惰匹配模式，只要在它后面加上一个问号?。这样.*?就意味着匹配任意数量的重复，但是在能使整个匹配成功的前提下使用最少的重复。

| 表达式      | 描述               |
| -------- | ---------------- |
| `*?`     | 重复任意次，但尽可能少重复    |
| `+?`     | 重复1次或更多次，但尽可能少重复 |
| `??`     | 重复0次或1次，但尽可能少重复  |
| `{n,m}?` | 重复n到m次，但尽可能少重复   |
| `{n,}?`  | 重复n次以上，但尽可能少重复   |

**例 Java正则中贪婪与懒惰的示例**

```java
// 贪婪匹配
Assert.assertTrue(findAll("a\\w*b", "abaabaaabaaaab") > 0);

// 懒惰匹配
Assert.assertTrue(findAll("a\\w*?b", "abaabaaabaaaab") > 0);
Assert.assertTrue(findAll("a\\w+?b", "abaabaaabaaaab") > 0);
Assert.assertTrue(findAll("a\\w??b", "abaabaaabaaaab") > 0);
Assert.assertTrue(findAll("a\\w{0,4}?b", "abaabaaabaaaab") > 0);
Assert.assertTrue(findAll("a\\w{3,}?b", "abaabaaabaaaab") > 0);
```

**输出**

```
regex = a\w*b, content: abaabaaabaaaab
[1th] start: 0, end: 14, group: abaabaaabaaaab
regex = a\w*?b, content: abaabaaabaaaab
[1th] start: 0, end: 2, group: ab
[2th] start: 2, end: 5, group: aab
[3th] start: 5, end: 9, group: aaab
[4th] start: 9, end: 14, group: aaaab
regex = a\w+?b, content: abaabaaabaaaab
[1th] start: 0, end: 5, group: abaab
[2th] start: 5, end: 9, group: aaab
[3th] start: 9, end: 14, group: aaaab
regex = a\w??b, content: abaabaaabaaaab
[1th] start: 0, end: 2, group: ab
[2th] start: 2, end: 5, group: aab
[3th] start: 6, end: 9, group: aab
[4th] start: 11, end: 14, group: aab
regex = a\w{0,4}?b, content: abaabaaabaaaab
[1th] start: 0, end: 2, group: ab
[2th] start: 2, end: 5, group: aab
[3th] start: 5, end: 9, group: aaab
[4th] start: 9, end: 14, group: aaaab
regex = a\w{3,}?b, content: abaabaaabaaaab
[1th] start: 0, end: 5, group: abaab
[2th] start: 5, end: 14, group: aaabaaaab
```

**说明**

本例中代码展示的是使用不同贪婪或懒惰策略去查找字符串"abaabaaabaaaab" 中匹配**以"a"开头，以"b"结尾的所有子字符串**。

请从输出结果中，细细体味使用不同的贪婪或懒惰策略，对于匹配子字符串有什么影响。



# 附录

## 匹配正则字符串的方法

由于正则表达式中很多元字符本身就是转义字符，在Java字符串的规则中不会被显示出来。

为此，可以使用一个工具类`org.apache.commons.lang3.StringEscapeUtils`来做特殊处理，使得转义字符可以打印。这个工具类提供的都是静态方法，从方法命名大致也可以猜出用法，这里不多做说明。

如果你了解maven，可以直接引入依赖

```xml
<dependency>
  <groupId>org.apache.commons</groupId>
  <artifactId>commons-lang3</artifactId>
  <version>${commons-lang3.version}</version>
</dependency>
```

**本文为了展示正则匹配规则用到的方法**

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



## 速查元字符字典

为了方便快查正则的元字符含义，在本节根据元字符的功能集中罗列正则的各种元字符。

### 限定符

| 字符      | 描述                                       |
| ------- | ---------------------------------------- |
| `*`     | 匹配前面的子表达式零次或多次。例如，zo* 能匹配 "z" 以及 "zoo"。* 等价于{0,}。 |
| `+`     | 匹配前面的子表达式一次或多次。例如，'zo+' 能匹配 "zo" 以及 "zoo"，但不能匹配 "z"。+ 等价于 {1,}。 |
| `?`     | 匹配前面的子表达式零次或一次。例如，"do(es)?" 可以匹配 "do" 或 "does" 中的"do" 。? 等价于 {0,1}。 |
| `{n}`   | n 是一个非负整数。匹配确定的 n 次。例如，'o{2}' 不能匹配 "Bob" 中的 'o'，但是能匹配 "food" 中的两个 o。 |
| `{n,}`  | n 是一个非负整数。至少匹配n 次。例如，'o{2,}' 不能匹配 "Bob" 中的 'o'，但能匹配 "foooood" 中的所有 o。'o{1,}' 等价于 'o+'。'o{0,}' 则等价于 'o*'。 |
| `{n,m}` | m 和 n 均为非负整数，其中n <= m。最少匹配 n 次且最多匹配 m 次。例如，"o{1,3}" 将匹配 "fooooood" 中的前三个 o。'o{0,1}' 等价于 'o?'。请注意在逗号和两个数之间不能有空格。 |



### 定位符

| 字符   | 描述                                       |
| ---- | ---------------------------------------- |
| `^`  | 匹配输入字符串开始的位置。如果设置了 RegExp 对象的 Multiline 属性，^ 还会与 \n 或 \r 之后的位置匹配。 |
| `$`  | 匹配输入字符串结尾的位置。如果设置了 RegExp 对象的 Multiline 属性，$ 还会与 \n 或 \r 之前的位置匹配。 |
| `\b` | 匹配一个字边界，即字与空格间的位置。                       |
| `\B` | 非字边界匹配。                                  |



### 非打印字符

| 字符    | 描述                                       |
| ----- | ---------------------------------------- |
| `\cx` | 匹配由x指明的控制字符。例如， \cM 匹配一个 Control-M 或回车符。x 的值必须为 A-Z 或 a-z 之一。否则，将 c 视为一个原义的 'c' 字符。 |
| `\f`  | 匹配一个换页符。等价于 \x0c 和 \cL。                  |
| `\n`  | 匹配一个换行符。等价于 \x0a 和 \cJ。                  |
| `\r`  | 匹配一个回车符。等价于 \x0d 和 \cM。                  |
| `\s`  | 匹配任何空白字符，包括空格、制表符、换页符等等。等价于 [ \f\n\r\t\v]。 |
| `\S`  | 匹配任何非空白字符。等价于 [ \f\n\r\t\v]。             |
| `\t`  | 匹配一个制表符。等价于 \x09 和 \cI。                  |
| `\v`  | 匹配一个垂直制表符。等价于 \x0b 和 \cK。                |



### 分组

| 表达式            | 描述                                       |
| -------------- | ---------------------------------------- |
| `(exp)`        | 匹配的子表达式。()中的内容就是子表达式。                    |
| `(?<name>exp)` | 命名的子表达式（反向引用）。                           |
| `(?:exp)`      | 非捕获组，表示当一个限定符应用到一个组，但组捕获的子字符串并非所需时，通常会使用非捕获组构造。 |
| `(?=exp)`      | 匹配exp前面的位置。                              |
| `(?<=exp)`     | 匹配exp后面的位置。                              |
| `(?!exp)`      | 匹配后面跟的不是exp的位置。                          |
| `(?<!exp)`     | 匹配前面不是exp的位置。                            |



### 特殊符号

| 字符   | 描述                                       |
| ---- | ---------------------------------------- |
| `\`  | 将下一个字符标记为或特殊字符、或原义字符、或向后引用、或八进制转义符。例如，  'n' 匹配字符 'n'。'\n' 匹配换行符。序列 '\\' 匹配 "\"，而 '\(' 则匹配 "("。 |
| `\|` | 指明两项之间的一个选择。                             |
| `[]` | 匹配方括号范围内的任意一个字符。形式如：[xyz]、[^xyz]、[a-z]、[^a-z]、[x,y,z] |



# 参考

[正则表达式30分钟入门教程](http://deerchao.net/tutorials/regex/regex.htm)

[msdn 正则表达式教程](https://msdn.microsoft.com/zh-cn/library/d9eze55x(v=vs.80).aspx)