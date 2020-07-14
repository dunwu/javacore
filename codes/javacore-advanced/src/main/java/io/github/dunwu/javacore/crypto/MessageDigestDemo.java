package io.github.dunwu.javacore.crypto;

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
