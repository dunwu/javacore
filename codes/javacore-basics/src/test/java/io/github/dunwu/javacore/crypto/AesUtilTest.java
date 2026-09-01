package io.github.dunwu.javacore.crypto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * {@link RsaUtil} 测试
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @date 2024-03-05
 */
public class AesUtilTest {

    private static AesUtil aes = null;

    @BeforeAll
    @DisplayName("初始化工具类")
    public static void init() throws Exception {
        aes = AesUtil.newInstance(AesUtil.AesAlgorithmEnum.AES_ECB_PKCS5PADDING,
            AesUtil.DEFAULT_IV.getBytes(), AesUtil.DEFAULT_SEED);
        System.out.printf("【Key（Base64）】\n%s\n", aes.getBase64Key());
    }

    @Test
    @DisplayName("私钥加密，公钥解密")
    public void test1() throws Exception {

        String content = "Hello World";
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);

        // 加密
        byte[] ciphertext = aes.encrypt(bytes);
        String ciphertextBase64 = Base64.getEncoder().encodeToString(ciphertext);

        // 解密
        byte[] plaintext = aes.decrypt(Base64.getDecoder().decode(ciphertextBase64));
        String plaintextStr = new String(plaintext);

        System.out.println("============== 私钥加密，公钥解密 ==============");
        System.out.println("原文：" + content);
        System.out.println("加密：" + ciphertextBase64);
        System.out.println("解密：" + plaintextStr);
        Assertions.assertThat(plaintextStr).isEqualTo(content);
    }

}
