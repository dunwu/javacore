package io.github.dunwu.javacore.jdk14.switchstmt;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link SwitchExpressionDemo} 单元测试。
 */
@DisplayName("Java 14 Switch 表达式示例测试")
public class SwitchExpressionDemoTest {

    @Test
    @DisplayName("示例 1：箭头语法单行表达式分支直接返回值")
    public void testArrowSyntax() {
        String output = captureOutput(SwitchExpressionDemo::arrowSyntax);
        assertThat(output).contains("WEDNESDAY 是周末吗: false");
    }

    @Test
    @DisplayName("示例 2：代码块分支使用 yield 返回值")
    public void testYieldDemo() {
        String output = captureOutput(SwitchExpressionDemo::yieldDemo);
        assertThat(output)
            .contains("（分支内可以有多条语句，用 yield 返回）")
            .contains("WEDNESDAY 的英文字母数: 9");
    }

    @Test
    @DisplayName("示例 3：传统 switch 语句写法仍然合法")
    public void testOldStyleSwitch() {
        String output = captureOutput(SwitchExpressionDemo::oldStyleSwitch);
        assertThat(output).contains("传统 switch 结果: 工作日");
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
