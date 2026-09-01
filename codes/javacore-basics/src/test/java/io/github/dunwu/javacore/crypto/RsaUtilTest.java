package io.github.dunwu.javacore.crypto;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import io.github.dunwu.javacore.bean.Query;
import io.github.dunwu.javacore.util.ParamFormatUtil;
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
public class RsaUtilTest {

    private static RsaUtil rsa = null;

    @BeforeAll
    @DisplayName("初始化工具类")
    public static void init() throws Exception {
        rsa = RsaUtil.newInstance(RsaUtil.ProviderType.BC);
        System.out.printf("【公钥（Base64）】\n%s\n", rsa.getBase64PublicKey());
        System.out.printf("【公钥（Pem）】\n%s\n", rsa.getPemPublicKey());
        System.out.printf("【私钥（Base64）】\n%s\n", rsa.getBase64PrivateKey());
        System.out.printf("【私钥（Pem）】\n%s\n", rsa.getPemPrivateKey());
    }

    @Test
    @DisplayName("私钥加密，公钥解密")
    public void test1() throws Exception {

        String content = "Hello World";
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);

        // 加密
        byte[] ciphertext = rsa.encryptByPrivateKey(bytes);
        String ciphertextBase64 = Base64.getEncoder().encodeToString(ciphertext);

        // 解密
        byte[] plaintext = rsa.decryptByPublicKey(Base64.getDecoder().decode(ciphertextBase64));
        String plaintextStr = new String(plaintext);

        System.out.println("============== 私钥加密，公钥解密 ==============");
        System.out.println("原文：" + content);
        System.out.println("加密：" + ciphertextBase64);
        System.out.println("解密：" + plaintextStr);
        Assertions.assertThat(plaintextStr).isEqualTo(content);
    }

    @Test
    @DisplayName("公钥加密，私钥解密")
    public void test2() throws Exception {

        String content = "Hello World";
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);

        // 加密
        byte[] ciphertext = rsa.encryptByPublicKey(bytes);
        String ciphertextBase64 = Base64.getEncoder().encodeToString(ciphertext);

        // 解密
        byte[] plaintext = rsa.decryptByPrivateKey(ciphertext);
        String plaintextStr = new String(plaintext);

        System.out.println("============== 公钥加密，私钥解密 ==============");
        System.out.println("原文：" + content);
        System.out.println("加密：" + ciphertextBase64);
        System.out.println("解密：" + plaintextStr);
        Assertions.assertThat(plaintextStr).isEqualTo(content);
    }

    @Test
    @DisplayName("未篡改数据的数字签名验证")
    public void verifyOkTest() throws Exception {

        DateTime date = DateUtil.parse("2020-01-01 00:00:00", DatePattern.NORM_DATETIME_PATTERN);
        long timestamp = date.getTime();

        for (long i = 1L; i <= 10L; i++) {
            Query query = new Query(i, "online");
            String content = ParamFormatUtil.getFormatStr(query, timestamp);
            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);

            for (RsaUtil.RsaAlgorithmEnum algorithm : RsaUtil.RsaAlgorithmEnum.values()) {
                byte[] sign = rsa.sign(algorithm, bytes);
                boolean isMatch = rsa.verify(algorithm, bytes, sign);
                String result = isMatch ? "数字签名匹配" : "数字签名不匹配";
                System.out.printf("============== %s 数字签名 ==============\n", algorithm.name());
                System.out.println("数字签名：" + Base64.getEncoder().encodeToString(sign));
                System.out.println("验证结果：" + result);
                Assertions.assertThat(isMatch).isTrue();
            }
        }
    }

    @Test
    @DisplayName("篡改数据的数字签名验证")
    public void verifyFailTest() throws Exception {

        DateTime date = DateUtil.parse("2020-01-01 00:00:00", DatePattern.NORM_DATETIME_PATTERN);
        long timestamp = date.getTime();

        for (long i = 1L; i <= 10L; i++) {
            Query query = new Query(i, "online");
            String content = ParamFormatUtil.getFormatStr(query, timestamp);
            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);

            query.setId(RandomUtil.randomLong());
            String content2 = ParamFormatUtil.getFormatStr(query, System.currentTimeMillis());
            byte[] modifyBytes = content2.getBytes(StandardCharsets.UTF_8);

            for (RsaUtil.RsaAlgorithmEnum algorithm : RsaUtil.RsaAlgorithmEnum.values()) {
                byte[] sign = rsa.sign(algorithm, bytes);
                boolean isMatch = rsa.verify(algorithm, modifyBytes, sign);
                String result = isMatch ? "数字签名匹配" : "数字签名不匹配";
                System.out.printf("============== %s 数字签名 ==============\n", algorithm.name());
                System.out.println("数字签名：" + Base64.getEncoder().encodeToString(sign));
                System.out.println("验证结果：" + result);
                Assertions.assertThat(isMatch).isFalse();
            }
        }
    }

}
