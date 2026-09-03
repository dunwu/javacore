package io.github.dunwu.javacore.concurrent;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 示例输出捕获工具
 * <p>
 * concurrent 模块的示例都以「打印到控制台」的方式呈现效果，没有返回值可供断言。为了让这些示例可被单元测试覆盖，
 * 这里把 {@code System.out} 临时替换为内存流，执行完再还原，从而把示例的输出变成可断言的字符串。
 * <p>
 * <b>前提</b>：被测示例的 {@code demo()} 必须在返回前等待自己创建的所有线程结束（{@code join} 或
 * {@code awaitTermination}），否则子线程的输出会落到还原之后的 {@code System.out} 上，导致捕获结果不完整、测试不稳定。
 * <p>
 * <b>注意</b>：JUnit 默认在同一个 JVM 内串行执行测试方法，且 {@code System.out} 是全局的，
 * 因此不要让被测示例残留任何在 {@code demo()} 返回后仍在运行的线程。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public final class DemoOutputCapture {

    private DemoOutputCapture() {}

    /**
     * 允许抛出受检异常的无参代码块，用于包装示例的 {@code demo()} 调用
     */
    @FunctionalInterface
    public interface ThrowingRunnable {

        void run() throws Exception;
    }

    /**
     * 执行 {@code runnable} 并捕获它打印到 {@code System.out} 的全部内容
     *
     * @return 捕获到的输出，换行符统一为 {@code \n}
     */
    public static String capture(ThrowingRunnable runnable) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            runnable.run();
        } catch (Exception e) {
            throw new IllegalStateException("示例执行失败：" + e.getMessage(), e);
        } finally {
            System.setOut(original);
        }
        return buffer.toString(StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    /**
     * 按行切分，保留空行，但去掉末尾换行符产生的最后一个空元素。
     * 不对行内容做 trim，因此可以放心断言尾随空格这类细节
     */
    public static String[] lines(String output) {
        String normalized = output.endsWith("\n") ? output.substring(0, output.length() - 1) : output;
        if (normalized.isEmpty()) {
            return new String[0];
        }
        return normalized.split("\n", -1);
    }

    /**
     * 按行切分并丢弃空白行。适用于示例用 {@code println("\n" + xxx)} 主动插入空行的情况
     */
    public static String[] nonBlankLines(String output) {
        return Arrays.stream(output.split("\n", -1))
                     .filter(line -> !line.trim().isEmpty())
                     .toArray(String[]::new);
    }

}
