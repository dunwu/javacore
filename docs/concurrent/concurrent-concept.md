# 从操作系统角度聊聊并发的发展

世上本无路，走的人多了也就有了路。我想，任何一种技术同样不是平白无故的出现，必然是为了解决某类问题而产生。

所以，我们不妨来谈谈并发编程的发展，这非常有助于加深我们对并发编程的理解。这就是知其然，也要知其所以然。

## 串行

早期的计算机系统是基于单个处理器（即 CPU）的顺序处理机器。何为顺序处理？简言之，凡事都得排队，一个一个处理。

而早起的软件程序也都是串行程序。即程序运行时只能执行

## 并发 vs 并行

> **并发**是指两个或多个事件在同一时间间隔发生。
>

> **并行**是指两个或者多个事件在同一时刻发生。
>

并发和并行的区别

> 打个比喻（来源于 [并发与并行的区别？ - 龚昱阳 Dozer的回答 - 知乎](https://www.zhihu.com/question/33515481/answer/58849148)）：
>
> 你吃饭吃到一半，电话来了，你一直到吃完了以后才去接，这就说明你不支持并发也不支持并行。
> 你吃饭吃到一半，电话来了，你停了下来接了电话，接完后继续吃饭，这说明你支持并发。
> 你吃饭吃到一半，电话来了，你一边打电话一边吃饭，这说明你支持并行。
>
> 并发的关键是你有处理多个任务的能力，不一定要同时。
> 并行的关键是你有同时处理多个任务的能力。

![img](https://pic4.zhimg.com/50/v2-674f0d37fca4fac1bd2df28a2b78e633_hd.jpg)

## 进程 vs 线程

> **进程**是操作系统能够进行资源分配的最小单位。可以理解为正在运行的程序。
>

> **线程**是操作系统能够进行运算调度的最小单位。
>

进程和线程的区别

线程是进程的子集。

不同的进程使用不同的内存空间；所有的线程共享一片相同的内存空间。

![“process thread”的图片搜索结果](https://i4mk.files.wordpress.com/2013/03/processes-vs-threads.jpg)

## 竞态条件和临界区

在同一程序中运行多个线程本身不会导致问题，问题在于多个线程访问了相同的资源。如，同一内存区（变量，数组，或对象）、系统（数据库，web services等）或文件。实际上，这些问题只有在一或多个线程向这些资源做了写操作时才有可能发生，只要资源没有发生变化,多个线程读取相同的资源就是安全的。

多线程同时执行下面的代码可能会出错：

```java
public class Counter {
    protected long count = 0;
    public void add(long value){
        this.count = this.count + value;  
    }
}
```

想象下线程A和B同时执行同一个Counter对象的add()方法，我们无法知道操作系统何时会在两个线程之间切换。JVM并不是将这段代码视为单条指令来执行的，而是按照下面的顺序：

1. 从内存获取 this.count 的值放到寄存器
2. 将寄存器中的值增加value
3. 将寄存器中的值写回内存

观察线程A和B交错执行会发生什么：

this.count = 0;
A:	读取 this.count 到一个寄存器 (0)
B:	读取 this.count 到一个寄存器 (0)
B: 	将寄存器的值加2
B:	回写寄存器值(2)到内存. this.count 现在等于 2
A:	将寄存器的值加3
A:	回写寄存器值(3)到内存. this.count 现在等于 3

两个线程分别加了2和3到count变量上，两个线程执行结束后count变量的值应该等于5。然而由于两个线程是交叉执行的，两个线程从内存中读出的初始值都是0。然后各自加了2和3，并分别写回内存。最终的值并不是期望的5，而是最后写回内存的那个线程的值，上面例子中最后写回内存的是线程A，但实际中也可能是线程B。如果没有采用合适的同步机制，线程间的交叉执行情况就无法预料。

### 竞态条件 & 临界区定义

当两个线程竞争同一资源时，如果对资源的访问顺序敏感，就称存在**竞态条件**。

导致竞态条件发生的代码区称作**临界区**。上例中add()方法就是一个临界区,它会产生竞态条件。在临界区中使用适当的同步就可以避免竞态条件。

## 线程安全与共享资源

允许被多个线程同时执行的代码称作线程安全的代码。线程安全的代码不包含竞态条件。当多个线程同时更新共享资源时会引发竞态条件。因此，了解Java线程执行时共享了什么资源很重要。

允许被多个线程同时执行的代码称作线程安全的代码。线程安全的代码不包含竞态条件。当多个线程同时更新共享资源时会引发竞态条件。因此，了解Java线程执行时共享了什么资源很重要。


### 局部变量

局部变量存储在线程自己的栈中。也就是说，局部变量永远也不会被多个线程共享。所以，基础类型的局部变量是线程安全的。下面是基础类型的局部变量的一个例子：

```java
public void someMethod(){
  long threadSafeInt = 0;
  threadSafeInt++;
}
```

### 局部的对象引用

对象的局部引用和基础类型的局部变量不太一样。尽管引用本身没有被共享，但引用所指的对象并没有存储在线程的栈内。所有的对象都存在共享堆中。如果在某个方法中创建的对象不会逃逸出（译者注：即该对象不会被其它方法获得，也不会被非局部变量引用到）该方法，那么它就是线程安全的。实际上，哪怕将这个对象作为参数传给其它方法，只要别的线程获取不到这个对象，那它仍是线程安全的。下面是一个线程安全的局部引用样例：

```java
public void someMethod(){
  LocalObject localObject = new LocalObject();
  localObject.callMethod();
  method2(localObject);
}

public void method2(LocalObject localObject){
  localObject.setValue("value");
}
```

样例中LocalObject对象没有被方法返回，也没有被传递给someMethod()方法外的对象。每个执行someMethod()的线程都会创建自己的LocalObject对象，并赋值给localObject引用。因此，这里的LocalObject是线程安全的。事实上，整个someMethod()都是线程安全的。即使将LocalObject作为参数传给同一个类的其它方法或其它类的方法时，它仍然是线程安全的。当然，如果LocalObject通过某些方法被传给了别的线程，那它就不再是线程安全的了。

### 对象成员

对象成员存储在堆上。如果两个线程同时更新同一个对象的同一个成员，那这个代码就不是线程安全的。下面是一个样例：

```java
public class NotThreadSafe{
    StringBuilder builder = new StringBuilder();
    public add(String text){
        this.builder.append(text);
    }
}
```

如果两个线程同时调用同一个NotThreadSafe实例上的add()方法，就会有竞态条件问题。例如：

```java
NotThreadSafe sharedInstance = new NotThreadSafe();
new Thread(new MyRunnable(sharedInstance)).start();
new Thread(new MyRunnable(sharedInstance)).start();

public class MyRunnable implements Runnable{

  NotThreadSafe instance = null;

  public MyRunnable(NotThreadSafe instance){

    this.instance = instance;

  }

  public void run(){
    this.instance.add("some text");
  }
}
```

注意两个MyRunnable共享了同一个NotThreadSafe对象。因此，当它们调用add()方法时会造成竞态条件。

当然，如果这两个线程在不同的NotThreadSafe实例上调用call()方法，就不会导致竞态条件。下面是稍微修改后的例子：

```java
new Thread(new MyRunnable(new NotThreadSafe())).start();
new Thread(new MyRunnable(new NotThreadSafe())).start();
```

现在两个线程都有自己单独的NotThreadSafe对象，调用add()方法时就会互不干扰，再也不会有竞态条件问题了。所以非线程安全的对象仍可以通过某种方式来消除竞态条件。

### 线程控制逃逸规则

线程控制逃逸规则可以帮助你判断代码中对某些资源的访问是否是线程安全的。

如果一个资源的创建，使用，销毁都在同一个线程内完成，且永远不会脱离该线程的控制，则该资源的使用就是线程安全的。

资源可以是对象，数组，文件，数据库连接，套接字等等。Java中你无需主动销毁对象，所以“销毁”指不再有引用指向对象。

即使对象本身线程安全，但如果该对象中包含其他资源（文件，数据库连接），整个应用也许就不再是线程安全的了。比如2个线程都创建了各自的数据库连接，每个连接自身是线程安全的，但它们所连接到的同一个数据库也许不是线程安全的。

比如，2个线程执行如下代码：

检查记录X是否存在，如果不存在，插入X

如果两个线程同时执行，而且碰巧检查的是同一个记录，那么两个线程最终可能都插入了记录：

* 线程1检查记录X是否存在。检查结果：不存在
* 线程2检查记录X是否存在。检查结果：不存在
* 线程1插入记录X
* 线程2插入记录X

同样的问题也会发生在文件或其他共享资源上。因此，区分某个线程控制的对象是资源本身，还是仅仅到某个资源的引用很重要。

## 线程安全及不可变性

当多个线程同时访问同一个资源，并且其中的一个或者多个线程对这个资源进行了写操作，才会产生竞态条件。多个线程同时读同一个资源不会产生竞态条件。


我们可以通过创建不可变的共享对象来保证对象在线程间共享时不会被修改，从而实现线程安全。如下示例：

```java
public class ImmutableValue{
    private int value = 0;

    public ImmutableValue(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }
}
```

请注意ImmutableValue类的成员变量value是通过构造函数赋值的，并且在类中没有set方法。这意味着一旦ImmutableValue实例被创建，value变量就不能再被修改，这就是不可变性。但你可以通过getValue()方法读取这个变量的值。

（译者注：注意，“不变”（Immutable）和“只读”（Read Only）是不同的。当一个变量是“只读”时，变量的值不能直接改变，但是可以在其它变量发生改变的时候发生改变。比如，一个人的出生年月日是“不变”属性，而一个人的年龄便是“只读”属性，但是不是“不变”属性。随着时间的变化，一个人的年龄会随之发生变化，而一个人的出生年月日则不会变化。这就是“不变”和“只读”的区别。（摘自《Java与模式》第34章））

如果你需要对ImmutableValue类的实例进行操作，可以通过得到value变量后创建一个新的实例来实现，下面是一个对value变量进行加法操作的示例：

```java
public class ImmutableValue{
    private int value = 0;

    public ImmutableValue(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public ImmutableValue add(int valueToAdd){
        return new ImmutableValue(this.value + valueToAdd);
    }
}
```

请注意add()方法以加法操作的结果作为一个新的ImmutableValue类实例返回，而不是直接对它自己的value变量进行操作。

### 引用不是线程安全的！

重要的是要记住，即使一个对象是线程安全的不可变对象，指向这个对象的引用也可能不是线程安全的。看这个例子：

```java
public class Calculator{
  private ImmutableValue currentValue = null;

  public ImmutableValue getValue(){
    return currentValue;
  }

  public void setValue(ImmutableValue newValue){
    this.currentValue = newValue;
  }

  public void add(int newValue){
    this.currentValue = this.currentValue.add(newValue);
  }
}
```

Calculator类持有一个指向ImmutableValue实例的引用。注意，通过setValue()方法和add()方法可能会改变这个引用。因此，即使Calculator类内部使用了一个不可变对象，但Calculator类本身还是可变的，因此Calculator类不是线程安全的。换句话说：ImmutableValue类是线程安全的，但使用它的类不是。当尝试通过不可变性去获得线程安全时，这点是需要牢记的。

要使Calculator类实现线程安全，将getValue()、setValue()和add()方法都声明为同步方法即可。

## Java 内存模型

