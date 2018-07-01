package io.github.dunwu.javacore.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * RandomAccessFile 写操作示例
 * 注：RandomAccessFile 读写文件操作较为麻烦，更建议使用字节流或字符流方法。
 *
 * @author Zhang Peng
 * @date 2018/4/26
 * @see RandomAccessFileDemo02
 */
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
