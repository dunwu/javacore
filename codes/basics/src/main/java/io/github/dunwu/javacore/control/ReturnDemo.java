package io.github.dunwu.javacore.control;

/**
 * 中断语句示例
 * @see BreakDemo
 * @see ContinueDemo
 * @see ReturnDemo
 * @author Zhang Peng
 */
public class ReturnDemo {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            if (i == 3) {
                return;
            }
            System.out.println(i);
        }
        System.out.println("示例结束");
    }
};
// output:
// 0
// 1
// 2
