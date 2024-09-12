package io.github.dunwu.javacore.concurrent.jmm;

import io.github.dunwu.javacore.concurrent.annotation.ThreadSafe;

import java.util.HashMap;
import java.util.Map;

/**
 * 不可变对象的初始化安全
 * <p/>
 * Initialization safety for immutable objects
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class SafeStates {

    private final Map<String, String> states;

    public SafeStates() {
        states = new HashMap<>();
        states.put("alaska", "AK");
        states.put("alabama", "AL");
        // ...
        states.put("wyoming", "WY");
    }

    public String getAbbreviation(String s) {
        return states.get(s);
    }

}
