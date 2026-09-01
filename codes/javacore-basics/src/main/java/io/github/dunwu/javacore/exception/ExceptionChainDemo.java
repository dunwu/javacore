package io.github.dunwu.javacore.exception;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-11
 */
public class ExceptionChainDemo {

    /**
     * 演示异常链：f1 抛出的 MyException1 在 f2 中被包装为 MyException2 继续上抛。
     */
    public static void demo() throws MyException2 {
        f2();
    }

    public static void main(String[] args) throws MyException2 {
        demo();
    }

    public static void f2() throws MyException2 {
        try {
            f1();
        } catch (MyException1 e) {
            throw new MyException2("出现 MyException2", e);
        }
    }

    public static void f1() throws MyException1 {
        throw new MyException1("出现 MyException1");
    }

    static class MyException1 extends Exception {

        public MyException1(String message) {
            super(message);
        }

    }

    static class MyException2 extends Exception {

        public MyException2(String message, Throwable cause) {
            super(message, cause);
        }

    }

}
