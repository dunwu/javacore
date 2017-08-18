# Chapter2 创建、销毁对象

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


### 第2条：遇到多个构造器参数时要考虑用构建器

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


第3条：用私有构造器或者枚举类型强化Singleton属性
第4条：通过私有构造器强化不可实例化的能力
第5条：避免创建不必要的对象
第6条：消除过期的对象引用
第7条：避免使用终结方法


# 对于所有对象都通用的方法

第8条：覆盖equals时请遵守通用约定
第9条：覆盖equals时总要覆盖hashCode
第10条：始终要覆盖toString
第11条：谨慎地覆盖clone
第12条：考虑实现Comparable接口


# 类和接口

第13条：使类和成员的可访问性最小化
第14条：在公有类中使用访问方法而非公有域
第15条：使可变性最小化
第16条：复合优先于继承
第17条：要么为继承而设计，并提供文档说明，要么就禁止继承
第18条：接口优于抽象类
第19条：接口只用于定义类型
第20条：类层次优于标签类
第21条：用函数对象表示策略
第22条：优先考虑静态成员类


# 泛型

第23条：请不要在新代码中使用原生态类型
第24条：消除非受检警告
第25条：列表优先于数组
第26条：优先考虑泛型
第27条：优先考虑泛型方法
第28条：利用有限制通配符来提升API的灵活性
第29条：优先考虑类型安全的异构容器


# 枚举和注解

第30条：用enum代替int常量
第31条：用实例域代替序数
第32条：用EnumSet代替位域
第33条：用EnumMap代替序数索引
第34条：用接口模拟可伸缩的枚举
第35条：注解优先于命名模式
第36条：坚持使用Override注解
第37条：用标记接口定义类型

# 方法

第38条：检查参数的有效性
第39条：必要时进行保护性拷贝
第40条：谨慎设计方法签名
第41条：慎用重载
第42条：慎用可变参数
第43条：返回零长度的数组或者集合，而不是：null
第44条：为所有导出的API元素编写文档注释


# 通用程序设计

第45条：将局部变量的作用域最小化
第46条：for-each循环优先于传统的for循环
第47条：了解和使用类库
第48条：如果需要精确的答案，请避免使用float和double
第49条：基本类型优先于装箱基本类型
第50条：如果其他类型更适合，则尽量避免使用字符串
第51条：当心字符串连接的性能
第52条：通过接口引用对象
第53条：接口优先于反射机制
第54条：谨慎地使用本地方法
第55条：谨慎地进行优化
第56条：遵守普遍接受的命名惯例


# 异常

第57条：只针对异常的情况才使用异常
第58条：对可恢复的情况使用受检异常，对编程错误使用运行时异常
第59条：避免不必要地使用受检的异常
第60条：优先使用标准的异常
第61条：抛出与抽象相对应的异常
第62条：每个方法抛出的异常都要有文档
第63条：在细节消息中包含能捕获失败的信息
第64条：努力使失败保持原子性
第65条：不要忽略异常


# 并发

第66条：同步访问共享的可变数据
第67条：避免过度同步
第68条：executor和task优先干线程
第69条：并发工具优先于wait和notify
第70条：线程安全性的文档化
第71条：慎用延迟初始化
第72条：不要依赖于线程调度器
第73条：避免使用线程组


# 序列化

第74条：谨慎地实现Serializable接口
第75条：考虑使用自定义的序列化形式
第76条：保护性地编写readObject方法
第77条：对于实例控制，枚举类型优先于readResolve
第78条：考虑用序列化代理代替序列化实例

