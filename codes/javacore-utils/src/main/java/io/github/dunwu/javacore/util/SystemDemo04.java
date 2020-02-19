package io.github.dunwu.javacore.util;

class Person {

    private String name;

    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() { // 覆写toString()方法
        return "姓名：" + this.name + "，年龄：" + this.age;
    }

    @Override
    public void finalize() throws Throwable { // 对象释放空间时默认调用此方法
        System.out.println("对象被释放 --> " + this);
    }

}

public class SystemDemo04 {

    public static void main(String[] args) {
        Person per = new Person("张三", 30);
        per = null; // 断开引用
        System.gc(); // 强制性释放空间
    }

}
