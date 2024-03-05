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
        String url = "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=2&tn=baiduhome_pg&wd=Base64&rsv_spt=1&oq=bouncycastle%2520%25E7%2594%259F%25E6%2588%2590%2520SHA256WithRSA&rsv_pq=dcbffa0c00285b6c&rsv_t=a1fd1gEVeKN5GGFcmeUPHEtDOzJ8t1sKdazPjgcuLs40XkmecFTDOrKCLBraRZGrKcj5&rqlang=cn&rsv_enter=0&rsv_dl=tb&rsv_btype=t&inputT=56033420&rsv_sug3=190&rsv_sug1=73&rsv_sug7=100&rsv_sug4=56033654";
        System.out.println("url:" + url);
        // 标准的 Base64 编码、解码
        byte[] encoded = Base64.getEncoder().encode(url.getBytes(StandardCharsets.UTF_8));
        byte[] decoded = Base64.getDecoder().decode(encoded);
        System.out.println("Base64 encoded: " + new String(encoded));
        System.out.println("Base64 decoded: " + new String(decoded));
        // URL 安全的 Base64 编码、解码
        byte[] encoded2 = Base64.getUrlEncoder().encode(url.getBytes(StandardCharsets.UTF_8));
        byte[] decoded2 = Base64.getUrlDecoder().decode(encoded2);
        System.out.println("Url Safe Base64 encoded: " + new String(encoded2));
        System.out.println("Url Safe Base64 decoded: " + new String(decoded2));
    }

}
