package io.github.dunwu.javacore.oop;

/**
 * 类的示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class Person {

    private String name; // 属性，用来描述事物的属性、状态

    private int age; // 属性，用来描述事物的属性、状态

    private String sex; // 属性，用来描述事物的属性、状态

    /**
     * 演示类的实例化：通过 new 创建对象。
     */
    public static void demo() {
        Person zhangsan = new Person(); // 对象
        Person lisi = new Person(); // 对象
    }

    public static void main(String[] args) {
        demo();
    }

    public static void think() {
        System.out.println("人区别与动物在于思考");
    }

    public void look() {
    } // 方法，用来描述事物的行为

    public void hear() {
    } // 方法，用来描述事物的行为

    public void talk() {
    } // 方法，用来描述事物的行为

}
