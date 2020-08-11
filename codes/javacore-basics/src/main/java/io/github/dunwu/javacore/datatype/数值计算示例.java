package io.github.dunwu.javacore.datatype;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * 使用 BigDecimal 表示和计算浮点数，且务必使用字符串的构造方法来初始化 BigDecimal
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-08-11
 */
@Slf4j
public class 数值计算示例 {

    public static void main(String[] args) {
        System.out.println("====================== wrong1 ======================");
        wrong1();
        System.out.println("====================== wrong2 ======================");
        wrong2();
        System.out.println("====================== right ======================");
        right();
        System.out.println("====================== testScale ======================");
        testScale();
    }

    /**
     * 浮点数计算结果不符合预期的场景
     */
    private static void wrong1() {
        System.out.println(0.1 + 0.2); // 0.30000000000000004
        System.out.println(1.0 - 0.8); // 0.19999999999999996
        System.out.println(4.015 * 100); // 401.49999999999994
        System.out.println(123.3 / 100); // 1.2329999999999999
        double amount1 = 2.15;
        double amount2 = 1.10;
        System.out.println(amount1 - amount2); // 1.0499999999999998
    }

    /**
     * BigDecimal 不正确的初始化方式
     */
    private static void wrong2() {
        System.out.println(new BigDecimal(0.1).add(new BigDecimal(0.2)));
        // Output: 0.3000000000000000166533453693773481063544750213623046875

        System.out.println(new BigDecimal(1.0).subtract(new BigDecimal(0.8)));
        // Output: 0.1999999999999999555910790149937383830547332763671875

        System.out.println(new BigDecimal(4.015).multiply(new BigDecimal(100)));
        // Output: 401.49999999999996802557689079549163579940795898437500

        System.out.println(new BigDecimal(123.3).divide(new BigDecimal(100)));
        // Output: 1.232999999999999971578290569595992565155029296875
    }

    /**
     * BigDecimal 正确的初始化方式
     */
    private static void right() {
        System.out.println(new BigDecimal("0.1").add(new BigDecimal("0.2")));
        System.out.println(new BigDecimal("1.0").subtract(new BigDecimal("0.8")));
        System.out.println(new BigDecimal("4.015").multiply(new BigDecimal("100")));
        System.out.println(new BigDecimal("123.3").divide(new BigDecimal("100")));
    }

    /**
     * BigDecimal 浮点数精度测试
     */
    private static void testScale() {
        BigDecimal bigDecimal1 = new BigDecimal("100");
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(100d));
        BigDecimal bigDecimal3 = new BigDecimal(String.valueOf(100));
        BigDecimal bigDecimal4 = BigDecimal.valueOf(100d);
        BigDecimal bigDecimal5 = new BigDecimal(Double.toString(100));

        print(bigDecimal1); //scale 0 precision 3 result 401.500
        print(bigDecimal2); //scale 1 precision 4 result 401.5000
        print(bigDecimal3); //scale 0 precision 3 result 401.500
        print(bigDecimal4); //scale 1 precision 4 result 401.5000
        print(bigDecimal5); //scale 1 precision 4 result 401.5000
    }

    private static void print(BigDecimal bigDecimal) {
        log.info("scale {} precision {} result {}", bigDecimal.scale(), bigDecimal.precision(),
            bigDecimal.multiply(new BigDecimal("4.015")));
    }

}
