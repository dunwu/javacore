package io.github.dunwu.javacore.bio.chars;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 字符流示例测试
 * <p>
 * 注：{@link BufferedReaderDemo} 需要交互式键盘输入，不纳入自动化测试。
 */
public class CharsStreamDemoTest {

    @FunctionalInterface
    interface ThrowingRunnable {

        void run() throws Exception;

    }

    private static String captureOutput(ThrowingRunnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            action.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.setOut(original);
        }
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    @Test
    @DisplayName("FileReader/FileWriter：字符流读写文件")
    void testFileReadWriteDemo() {
        String output = captureOutput(FileReadWriteDemo::demo);
        assertThat(output).contains("文件字符数为：16");
    }

    @Test
    @DisplayName("InputStreamReader：字节流转换为字符流读取")
    void testInputStreamReaderDemo() {
        String output = captureOutput(InputStreamReaderDemo::demo);
        assertThat(output).contains("hello world!!");
    }

    @Test
    @DisplayName("OutputStreamWriter：字符流转换为字节流写入")
    void testOutputStreamWriterDemo() {
        String output = captureOutput(OutputStreamWriterDemo::demo);
        assertThat(output).contains("hello world!!");
    }

}
