package me.chongfeng.javase.statement.circulation;

public class DoWhileDemo {
    // 完成一个四则运算的功能
    public static void main(String args[]) {
        int x = 1;
        int sum = 0; // 保存累加的结果
        do {
            sum += x; // 执行累加操作
            x++;
        } while (x <= 10);
        System.out.println("1 --> 10 累加的结果为：" + sum);
    }
};
