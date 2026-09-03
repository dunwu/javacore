package io.github.dunwu.javacore.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * annotation 包内置注解示例单元测试
 * <p>
 * 覆盖 {@code @Deprecated}、{@code @Override}、{@code @FunctionalInterface}、
 * {@code @SafeVarargs}、{@code @SuppressWarnings} 五个内置注解
 */
@DisplayName("内置注解示例测试")
public class AnnotationDemoTest {

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
    @DisplayName("@Deprecated：可标记字段、方法、类三种目标，且不影响运行期行为")
    void testDeprecatedAnnotationDemo() {
        String output = captureOutput(DeprecatedAnnotationDemo::demo);
        assertThat(output).isEqualTo("DeprecatedField\n"
            + "DeprecatedMethod\n"
            + "DeprecatedClass\n");
    }

    @Test
    @DisplayName("@FunctionalInterface：函数式接口既可用匿名内部类也可用方法引用实现")
    void testFunctionalInterfaceAnnotationDemo() {
        String output = captureOutput(FunctionalInterfaceAnnotationDemo::demo);
        // 前两个来自匿名内部类实现，第三个来自方法引用 System.out::println
        assertThat(output).isEqualTo("Hello\n"
            + "100\n"
            + "World\n");
    }

    @Test
    @DisplayName("@Override：父类引用调用被子类重写的方法，体现动态绑定")
    void testOverrideAnnotationDemo() {
        String output = captureOutput(OverrideAnnotationDemo::demo);
        assertThat(output).isEqualTo("override getName\n");
    }

    @Test
    @DisplayName("内置注解组合：子类重写为空方法体后，父类引用调用它无任何输出")
    void testInternalAnnotationDemo() {
        String output = captureOutput(InternalAnnotationDemo::demo);
        // B 重写的 method1() 方法体为空，故只输出未被重写的 method2()
        assertThat(output).isEqualTo("call method2\n");
    }

    @Test
    @DisplayName("@SafeVarargs：仅压制堆污染告警，运行期仍抛 ClassCastException")
    void testSafeVarargsAnnotationDemo() {
        String output = captureOutput(SafeVarargsAnnotationDemo::demo);
        assertThat(output).isEqualTo("捕获到 ClassCastException：@SafeVarargs 只压制告警，无法阻止堆污染导致的运行期异常\n");
    }

    @Test
    @DisplayName("@SuppressWarnings：只压制编译告警，不改变运行期行为")
    void testSuppressWarningsAnnotationDemo() {
        String output = captureOutput(SuppressWarningsAnnotationDemo::demo);
        assertThat(output).isEqualTo("地名：南京\n");
    }

}
