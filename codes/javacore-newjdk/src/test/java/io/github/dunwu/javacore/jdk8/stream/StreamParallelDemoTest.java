package io.github.dunwu.javacore.jdk8.stream;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link StreamParallelDemo} 单元测试。
 */
@DisplayName("Java 8 并行流示例测试")
public class StreamParallelDemoTest {

    @Test
    @DisplayName("示例 1：串行求和与并行求和结果一致")
    public void testSerialVsParallelSum() {
        String output = captureOutput(StreamParallelDemo::serialVsParallelSum);
        assertThat(output).contains("串行求和 == 并行求和: true");
    }

    @Test
    @DisplayName("示例 2：parallel/sequential 最后一次切换调用生效")
    public void testParallelSwitch() {
        String output = captureOutput(StreamParallelDemo::parallelSwitch);
        assertThat(output).contains("parallel().sequential() 后是否并行: false");
    }

    @Test
    @DisplayName("示例 3：forEachOrdered 保证并行流遭遇顺序")
    public void testOrderedTraversal() {
        String output = captureOutput(StreamParallelDemo::orderedTraversal);
        assertThat(output).contains("forEachOrdered 保持顺序: [1, 2, 3, 4, 5]");
    }

    @Test
    @DisplayName("示例 4：大数组平方和串行并行结果一致（耗时因机器而异，不做断言）")
    public void testParallelBenefit() {
        String output = captureOutput(StreamParallelDemo::parallelBenefit);
        assertThat(output)
            .contains("结果一致: true")
            .contains("串行耗时: ")
            .contains("并行耗时: ");
    }

    /**
     * 捕获被测代码的标准输出，测试结束后恢复原 System.out
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
