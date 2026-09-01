package io.github.dunwu.javacore.generics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * 泛型示例测试：验证各泛型示例的输出。
 * 反例（运行即抛异常）不在此测试：NoGenericsDemo。
 */
@DisplayName("泛型示例测试")
public class GenericsDemoTest {

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
    @DisplayName("GenericArrayDemo：泛型方法打印数组")
    public void testGenericArrayDemo() {
        String output = captureOutput(GenericArrayDemo::demo);
        assertThat(output).isEqualTo("1\t2\t3\t4\t5\t\nH\tE\tL\tL\tO\t\n");
    }

    @Test
    @DisplayName("GenericsClassDemo01：泛型类")
    public void testGenericsClassDemo01() {
        String output = captureOutput(GenericsClassDemo01::demo);
        assertThat(output).isEqualTo("10\nxyz\n");
    }

    @Test
    @DisplayName("GenericsClassDemo02：多参数泛型类")
    public void testGenericsClassDemo02() {
        String output = captureOutput(GenericsClassDemo02::demo);
        assertThat(output).isEqualTo("MyMap{key=1, value=one}\n");
    }

    @Test
    @DisplayName("GenericsClassDemo03：泛型嵌套")
    public void testGenericsClassDemo03() {
        String output = captureOutput(GenericsClassDemo03::demo);
        assertThat(output).isEqualTo("MyMap{key=1, value=Info{value=Hello}}\n");
    }

    @Test
    @DisplayName("GenericsClassDemo04/ErasureTypeDemo02/WildcardDemo：编译期约束示例正常运行")
    public void testCompileConstraintDemos() {
        assertThatCode(GenericsClassDemo04::demo).doesNotThrowAnyException();
        assertThatCode(GenericsErasureTypeDemo02::demo).doesNotThrowAnyException();
        assertThatCode(GenericsWildcardDemo::demo).doesNotThrowAnyException();
        assertThatCode(GenericsSuperDemo01::demo).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("GenericsErasureTypeDemo：类型擦除")
    public void testGenericsErasureTypeDemo() {
        String output = captureOutput(GenericsErasureTypeDemo::demo);
        assertThat(output).isEqualTo("class java.util.ArrayList\nclass java.util.ArrayList\n");
    }

    @Test
    @DisplayName("GenericsExtendsDemo01：泛型上界")
    public void testGenericsExtendsDemo01() {
        String output = captureOutput(GenericsExtendsDemo01::demo);
        assertThat(output).isEqualTo("5\n8.8\npear\n");
    }

    @Test
    @DisplayName("GenericsExtendsDemo02：多类型上界")
    public void testGenericsExtendsDemo02() {
        String output = captureOutput(GenericsExtendsDemo02::demo);
        assertThat(output).contains("D1");
    }

    @Test
    @DisplayName("GenericsInterfaceDemo01：实现泛型接口指定类型")
    public void testGenericsInterfaceDemo01() {
        String output = captureOutput(GenericsInterfaceDemo01::demo);
        assertThat(output).isEqualTo("10");
    }

    @Test
    @DisplayName("GenericsInterfaceDemo02：实现泛型接口保留类型参数")
    public void testGenericsInterfaceDemo02() {
        String output = captureOutput(GenericsInterfaceDemo02::demo);
        assertThat(output).isEqualTo("ABC");
    }

    @Test
    @DisplayName("GenericsLowerBoundedWildcardDemo：下限通配符")
    public void testGenericsLowerBoundedWildcardDemo() {
        String output = captureOutput(GenericsLowerBoundedWildcardDemo::demo);
        assertThat(output).isEqualTo("[1, 2, 3, 4, 5]\n");
    }

    @Test
    @DisplayName("GenericsUpperBoundedWildcardDemo：上限通配符")
    public void testGenericsUpperBoundedWildcardDemo() {
        String output = captureOutput(GenericsUpperBoundedWildcardDemo::demo);
        assertThat(output).isEqualTo("sum = 6.0\n");
    }

    @Test
    @DisplayName("GenericsUnboundedWildcardDemo：无边界通配符")
    public void testGenericsUnboundedWildcardDemo() {
        String output = captureOutput(GenericsUnboundedWildcardDemo::demo);
        assertThat(output).isEqualTo("1 2 3 \none two three \n");
    }

    @Test
    @DisplayName("GenericsMethodDemo01：泛型方法")
    public void testGenericsMethodDemo01() {
        String output = captureOutput(GenericsMethodDemo01::demo);
        assertThat(output).isEqualTo("class java.lang.String\nclass java.lang.Integer\n");
    }

    @Test
    @DisplayName("GenericVarargsMethodDemo：泛型可变参数")
    public void testGenericVarargsMethodDemo() {
        String output = captureOutput(GenericVarargsMethodDemo::demo);
        assertThat(output).isEqualTo("[A]\n[A, B, C]\n");
    }

    @Test
    @DisplayName("NoGenericsDemo02：不用泛型的类型安全依赖强转")
    public void testNoGenericsDemo02() {
        String output = captureOutput(NoGenericsDemo02::demo);
        assertThat(output).isEqualTo("str = [abc]\n");
    }

}
