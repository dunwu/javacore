package io.github.dunwu.javacore.bio.bytes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import io.github.dunwu.javacore.bio.bytes.ByteArrayStreamDemo;
import io.github.dunwu.javacore.bio.bytes.DataStreamDemo;
import io.github.dunwu.javacore.bio.bytes.FileStreamDemo;
import io.github.dunwu.javacore.bio.bytes.ObjectStreamDemo;
import io.github.dunwu.javacore.bio.bytes.PipedStreamDemo;
import io.github.dunwu.javacore.bio.bytes.PrintStreamDemo;
import io.github.dunwu.javacore.bio.bytes.SequenceInputStreamDemo;
import io.github.dunwu.javacore.bio.bytes.ZipStreamDemo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 字节流示例测试
 */
public class BytesStreamDemoTest {

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
    @DisplayName("字节数组流：内存中读写字节数据")
    void testByteArrayStreamDemo() {
        String output = captureOutput(ByteArrayStreamDemo::demo);
        assertThat(output).contains("helloworld");
    }

    @Test
    @DisplayName("数据流：按基本类型读写数据")
    void testDataStreamDemo() {
        String output = captureOutput(DataStreamDemo::demo);
        assertThat(output).contains("名称：衬衣；价格：98.30；数量：3").contains("结束");
    }

    @Test
    @DisplayName("文件字节流：读写文件内容")
    void testFileStreamDemo() {
        String output = captureOutput(FileStreamDemo::demo);
        assertThat(output).contains("读入数据的长度：12");
    }

    @Test
    @DisplayName("对象流：对象的序列化与反序列化")
    void testObjectStreamDemo() {
        String output = captureOutput(ObjectStreamDemo::demo);
        assertThat(output).contains("姓名：张三；年龄：30");
    }

    @Test
    @DisplayName("管道流：线程间通过管道传递数据")
    void testPipedStreamDemo() {
        String output = captureOutput(PipedStreamDemo::demo);
        assertThat(output).contains("接收的内容为：Hello World!!!");
    }

    @Test
    @DisplayName("打印流：将内容输出到文件")
    void testPrintStreamDemo() {
        File file = new File("temp_print.txt");
        if (file.exists()) {
            assertThat(file.delete()).isTrue();
        }
        captureOutput(PrintStreamDemo::demo);
        assertThat(file).exists();
        assertThat(file.delete()).isTrue();
    }

    @Test
    @DisplayName("合并流：将多个输入流顺序合并")
    void testSequenceInputStreamDemo() {
        String output = captureOutput(SequenceInputStreamDemo::demo);
        assertThat(output).contains("合并后的内容：Hello World!");
    }

    @Test
    @DisplayName("压缩流：ZIP 压缩与解压缩")
    void testZipStreamDemo() {
        String output = captureOutput(ZipStreamDemo::demo);
        assertThat(output).contains("压缩实体名称：demo.txt").contains("解压缩");
    }

}
