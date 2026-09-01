package io.github.dunwu.javacore.jdk11.string;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Java 11 String 增强示例。
 * <p>
 * Java 11 为 {@link String} 新增了一组实用方法：
 * <ul>
 * <li>{@code isBlank()}：是否为空白（空串或仅含空白字符，支持 Unicode 空白）</li>
 * <li>{@code lines()}：按行分割为 Stream&lt;String&gt;</li>
 * <li>{@code strip() / stripLeading() / stripTrailing()}：去除首尾/首部/尾部空白（支持 Unicode，优于 trim）</li>
 * <li>{@code repeat(int)}：重复字符串指定次数</li>
 * </ul>
 */
public class StringEnhanceDemo {

    /**
     * 示例 1：isBlank——空串或仅空白字符均视为 blank；lines 按行分割为 Stream
     */
    public static void isBlankAndLines() {
        System.out.println("\"\".isBlank(): " + "".isBlank());
        System.out.println("\"  \".isBlank(): " + "  ".isBlank());
        System.out.println("\"a\".isBlank(): " + "a".isBlank());

        // lines：按行分割，返回 Stream
        String text = "第一行\n第二行\n第三行";
        List<String> lines = text.lines().collect(Collectors.toList());
        System.out.println("lines: " + lines);
    }

    /**
     * 示例 2：strip 系列——支持 Unicode 空白字符（trim 只能处理 <= ' ' 的字符）
     */
    public static void stripMethods() {
        String padded = "\u2000前后有空格\u2000"; // \u2000 是 Unicode 空格
        System.out.println("strip 结果: [" + padded.strip() + "]");
        System.out.println("stripLeading 结果: [" + padded.stripLeading() + "]");
        System.out.println("stripTrailing 结果: [" + padded.stripTrailing() + "]");
    }

    /**
     * 示例 3：repeat 重复字符串，组合 strip 清洗用户输入
     */
    public static void repeatAndClean() {
        System.out.println("repeat 结果: " + "Java ".repeat(3));

        // 组合使用：清洗用户输入
        String input = "  \n hello world \n ";
        String cleaned = input.strip();
        System.out.println("清洗结果: [" + cleaned + "]");
    }

    public static void main(String[] args) {
        isBlankAndLines();
        stripMethods();
        repeatAndClean();
    }

}
// Output:
// "".isBlank(): true
// "  ".isBlank(): true
// "a".isBlank(): false
// lines: [第一行, 第二行, 第三行]
// strip 结果: [前后有空格]
// stripLeading 结果: [前后有空格\u2000]
// stripTrailing 结果: [\u2000前后有空格]
// repeat 结果: Java Java Java
// 清洗结果: [hello world]
