package io.github.dunwu.javacore.annotation;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-30
 */
@SuppressWarnings({ "unchecked", "deprecation" })
public class InternalAnnotationDemo {

    /**
     * 演示 {@code @SuppressWarnings}、{@code @Deprecated}、{@code @Override} 三个内置注解的组合使用
     * <p>
     * {@code B} 重写了 {@code method1()} 且方法体为空，因此通过父类引用 {@code A} 调用 {@code method1()}
     * 不会有任何输出（动态绑定到子类实现）；{@code method2()} 未被重写，调用的仍是父类 {@code A} 的实现
     */
    public static void demo() {
        A obj = new B();
        obj.method1();
        obj.method2();
    }

    public static void main(String[] args) {
        demo();
    }
    // Output:
    // call method2

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
