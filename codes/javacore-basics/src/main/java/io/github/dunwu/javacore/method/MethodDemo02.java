package io.github.dunwu.javacore.method;

public class MethodDemo02 {

    public static void main(String[] args) {
        int one = addInt(10, 20); // 调用整型的加法操作
        float two = addFloat(10.3f, 13.3f); // 调用浮点数的加法操作
        System.out.println("addOne的计算结果：" + one);
        System.out.println("addTwo的计算结果：" + two);
    }

    private static int addInt(int x, int y) {
        int temp = 0; // 方法中的参数，是局部变量
        temp = x + y; // 执行加法计算
        return temp; // 返回计算结果
    }

    private static float addFloat(float x, float y) {
        float temp = 0; // 方法中的参数，是局部变量
        temp = x + y; // 执行加法操作
        return temp; // 返回计算结果
    }

}
