package io.github.dunwu.javacore.operator;

/**
 * instanceof 操作符示例：判断对象是否是某个类（或其子类）的实例。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class InstanceofOperatorDemo {

    /**
     * 演示 instanceof：animal 实际指向 Cat 对象，因此判断为 true。
     */
    public static void demo() {
        Animal animal = new Cat();
        boolean result = animal instanceof Cat;
        System.out.println(result);
    }

    public static void main(String[] args) {
        demo();
    }

    static class Animal {

    }

    static class Cat extends Animal {

    }

}
// output:
// true
