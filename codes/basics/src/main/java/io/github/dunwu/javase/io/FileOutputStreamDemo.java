package io.github.dunwu.javase.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Zhang Peng
 * @date 2018/4/26
 */
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
