package io.github.dunwu.javacore.object;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * {@link InheritanceDemo} 单元测试：继承、重写与多态分派。
 * <p>
 * 这里全部使用精确断言（{@code isEqualTo}）：动态分派、静态绑定、构造顺序都是确定的语言行为，
 * 一旦 JVM 或编译器语义被误解，断言会立刻失败。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
@DisplayName("继承与多态示例测试")
public class InheritanceDemoTest {

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
    @DisplayName("override：重载在编译期按静态类型选定，重写在运行期按实际类型分派")
    void testOverride() {
        String output = captureOutput(InheritanceDemo::override);
        assertThat(output).isEqualTo("静态类型为 Object 时选中: 子类重写后的 call(Object)\n"
            + "静态类型为 String 时选中: call(String) —— 子类没有重写\n"
            + "协变返回类型得到的是: Sub\n"
            + "子类放宽了访问权限: 子类把 protected 放宽为 public\n");
    }

    @Test
    @DisplayName("upcastAndDispatch：实例方法按对象的运行时类型分派")
    void testUpcastAndDispatch() {
        String output = captureOutput(InheritanceDemo::upcastAndDispatch);
        assertThat(output).isEqualTo("声明为 Animal，实际是 Dog: 汪汪\n"
            + "声明为 Animal，实际是 Cat: 喵喵\n"
            + "统一遍历时调用 speak(): 汪汪\n"
            + "统一遍历时调用 speak(): 喵喵\n");
    }

    @Test
    @DisplayName("fieldAndStaticBinding：字段与静态方法不参与多态，按引用的声明类型绑定")
    void testFieldAndStaticBinding() {
        String output = captureOutput(InheritanceDemo::fieldAndStaticBinding);
        assertThat(output).isEqualTo("通过 Animal 引用访问 name: 动物\n"
            // 关键结论：子类隐藏了同名字段，但静态类型决定了访问到的是父类字段
            + "强转为 Dog 后访问 name: 狗\n"
            + "通过 Animal 类名调用: Animal 的静态方法\n"
            + "通过 Animal 引用调用（仍绑定到 Animal）: Animal 的静态方法\n"
            + "通过 Dog 类名调用: Dog 的静态方法\n"
            + "同一个引用的实例方法则是运行期分派: 汪汪\n");
    }

    @Test
    @DisplayName("constructorChain：父类构造器总在子类之前完成")
    void testConstructorChain() {
        String output = captureOutput(InheritanceDemo::constructorChain);
        assertThat(output).isEqualTo("--- 创建 GrandChild（三层继承） ---\n"
            + "1. Parent 无参构造器\n"
            + "2. Child 构造器\n"
            + "3. GrandChild 构造器\n"
            + "--- 创建 ExplicitChild（父类无无参构造器） ---\n"
            + "1. StrictParent 构造器: 必须显式调用\n"
            + "2. ExplicitChild 构造器\n");
    }

    @Test
    @DisplayName("downcast：向下转型前必须用 instanceof 判断，否则抛 ClassCastException")
    void testDowncast() {
        String output = captureOutput(InheritanceDemo::downcast);
        assertThat(output).isEqualTo("animal instanceof Dog: true\n"
            + "animal instanceof Cat: false\n"
            + "判断后转型成功: 汪汪\n"
            + "不判断直接转型抛出: ClassCastException\n"
            + "null instanceof Dog: false\n");
    }

    @Test
    @DisplayName("demo：完整演示可正常执行")
    void testDemo() {
        // 用 captureOutput 包裹，避免示例的输出直接打到构建日志里
        assertThatCode(() -> captureOutput(InheritanceDemo::demo)).doesNotThrowAnyException();
    }

}
