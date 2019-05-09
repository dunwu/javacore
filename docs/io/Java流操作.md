# Java 流操作

> :notebook: 本文已归档到：「[javacore](https://github.com/dunwu/javacore)」
>
> 关键词：`InputStream`、`OutputStream`、`Reader`、`Writer`

<!-- TOC depthFrom:2 depthTo:2 -->

- [字符流和字节流](#字符流和字节流)
- [FileReader 和 FileWriter](#filereader-和-filewriter)
- [InputStreamReader 和 OutputStreamWriter](#inputstreamreader-和-outputstreamwriter)
- [BufferedReader](#bufferedreader)
- [PrintStream](#printstream)
- [FileInputStream 和 FileOutputStream](#fileinputstream-和-fileoutputstream)
- [ByteArrayInputStream 和 ByteArrayOutputStream](#bytearrayinputstream-和-bytearrayoutputstream)
- [PipedInputStream 和 PipedOutputStream](#pipedinputstream-和-pipedoutputstream)
- [DataInputStream 和 DataOutputStream](#datainputstream-和-dataoutputstream)
- [ZipInputStream 和 ZipOutputStream](#zipinputstream-和-zipoutputstream)
- [ObjectInputStream 和 ObjectOutputStream](#objectinputstream-和-objectoutputstream)

<!-- /TOC -->

## 字符流和字节流

JAVA IO 中的流操作分为两类：

- **字节流主要操作字节类型数据（byte）。主要类是 `InputStream`（输入） 和 `OutputStream`（输出）。**
- **字符流主要操作字符类型数据，一个字符占两个字节。主要类是 `Reader`（输入） 和 `Writer`（输出）。**

JAVA IO 中的流操作类，常常是以输入、输出两种形式成对提供。

在 JAVA IO 中，流操作的一般流程如下：

1.  使用 File 类绑定一个文件。
2.  把 File 对象绑定到流对象上。
3.  进行读或写操作。
4.  关闭流

### 字符流和字节流的区别

- 字节流主要操作字节类型数据（byte）；字符流主要操作字符类型数据，一个字符占两个字节。
- 字节流在操作时本身不会用到缓冲区（内存），而是对文件本身直接操作的；字符流在操作时使用了缓冲区，通过缓冲区再操作文件。

## FileReader 和 FileWriter

**FileReader 和 FileWriter 用于输入输出文本文件。**

```java
import java.io.*;

public class ReaderAndWriterDemo {

    public static void output(String filepath) throws IOException {
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

    public static char[] input(String filepath) throws IOException {
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

    public static void main(String[] args) throws IOException {
        String filepath = "d:\\test.txt";

        output(filepath);
        System.out.println("内容为：" + new String(input(filepath)));
    }
}
```

## InputStreamReader 和 OutputStreamWriter

**InputStreamReader 和 OutputStreamWriter 可以将 InputStream 和 OutputStream 分别转换为 Reader 和 Writer。**

示例：

```java
import java.io.*;

public class OutputStreamWriterDemo {

    public static void main(String args[]) throws IOException {
        File f = new File("d:" + File.separator + "test.txt");
        Writer out = new OutputStreamWriter(new FileOutputStream(f));
        out.write("hello world!!");
        out.close();
    }
}

public class InputStreamReaderDemo {

    public static void main(String args[]) throws IOException {
        File f = new File("d:" + File.separator + "test.txt");
        Reader reader = new InputStreamReader(new FileInputStream(f));
        char c[] = new char[1024];
        int len = reader.read(c);
        reader.close();
        System.out.println(new String(c, 0, len));
    }
}
```

## BufferedReader

BufferedReader 类用于从缓冲区中读取内容，所有的输入字节数据都放在缓冲区中。

示例：

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BufferedReaderDemo {

    public static void main(String args[]) throws IOException {
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("请输入内容：");
            String str = buf.readLine();
            if (str.equalsIgnoreCase("exit")) {
                System.out.print("退出");
                break;
            }
            System.out.println("输入的内容为：" + str);
        }
    }
}
```

## PrintStream

PrintStream 提供了非常方便的打印功能。

事实上，我们常用的 System 中提供的静态成员 System.out 和 System.err 就是 PrintStream 对象。

示例：

```java
import java.io.*;

public class PrintStreamDemo {

    public static void main(String arg[]) throws Exception {
        final String filepath = "d:\\test.txt";
        // 如果现在是使用 FileOuputStream 实例化，意味着所有的数据都会输出到文件中
        OutputStream os = new FileOutputStream(new File(filepath));
        PrintStream ps = new PrintStream(os);
        ps.print("Hello ");
        ps.println("World!!!");
        ps.printf("姓名：%s；年龄：%d", "张三", 18);
        ps.close();
    }
}
```

## FileInputStream 和 FileOutputStream

**FileInputStream 和 FileOutputStream 用于输入、输出文件。**

示例：

```java
import java.io.*;

public class FileStreamDemo {

    private static final String FILEPATH = "d:\\test.txt";

    public static void output(String filepath) throws IOException {
        // 第1步、使用File类找到一个文件
        File f = new File(filepath);

        // 第2步、通过子类实例化父类对象
        OutputStream out = new FileOutputStream(f);
        // 实例化时，默认为覆盖原文件内容方式；如果添加true参数，则变为对原文件追加内容的方式。
        // OutputStream out = new FileOutputStream(f, true);

        // 第3步、进行写操作
        String str = "Hello World\r\n";
        byte[] bytes = str.getBytes();
        out.write(bytes);

        // 第4步、关闭输出流
        out.close();
    }

    public static void input(String filepath) throws IOException {
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

    public static void main(String args[]) throws Exception {
        output(FILEPATH);
        input(FILEPATH);
    }
}
```

## ByteArrayInputStream 和 ByteArrayOutputStream

**ByteArrayInputStream 和 ByteArrayOutputStream 用于在内存中输入、输出数据。**

示例：

```java
import java.io.*;

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
}
```

## PipedInputStream 和 PipedOutputStream

**PipedInputStream 和 PipedOutputStream 可以在两个线程间进行通信。**

示例：

```java
import java.io.*;

public class PipedStreamDemo {

    static class Send implements Runnable {

        private PipedOutputStream pos = null;

        Send() {
            pos = new PipedOutputStream();    // 实例化输出流
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
            byte b[] = new byte[1024];
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
}
```

## DataInputStream 和 DataOutputStream

DataInputStream 和 DataOutputStream 会一定格式将数据输入、输出。

示例：

```java
import java.io.*;

public class DataStreamDemo {

    public static final String FILEPATH = "d:\\order.txt";

    private static void output(String filepath) throws IOException {
        // 1.使用 File 类绑定一个文件
        File f = new File(filepath);

        // 2.把 File 对象绑定到流对象上
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));

        // 3.进行读或写操作
        String names[] = {"衬衣", "手套", "围巾"};
        float prices[] = {98.3f, 30.3f, 50.5f};
        int nums[] = {3, 2, 1};
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

    private static void input(String filepath) throws IOException {
        // 1.使用 File 类绑定一个文件
        File f = new File(filepath);

        // 2.把 File 对象绑定到流对象上
        DataInputStream dis = new DataInputStream(new FileInputStream(f));

        // 3.进行读或写操作
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
        } catch (EOFException e) {
            System.out.println("结束");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 4.关闭流
        dis.close();
    }

    public static void main(String args[]) throws IOException {
        output(FILEPATH);
        input(FILEPATH);
    }
}
```

## ZipInputStream 和 ZipOutputStream

示例：

```java
import java.io.*;
import java.util.zip.*;

public class ZipStreamDemo {

    public static final String ZIP_FILE_PATH = "d:\\zipdemo.zip";

    public static void demo01(String zipfilepath) throws IOException {
        File file = new File(zipfilepath);
        ZipFile zipFile = new ZipFile(file);
        ZipEntry entry = zipFile.getEntry("mldn.txt");
        System.out.println("压缩文件的名称：" + zipFile.getName());

        File outputFile = new File("d:" + File.separator + "mldn_unzip.txt");
        OutputStream out = new FileOutputStream(outputFile); // 实例化输出流
        InputStream input = zipFile.getInputStream(entry);    // 得到一个压缩实体的输入流
        int temp = 0;
        while ((temp = input.read()) != -1) {
            out.write(temp);
        }
        input.close();    // 关闭输入流
        out.close();    // 关闭输出流
    }

    /**
     * 压缩一个文件
     */
    public static void output1(String filepath, String zipfilepath) throws Exception {
        // 1.使用 File 类绑定一个文件
        // 定义要压缩的文件
        File file = new File(filepath);
        // 定义压缩文件名称
        File zipFile = new File(zipfilepath);

        // 2.把 File 对象绑定到流对象上
        InputStream input = new FileInputStream(file);
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));

        // 3.进行读或写操作
        zipOut.putNextEntry(new ZipEntry(file.getName()));
        zipOut.setComment("This is a zip file.");
        int temp = 0;
        while ((temp = input.read()) != -1) {    // 读取内容
            zipOut.write(temp);    // 压缩输出
        }

        // 4.关闭流
        input.close();
        zipOut.close();
    }

    /**
     * 读取实体为一个文件的压缩包
     */
    public static void input1(String zipfilepath, String filepath) throws Exception {
        // 1.使用 File 类绑定一个文件
        File zipFile = new File(zipfilepath);

        // 2.把 File 对象绑定到流对象上
        ZipInputStream input = new ZipInputStream(new FileInputStream(zipFile));

        // 3.进行读或写操作
        ZipEntry entry = input.getNextEntry();    // 得到一个压缩实体
        System.out.println("压缩实体名称：" + entry.getName());

        // 4.关闭流
        input.close();
    }

    /**
     * 压缩一个目录
     */
    public static void output2(String dirpath, String zipfilepath) throws Exception {
        // 1.使用 File 类绑定一个文件
        // 定义要压缩的文件夹
        File file = new File(dirpath);
        // 定义压缩文件名称
        File zipFile = new File(zipfilepath);

        // 2.把 File 对象绑定到流对象上
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
        zipOut.setComment("This is zip folder.");

        // 3.进行读或写操作
        int temp = 0;
        if (file.isDirectory()) {    // 判断是否是文件夹
            File lists[] = file.listFiles();    // 列出全部文件
            for (int i = 0; i < lists.length; i++) {
                InputStream input = new FileInputStream(lists[i]);
                // 设置ZipEntry对象
                zipOut.putNextEntry(new ZipEntry(file.getName() + File.separator + lists[i].getName()));
                while ((temp = input.read()) != -1) {
                    zipOut.write(temp);
                }
                input.close();
            }
        }

        // 4.关闭流
        zipOut.close();
    }

    /**
     * 解压实体为一个目录的压缩包
     */
    public static void input2(String zipfilepath, String dirpath) throws Exception {
        // 1.使用 File 类绑定一个文件
        File file = new File(zipfilepath);
        ZipFile zipFile = new ZipFile(file);

        // 2.把 File 对象绑定到流对象上
        ZipInputStream zis = new ZipInputStream(new FileInputStream(file));

        // 3.进行读或写操作
        ZipEntry entry = null;
        while ((entry = zis.getNextEntry()) != null) {    // 得到一个压缩实体
            System.out.println("解压缩" + entry.getName() + "文件。");
            // 定义输出的文件路径
            File outFile = new File(dirpath + File.separator + entry.getName());
            if (!outFile.getParentFile().exists()) {    // 如果输出文件夹不存在
                outFile.getParentFile().mkdirs();    // 创建文件夹
            }
            if (!outFile.exists()) {    // 判断输出文件是否存在
                outFile.createNewFile();    // 创建文件
            }
            InputStream input = zipFile.getInputStream(entry);    // 得到每一个实体的输入流
            OutputStream out = new FileOutputStream(outFile);    // 实例化文件输出流
            int temp = 0;
            while ((temp = input.read()) != -1) {
                out.write(temp);
            }
            input.close();        // 关闭输入流
            out.close();    // 关闭输出流
        }

        // 4.关闭流
        zis.close();
    }

    public static void main(String[] args) throws Exception {
        final String filepath = "d:\\demo.txt";
        final String zipfilepath = "d:\\demo.zip";

        final String dirpath = "d:\\demo2";
        final String dirpath2 = "d:\\new";
        final String zipfilepath2 = "d:\\demo2.zip";

        //        demo01(ZIP_FILE_PATH);
        output1(filepath, zipfilepath);
        input1(zipfilepath, filepath);

        output2(dirpath, zipfilepath2);
        input2(zipfilepath2, dirpath2);
    }
}
```

## ObjectInputStream 和 ObjectOutputStream

**ObjectInputStream 和 ObjectOutputStream 是对象输入输出流，一般用于对象序列化。**

示例：

```java
import java.io.*;

public class ObjectStream {

    public static class Person implements Serializable {

        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "姓名：" + this.name + "；年龄：" + this.age;
        }
    }

    public static void writeObject(String filepath, Object obj[]) throws Exception {
        // 1.使用 File 类绑定一个文件
        File f = new File(filepath);

        // 2.把 File 对象绑定到流对象上
        OutputStream out = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(out);

        // 3.进行读或写操作
        oos.writeObject(obj);

        // 4.关闭流
        oos.close();
    }

    public static Object[] readObject(String filepath) throws Exception {
        // 1.使用 File 类绑定一个文件
        File f = new File(filepath);

        // 2.把 File 对象绑定到流对象上
        InputStream input = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(input);

        // 3.进行读或写操作
        Object[] objects = (Object[]) ois.readObject();

        // 4.关闭流
        ois.close();
        return objects;
    }

    public static void main(String args[]) throws Exception {
        final String filepath = "d:\\object.txt";
        Person per[] = {new Person("张三", 30), new Person("李四", 31), new Person("王五", 32)};
        writeObject(filepath, per);
        Object o[] = readObject(filepath);
        for (int i = 0; i < o.length; i++) {
            Person p = (Person) o[i];
            System.out.println(p);
        }
    }
}
```
