package io.github.dunwu.javacore.jdk10.var;

import java.util.List;

/**
 * Java 10 var 使用限制示例。
 * <p>
 * {@code var} 只能用于带有初始化表达式的局部变量，以下场景均不合法（本类以注释形式展示反例）：
 * <ul>
 * <li>成员变量（字段）</li>
 * <li>方法参数</li>
 * <li>方法返回值类型</li>
 * <li>构造器参数</li>
 * <li>没有初始化表达式，或初始化为 null、lambda、数组初始化器</li>
 * <li>catch 的形式参数</li>
 * </ul>
 * 原因：var 依赖初始化表达式进行类型推断，上述场景编译器无从推断。
 */
public class VarLimitDemo {

    // 反例：var 不能用于成员变量
    // private var field = 1; // 编译错误

    /**
     * 示例 1：合法用法——局部变量带初始化表达式
     */
    public static void legalUsage() {
        var ok = "合法用法";
        System.out.println(ok);

        // 反例：没有初始化表达式
        // var noInit; // 编译错误：cannot use 'var' on variable without initializer

        // 反例：初始化为 null，编译器无法推断类型
        // var nullVar = null; // 编译错误：variable initializer is 'null'

        // 反例：初始化为 lambda 表达式，无法推断目标类型
        // var runnable = () -> System.out.println("hi"); // 编译错误
    }

    /**
     * 示例 2：反例——catch 参数不能使用 var
     */
    public static void catchParamLimit() {
        try {
            List.of("a").get(1);
        } catch (IndexOutOfBoundsException e) {
            // catch (var e) { ... } 是非法的
            System.out.println("捕获异常: " + e.getClass().getSimpleName());
        }
    }

    /**
     * 示例 3：反例——方法参数与方法返回值不能使用 var
     */
    public static void methodSignatureLimit() {
        // var result = compute(var input); // 编译错误
        System.out.println("compute 结果: " + compute(10));
    }

    public static void main(String[] args) {
        legalUsage();
        catchParamLimit();
        methodSignatureLimit();
    }

    // 反例：var 不能用于方法参数和返回值类型
    // private static var compute(var input) { return input; } // 编译错误
    private static int compute(int input) {
        return input * 2;
    }

}
// Output:
// 合法用法
// 捕获异常: IndexOutOfBoundsException
// compute 结果: 20
