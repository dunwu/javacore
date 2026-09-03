package io.github.dunwu.javacore.annotation;

/**
 * "@Override" 示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-30
 */
public class OverrideAnnotationDemo {

    /**
     * 演示 {@code @Override}：显式声明当前方法覆写了父类或接口的方法
     * <p>
     * 价值在于让编译器帮忙校验：如果方法名拼错或签名不一致，实际上并未构成覆写，编译会直接报错
     */
    public static void demo() {
        Person per = new Man();
        System.out.println(per.getName());
    }

    public static void main(String[] args) {
        demo();
    }

    static class Person {

        public String getName() {
            return "getName";
        }

    }

    static class Man extends Person {

        @Override
        public String getName() {
            return "override getName";
        }

        /**
         * 放开下面的注释，编译时会告警
         */
        /*
         * @Override public String getName2() { return "override getName2"; }
         */
    }

}
