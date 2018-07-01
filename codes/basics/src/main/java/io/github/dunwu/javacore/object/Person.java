package io.github.dunwu.javacore.object;

/**
 * 类的示例
 * @author Zhang Peng
 */
public class Person {
    private String name; // 属性，用来描述事物的属性、状态
    private int age; // 属性，用来描述事物的属性、状态
    private String sex; // 属性，用来描述事物的属性、状态

    public void look() {} // 方法，用来描述事物的行为

    public void hear() {} // 方法，用来描述事物的行为

    public void talk() {} // 方法，用来描述事物的行为

    public static void think() {
        System.out.println("人区别与动物在于思考");
    }

    public static void main(String[] args) {
        Person zhangsan = new Person(); // 对象
        Person lisi = new Person(); // 对象
    }
}
