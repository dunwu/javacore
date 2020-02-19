package io.github.dunwu.javacore.net.tcp;

import java.net.ServerSocket;
import java.net.Socket;

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
