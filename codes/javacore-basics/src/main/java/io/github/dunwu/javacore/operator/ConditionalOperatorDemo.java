package io.github.dunwu.javacore.operator;

/**
 * 三目（条件）操作符示例：条件 ? 值1 : 值2。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class ConditionalOperatorDemo {

    /**
     * 演示用三目运算符求两个数的最大值。
     */
    public static void demo() {
        int x = 3;
        int y = 10;
        // 通过三目运算符求出最大值
        int max = (x > y) ? x : y;
        System.out.println("最大值为：" + max);
    }

    public static void main(String[] args) {
        demo();
    }

}
// output:
// 最大值为：10
