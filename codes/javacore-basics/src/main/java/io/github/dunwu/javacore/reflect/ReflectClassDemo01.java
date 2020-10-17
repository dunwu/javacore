package io.github.dunwu.javacore.reflect;

/**
 * Class.forName 获取 Class 对象
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-26
 */
public class ReflectClassDemo01 {

    public static void main(String[] args) throws ClassNotFoundException {
        Class c1 = Class.forName("io.github.dunwu.javacore.reflect.ReflectClassDemo01");
        System.out.println(c1.getCanonicalName());

        Class c2 = Class.forName("[D");
        System.out.println(c2.getCanonicalName());

        Class c3 = Class.forName("[[Ljava.lang.String;");
        System.out.println(c3.getCanonicalName());
    }

}
// Output:
// io.github.dunwu.javacore.reflect.ReflectClassDemo01
// double[]
// java.lang.String[][]
