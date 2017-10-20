package io.github.dunwu.javase.statement.circulation;

public class WhileDemo {
    // 完成一个四则运算的功能
    public static void main(String args[]) {
        int x = 1;
        int sum = 0; // 保存累加的结果
        while (x <= 10) {
            sum += x; // 进行累加操作
            // x++ ; // 修改循环条件
        }
        System.out.println("1 --> 10 累加的结果为：" + sum);
    }
};
