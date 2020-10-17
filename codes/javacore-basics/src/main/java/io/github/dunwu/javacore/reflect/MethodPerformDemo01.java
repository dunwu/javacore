package io.github.dunwu.javacore.reflect;

import java.lang.reflect.Method;

/**
 * 使用 -verbose:class 打印加载的类
 * @author peng.zhang
 * @date 2020/10/17
 */
public class MethodPerformDemo01 {

    public static void target(int i) {
        // 空方法
    }

    public static void main(String[] args) throws Exception {
        Class<?> klass = Class.forName("io.github.dunwu.javacore.reflect.MethodPerformDemo01");
        Method method = klass.getMethod("target", int.class);

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

}
