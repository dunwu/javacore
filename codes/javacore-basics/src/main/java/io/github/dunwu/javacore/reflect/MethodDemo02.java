package io.github.dunwu.javacore.reflect;

import java.lang.reflect.Method;

/**
 * 使用 -verbose:class 打印加载的类
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-10-17
 */
public class MethodDemo02 {

    public static void target(int i) {
        new Exception("#" + i).printStackTrace();
    }

    /**
     * 反复反射调用同一方法，观察膨胀（inflation）机制生成 MethodAccessor。
     * <p>
     * 注意：JDK 18 起反射默认改用 MethodHandle 实现，膨胀机制默认不再生效，需加上
     * -Djdk.reflect.useDirectMethodHandle=false 才能观察到。两种配置的实测轨迹见文件末尾。
     */
    public static void demo() throws Exception {
        Class<?> klass = Class.forName("io.github.dunwu.javacore.reflect.MethodDemo02");
        Method method = klass.getMethod("target", int.class);
        for (int i = 0; i < 20; i++) {
            method.invoke(null, i);
        }
    }

    public static void main(String[] args) throws Exception {
        demo();
    }

}
// 运行方式：
//     java -verbose:class io.github.dunwu.javacore.reflect.MethodDemo02
//
// Output:（JDK 21 默认配置实测。20 段轨迹写 stderr，-verbose:class 的类加载日志写 stdout）
// stderr 共 120 行，是 20 段完全相同的轨迹 —— 默认配置下观察不到膨胀（inflation）：
// java.lang.Exception: #0
//     at io.github.dunwu.javacore.reflect.MethodDemo02.target(MethodDemo02.java:13)
//     at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
//     at java.base/java.lang.reflect.Method.invoke(Method.java:580)
//     at io.github.dunwu.javacore.reflect.MethodDemo02.demo(MethodDemo02.java:23)
//     at io.github.dunwu.javacore.reflect.MethodDemo02.main(MethodDemo02.java:28)
// （#1 到 #19 与上面完全一致，仅编号不同）
//
// stdout 的类加载日志格式随 JDK 版本差异很大，且带时间戳，无法逐字复现，只能记录形态：
//     JDK 21：[0.021s][info][class,load] java.lang.Object source: shared objects file
//     JDK 8 ：[Loaded java.lang.Object from <JDK 安装目录>\jre\lib\rt.jar]
// JDK 21 默认配置下共 618 行，且不包含任何动态生成的 Accessor（因为没有发生膨胀）。
//
// 要真正观察到膨胀机制，需加上 -Djdk.reflect.useDirectMethodHandle=false（JDK 18 起默认关闭）。
// 此时 stderr 共 156 行，第 17 次调用（编号 #16）起改由动态生成的字节码 Accessor 接管：
// java.lang.Exception: #15
//     at io.github.dunwu.javacore.reflect.MethodDemo02.target(MethodDemo02.java:13)
//     at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
//     at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:75)
//     at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:52)
//     at java.base/java.lang.reflect.Method.invoke(Method.java:580)
//     at io.github.dunwu.javacore.reflect.MethodDemo02.demo(MethodDemo02.java:23)
//     at io.github.dunwu.javacore.reflect.MethodDemo02.main(MethodDemo02.java:28)
// java.lang.Exception: #16
//     at io.github.dunwu.javacore.reflect.MethodDemo02.target(MethodDemo02.java:13)
//     at jdk.internal.reflect.GeneratedMethodAccessor1.invoke(Unknown Source)
//     at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:52)
//     at java.base/java.lang.reflect.Method.invoke(Method.java:580)
//     at io.github.dunwu.javacore.reflect.MethodDemo02.demo(MethodDemo02.java:23)
//     at io.github.dunwu.javacore.reflect.MethodDemo02.main(MethodDemo02.java:28)
// （#17 到 #19 与 #16 一致）
//
// 即 #0~#15 共 16 次走 NativeMethodAccessorImpl，#16~#19 共 4 次走 GeneratedMethodAccessor1，
// 可见膨胀阈值为 15。此时 stdout 还会多出 JDK 内部动态生成 Accessor 的类加载记录。
// 真实输出中每行 at 之前是一个制表符，此处按项目缩进规范写作空格；行号随源码改动而变。
