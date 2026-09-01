package io.github.dunwu.javacore.util.math;

/**
 * 示例：Math 工具类——全部为静态方法，直接用“类.方法()”调用。
 */
public class MathDemo01 {

    /**
     * 演示 Math 的常用数学运算。
     */
    public static void demo() {
        // Math类中的方法都是静态方法，直接使用“类.方法名称()”的形式调用即可
        System.out.println("求平方根：" + Math.sqrt(9.0));
        System.out.println("求两数的最大值：" + Math.max(10, 30));
        System.out.println("求两数的最小值：" + Math.min(10, 30));
        System.out.println("2的3次方：" + Math.pow(2, 3));
        System.out.println("四舍五入：" + Math.round(33.6));
    }

    public static void main(String[] args) {
        demo();
    }

}
