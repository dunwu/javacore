package io.github.dunwu.javacore.container.sort;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * javacore-container sort 包示例的单元测试
 */
@DisplayName("排序示例测试")
public class SortDemoTest {

    @Test
    @DisplayName("Comparable 内部比较器：对象自身定义比较规则")
    public void testComparableDemo() {
        String output = captureOutput(ComparableDemo::demo);
        assertThat(output).isEqualTo(
            "User{age=17, name='B'}\n"
                + "User{age=18, name='A'}\n"
                + "User{age=20, name='C'}\n");
    }

    @Test
    @DisplayName("Comparator 外部比较器：比较规则由外部提供")
    public void testComparatorDemo() {
        String output = captureOutput(ComparatorDemo::demo);
        assertThat(output).isEqualTo(
            "User{age=17, name='B'}\n"
                + "User{age=18, name='A'}\n"
                + "User{age=20, name='C'}\n");
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
