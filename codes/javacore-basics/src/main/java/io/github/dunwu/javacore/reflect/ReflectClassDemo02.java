package io.github.dunwu.javacore.reflect;

/**
 * 类名 + .class 获取 Class 对象
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-26
 */
public class ReflectClassDemo02 {

    public static void main(String[] args) {
        boolean b;
        // Class c = b.getClass(); // 编译错误
        Class c1 = boolean.class;
        System.out.println(c1.getCanonicalName());

        Class c2 = java.io.PrintStream.class;
        System.out.println(c2.getCanonicalName());

        Class c3 = int[][][].class;
        System.out.println(c3.getCanonicalName());
    }

}
// Output:
// boolean
// java.io.PrintStream
// int[][][]
