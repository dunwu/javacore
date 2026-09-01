package io.github.dunwu.javacore.reflect;

/**
 * Class.forName 获取 Class 对象
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-26
 */
public class ReflectClassDemo01 {

    /**
     * 演示 Class.forName 获取 Class 对象（包括数组类型）。
     */
    public static void demo() throws ClassNotFoundException {
        Class c1 = Class.forName("io.github.dunwu.javacore.reflect.ReflectClassDemo01");
        System.out.println(c1.getCanonicalName());

        Class c2 = Class.forName("[D");
        System.out.println(c2.getCanonicalName());

        Class c3 = Class.forName("[[Ljava.lang.String;");
        System.out.println(c3.getCanonicalName());
    }

    public static void main(String[] args) throws ClassNotFoundException {
        demo();
    }

}
// Output:
// io.github.dunwu.javacore.reflect.ReflectClassDemo01
// double[]
// java.lang.String[][]
