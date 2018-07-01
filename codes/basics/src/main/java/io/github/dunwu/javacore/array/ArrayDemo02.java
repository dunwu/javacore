package io.github.dunwu.javacore.array;

public class ArrayDemo02 {
    public static void main(String[] args) {
        int[] score = null; // 声明数组
        score = new int[3]; // 为数组开辟空间，大小为3
        for (int x = 0; x < 3; x++) { // 为每一个元素赋值
            score[x] = x * 2 + 1; // 每一个值都是奇数
        }
        for (int x = 0; x < score.length; x++) {
            System.out.println("score[" + x + "] = " + score[x]);
        }
    }
};
