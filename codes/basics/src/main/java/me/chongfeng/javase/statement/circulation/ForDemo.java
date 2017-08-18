package me.chongfeng.javase.statement.circulation;

public class ForDemo {
    // 完成一个四则运算的功能
    public static void main(String args[]) {
        int sum = 0; // 保存累加的结果
        for (int x = 1; x <= 10; x++) {
            sum += x;
        }
        System.out.println("1 --> 10 累加的结果为：" + sum);
    }
};
