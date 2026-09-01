package io.github.dunwu.javacore.nio.charset;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 示例：使用 {@link Charset} 的编码器 / 解码器进行编解码。
 * <p>
 * 注：本示例故意使用只能表示英文字符的 ISO_8859_1 对中文编码，
 * 运行时编码器会抛出 {@link java.nio.charset.CharacterCodingException}，
 * 以此展示“字符集无法映射的字符会编码失败”这一行为，不纳入自动化测试。
 */
public class CharsetEnDeDemo {

    /**
     * 演示 CharsetEncoder / CharsetDecoder 用法。
     * <p>
     * 对 ISO_8859_1 编码中文字符会抛出编码异常。
     */
    public static void demo() throws Exception {
        Charset charset = StandardCharsets.ISO_8859_1; // 只能表示的英文字符
        CharsetEncoder encoder = charset.newEncoder(); // 得到编码器
        CharsetDecoder decoder = charset.newDecoder(); // 得到解码器
        // 通过CharBuffer类中的
        CharBuffer cb = CharBuffer.wrap("梦里花落知多少");
        ByteBuffer buf = encoder.encode(cb); // 进行编码操作
        System.out.println(decoder.decode(buf)); // 进行解码操作
    }

    public static void main(String[] args) throws Exception {
        demo();
    }

}
