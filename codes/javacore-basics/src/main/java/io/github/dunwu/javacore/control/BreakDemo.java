package io.github.dunwu.javacore.control;

/**
 * 中断语句示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see BreakDemo
 * @see ContinueDemo
 * @see ReturnDemo
 */
public class BreakDemo {

    /**
     * 演示 break：当 i == 3 时立即跳出循环，后续循环不再执行。
     */
    public static void demo() {
        for (int i = 0; i < 10; i++) {
            if (i == 3) {
                break;
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
// 示例结束
