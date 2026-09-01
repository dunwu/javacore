package io.github.dunwu.javacore.jdk8.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Java 8 Base64 编解码示例。
 * <p>
 * Java 8 将 Base64 编解码纳入标准库（{@link Base64}），
 * 替代了 Apache Commons Codec、sun.misc.BASE64Encoder 等第三方/内部实现：
 * <ul>
 * <li>{@code getEncoder() / getDecoder()}：标准 Base64（字母表 A-Z a-z 0-9 + /）</li>
 * <li>{@code getUrlEncoder() / getUrlDecoder()}：URL 安全版（用 - 和 _ 替换 + 和 /）</li>
 * <li>{@code getMimeEncoder()}：MIME 格式（每 76 个字符换行，用于邮件等场景）</li>
 * </ul>
 * Base64 是编码不是加密，常用于在文本协议（HTTP、JSON）中传输二进制数据。
 */
public class Base64Demo {

    /**
     * 示例 1：标准编解码
     */
    public static void standardCodec() {
        String original = "Java 8 Base64 编码示例";
        byte[] bytes = original.getBytes(StandardCharsets.UTF_8);
        String encoded = Base64.getEncoder().encodeToString(bytes);
        System.out.println("标准编码: " + encoded);
        String decoded = new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
        System.out.println("标准解码: " + decoded);
    }

    /**
     * 示例 2：URL 安全编解码，标准结果中的 + / 在 URL 中有特殊含义，需替换
     */
    public static void urlSafeCodec() {
        byte[] urlBytes = {(byte) 0xFB, (byte) 0xFF, (byte) 0xFE};
        String standard = Base64.getEncoder().encodeToString(urlBytes);
        String urlSafe = Base64.getUrlEncoder().encodeToString(urlBytes);
        System.out.println("标准版含 +/: " + standard + ", URL 安全版: " + urlSafe);
        System.out.println("URL 解码还原一致: "
            + java.util.Arrays.equals(Base64.getUrlDecoder().decode(urlSafe), urlBytes));
    }

    /**
     * 示例 3：MIME 编码，按 76 字符换行（编码前长度超过 57 字节即会换行）
     */
    public static void mimeCodec() {
        String longText = "0123456789abcdefghijklmnopqrstuvwxyz"
            + "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String mime = Base64.getMimeEncoder().encodeToString(longText.getBytes(StandardCharsets.UTF_8));
        System.out.println("MIME 编码含换行: " + mime.contains("\r\n"));
        System.out.println("MIME 解码还原一致: "
            + new String(Base64.getMimeDecoder().decode(mime), StandardCharsets.UTF_8).equals(longText));
    }

    public static void main(String[] args) {
        standardCodec();
        urlSafeCodec();
        mimeCodec();
    }

}
// Output:
// 标准编码: SmF2YSA4IEJhc2U2NCDnvJbnoIHnpLrkvos=
// 标准解码: Java 8 Base64 编码示例
// 标准版含 +/: +//+, URL 安全版: -__-
// URL 解码还原一致: true
// MIME 编码含换行: true
// MIME 解码还原一致: true
