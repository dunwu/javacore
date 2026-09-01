package io.github.dunwu.javacore.array;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 数组示例测试（对应 array 包中带 demo 方法的示例，{@code ArrayDemoTest}、{@code ArrayRefDemoTest}
 * 中为另一组自包含示例）
 */
public class ArrayMainDemoTest {

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
    @DisplayName("ArrayDemo：数组基本用法，输出数组长度")
    void testArrayDemo() {
        String output = captureOutput(ArrayDemo::demo);
        assertThat(output).contains("array1 size is 2").contains("array2 size is 2");
    }

    @Test
    @DisplayName("ArrayDemo2：未初始化数组元素输出 null")
    void testArrayDemo2() {
        String output = captureOutput(ArrayDemo2::demo);
        assertThat(output).contains("array1:").contains("array2:").contains("null");
    }

    @Test
    @DisplayName("ArrayDemo3：不同初始化方式下的数组长度")
    void testArrayDemo3() {
        String output = captureOutput(ArrayDemo3::demo);
        assertThat(output)
            .contains("array3.length = [97]")
            .contains("array4.length = [3]")
            .contains("array5.length = [5]")
            .contains("array6.length = [99]");
    }

    @Test
    @DisplayName("ArrayDemo4：遍历输出数组元素")
    void testArrayDemo4() {
        String output = captureOutput(ArrayDemo4::demo);
        assertThat(output).isEqualTo("array[0] = 2\narray[1] = 3\narray[2] = 4\n");
    }

    @Test
    @DisplayName("ArrayRefDemo：数组引用传递，方法内修改影响原数组")
    void testArrayRefDemo() {
        String output = captureOutput(ArrayRefDemo::demo);
        assertThat(output).contains("1\t3\t5\t");
    }

    @Test
    @DisplayName("ArrayRefDemo2：数组引用传递返回修改后的数组")
    void testArrayRefDemo2() {
        String output = captureOutput(ArrayRefDemo2::demo);
        assertThat(output).isEqualTo("[1, 3, 5]\n");
    }

    @Test
    @DisplayName("ArraysDemo：Arrays 工具类的填充、排序、拷贝等操作")
    void testArraysDemo() {
        String output = captureOutput(ArraysDemo::demo);
        assertThat(output).contains("[4, 1, 5]").contains("[1, 4, 5]").contains("[6, 6, 6]");
    }

    @Test
    @DisplayName("GenericArrayDemo：泛型数组的创建与默认值")
    void testGenericArrayDemo() {
        String output = captureOutput(GenericArrayDemo::demo);
        assertThat(output).isEqualTo("[0, 1, null, null]\n");
    }

    @Test
    @DisplayName("MultiArrayDemo：多维数组的初始化与输出")
    void testMultiArrayDemo() {
        String output = captureOutput(MultiArrayDemo::demo);
        assertThat(output)
            .contains("a1: [[1, 2, 3], [4, 5, 6]]")
            .contains("[The, Quick, Sly, Fox]");
    }

}
