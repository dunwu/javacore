package io.github.dunwu.javacore.reflect;

import java.lang.reflect.Method;

/**
 * @author peng.zhang
 * @date 2020/10/17
 */
public class MethodPerformDemo04 {

    public static void target(int i) {
        // 空方法
    }

    public static void main(String[] args) throws Exception {
        Class<?> klass = Class.forName("io.github.dunwu.javacore.reflect.MethodPerformDemo04");
        Method method = klass.getMethod("target", int.class);
        method.setAccessible(true);  // 关闭权限检查
        polluteProfile();

        long current = System.currentTimeMillis();
        for (int i = 1; i <= 2_000_000_000; i++) {
            if (i % 100_000_000 == 0) {
                long temp = System.currentTimeMillis();
                System.out.println(temp - current);
                current = temp;
            }

            method.invoke(null, 128);
        }
    }

    public static void polluteProfile() throws Exception {
        Method method1 = MethodPerformDemo04.class.getMethod("target1", int.class);
        Method method2 = MethodPerformDemo04.class.getMethod("target2", int.class);
        for (int i = 0; i < 2000; i++) {
            method1.invoke(null, 0);
            method2.invoke(null, 0);
        }
    }

    public static void target1(int i) { }

    public static void target2(int i) { }

}
