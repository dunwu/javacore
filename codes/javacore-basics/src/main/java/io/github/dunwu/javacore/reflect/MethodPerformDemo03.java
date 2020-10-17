package io.github.dunwu.javacore.reflect;

import java.lang.reflect.Method;

/**
 * 在运行指令中添加如下两个虚拟机参数：
 * <ul>
 *     <li>-Djava.lang.Integer.IntegerCache.high=128</li>
 *     <li>-Dsun.reflect.noInflation=true</li>
 * </ul>
 * @author peng.zhang
 * @date 2020/10/17
 */
public class MethodPerformDemo03 {

    public static void target(int i) {
        // 空方法
    }

    public static void main(String[] args) throws Exception {
        Class<?> klass = Class.forName("io.github.dunwu.javacore.reflect.MethodPerformDemo03");
        Method method = klass.getMethod("target", int.class);
        method.setAccessible(true);  // 关闭权限检查

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
