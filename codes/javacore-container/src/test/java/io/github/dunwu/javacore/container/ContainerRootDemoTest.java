package io.github.dunwu.javacore.container;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * javacore-container 根包示例的单元测试
 */
@DisplayName("容器根包示例测试")
public class ContainerRootDemoTest {

    @Test
    @DisplayName("Enumeration 老式遍历输出全部元素")
    public void testEnumerationDemo01() {
        String output = captureOutput(EnumerationDemo01::demo);
        assertThat(output).isEqualTo("hello、_、world、");
    }

    @Test
    @DisplayName("HashMap 中 equals 相等的 Person 视为同一 key，后者覆盖前者")
    public void testIdentityHashMapDemo01() {
        String output = captureOutput(IdentityHashMapDemo01::demo);
        assertThat(output).contains("姓名：张三；年龄：30 --> zhangsan_2");
        assertThat(output).contains("姓名：李四；年龄：31 --> lisi");
        assertThat(output).doesNotContain("zhangsan_1");
    }

    @Test
    @DisplayName("IdentityHashMap 只按引用地址判断，内容相同的两个对象是不同 key")
    public void testIdentityHashMapDemo02() {
        String output = captureOutput(IdentityHashMapDemo02::demo);
        assertThat(output).contains("zhangsan_1");
        assertThat(output).contains("zhangsan_2");
        assertThat(output).contains("姓名：李四；年龄：31 --> lisi");
    }

    @Test
    @DisplayName("ListIterator 前向遍历时把元素改为小写，后向遍历输出小写元素")
    public void testListIteratorDemo() {
        String output = captureOutput(ListIteratorDemo::demo);
        assertThat(output).contains("由前向后输出：");
        assertThat(output).contains("A B C");
        assertThat(output).contains("由后向前输出：");
        assertThat(output).contains("c b a");
    }

    @Test
    @DisplayName("一对多关联：从学校找到全部学生")
    public void testTestDemo() {
        String output = captureOutput(TestDemo::demo);
        assertThat(output).contains("学校名称：清华大学");
        assertThat(output).contains("学生姓名：张三；年龄：21");
        assertThat(output).contains("学生姓名：李四；年龄：22");
        assertThat(output).contains("学生姓名：王五；年龄：23");
    }

    @Test
    @DisplayName("多对多关联：从课程找学生、从学生找课程")
    public void testTestMore() {
        String output = captureOutput(TestMore::demo);
        assertThat(output).contains("Course{name='英语', credit=3");
        assertThat(output).contains("学生姓名：张三；年龄：20");
        assertThat(output).contains("学生姓名：钱八；年龄：24");
        assertThat(output).contains("Course{name='计算机', credit=5");
    }

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

}
