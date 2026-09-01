package io.github.dunwu.javacore.jdk8.lambda;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Java 8 Lambda 表达式基础示例。
 * <p>
 * Lambda 表达式（JEP 126）是 Java 8 最重要的特性之一，
 * 允许把函数作为参数传递，使代码更简洁，是函数式编程在 Java 中的落地。
 * <p>
 * 基本语法：{@code (参数列表) -> { 方法体 }}，根据上下文可简化：
 * <ul>
 * <li>参数类型可省略（编译器推断）</li>
 * <li>单个参数可省略括号</li>
 * <li>单行表达式可省略大括号和 return</li>
 * </ul>
 * Lambda 本质是函数式接口（只有一个抽象方法的接口）的匿名实现。
 */
public class LambdaBasicDemo {

    /**
     * 示例 1：无参数 lambda 替代匿名内部类实现 Runnable
     */
    public static void runnableComparison() {
        Runnable oldStyle = new Runnable() {
            @Override
            public void run() {
                System.out.println("匿名内部类写法");
            }
        };
        oldStyle.run();
        Runnable lambdaStyle = () -> System.out.println("Lambda 写法");
        lambdaStyle.run();
    }

    /**
     * 示例 2：单参数 lambda（可省略括号与类型）
     */
    public static void singleParameterLambda() {
        java.util.function.Consumer<String> printer = message -> System.out.println("打印: " + message);
        printer.accept("Hello Lambda");
    }

    /**
     * 示例 3：多参数 + 单行表达式（省略大括号和 return）
     */
    public static void expressionLambda() {
        java.util.function.BinaryOperator<Integer> add = (a, b) -> a + b;
        System.out.println("3 + 5 = " + add.apply(3, 5));
    }

    /**
     * 示例 4：多行语句 lambda + 最常见用途——集合排序与遍历
     */
    public static void sortAndForEach() {
        // 多行语句需要大括号和显式 return
        Comparator<String> lengthComparator = (s1, s2) -> {
            int len1 = s1.length();
            int len2 = s2.length();
            return Integer.compare(len1, len2);
        };
        List<String> words = Arrays.asList("banana", "apple", "cherry");
        words.sort(lengthComparator);
        System.out.println("按长度排序: " + words);
        words.forEach(word -> System.out.println("  - " + word.toUpperCase()));
    }

    public static void main(String[] args) {
        runnableComparison();
        singleParameterLambda();
        expressionLambda();
        sortAndForEach();
    }

}
// Output:
// 匿名内部类写法
// Lambda 写法
// 打印: Hello Lambda
// 3 + 5 = 8
// 按长度排序: [apple, banana, cherry]
//   - APPLE
//   - BANANA
//   - CHERRY
