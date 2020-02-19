package io.github.dunwu.javacore.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-26
 */
public class ReflectMethodDemo {

    public static void main(String[] args)
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        // 返回所有方法
        Method[] methods1 = System.class.getDeclaredMethods();
        System.out.println("System getDeclaredMethods 清单（数量 = " + methods1.length + "）：");
        for (Method m : methods1) {
            System.out.println(m);
        }

        // 返回所有 public 方法
        Method[] methods2 = System.class.getMethods();
        System.out.println("System getMethods 清单（数量 = " + methods2.length + "）：");
        for (Method m : methods2) {
            System.out.println(m);
        }

        // 利用 Method 的 invoke 方法调用 System.currentTimeMillis()
        Method method = System.class.getMethod("currentTimeMillis");
        System.out.println(method);
        System.out.println(method.invoke(null));
    }

}
