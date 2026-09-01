package io.github.dunwu.javacore.jdk21.record;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link RecordPatternDemo} 单元测试。
 */
@DisplayName("Java 21 Record 模式解构示例测试")
public class RecordPatternDemoTest {

    @Test
    @DisplayName("示例 1：instanceof + record 模式一步完成类型判断与解构")
    public void testInstanceofPattern() {
        String output = captureOutput(RecordPatternDemo::instanceofPattern);
        assertThat(output).contains("解构 Point: x=3, y=4");
    }

    @Test
    @DisplayName("示例 2：switch + record 模式配合守卫条件 when")
    public void testSwitchPattern() {
        String output = captureOutput(RecordPatternDemo::switchPattern);
        assertThat(output).contains("对角线上的点: (0, 0)");
    }

    @Test
    @DisplayName("示例 3：嵌套解构一次性取出全部四个分量计算线段长度")
    public void testNestedPattern() {
        String output = captureOutput(RecordPatternDemo::nestedPattern);
        assertThat(output).contains("线段长度: 5.0");
    }

    @Test
    @DisplayName("示例 4：var 推断组件类型")
    public void testVarPattern() {
        String output = captureOutput(RecordPatternDemo::varPattern);
        assertThat(output).contains("var 解构: x=6, y=8");
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
