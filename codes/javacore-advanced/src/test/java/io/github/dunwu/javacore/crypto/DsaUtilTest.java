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
 * {@link DsaUtil} 测试
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @date 2024-03-05
 */
public class DsaUtilTest {

    private static DsaUtil dsa = null;

    @BeforeAll
    @DisplayName("初始化工具类")
    public static void init() throws Exception {
        dsa = DsaUtil.newInstance();
        System.out.printf("【公钥（Base64）】\n%s\n", dsa.getBase64PublicKey());
        System.out.printf("【私钥（Base64）】\n%s\n", dsa.getBase64PrivateKey());
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
                byte[] sign = dsa.sign(DsaUtil.DsaAlgorithmEnum.SHA1withDSA, bytes);
                boolean isMatch = dsa.verify(DsaUtil.DsaAlgorithmEnum.SHA1withDSA, bytes, sign);
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

            byte[] sign = dsa.sign(DsaUtil.DsaAlgorithmEnum.SHA1withDSA, bytes);
            boolean isMatch = dsa.verify(DsaUtil.DsaAlgorithmEnum.SHA1withDSA, modifyBytes, sign);
            String result = isMatch ? "数字签名匹配" : "数字签名不匹配";
            System.out.printf("============== %s 数字签名 ==============\n",
                DsaUtil.DsaAlgorithmEnum.SHA1withDSA.name());
            System.out.println("数字签名：" + Base64.getEncoder().encodeToString(sign));
            System.out.println("验证结果：" + result);
            Assertions.assertThat(isMatch).isFalse();
        }
    }

}
