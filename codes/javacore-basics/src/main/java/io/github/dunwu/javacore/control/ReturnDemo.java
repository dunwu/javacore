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

    /**
     * 演示 return：当 i == 3 时直接结束整个方法，"示例结束"不会被打印。
     */
    public static void demo() {
        for (int i = 0; i < 10; i++) {
            if (i == 3) {
                return;
            }
            System.out.println(i);
        }
        System.out.println("示例结束");
    }

    public static void main(String[] args) {
        demo();
    }

}
// output:
// 0
// 1
// 2
