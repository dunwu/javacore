package io.github.dunwu.javacore.net.tcp;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * TCP Echo 服务端（多线程版）：每来一个客户端连接就新建一个 {@link EchoThread} 处理，
 * 可同时服务多个客户端。
 * <p>
 * 配套示例：先运行本类，再运行 {@link EchoClient}。
 * 注：本示例为长驻服务（while(true) 循环），不纳入自动化测试。
 */
public class EchoThreadServer {

    public static void main(String[] args) throws Exception { // 所有异常抛出
        Socket client = null;
        ServerSocket server = new ServerSocket(8888); // 服务器在8888端口上监听
        while (true) {
            System.out.println("服务器运行，等待客户端连接。");
            client = server.accept(); // 得到连接，程序进入到阻塞状态
            new Thread(new EchoThread(client)).start(); // 每一个客户端表示一个线程
        }
        // server.close();
    }

}
