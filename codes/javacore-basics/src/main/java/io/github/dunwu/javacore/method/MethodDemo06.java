package io.github.dunwu.javacore.method;

/**
 * 演示递归方法：用递归实现 1 到 100 的累加。
 */
public class MethodDemo06 {

    public static void demo() {
        System.out.println("计算结果：" + sum(100)); // 调用操作
    }

    public static void main(String[] args) {
        demo();
    }

    private static int sum(int num) { // 定义方法用于求和操作
        if (num == 1) { // 判断是否是加到了最后一个数
            return 1;
        } else {
            return num + sum(num - 1); // 递归调用
        }
    }

}
