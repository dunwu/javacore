# Java BIO

> **📦 本文以及示例源码已归档在 [javacore](https://github.com/dunwu/javacore/)**
>
> ***关键词：`InputStream`、`OutputStream`、`Reader`、`Writer`***

<!-- TOC depthFrom:2 depthTo:3 -->

- [一、简介](#一简介)
  - [基本概念](#基本概念)
  - [BIO](#bio)
  - [NIO](#nio)
  - [AIO](#aio)
- [二、IO 流](#二io-流)
  - [字节流](#字节流)
  - [字符流](#字符流)
  - [字节流 vs. 字符流](#字节流-vs-字符流)
- [三、传统 BIO](#三传统-bio)
- [四、伪异步 BIO](#四伪异步-bio)
- [参考资料](#参考资料)

<!-- /TOC -->

## 一、简介

Java IO 方式有很多种，基于不同的 IO 抽象模型和交互方式，可以进行简单区分。

### 基本概念

- 区分同步或异步（synchronous/asynchronous）。简单来说，同步是一种可靠的有序运行机制，当我们进行同步操作时，后续的任务是等待当前调用返回，才会进行下一步；而异步则相反，其他任务不需要等待当前调用返回，通常依靠事件、回调等机制来实现任务间次序关系。
- 区分阻塞与非阻塞（blocking/non-blocking）。在进行阻塞操作时，当前线程会处于阻塞状态，无法从事其他任务，只有当条件就绪才能继续，比如 ServerSocket 新连接建立完毕，或数据读取、写入操作完成；而非阻塞则是不管 IO 操作是否结束，直接返回，相应操作在后台继续处理。

不能一概而论认为同步或阻塞就是低效，具体还要看应用和系统特征。

### BIO

> BIO（blocking IO） 即阻塞 IO。指的主要是传统的 `java.io` 包，它基于流模型实现。

`java.io` 包提供了我们最熟知的一些 IO 功能，比如 File 抽象、输入输出流等。交互方式是同步、阻塞的方式，也就是说，在读取输入流或者写入输出流时，在读、写动作完成之前，线程会一直阻塞在那里，它们之间的调用是可靠的线性顺序。

`java.io` 包的好处是代码比较简单、直观；缺点则是 IO 效率和扩展性存在局限性，容易成为应用性能的瓶颈。

很多时候，人们也把 java.net 下面提供的部分网络 API，比如 `Socket`、`ServerSocket`、`HttpURLConnection` 也归类到同步阻塞 IO 类库，因为网络通信同样是 IO 行为。

### NIO

> NIO（non-blocking IO） 即非阻塞 IO。指的是 Java 1.4 中引入的 `java.nio` 包。

`java.nio` 包提供了 `Channel`、`Selector`、`Buffer` 等新的抽象，可以构建多路复用的、同步非阻塞 IO 程序，同时提供了更接近操作系统底层的高性能数据操作方式。

### AIO

> AIO（Asynchronous IO） 即异步非阻塞 IO，指的是 Java 7 中，对 NIO 有了进一步的改进，也称为 NIO2，引入了异步非阻塞 IO 方式。

在 Java 7 中，NIO 有了进一步的改进，也就是 NIO 2，引入了异步非阻塞 IO 方式，也有很多人叫它 AIO（Asynchronous IO）。异步 IO 操作基于事件和回调机制，可以简单理解为，应用操作直接返回，而不会阻塞在那里，当后台处理完成，操作系统会通知相应线程进行后续工作。

## 二、IO 流

流从概念上来说是一个连续的数据流。当程序需要读数据的时候就需要使用输入流读取数据，当需要往外写数据的时候就需要输出流。

BIO 中操作的流主要有两大类，字节流和字符流，两类根据流的方向都可以分为输入流和输出流。

- **字节流**
  - 输入字节流：`InputStream`
  - 输出字节流：`OutputStream`
- **字符流**
  - 输入字符流：`Reader`
  - 输出字符流：`Writer`

![img](http://dunwu.test.upcdn.net/snap/20200219130627.png)

### 字节流

字节流主要操作字节数据或二进制对象。

字节流有两个核心抽象类：`InputStream` 和 `OutputStream`。所有的字节流类都继承自这两个抽象类。

![img](http://dunwu.test.upcdn.net/snap/20200219133627.png)

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
        // 第1步、使用File类找到一个文件
        File f = new File(filepath);

        // 第2步、通过子类实例化父类对象
        OutputStream out = new FileOutputStream(f);
        // 实例化时，默认为覆盖原文件内容方式；如果添加true参数，则变为对原文件追加内容的方式。
        // OutputStream out = new FileOutputStream(f, true);

        // 第3步、进行写操作
        String str = "Hello World\n";
        byte[] bytes = str.getBytes();
        out.write(bytes);

        // 第4步、关闭输出流
        out.close();
    }

    public static void read(String filepath) throws IOException {
        // 第1步、使用File类找到一个文件
        File f = new File(filepath);

        // 第2步、通过子类实例化父类对象
        InputStream input = new FileInputStream(f);

        // 第3步、进行读操作
        // 有三种读取方式，体会其差异
        byte[] bytes = new byte[(int) f.length()];
        int len = input.read(bytes); // 读取内容
        System.out.println("读入数据的长度：" + len);

        // 第4步、关闭输入流
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
        // 准备从内存ByteArrayInputStream中读取内容
        int temp = 0;
        while ((temp = bis.read()) != -1) {
            char c = (char) temp; // 读取的数字变为字符
            bos.write(Character.toLowerCase(c)); // 将字符变为小写
        }
        // 所有的数据就全部都在ByteArrayOutputStream中
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

这里不展开叙述，想了解详细内容和示例可以参考：[Java 序列化](java-serialization.md)

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
        // 1.使用 File 类绑定一个文件
        File f = new File(filepath);

        // 2.把 File 对象绑定到流对象上
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));

        // 3.进行读或写操作
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

        // 4.关闭流
        dos.close();
    }

    private static void read(String filepath) throws IOException {
        // 1.使用 File 类绑定一个文件
        File f = new File(filepath);

        // 2.把 File 对象绑定到流对象上
        DataInputStream dis = new DataInputStream(new FileInputStream(f));

        // 3.进行读或写操作
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
                    len++; // 读取长度加1
                }
                name = new String(temp, 0, len); // 将字符数组变为String
                price = dis.readFloat(); // 读取价格
                dis.readChar(); // 读取\t
                num = dis.readInt(); // 读取int
                dis.readChar(); // 读取\n
                System.out.printf("名称：%s；价格：%5.2f；数量：%d\n", name, price, num);
            }
        } catch (EOFException e) {
            System.out.println("结束");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 4.关闭流
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
        is1.close(); // 关闭输入流1
        is2.close(); // 关闭输入流2
        os.close(); // 关闭输出流
    }

}
```

### 字符流

字符流主要操作字符，一个字符等于两个字节。

字符流有两个核心类：`Reader` 类和 `Writer` 。所有的字符流类都继承自这两个抽象类。

![img](http://dunwu.test.upcdn.net/snap/20200219133648.png)

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
        // 1.使用 File 类绑定一个文件
        File f = new File(filepath);

        // 2.把 File 对象绑定到流对象上
        Writer out = new FileWriter(f);
        // Writer out = new FileWriter(f, true); // 追加内容方式

        // 3.进行读或写操作
        String str = "Hello World!!!\r\n";
        out.write(str);

        // 4.关闭流
        // 字符流操作时使用了缓冲区，并在关闭字符流时会强制将缓冲区内容输出
        // 如果不关闭流，则缓冲区的内容是无法输出的
        // 如果想在不关闭流时，将缓冲区内容输出，可以使用 flush 强制清空缓冲区
        out.flush();
        out.close();
    }

    public static char[] read(String filepath) throws IOException {
        // 1.使用 File 类绑定一个文件
        File f = new File(filepath);

        // 2.把 File 对象绑定到流对象上
        Reader input = new FileReader(f);

        // 3.进行读或写操作
        int temp = 0; // 接收每一个内容
        int len = 0; // 读取内容
        char[] c = new char[1024];
        while ((temp = input.read()) != -1) {
            // 如果不是-1就表示还有内容，可以继续读取
            c[len] = (char) temp;
            len++;
        }
        System.out.println("文件字符数为：" + len);

        // 4.关闭流
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
  - 字符流的数据是字符，一个字符等于两个字节。主要核心类是 `Reader` 类和 `Writer` 类。
- **缓冲区**
  - 字节流在操作时本身不会用到缓冲区（内存），是文件直接操作的。
  - 字符流在操作时是使用了缓冲区，通过缓冲区再操作文件。

选择：

所有的文件在硬盘或传输时都是以字节方式保存的，例如图片，影音文件等都是按字节方式存储的。字符流无法读写这些文件。

所以，除了纯文本数据文件使用字符流以外，其他文件类型都应该使用字节流方式。

## 三、传统 BIO

在 Linux 中，当应用进程调用 `recvfrom` 方法调用数据的时候，如果内核没有把数据准备好不会立刻返回，而是会经历等待数据准备就绪，数据从内核复制到用户空间之后再返回，这期间应用进程一直阻塞直到返回，所以被称为阻塞 IO 模型。

![img](http://dunwu.test.upcdn.net/snap/20200219153310.png)

**BIO 会阻塞进程，不适合高并发场景**。

采用 BIO 的服务端，通常由一个独立的 Acceptor 线程负责监听客户端的连接。我们一般通过在`while(true)` 循环中服务端会调用 `accept()` 方法等待接收客户端的连接的方式监听请求，请求一旦接收到一个连接请求，就可以建立通信套接字在这个通信套接字上进行读写操作，此时不能再接收其他客户端连接请求，只能等待同当前连接的客户端的操作执行完成， 不过可以通过多线程来支持多个客户端的连接，如上图所示。

如果要让 **BIO 通信模型** 能够同时处理多个客户端请求，就必须使用多线程（主要原因是`socket.accept()`、`socket.read()`、`socket.write()` 涉及的三个主要函数都是同步阻塞的），但会造成不必要的线程开销。不过可以通过 **线程池机制** 改善，线程池还可以让线程的创建和回收成本相对较低。使用`FixedThreadPool` 可以有效的控制了线程的最大数量，保证了系统有限的资源的控制，实现了 N(客户端请求数量):M(处理客户端请求的线程数量)的伪异步 I/O 模型（N 可以远远大于 M），下面一节"伪异步 BIO"中会详细介绍到。

**即使可以用线程池略微优化，但是会消耗宝贵的线程资源，并且在百万级并发场景下也撑不住**。如果并发访问量增加会导致线程数急剧膨胀可能会导致线程堆栈溢出、创建新线程失败等问题，最终导致进程宕机或者僵死，不能对外提供服务。

## 四、伪异步 BIO

为了解决同步阻塞 I/O 面临的一个链路需要一个线程处理的问题，后来有人对它的线程模型进行了优化一一一后端通过一个线程池来处理多个客户端的请求接入，形成客户端个数 M：线程池最大线程数 N 的比例关系，其中 M 可以远远大于 N。通过线程池可以灵活地调配线程资源，设置线程的最大值，防止由于海量并发接入导致线程耗尽。

![img](http://dunwu.test.upcdn.net/snap/20200219153340.png)

采用线程池和任务队列可以实现一种叫做伪异步的 I/O 通信框架，它的模型图如上图所示。当有新的客户端接入时，将客户端的 Socket 封装成一个 Task（该任务实现 java.lang.Runnable 接口）投递到后端的线程池中进行处理，JDK 的线程池维护一个消息队列和 N 个活跃线程，对消息队列中的任务进行处理。由于线程池可以设置消息队列的大小和最大线程数，因此，它的资源占用是可控的，无论多少个客户端并发访问，都不会导致资源的耗尽和宕机。

伪异步 I/O 通信框架采用了线程池实现，因此避免了为每个请求都创建一个独立线程造成的线程资源耗尽问题。不过因为它的底层仍然是同步阻塞的 BIO 模型，因此无法从根本上解决问题。

## 参考资料

- [《Java 编程思想（Thinking in java）》](https://item.jd.com/10058164.html)
- [《Java 核心技术 卷 I 基础知识》](https://item.jd.com/12759308.html)
- [《Java 从入门到精通》](https://item.jd.com/12555860.html)
- [《Java 核心技术面试精讲》](https://time.geekbang.org/column/intro/100006701)
- [BIO,NIO,AIO 总结](https://github.com/Snailclimb/JavaGuide/blob/master/docs/java/BIO-NIO-AIO.md)
