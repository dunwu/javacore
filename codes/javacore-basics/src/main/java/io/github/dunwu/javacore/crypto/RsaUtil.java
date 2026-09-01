package io.github.dunwu.javacore.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

/**
 * 非对称加密算法 RSA
 * <p>
 * RSA 既可以用来加密、解密，也可以用来做数字签名
 *
 * @author Zhang Peng
 * @since 2016年7月20日
 */
public class RsaUtil {

    public static final String RSA = "RSA";
    private static final Provider BC = new BouncyCastleProvider();
    private byte[] publicKeyBytes;
    private byte[] privateKeyBytes;

    public byte[] getPublicKeyBytes() {
        return this.publicKeyBytes;
    }

    public String getBase64PublicKey() {
        return Base64.getEncoder().encodeToString(this.publicKeyBytes);
    }

    public String getPemPublicKey() throws IOException {
        PemObject pem = new PemObject("PUBLIC KEY", this.publicKeyBytes);
        StringWriter str = new StringWriter();
        PemWriter pemWriter = new PemWriter(str);
        pemWriter.writeObject(pem);
        pemWriter.close();
        str.close();
        return str.toString();
    }

    public byte[] getPrivateKeyBytes() {
        return this.privateKeyBytes;
    }

    public String getBase64PrivateKey() {
        return Base64.getEncoder().encodeToString(this.privateKeyBytes);
    }

    public String getPemPrivateKey() throws IOException {
        PemObject pem = new PemObject("PRIVATE KEY", this.privateKeyBytes);
        StringWriter str = new StringWriter();
        PemWriter pemWriter = new PemWriter(str);
        pemWriter.writeObject(pem);
        pemWriter.close();
        str.close();
        return str.toString();
    }

    public byte[] encryptByPrivateKey(byte[] plaintext) throws GeneralSecurityException {
        return encryptByPrivateKey(plaintext, getPrivateKeyBytes());
    }

    public byte[] decryptByPublicKey(byte[] ciphertext) throws GeneralSecurityException {
        return decryptByPublicKey(ciphertext, getPublicKeyBytes());
    }

    public byte[] encryptByPublicKey(byte[] plaintext) throws GeneralSecurityException {
        return encryptByPublicKey(plaintext, getPublicKeyBytes());
    }

    public byte[] decryptByPrivateKey(byte[] ciphertext) throws GeneralSecurityException {
        return decryptByPrivateKey(ciphertext, getPrivateKeyBytes());
    }

    public byte[] sign(RsaAlgorithmEnum algorithm, byte[] data) throws GeneralSecurityException {
        return sign(algorithm, data, getPrivateKeyBytes());
    }

    public boolean verify(RsaAlgorithmEnum algorithm, byte[] data, byte[] sign) throws GeneralSecurityException {
        return verify(algorithm, data, sign, getPublicKeyBytes());
    }

    public static RsaUtil newInstance() throws NoSuchAlgorithmException {
        return newInstance(ProviderType.DEFAULT);
    }

    public static RsaUtil newInstance(ProviderType provider) throws NoSuchAlgorithmException {
        KeyPair keyPair = generateKeyPair(provider);
        RsaUtil util = new RsaUtil();
        util.publicKeyBytes = keyPair.getPublic().getEncoded();
        util.privateKeyBytes = keyPair.getPrivate().getEncoded();
        return util;
    }

    public static RsaUtil newInstance(byte[] publicKeyBytes, byte[] privateKeyBytes) throws GeneralSecurityException {
        if (publicKeyBytes == null || publicKeyBytes.length == 0) {
            throw new GeneralSecurityException("rsa public key is not valid!");
        }
        if (privateKeyBytes == null || privateKeyBytes.length == 0) {
            throw new GeneralSecurityException("rsa private key is not valid!");
        }
        RsaUtil util = new RsaUtil();
        util.publicKeyBytes = publicKeyBytes;
        util.privateKeyBytes = privateKeyBytes;
        return util;
    }

    public static byte[] encryptByPrivateKey(byte[] data, byte[] privateKey) throws GeneralSecurityException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public static byte[] decryptByPublicKey(byte[] data, byte[] publicKey) throws GeneralSecurityException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PublicKey key = keyFactory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) throws GeneralSecurityException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PublicKey key = keyFactory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public static byte[] decryptByPrivateKey(byte[] data, byte[] privateKey) throws GeneralSecurityException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public static byte[] sign(RsaAlgorithmEnum algorithm, byte[] data, byte[] privateKey)
        throws GeneralSecurityException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance(algorithm.name());
        signature.initSign(key);
        signature.update(data);
        return signature.sign();
    }

    public static boolean verify(RsaAlgorithmEnum algorithm, byte[] data, byte[] sign, byte[] publicKey)
        throws GeneralSecurityException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(algorithm.name());
        signature.initVerify(key);
        signature.update(data);
        return signature.verify(sign);
    }

    public static KeyPair generateKeyPair(ProviderType provider) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator;
        if (provider != null && provider == ProviderType.BC) {
            keyPairGenerator = KeyPairGenerator.getInstance(RSA);
        } else {
            keyPairGenerator = KeyPairGenerator.getInstance(RSA, BC);
        }
        keyPairGenerator.initialize(1024);
        return keyPairGenerator.generateKeyPair();
    }

    public enum RsaAlgorithmEnum {
        MD2WithRSA,
        MD5WithRSA,
        SHA1WithRSA,
        SHA256withRSA,
        SHA384withRSA,
        SHA512withRSA
    }

    public enum ProviderType {
        DEFAULT,
        BC
    }

    private RsaUtil() { }

}
