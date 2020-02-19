package io.github.dunwu.javacore.bio.bytes;

import java.io.*;

/**
 * 合并流示例 合并流的主要功能是将多个 InputStream 合并为一个 InputStream 流。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018/4/26
 */
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
