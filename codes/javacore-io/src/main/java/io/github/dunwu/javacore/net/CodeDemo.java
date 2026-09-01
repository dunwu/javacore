package io.github.dunwu.javacore.net;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 示例：{@link URLEncoder} / {@link URLDecoder} 的 URL 编解码用法。
 * <p>
 * 中文等非 ASCII 字符在 URL 中必须先编码（转为 %XX 形式），接收方再解码还原。
 */
public class CodeDemo {

    /**
     * 演示对中文关键词先进行 UTF-8 URL 编码，再解码还原。
     */
    public static void demo() throws Exception {
        String keyWord = "乘风破浪会有时";
        String encod = URLEncoder.encode(keyWord, "UTF-8"); // 进行编码的操作
        System.out.println("编码之后的内容：" + encod);
        String decod = URLDecoder.decode(encod, "UTF-8"); // 进行解码操作
        System.out.println("解码之后的内容：" + decod);
    }

    public static void main(String[] args) throws Exception {
        demo();
    }

}
