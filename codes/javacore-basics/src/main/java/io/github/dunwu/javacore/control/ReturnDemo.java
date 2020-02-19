package io.github.dunwu.javacore.control;

/**
 * 中断语句示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see BreakDemo
 * @see ContinueDemo
 * @see ReturnDemo
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

}
// output:
// 0
// 1
// 2
