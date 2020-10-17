package io.github.dunwu.javacore.reflect;

import java.lang.reflect.Method;

/**
 * 通过抛出异常方式 打印 Method.invoke 调用轨迹
 * <p>
 * 先调用 DelegatingMethodAccessorImpl；然后调用 NativeMethodAccessorImpl，最后调用实际方法
 * @author peng.zhang
 * @date 2020/10/17
 */
public class MethodDemo01 {

    public static void target(int i) {
        new Exception("#" + i).printStackTrace();
    }

    public static void main(String[] args) throws Exception {
        Class<?> clazz = Class.forName("io.github.dunwu.javacore.reflect.MethodDemo01");
        Method method = clazz.getMethod("target", int.class);
        method.invoke(null, 0);
    }

}
// Output:
// java.lang.Exception: #0
//     at io.github.dunwu.javacore.reflect.MethodDemo01.target(MethodDemo01.java:12)
//     at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
//     at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
