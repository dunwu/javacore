package io.github.dunwu.jvm.chapter07;

public class FieldResolution {

    interface Interface0 {

        int A = 0;
    }

    interface Interface1 extends Interface0 {

        int A = 1;
    }

    interface Interface2 {

        int A = 2;
    }

    static class Parent implements Interface1 {

        public static int A = 3;
    }

    static class Sub extends Parent implements Interface2 {

        public static int A = 4;
    }

    public static void main(String[] args) {
        System.out.println(Sub.A);
    }
}
