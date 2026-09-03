package io.github.dunwu.javacore.io;

import io.github.dunwu.javacore.DemoFiles;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * 示例：重定向 {@link System#out} 输出流，将控制台输出保存到文件。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class SystemOutDemo {

    /**
     * 重定向的目标文件路径。统一写到 {@code target/} 目录下，避免污染仓库工作目录，详见 {@link DemoFiles}
     */
    public static final String FILE_PATH = DemoFiles.tempPath("temp_stdout.txt");

    /**
     * 演示将 System.out 重定向到文件：重定向后，System.out.println 的内容会写入文件。
     */
    public static void demo() throws Exception {
        PrintStream oldOut = System.out; // 保存原始输出流，便于演示结束后恢复（避免影响后续程序）
        OutputStream out = new FileOutputStream(FILE_PATH);
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);
        try {
            System.out.println("人生若只如初见，何事秋风悲画扇");
        } finally {
            ps.close();
            out.close();
            System.setOut(oldOut); // 恢复原始输出流，否则后续 println 仍会写入文件
        }
        System.out.println("输出已重定向到文件：" + FILE_PATH);
    }

    public static void main(String[] args) throws Exception {
        demo();
    }

}
