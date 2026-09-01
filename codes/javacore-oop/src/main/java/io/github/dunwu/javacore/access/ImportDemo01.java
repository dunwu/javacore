package io.github.dunwu.javacore.access; // 放在不同的包中，使用其他包的类必须 import 或写全限定名

import java.util.Arrays;

/**
 * 示例：使用 import 导入其他包的类后，可以直接用短名称引用。
 */
public class ImportDemo01 {

    /**
     * 演示 import 单个类。
     */
    public static void demo() {
        // 已 import java.util.Arrays，可以直接用短名称调用
        int[] nums = { 3, 1, 2 };
        System.out.println("Arrays.toString: " + Arrays.toString(nums));
        // 未 import 的类必须写全限定名才能使用，例如：java.util.List
    }

    public static void main(String[] args) {
        demo();
    }

}
