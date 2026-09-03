package io.github.dunwu.javacore.bio.bytes;

import io.github.dunwu.javacore.DemoFiles;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * 打印流示例：把 PrintStream 接到文件上，用 print/println/printf 格式化输出（本例输出到 {@code target/} 下的临时文件）。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class PrintStreamDemo {

    /**
     * 打印流输出的目标文件路径。统一写到 {@code target/} 目录下，避免污染仓库工作目录，详见 {@link DemoFiles}
     */
    public static final String FILE_PATH = DemoFiles.tempPath("temp_print.txt");

    /** 演示用 PrintStream 向文件格式化输出。 */
    public static void demo() throws Exception {
        // 如果现在是使用 FileOutputStream 实例化，意味着所有的数据都会输出到文件中
        OutputStream os = new FileOutputStream(new File(FILE_PATH));
        PrintStream ps = new PrintStream(os);
        ps.print("Hello ");
        ps.println("World!!!");
        ps.printf("姓名：%s；年龄：%d", "张三", 18);
        ps.close();
    }

    public static void main(String[] args) throws Exception {
        demo();
    }

}
