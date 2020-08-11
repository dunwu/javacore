package io.github.dunwu.javacore.datatype;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-08-11
 */
public class 浮点数舍入 {

    public static void main(String[] args) {
        System.out.println("====================== wrong1 ======================");
        wrong1();
        System.out.println("====================== wrong2 ======================");
        wrong2();
        System.out.println("====================== right ======================");
        right();
    }

    private static void wrong1() {
        double num1 = 3.35;
        float num2 = 3.35f;
        System.out.println(String.format("%.1f", num1)); // 3.4
        System.out.println(String.format("%.1f", num2)); // 3.3
    }

    private static void wrong2() {
        double num1 = 3.35;
        float num2 = 3.35f;
        DecimalFormat format = new DecimalFormat("#.##");
        format.setRoundingMode(RoundingMode.DOWN);
        System.out.println(format.format(num1)); // 3.35
        format.setRoundingMode(RoundingMode.DOWN);
        System.out.println(format.format(num2)); // 3.34
    }

    private static void right() {
        BigDecimal num1 = new BigDecimal("3.35");
        BigDecimal num2 = num1.setScale(1, BigDecimal.ROUND_DOWN);
        System.out.println(num2); // 3.3
        BigDecimal num3 = num1.setScale(1, BigDecimal.ROUND_HALF_UP);
        System.out.println(num3); // 3.4
    }

}
