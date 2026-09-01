package io.github.dunwu.javacore.method;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-16
 */
public class MethodParamDemo2 {

    /**
     * 演示引用类型参数传值：方法内重新赋值形参不影响实参指向的对象。
     */
    public static void demo() {
        StringBuilder sb = new StringBuilder("A");
        System.out.println("sb = [" + sb.toString() + "]");
        method(sb);
        System.out.println("sb = [" + sb.toString() + "]");
        sb = new StringBuilder("C");
        System.out.println("sb = [" + sb.toString() + "]");
    }

    public static void main(String[] args) {
        demo();
    }

    public static void method(StringBuilder sb) {
        sb = new StringBuilder("B");
    }

}
// Output:
// sb = [A]
// sb = [A]
// sb = [C]
