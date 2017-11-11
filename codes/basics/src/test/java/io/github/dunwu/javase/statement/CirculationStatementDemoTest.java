package io.github.dunwu.javase.statement;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * @author Zhang Peng
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CirculationStatementDemoTest {

    @Test
    public void testFor() {
        int sum = 0;
        for (int x = 1; x <= 10; x++) {
            sum += x;
        }
        System.out.println("1 --> 10 累加的结果为：" + sum);
    }

    @Test
    public void testMultiFor() {
        for (int i = 1; i <= 9; i++) { // 控制行
            for (int j = 1; j <= i; j++) { // 控制列
                System.out.print(i + "*" + j + "=" + (i * j) + "\t");
            }
            System.out.println();
        }
    }

    @Test
    public void testForeach() {
        int nums[] = {1, 3, 5}; // 利用静态初始化方式定义数组
        for (int num : nums) {
            System.out.print(num + " ");
        }
    }

    @Test
    public void testWhile() {
        int x = 1;
        int sum = 0; // 保存累加的结果
        while (x <= 10) {
            sum += x; // 进行累加操作
            x++; // 修改循环条件
        }
        System.out.println("1 --> 10 累加的结果为：" + sum);
    }

    @Test
    public void testDoWhile() {
        int x = 1;
        int sum = 0; // 保存累加的结果
        do {
            sum += x; // 执行累加操作
            x++;
        } while (x <= 10);
        System.out.println("1 --> 10 累加的结果为：" + sum);
    }

    @Test
    public void testBreak() {
        for (int i = 0; i < 10; i++) {
            if (i == 3) {
                break;
            }
            System.out.println("i = " + i);
        }
    }

    @Test
    public void testContinue() {
        for (int i = 0; i < 10; i++) {
            if (i == 3) {
                continue;
            }
            System.out.println("i = " + i);
        }
    }
}
