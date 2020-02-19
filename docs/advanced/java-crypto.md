# Java 编码和加密

> **📦 本文以及示例源码已归档在 [javacore](https://github.com/dunwu/javacore/)**

## 术语

- **明文(Plaintext)**：指待加密信息。明文可以是文本文件、图片文件、二进制数据等。
- **密文(Ciphertext)**：指经过加密后的明文。密文通常以文本、二进制等形式存在。
- **加密(Encryption)**：指将明文转换为密文的过程。
- **解密(Decryption)**：指将密文转换为明文的过程。
- **加密密钥(Encryption Key)**：指通过加密算法进行加密操作用的密钥。
- **解密密钥(Decryption Key)**：指通过解密算法进行解密操作用的密钥。
- **信道(Channel)**：通信的通道，是信号传输的媒介。

## Base64 编码

### 算法简述

#### 定义

Base64 内容传送编码是一种以任意 8 位字节序列组合的描述形式，这种形式不易被人直接识别。

Base64 是一种很常见的编码规范，其作用是将二进制序列转换为人类可读的 ASCII 字符序列，常用在需用通过文本协议（比如 HTTP 和 SMTP）来传输二进制数据的情况下。**Base64 并不是加密解密算法**，尽管我们有时也听到使用 Base64 来加密解密的说法，但这里所说的加密与解密实际是指**编码（encode）**和**解码（decode）**的过程，其变换是非常简单的，仅仅能够避免信息被直接识别。

#### 原理

Base64 算法主要是将给定的字符以字符编码(如 ASCII 码，UTF-8 码)对应的十进制数为基准，做编码操作：

1.  将给定的字符串以字符为单位，转换为对应的字符编码。
2.  将获得字符编码转换为二进制
3.  对二进制码做分组转换，每 3 个字节为一组，转换为每 4 个 6 位二进制位一组（不足 6 位时低位补 0）。这是一个分组变化的过程，3 个 8 位二进制码和 4 个 6 位二进制码的长度都是 24 位（3*8 = 4*6 = 24）。
4.  对获得的 4-6 二进制码补位，向 6 位二进制码添加 2 位高位 0，组成 4 个 8 位二进制码。
5.  对获得的 4-8 二进制码转换为十进制码。
6.  将获得的十进制码转换为 Base64 字符表中对应的字符。

***Base64 编码表***

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

#### 应用

Base64 编码可用于在 HTTP 环境下传递较长的标识信息。在其他应用程序中，也常常需要把二进制数据编码为适合放在 URL(包括隐藏表单域)中的形式。此时，采用 Base64 编码具有不可读性，即所编码的数据不会被人用肉眼所直接看到，算是起到一个加密的作用。

然而，**标准的 Base64 并不适合直接放在 URL 里传输**，因为 URL 编码器会把标准 Base64 中的 `/` 和 `+` 字符变为形如 `%XX` 的形式，而这些 `%` 号在存入数据库时还需要再进行转换，因为 ANSI SQL 中已将 `%` 号用作通配符。

为解决此问题，可采用一种用于 URL 的改进 Base64 编码，它不仅在末尾填充 `=` 号，并将标准 Base64 中的“+”和“/”分别改成了 `-` 和 `_`，这样就免去了在 URL 编解码和数据库存储时所要作的转换，避免了编码信息长度在此过程中的增加，并统一了数据库、表单等处对象标识符的格式。

另有一种用于正则表达式的改进 Base64 变种，它将 `+` 和 `/` 改成了 `!` 和 `-`，因为 `+`, `*` 以及前面在 IRCu 中用到的 `[` 和 `]` 在正则表达式中都可能具有特殊含义。

### 算法实现

`commons-codec`开源包提供了对于 Base64 的实现，推荐使用。

请在 maven 工程中添加依赖：

```xml
<dependency>
  <groupId>commons-codec</groupId>
  <artifactId>commons-codec</artifactId>
  <version>1.10</version>
</dependency>
```

***范例***

注：在 commons-codec 包中的 Base64 这个类中提供了 Base64 的编码、解码方式。

其中，`encodeBase64`提供的是标准的 Base64 编码方式；`encodeBase64URLSafe`提供了 URL 安全的 Base64 编码方式（将+ 和 /替换为 - 和 \_）。

```java
package org.zp.javase.security.encrypt;

import org.apache.commons.codec.binary.Base64;
import java.io.UnsupportedEncodingException;

public class Base64Demo {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String url = "https://www.baidu.com/s?wd=Base64&rsv_spt=1&rsv_iqid=0xa9188d560005131f&issp=1&f=3&rsv_bp=0&rsv_idx=2&ie=utf-8&tn=baiduhome_pg&rsv_enter=1&rsv_sug3=1&rsv_sug1=1&rsv_sug7=001&rsv_sug2=1&rsp=0&rsv_sug9=es_2_1&rsv_sug4=2153&rsv_sug=9";
        // byte[] encoded = Base64.encodeBase64(url.getBytes("UTF8")); // 标准的Base64编码
        byte[] encoded = Base64.encodeBase64URLSafe(url.getBytes("UTF8")); // URL安全的Base64编码
        byte[] decoded = Base64.decodeBase64(encoded);
        System.out.println("url:" + url);
        System.out.println("encoded:" + new String(encoded));
        System.out.println("decoded:" + new String(decoded));
    }
}
```

## 对称加密

### 算法简述

对称加密算法是应用较早的加密算法，技术成熟。在对称加密算法中，数据发信方将明文（原始数据）和加密密钥（mi yao）一起经过特殊加密算法处理后，使其变成复杂的加密密文发送出去。收信方收到密文后，若想解读原文，则需要使用加密用过的密钥及相同算法的逆算法对密文进行解密，才能使其恢复成可读明文。在对称加密算法中，使用的密钥只有一个，发收信双方都使用这个密钥对数据进行加密和解密，这就要求解密方事先必须知道加密密钥。

#### 特点

##### 优点：

计算量小、加密速度快、加密效率高。

##### 缺点：

算法是公开的，安全性得不到保证。

通信双方每次使用对称加密算法时，都需要使用其他人不知道的惟一密钥，这会使得通信双方所拥有的密钥数量呈几何级数增长，密钥管理成为用户的负担。对称加密算法在分布式网络系统上使用较为困难，主要是因为密钥管理困难，使用成本较高。

而与公钥、密钥加密算法比起来，对称加密算法能够提供加密和认证却缺乏了签名功能，使得使用范围有所缩小。

#### 原理

对称加密要求加密与解密使用同一个密钥，解密是加密的逆运算。由于加密、解密使用同一个密钥，这要求通信双方必须在通信前商定该密钥，并妥善保存该密钥。

对称加密体制分为两种：

一种是对明文的单个位（或字节）进行运算，称为流密码，也称为序列密码；

一种是把明文信息划分为不同的组（或块）结构，分别对每个组（或块）进行加密、解密，称为分组密码。

![img](http://dunwu.test.upcdn.net/cs/java/advanced/symmetric-encryption.png!zp)

假设甲乙方作为通信双方。假定甲乙双方在消息传递前已商定加密算法，欲完成一次消息传递需要经过如下步骤。

![img](http://dunwu.test.upcdn.net/cs/java/advanced/symmetric-encryption-progress.png!zp)

#### 工作模式

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

#### 填充方法

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

#### 常用算法

对称加密算法主要有 DES、3DES（TripleDES）、AES、IDEA、RC2、RC4、RC5 和 Blowfish 等。

### 算法实现

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

##### 完整实例

一个完整的 DES 加密解密范例

```java
import org.bouncycastle.util.encoders.Base64;
import org.zp.javase.security.encode.Encode;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

/**
 * @Title DESCoder
 * @Description DES安全编码：是经典的对称加密算法。密钥仅56位，且迭代次数偏少。已被视为并不安全的加密算法。
 * @Author Victor Zhang
 * @Date 2016年7月14日
 */
public class DESCoder implements Encode {
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

    public DESCoder(String transformation)
            throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {
        this.key = initKey();
        this.cipher = Cipher.getInstance(transformation);
        this.transformation = transformation;
    }

    /**
     * @Title decrypt
     * @Description 解密
     * @Author Victor Zhang
     * @Date 2016年7月20日
     * @param input 密文
     * @return byte[] 明文
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidAlgorithmParameterException
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

    /**
     * @Title encrypt
     * @Description 加密
     * @Author Victor Zhang
     * @Date 2016年7月20日
     * @param input 明文
     * @return byte[] 密文
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidAlgorithmParameterException
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
     * @Title initKey
     * @Description 根据随机数种子生成一个密钥
     * @Author Victor Zhang
     * @Date 2016年7月14日
     * @Return Key
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    private Key initKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        // 根据种子生成一个安全的随机数
        SecureRandom secureRandom = null;
        secureRandom = new SecureRandom(SEED.getBytes());

        KeyGenerator keyGen = KeyGenerator.getInstance(KEY_ALGORITHM_DES);
        keyGen.init(secureRandom);
        return keyGen.generateKey();
    }

    private byte[] getIV() {
        String iv = "01234567"; // IV length: must be 8 bytes long
        return iv.getBytes();
    }

    public static void main(String[] args) throws Exception {
        DESCoder aes = new DESCoder(CIPHER_DES_CBC_PKCS5PADDING);

        String msg = "Hello World!";
        System.out.println("原文: " + msg);
        byte[] encoded = aes.encrypt(msg.getBytes("UTF8"));
        String encodedBase64 = Base64.toBase64String(encoded);
        System.out.println("密文: " + encodedBase64);

        byte[] decodedBase64 = Base64.decode(encodedBase64);
        byte[] decoded = aes.decrypt(decodedBase64);
        System.out.println("明文: " + new String(decoded));
    }
}
```

**输出**

```
原文: Hello World!
密文: TtnEu9ezNQtxFKpmq/37Qw==
明文: Hello World!
```

#### 基于口令加密的流程（PBE）

DES、DESede、AES、IDEA 这几种算法的应用模型几乎如出一辙。

但是，并非所有对称加密算法都是如此。

基于口令加密(Password Based Encryption, PBE)是一种基于口令加密的算法。其特点是：口令由用户自己掌管，采用随机数（这里叫做盐）杂凑多重加密等方法保证数据的安全性。

PBE 没有密钥概念，密钥在其他对称加密算法中是经过计算得出的，PBE 则使用口令替代了密钥。

流程：

![img](http://dunwu.test.upcdn.net/cs/java/advanced/password-based-encryption-progress.png!zp)

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

## 非对称加密

### 算法简述

非对称加密算法和对称加密算法的主要差别在于非对称加密算法用于加密和解密的密钥是不同的。一个公开，称为公钥（public key）；一个保密，称为私钥（private key）。因此，非对称加密算法也称为双钥加密算法或公钥加密算法。

#### 特点

##### 优点

非对称加密算法解决了对称加密算法的密钥分配问题，并极大地提高了算法安全性。

##### 缺点

算法比对称算法更复杂，因此加密、解密速度都比对称算法慢很多。

#### 原理

![img](http://dunwu.test.upcdn.net/cs/java/advanced/asymmetric-encryption.png!zp)

非对称加密算法实现机密信息交换的基本过程是：甲方生成一对密钥并将其中的一把作为公用密钥向其它方公开；得到该公用密钥的乙方使用该密钥对机密信息进行加密后再发送给甲方；甲方再用自己保存的另一把专用密钥对加密后的信息进行解密。

另一方面，甲方可以使用乙方的公钥对机密信息进行签名后再发送给乙方；乙方再用自己的私匙对数据进行验证。

甲方只能用其私钥解密，由其公钥加密后的任何信息。 非对称加密算法的保密性比较好，它消除了最终用户交换密钥的需要。

#### 常用算法

DH(Diffie-Hellman，密钥交换算法)、RSA

### 算法实现

#### 完整范例

```java
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSACoder {
    private final static String KEY_ALGORITHM = "RSA";
    private KeyPair keyPair;

    public RSACoder() throws Exception {
        this.keyPair = initKeyPair();
    }

    public byte[] encryptByPublicKey(byte[] plaintext, byte[] key) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(plaintext);
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

    public byte[] decryptByPrivateKey(byte[] ciphertext, byte[] key) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(ciphertext);
    }

    private KeyPair initKeyPair() throws Exception {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        // 初始化密钥对生成器，密钥大小为1024位
        keyPairGen.initialize(1024);
        // 生成一个密钥对
        return keyPairGen.genKeyPair();
    }

    public static void main(String[] args) throws Exception {
        String msg = "Hello World!";
        RSACoder coder = new RSACoder();
        // 私钥加密，公钥解密
        byte[] ciphertext = coder.encryptByPrivateKey(msg.getBytes("UTF8"), coder.keyPair.getPrivate().getEncoded());
        byte[] plaintext = coder.decryptByPublicKey(ciphertext, coder.keyPair.getPublic().getEncoded());

        // 公钥加密，私钥解密
        byte[] ciphertext2 = coder.encryptByPublicKey(msg.getBytes(), coder.keyPair.getPublic().getEncoded());
        byte[] plaintext2 = coder.decryptByPrivateKey(ciphertext2, coder.keyPair.getPrivate().getEncoded());

        System.out.println("原文：" + msg);
        System.out.println("公钥：" + Base64.encodeBase64URLSafeString(coder.keyPair.getPublic().getEncoded()));
        System.out.println("私钥：" + Base64.encodeBase64URLSafeString(coder.keyPair.getPrivate().getEncoded()));

        System.out.println("============== 私钥加密，公钥解密 ==============");
        System.out.println("密文：" + Base64.encodeBase64URLSafeString(ciphertext));
        System.out.println("明文：" + new String(plaintext));

        System.out.println("============== 公钥加密，私钥解密 ==============");
        System.out.println("密文：" + Base64.encodeBase64URLSafeString(ciphertext2));
        System.out.println("明文：" + new String(plaintext2));
    }
}
```

**输出**

```
原文：Hello World!
公钥：MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCVN2mWAMdatpo2l8dwavaX2VC8mRleVTdjwjyahsyCE6UxkdqHsKD6Ecq3OBbuJhEfHxnr7MAD_zoE6zalFs7_si09XTgpVFsFCztPXJpPw-rpQdvaaxYEXJHkY07M_DBrxh1URg2gQl9dEDaruIFrZ12ugTwwEkLA1K_LN7yZrwIDAQAB
私钥：MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJU3aZYAx1q2mjaXx3Bq9pfZULyZGV5VN2PCPJqGzIITpTGR2oewoPoRyrc4Fu4mER8fGevswAP_OgTrNqUWzv-yLT1dOClUWwULO09cmk_D6ulB29prFgRckeRjTsz8MGvGHVRGDaBCX10QNqu4gWtnXa6BPDASQsDUr8s3vJmvAgMBAAECgYBvU1M8LcKOJFQzzNNoRPVLX0AEJXkuzwcvL1hFtbJYjc2eiQHwYFAJokKKpZc-ADqf7HVLdmvfz4h66P3w925hYHjSF3cs6jiibI7fc9lrdrJLMpv44phPlRCiIanD-U6pyN3bZxRl4Giuz5uGL0SVU6Dxh2Sw7mtnvUBbHCyyaQJBAOixpR-t81Qnpdy4jlbZL8ufTTF1TzlSh0NEDB4tlpHlVolVmZB2M-rdJ3nP8fJXazdsGZMP0q38vgiN2HHMtxsCQQCkKWAaA6KxKNbj0mJDSP1p4qUJ4EAcgXBz4B_PKMZa3ZU2CdmFlhlLRRTOIjZX2VC6IjHKWssa-3V2EqBzCSz9AkBsiK9kH1anofaTBOIfUB4i86KltvnE2uGMVmjwioL4eefrFqoR35BHs-99uag4SN4Rc7JaDb9Ot9xLUR3rtniRAkB8dFXEQx9Teie4HmaapjpgzQ_b9eQE-GjdoHvdHQeMGdMmXb9IVGwmsV-9ixhx73IROx1OURkMArmhYyu7KqitAkBkeQ-7AYOIROJnTUSQTMUELUmZFF1Io_SJGXyRYLgDqz7JCmmhfH7sNm8Gcn6f2VWg-U2D9-G5IHO-vHfz2DS6
============== 私钥加密，公钥解密 ==============
密文：U2otXypy1Fg4wcXK187xAuOxWM88oORVDJfaNxvG74Q_rqZ-sT4fEZYLZO80KmsWiufkJbD9Gskgkg7dRPRCwG90pRaU3PD9_sTmksN0v8MUwCX2p80zUeG3gWU6BJwMMUZrltJaHFbKn-BhzoNrn3Q-4BJA8lt6-cKtH0TPeN4
明文：Hello World!
============== 公钥加密，私钥解密 ==============
密文：O_rknvo12qaFfWieyTI_Ay8_ph49y3V4jJVs1BykpI81GM3ozCPSnOjHbtdWdjPtgJHFfCjbspAnIT2eM4PtJldIJg6k_2HZCmCCaheUj2pxcvkrhb6GdhSlH-K2FhFGAnlxUAp-3tZpYpxzAteEw1-suldelHdikrCV_uXxAEM
明文：Hello World!
```

## 消息摘要

### 算法简述

#### 定义

它是一个唯一对应一个消息或文本的固定长度的值，它由一个单向 Hash 加密函数对消息进行作用而产生。如果消息在途中改变了，则接收者通过对收到消息的新产生的摘要与原摘要比较，就可知道消息是否被改变了。因此消息摘要保证了消息的完整性。消息摘要采用单向 Hash 函数将需加密的明文"摘要"成一串密文，这一串密文亦称为数字指纹(Finger Print)。它有固定的长度，且不同的明文摘要成密文，其结果总是不同的，而同样的明文其摘要必定一致。这样这串摘要便可成为验证明文是否是"真身"的"指纹"了。

#### 特点

消息摘要具有以下特点：

- 唯一性：数据只要有一点改变，那么再通过消息摘要算法得到的摘要也会发生变化。虽然理论上有可能会发生碰撞，但是概率极其低。
- 不可逆：消息摘要算法的密文无法被解密。
- 不需要密钥，可使用于分布式网络。
- 无论输入的明文有多长，计算出来的消息摘要的长度总是固定的。

#### 原理

消息摘要，其实就是将需要摘要的数据作为参数，经过哈希函数(Hash)的计算，得到的散列值。

#### 常用算法

消息摘要算法包括**MD(Message Digest，消息摘要算法)**、**SHA(Secure Hash Algorithm，安全散列算法)**、**MAC(Message AuthenticationCode，消息认证码算法)**共 3 大系列，常用于验证数据的完整性，是数字签名算法的核心算法。

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

![img](http://dunwu.test.upcdn.net/cs/java/advanced/java-message-digest-process.jpg!zp)

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

## 参考资料

- 《Core Java Volume2》
- 《Java 加密与解密技术》
