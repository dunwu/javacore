package io.github.dunwu.javacore.net;

import java.net.URL;
import java.net.URLConnection;

/**
 * {@link URLConnection} 示例：建立连接后读取响应头中的内容大小、内容类型。
 * <p>
 * 注：本示例依赖网络访问，不纳入自动化测试。
 */
public class URLConnectionDemo {

    public static void main(String[] args) throws Exception { // 所有异常抛出
        URL url = new URL("https://www.baidu.com");
        URLConnection urlCon = url.openConnection(); // 建立连接
        System.out.println("内容大小：" + urlCon.getContentLength());
        System.out.println("内容类型：" + urlCon.getContentType());
    }

}
