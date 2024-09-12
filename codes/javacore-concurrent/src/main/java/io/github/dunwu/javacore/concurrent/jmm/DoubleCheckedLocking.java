package io.github.dunwu.javacore.concurrent.jmm;

import io.github.dunwu.javacore.concurrent.annotation.NotThreadSafe;

/**
 * 双重检查锁
 * <p/>
 * Double-checked-locking antipattern
 *
 * @author Brian Goetz and Tim Peierls
 */
@NotThreadSafe
public class DoubleCheckedLocking {

    private static Resource resource;

    public static Resource getInstance() {
        if (resource == null) {
            synchronized (DoubleCheckedLocking.class) {
                if (resource == null) { resource = new Resource(); }
            }
        }
        return resource;
    }

    static class Resource { }

}
