// Concurrent canonicalizing map atop ConcurrentMap - Pages 273-274
package io.github.dunwu.javacore.effective.chapter10.item69;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Intern {

    private static final ConcurrentMap<String, String> map = new ConcurrentHashMap<String, String>();

    // Concurrent canonicalizing map atop ConcurrentMap - not optimal - Page 273
    // public static String intern(String s) {
    // String previousValue = map.putIfAbsent(s, s);
    // return previousValue == null ? s : previousValue;
    // }

    // Concurrent canonicalizing map atop ConcurrentMap - faster! - Page 274
    public static String intern(String s) {
        String result = map.get(s);
        if (result == null) {
            result = map.putIfAbsent(s, s);
            if (result == null) { result = s; }
        }
        return result;
    }

}
