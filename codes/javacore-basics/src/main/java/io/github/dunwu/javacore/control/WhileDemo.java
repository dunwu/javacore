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
public class WhileDemo {

    /**
     * 演示 while 循环：先判断条件再执行循环体，累加 1 到 10。
     */
    public static void demo() {
        int x = 1;
        int sum = 0; // 保存累加的结果
        while (x <= 10) {
            sum += x; // 进行累加操作
            x++; // 修改循环条件
        }
        System.out.println("1 --> 10 累加的结果为：" + sum);
    }

    public static void main(String[] args) {
        demo();
    }

}
// output:
// 1 --> 10 累加的结果为：55
