# Java 控制语句

> **📦 本文以及示例源码已归档在 [javacore](https://github.com/dunwu/javacore/)**
>
> Java 控制语句大致可分为三大类：
>
> - 选择语句
>   - if, else-if, else
>   - switch
> - 循环语句
>   - while
>   - do...while
>   - for
>   - foreach
> - 终端语句
>   - break
>   - continue
>   - return

<!-- TOC depthFrom:2 depthTo:3 -->

- [1. 选择语句](#1-选择语句)
  - [1.1. if 语句](#11-if-语句)
  - [1.2. if...else 语句](#12-ifelse-语句)
  - [1.3. if...else if...else 语句](#13-ifelse-ifelse-语句)
  - [1.4. 嵌套的 if…else 语句](#14-嵌套的-ifelse-语句)
  - [1.5. switch 语句](#15-switch-语句)
- [2. 循环语句](#2-循环语句)
  - [2.1. while 循环](#21-while-循环)
  - [2.2. do while 循环](#22-do-while-循环)
  - [2.3. for 循环](#23-for-循环)
  - [2.4. foreach 循环](#24-foreach-循环)
- [3. 中断语句](#3-中断语句)
  - [3.1. break 关键字](#31-break-关键字)
  - [3.2. continue 关键字](#32-continue-关键字)
  - [3.3. return 关键字](#33-return-关键字)
- [4. 最佳实践](#4-最佳实践)
- [5. 参考资料](#5-参考资料)

<!-- /TOC -->

## 1. 选择语句

### 1.1. if 语句

`if` 语句会判断括号中的条件是否成立，如果成立则执行 `if` 语句中的代码块，否则跳过代码块继续执行。

**语法**

```java
if(布尔表达式) {
   //如果布尔表达式为true将执行的语句
}
```

**示例**

```java
public class IfDemo {
    public static void main(String args[]) {
        int x = 10;
        if (x < 20) {
            System.out.print("这是 if 语句");
        }
    }
}
// output:
// 这是 if 语句
```

### 1.2. if...else 语句

`if` 语句后面可以跟 `else` 语句，当 `if` 语句的布尔表达式值为 `false` 时，`else` 语句块会被执行。

**语法**

```java
if(布尔表达式) {
   //如果布尔表达式的值为true
} else {
   //如果布尔表达式的值为false
}
```

**示例**

```java
public class IfElseDemo {
    public static void main(String args[]) {
        int x = 30;
        if (x < 20) {
            System.out.print("这是 if 语句");
        } else {
            System.out.print("这是 else 语句");
        }
    }
}
// output:
// 这是 else 语句
```

### 1.3. if...else if...else 语句

- `if` 语句至多有 1 个 `else` 语句，`else` 语句在所有的 `else if` 语句之后。
- `If` 语句可以有若干个 `else if` 语句，它们必须在 `else` 语句之前。
- 一旦其中一个 `else if` 语句检测为 `true`，其他的 `else if` 以及 `else` 语句都将跳过执行。

**语法**

```java
if (布尔表达式 1) {
   //如果布尔表达式 1的值为true执行代码
} else if (布尔表达式 2) {
   //如果布尔表达式 2的值为true执行代码
} else if (布尔表达式 3) {
   //如果布尔表达式 3的值为true执行代码
} else {
   //如果以上布尔表达式都不为true执行代码
}
```

**示例**

```java
public class IfElseifElseDemo {
    public static void main(String args[]) {
        int x = 3;

        if (x == 1) {
            System.out.print("Value of X is 1");
        } else if (x == 2) {
            System.out.print("Value of X is 2");
        } else if (x == 3) {
            System.out.print("Value of X is 3");
        } else {
            System.out.print("This is else statement");
        }
    }
}
// output:
// Value of X is 3
```

### 1.4. 嵌套的 if…else 语句

使用嵌套的 `if else` 语句是合法的。也就是说你可以在另一个 `if` 或者 `else if` 语句中使用 `if` 或者 `else if` 语句。

**语法**

```java
if (布尔表达式 1) {
   ////如果布尔表达式 1的值为true执行代码
   if (布尔表达式 2) {
      ////如果布尔表达式 2的值为true执行代码
   }
}
```

**示例**

```java
public class IfNestDemo {
    public static void main(String args[]) {
        int x = 30;
        int y = 10;

        if (x == 30) {
            if (y == 10) {
                System.out.print("X = 30 and Y = 10");
            }
        }
    }
}
// output:
// X = 30 and Y = 10
```

### 1.5. switch 语句

`switch` 语句判断一个变量与一系列值中某个值是否相等，每个值称为一个分支。

`switch` 语句有如下规则：

- `switch` 语句中的变量类型只能为 `byte`、`short`、`int`、`char` 或者 `String`。
- `switch` 语句可以拥有多个 `case` 语句。每个 `case` 后面跟一个要比较的值和冒号。
- `case` 语句中的值的数据类型必须与变量的数据类型相同，而且只能是常量或者字面常量。
- 当变量的值与 `case` 语句的值相等时，那么 `case` 语句之后的语句开始执行，直到 `break` 语句出现才会跳出 `switch` 语句。
- 当遇到 `break` 语句时，`switch` 语句终止。程序跳转到 `switch` 语句后面的语句执行。`case` 语句不必须要包含 `break` 语句。如果没有 `break` 语句出现，程序会继续执行下一条 `case` 语句，直到出现 `break` 语句。
- `switch` 语句可以包含一个 `default` 分支，该分支必须是 `switch` 语句的最后一个分支。`default` 在没有 `case` 语句的值和变量值相等的时候执行。`default` 分支不需要 `break` 语句。

**语法**

```java
switch(expression){
    case value :
       //语句
       break; //可选
    case value :
       //语句
       break; //可选
    //你可以有任意数量的case语句
    default : //可选
       //语句
       break; //可选，但一般建议加上
}
```

**示例**

```java
public class SwitchDemo {
    public static void main(String args[]) {
        char grade = 'C';

        switch (grade) {
        case 'A':
            System.out.println("Excellent!");
            break;
        case 'B':
        case 'C':
            System.out.println("Well done");
            break;
        case 'D':
            System.out.println("You passed");
        case 'F':
            System.out.println("Better try again");
            break;
        default:
            System.out.println("Invalid grade");
            break;
        }
        System.out.println("Your grade is " + grade);
    }
}
// output:
// Well done
// Your grade is C
```

## 2. 循环语句

### 2.1. while 循环

只要布尔表达式为 `true`，`while` 循环体会一直执行下去。

**语法**

```java
while( 布尔表达式 ) {
    //循环内容
}
```

**示例**

```java
public class WhileDemo {
    public static void main(String args[]) {
        int x = 10;
        while (x < 20) {
            System.out.print("value of x : " + x);
            x++;
            System.out.print("\n");
        }
    }
}
// output:
// value of x : 10
// value of x : 11
// value of x : 12
// value of x : 13
// value of x : 14
// value of x : 15
// value of x : 16
// value of x : 17
// value of x : 18
// value of x : 19
```

### 2.2. do while 循环

对于 `while` 语句而言，如果不满足条件，则不能进入循环。但有时候我们需要即使不满足条件，也至少执行一次。

`do while` 循环和 `while` 循环相似，不同的是，`do while` 循环至少会执行一次。

**语法**

```java
do {
    //代码语句
} while (布尔表达式);
```

布尔表达式在循环体的后面，所以语句块在检测布尔表达式之前已经执行了。 如果布尔表达式的值为 true，则语句块一直执行，直到布尔表达式的值为 false。

**示例**

```java
public class DoWhileDemo {
    public static void main(String args[]) {
        int x = 10;

        do {
            System.out.print("value of x : " + x);
            x++;
            System.out.print("\n");
        } while (x < 20);
    }
}
// output:
// value of x:10
// value of x:11
// value of x:12
// value of x:13
// value of x:14
// value of x:15
// value of x:16
// value of x:17
// value of x:18
// value of x:19
```

### 2.3. for 循环

虽然所有循环结构都可以用 `while` 或者 `do while` 表示，但 Java 提供了另一种语句 —— `for` 循环，使一些循环结构变得更加简单。
`for` 循环执行的次数是在执行前就确定的。

**语法**

```java
for (初始化; 布尔表达式; 更新) {
    //代码语句
}
```

- 最先执行初始化步骤。可以声明一种类型，但可初始化一个或多个循环控制变量，也可以是空语句。
- 然后，检测布尔表达式的值。如果为 true，循环体被执行。如果为 false，循环终止，开始执行循环体后面的语句。
- 执行一次循环后，更新循环控制变量。
- 再次检测布尔表达式。循环执行上面的过程。

**示例**

```java
public class ForDemo {
    public static void main(String args[]) {
        for (int x = 10; x < 20; x = x + 1) {
            System.out.print("value of x : " + x);
            System.out.print("\n");
        }
    }
}
// output:
// value of x : 10
// value of x : 11
// value of x : 12
// value of x : 13
// value of x : 14
// value of x : 15
// value of x : 16
// value of x : 17
// value of x : 18
// value of x : 19
```

### 2.4. foreach 循环

Java5 引入了一种主要用于数组的增强型 for 循环。

**语法**

```java
for (声明语句 : 表达式) {
    //代码句子
}
```

**声明语句**：声明新的局部变量，该变量的类型必须和数组元素的类型匹配。其作用域限定在循环语句块，其值与此时数组元素的值相等。

**表达式**：表达式是要访问的数组名，或者是返回值为数组的方法。

**示例**

```java
public class ForeachDemo {
    public static void main(String args[]) {
        int[] numbers = { 10, 20, 30, 40, 50 };

        for (int x : numbers) {
            System.out.print(x);
            System.out.print(",");
        }

        System.out.print("\n");
        String[] names = { "James", "Larry", "Tom", "Lacy" };

        for (String name : names) {
            System.out.print(name);
            System.out.print(",");
        }
    }
}
// output:
// 10,20,30,40,50,
// James,Larry,Tom,Lacy,
```

## 3. 中断语句

### 3.1. break 关键字

`break` 主要用在循环语句或者 `switch` 语句中，用来跳出整个语句块。

`break` 跳出最里层的循环，并且继续执行该循环下面的语句。

**示例**

```java
public class BreakDemo {
    public static void main(String args[]) {
        int[] numbers = { 10, 20, 30, 40, 50 };

        for (int x : numbers) {
            if (x == 30) {
                break;
            }
            System.out.print(x);
            System.out.print("\n");
        }

        System.out.println("break 示例结束");
    }
}
// output:
// 10
// 20
// break 示例结束
```

### 3.2. continue 关键字

`continue` 适用于任何循环控制结构中。作用是让程序立刻跳转到下一次循环的迭代。在 `for` 循环中，`continue` 语句使程序立即跳转到更新语句。在 `while` 或者 `do while` 循环中，程序立即跳转到布尔表达式的判断语句。

**示例**

```java
public class ContinueDemo {
    public static void main(String args[]) {
        int[] numbers = { 10, 20, 30, 40, 50 };

        for (int x : numbers) {
            if (x == 30) {
                continue;
            }
            System.out.print(x);
            System.out.print("\n");
        }
    }
}
// output:
// 10
// 20
// 40
// 50
```

### 3.3. return 关键字

跳出整个函数体，函数体后面的部分不再执行。

示例

```java
public class ReturnDemo {
    public static void main(String args[]) {
        int[] numbers = { 10, 20, 30, 40, 50 };

        for (int x : numbers) {
            if (x == 30) {
                return;
            }
            System.out.print(x);
            System.out.print("\n");
        }

        System.out.println("return 示例结束");
    }
}
// output:
// 10
// 20
```

> 🔔 注意：请仔细体会一下 `return` 和 `break` 的区别。

## 4. 最佳实践

- 选择分支特别多的情况下，`switch` 语句优于 `if...else if...else` 语句。
- `switch` 语句不要吝啬使用 `default`。
- `switch` 语句中的 `default` 要放在最后。
- `foreach` 循环优先于传统的 `for` 循环
- 不要循环遍历容器元素，然后删除特定元素。正确姿势应该是遍历容器的迭代器（`Iterator`），删除元素。

## 5. 参考资料

- [Java 编程思想](https://book.douban.com/subject/2130190/)
- [Java 核心技术（卷 1）](https://book.douban.com/subject/3146174/)
