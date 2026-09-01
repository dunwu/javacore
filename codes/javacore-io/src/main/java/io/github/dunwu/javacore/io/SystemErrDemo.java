package io.github.dunwu.javacore.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * 示例：重定向 {@link System#err} 错误输出流，将错误信息输出到内存流中。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class SystemErrDemo {

    /**
     * 演示将 System.err 重定向到 {@link ByteArrayOutputStream}，
     * 再从内存中取出重定向后的内容。
     */
    public static void demo() throws IOException {
        OutputStream bos = new ByteArrayOutputStream(); // 实例化
        PrintStream ps = new PrintStream(bos); // 实例化
        PrintStream oldErr = System.err; // 保存原始错误输出流，便于演示结束后恢复
        System.setErr(ps); // 输出重定向
        try {
            System.err.print("此处有误");
        } finally {
            System.setErr(oldErr); // 恢复原始错误输出流，避免影响后续程序
        }
        System.out.println(bos); // 输出内存中的数据
    }

    public static void main(String[] args) throws IOException {
        demo();
    }

}
