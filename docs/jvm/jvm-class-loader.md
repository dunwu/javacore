# 类加载机制

> :notebook: 本文已归档到：「[javacore](https://github.com/dunwu/javacore)」

## 类文件结构

Class 文件是一组以 8 位字节为基础单位的二进制流。

整个 Class 文件本质上就是一张表，由下表中的数据项组成。

<div align="center"><img src="https://gitee.com/turnon/images/raw/master/snap/1561452004933.png"/></div>

### 魔数与 Class 文件的版本

每个 Class 文件的头 4 个字节称为魔数（magic number），它的唯一作用是确定这个文件是否为一个能被虚拟机接收的 Class 文件。

紧接着魔数的 4 个字节存储的是 Class 文件的版本号：第 5 和第 6 个字节是次版本号（Minor Version）；第 7 和第 8 个字节是主版本号（Major Version）。

### 常量池

常量池主要存放两类常量：

- **字面量** - 如文本字符串、声明为 final 的常量值。
- **符号引用**
  - 类和接口的全限定名
  - 字段的名称和描述符
  - 方法的名称和描述符

<div align="center"><img src="https://gitee.com/turnon/images/raw/master/snap/1561473159265.png"/></div>

### 访问标志

常量池之后，紧接着的两个字节代表访问标志，这个标志用于识别一些类或者接口的访问信息，包括：这个 Class 是类还是接口；是否定义为 public 类型；是否定义为 abstract 类型；如果是类的话，是否被声明为 final 等。

<div align="center"><img src="https://gitee.com/turnon/images/raw/master/snap/1561473228816.png"/></div>

### 类索引、父类索引和接口索引集合

Class 文件由类索引、父类索引和接口索引集合这 3 项数据来确定这个类的继承关系。

### 字段表集合

字段表用于描述接口或者类中声明的变量。字段包括类级变量以及实例级变量，但不包括在方法内部声明的局部变量。包含的信息如下：

- 字段访问标志

<div align="center"><img src="https://gitee.com/turnon/images/raw/master/snap/1561473275089.png"/></div>

- 名称索引
- 描述符索引

<div align="center"><img src="https://gitee.com/turnon/images/raw/master/snap/1561473423673.png"/></div>

- 属性计数器
- 属性集合

### 方法表集合

方法表包含的信息如下：

- 方法访问标志

<div align="center"><img src="https://gitee.com/turnon/images/raw/master/snap/1561473522027.png"/></div>

- 并发可见性（volatile）
- 能否被序列化（transient）
- 字段数据类型（基本类型、对象、数组）
- 名称索引
- 描述符索引
- 属性计数器
- 属性集合

### 属性表集合

## 类加载机制

> 类是在运行期间动态加载的。

### 类生命周期

<div align="center">
<img src="https://gitee.com/turnon/images/raw/master/images/java/jvm/jmm-类加载-生命周期.jpg" />
</div>

Java 类的完整生命周期包括以下几个阶段：

- **加载（Loading）**
- **链接（Linking）**
  - **验证（Verification）**
  - **准备（Preparation）**
  - **解析（Resolution）**
- **初始化（Initialization）**
- **使用（Using）**
- **卸载（Unloading）**

加载、验证、准备、初始化和卸载这 5 个阶段的顺序是确定的，类的加载过程必须按照这种顺序按部就班地开始。而解析过程在某些情况下可以在初始化阶段之后再开始，这是为了支持 Java 的动态绑定。

### 类加载过程

类加载过程是指加载、验证、准备、解析和初始化这 5 个阶段。

#### （一）加载

加载是类加载的一个阶段，注意不要混淆。

加载过程完成以下三件事：

1. 通过一个类的全限定名来获取定义此类的二进制字节流。
2. 将这个字节流所代表的静态存储结构转化为方法区的运行时存储结构。
3. 在内存中生成一个代表这个类的 Class 对象，作为方法区这个类的各种数据的访问入口。

其中二进制字节流可以从以下方式中获取：

- 从 ZIP 包读取，这很常见，最终成为日后 JAR、EAR、WAR 格式的基础。
- 从网络中获取，这种场景最典型的应用是 Applet。
- 运行时计算生成，这种场景使用得最多得就是动态代理技术，在 `java.lang.reflect.Proxy` 中，就是用了 `ProxyGenerator.generateProxyClass` 的代理类的二进制字节流。
- 由其他文件生成，典型场景是 JSP 应用，即由 JSP 文件生成对应的 Class 类。
- 从数据库读取，这种场景相对少见，例如有些中间件服务器（如 SAP Netweaver）可以选择把程序安装到数据库中来完成程序代码在集群间的分发。
  ...

#### （二）验证

验证是链接阶段的第一步，它的目标是**确保 Class 文件的字节流中包含的信息符合当前虚拟机的要求，并且不会危害虚拟机自身的安全**。

- **文件格式验证** - 验证字节流是否符合 Class 文件格式的规范，并且能被当前版本的虚拟机处理。
- **元数据验证** - 对字节码描述的信息进行语义分析，以保证其描述的信息符合 Java 语言规范的要求。
- **字节码验证** - 通过数据流和控制流分析，确保程序语义是合法、符合逻辑的。
- **符号引用验证** - 发生在虚拟机将符号引用转换为直接引用的时候，对类自身以外（常量池中的各种符号引用）的信息进行匹配性校验。

#### （三）准备

**类变量是被 static 修饰的变量，准备阶段为类变量分配内存并设置初始值，使用的是方法区的内存。**

实例变量不会在这阶段分配内存，它将会在对象实例化时随着对象一起分配在 Java 堆中。（实例化不是类加载的一个过程，类加载发生在所有实例化操作之前，并且类加载只进行一次，实例化可以进行多次）

初始值一般为 0 值，例如下面的类变量 value 被初始化为 0 而不是 123。

```java
public static int value = 123;
```

如果类变量是常量，那么会按照表达式来进行初始化，而不是赋值为 0。

```java
public static final int value = 123;
```

#### （四）解析

解析阶段目标是**将常量池的符号引用替换为直接引用的过程**。

- **符号引用（Symbolic References）** - 符号引用以一组符号来描述所引用的目标，符号可以是任何形式的字面量，只要使用时能无歧义地定位到目标即可。
- **直接引用（Direct Reference）** - 直接引用可以是直接指向目标的指针、相对偏移量或是一个能间接定位到目标的句柄。

#### （五）初始化

初始化阶段才真正开始执行类中的定义的 Java 程序代码。**初始化阶段即虚拟机执行类构造器 `<clinit>()` 方法的过程**。

在准备阶段，类变量已经赋过一次系统要求的初始值，而在初始化阶段，根据程序员通过程序制定的主观计划去初始化类变量和其它资源。

`<clinit>()` 方法具有以下特点：

- 是由编译器自动收集类中所有类变量的赋值动作和静态语句块（static{} 块）中的语句合并产生的，编译器收集的顺序由语句在源文件中出现的顺序决定。特别注意的是，静态语句块只能访问到定义在它之前的类变量，定义在它之后的类变量只能赋值，不能访问。例如以下代码：

```java
public class Test {
    static {
        i = 0;                // 给变量赋值可以正常编译通过
        System.out.print(i);  // 这句编译器会提示“非法向前引用”
    }
    static int i = 1;
}
```

- 与类的构造函数（或者说实例构造器 &lt;init>()）不同，不需要显式的调用父类的构造器。虚拟机会自动保证在子类的 &lt;clinit>() 方法运行之前，父类的 &lt;clinit>() 方法已经执行结束。因此虚拟机中第一个执行 &lt;clinit>() 方法的类肯定为 java.lang.Object。
- 由于父类的 &lt;clinit>() 方法先执行，也就意味着父类中定义的静态语句块要优于子类的变量赋值操作。例如以下代码：

```java
static class Parent {
    public static int A = 1;
    static {
        A = 2;
    }
}

static class Sub extends Parent {
    public static int B = A;
}

public static void main(String[] args) {
     System.out.println(Sub.B);  // 输出结果是父类中的静态变量 A 的值，也就是 2。
}
```

- &lt;clinit>() 方法对于类或接口不是必须的，如果一个类中不包含静态语句块，也没有对类变量的赋值操作，编译器可以不为该类生成 &lt;clinit>() 方法。
- 接口中不可以使用静态语句块，但仍然有类变量初始化的赋值操作，因此接口与类一样都会生成 &lt;clinit>() 方法。但接口与类不同的是，执行接口的 &lt;clinit>() 方法不需要先执行父接口的 &lt;clinit>() 方法。只有当父接口中定义的变量使用时，父接口才会初始化。另外，接口的实现类在初始化时也一样不会执行接口的 &lt;clinit>() 方法。
- 虚拟机会保证一个类的 &lt;clinit>() 方法在多线程环境下被正确的加锁和同步，如果多个线程同时初始化一个类，只会有一个线程执行这个类的 &lt;clinit>() 方法，其它线程都会阻塞等待，直到活动线程执行 &lt;clinit>() 方法完毕。如果在一个类的 &lt;clinit>() 方法中有耗时的操作，就可能造成多个线程阻塞，在实际过程中此种阻塞很隐蔽。

##### 类初始化时机

**主动引用**

虚拟机规范中并没有强制约束何时进行加载，但是规范严格规定了有且只有下列五种情况必须对类进行初始化（加载、验证、准备都会随着发生）：

1. 遇到 new、getstatic、putstatic、invokestatic 这四条字节码指令时，如果类没有进行过初始化，则必须先触发其初始化。最常见的生成这 4 条指令的场景是：使用 new 关键字实例化对象的时候；读取或设置一个类的静态字段（被 final 修饰、已在编译期把结果放入常量池的静态字段除外）的时候；以及调用一个类的静态方法的时候。
2. 使用 java.lang.reflect 包的方法对类进行反射调用的时候，如果类没有进行初始化，则需要先触发其初始化。
3. 当初始化一个类的时候，如果发现其父类还没有进行过初始化，则需要先触发其父类的初始化。
4. 当虚拟机启动时，用户需要指定一个要执行的主类（包含 main() 方法的那个类），虚拟机会先初始化这个主类；
5. 当使用 JDK 1.7 的动态语言支持时，如果一个 java.lang.invoke.MethodHandle 实例最后的解析结果为 REF_getStatic, REF_putStatic, REF_invokeStatic 的方法句柄，并且这个方法句柄所对应的类没有进行过初始化，则需要先触发其初始化；

**被动引用**

以上 5 种场景中的行为称为对一个类进行主动引用。除此之外，所有引用类的方式都不会触发初始化，称为被动引用。被动引用的常见例子包括：

- 通过子类引用父类的静态字段，不会导致子类初始化。

```java
System.out.println(SubClass.value); // value 字段在 SuperClass 中定义
```

- 通过数组定义来引用类，不会触发此类的初始化。该过程会对数组类进行初始化，数组类是一个由虚拟机自动生成的、直接继承自 Object 的子类，其中包含了数组的属性和方法。

```java
SuperClass[] sca = new SuperClass[10];
```

- 常量在编译阶段会存入调用类的常量池中，本质上并没有直接引用到定义常量的类，因此不会触发定义常量的类的初始化。

```java
System.out.println(ConstClass.HELLOWORLD);
```

## 类加载器

实现类的加载动作。在 Java 虚拟机外部实现，以便让应用程序自己决定如何去获取所需要的类。

### 类与类加载器

两个类相等：类本身相等，并且使用同一个类加载器进行加载。这是因为每一个类加载器都拥有一个独立的类名称空间。

这里的相等，包括类的 Class 对象的 equals() 方法、isAssignableFrom() 方法、isInstance() 方法的返回结果为 true，也包括使用 instanceof 关键字做对象所属关系判定结果为 true。

### 类加载器分类

- **启动类加载器（Bootstrap ClassLoader）** - 此类加载器负责将存放在 `<JAVA_HOME>\lib` 目录中的，或者被 `-Xbootclasspath` 参数所指定的路径中的，并且是虚拟机识别的（仅按照文件名识别，如 rt.jar，名字不符合的类库即使放在 lib 目录中也不会被加载）类库加载到虚拟机内存中。启动类加载器无法被 Java 程序直接引用，用户在编写自定义类加载器时，如果需要把加载请求委派给启动类加载器，直接使用 null 代替即可。

- **扩展类加载器（Extension ClassLoader）** - 这个类加载器是由 `ExtClassLoader(sun.misc.Launcher\$ExtClassLoader)`实现的。它负责将 `<JAVA_HOME>\lib\ext` 或者被 java.ext.dir 系统变量所指定路径中的所有类库加载到内存中，开发者可以直接使用扩展类加载器。

- **应用程序类加载器（Application ClassLoader）** - 这个类加载器是由 `AppClassLoader（sun.misc.Launcher\$AppClassLoader）`实现的。由于这个类加载器是 `ClassLoader` 中的 `getSystemClassLoader()` 方法的返回值，因此一般称为系统类加载器。它负责加载用户类路径（ClassPath）上所指定的类库，开发者可以直接使用这个类加载器，如果应用程序中没有自定义过自己的类加载器，一般情况下这个就是程序中默认的类加载器。

### 双亲委派模型

应用程序都是由以上三种类加载器相互配合进行加载的，如果有必要，还可以加入自己定义的类加载器。

下图展示的类加载器之间的层次关系，称为类加载器的**双亲委派模型（Parents Delegation Model）**。**该模型要求除了顶层的启动类加载器外，其余的类加载器都应有自己的父类加载器**。**这里类加载器之间的父子关系一般通过组合（Composition）关系来实现，而不是通过继承（Inheritance）的关系实现**。

<div align="center">
<img src="https://gitee.com/turnon/images/raw/master/images/java/jvm/jmm-类加载-双亲委派.png" width="500" />
</div>

**（一）工作过程**

一个类加载器首先将类加载请求传送到父类加载器，只有当父类加载器无法完成类加载请求时才尝试加载。

**（二）好处**

**使得 Java 类随着它的类加载器一起具有一种带有优先级的层次关系**，从而使得基础类得到统一。

例如 `java.lang.Object` 存放在 rt.jar 中，如果编写另外一个 `java.lang.Object` 的类并放到 ClassPath 中，程序可以编译通过。因为双亲委派模型的存在，所以在 rt.jar 中的 Object 比在 ClassPath 中的 Object 优先级更高，因为 rt.jar 中的 Object 使用的是启动类加载器，而 ClassPath 中的 Object 使用的是应用程序类加载器。正因为 rt.jar 中的 Object 优先级更高，因为程序中所有的 Object 都是这个 Object。

**（三）实现**

以下是抽象类 `java.lang.ClassLoader` 的代码片段，其中的 `loadClass()` 方法运行过程如下：

- 先检查类是否已经加载过，如果没有则让父类加载器去加载。
- 当父类加载器加载失败时抛出 `ClassNotFoundException`，此时尝试自己去加载。

```java
public abstract class ClassLoader {
    // The parent class loader for delegation
    private final ClassLoader parent;

    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return loadClass(name, false);
    }

    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            // First, check if the class has already been loaded
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                try {
                    if (parent != null) {
                        c = parent.loadClass(name, false);
                    } else {
                        c = findBootstrapClassOrNull(name);
                    }
                } catch (ClassNotFoundException e) {
                    // ClassNotFoundException thrown if class not found
                    // from the non-null parent class loader
                }

                if (c == null) {
                    // If still not found, then invoke findClass in order
                    // to find the class.
                    c = findClass(name);
                }
            }
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        throw new ClassNotFoundException(name);
    }
}
```

### 自定义类加载器实现

FileSystemClassLoader 是自定义类加载器，继承自 `java.lang.ClassLoader`，用于加载文件系统上的类。它首先根据类的全名在文件系统上查找类的字节代码文件（.class 文件），然后读取该文件内容，最后通过 defineClass() 方法来把这些字节代码转换成 java.lang.Class 类的实例。

`java.lang.ClassLoader` 类的方法 `loadClass()` 实现了双亲委派模型的逻辑，因此自定义类加载器一般不去重写它，而是通过重写 `findClass()` 方法。

```java
public class FileSystemClassLoader extends ClassLoader {

    private String rootDir;

    public FileSystemClassLoader(String rootDir) {
        this.rootDir = rootDir;
    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classData = getClassData(name);
        if (classData == null) {
            throw new ClassNotFoundException();
        } else {
            return defineClass(name, classData, 0, classData.length);
        }
    }

    private byte[] getClassData(String className) {
        String path = classNameToPath(className);
        try {
            InputStream ins = new FileInputStream(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int bufferSize = 4096;
            byte[] buffer = new byte[bufferSize];
            int bytesNumRead;
            while ((bytesNumRead = ins.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesNumRead);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String classNameToPath(String className) {
        return rootDir + File.separatorChar
                + className.replace('.', File.separatorChar) + ".class";
    }
}
```

## 参考资料

- [深入理解 Java 虚拟机：JVM 高级特性与最佳实践（第 2 版）](https://item.jd.com/11252778.html)
- [从表到里学习 JVM 实现](https://www.douban.com/doulist/2545443/)
