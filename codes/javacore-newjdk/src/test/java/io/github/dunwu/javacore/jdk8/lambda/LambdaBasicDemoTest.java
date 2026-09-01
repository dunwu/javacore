package io.github.dunwu.javacore.jdk8.lambda;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link LambdaBasicDemo} 单元测试。
 */
@DisplayName("Java 8 Lambda 表达式基础示例测试")
public class LambdaBasicDemoTest {

    @Test
    @DisplayName("示例 1：无参数 lambda 与匿名内部类等价")
    public void testRunnableComparison() {
        String output = captureOutput(LambdaBasicDemo::runnableComparison);
        assertThat(output)
            .contains("匿名内部类写法")
            .contains("Lambda 写法");
    }

    @Test
    @DisplayName("示例 2：单参数 lambda 消费字符串")
    public void testSingleParameterLambda() {
        String output = captureOutput(LambdaBasicDemo::singleParameterLambda);
        assertThat(output).contains("打印: Hello Lambda");
    }

    @Test
    @DisplayName("示例 3：多参数单行表达式 lambda 求和")
    public void testExpressionLambda() {
        String output = captureOutput(LambdaBasicDemo::expressionLambda);
        assertThat(output).contains("3 + 5 = 8");
    }

    @Test
    @DisplayName("示例 4：多行 lambda 比较器实现集合排序与遍历")
    public void testSortAndForEach() {
        String output = captureOutput(LambdaBasicDemo::sortAndForEach);
        assertThat(output)
            .contains("按长度排序: [apple, banana, cherry]")
            .contains("- APPLE")
            .contains("- BANANA")
            .contains("- CHERRY");
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
