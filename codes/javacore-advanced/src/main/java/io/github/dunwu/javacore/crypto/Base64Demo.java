package io.github.dunwu.javacore.crypto;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Base64编码、解码范例
 *
 * @author Zhang Peng
 * @since 2016年7月21日
 */
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
