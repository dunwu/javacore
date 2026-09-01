---
title: 深入理解 Java 数组
date: 2019-05-06 15:02:02
order: 05
categories:
  - Java
  - JavaCore
  - 基础
tags:
  - Java
  - JavaCore
  - 数组
permalink: /pages/f0dba158/
---

# 深入理解 Java 数组

## 简介

### 数组的特性

数组对于每一门编程语言来说都是重要的数据结构之一，当然不同语言对数组的实现及处理也不尽相同。几乎所有程序设计语言都支持数组。

数组代表一系列对象或者基本数据类型，所有相同的类型都封装到一起，采用一个统一的标识符名称。

**数组的定义和使用需要通过方括号 `[]`。**

> **Java 中，数组是一种引用类型。**
>
> **Java 中，数组是用来存储固定大小的同类型元素。**

### 数组和容器

Java 中，既然有了强大的容器，是不是就不需要数组了？

答案是不。

诚然，大多数情况下，应该选择容器存储数据。

但是，数组也不是毫无是处：

- Java 中，数组是一种效率最高的存储和随机访问对象引用序列的方式。**数组的效率要高于容器**（如 `ArrayList`）。
- **数组可以持有值类型，而容器则不能**（这时，就必须用到包装类）。

### Java 数组的本质是对象

**Java 数组的本质是对象**。它具有 Java 中其他对象的一些基本特点：封装了一些数据，可以访问属性，也可以调用方法。所以，数组是对象。

如果有两个类 A 和 B，如果 B 继承（extends）了 A，那么 A[] 类型的引用就可以指向 B[] 类型的对象。

> 扩展阅读：[Java 中数组的特性](https://blog.csdn.net/zhangjg_blog/article/details/16116613#t1)
>
> 如果想要论证 **Java 数组本质是对象**，不妨一读这篇文章。

### Java 数组和内存

Java 数组在内存中的存储是这样的：

数组对象（这里可以看成一个指针）存储在栈中。

数组元素存储在堆中。

如下图所示：只有当 JVM 执行 `new String[]` 时，才会在堆中开辟相应的内存区域。数组对象 array 可以视为一个指针，指向这块内存的存储地址。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2019/03/a38003933a4341f4b118d6843202160e.png)

## 声明数组

声明数组变量的语法如下：

```java
int[] arr1; // 推荐风格
int arr2[]; // 效果相同
```

## 创建数组

Java 语言使用 `new` 操作符来创建数组。有两种创建数组方式：

- 指定数组维度
  - 为数组开辟指定大小的数组维度。
  - 如果数组元素是基础数据类型，会将每个元素设为默认值；如果是引用类型，元素值为 `null`。
- 不指定数组维度
  - 用花括号中的实际元素初始化数组，数组大小与元素数相同。

示例 1：

```java
public class ArrayDemo {
    public static void main(String[] args) {
        int[] array1 = new int[2]; // 指定数组维度
        int[] array2 = new int[] { 1, 2 }; // 不指定数组维度

        System.out.println("array1 size is " + array1.length);
        for (int item : array1) {
            System.out.println(item);
        }

        System.out.println("array2 size is " + array1.length);
        for (int item : array2) {
            System.out.println(item);
        }
    }
}
// Output:
// array1 size is 2
// 0
// 0
// array2 size is 2
// 1
// 2
```

> 💡 **说明**
> 请注意数组 array1 中的元素虽然没有初始化，但是 length 和指定的数组维度是一样的。这表明**指定数组维度后，无论后面是否初始化数组中的元素，数组都已经开辟了相应的内存**。
>
> 数组 array1 中的元素都被设为默认值。

示例 2：

```java
public class ArrayDemo2 {
    static class User {}

    public static void main(String[] args) {
        User[] array1 = new User[2]; // 指定数组维度
        User[] array2 = new User[] {new User(), new User()}; // 不指定数组维度

        System.out.println("array1: ");
        for (User item : array1) {
            System.out.println(item);
        }

        System.out.println("array2: ");
        for (User item : array2) {
            System.out.println(item);
        }
    }
}
// Output:
// array1:
// null
// null
// array2:
// io.github.dunwu.javacore.array.ArrayDemo2$User@4141d797
// io.github.dunwu.javacore.array.ArrayDemo2$User@68f7aae2
```

> 💡 **说明**
>
> 请将本例与示例 1 比较，可以发现：如果使用指定数组维度方式创建数组，且数组元素为引用类型，则数组中的元素元素值为 `null`。

### 数组维度的形式

创建数组时，指定的数组维度可以有多种形式：

- 数组维度可以是整数、字符。
- 数组维度可以是整数型、字符型变量。
- 数组维度可以是计算结果为整数或字符的表达式。

示例：

```java
public class ArrayDemo3 {
    public static void main(String[] args) {
        int length = 3;
        // 放开被注掉的代码，编译器会报错
        // int[] array = new int[4.0];
        // int[] array2 = new int["test"];
        int[] array3 = new int['a'];
        int[] array4 = new int[length];
        int[] array5 = new int[length + 2];
        int[] array6 = new int['a' + 2];
        // int[] array7 = new int[length + 2.1];
        System.out.println("array3.length = [" + array3.length + "]");
        System.out.println("array4.length = [" + array4.length + "]");
        System.out.println("array5.length = [" + array5.length + "]");
        System.out.println("array6.length = [" + array6.length + "]");
    }
}
// Output:
// array3.length = [97]
// array4.length = [3]
// array5.length = [5]
// array6.length = [99]
```

> 💡 **说明**
>
> 当指定的数组维度是字符时，Java 会将其转为整数。如字符 `a` 的 ASCII 码是 97。
>
> 综上，**Java 数组的数组维度可以是常量、变量、表达式，只要转换为整数即可**。
>
> 请留意，有些编程语言则不支持这点，如 C/C++ 语言，只允许数组维度是常量。

### 数组维度的大小

**数组维度并非没有上限的，如果数值过大，编译时会报错。**

```java
int[] array = new int[6553612431]; // 数组维度过大，编译报错
```

此外，**数组过大，可能会导致栈溢出**。

## 访问数组

**Java 中，可以通过在 `[]` 中指定下标，访问数组元素，下标位置从 0 开始。**

```java
public class ArrayDemo4 {
    public static void main(String[] args) {
        int[] array = {1, 2, 3};
        for (int i = 0; i < array.length; i++) {
            array[i]++;
            System.out.println(String.format("array[%d] = %d", i, array[i]));
        }
    }
}
// Output:
// array[0] = 2
// array[1] = 3
// array[2] = 4
```

> 💡 **说明**
>
> 上面的示例中，从 0 开始，使用下标遍历数组 array 的所有元素，为每个元素值加 1 。

## 数组的引用

**Java 中，数组类型是一种引用类型**。

因此，它可以作为引用，被 Java 函数**作为函数入参或返回值**。

数组作为函数入参的示例：

```java
public class ArrayRefDemo {
    private static void fun(int[] array) {
        for (int i : array) {
            System.out.print(i + "\t");
        }
    }

    public static void main(String[] args) {
        int[] array = new int[] {1, 3, 5};
        fun(array);
    }
}
// Output:
// 1	3	5
```

数组作为函数返回值的示例：

```java
public class ArrayRefDemo2 {
    /**
     * 返回一个数组
     */
    private static int[] fun() {
        return new int[] {1, 3, 5};
    }

    public static void main(String[] args) {
        int[] array = fun();
        System.out.println(Arrays.toString(array));
    }
}
// Output:
// [1, 3, 5]
```

## 泛型和数组

通常，数组和泛型不能很好地结合。你不能实例化具有参数化类型的数组。

```java
Peel<Banana>[] peels = new Pell<Banana>[10]; // 这行代码非法
```

Java 中不允许直接创建泛型数组。但是，可以通过创建一个类型擦除的数组，然后转型的方式来创建泛型数组。

```java
public class GenericArrayDemo<T> {

    static class GenericArray<T> {
        private T[] array;

        public GenericArray(int num) {
            array = (T[]) new Object[num];
        }

        public void put(int index, T item) {
            array[index] = item;
        }

        public T get(int index) { return array[index]; }

        public T[] array() { return array; }
    }



    public static void main(String[] args) {
        GenericArray<Integer> genericArray = new GenericArray<Integer>(4);
        genericArray.put(0, 0);
        genericArray.put(1, 1);
        Object[] array = genericArray.array();
        System.out.println(Arrays.deepToString(array));
    }
}
// Output:
// [0, 1, null, null]
```

> 扩展阅读：https://www.cnblogs.com/jiangzhaowei/p/7399522.html
>
> 我认为，对于泛型数组的理解，点到为止即可。实际上，真的需要存储泛型，还是使用容器更合适。

## 多维数组

多维数组可以看成是数组的数组，比如二维数组就是一个特殊的一维数组，其每一个元素都是一个一维数组。

Java 可以支持二维数组、三维数组、四维数组、五维数组。。。

但是，以正常人的理解能力，一般也就最多能理解三维数组。所以，请不要做反人类的事，去定义过多维度的数组。

多维数组使用示例：

```java
public class MultiArrayDemo {
    public static void main(String[] args) {
        Integer[][] a1 = { // 自动装箱
            {1, 2, 3,},
            {4, 5, 6,},
        };
        Double[][][] a2 = { // 自动装箱
            { {1.1, 2.2}, {3.3, 4.4} },
            { {5.5, 6.6}, {7.7, 8.8} },
            { {9.9, 1.2}, {2.3, 3.4} },
        };
        String[][] a3 = {
            {"The", "Quick", "Sly", "Fox"},
            {"Jumped", "Over"},
            {"The", "Lazy", "Brown", "Dog", "and", "friend"},
        };
        System.out.println("a1: " + Arrays.deepToString(a1));
        System.out.println("a2: " + Arrays.deepToString(a2));
        System.out.println("a3: " + Arrays.deepToString(a3));
    }
}
// Output:
// a1: [[1, 2, 3], [4, 5, 6]]
// a2: [[[1.1, 2.2], [3.3, 4.4]], [[5.5, 6.6], [7.7, 8.8]], [[9.9, 1.2], [2.3, 3.4]]]
// a3: [[The, Quick, Sly, Fox], [Jumped, Over], [The, Lazy, Brown, Dog, and, friend]]
```

## Arrays 类

Java 中，提供了一个很有用的数组工具类：Arrays。

它提供的主要操作有：

- `sort` - 排序
- `binarySearch` - 查找
- `equals` - 比较
- `fill` - 填充
- `asList` - 转列表
- `hash` - 哈希
- `toString` - 转字符串

> 扩展阅读：https://juejin.im/post/5a6ade5c518825733e60acb8

## 典型应用场景

### 场景一：算法与数据处理

数组是算法实现中最基本的数据结构，常用于排序、查找、动态规划等：

```java
// 二分查找 - 在有序数组中查找目标值
public static int binarySearch(int[] arr, int target) {
    int left = 0, right = arr.length - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (arr[mid] == target) return mid;
        else if (arr[mid] < target) left = mid + 1;
        else right = mid - 1;
    }
    return -1;
}
```

### 场景二：缓冲区与批量数据处理

数组适合作为 I/O 操作的缓冲区，或批量处理数据的容器：

```java
// 文件读取缓冲区
byte[] buffer = new byte[8192];
int bytesRead;
try (InputStream is = new FileInputStream("data.bin")) {
    while ((bytesRead = is.read(buffer)) != -1) {
        processBytes(buffer, bytesRead);
    }
}
```

### 场景三：矩阵与多维数据

在科学计算、图像处理等场景中，多维数组用于表示矩阵和网格数据：

```java
// 矩阵转置
public static int[][] transpose(int[][] matrix) {
    int rows = matrix.length;
    int cols = matrix[0].length;
    int[][] result = new int[cols][rows];
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            result[j][i] = matrix[i][j];
        }
    }
    return result;
}
```

## 最佳实践

1. **优先使用容器类**：除非对性能有极致要求，否则优先使用 `ArrayList`、`List` 等容器。
2. **指定合理的初始容量**：创建数组或 ArrayList 时，如果知道大致大小，应指定初始容量避免扩容。
3. **使用 `Arrays` 工具类**：利用 `Arrays.sort()`、`Arrays.binarySearch()`、`Arrays.equals()` 等方法简化操作。
4. **避免数组越界**：访问数组前始终检查下标合法性，或使用增强 for 循环避免手动索引。
5. **使用 `System.arraycopy` 复制数组**：比循环逐个赋值更高效。
6. **多维数组不超过三维**：以人的理解能力，超过三维的数组难以维护，应考虑重构。
7. **数组初始化时注意默认值**：基本类型默认为 0/false，引用类型默认为 null，使用前必须检查。

## 常见问题

### Q1：数组和 ArrayList 如何选择？

- **数组**：固定大小、性能更高、可以存储基本类型。适用于大小确定且不变的场景。
- **ArrayList**：动态扩容、提供丰富的 API、支持泛型。适用于大多数业务场景。

### Q2：为什么 Java 不支持泛型数组？

因为泛型通过类型擦除实现，在运行时泛型信息不可知。而数组是具体化的（reified），在运行时知道元素类型。两者不兼容：如果允许泛型数组，可能导致运行时类型安全问题（数组协变 + 泛型擦除 = 类型漏洞）。

### Q3：如何优雅地打印数组内容？

直接打印数组对象只会输出内存地址的哈希值。应使用 `Arrays.toString()` 打印一维数组，`Arrays.deepToString()` 打印多维数组。

## 小结

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2019/03/b45006449d794e10b8b3284ddfae02f1.png)

## 参考资料

- [Java 编程思想](https://book.douban.com/subject/2130190/)
- [Java 核心技术（卷 1）](https://book.douban.com/subject/3146174/)
- [Java 中数组的特性](https://blog.csdn.net/zhangjg_blog/article/details/16116613#t1)
- https://juejin.im/post/59cae3de6fb9a00a4551915b
- https://www.cnblogs.com/jiangzhaowei/p/7399522.html
- https://juejin.im/post/5a6ade5c518825733e60acb8
