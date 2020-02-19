package io.github.dunwu.javacore.object;

public class ClassDemo02 {

    public static void main(String[] args) {
        Person person = new Person();

        // 给类的属性赋值
        person.name = "张三";
        person.age = 30;

        // 调用类的方法
        person.tell();
    }

}
