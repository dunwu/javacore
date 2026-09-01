package io.github.dunwu.javacore.oop;

/**
 * 通过类的完全限定名使用其它包的类
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-16
 */
public class PackageDemo {

    /**
     * 通过完全限定名创建 java.util.Date 对象。
     */
    public static void demo() {
        System.out.println(new java.util.Date());
    }

    public static void main(String[] args) {
        demo();
    }

}
