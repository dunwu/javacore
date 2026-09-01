package io.github.dunwu.javacore.jdk21.switchstmt;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link SwitchPatternDemo} 单元测试。
 */
@DisplayName("Java 21 Switch 模式匹配示例测试")
public class SwitchPatternDemoTest {

    @Test
    @DisplayName("示例 1：类型模式按类型分派，case null 显式处理空值")
    public void testTypePatternAndNull() {
        String output = captureOutput(SwitchPatternDemo::typePatternAndNull);
        assertThat(output)
            .contains("字符串: Java")
            .contains("整数: 21")
            .contains("浮点数: 3.14")
            .contains("空值");
    }

    @Test
    @DisplayName("示例 2：守卫条件 when 在同一类型下进一步细分")
    public void testGuardWhen() {
        String output = captureOutput(SwitchPatternDemo::guardWhen);
        assertThat(output)
            .contains("正整数: 100")
            .contains("非正整数: -5")
            .contains("非空字符串: 0");
    }

    @Test
    @DisplayName("示例 3：sealed 类型穷举覆盖全部子类型后无需 default")
    public void testSealedExhaustive() {
        String output = captureOutput(SwitchPatternDemo::sealedExhaustive);
        assertThat(output)
            .contains("圆面积: 12.566370614359172")
            .contains("正方形面积: 9.0");
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
