package io.github.dunwu.javase.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Zhang Peng
 * @date 2018/4/26
 */
public class FileInputStreamDemo {

    private static void read1(InputStream input, byte[] b) throws IOException {
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
