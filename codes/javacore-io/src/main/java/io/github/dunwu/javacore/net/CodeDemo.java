package io.github.dunwu.javacore.net;

import java.net.URLDecoder;
import java.net.URLEncoder;

public class CodeDemo {

    public static void main(String[] args) throws Exception {
        String keyWord = "乘风破浪会有时";
        String encod = URLEncoder.encode(keyWord, "UTF-8"); // 进行编码的操作
        System.out.println("编码之后的内容：" + encod);
        String decod = URLDecoder.decode(encod, "UTF-8"); // 进行解码操作
        System.out.println("解码之后的内容：" + decod);
    }

}
