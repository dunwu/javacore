package io.github.dunwu.javacore.datatype;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * {@link StringImmutabilityDemo} 单元测试：String 的不可变性。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
@DisplayName("String 不可变性示例测试")
public class StringImmutabilityDemoTest {

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
    @DisplayName("所有「看似修改」的方法都返回新对象，原串不变")
    void testOperationsReturnNewObject() {
        String output = captureOutput(StringImmutabilityDemo::operationsReturnNewObject);
        assertThat(output).isEqualTo("原串: Java\n"
            + "toUpperCase() 返回: JAVA，此时原串: Java\n"
            + "replace('a', 'o') 返回: Jovo，此时原串: Java\n"
            + "substring(0, 2) 返回: Ja，此时原串: Java\n"
            + "concat(\"Core\") 返回: JavaCore，此时原串: Java\n"
            + "四个结果都不是原对象: true\n");
    }

    @Test
    @DisplayName("+= 是让引用改指向新对象，而不是修改原对象")
    void testReassignNotMutate() {
        String output = captureOutput(StringImmutabilityDemo::reassignNotMutate);
        assertThat(output).isEqualTo("拼接前 str: a\n"
            + "拼接后 str: ab\n"
            + "原来那个对象的内容仍是: a\n"
            + "拼接后 str 与原对象是同一个: false\n");
    }

    @Test
    @DisplayName("构造与取出都做防御性拷贝，外部改数组影响不到 String")
    void testDefensiveCopy() {
        String output = captureOutput(StringImmutabilityDemo::defensiveCopy);
        assertThat(output).isEqualTo("用 char[] 构造出的 String: Java\n"
            + "把数组首元素改成 X 后，String 仍是: Java\n"
            + "把 toCharArray() 结果首元素改成 Y 后，String 仍是: Java\n");
    }

    @Test
    @DisplayName("纯字面量拼接会被编译期折叠，含变量的拼接则在运行期新建对象")
    void testCompileTimeFolding() {
        String output = captureOutput(StringImmutabilityDemo::compileTimeFolding);
        assertThat(output).isEqualTo("纯字面量拼接后与 \"ab\" 是同一个对象: true\n"
            + "含变量的拼接与 \"ab\" 是同一个对象: false\n"
            + "但两者内容相等: true\n");
    }

    @Test
    @DisplayName("不可变的三个好处，并用「可变对象作 key」做反例")
    void testBenefits() {
        String output = captureOutput(StringImmutabilityDemo::benefits);
        assertThat(output).isEqualTo("好处一：内容永不变，因此多线程共享同一个 String 无需任何同步\n"
            + "好处二：hashCode 只需算一次即可缓存，所以 String 适合做 key，取值得到: 1\n"
            + "改动内容前，用等值的 key 能取到: value\n"
            // 关键结论：List 重写了 hashCode，put 之后改动内容会让「等值 key」再也查不到它
            + "改动内容后，用等值的 key 能取到: null\n"
            + "内容相同的两个 StringBuilder 是否 equals: false\n");
    }

    @Test
    @DisplayName("常量池：字面量共享同一实例，new String 另建对象，intern 指回常量池")
    void testConstantPool() {
        String output = captureOutput(StringImmutabilityDemo::constantPool);
        assertThat(output).isEqualTo("两个字面量是同一个对象: true\n"
            + "new String(\"shared\") 与字面量是同一个对象: false\n"
            + "但两者内容相等: true\n"
            + "intern() 之后与字面量是同一个对象: true\n");
    }

    @Test
    @DisplayName("demo：完整演示可正常执行")
    void testDemo() {
        // 用 captureOutput 包裹，避免示例的输出直接打到构建日志里
        assertThatCode(() -> captureOutput(StringImmutabilityDemo::demo)).doesNotThrowAnyException();
    }

}
