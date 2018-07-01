package io.github.dunwu.javacore.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;

/**
 * @author Zhang Peng
 * @date 2018/6/5
 */
public class ReflectClassTest {
    enum E {A, B}

    /**
     * getClass() 示例
     */
    @Test
    public void demo1() {
        Class c = "foo".getClass();
        System.out.println(c.getCanonicalName());

        Class c2 = ReflectClassTest.E.A.getClass();
        System.out.println(c2.getCanonicalName());

        byte[] bytes = new byte[1024];
        Class c3 = bytes.getClass();
        System.out.println(c3.getCanonicalName());

        Set<String> set = new HashSet<>();
        Class c4 = set.getClass();
        System.out.println(c4.getCanonicalName());
    }

    /**
     * .class 示例
     */
    @Test
    public void demo2() {
        boolean b;
        // Class c = b.getClass(); // 编译错误
        Class c1 = boolean.class;
        System.out.println(c1.getCanonicalName());

        Class c2 = java.io.PrintStream.class;
        System.out.println(c2.getCanonicalName());

        Class c3 = int[][][].class;
        System.out.println(c3.getCanonicalName());
    }

    /**
     * forName() 示例
     */
    @Test
    public void demo3() {
        try {
            Class c1 = Class.forName("io.github.dunwu.javacore.reflect.ReflectClassTest");
            System.out.println(c1.getCanonicalName());

            Class c2 = Class.forName("[D");
            System.out.println(c2.getCanonicalName());

            Class c3 = Class.forName("[[Ljava.lang.String;");
            System.out.println(c3.getCanonicalName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * .TYPE 示例
     */
    @Test
    public void demo4() {
        Class c1 = Double.TYPE;
        System.out.println(c1.getCanonicalName());

        Class c2 = Void.TYPE;
        System.out.println(c2.getCanonicalName());
    }

    @Test
    public void demo5() throws NoSuchFieldException {
        Class c1 = javax.swing.JButton.class.getSuperclass();
        System.out.println(c1.getCanonicalName());

        Class<?>[] classes = Character.class.getClasses();
        for (Class c : classes) {
            System.out.println(c.getCanonicalName());
        }

        Class<?>[] classes2 = Character.class.getDeclaredClasses();
        for (Class c : classes2) {
            System.out.println(c.getCanonicalName());
        }

        Field f = System.class.getField("out");
        Class c2 = f.getDeclaringClass();
        System.out.println(c2.getCanonicalName());

        Class c3 = Thread.State.class.getEnclosingClass();
        System.out.println(c3.getCanonicalName());
    }

    @Test
    public void demo06() {
        ArrayList arrayList = new ArrayList();
        if (arrayList instanceof List) {
            System.out.println("ArrayList is List");
        }
        if (List.class.isInstance(arrayList)) {
            System.out.println("ArrayList is List");
        }
    }

    @Test
    public void demo07()
        throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?> c1 = String.class;
        Object str1 = c1.newInstance();
        System.out.println(str1.getClass().getCanonicalName());

        //获取String所对应的Class对象
        Class<?> c2 = String.class;
        //获取String类带一个String参数的构造器
        Constructor constructor = c2.getConstructor(String.class);
        //根据构造器创建实例
        Object obj = constructor.newInstance("bbb");
        System.out.println(obj.getClass().getCanonicalName());
    }
}
