package io.github.dunwu.javacore.crypto;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * 基于口令加密(Password Based Encryption, PBE)，是一种对称加密算法。 其特点是：口令由用户自己掌管，采用随机数（这里叫做盐）杂凑多重加密等方法保证数据的安全性。
 * PBE没有密钥概念，密钥在其他对称加密算法中是经过计算得出的，PBE则使用口令替代了密钥。
 *
 * @author Zhang Peng
 * @since 2016年7月20日
 */
public class PBECoder {

    public static final String KEY_ALGORITHM = "PBEWITHMD5andDES";

    public static final int ITERATION_COUNT = 100;

    private Key key;

    private byte[] salt;

    public PBECoder(String password) throws Exception {
        this.salt = initSalt();
        this.key = initKey(password);
    }

    private byte[] initSalt() {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.generateSeed(8); // 盐长度必须为8字节
    }

    private Key initKey(String password) throws Exception {
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generateSecret(keySpec);
    }

    public byte[] encrypt(byte[] plaintext) throws Exception {
        PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATION_COUNT);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        return cipher.doFinal(plaintext);
    }

    public byte[] decrypt(byte[] ciphertext) throws Exception {
        PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATION_COUNT);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        return cipher.doFinal(ciphertext);
    }

    public static void test1() throws Exception {

        // 产生盐
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = secureRandom.generateSeed(8); // 盐长度必须为8字节

        // 产生Key
        String password = "123456";
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        SecretKey secretKey = keyFactory.generateSecret(keySpec);

        PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATION_COUNT);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);

        byte[] plaintext = "Hello World".getBytes();
        byte[] ciphertext = cipher.doFinal(plaintext);
        new String(ciphertext);
    }

    public static void main(String[] args) throws Exception {
        PBECoder encode = new PBECoder("123456");
        String message = "Hello World!";
        byte[] ciphertext = encode.encrypt(message.getBytes());
        byte[] plaintext = encode.decrypt(ciphertext);

        System.out.println("原文：" + message);
        System.out.println("密文：" + Base64.getUrlEncoder().encodeToString(ciphertext));
        System.out.println("明文：" + new String(plaintext));
    }

}
