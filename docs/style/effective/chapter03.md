# Chapter03 对于所有对象都通用的方法

> 对于所有对象都通用的方法：
> - 第8条：覆盖equals时请遵守通用约定
> - 第9条：覆盖equals时总要覆盖hashCode
> - 第10条：始终要覆盖toString
> - 第11条：谨慎地覆盖clone
> - 第12条：考虑实现Comparable接口

## 第8条：覆盖equals时请遵守通用约定

### 不应该覆盖

- 一个类的每个实例本质上都是唯一的。eg. `Thread`
- 不关心一个类是否提供了“逻辑相等（logical equality）”的测试功能。eg. `java.util.Random`
- 超类已经改写了 `equals`，从超类继承过来的行为对于子类也是合适的。eg. `List` 、`Map`
- 一个类是私有的，或是包级私有的，并且可以确定它的 equals 方法永远也不会被调用。

### 应该覆盖

当一个类有自己特有的“逻辑相等”概念（不同于对象身份的概念），而且超类也没有改写 `equals` 以实现期望的行为。

### equals 方法实现了等价关系（equivalence relation）

- 自反性: *x.equals(x)==true*
- 对称性: *x.equals(y)==y.equals(x)*
- 传递性: *x.equals(y)==y.equals(z)==z.equals(x)*
- 一致性: *x.equals(y)==x.equals(y)==x.equals(y)==...*
- 非空性: *x.equals(null)->false*

### 要点

1. 使用 `==` 操作符检查”实参是否为指向对象的一个引用“。
2. 使用 `instanceof` 操作符价差“实参是否为正确的类型”。
3. 把实参转换到正确的类型。
4. 对于该类中每一个“关键（significant）”域，检查实参中的域域当前对象中对应的域值是否匹配。
5. 当你编写完成了 `equals` 方法之后，应该问自己三个问题：它是否是对称的、传递的、一致的？（其它两个特性通常会自行满足）

### 告诫

- 当你改写 `equals` 的时候，总是要改写 `hashCode` （见第9条）。
- 不要企图让 `equals` 方法过于智能。
- 不要使 `equals` 方法依赖于不可靠的资源。
- 不要讲 `equals` 声明中的 `Object` 对象替换为其它的类型。

**示例**  

```java
public class ColorPoint {
    ...
    
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ColorPoint))
			return false;
		ColorPoint cp = (ColorPoint) o;
		return cp.point.equals(point) && cp.color.equals(color);
	}

	@Override
	public int hashCode() {
		return point.hashCode() * 33 + color.hashCode();
	}
}

```

## 第9条：覆盖equals时总要覆盖hashCode

### Object 规范

- 一个对象只要它用于比较操作的信息没有变化，那么无论 hashCode 何时被调用都应该返回同一个整数。
- 如果两个对象根据 equals 方法比较是相等的，那么调用这两个对象中任意一个对象的 hashCode 方法都必须产生同样的整数结果。
- 如果两个对象根据 equals 方法比较是不相等的，那么调用这两个对象中任意一个对象的 hashCode 方法，则不一定（但是推荐）产生不同的整数结果。

没有覆盖 hashCode 违反了第2条：相等的对象必须具有相等的散列码。

## 第10条：始终要覆盖toString

提供好的 `toString` 实现可以使类用起来更加舒适。

在实际应用中，toString 方法应该返回对象中包含的所有值得关注的信息。

无论你是否关注指定格式，都应该在文档中明确地表明你的意图。

无论是否指定格式，都为 toString 返回值中包含的所有信息，提供一种编程式的访问途径。

## 第11条：谨慎地覆盖clone

Cloneable interface does not contain methods If a class implements Cloneable, Object's clone method returns a field-by-field copy of the object. Otherwise it throws CloneNotSupportedException.

If you override the clone method in a nonfinal class, you should return an object obtained by invoking *super.clone*. A class that implements *Cloneable* is expected to provide a properly functioning public *clone* method.

Simple clone method if object does **not** contain fields that refer to mutable objects.

```
	@Override public PhoneNumber clone() {
		try {
			//PhoneNumber.clone must cast the result of super.clone() before returning it.
			return (PhoneNumber) super.clone();
		} catch(CloneNotSupportedException e) {
			throw new AssertionError(); // Can't happen
		}
	}
```

If object **contains** fields that refer to mutable objects, we need another solution. Mutable fields will point to same objects in memory and the original and the cloned method will share these objects.

*clone* is another constructor and therefore it must ensure not harming the original object and establishing invariants.
Calling *clone* recursively in the mutable objects is the easiest way.

```
	@Override public Stack clone() {
		try {
			Stack result = (Stack) super.clone();
			// From Java 1.5, don't need casting when cloning arrays
			result.elements = elements.clone();
			return result;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
```

Mutable objects and finals: The *clone* architecture is incompatible with normal use of final fields referring to mutable objects. More complex objects would need specific approaches where recursively calling *clone* won't work.

A *clone* method should not invoke any nonfinal methods on the clone under construction ([Item 17](https://github.com/HugoMatilla/Effective-JAVA-Summary#17-design-and-document-for-inheritance-or-else-prohibit-it)).

Object's *clone* method is declared to throw *CloneNotSupportedException*, but overriding clone methods can omit this declaration.
Public *clone* methods should omit it. ([Item 59](https://github.com/HugoMatilla/Effective-JAVA-Summary#59-avoid-unnecessary-use-of-checked-exceptions)).
If a class overrides clone, the overriding method should mimic the behavior of *Object.clone*:

- it should be declared protected,
- it should be declared to throw CloneNotSupportedException,
- it should not implement Cloneable.

Subclasses are free to implement Cloneable or not, just as if they extended Object directly

*clone* method must be properly synchronized just like any other method ([Item 66](https://github.com/HugoMatilla/Effective-JAVA-Summary#66-synchronize-access-to-shared-mutable-data)).

Summary: classes that implement Cloneable should create a method that:

- override clone
- return type is the class
- call *super.clone*
- fix fields that need to be fixed

Better provide an alternative of object copying, or don't provide it at all.

**Copy Constructor**

```
	public Yum(Yum yum);
```

**Copy Factory**

```
	public static Yum newInstance(Yum yum);
```

These alternatives:

- don't rely on a risk-prone extra-linguistic object creation mechanism
- don't demand adherence to thinly documented conventions
- don't conflict with the proper use of final fields
- don't throw unnecessary checked exceptions
- don't require casts.

Furthermore they can use its Interface-based copy constructors and factories, *conversion constructors* and *conversion factories*and allow clients to choose the implementation type `public HashSet(Set set) -> TreeSet;`

## 第12条：考虑实现Comparable接口

*Comparable* is an interface. It is not declared in *Object*

Sorting an array of objects that implement *Comparable* is as simple as `Arrays.sort(a);`

The class will interoperate with many generic algorithms and collection implementations that depend on this interface. You gain lot of power with small effort.

Follow this provisions (Reflexive, Transitive, Symmetric):

1. `if a > b then b < a` `if a == b then b == a` `if a < b then b > a`
2. `if a > b and b > c then a > c`
3. `if a == b and b == c then a == c`
4. Strong suggestion: `a.equals(b) == a.compareTo(b)`

For integral primitives use `<` and `>`operators.

For floating-point fields use *Float.compare* or *Double.compare*

For arrays start with the most significant field and work your way down.