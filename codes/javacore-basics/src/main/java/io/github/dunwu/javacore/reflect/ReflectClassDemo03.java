package io.github.dunwu.javacore.reflect;

import java.util.HashSet;
import java.util.Set;

/**
 * Object 的 getClass 方法获取 Class 对象
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-26
 */
public class ReflectClassDemo03 {

    public static void main(String[] args) {
        Class c = "foo".getClass();
        System.out.println(c.getCanonicalName());

        Class c2 = ReflectClassDemo03.E.A.getClass();
        System.out.println(c2.getCanonicalName());

        byte[] bytes = new byte[1024];
        Class c3 = bytes.getClass();
        System.out.println(c3.getCanonicalName());

        Set<String> set = new HashSet<>();
        Class c4 = set.getClass();
        System.out.println(c4.getCanonicalName());
    }

    enum E {

        A,
        B
    }

}
// Output:
// java.lang.String
// io.github.dunwu.javacore.reflect.ReflectClassDemo.E
// byte[]
// java.util.HashSet
