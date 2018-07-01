## 什么是注解（Annotation）

Annotation（注解）就是 Java 提供了一种元程序中的元素关联任何信息和着任何元数据（metadata）的途径和方法。

`java.lang.annotation.Annotation` 是一个接口，程序可以通过反射来获取指定程序元素的注解对象，然后通过注解对象来获取注解里面的元数据。

注解是 Java 5 及以后版本引入的。它可以用于创建文档，跟踪代码中的依赖性，甚至执行基本编译时检查。从某些方面看，annotation 就像修饰符一样被使用，并应用于包、类型、构造方法、方法、成员变量、参数、本地变量的声明中。这些信息被存储在注解的“name=value”结构对中。

注解的成员在注解类型中以无参数的方法的形式被声明。其方法名和返回值定义了该成员的名字和类型。在此有一个特定的默认语法：允许声明任何注解成员的默认值：一个注解可以将 name=value 对作为没有定义默认值的注解成员的值，当然也可以使用 name=value 对来覆盖其它成员默认值。这一点有些近似类的继承特性，父类的构造函数可以作为子类的默认构造函数，但是也可以被子类覆盖。

注解能被用来为某个程序元素（类、方法、成员变量等）关联任何的信息。需要注意的是，这里存在着一个基本的规则：注解不能影响程序代码的执行，无论增加、删除注解，代码都始终如一的执行。另外，尽管一些注解通过 java 的反射 api 方法在运行时被访问，而 java 语言解释器在工作时忽略了这些注解。正是由于 java 虚拟机忽略了注解，导致了注解类型在代码中是“不起作用”的；只有通过某种配套的工具才会对注解类型中的信息进行访问和处理。本文中将涵盖标准的注解和元注解类型，陪伴这些 annotation 类型的工具是 java 编译器（当然要以某种特殊的方式处理它们）。

## 什么是 metadata（元数据）

元数据从 metadata 一词译来，就是“关于数据的数据”的意思。

元数据的功能作用有很多，比如：你可能用过 Javadoc 的注释自动生成文档。这就是元数据功能的一种。总的来说，元数据可以用来创建文档，跟踪代码的依赖性，执行编译时格式检查，代替已有的配置文件。如果要对于元数据的作用进行分类，目前还没有明确的定义，不过我们可以根据它所起的作用，大致可分为三类：

1.  编写文档：通过代码里标识的元数据生成文档

2.  代码分析：通过代码里标识的元数据对代码进行分析

3.  编译检查：通过代码里标识的元数据让编译器能实现基本的编译检查

在 Java 中元数据以标签的形式存在于 Java 代码中，元数据标签的存在并不影响程序代码的编译和执行，它只是被用来生成其它的文件或针在运行时知道被运行代码的描述信息。

综上所述：

1.  元数据以标签的形式存在于 Java 代码中。

2.  元数据描述的信息是类型安全的，即元数据内部的字段都是有明确类型的。

3.  元数据需要编译器之外的工具额外的处理用来生成其它的程序部件。

4.  元数据可以只存在于 Java 源代码级别，也可以存在于编译之后的 Class 文件内部。

## Annotation 和 Annotation 类型

### 注解

注解使用了在 java 5 所带来的新语法，它的行为十分类似 public、final 这样的修饰符。每个注解具有一个名字和成员个数>=0。每个注解的成员具有被称为 name=value 对的名字和值（就像 javabean 一样）， name=value 装载了注解 的信息。

### 注解类型

注解类型定义了注解的名字、类型、成员默认值。一个注解类型可以说是一个特殊的 java 接口，它的成员变量是受限制的，而声明注解类型时需要使用新语法。当我们通过 java 反射 api 访问注解时，返回值将是一个实现了该注解类型接口的对象，通过访问这个对象我们能方便的访问到其注解成员。后面的章节将提到在 java 5 的 `java.lang` 包里包含的 3 个标准注解类型。

## 注解的分类

根据注解参数的个数，我们可以将注解分为三类：

1.  标记注解

    一个没有成员定义的注解类型被称为标记注解。这种注解类型仅使用自身的存在与否来为我们提供信息。比如后面的系统注解 `@Override` 。

2.  单值注解

3.  完整注解

根据注解使用方法和用途，我们可以将注解分为三类：

1.  JDK 内置系统注解

2.  元注解

3.  自定义注解

### 内置系统注解

注解的语法比较简单，除了@符号的使用外，他基本与 Java 固有的语法一致。

JavaCore 中内置三个标准注解，定义在 `java.lang` 中：

1.  **@Override**：用于修饰此方法覆盖了父类的方法;
2.  **@Deprecated**：用于修饰已经过时的方法;
3.  **@SuppressWarnnings**：用于通知 java 编译器禁止特定的编译警告。

下面我们依次看看三个内置标准注解的作用和使用场景。

#### @Override

限定重写父类方法。

`@Override` 是一个标记注解类型，它被用作标注方法。它说明了被标注的方法重载了父类的方法，起到了断言的作用。如果我们使用了这种注解在一个没有覆盖父类方法的方法时，java 编译器将以一个编译错误来警示。这个注解常常在我们试图覆盖父类方法而确又写错了方法名时发挥威力。使用方法极其简单：在使用此注解时只要在被修饰的方法前面加上 `@Override` 即可。下面的代码是一个使用 `@Override` 修饰一个企图重载父类的 displayName()方法，而又存在拼写错误的实例：

```java
public class Fruit {

    public void displayName(){
        System.out.println("水果的名字是：*****");
    }
}

class Orange extends Fruit {
    @Override
    public void displayName(){
        System.out.println("水果的名字是：桔子");
    }
}

class Apple extends Fruit {
    @Override
    public void displayname(){
        System.out.println("水果的名字是：苹果");
    }
}
```

#### @Deprecated

标记已过时。

同样 `@Deprecated` 也是一个标记注解。当一个类型或者类型成员使用 `@Deprecated` 修饰的话，编译器将不鼓励使用这个被标注的程序元素。而且这种修饰具有一定的“延续性”：如果我们在代码中通过继承或者覆盖的方式使用了这个过时的类型或者成员，虽然继承或者覆盖后的类型或者成员并不是被声明为 `@Deprecated` ，但编译器仍然要报警。

注意： `@Deprecated` 这个注解类型和 javadoc 中的 `@deprecated` 这个 tag 是有区别的：前者是 java 编译器识别的；而后者是被 javadoc 工具所识别用来生成文档（包含程序成员为什么已经过时、它应当如何被禁止或者替代的描述）。

在 Java 5，java 编译器仍然象其从前版本那样寻找 `@deprecated` 这个 javadoc tag，并使用它们产生警告信息。但是这种状况将在后续版本中改变，我们应在现在就开始使用 `@Deprecated` 来修饰过时的方法而不是 `@deprecated` javadoc tag。

#### @SuppressWarnnings

抑制编译器警告。

`@SuppressWarnings` 被用于有选择的关闭编译器对类、方法、成员变量、变量初始化的警告。在 Java 5，sun 提供的 javac 编译器为我们提供了`-Xlint` 选项来使编译器对合法的程序代码提出警告，此种警告从某种程度上代表了程序错误。例如当我们使用一个 generic collection 类而又没有提供它的类型时，编译器将提示出"unchecked warning"的警告。通常当这种情况发生时，我们就需要查找引起警告的代码。如果它真的表示错误，我们就需要纠正它。例如如果警告信息表明我们代码中的 `switch` 语句没有覆盖所有可能的 `case`，那么我们就应增加一个默认的 `case` 来避免这种警告。

有时我们无法避免这种警告，例如，我们使用必须和非 generic 的旧代码交互的 generic collection 类时，我们不能避免这个 unchecked warning。此时 @`SuppressWarning` 就要派上用场了，在调用的方法前增加 `@SuppressWarnings` 修饰，告诉编译器停止对此方法的警告。

`@SuppressWarning` 不是一个标记注解。它有一个类型为 String[]的成员，这个成员的值为被禁止的警告名。对于 javac 编译器来讲，被 `-Xlint` 选项有效的警告名也同样对 `@SuppressWarings` 有效，同时编译器忽略掉无法识别的警告名。

实例如下：

```java
public class FruitService {

    @SuppressWarnings(value={ "rawtypes", "unchecked" })
    public static  List<Fruit> getFruitList(){
        List<Fruit> fruitList=new ArrayList();
        return fruitList;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static  List<Fruit> getFruit(){
        List<Fruit> fruitList=new ArrayList();
        return fruitList;
    }

    @SuppressWarnings("unused")
    public static void main(String[] args){
        List<String> strList=new ArrayList<String>();
    }
}
```

在这个例子中 `@SuppressWarnings` 注解类型只定义了一个单一的成员，所以只有一个简单的 value={...} 作为 name=value 对。又由于成员值是一个数组，故使用大括号来声明数组值。注意：我们可以在下面的情况中缩写注解：当注解只有单一成员，并成员命名为"value="。这时可以省去"value="。比如将上面方法 `getFruit()` 的 SuppressWarnings annotation 就是缩写的。

`@SuppressWarnings` 注解的常见参数值的简单说明：

| **Type**                 | **Descption**                                                                            |
| ------------------------ | ---------------------------------------------------------------------------------------- |
| all                      | to suppress all warnings                                                                 |
| boxing                   | to suppress warnings relative to boxing/unboxing operations                              |
| cast                     | to suppress warnings relative to cast operations                                         |
| dep-ann                  | to suppress warnings relative to deprecated annotation                                   |
| deprecation              | to suppress warnings relative to deprecation                                             |
| fallthrough              | to suppress warnings relative to missing breaks in switch statements                     |
| finally                  | to suppress warnings relative to finally block that don’t return                         |
| hiding                   | to suppress warnings relative to locals that hide variable                               |
| incomplete-switch        | to suppress warnings relative to missing entries in a switch statement (enum case)       |
| nls                      | to suppress warnings relative to non-nls string literals                                 |
| null                     | to suppress warnings relative to null analysis                                           |
| rawtypes                 | to suppress warnings relative to un-specific types when using generics on class params   |
| restriction              | to suppress warnings relative to usage of discouraged or forbidden references            |
| serial                   | to suppress warnings relative to missing serialVersionUID field for a serializable class |
| static-access            | to suppress warnings relative to incorrect static access                                 |
| synthetic-access         | to suppress warnings relative to unoptimized access from inner classes                   |
| unchecked                | to suppress warnings relative to unchecked operations                                    |
| unqualified-field-access | to suppress warnings relative to field access unqualified                                |
| unused                   | to suppress warnings relative to unused code                                             |

### 元注解

元注解的作用就是负责注解其它注解。

Java 5 定义了 4 个标准的元注解类型，它们被用来提供对其它注解类型作说明。Java 5 定义的元注解：

* @Target
* @Retention
* @Documented
* @Inherited

这些类型和它们所支持的类在 `java.lang.annotation` 包中可以找到。下面我们看一下每个元注解的作用和相应分参数的使用说明。

#### @Target

`@Target` 说明了注解所修饰的对象范围。注解可被用于 packages、types（类、接口、枚举、Annotation 类型）、类型成员（方法、构造方法、成员变量、枚举值）、方法参数和本地变量（如循环变量、catch 参数）。在注解类型的声明中使用了 `@target` 可以更加明确其修饰的目标。

**作用：用于描述注解的使用范围（即：被描述的注解可以用在什么地方）**

取值（ElementType）有：

* **CONSTRUCTOR**：用于描述构造器
* **FIELD**：用于描述域
* **LOCAL_VARIABLE**：用于描述局部变量
* **METHOD**：用于描述方法
* **PACKAGE**：用于描述包
* **PARAMETER**：用于描述参数
* **TYPE**：用于描述类、接口(包括注解类型) 或 enum 声明

```java
@Target(ElementType.TYPE)
public @interface Table {
    /**
     * 数据表名称注解，默认值为类名称
     * @return
     */
    public String tableName() default "className";
}

@Target(ElementType.FIELD)
public @interface NoDBColumn {
}
```

#### @Retention

`@Retention` 定义了注解被保留的时间长短：某些注解仅出现在源代码中，而被编译器丢弃；而另一些却被编译在 class 文件中；编译在 class 文件中的注解可能会被虚拟机忽略，而另一些在 class 被装载时将被读取（请注意并不影响 class 的执行，因为注解与 class 在使用上是被分离的）。使用这个元注解可以限制注解的“生命周期”。

作用：表示需要在什么级别保存该注释信息，用于描述注解的生命周期（即：被描述的注解在什么范围内有效）

取值（RetentionPoicy）有：

* **SOURCE**：在源文件中有效（即源文件保留）

* **CLASS**：在 class 文件中有效（即 class 保留）

* **RUNTIME**：在运行时有效（即运行时保留）

`@Retention` 类型有唯一的 value 作为成员，它的取值来自 `java.lang.annotation.RetentionPolicy` 的枚举类型值。具体实例如下：

```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    public String name() default "fieldName";
    public String setFuncName() default "setField";
    public String getFuncName() default "getField";
    public boolean defaultDBValue() default false;
}
```

#### @Documented

`@Documented` 用于描述注解应该被作为被标注的程序成员的公共 API，因此可以被例如 javadoc 此类的工具文档化。`@Documented` 是一个标记注解，没有成员。

```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
    public String name() default "fieldName";
    public String setFuncName() default "setField";
    public String getFuncName() default "getField";
    public boolean defaultDBValue() default false;
}
```

#### @Inherited

`@Inherited` 元注解是一个标记注解，`@Inherited` 阐述了某个被标注的类型是被继承的。如果一个使用了 `@Inherited` 修饰的注解类型被用于一个 class，则这个 annotation 将被用于该 class 的子类。

注意：`@Inherited` 注解类型是被标注过的 class 的子类所继承。类并不从它所实现的接口继承注解，方法并不从它所重载的方法继承注解。

当 `@Inherited` 类型标注的注解的 `@Retention` 是 `RetentionPolicy.RUNTIME`，则反射 API 增强了这种继承性。如果我们使用 `java.lang.reflect` 去查询一个 `@Inherited` 类型的注解时，反射代码检查将展开工作：检查 class 和其父类，直到发现指定的注解类型被发现，或者到达类继承结构的顶层。

```java
@Inherited
public @interface Greeting {
    public enum FontColor{ BULE,RED,GREEN};
    String name();
    FontColor fontColor() default FontColor.GREEN;
}
```

## 自定义注解

使用 `@interface` 自定义注解时，自动继承了 `java.lang.annotation.Annotation` 接口，由编译程序自动完成其他细节。在定义注解时，不能继承其他的注解或接口。`@interface` 用来声明一个注解，其中的每一个方法实际上是声明了一个配置参数。方法的名称就是参数的名称，返回值类型就是参数的类型（返回值类型只能是基本类型、Class、String、enum）。可以通过 `default` 来声明参数的默认值。

#### 定义注解格式

```java
public @interface 注解名 {定义体}
```

#### 注解参数的可支持数据类型

* 所有基本数据类型（byte、char、short、int、long、float、double、boolean）

* String 类型

* Class 类型

* enum 类型

* Annotation 类型

* 以上所有类型的数组

注解类型里面的参数该怎么设定:

* 只能用 public 或默认（default）这两个访问权修饰。

  例如：`String value();` 这里把方法设为 default 默认类型。

* 参数成员只能用基本类型 byte、char、short、int、long、float、double、boolean 八种基本数据类型和 String、Enum、Class、注解等数据类型，以及这一些类型的数组。例如：`String value();` 这里的参数成员就为 String。

* 如果只有一个参数成员，最好把参数名称设为"value"，后加小括号。例：下面的例子 FruitName 注解就只有一个参数成员。

简单的自定义注解和使用注解实例：

自定义注解

```java
// 只有一个参数成员的注解
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FruitName {
    String value() default "";
}

// 值为枚举值的注解
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FruitColor {
    public enum Color{ BULE,RED,GREEN};
    Color fruitColor() default Color.GREEN;
}
```

使用自定义注解

```java
public class Apple {

    @FruitName("Apple")
    private String appleName;

    @FruitColor(fruitColor=Color.RED)
    private String appleColor;

    public void setAppleColor(String appleColor) {
        this.appleColor = appleColor;
    }
    public String getAppleColor() {
        return appleColor;
    }

    public void setAppleName(String appleName) {
        this.appleName = appleName;
    }
    public String getAppleName() {
        return appleName;
    }

    public void displayName(){
        System.out.println("水果的名字是：苹果");
    }
}
```

#### 注解元素的默认值

注解元素必须有确定的值，要么在定义注解的默认值中指定，要么在使用注解时指定。非基本类型的注解元素的值不可为 null。因此, 使用空字符串或 0 作为默认值是一种常用的做法。这个约束使得处理器很难表现一个元素的存在或缺失的状态，因为每个注解的声明中，所有元素都存在，并且都具有相应的值，为了绕开这个约束，我们只能定义一些特殊的值，例如空字符串或者负数，一次表示某个元素不存在，在定义注解时，这已经成为一个习惯用法。例如：

```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FruitProvider {
    /**
     * 供应商编号
     * @return
     */
    public int id() default -1;

    /**
     * 供应商名称
     * @return
     */
    public String name() default "";

    /**
     * 供应商地址
     * @return
     */
    public String address() default "";
}
```

定义了注解，并在需要的时候给相关类，类属性加上注解信息，如果没有响应的注解信息处理流程，注解可以说是没有实用价值。如何让注解真真的发挥作用，主要就在于注解处理方法，下一步我们将学习注解信息的获取和处理！

## 注解处理器

如果没有用来读取注解的方法和工作，那么注解也就不会比注释更有用处了。使用注解的过程中，很重要的一部分就是创建于使用注解处理器。Java 5 扩展了反射机制的 API，以帮助程序员快速的构造自定义注解处理器。

**注解处理器类库 `java.lang.reflect.AnnotatedElement`**

Java 使用 `java.lang.annotation.Annotation` 接口来代表程序元素前面的注解，该接口是所有注解类型的父接口。除此之外，Java 新增了 `java.lang.reflect.AnnotatedElement` 接口，该接口代表程序中可以接受注解的程序元素，该接口主要有如下几个实现类：

* **Class**：类定义
* **Constructor**：构造器定义
* **Field**：累的成员变量定义
* **Method**：类的方法定义
* **Package**：类的包定义

`java.lang.reflect` 包下主要包含一些实现反射功能的工具类。实际上，`java.lang.reflect` 包所有提供的反射 API 扩充了读取运行时注解信息的能力。当一个注解类型被定义为运行时的注解后，该注解才能是运行时可见，当 class 文件被装载时被保存在 class 文件中的注解才会被虚拟机读取。
`AnnotatedElement` 接口是所有程序元素（Class、Method 和 Constructor）的父接口，所以程序通过反射获取了某个类的`AnnotatedElement` 对象之后，程序就可以调用该对象的如下四个个方法来访问注解信息：

1.  `<T extends Annotation> T getAnnotation(Class<T> annotationClass)` ：返回该程序元素上存在的、指定类型的注解，如果该类型注解不存在，则返回 null。
2.  `Annotation[] getAnnotations()` ：返回该程序元素上存在的所有注解。
3.  `boolean isAnnotationPresent(Class<?extends Annotation> annotationClass)` ：判断该程序元素上是否包含指定类型的注解，存在则返回 true，否则返回 false。
4.  `Annotation[] getDeclaredAnnotations()` ：返回直接存在于此元素上的所有注释。与此接口中的其他方法不同，该方法将忽略继承的注释。（如果没有注释直接存在于此元素上，则返回长度为零的一个数组。）该方法的调用者可以随意修改返回的数组；这不会对其他调用者返回的数组产生任何影响。

一个简单的注解处理器：

```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FruitName {
    String value() default "";
}

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FruitColor {

    public enum Color{ BULE,RED,GREEN};

    Color fruitColor() default Color.GREEN;
}


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FruitProvider {

    public int id() default -1;

    public String name() default "";

    public String address() default "";
}

/***********注解使用***************/
public class Apple {

    @FruitName("Apple")
    private String appleName;

    @FruitColor(fruitColor=Color.RED)
    private String appleColor;

    @FruitProvider(id=1,name="陕西红富士集团",address="陕西省西安市延安路89号红富士大厦")
    private String appleProvider;

    public void setAppleColor(String appleColor) {
        this.appleColor = appleColor;
    }
    public String getAppleColor() {
        return appleColor;
    }

    public void setAppleName(String appleName) {
        this.appleName = appleName;
    }
    public String getAppleName() {
        return appleName;
    }

    public void setAppleProvider(String appleProvider) {
        this.appleProvider = appleProvider;
    }
    public String getAppleProvider() {
        return appleProvider;
    }

    public void displayName(){
        System.out.println("水果的名字是：苹果");
    }
}

/***********注解处理器***************/
public class FruitInfoUtil {
    public static void getFruitInfo(Class<?> clazz){

        String strFruitName=" 水果名称：";
        String strFruitColor=" 水果颜色：";
        String strFruitProvicer="供应商信息：";

        Field[] fields = clazz.getDeclaredFields();

        for(Field field :fields){
            if(field.isAnnotationPresent(FruitName.class)){
                FruitName fruitName = (FruitName) field.getAnnotation(FruitName.class);
                strFruitName=strFruitName+fruitName.value();
                System.out.println(strFruitName);
            }
            else if(field.isAnnotationPresent(FruitColor.class)){
                FruitColor fruitColor= (FruitColor) field.getAnnotation(FruitColor.class);
                strFruitColor=strFruitColor+fruitColor.fruitColor().toString();
                System.out.println(strFruitColor);
            }
            else if(field.isAnnotationPresent(FruitProvider.class)){
                FruitProvider fruitProvider= (FruitProvider) field.getAnnotation(FruitProvider.class);
                strFruitProvicer=" 供应商编号："+fruitProvider.id()+" 供应商名称："+fruitProvider.name()+" 供应商地址："+fruitProvider.address();
                System.out.println(strFruitProvicer);
            }
        }
    }
}

/***********输出结果***************/
public class FruitRun {
    public static void main(String[] args) {
        FruitInfoUtil.getFruitInfo(Apple.class);
    }
}

====================================
 水果名称：Apple
 水果颜色：RED
 供应商编号：1 供应商名称：陕西红富士集团 供应商地址：陕西省西安市延安路89号红富士大厦
```

## 资料

* [注解（Annotation）基本概念](http://www.cnblogs.com/peida/archive/2013/04/23/3036035.html) by peida

* [注解（Annotation）自定义注解入门](http://www.cnblogs.com/peida/archive/2013/04/24/3036689.html) by peida

* [注解（Annotation）注解处理器](http://www.cnblogs.com/peida/archive/2013/04/26/3038503.html) by peida
