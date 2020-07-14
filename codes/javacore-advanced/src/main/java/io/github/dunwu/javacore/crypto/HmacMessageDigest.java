package io.github.dunwu.javacore.crypto;

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
