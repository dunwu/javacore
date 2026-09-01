package io.github.dunwu.javacore.util.string;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * string 包示例测试：StringBuffer 的 append、insert、reverse、replace、substring、delete、indexOf 与拼接性能。
 */
@DisplayName("StringBuffer 示例测试")
public class StringBufferDemoTest {

    @Test
    @DisplayName("StringBufferDemo01：append() 追加各种类型")
    void testStringBufferDemo01() {
        String output = captureOutput(StringBufferDemo01::demo);
        assertThat(output).contains("Hello World!!!");
        assertThat(output).contains("数字 = 1");
        assertThat(output).contains("字符 = C");
        assertThat(output).contains("布尔 = true");
    }

    @Test
    @DisplayName("StringBufferDemo02：引用传递，方法内修改影响原对象")
    void testStringBufferDemo02() {
        String output = captureOutput(StringBufferDemo02::demo);
        assertThat(output).contains("Hello JAVA Zhang Peng");
    }

    @Test
    @DisplayName("StringBufferDemo03：insert() 在开头和末尾插入")
    void testStringBufferDemo03() {
        String output = captureOutput(StringBufferDemo03::demo);
        assertThat(output).contains("Hello World!!");
        assertThat(output).contains("Hello World!!JAVA~");
    }

    @Test
    @DisplayName("StringBufferDemo04：reverse() 内容反转")
    void testStringBufferDemo04() {
        String output = captureOutput(StringBufferDemo04::demo);
        assertThat(output).contains("!!dlroW olleH");
    }

    @Test
    @DisplayName("StringBufferDemo05：replace() 替换指定范围")
    void testStringBufferDemo05() {
        String output = captureOutput(StringBufferDemo05::demo);
        assertThat(output).contains("内容替换之后的结果：Hello Zhang Peng!!");
    }

    @Test
    @DisplayName("StringBufferDemo06：substring() 截取指定范围")
    void testStringBufferDemo06() {
        String output = captureOutput(StringBufferDemo06::demo);
        assertThat(output).contains("内容替换之后的结果：Zhang Pen");
    }

    @Test
    @DisplayName("StringBufferDemo07：delete() 删除指定范围")
    void testStringBufferDemo07() {
        String output = captureOutput(StringBufferDemo07::demo);
        assertThat(output).contains("删除之后的结果：Hello g!!");
    }

    @Test
    @DisplayName("StringBufferDemo08：indexOf() 查找内容")
    void testStringBufferDemo08() {
        String output = captureOutput(StringBufferDemo08::demo);
        assertThat(output).contains("可以查找到指定的内容");
    }

    @Test
    @DisplayName("StringBufferDemo09 与 10：String 拼接与 StringBuffer 拼接结果一致")
    void testStringBufferDemo09And10() {
        String output09 = captureOutput(StringBufferDemo09::demo).trim();
        String output10 = captureOutput(StringBufferDemo10::demo).trim();
        // 两种写法最终拼接结果相同，但 String 写法每次 += 都产生新对象
        assertThat(output09).isEqualTo(output10);
        assertThat(output10).startsWith("Zhang Peng");
        assertThat(output10).endsWith("99");
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
