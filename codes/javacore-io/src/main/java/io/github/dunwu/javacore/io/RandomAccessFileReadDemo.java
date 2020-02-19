package io.github.dunwu.javacore.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * {@link RandomAccessFile} 读操作示例。本示例读取 {@link RandomAccessFileWriteDemo} 示例生成的临时文件。
 *
 * 注：{@link RandomAccessFile} 读写文件操作较为麻烦，更建议使用字节流或字符流方法。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see RandomAccessFileWriteDemo
 * @since 2018/4/26
 */
public class RandomAccessFileReadDemo {

    public static void main(String[] args) throws IOException {

        // 指定要操作的文件
        File f = new File("temp.log");

        // 声明RandomAccessFile类的对象，以只读的方式打开文件
        RandomAccessFile rdf = null;
        rdf = new RandomAccessFile(f, "r");

        byte[] b = new byte[8];

        // 读取第二个人的信息，意味着要空出第一个人的信息
        rdf.skipBytes(12); // 跳过第一个人的信息
        for (int i = 0; i < b.length; i++) {
            b[i] = rdf.readByte(); // 读取一个字节
        }
        String name = new String(b); // 将读取出来的byte数组变为字符串

        int age = rdf.readInt();  // 读取数字
        System.out.println("第二个人的信息 --> 姓名：" + name + "；年龄：" + age);

        // 读取第一个人的信息
        rdf.seek(0); // 指针回到文件的开头
        for (int i = 0; i < b.length; i++) {
            b[i] = rdf.readByte(); // 读取一个字节
        }
        name = new String(b); // 将读取出来的byte数组变为字符串
        age = rdf.readInt(); // 读取数字
        System.out.println("第一个人的信息 --> 姓名：" + name + "；年龄：" + age);

        // 读取第三个人的信息
        rdf.skipBytes(12); // 空出第二个人的信息
        for (int i = 0; i < b.length; i++) {
            b[i] = rdf.readByte(); // 读取一个字节
        }
        name = new String(b); // 将读取出来的byte数组变为字符串
        age = rdf.readInt(); // 读取数字
        System.out.println("第三个人的信息 --> 姓名：" + name + "；年龄：" + age);

        // 关闭
        rdf.close();
    }

}
