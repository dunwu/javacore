package io.github.dunwu.javacore.exception;

/**
 * 自定义异常示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-11
 */
public class MyExceptionDemo {

    /**
     * 反例：直接抛出未被捕获的自定义异常，程序终止。
     */
    public static void demo() {
        throw new MyException("自定义异常");
    }

    public static void main(String[] args) {
        demo();
    }

    static class MyException extends RuntimeException {

        public MyException(String message) {
            super(message);
        }

    }

}
