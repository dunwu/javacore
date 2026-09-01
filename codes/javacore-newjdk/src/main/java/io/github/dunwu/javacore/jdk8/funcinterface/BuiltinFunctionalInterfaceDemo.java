package io.github.dunwu.javacore.jdk8.funcinterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Java 8 内置函数式接口示例。
 * <p>
 * {@code java.util.function} 包提供了 43 个内置函数式接口，核心是四大类：
 * <ul>
 * <li>{@link Function}：消费一个值，返回另一个值（T -&gt; R），还有 BiFunction、UnaryOperator、BinaryOperator</li>
 * <li>{@link Consumer}：消费一个值，无返回值（T -&gt; void），还有 BiConsumer</li>
 * <li>{@link Supplier}：无参数，返回一个值（() -&gt; T）</li>
 * <li>{@link Predicate}：消费一个值，返回布尔值（T -&gt; boolean）</li>
 * </ul>
 * 它们都提供了组合方法（andThen/compose/and/or/negate），支持函数式组合。
 */
public class BuiltinFunctionalInterfaceDemo {

    /**
     * 示例 1：Function 系列（Function/BiFunction/UnaryOperator/BinaryOperator）
     */
    public static void functionDemo() {
        // Function：T -> R，andThen 表示先执行自身再执行后续，compose 相反
        Function<String, Integer> toLength = String::length;
        Function<Integer, String> toMessage = n -> "长度=" + n;
        System.out.println("andThen: " + toLength.andThen(toMessage).apply("Java 8"));
        System.out.println("compose: " + toMessage.compose(toLength).apply("Java 8"));
        System.out.println("identity: " + Function.<String>identity().apply("原样返回"));

        // BiFunction：(T, U) -> R
        BiFunction<Integer, Integer, Integer> multiply = (a, b) -> a * b;
        System.out.println("BiFunction: 6 * 7 = " + multiply.apply(6, 7));

        // UnaryOperator / BinaryOperator：Function 的特殊形式（入参出参同类型）
        UnaryOperator<String> upper = String::toUpperCase;
        BinaryOperator<Integer> max = Integer::max;
        System.out.println("UnaryOperator: " + upper.apply("abc"));
        System.out.println("BinaryOperator: max(3, 9) = " + max.apply(3, 9));
    }

    /**
     * 示例 2：Consumer 系列（Consumer/BiConsumer），消费动作无返回值
     */
    public static void consumerDemo() {
        // Consumer：消费动作，andThen 串联多个消费动作
        List<String> log = new ArrayList<>();
        Consumer<String> append = log::add;
        Consumer<String> print = s -> System.out.println("Consumer 打印: " + s);
        append.andThen(print).accept("先记录后打印");
        System.out.println("记录的内容: " + log);

        BiConsumer<String, Integer> biConsumer = (name, age) -> System.out.println("BiConsumer: " + name + ", " + age);
        biConsumer.accept("张三", 18);
    }

    /**
     * 示例 3：Supplier，惰性提供值（常用于工厂、默认值、延迟初始化）
     */
    public static void supplierDemo() {
        Supplier<String> greeting = () -> "由 Supplier 提供";
        Supplier<List<String>> listFactory = ArrayList::new;
        System.out.println("Supplier: " + greeting.get() + ", 工厂创建: " + listFactory.get().getClass().getSimpleName());
    }

    /**
     * 示例 4：Predicate，条件判断，支持 and/or/negate 组合
     */
    public static void predicateDemo() {
        Predicate<String> notEmpty = s -> !s.isEmpty();
        Predicate<String> shortText = s -> s.length() < 5;
        List<String> texts = Arrays.asList("", "hi", "hello world");
        texts.forEach(t -> System.out.println("\"" + t + "\" 非空且较短: "
            + notEmpty.and(shortText).test(t)));
        System.out.println("negate(notEmpty).test(\"\"): " + notEmpty.negate().test(""));
    }

    public static void main(String[] args) {
        functionDemo();
        consumerDemo();
        supplierDemo();
        predicateDemo();
    }

}
// Output:
// andThen: 长度=6
// compose: 长度=6
// identity: 原样返回
// BiFunction: 6 * 7 = 42
// UnaryOperator: ABC
// BinaryOperator: max(3, 9) = 9
// Consumer 打印: 先记录后打印
// 记录的内容: [先记录后打印]
// BiConsumer: 张三, 18
// Supplier: 由 Supplier 提供, 工厂创建: ArrayList
// "" 非空且较短: false
// "hi" 非空且较短: true
// "hello world" 非空且较短: false
// negate(notEmpty).test(""): true
