package io.github.dunwu.javacore.crypto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * crypto 包示例测试：Base64、消息摘要、Hmac、对称加密（AES/DES/3DES/PBE）。
 * <p>
 * 注意：DsaUtil、RsaUtil 已有独立测试（DsaUtilTest、RsaUtilTest、AesUtilTest）。
 */
@DisplayName("加密示例测试")
public class CryptoDemoTest {

    @Test
    @DisplayName("Base64Demo：标准与 URL 安全的 Base64 编解码")
    void testBase64Demo() throws Exception {
        String output = captureOutput(Base64Demo::demo);
        assertThat(output).contains("url:https://www.baidu.com");
        assertThat(output).contains("Base64 decoded: https://www.baidu.com");
        assertThat(output).contains("Url Safe Base64 decoded: https://www.baidu.com");
    }

    @Test
    @DisplayName("MessageDigestDemo：MD/SHA 系列消息摘要")
    void testMessageDigestDemo() throws Exception {
        String output = captureOutput(MessageDigestDemo::demo);
        assertThat(output).contains("MD2: ");
        assertThat(output).contains("MD5: ");
        assertThat(output).contains("SHA1: ");
        assertThat(output).contains("SHA256: ");
        assertThat(output).contains("SHA384: ");
        assertThat(output).contains("SHA512: ");
        // 同一输入的摘要结果稳定
        byte[] msg = "Hello World!".getBytes();
        assertThat(MessageDigestDemo.encodeWithBase64String(msg, MessageDigestDemo.Type.MD5))
            .isEqualTo(MessageDigestDemo.encodeWithBase64String(msg, MessageDigestDemo.Type.MD5));
    }

    @Test
    @DisplayName("HmacMessageDigest：Hmac 系列带盐消息摘要")
    void testHmacMessageDigest() throws Exception {
        String output = captureOutput(HmacMessageDigest::demo);
        assertThat(output).contains("原文: Hello World!");
        assertThat(output).contains("HmacMD5: ");
        assertThat(output).contains("HmacSHA512: ");
    }

    @Test
    @DisplayName("AesUtil.demo：AES 加密后解密还原原文")
    void testAesDemo() throws Exception {
        String output = captureOutput(AesUtil::demo);
        assertThat(output).contains("[AES加密、解密]");
        assertThat(output).contains("message: Hello World!");
        assertThat(output).contains("decoded: Hello World!");
    }

    @Test
    @DisplayName("DESCoder.demo：DES 加密后解密还原原文")
    void testDesDemo() throws Exception {
        String output = captureOutput(DESCoder::demo);
        assertThat(output).contains("原文: Hello World!");
        assertThat(output).contains("密文: ");
        assertThat(output).contains("明文: Hello World!");
    }

    @Test
    @DisplayName("DESedeCoder.demo：3DES 加密后解密还原原文")
    void testDesedeDemo() throws Exception {
        String output = captureOutput(DESedeCoder::demo);
        assertThat(output).contains("Hello World!");
    }

    @Test
    @DisplayName("PBECoder.demo：PBE 口令加盐加密后解密还原原文")
    void testPbeDemo() throws Exception {
        String output = captureOutput(PBECoder::demo);
        assertThat(output).contains("原文：Hello World!");
        assertThat(output).contains("密文：");
        assertThat(output).contains("明文：Hello World!");
    }

    /**
     * 捕获 System.out 输出（支持抛出受检异常的示例）。
     */
    @FunctionalInterface
    private interface ThrowingRunnable {

        void run() throws Exception;
    }

    private static String captureOutput(ThrowingRunnable action) throws Exception {
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
