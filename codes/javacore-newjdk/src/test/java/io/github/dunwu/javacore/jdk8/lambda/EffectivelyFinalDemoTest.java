package io.github.dunwu.javacore.jdk8.lambda;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link EffectivelyFinalDemo} 单元测试。
 */
@DisplayName("Java 8 Lambda 变量捕获示例测试")
public class EffectivelyFinalDemoTest {

    @Test
    @DisplayName("示例 1：捕获 final 与 effectively final 局部变量")
    public void testCaptureLocalVariables() {
        String output = captureOutput(() -> new EffectivelyFinalDemo().captureLocalVariables());
        assertThat(output)
            .contains("捕获 final 变量: final 变量")
            .contains("捕获 effectively final 变量: effectively final 变量");
    }

    @Test
    @DisplayName("示例 2：lambda 访问实例字段读取的是最新状态")
    public void testCaptureInstanceField() {
        String output = captureOutput(() -> new EffectivelyFinalDemo().captureInstanceField());
        assertThat(output).contains("捕获实例字段: 被修改后的实例字段");
    }

    @Test
    @DisplayName("示例 3：匿名内部类 this 指向自身，lambda this 指向外部类")
    public void testCompareThis() {
        String output = captureOutput(() -> new EffectivelyFinalDemo().compareThis());
        assertThat(output)
            .contains("匿名内部类 this 指向自身: true")
            .contains("Lambda this 指向外部类: true");
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
