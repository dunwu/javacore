package io.github.dunwu.javacore.util.string;

/**
 * 示例：String 拼接的性能问题——每次 += 都会产生新的 String 对象。
 */
public class StringBufferDemo09 {

    /** 演示 String 循环拼接：内容不变，仅说明"不断产生新对象"的低效写法。 */
    public static void demo() {
        String str1 = "Zhang Peng";
        for (int i = 0; i < 100; i++) {
            str1 += i; // 不断修改String的内存引用，性能低
        }
        System.out.println(str1);
    }

    public static void main(String[] args) {
        demo();
    }

}
