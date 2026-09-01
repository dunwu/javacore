package io.github.dunwu.javacore.exception;

/**
 * 反例：未捕获的运行时异常（除零）会导致程序终止。
 */
public class RuntimeExceptionDemo {

    public static void demo() {
        // 此处产生了异常
        int result = 10 / 0;
        System.out.println("两个数字相除的结果：" + result);
        System.out.println("----------------------------");
    }

    public static void main(String[] args) {
        demo();
    }

}
