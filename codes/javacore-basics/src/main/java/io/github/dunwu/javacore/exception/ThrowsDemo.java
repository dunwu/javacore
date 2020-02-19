package io.github.dunwu.javacore.exception;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ThrowsDemo {

    public static void main(String[] args) {
        f2();
    }

    public static void f2() {
        try {
            // 调用 f1 处，如果不用 try catch ，编译时会报错
            f1();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void f1() throws NoSuchMethodException, NoSuchFieldException {
        Field field = Integer.class.getDeclaredField("digits");
        if (field != null) {
            System.out.println("反射获取 digits 方法成功");
        }
        Method method = String.class.getMethod("toString", int.class);
        if (method != null) {
            System.out.println("反射获取 toString 方法成功");
        }
    }

}
