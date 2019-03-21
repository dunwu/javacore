package io.github.dunwu.javacore.generics;

public class GenericsExtendsDemo02 {
    static class A { /* ... */ }
    interface B { /* ... */ }
    interface C { /* ... */ }
    static class D1 <T extends A & B & C> { /* ... */ }
    // static class D2 <T extends B & A & C> { /* ... */ } // 编译报错
    static class E extends A implements B, C { /* ... */ }

    public static void main(String[] args) {
        D1<E> demo1 = new D1<>();
        System.out.println(demo1.getClass().toString());
        // D1<String> demo2 = new D1<>(); // 编译报错
    }
}
