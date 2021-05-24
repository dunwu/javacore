# Java 编码和加密

> **📦 本文以及示例源码已归档在 [javacore](https://github.com/dunwu/javacore/)**
>
> 关键词：`Base64`、`消息摘要`、`数字签名`、`对称加密`、`非对称加密`、`MD5`、`SHA`、`HMAC`、`AES`、`DES`、`DESede`、`RSA`

<!-- TOC depthFrom:2 depthTo:3 -->

- [一、Base64 编码](#一base64-编码)
  - [Base64 原理](#base64-原理)
  - [Base64 应用](#base64-应用)
- [二、消息摘要](#二消息摘要)
  - [消息摘要概述](#消息摘要概述)
  - [消息摘要特点](#消息摘要特点)
  - [消息摘要常用算法](#消息摘要常用算法)
  - [消息摘要应用](#消息摘要应用)
- [三、数字签名](#三数字签名)
  - [数字签名概述](#数字签名概述)
  - [数字签名算法应用](#数字签名算法应用)
- [四、对称加密](#四对称加密)
  - [对称加密概述](#对称加密概述)
  - [对称加密应用](#对称加密应用)
- [五、非对称加密](#五非对称加密)
  - [非对称加密概述](#非对称加密概述)
  - [非对称加密算法应用](#非对称加密算法应用)
- [六、术语](#六术语)
- [参考资料](#参考资料)

<!-- /TOC -->

## 一、Base64 编码

### Base64 原理

Base64 内容传送编码是一种以任意 8 位字节序列组合的描述形式，这种形式不易被人直接识别。

Base64 是一种很常见的编码规范，其作用是将二进制序列转换为人类可读的 ASCII 字符序列，常用在需用通过文本协议（比如 HTTP 和 SMTP）来传输二进制数据的情况下。**Base64 并不是加密解密算法**，尽管我们有时也听到使用 Base64 来加密解密的说法，但这里所说的加密与解密实际是指**编码（encode）**和**解码（decode）**的过程，其变换是非常简单的，仅仅能够避免信息被直接识别。

Base64 算法主要是将给定的字符以字符编码(如 ASCII 码，UTF-8 码)对应的十进制数为基准，做编码操作：

1.  将给定的字符串以字符为单位，转换为对应的字符编码。
2.  将获得字符编码转换为二进制
3.  对二进制码做分组转换，每 3 个字节为一组，转换为每 4 个 6 位二进制位一组（不足 6 位时低位补 0）。这是一个分组变化的过程，3 个 8 位二进制码和 4 个 6 位二进制码的长度都是 24 位（3*8 = 4*6 = 24）。
4.  对获得的 4-6 二进制码补位，向 6 位二进制码添加 2 位高位 0，组成 4 个 8 位二进制码。
5.  对获得的 4-8 二进制码转换为十进制码。
6.  将获得的十进制码转换为 Base64 字符表中对应的字符。

**_Base64 编码表_**

| **索引** | **对应字符** | **索引** | **对应字符** | **索引** | **对应字符** | **索引** | **对应字符** |
| -------- | ------------ | -------- | ------------ | -------- | ------------ | -------- | ------------ |
| 0        | A            | 17       | R            | 34       | i            | 51       | z            |
| 1        | B            | 18       | S            | 35       | j            | 52       | 0            |
| 2        | C            | 19       | T            | 36       | k            | 53       | 1            |
| 3        | D            | 20       | U            | 37       | l            | 54       | 2            |
| 4        | E            | 21       | V            | 38       | m            | 55       | 3            |
| 5        | F            | 22       | W            | 39       | n            | 56       | 4            |
| 6        | G            | 23       | X            | 40       | o            | 57       | 5            |
| 7        | H            | 24       | Y            | 41       | p            | 58       | 6            |
| 8        | I            | 25       | Z            | 42       | q            | 59       | 7            |
| 9        | J            | 26       | a            | 43       | r            | 60       | 8            |
| 10       | K            | 27       | b            | 44       | s            | 61       | 9            |
| 11       | L            | 28       | c            | 45       | t            | 62       | +            |
| 12       | M            | 29       | d            | 46       | u            | 63       | /            |
| 13       | N            | 30       | e            | 47       | v            |          |              |
| 14       | O            | 31       | f            | 48       | w            |          |              |
| 15       | P            | 32       | g            | 49       | x            |          |              |
| 16       | Q            | 33       | h            | 50       | y            |          |              |

### Base64 应用

Base64 编码可用于在 HTTP 环境下传递较长的标识信息。在其他应用程序中，也常常需要把二进制数据编码为适合放在 URL(包括隐藏表单域)中的形式。此时，采用 Base64 编码具有不可读性，即所编码的数据不会被人用肉眼所直接看到，算是起到一个加密的作用。

然而，**标准的 Base64 并不适合直接放在 URL 里传输**，因为 URL 编码器会把标准 Base64 中的 `/` 和 `+` 字符变为形如 `%XX` 的形式，而这些 `%` 号在存入数据库时还需要再进行转换，因为 ANSI SQL 中已将 `%` 号用作通配符。

为解决此问题，可采用一种用于 URL 的改进 Base64 编码，它不仅在末尾填充 `=` 号，并将标准 Base64 中的“+”和“/”分别改成了 `-` 和 `_`，这样就免去了在 URL 编解码和数据库存储时所要作的转换，避免了编码信息长度在此过程中的增加，并统一了数据库、表单等处对象标识符的格式。

另有一种用于正则表达式的改进 Base64 变种，它将 `+` 和 `/` 改成了 `!` 和 `-`，因为 `+`, `*` 以及前面在 IRCu 中用到的 `[` 和 `]` 在正则表达式中都可能具有特殊含义。

【示例】`java.util.Base64` 编码、解码示例

`Base64.getEncoder()` 和 `Base64.getDecoder()` 提供了的是标准的 Base64 编码、解码方式；

`Base64.getUrlEncoder()` 和 `Base64.getUrlDecoder()` 提供了 URL 安全的 Base64 编码、解码方式（将 `+` 和 `/` 替换为 `-` 和 `_`）。

```java
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Demo {

    public static void main(String[] args) {
        String url = "https://www.baidu.com";
        System.out.println("url:" + url);
        // 标准的 Base64 编码、解码
        byte[] encoded = Base64.getEncoder().encode(url.getBytes(StandardCharsets.UTF_8));
        byte[] decoded = Base64.getDecoder().decode(encoded);
        System.out.println("Url Safe Base64 encoded:" + new String(encoded));
        System.out.println("Url Safe Base64 decoded:" + new String(decoded));
        // URL 安全的 Base64 编码、解码
        byte[] encoded2 = Base64.getUrlEncoder().encode(url.getBytes(StandardCharsets.UTF_8));
        byte[] decoded2 = Base64.getUrlDecoder().decode(encoded2);
        System.out.println("Base64 encoded:" + new String(encoded2));
        System.out.println("Base64 decoded:" + new String(decoded2));
    }

}
```

输出：

```
url:https://www.baidu.com
Url Safe Base64 encoded:aHR0cHM6Ly93d3cuYmFpZHUuY29t
Url Safe Base64 decoded:https://www.baidu.com
Base64 encoded:aHR0cHM6Ly93d3cuYmFpZHUuY29t
Base64 decoded:https://www.baidu.com
```

## 二、消息摘要

### 消息摘要概述

**消息摘要，其实就是将需要摘要的数据作为参数，经过哈希函数(Hash)的计算，得到的散列值**。

消息摘要是一个唯一对应一个消息或文本的固定长度的值，它由一个单向 Hash 加密函数对消息进行作用而产生。如果消息在途中改变了，则接收者通过对收到消息的新产生的摘要与原摘要比较，就可知道消息是否被改变了。因此消息摘要保证了消息的完整性。消息摘要采用单向 Hash 函数将需加密的明文"摘要"成一串密文，这一串密文亦称为数字指纹(Finger Print)。它有固定的长度，且不同的明文摘要成密文，其结果总是不同的，而同样的明文其摘要必定一致。这样这串摘要便可成为验证明文是否是"真身"的"指纹"了。

### 消息摘要特点

- 唯一性：数据只要有一点改变，那么再通过消息摘要算法得到的摘要也会发生变化。虽然理论上有可能会发生碰撞，但是概率极其低。
- 不可逆：消息摘要算法的密文无法被解密。
- 不需要密钥，可使用于分布式网络。
- 无论输入的明文有多长，计算出来的消息摘要的长度总是固定的。

### 消息摘要常用算法

消息摘要算法包括**MD(Message Digest，消息摘要算法)**、**SHA(Secure Hash Algorithm，安全散列算法)**、**MAC(Message AuthenticationCode，消息认证码算法)**共 3 大系列，常用于验证数据的完整性，是数字签名算法的核心算法。

**MD5**和**SHA1**分别是**MD**、**SHA**算法系列中最有代表性的算法。

如今，MD5 已被发现有许多漏洞，从而不再安全。SHA 算法比 MD 算法的摘要长度更长，也更加安全。

### 消息摘要应用

#### MD5、SHA 的范例

JDK 中使用 MD5 和 SHA 这两种消息摘要的方式基本一致，步骤如下：

1.  初始化 MessageDigest 对象
2.  更新要计算的内容
3.  生成摘要

```java
import java.security.MessageDigest;
import java.util.Base64;

public class MessageDigestDemo {

    public static byte[] encode(byte[] input, Type type) throws Exception {
        // 根据类型，初始化消息摘要对象
        MessageDigest md5Digest = MessageDigest.getInstance(type.getName());

        // 更新要计算的内容
        md5Digest.update(input);

        // 完成哈希计算，返回摘要
        return md5Digest.digest();
    }

    public static byte[] encodeWithBase64(byte[] input, Type type) throws Exception {
        return Base64.getUrlEncoder().encode(encode(input, type));
    }

    public static String encodeWithBase64String(byte[] input, Type type) throws Exception {
        return Base64.getUrlEncoder().encodeToString(encode(input, type));
    }

    public enum Type {
        MD2("MD2"),
        MD5("MD5"),
        SHA1("SHA1"),
        SHA256("SHA-256"),
        SHA384("SHA-384"),
        SHA512("SHA-512");

        private String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static void main(String[] args) throws Exception {
        String msg = "Hello World!";
        System.out.println("MD2: " + encodeWithBase64String(msg.getBytes(), Type.MD2));
        System.out.println("MD5: " + encodeWithBase64String(msg.getBytes(), Type.MD5));
        System.out.println("SHA1: " + encodeWithBase64String(msg.getBytes(), Type.SHA1));
        System.out.println("SHA256: " + encodeWithBase64String(msg.getBytes(), Type.SHA256));
        System.out.println("SHA384: " + encodeWithBase64String(msg.getBytes(), Type.SHA384));
        System.out.println("SHA512: " + encodeWithBase64String(msg.getBytes(), Type.SHA512));
    }

}
```

【输出】

```
MD2: MV98ZyI_Aft8q0uVEA6HLg==
MD5: 7Qdih1MuhjZehB6Sv8UNjA==
SHA1: Lve95gjOVATpfV8EL5X4nxwjKHE=
SHA256: f4OxZX_x_FO5LcGBSKHWXfwtSx-j1ncoSt3SABJtkGk=
SHA384: v9dsDrvQBv7lg0EFR8GIewKSvnbVgtlsJC0qeScj4_1v0GH51c_RO4-WE1jmrbpK
SHA512: hhhE1nBOhXP-w02WfiC8_vPUJM9IvgTm3AjyvVjHKXQzcQFerYkcw88cnTS0kmS1EHUbH_nlN5N7xGtdb_TsyA==
```

#### HMAC 的范例

```java

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacMessageDigest {

    public static void main(String[] args) throws Exception {
        String msg = "Hello World!";
        byte[] salt = "My Salt".getBytes(StandardCharsets.UTF_8);
        System.out.println("原文: " + msg);
        System.out.println("HmacMD5: " + encodeWithBase64String(msg.getBytes(), salt, HmacTypeEn.HmacMD5));
        System.out.println("HmacSHA1: " + encodeWithBase64String(msg.getBytes(), salt, HmacTypeEn.HmacSHA1));
        System.out.println("HmacSHA256: " + encodeWithBase64String(msg.getBytes(), salt, HmacTypeEn.HmacSHA256));
        System.out.println("HmacSHA384: " + encodeWithBase64String(msg.getBytes(), salt, HmacTypeEn.HmacSHA384));
        System.out.println("HmacSHA512: " + encodeWithBase64String(msg.getBytes(), salt, HmacTypeEn.HmacSHA512));
    }

    public static byte[] encode(byte[] plaintext, byte[] salt, HmacTypeEn type) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(salt, type.name());
        Mac mac = Mac.getInstance(keySpec.getAlgorithm());
        mac.init(keySpec);
        return mac.doFinal(plaintext);
    }

    public static byte[] encodeWithBase64(byte[] plaintext, byte[] salt, HmacTypeEn type) throws Exception {
        return Base64.getUrlEncoder().encode(encode(plaintext, salt, type));
    }

    public static String encodeWithBase64String(byte[] plaintext, byte[] salt, HmacTypeEn type) throws Exception {
        return Base64.getUrlEncoder().encodeToString(encode(plaintext, salt, type));
    }

    /**
     * JDK支持 HmacMD5, HmacSHA1, HmacSHA256, HmacSHA384, HmacSHA512
     */
    public enum HmacTypeEn {

        HmacMD5, HmacSHA1, HmacSHA256, HmacSHA384, HmacSHA512;
    }

}
```

**输出**

```
原文: Hello World!
HmacMD5: re6BLRsB1Q26SfJTwXZUSQ==
HmacSHA1: CFu8a9H6CbY9C5fo0OmJ2bnuILM=
HmacSHA256: Z1czUqDWWfYYl7qEDJ2sUH6iieHVI7o83dXMl0JYER0=
HmacSHA384: 34mKtRQBOYnwwznmQubjrDk_MsLDGqM2PmgcplZUpLsKNrG_cwfz4bLPJCbBW88b
HmacSHA512: 6n77htTZ_atc04-SsmxhSK3wzh1sAmdudCl0Cb_RZp4DpienG4LZkhXMbq8lcK7XSnz6my_wIpnStDp6PC_-5w==
```

## 三、数字签名

### 数字签名概述

数字签名算法可以看做是一种带有密钥的消息摘要算法，并且这种密钥包含了公钥和私钥。也就是说，**数字签名算法是非对称加密算法和消息摘要算法的结合体**。

数字签名算法要求能够验证数据完整性、认证数据来源，并起到抗否认的作用。

数字签名算法包含签名和验证两项操作，遵循私钥签名，公钥验证的方式。

签名时要使用私钥和待签名数据，验证时则需要公钥、签名值和待签名数据，其核心算法主要是消息摘要算法。

![img](https://raw.githubusercontent.com/dunwu/images/dev/cs/java/advanced/java-message-digest-process.jpg)

数字签名常用算法：**RSA**、**DSA**、**ECDSA**

### 数字签名算法应用

#### DSA 的范例

数字签名有两个流程：签名和验证。

它们的前提都是要有一个公钥、密钥对。

数字签名用私钥为消息计算签名。

【示例】用公钥验证摘要

```java
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
```

【输出】

```
数字签名：MCwCFDPUO_VrONl5ST0AWary-MLXJuSCAhRMeMnUVhpizfa2H2M37tne0pUtoA==
验证结果：数字签名匹配
```

## 四、对称加密

### 对称加密概述

对称加密算法主要有 DES、3DES（TripleDES）、AES、IDEA、RC2、RC4、RC5 和 Blowfish 等。

对称加密算法是应用较早的加密算法，技术成熟。在对称加密算法中，数据发信方将明文（原始数据）和加密密钥（mi yao）一起经过特殊加密算法处理后，使其变成复杂的加密密文发送出去。收信方收到密文后，若想解读原文，则需要使用加密用过的密钥及相同算法的逆算法对密文进行解密，才能使其恢复成可读明文。在对称加密算法中，使用的密钥只有一个，发收信双方都使用这个密钥对数据进行加密和解密，这就要求解密方事先必须知道加密密钥。

对称加密特点：

- 优点：计算量小、加密速度快、加密效率高。
- 缺点：算法是公开的，安全性得不到保证。

通信双方每次使用对称加密算法时，都需要使用其他人不知道的惟一密钥，这会使得通信双方所拥有的密钥数量呈几何级数增长，密钥管理成为用户的负担。对称加密算法在分布式网络系统上使用较为困难，主要是因为密钥管理困难，使用成本较高。

而与公钥、密钥加密算法比起来，对称加密算法能够提供加密和认证却缺乏了签名功能，使得使用范围有所缩小。

#### 对称加密原理

对称加密要求加密与解密使用同一个密钥，解密是加密的逆运算。由于加密、解密使用同一个密钥，这要求通信双方必须在通信前商定该密钥，并妥善保存该密钥。

对称加密体制分为两种：

一种是对明文的单个位（或字节）进行运算，称为流密码，也称为序列密码；

一种是把明文信息划分为不同的组（或块）结构，分别对每个组（或块）进行加密、解密，称为分组密码。

![img](https://raw.githubusercontent.com/dunwu/images/dev/cs/java/advanced/symmetric-encryption.png)

假设甲乙方作为通信双方。假定甲乙双方在消息传递前已商定加密算法，欲完成一次消息传递需要经过如下步骤。

![img](https://raw.githubusercontent.com/dunwu/images/dev/cs/java/advanced/symmetric-encryption-progress.png)

#### 对称加密工作模式

以 DES 算法的工作模式为例，DES 算法根据其加密算法所定义的明文分组的大小（56 位），将数据分割成若干 56 位的加密区块，再以加密区块为单位，分别进行加密处理。如果最后剩下不足一个区块的大小，称之为**短块**。短块的处理方法有填充法、流密码加密法、密文挪用技术。

根据数据加密时每个加密区块见得关联方式来区分，可以分为以下种工作模式：

**(1) 电子密码本模式(Electronic Code Book, ECB)**

用途：适合加密密钥，随机数等短数据。例如，安全地传递 DES 密钥，ECB 是最合适的模式。

**(2) 密文链接模式(Cipher Booki Chaining, CBC)**

用途：可加密任意长度的数据，适用于计算产生检测数据完整性的消息认证 MAC。

**(3) 密文反馈模式(Cipher Feed Back, CFB)**

用途：因错误传播无界，可以用于检查发现明文密文的篡改。

**(4) 输出反馈模式(Output Feed Back, OFB)**

用途：使用于加密冗余性较大的数据，比如语音和图像数据。

AES 算法除了以上 4 中模式外，还有一种新的工作模式：

**(5) 计数器模式(Counter, CTR)**

用途：适用于各种加密应用。

本文对于各种工作模式的原理展开描述。个人认为，作为工程应用，了解其用途即可。

#### 对称加密填充方法

Java 中对称加密对于短块的处理，一般是采用填充方式。

常采用的是：NoPadding（不填充）、Zeros 填充（0 填充）、PKCS5Padding 填充。

**ZerosPadding**

方式：全部填充为 0 的字节

结果如下：

F1 F2 F3 F4 F5 F6 F7 F8 //第一块

F9 00 00 00 00 00 00 00 //第二块

**PKCS5Padding**

方式：每个填充的字节都记录了填充的总字节数

结果如下：

F1 F2 F3 F4 F5 F6 F7 F8 //第一块

F9 07 07 07 07 07 07 07 //第二块

### 对称加密应用

#### 基于密钥加密的流程（DES、DESede、AES 和 IDEA）

DES、DESede、AES 和 IDEA 等算法都是基于密钥加密的对称加密算法，它们的实现流程也基本一致。步骤如下：

（1）生成密钥

```java
KeyGenerator kg = KeyGenerator.getInstance("DES");
SecureRandom random = new SecureRandom();
kg.init(random);
SecretKey secretKey = kg.generateKey();
```

建议使用随机数来初始化密钥的生成。

（2）初始化密码对象

```java
Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, secretKey);
```

`ENCRYPT_MODE`：加密模式

`DECRYPT_MODE`：解密模式

（3）执行

```
String plaintext = "Hello World";
byte[] ciphertext = cipher.doFinal(plaintext.getBytes());
```

一个完整的 DES 加密解密范例

```java
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
```

**输出**

```
原文: Hello World!
密文: TtnEu9ezNQtxFKpmq_37Qw==
明文: Hello World!
```

#### 基于口令加密的流程（PBE）

DES、DESede、AES、IDEA 这几种算法的应用模型几乎如出一辙。

但是，并非所有对称加密算法都是如此。

基于口令加密(Password Based Encryption, PBE)是一种基于口令加密的算法。其特点是：口令由用户自己掌管，采用随机数（这里叫做盐）杂凑多重加密等方法保证数据的安全性。

PBE 没有密钥概念，密钥在其他对称加密算法中是经过计算得出的，PBE 则使用口令替代了密钥。

流程：

![img](https://raw.githubusercontent.com/dunwu/images/dev/cs/java/advanced/password-based-encryption-progress.png)

步骤如下：

**（1）产生盐**

```java
SecureRandom secureRandom = new SecureRandom();
byte[] salt = secureRandom.generateSeed(8); // 盐长度必须为8字节
```

**（2）根据密码产生 Key**

```java
String password = "123456";
PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
SecretKey secretKey = keyFactory.generateSecret(keySpec);
```

**（3）初始化加密或解密对象**

```java
PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATION_COUNT);
Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
```

**（4）执行**

```java
byte[] plaintext = "Hello World".getBytes();
byte[] ciphertext = cipher.doFinal(plaintext);
```

（5）完整 PBE 示例

```java
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
```

## 五、非对称加密

### 非对称加密概述

非对称加密常用算法：DH(Diffie-Hellman，密钥交换算法)、RSA

非对称加密算法和对称加密算法的主要差别在于非对称加密算法用于加密和解密的密钥是不同的。一个公开，称为公钥（public key）；一个保密，称为私钥（private key）。因此，非对称加密算法也称为双钥加密算法或公钥加密算法。

非对称加密特点：

- 优点：非对称加密算法解决了对称加密算法的密钥分配问题，并极大地提高了算法安全性。
- 缺点：算法比对称算法更复杂，因此加密、解密速度都比对称算法慢很多。

![img](https://raw.githubusercontent.com/dunwu/images/dev/cs/java/advanced/asymmetric-encryption.png)

非对称加密算法实现机密信息交换的基本过程是：甲方生成一对密钥并将其中的一把作为公用密钥向其它方公开；得到该公用密钥的乙方使用该密钥对机密信息进行加密后再发送给甲方；甲方再用自己保存的另一把专用密钥对加密后的信息进行解密。

另一方面，甲方可以使用乙方的公钥对机密信息进行签名后再发送给乙方；乙方再用自己的私匙对数据进行验证。

甲方只能用其私钥解密，由其公钥加密后的任何信息。 非对称加密算法的保密性比较好，它消除了最终用户交换密钥的需要。

### 非对称加密算法应用

```java
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
```

**输出**

```
原文：Hello World!
公钥：MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCzPtRLErTUcYtr8GmIpvbso7FN18thuEq02U21mh7TA4FH4TjvNgOZrZEORYu94dxrPdnrPjh0p62P5pDIjx_dtGlZr0aGWgtTvBbPwAKE4keXyPqv4VV6iXRzyQ2HdOvFOovim5eu0Tu_TxGeNpFfp0pYj2LXCzpsgSrdUPuPmwIDAQAB
私钥：MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALM-1EsStNRxi2vwaYim9uyjsU3Xy2G4SrTZTbWaHtMDgUfhOO82A5mtkQ5Fi73h3Gs92es-OHSnrY_mkMiPH920aVmvRoZaC1O8Fs_AAoTiR5fI-q_hVXqJdHPJDYd068U6i-Kbl67RO79PEZ42kV-nSliPYtcLOmyBKt1Q-4-bAgMBAAECgYBJxOXiL8S0WjajKcKFNxIQuh3Sh6lwgkRcwcI1p0RgW-TtDEg-SuCYctJsKTsl3rq0eDQjmOvrNsc7ngygPidCiTdbD1H6m3tLrebBB-wZdXMSWPsHtQJsq4dE0e93mmfysciOP6QExOs0JqVjTyyBSK37LpUcLdalj2IJDtC0gQJBAPfMngZAuIPmXued7PUuWNBuwxnkmdMcs308eC_9vnLLXWhDB9xKMuXCMwqk16MJ6j1FQWtJu62T21yniWWQHIsCQQC5LWqKfRxVukgnBg0Pa95NVWWY01Yttnb125JsLxeKbR97KU4VgBaBcB9TyUdPr9lxAzGFg6Y3A1wfsfukaGsxAkEA1l719oLXHYSWZdmBvTozK14m-qeBS9lwjc9aSmpB8B1u2Vvj2Pd3wLyYW4Tv5-QT-J2JUr-e1TMseqOVgX-CsQJAETRoBq_zFv_0vjNwuTMTd2nsw5M3GY4vZU5eP1Dsxf63gxDmYVcCQEpzjqxPxNaYxEhArJ_7rHbSc1ts_ux4sQJBAIlbGQC4-92foXGzWT80rsqZlMQ8J8Nbjpoo7RUN9tgx60Vkr3xv26Vos77oqdufWlt5IiBZBS9acTA2suav6Qg=
============== 私钥加密，公钥解密 ==============
密文：qn6iGjSJV45EnH21RYRx2UZfMueqplbm1g3VIpBBQBuF63RdHdSgMJsVPAuB__V0rxpPlU3gR6qLyWu1mpaJ-ix_6KogAH64wqTWqPRh7E6aj767rybNpt9JyVlCmmpy9DiqHAUFWtBJDo34q-a7Fhq9c8bWrJ6jnn47IdmzHfU=
明文：Hello World!
============== 公钥加密，私钥解密 ==============
密文：fsz2IFs69d7JDrH-yoe5pi5WKQU1Zml7SDSpPqTZUn6muSCjNp6x312deQCXKMGSeAdMpVeb01yZBfa0MT_6eYJYVseU7Rd6bDf6YIg3AZFC41yh5ITiTvQ-XzxugnppS12sLpXSWg0faa5qjcVZnoTX9p7nHr8n20y4CNMI6Rw=
明文：Hello World!
============== 数字签名 ==============
数字签名：dTtUUlWX1wRQbW1PcA8O6WJcWcrHinEZRXwgLKEwBOm2DpvHnynvV_HYKS-qFE5_4vJQcPGJ2hZqWbfv1VKLHMUWuiXM7VJk70g3g7BF8i8RWbrCDOxgTR77jrEwidpr1PYJzWJVGq_HP36MxInGFLcVh2sN0fu8MppzsXUENZQ=
验证结果：数字签名匹配
```

## 六、术语

- **明文(Plaintext)**：指待加密信息。明文可以是文本文件、图片文件、二进制数据等。
- **密文(Ciphertext)**：指经过加密后的明文。密文通常以文本、二进制等形式存在。
- **加密(Encryption)**：指将明文转换为密文的过程。
- **解密(Decryption)**：指将密文转换为明文的过程。
- **加密密钥(Encryption Key)**：指通过加密算法进行加密操作用的密钥。
- **解密密钥(Decryption Key)**：指通过解密算法进行解密操作用的密钥。
- **信道(Channel)**：通信的通道，是信号传输的媒介。

## 参考资料

- [《Java 核心技术 卷 II 高级特性》](https://book.douban.com/subject/27165931/)
- [《Java 加密与解密的艺术》](https://book.douban.com/subject/25861566/)
