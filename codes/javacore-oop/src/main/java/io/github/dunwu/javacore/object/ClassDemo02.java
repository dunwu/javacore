package io.github.dunwu.javacore.object;

/**
 * 示例：创建对象后给属性赋值，并调用对象的方法。
 */
public class ClassDemo02 {

    /**
     * 演示通过对象访问属性、调用方法。
     */
    public static void demo() {
        Person person = new Person();

        // 给类的属性赋值（直接访问属性，实际开发中建议用 setter）
        person.name = "张三";
        person.age = 30;

        // 调用类的方法
        person.tell();
    }

    public static void main(String[] args) {
        demo();
    }

}
