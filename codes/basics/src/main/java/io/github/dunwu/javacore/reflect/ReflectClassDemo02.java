package io.github.dunwu.javacore.reflect;

/**
 * @author Zhang Peng
 * @date 2019-03-26
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
//Output:
//boolean
//java.io.PrintStream
//int[][][]
