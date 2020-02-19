package io.github.dunwu.javacore.generics;

import java.util.ArrayList;
import java.util.List;

public class GenericsSuperDemo01 {

    public static void main(String[] args) {
        List<? super Fruit> fruits = new ArrayList<>();
        fruits.add(new Apple());
        fruits.add(new Orange());
        Object apple = fruits.get(0);
        // Orange orange = fruits.get(1); // 编译会报错
    }

    static class Fruit {

    }

    static class Apple extends Fruit {

    }

    static class Orange extends Fruit {

    }

}
