package io.github.dunwu.javacore.nio.charset;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

/**
 * 取得Charset类的全部编码
 */
public class GetAllCharsetDemo {

    public static void main(String[] args) {
        SortedMap<String, Charset> all = Charset.availableCharsets(); // 得到全部可用的字符集
        Iterator<Map.Entry<String, Charset>> iter = all.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Charset> me = iter.next();
            System.out.println(me.getKey() + " --> " + me.getValue());
        }
    }

}
