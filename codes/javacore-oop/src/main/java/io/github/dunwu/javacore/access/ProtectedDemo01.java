package io.github.dunwu.javacore.access;

class SubHelloDemo extends Hello {

    public void print() {
        System.out.println("访问受保护属性：" + super.name);
    }

}

public class ProtectedDemo01 {

    public static void main(String[] args) {
        SubHelloDemo sub = new SubHelloDemo();
        sub.print();
    }

}
