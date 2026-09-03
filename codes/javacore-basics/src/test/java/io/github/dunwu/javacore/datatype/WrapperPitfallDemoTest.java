package io.github.dunwu.javacore.datatype;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * {@link WrapperPitfallDemo} 单元测试
 * <p>
 * 缓存范围与拆箱行为都是确定的语言事实，因此对输出做精确断言；只有累加耗时因机器而异，不断言。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class WrapperPitfallDemoTest {

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

    @Test
    @DisplayName("cacheRange：各包装类缓存范围不同，Float/Double 完全不缓存")
    void testCacheRange() {
        String output = captureOutput(WrapperPitfallDemo::cacheRange);
        assertThat(output).isEqualTo("Byte      127 == 127 : true（Byte 全部取值都在缓存内）\n"
            + "Short     127 == 127 : true | 128 == 128 : false\n"
            + "Integer   127 == 127 : true | 128 == 128 : false\n"
            + "Long      127 == 127 : true | 128 == 128 : false\n"
            + "Character 127 == 127 : true | 128 == 128 : false\n"
            + "Boolean   true == true : true\n"
            + "Float     1.0 == 1.0 : false\n"
            + "Double    1.0 == 1.0 : false\n");
    }

    @Test
    @DisplayName("unboxingNpe：null 包装类赋值、运算、Map.get 三种场景都会拆箱抛 NPE")
    void testUnboxingNpe() {
        String output = captureOutput(WrapperPitfallDemo::unboxingNpe);
        assertThat(output).isEqualTo("场景一 int value = nullInteger 抛出 NullPointerException\n"
            + "场景二 nullInteger + 1 抛出 NullPointerException\n"
            + "场景三 scores.get(\"math\") 返回 null，拆箱时抛出 NullPointerException\n");
    }

    @Test
    @DisplayName("ternaryNpe：三目运算符选中 null 分支时拆箱抛 NPE")
    void testTernaryNpe() {
        String output = captureOutput(WrapperPitfallDemo::ternaryNpe);
        assertThat(output).isEqualTo("场景一 三目运算符选中 null 分支时拆箱抛出 NullPointerException\n"
            + "场景二 true ? Integer : Long 的结果类型是 Long，值为 1\n"
            + "场景三 显式判空后取值: -1\n");
    }

    @Test
    @DisplayName("sumByBoxing / sumByPrimitive：两种累加器写法结果完全一致")
    void testSumEquivalence() {
        assertThat(WrapperPitfallDemo.sumByBoxing(3)).isEqualTo(3L);
        assertThat(WrapperPitfallDemo.sumByPrimitive(3)).isEqualTo(3L);
        // 0..99999 的和为 99999 * 100000 / 2
        assertThat(WrapperPitfallDemo.sumByBoxing(100_000)).isEqualTo(4_999_950_000L);
        assertThat(WrapperPitfallDemo.sumByPrimitive(100_000)).isEqualTo(WrapperPitfallDemo.sumByBoxing(100_000));
    }

    @Test
    @DisplayName("loopBoxingPerformance：输出累加结果与两种写法的耗时对比")
    void testLoopBoxingPerformance() {
        String output = captureOutput(WrapperPitfallDemo::loopBoxingPerformance);
        assertThat(output).contains("累加 0..99999 的结果: 4999950000");
        assertThat(output).contains("两种写法结果一致: true");
        assertThat(output).contains("包装类型 Long 累加耗时: ");
        assertThat(output).contains("基本类型 long 累加耗时: ");
    }

    @Test
    @DisplayName("demo：完整演示可正常执行（三处 NPE 均被示例内部捕获）")
    void testDemo() {
        // 用 captureOutput 包裹，避免示例的输出直接打到构建日志里
        assertThatCode(() -> captureOutput(WrapperPitfallDemo::demo)).doesNotThrowAnyException();
    }

}
