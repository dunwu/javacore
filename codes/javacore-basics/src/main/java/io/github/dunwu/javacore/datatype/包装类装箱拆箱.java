package io.github.dunwu.javacore.datatype;

/**
 * 装箱、拆箱示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-10
 */
public class 包装类装箱拆箱 {

    public static void main(String[] args) {
        Integer i1 = 10; // 自动装箱
        Integer i2 = new Integer(10); // 非自动装箱
        Integer i3 = Integer.valueOf(10); // 非自动装箱
        int i4 = new Integer(10); // 自动拆箱
        int i5 = i2.intValue(); // 非自动拆箱
        System.out.println("i1 = [" + i1 + "]");
        System.out.println("i2 = [" + i2 + "]");
        System.out.println("i3 = [" + i3 + "]");
        System.out.println("i4 = [" + i4 + "]");
        System.out.println("i5 = [" + i5 + "]");
        System.out.println("i1 == i2 is [" + (i1.equals(i2)) + "]");
        System.out.println("i1 == i4 is [" + (i1 == i4) + "]");
    }
    // Output:
    // i1 = [10]
    // i2 = [10]
    // i3 = [10]
    // i4 = [10]
    // i5 = [10]
    // i1 == i2 is [false]
    // i1 == i4 is [true]
}
