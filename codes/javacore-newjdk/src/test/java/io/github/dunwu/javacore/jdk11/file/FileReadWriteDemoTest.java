package io.github.dunwu.javacore.jdk11.file;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link FileReadWriteDemo} 单元测试。
 */
@DisplayName("Java 11 Files 读写增强示例测试")
public class FileReadWriteDemoTest {

    @Test
    @DisplayName("示例 1：writeString 一行写入、readString 一行读取")
    public void testWriteAndRead() {
        String output = captureOutput(FileReadWriteDemo::writeAndRead);
        assertThat(output)
            .contains("写入文件: ")
            .contains("读取内容:")
            .contains("你好，Java 11！")
            .contains("这是第二行内容。")
            .contains("临时文件已删除");
    }

    @Test
    @DisplayName("示例 2：APPEND 选项追加写入后行数增加")
    public void testAppendWrite() {
        String output = captureOutput(FileReadWriteDemo::appendWrite);
        assertThat(output)
            .contains("追加后行数: 3")
            .contains("临时文件已删除");
    }

    /**
     * 捕获被测代码的标准输出，测试结束后恢复原 System.out
     */
    private static String captureOutput(ThrowingRunnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            action.run();
        } catch (Exception e) {
            throw new AssertionError("被测代码抛出意外异常", e);
        } finally {
            System.setOut(original);
        }
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
    }

    /**
     * 允许抛出受检异常的 Runnable
     */
    @FunctionalInterface
    private interface ThrowingRunnable {

        void run() throws Exception;

    }

}
