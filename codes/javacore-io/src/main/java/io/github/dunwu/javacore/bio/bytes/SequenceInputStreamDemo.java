package io.github.dunwu.javacore.bio.bytes;

import io.github.dunwu.javacore.DemoFiles;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 合并流示例：SequenceInputStream 将多个 InputStream 合并为一个，读起来如同一个连续的流。
 * <p>本例先准备两个小文件，再合并读取并写入第三个文件，最后输出合并结果。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018/4/26
 */
public class SequenceInputStreamDemo {

    /**
     * 两个待合并的源文件路径。统一写到 {@code target/} 目录下，避免污染仓库工作目录，详见 {@link DemoFiles}
     */
    private static final String FILE_PATH_1 = DemoFiles.tempPath("temp_sequence1.log");

    private static final String FILE_PATH_2 = DemoFiles.tempPath("temp_sequence2.log");

    /**
     * 合并结果的输出文件路径
     */
    private static final String FILE_PATH_3 = DemoFiles.tempPath("temp_sequence3.log");

    /** 演示把两个文件的输入流合并后一次性读出并写入新文件。 */
    public static void demo() throws Exception {
        // 准备两个待合并的源文件（真实场景中这两个文件通常已存在）
        try (OutputStream os1 = new FileOutputStream(FILE_PATH_1);
             OutputStream os2 = new FileOutputStream(FILE_PATH_2)) {
            os1.write("Hello ".getBytes(StandardCharsets.UTF_8));
            os2.write("World!".getBytes(StandardCharsets.UTF_8));
        }

        InputStream is1 = new FileInputStream(FILE_PATH_1);
        InputStream is2 = new FileInputStream(FILE_PATH_2);
        SequenceInputStream sis = new SequenceInputStream(is1, is2);

        int temp; // 接收内容
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStream os = new FileOutputStream(FILE_PATH_3);
        while ((temp = sis.read()) != -1) { // 循环输出
            os.write(temp); // 保存内容
            bos.write(temp);
        }

        sis.close(); // 关闭合并流
        is1.close(); // 关闭输入流1
        is2.close(); // 关闭输入流2
        os.close(); // 关闭输出流
        System.out.println("合并后的内容：" + bos);
    }

    public static void main(String[] args) throws Exception {
        demo();
    }

}
