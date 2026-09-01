package io.github.dunwu.javacore.jdk8.lambda;

import java.util.function.Supplier;

/**
 * Java 8 Lambda 变量捕获（闭包）示例。
 * <p>
 * Lambda 可以捕获外部变量，但规则与匿名内部类一致：
 * <ul>
 * <li>捕获的局部变量必须是 final 或 <b>effectively final</b>（事实上不可变：声明后不再重新赋值）</li>
 * <li>捕获实例字段/静态字段不受此限制（因为访问的是对象状态，而非拷贝）</li>
 * </ul>
 * 原因：Lambda 捕获局部变量时是值的拷贝，若允许原变量修改，会产生数据不一致。
 */
public class EffectivelyFinalDemo {

    private String instanceField = "实例字段";

    /**
     * 示例 1：捕获 final 与 effectively final 局部变量
     */
    public void captureLocalVariables() {
        // 捕获 final 变量
        final String finalVar = "final 变量";
        Supplier<String> s1 = () -> finalVar;
        System.out.println("捕获 final 变量: " + s1.get());

        // 捕获 effectively final 变量（未显式 final，但从未重新赋值）
        String effectivelyFinal = "effectively final 变量";
        Supplier<String> s2 = () -> effectivelyFinal;
        System.out.println("捕获 effectively final 变量: " + s2.get());

        // 被捕获的局部变量不允许再赋值（以下代码编译失败）
        // String captured = "x";
        // Runnable r = () -> System.out.println(captured);
        // captured = "y"; // 编译错误：Variable used in lambda expression should be final or effectively final
    }

    /**
     * 示例 2：捕获实例字段不受限制：lambda 访问的是 this 的状态
     */
    public void captureInstanceField() {
        Supplier<String> supplier = () -> instanceField;
        instanceField = "被修改后的实例字段";
        System.out.println("捕获实例字段: " + supplier.get());
    }

    /**
     * 示例 3：Lambda 与匿名内部类的 this 差异：
     * 匿名内部类有独立的 this（指向自身），lambda 的 this 指向外部对象
     */
    public void compareThis() {
        Runnable anonymous = new Runnable() {
            @Override
            public void run() {
                System.out.println("匿名内部类 this 指向自身: " + (this instanceof Runnable));
            }
        };
        anonymous.run();
        Runnable lambda = () -> System.out.println("Lambda this 指向外部类: "
            + (this instanceof EffectivelyFinalDemo));
        lambda.run();
    }

    public static void main(String[] args) {
        EffectivelyFinalDemo demo = new EffectivelyFinalDemo();
        demo.captureLocalVariables();
        demo.captureInstanceField();
        demo.compareThis();
    }

}
// Output:
// 捕获 final 变量: final 变量
// 捕获 effectively final 变量: effectively final 变量
// 捕获实例字段: 被修改后的实例字段
// 匿名内部类 this 指向自身: true
// Lambda this 指向外部类: true
