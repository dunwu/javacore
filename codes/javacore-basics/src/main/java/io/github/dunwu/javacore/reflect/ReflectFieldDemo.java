package io.github.dunwu.javacore.reflect;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-26
 */
public class ReflectFieldDemo {

    public static void main(String[] args) throws NoSuchFieldException {
        Field f1 = FieldSpy.class.getField("b");
        System.out.format("Type: %s%n", f1.getType());

        Field f2 = FieldSpy.class.getField("name");
        System.out.format("Type: %s%n", f2.getType());

        Field f3 = FieldSpy.class.getField("list");
        System.out.format("Type: %s%n", f3.getType());

        Field f4 = FieldSpy.class.getField("val");
        System.out.format("Type: %s%n", f4.getType());
    }

    class FieldSpy<T> {

        public boolean[][] b = { { false, false }, { true, true } };

        public String name = "Alice";

        public List<Integer> list;

        public T val;

    }

}
// Output:
// Type: class [[Z
// Type: class java.lang.String
// Type: interface java.util.List
// Type: class java.lang.Object
