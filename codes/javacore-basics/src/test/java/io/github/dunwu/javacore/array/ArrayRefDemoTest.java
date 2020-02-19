package io.github.dunwu.javacore.array;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ArrayRefDemoTest {

    @Test
    public void demo01() {
        int[] temp = { 1, 3, 5 }; // 利用静态初始化方式定义数组
        acceptArray01(temp); // 传递数组
        for (int i = 0; i < temp.length; i++) {
            System.out.print(temp[i] + "、");
        }
    }

    private void acceptArray01(int[] x) { // 接收整型数组的引用
        x[0] = 6; // 修改第一个元素
    }

    @Test
    public void demo02() {
        int[] temp = acceptArray02(); // 通过方法实例化数组
        print(temp); // 打印数组内容
    }

    private int[] acceptArray02() { // 返回一个数组
        int[] ss = { 1, 3, 5, 7, 9 }; // 定义一个数组
        return ss;
    }

    private void print(int[] x) {
        for (int i = 0; i < x.length; i++) {
            System.out.print(x[i] + "、");
        }
    }

    @Test
    public void demo03() {
        int[] score = { 67, 89, 87, 69, 90, 100, 75, 90 }; // 定义整型数组
        int[] age = { 31, 30, 18, 17, 8, 9, 1, 39 }; // 定义整型数组
        sort(score); // 数组排序
        print(score); // 数组打印
        System.out.println("\n---------------------------");
        sort(age); // 数组排序
        print(age); // 数组打印
    }

    private void sort(int[] temp) { // 执行排序操作
        for (int i = 1; i < temp.length; i++) {
            for (int j = 0; j < temp.length; j++) {
                if (temp[i] < temp[j]) {
                    int x = temp[i];
                    temp[i] = temp[j];
                    temp[j] = x;
                }
            }
        }
    }

    @Test
    public void demo04() {
        int[] score = { 67, 89, 87, 69, 90, 100, 75, 90 }; // 定义整型数组
        int[] age = { 31, 30, 18, 17, 8, 9, 1, 39 }; // 定义整型数组
        java.util.Arrays.sort(score); // 数组排序
        print(score); // 数组打印
        System.out.println("\n---------------------------");
        java.util.Arrays.sort(age); // 数组排序
        print(age); // 数组打印
    }

    @Test
    public void demo05() {
        int[] i1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9 }; // 源数组
        int[] i2 = { 11, 22, 33, 44, 55, 66, 77, 88, 99 };// 目标数组
        copy(i1, 3, i2, 1, 3); // 调用拷贝方法
        print(i2);
    }

    /**
     * 源数组名称，源数组开始点，目标数组名称，目标数组开始点，拷贝长度
     */
    private void copy(int[] s, int s1, int[] o, int s2, int len) {
        for (int i = 0; i < len; i++) {
            o[s2 + i] = s[s1 + i]; // 进行拷贝操作
        }
    }

    @Test
    public void demo06() {
        int[] i1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9 }; // 源数组
        int[] i2 = { 11, 22, 33, 44, 55, 66, 77, 88, 99 };// 目标数组
        System.arraycopy(i1, 3, i2, 1, 3); // 调用Java中对数组支持的拷贝方法
        print(i2);
    }

}
