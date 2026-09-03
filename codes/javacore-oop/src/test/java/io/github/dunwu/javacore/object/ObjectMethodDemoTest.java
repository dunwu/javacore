package io.github.dunwu.javacore.object;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Object 通用方法示例测试：{@code equals} / {@code hashCode} / {@code toString} / {@code clone}。
 * <p>
 * 这些示例的输出都是确定的语言行为，因此对关键结论做精确断言；
 * 只有默认 {@code toString} 里含有对象地址，用正则匹配。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
@DisplayName("Object 通用方法示例测试")
public class ObjectMethodDemoTest {

    @FunctionalInterface
    interface ThrowingRunnable {

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

    @Test
    @DisplayName("equals：不重写时 Object.equals 就是 ==，字段相同也不相等")
    void testDefaultEquals() {
        String output = captureOutput(EqualsAndHashCodeDemo::defaultEquals);
        assertThat(output).isEqualTo("字段完全相同的两个对象 == : false\n"
            + "字段完全相同的两个对象 equals: false\n"
            + "同一个引用 equals 自身: true\n"
            + "与 null 比较: false\n");
    }

    @Test
    @DisplayName("equals：自反性、对称性、传递性、一致性、非空性五条契约")
    void testEqualsContract() {
        String output = captureOutput(EqualsAndHashCodeDemo::equalsContract);
        assertThat(output).isEqualTo("自反性 a.equals(a): true\n"
            + "对称性 a.equals(b) == b.equals(a): true\n"
            + "传递性 a=b, b=d, 则 a.equals(d): true\n"
            + "一致性 连续三次调用结果相同: true\n"
            + "非空性 a.equals(null): false\n"
            + "与字符串比较 a.equals(\"张三\"): false\n"
            + "字段不同 a.equals(c): false\n");
    }

    @Test
    @DisplayName("hashCode：equals 相等则 hashCode 必须相等")
    void testHashCodeContract() {
        String output = captureOutput(EqualsAndHashCodeDemo::hashCodeContract);
        assertThat(output).isEqualTo("equals 相等 => hashCode 相等: true\n"
            + "同一对象多次调用 hashCode 一致: true\n"
            + "hashCode 是否参与 equals 判断: false（它只用于定位桶）\n");
    }

    @Test
    @DisplayName("反例：只重写 equals 不重写 hashCode，HashSet 存进重复元素、HashMap 取不出值")
    void testMissingHashCode() {
        String output = captureOutput(EqualsAndHashCodeDemo::missingHashCode);
        assertThat(output).isEqualTo("HashSet 里的元素个数（应为 1）: 2\n"
            + "contains 一个字段相同的新对象: false\n"
            + "用原来的 key 能取到: 工程师\n"
            + "用相等的新 key 能取到: null\n");
    }

    @Test
    @DisplayName("正例：equals 与 hashCode 一起重写后，哈希容器恢复正常")
    void testCorrectHashCode() {
        String output = captureOutput(EqualsAndHashCodeDemo::correctHashCode);
        assertThat(output).isEqualTo("HashSet 里的元素个数: 1\n"
            + "contains 一个字段相同的新对象: true\n"
            + "用相等的新 key 能取到: 工程师\n");
    }

    @Test
    @DisplayName("toString：默认格式是「类名@十六进制hashCode」，字符串拼接会隐式调用")
    void testDefaultToString() {
        String output = captureOutput(ToStringDemo::defaultToString);
        assertThat(output).contains("默认 toString 的格式是「类名@十六进制hashCode」: true");
        // 隐式调用的输出里含对象地址，每次运行都不同，只用正则校验格式
        assertThat(output).containsPattern("隐式调用: " + ToStringDemo.class.getName().replace(".", "\\.")
            + "\\$Plain@[0-9a-f]+\\n");
    }

    @Test
    @DisplayName("toString：重写后包含全部字段，日志可自解释")
    void testOverriddenToString() {
        String output = captureOutput(ToStringDemo::overriddenToString);
        assertThat(output).isEqualTo("重写后的 toString: Employee{name='张三', age=30}\n"
            + "包含全部字段: true\n");
    }

    @Test
    @DisplayName("toString：集合与嵌套对象会递归调用元素的 toString")
    void testNestedAndCollection() {
        String output = captureOutput(ToStringDemo::nestedAndCollection);
        assertThat(output).isEqualTo("List: [Employee{name='张三', age=30}, Employee{name='李四', age=25}]\n"
            + "嵌套对象: Department{name='研发部', employees="
            + "[Employee{name='张三', age=30}, Employee{name='李四', age=25}]}\n"
            + "Map: {emp001=Employee{name='张三', age=30}}\n");
    }

    @Test
    @DisplayName("clone：未实现 Cloneable 时抛 CloneNotSupportedException")
    void testNotCloneable() {
        String output = captureOutput(CloneDemo::notCloneable);
        assertThat(output).isEqualTo("未实现 Cloneable 时抛出的异常: CloneNotSupportedException\n");
    }

    @Test
    @DisplayName("clone：浅拷贝下两个对象共享 address，改克隆体会连带改到原对象")
    void testShallowClone() {
        String output = captureOutput(CloneDemo::shallowClone);
        assertThat(output).isEqualTo("克隆后是两个不同的对象: true\n"
            + "修改克隆体的 name 后，原对象的 name: 张三\n"
            + "克隆体的 name: 李四\n"
            + "两个对象的 address 是同一个实例: true\n"
            // 这一行是浅拷贝的事故现场：原对象的城市被克隆体的修改改掉了
            + "修改克隆体的城市后，原对象的城市: 北京\n");
    }

    @Test
    @DisplayName("clone：深拷贝复制了可变字段，两个对象彻底独立")
    void testDeepClone() {
        String output = captureOutput(CloneDemo::deepClone);
        assertThat(output).isEqualTo("两个对象的 address 是同一个实例: false\n"
            + "修改克隆体的城市后，原对象的城市: 南京\n"
            + "克隆体的城市: 北京\n");
    }

    @Test
    @DisplayName("clone：拷贝构造器是更推荐的替代方案")
    void testCopyConstructor() {
        String output = captureOutput(CloneDemo::copyConstructor);
        assertThat(output).isEqualTo("拷贝构造器得到的是独立对象: true\n"
            + "修改副本的城市后，原对象的城市: 南京\n"
            + "副本的城市: 北京\n"
            + "final 字段可以正常赋值: true\n");
    }

    @Test
    @DisplayName("demo：三个示例的完整演示均可正常执行")
    void testAllDemos() {
        // 用 captureOutput 包裹，避免示例的输出直接打到构建日志里
        assertThatCode(() -> captureOutput(EqualsAndHashCodeDemo::demo)).doesNotThrowAnyException();
        assertThatCode(() -> captureOutput(ToStringDemo::demo)).doesNotThrowAnyException();
        assertThatCode(() -> captureOutput(CloneDemo::demo)).doesNotThrowAnyException();
    }

}
