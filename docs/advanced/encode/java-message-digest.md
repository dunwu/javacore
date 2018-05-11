## 消息摘要

### 算法简述

#### 定义

它是一个唯一对应一个消息或文本的固定长度的值，它由一个单向 Hash 加密函数对消息进行作用而产生。如果消息在途中改变了，则接收者通过对收到消息的新产生的摘要与原摘要比较，就可知道消息是否被改变了。因此消息摘要保证了消息的完整性。消息摘要采用单向 Hash  函数将需加密的明文"摘要"成一串密文，这一串密文亦称为数字指纹(Finger Print)。它有固定的长度，且不同的明文摘要成密文，其结果总是不同的，而同样的明文其摘要必定一致。这样这串摘要便可成为验证明文是否是"真身"的"指纹"了。

#### 特点

消息摘要具有以下特点：

* 唯一性：数据只要有一点改变，那么再通过消息摘要算法得到的摘要也会发生变化。虽然理论上有可能会发生碰撞，但是概率极其低。
* 不可逆：消息摘要算法的密文无法被解密。
* 不需要密钥，可使用于分布式网络。
* 无论输入的明文有多长，计算出来的消息摘要的长度总是固定的。

#### 原理

消息摘要，其实就是将需要摘要的数据作为参数，经过哈希函数(Hash)的计算，得到的散列值。

#### 常用算法

消息摘要算法包括**MD(Message Digest，消息摘要算法)**、**SHA(Secure Hash Algorithm，安全散列算法)**、**MAC(Message AuthenticationCode，消息认证码算法)**共 3 大系列，常用于验证数据的完整性，是数字签名算法的核心算法。

**MD5**和**SHA1**分别是**MD**、**SHA**算法系列中最有代表性的算法。

如今，MD5 已被发现有许多漏洞，从而不再安全。SHA 算法比 MD 算法的摘要长度更长，也更加安全。

### 算法实现

#### MD5、SHA 的范例

JDK 中使用 MD5 和 SHA 这两种消息摘要的方式基本一致，步骤如下：

1.  初始化 MessageDigest 对象
2.  更新要计算的内容
3.  生成摘要

**范例**

```java
importjava.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class MsgDigestDemo{
    public static void main(String args[]) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String msg = "Hello World!";

        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
        // 更新要计算的内容
        md5Digest.update(msg.getBytes());
        // 完成哈希计算，得到摘要
        byte[] md5Encoded = md5Digest.digest();

        MessageDigest shaDigest = MessageDigest.getInstance("SHA");
        // 更新要计算的内容
        shaDigest.update(msg.getBytes());
        // 完成哈希计算，得到摘要
        byte[] shaEncoded = shaDigest.digest();

        System.out.println("原文: " + msg);
        System.out.println("MD5摘要: " + Base64.encodeBase64URLSafeString(md5Encoded));
        System.out.println("SHA摘要: " + Base64.encodeBase64URLSafeString(shaEncoded));
    }
}
```

**输出**

```
原文:Hello World!
MD5摘要: 7Qdih1MuhjZehB6Sv8UNjA
SHA摘要:Lve95gjOVATpfV8EL5X4nxwjKHE
```

#### HMAC 的范例

```java
importjavax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class HmacCoder{
    /**
     * JDK支持HmacMD5, HmacSHA1,HmacSHA256, HmacSHA384, HmacSHA512
     */
    public enum HmacTypeEn {
        HmacMD5, HmacSHA1, HmacSHA256, HmacSHA384, HmacSHA512;
    }

    public static byte[] encode(byte[] plaintext, byte[] secretKey, HmacTypeEn type) throwsException {
        SecretKeySpec keySpec = new SecretKeySpec(secretKey, type.name());
        Mac mac = Mac.getInstance(keySpec.getAlgorithm());
        mac.init(keySpec);
        return mac.doFinal(plaintext);
    }

    public static void main(String[] args) throws Exception {
        String msg = "Hello World!";
        byte[] secretKey = "Secret_Key".getBytes("UTF8");
        byte[] digest = HmacCoder.encode(msg.getBytes(), secretKey, HmacTypeEn.HmacSHA256);
        System.out.println("原文: " + msg);
        System.out.println("摘要: " + Base64.encodeBase64URLSafeString(digest));
    }
}
```

**输出**

```
原文:Hello World!
摘要: b8-eUifaOJ5OUFweOoq08HbGAMsIpC3Nt-Yv-S91Yr4
```

## 数字签名

### 算法简述

数字签名算法可以看做是一种带有密钥的消息摘要算法，并且这种密钥包含了公钥和私钥。也就是说，数字签名算法是非对称加密算法和消息摘要算法的结合体。

#### 特点

数字签名算法要求能够验证数据完整性、认证数据来源，并起到抗否认的作用。

#### 原理

数字签名算法包含签名和验证两项操作，遵循私钥签名，公钥验证的方式。

签名时要使用私钥和待签名数据，验证时则需要公钥、签名值和待签名数据，其核心算法主要是消息摘要算法。

![Java实现消息摘要与数字签名图](http://oyz7npk35.bkt.clouddn.com//image/javase/encode/java-message-digest-process.jpg)

#### 常用算法

RSA、DSA、ECDSA

### 算法实现

#### DSA 的范例

数字签名有两个流程：签名和验证。

它们的前提都是要有一个公钥、密钥对。

**签名**

用私钥为消息计算签名

**范例**

用公钥验证摘要

```java
importjava.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;

public class DsaCoder{
    public static final String KEY_ALGORITHM = "DSA";

    public enum DsaTypeEn {
        MD5withDSA, SHA1withDSA
    }

    /**
     * DSA密钥长度默认1024位。 密钥长度必须是64的整数倍，范围在512~1024之间
     */
    private static final int KEY_SIZE = 1024;

    private KeyPair keyPair;

    public DsaCoder() throws Exception {
        keyPair = initKey();
    }

    public byte[] signature(byte[] data, byte[] privateKey) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey key =keyFactory.generatePrivate(keySpec);

        Signature signature = Signature.getInstance(DsaTypeEn.SHA1withDSA.name());
        signature.initSign(key);
        signature.update(data);
        return signature.sign();
    }

    public boolean verify(byte[] data, byte[] publicKey, byte[] sign) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey key =keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance(DsaTypeEn.SHA1withDSA.name());
        signature.initVerify(key);
        signature.update(data);
        return signature.verify(sign);
    }

    private KeyPair initKey() throws Exception {
        // 初始化密钥对生成器
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        // 实例化密钥对生成器
        keyPairGen.initialize(KEY_SIZE);
        // 实例化密钥对
        return keyPairGen.genKeyPair();
    }

    public byte[] getPublicKey() {
        return keyPair.getPublic().getEncoded();
    }

    public byte[] getPrivateKey() {
        return keyPair.getPrivate().getEncoded();
    }

    public static void main(String[] args) throws Exception {
        String msg = "Hello World";
        DsaCoder dsa = new DsaCoder();
        byte[] sign = dsa.signature(msg.getBytes(), dsa.getPrivateKey());
        boolean flag = dsa.verify(msg.getBytes(), dsa.getPublicKey(), sign);
        String result = flag ? "数字签名匹配" : "数字签名不匹配";
        System.out.println("数字签名：" + Base64.encodeBase64URLSafeString(sign));
        System.out.println("验证结果：" + result);
    }
}
```

## 参考

《Core Java Volume2》

《Java 加密与解密技术》
