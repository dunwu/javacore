package io.github.dunwu.javacore.method;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-16
 */
public class MainMethodDemo {

    public static void main(String[] args) {
        for (String arg : args) {
            System.out.println("arg = [" + arg + "]");
        }
    }

}
// 依次执行
// javac MainMethodDemo.java
// java MainMethodDemo A B C
// Output
// arg = [A]
// arg = [B]
// arg = [C]
