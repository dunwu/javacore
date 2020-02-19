package io.github.dunwu.javacore.exception;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-11
 */
public class FinallyOverrideExceptionDemo {

    public static void main(String[] args) {
        try {
            f();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
