package io.github.dunwu.javacore.net.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

    public static void main(String[] args) throws Exception { // 所有异常抛出
        ServerSocket server = new ServerSocket(8888); // 服务器在8888端口上监听
        boolean flag = true;
        while (flag) {
            System.out.println("服务器运行，等待客户端连接。");
            Socket client = server.accept(); // 得到连接，程序进入到阻塞状态
            PrintStream out = new PrintStream(client.getOutputStream());
            // 准备接收客户端的输入信息
            BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
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
            break;
        }
        server.close();
    }

}
