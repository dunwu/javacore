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
public class DoWhileDemo {

    public static void main(String[] args) {
        int x = 1;
        int sum = 0; // 保存累加的结果
        do {
            sum += x; // 执行累加操作
            x++;
        }
        while (x <= 10);
        System.out.println("1 --> 10 累加的结果为：" + sum);
    }

}
// output:
// 1 --> 10 累加的结果为：55
