package io.github.dunwu.javacore.bio.chars;

import java.io.*;

/**
 * 转换流示例：InputStreamReader 把字节输入流转为字符输入流，
 * 适合读取文本文件（自动按平台默认编码解码字节为字符）。
 * <p>本例先写入一个文本文件，再用 InputStreamReader 读取，保证示例自包含。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class InputStreamReaderDemo {

    /** 演示把 FileInputStream 包装为 Reader 读取文本内容。 */
    public static void demo() throws IOException {
        File f = new File("temp.log");
        // 先准备一个文本文件（真实场景中文件通常已存在）
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(f))) {
            writer.write("hello world!!");
        }
        Reader reader = new InputStreamReader(new FileInputStream(f));
        char[] c = new char[1024];
        int len = reader.read(c);
        reader.close();
        System.out.println(new String(c, 0, len));
    }

    public static void main(String[] args) throws IOException {
        demo();
    }

}
