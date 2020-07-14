package io.github.dunwu.javacore.crypto;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

/**
 * RSA安全编码：非对称加密算法。它既可以用来加密、解密，也可以用来做数字签名
 *
 * @author Zhang Peng
 * @since 2016年7月20日
 */
public class RSACoder {

    public final static String KEY_ALGORITHM = "RSA";

    public final static String SIGN_ALGORITHM = "MD5WithRSA";

    private KeyPair keyPair;

    public RSACoder() throws Exception {
        this.keyPair = initKeyPair();
    }

    private KeyPair initKeyPair() throws Exception {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        // 初始化密钥对生成器，密钥大小为1024位
        keyPairGen.initialize(1024);
        // 生成一个密钥对
        return keyPairGen.genKeyPair();
    }

    public byte[] encryptByPrivateKey(byte[] plaintext, byte[] key) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(plaintext);
    }

    public byte[] decryptByPublicKey(byte[] ciphertext, byte[] key) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(ciphertext);
    }

    public byte[] encryptByPublicKey(byte[] plaintext, byte[] key) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(plaintext);
    }

    public byte[] decryptByPrivateKey(byte[] ciphertext, byte[] key) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(ciphertext);
    }

    public byte[] signature(byte[] data, byte[] privateKey, RsaSignTypeEn type) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey key = keyFactory.generatePrivate(keySpec);

        Signature signature = Signature.getInstance(type.name());
        signature.initSign(key);
        signature.update(data);
        return signature.sign();
    }

    public byte[] getPrivateKey() {
        return keyPair.getPrivate().getEncoded();
    }

    public boolean verify(byte[] data, byte[] publicKey, byte[] sign, RsaSignTypeEn type) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey key = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance(type.name());
        signature.initVerify(key);
        signature.update(data);
        return signature.verify(sign);
    }

    public byte[] getPublicKey() {
        return keyPair.getPublic().getEncoded();
    }

    public enum RsaSignTypeEn {

        MD2WithRSA,
        MD5WithRSA,
        SHA1WithRSA
    }

    public static void main(String[] args) throws Exception {
        String msg = "Hello World!";
        RSACoder coder = new RSACoder();
        // 私钥加密，公钥解密
        byte[] ciphertext = coder.encryptByPrivateKey(msg.getBytes(StandardCharsets.UTF_8), coder.keyPair.getPrivate().getEncoded());
        byte[] plaintext = coder.decryptByPublicKey(ciphertext, coder.keyPair.getPublic().getEncoded());

        // 公钥加密，私钥解密
        byte[] ciphertext2 = coder.encryptByPublicKey(msg.getBytes(), coder.keyPair.getPublic().getEncoded());
        byte[] plaintext2 = coder.decryptByPrivateKey(ciphertext2, coder.keyPair.getPrivate().getEncoded());

        byte[] sign = coder.signature(msg.getBytes(), coder.getPrivateKey(), RsaSignTypeEn.SHA1WithRSA);
        boolean flag = coder.verify(msg.getBytes(), coder.getPublicKey(), sign, RsaSignTypeEn.SHA1WithRSA);
        String result = flag ? "数字签名匹配" : "数字签名不匹配";

        System.out.println("原文：" + msg);
        System.out.println("公钥：" + Base64.getUrlEncoder().encodeToString(coder.keyPair.getPublic().getEncoded()));
        System.out.println("私钥：" + Base64.getUrlEncoder().encodeToString(coder.keyPair.getPrivate().getEncoded()));

        System.out.println("============== 私钥加密，公钥解密 ==============");
        System.out.println("密文：" + Base64.getUrlEncoder().encodeToString(ciphertext));
        System.out.println("明文：" + new String(plaintext));

        System.out.println("============== 公钥加密，私钥解密 ==============");
        System.out.println("密文：" + Base64.getUrlEncoder().encodeToString(ciphertext2));
        System.out.println("明文：" + new String(plaintext2));

        System.out.println("============== 数字签名 ==============");
        System.out.println("数字签名：" + Base64.getUrlEncoder().encodeToString(sign));
        System.out.println("验证结果：" + result);
    }

}
