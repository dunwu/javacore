package io.github.dunwu.javacore.operator;

/**
 * 关系操作符示例
 * @author Zhang Peng
 */
public class RelationOperatorDemo {
    public static void main(String[] args) {
        int a = 10;
        int b = 20;
        System.out.println("a == b = " + (a == b));
        System.out.println("a != b = " + (a != b));
        System.out.println("a > b = " + (a > b));
        System.out.println("a < b = " + (a < b));
        System.out.println("b >= a = " + (b >= a));
        System.out.println("b <= a = " + (b <= a));
    }
}
// output:
// a == b = false
// a != b = true
// a > b = false
// a < b = true
// b >= a = true
// b <= a = false
