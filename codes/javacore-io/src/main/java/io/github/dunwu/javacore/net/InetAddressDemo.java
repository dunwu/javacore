package io.github.dunwu.javacore.net;

import java.net.InetAddress;

/**
 * {@link InetAddress} 示例：获取本机地址、解析域名、测试可达性。
 * <p>
 * 注：本示例依赖网络与 DNS 解析，不纳入自动化测试。
 */
public class InetAddressDemo {

    public static void main(String[] args) throws Exception {// 所有异常抛出
        InetAddress locAdd = InetAddress.getLocalHost();
        InetAddress remAdd = InetAddress.getByName("www.baidu.com");
        System.out.println("本机的IP地址：" + locAdd.getHostAddress());
        System.out.println("www.baidu.com 的IP地址：" + remAdd.getHostAddress());
        System.out.println("本机是否可达：" + locAdd.isReachable(5000));
    }

}
