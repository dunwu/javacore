package io.github.dunwu.javacore.jdk8.iface;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link DefaultMethodDemo} 单元测试。
 */
@DisplayName("Java 8 接口默认方法与静态方法示例测试")
public class DefaultMethodDemoTest {

    @Test
    @DisplayName("示例 1：默认方法可直接继承使用，也可被重写")
    public void testDefaultMethodInheritAndOverride() {
        String output = captureOutput(DefaultMethodDemo::defaultMethodInheritAndOverride);
        assertThat(output)
            .contains("Greeting 默认: 你好")
            .contains("重写默认方法: Hello!");
    }

    @Test
    @DisplayName("示例 2：接口静态方法只能通过接口名调用")
    public void testStaticMethod() {
        String output = captureOutput(DefaultMethodDemo::staticMethod);
        assertThat(output).contains("接口静态方法: Greeting v1.0");
    }

    @Test
    @DisplayName("示例 3：菱形冲突时实现类显式重写并选择默认方法")
    public void testDiamondConflict() {
        String output = captureOutput(DefaultMethodDemo::diamondConflict);
        assertThat(output).contains("MixedGreeting 显式选择 -> FormalGreeting 默认: 您好，幸会");
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
