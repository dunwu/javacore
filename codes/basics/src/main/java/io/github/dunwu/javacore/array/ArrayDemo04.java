package io.github.dunwu.javacore.array;

public class ArrayDemo04 {
    public static void main(String[] args) {
        int[] score = { 91, 92, 93, 94, 95, 96 }; // 使用静态初始化声明数组
        for (int x = 0; x < score.length; x++) { // 循环输出
            System.out.println("score[" + x + "] = " + score[x]);
        }
    }
};
