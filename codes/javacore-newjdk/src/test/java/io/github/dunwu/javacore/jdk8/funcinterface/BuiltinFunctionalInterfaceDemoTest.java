package io.github.dunwu.javacore.jdk8.funcinterface;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link BuiltinFunctionalInterfaceDemo} 单元测试。
 */
@DisplayName("Java 8 内置函数式接口示例测试")
public class BuiltinFunctionalInterfaceDemoTest {

    @Test
    @DisplayName("示例 1：Function 系列及 andThen/compose/identity 组合")
    public void testFunctionDemo() {
        String output = captureOutput(BuiltinFunctionalInterfaceDemo::functionDemo);
        assertThat(output)
            .contains("andThen: 长度=6")
            .contains("compose: 长度=6")
            .contains("identity: 原样返回")
            .contains("BiFunction: 6 * 7 = 42")
            .contains("UnaryOperator: ABC")
            .contains("BinaryOperator: max(3, 9) = 9");
    }

    @Test
    @DisplayName("示例 2：Consumer 系列消费动作与 andThen 串联")
    public void testConsumerDemo() {
        String output = captureOutput(BuiltinFunctionalInterfaceDemo::consumerDemo);
        assertThat(output)
            .contains("Consumer 打印: 先记录后打印")
            .contains("记录的内容: [先记录后打印]")
            .contains("BiConsumer: 张三, 18");
    }

    @Test
    @DisplayName("示例 3：Supplier 惰性提供值与方法引用工厂")
    public void testSupplierDemo() {
        String output = captureOutput(BuiltinFunctionalInterfaceDemo::supplierDemo);
        assertThat(output).contains("Supplier: 由 Supplier 提供, 工厂创建: ArrayList");
    }

    @Test
    @DisplayName("示例 4：Predicate 的 and/negate 组合判断")
    public void testPredicateDemo() {
        String output = captureOutput(BuiltinFunctionalInterfaceDemo::predicateDemo);
        assertThat(output)
            .contains("\"\" 非空且较短: false")
            .contains("\"hi\" 非空且较短: true")
            .contains("\"hello world\" 非空且较短: false")
            .contains("negate(notEmpty).test(\"\"): true");
    }

    /**
     * 捕获被测代码的标准输出，测试结束后恢复原 System.out
     */
    private static String captureOutput(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            action.run();
        } finally {
            System.setOut(original);
        }
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
    }

}
