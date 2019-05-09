# Java 本地化

> :notebook: 本文已归档到：「[javacore](https://github.com/dunwu/javacore)」

<!-- TOC depthFrom:2 depthTo:3 -->

- [背景知识](#背景知识)
    - [语言编码、国家/地区编码](#语言编码国家地区编码)
    - [字符编码](#字符编码)
- [Java 中实现本地化](#java-中实现本地化)
    - [定义不同语种的模板](#定义不同语种的模板)
    - [选择语种](#选择语种)
    - [加载指定语种的模板](#加载指定语种的模板)
- [支持本地化的工具类](#支持本地化的工具类)
    - [NumberFormat](#numberformat)
    - [DateFormat](#dateformat)
    - [MessageFormat](#messageformat)

<!-- /TOC -->

## 背景知识

通讯的发达，使得世界各地交流越来越紧密。许多的软件产品也要面向世界上不同国家的用户。其中，语言障碍显然是产品在不同语种用户中进行推广的一个重要问题。

本文围绕本地化这一主题，先介绍国际标准的语言编码，然后讲解在 Java 应用中如何去实现本地化。

### 语言编码、国家/地区编码

做 web 开发的朋友可能多多少少接触过类似 **zh-cn**, **en-us** 这样的编码字样。

这些编码是用来表示指定的国家地区的语言类型的。那么，这些含有特殊含义的编码是如何产生的呢？

[**ISO-639**](http://www.loc.gov/standards/iso639-2/php/English_list.php) 标准使用编码定义了国际上常见的语言，每一种语言由两个小写字母表示。

[**ISO-3166**](https://www.iso.org/obp/ui/#iso:std:iso:3166:-2:ed-3:v1:en,fr) 标准使用编码定义了国家/地区，每个国家/地区由两个大写字母表示。

下表列举了一些常见国家、地区的语言编码：

| 国家/地区          | 语言编码 | 国家/地区          | 语言编码 |
| ------------------ | -------- | ------------------ | -------- |
| 简体中文(中国)     | zh-cn    | 繁体中文(台湾地区) | zh-tw    |
| 繁体中文(香港)     | zh-hk    | 英语(香港)         | en-hk    |
| 英语(美国)         | en-us    | 英语(英国)         | en-gb    |
| 英语(全球)         | en-ww    | 英语(加拿大)       | en-ca    |
| 英语(澳大利亚)     | en-au    | 英语(爱尔兰)       | en-ie    |
| 英语(芬兰)         | en-fi    | 芬兰语(芬兰)       | fi-fi    |
| 英语(丹麦)         | en-dk    | 丹麦语(丹麦)       | da-dk    |
| 英语(以色列)       | en-il    | 希伯来语(以色列)   | he-il    |
| 英语(南非)         | en-za    | 英语(印度)         | en-in    |
| 英语(挪威)         | en-no    | 英语(新加坡)       | en-sg    |
| 英语(新西兰)       | en-nz    | 英语(印度尼西亚)   | en-id    |
| 英语(菲律宾)       | en-ph    | 英语(泰国)         | en-th    |
| 英语(马来西亚)     | en-my    | 英语(阿拉伯)       | en-xa    |
| 韩文(韩国)         | ko-kr    | 日语(日本)         | ja-jp    |
| 荷兰语(荷兰)       | nl-nl    | 荷兰语(比利时)     | nl-be    |
| 葡萄牙语(葡萄牙)   | pt-pt    | 葡萄牙语(巴西)     | pt-br    |
| 法语(法国)         | fr-fr    | 法语(卢森堡)       | fr-lu    |
| 法语(瑞士)         | fr-ch    | 法语(比利时)       | fr-be    |
| 法语(加拿大)       | fr-ca    | 西班牙语(拉丁美洲) | es-la    |
| 西班牙语(西班牙)   | es-es    | 西班牙语(阿根廷)   | es-ar    |
| 西班牙语(美国)     | es-us    | 西班牙语(墨西哥)   | es-mx    |
| 西班牙语(哥伦比亚) | es-co    | 西班牙语(波多黎各) | es-pr    |
| 德语(德国)         | de-de    | 德语(奥地利)       | de-at    |
| 德语(瑞士)         | de-ch    | 俄语(俄罗斯)       | ru-ru    |
| 意大利语(意大利)   | it-it    | 希腊语(希腊)       | el-gr    |
| 挪威语(挪威)       | no-no    | 匈牙利语(匈牙利)   | hu-hu    |
| 土耳其语(土耳其)   | tr-tr    | 捷克语(捷克共和国) | cs-cz    |
| 斯洛文尼亚语       | sl-sl    | 波兰语(波兰)       | pl-pl    |
| 瑞典语(瑞典)       | sv-se    |                    |          |

**注：由表中可以看出语言、国家/地区编码一般都是英文单词的缩写。**

### 字符编码

在此处，引申一下字符编码的概念。

**是不是有了语言、国家/地区编码，计算机就可以识别各种语言了？**

答案是否。作为程序员，相信每个人都会遇到过这样的情况：期望打印中文，结果输出的却是乱码。

这种情况，往往是因为字符编码的问题。

计算机在设计之初，并没有考虑多个国家，多种不同语言的应用场景。当时定义一种`ASCII`码，将字母、数字和其他符号编号用 7 比特的二进制数来表示。后来，计算机在世界开始普及，为了适应多种文字，出现了多种编码格式，例如中文汉字一般使用的编码格式为`GB2312`、`GBK`。

由此，又产生了一个问题，**不同字符编码之间互相无法识别**。于是，为了一统江湖，出现了 `unicode` 编码。它为每种语言的每个字符设定了统一并且唯一的二进制编码，以满足跨语言、跨平台的文本转换需求。

有人不禁要问，既然 `Unicode` 可以支持所有语言的字符，那还要其他字符编码做什么？

`Unicode` 有一个缺点：为了支持所有语言的字符，所以它需要用更多位数去表示，比如 `ASCII` 表示一个英文字符只需要一个字节，而 `Unicode` 则需要两个字节。很明显，如果字符数多，这样的效率会很低。

为了解决这个问题，有出现了一些中间格式的字符编码：如 `UTF-8`、`UTF-16`、`UTF-32` 等（中国的程序员一般使用**UTF-8**编码）。

## Java 中实现本地化

本地化的实现原理很简单：

1.  先定义好不同语种的模板；
2.  选择语种；
3.  加载指定语种的模板。

接下来，本文会按照步骤逐一讲解实现本地化的具体步骤

### 定义不同语种的模板

**Java 中将多语言文本存储在格式为 `properties` 的资源文件中。**

它必须遵照以下的命名规范：

```
<资源名>_<语言代码>_<国家/地区编码>.properties
```

其中，语言编码和国家/地区编码都是可选的。

注：`<资源名>.properties` 命名的本地化资源文件是**默认的资源文件**，即某个本地化类型在系统中找不到对应的资源文件，就采用这个默认的资源文件。

#### 定义 properties 文件

在`src/main/resources/locales` 路径下定义名为 content 的不同语种资源文件：

**content_en_US.properties**

```
helloWorld = HelloWorld!
time = The current time is %s.
```

**content_zh_CN.properties**

```
helloWorld = \u4e16\u754c\uff0c\u4f60\u597d\uff01
time = \u5f53\u524d\u65f6\u95f4\u662f\u0025\u0073\u3002
```

可以看到：几个资源文件中，定义的 Key 完全一致，只是 Value 是对应语言的字符串。

虽然属性值各不相同，但属性名却是相同的，这样应用程序就可以通过 Locale 对象和属性名精确调用到某个具体的属性值了。

#### Unicode 转换工具

上一节中，我们定义的中文资源文件中的属性值都是以\u 开头的四位 16 进制数。其实，这表示的是一个 Unicode 编码。

```
helloWorld = \u4e16\u754c\uff0c\u4f60\u597d\uff01
time = \u5f53\u524d\u65f6\u95f4\u662f\u0025\u0073\u3002
```

本文的字符编码中提到了，为了达到跨编码也正常显示的目的，有必要将非 `ASCII` 字符转为 `Unicode` 编码。上面的中文资源文件就是中文转为 `Unicode` 的结果。

怎么将非 `ASCII` 字符转为 `Unicode` 编码呢？

JDK 在 bin 目录下为我们提供了一个转换工具：**native2ascii**。

它可以将中文字符的资源文件转换为 `Unicode` 代码格式的文件，命令格式如下：

```
native2ascii [-reverse] [-encoding 编码] [输入文件 [输出文件]]
```

假设**content_zh_CN.properties** 在 `d:\` 目录。执行以下命令可以新建一个名为 **content_zh_CN_new.properties** 的文件，其中的内容就中文字符转为 `UTF-8` 编码格式的结果。

```
native2ascii -encoding utf-8 d:\content_zh_CN.properties d:\content_zh_CN_new.properties
```

### 选择语种

定义了多语言资源文件，第二步就是根据本地语种选择模板文件了。

#### Locale

在 Java 中，一个 `java.util.Locale` 对象表示了特定的地理、政治和文化地区。需要 Locale 来执行其任务的操作称为语言环境敏感的操作，它使用 Locale 为用户量身定制本地信息。

它有三个构造方法

`Locale(String language)` ：根据语言编码初始化
`Locale(String language, String country)` ：根据语言编码、国家编码初始化
`Locale(String language, String country, String variant)` ：根据语言编码、国家编码、变体初始化

此外，Locale 定义了一些常用的 Locale 常量：`Locale.ENGLISH`、`Locale.CHINESE` 等。

```java
// 初始化一个通用英语的locale.
Locale locale1 = new Locale("en");
// 初始化一个加拿大英语的locale.
Locale locale2 = new Locale("en", "CA");
// 初始化一个美式英语变种硅谷英语的locale
Locale locale3 = new Locale("en", "US", "SiliconValley");
// 根据Locale常量初始化一个简体中文
Locale locale4 = Locale.SIMPLIFIED_CHINESE;
```

### 加载指定语种的模板

#### ResourceBoundle

Java 为我们提供了用于加载本地化资源文件的工具类：`java.util.ResourceBoundle`。

`ResourceBoundle` 提供了多个名为 `getBundle` 的静态重载方法，这些方法的作用是用来根据资源名、Locale 选择指定语种的资源文件。需要说明的是： `getBundle` 方法的第一个参数一般都是`baseName` ，这个参数表示资源文件名。

`ResourceBoundle` 还提供了名为 `getString` 的方法，用来获取资源文件中 key 对应的 value。

```java
public static void main(String[] args) {
    // 根据语言+地区编码初始化
    ResourceBundle rbUS = ResourceBundle.getBundle("locales.content", new Locale("en", "US"));
    // 根据Locale常量初始化
    ResourceBundle rbZhCN = ResourceBundle.getBundle("locales.content", Locale.SIMPLIFIED_CHINESE);
    // 获取本地系统默认的Locale初始化
    ResourceBundle rbDefault = ResourceBundle.getBundle("locales.content");
    // ResourceBundle rbDefault =ResourceBundle.getBundle("locales.content", Locale.getDefault()); // 与上行代码等价

    System.out.println("us-US:" + rbUS.getString("helloWorld"));
    System.out.println("us-US:" + String.format(rbUS.getString("time"), "08:00"));
    System.out.println("zh-CN：" + rbZhCN.getString("helloWorld"));
    System.out.println("zh-CN：" + String.format(rbZhCN.getString("time"), "08:00"));
    System.out.println("default：" + rbDefault.getString("helloWorld"));
    System.out.println("default：" + String.format(rbDefault.getString("time"), "08:00"));
}
```

**输出**

```
us-US:HelloWorld!
us-US:The current time is 08:00.
zh-CN：世界，你好！
zh-CN：当前时间是08:00。
default：世界，你好！
default：当前时间是08:00。
```

注：在加载资源时，如果指定的本地化资源文件不存在，它会尝试按下面的顺序加载其他的资源：本地系统默认本地化对象对应的资源 -> 默认的资源。如果指定错误，Java 会提示找不到资源文件。

## 支持本地化的工具类

Java 中也提供了几个支持本地化的格式化工具类。例如：`NumberFormat`、`DateFormat`、`MessageFormat`

### NumberFormat

`NumberFormat` 是所有数字格式类的基类。它提供格式化和解析数字的接口。它也提供了决定数字所属语言类型的方法。

```java
public static void main(String[] args) {
	double num = 123456.78;
	NumberFormat format = NumberFormat.getCurrencyInstance(Locale.SIMPLIFIED_CHINESE);
	System.out.format("%f 的本地化（%s）结果: %s", num, Locale.SIMPLIFIED_CHINESE, format.format(num));
}
```

### DateFormat

DateFormat 是日期、时间格式化类的抽象类。它支持基于语言习惯的日期、时间格式。

```java
public static void main(String[] args) {
	Date date = new Date();
	DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);
	DateFormat df2 = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.SIMPLIFIED_CHINESE);
	System.out.format("%s 的本地化（%s）结果: %s\n", date, Locale.SIMPLIFIED_CHINESE, df.format(date));
	System.out.format("%s 的本地化（%s）结果: %s\n", date, Locale.SIMPLIFIED_CHINESE, df2.format(date));
}
```

### MessageFormat

Messageformat 提供一种与语言无关的拼接消息的方式。通过这种拼接方式，将最终呈现返回给使用者。

```java
public static void main(String[] args) {
	String pattern1 = "{0}，你好！你于  {1} 消费  {2} 元。";
	String pattern2 = "At {1,time,short} On {1,date,long}，{0} paid {2,number, currency}.";
	Object[] params = {"Jack", new GregorianCalendar().getTime(), 8888};
	String msg1 = MessageFormat.format(pattern1, params);
	MessageFormat mf = new MessageFormat(pattern2, Locale.US);
	String msg2 = mf.format(params);
	System.out.println(msg1);
	System.out.println(msg2);
}
```
