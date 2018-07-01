package io.github.dunwu.javacore.array;

public class ArrayDemo08 {
    public static void main(String[] args) {
        int[][] score = new int[4][3]; // 声明并实例化二维数组
        score[0][1] = 30; // 为数组中的内容赋值
        score[1][0] = 31; // 为数组中的内容赋值
        score[2][2] = 32; // 为数组中的内容赋值
        score[3][1] = 33; // 为数组中的内容赋值
        score[1][1] = 30; // 为数组中的内容赋值
        for (int[] aScore : score) {
            for (int anAScore : aScore) {
                System.out.print(anAScore + "\t");
            }
            System.out.println("");
        }
    }
};
