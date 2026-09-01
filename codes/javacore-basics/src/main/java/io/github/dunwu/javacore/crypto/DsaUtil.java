package io.github.dunwu.javacore.crypto;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 数字签名算法(Digital Signature Algorithm, DSA)
 * <p>
 * DSA 是一种数字签名算法。 DSA仅支持SHA系列算法，而 JDK 仅支持 SHA1withDSA
 *
 * @author Zhang Peng
 * @since 2016年7月21日
 */
public class DsaUtil {

    public static final String DSA = "DSA";
    private byte[] publicKeyBytes;
    private byte[] privateKeyBytes;

    public byte[] getPublicKeyBytes() {
        return this.publicKeyBytes;
    }

    public String getBase64PublicKey() {
        return Base64.getEncoder().encodeToString(this.publicKeyBytes);
    }

    public byte[] getPrivateKeyBytes() {
        return this.privateKeyBytes;
    }

    public String getBase64PrivateKey() {
        return Base64.getEncoder().encodeToString(this.privateKeyBytes);
    }

    public byte[] sign(DsaAlgorithmEnum algorithm, byte[] data) throws GeneralSecurityException {
        return sign(algorithm, data, getPrivateKeyBytes());
    }

    public boolean verify(DsaAlgorithmEnum algorithm, byte[] data, byte[] sign) throws GeneralSecurityException {
        return verify(algorithm, data, sign, getPublicKeyBytes());
    }

    public static DsaUtil newInstance() throws NoSuchAlgorithmException {
        KeyPair keyPair = generateKeyPair();
        DsaUtil util = new DsaUtil();
        util.publicKeyBytes = keyPair.getPublic().getEncoded();
        util.privateKeyBytes = keyPair.getPrivate().getEncoded();
        return util;
    }

    public static DsaUtil newInstance(byte[] publicKeyBytes, byte[] privateKeyBytes) throws GeneralSecurityException {
        if (publicKeyBytes == null || publicKeyBytes.length == 0) {
            throw new GeneralSecurityException("dsa public key is not valid!");
        }
        if (privateKeyBytes == null || privateKeyBytes.length == 0) {
            throw new GeneralSecurityException("dsa private key is not valid!");
        }
        DsaUtil util = new DsaUtil();
        util.publicKeyBytes = publicKeyBytes;
        util.privateKeyBytes = privateKeyBytes;
        return util;
    }

    public static byte[] sign(DsaAlgorithmEnum algorithm, byte[] data, byte[] privateKey)
        throws GeneralSecurityException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(DSA);
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance(algorithm.name());
        signature.initSign(key);
        signature.update(data);
        return signature.sign();
    }

    public static boolean verify(DsaAlgorithmEnum algorithm, byte[] data, byte[] sign, byte[] publicKey)
        throws GeneralSecurityException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(DSA);
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(algorithm.name());
        signature.initVerify(key);
        signature.update(data);
        return signature.verify(sign);
    }

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(DSA);
        keyPairGenerator.initialize(1024);
        return keyPairGenerator.genKeyPair();
    }

    public enum DsaAlgorithmEnum {
        RawDSA,
        SHA1withDSA
    }

    private DsaUtil() { }

}
