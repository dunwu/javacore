package io.github.dunwu.javacore.annotation;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-04-05
 */
public class FunctionalInterfaceAnnotationDemo {

    /**
     * @param <T>
     * @FunctionalInterface 修饰的接口中定义两个抽象方法，编译时会报错
     */
    /*
     * @FunctionalInterface public interface Func2<T> { void printMessage(T message); void
     * printMessage2(T message); }
     */
    public static void main(String[] args) {
        Func1 func1 = new Func1() {
            @Override
            public void printMessage(Object message) {
                System.out.println(message);
            }
        };
        func1.printMessage("Hello");
        func1.printMessage(100);
    }

    public interface Func1<T> {

        void printMessage(T message);

    }

}
