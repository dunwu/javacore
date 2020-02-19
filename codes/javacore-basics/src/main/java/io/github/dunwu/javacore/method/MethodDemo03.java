package io.github.dunwu.javacore.method;

public class MethodDemo03 {

    public static void main(String[] args) {
        int one = add(10, 20); // 调用整型的加法操作
        float two = add(10.3f, 13.3f); // 调用浮点数的加法操作
        int three = add(10, 20, 30); // 调用有三个参数的加法操作
        System.out.println("add(int x,int y)的计算结果：" + one);
        System.out.println("add(float x,float y)的计算结果：" + two);
        System.out.println("(int x,int y,int z)的计算结果：" + three);
    }

    private static int add(int x, int y) {
        int temp = 0; // 方法中的参数，是局部变量
        temp = x + y; // 执行加法计算
        return temp; // 返回计算结果
    }

    private static float add(float x, float y) {
        float temp = 0; // 方法中的参数，是局部变量
        temp = x + y; // 执行加法操作
        return temp; // 返回计算结果
    }

    private static int add(int x, int y, int z) {
        int temp = 0; // 方法中的参数，是局部变量
        temp = x + y + z; // 执行加法计算
        return temp; // 返回计算结果
    }

}
