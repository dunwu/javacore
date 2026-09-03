package io.github.dunwu.javacore.annotation;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-04-05
 */
public class FunctionalInterfaceAnnotationDemo {

    /**
     * 演示 {@code @FunctionalInterface}：被它修饰的接口有且仅有一个抽象方法，
     * 因此可以用匿名内部类、lambda 表达式或方法引用来实现
     * <p>
     * 该注解的价值在于编译期约束：如果接口里定义了第二个抽象方法，编译会直接报错（见下方保留的反例）
     */
    public static void demo() {
        // 写法一：匿名内部类
        Func1<Object> func1 = new Func1<>() {

            @Override
            public void printMessage(Object message) {
                System.out.println(message);
            }
        };
        func1.printMessage("Hello");
        func1.printMessage(100);

        // 写法二：方法引用（只有函数式接口才能这样写）
        Func1<Object> func2 = System.out::println;
        func2.printMessage("World");
    }

    /**
     * 反例：{@code @FunctionalInterface} 修饰的接口中定义两个抽象方法，编译时会报错
     */
    /*
     * @FunctionalInterface public interface Func2<T> { void printMessage(T message); void
     * printMessage2(T message); }
     */
    public static void main(String[] args) {
        demo();
    }
    // Output:
    // Hello
    // 100
    // World

    /**
     * 函数式接口：有且仅有一个抽象方法
     *
     * @param <T> 消息类型
     */
    @FunctionalInterface
    public interface Func1<T> {

        void printMessage(T message);

    }

}
