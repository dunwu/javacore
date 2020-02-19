package io.github.dunwu.javacore.util;

public class SystemDemo01 {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis(); // 取得开始计算之前的时间
        int sum = 0; // 声明变量
        for (int i = 0; i < 30000000; i++) { // 执行累加操作
            sum += i;
        }
        long endTime = System.currentTimeMillis(); // 取得计算之后的时间
        // 结束时间减去开始时间
        System.out.println("计算所花费的时间：" + (endTime - startTime) + "毫秒");
    }

}
