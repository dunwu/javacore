package io.github.dunwu.javacore.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * System.in 示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class SystemInDemo {

    public static void main(String[] args) throws IOException {
        InputStream input = System.in;
        StringBuffer buf = new StringBuffer();
        System.out.print("请输入内容：");
        int temp = 0;
        while ((temp = input.read()) != -1) {
            char c = (char) temp;
            if (c == '\n') {
                break;
            }
            buf.append(c);
        }
        System.out.println("输入的内容为：" + buf);
        input.close();
    }

}
