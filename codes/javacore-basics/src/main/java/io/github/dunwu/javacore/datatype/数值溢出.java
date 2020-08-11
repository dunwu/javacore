package io.github.dunwu.javacore.datatype;

import java.math.BigInteger;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-08-11
 */
public class 数值溢出 {

    public static void main(String[] args) {
        System.out.println("====================== wrong1 ======================");
        wrong();
        System.out.println("====================== right1 ======================");
        right1();
        System.out.println("====================== right2 ======================");
        right2();
    }

    private static void wrong() {
        long l = Long.MAX_VALUE;
        System.out.println(l + 1); // -9223372036854775808
        System.out.println(l + 1 == Long.MIN_VALUE); // true
    }

    private static void right1() {
        try {
            long l = Long.MAX_VALUE;
            System.out.println(Math.addExact(l, 1));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void right2() {
        BigInteger i = new BigInteger(String.valueOf(Long.MAX_VALUE));
        System.out.println(i.add(BigInteger.ONE).toString());

        try {
            long l = i.add(BigInteger.ONE).longValueExact();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
