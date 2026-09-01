package io.github.dunwu.javacore.object;

/**
 * 示例：声明对象不会调用构造方法，只有 new 实例化时才会调用构造方法。
 */
public class ConstructorDemo01 {

    /**
     * 演示声明与实例化的区别。
     */
    public static void demo() {
        System.out.println("声明对象：Person per = null ;");
        Person per = null; // 声明对象时并不去调用构造方法
        System.out.println("实例化对象：per = new Person() ;");
        per = new Person(); // 实例化对象才会触发构造方法（Person 使用默认无参构造）
    }

    public static void main(String[] args) {
        demo();
    }

}
