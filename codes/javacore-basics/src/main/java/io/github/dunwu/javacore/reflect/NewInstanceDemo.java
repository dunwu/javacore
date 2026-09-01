package io.github.dunwu.javacore.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-26
 */
public class NewInstanceDemo {

    /**
     * 演示反射创建实例：无参构造与指定构造器两种方式。
     */
    public static void demo()
        throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?> c1 = StringBuilder.class;
        StringBuilder sb = (StringBuilder) c1.newInstance();
        sb.append("aaa");
        System.out.println(sb.toString());

        // 获取String所对应的Class对象
        Class<?> c2 = String.class;
        // 获取String类带一个String参数的构造器
        Constructor constructor = c2.getConstructor(String.class);
        // 根据构造器创建实例
        String str2 = (String) constructor.newInstance("bbb");
        System.out.println(str2);
    }

    public static void main(String[] args)
        throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        demo();
    }

}
// Output:
// aaa
// bbb
