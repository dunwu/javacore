# Chapter02 创建和销毁对象

> 创建和销毁对象的条规：
>
> - 第1条：考虑用静态工厂方法代替构造器
> - 第2条：遇到多个构造器参数时要考虑用构建器
> - 第3条：用私有构造器或者枚举类型强化Singleton属性
> - 第4条：通过私有构造器强化不可实例化的能力
> - 第5条：避免创建不必要的对象
> - 第6条：消除过期的对象引用
> - 第7条：避免使用终结方法

## 第1条：考虑用静态工厂方法代替构造器

### 静态工厂方法相比构造器的优点

1. 有名称。
   考虑一下重载构造函数，用户常常不知道如何选用构造函数。但是如果使用静态工厂方法，就可以选取一个描述较准确的方法名，从而提高代码可读性。
   eg: `BigInteger(int , int , Random)` 返回的可能为素数，而`BigInteger.probablePrime`这样的静态工厂方法显然更为直观。

2. 不必在每次调用它们的时候都创建一个新对象。
   可以使用预先构建好的实例，或者将构建好的实例缓存起来，进行重复利用，从而避免不必要的重复对象。
   典型场景：`Boolean.valueOf(boolean)` 从来不创建对象。这种方法类似于享元模式(Flyweight)。

   可以为重复的调用返回相同对象，这样有助于类总能严格控制在某个时刻哪些实例应该存在。这种类被称作实例受控的类。
   典型场景：单例模式(Sigleton)

3. 它们可以返回原返回类型的任何子类型的对象。
   这样，我们在选择返回对象的类时就有了更大的灵活性。
   API可以返回对象，同时又不会使对象的类变成公有的。这种方式隐藏实现类会使API变得非常简洁。
   范例
```java
public interface Service {
	// 在这里指定方法
}

public interface Provider {
	Service newService();
}

public class Services {
	private Services() {
	} // 私有构造器，阻止实例化 (Item 4)

	// 根据名称映射Provider的实现类
	private static final Map<String, Provider> providers = new ConcurrentHashMap<String, Provider>();
	public static final String DEFAULT_PROVIDER_NAME = "<def>";

	// Provider 注册接口
	public static void registerDefaultProvider(Provider p) {
		registerProvider(DEFAULT_PROVIDER_NAME, p);
	}

	public static void registerProvider(String name, Provider p) {
		providers.put(name, p);
	}

	// Service 访问接口
	public static Service newInstance() {
		return newInstance(DEFAULT_PROVIDER_NAME);
	}

	public static Service newInstance(String name) {
		Provider p = providers.get(name);
		if (p == null)
			throw new IllegalArgumentException(
					"No provider registered with name: " + name);
		return p.newService();
	}
}
```

4. 在创建参数化类型实例的时候，它们使代码变得更加简洁。
   在调用泛型化类的构造器时，即使类型参数很明显，也需要连续两次提供类型参数。书中举的例子是：

```java
Map<String, List<String>> m = new HashMap<String, List<String>>();
```

   注：本人使用的JDK是1.8，并不需要连续两次指明类型参数。

### 静态工厂方法相比构造器的缺点

1. 类如果不含公有的或者受保护的构造器，就不能被子类化。
2. 它们与其他的静态方法实际上没有任何区别。

### 静态工厂方法的惯用名称

- valueOf
- of
- getInstance
- newInstance
- getType
- newType


## 第2条：遇到多个构造器参数时要考虑用构建器

静态工厂和构造器有个共同的局限性：它们都不能很好地扩展到大量的可选参数。

```java
public class NutritionFacts {
	private final int servingSize; // (mL) required
	private final int servings; // (per container) required
	private final int calories; // optional
	private final int fat; // (g) optional
	private final int sodium; // (mg) optional
	private final int carbohydrate; // (g) optional

	public NutritionFacts(int servingSize, int servings) {
		this(servingSize, servings, 0);
	}

	public NutritionFacts(int servingSize, int servings, int calories) {
		this(servingSize, servings, calories, 0);
	}

	public NutritionFacts(int servingSize, int servings, int calories, int fat) {
		this(servingSize, servings, calories, fat, 0);
	}

	public NutritionFacts(int servingSize, int servings, int calories, int fat,
			int sodium) {
		this(servingSize, servings, calories, fat, sodium, 0);
	}

	public NutritionFacts(int servingSize, int servings, int calories, int fat,
			int sodium, int carbohydrate) {
		this.servingSize = servingSize;
		this.servings = servings;
		this.calories = calories;
		this.fat = fat;
		this.sodium = sodium;
		this.carbohydrate = carbohydrate;
	}

	public static void main(String[] args) {
		NutritionFacts cocaCola = new NutritionFacts(240, 8, 100, 0, 35, 27);
	}
}
```

重点：构造器模式(Builder)

## 第3条：用私有构造器或者枚举类型强化Singleton属性

## 第4条：通过私有构造器强化不可实例化的能力

## 第5条：避免创建不必要的对象

一般来说，最好能重用对象而不是在每次需要的时候就创建一个相同功能的新对象。重用方式既快速、又流行。如果对象是不可变的（immutable），它就始终可以被重用。

对于同时提供了静态工厂方法和构造器的不可变类，通常可以使用静态工厂方法而不是构造器，以避免创建不必要的对象。

要优先使用基本类型而不是装箱基本类型，要当心无意识的自动装箱。因为性能会变差。

## 第6条：消除过期的对象引用

过期引用，是指永远也不会再被解除的引用。

如果一个对象引用被无意识地保留起来了，那么，垃圾回收机制不仅不会处理这个对象，而且也不会处理被这个对象所引用的所有其他对象。

一旦对象引用已经过期，只需清空这些引用即可。

## 第7条：避免使用终结方法

> 终结方法（finalizer）通常是不可预测的，也是很危险的，一般情况下是不必要的。使用终结方法会导致行为不稳定、降低性能，以及可移植性问题。
