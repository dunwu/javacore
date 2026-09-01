package io.github.dunwu.javacore.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * util 根包示例测试：Arrays、Runtime、System。
 */
@DisplayName("util 根包示例测试")
public class UtilDemoTest {

    @Test
    @DisplayName("ArraysDemo：排序、二分查找、填充")
    void testArraysDemo() {
        String output = captureOutput(ArraysDemo::demo);
        assertThat(output).contains("排序后的数组：[1, 2, 3, 4, 5, 6, 7, 8, 9]");
        assertThat(output).contains("元素‘3’的位置在：2");
        assertThat(output).contains("数组填充：[3, 3, 3, 3, 3, 3, 3, 3, 3]");
    }

    @Test
    @DisplayName("RuntimeDemo01：观察 JVM 内存与 gc()")
    void testRuntimeDemo01() {
        String output = captureOutput(RuntimeDemo01::demo);
        // 内存数值因环境而异，只断言标签与固定内容
        assertThat(output).contains("JVM最大内存量：");
        assertThat(output).contains("JVM空闲内存量：");
        assertThat(output).contains("Hello World!!!");
        assertThat(output).contains("操作String之后的,JVM空闲内存量：");
        assertThat(output).contains("垃圾回收之后的,JVM空闲内存量：");
    }

    @Test
    @DisplayName("Runtime：单例可获取（不实际调用弹窗示例）")
    void testRuntimeSingleton() {
        assertThat(Runtime.getRuntime()).isNotNull();
        assertThat(Runtime.getRuntime().availableProcessors()).isGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("SystemDemo01：统计代码执行耗时")
    void testSystemDemo01() {
        String output = captureOutput(SystemDemo01::demo);
        assertThat(output).contains("计算所花费的时间：");
        assertThat(output).contains("毫秒");
    }

    @Test
    @DisplayName("SystemDemo02：列出全部系统属性")
    void testSystemDemo02() {
        String output = captureOutput(SystemDemo02::demo);
        assertThat(output).contains("java.version");
        assertThat(output).contains("os.name");
    }

    @Test
    @DisplayName("SystemDemo03：读取常用系统属性")
    void testSystemDemo03() {
        String output = captureOutput(SystemDemo03::demo);
        assertThat(output).contains("系统版本：");
        assertThat(output).contains("系统用户：");
        assertThat(output).contains("当前用户目录：");
        assertThat(output).contains("当前用户工作目录：");
    }

    @Test
    @DisplayName("SystemDemo04：断开引用并请求垃圾回收")
    void testSystemDemo04() {
        String output = captureOutput(SystemDemo04::demo);
        // finalize() 输出时机不保证，只断言 toString() 的输出
        assertThat(output).contains("对象信息：姓名：张三，年龄：30");
    }

    /**
     * 捕获 System.out 输出。
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
