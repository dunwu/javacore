package io.github.dunwu.javacore.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * io 包示例测试
 * <p>
 * 注：{@link ScannerDemo}、{@link SystemInDemo} 需要交互式键盘输入，不纳入自动化测试。
 * {@link FileDemo} 由 {@code io.github.dunwu.javacore.bio.FileDemoTest} 测试。
 */
public class IODemoTest {

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
    @DisplayName("Properties：读取属性与默认值")
    void testPropertiesDemo01() {
        String output = captureOutput(PropertiesDemo01::demo);
        assertThat(output)
            .contains("1、BJ属性存在：BeiJing")
            .contains("3、SC属性不存在，同时设置显示的默认值：没有发现");
    }

    @Test
    @DisplayName("Properties：将属性保存到文件")
    void testPropertiesDemo02() {
        String output = captureOutput(PropertiesDemo02::demo);
        assertThat(output).contains("属性已保存到文件：");
        assertThat(new File(PropertiesDemo02.FILE_PATH)).exists();
    }

    @Test
    @DisplayName("Properties：从文件加载属性")
    void testPropertiesDemo03() {
        String output = captureOutput(PropertiesDemo03::demo);
        assertThat(output).contains("1、BJ属性存在：BeiJing");
    }

    @Test
    @DisplayName("Properties：将属性保存到 XML 文件")
    void testPropertiesDemo04() {
        String output = captureOutput(PropertiesDemo04::demo);
        assertThat(output).contains("属性已保存到XML文件：");
        assertThat(new File(PropertiesDemo04.FILE_PATH)).exists();
    }

    @Test
    @DisplayName("Properties：从 XML 文件加载属性")
    void testPropertiesDemo05() {
        String output = captureOutput(PropertiesDemo05::demo);
        assertThat(output).contains("1、BJ属性存在：BeiJing");
    }

    @Test
    @DisplayName("RandomAccessFile：随机访问写入多条记录")
    void testRandomAccessFileWriteDemo() {
        String output = captureOutput(() -> RandomAccessFileWriteDemo.demo());
        assertThat(output).contains("已写入三组记录到文件：");
        assertThat(new File(RandomAccessFileWriteDemo.FILE_PATH)).exists();
    }

    @Test
    @DisplayName("RandomAccessFile：随机定位读取指定记录")
    void testRandomAccessFileReadDemo() {
        String output = captureOutput(() -> RandomAccessFileReadDemo.demo());
        assertThat(output)
            .contains("第二个人的信息 --> 姓名：lisi")
            .contains("；年龄：31")
            .contains("；年龄：30")
            .contains("；年龄：32");
    }

    @Test
    @DisplayName("System.err：标准错误输出")
    void testSystemErrDemo() {
        String output = captureOutput(() -> SystemErrDemo.demo());
        assertThat(output).contains("此处有误");
    }

    @Test
    @DisplayName("System.out：标准输出重定向到文件")
    void testSystemOutDemo() {
        String output = captureOutput(() -> SystemOutDemo.demo());
        // 路径必须取自示例自身的常量：示例已统一把临时文件写到 target/ 下
        assertThat(output).contains("输出已重定向到文件：" + SystemOutDemo.FILE_PATH);
        assertThat(new File(SystemOutDemo.FILE_PATH)).exists();
    }

}
