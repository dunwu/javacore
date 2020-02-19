package io.github.dunwu.javacore.bio.bytes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class PrintStreamDemo {

    public static void main(String[] arg) throws Exception {
        final String filepath = "d:\\test.txt";
        // 如果现在是使用 FileOuputStream 实例化，意味着所有的数据都会输出到文件中
        OutputStream os = new FileOutputStream(new File(filepath));
        PrintStream ps = new PrintStream(os);
        ps.print("Hello ");
        ps.println("World!!!");
        ps.printf("姓名：%s；年龄：%d", "张三", 18);
        ps.close();
    }

}
