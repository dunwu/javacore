package io.github.dunwu.javacore.control;

/**
 * 循环语句示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see WhileDemo
 * @see DoWhileDemo
 * @see ForDemo
 * @see ForNestedDemo
 * @see ForeachDemo
 */
public class ForDemo {

    public static void main(String[] args) {
        int sum = 0; // 保存累加的结果
        for (int x = 1; x <= 10; x++) {
            sum += x;
        }
        System.out.println("1 --> 10 累加的结果为：" + sum);
    }

}
// output:
// 1 --> 10 累加的结果为：55
