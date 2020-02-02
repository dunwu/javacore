package io.github.dunwu.javacore.net.tcp;

import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HelloServer {

    public static void main(String[] args) throws Exception {
        // Socket 服务端
        // 服务器在8888端口上监听
        ServerSocket server = new ServerSocket(8888);
        // 得到连接，程序进入到阻塞状态
        Socket client = server.accept();
        System.out.println("服务器运行，等待客户端连接。");
        // 打印流输出最方便
        PrintStream out = new PrintStream(client.getOutputStream());
        // 向客户端输出信息
        out.println("hello world");
        client.close();
        server.close();
    }

};
