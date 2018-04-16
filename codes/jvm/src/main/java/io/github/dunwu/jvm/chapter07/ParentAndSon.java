package io.github.dunwu.jvm.chapter07;

/**
 * @author Zhang Peng
 * @date 2018/4/16
 */
public class ParentAndSon {

    static class Parent {

        public static int A = 1;

        static {
            A = 2;
        }
    }

    static class Sub extends Parent {

        public static int B = A;
    }

    public static void main(String[] args) {
        System.out.println(Sub.B);
    }


}
