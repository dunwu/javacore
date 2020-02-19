package io.github.dunwu.javacore.object;

public class ObjectRefDemo01 {

    public static void main(String[] args) {
        Person person = new Person();
        person.age = 50;
        System.out.println("fun()方法调用之前：" + person.age);
        fun(person);
        System.out.println("fun()方法调用之后：" + person.age);
    }

    private static void fun(Person d2) {
        d2.age = 10;
    }

}
