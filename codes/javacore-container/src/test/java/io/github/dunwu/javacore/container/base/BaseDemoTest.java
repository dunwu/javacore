package io.github.dunwu.javacore.container.base;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * javacore-container base 包示例的单元测试。
 * <p>注：FailFastDemo 为多线程并发触发 ConcurrentModificationException 的反例，不在测试中执行。
 */
@DisplayName("容器遍历基础示例测试")
public class BaseDemoTest {

    @Test
    @DisplayName("foreach 遍历 List")
    public void testForeachDemo01() {
        String output = captureOutput(ForeachDemo01::demo);
        assertThat(output).isEqualTo("hello、_、world、");
    }

    @Test
    @DisplayName("foreach 遍历 Map 的 entrySet")
    public void testForeachDemo02() {
        String output = captureOutput(ForeachDemo02::demo);
        assertThat(output).contains("mldn --> www.mldn.cn");
        assertThat(output).contains("zhinangtuan --> www.zhinangtuan.net.cn");
        assertThat(output).contains("mldnjava --> www.mldnjava.cn");
    }

    @Test
    @DisplayName("迭代器遍历 List")
    public void testIteratorDemo() {
        String output = captureOutput(IteratorDemo::demo);
        assertThat(output).isEqualTo("1\n2\n3\n");
    }

    @Test
    @DisplayName("迭代中用 iterator.remove() 安全删除元素")
    public void testIteratorDemo2() {
        String output = captureOutput(IteratorDemo2::demo);
        assertThat(output).contains("执行前：[1, 2, 3]");
        assertThat(output).contains("执行后：[1, 3]");
        assertThat(output).contains("1");
        assertThat(output).contains("3");
    }

    @Test
    @DisplayName("迭代器遍历 Map 的 entrySet")
    public void testIteratorDemo3() {
        String output = captureOutput(IteratorDemo3::demo);
        assertThat(output).contains("1 -> A");
        assertThat(output).contains("2 -> B");
        assertThat(output).contains("3 -> C");
    }

    private static String captureOutput(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            action.run();
        } finally {
            System.setOut(original);
        }
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

}
