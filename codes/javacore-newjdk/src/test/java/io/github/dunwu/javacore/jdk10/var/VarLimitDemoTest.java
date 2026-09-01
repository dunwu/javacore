package io.github.dunwu.javacore.jdk10.var;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link VarLimitDemo} 单元测试。
 */
@DisplayName("Java 10 var 使用限制示例测试")
public class VarLimitDemoTest {

    @Test
    @DisplayName("示例 1：合法用法为带初始化表达式的局部变量")
    public void testLegalUsage() {
        String output = captureOutput(VarLimitDemo::legalUsage);
        assertThat(output).contains("合法用法");
    }

    @Test
    @DisplayName("示例 2：catch 参数不能使用 var，需显式声明异常类型")
    public void testCatchParamLimit() {
        String output = captureOutput(VarLimitDemo::catchParamLimit);
        assertThat(output).contains("捕获异常: IndexOutOfBoundsException");
    }

    @Test
    @DisplayName("示例 3：方法参数与返回值不能使用 var")
    public void testMethodSignatureLimit() {
        String output = captureOutput(VarLimitDemo::methodSignatureLimit);
        assertThat(output).contains("compute 结果: 20");
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
