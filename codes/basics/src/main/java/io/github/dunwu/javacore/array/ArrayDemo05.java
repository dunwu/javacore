package io.github.dunwu.javacore.array;

public class ArrayDemo05 {
    public static void main(String[] args) {
        int[] score = { 67, 89, 87, 69, 90, 100, 75, 90 }; // 使用静态初始化声明数组
        int max = 0; // 保存数组中的最大值
        int min = 0; // 保存数组中的最小值
        max = min = score[0]; // 把第一个元素的内容赋值给max和min
        for (int x = 0; x < score.length; x++) { // 循环输出
            if (score[x] > max) { // 依次判断后续元素是否比max大
                max = score[x]; // 如果大，则修改max的内容
            }
            if (score[x] < min) { // 依次判断后续的元素是否比min小
                min = score[x]; // 如果小，则修改min内容
            }
        }
        System.out.println("最高成绩：" + max);
        System.out.println("最低成绩：" + min);
    }
};
