package io.github.dunwu.javacore.jdk11.lambda;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Java 11 lambda 表达式参数使用 var 示例。
 * <p>
 * Java 10 引入的 var 只能用于局部变量；Java 11 扩展了 var 的适用范围，
 * 允许在 lambda 表达式的隐式类型参数上使用 var。
 * <p>
 * 这一扩展的主要价值：可以在 lambda 参数上添加注解（注解必须依附于显式类型声明），
 * 而不必写出冗长的完整参数类型。
 */
public class LambdaVarDemo {

    /**
     * 示例 1：不带注解时，var 与省略类型等价（这种场景 var 并非必需）
     */
    public static void varWithoutAnnotation() {
        BiFunction<Integer, Integer, Integer> sum1 = (a, b) -> a + b;
        BiFunction<Integer, Integer, Integer> sum2 = (var a, var b) -> a + b;
        System.out.println("不带注解: " + sum1.apply(1, 2) + ", " + sum2.apply(3, 4));
    }

    /**
     * 示例 2：核心价值——lambda 参数上使用注解，无需写出完整类型
     */
    public static void varWithAnnotation() {
        GreetingService greeting = (@UpperCase var name) -> "Hello, " + name + "!";
        System.out.println(greeting.greet("Java"));
    }

    /**
     * 示例 3：结合 Stream 使用——对参数标注注解
     */
    public static void varWithStream() {
        List.of("a", "", "b").forEach((@NonNull var item) -> {
            if (!item.isEmpty()) {
                System.out.println("处理元素: " + item);
            }
        });
    }

    public static void main(String[] args) {
        varWithoutAnnotation();
        varWithAnnotation();
        varWithStream();
    }

    @FunctionalInterface
    interface GreetingService {

        String greet(String name);

    }

    /**
     * 示例注解：标记字符串应转为大写
     */
    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @interface UpperCase {

    }

    /**
     * 示例注解：标记参数不允许为 null
     */
    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @interface NonNull {

    }

}
// Output:
// 不带注解: 3, 7
// Hello, Java!
// 处理元素: a
// 处理元素: b
