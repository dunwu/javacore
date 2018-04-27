---
title: Java 字符流
date: 2015/05/19
categories:
- javase
tags:
- javase
- basics
- io
---

# Java 字符流

## 概念

Java 程序中，一个字符等于两个字节。

`Reader` 和 `Writer` 两个就是专门用于操作字符流的类。

`Writer` 是一个字符流的抽象类。

`Reader` 是读取字符流的抽象类。

## 文件字符流

`FileWriter` 示例

```java
public class FileWriterDemo {

    public static void main(String args[]) throws Exception { // 异常抛出，不处理
        // 第1步、使用File类找到一个文件
        File f = new File("d:" + File.separator + "test.txt"); // 声明File对象

        // 第2步、通过子类实例化父类对象
        Writer out = new FileWriter(f);
        // Writer out = new FileWriter(f, true); // 追加内容方式

        // 第3步、进行写操作
        String str = "Hello World!!!\r\n";
        out.write(str); // 将内容输出

        // 第4步、关闭输出流
        out.close();
    }
}
```

`FileReader` 示例

```java
public class FileReaderDemo {

    // 将所有内容直接读取到数组中
    public static int read1(Reader input, char[] c) throws IOException {
        int len = input.read(c); // 读取内容
        return len;
    }

    // 每次读取一个字符，直到遇到字符值为-1，表示读文件结束
    public static int read2(Reader input, char[] c) throws IOException {
        int temp = 0; // 接收每一个内容
        int len = 0; // 读取内容
        while ((temp = input.read()) != -1) {
            // 如果不是-1就表示还有内容，可以继续读取
            c[len] = (char) temp;
            len++;
        }
        return len;
    }

    public static void main(String args[]) throws Exception {
        // 第1步、使用File类找到一个文件
        File f = new File("d:" + File.separator + "test.txt");

        // 第2步、通过子类实例化父类对象
        Reader input = new FileReader(f);

        // 第3步、进行读操作
        char c[] = new char[1024];
        //        int len = read1(input, c);
        int len = read2(input, c);

        // 第4步、关闭输出流
        input.close();
        System.out.println("内容为：" + new String(c, 0, len)); // 把字符数组变为字符串输出
    }
}
```
