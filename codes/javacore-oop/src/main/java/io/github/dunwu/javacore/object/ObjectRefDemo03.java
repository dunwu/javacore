package io.github.dunwu.javacore.object;

/**
 * 示例：对象属性修改——方法内修改对象属性（而非重新赋值引用），会影响外部对象。
 */
public class ObjectRefDemo03 {

    /**
     * 演示方法内修改对象属性对外部的影响。
     */
    public static void demo() {
        Person person = new Person();
        person.name = "world";
        System.out.println("fun()方法调用之前：" + person.name);
        fun(person);
        System.out.println("fun()方法调用之后：" + person.name);
    }

    public static void main(String[] args) {
        demo();
    }

    private static void fun(Person person) {
        person.name = "javase";
    }

}
