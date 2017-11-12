## 第8章 通用程序设计

### 第45条：将局部变量的作用域最小化

要使局部变量的作用域最小化，最有力的方法就是在第一次使用它的地方声明。

几乎每个局部变量的声明都应该包含一个初始化表达式。

for 循环优于 while 循环。

使方法小而集中。

### 第46条：for-each循环优先于传统的for循环

for-each 循环，通过完全隐藏迭代器或者索引变量，避免了混乱和出错的可能。

```java
// No longer the preferred idiom to iterate over a collection!
for (Iterator i = c.iterator(); i.hasNext(); ) {
    doSomething((Element) i.next()); // (No generics before 1.5)
}
// No longer the preferred idiom to iterate over an array!
for (int i = 0; i < a.length; i++) {
   doSomething(a[i]);
}
```

使用 foreach 循环：

```java
// The preferred idiom for iterating over collections and arrays
for (Element e : elements) {
    doSomething(e);
}
```

迭代器多重循环时出错

```java
// Can you spot the bug?
enum Suit { CLUB, DIAMOND, HEART, SPADE }
enum Rank { ACE, DEUCE, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT,
NINE, TEN, JACK, QUEEN, KING }
...
Collection<Suit> suits = Arrays.asList(Suit.values());
Collection<Rank> ranks = Arrays.asList(Rank.values());
List<Card> deck = new ArrayList<Card>();
for (Iterator<Suit> i = suits.iterator(); i.hasNext(); )
    for (Iterator<Rank> j = ranks.iterator(); j.hasNext(); )
        deck.add(new Card(i.next(), j.next())); // i.next() should be run only in the outer loop
```

一种解决方案：

```java
// Fixed, but ugly - you can do better!
for (Iterator<Suit> i = suits.iterator(); i.hasNext(); ) {
    Suit suit = i.next();
    for (Iterator<Rank> j = ranks.iterator(); j.hasNext(); )
        deck.add(new Card(suit, j.next()));
}
```

foreach 的解决方案：

```java
// Preferred idiom for nested iteration on collections and arrays
for (Suit suit : suits)
    for (Rank rank : ranks)
        deck.add(new Card(suit, rank));
```

有三种常见的情况无法使用 for-each 循环：

- **过滤——**如果需要遍历集合，并删除选定元素，就需要使用显示的迭代器，以便可以调用它的 remove 方法。
- **转换——**如果需要遍历列表或者数组，并取代它部分或者全部的元素值，就需要列表迭代器或者数组索引，以便设定元素的值。
- **平行迭代**——如果需要并行地遍历多个集合，就需要显式地控制迭代器或者索引变量，以便所有迭代器或者索引变量都可以得到同步前移。

### 第47条：了解和使用类库

避免重复造轮子。

使用标准库的好处：

- 可以充分利用这些编写标准类库的专家的知识，以及在你之前的其他人的使用经验。
- 不必浪费时间为那些与工作不太相关的问题提供特别的解决方案。
- 它们的性能往往会随着时间的推移而不断提高，无需你做任何努力。
- 这样的代码更易读、更易维护、更易被大多数的开发人员重用。

每个程序员应该熟悉这些库：

- java.lang
- java.util
- java.io
- java.util.concurrent

### 第48条：如果需要精确的答案，请避免使用float和double

float 和 double 类型主要是为了科学计算和工程计算而设计的，不适合用于货币计算。

如果数值范围没有超过 9 位十进制数字，就可以使用 int；如果不超过 18 位数字，就可以使用 long。如果数值可能超过 18 位数字，就必须使用 BigDecimal。

### 第49条：基本类型优先于装箱基本类型

基本类型：*int*, *double*, *boolean*

基本装箱类型：*Integer*, *Double*, *Boolean*

差异：

- 两个基本装箱类型可能有相同的值，但却是不同的识别。
- 基本装箱类型有一个非功能值：null
- 基本类型更节省时间和空间

不要在原型数据和装箱类之间使用 ==

```java
first = new Integer(1);
second = new Integer(1);
first == second; //Uses unboxing  Don't have to be true.
```

使用自动拆箱创建新的原型数据

```java
...
int f = first;  //Auto-unboxing
int s = second  //Auto-unboxing
f == s;// This is true
```

如果一个基本装箱类型没有被初始化，将返回 null

```java
Integer  i;
i == 42 // NullPointerException
```

当在操作中混合使用基本类型和装箱基本类型是，装箱基本类就会自动拆箱，这会导致性能下降。

必须使用装箱基本类型的场景：

- 作为集合中的元素、键和值
- 在参数化类型中，作为类型参数
- 在进行反射的方法调用时，

其他场景，都应该尽量使用基本类型。

### 第50条：如果其他类型更适合，则尽量避免使用字符串

- 字符串不适合代替其他的值类型。
- 字符串不适合代替枚举类型。
- 字符串不适合代替聚集类型（*aggregate* types）。
- 字符串不适合代替能力表（capabilities）。
- 若使用不当，字符串会比其他的类型更不灵活、速度更慢、也更容易出错。

### 第51条：当心字符串连接的性能

为连接 n 个字符串而重复使用字符串连接操作符，需要 n 的平方级的时间。

```java
// Inappropriate use of string concatenation - Performs horribly!
public String statement() {
    String result = "";
    for (int i = 0; i < numItems(); i++)
        result += lineForItem(i);
    return result; 
}
```

为了获得可以接受的性能，请使用 StringBuilder 代替 String 做连接操作。

```java
public String statement(){
	StringBuilder b = new StringBuilder(numItems() * LINE_WIDTH);
	for (int i = 0; i < numItems(); i++)
		b.append(lineForItem(i));
	return b.toString();
}
```

### 第52条：通过接口引用对象

如果有合适的接口类型存在，那么对于参数、返回值、变量和域来说，就都应该使用接口类型进行声明。

Good 示例：

```java
// Good - uses interface as type
List<Subscriber> subscribers = new Vector<Subscriber>();
```

Bad 示例：

```java
// Bad - uses class as type!
Vector<Subscriber> subscribers = new Vector<Subscriber>();
```

使用接口作为类型，使得程序更加灵活。当你决定更换实现时，所要做的仅仅是改变构造器中类的名称（或者使用一个不同的静态工厂）。

```java
List<Subscriber> subscribers = new ArrayList<Subscriber>();
```

> **注意**
>
> 如果原来的实现提供了某种特殊的功能，而这种功能并不是这个接口的通用约定所要求的，并且周围代码又依赖于这个功能，那么很关键的一点是：新的实现也要提供同样的功能。

如果不存在合适的接口，完全可以用类来引用对象。主要场景有：

- 值类：String, BigDecimal 等。
- 框架中的类。
- 类实现了接口，但是它提供了接口中不存在的额外方法，例如 LinkedHashMap 。

### 第53条：接口优先于反射机制

`java.lang.reflection` 支持**通过程序来访问关于已装载的类的信息**。给定一个 Class 实例，你可以获得  Constructor, Method 和 Field 实例。

反射机制允许一个类使用另一个类，及时当前者被编译的时候还根本不存在。但是，也会付出一定的代价：

- 丧失了编译时类型检查的好处。
- 执行反射访问所需要的代码非常笨拙和冗长。
- 性能损失。

**通常，普通应用程序在运行时不应该以反射方式访问对象。**

如果是**以反射方式创建实例，然后通过它们的接口或者超类，以正常方式访问这些实例**，这种形式代价较小，但是可以获得许多好处。

```java
// Reflective instantiation with interface access
public static void main (String[] args){
	// Translate the class name into a class object
	Class<?> cl =  null;
	try{
		cl = Class.forName(args[0]);// Class is specified by the first command line argument
	}catch(ClassNotFoundException e){
		System.err.println("Class not found");
		System.exit(1);
	}

	//Instantiate the class
	Set<String> s = null;
	try{
		s = (Set<String>) cl.newInstance(); //  The class can be either a HashSet or a TreeSet
	} catch(IllegalAccessException e){
		System.err.println("Class not accessible");
		System.exit(1);
	}catch(InstantionationException e){
		System.err.println("Class not instantiable");
		System.exit(1);
	}

	//Excercise the Set
	// Print the remaining arguments. The order depends in the class. If it is a HashSet
	// the order will be random, if it is a TreeSet it will be alphabetically
	s.addAll(Arrays.asList(args).subList(1,args.length));
	System.out.println(s);
}
```

类对于在运行时可能不存在的其他类、方法或者域的依赖性，用反射法进行管理，这种用法是合理的。

反射机制是一种功能强大的机制，对于特定的复杂系统编程任务，它是非常必要的，但它也有一些缺点。如果你编写的程序必须要与编译时未知的类一起工作，如有可能，就应该仅仅使用反射机制来实例化对象，而访问对象时则使用编译时已知的某个接口或者超类。

### 第54条：谨慎地使用本地方法

> **Java Native Interface(JNI)** 允许 Java 程序可以调用本地方法，所谓本地方法是指用本地程序设计语言（如 C 或者 C++）来编写的特殊方法。

从历史上看，本地方法主要有三种用途：

- 它们可以访问特定平台中的功能，如：注册表、文件锁。
- 它们可以访问遗留代码库。
- 编写程序中注重性能的部分。

随着 JVM 越来越快，使用本地方法来提高性能的做法就变得没有必要了。

而且，使用本地方法有一些严重缺点：

- 本地语言不是安全的（可能出现内存问题）。

  比如本地语言为 C 或者 C++ 程序，你可能就需要考虑内存的创建和释放，使用不当，可能会出现内存泄漏的风险。

- 本地语言与平台相关，导致程序不再是可以自由跨平台移植的。

- 本地方法的应用程序更难调试。

- 进入和退出本地代码时，需要相关的固定开销。

总之，请谨慎的使用本地方法。

### 第55条：谨慎地进行优化

业内专家们的建议：

- 优化的弊大于利，特别是不成熟的优化。
- 要努力编写好的程序，而不是快的程序。
- 努力避免那些限制性能的设计决策。
- 要考虑 API 设计决策的性能后果。

总结：

指导性原则是：要努力编写好的程序，而不是快的程序。

设计系统时，对于 API、协议、永久数据格式等一些关键点，需要考虑性能因素。在构建系统完成后，测量性能，如果速度足够快，任务就完成了。否则，可以在性能剖析器的帮助下，反复测试，直到真正找到问题的根源，然后设法优化相关部分。

### 第56条：遵守普遍接受的命名惯例

**字面命名惯例**

| 标识符                | 示例                                       |
| ------------------ | ---------------------------------------- |
| Package            | com.google.inject, org.joda.time.format  |
| Class or Interface | Timer, FutureTask, LinkedHashMap, HttpServlet |
| Method or Field    | remove, ensureCapacity, getCrc           |
| Constant Field     | MIN_VALUE, NEGATIVE_INFINITY             |
| Local Variable     | i, xref, houseNumber                     |
| Type Parameter     | T, E, K, V, X, T1, T2                    |

**语法命名惯例**

| 类型                                       | 惯例                                       | 示例                                       |
| ---------------------------------------- | ---------------------------------------- | ---------------------------------------- |
| Classes and enum types                   | Singular noun or noun phrase             | Timer, BufferedWriter, ChessPiece        |
| Interfaces                               | Like classes                             | Collection, Comparator                   |
| Interfaces                               | With an adjective ending in *able* or *ible* | Runnable, Iterable, Accessible           |
| Annotation types                         | Nouns, verbs, prepositions, adjectives ... | BindingAnnotation, Inject, ImplementedBy, Singleton |
| Static factories (common names)          | ---                                      | valueOf, of, getInstance, newInstance, getType, newType |
| **Methods that...**                      | ---                                      | ---                                      |
| perform actions                          | verb or verb phrase                      | append, drawImage                        |
| return a boolean                         | names beginning with *is* or, *has*      | isDigit, isProbablePrime, isEmpty, isEnabled, hasSiblings |
| return a non-boolean or attribute        | noun, a noun phrase, or begin with *get* | size, hashCode, or getTime               |
| convert the type of an object            | *toType*                                 | toString, toArray                        |
| return a view ([Item 5](https://github.com/HugoMatilla/Effective-JAVA-Summary#5-avoid-creating-objects)) of a different type | *asType*                                 | asList                                   |
| return a primitive with the same value   |                                          |                                          |