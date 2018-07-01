package io.github.dunwu.javacore.reflect;

import java.lang.reflect.Field;
import java.util.List;
import org.junit.Test;

/**
 * @author Zhang Peng
 * @date 2018/6/5
 */
public class ReflectFieldTest {
    class FieldSpy<T> {
        public boolean[][] b = {{ false, false }, { true, true } };
        public String name  = "Alice";
        public List<Integer> list;
        public T val;
    }

    @Test
    public void demo1() {
        try {
            Field f1 = FieldSpy.class.getField("b");
            System.out.format("Type: %s%n", f1.getType());

            Field f2 = FieldSpy.class.getField("name");
            System.out.format("Type: %s%n", f2.getType());

            Field f3 = FieldSpy.class.getField("list");
            System.out.format("Type: %s%n", f3.getType());

            Field f4 = FieldSpy.class.getField("val");
            System.out.format("Type: %s%n", f4.getType());
        } catch (NoSuchFieldException x) {
            x.printStackTrace();
        }
    }
}
