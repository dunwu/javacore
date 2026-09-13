package io.github.dunwu.javacore.reflect;

import java.lang.reflect.Method;

/**
 * 通过抛出异常方式 打印 Method.invoke 调用轨迹
 * <p>
 * JDK 8 的调用顺序是：先调用 DelegatingMethodAccessorImpl，然后调用 NativeMethodAccessorImpl，
 * 最后调用实际方法。JDK 18 起反射默认改用 MethodHandle 实现，默认轨迹中不再出现这两个 Accessor；
 * 加上 -Djdk.reflect.useDirectMethodHandle=false 可恢复旧行为。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-10-17
 */
public class MethodDemo01 {

    public static void target(int i) {
        new Exception("#" + i).printStackTrace();
    }

    /**
     * 通过反射调用 target 方法，观察 Method.invoke 的调用轨迹。
     */
    public static void demo() throws Exception {
        Class<?> clazz = Class.forName("io.github.dunwu.javacore.reflect.MethodDemo01");
        Method method = clazz.getMethod("target", int.class);
        method.invoke(null, 0);
    }

    public static void main(String[] args) throws Exception {
        demo();
    }

}
// Output:（JDK 21 默认配置实测。printStackTrace() 写的是 stderr，故无法用捕获 stdout 的测试断言）
// java.lang.Exception: #0
//     at io.github.dunwu.javacore.reflect.MethodDemo01.target(MethodDemo01.java:15)
//     at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
//     at java.base/java.lang.reflect.Method.invoke(Method.java:580)
//     at io.github.dunwu.javacore.reflect.MethodDemo01.demo(MethodDemo01.java:24)
//     at io.github.dunwu.javacore.reflect.MethodDemo01.main(MethodDemo01.java:28)
//
// 加上 -Djdk.reflect.useDirectMethodHandle=false 后，可复现 JDK 8 风格的 Accessor 轨迹：
// java.lang.Exception: #0
//     at io.github.dunwu.javacore.reflect.MethodDemo01.target(MethodDemo01.java:15)
//     at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
//     at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:75)
//     at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:52)
//     at java.base/java.lang.reflect.Method.invoke(Method.java:580)
//     at io.github.dunwu.javacore.reflect.MethodDemo01.demo(MethodDemo01.java:24)
//     at io.github.dunwu.javacore.reflect.MethodDemo01.main(MethodDemo01.java:28)
//
// 栈帧行号随源码改动而变，Accessor 名称与 java.base/ 模块前缀随 JDK 版本而变（JDK 8 是
// sun.reflect.* 且无模块前缀）。真实输出中每行 at 之前是一个制表符，此处按项目缩进规范写作空格。
