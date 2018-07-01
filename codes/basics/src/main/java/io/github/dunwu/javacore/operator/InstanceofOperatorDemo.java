package io.github.dunwu.javacore.operator;

/**
 * 算术操作符示例
 * @author Zhang Peng
 */
public class InstanceofOperatorDemo {
    public static void main(String[] args) {
        Animal animal = new Cat();
        boolean result = animal instanceof Cat;
        System.out.println(result);
    }
}

class Animal {}
class Cat extends Animal {}

// output:
// true
