package io.github.dunwu.javacore.method;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-16
 */
public class FinalMethodDemo {

    public static void main(String[] args) {
        Father demo = new Son();
        demo.print();
    }

    static class Father {

        protected final void print() {
            System.out.println("call Father print()");
        }

    }

    static class Son extends Father {

        // 放开注释会报错
        // @Override
        // protected void print() {
        // System.out.println("call print()");
        // }
    }

}
