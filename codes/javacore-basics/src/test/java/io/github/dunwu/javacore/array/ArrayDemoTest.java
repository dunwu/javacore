package io.github.dunwu.javacore.array;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * 数组示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ArrayDemoTest {

    @Test
    public void demo01() {
        // 声明数组
        int[] score = null;
        // 为数组开辟空间，大小为3
        score = new int[3];
        System.out.println("score[0] = " + score[0]);
        System.out.println("score[1] = " + score[1]);
        System.out.println("score[2] = " + score[2]);
        for (int x = 0; x < 3; x++) {
            System.out.println("score[" + x + "] = " + score[x]);
        }
        // 超出数组维度：ArrayIndexOutOfBoundsException
        // System.out.println("score[3] = " + score[3]);
    }

    @Test
    public void demo02() {
        // 声明数组
        int[] score = null;
        // 为数组开辟空间，大小为3
        score = new int[3];
        // 为每一个元素赋值
        for (int x = 0; x < 3; x++) {
            // 每一个值都是奇数
            score[x] = x * 2 + 1;
        }
        for (int x = 0; x < score.length; x++) {
            System.out.println("score[" + x + "] = " + score[x]);
        }
    }

    @Test
    public void demo03() {
        // 声明数组
        int[] score = null;
        // 为数组开辟空间，大小为3
        score = new int[3];
        System.out.println("数组长度为：" + score.length);
    }

    @Test
    public void demo04() {
        // 使用静态初始化声明数组
        int[] score = { 91, 92, 93, 94, 95, 96 };
        // 循环输出
        for (int x = 0; x < score.length; x++) {
            System.out.println("score[" + x + "] = " + score[x]);
        }
    }

    @Test
    public void demo05() {
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

    @Test
    public void demo06() {
        int[] score = { 67, 89, 87, 69, 90, 100, 75, 90 }; // 使用静态初始化声明数组
        for (int i = 1; i < score.length; i++) {
            for (int j = 0; j < score.length; j++) {
                if (score[i] < score[j]) { // 交换位置
                    int temp = score[i]; // 中间变量
                    score[i] = score[j];
                    score[j] = temp;
                }
            }
        }
        for (int i = 0; i < score.length; i++) { // 循环输出
            System.out.print(score[i] + "\t");
        }
    }

    @Test
    public void demo07() {
        int[] score = { 67, 89, 87, 69, 90, 100, 75, 90 }; // 使用静态初始化声明数组
        for (int i = 1; i < score.length; i++) {
            for (int j = 0; j < score.length; j++) {
                if (score[i] < score[j]) { // 交换位置
                    int temp = score[i]; // 中间变量
                    score[i] = score[j];
                    score[j] = temp;
                }
            }
            System.out.print("第" + i + "次排序的结果：");
            for (int j = 0; j < score.length; j++) { // 循环输出
                System.out.print(score[j] + "\t");
            }
            System.out.println();
        }
        for (int i = 0; i < score.length; i++) { // 循环输出
            System.out.print(score[i] + "\t");
        }
    }

    @Test
    public void demo08() {
        int[][] score = new int[4][3]; // 声明并实例化二维数组
        score[0][1] = 30; // 为数组中的内容赋值
        score[1][0] = 31; // 为数组中的内容赋值
        score[2][2] = 32; // 为数组中的内容赋值
        score[3][1] = 33; // 为数组中的内容赋值
        score[1][1] = 30; // 为数组中的内容赋值
        for (int i = 0; i < score.length; i++) {
            for (int j = 0; j < score[i].length; j++) {
                System.out.print(score[i][j] + "\t");
            }
            System.out.println();
        }
    }

    @Test
    public void demo09() {
        int[][] score = { { 67, 61 }, { 78, 89, 83 }, { 99, 100, 98, 66, 95 } }; // 静态初始化完成，每行的数组元素个数不一样1
        for (int i = 0; i < score.length; i++) {
            for (int j = 0; j < score[i].length; j++) {
                System.out.print(score[i][j] + "\t");
            }
            System.out.println();
        }
    }

    @Test
    public void demo10() {
        // 静态初始化完成，每行的数组元素个数不一样
        int[][][] score = { { { 5, 1 }, { 6, 7 } }, { { 9, 4 }, { 8, 3 } } };
        for (int i = 0; i < score.length; i++) {
            for (int j = 0; j < score[i].length; j++) {
                for (int k = 0; k < score[i][j].length; k++) {
                    System.out.println("score[" + i + "][" + j + "][" + k + "] = " + score[i][j][k] + "\t");
                }
            }
        }
    }

}
