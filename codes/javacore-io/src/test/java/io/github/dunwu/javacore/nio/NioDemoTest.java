package io.github.dunwu.javacore.nio;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import io.github.dunwu.javacore.nio.buffer.ByteBufferDemo01;
import io.github.dunwu.javacore.nio.buffer.IntBufferDemo01;
import io.github.dunwu.javacore.nio.buffer.IntBufferDemo02;
import io.github.dunwu.javacore.nio.buffer.IntBufferDemo03;
import io.github.dunwu.javacore.nio.channel.FileChannelDemo01;
import io.github.dunwu.javacore.nio.channel.FileChannelDemo02;
import io.github.dunwu.javacore.nio.channel.FileChannelDemo03;
import io.github.dunwu.javacore.nio.charset.GetAllCharsetDemo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.ReadOnlyBufferException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * NIO 示例测试
 * <p>
 * 注：{@link io.github.dunwu.javacore.nio.charset.CharsetEnDeDemo} 运行即抛编码异常（反例），
 * {@link io.github.dunwu.javacore.nio.lock.FileLockDemo} 包含数秒等待，
 * {@link io.github.dunwu.javacore.nio.selector.DateServer} 为长驻服务，均不纳入自动化测试。
 */
public class NioDemoTest {

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
    @DisplayName("ByteBuffer：字节缓冲区存取数据")
    void testByteBufferDemo01() {
        String output = captureOutput(ByteBufferDemo01::demo);
        assertThat(output).isEqualTo("主缓冲区中的内容：1、3、5、7、9、\n");
    }

    @Test
    @DisplayName("IntBuffer：position/limit/capacity 的变化与 flip 操作")
    void testIntBufferDemo01() {
        String output = captureOutput(IntBufferDemo01::demo);
        assertThat(output)
            .contains("position = 0，limit = 10，capacty = 10")
            .contains("position = 4，limit = 10，capacty = 10")
            .contains("position = 0，limit = 4，capacty = 10")
            .contains("缓冲区中的内容：3、5、7、9、");
    }

    @Test
    @DisplayName("IntBuffer：缓冲区的 put/get 批量操作")
    void testIntBufferDemo02() {
        String output = captureOutput(IntBufferDemo02::demo);
        assertThat(output).isEqualTo("主缓冲区中的内容：1、3、4、6、8、10、13、15、17、19、\n");
    }

    @Test
    @DisplayName("IntBuffer（反例）：向只读缓冲区写入抛 ReadOnlyBufferException")
    void testIntBufferDemo03() {
        assertThatThrownBy(IntBufferDemo03::demo).isInstanceOf(ReadOnlyBufferException.class);
    }

    @Test
    @DisplayName("FileChannel：通过通道写入文件")
    void testFileChannelDemo01() {
        String output = captureOutput(() -> FileChannelDemo01.demo());
        assertThat(output).contains("内容已通过通道写入文件：");
    }

    @Test
    @DisplayName("FileChannel：通过通道复制文件")
    void testFileChannelDemo02() {
        String output = captureOutput(() -> FileChannelDemo02.demo());
        assertThat(output).contains("文件已复制为：");
    }

    @Test
    @DisplayName("FileChannel：通过通道读取文件内容")
    void testFileChannelDemo03() {
        String output = captureOutput(() -> FileChannelDemo03.demo());
        assertThat(output).contains("大风起兮云飞扬，");
    }

    @Test
    @DisplayName("Charset：获取 JVM 支持的所有字符集")
    void testGetAllCharsetDemo() {
        String output = captureOutput(GetAllCharsetDemo::demo);
        assertThat(output).contains("UTF-8");
    }

}
