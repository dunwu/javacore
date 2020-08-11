package io.github.dunwu.javacore.datatype;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-08-11
 */
public class BigDecimal判等 {

    public static void main(String[] args) {
        System.out.println("====================== wrong ======================");
        wrong();
        System.out.println("====================== right ======================");
        right();
        System.out.println("====================== set ======================");
        set();
    }

    private static void wrong() {
        System.out.println(new BigDecimal("1.0").equals(new BigDecimal("1")));
    }

    private static void right() {
        System.out.println(new BigDecimal("1.0").compareTo(new BigDecimal("1")) == 0);
    }

    private static void set() {
        Set<BigDecimal> hashSet1 = new HashSet<>();
        hashSet1.add(new BigDecimal("1.0"));
        System.out.println(hashSet1.contains(new BigDecimal("1")));//返回false

        Set<BigDecimal> hashSet2 = new HashSet<>();
        hashSet2.add(new BigDecimal("1.0").stripTrailingZeros());
        System.out.println(hashSet2.contains(new BigDecimal("1.000").stripTrailingZeros()));//返回true

        Set<BigDecimal> treeSet = new TreeSet<>();
        treeSet.add(new BigDecimal("1.0"));
        System.out.println(treeSet.contains(new BigDecimal("1")));//返回true
    }

}
