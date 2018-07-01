package io.github.dunwu.javacore.control;

/**
 * 循环语句示例
 * @see WhileDemo
 * @see DoWhileDemo
 * @see ForDemo
 * @see ForNestedDemo
 * @see ForeachDemo
 * @author Zhang Peng
 */
public class WhileDemo {
    public static void main(String[] args) {
        int x = 1;
        int sum = 0; // 保存累加的结果
        while (x <= 10) {
            sum += x; // 进行累加操作
            x++; // 修改循环条件
        }
        System.out.println("1 --> 10 累加的结果为：" + sum);
    }
};
// output:
// 1 --> 10 累加的结果为：55
