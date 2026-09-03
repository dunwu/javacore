package io.github.dunwu.javacore.datatype;

/**
 * 装箱、拆箱示例
 * <p>
 * 装箱：把基本类型转为包装类型；拆箱：把包装类型转为基本类型。
 * 自动装箱由编译器翻译为 {@code Integer.valueOf(int)}，自动拆箱翻译为 {@code intValue()}。
 * <p>
 * 注意：{@code Integer.valueOf} 对 [-128, 127] 区间的值做了缓存（享元模式），
 * 区间内自动装箱得到的是同一个对象，{@code ==} 为 true；超出区间则每次都是新对象，{@code ==} 为 false。
 * 因此比较包装类的数值必须用 {@code equals}，不能用 {@code ==}。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-10
 */
public class 包装类装箱拆箱 {

    /**
     * 演示自动/手动装箱拆箱的写法，以及 == 与 equals 在缓存池内外的差异
     */
    public static void demo() {
        // ========== 装箱 ==========
        Integer i1 = 10; // 自动装箱，等价于 Integer.valueOf(10)
        Integer i2 = Integer.valueOf(10); // 手动装箱（推荐写法）
        Integer i3 = 128; // 自动装箱，超出缓存范围 [-128, 127]
        Integer i4 = Integer.valueOf(128); // 手动装箱，超出缓存范围

        // ========== 拆箱 ==========
        int i5 = i1; // 自动拆箱，等价于 i1.intValue()
        int i6 = i1.intValue(); // 手动拆箱

        System.out.println("i1 = [" + i1 + "], i2 = [" + i2 + "]");
        System.out.println("i3 = [" + i3 + "], i4 = [" + i4 + "]");
        System.out.println("i5 = [" + i5 + "], i6 = [" + i6 + "]");

        // ========== 比较：== 比较引用，equals 比较数值 ==========
        // 10 在缓存范围内，i1 与 i2 是同一个对象，两种比较都为 true
        System.out.println("i1 == i2 is [" + (i1 == i2) + "]");
        System.out.println("i1.equals(i2) is [" + i1.equals(i2) + "]");
        // 128 超出缓存范围，i3 与 i4 是两个不同对象，== 为 false 而 equals 为 true
        System.out.println("i3 == i4 is [" + (i3 == i4) + "]");
        System.out.println("i3.equals(i4) is [" + i3.equals(i4) + "]");
        // 包装类与基本类型比较时，包装类会自动拆箱，按数值比较
        System.out.println("i3 == 128 is [" + (i3 == 128) + "]");
    }

    public static void main(String[] args) {
        demo();
    }
    // Output:
    // i1 = [10], i2 = [10]
    // i3 = [128], i4 = [128]
    // i5 = [10], i6 = [10]
    // i1 == i2 is [true]
    // i1.equals(i2) is [true]
    // i3 == i4 is [false]
    // i3.equals(i4) is [true]
    // i3 == 128 is [true]
}
