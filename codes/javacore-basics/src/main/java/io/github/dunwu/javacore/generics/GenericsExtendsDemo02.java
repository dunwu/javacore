package io.github.dunwu.javacore.generics;

/**
 * 演示多类型上界：T extends A & B & C，类必须在接口之前。
 */
public class GenericsExtendsDemo02 {

    /**
     * D1 的类型参数只能是 E（A 的子类且实现 B、C），D1<String> 会编译报错。
     */
    public static void demo() {
        D1<E> demo1 = new D1<>();
        System.out.println(demo1.getClass().toString());
        // D1<String> demo2 = new D1<>(); // 编译报错
    }

    public static void main(String[] args) {
        demo();
    }

    interface B {

        /* ... */
    }

    interface C {

        /* ... */
    }

    static class A {

        /* ... */
    }

    static class D1<T extends A & B & C> {

        /* ... */
    }

    // static class D2 <T extends B & A & C> { /* ... */ } // 编译报错
    static class E extends A implements B, C {

        /* ... */
    }

}
