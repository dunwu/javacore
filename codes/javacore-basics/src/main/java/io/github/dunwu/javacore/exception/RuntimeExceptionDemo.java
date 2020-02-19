package io.github.dunwu.javacore.exception;

public class RuntimeExceptionDemo {

    public static void main(String[] args) {
        // 此处产生了异常
        int result = 10 / 0;
        System.out.println("两个数字相除的结果：" + result);
        System.out.println("----------------------------");
    }

}
