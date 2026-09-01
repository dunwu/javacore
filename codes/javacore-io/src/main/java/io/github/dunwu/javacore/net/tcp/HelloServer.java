package io.github.dunwu.javacore.net.tcp;

import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TCP 最简服务端：在 8888 端口监听，接受一个客户端连接并发送 "hello world"。
 * <p>
 * 配套示例：先运行本类，再运行 {@link HelloClient}。
 * 注：本示例为阻塞式服务、需要配合客户端运行，不纳入自动化测试。
 */
public class HelloServer {

    public static void main(String[] args) throws Exception {
        // Socket 服务端
        // 服务器在8888端口上监听
        ServerSocket server = new ServerSocket(8888);
        System.out.println("服务器运行中，等待客户端连接。");
        // 得到连接，程序进入到阻塞状态
        Socket client = server.accept();
        // 打印流输出最方便
        PrintStream out = new PrintStream(client.getOutputStream());
        // 向客户端输出信息
        out.println("hello world");
        client.close();
        server.close();
        System.out.println("服务器已向客户端发送消息，退出。");
    }

}
