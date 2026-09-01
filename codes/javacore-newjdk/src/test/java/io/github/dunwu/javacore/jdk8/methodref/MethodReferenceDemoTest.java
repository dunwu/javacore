package io.github.dunwu.javacore.jdk8.methodref;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link MethodReferenceDemo} 单元测试。
 */
@DisplayName("Java 8 方法引用示例测试")
public class MethodReferenceDemoTest {

    @Test
    @DisplayName("示例 1：静态方法引用与绑定对象的实例方法引用")
    public void testStaticAndBoundReference() {
        String output = captureOutput(MethodReferenceDemo::staticAndBoundReference);
        assertThat(output)
            .contains("静态方法引用: 123")
            .contains("绑定对象的实例方法引用");
    }

    @Test
    @DisplayName("示例 2：未绑定的实例方法引用（类名::实例方法）")
    public void testUnboundReference() {
        String output = captureOutput(MethodReferenceDemo::unboundReference);
        assertThat(output).contains("类::实例方法: 16");
    }

    @Test
    @DisplayName("示例 3：构造器引用与数组引用创建对象")
    public void testConstructorReference() {
        String output = captureOutput(MethodReferenceDemo::constructorReference);
        assertThat(output).contains("构造器引用: list.size=1, array.length=5");
    }

    @Test
    @DisplayName("示例 4：方法引用与 lambda 写法结果等价")
    public void testLambdaEquivalence() {
        String output = captureOutput(MethodReferenceDemo::lambdaEquivalence);
        assertThat(output).contains("两种写法结果一致: true");
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
