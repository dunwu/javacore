package io.github.dunwu.javacore.datatype;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@link StringBuilderDemo} 单元测试
 * <p>
 * 注：拼接耗时与 JVM 预热、机器负载相关，属于非确定性输出，因此只对内容与结论做断言。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class StringBuilderDemoTest {

    @FunctionalInterface
    private interface ThrowingRunnable {

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

    /**
     * 在重定向标准输出的前提下执行动作，并返回动作的结果。
     * <p>
     * 用于那些「既有返回值、内部又会打印耗时」的方法：直接调用会让耗时行泄漏到构建日志里。
     */
    private static <T> T captureOutputAndGet(Supplier<T> action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            return action.get();
        } finally {
            System.setOut(original);
        }
    }

    @Test
    @DisplayName("commonApi：append/insert/delete/replace/reverse/setLength 逐步改变内容")
    void testCommonApi() {
        String output = captureOutput(StringBuilderDemo::commonApi);
        assertThat(output).isEqualTo("原始内容: Java\n"
            + "append(\"Core\") 后: JavaCore\n"
            + "insert(4, '-') 后: Java-Core\n"
            + "delete(4, 5) 后: JavaCore\n"
            + "replace(0, 4, \"JAVA\") 后: JAVACore\n"
            + "reverse() 后: eroCAVAJ\n"
            + "setLength(4) 后: eroC\n"
            + "new StringBuilder() 的初始容量: 16\n");
    }

    @Test
    @DisplayName("concatCompare：StringBuilder 与 String += 的拼接结果完全一致")
    void testConcatCompare() {
        String output = captureOutput(StringBuilderDemo::concatCompare);
        assertThat(output).contains("两种写法拼接结果是否一致: true");
        assertThat(output).contains("拼接结果长度: 6890");
        assertThat(output).contains("拼接结果前 20 个字符: [No.0\tNo.1\tNo.2\tNo.3\t]");
    }

    @Test
    @DisplayName("concatByStringBuilder：高效写法拼出预期内容")
    void testConcatByStringBuilder() {
        assertThat(captureOutputAndGet(() -> StringBuilderDemo.concatByStringBuilder(3)))
            .isEqualTo("No.0\tNo.1\tNo.2\t");
    }

    @Test
    @DisplayName("concatByPlus：低效写法结果与 StringBuilder 一致，仅性能有别")
    void testConcatByPlus() {
        String byPlus = captureOutputAndGet(() -> StringBuilderDemo.concatByPlus(3));
        String byBuilder = captureOutputAndGet(() -> StringBuilderDemo.concatByStringBuilder(3));
        assertThat(byPlus).isEqualTo(byBuilder);
    }

    @Test
    @DisplayName("demo：完整演示可正常执行并输出关键结论")
    void testDemo() {
        String output = captureOutput(StringBuilderDemo::demo);
        assertThat(output).contains("原始内容: Java");
        assertThat(output).contains("两种写法拼接结果是否一致: true");
    }

}
