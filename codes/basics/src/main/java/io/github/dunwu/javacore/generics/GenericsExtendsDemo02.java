package io.github.dunwu.javacore.generics;

import java.util.ArrayList;
import java.util.List;

public class GenericsExtendsDemo02 {
    static class Fruit{}
    static class Apple extends GenericsSuperDemo01.Fruit {}
    static class Orange extends GenericsSuperDemo01.Fruit {}

    public static void main(String[] args) {
        List<? super Fruit> fruits = new ArrayList<>();
        // fruits.add(new Apple()); // 编译会报错
        // fruits.add(new Orange()); // 编译会报错
    }
}
// Output:
// 5
// 8.8
// pear
