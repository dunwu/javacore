package io.github.dunwu.javacore.exception;

public class TryCatchFinallyDemo {

    public static void main(String[] args) {
        try {
            // 此处产生了异常
            int temp = 10 / 0;
            System.out.println("两个数字相除的结果：" + temp);
            System.out.println("----------------------------");
        } catch (ArithmeticException e) {
            System.out.println("出现异常了：" + e);
        } finally {
            System.out.println("不管是否出现异常，都执行此代码");
        }
    }

}
