package io.github.dunwu.javacore.reflect;

import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import org.junit.Test;

/**
 * @author Zhang Peng
 * @date 2018/6/5
 */
public class ReflectConstructorTest {
    @Test
    public void demo1() throws NoSuchMethodException {
        Constructor<?>[] constructors1 = FileOutputStream.class.getDeclaredConstructors();
        System.out.println("FileOutputStream getDeclaredConstructors 清单（数量 = " + constructors1.length + "）：");
        for (Constructor c : constructors1) {
            System.out.println(c);
        }

        Constructor<?>[] constructors2 = FileOutputStream.class.getConstructors();
        System.out.println("FileOutputStream getConstructors 清单（数量 = " + constructors2.length + "）：");
        for (Constructor c : constructors2) {
            System.out.println(c);
        }

        System.out.println("====================");
        Constructor constructor = FileOutputStream.class.getConstructor(String.class, boolean.class);
        System.out.println(constructor);
    }
}
