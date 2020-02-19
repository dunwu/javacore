package io.github.dunwu.javacore.net.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

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
