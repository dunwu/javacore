package io.github.dunwu.javacore.serialize;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.NotSerializableException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * serialize 包序列化示例单元测试
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class SerializeDemoTest {

    @FunctionalInterface
    private interface ThrowingRunnable {

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

    private static void cleanup(String filename) {
        new File(filename).delete();
    }

    @Test
    @DisplayName("ExternalizeDemo01：writeExternal 为空实现，所有字段都不会被序列化")
    void testExternalizeDemo01() {
        String output = captureOutput(ExternalizeDemo01::demo);
        assertThat(output).isEqualTo("call Person()\nname: null, age: null, sex: null\n");
        cleanup("temp_externalize01.dat");
    }

    @Test
    @DisplayName("ExternalizeDemo02：writeExternal 只写入 name 和 age，sex 不会被序列化")
    void testExternalizeDemo02() {
        String output = captureOutput(ExternalizeDemo02::demo);
        assertThat(output).isEqualTo("call Person()\nname: Jack, age: 30, sex: null\n");
        cleanup("temp_externalize02.dat");
    }

    @Test
    @DisplayName("SerializeDemo01：实现 Serializable 接口，所有字段都被完整序列化")
    void testSerializeDemo01() {
        String output = captureOutput(SerializeDemo01::demo);
        assertThat(output).isEqualTo("Person{name='Jack', age=30, sex=MALE}\n");
        cleanup("temp_serialize01.dat");
    }

    @Test
    @DisplayName("SerializeDemo02：transient 修饰的 age 字段反序列化后为 null")
    void testSerializeDemo02() {
        String output = captureOutput(SerializeDemo02::demo);
        assertThat(output).isEqualTo("name: Jack, age: null, sex: MALE\n");
        cleanup("temp_serialize02.dat");
    }

    @Test
    @DisplayName("SerializeDemo03：自定义 writeObject/readObject 使 transient 字段仍能被序列化")
    void testSerializeDemo03() {
        String output = captureOutput(SerializeDemo03::demo);
        assertThat(output).isEqualTo("name: Jack, age: 30, sex: MALE\n");
        cleanup("temp_serialize03.dat");
    }

    @Test
    @DisplayName("SerializeDemo04：反序列化破坏单例，与原单例实例不相等")
    void testSerializeDemo04() {
        String output = captureOutput(SerializeDemo04::demo);
        assertThat(output).isEqualTo("name: Jack, age: null, sex: MALE\nfalse\n");
        cleanup("temp_serialize04.dat");
    }

    @Test
    @DisplayName("SerializeDemo05：readResolve 保护单例，反序列化返回原单例实例")
    void testSerializeDemo05() {
        String output = captureOutput(SerializeDemo05::demo);
        assertThat(output).isEqualTo("name: Tom, age: 31, sex: MALE\ntrue\n");
        cleanup("temp_serialize05.dat");
    }

    @Test
    @DisplayName("UnSerializeDemo（反例）：未实现 Serializable 接口，抛出 NotSerializableException")
    void testUnSerializeDemo() {
        assertThatThrownBy(UnSerializeDemo::demo).isInstanceOf(NotSerializableException.class);
        cleanup("temp_unserialize.dat");
    }

}
