package io.github.dunwu.javacore.crypto;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 数字签名算法(Digital Signature Algorithm, DSA)工具类。 DSA是一种数字签名算法。 DSA仅支持SHA系列算法，而JDK仅支持SHA1withDSA。
 *
 * @author Zhang Peng
 * @since 2016年7月21日
 */
public class DsaCoder {

    public static final String KEY_ALGORITHM = "DSA";

    public static final String SIGN_ALGORITHM = "SHA1withDSA";

    /**
     * DSA密钥长度默认1024位。 密钥长度必须是64的整数倍，范围在512~1024之间
     */
    private static final int KEY_SIZE = 1024;

    private KeyPair keyPair;

    public DsaCoder() throws Exception {
        this.keyPair = initKey();
    }

    private KeyPair initKey() throws Exception {
        // 初始化密钥对生成器
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(DsaCoder.KEY_ALGORITHM);
        // 实例化密钥对生成器
        keyPairGen.initialize(KEY_SIZE);
        // 实例化密钥对
        return keyPairGen.genKeyPair();
    }

    public byte[] signature(byte[] data, byte[] privateKey) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey key = keyFactory.generatePrivate(keySpec);

        Signature signature = Signature.getInstance(SIGN_ALGORITHM);
        signature.initSign(key);
        signature.update(data);
        return signature.sign();
    }

    public byte[] getPrivateKey() {
        return keyPair.getPrivate().getEncoded();
    }

    public boolean verify(byte[] data, byte[] publicKey, byte[] sign) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey key = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance(SIGN_ALGORITHM);
        signature.initVerify(key);
        signature.update(data);
        return signature.verify(sign);
    }

    public byte[] getPublicKey() {
        return keyPair.getPublic().getEncoded();
    }

    public static void main(String[] args) throws Exception {
        String msg = "Hello World";
        DsaCoder dsa = new DsaCoder();
        byte[] sign = dsa.signature(msg.getBytes(), dsa.getPrivateKey());
        boolean flag = dsa.verify(msg.getBytes(), dsa.getPublicKey(), sign);
        String result = flag ? "数字签名匹配" : "数字签名不匹配";
        System.out.println("数字签名：" + Base64.getUrlEncoder().encodeToString(sign));
        System.out.println("验证结果：" + result);
    }

}
