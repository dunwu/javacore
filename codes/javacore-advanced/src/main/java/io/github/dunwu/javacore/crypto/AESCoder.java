package io.github.dunwu.javacore.crypto;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

/**
 * AES安全编码：对称加密算法。DES的替代方案。
 *
 * @author Zhang Peng
 * @since 2016年7月14日
 */
public class AESCoder {

    public static final String KEY_ALGORITHM_AES = "AES";

    public static final String CIPHER_AES_DEFAULT = "AES";

    public static final String CIPHER_AES_ECB_PKCS5PADDING = "AES/ECB/PKCS5Padding"; // 算法/模式/补码方式

    public static final String CIPHER_AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";

    public static final String CIPHER_AES_CBC_NOPADDING = "AES/CBC/NoPadding";

    private static final String SEED = "%%%today is nice***"; // 用于生成随机数的种子

    private Key key;

    private Cipher cipher;

    private String transformation;

    public AESCoder() throws NoSuchAlgorithmException, NoSuchPaddingException {
        this.key = initKey();
        this.cipher = Cipher.getInstance(CIPHER_AES_DEFAULT);
        this.transformation = CIPHER_AES_DEFAULT;
    }

    /**
     * 根据随机数种子生成一个密钥
     *
     * @return Key
     * @throws NoSuchAlgorithmException
     * @author Zhang Peng
     * @since 2016年7月14日
     */
    private Key initKey() throws NoSuchAlgorithmException {
        // 根据种子生成一个安全的随机数
        SecureRandom secureRandom = null;
        secureRandom = new SecureRandom(SEED.getBytes());

        KeyGenerator keyGen = KeyGenerator.getInstance(KEY_ALGORITHM_AES);
        keyGen.init(secureRandom);
        return keyGen.generateKey();
    }

    public AESCoder(String transformation) throws NoSuchAlgorithmException, NoSuchPaddingException {
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
        if (transformation.equals(CIPHER_AES_CBC_PKCS5PADDING) || transformation.equals(CIPHER_AES_CBC_NOPADDING)) {
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
        if (transformation.equals(CIPHER_AES_CBC_PKCS5PADDING) || transformation.equals(CIPHER_AES_CBC_NOPADDING)) {
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(getIV()));
        } else {
            cipher.init(Cipher.DECRYPT_MODE, key);
        }
        return cipher.doFinal(input);
    }

    private byte[] getIV() {
        String iv = "0123456789ABCDEF"; // IV length: must be 16 bytes long
        return iv.getBytes();
    }

    public static void main(String[] args) throws Exception {
        AESCoder aes = new AESCoder(CIPHER_AES_CBC_PKCS5PADDING);

        String msg = "Hello World!";
        System.out.println("[AES加密、解密]");
        System.out.println("message: " + msg);
        byte[] encoded = aes.encrypt(msg.getBytes(StandardCharsets.UTF_8));
        String encodedBase64 = Base64.getUrlEncoder().encodeToString(encoded);
        System.out.println("encoded: " + encodedBase64);

        byte[] decodedBase64 = Base64.getDecoder().decode(encodedBase64);
        byte[] decoded = aes.decrypt(decodedBase64);
        System.out.println("decoded: " + new String(decoded));
    }

}
