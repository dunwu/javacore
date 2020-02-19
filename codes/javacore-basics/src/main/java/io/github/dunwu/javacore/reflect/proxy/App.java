package io.github.dunwu.javacore.reflect.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @title App
 * @description 动态代理范例
 * @since 2016年8月5日
 */
public class App {

    public static void main(String[] args) {
        // 初始化真实对象
        Purchaser zhangsan = new Consumer("张三");

        // 将真实对象托管给动态对象
        InvocationHandler handler = new Amazon(zhangsan);

        // 通过Proxy.newProxyInstance构建代理对象
        Purchaser proxy = (Purchaser) Proxy.newProxyInstance(zhangsan.getClass().getClassLoader(),
            zhangsan.getClass().getInterfaces(), handler);

        // 通过调用代理对象的方法去调用真实对象的方法
        proxy.purchase("进口奶粉");
    }

}
