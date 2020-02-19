package io.github.dunwu.javacore.net.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {

    public static void main(String[] args) throws Exception { // 所有异常抛出
        String str = "hello World!!!";
        DatagramSocket ds = new DatagramSocket(3000); // 服务端在3000端口上等待服务器发送信息
        DatagramPacket dp =
            new DatagramPacket(str.getBytes(), str.length(), InetAddress.getByName("localhost"), 9000); // 所有的信息使用buf保存
        System.out.println("发送信息。");
        ds.send(dp); // 发送信息出去
        ds.close();
    }

}
