package io.github.dunwu.javacore.jdk8.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link MapNewApiDemo} 单元测试。
 */
@DisplayName("Java 8 Map 接口新增方法示例测试")
public class MapNewApiDemoTest {

    @Test
    @DisplayName("示例 1：getOrDefault 默认值与 putIfAbsent 不覆盖已有 key")
    public void testGetOrDefaultAndPutIfAbsent() {
        String output = captureOutput(MapNewApiDemo::getOrDefaultAndPutIfAbsent);
        assertThat(output)
            .contains("getOrDefault: 0")
            .contains("putIfAbsent 后: {张三=80, 李四=90, 王五=60}");
    }

    @Test
    @DisplayName("示例 2：computeIfAbsent 计算放入与 compute 按旧值计算")
    public void testComputeMethods() {
        String output = captureOutput(MapNewApiDemo::computeMethods);
        assertThat(output)
            .contains("computeIfAbsent 后: {张三=80, 李四=90, 王五=60, 赵六=66}")
            .contains("compute 张三加分后: 85");
    }

    @Test
    @DisplayName("示例 3：merge 词频统计，合并函数返回 null 时删除 key")
    public void testMergeWordCount() {
        String output = captureOutput(MapNewApiDemo::mergeWordCount);
        assertThat(output)
            .contains("merge 词频统计: {java=3, go=2}")
            .contains("merge 返回 null 删除 go: {java=3}");
    }

    @Test
    @DisplayName("示例 4：replaceAll 批量修改与 remove(key, value) 条件删除")
    public void testReplaceAllAndRemove() {
        String output = captureOutput(MapNewApiDemo::replaceAllAndRemove);
        assertThat(output)
            .contains("replaceAll 全班加 10 分: {张三=95, 李四=100, 王五=70, 赵六=76}")
            .contains("remove 匹配删除: true, 删除后还有王五吗: false");
    }

    @Test
    @DisplayName("示例 5：forEach 配合 lambda 遍历 Map")
    public void testForEachTraversal() {
        String output = captureOutput(MapNewApiDemo::forEachTraversal);
        assertThat(output)
            .contains("张三 -> 95")
            .contains("李四 -> 100")
            .contains("赵六 -> 76");
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

