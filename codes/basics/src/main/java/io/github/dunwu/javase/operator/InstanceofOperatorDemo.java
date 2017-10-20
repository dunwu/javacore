package io.github.dunwu.javase.operator;

class Animal {}

class Cat extends Animal {}

/**
 * instanceof 运算符示例
 */
public class InstanceofOperatorDemo {
    public static void main(String args[]) {
        Animal animal = new Cat();
        boolean result = animal instanceof Cat;
        System.out.println(result);
    }
};
