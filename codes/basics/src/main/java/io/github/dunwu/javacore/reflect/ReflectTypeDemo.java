package io.github.dunwu.javacore.reflect;

/**
 * @author Zhang Peng
 * @date 2019-03-26
 */
public class ReflectTypeDemo {
    public static void main(String[] args) {
        Class c1 = Double.TYPE;
        System.out.println(c1.getCanonicalName());

        Class c2 = Void.TYPE;
        System.out.println(c2.getCanonicalName());
    }
}
