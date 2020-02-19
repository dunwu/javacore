package io.github.dunwu.javacore.io;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * 重定向 System.out 输出流
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class SystemOutDemo {

    public static void main(String[] args) throws Exception {
        OutputStream out = new FileOutputStream("d:\\test.txt");
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);
        System.out.println("人生若只如初见，何事秋风悲画扇");
        ps.close();
        out.close();
    }

}
