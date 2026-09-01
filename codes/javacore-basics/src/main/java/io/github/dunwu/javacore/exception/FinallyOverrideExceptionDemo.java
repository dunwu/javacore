package io.github.dunwu.javacore.exception;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-11
 */
public class FinallyOverrideExceptionDemo {

    /**
     * 演示 finally 中抛异常会覆盖 try/catch 中的异常，导致原始异常信息丢失。
     */
    public static void demo() {
        try {
            f();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        demo();
    }

    static void f() throws Exception {
        try {
            throw new Exception("A");
        } catch (Exception e) {
            throw new Exception("B");
        } finally {
            throw new Exception("C");
        }
    }

}
