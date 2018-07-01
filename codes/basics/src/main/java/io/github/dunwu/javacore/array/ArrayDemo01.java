package io.github.dunwu.javacore.array;

public class ArrayDemo01 {
    public static void main(String[] args) {
        int[] score = null; // 声明数组
        score = new int[3]; // 为数组开辟空间，大小为3
        System.out.println("score[0] = " + score[0]);
        System.out.println("score[1] = " + score[1]);
        System.out.println("score[2] = " + score[2]);
        for (int x = 0; x < 3; x++) {
            System.out.println("score[" + x + "] = " + score[x]);
        }
        System.out.println("score[3] = " + score[3]);
    }
};
