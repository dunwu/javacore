package io.github.dunwu.javacore.object;

/**
 * 示例：对象引用传递——方法内修改对象属性会影响外部对象（引用传参本质传的是地址）。
 */
public class ObjectRefDemo01 {

    /**
     * 演示方法内修改对象属性对外部的影响。
     */
    public static void demo() {
        Person person = new Person();
        person.age = 50;
        System.out.println("fun()方法调用之前：" + person.age);
        fun(person);
        System.out.println("fun()方法调用之后：" + person.age);
    }

    public static void main(String[] args) {
        demo();
    }

    private static void fun(Person d2) {
        d2.age = 10;
    }

}
