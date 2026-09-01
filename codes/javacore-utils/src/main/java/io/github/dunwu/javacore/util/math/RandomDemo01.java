package io.github.dunwu.javacore.util.math;

import java.util.Random;

/**
 * 示例：Random 生成指定范围的随机数（nextInt(100) 生成 [0, 100) 的随机整数）。
 */
public class RandomDemo01 {

    /**
     * 演示生成 10 个 [0, 100) 的随机整数。
     */
    public static void demo() {
        Random r = new Random(); // 实例化Random对象
        for (int i = 0; i < 10; i++) {
            System.out.print(r.nextInt(100) + "\t");
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
