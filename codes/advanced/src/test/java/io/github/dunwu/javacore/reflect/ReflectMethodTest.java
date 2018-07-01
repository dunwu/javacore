package io.github.dunwu.javacore.reflect;

import java.lang.reflect.Method;
import org.junit.Test;

/**
 * @author Zhang Peng
 * @date 2018/6/5
 */
public class ReflectMethodTest {
    @Test
    public void demo1() throws NoSuchMethodException {
        // getDeclaredMethods()方法返回类或接口声明的所有方法，包括公共、保护、默认（包）访问和私有方法，但不包括继承的方法。
        Method[] methods1 = Thread.class.getDeclaredMethods();
        System.out.println("Thread getDeclaredMethods 清单（数量 = " + methods1.length + "）：");
        for (Method m : methods1) {
            System.out.println(m);
        }

        // getMethods() 方法返回某个类的所有公用（public）方法，包括其继承类的公用方法。
        Method[] methods2 = Thread.class.getMethods();
        System.out.println("Thread getMethods 清单（数量 = " + methods2.length + "）：");
        for (Method m : methods2) {
            System.out.println(m);
        }

        Method method = Thread.class.getMethod("join", long.class, int.class);
        System.out.println(method);
    }
}
