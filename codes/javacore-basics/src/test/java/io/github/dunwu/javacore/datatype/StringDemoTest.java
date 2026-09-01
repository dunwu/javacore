package io.github.dunwu.javacore.datatype;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class StringDemoTest {

    public static void main(String[] args) {
        //

        System.out.println();

        // 根据参数截取字符串
        System.out.println("Hello World".substring(6)); // 从位置6开始截取
        System.out.println("Hello World".substring(0, 5)); // 截取0~5个位置的内容

        // 按照指定字符拆分字符串
        String[] s = "sample@sina.com".split("@");
        for (int i = 0; i < s.length; i++) {
            System.out.println(s[i]);
        }

        // 去除左右空格
        System.out.println("    Night       ".trim()); // 去除左右空格输出

        // 转换大小写
        System.out.println("China".toLowerCase());
        System.out.println("China".toUpperCase());

        // 判断是否以指定的字符串开头或结尾
        if ("**NAME".startsWith("**")) {
            System.out.println("**NAME 以**开头");
        }
        if ("NAME**".endsWith("**")) {
            System.out.println("NAME** 以**结尾");
        }

        // 替换源子字符串为目标子字符串
        System.out.println("good".replaceAll("o", "x"));
    }

    /**
     * 获取字符串字符个数
     */
    @Test
    @DisplayName("length()：获取字符串字符个数")
    public void testLength() {
        int length = " Goodbye ".length();
        Assertions.assertEquals(9, length);
        System.out.println(length);
    }

    /**
     * 获取 String 中该索引位置上的 char
     */
    @Test
    @DisplayName("charAt()：获取指定索引位置的字符")
    public void testCharAt() {
        char c = "Computer".charAt(4);
        Assertions.assertEquals('u', c);
        System.out.println(c);
    }

    /**
     * 复制 byte 到一个目标数组
     */
    @Test
    @DisplayName("getBytes()：将字符串转为 byte 数组")
    public void testGetBytes() {
        byte[] bytes = "Winter".getBytes(); // 将字符串转为 byte 数组
        Assertions.assertEquals("Winter", new String(bytes));
        Assertions.assertEquals("int", new String(bytes, 1, 3));
        System.out.println(new String(bytes)); // 将完整 byte 数组转为字符串
        System.out.println(new String(bytes, 1, 3)); // 将部分 byte 数组转为字符串
    }

    /**
     * 复制 char 到一个目标数组
     */
    @Test
    @DisplayName("getChars()：将字符串字符拷贝到目标 char 数组")
    public void testGetChars() {
        char[] chars = new char[10];
        "Summer".getChars(0, 6, chars, 2); // 将字符串0~6位置的内容拷贝到 char 数组中，从数组位置2开始
        System.out.println(new String(chars)); // 将完整 char 数组转为字符串
        System.out.println(new String(chars, 1, 3)); // 将部分 char 数组转为字符串
    }

    /**
     * 字符串转char数组
     */
    @Test
    @DisplayName("toCharArray()：将字符串转为 char 数组")
    public void testToCharArray() {
        char[] expected = new char[] { 'B', 'a', 'b', 'y' };
        char[] actual = "Baby".toCharArray();
        Assertions.assertArrayEquals(expected, actual);
        for (char c : actual) {
            System.out.print(c + " ");
        }
    }

    /**
     * 如果String不包含此参数，返回-1，否则返回此参数在String中的起始索引。lastIndexOf是从后向前查找
     */
    @Test
    @DisplayName("indexOf/lastIndexOf：查找子串的位置")
    public void testIndexOf() {
        final String origin = "How are you";
        // 查找返回位置
        Assertions.assertEquals(1, origin.indexOf("o"));
        // 查找返回位置, 从位置5开始
        Assertions.assertEquals(9, origin.indexOf("o", 5));
        // 没有查到返回-1
        Assertions.assertEquals(-1, origin.indexOf("z"));
        // 查找返回位置
        Assertions.assertEquals(9, origin.lastIndexOf("o"));
        // 查找返回位置, 从位置5开始
        Assertions.assertEquals(1, origin.lastIndexOf("o", 5));
        // 没有查到返回-1
        Assertions.assertEquals(-1, origin.lastIndexOf("z"));
    }

}
