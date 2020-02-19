package io.github.dunwu.javacore.net.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class EchoThread implements Runnable {

    private Socket client = null;

    public EchoThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        BufferedReader buf = null; // 接收输入流
        PrintStream out = null; // 打印流输出最方便
        try {
            out = new PrintStream(client.getOutputStream());
            // 准备接收客户端的输入信息
            buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
            boolean flag = true; // 标志位，表示可以一直接收并回应信息
            while (flag) {
                String str = buf.readLine(); // 接收客户端发送的内容
                if (str == null || "".equals(str)) { // 表示没有内容
                    flag = false; // 退出循环
                } else {
                    if ("bye".equals(str)) { // 如果输入的内容为bye表示结束
                        flag = false;
                    } else {
                        out.println("ECHO : " + str); // 回应信息
                    }
                }
            }
            client.close();
        } catch (Exception e) {
        }
    }

}
