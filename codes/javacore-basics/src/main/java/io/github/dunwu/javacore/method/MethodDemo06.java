package io.github.dunwu.javacore.method;

public class MethodDemo06 {

    public static void main(String[] args) {
        System.out.println("计算结果：" + sum(100)); // 调用操作
    }

    private static int sum(int num) { // 定义方法用于求和操作
        if (num == 1) { // 判断是否是加到了最后一个数
            return 1;
        } else {
            return num + sum(num - 1); // 递归调用
        }
    }

}
