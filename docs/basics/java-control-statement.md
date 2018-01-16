---
title: Java 控制语句
date: 2015/05/28
categories:
- javase
tags:
- javase
- basics
---

# Java 控制语句

## 选择语句

### if语句

if 语句会判断括号中的条件是否成立，如果成立则执行 if 语句中的代码块，否则跳过代码块继续执行。

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

### if...else 语句

if 语句后面可以跟 else 语句，当 if 语句的布尔表达式值为 false 时，else 语句块会被执行。

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

### if...else if...else语句

- if语句至多有1个else语句，else语句在所有的elseif语句之后。
- If语句可以有若干个elseif语句，它们必须在else语句之前。
- 一旦其中一个else if语句检测为true，其他的else if以及else语句都将跳过执行。

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

### 嵌套的if…else语句

使用嵌套的if-else语句是合法的。也就是说你可以在另一个if或者elseif语句中使用if或者elseif语句。

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

### **switch语句**

switch语句判断一个变量与一系列值中某个值是否相等，每个值称为一个分支。

switch语句有如下规则：

- switch语句中的变量类型只能为byte、short、int、char 或者 String。
- switch语句可以拥有多个case语句。每个case后面跟一个要比较的值和冒号。
- case语句中的值的数据类型必须与变量的数据类型相同，而且只能是常量或者字面常量。
- 当变量的值与case语句的值相等时，那么case语句之后的语句开始执行，直到break语句出现才会跳出switch语句。
- 当遇到break语句时，switch语句终止。程序跳转到switch语句后面的语句执行。case语句不必须要包含break语句。如果没有break语句出现，程序会继续执行下一条case语句，直到出现break语句。
- switch语句可以包含一个default分支，该分支必须是switch语句的最后一个分支。default在没有case语句的值和变量值相等的时候执行。default分支不需要break语句。

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

##  循环语句

### while 循环

**语法**

```java
while( 布尔表达式 ) {
    //循环内容
}
```

> 只要布尔表达式为true，循环体会一直执行下去。
>

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

###  do while 循环

对于while语句而言，如果不满足条件，则不能进入循环。但有时候我们需要即使不满足条件，也至少执行一次。

do…while循环和while循环相似，不同的是，do…while循环至少会执行一次。

**语法**

```java
do {
    //代码语句
} while (布尔表达式);
```

布尔表达式在循环体的后面，所以语句块在检测布尔表达式之前已经执行了。 如果布尔表达式的值为true，则语句块一直执行，直到布尔表达式的值为false。

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

### for 循环

虽然所有循环结构都可以用while或者do...while表示，但Java提供了另一种语句 —— for循环，使一些循环结构变得更加简单。
for循环执行的次数是在执行前就确定的。

**语法**

```java
for (初始化; 布尔表达式; 更新) {
    //代码语句
}
```

- 最先执行初始化步骤。可以声明一种类型，但可初始化一个或多个循环控制变量，也可以是空语句。
- 然后，检测布尔表达式的值。如果为true，循环体被执行。如果为false，循环终止，开始执行循环体后面的语句。
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

### foreach 循环

Java5引入了一种主要用于数组的增强型for循环。 

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

### **break 关键字**

break主要用在循环语句或者switch语句中，用来跳出整个语句块。

break跳出最里层的循环，并且继续执行该循环下面的语句。

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
    }
}
// output:
// 10
// 20
```

### continue 关键字

continue适用于任何循环控制结构中。作用是让程序立刻跳转到下一次循环的迭代。
在for循环中，continue语句使程序立即跳转到更新语句。
在while或者do…while循环中，程序立即跳转到布尔表达式的判断语句。

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
