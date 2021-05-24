# Java IO 工具类

> **📦 本文以及示例源码已归档在 [javacore](https://github.com/dunwu/javacore/)**
>
> **_关键词：`File`、`RandomAccessFile`、`System`、`Scanner`_**
>
> 本文介绍 Java IO 的一些常见工具类的用法和特性。

<!-- TOC depthFrom:2 depthTo:3 -->

- [一、File](#一file)
  - [createNewFille](#createnewfille)
  - [mkdir](#mkdir)
  - [delete](#delete)
  - [list 和 listFiles](#list-和-listfiles)
- [二、RandomAccessFile](#二randomaccessfile)
  - [RandomAccessFile 写操作](#randomaccessfile-写操作)
  - [RandomAccessFile 读操作](#randomaccessfile-读操作)
- [三、System](#三system)
- [四、Scanner](#四scanner)
- [参考资料](#参考资料)

<!-- /TOC -->

## 一、File

`File` 类是 `java.io` 包中唯一对文件本身进行操作的类。它可以对文件、目录进行增删查操作。

### createNewFille

**可以使用 `createNewFille()` 方法创建一个新文件**。

注：

Windows 中使用反斜杠表示目录的分隔符 `\`。

Linux 中使用正斜杠表示目录的分隔符 `/`。

最好的做法是使用 `File.separator` 静态常量，可以根据所在操作系统选取对应的分隔符。

【示例】创建文件

```java
File f = new File(filename);
boolean flag = f.createNewFile();
```

### mkdir

**可以使用 `mkdir()` 来创建文件夹**，但是如果要创建的目录的父路径不存在，则无法创建成功。

如果要解决这个问题，可以使用 `mkdirs()`，当父路径不存在时，会连同上级目录都一并创建。

【示例】创建目录

```java
File f = new File(filename);
boolean flag = f.mkdir();
```

### delete

**可以使用 `delete()` 来删除文件或目录**。

需要注意的是，如果删除的是目录，且目录不为空，直接用 `delete()` 删除会失败。

【示例】删除文件或目录

```java
File f = new File(filename);
boolean flag = f.delete();
```

### list 和 listFiles

`File` 中给出了两种列出文件夹内容的方法：

- **`list()`: 列出全部名称，返回一个字符串数组**。
- **`listFiles()`: 列出完整的路径，返回一个 `File` 对象数组**。

`list()` 示例：

```java
File f = new File(filename);
String str[] = f.list();
```

`listFiles()` 示例：

```java
File f = new File(filename);
File files[] = f.listFiles();
```

## 二、RandomAccessFile

> 注：`RandomAccessFile` 类虽然可以实现对文件内容的读写操作，但是比较复杂。所以一般操作文件内容往往会使用字节流或字符流方式。

`RandomAccessFile` 类是随机读取类，它是一个完全独立的类。

它适用于由大小已知的记录组成的文件，所以我们可以使用 `seek()` 将记录从一处转移到另一处，然后读取或者修改记录。

文件中记录的大小不一定都相同，只要能够确定哪些记录有多大以及它们在文件中的位置即可。

### RandomAccessFile 写操作

当用 `rw` 方式声明 `RandomAccessFile` 对象时，如果要写入的文件不存在，系统将自行创建。

`r` 为只读；`w` 为只写；`rw` 为读写。

【示例】文件随机读写

```java
public class RandomAccessFileDemo01 {

    public static void main(String args[]) throws IOException {
        File f = new File("d:" + File.separator + "test.txt"); // 指定要操作的文件
        RandomAccessFile rdf = null; // 声明RandomAccessFile类的对象
        rdf = new RandomAccessFile(f, "rw");// 读写模式，如果文件不存在，会自动创建
        String name = null;
        int age = 0;
        name = "zhangsan"; // 字符串长度为8
        age = 30; // 数字的长度为4
        rdf.writeBytes(name); // 将姓名写入文件之中
        rdf.writeInt(age); // 将年龄写入文件之中
        name = "lisi    "; // 字符串长度为8
        age = 31; // 数字的长度为4
        rdf.writeBytes(name); // 将姓名写入文件之中
        rdf.writeInt(age); // 将年龄写入文件之中
        name = "wangwu  "; // 字符串长度为8
        age = 32; // 数字的长度为4
        rdf.writeBytes(name); // 将姓名写入文件之中
        rdf.writeInt(age); // 将年龄写入文件之中
        rdf.close(); // 关闭
    }
}
```

### RandomAccessFile 读操作

读取是直接使用 `r` 的模式即可，以只读的方式打开文件。

读取时所有的字符串只能按照 byte 数组方式读取出来，而且长度必须和写入时的固定大小相匹配。

```java
public class RandomAccessFileDemo02 {

    public static void main(String args[]) throws IOException {
        File f = new File("d:" + File.separator + "test.txt");    // 指定要操作的文件
        RandomAccessFile rdf = null;        // 声明RandomAccessFile类的对象
        rdf = new RandomAccessFile(f, "r");// 以只读的方式打开文件
        String name = null;
        int age = 0;
        byte b[] = new byte[8];    // 开辟byte数组
        // 读取第二个人的信息，意味着要空出第一个人的信息
        rdf.skipBytes(12);        // 跳过第一个人的信息
        for (int i = 0; i < b.length; i++) {
            b[i] = rdf.readByte();    // 读取一个字节
        }
        name = new String(b);    // 将读取出来的byte数组变为字符串
        age = rdf.readInt();    // 读取数字
        System.out.println("第二个人的信息 --> 姓名：" + name + "；年龄：" + age);
        // 读取第一个人的信息
        rdf.seek(0);    // 指针回到文件的开头
        for (int i = 0; i < b.length; i++) {
            b[i] = rdf.readByte();    // 读取一个字节
        }
        name = new String(b);    // 将读取出来的byte数组变为字符串
        age = rdf.readInt();    // 读取数字
        System.out.println("第一个人的信息 --> 姓名：" + name + "；年龄：" + age);
        rdf.skipBytes(12);    // 空出第二个人的信息
        for (int i = 0; i < b.length; i++) {
            b[i] = rdf.readByte();    // 读取一个字节
        }
        name = new String(b);    // 将读取出来的byte数组变为字符串
        age = rdf.readInt();    // 读取数字
        System.out.println("第三个人的信息 --> 姓名：" + name + "；年龄：" + age);
        rdf.close();                // 关闭
    }
}
```

## 三、System

`System` 类中提供了大量的静态方法，可以获取系统相关的信息或系统级操作，其中提供了三个常用于 IO 的静态成员：

- `System.out` - 一个 PrintStream 流。System.out 一般会把你写到其中的数据输出到控制台上。System.out 通常仅用在类似命令行工具的控制台程序上。System.out 也经常用于打印程序的调试信息(尽管它可能并不是获取程序调试信息的最佳方式)。
- `System.err` - 一个 PrintStream 流。System.err 与 System.out 的运行方式类似，但它更多的是用于打印错误文本。一些类似 Eclipse 的程序，为了让错误信息更加显眼，会将错误信息以红色文本的形式通过 System.err 输出到控制台上。
- `System.in` - 一个典型的连接控制台程序和键盘输入的 InputStream 流。通常当数据通过命令行参数或者配置文件传递给命令行 Java 程序的时候，System.in 并不是很常用。图形界面程序通过界面传递参数给程序，这是一块单独的 Java IO 输入机制。

【示例】重定向 `System.out` 输出流

```java
import java.io.*;
public class SystemOutDemo {

    public static void main(String args[]) throws Exception {
        OutputStream out = new FileOutputStream("d:\\test.txt");
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);
        System.out.println("人生若只如初见，何事秋风悲画扇");
        ps.close();
        out.close();
    }
}
```

【示例】重定向 `System.err` 输出流

```java
public class SystemErrDemo {

    public static void main(String args[]) throws IOException {
        OutputStream bos = new ByteArrayOutputStream();        // 实例化
        PrintStream ps = new PrintStream(bos);        // 实例化
        System.setErr(ps);    // 输出重定向
        System.err.print("此处有误");
        System.out.println(bos);    // 输出内存中的数据
    }
}
```

【示例】`System.in` 接受控制台输入信息

```java
import java.io.*;
public class SystemInDemo {

    public static void main(String args[]) throws IOException {
        InputStream input = System.in;
        StringBuffer buf = new StringBuffer();
        System.out.print("请输入内容：");
        int temp = 0;
        while ((temp = input.read()) != -1) {
            char c = (char) temp;
            if (c == '\n') {
                break;
            }
            buf.append(c);
        }
        System.out.println("输入的内容为：" + buf);
        input.close();
    }
}
```

## 四、Scanner

**`Scanner` 可以获取用户的输入，并对数据进行校验**。

【示例】校验输入数据是否格式正确

```java
import java.io.*;
public class ScannerDemo {

    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);    // 从键盘接收数据
        int i = 0;
        float f = 0.0f;
        System.out.print("输入整数：");
        if (scan.hasNextInt()) {    // 判断输入的是否是整数
            i = scan.nextInt();    // 接收整数
            System.out.println("整数数据：" + i);
        } else {
            System.out.println("输入的不是整数！");
        }

        System.out.print("输入小数：");
        if (scan.hasNextFloat()) {    // 判断输入的是否是小数
            f = scan.nextFloat();    // 接收小数
            System.out.println("小数数据：" + f);
        } else {
            System.out.println("输入的不是小数！");
        }

        Date date = null;
        String str = null;
        System.out.print("输入日期（yyyy-MM-dd）：");
        if (scan.hasNext("^\\d{4}-\\d{2}-\\d{2}$")) {    // 判断
            str = scan.next("^\\d{4}-\\d{2}-\\d{2}$");    // 接收
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(str);
            } catch (Exception e) {}
        } else {
            System.out.println("输入的日期格式错误！");
        }
        System.out.println(date);
    }
}
```

输出：

```
输入整数：20
整数数据：20
输入小数：3.2
小数数据：3.2
输入日期（yyyy-MM-dd）：1988-13-1
输入的日期格式错误！
null
```

## 参考资料

- [《Java 编程思想（Thinking in java）》](https://book.douban.com/subject/2130190/)
- [《Java 核心技术 卷 I 基础知识》](https://book.douban.com/subject/26880667/)
- [System 官方 API 手册](https://docs.oracle.com/javase/7/docs/api/java/lang/System.html)
