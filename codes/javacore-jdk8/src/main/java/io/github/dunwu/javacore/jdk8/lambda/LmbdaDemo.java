package io.github.dunwu.javacore.jdk8.lambda;

/**
 * Lambda 表达式，也可称为闭包，它是推动 Java 8 发布的最重要新特性。 Lambda 允许把函数作为一个方法的参数（函数作为参数传递进方法中）。 使用 Lambda 表达式可以使代码变的更加简洁紧凑。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class LmbdaDemo {

    public static void main(String[] args) {
        LmbdaDemo demo = new LmbdaDemo();

        // 类型声明
        MathOperation add = (int a, int b) -> a + b;

        // 不用类型声明
        MathOperation sub = (a, b) -> a - b;

        // 大括号中的返回语句
        MathOperation mul = (int a, int b) -> {
            return a * b;
        };

        // 没有大括号及返回语句
        MathOperation div = (int a, int b) -> a / b;

        System.out.println("10 + 5 = " + demo.operate(10, 5, add));
        System.out.println("10 - 5 = " + demo.operate(10, 5, sub));
        System.out.println("10 x 5 = " + demo.operate(10, 5, mul));
        System.out.println("10 / 5 = " + demo.operate(10, 5, div));

        // 不用括号
        GreetingService greetService1 = message -> System.out.println("Hello " + message);

        // 用括号
        GreetingService greetService2 = (message) -> System.out.println("Hello " + message);

        greetService1.sayMessage("Runoob");
        greetService2.sayMessage("Google");
    }

    private int operate(int a, int b, MathOperation mathOperation) {
        return mathOperation.operation(a, b);
    }

    interface MathOperation {

        int operation(int a, int b);

    }

    interface GreetingService {

        void sayMessage(String message);

    }

}
