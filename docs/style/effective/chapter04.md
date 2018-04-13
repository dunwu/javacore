# Chapter04 类和接口

> 类和接口的规则：
>
> - 第13条：使类和成员的可访问性最小化
> - 第14条：在公有类中使用访问方法而非公有域
> - 第15条：使可变性最小化
> - 第16条：复合优先于继承
> - 第17条：要么为继承而设计，并提供文档说明，要么就禁止继承
> - 第18条：接口优于抽象类
> - 第19条：接口只用于定义类型
> - 第20条：类层次优于标签类
> - 第21条：用函数对象表示策略
> - 第22条：优先考虑静态成员类

## 第13条：使类和成员的可访问性最小化

**尽可能地使每个类或者成员不被外界访问。**

对于顶层的类和接口，只有两种访问级别：package-private 和 public 。

如果一个包级私有的顶层类（或者接口）只是在某一个类的内部被用到，就应该考虑使用它成为唯一使用它的那个类的私有嵌套类（见第22条）。

实例域绝对不能是公有的（见第14条）。包含公有可变域的类不是线程安全的。

类具有公有的静态 final 数组域，或者返回这种域的访问方法，这几乎总是错误的。

```java
// Potential security hole!
public static final Thing[] VALUES = {...}
```

解决方法：

```java
private static final Thing[] PRIVATE_VALUES = {...}
public static final List<Thing> VALUES = Collections.unmodifiableList(Arrays.asList(PRIVATE_VALUES));
```

或

```java
private static final Thing[] PRIVATE_VALUES = {...}
public static final Thing[] values(){
    return PRIVATE_VALUES.clone;
}
```

## 第14条：在公有类中使用访问方法而非公有域

如果类可以在它所在的包的外部进行访问，就提供访问方法。

如果类是包级私有的，或者是私有的嵌套类，直接暴露它的数据成员并没有本质的错误——假设这些数据成员确实描述了该类所提供的抽象。

如果成员是不可变的，设为 public 的危害比较小一些。

## 第15条：使可变性最小化

不可变类只是其实例不能被修改的类。

### 不可变类规则

- **不要提供任何会修改对象状态的方法。**
- **保证类不会被扩展。**使用 final 修饰类。
- **使所有成员都是 final 的。**
- **使所有成员都是 private 的。**
- **确保对于任何可变组件的互斥访问。**

### 不可变对象优点

- 比较简单。不可变对象可以只有创建时的状态。
- 本质上是线程安全的，它们不要求同步。
- 可以被自由地共享。
- 为其他对象提供了大量的构件

### 不可变对象的缺点

对于每个不同的值都需要一个单独的对象。

示例

```java
public final class Complex {
	private final double re;
	private final double im;

	private Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}

	public static Complex valueOf(double re, double im) {
		return new Complex(re, im);
	}

	public static Complex valueOfPolar(double r, double theta) {
		return new Complex(r * Math.cos(theta), r * Math.sin(theta));
	}

	public static final Complex ZERO = new Complex(0, 0);
	public static final Complex ONE = new Complex(1, 0);
	public static final Complex I = new Complex(0, 1);

	// Accessors with no corresponding mutators
	public double realPart() {
		return re;
	}

	public double imaginaryPart() {
		return im;
	}

	public Complex add(Complex c) {
		return new Complex(re + c.re, im + c.im);
	}
  
    ...
}
```

## 第16条：复合优先于继承

继承打破了封装性。

### 继承的问题

父类新添方法，如果恰好与子类的方法名相同，返回类型不同，编译会失败。

如果新添方法与子类的方法同名但是返回类型不同，就成了覆写（Override）。

### 复合（composition）

在新类中增加一个私有成员，引用现有类的一个实例。

### 示例

```java
public class ForwardingSet<E> implements Set<E> {
	private final Set<E> s;

	public ForwardingSet(Set<E> s) {
		this.s = s;
	}

	public void clear() {
		s.clear();
	}

	...
}
```

这种设计叫做装饰设计模式，这种类则叫做包装类。

需要注意，包装类有一个缺点：包装类不适合用在回调框架中；在回调框架中，对象把自身的引用传递给其他的对象，用于后续的回调。

## 第17条：要么为继承而设计，并提供文档说明，要么就禁止继承

## 第18条：接口优于抽象类

已有的类可以很容易被更新，以实现新的接口。

接口定义是混合类型的理想选择。

接口使得我们可以构造出非层次结构的类型框架。

接口使得安全地增强一个类的功能成为可能。

## 第19条：接口只用于定义类型

**常量接口模式是对接口的不良使用。** 

一个类需要使用哪些常量，这纯粹是实现细节，不应该对外暴露。

### 示例

**BAD**

```java
public interface PhysicalConstants {
	static final double AVOGADROS_NUMBER = 6.02214199e23;
	static final double BOLTZMANN_CONSTANT = 1.3806503e-23;
	static final double ELECTRON_MASS = 9.10938188e-31;
}
```

**GOOD**

```java
public class PhysicalConstants {
	public static final double AVOGADROS_NUMBER = 6.02214199e23;
	public static final double BOLTZMANN_CONSTANT = 1.3806503e-23;
	public static final double ELECTRON_MASS = 9.10938188e-31;
}
```

## 第20条：类层次优于标签类

标签类是冗长的，低效且容易出错。

它们有很多引用，可读性差，它们增加了内存占用以及更多的短缺。

标签类只是类继承的模仿

### 示例

**BAD**

```java
class Figure {
	enum Shape {
		RECTANGLE, CIRCLE
	};

	// Tag field - the shape of this figure
	final Shape shape;

	// These fields are used only if shape is RECTANGLE
	double length;
	double width;

	// This field is used only if shape is CIRCLE
	double radius;

	// Constructor for circle
	Figure(double radius) {
		shape = Shape.CIRCLE;
		this.radius = radius;
	}

	// Constructor for rectangle
	Figure(double length, double width) {
		shape = Shape.RECTANGLE;
		this.length = length;
		this.width = width;
	}

	double area() {
		switch (shape) {
		case RECTANGLE:
			return length * width;
		case CIRCLE:
			return Math.PI * (radius * radius);
		default:
			throw new AssertionError();
		}
	}
}
```

**GOOD**

```java
abstract class Figure {
	abstract double area();
}

class Circle extends Figure {
	final double radius;

	Circle(double radius) {
		this.radius = radius;
	}

	double area() {
		return Math.PI * (radius * radius);
	}
}

class Rectangle extends Figure {
	final double length;
	final double width;

	Rectangle(double length, double width) {
		this.length = length;
		this.width = width;
	}

	double area() {
		return length * width;
	}
}

class Square extends Rectangle {
	Square(double side) {
		super(side, side);
	}
}
```

## 第21条：用函数对象表示策略

## 第22条：优先考虑静态成员类
