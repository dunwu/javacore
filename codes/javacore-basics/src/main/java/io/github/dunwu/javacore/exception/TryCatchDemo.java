package io.github.dunwu.javacore.exception;

/**
 * 演示 try-catch：捕获除零产生的 ArithmeticException。
 */
public class TryCatchDemo {

    public static void demo() {
        try {
            // 此处产生了异常
            int temp = 10 / 0;
            System.out.println("两个数字相除的结果：" + temp);
            System.out.println("----------------------------");
        } catch (ArithmeticException e) {
            System.out.println("出现异常了：" + e);
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
