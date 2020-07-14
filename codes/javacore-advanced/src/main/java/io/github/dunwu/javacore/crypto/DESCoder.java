package io.github.dunwu.javacore.crypto;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

/**
 * DES安全编码：是经典的对称加密算法。密钥仅56位，且迭代次数偏少。已被视为并不安全的加密算法。
 *
 * @author Zhang Peng
 * @since 2016年7月14日
 */
public class DESCoder {

    public static final String KEY_ALGORITHM_DES = "DES";

    public static final String CIPHER_DES_DEFAULT = "DES";

    public static final String CIPHER_DES_ECB_PKCS5PADDING = "DES/ECB/PKCS5Padding"; // 算法/模式/补码方式

    public static final String CIPHER_DES_CBC_PKCS5PADDING = "DES/CBC/PKCS5Padding";

    public static final String CIPHER_DES_CBC_NOPADDING = "DES/CBC/NoPadding";

    private static final String SEED = "%%%today is nice***"; // 用于生成随机数的种子

    private Key key;

    private Cipher cipher;

    private String transformation;

    public DESCoder() throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {
        this.key = initKey();
        this.cipher = Cipher.getInstance(CIPHER_DES_DEFAULT);
        this.transformation = CIPHER_DES_DEFAULT;
    }

    /**
     * 根据随机数种子生成一个密钥
     *
     * @return Key
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @author Zhang Peng
     * @since 2016年7月14日
     */
    private Key initKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        // 根据种子生成一个安全的随机数
        SecureRandom secureRandom = null;
        secureRandom = new SecureRandom(SEED.getBytes());

        KeyGenerator keyGen = KeyGenerator.getInstance(KEY_ALGORITHM_DES);
        keyGen.init(secureRandom);
        return keyGen.generateKey();
    }

    public DESCoder(String transformation)
        throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {
        this.key = initKey();
        this.cipher = Cipher.getInstance(transformation);
        this.transformation = transformation;
    }

    /**
     * 加密
     *
     * @param input 明文
     * @return byte[] 密文
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidAlgorithmParameterException
     * @author Zhang Peng
     * @since 2016年7月20日
     */
    public byte[] encrypt(byte[] input) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
        InvalidAlgorithmParameterException {
        if (transformation.equals(CIPHER_DES_CBC_PKCS5PADDING) || transformation.equals(CIPHER_DES_CBC_NOPADDING)) {
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(getIV()));
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, key);
        }
        return cipher.doFinal(input);
    }

    /**
     * 解密
     *
     * @param input 密文
     * @return byte[] 明文
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidAlgorithmParameterException
     * @author Zhang Peng
     * @since 2016年7月20日
     */
    public byte[] decrypt(byte[] input) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
        InvalidAlgorithmParameterException {
        if (transformation.equals(CIPHER_DES_CBC_PKCS5PADDING) || transformation.equals(CIPHER_DES_CBC_NOPADDING)) {
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(getIV()));
        } else {
            cipher.init(Cipher.DECRYPT_MODE, key);
        }
        return cipher.doFinal(input);
    }

    private byte[] getIV() {
        String iv = "01234567"; // IV length: must be 8 bytes long
        return iv.getBytes();
    }

    public static void main(String[] args) throws Exception {
        DESCoder aes = new DESCoder(CIPHER_DES_CBC_PKCS5PADDING);

        String msg = "Hello World!";
        System.out.println("原文: " + msg);
        byte[] encoded = aes.encrypt(msg.getBytes(StandardCharsets.UTF_8));
        String encodedBase64 = Base64.getUrlEncoder().encodeToString(encoded);
        System.out.println("密文: " + encodedBase64);

        byte[] decodedBase64 = Base64.getUrlDecoder().decode(encodedBase64);
        byte[] decoded = aes.decrypt(decodedBase64);
        System.out.println("明文: " + new String(decoded));
    }

}
