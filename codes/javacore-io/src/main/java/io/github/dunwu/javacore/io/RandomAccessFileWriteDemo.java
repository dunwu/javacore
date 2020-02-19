package io.github.dunwu.javacore.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * {@link RandomAccessFile} 写操作示例。本示例生成的临时文件可以使用 {@link RandomAccessFileReadDemo} 示例读取。
 * <p>
 * 注：{@link RandomAccessFile} 读写文件操作较为麻烦，更建议使用字节流或字符流方法。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see RandomAccessFileReadDemo
 * @since 2018/4/26
 */
public class RandomAccessFileWriteDemo {

    public static void main(String[] args) throws IOException {

        // 指定要操作的文件
        File f = new File("temp.log");

        // 声明RandomAccessFile类的对象，读写模式，如果文件不存在，会自动创建
        RandomAccessFile rdf = new RandomAccessFile(f, "rw");

        // 写入一组记录
        String name = "zhangsan";
        int age = 30;
        rdf.writeBytes(name);
        rdf.writeInt(age);

        // 写入一组记录
        name = "lisi    ";
        age = 31;
        rdf.writeBytes(name);
        rdf.writeInt(age);

        // 写入一组记录
        name = "wangwu  ";
        age = 32;
        rdf.writeBytes(name);
        rdf.writeInt(age);

        // 关闭
        rdf.close();
    }

}
