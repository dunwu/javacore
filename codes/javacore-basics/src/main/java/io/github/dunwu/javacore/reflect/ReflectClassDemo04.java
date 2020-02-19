package io.github.dunwu.javacore.reflect;

import java.lang.reflect.Field;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-26
 */
public class ReflectClassDemo04 {

    public static void main(String[] args) throws NoSuchFieldException {
        Class c1 = java.util.ArrayList.class.getSuperclass();
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

}
// Output:
// java.util.AbstractList
// java.lang.Character.Subset
// java.lang.Character.UnicodeBlock
// java.lang.Character.UnicodeScript
// java.lang.Character.CharacterCache
// java.lang.Character.Subset
// java.lang.Character.UnicodeBlock
// java.lang.Character.UnicodeScript
// java.lang.System
// java.lang.Thread
