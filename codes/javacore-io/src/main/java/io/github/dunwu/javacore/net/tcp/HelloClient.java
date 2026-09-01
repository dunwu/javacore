package io.github.dunwu.javacore.net.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * TCP 最简客户端：连接 {@link HelloServer}，接收一条消息后退出。
 * <p>
 * 配套示例：先运行 {@link HelloServer}，再运行本类。
 * 注：本示例需要配合服务端运行，不纳入自动化测试。
 */
public class HelloClient {

    public static void main(String[] args) throws Exception {
        // Socket 客户端
        Socket client = new Socket("localhost", 8888);
        InputStreamReader inputStreamReader = new InputStreamReader(client.getInputStream());
        // 一次性接收完成
        BufferedReader buf = new BufferedReader(inputStreamReader);
        String str = buf.readLine();
        buf.close();
        client.close();
        System.out.println("客户端接收到服务器消息：" + str + "，退出");
    }

}
