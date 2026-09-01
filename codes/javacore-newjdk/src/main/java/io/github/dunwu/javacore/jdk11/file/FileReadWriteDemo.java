package io.github.dunwu.javacore.jdk11.file;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Java 11 Files 读写增强示例。
 * <p>
 * Java 11 为 {@link Files} 新增了四个便捷方法，一行代码即可完成小文件的读写：
 * <ul>
 * <li>{@code Files.readString(Path)}：把整个文件读取为字符串（默认 UTF-8）</li>
 * <li>{@code Files.readString(Path, Charset)}：指定字符集读取</li>
 * <li>{@code Files.writeString(Path, CharSequence)}：把字符串写入文件（创建或覆盖）</li>
 * <li>{@code Files.writeString(Path, CharSequence, Charset, OpenOption...)}：指定字符集和打开选项</li>
 * </ul>
 * 注意：这些方法会一次性把整个文件载入内存，仅适合小文件，大文件仍应使用流或通道。
 */
public class FileReadWriteDemo {

    /**
     * 示例 1：writeString 一行写入、readString 一行读取整个文件
     */
    public static void writeAndRead() throws IOException {
        Path file = Files.createTempFile("javacore-jdk11-demo", ".txt");
        try {
            // writeString：一行代码写入文件（默认 UTF-8，CREATE + TRUNCATE_EXISTING）
            String content = "你好，Java 11！\n这是第二行内容。";
            Files.writeString(file, content);
            System.out.println("写入文件: " + file);

            // readString：一行代码读取整个文件
            String read = Files.readString(file);
            System.out.println("读取内容:");
            System.out.println(read);
        } finally {
            // 清理临时文件
            Files.deleteIfExists(file);
            System.out.println("临时文件已删除");
        }
    }

    /**
     * 示例 2：指定字符集与 APPEND 选项追加写入
     */
    public static void appendWrite() throws IOException {
        Path file = Files.createTempFile("javacore-jdk11-demo", ".txt");
        try {
            Files.writeString(file, "你好，Java 11！\n这是第二行内容。");

            // 追加写入
            Files.writeString(file, "\n这是追加的内容。", StandardCharsets.UTF_8,
                java.nio.file.StandardOpenOption.APPEND);
            System.out.println("追加后行数: " + Files.readString(file).lines().count());
        } finally {
            // 清理临时文件
            Files.deleteIfExists(file);
            System.out.println("临时文件已删除");
        }
    }

    public static void main(String[] args) throws IOException {
        writeAndRead();
        appendWrite();
    }

}
