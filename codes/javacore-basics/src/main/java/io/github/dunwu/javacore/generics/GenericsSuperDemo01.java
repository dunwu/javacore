package io.github.dunwu.javacore.generics;

import java.util.ArrayList;
import java.util.List;

/**
 * 演示下限通配符：List<? super Fruit> 可以写入 Fruit 的子类，取出时只能当作 Object。
 */
public class GenericsSuperDemo01 {

    public static void demo() {
        List<? super Fruit> fruits = new ArrayList<>();
        fruits.add(new Apple());
        fruits.add(new Orange());
        Object apple = fruits.get(0);
        // Orange orange = fruits.get(1); // 编译会报错
    }

    public static void main(String[] args) {
        demo();
    }

    static class Fruit {

    }

    static class Apple extends Fruit {

    }

    static class Orange extends Fruit {

    }

}
