package io.github.dunwu.javacore.crypto;

import cn.hutool.core.util.StrUtil;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 对称加密算法。DES的替代方案。
 *
 * @author Zhang Peng
 * @since 2016年7月14日
 */
public class AesUtil {

    public static final String AES = "AES";
    public static final String DEFAULT_SEED = "%%%today is nice***";
    public static final String DEFAULT_IV = "2098432527847288";

    private byte[] keyBytes;
    private byte[] ivBytes;

    private AesAlgorithmEnum algorithm;

    public byte[] getKeyBytes() {
        return this.keyBytes;
    }

    public String getBase64Key() {
        return Base64.getEncoder().encodeToString(this.keyBytes);
    }

    public Key getKey() {
        return new SecretKeySpec(keyBytes, AES);
    }

    public byte[] encrypt(byte[] data) throws GeneralSecurityException {
        return encrypt(algorithm, data, keyBytes, this.ivBytes);
    }

    public byte[] decrypt(byte[] data) throws GeneralSecurityException {
        return decrypt(algorithm, data, keyBytes, this.ivBytes);
    }

    public static AesUtil newInstance(byte[] ivBytes, String seed) throws GeneralSecurityException {
        Key key = generateKey(seed.getBytes(StandardCharsets.UTF_8));
        AesUtil util = new AesUtil();
        util.algorithm = AesAlgorithmEnum.AES;
        util.keyBytes = key.getEncoded();
        util.ivBytes = ivBytes;
        return util;
    }

    public static AesUtil newInstance(AesAlgorithmEnum algorithm, byte[] ivBytes, String seed)
        throws GeneralSecurityException {
        Key key = generateKey(seed.getBytes(StandardCharsets.UTF_8));
        AesUtil util = new AesUtil();
        util.algorithm = algorithm;
        util.keyBytes = key.getEncoded();
        util.ivBytes = ivBytes;
        return util;
    }

    public static AesUtil newInstance(AesAlgorithmEnum algorithm, byte[] ivBytes, byte[] keyBytes) {
        AesUtil util = new AesUtil();
        util.algorithm = algorithm;
        util.keyBytes = keyBytes;
        util.ivBytes = ivBytes;
        return util;
    }

    public static byte[] encrypt(AesAlgorithmEnum algorithm, byte[] data, byte[] keyBytes, byte[] ivBytes)
        throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(algorithm.getCode());
        Key key = new SecretKeySpec(keyBytes, AES);
        switch (algorithm) {
            case AES_CBC_PKCS5PADDING:
            case AES_CBC_NOPADDING:
                cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivBytes));
                break;
            case AES:
            case AES_ECB_PKCS5PADDING:
            default:
                cipher.init(Cipher.ENCRYPT_MODE, key);
                break;
        }
        return cipher.doFinal(data);
    }

    public static byte[] decrypt(AesAlgorithmEnum algorithm, byte[] data, byte[] keyBytes, byte[] ivBytes)
        throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(algorithm.getCode());
        Key key = new SecretKeySpec(keyBytes, AES);
        switch (algorithm) {
            case AES_CBC_PKCS5PADDING:
            case AES_CBC_NOPADDING:
                cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivBytes));
                break;
            case AES:
            case AES_ECB_PKCS5PADDING:
            default:
                cipher.init(Cipher.DECRYPT_MODE, key);
                break;
        }
        return cipher.doFinal(data);
    }

    public static Key generateKey(byte[] seed) throws NoSuchAlgorithmException {
        // 根据种子生成一个安全的随机数
        SecureRandom secureRandom = new SecureRandom(seed);
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        keyGenerator.init(secureRandom);
        return keyGenerator.generateKey();
    }

    public enum AesAlgorithmEnum {

        AES("AES"),

        AES_ECB_PKCS5PADDING("AES/ECB/PKCS5Padding"),

        AES_CBC_PKCS5PADDING("AES/CBC/PKCS5Padding"),

        AES_CBC_NOPADDING("AES/CBC/NoPadding");

        private final String code;

        AesAlgorithmEnum(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public static AesAlgorithmEnum getEnumByCode(String algorithm) {
            if (StrUtil.isBlank(algorithm)) {
                return null;
            }
            for (AesAlgorithmEnum type : AesAlgorithmEnum.values()) {
                if (type.getCode().equals(algorithm)) {
                    return type;
                }
            }
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        AesUtil aes = AesUtil.newInstance(AesAlgorithmEnum.AES_ECB_PKCS5PADDING, DEFAULT_IV.getBytes(), DEFAULT_SEED);

        String msg = "Hello World!";
        System.out.println("[AES加密、解密]");
        System.out.println("message: " + msg);
        byte[] encoded = aes.encrypt(msg.getBytes(StandardCharsets.UTF_8));
        String encodedBase64 = Base64.getUrlEncoder().encodeToString(encoded);
        System.out.println("encoded: " + encodedBase64);

        byte[] decodedBase64 = Base64.getUrlDecoder().decode(encodedBase64);
        byte[] decoded = aes.decrypt(decodedBase64);
        System.out.println("decoded: " + new String(decoded));
    }

}
