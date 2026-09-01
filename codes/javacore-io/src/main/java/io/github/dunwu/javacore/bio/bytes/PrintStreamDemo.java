package io.github.dunwu.javacore.bio.bytes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * 打印流示例：把 PrintStream 接到文件上，用 print/println/printf 格式化输出（本例输出到相对路径的临时文件）。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class PrintStreamDemo {

    /** 演示用 PrintStream 向文件格式化输出。 */
    public static void demo() throws Exception {
        final String filepath = "temp_print.txt";
        // 如果现在是使用 FileOuputStream 实例化，意味着所有的数据都会输出到文件中
        OutputStream os = new FileOutputStream(new File(filepath));
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
