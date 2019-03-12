package io.github.dunwu.javacore.operator;

/**
 * 关系操作符示例
 * @author Zhang Peng
 */
public class RelationOperatorDemo {
    public static void main(String[] args) {
        int x = 20;
        int y = 10;
        System.out.println("x == y = " + (x == y));
        System.out.println("x != y = " + (x != y));
        System.out.println("x > y = " + (x > y));
        System.out.println("x < y = " + (x < y));
        System.out.println("x >= y = " + (x >= y));
        System.out.println("x <= y = " + (x <= y));
    }
}
// output:
// x == y = false
// x != y = true
// x > y = true
// x < y = false
// x >= y = true
// x <= y = false
