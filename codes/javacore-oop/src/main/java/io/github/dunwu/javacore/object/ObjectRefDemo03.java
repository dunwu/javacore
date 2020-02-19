package io.github.dunwu.javacore.object;

public class ObjectRefDemo03 {

    public static void main(String[] args) {
        Person person = new Person();
        person.name = "world";
        System.out.println("fun()方法调用之前：" + person.name);
        fun(person);
        System.out.println("fun()方法调用之后：" + person.name);
    }

    private static void fun(Person person) {
        person.name = "javase";
    }

}
