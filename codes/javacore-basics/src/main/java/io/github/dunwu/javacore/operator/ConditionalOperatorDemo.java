package io.github.dunwu.javacore.operator;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class ConditionalOperatorDemo {

    public static void main(String[] args) {
        int x = 3;
        int y = 10;
        // 通过三目运算符求出最大值
        int max = (x > y) ? x : y;
        System.out.println("最大值为：" + max);
    }

}
// output:
// 最大值为：10
