package io.github.dunwu.javacore.method;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-16
 */
public class DefaultMethodDemo {

    /**
     * 演示接口默认方法：实现类无需重写即可继承默认实现。
     */
    public static void demo() {
        MyInterface obj = new MyClass();
        obj.print();
    }

    public static void main(String[] args) {
        demo();
    }

    interface MyInterface {

        default void print() {
            System.out.println("Hello World");
        }

    }

    static class MyClass implements MyInterface {}

}
// Output:
// Hello World
