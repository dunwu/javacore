package io.github.dunwu.javacore.exception;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-11
 */
public class ExceptionChainDemo {

    public static void main(String[] args) throws MyException2 {
        f2();
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
