package io.github.dunwu.javacore.method;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-16
 */
public class FinalMethodDemo {

    /**
     * 演示 final 方法：子类不能重写（放开注释即编译报错）。
     */
    public static void demo() {
        Father demo = new Son();
        demo.print();
    }

    public static void main(String[] args) {
        demo();
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
