package io.github.dunwu.javacore.jvm.classloader.exception;

/**
 * ClassNotFoundException 示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-03-07
 */
public class ClassNotFoundExceptionDemo {

    public static void main(String[] args) {
        try {
            Class.forName("NotFound");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
