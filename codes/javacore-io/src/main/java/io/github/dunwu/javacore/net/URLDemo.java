package io.github.dunwu.javacore.net;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

/**
 * {@link URL} 示例：通过 URL 直接打开网络输入流并逐行读取网页内容。
 * <p>
 * 注：本示例依赖网络访问，不纳入自动化测试。
 */
public class URLDemo {

    public static void main(String[] args) throws Exception { // 所有异常抛出
        URL url = new URL("https", "www.baidu.com", 443, "/");
        InputStream input = url.openStream(); // 打开输入流
        Scanner scan = new Scanner(input); // 实例化Scanner类
        scan.useDelimiter("\n"); // 设置读取分隔符
        while (scan.hasNext()) {
            System.out.println(scan.next());
        }
    }

}
