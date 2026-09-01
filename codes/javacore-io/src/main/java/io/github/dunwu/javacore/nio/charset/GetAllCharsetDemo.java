package io.github.dunwu.javacore.nio.charset;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

/**
 * 示例：通过 {@link Charset#availableCharsets} 列出 JVM 支持的全部字符集。
 */
public class GetAllCharsetDemo {

    /**
     * 遍历输出全部可用字符集的名称与实例。
     */
    public static void demo() {
        SortedMap<String, Charset> all = Charset.availableCharsets(); // 得到全部可用的字符集
        Iterator<Map.Entry<String, Charset>> iter = all.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Charset> me = iter.next();
            System.out.println(me.getKey() + " --> " + me.getValue());
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
