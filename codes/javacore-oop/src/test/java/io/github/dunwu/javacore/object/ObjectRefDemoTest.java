package io.github.dunwu.javacore.object;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ObjectRefDemo 系列示例测试：对象引用传递与对象关联。
 */
@DisplayName("Java 对象引用示例测试")
public class ObjectRefDemoTest {

    @Test
    @DisplayName("示例 1：引用传参——方法内修改属性会影响外部对象")
    public void testObjectRefDemo01() {
        String output = captureOutput(ObjectRefDemo01::demo);
        assertThat(output)
            .contains("fun()方法调用之前：50")
            .contains("fun()方法调用之后：10");
    }

    @Test
    @DisplayName("示例 2：String 不可变——方法内重新赋值不影响外部")
    public void testObjectRefDemo02() {
        String output = captureOutput(ObjectRefDemo02::demo);
        assertThat(output)
            .contains("fun()方法调用之前：hello")
            .contains("fun()方法调用之后：hello");
    }

    @Test
    @DisplayName("示例 3：方法内修改对象属性会影响外部")
    public void testObjectRefDemo03() {
        String output = captureOutput(ObjectRefDemo03::demo);
        assertThat(output)
            .contains("fun()方法调用之前：world")
            .contains("fun()方法调用之后：javase");
    }

    @Test
    @DisplayName("示例 4：对象传回本类方法，可直接修改私有属性")
    public void testObjectRefDemo04() {
        String output = captureOutput(ObjectRefDemo04::demo);
        assertThat(output).contains("age = 18");
    }

    @Test
    @DisplayName("示例 5：人与书双向关联，互相查找")
    public void testObjectRefDemo05() {
        String output = captureOutput(ObjectRefDemo05::demo);
        assertThat(output)
            .contains("从人找到书 --> 姓名：张三；年龄：30；书名：JAVA SE核心开发；价格：90.0")
            .contains("从书找到人 --> 书名：JAVA SE核心开发；价格：90.0；姓名：张三；年龄：30");
    }

    @Test
    @DisplayName("示例 6：沿对象引用链逐层查找（人→书、书→人、人→孩子→书）")
    public void testObjectRefDemo06() {
        String output = captureOutput(ObjectRefDemo06::demo);
        assertThat(output)
            .contains("从人找到书 --> 姓名：张三；年龄：30；书名：JAVA SE核心开发；价格：90.0")
            .contains("从书找到人 --> 书名：JAVA SE核心开发；价格：90.0；姓名：张三；年龄：30")
            .contains("张三的孩子 --> 姓名：张草；年龄：10；书名：一千零一夜；价格：30.3");
    }

    @Test
    @DisplayName("Book：getter/setter 与关联查询")
    public void testBook() {
        Book book = new Book("Effective Java", 88.8f);
        assertThat(book.getTitle()).isEqualTo("Effective Java");
        assertThat(book.getPrice()).isEqualTo(88.8f);

        Person2 owner = new Person2("王五", 40);
        book.setPerson2(owner);
        owner.setBook(book);
        assertThat(book.getPerson2().getName()).isEqualTo("王五");
        assertThat(owner.getBook().getTitle()).isEqualTo("Effective Java");
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
