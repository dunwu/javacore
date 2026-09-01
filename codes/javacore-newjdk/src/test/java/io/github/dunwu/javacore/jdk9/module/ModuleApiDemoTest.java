package io.github.dunwu.javacore.jdk9.module;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link ModuleApiDemo} 单元测试。
 */
@DisplayName("Java 9 模块系统 Module API 示例测试")
public class ModuleApiDemoTest {

    @Test
    @DisplayName("示例 1：String 属于具名模块 java.base 且可列出导出的包")
    public void testBaseModuleInfo() {
        String output = captureOutput(ModuleApiDemo::baseModuleInfo);
        assertThat(output)
            .contains("String 所属模块: java.base")
            .contains("String 模块是否为具名模块: true")
            .contains("String 模块导出的包（前 3 个）:");
    }

    @Test
    @DisplayName("示例 2：类路径代码位于未命名模块")
    public void testUnnamedModuleInfo() {
        String output = captureOutput(ModuleApiDemo::unnamedModuleInfo);
        assertThat(output)
            .contains("本示例类所属模块: unnamed module")
            .contains("本示例类模块是否为具名模块: false");
    }

    @Test
    @DisplayName("示例 3：Boot Layer 中可列出部分 java.* 模块")
    public void testBootLayerModules() {
        String output = captureOutput(ModuleApiDemo::bootLayerModules);
        assertThat(output)
            .contains("Boot Layer 中的部分 JDK 模块：")
            .contains("java.base");
    }

    @Test
    @DisplayName("示例 4：Runtime.version 可读取当前 Java 版本")
    public void testRuntimeVersion() {
        String output = captureOutput(ModuleApiDemo::runtimeVersion);
        assertThat(output).contains("java.base 版本: ");
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
