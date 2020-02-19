package io.github.dunwu.javacore.net;

import java.net.InetAddress;

public class InetAddressDemo {

    public static void main(String[] args) throws Exception {// 所有异常抛出
        InetAddress locAdd = InetAddress.getLocalHost();
        InetAddress remAdd = InetAddress.getByName("www.baidu.com");
        System.out.println("本机的IP地址：" + locAdd.getHostAddress());
        System.out.println("www.baidu.com 的IP地址：" + remAdd.getHostAddress());
        System.out.println("本机是否可达：" + locAdd.isReachable(5000));
    }

}
