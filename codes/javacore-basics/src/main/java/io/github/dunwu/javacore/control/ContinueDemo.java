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

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            if (i == 3) {
                continue;
            }
            System.out.println("i = " + i);
        }
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
