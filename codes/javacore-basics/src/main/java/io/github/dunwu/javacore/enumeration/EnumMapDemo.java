package io.github.dunwu.javacore.enumeration;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-19
 */
public class EnumMapDemo {

    public static void main(String[] args) {
        System.out.println("EnumMap展示");
        EnumMap<Signal, String> errMap = new EnumMap(Signal.class);
        errMap.put(Signal.RED, "红灯");
        errMap.put(Signal.YELLOW, "黄灯");
        errMap.put(Signal.GREEN, "绿灯");
        for (Iterator<Map.Entry<Signal, String>> iter = errMap.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry<Signal, String> entry = iter.next();
            System.out.println(entry.getKey().name() + " : " + entry.getValue());
        }
    }

    public enum Signal {

        GREEN,
        YELLOW,
        RED
    }

}
