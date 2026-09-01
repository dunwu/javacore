package io.github.dunwu.javacore.object;

/**
 * 示例：把对象传回自己的类中——类内部方法可以直接访问本类的私有属性。
 */
public class ObjectRefDemo04 {

    /**
     * 演示对象传回本类方法后私有属性被修改。
     */
    public static void demo() {
        Person2 person = new Person2(); // 实例化对象
        person.setAge(30); // 只能通过 setter 方法修改内容
        person.fun(person); // 把对象传回到自己的类中，方法内部直接修改私有属性
        System.out.println("age = " + person.getAge());
    }

    public static void main(String[] args) {
        demo();
    }

}
