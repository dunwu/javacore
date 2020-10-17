package io.github.dunwu.javacore.reflect;

import java.lang.reflect.Method;

/**
 * 使用 -verbose:class 打印加载的类
 * @author peng.zhang
 * @date 2020/10/17
 */
public class MethodDemo02 {

    public static void target(int i) {
        new Exception("#" + i).printStackTrace();
    }

    public static void main(String[] args) throws Exception {
        Class<?> klass = Class.forName("io.github.dunwu.javacore.reflect.MethodDemo02");
        Method method = klass.getMethod("target", int.class);
        for (int i = 0; i < 20; i++) {
            method.invoke(null, i);
        }
    }

}
// 使用 -verbose:class 打印加载的类
// Output:
//     java.lang.Exception: #14
//     at io.github.dunwu.javacore.reflect.MethodDemo02.target(MethodDemo02.java:13)
//     at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
//     at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
//     at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
//     at java.lang.reflect.Method.invoke(Method.java:498)
//     at io.github.dunwu.javacore.reflect.MethodDemo02.main(MethodDemo02.java:20)
//     [Loaded sun.reflect.ClassFileConstants from D:\Tools\Java\jdk1.8.0_192\jre\lib\rt.jar]
//     [Loaded sun.reflect.AccessorGenerator from D:\Tools\Java\jdk1.8.0_192\jre\lib\rt.jar]
//     [Loaded sun.reflect.MethodAccessorGenerator from D:\Tools\Java\jdk1.8.0_192\jre\lib\rt.jar]
//     [Loaded sun.reflect.ByteVectorFactory from D:\Tools\Java\jdk1.8.0_192\jre\lib\rt.jar]
//     [Loaded sun.reflect.ByteVector from D:\Tools\Java\jdk1.8.0_192\jre\lib\rt.jar]
//     [Loaded sun.reflect.ByteVectorImpl from D:\Tools\Java\jdk1.8.0_192\jre\lib\rt.jar]
//     [Loaded sun.reflect.ClassFileAssembler from D:\Tools\Java\jdk1.8.0_192\jre\lib\rt.jar]
//     [Loaded sun.reflect.UTF8 from D:\Tools\Java\jdk1.8.0_192\jre\lib\rt.jar]
//     [Loaded sun.reflect.Label from D:\Tools\Java\jdk1.8.0_192\jre\lib\rt.jar]
//     [Loaded sun.reflect.Label$PatchInfo from D:\Tools\Java\jdk1.8.0_192\jre\lib\rt.jar]
//     [Loaded java.util.ArrayList$Itr from D:\Tools\Java\jdk1.8.0_192\jre\lib\rt.jar]
//     [Loaded sun.reflect.MethodAccessorGenerator$1 from D:\Tools\Java\jdk1.8.0_192\jre\lib\rt.jar]
//     [Loaded sun.reflect.ClassDefiner from D:\Tools\Java\jdk1.8.0_192\jre\lib\rt.jar]
//     [Loaded sun.reflect.ClassDefiner$1 from D:\Tools\Java\jdk1.8.0_192\jre\lib\rt.jar]
//     [Loaded sun.reflect.GeneratedMethodAccessor1 from __JVM_DefineClass__]
//     java.lang.Exception: #15
//     at io.github.dunwu.javacore.reflect.MethodDemo02.target(MethodDemo02.java:13)
//     at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
//     at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
//     at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
//     at java.lang.reflect.Method.invoke(Method.java:498)
//     at io.github.dunwu.javacore.reflect.MethodDemo02.main(MethodDemo02.java:20)
//     [Loaded java.util.concurrent.ConcurrentHashMap$ForwardingNode from D:\Tools\Java\jdk1.8.0_192\jre\lib\rt.jar]
//     java.lang.Exception: #16
//     at io.github.dunwu.javacore.reflect.MethodDemo02.target(MethodDemo02.java:13)
//     at sun.reflect.GeneratedMethodAccessor1.invoke(Unknown Source)
//     at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
//     at java.lang.reflect.Method.invoke(Method.java:498)
//     at io.github.dunwu.javacore.reflect.MethodDemo02.main(MethodDemo02.java:20)
