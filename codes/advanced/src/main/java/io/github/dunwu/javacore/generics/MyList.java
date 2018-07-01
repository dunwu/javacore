package io.github.dunwu.javacore.generics;

import java.util.List;

/**
 * 泛型类
 *
 * @param <T>
 */
public class MyList<T> {
    private T t;

    public void add(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }

    public static void main(String[] args) {
        List<String> list= null;
        MyList<Integer> integerBox = new MyList<Integer>();
        MyList<String> stringBox = new MyList<String>();

        integerBox.add(new Integer(10));
        stringBox.add(new String("菜鸟教程"));

        System.out.printf("整型值为 :%d\n\n", integerBox.get());
        System.out.printf("字符串为 :%s\n", stringBox.get());
    }
}
