package io.github.dunwu.javacore.jdk9.iface;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link InterfacePrivateMethodDemo} 单元测试。
 */
@DisplayName("Java 9 接口私有方法示例测试")
public class InterfacePrivateMethodDemoTest {

    @Test
    @DisplayName("示例 1：默认方法复用私有实例方法 log")
    public void testPrivateInstanceMethodDemo() {
        String output = captureOutput(InterfacePrivateMethodDemo::privateInstanceMethodDemo);
        assertThat(output)
            .contains("[LOG] [INFO] >>> 服务启动成功 <<<")
            .contains("[LOG] [ERROR] >>> 连接超时 <<<");
    }

    @Test
    @DisplayName("示例 2：静态方法复用私有静态方法 decorate")
    public void testPrivateStaticMethodDemo() {
        String output = captureOutput(InterfacePrivateMethodDemo::privateStaticMethodDemo);
        assertThat(output).contains(">>> 日志系统已初始化 <<<");
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
