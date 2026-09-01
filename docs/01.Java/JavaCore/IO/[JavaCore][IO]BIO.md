---
title: Java IO 之 BIO
date: 2020-06-30 21:34:59
order: 05
categories:
  - Java
  - JavaCore
  - IO
tags:
  - Java
  - JavaCore
  - IO
  - BIO
  - InputStream
  - OutputStream
  - Reader
  - Writer
permalink: /pages/84fba019/
---

# Java IO 之 BIO

## 简介

BIO（Blocking IO）即同步阻塞 IO，是 Java 传统的 IO 模型，基于 `java.io` 包实现。BIO 以流（Stream）为单位处理数据，分为字节流和字符流两大类。其特点是 API 简洁、易于理解，但在高并发场景下每个连接需要一个线程，资源开销大。BIO 适用于连接数较少且固定的场景。

## BIO

BIO（blocking IO） 即阻塞 IO。指的主要是传统的 `java.io` 包，它基于流模型实现。流从概念上来说是一个连续的数据流。当程序需要读数据的时候就需要使用输入流读取数据，当需要往外写数据的时候就需要输出流。

`java.io` 包提供了我们最熟知的一些 IO 功能，比如 File 抽象、输入输出流等。交互方式是同步、阻塞的方式，也就是说，在读取输入流或者写入输出流时，在读、写动作完成之前，线程会一直阻塞在那里，它们之间的调用是可靠的线性顺序。很多时候，人们也把 java.net 下面提供的部分网络 API，比如 `Socket`、`ServerSocket`、`HttpURLConnection` 也归类到同步阻塞 IO 类库，因为网络通信同样是 IO 行为。

BIO 中操作的流主要有两大类，字节流和字符流，两类根据流的方向都可以分为输入流和输出流。

- **字节流**
  - 输入字节流：`InputStream`
  - 输出字节流：`OutputStream`
- **字符流**
  - 输入字符流：`Reader`
  - 输出字符流：`Writer`

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2020/02/e80caaa647cf46f9afc662abadb9238f.png)

### 字节流

字节流主要操作字节数据或二进制对象。

字节流有两个核心抽象类：`InputStream` 和 `OutputStream`。所有的字节流类都继承自这两个抽象类。

![](https://raw.githubusercontent.com/dunwu/images/master/archive/2020/02/8a631489a9ef4d46a96833b68d929e3e.png)

#### InputStream

`InputStream`用于从源头（通常是文件）读取数据（字节信息）到内存中，`java.io.InputStream`抽象类是所有字节输入流的父类。

`InputStream` 常用方法：

- `read()`：返回输入流中下一个字节的数据。返回的值介于 0 到 255 之间。如果未读取任何字节，则代码返回 `-1` ，表示文件结束。
- `read(byte b[ ])` : 从输入流中读取一些字节存储到数组 `b` 中。如果数组 `b` 的长度为零，则不读取。如果没有可用字节读取，返回 `-1`。如果有可用字节读取，则最多读取的字节数最多等于 `b.length` ， 返回读取的字节数。这个方法等价于 `read(b, 0, b.length)`。
- `read(byte b[], int off, int len)`：在`read(byte b[ ])` 方法的基础上增加了 `off` 参数（偏移量）和 `len` 参数（要读取的最大字节数）。
- `skip(long n)`：忽略输入流中的 n 个字节 , 返回实际忽略的字节数。
- `available()`：返回输入流中可以读取的字节数。
- `close()`：关闭输入流释放相关的系统资源。

从 Java 9 开始，`InputStream` 新增加了多个实用的方法：

- `readAllBytes()`：读取输入流中的所有字节，返回字节数组。
- `readNBytes(byte[] b, int off, int len)`：阻塞直到读取 `len` 个字节。
- `transferTo(OutputStream out)`：将所有字节从一个输入流传递到一个输出流。

#### OutputStream

`OutputStream` 用于将数据（字节信息）写入到目的地（通常是文件），`java.io.OutputStream`抽象类是所有字节输出流的父类。

`OutputStream` 常用方法：

- `write(int b)`：将特定字节写入输出流。
- `write(byte b[ ])` : 将数组`b` 写入到输出流，等价于 `write(b, 0, b.length)` 。
- `write(byte[] b, int off, int len)` : 在`write(byte b[ ])` 方法的基础上增加了 `off` 参数（偏移量）和 `len` 参数（要读取的最大字节数）。
- `flush()`：刷新此输出流并强制写出所有缓冲的输出字节。
- `close()`：关闭输出流释放相关的系统资源。

#### 文件字节流

`FileOutputStream` 和 `FileInputStream` 提供了读写字节到文件的能力。

文件流操作一般步骤：

1. 使用 `File` 类绑定一个文件。
2. 把 `File` 对象绑定到流对象上。
3. 进行读或写操作。
4. 关闭流

`FileOutputStream` 和 `FileInputStream` 示例：

```java
public class FileStreamDemo {

    private static final String FILEPATH = "temp.log";

    public static void main(String[] args) throws Exception {
        write(FILEPATH);
        read(FILEPATH);
    }

    public static void write(String filepath) throws IOException {
        // 第 1 步、使用 File 类找到一个文件
        File f = new File(filepath);

        // 第 2 步、通过子类实例化父类对象
        OutputStream out = new FileOutputStream(f);
        // 实例化时，默认为覆盖原文件内容方式；如果添加 true 参数，则变为对原文件追加内容的方式。
        // OutputStream out = new FileOutputStream(f, true);

        // 第 3 步、进行写操作
        String str = "Hello World\n";
        byte[] bytes = str.getBytes();
        out.write(bytes);

        // 第 4 步、关闭输出流
        out.close();
    }

    public static void read(String filepath) throws IOException {
        // 第 1 步、使用 File 类找到一个文件
        File f = new File(filepath);

        // 第 2 步、通过子类实例化父类对象
        InputStream input = new FileInputStream(f);

        // 第 3 步、进行读操作
        // 有三种读取方式，体会其差异
        byte[] bytes = new byte[(int) f.length()];
        int len = input.read(bytes); // 读取内容
        System.out.println("读入数据的长度：" + len);

        // 第 4 步、关闭输入流
        input.close();
        System.out.println("内容为：\n" + new String(bytes));
    }

}
```

#### 内存字节流

`ByteArrayInputStream` 和 `ByteArrayOutputStream` 是用来完成内存的输入和输出功能。

内存操作流一般在生成一些临时信息时才使用。 如果临时信息保存在文件中，还需要在有效期过后删除文件，这样比较麻烦。

`ByteArrayInputStream` 和 `ByteArrayOutputStream` 示例：

```java
public class ByteArrayStreamDemo {

    public static void main(String[] args) {
        String str = "HELLOWORLD"; // 定义一个字符串，全部由大写字母组成
        ByteArrayInputStream bis = new ByteArrayInputStream(str.getBytes());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // 准备从内存 ByteArrayInputStream 中读取内容
        int temp = 0;
        while ((temp = bis.read()) != -1) {
            char c = (char) temp; // 读取的数字变为字符
            bos.write(Character.toLowerCase(c)); // 将字符变为小写
        }
        // 所有的数据就全部都在 ByteArrayOutputStream 中
        String newStr = bos.toString(); // 取出内容
        try {
            bis.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(newStr);
    }

}
```

#### 管道流

管道流的主要作用是可以进行两个线程间的通信。

如果要进行管道通信，则必须把 `PipedOutputStream` 连接在 `PipedInputStream` 上。为此，`PipedOutputStream` 中提供了 `connect()` 方法。

```java
public class PipedStreamDemo {

    public static void main(String[] args) {
        Send s = new Send();
        Receive r = new Receive();
        try {
            s.getPos().connect(r.getPis()); // 连接管道
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(s).start(); // 启动线程
        new Thread(r).start(); // 启动线程
    }

    static class Send implements Runnable {

        private PipedOutputStream pos = null;

        Send() {
            pos = new PipedOutputStream(); // 实例化输出流
        }

        @Override
        public void run() {
            String str = "Hello World!!!";
            try {
                pos.write(str.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                pos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 得到此线程的管道输出流
         */
        PipedOutputStream getPos() {
            return pos;
        }

    }

    static class Receive implements Runnable {

        private PipedInputStream pis = null;

        Receive() {
            pis = new PipedInputStream();
        }

        @Override
        public void run() {
            byte[] b = new byte[1024];
            int len = 0;
            try {
                len = pis.read(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                pis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("接收的内容为：" + new String(b, 0, len));
        }

        /**
         * 得到此线程的管道输入流
         */
        PipedInputStream getPis() {
            return pis;
        }

    }

}
```

#### 对象字节流

**ObjectInputStream 和 ObjectOutputStream 是对象输入输出流，一般用于对象序列化。**

这里不展开叙述，想了解详细内容和示例可以参考：[Java 序列化]([JavaCore][IO]序列化.md)

#### 数据操作流

数据操作流提供了格式化读入和输出数据的方法，分别为 `DataInputStream` 和 `DataOutputStream`。

`DataInputStream` 和 `DataOutputStream` 格式化读写数据示例：

```java
public class DataStreamDemo {

    public static final String FILEPATH = "temp.log";

    public static void main(String[] args) throws IOException {
        write(FILEPATH);
        read(FILEPATH);
    }

    private static void write(String filepath) throws IOException {
        // 1. 使用 File 类绑定一个文件
        File f = new File(filepath);

        // 2. 把 File 对象绑定到流对象上
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));

        // 3. 进行读或写操作
        String[] names = { "衬衣", "手套", "围巾" };
        float[] prices = { 98.3f, 30.3f, 50.5f };
        int[] nums = { 3, 2, 1 };
        for (int i = 0; i < names.length; i++) {
            dos.writeChars(names[i]);
            dos.writeChar('\t');
            dos.writeFloat(prices[i]);
            dos.writeChar('\t');
            dos.writeInt(nums[i]);
            dos.writeChar('\n');
        }

        // 4. 关闭流
        dos.close();
    }

    private static void read(String filepath) throws IOException {
        // 1. 使用 File 类绑定一个文件
        File f = new File(filepath);

        // 2. 把 File 对象绑定到流对象上
        DataInputStream dis = new DataInputStream(new FileInputStream(f));

        // 3. 进行读或写操作
        String name = null; // 接收名称
        float price = 0.0f; // 接收价格
        int num = 0; // 接收数量
        char[] temp = null; // 接收商品名称
        int len = 0; // 保存读取数据的个数
        char c = 0; // '\u0000'
        try {
            while (true) {
                temp = new char[200]; // 开辟空间
                len = 0;
                while ((c = dis.readChar()) != '\t') { // 接收内容
                    temp[len] = c;
                    len++; // 读取长度加 1
                }
                name = new String(temp, 0, len); // 将字符数组变为 String
                price = dis.readFloat(); // 读取价格
                dis.readChar(); // 读取、t
                num = dis.readInt(); // 读取 int
                dis.readChar(); // 读取、n
                System.out.printf("名称：%s；价格：%5.2f；数量：%d\n", name, price, num);
            }
        } catch (EOFException e) {
            System.out.println("结束");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 4. 关闭流
        dis.close();
    }

}
```

#### 合并流

合并流的主要功能是将多个 `InputStream` 合并为一个 `InputStream` 流。合并流的功能由 `SequenceInputStream` 完成。

```java
public class SequenceInputStreamDemo {

    public static void main(String[] args) throws Exception {

        InputStream is1 = new FileInputStream("temp1.log");
        InputStream is2 = new FileInputStream("temp2.log");
        SequenceInputStream sis = new SequenceInputStream(is1, is2);

        int temp = 0; // 接收内容
        OutputStream os = new FileOutputStream("temp3.logt");
        while ((temp = sis.read()) != -1) { // 循环输出
            os.write(temp); // 保存内容
        }

        sis.close(); // 关闭合并流
        is1.close(); // 关闭输入流 1
        is2.close(); // 关闭输入流 2
        os.close(); // 关闭输出流
    }

}
```

### 字符流

字符流主要操作字符，一般用于处理文本数据。

字符流有两个核心类：`Reader` 类和 `Writer` 。所有的字符流类都继承自这两个抽象类。

#### 文件字符流

文件字符流 `FileReader` 和 `FileWriter` 可以向文件读写文本数据。

`FileReader` 和 `FileWriter` 读写文件示例：

```java
public class FileReadWriteDemo {

    private static final String FILEPATH = "temp.log";

    public static void main(String[] args) throws IOException {
        write(FILEPATH);
        System.out.println("内容为：" + new String(read(FILEPATH)));
    }

    public static void write(String filepath) throws IOException {
        // 1. 使用 File 类绑定一个文件
        File f = new File(filepath);

        // 2. 把 File 对象绑定到流对象上
        Writer out = new FileWriter(f);
        // Writer out = new FileWriter(f, true); // 追加内容方式

        // 3. 进行读或写操作
        String str = "Hello World!!!\r\n";
        out.write(str);

        // 4. 关闭流
        // 字符流操作时使用了缓冲区，并在关闭字符流时会强制将缓冲区内容输出
        // 如果不关闭流，则缓冲区的内容是无法输出的
        // 如果想在不关闭流时，将缓冲区内容输出，可以使用 flush 强制清空缓冲区
        out.flush();
        out.close();
    }

    public static char[] read(String filepath) throws IOException {
        // 1. 使用 File 类绑定一个文件
        File f = new File(filepath);

        // 2. 把 File 对象绑定到流对象上
        Reader input = new FileReader(f);

        // 3. 进行读或写操作
        int temp = 0; // 接收每一个内容
        int len = 0; // 读取内容
        char[] c = new char[1024];
        while ((temp = input.read()) != -1) {
            // 如果不是-1 就表示还有内容，可以继续读取
            c[len] = (char) temp;
            len++;
        }
        System.out.println("文件字符数为：" + len);

        // 4. 关闭流
        input.close();

        return c;
    }

}
```

#### 字节流转换字符流

我们可以在程序中通过 `InputStream` 和 `Reader` 从数据源中读取数据，然后也可以在程序中将数据通过 `OutputStream` 和 `Writer` 输出到目标媒介中

使用 `InputStreamReader` 可以将输入字节流转化为输入字符流；使用`OutputStreamWriter`可以将输出字节流转化为输出字符流。

`OutputStreamWriter` 示例：

```java
public class OutputStreamWriterDemo {

    public static void main(String[] args) throws IOException {
        File f = new File("temp.log");
        Writer out = new OutputStreamWriter(new FileOutputStream(f));
        out.write("hello world!!");
        out.close();
    }

}
```

`InputStreamReader` 示例：

```java
public class InputStreamReaderDemo {

    public static void main(String[] args) throws IOException {
        File f = new File("temp.log");
        Reader reader = new InputStreamReader(new FileInputStream(f));
        char[] c = new char[1024];
        int len = reader.read(c);
        reader.close();
        System.out.println(new String(c, 0, len));
    }

}
```

### 字节流 vs. 字符流

相同点：

字节流和字符流都有 `read()`、`write()`、`flush()`、`close()` 这样的方法，这决定了它们的操作方式近似。

不同点：

- **数据类型**
  - 字节流的数据是字节（二进制对象）。主要核心类是 `InputStream` 类和 `OutputStream` 类。
  - 字符流的数据是字符。主要核心类是 `Reader` 类和 `Writer` 类。
- **缓冲区**
  - 字节流在操作时本身不会用到缓冲区（内存），是文件直接操作的。
  - 字符流在操作时是使用了缓冲区，通过缓冲区再操作文件。

选择：

所有的文件在硬盘或传输时都是以字节方式保存的，例如图片，影音文件等都是按字节方式存储的。字符流无法读写这些文件。

所以，除了纯文本数据文件使用字符流以外，其他文件类型都应该使用字节流方式。

## I/O 工具类

### File

`File` 类是 `java.io` 包中唯一对文件本身进行操作的类。它可以对文件、目录进行增删查操作。

#### createNewFille

**可以使用 `createNewFille()` 方法创建一个新文件**。

注：

Windows 中使用反斜杠表示目录的分隔符 `\`。~~~~~~~~

Linux 中使用正斜杠表示目录的分隔符 `/`。

最好的做法是使用 `File.separator` 静态常量，可以根据所在操作系统选取对应的分隔符。

【示例】创建文件

```java
File f = new File(filename);
boolean flag = f.createNewFile();
```

#### mkdir

**可以使用 `mkdir()` 来创建文件夹**，但是如果要创建的目录的父路径不存在，则无法创建成功。

如果要解决这个问题，可以使用 `mkdirs()`，当父路径不存在时，会连同上级目录都一并创建。

【示例】创建目录

```java
File f = new File(filename);
boolean flag = f.mkdir();
```

#### delete

**可以使用 `delete()` 来删除文件或目录**。

需要注意的是，如果删除的是目录，且目录不为空，直接用 `delete()` 删除会失败。

【示例】删除文件或目录

```java
File f = new File(filename);
boolean flag = f.delete();
```

#### list 和 listFiles

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

### RandomAccessFile

> 注：`RandomAccessFile` 类虽然可以实现对文件内容的读写操作，但是比较复杂。所以一般操作文件内容往往会使用字节流或字符流方式。

`RandomAccessFile` 类是随机读取类，它是一个完全独立的类。

它适用于由大小已知的记录组成的文件，所以我们可以使用 `seek()` 将记录从一处转移到另一处，然后读取或者修改记录。

文件中记录的大小不一定都相同，只要能够确定哪些记录有多大以及它们在文件中的位置即可。

#### RandomAccessFile 写操作

当用 `rw` 方式声明 `RandomAccessFile` 对象时，如果要写入的文件不存在，系统将自行创建。

`r` 为只读；`w` 为只写；`rw` 为读写。

【示例】文件随机读写

```java
public class RandomAccessFileDemo01 {

    public static void main(String args[]) throws IOException {
        File f = new File("d:" + File.separator + "test.txt"); // 指定要操作的文件
        RandomAccessFile rdf = null; // 声明 RandomAccessFile 类的对象
        rdf = new RandomAccessFile(f, "rw");// 读写模式，如果文件不存在，会自动创建
        String name = null;
        int age = 0;
        name = "zhangsan"; // 字符串长度为 8
        age = 30; // 数字的长度为 4
        rdf.writeBytes(name); // 将姓名写入文件之中
        rdf.writeInt(age); // 将年龄写入文件之中
        name = "lisi    "; // 字符串长度为 8
        age = 31; // 数字的长度为 4
        rdf.writeBytes(name); // 将姓名写入文件之中
        rdf.writeInt(age); // 将年龄写入文件之中
        name = "wangwu  "; // 字符串长度为 8
        age = 32; // 数字的长度为 4
        rdf.writeBytes(name); // 将姓名写入文件之中
        rdf.writeInt(age); // 将年龄写入文件之中
        rdf.close(); // 关闭
    }
}
```

#### RandomAccessFile 读操作

读取是直接使用 `r` 的模式即可，以只读的方式打开文件。

读取时所有的字符串只能按照 byte 数组方式读取出来，而且长度必须和写入时的固定大小相匹配。

```java
public class RandomAccessFileDemo02 {

    public static void main(String args[]) throws IOException {
        File f = new File("d:" + File.separator + "test.txt");    // 指定要操作的文件
        RandomAccessFile rdf = null;        // 声明 RandomAccessFile 类的对象
        rdf = new RandomAccessFile(f, "r");// 以只读的方式打开文件
        String name = null;
        int age = 0;
        byte b[] = new byte[8];    // 开辟 byte 数组
        // 读取第二个人的信息，意味着要空出第一个人的信息
        rdf.skipBytes(12);        // 跳过第一个人的信息
        for (int i = 0; i < b.length; i++) {
            b[i] = rdf.readByte();    // 读取一个字节
        }
        name = new String(b);    // 将读取出来的 byte 数组变为字符串
        age = rdf.readInt();    // 读取数字
        System.out.println("第二个人的信息 --> 姓名：" + name + "；年龄：" + age);
        // 读取第一个人的信息
        rdf.seek(0);    // 指针回到文件的开头
        for (int i = 0; i < b.length; i++) {
            b[i] = rdf.readByte();    // 读取一个字节
        }
        name = new String(b);    // 将读取出来的 byte 数组变为字符串
        age = rdf.readInt();    // 读取数字
        System.out.println("第一个人的信息 --> 姓名：" + name + "；年龄：" + age);
        rdf.skipBytes(12);    // 空出第二个人的信息
        for (int i = 0; i < b.length; i++) {
            b[i] = rdf.readByte();    // 读取一个字节
        }
        name = new String(b);    // 将读取出来的 byte 数组变为字符串
        age = rdf.readInt();    // 读取数字
        System.out.println("第三个人的信息 --> 姓名：" + name + "；年龄：" + age);
        rdf.close();                // 关闭
    }
}
```

### System

`System` 类中提供了大量的静态方法，可以获取系统相关的信息或系统级操作，其中提供了三个常用于 IO 的静态成员：

- `System.out` - 一个 PrintStream 流。System.out 一般会把你写到其中的数据输出到控制台上。System.out 通常仅用在类似命令行工具的控制台程序上。System.out 也经常用于打印程序的调试信息（尽管它可能并不是获取程序调试信息的最佳方式）。
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

### Scanner

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

## 网络编程

> **_关键词：`Socket`、`ServerSocket`、`DatagramPacket`、`DatagramSocket`_**
>
> 网络编程是指编写运行在多个设备（计算机）的程序，这些设备都通过网络连接起来。
>
> `java.net` 包中提供了低层次的网络通信细节。你可以直接使用这些类和接口，来专注于解决问题，而不用关注通信细节。
>
> java.net 包中提供了两种常见的网络协议的支持：
>
> - **TCP** - TCP 是传输控制协议的缩写，它保障了两个应用程序之间的可靠通信。通常用于互联网协议，被称 TCP/ IP。
> - **UDP** - UDP 是用户数据报协议的缩写，一个无连接的协议。提供了应用程序之间要发送的数据的数据包。

### Socket 和 ServerSocket

套接字（Socket）使用 TCP 提供了两台计算机之间的通信机制。 客户端程序创建一个套接字，并尝试连接服务器的套接字。

**Java 通过 Socket 和 ServerSocket 实现对 TCP 的支持**。Java 中的 Socket 通信可以简单理解为：**`java.net.Socket` 代表客户端，`java.net.ServerSocket` 代表服务端**，二者可以建立连接，然后通信。

以下为 Socket 通信中建立建立的基本流程：

- 服务器实例化一个 `ServerSocket` 对象，表示服务器绑定一个端口。
- 服务器调用 `ServerSocket` 的 `accept()` 方法，该方法将一直等待，直到客户端连接到服务器的绑定端口（即监听端口）。
- 服务器监听端口时，客户端实例化一个 `Socket` 对象，指定服务器名称和端口号来请求连接。
- `Socket` 类的构造函数试图将客户端连接到指定的服务器和端口号。如果通信被建立，则在客户端创建一个 Socket 对象能够与服务器进行通信。
- 在服务器端，`accept()` 方法返回服务器上一个新的 `Socket` 引用，该引用连接到客户端的 `Socket` 。

连接建立后，可以通过使用 IO 流进行通信。每一个 `Socket` 都有一个输出流和一个输入流。客户端的输出流连接到服务器端的输入流，而客户端的输入流连接到服务器端的输出流。

TCP 是一个双向的通信协议，因此数据可以通过两个数据流在同一时间发送，以下是一些类提供的一套完整的有用的方法来实现 sockets。

#### ServerSocket

服务器程序通过使用 `java.net.ServerSocket` 类以获取一个端口，并且监听客户端请求连接此端口的请求。

##### ServerSocket 构造方法

`ServerSocket` 有多个构造方法：

| **方法**                                                   | **描述**                                                            |
| ---------------------------------------------------------- | ------------------------------------------------------------------- |
| `ServerSocket()`                                           | 创建非绑定服务器套接字。                                            |
| `ServerSocket(int port)`                                   | 创建绑定到特定端口的服务器套接字。                                  |
| `ServerSocket(int port, int backlog)`                      | 利用指定的 `backlog` 创建服务器套接字并将其绑定到指定的本地端口号。 |
| `ServerSocket(int port, int backlog, InetAddress address)` | 使用指定的端口、监听 `backlog` 和要绑定到的本地 IP 地址创建服务器。 |

##### ServerSocket 常用方法

创建非绑定服务器套接字。 如果 `ServerSocket` 构造方法没有抛出异常，就意味着你的应用程序已经成功绑定到指定的端口，并且侦听客户端请求。

这里有一些 `ServerSocket` 类的常用方法：

| **方法**                                     | **描述**                                              |
| -------------------------------------------- | ----------------------------------------------------- |
| `int getLocalPort()`                         | 返回此套接字在其上侦听的端口。                        |
| `Socket accept()`                            | 监听并接受到此套接字的连接。                          |
| `void setSoTimeout(int timeout)`             | 通过指定超时值启用/禁用 `SO_TIMEOUT`，以毫秒为单位。  |
| `void bind(SocketAddress host, int backlog)` | 将 `ServerSocket` 绑定到特定地址（IP 地址和端口号）。 |

#### Socket

`java.net.Socket` 类代表客户端和服务器都用来互相沟通的套接字。客户端要获取一个 `Socket` 对象通过实例化 ，而 服务器获得一个 `Socket` 对象则通过 `accept()` 方法 a 的返回值。

##### Socket 构造方法

`Socket` 类有 5 个构造方法：

| **方法**                                                                      | **描述**                                                 |
| ----------------------------------------------------------------------------- | -------------------------------------------------------- |
| `Socket()`                                                                    | 通过系统默认类型的 `SocketImpl` 创建未连接套接字         |
| `Socket(String host, int port)`                                               | 创建一个流套接字并将其连接到指定主机上的指定端口号。     |
| `Socket(InetAddress host, int port)`                                          | 创建一个流套接字并将其连接到指定 IP 地址的指定端口号。   |
| `Socket(String host, int port, InetAddress localAddress, int localPort)`      | 创建一个套接字并将其连接到指定远程主机上的指定远程端口。 |
| `Socket(InetAddress host, int port, InetAddress localAddress, int localPort)` | 创建一个套接字并将其连接到指定远程地址上的指定远程端口。 |

当 Socket 构造方法返回，并没有简单的实例化了一个 Socket 对象，它实际上会尝试连接到指定的服务器和端口。

##### Socket 常用方法

下面列出了一些感兴趣的方法，注意客户端和服务器端都有一个 Socket 对象，所以无论客户端还是服务端都能够调用这些方法。

| **方法**                                        | **描述**                                              |
| ----------------------------------------------- | ----------------------------------------------------- |
| `void connect(SocketAddress host, int timeout)` | 将此套接字连接到服务器，并指定一个超时值。            |
| `InetAddress getInetAddress()`                  | 返回套接字连接的地址。                                |
| `int getPort()`                                 | 返回此套接字连接到的远程端口。                        |
| `int getLocalPort()`                            | 返回此套接字绑定到的本地端口。                        |
| `SocketAddress getRemoteSocketAddress()`        | 返回此套接字连接的端点的地址，如果未连接则返回 null。 |
| `InputStream getInputStream()`                  | 返回此套接字的输入流。                                |
| `OutputStream getOutputStream()`                | 返回此套接字的输出流。                                |
| `void close()`                                  | 关闭此套接字。                                        |

#### Socket 通信示例

服务端示例：

```java
public class HelloServer {

    public static void main(String[] args) throws Exception {
        // Socket 服务端
        // 服务器在 8888 端口上监听
        ServerSocket server = new ServerSocket(8888);
        System.out.println("服务器运行中，等待客户端连接。");
        // 得到连接，程序进入到阻塞状态
        Socket client = server.accept();
        // 打印流输出最方便
        PrintStream out = new PrintStream(client.getOutputStream());
        // 向客户端输出信息
        out.println("hello world");
        client.close();
        server.close();
        System.out.println("服务器已向客户端发送消息，退出。");
    }

}
```

客户端示例：

```java
public class HelloClient {

    public static void main(String[] args) throws Exception {
        // Socket 客户端
        Socket client = new Socket("localhost", 8888);
        InputStreamReader inputStreamReader = new InputStreamReader(client.getInputStream());
        // 一次性接收完成
        BufferedReader buf = new BufferedReader(inputStreamReader);
        String str = buf.readLine();
        buf.close();
        client.close();
        System.out.println("客户端接收到服务器消息：" + str + "，退出");
    }

}
```

### DatagramSocket 和 DatagramPacket

Java 通过 `DatagramSocket` 和 `DatagramPacket` 实现对 UDP 协议的支持。

- `DatagramPacket`：数据包类
- `DatagramSocket`：通信类

UDP 服务端示例：

```java
public class UDPServer {

    public static void main(String[] args) throws Exception { // 所有异常抛出
        String str = "hello World!!!";
        DatagramSocket ds = new DatagramSocket(3000); // 服务端在 3000 端口上等待服务器发送信息
        DatagramPacket dp =
            new DatagramPacket(str.getBytes(), str.length(), InetAddress.getByName("localhost"), 9000); // 所有的信息使用 buf 保存
        System.out.println("发送信息。");
        ds.send(dp); // 发送信息出去
        ds.close();
    }

}
```

UDP 客户端示例：

```java
public class UDPClient {

    public static void main(String[] args) throws Exception { // 所有异常抛出
        byte[] buf = new byte[1024]; // 开辟空间，以接收数据
        DatagramSocket ds = new DatagramSocket(9000); // 客户端在 9000 端口上等待服务器发送信息
        DatagramPacket dp = new DatagramPacket(buf, 1024); // 所有的信息使用 buf 保存
        ds.receive(dp); // 接收数据
        String str = new String(dp.getData(), 0, dp.getLength()) + "from " + dp.getAddress().getHostAddress() + "："
            + dp.getPort();
        System.out.println(str); // 输出内容
    }

}
```

### InetAddress

`InetAddress` 类表示互联网协议 (IP) 地址。

没有公有的构造函数，只能通过静态方法来创建实例。

```java
InetAddress.getByName(String host);
InetAddress.getByAddress(byte[] address);
```

### URL

可以直接从 URL 中读取字节流数据。

```java
public static void main(String[] args) throws IOException {

    URL url = new URL("http://www.baidu.com");

    /* 字节流 */
    InputStream is = url.openStream();

    /* 字符流 */
    InputStreamReader isr = new InputStreamReader(is, "utf-8");

    /* 提供缓存功能 */
    BufferedReader br = new BufferedReader(isr);

    String line;
    while ((line = br.readLine()) != null) {
        System.out.println(line);
    }

    br.close();
}
```

## 典型应用场景

### 场景一：配置文件读取

使用 BIO 读取 properties 配置文件：

```java
Properties props = new Properties();
try (InputStream is = new FileInputStream("app.properties")) {
    props.load(is);
    String dbUrl = props.getProperty("database.url");
}
```

### 场景二：文件复制

使用缓冲流高效复制文件：

```java
public static void copyFile(File src, File dest) throws IOException {
    try (InputStream in = new BufferedInputStream(new FileInputStream(src));
         OutputStream out = new BufferedOutputStream(new FileOutputStream(dest))) {
        byte[] buffer = new byte[8192];
        int len;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
    }
}
```

### 场景三：简单的 TCP 服务器

使用 BIO 实现简单的 TCP Echo 服务器：

```java
try (ServerSocket server = new ServerSocket(9090)) {
    while (true) {
        Socket client = server.accept(); // 阻塞等待连接
        new Thread(() -> {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                 PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
                String line;
                while ((line = in.readLine()) != null) {
                    out.println("Echo: " + line);
                }
            } catch (IOException e) { e.printStackTrace(); }
        }).start();
    }
}
```

## 最佳实践

1. **始终使用 try-with-resources**：自动关闭流，避免资源泄漏。
2. **使用缓冲流包装**：`BufferedInputStream`/`BufferedReader` 减少系统调用。
3. **字节流与字符流的选择**：处理文本用字符流（Reader/Writer），处理二进制用字节流（InputStream/OutputStream）。
4. **指定字符集**：使用 `InputStreamReader(is, StandardCharsets.UTF_8)` 避免乱码。
5. **高并发场景不要使用 BIO**：每个连接一个线程的模式无法支撑万级并发。

## 常见问题

### Q1：字节流和字符流有什么区别？

- **字节流**（InputStream/OutputStream）：以字节（8bit）为单位，适合处理图片、音频等二进制数据。
- **字符流**（Reader/Writer）：以字符为单位，内部处理编码转换，适合处理文本数据。

### Q2：BIO 的 ServerSocket 如何实现高并发？

传统 BIO 通过线程池可以一定程度上提升并发能力，但本质上仍是一个连接一个线程。真正的万级并发需要使用 NIO（如 Netty 框架）。BIO 在高并发下线程数会急剧膨胀，导致栈溢出或 OOM。

### Q3：`FileInputStream` 和 `FileReader` 的区别？

`FileInputStream` 读取原始字节，`FileReader` 基于系统默认字符集将字节转为字符。推荐显式使用 `new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)` 代替 `FileReader`。

## 参考资料

- [《Java 编程思想（Thinking in java）》](https://book.douban.com/subject/2130190/)
- [《Java 核心技术 卷 I 基础知识》](https://book.douban.com/subject/26880667/)
- [System 官方 API 手册](https://docs.oracle.com/javase/7/docs/api/java/lang/System.html)
- [Java 网络编程](https://www.runoob.com/java/java-networking.html)
