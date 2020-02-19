package io.github.dunwu.javacore.net.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPClient {

    public static void main(String[] args) throws Exception { // 所有异常抛出
        byte[] buf = new byte[1024]; // 开辟空间，以接收数据
        DatagramSocket ds = new DatagramSocket(9000); // 客户端在9000端口上等待服务器发送信息
        DatagramPacket dp = new DatagramPacket(buf, 1024); // 所有的信息使用buf保存
        ds.receive(dp); // 接收数据
        String str = new String(dp.getData(), 0, dp.getLength()) + "from " + dp.getAddress().getHostAddress() + "："
            + dp.getPort();
        System.out.println(str); // 输出内容
    }

}
