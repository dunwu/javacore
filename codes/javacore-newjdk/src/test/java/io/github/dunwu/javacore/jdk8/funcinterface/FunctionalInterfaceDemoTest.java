package io.github.dunwu.javacore.jdk8.funcinterface;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link FunctionalInterfaceDemo} 单元测试。
 */
@DisplayName("Java 8 函数式接口示例测试")
public class FunctionalInterfaceDemoTest {

    @Test
    @DisplayName("示例 1：lambda 实现自定义函数式接口完成类型转换")
    public void testCustomConverter() {
        String output = captureOutput(FunctionalInterfaceDemo::customConverter);
        assertThat(output).contains("字符串转整数: 2024");
    }

    @Test
    @DisplayName("示例 2：函数式接口的默认方法 negate 取反校验逻辑")
    public void testValidatorWithDefaultMethod() {
        String output = captureOutput(FunctionalInterfaceDemo::validatorWithDefaultMethod);
        assertThat(output)
            .contains("校验 \"abc\": true")
            .contains("校验 \"\": false")
            .contains("取反后校验 \"\": true");
    }

    @Test
    @DisplayName("示例 3：JDK 自带的 Runnable 就是函数式接口")
    public void testBuiltinRunnable() {
        String output = captureOutput(FunctionalInterfaceDemo::builtinRunnable);
        assertThat(output).contains("Runnable 是函数式接口");
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
