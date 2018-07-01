package io.github.dunwu.javacore.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;

/**
 * 合并流示例
 * 合并流的主要功能是将多个 InputStream 合并为一个 InputStream 流。
 *
 * @author Zhang Peng
 * @date 2018/4/26
 */
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
