package io.github.dunwu.javacore.object;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ClassDemo 系列示例测试：对象的实例化、属性赋值、引用共享与封装。
 */
@DisplayName("Java 类与对象示例测试")
public class ClassDemoTest {

    @Test
    @DisplayName("示例 1：两种实例化写法，声明本身不会分配堆内存")
    public void testClassDemo01() {
        String output = captureOutput(ClassDemo01::demo);
        assertThat(output)
            .contains("person1 已实例化：true")
            .contains("person2 已实例化：true");
    }

    @Test
    @DisplayName("示例 2：给属性赋值并调用方法")
    public void testClassDemo02() {
        String output = captureOutput(ClassDemo02::demo);
        assertThat(output).contains("姓名：张三；年龄：30");
    }

    @Test
    @DisplayName("示例 3：两个独立对象的属性互不影响")
    public void testClassDemo03() {
        String output = captureOutput(ClassDemo03::demo);
        assertThat(output)
            .contains("person1对象中的内容 --> 姓名：张三；年龄：30")
            .contains("person2对象中的内容 --> 姓名：李四；年龄：33");
    }

    @Test
    @DisplayName("示例 4：引用赋值共享堆内存，修改任一方影响另一方")
    public void testClassDemo04() {
        String output = captureOutput(ClassDemo04::demo);
        assertThat(output)
            .contains("person1对象中的内容 --> 姓名：李四；年龄：33")
            .contains("person2对象中的内容 --> 姓名：李四；年龄：33");
    }

    @Test
    @DisplayName("示例 5：引用转指后两个引用指向同一对象")
    public void testClassDemo05() {
        String output = captureOutput(ClassDemo05::demo);
        assertThat(output)
            .contains("person1对象中的内容 --> 姓名：张三；年龄：30")
            .contains("person2对象中的内容 --> 姓名：张三；年龄：30");
    }

    @Test
    @DisplayName("示例 6：封装——只能通过 setter 访问私有属性，且 setter 带校验")
    public void testClassDemo06() {
        String output = captureOutput(ClassDemo06::demo);
        assertThat(output).contains("姓名：张三，年龄：18");
    }

    @Test
    @DisplayName("Person2：setAge 校验，非法年龄（负数）被忽略")
    public void testPerson2AgeValidation() {
        Person2 person = new Person2();
        person.setAge(-30); // 非法，不生效
        assertThat(person.getAge()).isEqualTo(0);
        person.setAge(20); // 合法，生效
        assertThat(person.getAge()).isEqualTo(20);
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
