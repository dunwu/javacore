package io.github.dunwu.javacore.jdk16.pattern;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link InstanceofPatternDemo} 单元测试。
 */
@DisplayName("Java 16 instanceof 模式匹配示例测试")
public class InstanceofPatternDemoTest {

    @Test
    @DisplayName("示例 1：类型判断 + 强制转换 + 赋值三步合并为一步")
    public void testPatternMatchingBasic() {
        String output = captureOutput(InstanceofPatternDemo::patternMatchingBasic);
        assertThat(output)
            .contains("字符串，长度 7: Java 16")
            .contains("整数: 84")
            .contains("列表，元素个数: 2")
            .contains("其他类型: Double");
    }

    @Test
    @DisplayName("示例 2：模式变量与逻辑运算符组合及取反场景的作用域")
    public void testPatternWithLogicalOperators() {
        String output = captureOutput(InstanceofPatternDemo::patternWithLogicalOperators);
        assertThat(output)
            .contains("长度大于 5 的字符串: HELLO PATTERN MATCHING")
            .contains("取反分支外仍可访问 s: 22 个字符");
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
