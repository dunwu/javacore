package io.github.dunwu.javacore.jdk11.lambda;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link LambdaVarDemo} 单元测试。
 */
@DisplayName("Java 11 lambda 参数使用 var 示例测试")
public class LambdaVarDemoTest {

    @Test
    @DisplayName("示例 1：不带注解时 var 与省略类型等价")
    public void testVarWithoutAnnotation() {
        String output = captureOutput(LambdaVarDemo::varWithoutAnnotation);
        assertThat(output).contains("不带注解: 3, 7");
    }

    @Test
    @DisplayName("示例 2：lambda 参数上使用注解无需写出完整类型")
    public void testVarWithAnnotation() {
        String output = captureOutput(LambdaVarDemo::varWithAnnotation);
        assertThat(output).contains("Hello, Java!");
    }

    @Test
    @DisplayName("示例 3：结合 Stream 对参数标注注解")
    public void testVarWithStream() {
        String output = captureOutput(LambdaVarDemo::varWithStream);
        assertThat(output)
            .contains("处理元素: a")
            .contains("处理元素: b");
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
