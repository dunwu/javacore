---
title: Java 输入输出
date: 2015/05/18
categories:
- javase
tags:
- javase
- basics
- io
---

# Java 输入输出

## 知识点

## 概念

## File 类

`File` 类是 `java.io` 包中唯一对文件本身进行操作的类。它可以对文件、目录进行增删查操作。

createNewFille

delete

mkdir

list

listFiles

## RandomAccessFile 类

> 注：`RandomAccessFile` 类虽然可以实现对文件内容的读写操作，但是比较复杂。所以一般操作文件内容往往会使用字节流或字符流方式。

`RandomAccessFile` 类是随机读取类，它是一个完全独立的类。

它适用于由大小已知的记录组成的文件，所以我们可以使用 `seek()` 将记录从一处转移到另一处，然后读取或者修改记录。

文件中记录的大小不一定都相同，只要能够确定哪些记录有多大以及它们在文件中的位置即可。

### 写操作

当用 `rw` 方式声明 `RandomAccessFile` 对象时，如果要写入的文件不存在，系统将自行创建。

`r` 为只读；`w` 为只写；`rw` 为读写。

示例：

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

### 读操作

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

## 字节流

### 文件字节流

文件字节流有两个类：`FileOutputStream` 和 `FileInputStream`。

它们提供了方法将字节写入到文件和将数据以字节形式从文件中读取出来。

文件流操作步骤：

1. 使用 File 类绑定一个文件。
2. 把 File 对象绑定到流对象上。
3. 进行读或写操作。
4. 关闭流

`FileOutputStream` 示例

```java
public class FileOutputStreamDemo {

    private static void write1(OutputStream out, byte[] bytes) throws IOException {
        out.write(bytes); // 将内容输出，保存文件
    }

    public static void write2(OutputStream out, byte[] bytes) throws IOException {
        for (byte b : bytes) { // 采用循环方式写入
            out.write(b); // 每次只写入一个内容
        }
    }

    public static void main(String args[]) throws Exception {
        // 第1步、使用File类找到一个文件
        File f = new File("d:" + File.separator + "test.txt"); // 声明File对象

        // 第2步、通过子类实例化父类对象
        OutputStream out = new FileOutputStream(f); // 通过对象多态性，进行实例化
        // 实例化时，默认为覆盖原文件内容方式；如果添加true参数，则变为对原文件追加内容的方式。
        // OutputStream out = new FileOutputStream(f, true);

        // 第3步、进行写操作
        String str = "Hello World\r\n"; // 准备一个字符串
        byte b[] = str.getBytes(); // 只能输出byte数组，所以将字符串变为byte数组
        write1(out, b);
        // write2(out, b);

        // 第4步、关闭输出流
        out.close();
    }
}
```

`FileInputStream` 示例

```java
public class FileInputStreamDemo {

    public static void read1(InputStream input, byte[] b) throws IOException {
        int len = input.read(b); // 读取内容
        System.out.println("读入数据的长度：" + len);
    }

    public static void read2(InputStream input, byte[] b) throws IOException {
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) input.read(); // 读取内容
        }
    }

    public static void read3(InputStream input, byte[] b) throws IOException {
        int len = 0;
        int temp = 0; // 接收每一个读取进来的数据
        while ((temp = input.read()) != -1) {
            // 表示还有内容，文件没有读完
            b[len] = (byte) temp;
            len++;
        }
    }

    public static void main(String args[]) throws Exception { // 异常抛出，不处理
        // 第1步、使用File类找到一个文件
        File f = new File("d:" + File.separator + "test.txt"); // 声明File对象

        // 第2步、通过子类实例化父类对象
        InputStream input = new FileInputStream(f); // 准备好一个输入的对象

        // 第3步、进行读操作
        // 有三种读取方式，体会其差异
        byte[] b = new byte[(int) f.length()];
        read1(input, b);
        // read2(input, b);
        // read3(input, b);

        // 第4步、关闭输入流
        input.close();
        System.out.println("内容为：\n" + new String(b)); // 把byte数组变为字符串输出
    }
}
```

### 内存流

`ByteArrayInputStream` 和 `ByteArrayOutputStream` 是用来完成内存的输入和输出功能。

内存操作流一般在生成一些临时信息时才使用。 如果临时信息保存在文件中，还需要在有效期过后删除文件，这样比较麻烦。

```java
public class ByteArrayStreamDemo {

    public static void main(String args[]) {
        String str = "HELLOWORLD"; // 定义一个字符串，全部由大写字母组成
        ByteArrayInputStream bis = new ByteArrayInputStream(str.getBytes());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // 准备从内存ByteArrayInputStream中读取内容
        int temp = 0;
        while ((temp = bis.read()) != -1) {
            char c = (char) temp;    // 读取的数字变为字符
            bos.write(Character.toLowerCase(c));    // 将字符变为小写
        }
        // 所有的数据就全部都在ByteArrayOutputStream中
        String newStr = bos.toString();    // 取出内容
        try {
            bis.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(newStr);
    }
};
```

### 管道流

管道流的主要作用是可以进行两个线程间的通信。

如果要进行管道通信，则必须把 `PipedOutputStream` 连接在 `PipedInputStream` 上。

为此，`PipedOutputStream` 中提供了 `connect()` 方法。

```java
public class PipedStreamDemo {

    public static void main(String args[]) {
        Send s = new Send();
        Receive r = new Receive();
        try {
            s.getPos().connect(r.getPis());    // 连接管道
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(s).start();    // 启动线程
        new Thread(r).start();    // 启动线程
    }
};

class Send implements Runnable {            // 线程类

    private PipedOutputStream pos = null;    // 管道输出流

    Send() {
        this.pos = new PipedOutputStream();    // 实例化输出流
    }

    public void run() {
        String str = "Hello World!!!";    // 要输出的内容
        try {
            this.pos.write(str.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.pos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    PipedOutputStream getPos() {    // 得到此线程的管道输出流
        return this.pos;
    }
};

class Receive implements Runnable {

    private PipedInputStream pis = null;    // 管道输入流

    Receive() {
        this.pis = new PipedInputStream();    // 实例化输入流
    }

    public void run() {
        byte b[] = new byte[1024];    // 接收内容
        int len = 0;
        try {
            len = this.pis.read(b);    // 读取内容
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.pis.close();    // 关闭
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("接收的内容为：" + new String(b, 0, len));
    }

    PipedInputStream getPis() {
        return this.pis;
    }
};
```

### 数据操作流

数据操作流提供了格式化读入和输出数据的方法，分别为 `DataInputStream` 和 `DataOutputStream`。

`DataOutputStream` 示例

```java
public class DataOutputStreamDemo {

    public static void main(String args[]) throws Exception {    // 所有异常抛出
        File f = new File("d:" + File.separator + "order.txt"); // 文件的保存路径
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));    // 实例化数据输出流对象
        String names[] = {"衬衣", "手套", "围巾"};    // 商品名称
        float prices[] = {98.3f, 30.3f, 50.5f};        // 商品价格
        int nums[] = {3, 2, 1};    // 商品数量
        for (int i = 0; i < names.length; i++) {    // 循环输出
            dos.writeChars(names[i]);    // 写入字符串
            dos.writeChar('\t');    // 写入分隔符
            dos.writeFloat(prices[i]); // 写入价格
            dos.writeChar('\t');    // 写入分隔符
            dos.writeInt(nums[i]); // 写入数量
            dos.writeChar('\n');    // 换行
        }
        dos.close();    // 关闭输出流
    }
};
```

`DataInputStream` 示例

```java
public class DataInputStreamDemo {

    public static void main(String args[]) throws Exception {    // 所有异常抛出
        File f = new File("d:" + File.separator + "order.txt"); // 文件的保存路径
        DataInputStream dis = new DataInputStream(new FileInputStream(f));    // 实例化数据输入流对象
        String name = null;    // 接收名称
        float price = 0.0f;    // 接收价格
        int num = 0;    // 接收数量
        char temp[] = null;    // 接收商品名称
        int len = 0;    // 保存读取数据的个数
        char c = 0;    // '\u0000'
        try {
            while (true) {
                temp = new char[200];    // 开辟空间
                len = 0;
                while ((c = dis.readChar()) != '\t') {    // 接收内容
                    temp[len] = c;
                    len++;    // 读取长度加1
                }
                name = new String(temp, 0, len);    // 将字符数组变为String
                price = dis.readFloat();    // 读取价格
                dis.readChar();    // 读取\t
                num = dis.readInt();    // 读取int
                dis.readChar();    // 读取\n
                System.out.printf("名称：%s；价格：%5.2f；数量：%d\n", name, price, num);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dis.close();
    }
};
```

### 合并流

合并流的主要功能是将多个 `InputStream` 合并为一个 `InputStream` 流。

合并流的功能由 `SequenceInputStream` 完成。

```java
public class SequenceInputStreamDemo {

    public static void main(String args[]) throws Exception {    // 所有异常抛出
        SequenceInputStream sis = null;    // 合并流
        InputStream is1 = new FileInputStream("d:" + File.separator + "a.txt");
        InputStream is2 = new FileInputStream("d:" + File.separator + "b.txt");
        OutputStream os = new FileOutputStream("d:" + File.separator + "ab.txt");
        sis = new SequenceInputStream(is1, is2);    // 实例化合并流
        int temp = 0;    // 接收内容
        while ((temp = sis.read()) != -1) {    // 循环输出
            os.write(temp);    // 保存内容
        }
        sis.close();    // 关闭合并流
        is1.close();    // 关闭输入流1`
        is2.close();    // 关闭输入流2
        os.close();    // 关闭输出流
    }
};
```