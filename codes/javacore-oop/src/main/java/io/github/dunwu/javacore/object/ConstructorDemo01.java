package io.github.dunwu.javacore.object;

public class ConstructorDemo01 {

    public static void main(String[] args) {
        System.out.println("声明对象：Person per = null ;");
        Person per = null; // 声明对象时并不去调用构造方法
        System.out.println("实例化对象：per = new Person() ;");
        per = new Person();// 实例化对象
    }

}
