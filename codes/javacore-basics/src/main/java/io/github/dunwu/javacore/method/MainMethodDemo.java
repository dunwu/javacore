package io.github.dunwu.javacore.method;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-16
 */
public class MainMethodDemo {

    /**
     * 演示 main 方法的命令行参数：逐个打印 args 数组元素。
     */
    public static void demo(String[] args) {
        for (String arg : args) {
            System.out.println("arg = [" + arg + "]");
        }
    }

    public static void main(String[] args) {
        demo(args);
    }

}
// 依次执行
// javac MainMethodDemo.java
// java MainMethodDemo A B C
// Output
// arg = [A]
// arg = [B]
// arg = [C]
