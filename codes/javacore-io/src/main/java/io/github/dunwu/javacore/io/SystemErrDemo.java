package io.github.dunwu.javacore.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class SystemErrDemo {

    public static void main(String[] args) throws IOException {
        OutputStream bos = new ByteArrayOutputStream(); // 实例化
        PrintStream ps = new PrintStream(bos); // 实例化
        System.setErr(ps); // 输出重定向
        System.err.print("此处有误");
        System.out.println(bos); // 输出内存中的数据
    }

}
