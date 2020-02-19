package io.github.dunwu.javacore.exception;

/**
 * 自定义异常示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-11
 */
public class MyExceptionDemo {

    public static void main(String[] args) {
        throw new MyException("自定义异常");
    }

    static class MyException extends RuntimeException {

        public MyException(String message) {
            super(message);
        }

    }

}
