package io.github.dunwu.javacore.object;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ConstructorDemo 系列示例测试：构造方法与匿名对象。
 */
@DisplayName("Java 构造方法示例测试")
public class ConstructorDemoTest {

    @Test
    @DisplayName("示例 1：声明对象不触发构造方法，new 实例化才触发")
    public void testConstructorDemo01() {
        String output = captureOutput(ConstructorDemo01::demo);
        assertThat(output)
            .contains("声明对象：Person per = null ;")
            .contains("实例化对象：per = new Person() ;");
    }

    @Test
    @DisplayName("示例 2：带参构造方法在实例化时完成属性初始化")
    public void testConstructorDemo02() {
        String output = captureOutput(ConstructorDemo02::demo);
        assertThat(output).contains("姓名：张三，年龄：30");
    }

    @Test
    @DisplayName("示例 3：匿名对象直接调用方法，无需保留引用")
    public void testConstructorDemo03() {
        String output = captureOutput(ConstructorDemo03::demo);
        assertThat(output).contains("姓名：张三，年龄：30");
    }

    @Test
    @DisplayName("Person2：无参构造与带参构造都能正常实例化")
    public void testPerson2Constructors() {
        Person2 empty = new Person2();
        assertThat(empty.getName()).isNull();
        Person2 full = new Person2("李四", 25);
        assertThat(full.getName()).isEqualTo("李四");
        assertThat(full.getAge()).isEqualTo(25);
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
