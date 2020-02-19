package io.github.dunwu.javacore.annotation;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-30
 */
@SuppressWarnings({ "uncheck", "deprecation" })
public class InternalAnnotationDemo {

    public static void main(String[] args) {
        A obj = new B();
        obj.method1();
        obj.method2();
    }

    /**
     * @SuppressWarnings 标记消除当前类的告警信息
     */
    @SuppressWarnings({ "deprecation" })
    static class A {

        public void method1() {
            System.out.println("call method1");
        }

        /**
         * @Deprecated 标记当前方法为废弃方法，不建议使用
         */
        @Deprecated
        public void method2() {
            System.out.println("call method2");
        }

    }

    /**
     * @Deprecated 标记当前类为废弃类，不建议使用
     */
    @Deprecated
    static class B extends A {

        /**
         * @Override 标记显示指明当前方法覆写了父类或接口的方法
         */
        @Override
        public void method1() {
        }

    }

}
