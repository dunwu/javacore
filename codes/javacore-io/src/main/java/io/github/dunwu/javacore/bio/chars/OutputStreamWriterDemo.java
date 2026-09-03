package io.github.dunwu.javacore.bio.chars;

import io.github.dunwu.javacore.DemoFiles;

import java.io.*;

/**
 * 转换流示例：OutputStreamWriter 把字节输出流转为字符输出流，
 * 适合写文本文件（字符按平台默认编码编码为字节）。
 * <p>本例写入后立即读回验证，保证示例自包含。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class OutputStreamWriterDemo {

    /**
     * 示例使用的数据文件路径。统一写到 {@code target/} 目录下，避免污染仓库工作目录，详见 {@link DemoFiles}。
     * <p>
     * 文件名带上示例名作为前缀，避开本模块其他示例（旧代码中多个示例共用 {@code temp.log}，会互相覆盖）。
     */
    private static final String FILEPATH = DemoFiles.tempPath("temp_outputstreamwriter.log");

    /** 演示把 FileOutputStream 包装为 Writer 写文本，并读回验证。 */
    public static void demo() throws IOException {
        File f = new File(FILEPATH);
        Writer out = new OutputStreamWriter(new FileOutputStream(f));
        out.write("hello world!!");
        out.close();

        // 读回验证写入结果
        Reader in = new InputStreamReader(new FileInputStream(f));
        char[] c = new char[1024];
        int len = in.read(c);
        in.close();
        System.out.println("写入文件的内容：" + new String(c, 0, len));
    }

    public static void main(String[] args) throws IOException {
        demo();
    }

}
