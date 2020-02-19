package io.github.dunwu.javacore.bio.chars;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class BufferedReaderDemo {

    public static void main(String[] args) throws IOException {
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("请输入内容：");
            String str = buf.readLine();
            if ("exit".equalsIgnoreCase(str)) {
                System.out.print("退出");
                break;
            }
            System.out.println("输入的内容为：" + str);
        }
    }

}
