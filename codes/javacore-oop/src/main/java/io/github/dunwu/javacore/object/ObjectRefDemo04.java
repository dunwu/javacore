package io.github.dunwu.javacore.object;

public class ObjectRefDemo04 {

    public static void main(String[] args) {
        Person2 person = new Person2(); // 实例化对象
        person.setAge(30); // 只能通过setter方法修改内容
        person.fun(person); // 此处把对象传回到自己的类中
        System.out.println("age = " + person.getAge());
    }

}
