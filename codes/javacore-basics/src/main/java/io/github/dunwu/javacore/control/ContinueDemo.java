package io.github.dunwu.javacore.control;

/**
 * 中断语句示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see BreakDemo
 * @see ContinueDemo
 * @see ReturnDemo
 */
public class ContinueDemo {

    /**
     * 演示 continue：当 i == 3 时跳过本次循环体剩余部分，继续下一次循环。
     */
    public static void demo() {
        for (int i = 0; i < 10; i++) {
            if (i == 3) {
                continue;
            }
            System.out.println("i = " + i);
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
// output:
// i = 0
// i = 1
// i = 2
// i = 4
// i = 5
// i = 6
// i = 7
// i = 8
// i = 9
