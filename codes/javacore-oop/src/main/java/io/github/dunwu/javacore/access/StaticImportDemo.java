package io.github.dunwu.javacore.access;

import static io.github.dunwu.javacore.access.Operate.*;

/**
 * 示例：静态导入——import static 后可直接调用目标类的静态方法，无需类名前缀。
 */
public class StaticImportDemo {

    /**
     * 演示静态导入后直接调用静态方法。
     */
    public static void demo() {
        System.out.println("3 + 3 = " + add(3, 3)); // 直接调用静态方法，等价于 Operate.add(3, 3)
        System.out.println("3 - 2 = " + sub(3, 2)); // 直接调用静态方法，等价于 Operate.sub(3, 2)
        System.out.println("3 * 3 = " + mul(3, 3)); // 直接调用静态方法，等价于 Operate.mul(3, 3)
        System.out.println("3 / 3 = " + div(3, 3)); // 直接调用静态方法，等价于 Operate.div(3, 3)
    }

    public static void main(String[] args) {
        demo();
    }

}
