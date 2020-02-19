package io.github.dunwu.javacore.method;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-16
 */
public class MethodParamDemo {

    public static void main(String[] args) {
        int num = 0;
        method(num);
        System.out.println("num = [" + num + "]");
        method(num);
        System.out.println("num = [" + num + "]");
    }

    public static void method(int value) {
        value = value + 1;
    }

}
// Output:
// num = [0]
// num = [0]
