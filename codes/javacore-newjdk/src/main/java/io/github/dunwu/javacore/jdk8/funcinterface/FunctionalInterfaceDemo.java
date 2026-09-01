package io.github.dunwu.javacore.jdk8.funcinterface;

/**
 * Java 8 函数式接口（Functional Interface）示例。
 * <p>
 * 函数式接口是"只有一个抽象方法"的接口，Lambda 表达式就是它的匿名实现。
 * <ul>
 * <li>{@code @FunctionalInterface} 注解用于编译期校验（可省略，但推荐显式标注）</li>
 * <li>接口中的默认方法、静态方法、私有方法不影响"唯一抽象方法"的判定</li>
 * <li>重写 Object 公有方法（如 equals）也不计入抽象方法数量</li>
 * </ul>
 */
public class FunctionalInterfaceDemo {

    /**
     * 示例 1：用 lambda 实现自定义函数式接口
     */
    public static void customConverter() {
        Converter<String, Integer> toInt = Integer::parseInt;
        System.out.println("字符串转整数: " + toInt.convert("2024"));
    }

    /**
     * 示例 2：函数式接口可以带泛型、默认方法
     */
    public static void validatorWithDefaultMethod() {
        Validator<String> notEmpty = s -> s != null && !s.isEmpty();
        System.out.println("校验 \"abc\": " + notEmpty.validate("abc"));
        System.out.println("校验 \"\": " + notEmpty.validate(""));
        // 使用默认方法取反
        System.out.println("取反后校验 \"\": " + notEmpty.negate().validate(""));
    }

    /**
     * 示例 3：JDK 自带的 Runnable 就是最经典的函数式接口
     */
    public static void builtinRunnable() {
        Runnable task = () -> System.out.println("Runnable 是函数式接口");
        task.run();
    }

    public static void main(String[] args) {
        customConverter();
        validatorWithDefaultMethod();
        builtinRunnable();
    }

    /**
     * 自定义函数式接口：只能有一个抽象方法
     */
    @FunctionalInterface
    interface Converter<F, T> {

        T convert(F from);

    }

    /**
     * 默认方法和静态方法不影响函数式接口的判定
     */
    @FunctionalInterface
    interface Validator<T> {

        boolean validate(T value);

        /**
         * 默认方法：返回一个逻辑取反的校验器
         */
        default Validator<T> negate() {
            return value -> !validate(value);
        }

    }

}
// Output:
// 字符串转整数: 2024
// 校验 "abc": true
// 校验 "": false
// 取反后校验 "": true
// Runnable 是函数式接口
