package io.github.dunwu.javacore.method;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-16
 */
public class DefaultMethodDemo {

    /**
     * 演示接口方法的实现：实现类必须实现接口中的抽象方法。
     */
    public static void demo() {
        MyInterface obj = new MyClass();
        obj.print();
    }

    public static void main(String[] args) {
        demo();
    }

    interface MyInterface {

        void print();

    }

    static class MyClass implements MyInterface {

        @Override
        public void print() {
            System.out.println("Hello World");
        }

    }

}
// Output:
// Hello World
